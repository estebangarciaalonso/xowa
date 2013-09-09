/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012 gnosygnu@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package gplx.fsdb; import gplx.*;
import gplx.dbs.*; import gplx.cache.*;
public class Fsdb_vlm_db_data implements CompareAble {
	private Gfo_cache_mgr dir_cache = new Gfo_cache_mgr();
	public int Id() {return id;} private int id;
	public String Key() {return key;} private String key;
	public Io_url Url() {return url;} private Io_url url;
	public byte[] Rng_bgn() {return rng_bgn;} public Fsdb_vlm_db_data Rng_bgn_(byte[] v) {rng_bgn = v; return this;} private byte[] rng_bgn;
	public Db_provider Provider() {return provider;} private Db_provider provider;
	public Fsdb_vlm_db_data Init(Io_url url, int id, String key, byte[] rng_bgn) {
		this.id = id; this.key = key; this.url = url; this.rng_bgn = rng_bgn;
		return this;
	}
	public void Init_by_provider(Db_provider p) {
		provider = p;
		Fsdb_dir_tbl.Create_table(p);
		Fsdb_fil_tbl.Create_table(p);
		Fsdb_bin_tbl.Create_table(p);
		Fsdb_cfg_tbl.Create_table(p);
		Fsdb_xtn_thm_tbl.Create_table(p);
		Fsdb_xtn_img_tbl.Create_table(p);
	}
	public void Fil_delete(Db_provider p, int fil_id) {
		Fsdb_xtn_thm_itm[] itms = Fsdb_xtn_thm_tbl.Select_itms_by_owner(p, fil_id);
		int len = itms.length;
		Db_stmt stmt = Fsdb_xtn_thm_tbl.Delete_by_id_stmt(p);
		for (int i = 0; i < len; i++)
			Fsdb_xtn_thm_tbl.Delete_by_id(stmt, itms[i].Id());
		Fsdb_fil_tbl.Delete_by_id(p, fil_id);
	}
	public void Thm_delete(Db_provider p, int thm_id) {
		Fsdb_xtn_thm_tbl.Delete_by_id(p, thm_id);
		Fsdb_bin_tbl.Delete(p, thm_id);
	}
	public void Thm_insert(Fsdb_xtn_thm_itm rv, String dir, String fil, int ext_id, int width, int thumbtime, int height, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		int dir_id = Dir_id__get_by_mem_or_db(dir);
		int fil_id = Fsdb_fil_tbl.Select_itm_by_name(provider, dir_id, fil).Id();
		if (fil_id == 0) {
			fil_id = Fsdb_cfg_tbl.Update_next_id(provider);
			Fsdb_fil_tbl.Insert(provider, fil_id, dir_id, fil, Fsdb_xtn_tid_.Tid_thm, ext_id, bin_len, modified, hash);
		}
		int thm_id = Fsdb_cfg_tbl.Update_next_id(provider);
		Fsdb_xtn_thm_tbl.Insert(provider, thm_id, fil_id, width, thumbtime, height, bin_len, modified, hash);
		rv.Id_(thm_id).Owner_(fil_id).Dir_id_(dir_id);
		Fsdb_bin_tbl.Insert_rdr(provider, thm_id, Fsdb_bin_tbl.Owner_tid_thm, bin_len, bin_rdr);
	}
	public int Thm_insert(Db_stmt img_stmt, Db_stmt bin_stmt, int dir_id, int fil_id, int ext_id, int width, int thumbtime, int height, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		int thm_id = Fsdb_cfg_tbl.Update_next_id(provider);
		Fsdb_xtn_thm_tbl.Insert(img_stmt, thm_id, fil_id, width, thumbtime, height, bin_len, modified, hash);
		Fsdb_bin_tbl.Insert_rdr(bin_stmt, thm_id, Fsdb_bin_tbl.Owner_tid_thm, bin_len, bin_rdr);
		return thm_id;
	}
	public void Img_insert(Fsdb_xtn_thm_itm rv, String dir, String fil, int ext_id, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr, int img_w, int img_h, int img_bits) {
		int dir_id = Dir_id__get_by_mem_or_db(dir);
		int fil_id = Fsdb_fil_tbl.Select_itm_by_name(provider, dir_id, fil).Id();
		if (fil_id == 0) {
			fil_id = Fsdb_cfg_tbl.Update_next_id(provider);
			Fsdb_fil_tbl.Insert(provider, fil_id, dir_id, fil, Fsdb_xtn_tid_.Tid_img, ext_id, bin_len, modified, hash);
		}
		Fsdb_xtn_img_tbl.Insert(provider, fil_id, img_w, img_h, img_bits);
		if (bin_len > 0) // bin-less entries can be created; EX: create stub for png of 4800,4000;
			Fsdb_bin_tbl.Insert_rdr(provider, fil_id, Fsdb_bin_tbl.Owner_tid_thm, bin_len, bin_rdr);
	}
	public int compareTo(Object obj) {
		Fsdb_vlm_db_data comp = (Fsdb_vlm_db_data)obj;
		return ByteAry_.Compare(rng_bgn, comp.rng_bgn);
	}
	public static Fsdb_vlm_db_data make_root_by_dir(Io_url dir) {return make_root_(url_root_(dir));}
	public static Fsdb_vlm_db_data make_root_(Io_url url) {
		Fsdb_vlm_db_data rv = new Fsdb_vlm_db_data().Init(url, 0, "", Path_bgn_0);
		Db_provider p = Db_provider_.new_(gplx.dbs.Db_connect_sqlite.make_(url));
		rv.Init_by_provider(p);
		return rv;
	}
	public static Io_url url_root_(Io_url dir) {return dir.GenSubFil(Name_data_root);}
	public void Rls() {provider.Rls();}
	private int Dir_id__get_by_mem_or_db(String dir) {
		int rv = -1;
		Object rv_obj = dir_cache.Get_val_or_null(dir);
		if (rv_obj == null) {
			rv = Fsdb_cfg_tbl.Update_next_id(provider);
			Fsdb_dir_tbl.Insert(provider, rv, dir, 0);	// 0: always assume root owner
			dir_cache.Add(dir, IntRef.new_(rv));
		}
		else
			rv = ((IntRef)rv_obj).Val();
		return rv;
	}
	private static final String Name_data_root = "data.sqlite3";
	private static final byte[] Path_bgn_0 = ByteAry_.Empty;
}
//class Xof_fsdb_mok {
//	private Hash_adp_bry hash = new Hash_adp_bry(true);
//	private ByteAryBfr tmp_bfr = ByteAryBfr.new_();
//	public void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int w, int thumbtime, boolean is_orig, int h, int bin_len, DateAdp modified, String hash, gplx.ios.Io_stream_rdr rdr) {
////		tmp_bfr.Add(dir).Add_byte_pipe().Add(fil).Add_byte_pipe();
//	}
//	public void Thm_select() {
//	}
//}

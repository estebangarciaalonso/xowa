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
public class Fsdb_db_atr_fil implements RlsAble {
	private Gfo_cache_mgr_obj dir_cache = new Gfo_cache_mgr_obj();
	public int Id() {return id;} private int id;
	public Io_url Url() {return url;} private Io_url url;
	public String Path_bgn() {return path_bgn;} private String path_bgn;
	public byte Cmd_mode() {return cmd_mode;} public Fsdb_db_atr_fil Cmd_mode_(byte v) {cmd_mode = v; return this;} private byte cmd_mode;
	public Db_provider Provider() {
		if (provider == null) {
			if (cmd_mode == Db_cmd_mode.Create) {
				provider = Db_provider_.new_(Db_connect_sqlite.make_(url));
				Fsdb_cfg_tbl.Create_table(provider);
				Fsdb_dir_tbl.Create_table(provider);
				Fsdb_fil_tbl.Create_table(provider);
				Fsdb_xtn_img_tbl.Create_table(provider);
				Fsdb_xtn_thm_tbl.Create_table(provider);
			}
			else
				provider = Db_provider_.new_(Db_connect_sqlite.load_(url));
		}
		return provider;
	} 	private Db_provider provider;
	public void Rls() {if (provider != null) provider.Rls();}
	public Fsdb_fil_itm Fil_select(byte[] dir, byte[] fil) {
		Db_provider p = this.Provider();
		IntVal dir_id_obj = (IntVal)dir_cache.Get_or_null(dir);
		if (dir_id_obj == null) {
			Fsdb_dir_itm dir_itm = Fsdb_dir_tbl.Select_itm(p, String_.new_utf8_(dir));
			dir_id_obj = dir_itm == Fsdb_dir_itm.Null ? IntVal.neg1_() : IntVal.new_(dir_itm.Id());
			dir_cache.Add(dir, dir_id_obj);
		}
		int dir_id = dir_id_obj.Val(); if (dir_id == Int_.Neg1) return Fsdb_fil_itm.Null;
		return Fsdb_fil_tbl.Select_itm_by_name(p, dir_id, String_.new_utf8_(fil));
	}
	public Fsdb_xtn_thm_itm Thm_select(int fil_id, int width, int thumbtime) {
		return Fsdb_xtn_thm_tbl.Select_itm_by_fil_width(this.Provider(), fil_id, width, thumbtime);
	}
	public int Img_insert(Fsdb_xtn_img_itm rv, String dir, String fil, int ext_id, int img_w, int img_h, DateAdp modified, String hash, int bin_db_id, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		int dir_id = Dir_id__get_by_mem_or_db(dir);
		int fil_id = Fsdb_fil_tbl.Select_itm_by_name(provider, dir_id, fil).Id();
		if (fil_id == 0) {
			fil_id = Fsdb_cfg_tbl.Update_next_id(provider);
			Fsdb_fil_tbl.Insert(provider, fil_id, dir_id, fil, Fsdb_xtn_tid_.Tid_img, ext_id, bin_len, modified, hash, bin_db_id);
		}
		Fsdb_xtn_img_tbl.Insert(provider, fil_id, img_w, img_h);
		return fil_id;
	}
	public int Thm_insert(Fsdb_xtn_thm_itm rv, String dir, String fil, int ext_id, int thm_w, int thm_h, int thumbtime, DateAdp modified, String hash, int bin_db_id, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		int dir_id = Dir_id__get_by_mem_or_db(dir);
		int fil_id = Fsdb_fil_tbl.Select_itm_by_name(provider, dir_id, fil).Id();
		if (fil_id == 0) {
			fil_id = Fsdb_cfg_tbl.Update_next_id(provider);
			Fsdb_fil_tbl.Insert(provider, fil_id, dir_id, fil, Fsdb_xtn_tid_.Tid_thm, ext_id, bin_len, modified, hash, bin_db_id);
		}
		int thm_id = Fsdb_cfg_tbl.Update_next_id(provider);
		Fsdb_xtn_thm_tbl.Insert(provider, thm_id, fil_id, thm_w, thm_h, thumbtime, bin_db_id, bin_len, modified, hash);
		rv.Id_(thm_id).Owner_(fil_id).Dir_id_(dir_id);
		return thm_id;
	}
	public static Fsdb_db_atr_fil load_(DataRdr rdr, Io_url dir) {
		Fsdb_db_atr_fil rv = new Fsdb_db_atr_fil();
		rv.id = rdr.ReadInt(Fsdb_db_atr_tbl.Fld_fda_id);
		rv.url = dir.GenSubFil(rdr.ReadStr(Fsdb_db_atr_tbl.Fld_fda_url));
		rv.path_bgn = rdr.ReadStr(Fsdb_db_atr_tbl.Fld_fda_path_bgn);
		rv.cmd_mode = Db_cmd_mode.Ignore;
		return rv;
	}
	public static Fsdb_db_atr_fil make_(int id, Io_url url, String path_bgn) {
		Fsdb_db_atr_fil rv = new Fsdb_db_atr_fil();
		rv.id = id;
		rv.url = url;
		rv.path_bgn = path_bgn;
		rv.cmd_mode = Db_cmd_mode.Create;
		rv.Provider(); // force table create
		return rv;
	}
	public static Io_url url_(Io_url dir, String wiki_domain, int id) {
		return dir.GenSubFil_ary(wiki_domain, "#main#file.atr#", Int_.XtoStr_PadBgn(id, 4), ".sqlite3");
	}
	private int Dir_id__get_by_mem_or_db(String dir) {
		int rv = -1;
		Db_provider p = this.Provider();
		Object rv_obj = dir_cache.Get_or_null(dir);
		if (rv_obj == null) {
			Fsdb_dir_itm itm = Fsdb_dir_tbl.Select_itm(p, dir);
			if (itm == Fsdb_dir_itm.Null) {
				rv = Fsdb_cfg_tbl.Update_next_id(p);
				Fsdb_dir_tbl.Insert(p, rv, dir, 0);	// 0: always assume root owner
			}
			else {
				rv = itm.Id();
			}
			dir_cache.Add(dir, IntRef.new_(rv));
		}
		else
			rv = ((IntRef)rv_obj).Val();
		return rv;
	}
}

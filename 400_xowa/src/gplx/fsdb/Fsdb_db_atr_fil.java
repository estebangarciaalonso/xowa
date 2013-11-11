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
	private Db_provider provider;
	private Fsdb_dir_tbl tbl_dir; private Fsdb_fil_tbl tbl_fil; private Fsdb_xtn_thm_tbl tbl_thm; private Fsdb_xtn_img_tbl tbl_img;
	public Fsdb_db_atr_fil(Fsdb_db_abc_mgr abc_mgr, Io_url url, boolean create) {
		this.abc_mgr = abc_mgr;
		Db_connect connect = create ? Db_connect_sqlite.make_(url) : Db_connect_sqlite.load_(url);
		provider = Db_provider_.new_(connect);
		tbl_dir = new Fsdb_dir_tbl(provider, create);
		tbl_fil = new Fsdb_fil_tbl(provider, create);
		tbl_thm = new Fsdb_xtn_thm_tbl(provider, create);
		tbl_img = new Fsdb_xtn_img_tbl(provider, create);
	}	private Fsdb_db_abc_mgr abc_mgr;
	public int Id() {return id;} private int id;
	public Io_url Url() {return url;} private Io_url url;
	public String Path_bgn() {return path_bgn;} private String path_bgn;
	public byte Cmd_mode() {return cmd_mode;} public Fsdb_db_atr_fil Cmd_mode_(byte v) {cmd_mode = v; return this;} private byte cmd_mode;
	public void Rls() {
		tbl_dir.Rls();
		tbl_fil.Rls();
		tbl_img.Rls();
		tbl_thm.Rls();
		provider.Txn_mgr().Txn_end_all();
		provider.Rls();
	}
	public void Commit() {
		provider.Txn_mgr().Txn_end_all_bgn_if_none();
	}
	public Fsdb_fil_itm Fil_select(byte[] dir, byte[] fil) {
		IntVal dir_id_obj = (IntVal)dir_cache.Get_or_null(dir);
		if (dir_id_obj == null) {
			Fsdb_dir_itm dir_itm = tbl_dir.Select_itm(String_.new_utf8_(dir));
			dir_id_obj = dir_itm == Fsdb_dir_itm.Null ? IntVal.neg1_() : IntVal.new_(dir_itm.Id());
			dir_cache.Add(dir, dir_id_obj);
		}
		int dir_id = dir_id_obj.Val(); if (dir_id == Int_.Neg1) return Fsdb_fil_itm.Null;
		return tbl_fil.Select_itm_by_name(dir_id, String_.new_utf8_(fil));
	}
	public Fsdb_xtn_thm_itm Thm_select(int fil_id, int width, int thumbtime) {
		return tbl_thm.Select_itm_by_fil_width(fil_id, width, thumbtime);
	}
	public int Fil_insert(Fsdb_fil_itm rv    , String dir, String fil, int ext_id, DateAdp modified, String hash, int bin_db_id, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		int dir_id = Dir_id__get_by_mem_or_db(dir);
		int fil_id = tbl_fil.Select_itm_by_name(dir_id, fil).Id();
		if (fil_id == 0) {
			fil_id = abc_mgr.Next_id();				
			tbl_fil.Insert(fil_id, dir_id, fil, Fsdb_xtn_tid_.Tid_none, ext_id, bin_len, modified, hash, bin_db_id);
		}
		return fil_id;
	}
	public int Img_insert(Fsdb_xtn_img_itm rv, String dir, String fil, int ext_id, int img_w, int img_h, DateAdp modified, String hash, int bin_db_id, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		int dir_id = Dir_id__get_by_mem_or_db(dir);
		int fil_id = tbl_fil.Select_itm_by_name(dir_id, fil).Id();
		if (fil_id == 0) {
			fil_id = abc_mgr.Next_id();
			tbl_fil.Insert(fil_id, dir_id, fil, Fsdb_xtn_tid_.Tid_img, ext_id, bin_len, modified, hash, bin_db_id);
		}
		tbl_img.Insert(fil_id, img_w, img_h);
		return fil_id;
	}
	public int Thm_insert(Fsdb_xtn_thm_itm rv, String dir, String fil, int ext_id, int thm_w, int thm_h, int thumbtime, DateAdp modified, String hash, int bin_db_id, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		int dir_id = Dir_id__get_by_mem_or_db(dir);
		int fil_id = tbl_fil.Select_itm_by_name(dir_id, fil).Id();
		if (fil_id == 0) {
			fil_id = abc_mgr.Next_id();
			tbl_fil.Insert(fil_id, dir_id, fil, Fsdb_xtn_tid_.Tid_thm, ext_id, bin_len, modified, hash, Fsdb_bin_tbl.Db_bin_id_null);
		}
		int thm_id = abc_mgr.Next_id();
		tbl_thm.Insert(thm_id, fil_id, thm_w, thm_h, thumbtime, bin_db_id, bin_len, modified, hash);
		rv.Id_(thm_id).Owner_(fil_id).Dir_id_(dir_id);
		return thm_id;
	}
	public static Fsdb_db_atr_fil load_(Fsdb_db_abc_mgr abc_mgr, DataRdr rdr, Io_url dir) {
		Io_url url = dir.GenSubFil(rdr.ReadStr(Fsdb_db_atr_tbl.Fld_fda_url));
		Fsdb_db_atr_fil rv = new Fsdb_db_atr_fil(abc_mgr, url, false);
		rv.id = rdr.ReadInt(Fsdb_db_atr_tbl.Fld_fda_id);
		rv.url = url;
		rv.path_bgn = rdr.ReadStr(Fsdb_db_atr_tbl.Fld_fda_path_bgn);
		rv.cmd_mode = Db_cmd_mode.Ignore;
		return rv;
	}
	public static Fsdb_db_atr_fil make_(Fsdb_db_abc_mgr abc_mgr, int id, Io_url url, String path_bgn) {
		Fsdb_db_atr_fil rv = new Fsdb_db_atr_fil(abc_mgr, url, true);
		rv.id = id;
		rv.url = url;
		rv.path_bgn = path_bgn;
		rv.cmd_mode = Db_cmd_mode.Create;
		return rv;
	}
	public static Io_url url_(Io_url dir, int id) {
		return dir.GenSubFil_ary("fsdb.atr#", Int_.XtoStr_PadBgn(id, 2), ".sqlite3");
	}
	private int Dir_id__get_by_mem_or_db(String dir) {
		int rv = -1;
		Object rv_obj = dir_cache.Get_or_null(dir);
		if (rv_obj == null) {
			Fsdb_dir_itm itm = tbl_dir.Select_itm(dir);
			if (itm == Fsdb_dir_itm.Null) {
				rv = abc_mgr.Next_id();
				tbl_dir.Insert(rv, dir, 0);	// 0: always assume root owner
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

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
import gplx.dbs.*;
public class Fsdb_mnt_mgr {
	private Db_provider provider;
	private Fsdb_cfg_tbl tbl_cfg;
	private Fsdb_db_abc_mgr[] ary; int ary_len = 0;
	public void Init(Io_url cur_dir) {
		Fsdb_mnt_itm[] mnts = Db_load_or_make(cur_dir);
		ary_len = mnts.length;
		ary = new Fsdb_db_abc_mgr[ary_len];
		for (int i = 0; i < ary_len; i++) {
			Fsdb_mnt_itm itm = mnts[i];
			Io_url abc_url = cur_dir.GenSubFil_nest(itm.Url(), "fsdb.abc.sqlite3");
			ary[i] = new Fsdb_db_abc_mgr(this).Init(abc_url.OwnerDir());
		}
		insert_db = tbl_cfg.Select_as_int("core", "mnt.insert_idx");
	}
	public int Insert_db() {return insert_db;} public Fsdb_mnt_mgr Insert_db_(int v) {insert_db = v; return this;} private int insert_db = Mnt_idx_user;
	public Fsdb_db_abc_mgr Abc_mgr_at(int i) {return ary[i];}
	private Fsdb_mnt_itm[] Db_load_or_make(Io_url cur_dir) {
		BoolRef created = BoolRef.false_();
		provider = Sqlite_engine_.Provider_load_or_make_(cur_dir.GenSubFil("file.core.sqlite3"), created);
		tbl_cfg = new Fsdb_cfg_tbl(provider, created.Val());
		if (created.Val()) {
			Fsdb_mnt_tbl.Create_table(provider);
			Fsdb_mnt_tbl.Insert(provider, Mnt_idx_main, "fsdb.main", "fsdb.main");
			Fsdb_mnt_tbl.Insert(provider, Mnt_idx_update, "fsdb.update_00", "fsdb.update_00");
			Fsdb_mnt_tbl.Insert(provider, Mnt_idx_user, "fsdb.user", "fsdb.user");
			tbl_cfg.Insert("core", "mnt.insert_idx", Int_.XtoStr(Mnt_idx_user));
		}
		return Fsdb_mnt_tbl.Select_all(provider);
	}
	public Fsdb_db_bin_fil Bin_db_get(int mnt_id, int bin_db_id) {
		return ary[mnt_id].Bin_mgr().Get_at(bin_db_id);
	}
	public Fsdb_fil_itm Fil_select_bin(byte[] dir, byte[] fil, boolean is_thumb, int width, int thumbtime) {
		for (int i = 0; i < ary_len; i++) {
			Fsdb_fil_itm rv = ary[i].Fil_select_bin(dir, fil, is_thumb, width, thumbtime);
			if (rv != Fsdb_fil_itm.Null && rv.Db_bin_id() != Fsdb_bin_tbl.Db_bin_id_null) {	// NOTE: mnt_0 can have thumb, but mnt_1 can have itm; check for itm with Db_bin_id; DATE:2013-11-16
				rv.Mnt_id_(i);
				return rv;
			}
		}
		return Fsdb_fil_itm.Null;
	}
	public Fsdb_xtn_thm_itm Thm_select_bin(byte[] dir, byte[] fil, int width, int thumbtime) {
		for (int i = 0; i < ary_len; i++) {
			Fsdb_xtn_thm_itm rv = ary[i].Thm_select_bin(dir, fil, width, thumbtime);
			if (rv != Fsdb_xtn_thm_itm.Null) {
				rv.Mnt_id_(i);
				return rv;
			}
		}
		return Fsdb_xtn_thm_itm.Null;
	}
	public void Fil_insert(Fsdb_fil_itm rv    , byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		ary[insert_db].Fil_insert(rv, dir, fil, ext_id, modified, hash, bin_len, bin_rdr);
	}
	public void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int w, int h, int thumbtime, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		ary[insert_db].Thm_insert(rv, dir, fil, ext_id, w, h, thumbtime, modified, hash, bin_len, bin_rdr);
	}
	public void Img_insert(Fsdb_xtn_img_itm rv, byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr, int img_w, int img_h) {
		ary[insert_db].Img_insert(rv, dir, fil, ext_id, modified, hash, bin_len, bin_rdr, img_w, img_h);
	}
	public void Bin_db_max_(long v) {
		for (int i = 0; i < ary_len; i++)
			ary[i].Bin_mgr().Db_bin_max_(v);
	}
	public void Txn_open() {
		for (int i = 0; i < ary_len; i++)
			ary[i].Txn_open();
	}
	public void Txn_save() {
		for (int i = 0; i < ary_len; i++)
			ary[i].Txn_save();
	}
	public void Rls() {
		for (int i = 0; i < ary_len; i++)
			ary[i].Rls();
		tbl_cfg.Rls();
	}
	public static final int Mnt_idx_main = 0, Mnt_idx_update = 1, Mnt_idx_user = 2;
}

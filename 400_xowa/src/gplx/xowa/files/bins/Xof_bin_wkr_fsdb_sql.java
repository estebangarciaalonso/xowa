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
package gplx.xowa.files.bins; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.dbs.*; import gplx.cache.*; import gplx.fsdb.*; import gplx.xowa.files.fsdb.*;
public class Xof_bin_wkr_fsdb_sql implements Xof_bin_wkr, GfoInvkAble {
	private byte[] bin_bfr; private int bin_flush_when = Io_mgr.Len_mb;
	private IntRef tmp_itm_id = IntRef.neg1_(), tmp_bin_db_id = IntRef.neg1_(), tmp_mnt_id = IntRef.neg1_();
	public Xof_bin_wkr_fsdb_sql(Xof_fsdb_mgr_sql fsdb_mgr) {this.fsdb_mgr = fsdb_mgr;}
	public Xof_fsdb_mgr_sql Fsdb_mgr() {return fsdb_mgr;} private Xof_fsdb_mgr_sql fsdb_mgr;
	public byte Bin_wkr_tid() {return Xof_bin_wkr_.Tid_fsdb_wiki;}
	public boolean Bin_wkr_resize() {return bin_wkr_resize;} public void Bin_wkr_resize_(boolean v) {bin_wkr_resize = v;} private boolean bin_wkr_resize = false;
	public int Bin_bfr_len() {return bin_bfr_len;} public Xof_bin_wkr_fsdb_sql Bin_bfr_len_(int v) {bin_bfr_len = v; return this;} private int bin_bfr_len = 32;
	public gplx.ios.Io_stream_rdr Bin_wkr_get_as_rdr(ListAdp temp_files, Xof_fsdb_itm itm, boolean is_thumb, int w) {
		Bin_wkr_get_ids(itm, is_thumb, w, tmp_itm_id, tmp_bin_db_id, tmp_mnt_id);
		int bin_db_id = tmp_bin_db_id.Val(); if (bin_db_id == -1) return gplx.ios.Io_stream_rdr_.Null;
		Fsdb_db_bin_fil bin_db = fsdb_mgr.Bin_db_get(tmp_mnt_id.Val(), bin_db_id);
		return bin_db.Get_as_rdr(tmp_itm_id.Val());
	}
	public boolean Bin_wkr_get_to_url(ListAdp temp_files, Xof_fsdb_itm itm, boolean is_thumb, int w, Io_url bin_url) {
		if (bin_bfr == null) bin_bfr = new byte[bin_bfr_len];
		Bin_wkr_get_ids(itm, is_thumb, w, tmp_itm_id, tmp_bin_db_id, tmp_mnt_id);
		int bin_db_id = tmp_bin_db_id.Val(); if (bin_db_id == -1) return false;
		Fsdb_db_bin_fil bin_db = fsdb_mgr.Bin_db_get(tmp_mnt_id.Val(), bin_db_id);
		return bin_db.Get_to_url(tmp_itm_id.Val(), bin_url, bin_bfr, bin_flush_when);
	}
	private void Bin_wkr_get_ids(Xof_fsdb_itm itm, boolean is_thumb, int w, IntRef tmp_itm_id, IntRef tmp_bin_db_id, IntRef tmp_mnt_id) {
		byte[] dir = itm.Orig_wiki(), fil = itm.Lnki_ttl();
		int thumbtime = itm.Lnki_thumbtime();
		if (is_thumb) {
			Fsdb_xtn_thm_itm thm_itm = fsdb_mgr.Thm_select_bin(dir, fil, w, thumbtime);
			tmp_itm_id.Val_(thm_itm.Id());
			tmp_bin_db_id.Val_(thm_itm.Db_bin_id());
			tmp_mnt_id.Val_(thm_itm.Mnt_id());
		}
		else {
			Fsdb_fil_itm fil_itm = fsdb_mgr.Fil_select_bin(dir, fil, is_thumb, w, thumbtime);
			tmp_itm_id.Val_(fil_itm.Id());
			tmp_bin_db_id.Val_(fil_itm.Db_bin_id());
			tmp_mnt_id.Val_(fil_itm.Mnt_id());
		}
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_db_dir_))		fsdb_mgr.Db_dir_(m.ReadIoUrl("v"));
		else if	(ctx.Match(k, Invk_url_))			fsdb_mgr.Db_dir_(m.ReadIoUrl("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_db_dir_ = "db_dir_", Invk_url_ = "url_";
}

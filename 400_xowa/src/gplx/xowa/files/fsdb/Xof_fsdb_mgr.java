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
package gplx.xowa.files.fsdb; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.dbs.*; import gplx.fsdb.*; import gplx.xowa.files.bins.*; import gplx.xowa.files.qrys.*;
public class Xof_fsdb_mgr implements RlsAble {
	private Fsdb_mnt_itm[] mnt_itms; private int mnt_itms_len;
	private Db_provider mnt_regy_provider = null; private Db_provider img_regy_provider = null;
	private Io_url file_dir;
	public Xof_qry_mgr Qry_mgr() {return qry_mgr;} private Xof_qry_mgr qry_mgr = new Xof_qry_mgr();
	public Xof_bin_mgr Bin_mgr() {return bin_mgr;} private Xof_bin_mgr bin_mgr;
	public void Init_by_wiki(Xow_wiki wiki) {Init_by_wiki(wiki.App().Fsys_mgr().File_dir(), wiki.App().Fsys_mgr().File_dir().GenSubDir(wiki.Domain_str()), wiki.Rls_list(), wiki.App().File_mgr().Repos());}
	public void Init_by_wiki(Io_url file_dir, Io_url root_dir, ListAdp rls_list, Xof_repo_mgr repo_mgr) {// TEST
		this.file_dir = file_dir;
		Init_img_regy_provider(root_dir);
		Init_mnt_regy_provider(root_dir);
		rls_list.Add(this);
		bin_mgr = new Xof_bin_mgr(repo_mgr);
	}
	public Fsdb_vlm_db_data Vlm_find(byte[] ttl) {
		Fsdb_vlm_db_data rv = null;
		for (int i = 0; i < mnt_itms_len; i++) {
			Fsdb_mnt_itm mnt = mnt_itms[i];
			rv = mnt.Vlm_find(ttl);
			if (rv != null) return rv;
		}
		return null;
	}
	public void Reg_select(Xog_win_wtr win_wtr, ListAdp lnki_list) {Reg_select(win_wtr, Reg_select__to_hash(lnki_list));}
	public void Reg_select(Xog_win_wtr win_wtr, OrderedHash itm_hash) {
		int itm_hash_len = itm_hash.Count();
		Xof_tbl_reg.Select(img_regy_provider, itm_hash, itm_hash_len);
		ListAdp fsdb_list = ListAdp_.new_();
		for (int i = 0; i < itm_hash_len; i++) {
			Xof_fsdb_itm itm = (Xof_fsdb_itm)itm_hash.FetchAt(i);
			Itm_process(win_wtr, itm, fsdb_list);
		}
		int itms_len = fsdb_list.Count(); if (itms_len == 0) return;	// all items found; return;
		Reg_search(fsdb_list, itms_len);
	}
	private void Reg_search(ListAdp itms_list, int itms_len) {
		for (int i = 0; i < itms_len; i++) {
			Xof_fsdb_itm itm = (Xof_fsdb_itm)itms_list.FetchAt(i);
			switch (itm.Rslt_reg()) {
				case Xof_reg_wkr_.Tid_missing_qry:
				case Xof_reg_wkr_.Tid_missing_bin:	continue;	// already missing; do not try to find again
			}
			if (qry_mgr.Find(itm)) {
				if (bin_mgr.Find(itm)) {
					String rel_url = itm.Html_url_abs().GenRelUrl_orEmpty(file_dir);
					this.Reg_insert(itm.Lnki_key(), itm.Html_w(), itm.Html_h(), rel_url, Xof_reg_wkr_.Tid_found_url, 0, 1, 0, 0, 0);
					break;
				}
				else {
					itm.Rslt_bin_(Xof_bin_wkr_.Tid_not_found);
					this.Reg_insert(itm.Lnki_key(), itm.Html_w(), itm.Html_h(), "", Xof_reg_wkr_.Tid_missing_bin, 0, 1, 0, 0, 0);
				}
			}
			else {
				this.Reg_insert(itm.Lnki_key(), itm.Html_w(), itm.Html_h(), "", Xof_reg_wkr_.Tid_missing_qry, 0, 1, 0, 0, 0);
			}
		}
	}
	private void Itm_process(Xog_win_wtr win_wtr, Xof_fsdb_itm itm, ListAdp fsdb_list) {
		switch (itm.Rslt_reg()) {
			case Xof_reg_wkr_.Tid_found_url:		win_wtr.Html_img_update("xowa_file_img_" + "", "", itm.Html_w(), itm.Html_h()); break;
			case Xof_reg_wkr_.Tid_missing_qry:
			case Xof_reg_wkr_.Tid_missing_bin:		break;
			case Xof_reg_wkr_.Tid_missing_reg:
			case Xof_reg_wkr_.Tid_null:				fsdb_list.Add(itm); break;
			default:								throw Err_.unhandled(itm.Rslt_reg());
		}
	}
	private OrderedHash Reg_select__to_hash(ListAdp list) {
		OrderedHash rv = OrderedHash_.new_bry_();
		int list_len = list.Count();
		Xof_fsdb_itm_key_bldr key_bldr = new Xof_fsdb_itm_key_bldr();
		Xof_fsdb_itm tmp_itm = new Xof_fsdb_itm();
		for (int i = 0; i < list_len; i++) {
			Xof_xfer_itm xfer_itm = (Xof_xfer_itm)list.FetchAt(i);				
			tmp_itm.Init_by_lnki(key_bldr, xfer_itm.Ttl(), xfer_itm.Lnki_type(), xfer_itm.Lnki_w(), xfer_itm.Lnki_h(), xfer_itm.Lnki_upright(), xfer_itm.Lnki_thumbtime());				
			byte[] reg_key = tmp_itm.Lnki_key();
			Xof_fsdb_itm itm = (Xof_fsdb_itm)rv.Fetch(reg_key);
			if (itm == null) {
//					tmp_itm.Link_html_id_add(xfer_itm.Html_dynamic_id());
				rv.Add(reg_key, tmp_itm);
				tmp_itm = new Xof_fsdb_itm();
			}
			else {
//					itm.Link_html_id_add(xfer_itm.Html_dynamic_id());
			}
		}
		return rv;
	}
	public void Reg_insert(byte[] key, int w, int h, String url, byte status, long size, long viewed, int mnt_id, int tid_id, int img_id) {
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = Xof_tbl_reg.Insert_stmt(img_regy_provider);
			Xof_tbl_reg.Insert(stmt, String_.new_utf8_(key), url, w, h, status, size, viewed, mnt_id, tid_id, img_id);
		} finally {stmt.Rls();}
	}
	public int Thm_insert_idx() {return fsdb_insert_idx;} private int fsdb_insert_idx = 0;
	public void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int w, int h, int bin_len, gplx.ios.Io_stream_rdr rdr) {Thm_insert(rv, dir, fil, Xof_ext_.Id_unknown, w, Xop_lnki_tkn.Thumbtime_null, h, Fsdb_xtn_thm_tbl.Modified_null, Fsdb_xtn_thm_tbl.Hash_null, bin_len, rdr);}
	public void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int w, int thumbtime, int h, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		mnt_itms[fsdb_insert_idx].Thm_insert(rv, dir, fil, ext_id, w, thumbtime, h, modified, hash, bin_len, bin_rdr);
	}
	public void Img_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr, int img_w, int img_h, int img_bits) {
		mnt_itms[fsdb_insert_idx].Img_insert(rv, dir, fil, ext_id, modified, hash, bin_len, bin_rdr, img_w, img_h, img_bits);
	}
	public void Rls() {
		img_regy_provider.Rls();
		mnt_regy_provider.Rls();
		for (int i = 0; i < mnt_itms_len; i++) 
			mnt_itms[i].Rls();
	}
	private void Init_img_regy_provider(Io_url root_dir) {
		BoolRef created = BoolRef.false_();
		img_regy_provider = Sqlite_engine_.Provider_load_or_make_(root_dir.GenSubFil("img_regy.sqlite3"), created);
		if (created.Val())
			Xof_tbl_reg.Create_table(img_regy_provider);
	}
	private void Init_mnt_regy_provider(Io_url root_dir) {
		BoolRef created = BoolRef.false_();
		mnt_regy_provider = Sqlite_engine_.Provider_load_or_make_(root_dir.GenSubFil("mnt_regy.sqlite3"), created);
		if (created.Val()) {
			Fsdb_mnt_tbl.Create_table(mnt_regy_provider);
			Fsdb_mnt_tbl.Insert(mnt_regy_provider, "main", "main.sqlite3");
		}
		mnt_itms = Fsdb_mnt_tbl.Select_all(mnt_regy_provider, root_dir);
		mnt_itms_len = mnt_itms.length;
		for (int i = 0; i < mnt_itms_len; i++)
			mnt_itms[i].Init_vlm_mgr(root_dir.GenSubDir("main"));
	}
}

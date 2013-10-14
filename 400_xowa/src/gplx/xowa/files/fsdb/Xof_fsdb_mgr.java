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
import gplx.fsdb.*; import gplx.xowa.files.bins.*; import gplx.xowa.files.qrys.*; import gplx.xowa.files.main.orig.*;
public interface Xof_fsdb_mgr extends RlsAble {
	boolean Tid_is_mem();
	Xof_qry_mgr Qry_mgr();
	Xof_bin_mgr Bin_mgr();
	Xof_bin_wkr Bin_wkr_fsdb();
	void Db_bin_max_(long v);
	void Init_by_wiki(Xow_wiki wiki, Io_url db_dir, Io_url fs_dir, Xow_repo_mgr repo_mgr);
	void Img_insert(Fsdb_xtn_img_itm rv, byte[] dir, byte[] fil, int ext_id, int img_w, int img_h, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr);
	void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int thm_w, int thm_h, int thumbtime, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr);
	void Reg_insert(Xof_fsdb_itm itm, byte repo_id, byte status);
	void Reg_select(Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms);
}
class Xof_fsdb_mgr_utl {
	public void Fsdb_search(Xof_fsdb_mgr fsdb_mgr, Io_url file_dir, Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms, Xow_repo_mgr repo_mgr, Xof_url_bldr url_bldr) {
		ListAdp fsdb_list = ListAdp_.new_();
		int itms_len = itms.Count();
		for (int i = 0; i < itms_len; i++) {
			Xof_fsdb_itm itm = (Xof_fsdb_itm)itms.FetchAt(i);
			Itm_process(file_dir, win_wtr, itm, fsdb_list, repo_mgr, url_bldr, exec_tid);
		}
		itms_len = fsdb_list.Count(); if (itms_len == 0) return;	// all items found; return;
		Reg_search(fsdb_mgr, file_dir, win_wtr, exec_tid, fsdb_list, itms_len, repo_mgr);
	}
	private void Reg_search(Xof_fsdb_mgr fsdb_mgr, Io_url file_dir, Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms_list, int itms_len, Xow_repo_mgr repo_mgr) {
		for (int i = 0; i < itms_len; i++) {
			Xof_fsdb_itm itm = (Xof_fsdb_itm)itms_list.FetchAt(i);
			switch (itm.Rslt_reg()) {
				case Xof_orig_wkr_.Tid_missing_qry:
				case Xof_orig_wkr_.Tid_missing_bin:	continue;	// already missing; do not try to find again
			}
			if (fsdb_mgr.Qry_mgr().Find(exec_tid, itm)) {
				Xof_repo_pair repo_pair = repo_mgr.Repos_get_by_wiki(itm.Orig_wiki());
				byte orig_wiki = repo_pair.Id();
				if (fsdb_mgr.Bin_mgr().Find_to_url_as_bool(exec_tid, itm)) {
					fsdb_mgr.Reg_insert(itm, orig_wiki, Xof_orig_wkr_.Tid_found_orig);
					if (itm.Rslt_bin() != Xof_bin_wkr_.Tid_fsdb)
						Fsdb_save(fsdb_mgr, itm);
					Gui_update(win_wtr, itm);
				}
				else {
					itm.Rslt_bin_(Xof_bin_wkr_.Tid_not_found);
					fsdb_mgr.Reg_insert(itm, orig_wiki, Xof_orig_wkr_.Tid_missing_bin);
				}
			}
			else {
				fsdb_mgr.Reg_insert(itm, Xof_repo_itm.Repo_unknown, Xof_orig_wkr_.Tid_missing_qry);
			}
		}
	}
	private Xof_img_size img_size = new Xof_img_size();
	private void Itm_process(Io_url file_dir, Xog_win_wtr win_wtr, Xof_fsdb_itm itm, ListAdp fsdb_list, Xow_repo_mgr repo_mgr, Xof_url_bldr url_bldr, byte exec_tid) {
		switch (itm.Rslt_reg()) {
			case Xof_orig_wkr_.Tid_found_orig:
				itm.Html_size_calc(img_size, exec_tid);
				Xof_repo_itm repo = repo_mgr.Repos_get_by_wiki(itm.Orig_wiki()).Trg();
				Io_url trg_url = url_bldr.Set_trg_file_(itm.Lnki_type_as_mode(), repo, itm.Lnki_ttl(), itm.Lnki_md5(), itm.Lnki_ext(), itm.Html_w(), itm.Lnki_thumbtime()).Xto_url();
				itm.Html_url_(trg_url);
				Gui_update(win_wtr, itm);
				break;
			case Xof_orig_wkr_.Tid_missing_qry:
			case Xof_orig_wkr_.Tid_missing_bin:		break;
			case Xof_orig_wkr_.Tid_missing_reg:
			case Xof_orig_wkr_.Tid_null:				fsdb_list.Add(itm); break;
			default:								throw Err_.unhandled(itm.Rslt_reg());
		}
	}
	private void Fsdb_save(Xof_fsdb_mgr fsdb_mgr, Xof_fsdb_itm itm) {
		Io_url html_url = itm.Html_url();
		long bin_len = Io_mgr._.QueryFil(html_url).Size();
		gplx.ios.Io_stream_rdr bin_rdr = gplx.ios.Io_stream_rdr_.file_(html_url);
		try {
			bin_rdr.Open();
			if (itm.File_is_orig()) {
				Fsdb_xtn_img_itm img_itm = new Fsdb_xtn_img_itm();
				fsdb_mgr.Img_insert(img_itm, itm.Orig_wiki(), itm.Lnki_ttl(), itm.Lnki_ext().Id(), itm.Html_w(), itm.Html_h(), Fsdb_xtn_thm_tbl.Modified_null, Fsdb_xtn_thm_tbl.Hash_null, bin_len, bin_rdr);
			}
			else {
				Fsdb_xtn_thm_itm thm_itm = new Fsdb_xtn_thm_itm();
				fsdb_mgr.Thm_insert(thm_itm, itm.Orig_wiki(), itm.Lnki_ttl(), itm.Lnki_ext().Id(), itm.Html_w(), itm.Html_h(), itm.Lnki_thumbtime(), Fsdb_xtn_thm_tbl.Modified_null, Fsdb_xtn_thm_tbl.Hash_null, bin_len, bin_rdr);
			}
		} finally {bin_rdr.Rls();}
	}
	private void Gui_update(Xog_win_wtr win_wtr, Xof_fsdb_itm itm) {
		int len = itm.Html_ids_len();
		for (int i = 0; i < len; i++) {
			win_wtr.Html_img_update("xowa_file_img_" + itm.Html_ids_get(i), itm.Html_url().To_http_file_str(), itm.Html_w(), itm.Html_h());
		}
	}
	public static final Xof_fsdb_mgr_utl _ = new Xof_fsdb_mgr_utl(); Xof_fsdb_mgr_utl() {}
}

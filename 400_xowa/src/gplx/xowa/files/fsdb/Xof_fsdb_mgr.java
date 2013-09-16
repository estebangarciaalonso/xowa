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
import gplx.fsdb.*; import gplx.xowa.files.bins.*; import gplx.xowa.files.qrys.*; import gplx.xowa.files.regs.*;
public interface Xof_fsdb_mgr extends RlsAble {
	boolean Tid_is_mem();
	Xof_qry_mgr Qry_mgr();
	Xof_bin_mgr Bin_mgr();
	Xof_bin_wkr Bin_wkr_fsdb();
	void Init_by_wiki(Io_url db_dir, Io_url fs_dir, Xow_repo_mgr repo_mgr);
	void Img_insert(Fsdb_xtn_img_itm rv, byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr, int img_w, int img_h, int img_bits);
	void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int w, int thumbtime, int h, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr);
	void Reg_insert(Xof_fsdb_itm itm, byte repo_id, byte status);
	void Reg_select(Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms);
}
class Xof_fsdb_mgr_utl {
	public void Fsdb_search(Xof_fsdb_mgr fsdb_mgr, Io_url file_dir, Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms, int itms_len, Xow_repo_mgr repo_mgr, Xof_url_bldr url_bldr) {
		ListAdp fsdb_list = ListAdp_.new_();
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
				case Xof_reg_wkr_.Tid_missing_qry:
				case Xof_reg_wkr_.Tid_missing_bin:	continue;	// already missing; do not try to find again
			}
			if (fsdb_mgr.Qry_mgr().Find(exec_tid, itm)) {
				Xof_repo_pair repo_pair = repo_mgr.Repos_get_by_wiki(itm.Orig_wiki());
				byte orig_wiki = repo_pair.Id();
				if (fsdb_mgr.Bin_mgr().Find(exec_tid, itm)) {
//						String rel_url = itm.Html_url_abs().GenRelUrl_orEmpty(file_dir);
					fsdb_mgr.Reg_insert(itm, orig_wiki, Xof_reg_wkr_.Tid_found_orig);
					Update(win_wtr, itm);
				}
				else {
					itm.Rslt_bin_(Xof_bin_wkr_.Tid_not_found);
					fsdb_mgr.Reg_insert(itm, orig_wiki, Xof_reg_wkr_.Tid_missing_bin);
				}
			}
			else {
				fsdb_mgr.Reg_insert(itm, Xof_repo_itm.Repo_unknown, Xof_reg_wkr_.Tid_missing_qry);
			}
		}
	}
	private Int_2_ref html_size = new Int_2_ref();
	private void Itm_process(Io_url file_dir, Xog_win_wtr win_wtr, Xof_fsdb_itm itm, ListAdp fsdb_list, Xow_repo_mgr repo_mgr, Xof_url_bldr url_bldr, byte exec_tid) {
		switch (itm.Rslt_reg()) {
			case Xof_reg_wkr_.Tid_found_orig:
				itm.Html_size_calc(html_size, exec_tid);
				Xof_repo_itm repo = repo_mgr.Repos_get_by_wiki(itm.Orig_wiki()).Trg();
				Io_url trg_url = url_bldr.Set_trg_file_(itm.Lnki_type_as_mode(), repo, itm.Lnki_ttl(), itm.Lnki_md5(), itm.Lnki_ext(), itm.Html_w(), itm.Lnki_thumbtime()).Xto_url();
				itm.Html_url_(trg_url);
				Update(win_wtr, itm);
				break;
			case Xof_reg_wkr_.Tid_missing_qry:
			case Xof_reg_wkr_.Tid_missing_bin:		break;
			case Xof_reg_wkr_.Tid_missing_reg:
			case Xof_reg_wkr_.Tid_null:				fsdb_list.Add(itm); break;
			default:								throw Err_.unhandled(itm.Rslt_reg());
		}
	}
	private void Update(Xog_win_wtr win_wtr, Xof_fsdb_itm itm) {
		int len = itm.Html_ids_len();
		for (int i = 0; i < len; i++) {
			win_wtr.Html_img_update("xowa_file_img_" + itm.Html_ids_get(i), itm.Html_url().To_http_file_str(), itm.Html_w(), itm.Html_h());
		}
	}
        public static final Xof_fsdb_mgr_utl _ = new Xof_fsdb_mgr_utl(); Xof_fsdb_mgr_utl() {}
}

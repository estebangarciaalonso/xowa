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
import gplx.fsdb.*; import gplx.xowa.files.bins.*; import gplx.xowa.files.qrys.*; import gplx.xowa.files.wiki_orig.*;
import gplx.xowa.files.fsdb.caches.*;
import gplx.xowa.files.gui.*;
public interface Xof_fsdb_mgr extends RlsAble {
	boolean Tid_is_mem();
	Xow_wiki Wiki();
	Xof_qry_mgr Qry_mgr();
	Xof_bin_mgr Bin_mgr();
	Xof_bin_wkr Bin_wkr_fsdb();
	Gfo_usr_dlg Usr_dlg();
	Fsdb_mnt_mgr Mnt_mgr();
	Cache_mgr Cache_mgr();
	void Db_bin_max_(long v);
	void Init_by_wiki(Xow_wiki wiki, Io_url db_dir, Io_url fs_dir, Xow_repo_mgr repo_mgr);
	boolean Init_by_wiki(Xow_wiki wiki);
	boolean Init_by_wiki__add_bin_wkrs(Xow_wiki wiki);
	void Img_insert(Fsdb_xtn_img_itm rv, byte[] dir, byte[] fil, int ext_id, int img_w, int img_h, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr);
	void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int thm_w, int thm_h, double thumbtime, int page, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr);
	void Fil_insert(Fsdb_fil_itm rv    , byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr);
	void Reg_insert(Xof_fsdb_itm itm, byte repo_id, byte status);
	void Reg_select(Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms);
	void Reg_select_only(Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms, OrderedHash hash);
}
class Xof_fsdb_mgr_utl {
	public void Fsdb_search(Xof_fsdb_mgr fsdb_mgr, Io_url file_dir, Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms, Xow_repo_mgr repo_mgr, Xof_url_bldr url_bldr) {
		ListAdp fsdb_list = ListAdp_.new_();
		int itms_len = itms.Count();
		for (int i = 0; i < itms_len; i++) {
			if (win_wtr.Canceled()) return;
			Xof_fsdb_itm itm = (Xof_fsdb_itm)itms.FetchAt(i);
			Itm_process(fsdb_mgr, file_dir, win_wtr, itm, fsdb_list, repo_mgr, url_bldr, exec_tid);
		}
		itms_len = fsdb_list.Count(); if (itms_len == 0) return;	// all items found; return;
		Reg_search(fsdb_mgr, file_dir, win_wtr, exec_tid, fsdb_list, itms_len, repo_mgr);
	}
	private void Reg_search(Xof_fsdb_mgr fsdb_mgr, Io_url file_dir, Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms_list, int itms_len, Xow_repo_mgr repo_mgr) {
		for (int i = 0; i < itms_len; i++) {
			if (win_wtr.Canceled()) return;
			Xof_fsdb_itm itm = (Xof_fsdb_itm)itms_list.FetchAt(i);
			try {
				Reg_search_itm(fsdb_mgr, file_dir, win_wtr, exec_tid, itms_list, itms_len, repo_mgr, itm );
			} catch (Exception e) {win_wtr.Warn_many("", "", "file.search.error: page=~{0} img=~{1} err=~{2}", String_.new_utf8_(fsdb_mgr.Wiki().Ctx().Page().Ttl().Raw()), String_.new_utf8_(itm.Lnki_ttl()), Err_.Message_gplx_brief(e));}
		}
	}
	private void Reg_search_itm(Xof_fsdb_mgr fsdb_mgr, Io_url file_dir, Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms_list, int itms_len, Xow_repo_mgr repo_mgr, Xof_fsdb_itm itm) {
		switch (itm.Rslt_reg()) {
			case Xof_wiki_orig_wkr_.Tid_missing_qry:
			case Xof_wiki_orig_wkr_.Tid_missing_bin:	return;	// already missing; do not try to find again
		}
		if (itm.Lnki_ext().Id_is_audio() && exec_tid != Xof_exec_tid.Tid_viewer_app) {	// NOTE: was audio_strict, but v2 always redefines .ogg as .ogv; DATE:2014-02-02
			itm.Rslt_qry_(Xof_qry_wkr_.Tid_noop);
			return;
		}
		if (fsdb_mgr.Qry_mgr().Find(exec_tid, itm)) {
			Xof_repo_pair repo_pair = repo_mgr.Repos_get_by_wiki(itm.Orig_wiki());
			if (repo_pair == null) {
				fsdb_mgr.Reg_insert(itm, Xof_repo_itm.Repo_unknown, Xof_wiki_orig_wkr_.Tid_missing_qry);
				return;
			}
			byte orig_wiki = repo_pair.Repo_id();	// NOTE: should be itm.Orig_repo, but throws null refs
			if (itm.Lnki_ext().Id_is_audio() && exec_tid != Xof_exec_tid.Tid_viewer_app) {	// NOTE: was audio_strict, but v2 always redefines .ogg as .ogv; DATE:2014-02-02
				itm.Rslt_qry_(Xof_qry_wkr_.Tid_mock);
				itm.Rslt_bin_(Xof_bin_wkr_.Tid_noop);
				fsdb_mgr.Reg_insert(itm, orig_wiki, Xof_wiki_orig_wkr_.Tid_noop);
				return;
			}
			if (fsdb_mgr.Bin_mgr().Find_to_url_as_bool(ListAdp_.Null, exec_tid, itm)) {
				fsdb_mgr.Reg_insert(itm, orig_wiki, Xof_wiki_orig_wkr_.Tid_found_orig);
				// TODO: this "breaks" tests b/c mock bin_wkr is fsdb; 
				if (itm.Rslt_bin() != Xof_bin_wkr_.Tid_fsdb_wiki)	// if bin is from fsdb, don't save it; occurs when page has new file listed twice; 1st file inserts into fsdb; 2nd file should find in fsdb and not save again
					Fsdb_save(fsdb_mgr, itm);
				Js_img_mgr.Update_img(win_wtr, itm);
			}
			else {
				itm.Rslt_bin_(Xof_bin_wkr_.Tid_not_found);
				fsdb_mgr.Reg_insert(itm, orig_wiki, Xof_wiki_orig_wkr_.Tid_missing_bin);
//					gplx.xowa.files.gui.Js_img_mgr.Update_img_missing(win_wtr, itm.Html_uid());
			}
		}
		else {
			fsdb_mgr.Reg_insert(itm, Xof_repo_itm.Repo_unknown, Xof_wiki_orig_wkr_.Tid_missing_qry);
//				gplx.xowa.files.gui.Js_img_mgr.Update_img_missing(win_wtr, itm.Html_uid());
		}
	}
	private Xof_img_size img_size = new Xof_img_size();
	private void Itm_process(Xof_fsdb_mgr fsdb_mgr, Io_url file_dir, Xog_win_wtr win_wtr, Xof_fsdb_itm itm, ListAdp fsdb_list, Xow_repo_mgr repo_mgr, Xof_url_bldr url_bldr, byte exec_tid) {
		switch (itm.Rslt_reg()) {
			case Xof_wiki_orig_wkr_.Tid_found_orig:
				itm.Html__init(repo_mgr, url_bldr, img_size, exec_tid);
				//	Js_img_mgr.Update_img(win_wtr, itm);		// DELETE: DATE:2014-02-01
				if (!Env_.Mode_testing()) {
					Cache_fil_itm cache_fil_itm = fsdb_mgr.Cache_mgr().Reg(fsdb_mgr.Wiki(), itm, 0);
					if (cache_fil_itm.Fil_size() == 0) {
						long fil_size = Io_mgr._.QueryFil(itm.Html_url()).Size();
						cache_fil_itm.Fil_size_(fil_size);
					}
				}
				break;
			case Xof_wiki_orig_wkr_.Tid_missing_qry:
			case Xof_wiki_orig_wkr_.Tid_missing_bin:		break;
			case Xof_wiki_orig_wkr_.Tid_missing_reg:
			case Xof_wiki_orig_wkr_.Tid_noop:			// previous attempt was noop; only occurs if oga and exec_tid != viewer
			case Xof_wiki_orig_wkr_.Tid_null:			fsdb_list.Add(itm); break;
			default:									throw Err_.unhandled(itm.Rslt_reg());
		}
	}
	private void Fsdb_save(Xof_fsdb_mgr fsdb_mgr, Xof_fsdb_itm itm) {
		Io_url html_url = itm.Html_url();
		long bin_len = Io_mgr._.QueryFil(html_url).Size();
		gplx.ios.Io_stream_rdr bin_rdr = gplx.ios.Io_stream_rdr_.file_(html_url);
		try {
			bin_rdr.Open();
			if (itm.Lnki_ext().Id_is_thumbable_img()) {
				if (itm.File_is_orig()) {
					Fsdb_xtn_img_itm img_itm = new Fsdb_xtn_img_itm();
					fsdb_mgr.Img_insert(img_itm, itm.Orig_wiki(), itm.Lnki_ttl(), itm.Lnki_ext().Id(), itm.Html_w(), itm.Html_h(), Fsdb_xtn_thm_tbl.Modified_null, Fsdb_xtn_thm_tbl.Hash_null, bin_len, bin_rdr);
				}
				else {
					Fsdb_xtn_thm_itm thm_itm = Fsdb_xtn_thm_itm.new_();
					fsdb_mgr.Thm_insert(thm_itm, itm.Orig_wiki(), itm.Lnki_ttl(), itm.Lnki_ext().Id(), itm.Html_w(), itm.Html_h(), itm.Lnki_thumbtime(), itm.Lnki_page(), Fsdb_xtn_thm_tbl.Modified_null, Fsdb_xtn_thm_tbl.Hash_null, bin_len, bin_rdr);
				}
			}
			else {
				if (itm.Lnki_ext().Id_is_video() && !itm.File_is_orig()) {	// insert as thumbnail
					Fsdb_xtn_thm_itm thm_itm = Fsdb_xtn_thm_itm.new_();
					fsdb_mgr.Thm_insert(thm_itm, itm.Orig_wiki(), itm.Lnki_ttl(), itm.Lnki_ext().Id(), itm.Html_w(), itm.Html_h(), itm.Lnki_thumbtime(), itm.Lnki_page(), Fsdb_xtn_thm_tbl.Modified_null, Fsdb_xtn_thm_tbl.Hash_null, bin_len, bin_rdr);
				}
				else {
					Fsdb_fil_itm fil_itm = new Fsdb_fil_itm();
					fsdb_mgr.Fil_insert(fil_itm, itm.Orig_wiki(), itm.Lnki_ttl(), itm.Lnki_ext().Id(), Fsdb_xtn_thm_tbl.Modified_null, Fsdb_xtn_thm_tbl.Hash_null, bin_len, bin_rdr);
				}
			}
			if (!Env_.Mode_testing())
				fsdb_mgr.Cache_mgr().Reg(fsdb_mgr.Wiki(), itm, bin_len);
		}
		catch (Exception e) {
			fsdb_mgr.Usr_dlg().Warn_many("", "", "failed to save file: ttl=~{0} url=~{1} err=~{2}", String_.new_utf8_(itm.Lnki_ttl()), html_url.Raw(), Err_.Message_gplx(e));
		}
		finally {bin_rdr.Rls();}
	}
	public static final Xof_fsdb_mgr_utl _ = new Xof_fsdb_mgr_utl(); Xof_fsdb_mgr_utl() {}
}

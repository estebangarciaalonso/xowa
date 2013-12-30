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
package gplx.xowa.files; import gplx.*; import gplx.xowa.*;
import gplx.xowa.files.fsdb.*; import gplx.xowa.files.bins.*;
import gplx.xowa.parsers.lnkis.*;
public class Xof_lnki_file_mgr {
	private boolean page_init_needed = true;
	private ListAdp lnki_list = ListAdp_.new_(), fsdb_list = ListAdp_.new_();
	private OrderedHash xfer_list = OrderedHash_.new_bry_();
	private Xof_url_bldr url_bldr = new Xof_url_bldr();
	private Xof_img_size tmp_img_size = new Xof_img_size();
	private Xof_fsdb_itm tmp_fsdb_itm = new Xof_fsdb_itm();
	public void Clear() {
		page_init_needed = true;
		lnki_list.Clear();
		fsdb_list.Clear();
		xfer_list.Clear();
		temp_list.Clear();
	}
	public void Add(Xop_lnki_tkn lnki) {
		lnki_list.Add(lnki);
	}
	private OrderedHash temp_list = OrderedHash_.new_bry_();
	public boolean Find(Xow_wiki wiki, byte exec_tid, Xof_xfer_itm xfer_itm) {
		try {
			if (page_init_needed) {
				page_init_needed = false;
				Create_xfer_itms();
				wiki.File_mgr().Fsdb_mgr().Init_by_wiki__add_bin_wkrs(wiki);	// NOTE: fsdb_mgr may not be init'd for wiki; assert that that it is
				wiki.File_mgr().Fsdb_mgr().Reg_select_only(wiki.App().Gui_wtr(), exec_tid, fsdb_list, temp_list);
				Hash_xfer_itms();
			}
			Init_fsdb_itm(tmp_fsdb_itm, xfer_itm);
			Xof_fsdb_itm fsdb_itm = (Xof_fsdb_itm)xfer_list.Fetch(tmp_fsdb_itm.Lnki_ttl());
			if (fsdb_itm == null) {	// no orig_data found for the current item
//					if (!xfer_itm.Lnki_ext().Id_is_media())
//						wiki.App().Usr_dlg().Warn_many("", "", "fsdb:item does not have orig data: ttl=~{0}", String_.new_utf8_(tmp_fsdb_itm.Lnki_ttl()));
			}
			else {
				if (fsdb_itm.Orig_wiki() == null) return false; // itm not found; return now, else null exception later;
				Init_fsdb_itm(fsdb_itm, xfer_itm);				// copy xfer itm props to fsdb_itm
				fsdb_itm.Html__init(wiki.File_mgr().Repo_mgr(), url_bldr, tmp_img_size, exec_tid);
				if (Io_mgr._.ExistsFil(fsdb_itm.Html_url())) {
					xfer_itm.Atrs_calc_by_fsdb(fsdb_itm.Html_w(), fsdb_itm.Html_h(), fsdb_itm.Html_url(), fsdb_itm.Html_orig_url());
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			wiki.App().Usr_dlg().Warn_many("", "", "failed to find img: img=~{0} err=~{1}", String_.new_utf8_(xfer_itm.Lnki_ttl()), Err_.Message_gplx_brief(e));
			return false;
		}
	}
	private void Create_xfer_itms() {
		int len = lnki_list.Count();
		for (int i = 0; i < len; i++) {
			Xop_lnki_tkn lnki_tkn = (Xop_lnki_tkn)lnki_list.FetchAt(i);
			Xof_fsdb_itm fsdb_itm = new Xof_fsdb_itm();
			Init_fsdb_itm(fsdb_itm, lnki_tkn);
			fsdb_list.Add(fsdb_itm);
		}
	}
	private void Hash_xfer_itms() {
		int len = fsdb_list.Count();
		for (int i = 0; i < len; i++) {
			Xof_fsdb_itm fsdb_itm = (Xof_fsdb_itm)fsdb_list.FetchAt(i);
			byte[] fsdb_itm_key = fsdb_itm.Lnki_ttl();
			if (!temp_list.Has(fsdb_itm_key) && !xfer_list.Has(fsdb_itm_key))
				xfer_list.Add(fsdb_itm_key, fsdb_itm);
			byte[] fsdb_redirect_ttl = fsdb_itm.Orig_ttl();
			if (fsdb_redirect_ttl != null && !temp_list.Has(fsdb_itm_key) && !xfer_list.Has(fsdb_itm_key))
				xfer_list.Add(fsdb_redirect_ttl, fsdb_itm);
		}
	}
	private void Init_fsdb_itm(Xof_fsdb_itm fsdb_itm, Xop_lnki_tkn lnki_tkn) {
		byte[] lnki_ttl = lnki_tkn.Ttl().Page_db();
		Xof_ext lnki_ext = Xof_ext_.new_by_ttl_(lnki_ttl);
		byte[] lnki_md5 = Xof_xfer_itm.Md5_(lnki_ttl);
		fsdb_itm.Init_by_lnki(lnki_ttl, lnki_ext, lnki_md5, lnki_tkn.Lnki_type(), lnki_tkn.Width(), lnki_tkn.Height(), lnki_tkn.Upright(), lnki_tkn.Thumbtime());
	}
	private void Init_fsdb_itm(Xof_fsdb_itm fsdb_itm, Xof_xfer_itm xfer_itm) {
		byte[] lnki_ttl = xfer_itm.Lnki_ttl();
		Xof_ext lnki_ext = xfer_itm.Lnki_ext();
		byte[] lnki_md5 = Xof_xfer_itm.Md5_(lnki_ttl);
		fsdb_itm.Init_by_lnki(lnki_ttl, lnki_ext, lnki_md5, xfer_itm.Lnki_type(), xfer_itm.Lnki_w(), xfer_itm.Lnki_h(), xfer_itm.Lnki_upright(), xfer_itm.Lnki_thumbtime());
	}
}

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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.fsdb.*; import gplx.ios.*; import gplx.xowa.files.*; import gplx.xowa.files.bins.*; import gplx.xowa.files.fsdb.*; import gplx.xowa.dbs.tbls.*;
public class Xob_fsdb_make extends Xob_itm_basic_base implements Xob_cmd {
	private byte[] prv_ttl = ByteAry_.Empty; private byte prv_repo = 0;
	private int select_interval = 2500, progress_interval = 1, commit_interval = 1;
	private byte[] wiki_key;
	private int exec_count; int exec_errors;
	public Xob_fsdb_make(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.fsdb_make";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		this.wiki_key = wiki.Key_bry();
		wiki.Init_assert();
		bin_mgr = fsdb_mgr.Init_by_wiki(wiki).Bin_mgr();
	}
	public void Cmd_run() {Exec();}
	public void Cmd_end() {}
	public void Cmd_print() {}
	private Xof_bin_mgr bin_mgr;
	private Xof_fsdb_mgr_sql fsdb_mgr = new Xof_fsdb_mgr_sql(); 
	private Db_provider provider; private Db_stmt lnki_regy_stmt;
	private Xob_bmk_mgr bmk_mgr = new Xob_bmk_mgr();
	public void Exec() {
		Init_db();
		ListAdp list = ListAdp_.new_();
		while (true) {
			if (!Select_ttls(list)) break;	// no more ttls found
			int list_count = list.Count();
			for (int i = 0; i < list_count; i++) {
				Xodb_tbl_oimg_xfer_itm itm = (Xodb_tbl_oimg_xfer_itm)list.FetchAt(i);
				Download_itm(itm);
			}
		}
		// mass update of img_regy
	}
	private void Init_db() {
		Xodb_db_file db_file = Xodb_db_file.init__oimg_lnki(wiki);
		provider = db_file.Provider();
		bmk_mgr.Init(provider, this.Cmd_key(), true, false, true).Load();
		prv_repo = bmk_mgr.Repo_prv();
		prv_ttl = bmk_mgr.Ttl_prv();
		lnki_regy_stmt = Xob_xfer_regy_tbl.Update_stmt(provider);
	}
	private boolean Select_ttls(ListAdp list) {
		boolean found = false;
		list.Clear();
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = Xob_xfer_regy_tbl.Select(provider, prv_repo, prv_ttl, select_interval);	// use ttl order for optimized retrival from fsdb
			while (rdr.MoveNextPeer()) {
				Xodb_tbl_oimg_xfer_itm itm = Xodb_tbl_oimg_xfer_itm.new_rdr_(rdr);
				list.Add(itm);
				found = true;
			}
		}	finally {rdr.Rls();}
		return found;
	}
	private void Download_itm(Xodb_tbl_oimg_xfer_itm itm) {
		try {
			Download(itm);
			prv_repo = itm.Orig_repo();
			prv_ttl = itm.Lnki_ttl();
			bmk_mgr.Update(prv_repo, Xow_ns_.Id_null, prv_ttl);
			if (exec_count % commit_interval == 0) {
				provider.Txn_mgr().Txn_end_all_bgn_if_none();
				bmk_mgr.Save();
				usr_dlg.Prog_many("", "", "committing data: count=~{0} failed=~{1}", exec_count, exec_errors);
			}
		}
		catch (Exception exc) {
			++exec_errors;
			usr_dlg.Warn_many("", "", "download error; ttl=~{0} w=~{1} err=~{2}", String_.new_utf8_(itm.Lnki_ttl()), itm.Lnki_w(), Err_.Message_gplx_brief(exc));
		}
	}
	private void Download(Xodb_tbl_oimg_xfer_itm itm) {
		++exec_count;
		byte[] wiki = itm.Orig_repo() == Xof_repo_itm.Repo_local ? wiki_key : Xow_wiki_.Domain_commons_bry;
		itm.Orig_wiki_(wiki);
		if (exec_count % progress_interval == 0) usr_dlg.Prog_many("", "", "downloading image: count=~{0} failed=~{1} ttl=~{2}", exec_count, exec_errors, String_.new_utf8_(itm.Orig_ttl()));
		if (itm.Html_w() > 0) {
			Io_stream_rdr bin_rdr = bin_mgr.Find_as_rdr(Xof_exec_tid.Tid_wiki_page, itm);
			if (bin_rdr != Io_stream_rdr_.Null) {
				Download_pass(itm, bin_rdr);
				return;
			}
		}
		Download_fail(itm);
	}
	private void Download_fail(Xodb_tbl_oimg_xfer_itm itm) {
		Xob_xfer_regy_tbl.Update(lnki_regy_stmt, Bool_.N, itm.Lnki_id(), itm.Lnki_wkr_tid(), itm.Lnki_wkr_msg());
		++exec_errors;
		usr_dlg.Warn_many("", "", "failed: ttl=~{0} w=~{1}", String_.new_utf8_(itm.Lnki_ttl()), itm.Lnki_w());
	}
	private Fsdb_xtn_thm_itm tmp_thm_itm = new Fsdb_xtn_thm_itm();
	private void Download_pass(Xodb_tbl_oimg_xfer_itm itm, Io_stream_rdr rdr) {
		fsdb_mgr.Thm_insert(tmp_thm_itm, itm.Orig_wiki(), itm.Lnki_ttl(), itm.Lnki_ext_id(), itm.Html_w(), itm.Html_h(), itm.Lnki_thumbtime(), Sqlite_engine_.Date_null, Fsdb_xtn_thm_tbl.Hash_null, rdr.Len(), rdr);
		Xob_xfer_regy_tbl.Update(lnki_regy_stmt, Bool_.Y, itm.Lnki_id(), itm.Lnki_wkr_tid(), itm.Lnki_wkr_msg());			
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_prv_ttl_))				prv_ttl = m.ReadBry("v");
		else if	(ctx.Match(k, Invk_progress_interval_))		progress_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_commit_interval_))		commit_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_select_interval_))		select_interval = m.ReadInt("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_prv_ttl_ = "prv_ttl_", Invk_select_interval_ = "select_interval_", Invk_progress_interval_ = "progress_interval_", Invk_commit_interval_ = "commit_interval_";
	public static byte Status_null = 0, Status_pass = 1, Status_fail = 2;
}
class Xodb_tbl_oimg_xfer_itm extends Xof_fsdb_itm {	public int 			Lnki_id() {return lnki_id;} private int lnki_id;
	public int 			Orig_page_id() {return orig_page_id;} private int orig_page_id;
	public int			Lnki_ext_id() {return lnki_ext_id;} private int lnki_ext_id;
	public byte			Lnki_done() {return lnki_done;} private byte lnki_done;
	public byte			Lnki_wkr_tid() {return lnki_wkr_tid;} private byte lnki_wkr_tid;
	public String		Lnki_wkr_msg() {return lnki_wkr_msg;} private String lnki_wkr_msg;
	public static Xodb_tbl_oimg_xfer_itm new_rdr_(DataRdr rdr) {
		Xodb_tbl_oimg_xfer_itm rv = new Xodb_tbl_oimg_xfer_itm();
		rv.lnki_id = rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_lnki_id);
		rv.Orig_repo_(rdr.ReadByte(Xob_xfer_regy_tbl.Fld_oxr_xfer_repo));
		rv.orig_page_id = rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_orig_page_id);
		rv.File_is_orig_(rdr.ReadByte(Xob_xfer_regy_tbl.Fld_oxr_file_is_orig) == Bool_.Y_byte);
		rv.Orig_ttl_(rdr.ReadBryByStr(Xob_xfer_regy_tbl.Fld_oxr_xfer_ttl));
		rv.lnki_ext_id = rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_xfer_ext);
		rv.Lnki_thumbtime_(rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_xfer_thumbtime));
		rv.lnki_done = rdr.ReadByte(Xob_xfer_regy_tbl.Fld_oxr_xfer_status);
		rv.lnki_wkr_tid = rdr.ReadByte(Xob_xfer_regy_tbl.Fld_oxr_xfer_bin_tid);
		rv.lnki_wkr_msg = rdr.ReadStr(Xob_xfer_regy_tbl.Fld_oxr_xfer_bin_msg);
		rv.Lnki_ttl_(rdr.ReadBryByStr(Xob_xfer_regy_tbl.Fld_oxr_xfer_ttl));
		rv.Html_size_(rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_file_w), Xof_img_size.Null);
//			rv.Lnki_w_(rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_html_w));
//			rv.Lnki_h_(rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_html_h));
//			rv.Lnki_type_(rdr.ReadByte(Xob_xfer_regy_tbl.Fld_oxr_xfer_type));
//			rv.Orig_redirect_(rdr.ReadBryByStr(Xob_xfer_regy_tbl.Fld_oxr_orig_redirect));
		return rv;
	}
}

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
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.fsdb.*; import gplx.ios.*; import gplx.xowa.files.fsdb.*; import gplx.xowa.files.bins.*;
public class Xob_fsdb_make extends Xob_itm_basic_base implements Xob_cmd {
	private byte[] prv_ttl = ByteAry_.Empty; private int select_interval = 2500, progress_interval = 1;
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
	public void Cmd_run() {
		this.Select_all();
	}
	public void Cmd_end() {}
	public void Cmd_print() {}
	private Xof_bin_mgr bin_mgr;
	private Xob_xfer_regy_tbl tbl = new Xob_xfer_regy_tbl();
	private Xof_fsdb_mgr_sql fsdb_mgr = new Xof_fsdb_mgr_sql(); 
	private Db_stmt lnki_regy_stmt;
	public void Select_all() {
		ListAdp list = ListAdp_.new_();
		Db_provider provider = Sqlite_engine_.Provider_load_or_make_(wiki.Db_mgr_as_sql().Fsys_mgr().Trg_dir().GenSubFil("oimg_lnki.sqlite3"));
		byte prv_wiki_id = Byte_.Zero;
		lnki_regy_stmt = tbl.Update_stmt(provider);
		while (true) {
			list.Clear();
			DataRdr rdr = DataRdr_.Null;
			try {
				rdr = tbl.Select(provider, prv_wiki_id, prv_ttl, select_interval);	// use ttl order for optimized retrival from fsdb
				while (rdr.MoveNextPeer()) {
					Xodb_tbl_oimg_xfer_itm itm = Xodb_tbl_oimg_xfer_itm.new_rdr_(rdr);
					list.Add(itm);
					prv_ttl = itm.Lnki_ttl();
				}
			}	finally {rdr.Rls();}
			int list_count = list.Count(); if (list_count == 0) break;	// no more found
			for (int i = 0; i < list_count; i++) {
				Xodb_tbl_oimg_xfer_itm itm = (Xodb_tbl_oimg_xfer_itm)list.FetchAt(i);
				Download(itm);
			}
		}
		// mass update of img_regy
	}	
	private Int_2_ref actl_size = new Int_2_ref();
	private void Download(Xodb_tbl_oimg_xfer_itm itm) {
		++exec_count;
		if (itm.Orig_w() == Xop_lnki_tkn.Width_null) {
			boolean qry_pass = fsdb_mgr.Qry_mgr().Find(Xof_exec_tid.Tid_wiki_page, itm);
			if (!qry_pass) return;
		}
		itm.Html_size_calc(actl_size, Xof_exec_tid.Tid_wiki_page);
		byte[] wiki = itm.Orig_repo() == Xof_repo_itm.Repo_local ? wiki_key : Xow_wiki_.Domain_commons_bry;
		itm.Orig_wiki_(wiki);
		if (exec_count % progress_interval == 0) usr_dlg.Prog_many("", "", "downloading image: count=~{0} failed=~{1} ttl=~{2}", exec_count, exec_errors, String_.new_utf8_(itm.Orig_ttl()));
		Io_stream_rdr bin_rdr = bin_mgr.Find_as_rdr(Xof_exec_tid.Tid_wiki_page, itm);
		if (bin_rdr == Io_stream_rdr_.Null)
			Download_fail(itm);
		else
			Download_pass(itm, bin_rdr);
	}
	private void Download_fail(Xodb_tbl_oimg_xfer_itm itm) {
		tbl.Update(lnki_regy_stmt, Bool_.N, itm.Lnki_id(), itm.Lnki_wkr_tid(), itm.Lnki_wkr_msg());
		++exec_errors;
	}
	private Fsdb_xtn_thm_itm tmp_thm_itm = new Fsdb_xtn_thm_itm();
	private void Download_pass(Xodb_tbl_oimg_xfer_itm itm, Io_stream_rdr rdr) {
		fsdb_mgr.Thm_insert(tmp_thm_itm, itm.Orig_wiki(), itm.Lnki_ttl(), itm.Lnki_ext_id(), itm.Html_w(), itm.Html_h(), itm.Lnki_thumbtime(), Sqlite_engine_.Date_null, Fsdb_xtn_thm_tbl.Hash_null, rdr.Len(), rdr);
		tbl.Update(lnki_regy_stmt, Bool_.Y, itm.Lnki_id(), itm.Lnki_wkr_tid(), itm.Lnki_wkr_msg());			
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_prv_ttl_))				prv_ttl = m.ReadBry("v");
		else if	(ctx.Match(k, Invk_progress_interval_))		progress_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_select_interval_))		select_interval = m.ReadInt("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_prv_ttl_ = "prv_ttl_", Invk_select_interval_ = "select_interval_", Invk_progress_interval_ = "progress_interval_";
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
		rv.Orig_repo_(rdr.ReadByte(Xob_xfer_regy_tbl.Fld_oxr_orig_repo));
		rv.Orig_redirect_(rdr.ReadBryByStr(Xob_xfer_regy_tbl.Fld_oxr_orig_redirect));
		rv.orig_page_id = rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_orig_page_id);
		rv.Orig_ttl_(rdr.ReadBryByStr(Xob_xfer_regy_tbl.Fld_oxr_orig_ttl));
		rv.Lnki_ttl_(rdr.ReadBryByStr(Xob_xfer_regy_tbl.Fld_oxr_lnki_ttl));
		rv.lnki_ext_id = rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_orig_ext);
		rv.Lnki_type_(rdr.ReadByte(Xob_xfer_regy_tbl.Fld_oxr_lnki_type));
		rv.Lnki_w_(rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_lnki_w));
		rv.Lnki_h_(rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_lnki_h));
		rv.Lnki_thumbtime_(rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_lnki_thumbtime));
		rv.lnki_done = rdr.ReadByte(Xob_xfer_regy_tbl.Fld_oxr_lnki_status);
		rv.lnki_wkr_tid = rdr.ReadByte(Xob_xfer_regy_tbl.Fld_oxr_lnki_bin_tid);
		rv.lnki_wkr_msg = rdr.ReadStr(Xob_xfer_regy_tbl.Fld_oxr_lnki_bin_msg);
		rv.Html_size_(rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_html_w), rdr.ReadInt(Xob_xfer_regy_tbl.Fld_oxr_html_h));
		return rv;
	}
}

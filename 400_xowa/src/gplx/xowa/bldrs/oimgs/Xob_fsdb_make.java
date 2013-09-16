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
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.fsdb.*; import gplx.ios.*; import gplx.xowa.files.fsdb.*;
public class Xob_fsdb_make extends Xob_itm_basic_base implements Xob_cmd {
	private byte[] prv_ttl = ByteAry_.Empty; private int select_interval = 2500; //commit_interval = 1000, progress_interval = 250;
	public Xob_fsdb_make(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.fsdb_make";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
// wiki.Init_assert();
// select all from xfer_make
// download
	}
	public void Cmd_run() {}
	public void Cmd_end() {}
	public void Cmd_print() {}
//	private Xof_bin_mgr bin_mgr = new Xof_bin_mgr(null);
//	private Db_stmt lnki_regy_stmt = null;
	public void Select_all(Db_provider p) {
		ListAdp list = ListAdp_.new_();
		DataRdr rdr = DataRdr_.Null;
		Xodb_file db_file = wiki.Db_mgr_as_sql().Fsys_mgr().Get_or_make("oimg_lnki");
		Db_provider provider = db_file.Provider();
		Xodb_tbl_oimg_xfer_regy tbl = new Xodb_tbl_oimg_xfer_regy();
		byte prv_wiki_id = Byte_.Zero;
//		lnki_regy_stmt = tbl.Update_stmt(provider);
		while (true) {
			list.Clear();
			try {
				rdr = tbl.Select(provider, prv_wiki_id, prv_ttl, select_interval);	// use ttl order for optimized retrival from fsdb
				Xof_fsdb_itm itm = new Xof_fsdb_itm();
				byte[] lnki_ttl = rdr.ReadBryByStr(Xodb_tbl_oimg_xfer_regy.Fld_oxr_ttl);
				Xof_ext lnki_ext = Xof_ext_.new_by_ttl_(lnki_ttl);
				byte lnki_type = rdr.ReadByte(Xodb_tbl_oimg_file_regy.Fld_ofr_type);
				int lnki_w = rdr.ReadInt(Xodb_tbl_oimg_file_regy.Fld_ofr_width);
				int lnki_h = rdr.ReadInt(Xodb_tbl_oimg_file_regy.Fld_ofr_height);
				double lnki_upright = rdr.ReadDouble(Xodb_tbl_oimg_file_regy.Fld_ofr_upright);
				int lnki_thumbtime = rdr.ReadInt(Xodb_tbl_oimg_file_regy.Fld_ofr_time);
				itm.Init_by_lnki(lnki_ttl, lnki_ext, ByteAry_.Empty, lnki_type, lnki_w, lnki_h, lnki_upright, lnki_thumbtime);
				list.Add(itm);
				prv_ttl = lnki_ttl;
			} finally {rdr.Rls();}
			int list_count = list.Count();
			if (list_count == 0) break;	// no more found.
			// commit txn
			for (int i = 0; i < list_count; i++) {
				Xof_fsdb_itm itm = (Xof_fsdb_itm)list.FetchAt(i);
				Download(itm);
			}
			// process
		}
	}	
	private void Download(Xof_fsdb_itm itm) {
		/*
		Io_stream_rdr bin_rdr = bin_mgr.Find(Xof_exec_tid.Tid_wiki_page, itm);
		if (bin_rdr == Io_stream_rdr_.Null)
			Dowonload_fail(itm);
		else
			Download_pass(itm, bin_rdr);
		*/
	}
	void Download_fail(Xof_fsdb_itm itm) {
//		tbl.Update_fail();		
	}
	void Download_pass(Xof_fsdb_itm itm, Io_stream_rdr rdr) {
		/*
		fsdb_mgr.Insert(rdr);]
		reg.Insert(itm);
		tbl.Update_pass(itm);
		*/
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_prv_ttl_))				prv_ttl = m.ReadBry("v");
//			else if	(ctx.Match(k, Invk_commit_interval_))		commit_interval = m.ReadInt("v");
//			else if	(ctx.Match(k, Invk_progress_interval_))		progress_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_select_interval_))		select_interval = m.ReadInt("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_prv_ttl_ = "prv_ttl_", Invk_select_interval_ = "select_interval_";//, Invk_progress_interval_ = "progress_interval_", Invk_commit_interval_ = "commit_interval_";
//		public static int exec_count; int exec_errors;
	public static byte Status_null = 0, Status_pass = 1, Status_fail = 2; 
}

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
import gplx.xowa.dbs.*; import gplx.xowa.dbs.tbls.*; import gplx.dbs.*;
public class Xob_parse_all extends Xob_itm_basic_base implements Xob_cmd, GfoInvkAble {
	private int commit_interval = 1000, progress_interval = 250, select_interval = 2500;
	private Xob_parse_all_db_sql page_src; private Xodb_xowa_cfg_tbl tbl_cfg;
	private int[] ns_ary; private byte[] prv_ttl = ByteAry_.Empty; private int prv_ns = -1; private boolean prv_ns_dirty = false;
	private Xop_parser parser; private Xop_ctx ctx; private Xop_root_tkn root; private Xop_redirect_mgr redirect_mgr; private Gfo_msg_log msg_log;  private Xoad_wtr_dump log_dump;
	public Xob_parse_all(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	public String Cmd_key() {return KEY;} public static final String KEY = "parse.all";
	public void Cmd_bgn(Xob_bldr bldr) {
		// Xow_ns_mgr_.rebuild_(wiki.Lang(), wiki.Ns_mgr());	// rebuild; different lang will change namespaces; EX: de.wikisource.org will have Seite for File and none of {{#lst}} will work
		ctx = wiki.Ctx();
		msg_log = ctx.Msg_log();
		parser = wiki.Parser();
		root = ctx.Tkn_mkr().Root(ByteAry_.Empty);
		redirect_mgr = wiki.Redirect_mgr();
		
		wiki.Init_assert();
		page_src = new Xob_parse_all_db_sql().Init(wiki, 990, Bool_.N_byte);
		ns_ary = Int_.Ary(Xow_ns_.Id_main, Xow_ns_.Id_category, Xow_ns_.Id_template);
		
		log_dump = new Xoad_wtr_dump(bldr.App().Fsys_mgr().Temp_dir().GenSubDir("parser_dump"));

		ctx.Sys_load_tmpls_(false);
//			if (tmpl_on) Tmpl_load(wiki);
//			Xot_invk_tkn.Cache_enabled = true;
		bldr.App().Wiki_mgr().Wdata_mgr().Enabled_(false);
		
		tbl_cfg = new Xodb_xowa_cfg_tbl().Provider_(wiki.Db_mgr_as_sql().Fsys_mgr().Core_provider());
		String prv_ttl_str = tbl_cfg.Select_val("bldr.parse_all", "prv_ttl");
		if (prv_ttl_str != null) {	// previous bmk found;
			prv_ttl = ByteAry_.new_utf8_(prv_ttl_str);
			prv_ns = Int_.parse_or_(tbl_cfg.Select_val("bldr.parse_all", "prv_ns"), -1);
			usr_dlg.Note_many("", "", "restoring from bmk: ~{0}", prv_ttl_str);
		}
		else
			tbl_cfg.Insert_str("bldr.parse_all", "prv_ttl", "");
	}
	private int Calc_ns_idx_bgn(int ns_ary_len) {
		if (prv_ns != -1) {	// restoring from bmk; set correct ns;
			for (int i = 0; i < ns_ary_len; i++) {
				int ns_id = ns_ary[i];
				if (ns_id == prv_ns)
					return i;				
			}
		}
		return 0;
	}
	public void Cmd_run() {
		int ns_ary_len = ns_ary.length;
		Xow_ns ns = null;
		int ns_idx_bgn = Calc_ns_idx_bgn(ns_ary_len);
		for (int i = ns_idx_bgn; i < ns_ary_len; i++) {
			int ns_id = ns_ary[i];
			ns = wiki.Ns_mgr().Get_by_id(ns_id);
			Exec_ns(ns, ns_id);
		}
		Commit(ns, prv_ttl);
		wiki.Tmpl_regy().Clear();
		wiki.Page_cache().Clear();
	}
	private void Exec_ns(Xow_ns ns, int ns_id) {
		OrderedHash pages = OrderedHash_.new_();
		prv_ns_dirty = true;
		while (true) {
			pages.Clear();
			page_src.Fetch_next(pages, ns_id, prv_ttl);
			int page_count = pages.Count();
			if (page_count == 0)
				break;	// no more pages in ns;
			Exec_pages(ns, pages, page_count);
			prv_ttl = ((Xodb_page)pages.FetchAt(page_count - 1)).Ttl_wo_ns();
		}
	}
	private void Exec_pages(Xow_ns ns, OrderedHash pages, int page_count) {
		for (int i = 0; i < page_count; i++) {
			Xodb_page page = (Xodb_page)pages.FetchAt(i);
			Exec_page(ns, page);
		}
	}
	private void Exec_page(Xow_ns ns, Xodb_page page) {
		byte[] src = page.Text(), ttl = ns.Gen_ttl(page.Ttl_wo_ns());
		ctx.Page().Page_ttl_(Xoa_ttl.parse_(wiki, ttl)).Page_id_(page.Id());
		try {
//				msg_log.Clear();
			if (ns.Id_tmpl())
				parser.Parse_tmpl(ctx, ctx.Tkn_mkr(), wiki.Ns_mgr().Ns_template(), ttl, src);
			else {
				ctx.Tab().Display_ttl_(ttl);
				parser.Parse_page_all_clear(root, ctx, ctx.Tkn_mkr(), src);
				root.Clear();
			}
			++exec_count;
			redirect_mgr.Extract_redirect(src, src.length);
			if (msg_log.Ary_len() > 0) {
				exec_errors++;
				log_dump.Write(ttl, exec_count, ctx.Msg_log());
			}
			ctx.App().Utl_bry_bfr_mkr().Clear_fail_check();
			if		((exec_count % commit_interval) == 0)
				Commit(ns, ttl);
			else if ((exec_count % progress_interval) == 0)
				usr_dlg.Prog_many("", "", "parse progress: count=~{0} last=~{1} ns=~{2}", exec_count, String_.new_utf8_(ttl), ns.Id());
			if ((exec_count % select_interval) == 0)
				ctx.App().Reset_all();
			if (ctx.App().Tmpl_result_cache().Count() > 50000) 
				ctx.App().Tmpl_result_cache().Clear();
		}
		catch (Exception exc) {
			bldr.Usr_dlg().Warn_many(GRP_KEY, "parse", "failed to parse ~{0} error ~{1}", String_.new_utf8_(ttl), Err_.Message_lang(exc));
			ctx.App().Utl_bry_bfr_mkr().Clear();
		}
	}
	private void Commit(Xow_ns ns, byte[] ttl) {
		tbl_cfg.Update("bldr.parse_all", "prv_ttl", String_.new_utf8_(prv_ttl));
		if (prv_ns_dirty) {
			tbl_cfg.Update("bldr.parse_all", "prv_ns", ns.Id());
			prv_ns_dirty = false;
		}
		usr_dlg.Prog_many("", "", "committing: count=~{0} last=~{1} ns=~{2}", exec_count, String_.new_utf8_(ttl), ns.Id());
	}
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_end() {}
	public void Cmd_print() {}		
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_prv_ttl_))				prv_ttl = m.ReadBry("v");
		else if	(ctx.Match(k, Invk_commit_interval_))		commit_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_progress_interval_))		progress_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_select_interval_))		select_interval = m.ReadInt("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_prv_ttl_ = "prv_ttl_", Invk_progress_interval_ = "progress_interval_", Invk_commit_interval_ = "commit_interval_", Invk_select_interval_ = "select_interval_";
	public static int exec_count; int exec_errors;
	private static final String GRP_KEY = "xowa.bldr.parse";
}

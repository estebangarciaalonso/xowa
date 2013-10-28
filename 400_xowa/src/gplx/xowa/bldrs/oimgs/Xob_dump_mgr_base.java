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
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.dbs.tbls.*; import gplx.xowa.wikis.caches.*;
public abstract class Xob_dump_mgr_base extends Xob_itm_basic_base implements Xob_cmd, GfoInvkAble {
	private int exec_count;
	private int commit_interval = 1000, progress_interval = 250, cleanup_interval = 2500, select_count = 1000;
	private Xodb_xowa_cfg_tbl tbl_cfg;
	private Xob_dump_src_id page_src;
	private Xodb_file[] text_db_ary; private int[] ns_ary;
	private int text_db_init = -1;
	private boolean bmk_ns_bgn_dirty = false, bmk_db_bgn_dirty = false; 
	private int bmk_ns_bgn = -1, bmk_db_bgn =  0, bmk_id_bgn = -1;
	private int bmk_ns_end = -1, bmk_db_end = -1, bmk_id_end = -1;
	private int exec_count_max = -1;
	private boolean reset_db = false;
	private Xodb_fsys_mgr db_fsys_mgr; protected Xop_parser parser; protected Xop_ctx ctx; protected Xop_root_tkn root;
	private boolean load_tmpls;
	public abstract String Cmd_key();
	public void Cmd_bgn(Xob_bldr bldr) {
		parser = wiki.Parser();
		ctx = wiki.Ctx();
		root = ctx.Tkn_mkr().Root(ByteAry_.Empty);			
		wiki.Init_assert();	// NOTE: must init wiki for db_mgr_as_sql
		db_fsys_mgr = wiki.Db_mgr_as_sql().Fsys_mgr();
		text_db_ary = Xob_dump_src_ttl.Init_text_files_ary(db_fsys_mgr);
		text_db_init = text_db_ary[0].Id();

		page_src = new Xob_dump_src_id().Init(wiki, this.Init_redirect(), select_count);
		ns_ary = Init_ns_ary();						
		Db_provider provider = Init_db_file();
		tbl_cfg = new Xodb_xowa_cfg_tbl().Provider_(provider);
		if (reset_db) Init_reset(provider);
		Init_bmk(tbl_cfg);
		Cmd_bgn_end();
	}
	protected abstract void Cmd_bgn_end();
	public abstract byte Init_redirect();
	public abstract int[] Init_ns_ary();
	protected abstract void Init_reset(Db_provider p);
	protected abstract Db_provider Init_db_file();
	private long time_bgn;
	public void Cmd_run() {
		if (load_tmpls) Load_all_tmpls();
		if (bmk_id_bgn == Int_.MaxValue) return;
		ListAdp pages = ListAdp_.new_();
		int ns_ary_len = ns_ary.length;
		int ns_idx_bgn = Calc_ns_idx_bgn(ns_ary_len);
		time_bgn = Env_.TickCount();
		for (int i = ns_idx_bgn; i < ns_ary_len; i++) {
			bmk_ns_bgn_dirty = true;
			bmk_ns_bgn = ns_ary[i];
			Xow_ns ns = wiki.Ns_mgr().Get_by_id(bmk_ns_bgn);
			Exec_ns(pages, ns);
			bmk_db_bgn = text_db_init;
			if (bmk_ns_bgn == bmk_ns_end) break;
		}
		Exec_commit(Int_.MaxValue, ByteAry_.Empty);
		Free();
		Exec_end();
		usr_dlg.Note_many("", "", "done: ~{0} ~{1}", exec_count, DecimalAdp_.divide_(exec_count, (Env_.TickCount() - time_bgn) / 1000).XtoStr("#,###.000"));
		usr_dlg.Note_many("", "", "property count: ~{0}", Int_.XtoStr_fmt(gplx.xowa.xtns.wdatas.Wdata_pf_property.Func_count, "#,###"));
	}
	public abstract void Exec_end();
	private void Exec_ns(ListAdp pages, Xow_ns ns) {
		int text_dbs_len = text_db_ary.length;
		for (int i = bmk_db_bgn; i < text_dbs_len; i++) {
			bmk_db_bgn_dirty = true;
			bmk_db_bgn = text_db_ary[i].Id();
			if (!Exec_text_db(pages, ns, bmk_db_bgn)) return;
			bmk_id_bgn = -1;
			if (bmk_db_bgn == bmk_db_end) break;
		}
	}
	private boolean Exec_text_db(ListAdp pages, Xow_ns ns, int text_db_id) {
		while (true) {
			usr_dlg.Prog_many("", "", "fetching pages");
			page_src.Get_pages(pages, text_db_id, ns.Id(), bmk_id_bgn);
			int page_count = pages.Count();
			usr_dlg.Prog_many("", "", "fetched pages: ~{0}", page_count);
			if (page_count == 0) break;	// no more pages in db;
			for (int i = 0; i < page_count; i++) {
				Xodb_page page = (Xodb_page)pages.FetchAt(i);
				Exec_page(ns, page);
				if (	(bmk_id_end != -1 && page.Id() >= bmk_id_end)
					||	(exec_count != -1 && exec_count >= exec_count_max)) {
					return false;
				}
			}
		}
		return true;
	}
	public abstract void Exec_page_hook(Xow_ns ns, Xodb_page page, byte[] page_text);
	private void Exec_page(Xow_ns ns, Xodb_page page) {
		try {
			if ((exec_count % progress_interval) == 0)
				usr_dlg.Prog_many("", "", "parsing: ns=~{0} text_db=~{1} count=~{2} page_id=~{3} ttl=~{4}", bmk_ns_bgn, bmk_db_bgn, exec_count, page.Id(), String_.new_utf8_(page.Ttl_wo_ns()));
			Exec_page_hook(ns, page, page.Text());
			++exec_count;
			ctx.App().Utl_bry_bfr_mkr().Clear_fail_check();	// make sure all bfrs are released
			if	((exec_count % commit_interval) == 0)
				Exec_commit(page.Id(), page.Ttl_wo_ns());
			if ((exec_count % cleanup_interval) == 0)
				Free();
			if (ctx.App().Tmpl_result_cache().Count() > 50000) 
				ctx.App().Tmpl_result_cache().Clear();
		}
		catch (Exception exc) {
			bldr.Usr_dlg().Warn_many(GRP_KEY, "parse", "failed to parse ~{0} error ~{1}", String_.new_utf8_(page.Ttl_wo_ns()), Err_.Message_lang(exc));
			ctx.App().Utl_bry_bfr_mkr().Clear();
		}
	}
	public abstract void Exec_commit_bgn();
	private int Calc_ns_idx_bgn(int ns_ary_len) {
		if (bmk_ns_bgn != -1) {	// restoring from bmk; set correct ns;
			for (int i = 0; i < ns_ary_len; i++) {
				int ns_id = ns_ary[i];
				if (ns_id == bmk_ns_bgn)
					return i;				
			}
		}
		return 0;
	}
	private void Exec_commit(int page_id, byte[] ttl) {
		Exec_commit_bgn();
		bmk_id_bgn = page_id;
		tbl_cfg.Update(Cfg_parse_all, "bmk_id_bgn", page_id);
		if (bmk_ns_bgn_dirty) {
			tbl_cfg.Update(Cfg_parse_all, "bmk_ns_bgn", bmk_ns_bgn);
			bmk_ns_bgn_dirty = false;
		}
		if (bmk_db_bgn_dirty) {
			tbl_cfg.Update(Cfg_parse_all, "bmk_db_bgn", bmk_db_bgn);
			bmk_db_bgn_dirty = false;
		}
		usr_dlg.Prog_many("", "", "committing: ns=~{0} text_db=~{1} count=~{2} page_id=~{3} ttl=~{4}", bmk_ns_bgn, bmk_db_bgn, exec_count, bmk_id_bgn, String_.new_utf8_(ttl));
	}
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_end() {}
	public void Cmd_print() {}		
	private void Free() {
		ctx.App().Free_mem(true);
		gplx.xowa.xtns.scribunto.Scrib_engine.Engine_invalidate();
		wiki.Cache_mgr().Free_mem_all();
//			Env_.GarbageCollect();
//			Tfds.Write(ctx.App().Tmpl_result_cache().Count());
	}
	private void Load_all_tmpls() {
		ListAdp pages = ListAdp_.new_();
		Xow_ns ns_tmpl = wiki.Ns_mgr().Ns_template();
		Xow_defn_cache tmpl_regy = wiki.Cache_mgr().Defn_cache();
		boolean case_match = ns_tmpl.Case_match() == Xow_ns_.Case_match_1st;
		int cur_page_id = -1;
		int load_count = 0;
		usr_dlg.Note_many("", "", "tmpl_load init");
		while (true) {
			page_src.Get_pages(pages, text_db_init, Xow_ns_.Id_template, cur_page_id);
			int page_count = pages.Count();
			if (page_count == 0) break;	// no more pages in db;
			Xodb_page page = null;
			for (int i = 0; i < page_count; i++) {
				page = (Xodb_page)pages.FetchAt(i);
				Xot_defn_tmpl defn = new Xot_defn_tmpl();
				defn.Init_by_new(ns_tmpl, ns_tmpl.Gen_ttl(page.Ttl_wo_ns()), page.Text(), null, false);	// NOTE: passing null, false; will be overriden later when Parse is called
				tmpl_regy.Add(defn, case_match);
				++load_count;
			}
			cur_page_id = page.Id();
		}
		usr_dlg.Note_many("", "", "tmpl_load done: ~{0}", load_count);
	}
	private void Init_bmk(Xodb_xowa_cfg_tbl tbl_cfg) {
		String bmk_id_str = tbl_cfg.Select_val(Cfg_parse_all, Cfg_bmk_id);
		if (bmk_id_str == null) {	// bmks not found; new db; insert;
			tbl_cfg.Insert_str(Cfg_parse_all, Cfg_bmk_ns		, Int_.XtoStr(ns_ary[0]));
			tbl_cfg.Insert_str(Cfg_parse_all, Cfg_bmk_db		, Int_.XtoStr(text_db_init));
			tbl_cfg.Insert_str(Cfg_parse_all, Cfg_bmk_id		, "-1");
		}
		else {
			if (bmk_ns_bgn == -1) {
				bmk_ns_bgn = Int_.parse_or_(tbl_cfg.Select_val(Cfg_parse_all, Cfg_bmk_ns), -1);
				usr_dlg.Note_many("", "", "restoring from bmk: ns=~{0}", bmk_ns_bgn);
			}
			if (bmk_db_bgn ==  0) {
				bmk_db_bgn = tbl_cfg.Select_val_as_int(Cfg_parse_all, Cfg_bmk_db);
				usr_dlg.Note_many("", "", "restoring from bmk: db=~{0}", bmk_db_bgn);
			}
			if (bmk_id_bgn == -1) {
				bmk_id_bgn = Int_.parse_(bmk_id_str);
				usr_dlg.Note_many("", "", "restoring from bmk: id=~{0}", bmk_id_bgn);
			}
		}
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_commit_interval_))		commit_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_progress_interval_))		progress_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_cleanup_interval_))		cleanup_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_select_count_))			select_count = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_bmk_ns_bgn_))			bmk_ns_bgn = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_bmk_db_bgn_))			bmk_db_bgn = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_bmk_id_bgn_))			bmk_id_bgn = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_bmk_ns_end_))			bmk_ns_end = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_bmk_db_end_))			bmk_db_end = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_bmk_id_end_))			bmk_id_end = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_exec_count_max_))		exec_count_max = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_load_tmpls_))			load_tmpls = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_reset_db_))				reset_db = m.ReadYn("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String Invk_progress_interval_ = "progress_interval_", Invk_commit_interval_ = "commit_interval_", Invk_cleanup_interval_ = "cleanup_interval_"
	, Invk_select_count_ = "select_count_"
	, Invk_bmk_ns_bgn_ = "bmk_ns_bgn_", Invk_bmk_db_bgn_ = "bmk_db_bgn_", Invk_bmk_id_bgn_ = "bmk_id_bgn_"
	, Invk_bmk_ns_end_ = "bmk_ns_end_", Invk_bmk_db_end_ = "bmk_db_end_", Invk_bmk_id_end_ = "bmk_id_end_"
	, Invk_exec_count_max_ = "exec_count_max_", Invk_load_tmpls_ = "load_tmpls_", Invk_reset_db_ = "reset_db_"
	;
	private static final String Cfg_parse_all = "bldr.parse_all", Cfg_bmk_id = "bmk_id_bgn", Cfg_bmk_db = "bmk_db_bgn", Cfg_bmk_ns = "bmk_ns_bgn";
	private static final String GRP_KEY = "xowa.bldr.parse";
}

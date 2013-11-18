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
	private int exec_count, exec_count_max = Int_.MaxValue;
	private int commit_interval = 1000, progress_interval = 250, cleanup_interval = 2500, select_size = 10 * Io_mgr.Len_mb;
	private Xodb_xowa_cfg_tbl tbl_cfg;
	private Xob_dump_src_id page_src;
	private Xodb_file[] text_db_ary; private int[] ns_ary;
	private int db_val_reset = 0, id_val_reset = 0;
	private boolean ns_val_dirty = false, db_val_dirty = false; 
	private int ns_bmk = -1, db_bmk = -1, id_bmk = -1;
	private int ns_end = -1, db_end = -1, id_end = Int_.MaxValue;
	private int ns_val = -1, db_val = -1, id_val = -1;		
	private boolean reset_db = false, exit_after_commit = false, exit_now = false;
	private Xodb_fsys_mgr db_fsys_mgr; protected Xop_parser parser; protected Xop_ctx ctx; protected Xop_root_tkn root;
	private boolean load_tmpls;
	public abstract String Cmd_key();
	private Xob_poll_mgr poll_mgr; private int poll_interval;
	@Override protected void Cmd_ctor_end(Xob_bldr bldr, Xow_wiki wiki) {
		poll_mgr = new Xob_poll_mgr(bldr.App());
	}
	public void Cmd_bgn(Xob_bldr bldr) {
		parser = wiki.Parser();
		ctx = wiki.Ctx();
		root = ctx.Tkn_mkr().Root(ByteAry_.Empty);			
		wiki.Init_assert();	// NOTE: must init wiki for db_mgr_as_sql
		db_fsys_mgr = wiki.Db_mgr_as_sql().Fsys_mgr();
		text_db_ary = Xob_dump_src_ttl.Init_text_files_ary(db_fsys_mgr);
		poll_interval = poll_mgr.Poll_interval();

		page_src = new Xob_dump_src_id().Init(wiki, this.Init_redirect(), select_size);
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
		if (id_bmk == Int_.MaxValue) return;
		ListAdp pages = ListAdp_.new_();
		int ns_ary_len = ns_ary.length;
		time_bgn = Env_.TickCount();
		id_val = id_bmk;
		for (int i = 0; i < ns_ary_len; i++) {
			if (i < ns_bmk) continue;
			ns_bmk = i;
			ns_val = ns_ary[i];
			ns_val_dirty = true;
			Xow_ns ns = wiki.Ns_mgr().Get_by_id(ns_val);
			Exec_ns(pages, ns);
			if (ns_val == ns_end) break;
			if (exit_now) break;
			db_val = db_val_reset;
		}
	}
	public abstract void Exec_end();
	private void Exec_ns(ListAdp pages, Xow_ns ns) {
		int text_dbs_len = text_db_ary.length;
		for (int i = 0; i < text_dbs_len; i++) {
			if (i < db_bmk) continue;
			db_bmk = i;
			db_val = text_db_ary[i].Id();
			db_val_dirty = true;
			Exec_text_db(pages, ns);
			if (db_val == db_end) return;
			if (exit_now) return;
			id_val = id_val_reset;
		}
	}
	private void Exec_text_db(ListAdp pages, Xow_ns ns) {
		while (true) {
			page_src.Get_pages(pages, db_val, ns_val, id_val);
			int page_count = pages.Count();
			usr_dlg.Prog_many("", "", "fetched pages: ~{0}", page_count);
			if (page_count == 0) return;	// no more pages in db;
			for (int i = 0; i < page_count; i++) {
				Xodb_page page = (Xodb_page)pages.FetchAt(i);
				Exec_page(ns, page);
				if (	id_val     >= id_end
					||	exec_count >= exec_count_max) {
					exit_now = true;
				}
				if (exit_now) return;
			}
		}
	}
	public abstract void Exec_page_hook(Xow_ns ns, Xodb_page page, byte[] page_text);
	private void Exec_page(Xow_ns ns, Xodb_page page) {
		try {
			if ((exec_count % progress_interval) == 0)
				usr_dlg.Prog_many("", "", "parsing: ns=~{0} text_db=~{1} count=~{2} time=~{3} page_id=~{4} ttl=~{5}", ns_val, db_val, exec_count, Env_.TickCount_elapsed_in_sec(time_bgn), page.Id(), String_.new_utf8_(page.Ttl_wo_ns()));
			Exec_page_hook(ns, page, page.Text());
			++exec_count;
//				exec_size_len += page.Text_len();
			id_bmk = id_val = page.Id();
			ctx.App().Utl_bry_bfr_mkr().Clear_fail_check();	// make sure all bfrs are released
			if ((exec_count % poll_interval) == 0)
				poll_mgr.Poll();
			if	((exec_count % commit_interval) == 0)
				Exec_commit(page.Ttl_wo_ns());
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
	private void Exec_commit(byte[] ttl) {
		Exec_commit_bgn();
		tbl_cfg.Update(Cfg_parse_all, Cfg_id_bmk, id_bmk);
		if (ns_val_dirty) {
			tbl_cfg.Update(Cfg_parse_all, Cfg_ns_bmk, ns_bmk);
			ns_val_dirty = false;
		}
		if (db_val_dirty) {
			tbl_cfg.Update(Cfg_parse_all, Cfg_db_bmk, db_bmk);
			db_val_dirty = false;
		}
		tbl_cfg.Provider().Txn_mgr().Txn_end_all_bgn_if_none();
		usr_dlg.Prog_many("", "", "committing: ns=~{0} text_db=~{1} count=~{2} page_id=~{3} ttl=~{4}", ns_val, db_val, exec_count, id_val, String_.new_utf8_(ttl));
		if (exit_after_commit) exit_now = true;
	}
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_end() {
		if (!exit_now)
			id_bmk = Int_.MaxValue;
		Exec_commit(ByteAry_.Empty);
		Free();
		Exec_end();
		usr_dlg.Note_many("", "", "done: ~{0} ~{1}", exec_count, DecimalAdp_.divide_safe_(exec_count, Env_.TickCount_elapsed_in_sec(time_bgn)).XtoStr("#,###.000"));
	}
	public void Cmd_print() {}		
	private void Free() {
		ctx.App().Free_mem(true);
		gplx.xowa.xtns.scribunto.Scrib_engine.Engine_invalidate();
		wiki.Cache_mgr().Defn_cache().Free_mem_all();
//			Env_.GarbageCollect();
//			Tfds.Write(ctx.App().Tmpl_result_cache().Count());
	}
	private void Load_all_tmpls() {
		ListAdp pages = ListAdp_.new_();
		Xow_ns ns_tmpl = wiki.Ns_mgr().Ns_template();
		Xow_defn_cache defn_cache = wiki.Cache_mgr().Defn_cache();
		boolean case_match = ns_tmpl.Case_match() == Xow_ns_.Case_match_1st;
		int cur_page_id = -1;
		int load_count = 0;
		usr_dlg.Note_many("", "", "tmpl_load init");
		while (true) {
			page_src.Get_pages(pages, 0, Xow_ns_.Id_template, cur_page_id);	// 0 is always template db
			int page_count = pages.Count();
			if (page_count == 0) break;	// no more pages in db;
			Xodb_page page = null;
			for (int i = 0; i < page_count; i++) {
				page = (Xodb_page)pages.FetchAt(i);
				Xot_defn_tmpl defn = new Xot_defn_tmpl();
				defn.Init_by_new(ns_tmpl, ns_tmpl.Gen_ttl(page.Ttl_wo_ns()), page.Text(), null, false);	// NOTE: passing null, false; will be overriden later when Parse is called
				defn_cache.Add(defn, case_match);
				++load_count;
				if ((load_count % 10000) == 0) usr_dlg.Prog_many("", "", "tmpl_loading: ~{0}", load_count);
			}
			cur_page_id = page.Id();
		}
		usr_dlg.Note_many("", "", "tmpl_load done: ~{0}", load_count);
	}
	private void Init_bmk(Xodb_xowa_cfg_tbl tbl_cfg) {
		String id_str = tbl_cfg.Select_val(Cfg_parse_all, Cfg_id_bmk);
		if (id_str == null) {	// bmks not found; new db; insert;
			ns_bmk = db_bmk = 0;	// set to 1st index of array
			id_bmk = 0;
			tbl_cfg.Insert_str(Cfg_parse_all, Cfg_ns_bmk		, Int_.XtoStr(ns_bmk));
			tbl_cfg.Insert_str(Cfg_parse_all, Cfg_db_bmk		, Int_.XtoStr(db_bmk));
			tbl_cfg.Insert_str(Cfg_parse_all, Cfg_id_bmk		, Int_.XtoStr(id_bmk));
		}
		else {
			if (ns_bmk == -1) {
				ns_bmk = tbl_cfg.Select_val_as_int(Cfg_parse_all, Cfg_ns_bmk);
				usr_dlg.Note_many("", "", "restoring from bmk: ns=~{0}", ns_bmk);
			}
			if (db_bmk == -1) {
				db_bmk = tbl_cfg.Select_val_as_int(Cfg_parse_all, Cfg_db_bmk);
				usr_dlg.Note_many("", "", "restoring from bmk: db=~{0}", db_bmk);
			}
			if (id_bmk == -1) {
				id_bmk = Int_.parse_(id_str);
				usr_dlg.Note_many("", "", "restoring from bmk: id=~{0}", id_bmk);
			}
		}
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_commit_interval_))		commit_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_progress_interval_))		progress_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_cleanup_interval_))		cleanup_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_select_size_))			select_size = m.ReadInt("v") * Io_mgr.Len_mb;
		else if	(ctx.Match(k, Invk_ns_bmk_))				ns_bmk = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_db_bmk_))				db_bmk = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_id_bmk_))				id_bmk = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_ns_end_))				ns_end = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_db_end_))				db_end = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_id_end_))				id_end = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_db_val_reset_))			db_val_reset = db_bmk;
		else if	(ctx.Match(k, Invk_id_val_reset_))			id_val_reset = id_bmk;
		else if	(ctx.Match(k, Invk_load_tmpls_))			load_tmpls = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_poll_mgr))				return poll_mgr;
		else if	(ctx.Match(k, Invk_reset_db_))				reset_db = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_exec_count_max_))		exec_count_max = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_exit_now_))				exit_now = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_exit_after_commit_))		exit_after_commit = m.ReadYn("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String Invk_progress_interval_ = "progress_interval_", Invk_commit_interval_ = "commit_interval_", Invk_cleanup_interval_ = "cleanup_interval_"
	, Invk_select_size_ = "select_size_"
	, Invk_ns_bmk_ = "ns_bmk_", Invk_db_bmk_ = "db_bmk_", Invk_id_bmk_ = "id_bmk_"
	, Invk_ns_end_ = "ns_end_", Invk_db_end_ = "db_end_", Invk_id_end_ = "id_end_"
	, Invk_db_val_reset_ = "db_val_reset_", Invk_id_val_reset_ = "id_val_reset_"
	, Invk_load_tmpls_ = "load_tmpls_"
	, Invk_poll_mgr = "poll_mgr", Invk_reset_db_ = "reset_db_"
	, Invk_exec_count_max_ = "exec_count_max_", Invk_exit_now_ = "exit_now_", Invk_exit_after_commit_ = "exit_after_commit_"
	;
	private static final String Cfg_parse_all = "bldr.parse_all", Cfg_id_bmk = "id_bmk", Cfg_db_bmk = "db_bmk", Cfg_ns_bmk = "ns_bmk";
	private static final String GRP_KEY = "xowa.bldr.parse";
}

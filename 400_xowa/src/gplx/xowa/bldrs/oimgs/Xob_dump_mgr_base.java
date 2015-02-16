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
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.dbs.tbls.*; import gplx.xowa.wikis.caches.*; import gplx.xowa.bldrs.files.*;
public abstract class Xob_dump_mgr_base extends Xob_itm_basic_base implements Xob_cmd, GfoInvkAble {
	private Xob_dump_src_id page_src;
	private Xodb_fsys_mgr db_fsys_mgr; protected Xop_parser parser; protected Xop_ctx ctx; protected Xop_root_tkn root;
	private int[] ns_ary; private Xodb_file[] db_ary;
	private int ns_bgn = -1, db_bgn = -1, pg_bgn = -1;
	private int ns_end = -1, db_end = -1, pg_end = Int_.MaxValue;
	private int commit_interval = 1000, progress_interval = 250, cleanup_interval = 2500, select_size = 10 * Io_mgr.Len_mb;
	private int exec_count, exec_count_max = Int_.MaxValue;
	private boolean reset_db = false, exit_after_commit = false, exit_now = false;
	private boolean load_tmpls;
	private Xob_dump_bmk_mgr bmk_mgr = new Xob_dump_bmk_mgr();
	private Xobu_poll_mgr poll_mgr; private int poll_interval = 5000;
	private Xob_rate_mgr rate_mgr = new Xob_rate_mgr();
	public abstract String Cmd_key();
	@Override protected void Cmd_ctor_end(Xob_bldr bldr, Xow_wiki wiki) {
		poll_mgr = new Xobu_poll_mgr(bldr.App());	// init in ctor so gfs can invoke methods
	}
	public void Cmd_bgn(Xob_bldr bldr) {
		parser = wiki.Parser();
		ctx = wiki.Ctx();
		root = ctx.Tkn_mkr().Root(Bry_.Empty);			
		wiki.Init_assert();	// NOTE: must init wiki for db_mgr_as_sql
		wiki.Db_mgr_as_sql().Init_load(Db_url_.sqlite_(Xodb_mgr_sql.Find_core_url(wiki)));	// NOTE: must reinit providers as previous steps may have rls'd (and left member variable conn which is closed)
		db_fsys_mgr = wiki.Db_mgr_as_sql().Fsys_mgr();
		db_ary = Xob_dump_src_ttl.Init_text_files_ary(db_fsys_mgr);
		poll_interval = poll_mgr.Poll_interval();

		page_src = new Xob_dump_src_id().Init(wiki, this.Init_redirect(), select_size);
		ns_ary = Init_ns_ary();						
		Db_conn conn = Init_db_file();
		Io_url wiki_dir = wiki.Fsys_mgr().Root_dir();
		bmk_mgr.Cfg_url_(wiki_dir.GenSubFil("xowa.file.make.cfg.gfs"));
		rate_mgr.Log_file_(wiki_dir.GenSubFil("xowa.file.make.log.csv"));
		if (reset_db) {
			bmk_mgr.Reset();
			Init_reset(conn);
		}
		bmk_mgr.Load(wiki.App(), this);
		Cmd_bgn_end();
	}
	protected abstract void Cmd_bgn_end();
	public abstract byte Init_redirect();
	public abstract int[] Init_ns_ary();
	protected abstract void Init_reset(Db_conn p);
	protected abstract Db_conn Init_db_file();
	private long time_bgn;
	public void Cmd_run() {Exec_ns_ary();}
	private void Exec_ns_ary() {
		if (pg_bgn == Int_.MaxValue) return;
		if (load_tmpls) Xob_dump_mgr_base_.Load_all_tmpls(usr_dlg, wiki, page_src);
		time_bgn = Env_.TickCount();
		Xob_dump_bmk dump_bmk = new Xob_dump_bmk();
		rate_mgr.Init();
		int ns_ary_len = ns_ary.length;
		for (int i = 0; i < ns_ary_len; i++) {
			int ns_id = ns_ary[i];
			if (ns_bgn != -1) {							// ns_bgn set
				if (ns_id == ns_bgn)					// ns_id is ns_bgn; null out ns_bgn and continue
					ns_bgn = -1;
				else									// ns_id is not ns_bgn; keep looking
					continue;
			}
			dump_bmk.Ns_id_(ns_id);
			Exec_db_ary(dump_bmk, ns_id);
			if (ns_id == ns_end) exit_now = true;		// ns_end set; exit
			if (exit_now) break;						// exit_now b/c of pg_bgn, db_bgn or something else
		}
		Exec_commit(dump_bmk.Ns_id(), dump_bmk.Db_id(), dump_bmk.Pg_id(), Bry_.Empty);
	}
	private void Exec_db_ary(Xob_dump_bmk dump_bmk, int ns_id) {
		int db_ary_len = db_ary.length;
		for (int i = 0; i < db_ary_len; i++) {
			int db_id = db_ary[i].Id();
			if (db_bgn != -1) {							// db_bgn set
				if (db_id == db_bgn)					// db_id is db_bgn; null out db_bgn and continue
					db_bgn = -1;
				else									// db_id is not db_bgn; keep looking
					continue;
			}
			dump_bmk.Db_id_(db_id);
			Exec_db_itm(dump_bmk, ns_id, db_id);
			if (db_id == db_end) exit_now = true;		// db_end set; exit;
			if (exit_now) return;						// exit_now b/c of pg_bgn, db_bgn or something else
		}
	}
	private void Exec_db_itm(Xob_dump_bmk dump_bmk, int ns_id, int db_id) {
		ListAdp pages = ListAdp_.new_();
		Xow_ns ns = wiki.Ns_mgr().Ids_get_or_null(ns_id);
		int pg_id = pg_bgn;
		while (true) {
			page_src.Get_pages(pages, db_id, ns_id, pg_id);
			int pages_len = pages.Count();
			if (pages_len == 0) {	// no more pages in db;
				if (pg_id > pg_bgn)	// reset pg_bgn to 0 only if pg_bgn seen;
					pg_bgn = 0;
				return;	
			}
			usr_dlg.Prog_many("", "", "fetched pages: ~{0}", pages_len);
			for (int i = 0; i < pages_len; i++) {
				Xodb_page page = (Xodb_page)pages.FetchAt(i);
				dump_bmk.Pg_id_(pg_id);
				Exec_pg_itm(ns, db_id, page);
				if (	pg_id		>= pg_end
					||	exec_count	>= exec_count_max) {
					exit_now = true;
				}
				if (exit_now) return;
				pg_id = page.Id();
			}
		}
	}
	private void Exec_pg_itm(Xow_ns ns, int db_id, Xodb_page page) {
		try {
			if ((exec_count % progress_interval) == 0)
				usr_dlg.Prog_many("", "", "parsing: ns=~{0} db=~{1} pg=~{2} count=~{3} time=~{4} rate=~{5} ttl=~{6}"
					, ns.Id(), db_id, page.Id(), exec_count
					, Env_.TickCount_elapsed_in_sec(time_bgn), rate_mgr.Rate_as_str(), String_.new_utf8_(page.Ttl_wo_ns()));
			ctx.Clear();
			Exec_pg_itm_hook(ns, page, page.Text());
			ctx.App().Utl_bry_bfr_mkr().Clear_fail_check();	// make sure all bfrs are released
			if (ctx.Wiki().Cache_mgr().Tmpl_result_cache().Count() > 50000) 
				ctx.Wiki().Cache_mgr().Tmpl_result_cache().Clear();
			++exec_count;
			rate_mgr.Increment();
			if ((exec_count % poll_interval) == 0)
				poll_mgr.Poll();
			if	((exec_count % commit_interval) == 0)
				Exec_commit(ns.Id(), db_id, page.Id(), page.Ttl_wo_ns());
			if ((exec_count % cleanup_interval) == 0)
				Free();
		}
		catch (Exception exc) {
			bldr.Usr_dlg().Warn_many(GRP_KEY, "parse", "failed to parse ~{0} error ~{1}", String_.new_utf8_(page.Ttl_wo_ns()), Err_.Message_lang(exc));
			ctx.App().Utl_bry_bfr_mkr().Clear();
			this.Free();
		}
	}
	public abstract void Exec_pg_itm_hook(Xow_ns ns, Xodb_page page, byte[] page_text);
	private void Exec_commit(int ns_id, int db_id, int pg_id, byte[] ttl) {
		usr_dlg.Prog_many("", "", "committing: ns=~{0} db=~{1} pg=~{2} count=~{3} ttl=~{4}", ns_id, db_id, pg_id, exec_count, String_.new_utf8_(ttl));
		Exec_commit_hook();
		bmk_mgr.Save(ns_id, db_id, pg_id);
		if (exit_after_commit) exit_now = true;
	}
	public abstract void Exec_commit_hook();
	public abstract void Exec_end_hook();
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_print() {}		
	public void Cmd_end() {
		if (!exit_now)
			pg_bgn = Int_.MaxValue;
		Exec_commit(-1, -1, -1, Bry_.Empty);
		Exec_end_hook();
		Free();
		usr_dlg.Note_many("", "", "done: ~{0} ~{1}", exec_count, DecimalAdp_.divide_safe_(exec_count, Env_.TickCount_elapsed_in_sec(time_bgn)).Xto_str("#,###.000"));
	}
	private void Free() {
		ctx.App().Free_mem(true);
		gplx.xowa.xtns.scribunto.Scrib_core.Core_invalidate();
		wiki.Cache_mgr().Free_mem_all();
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_commit_interval_))		commit_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_progress_interval_))		progress_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_rate_interval_))			rate_mgr.Reset_interval_(m.ReadInt("v"));
		else if	(ctx.Match(k, Invk_cleanup_interval_))		cleanup_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_select_size_))			select_size = m.ReadInt("v") * Io_mgr.Len_mb;
		else if	(ctx.Match(k, Invk_ns_bgn_))				{ns_bgn = m.ReadInt("v"); Notify_restoring("ns", ns_bgn);}
		else if	(ctx.Match(k, Invk_db_bgn_))				{db_bgn = m.ReadInt("v"); Notify_restoring("db", db_bgn);}
		else if	(ctx.Match(k, Invk_pg_bgn_))				{pg_bgn = m.ReadInt("v"); Notify_restoring("pg", pg_bgn);}
		else if	(ctx.Match(k, Invk_ns_end_))				ns_end = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_db_end_))				db_end = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_pg_end_))				pg_end = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_load_tmpls_))			load_tmpls = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_poll_mgr))				return poll_mgr;
		else if	(ctx.Match(k, Invk_reset_db_))				reset_db = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_exec_count_max_))		exec_count_max = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_exit_now_))				exit_now = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_exit_after_commit_))		exit_after_commit = m.ReadYn("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private void Notify_restoring(String itm, int val) {
		usr_dlg.Note_many("", "", "restoring: itm=~{0} val=~{1}", itm, val);
	}
	public static final String 
	  Invk_progress_interval_ = "progress_interval_", Invk_commit_interval_ = "commit_interval_", Invk_cleanup_interval_ = "cleanup_interval_", Invk_rate_interval_ = "rate_interval_"
	, Invk_select_size_ = "select_size_"
	, Invk_ns_bgn_ = "ns_bgn_", Invk_db_bgn_ = "db_bgn_", Invk_pg_bgn_ = "pg_bgn_"
	, Invk_ns_end_ = "ns_end_", Invk_db_end_ = "db_end_", Invk_pg_end_ = "pg_end_"
	, Invk_load_tmpls_ = "load_tmpls_"
	, Invk_poll_mgr = "poll_mgr", Invk_reset_db_ = "reset_db_"
	, Invk_exec_count_max_ = "exec_count_max_", Invk_exit_now_ = "exit_now_", Invk_exit_after_commit_ = "exit_after_commit_"
	;
	private static final String GRP_KEY = "xowa.bldr.parse";
}
class Xob_dump_mgr_base_ {
	public static void Load_all_tmpls(Gfo_usr_dlg usr_dlg, Xow_wiki wiki, Xob_dump_src_id page_src) {
		ListAdp pages = ListAdp_.new_();
		Xow_ns ns_tmpl = wiki.Ns_mgr().Ns_template();
		Xow_defn_cache defn_cache = wiki.Cache_mgr().Defn_cache();
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
				defn_cache.Add(defn, ns_tmpl.Case_match());
				++load_count;
				if ((load_count % 10000) == 0) usr_dlg.Prog_many("", "", "tmpl_loading: ~{0}", load_count);
			}
			cur_page_id = page.Id();
		}
		usr_dlg.Note_many("", "", "tmpl_load done: ~{0}", load_count);
	}
}
class Xob_dump_bmk_mgr {
	private Bry_bfr save_bfr = Bry_bfr.reset_(1024);
	public Io_url Cfg_url() {return cfg_url;} public Xob_dump_bmk_mgr Cfg_url_(Io_url v) {cfg_url = v; return this;} private Io_url cfg_url;
	public void Reset() {Io_mgr._.DeleteFil(cfg_url);}
	public void Load(Xoa_app app, Xob_dump_mgr_base dump_mgr) {
		app.Gfs_mgr().Run_url_for(dump_mgr, cfg_url);
	}
	public void Save(int ns_id, int db_id, int pg_id) {
		Save_itm(save_bfr, Xob_dump_mgr_base.Invk_ns_bgn_, ns_id);
		Save_itm(save_bfr, Xob_dump_mgr_base.Invk_db_bgn_, db_id);
		Save_itm(save_bfr, Xob_dump_mgr_base.Invk_pg_bgn_, pg_id);
		Io_mgr._.SaveFilBfr(cfg_url, save_bfr);
	}
	private void Save_itm(Bry_bfr save_bfr, String key, int val) {
		String fmt = "{0}('{1}');\n";
		String str = String_.Format(fmt, key, val);
		save_bfr.Add_str(str);
	}
}
class Xob_rate_mgr {
	private long time_bgn;
	private int item_len;
	private Bry_bfr save_bfr = Bry_bfr.reset_(255);
	public int Reset_interval() {return reset_interval;} public Xob_rate_mgr Reset_interval_(int v) {reset_interval = v; return this;} private int reset_interval = 10000;
	public Io_url Log_file() {return log_file;} public Xob_rate_mgr Log_file_(Io_url v) {log_file = v; return this;} private Io_url log_file;
	public void Init() {time_bgn = Env_.TickCount();}
	public void Increment() {
		++item_len;
		if (item_len % reset_interval == 0) {
			long time_end = Env_.TickCount();
			Save(item_len, time_bgn, time_end);
			time_bgn = time_end;
			item_len = 0;
		}
	}
	private void Save(int count, long bgn, long end) {
		int dif = (int)(end - bgn) / 1000;
		DecimalAdp rate = DecimalAdp_.divide_safe_(count, dif);
		save_bfr
			.Add_str(rate.Xto_str("#,##0.000")).Add_byte_pipe()
			.Add_int_variable(count).Add_byte_pipe()
			.Add_int_variable(dif).Add_byte_nl()
			;
		Io_mgr._.AppendFilByt(log_file, save_bfr.Xto_bry_and_clear());
	}
	public String Rate_as_str() {return Int_.Xto_str(Rate());}
	public int Rate() {
		int elapsed = Env_.TickCount_elapsed_in_sec(time_bgn);
		return Math_.Div_safe_as_int(item_len, elapsed);
	}
}
class Xob_dump_bmk {
	public int Ns_id() {return ns_id;} public Xob_dump_bmk Ns_id_(int v) {ns_id = v; return this;} private int ns_id;
	public int Db_id() {return db_id;} public Xob_dump_bmk Db_id_(int v) {db_id = v; return this;} private int db_id;
	public int Pg_id() {return pg_id;} public Xob_dump_bmk Pg_id_(int v) {pg_id = v; return this;} private int pg_id;
}

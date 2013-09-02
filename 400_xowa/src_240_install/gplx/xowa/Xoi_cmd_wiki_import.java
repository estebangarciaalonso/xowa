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
package gplx.xowa; import gplx.*;
import gplx.thread_cmds.*;
class Xoi_cmd_wiki_import implements Gfo_thread_cmd {
	public Xoi_cmd_wiki_import(Xoi_setup_mgr install_mgr, String wiki_key, String wiki_date, String dump_type) {this.install_mgr = install_mgr; this.Owner_(install_mgr); this.wiki_key = wiki_key; this.wiki_date = wiki_date; this.dump_type = dump_type;} private Xoi_setup_mgr install_mgr; String wiki_key, wiki_date, dump_type;
	public static final String KEY = "wiki.import";
	public void Cmd_init() {}
	public String Async_key() {return KEY;}
	public int Async_sleep_interval()	{return Gfo_thread_cmd_.Async_sleep_interval_1_second;}
	public boolean Async_prog_enabled()	{return false;}
	public void Async_prog_run(int async_sleep_sum) {}
	public byte Async_init() {return Gfo_thread_cmd_.Init_ok;}
	public boolean Async_term() {
		install_mgr.App().Gui_wtr().Log_many(GRP_KEY, "import.end", "import.end ~{0} ~{1} ~{2}", wiki_key, wiki_date, dump_type);
		return true;
	}
	public GfoInvkAble Owner() {return owner;} public Xoi_cmd_wiki_import Owner_(GfoInvkAble v) {owner = v; return this;} GfoInvkAble owner;
	public Gfo_thread_cmd Async_next_cmd() {return next_cmd;} public void Async_next_cmd_(Gfo_thread_cmd v) {next_cmd = v;} Gfo_thread_cmd next_cmd;
	public void Async_run() {
		running = true;
		install_mgr.App().Gui_wtr().Log_many(GRP_KEY, "import.bgn", "import.bgn ~{0} ~{1} ~{2}", wiki_key, wiki_date, dump_type);
		ThreadAdp_.invk_(this, Invk_process_async).Start();			
	}
	public boolean Async_running() {
		return running;
	}
	boolean running;
	public boolean Import_move_bz2_to_done() {return import_move_bz2_to_done;} public Xoi_cmd_wiki_import Import_move_bz2_to_done_(boolean v) {import_move_bz2_to_done = v; return this;} private boolean import_move_bz2_to_done = true;
	void Process_txt(Xob_bldr bldr) {
		((gplx.xowa.bldrs.imports.Xobc_core_cleanup)bldr.Cmd_mgr().Add_cmd(wiki, "core.cleanup")).Delete_wiki_(true).Delete_sqlite3_(true);
		bldr.Cmd_mgr().Add_cmd(wiki, "core.init");
		bldr.Cmd_mgr().Add_cmd(wiki, "core.make_page");
		bldr.Cmd_mgr().Add_cmd(wiki, "core.make_id");
		bldr.Cmd_mgr().Add_cmd(wiki, "core.make_search_title");
		if (wiki.Bldr_props().Category_version() == gplx.xowa.ctgs.Xoa_ctg_mgr.Version_1)
			bldr.Cmd_mgr().Add_cmd(wiki, "core.make_category");
		bldr.Cmd_mgr().Add_cmd(wiki, "core.calc_stats");	
		bldr.Cmd_mgr().Add_cmd(wiki, "core.term");
	}	
	void Process_sql(Xob_bldr bldr) {
		((gplx.xowa.bldrs.imports.Xobc_core_cleanup)bldr.Cmd_mgr().Add_cmd(wiki, "core.cleanup")).Delete_wiki_(true).Delete_sqlite3_(true);
		bldr.Cmd_mgr().Add_cmd(wiki, "import.sql.init");
		bldr.Cmd_mgr().Add_cmd(wiki, "import.sql.page");
		if (wiki.Bldr_props().Category_version() == gplx.xowa.ctgs.Xoa_ctg_mgr.Version_1) {
			bldr.Cmd_mgr().Add_cmd(wiki, "import.sql.category_v1");
			bldr.Cmd_mgr().Add_cmd(wiki, "import.sql.category_registry");
			bldr.Cmd_mgr().Add_cmd(wiki, "import.sql.categorylinks");
		}
//			if (wiki.Bldr_props().Category_version() == gplx.xowa.ctgs.Xoa_ctg_mgr.Version_2)
//				bldr.Cmd_mgr().Add_cmd(wiki, "import.sql.hiddencat");
		bldr.Cmd_mgr().Add_cmd(wiki, "import.sql.term");	
	}	
	void Process_async() {
		Xoa_app app = install_mgr.App();
		app.Usr_dlg().Prog_one("", "", "preparing import: ~{0}", wiki_key);
		Xob_bldr bldr = app.Bldr();
		wiki = app.Wiki_mgr().Get_by_key_or_make(ByteAry_.new_ascii_(wiki_key));
		wiki.Init_assert();
		bldr.Cmd_mgr().Clear();
		bldr.Pause_at_end_(false);
		Io_url src_url = wiki.Bldr_props().Src_rdr().Src_url();
		if (install_mgr.Dump_mgr().Wiki_storage_type_is_sql())
			Process_sql(bldr);
		else
			Process_txt(bldr);
		bldr.Run();
		app.Gui_wtr().Prog_none(GRP_KEY, "clear", "");
		app.Gui_wtr().Note_none(GRP_KEY, "clear", "");
		app.User().Available_from_fsys();
		wiki.Init_needed_(true);
		wiki.Html_mgr().Output_mgr().Init_(true);
		wiki.Init_assert();
		if		(String_.Eq(src_url.Ext(), ".xml")) {
			if (app.Setup_mgr().Dump_mgr().Delete_xml_file())
				Io_mgr._.DeleteFil(src_url);
		}
		else if (String_.Eq(src_url.Ext(), ".bz2")) {
			Io_url trg_fil = app.Fsys_mgr().Wiki_dir().GenSubFil_nest("#dump", "done", src_url.NameAndExt());
			if (import_move_bz2_to_done)
				Io_mgr._.MoveFil_args(src_url, trg_fil, true).Exec();
		}
		running = false;
		wiki.Bldr_props().Src_fil_xml_(null).Src_fil_bz2_(null);	// reset file else error when going from Import/Script to Import/List
		app.Gui_mgr().Kit().New_cmd_sync(this).Invk(GfsCtx.new_(), 0, Invk_open_wiki, GfoMsg_.Null);
	}	Xow_wiki wiki;
	void Open_wiki(String wiki_key) {
		Xog_win main_win = install_mgr.App().Gui_mgr().Main_win();
		if (main_win.Page() == null) return; // will be null when invoked through cmd-line
		byte[] url = ByteAry_.Add(wiki.Key_bry(), Xoh_href_parser.Href_wiki_bry, wiki.Props().Main_page());
		main_win.Exec_url_exec(String_.new_utf8_(url));
	}	
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_process_async))			Process_async();
		else if	(ctx.Match(k, Invk_owner))					return owner;
		else if	(ctx.Match(k, Invk_open_wiki))				Open_wiki(wiki_key);
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_process_async = "run_async", Invk_owner = "owner", Invk_open_wiki = "open_wiki";
	static final String GRP_KEY = "xowa.thread.op.build";
}

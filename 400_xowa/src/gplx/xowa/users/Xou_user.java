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
package gplx.xowa.users; import gplx.*; import gplx.xowa.*;
import gplx.xowa.wikis.*; import gplx.xowa.wikis.xwikis.*; import gplx.xowa.users.dbs.*; import gplx.xowa.users.history.*; import gplx.xowa.xtns.scribunto.*;
import gplx.xowa.users.data.*;
public class Xou_user implements GfoEvMgrOwner, GfoInvkAble {
	public Xou_user(Xoa_app app, Io_url user_dir) {
		this.evMgr = GfoEvMgr.new_(this);
		this.app = app; this.key_str = user_dir.NameOnly(); key_bry = Bry_.new_utf8_(key_str);
		fsys_mgr = new Xou_fsys_mgr(app, this, user_dir);
		prefs_mgr = new gplx.xowa.users.prefs.Prefs_mgr(app);
		cfg_mgr = new Xou_cfg(this);
		session_mgr = new Xou_session(this);
		history_mgr = new Xou_history_mgr(fsys_mgr.App_data_history_fil());
		db_mgr = new Xou_db_mgr(app);
	}
	public Xoa_app App() {return app;} private Xoa_app app;
	public String Key_str() {return key_str;} private String key_str;
	public byte[] Key_bry() {return key_bry;} private byte[] key_bry;
	public void Key_str_(String v) {this.key_str = v; this.key_bry = Bry_.new_utf8_(v);}
	public GfoEvMgr EvMgr() {return evMgr;} private final GfoEvMgr evMgr;
	public Xoud_data_mgr Data_mgr() {return data_mgr;} private Xoud_data_mgr data_mgr = new Xoud_data_mgr();
	public Xol_lang Lang() {if (lang == null) {lang = app.Lang_mgr().Get_by_key_or_new(app.Sys_cfg().Lang()); lang.Init_by_load();} return lang;} private Xol_lang lang;		
	public void Lang_(Xol_lang v) {
		lang = v;
		this.Msg_mgr().Lang_(v);
		wiki.Msg_mgr().Clear();	// clear home wiki msgs whenever lang changes; else messages cached from old lang will not be replaced; EX:Read/Edit; DATE:2014-05-26
		GfoEvMgr_.PubVal(this, Evt_lang_changed, lang);
	}
	public Xou_fsys_mgr Fsys_mgr() {return fsys_mgr;} private Xou_fsys_mgr fsys_mgr;
	public Xow_wiki Wiki() {if (wiki == null) wiki = Xou_user_.new_or_create_(this, app); return wiki;} private Xow_wiki wiki;
	public Xou_history_mgr History_mgr() {return history_mgr;} private Xou_history_mgr history_mgr;
	public Xou_cfg Cfg_mgr() {return cfg_mgr;} private Xou_cfg cfg_mgr;
	public Xou_session Session_mgr() {return session_mgr;} private Xou_session session_mgr;
	public Xou_db_mgr Db_mgr() {return db_mgr;} private Xou_db_mgr db_mgr;
	public gplx.xowa.users.prefs.Prefs_mgr Prefs_mgr() {return prefs_mgr;} gplx.xowa.users.prefs.Prefs_mgr prefs_mgr;
	public Xow_msg_mgr Msg_mgr() {
		if (msg_mgr == null)
			msg_mgr = new Xow_msg_mgr(this.Wiki(), this.Lang());	// NOTE: must call this.Lang() not this.lang, else nullRef exception when using "app.shell.fetch_page"; DATE:2013-04-12
		return msg_mgr;
	}	private Xow_msg_mgr msg_mgr;
	public void Init_by_app() {
		Io_url user_system_cfg = fsys_mgr.App_data_cfg_dir().GenSubFil(Xou_fsys_mgr.Name_user_system_cfg);
		if (!Io_mgr._.ExistsFil(user_system_cfg)) Xou_user_.User_system_cfg_make(app.Usr_dlg(), user_system_cfg);
		if (!Env_.Mode_testing()) {
			db_mgr.App_init();
			this.Available_from_fsys();
			// data_mgr.Init_by_app(app);
		}
	}
	public void App_term() {
		session_mgr.Window_mgr().Save_window(app.Gui_mgr().Browser_win().Win_box());
		history_mgr.Save(app);
		if (app.Gui_mgr().Browser_win().Tab_mgr().Html_load_tid__url())
			Io_mgr._.DeleteDirDeep(fsys_mgr.App_temp_html_dir());
		db_mgr.App_term();
	}
	public void Bookmarks_add(byte[] wiki_domain, byte[] ttl_full_txt) {
		Bry_bfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_k004();
		bookmarks_add_fmtr.Bld_bfr_many(tmp_bfr, wiki_domain, ttl_full_txt);
		byte[] new_entry = tmp_bfr.Mkr_rls().Xto_bry_and_clear();
		Xoa_ttl bookmarks_ttl = Xoa_ttl.parse_(wiki, Bry_data_bookmarks);
		Xoa_page bookmarks_page = wiki.Data_mgr().Get_page(bookmarks_ttl, false);
		byte[] new_data = Bry_.Add(bookmarks_page.Data_raw(), new_entry);
		wiki.Db_mgr().Save_mgr().Data_update(bookmarks_page, new_data);
	}	private Bry_fmtr bookmarks_add_fmtr = Bry_fmtr.new_("* [[~{wiki_key}:~{page_name}]]\n", "wiki_key", "page_name"); byte[] Bry_data_bookmarks = Bry_.new_utf8_("Data:Bookmarks");
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {			
		if		(ctx.Match(k, Invk_available_from_bulk))		Available_from_bulk(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_available_from_fsys))		Available_from_fsys();
		else if	(ctx.Match(k, Invk_msgs))						return this.Msg_mgr();
		else if	(ctx.Match(k, Invk_lang))						return lang;
		else if	(ctx.Match(k, Invk_bookmarks_add_fmt_))			bookmarks_add_fmtr.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_name))						return key_str;
		else if	(ctx.Match(k, Invk_wiki))						return this.Wiki();	// NOTE: mass parse relies on this being this.Wiki(), not wiki
		else if	(ctx.Match(k, Invk_history))					return history_mgr;
		else if	(ctx.Match(k, Invk_fsys))						return fsys_mgr;
		else if	(ctx.Match(k, Invk_prefs))						return prefs_mgr;
		else if	(ctx.Match(k, Invk_cfg))						return cfg_mgr;
		else if	(ctx.Match(k, Invk_session))					return session_mgr;
		else return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_available_from_fsys = "available_from_fsys", Invk_available_from_bulk = "available_from_bulk", Invk_bookmarks_add_fmt_ = "bookmarks_add_fmt_"
		, Invk_name = "name", Invk_wiki = "wiki", Invk_history = "history", Invk_fsys = "fsys", Invk_lang = "lang", Invk_msgs = "msgs", Invk_prefs = "prefs", Invk_cfg = "cfg", Invk_session = "session";
	public static final String Key_xowa_user = "anonymous";
	public static final String Evt_lang_changed = "lang_changed";
	public void Available_from_fsys() {
		Io_url bookmarks_dir = fsys_mgr.Home_wiki_dir().GenSubDir_nest("wiki", "home", "ns", "730");	// NOTE: putting bookmark check here (instead of at init) b/c Init runs before xowa.gfs, and Bookmarks needs xowa.gfs to run first
		if (!Io_mgr._.ExistsDir(bookmarks_dir)) Xou_user_.Bookmarks_make(app, this.Wiki());

		Io_url[] dirs = Io_mgr._.QueryDir_args(app.Fsys_mgr().Wiki_dir()).Recur_(false).DirOnly_().ExecAsUrlAry();
		Xow_wiki usr_wiki = Wiki();
		int dirs_len = dirs.length;
		for (int i = 0; i < dirs_len; i++) {
			Io_url dir = dirs[i];
			String name = dir.NameOnly();
			if (String_.Eq(name, gplx.xowa.bldrs.imports.Xobc_core_batch.Dir_dump)
//					|| !Io_mgr._.ExistsDir(dir.GenSubFil_nest("ns"))
				) continue;
			byte[] dir_name_as_bry = Bry_.new_utf8_(name);
			Xow_xwiki_itm xwiki = Available_add(usr_wiki, dir_name_as_bry);
			if (xwiki != null)			// Add_full can return null if adding invalid lang; should not apply here, but guard against null ref
				xwiki.Offline_(true);	// mark xwiki as offline; needed for available wikis sidebar; DATE:2014-09-21
			app.Setup_mgr().Maint_mgr().Wiki_mgr().Add(dir_name_as_bry);
		}
	}
	private void Available_from_bulk(byte[] raw) {
		byte[][] wikis = Bry_.Split(raw, Byte_ascii.NewLine);
		Xow_wiki usr_wiki = Wiki();
		int wikis_len = wikis.length;
		for (int i = 0; i < wikis_len; i++)
			Available_add(usr_wiki, wikis[i]);
	}
	private Xow_xwiki_itm Available_add(Xow_wiki usr_wiki, byte[] wiki_name) {
		return usr_wiki.Xwiki_mgr().Add_full(wiki_name, wiki_name);
	}
}

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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.xtns.scribunto.lib.*;
import gplx.xowa.xtns.scribunto.engines.*;
public class Scrib_core {
	private Hash_adp_bry mods = Hash_adp_bry.cs_();
	public Scrib_core(Xoa_app app, Xop_ctx ctx) {// NOTE: ctx needed for language reg
		this.app = app; this.ctx = ctx;
		this.wiki = ctx.Wiki(); this.page = ctx.Page();	// NOTE: wiki / page needed for title reg; DATE:2014-02-05
		this.Engine_(Scrib_engine_type.Type_lua);	// TEST: default to lua
		fsys_mgr.Root_dir_(app.Fsys_mgr().Root_dir().GenSubDir_nest("bin", "any", "lua", "mediawiki", "extensions", "Scribunto"));
		lib_mw = new Scrib_lib_mw(this);
		lib_uri = new Scrib_lib_uri(this); 
		lib_ustring = new Scrib_lib_ustring(this);
		lib_language = new Scrib_lib_language(this);
 		lib_site = new Scrib_lib_site(this);
 		lib_title = new Scrib_lib_title(this);
 		lib_message = new Scrib_lib_message(this);
 		lib_text = new Scrib_lib_text(this);
		lib_wikibase = new Scrib_lib_wikibase(this);
		lib_wikibase_entity = new Scrib_lib_wikibase_entity(this);
	}
	public Xoa_app App() {return app;} private Xoa_app app;
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	@gplx.Internal protected void Wiki_(Xow_wiki v) {this.wiki = v;} // TEST:
	public Xoa_page Page() {return page;} private Xoa_page page;
	public boolean Enabled() {return enabled;} private boolean enabled = true;
	private void Engine_(byte cmd) {
		if		(cmd == Scrib_engine_type.Type_lua)
			engine = new gplx.xowa.xtns.scribunto.engines.process.Process_engine(app, this);
		else if (cmd == Scrib_engine_type.Type_luaj)
			engine = new gplx.xowa.xtns.scribunto.engines.luaj.Luaj_engine(this);
	}
	public Scrib_fsys_mgr Fsys_mgr() {return fsys_mgr;} private Scrib_fsys_mgr fsys_mgr = new Scrib_fsys_mgr();
	public Scrib_proc_mgr Proc_mgr() {return proc_mgr;} private Scrib_proc_mgr proc_mgr = new Scrib_proc_mgr();
	public Scrib_engine Interpreter() {return engine;} private Scrib_engine engine;
	public Scrib_lib_mw Lib_mw() {return lib_mw;} private Scrib_lib_mw lib_mw;
	public Scrib_lib_uri Lib_uri() {return lib_uri;} private Scrib_lib_uri lib_uri;
	public Scrib_lib_ustring Lib_ustring() {return lib_ustring;} private Scrib_lib_ustring lib_ustring;
	public Scrib_lib_language Lib_language() {return lib_language;} private Scrib_lib_language lib_language;
	public Scrib_lib_site Lib_site() {return lib_site;} private Scrib_lib_site lib_site;
	public Scrib_lib_title Lib_title() {return lib_title;} private Scrib_lib_title lib_title;
	public Scrib_lib_message Lib_message() {return lib_message;} private Scrib_lib_message lib_message;
	public Scrib_lib_text Lib_text() {return lib_text;} private Scrib_lib_text lib_text;
	public Scrib_lib_wikibase Lib_wikibase() {return lib_wikibase;} private Scrib_lib_wikibase lib_wikibase;
	public Scrib_lib_wikibase_entity Lib_wikibase_entity() {return lib_wikibase_entity;} private Scrib_lib_wikibase_entity lib_wikibase_entity;
	public Scrib_core Init() {	// REF:LuaCommon.php!Load
		Scrib_xtn_mgr opts = (Scrib_xtn_mgr)app.Xtn_mgr().Get_or_fail(Scrib_xtn_mgr.XTN_KEY);
		Engine_(opts.Lua_engine_type());
		engine.Server().Server_timeout_(opts.Lua_timeout()).Server_timeout_polling_(opts.Lua_timeout_polling()).Server_timeout_busy_wait_(opts.Lua_timeout_busy_wait());
		enabled = opts.Enabled();
		Io_url root_dir = fsys_mgr.Root_dir(), script_dir = fsys_mgr.Script_dir();
		engine.Server().Init
		(	app.Fsys_mgr().App_mgr().App_lua().Exe_url().Raw()
		,	root_dir.GenSubFil_nest("engines", "LuaStandalone", "mw_main.lua").Raw()
		,	root_dir.Raw()
		);
		Init_register(script_dir, lib_mw, lib_uri, lib_ustring, lib_language, lib_site, lib_title, lib_text, lib_message, lib_wikibase, lib_wikibase_entity);
		return this;
	}
	private void Init_register(Io_url script_dir, Scrib_lib... ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++)
			ary[i].Register(this, script_dir);
	}
	public void Term() {engine.Server().Term();}
	public void When_page_changed(Xoa_page page) {
		mods.Clear();	// clear any loaded modules
		Xow_wiki wiki = page.Wiki();
		this.page = page;
		byte[] new_wiki = wiki.Domain_bry();
		if (!ByteAry_.Eq(cur_wiki, new_wiki)) {
			cur_wiki = new_wiki;
			lib_site.Notify_wiki_changed();
			lib_text.Notify_wiki_changed();
		}
		byte[] new_lang = wiki.Lang().Key_bry();
		if (!ByteAry_.Eq(cur_lang, new_lang)) {
			cur_lang = new_lang;
			lib_message.Notify_lang_changed();
			lib_language.Notify_lang_changed();
		}
		lib_uri.Notify_page_changed();
		lib_title.Notify_page_changed();
	}	byte[] cur_wiki = ByteAry_.Empty; 
	public byte[] Cur_lang() {return cur_lang;} private byte[] cur_lang = ByteAry_.Empty;
	public Scrib_lua_mod RegisterInterface(Scrib_lib lib, Io_url url, KeyVal... args) {
		this.RegisterLibrary(lib.Procs());
		Scrib_lua_mod rv = this.LoadLibraryFromFile(url.NameAndExt(), Io_mgr._.LoadFilStr(url));
		Scrib_lua_proc setupInterface_func = rv.Fncs_get_by_key("setupInterface");
		if (setupInterface_func != null)
			engine.CallFunction(setupInterface_func.Id(), Scrib_kv_utl_.base1_obj_(args));
		return rv;
	}
	public void RegisterLibrary(Scrib_proc_mgr lib_proc_mgr) {
		int len = lib_proc_mgr.Len();
		KeyVal[] functions_ary = new KeyVal[len];
		for (int i = 0; i < len; i++) {
			Scrib_proc lib_proc = lib_proc_mgr.Get_at(i);
			String lib_proc_key = lib_proc.Proc_key();
			this.proc_mgr.Set(lib_proc_key, lib_proc);
			functions_ary[i] = KeyVal_.new_(lib_proc.Proc_name(), lib_proc_key);
		}
		engine.RegisterLibrary(functions_ary);
	}
	@gplx.Internal protected Scrib_lua_mod LoadLibraryFromFile(String name, String text) {
		int lib_id = engine.LoadString("@" + name, text).Id();	// NOTE: 'Prepending an "@" to the chunk name makes Lua think it is a filename'
		KeyVal[] values = engine.CallFunction(lib_id, KeyVal_.Ary_empty);
		Scrib_lua_mod rv = new Scrib_lua_mod(this, name);
		if (values.length > 0) {	// NOTE: values.length == 0 for "package.lua" (no fnc_ids returned);
			KeyVal[] fncs = Scrib_kv_utl_.Val_to_KeyVal_ary(values, 0);
			int len = fncs.length;
			for (int i = 0; i < len; i++) {
				KeyVal itm = fncs[i];
				Scrib_lua_proc fnc = Scrib_lua_proc.cast_or_null_(itm.Val());
				if (fnc != null) rv.Fncs_add(fnc);	// NOTE: some lua funcs will return INF; EX: stringLengthLimit
			}
		}
		return rv;
	}
	public Xot_invk Cur_frame_invoke() {return cur_frame_invoke;} public Scrib_core Cur_frame_invoke_(Xot_invk v) {cur_frame_invoke = v; return this;} private Xot_invk cur_frame_invoke;
	public Xot_invk Cur_frame_owner() {return cur_frame_owner;} public Scrib_core Cur_frame_owner_(Xot_invk v) {cur_frame_owner = v; return this;} private Xot_invk cur_frame_owner;
	public Xop_ctx Ctx() {return ctx;} private Xop_ctx ctx;
	public KeyVal[] ExecuteFunctionChunk(Scrib_lua_proc fnc, Xot_invk frame_invoke) {
		Xot_invk old_frame_invoke = cur_frame_invoke;
		cur_frame_invoke = frame_invoke;
		KeyVal[] rv = engine.CallFunction(lib_mw.Mod().Fncs_get_id("executeFunction"), Scrib_kv_utl_.base1_obj_(fnc));
		cur_frame_invoke = old_frame_invoke;
		return rv;
	}
	public void Invoke(Xow_wiki wiki, Xop_ctx ctx, byte[] src, Xot_invk owner_frame, Xot_invk invoke_frame, ByteAryBfr bfr, byte[] mod_name, byte[] mod_text, byte[] fnc_name) {
		cur_frame_owner = owner_frame; this.wiki = wiki; this.ctx = ctx;
		lib_mw.Invoke_bgn(wiki, ctx, src);
		try {
			Scrib_lua_mod mod = Mods_get_or_new(mod_name, mod_text);
			KeyVal[] values = ExecuteFunctionChunk(mod.Fncs_get_by_key(String_.new_utf8_(fnc_name)), invoke_frame);
			String rslt = Scrib_kv_utl_.Val_to_str(values, 0);	// NOTE: expects a values with 1 scalar value
			bfr.Add_str(rslt);
		}
		finally {lib_mw.Invoke_end();}
	}
	public Scrib_lua_mod Mods_get(byte[] mod_name) {return (Scrib_lua_mod)mods.Fetch(mod_name);}
	private Scrib_lua_mod Mods_get_or_new(byte[] mod_name, byte[] mod_text) {
		Scrib_lua_mod rv = (Scrib_lua_mod)mods.Fetch(mod_name);
		if (rv == null) {
			rv = new Scrib_lua_mod(this, String_.new_utf8_(mod_name));
			rv.LoadString(String_.new_utf8_(mod_text));
			mods.Add(mod_name, rv);
		}
		rv.Execute();
		return rv;
	}
	public static Scrib_core Core() {return core;} public static Scrib_core Core_new_(Xoa_app app, Xop_ctx ctx) {core = new Scrib_core(app, ctx); return core;} private static Scrib_core core;
	public static void Core_page_changed(Xoa_page page) {
		if (core != null)
			core.When_page_changed(page);
	}
	public static void Core_invalidate() {if (core != null) core.Term(); core = null;}
	public static final String Frame_key_current = "current", Frame_key_parent = "parent";
	public static final int Base_1 = 1;
	public static final String Key_mw_interface = "mw_interface";
}

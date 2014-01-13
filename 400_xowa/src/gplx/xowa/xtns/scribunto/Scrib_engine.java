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
public class Scrib_engine {
	public Scrib_engine(Xoa_app app, Xop_ctx ctx) {// NOTE: ctx needed for language reg
		this.app = app; this.ctx = ctx; interpreter = new Scrib_interpreter(app, this);
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
	}
	public Xoa_app App() {return app;} private Xoa_app app;
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	@gplx.Internal protected void Wiki_(Xow_wiki v) {this.wiki = v;} // TEST:
	public boolean Enabled() {return enabled;} private boolean enabled = true;
	@gplx.Internal protected Scrib_fsys_mgr Fsys_mgr() {return fsys_mgr;} Scrib_fsys_mgr fsys_mgr = new Scrib_fsys_mgr();
	@gplx.Internal protected Scrib_interpreter Interpreter() {return interpreter;} Scrib_interpreter interpreter;
	@gplx.Internal protected Scrib_lib_mw Lib_mw() {return lib_mw;} Scrib_lib_mw lib_mw;
	@gplx.Internal protected Scrib_lib_uri Lib_uri() {return lib_uri;} Scrib_lib_uri lib_uri;
	@gplx.Internal protected Scrib_lib_ustring Lib_ustring() {return lib_ustring;} Scrib_lib_ustring lib_ustring;
	@gplx.Internal protected Scrib_lib_language Lib_language() {return lib_language;} Scrib_lib_language lib_language;
	@gplx.Internal protected Scrib_lib_site Lib_site() {return lib_site;} Scrib_lib_site lib_site;
	@gplx.Internal protected Scrib_lib_title Lib_title() {return lib_title;} Scrib_lib_title lib_title;
	@gplx.Internal protected Scrib_lib_message Lib_message() {return lib_message;} Scrib_lib_message lib_message;
	@gplx.Internal protected Scrib_lib_wikibase Lib_wikibase() {return lib_wikibase;} Scrib_lib_wikibase lib_wikibase;
	@gplx.Internal protected Scrib_lib_text Lib_text() {return lib_text;} Scrib_lib_text lib_text;
	public Scrib_engine Init() {	// REF:LuaCommon.php!Load
		Xow_xtn_scribunto opts = (Xow_xtn_scribunto)app.Xtn_mgr().Get_or_fail(Xow_xtn_scribunto.XTN_KEY);
		interpreter.Server().Server_timeout_(opts.Lua_timeout()).Server_timeout_polling_(opts.Lua_timeout_polling()).Server_timeout_busy_wait_(opts.Lua_timeout_busy_wait());
		enabled = opts.Enabled();
		Io_url root_dir = fsys_mgr.Root_dir(), script_dir = fsys_mgr.Script_dir();
		interpreter.Server().Init
		(	app.Fsys_mgr().App_mgr().App_lua().Exe_url().Raw()
		,	root_dir.GenSubFil_nest("engines", "LuaStandalone", "mw_main.lua").Raw()
		,	root_dir.Raw()
		);
		Init_register(script_dir, lib_mw, lib_uri, lib_ustring, lib_language, lib_site, lib_title, lib_text, lib_message, lib_wikibase);
		return this;
	}
	private void Init_register(Io_url script_dir, Scrib_lib... ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++)
			ary[i].Register(this, script_dir);
	}
	public void Term() {interpreter.Server().Term();}
	public void When_page_changed(Xoa_page page) {
		mods.Clear();	// clear any loaded modules
		Xow_wiki wiki = page.Wiki();
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
	@gplx.Internal protected Scrib_mod RegisterInterface(Io_url url, GfoInvkAble invk, String[] func_names, KeyVal... args) {
		interpreter.RegisterLibrary(Scrib_engine.Key_mw_interface, invk, func_names);
		Scrib_mod rv = this.LoadLibraryFromFile(url.NameAndExt(), Io_mgr._.LoadFilStr(url));
		Scrib_fnc setupInterface_func = rv.Fncs_get_by_key("setupInterface");
		if (setupInterface_func != null) interpreter.CallFunction(setupInterface_func.Id(), Scrib_kv_utl.base1_obj_(args));
		return rv;
	}
	@gplx.Internal protected Scrib_mod LoadLibraryFromFile(String name, String text) {
		int lib_id = interpreter.LoadString("@" + name, text).Id();	// NOTE: 'Prepending an "@" to the chunk name makes Lua think it is a filename'
		KeyVal[] values = interpreter.CallFunction(lib_id, KeyVal_.Ary_empty);
		Scrib_mod rv = new Scrib_mod(this, name);
		if (values.length > 0) {	// NOTE: values.length == 0 for "package.lua" (no fnc_ids returned);
			KeyVal[] fncs = Scrib_kv_utl.Val_to_KeyVal_ary(values, 0);
			int len = fncs.length;
			for (int i = 0; i < len; i++) {
				KeyVal itm = fncs[i];
				Scrib_fnc fnc = Scrib_fnc.cast_or_null_(itm.Val());
				if (fnc != null) rv.Fncs_add(fnc);	// NOTE: some lua funcs will return INF; EX: stringLengthLimit
			}
		}
		return rv;
	}
	public Xot_invk Cur_frame_invoke() {return cur_frame_invoke;} public Scrib_engine Cur_frame_invoke_(Xot_invk v) {cur_frame_invoke = v; return this;} private Xot_invk cur_frame_invoke;
	public Xot_invk Cur_frame_owner() {return cur_frame_owner;} public Scrib_engine Cur_frame_owner_(Xot_invk v) {cur_frame_owner = v; return this;} private Xot_invk cur_frame_owner;
	public Xop_ctx Ctx() {return ctx;} private Xop_ctx ctx;
	public KeyVal[] ExecuteFunctionChunk(Scrib_fnc fnc, Xot_invk frame_invoke) {
		Xot_invk old_frame_invoke = cur_frame_invoke;
		cur_frame_invoke = frame_invoke;
		KeyVal[] rv = interpreter.CallFunction(lib_mw.Mod().Fncs_get_id("executeFunction"), Scrib_kv_utl.base1_obj_(fnc));
		cur_frame_invoke = old_frame_invoke;
		return rv;
	}
	public void Invoke(Xow_wiki wiki, Xop_ctx ctx, byte[] src, Xot_invk owner_frame, Xot_invk invoke_frame, ByteAryBfr bfr, byte[] mod_name, byte[] mod_text, byte[] fnc_name) {
		cur_frame_owner = owner_frame; this.wiki = wiki; this.ctx = ctx;
		lib_mw.Invoke_bgn(wiki, ctx, src);
		try {
			Scrib_mod mod = Mods_get_or_new(mod_name, mod_text);
			KeyVal[] values = ExecuteFunctionChunk(mod.Fncs_get_by_key(String_.new_utf8_(fnc_name)), invoke_frame);
			String rslt = Scrib_kv_utl.Val_to_str(values, 0);	// NOTE: expects a values with 1 scalar value
			bfr.Add_str(rslt);
		}
		finally {lib_mw.Invoke_end();}
	}
	public Scrib_mod Mods_get(byte[] mod_name) {return (Scrib_mod)mods.Fetch(mod_name);}
	Scrib_mod Mods_get_or_new(byte[] mod_name, byte[] mod_text) {
		Scrib_mod rv = (Scrib_mod)mods.Fetch(mod_name);
		if (rv == null) {
			rv = new Scrib_mod(this, String_.new_utf8_(mod_name));
			rv.LoadString(String_.new_utf8_(mod_text));
			mods.Add(mod_name, rv);
		}
		rv.Execute();
		return rv;
	}	Hash_adp_bry mods = new Hash_adp_bry(true);
	public static Scrib_engine Engine() {return engine;} public static Scrib_engine Engine_new_(Xoa_app app, Xop_ctx ctx) {engine = new Scrib_engine(app, ctx); return engine;} static Scrib_engine engine;
	public static void Engine_page_changed(Xoa_page page) {
		if (engine != null)
			engine.When_page_changed(page);
	}
	public static void Engine_invalidate() {if (engine != null) engine.Term(); engine = null;}
	public static final String Frame_key_current = "current", Frame_key_parent = "parent", Key_mw_interface = "mw_interface";
}

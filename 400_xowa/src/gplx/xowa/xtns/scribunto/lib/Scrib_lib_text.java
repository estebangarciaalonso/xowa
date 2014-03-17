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
package gplx.xowa.xtns.scribunto.lib; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*;
public class Scrib_lib_text implements Scrib_lib {
	public Scrib_lib_text(Scrib_core core) {this.core = core;} Scrib_core core;
	public Scrib_lua_mod Mod() {return mod;} Scrib_lua_mod mod;
	public Scrib_lib Init() {procs.Init_by_lib(this, Proc_names); return this;}
	public Scrib_lua_mod Register(Scrib_core core, Io_url script_dir) {
		Init();
		mod = core.RegisterInterface(this, script_dir.GenSubFil("mw.text.lua"));
		notify_wiki_changed_fnc = mod.Fncs_get_by_key("notify_wiki_changed");
		return mod;
	}	Scrib_lua_proc notify_wiki_changed_fnc;
	public Scrib_proc_mgr Procs() {return procs;} private Scrib_proc_mgr procs = new Scrib_proc_mgr();
	public boolean Procs_exec(int key, Scrib_proc_args args, Scrib_proc_rslt rslt) {
		switch (key) {
			case Proc_unstrip:							return Unstrip(args, rslt);
			case Proc_getEntityTable:					return GetEntityTable(args, rslt);
			case Proc_init_text_for_wiki:				return Init_text_for_wiki(args, rslt);
			default: throw Err_.unhandled(key);
		}
	}
	private static final int Proc_unstrip = 0, Proc_getEntityTable = 1, Proc_init_text_for_wiki = 2;
	public static final String Invk_unstrip = "unstrip", Invk_getEntityTable = "getEntityTable", Invk_init_text_for_wiki = "init_text_for_wiki";
	private static final String[] Proc_names = String_.Ary(Invk_unstrip, Invk_getEntityTable, Invk_init_text_for_wiki);
	public boolean Unstrip(Scrib_proc_args args, Scrib_proc_rslt rslt) {return rslt.Init_obj(args.Pull_str(0));}
	public boolean GetEntityTable(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		if (Html_consts == null) Html_consts = Scrib_lib_text_html_entities.new_();
		return rslt.Init_obj(Html_consts);
	}	static KeyVal[] Html_consts;
	public void Notify_wiki_changed() {if (notify_wiki_changed_fnc != null) core.Interpreter().CallFunction(notify_wiki_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public boolean Init_text_for_wiki(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xow_msg_mgr msg_mgr = core.Wiki().Msg_mgr();
		KeyVal[] rv = new KeyVal[3];
		rv[0] = KeyVal_.new_("comma", Init_lib_text_get_msg(msg_mgr, "comma-separator"));
		rv[1] = KeyVal_.new_("and", Init_lib_text_get_msg(msg_mgr, "and") + Init_lib_text_get_msg(msg_mgr, "word-separator"));
		rv[2] = KeyVal_.new_("ellipsis", Init_lib_text_get_msg(msg_mgr, "ellipsis"));
		return rslt.Init_obj(rv);
	}
	String Init_lib_text_get_msg(Xow_msg_mgr msg_mgr, String msg_key) {
		return String_.new_utf8_(msg_mgr.Val_by_key_obj(ByteAry_.new_utf8_(msg_key)));
	}
}

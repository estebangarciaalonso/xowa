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
class Scrib_lib_text implements Scrib_lib {
	public Scrib_lib_text(Scrib_engine engine) {this.engine = engine;} Scrib_engine engine;
	public Scrib_mod Mod() {return mod;} Scrib_mod mod;
	public Scrib_mod Register(Scrib_engine engine, Io_url script_dir) {
		mod = engine.RegisterInterface(script_dir.GenSubFil("mw.text.lua"), this
			, String_.Ary(Invk_unstrip, Invk_getEntityTable, Invk_init_text_for_wiki)
			);
		notify_wiki_changed_fnc = mod.Fncs_get_by_key("notify_wiki_changed");
		return mod;
	}	Scrib_fnc notify_wiki_changed_fnc;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_unstrip))				return Unstrip((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_getEntityTable))			return GetEntityTable((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_init_text_for_wiki))		return Init_text_for_wiki();
		else	return GfoInvkAble_.Rv_unhandled;
	}
	public KeyVal[] Unstrip(KeyVal[] values) {return Scrib_kv_utl.base1_obj_(Scrib_kv_utl.Val_to_str(values, 0));}
	public KeyVal[] GetEntityTable(KeyVal[] values) {
		if (Html_entities == null) Html_entities = Scrib_lib_text_html_entities.new_();
		return Scrib_kv_utl.base1_obj_(Html_entities);
	}	static KeyVal[] Html_entities;
	public void Notify_wiki_changed() {if (notify_wiki_changed_fnc != null) engine.Interpreter().CallFunction(notify_wiki_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public KeyVal[] Init_text_for_wiki() {
		Xow_msg_mgr msg_mgr = engine.Wiki().Msg_mgr();
		KeyVal[] rv = new KeyVal[3];
		rv[0] = KeyVal_.new_("comma", Init_lib_text_get_msg(msg_mgr, "comma-separator"));
		rv[1] = KeyVal_.new_("and", Init_lib_text_get_msg(msg_mgr, "and") + Init_lib_text_get_msg(msg_mgr, "word-separator"));
		rv[2] = KeyVal_.new_("ellipsis", Init_lib_text_get_msg(msg_mgr, "ellipsis"));
		return Scrib_kv_utl.base1_obj_(rv);
	}
	String Init_lib_text_get_msg(Xow_msg_mgr msg_mgr, String msg_key) {
		return String_.new_utf8_(msg_mgr.Val_by_key_obj(ByteAry_.new_utf8_(msg_key)));
	}
	public static final String Invk_unstrip = "unstrip", Invk_getEntityTable = "getEntityTable", Invk_init_text_for_wiki = "init_text_for_wiki";
}

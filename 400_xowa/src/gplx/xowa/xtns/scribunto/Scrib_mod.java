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
public class Scrib_mod {
	public Scrib_mod(Scrib_engine engine, String name) {this.name = name; this.engine = engine;} Scrib_engine engine;
	public int Lua_id() {return lua_id;} private int lua_id = -1;
	public String Name() {return name;} private String name;
	public byte[] Text_bry() {return text_bry;} private byte[] text_bry;
	public void Fncs_clear() {hash.Clear();}
	public int Fncs_len() {return hash.Count();} OrderedHash hash = OrderedHash_.new_();
	public Scrib_fnc Fncs_get_at(int i) {return (Scrib_fnc)hash.FetchAt(i);}
	public Scrib_fnc Fncs_get_by_key(String key) {return (Scrib_fnc)hash.Fetch(key);}
	public void Fncs_add(Scrib_fnc prc) {hash.Add(prc.Key(), prc);}
	public int Fncs_get_id(String key) {
		Scrib_fnc fnc = Fncs_get_by_key(key); if (fnc == null) throw Err_.new_fmt_("Scrb_fnc does not exist: module={0} func={1}", name, key);
		return fnc.Id();
	}
	public Scrib_fnc LoadString(String text) {
		if (lua_id != -1) return load_string_fnc;
		text = String_.Replace(text, "&#09;", "\t");	// NOTE: this should only get called once per module
		text_bry = ByteAry_.new_utf8_(text);
		load_string_fnc = engine.Interpreter().LoadString("=" + name, text);	// MW: Scribunto: Prepending an "=" to the chunk name avoids truncation or a "[string" prefix
		lua_id = load_string_fnc.Id();
		return load_string_fnc;
	}
	Scrib_fnc load_string_fnc;
	public void Execute() {
		hash.Clear();
		this.LoadString(name);	// assert lua_id;
		KeyVal[] prcs_ary = engine.Interpreter().ExecuteModule(lua_id);
		prcs_ary = (KeyVal[])prcs_ary[0].Val();
		int prcs_len = prcs_ary.length;
		for (int i = 0; i < prcs_len; i++) {
			KeyVal prc_kv = prcs_ary[i];
			String prc_key = prc_kv.Key();
			Object prc_val = prc_kv.Val();
			Scrib_fnc fnc = null;
			if (ClassAdp_.ClassOf_obj(prc_val) == Scrib_fnc.class)
				fnc = (Scrib_fnc)prc_val;
			else
				fnc = new Scrib_fnc(prc_key, -1);
			Fncs_add(fnc);
		}
//			return exports[0].Val();
	}
}
class Scrib_cbk {
	public Scrib_cbk(GfoInvkAble invk, String lib, String prc) {this.invk = invk; this.lib = lib; this.prc = prc; this.key = lib + "-" + prc;} GfoInvkAble invk;
	public String Lib() {return lib;} private String lib;
	public String Prc() {return prc;} private String prc;
	public String Key() {return key;} private String key;
	public Object Invk(KeyVal[] values) {return GfoInvkAble_.InvkCmd_val(invk, prc, values);}
}

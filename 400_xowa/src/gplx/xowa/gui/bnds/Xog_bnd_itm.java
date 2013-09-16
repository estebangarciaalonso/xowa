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
package gplx.xowa.gui.bnds; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
import gplx.gfui.*;
class Xog_bnd_itm implements GfoInvkAble {
	public Xog_bnd_itm(String key) {this.key = key;}
	public String Key() {return key;} private String key;
	public String Bnd() {return bnd;} private String bnd;
	public String Name() {return name;} private String name;
	public IptArg[] Bnd_args() {return bnd_args;} private IptArg[] bnd_args;
	public String Cmd() {return cmd;} private String cmd;
	public String Owner() {return owner;} private String owner;
	private void Bnd_(String v) {
		bnd_args = parse_ary_or_null(v);
		if (bnd_args != null)
			bnd = v;
	}
	public static IptArg[] parse_ary_or_null(String v) {
		IptArg[] rv = IptArg_.parse_ary_(v);
		int len = rv.length;
		for (int i = 0; i < len; i++)
			if (rv[i] == null) return null;	// indicates failed parse
		return rv;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_key)) 			return key;
		else if	(ctx.Match(k, Invk_bnd)) 			return bnd;
		else if	(ctx.Match(k, Invk_bnd_)) 			Bnd_(m.ReadStr("v"));
		else if	(ctx.Match(k, Invk_cmd)) 			return cmd;
		else if	(ctx.Match(k, Invk_cmd_)) 			cmd = m.ReadStr("v");
		else if	(ctx.Match(k, Invk_bnd)) 			return bnd;
		else if	(ctx.Match(k, Invk_bnd)) 			bnd = m.ReadStr("v");
		else if	(ctx.Match(k, Invk_name)) 			return name;
		else if	(ctx.Match(k, Invk_name_)) 			name = m.ReadStr("v");
		else if	(ctx.Match(k, Invk_owner)) 			return owner;
		else if	(ctx.Match(k, Invk_owner_)) 		owner = m.ReadStr("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_key = "key", Invk_bnd = "bnd", Invk_bnd_ = "bnd_", Invk_cmd = "cmd", Invk_cmd_ = "cmd_", Invk_owner = "owner", Invk_owner_ = "owner_", Invk_name = "name", Invk_name_ = "name_";
}
class Xog_bnd_mgr implements GfoInvkAble {
	private OrderedHash hash = OrderedHash_.new_();
	private Xog_win win;
	public Xog_bnd_mgr(Xog_win win) {this.win = win;}
	public Xog_bnd_itm Get_or_new(String key) {
		Xog_bnd_itm rv = (Xog_bnd_itm)hash.Fetch(key);
		if (rv == null) {
			rv = new Xog_bnd_itm(key);
			hash.Add(key, rv);
		}
		return rv;
	}
	public void Exec(String key) {
		Xog_bnd_itm rv = (Xog_bnd_itm)hash.Fetch(key);
		GfuiElem owner = Get_owner(rv.Owner());		
		owner.IptBnds().Change(rv.Key(), rv.Bnd_args());
	}
	private GfuiElem Get_owner(String key) {
		String[] path = String_.Split(key, '.');
		int path_len = path.length;
		GfuiWin main_win = win.Win();
		if (path_len == 1) {
			if	(String_.Eq(key, "main_win")) return main_win;			
		}
		else if (path_len == 2) {
			GfuiElem rv = win.Win().SubElems().Fetch(key);
			if (rv != null) return rv;			
		}
		throw Err_.new_fmt_("unknown key: {0}", key);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_get)) 			return Get_or_new(m.ReadStr("v"));
		else if	(ctx.Match(k, Invk_exec)) 			Exec(m.ReadStr("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String Invk_get = "get", Invk_exec = "exec";
}

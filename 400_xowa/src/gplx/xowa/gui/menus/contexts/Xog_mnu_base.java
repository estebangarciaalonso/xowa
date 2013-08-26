/*
XOWA: the extensible offline wiki application
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
package gplx.xowa.gui.menus.contexts; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*; import gplx.xowa.gui.menus.*;
public class Xog_mnu_base implements GfoInvkAble {
	private OrderedHash hash = OrderedHash_.new_();
	public void Clear() {
		int len = hash.Count();
		for (int i = 0; i < len; i++) {
			Xog_mnu_itm itm = (Xog_mnu_itm)hash.FetchAt(i);
			itm.Clear();
		}
		hash.Clear();
	}
	public int Len() {return hash.Count();}
	public Xog_mnu_itm Get_at(int i) {return (Xog_mnu_itm)hash.FetchAt(i);}
	public Xog_mnu_itm Add_btn_default(String key) {return Add_itm_default(Xog_mnu_itm.Tid_btn, key);}
	public Xog_mnu_itm Add_grp_default(String key) {return Add_itm_default(Xog_mnu_itm.Tid_grp, key);}
	private Xog_mnu_itm Add_itm_default(byte tid, String key) {
		Xog_mnu_itm itm = (Xog_mnu_itm)Xog_mnu_defaults._.Get_or_null(key);
		if (itm == null)
			itm = new Xog_mnu_itm(tid, key).Init(key, "", "", "");
		hash.Add(key, itm);
		return itm;
	}
	public Xog_mnu_itm Add_btn(String key, String text, String shortcut, String img, String cmd)	{return Add_itm(Xog_mnu_itm.Tid_btn, key, text, shortcut, img, cmd);}
	public Xog_mnu_itm Add_grp(String key, String text, String shortcut, String img)				{return Add_itm(Xog_mnu_itm.Tid_grp, key, text, shortcut, img, "");}
	private Xog_mnu_itm Add_itm(byte tid, String key, String text, String shortcut, String img, String cmd) {
		Xog_mnu_itm itm = Get_or_null(key);
		if (itm == null) {
			itm = new Xog_mnu_itm(tid, key);
			hash.Add(key, itm);
		}
		itm.Init(text, shortcut, img, cmd);
		return itm;
	}
	public Xog_mnu_itm Add_spr() {
		String key = "xowa.spr" + Int_.XtoStr(hash.Count());
		Xog_mnu_itm rv = new Xog_mnu_itm(Xog_mnu_itm.Tid_spr, key);
		hash.Add(key, rv);
		return rv;
	}
	private Xog_mnu_itm Get_or_null(String key) {return (Xog_mnu_itm)hash.Fetch(key);}
	@gplx.Virtual public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_add_btn_default))	return Add_btn_default(m.ReadStr("v"));
		else if	(ctx.Match(k, Invk_add_btn))			return Add_btn(m.ReadStr("key"), m.ReadStr("text"), m.ReadStr("shortcut"), m.ReadStr("img"), m.ReadStr("cmd"));
		else if	(ctx.Match(k, Invk_add_grp_default))	return Add_grp_default(m.ReadStr("v"));
		else if	(ctx.Match(k, Invk_add_grp))			return Add_grp(m.ReadStr("key"), m.ReadStr("text"), m.ReadStr("shortcut"), m.ReadStrOr("img", ""));
		else if	(ctx.Match(k, Invk_add_spr))			return Add_spr();
		else if	(ctx.Match(k, Invk_clear))				this.Clear();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_add_spr = "add_spr", Invk_add_btn_default = "add_btn_default", Invk_add_btn = "add_btn", Invk_add_grp_default = "add_grp_default", Invk_add_grp = "add_grp", Invk_clear = "clear";
}

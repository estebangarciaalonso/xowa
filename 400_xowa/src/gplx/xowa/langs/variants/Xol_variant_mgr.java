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
package gplx.xowa.langs.variants; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
public class Xol_variant_mgr implements GfoInvkAble {
	private OrderedHash grps = OrderedHash_.new_bry_();
	public Xol_variant_mgr(Xoa_lang_mgr lang_mgr) {
		this.Set("zh|zh-hans;zh-hant;zh-cn;zh-tw;zh-hk;zh-mo;zh-sg;zh-my");
	}
	public Xol_variant_grp Get_by_key(byte[] key) {return (Xol_variant_grp)grps.Fetch(key);}
	public void Set(String s) {
		Xol_variant_grp grp = Xol_variant_grp.csv_(s);
		grps.Del(grp.Grp());		// remove any pre-existing items
		grps.Add(grp.Grp(), grp);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_set))		Set(m.ReadStr("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_set = "set";
}

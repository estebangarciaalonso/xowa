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
public class Xow_variant_mgr implements GfoInvkAble {
	private Xow_wiki wiki;
	public Xow_variant_mgr(Xow_wiki wiki) {
		this.wiki = wiki;
	}
	public boolean Enabled() {return sub != null;}
	public byte[] Grp() {return grp;} private byte[] grp;
	public byte[] Sub() {return sub;} private byte[] sub;
	public void Init_post_cfg(Xol_variant_mgr variant_mgr, Xol_lang lang) {
		if (grp != null || sub != null) return;	// grp / sub already specified by user_cfg; exit
		Xol_variant_grp lang_grp = variant_mgr.Get_by_key(lang.Key_bry());
		if (lang_grp == null) return;
		this.Set(lang_grp.Grp(), lang_grp.Subs()[0]);
	}
	public void Set(byte[] grp, byte[] sub) {
		this.grp = ByteAry_.Null_if_empty(grp);
		this.sub = ByteAry_.Null_if_empty(sub);
		Xop_variant_lxr.set_(wiki, grp);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_set))		Set(m.ReadBryOr("grp", null), m.ReadBryOr("sub", null));
		else if	(ctx.Match(k, Invk_grp))		return String_.new_utf8_(grp);
		else if	(ctx.Match(k, Invk_grp_))		grp = m.ReadBry("v");
		else if	(ctx.Match(k, Invk_sub))		return String_.new_utf8_(sub);
		else if	(ctx.Match(k, Invk_sub_))		sub = m.ReadBry("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_set = "set", Invk_grp = "grp", Invk_grp_ = "grp_", Invk_sub = "sub", Invk_sub_ = "sub_";
}
// wiki.variants.set('', '');
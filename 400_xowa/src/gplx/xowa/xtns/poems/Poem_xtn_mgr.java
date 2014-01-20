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
package gplx.xowa.xtns.poems; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Poem_xtn_mgr implements Xow_xtn_itm {
	public byte[] Xtn_key() {return xtn_key;} private byte[] xtn_key = Xtn_key_static;
	public void Xtn_init_by_app(Xoa_app app) {
	}
	public void Xtn_init_by_wiki(Xow_wiki wiki) {
		parser = new Xop_parser(wiki.Parser().Tmpl_lxr_mgr(), Xop_lxr_mgr.Poem_lxr_mgr);
		parser.Init_by_wiki(wiki);
		parser.Init_by_lang(wiki.Lang());
	}
	public boolean Enabled() {return enabled;} private boolean enabled;
	public Xop_parser Parser() {return parser;} private Xop_parser parser;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_enabled))			return Yn.XtoStr(enabled);
		else if	(ctx.Match(k, Invk_enabled_))			enabled = m.ReadYn("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_enabled = "enabled", Invk_enabled_ = "enabled_";
	public static final byte[] Xtn_key_static = ByteAry_.new_ascii_("poem");
}

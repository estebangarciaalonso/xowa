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
public class Xog_mnu_itm extends Xog_mnu_base {
	public Xog_mnu_itm(byte tid, String key) {
		this.tid = tid;
		this.key = key;
	}
	public byte Tid() {return tid;} private byte tid;
	public String Key() {return key;} private String key;
	public String Text() {return text;} private String text;
	public String Shortcut() {return shortcut;} private String shortcut;
	public String Cmd() {return cmd;} private String cmd;
	public String[] Img_nest() {return img_nest;} private String[] img_nest = String_.Ary_empty;
	public Xog_mnu_itm Init(String text, String shortcut, String img, String cmd) {return Init(text, shortcut, Img_nest_of(img), cmd);}
	private Xog_mnu_itm Init(String text, String shortcut, String[] img_nest, String cmd) {
		this.text = text; this.shortcut = shortcut; this.cmd = cmd;
		this.img_nest = img_nest;
		return this;
	}
	public Xog_mnu_itm Clone() {return new Xog_mnu_itm(tid, key).Init(text, shortcut, img_nest, cmd);}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_text))			return text;
		else if	(ctx.Match(k, Invk_text_))			text = m.ReadStr("v");
		else if	(ctx.Match(k, Invk_shortcut))		return shortcut;
		else if	(ctx.Match(k, Invk_shortcut_))		shortcut = m.ReadStr("v");
		else if	(ctx.Match(k, Invk_cmd))			return cmd;
		else if	(ctx.Match(k, Invk_cmd_))			cmd = m.ReadStr("v");
		else if	(ctx.Match(k, Invk_img))			return String_.Concat_with_str("/", img_nest);
		else if	(ctx.Match(k, Invk_img_))			img_nest = Img_nest_of(m.ReadStr("v"));
		else	return super.Invk(ctx, ikey, k, m);
		return this;
	}
	private static final String Invk_text = "text", Invk_text_ = "text_", Invk_shortcut = "shortcut", Invk_shortcut_ = "shortcut_", Invk_cmd = "cmd", Invk_cmd_ = "cmd_", Invk_img = "img", Invk_img_ = "img_";
	public static final byte Tid_null = 0, Tid_spr = 1, Tid_grp = 2, Tid_btn = 3;
	private static String[] Img_nest_of(String img) {return String_.Len_eq_0(img) ? String_.Ary_empty : String_.Split(img, "/");}
	public static final Xog_mnu_itm Null = new Xog_mnu_itm(Tid_null, "null");
}

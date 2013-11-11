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
package gplx.xowa.html; import gplx.*; import gplx.xowa.*;
import gplx.xowa.wikis.*;
public class Xoh_imgs_mgr implements GfoInvkAble {
	public Xoh_imgs_mgr(Xow_html_mgr html_mgr) {wiki_is_default = html_mgr.Wiki().Wiki_tid() == Xow_wiki_type_.Tid_home;} private boolean wiki_is_default;
	public BoolRef Alt_in_caption() {return alt_in_caption;} BoolRef alt_in_caption = BoolRef.true_();
	public BoolRef Alt_defaults_to_caption() {return alt_defaults_to_caption;} BoolRef alt_defaults_to_caption = BoolRef.true_();
	public void Copy_cfg(Xoh_imgs_mgr copy) {this.alt_in_caption = copy.alt_in_caption;}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_alt_in_caption))					return Yn.XtoStr(alt_in_caption.Val());
		else if	(ctx.Match(k, Invk_alt_in_caption_))				alt_in_caption = Modify(wiki_is_default, alt_in_caption, m.ReadYn("v"));
		else if	(ctx.Match(k, Invk_alt_defaults_to_caption))		return Yn.XtoStr(alt_defaults_to_caption.Val());
		else if	(ctx.Match(k, Invk_alt_defaults_to_caption_))		alt_defaults_to_caption = Modify(wiki_is_default, alt_defaults_to_caption, m.ReadYn("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_alt_in_caption = "alt_in_caption", Invk_alt_in_caption_ = "alt_in_caption_", Invk_alt_defaults_to_caption = "alt_defaults_to_caption", Invk_alt_defaults_to_caption_ = "alt_defaults_to_caption_";
	private static BoolRef Modify(boolean orig, BoolRef cur, boolean v) {return orig ? cur.Val_(v) : BoolRef.new_(v);}
}

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
package gplx.xowa.xtns; import gplx.*; import gplx.xowa.*;
import gplx.xowa.xtns.poems.*;
import gplx.xowa.xtns.gallery.*;
import gplx.xowa.xtns.scribunto.*;
public class Xow_xtn_mgr implements GfoInvkAble {
	private Hash_adp_bry regy = new Hash_adp_bry(true);
	public Gallery_mgr Gallery() {return xtn_gallery;} private Gallery_mgr xtn_gallery;
	public Xow_xtn_scribunto Xtn_scribunto() {return xtn_scribunto;} private Xow_xtn_scribunto xtn_scribunto;
	public Poem_xtn_mgr Xtn_poem() {return xtn_poem;} private Poem_xtn_mgr xtn_poem;
	public Xow_xtn_mgr Ctor_by_app(Xoa_app app) {
		this.app = app;
		xtn_scribunto = (Xow_xtn_scribunto)Add(new Xow_xtn_scribunto());
		Add(new gplx.xowa.xtns.scores.Xow_xtn_score());
		xtn_poem = (Poem_xtn_mgr)Add(new Poem_xtn_mgr());
		return this;
	}	private Xoa_app app;
	public Xow_xtn_mgr Ctor_by_wiki(Xow_wiki wiki) {
		this.wiki = wiki;
		this.app = wiki.App();
		xtn_gallery = new Gallery_mgr();
		xtn_scribunto = (Xow_xtn_scribunto)Get_or_new(Xow_xtn_scribunto.XTN_KEY);
		xtn_poem = (Poem_xtn_mgr)Get_or_new(Poem_xtn_mgr.Xtn_key_static);
		return this;
	}	private Xow_wiki wiki;
	public Xow_xtn_mgr Init_by_wiki(Xow_wiki wiki) {
		xtn_gallery.Parser().Init_by_wiki(wiki);
		return this;
	}
	public Xow_xtn_itm Get_or_fail(byte[] key) {Object rv = regy.Get_by_bry(key); if (rv == null) throw Err_.new_fmt_("unknown xtn: ~{0}", String_.new_utf8_(key)); return (Xow_xtn_itm)rv;}
	public Xow_xtn_itm Get_or_new(byte[] key) {
		Object rv_obj = regy.Get_by_bry(key);
		if (rv_obj == null) {
			Xow_xtn_itm rv = app.Xtn_mgr().Get_or_fail(key);
			rv.Xtn_init_by_wiki(wiki);
			regy.Add(key, rv);
			return rv;
		}
		return (Xow_xtn_itm)rv_obj;
	}
	private Xow_xtn_itm Add(Xow_xtn_itm xtn) {
		xtn.Xtn_init_by_app(app);
		regy.Add(xtn.Xtn_key(), xtn);
		return xtn;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_get))				return Get_or_fail(m.ReadBry("v"));
		else return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_get = "get";
}

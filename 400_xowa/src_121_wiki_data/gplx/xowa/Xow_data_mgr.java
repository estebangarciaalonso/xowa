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
package gplx.xowa; import gplx.*;
import gplx.lists.*; /*ComparerAble*/ import gplx.xowa.bldrs.imports.ctgs.*;
import gplx.xowa.dbs.*;
public class Xow_data_mgr implements GfoInvkAble {
	public Xow_data_mgr(Xow_wiki wiki) {
		this.wiki = wiki; this.redirect_mgr = wiki.Redirect_mgr();
	}	private Xop_redirect_mgr redirect_mgr;
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public Xoa_page Get_page(Xoa_ttl ttl, boolean called_from_tmpl) {Xoa_url url = new Xoa_url(); wiki.App().Url_parser().Parse(url, ttl.Raw()); return Get_page(url, ttl, called_from_tmpl);}
	public Xoa_page Get_page(Xoa_url url, Xoa_ttl ttl, boolean called_from_tmpl) {
		Xow_ns ns = ttl.Ns();
		Xoa_page rv = new Xoa_page(wiki, ttl);
		switch (ns.Id()) {
			case Xow_ns_.Id_special:
				wiki.Special_mgr().Special_gen(url, rv, wiki, ttl);
				return rv;
			case Xow_ns_.Id_mediaWiki:
				byte[] ttl_leaf = ttl.Leaf_txt();
				if (ByteAry_.Eq(ttl_leaf, wiki.Lang().Key_bry())) {	// ttl ends in lang of current wiki; EX: MediaWiki:Mainpage/zh; DATE:2014-02-22
					byte[] old_raw = ttl.Raw();
					byte[] new_raw = ByteAry_.Mid(old_raw, 0, old_raw.length - ttl_leaf.length - 1);	// - 1 to include "/"
					ttl = Xoa_ttl.parse_(wiki, new_raw);
				}
				break;
		}
		return Get_page(rv, url, ns, ttl, called_from_tmpl);
	}
	private Xoa_page Get_page(Xoa_page rv, Xoa_url url, Xow_ns ns, Xoa_ttl ttl, boolean called_from_tmpl) {
		int redirects = 0;
		Xodb_page db_page = Xodb_page.tmp_();
		while (true) {
			boolean exists = wiki.Db_mgr().Load_mgr().Load_by_ttl(db_page, ns, ttl.Page_db());
			if (!exists) return rv.Missing_();
			wiki.App().Gui_wtr().Prog_many(GRP_KEY, "file_load", "loading page for ~{0}", String_.new_utf8_(ttl.Raw()));
			wiki.Db_mgr().Load_mgr().Load_page(db_page, ns, !called_from_tmpl);
			byte[] bry = db_page.Text();
			rv.Data_raw_(bry).Modified_on_(db_page.Modified_on()).Id_(db_page.Id());
			if (url != null && url.Redirect_force()) return rv;
			Xoa_ttl redirect_ttl = redirect_mgr.Extract_redirect(bry, bry.length);
			if  (	redirect_ttl == null				// not a redirect
				||	redirects++ > 4)					// too many redirects; something went wrong
				break;				
			rv.Redirect_list().Add(ttl.Full_url());	// NOTE: must be url_encoded; EX: "en.wikipedia.org/?!" should generate link of "en.wikipedia.org/%3F!?redirect=no"
			rv.Ttl_(redirect_ttl);
			ns = redirect_ttl.Ns();
			ttl = redirect_ttl;
		}
		return rv;
	}
	public Xoa_page Redirect(Xoa_page page, byte[] page_bry) {
		Xoa_ttl trg_ttl = Xoa_ttl.parse_(wiki, page_bry);
		Xoa_url trg_url = Xoa_url.new_(wiki.Domain_bry(), page_bry);
		page.Ttl_(trg_ttl).Url_(trg_url).Redirected_(true);
		return wiki.Data_mgr().Get_page(page, trg_url, trg_ttl.Ns(), trg_ttl, false);
	}
	public static final int File_idx_unknown = -1;
	static final String GRP_KEY = "xowa.wiki.data";
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_create_enabled_))				wiki.Db_mgr().Save_mgr().Create_enabled_(m.ReadYn("v"));
		else if	(ctx.Match(k, Invk_update_modified_on_enabled_))	wiki.Db_mgr().Save_mgr().Update_modified_on_enabled_(m.ReadYn("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_create_enabled_ = "create_enabled_", Invk_update_modified_on_enabled_ = "update_modified_on_enabled_";
}

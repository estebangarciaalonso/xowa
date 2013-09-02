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
import org.junit.*;
public class Xoh_wiki_article_tst {
	@Before public void init() {}
	@Test  public void Logo_has_correct_main_page() {	// PURPOSE: Logo href should be "/site/en.wikipedia.org/wiki/", not "/wiki/Main_Page"
		Xoa_app app = Xoa_app_fxt.app_();
		Xow_wiki wiki = Xoa_app_fxt.wiki_tst_(app);
		Xowh_portal_mgr portal_mgr = wiki.Html_mgr().Portal_mgr();
		GfoInvkAble_.InvkCmd_val(portal_mgr, Xowh_portal_mgr.Invk_div_logo_, ByteAry_.new_ascii_("~{portal_nav_main_href}"));
		portal_mgr.Init_assert();
		Xoh_wiki_article html_mgr = new Xoh_wiki_article(app, true);
		html_mgr.Gen(wiki.Ctx().Page(), Xoh_wiki_article.Tid_view_read);
		Tfds.Eq(String_.new_ascii_(portal_mgr.Div_logo_bry()), "/site/en.wikipedia.org/wiki/");
	}
}

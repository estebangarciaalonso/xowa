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
import org.junit.*;
import gplx.xowa.gui.*; import gplx.xowa.html.*; import gplx.xowa.html.portal.*; import gplx.xowa.pages.*;
public class Xoh_page_wtr_mgr_tst {
	@Before public void init() {}
	@Test  public void Logo_has_correct_main_page() {	// PURPOSE: Logo href should be "/site/en.wikipedia.org/wiki/", not "/wiki/Main_Page"
		Xoa_app app = Xoa_app_fxt.app_();
		Xow_wiki wiki = Xoa_app_fxt.wiki_tst_(app);
		Xow_portal_mgr portal_mgr = wiki.Html_mgr().Portal_mgr();
		GfoInvkAble_.InvkCmd_val(portal_mgr, Xow_portal_mgr.Invk_div_logo_, Bry_.new_ascii_("~{portal_nav_main_href}"));
		portal_mgr.Init_assert();
		Xoh_page_wtr_mgr page_wtr_mgr = new Xoh_page_wtr_mgr(app, true);
		page_wtr_mgr.Gen(wiki.Ctx().Cur_page(), Xopg_view_mode.Tid_read);
		Tfds.Eq(String_.new_ascii_(portal_mgr.Div_logo_bry()), "/site/en.wikipedia.org/wiki/");
	}
}

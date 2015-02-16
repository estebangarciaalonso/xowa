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
import gplx.xowa.gui.*; import gplx.xowa.pages.*;
public class Xoh_page_wtr_wkr_tst {		
	@Before public void init() {fxt.Clear();} private Xoh_page_wtr_wkr_fxt fxt = new Xoh_page_wtr_wkr_fxt();
	@Test  public void Page_name() {
		fxt.Test_page_name_by_ttl("Earth", "Earth");
		fxt.Test_page_name_by_ttl("File:A.png", "File:A.png");
		fxt.Test_page_name_by_ttl("Special:Search/earth?fulltext=y", "Special:Search/earth");
		fxt.Test_page_name_by_ttl("Special:Search/earth", "Special:Search/earth");
		fxt.Test_page_name_by_display("Special:Allpages", "All pages", "All pages");
	}
	@Test  public void Edit() {
		fxt.Test_edit("&#9;", "&amp;#9;\n");	// NOTE: cannot by &#9; or will show up in edit box as "\t" and save as "\t" instead of &#9;
	}
	@Test  public void Css() {
		fxt.App().Html_mgr().Page_mgr().Content_code_fmtr().Fmt_("<pre style='overflow:auto'>~{page_text}</pre>");
		fxt.Test_read("MediaWiki:Common.css", ".xowa {}", "<pre style='overflow:auto'>.xowa {}</pre>");
		fxt.App().Html_mgr().Page_mgr().Content_code_fmtr().Fmt_("<pre>~{page_text}</pre>");
	}
	@Test  public void Amp_disable() {	// PURPOSE: in js documents; &quot; should be rendered as &quot;, not as "; DATE:2013-11-07
		fxt.Test_read("MediaWiki:Gadget.js", "&quot;", "<pre>&amp;quot;</pre>");
	}
}
class Xoh_page_wtr_wkr_fxt {
	public void Clear() {
		if (app == null) {
			app = Xoa_app_fxt.app_();
			wiki = Xoa_app_fxt.wiki_tst_(app);
		}
	}	private Bry_bfr tmp_bfr = Bry_bfr.reset_(255); private Xow_wiki wiki;
	public Xoa_app App() {return app;} private Xoa_app app; 
	public void Test_page_name_by_display(String ttl, String display, String expd) {
		Tfds.Eq(expd, String_.new_ascii_(Xoh_page_wtr_wkr_.Bld_page_name(tmp_bfr, Xoa_ttl.parse_(wiki, Bry_.new_ascii_(ttl)), Bry_.new_ascii_(display))));
	}
	public void Test_page_name_by_ttl(String raw, String expd) {
		Tfds.Eq(expd, String_.new_ascii_(Xoh_page_wtr_wkr_.Bld_page_name(tmp_bfr, Xoa_ttl.parse_(wiki, Bry_.new_ascii_(raw)), null)));
	}
	public void Test_edit(String raw, String expd) {
		wiki.Html_mgr().Page_wtr_mgr().Html_capable_(true);
		Xoa_page page = wiki.Ctx().Cur_page();
		page.Data_raw_(Bry_.new_utf8_(raw));
		Xoh_page_wtr_mgr mgr = wiki.Html_mgr().Page_wtr_mgr();
		Xoh_page_wtr_wkr wkr = mgr.Wkr(Xopg_view_mode.Tid_edit).Page_(page).Mgr_(mgr);
		wkr.XferAry(tmp_bfr, 0);
		Tfds.Eq(expd, tmp_bfr.Xto_str_and_clear());
	}
	public void Test_read(String page_name, String page_text, String expd) {
		wiki.Html_mgr().Page_wtr_mgr().Html_capable_(true);
		Xoa_page page = wiki.Ctx().Cur_page();
		page.Ttl_(Xoa_ttl.parse_(wiki, Bry_.new_ascii_(page_name)));
		page.Data_raw_(Bry_.new_utf8_(page_text));
		Xoh_page_wtr_mgr mgr = wiki.Html_mgr().Page_wtr_mgr();
		Xoh_page_wtr_wkr wkr = mgr.Wkr(Xopg_view_mode.Tid_read).Page_(page).Mgr_(mgr);
		wkr.XferAry(tmp_bfr, 0);
		Tfds.Eq(expd, tmp_bfr.Xto_str_and_clear());
	}
}

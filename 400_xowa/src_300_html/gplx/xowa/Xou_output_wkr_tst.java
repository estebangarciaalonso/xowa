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
public class Xou_output_wkr_tst {
	Xou_output_wkr_fxt fxt = new Xou_output_wkr_fxt();
	@Before public void init() {fxt.Clear();}
	@Test  public void Page_name() {
		fxt.Test_page_name_by_ttl("Earth", "Earth");
		fxt.Test_page_name_by_ttl("File:A.png", "File:A.png");
		fxt.Test_page_name_by_ttl("Special:Search/earth?fulltext=y", "Special:Search/earth");
		fxt.Test_page_name_by_ttl("Special:Search/earth", "Special:Search/earth");
		fxt.Test_page_name_by_display("Special:Allpages", "All pages", "All pages");
	}
	@Test  public void Edit() {
		fxt.Edit_tst("&#9;", "&amp;#9;");	// NOTE: cannot by &#9; or will show up in edit box as "\t" and save as "\t" instead of &#9;
	}
}
class Xou_output_wkr_fxt {
	public void Clear() {
		if (app == null) {
			app = Xoa_app_fxt.app_();
			wiki = Xoa_app_fxt.wiki_tst_(app);
		}
	}	private ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255); Xoa_app app; Xow_wiki wiki;
	public void Test_page_name_by_display(String ttl, String display, String expd) {
		Tfds.Eq(expd, String_.new_ascii_(Xou_output_wkr.Page_name(tmp_bfr, Xoa_ttl.parse_(wiki, ByteAry_.new_ascii_(ttl)), ByteAry_.new_ascii_(display))));
	}
	public void Test_page_name_by_ttl(String raw, String expd) {
		Tfds.Eq(expd, String_.new_ascii_(Xou_output_wkr.Page_name(tmp_bfr, Xoa_ttl.parse_(wiki, ByteAry_.new_ascii_(raw)), null)));
	}
	public void Edit_tst(String raw, String expd) {
		wiki.Html_mgr().Output_mgr().Html_capable_(true);
		Xoa_page page = wiki.Ctx().Page();
		page.Data_raw_(ByteAry_.new_utf8_(raw));
		Xoh_wiki_article mgr = wiki.Html_mgr().Output_mgr();
		Xou_output_wkr wkr = mgr.Wkr(Xoh_wiki_article.Tid_view_edit).Page_(page).Mgr_(mgr);
		wkr.XferAry(tmp_bfr, 0);
		Tfds.Eq(expd, tmp_bfr.XtoStrAndClear());
	}
}

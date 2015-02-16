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
package gplx.xowa.specials.randoms; import gplx.*; import gplx.xowa.*; import gplx.xowa.specials.*;
import org.junit.*;
public class Xop_randomRootPage_page_tst {
	@Before public void init() {fxt.Clear();} private Xop_randomRootPage_page_fxt fxt = new Xop_randomRootPage_page_fxt();
	@Test   public void Ns_main() {
		fxt.Init_create_page("A");
		fxt.Init_create_page("A/B/C");
		fxt.Test_open("Special:RandomRootPage/Main", "A");
	}
	@Test   public void Ns_help() {
		fxt.Init_create_page("Help:A");
		fxt.Init_create_page("Help:A/B/C");
		fxt.Test_open("Special:RandomRootPage/Help", "Help:A");
	}
}
class Xop_randomRootPage_page_fxt {
	public void Clear() {
		parser_fxt = new Xop_fxt();
		parser_fxt.Reset();
		wiki = parser_fxt.Wiki();
		special_page = wiki.Special_mgr().Page_randomRootPage();
	}	private Xop_fxt parser_fxt; private Xop_randomRootPage_page special_page; private Xow_wiki wiki;
	public void Init_create_page(String page) {parser_fxt.Init_page_create(page, page);}
	public void Test_open(String special_url, String expd) {
		Xoa_page page = Test_special_open(wiki, special_page, special_url);
		Tfds.Eq(expd, String_.new_ascii_(page.Url().Page_bry()));
		Tfds.Eq(expd, String_.new_ascii_(page.Data_raw()));
	}
	public static Xoa_page Test_special_open(Xow_wiki wiki, Xows_page special_page, String special_url) {
		Xoa_page page = wiki.Ctx().Cur_page();
		Xoa_url url = Xoa_url_parser.Parse_url(wiki.App(), wiki, special_url);
		page.Url_(url);
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, Bry_.new_ascii_(special_url));
		page.Ttl_(ttl);
		special_page.Special_gen(url, page, wiki, ttl);
		return page;
	}
}

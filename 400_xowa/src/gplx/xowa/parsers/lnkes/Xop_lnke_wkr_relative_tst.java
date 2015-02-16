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
package gplx.xowa.parsers.lnkes; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import org.junit.*;
public class Xop_lnke_wkr_relative_tst {
	@Before public void init() {fxt.Reset();} private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Relative_obj() {
		fxt.Test_parse_page_wiki("[//a b]"
			, fxt.tkn_lnke_(0, 7).Lnke_rng_(1, 4).Subs_(fxt.tkn_txt_(5, 6))
			);
	}
	@Test  public void Relative_external() {
		fxt.Test_parse_page_wiki_str("[//www.a.org a]", "<a href=\"http://www.a.org\" class=\"external text\" rel=\"nofollow\">a</a>");
	}
	@Test  public void Relative_internal() {
		fxt.Init_xwiki_add_user_("en.wikipedia.org");
		fxt.Test_parse_page_wiki_str("[//en.wikipedia.org/wiki Wikipedia]", "<a href=\"/site/en.wikipedia.org/wiki/\">Wikipedia</a>");
	}
	@Test  public void Relative_w_category() {	// EX: [//commons.wikimedia.org/wiki/Category:Diomedeidae A]
		fxt.Init_xwiki_add_user_("en.wikipedia.org");
		fxt.Test_parse_page_wiki_str("[//en.wikipedia.org/wiki/Category:A A]", "<a href=\"/site/en.wikipedia.org/wiki/Category:A\">A</a>");
	}
	@Test   public void Relurl() {
		fxt.App().User().Wiki().Xwiki_mgr().Add_full(Bry_.new_utf8_("en.wikipedia.org"), Bry_.new_utf8_("en.wikipedia.org"));
		fxt.Test_parse_page_wiki_str("[[//en.wikipedia.org/ a]]", "[<a href=\"/site/en.wikipedia.org/wiki/\">a</a>]");
	}
}

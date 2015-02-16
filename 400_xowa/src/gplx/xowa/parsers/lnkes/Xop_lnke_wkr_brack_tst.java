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
public class Xop_lnke_wkr_brack_tst {
	@Before public void init() {fxt.Reset();} private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Brace_noText() {
		fxt.Test_parse_page_wiki("[irc://a]", fxt.tkn_lnke_(0, 9).Lnke_typ_(Xop_lnke_tkn.Lnke_typ_brack).Lnke_rng_(1, 8));
	}
	@Test  public void Brace_eos() {
		fxt.Test_parse_page_wiki("[irc://a", fxt.tkn_txt_(0, 1), fxt.tkn_lnke_(1, 8).Lnke_typ_(Xop_lnke_tkn.Lnke_typ_brack_dangling).Lnke_rng_(1, 8));
	}
	@Test  public void Brace_text() {
		fxt.Test_parse_page_wiki("[irc://a b c]", fxt.tkn_lnke_(0, 13).Lnke_rng_(1, 8).Subs_(fxt.tkn_txt_(9, 10), fxt.tkn_space_(10, 11), fxt.tkn_txt_(11, 12)));
	}
	@Test  public void Brace_lt() {
		fxt.Init_log_(Xop_xnde_log.Eos_while_closing_tag).Test_parse_page_wiki("[irc://a<b c]", fxt.tkn_lnke_(0, 13).Lnke_rng_(1, 8).Subs_(fxt.tkn_txt_(8, 10), fxt.tkn_space_(10, 11), fxt.tkn_txt_(11, 12)));
	}
	@Test  public void Brace_xnde_bgn() {// PURPOSE: occurred at ref of UK; a {{cite web|url=http://www.abc.gov/{{dead link|date=December 2011}}|title=UK}} b
		fxt.Test_parse_page_wiki_str
			(	"[http://b.org<sup>c</sup>]"
			,	"<a href=\"http://b.org\" class=\"external text\" rel=\"nofollow\"><sup>c</sup></a>"
			);
	}
	@Test  public void Brace_newLine() {
		fxt.Test_parse_page_wiki("[irc://a\n]", fxt.tkn_txt_(0, 8), fxt.tkn_nl_char_len1_(8), fxt.tkn_txt_(9, 10));
	}
	@Test  public void Html_brack() {
		fxt.Test_parse_page_wiki_str("[irc://a]", "<a href=\"irc://a\" class=\"external text\" rel=\"nofollow\">[1]</a>");
	}
	@Test  public void Apos() {
		fxt.Test_parse_page_wiki_str("[http://www.a.org''b'']", "<a href=\"http://www.a.org\" class=\"external text\" rel=\"nofollow\"><i>b</i></a>");		
		fxt.Test_parse_page_wiki_str("[http://www.a.org'b]", "<a href=\"http://www.a.org'b\" class=\"external text\" rel=\"nofollow\">[1]</a>");
	}
	@Test   public void Nowiki() {
		fxt.Test_parse_page_all_str
		(	"<nowiki>http://a.org</nowiki>"
		,	"http://a.org"
		);
	}
	@Test  public void Lnki_one() {	// PURPOSE: parallel test for "http://a.org[[B]]"; DATE:2014-07-11
		fxt.Test_parse_page_wiki_str
		( "[http://a.org b [[C]] d]"
		,String_.Concat_lines_nl_skip_last
		( "<a href=\"http://a.org\" class=\"external text\" rel=\"nofollow\">b <a href=\"/wiki/C\">C</a> d</a>"
		));
	}
	@Test  public void Encode_xwiki() {	// PURPOSE: href title and args should always be encoded; PAGE:en.w:List_of_Category_A_listed_buildings_in_West_Lothian DATE:2014-07-15
		fxt.App().User().Wiki().Xwiki_mgr().Add_full(Bry_.new_utf8_("commons.wikimedia.org"), Bry_.new_utf8_("commons.wikimedia.org"));
		fxt.Test_parse_page_wiki_str		// encode page
		( "[http://commons.wikimedia.org/%22%3E_A B]"
		, "<a href=\"/site/commons.wikimedia.org/wiki/%22%3E_A\">B</a>"		// '%22%3E' not '">'
		);
		fxt.Test_parse_page_wiki_str		// encode args
		( "[http://commons.wikimedia.org/A?b=%22%3E_C D]"
		, "<a href=\"/site/commons.wikimedia.org/wiki/A?b=%22%3E_C\">D</a>"	// '%22%3E' not '">'
		);
	}
	@Test  public void Encode_basic() {	// PURPOSE: counterpart to Encode_xwiki; DATE:2014-07-15
		fxt.Test_parse_page_wiki_str		// encode page
		( "[http://a.org/%22%3E_A B]"
		, "<a href=\"http://a.org/%22%3E_A\" class=\"external text\" rel=\"nofollow\">B</a>"		// '%22%3E' not '">'
		);
		fxt.Test_parse_page_wiki_str		// encode args
		( "[http://a.org/A?b=%22%3E_C D]"
		, "<a href=\"http://a.org/A?b=%22%3E_C\" class=\"external text\" rel=\"nofollow\">D</a>"	// '%22%3E' not '">'
		);
	}
	@Test  public void Encode_relative() {	// PURPOSE: counterpart to Encode_xwiki; DATE:2014-07-15
		fxt.Test_parse_page_wiki_str		// encode page
		( "[//a.org/%22%3E_A B]"
		, "<a href=\"http://a.org/%22%3E_A\" class=\"external text\" rel=\"nofollow\">B</a>"		// '%22%3E' not '">'
		);
		fxt.Test_parse_page_wiki_str		// encode args
		( "[//a.org/A?b=%22%3E_C D]"
		, "<a href=\"http://a.org/A?b=%22%3E_C\" class=\"external text\" rel=\"nofollow\">D</a>"	// '%22%3E' not '">'
		);
	}
}

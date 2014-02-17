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
public class Xop_lnke_wkr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Raw() {
		fxt.Test_parse_page_wiki("irc://a", fxt.tkn_lnke_(0, 7).Lnke_typ_(Xop_lnke_tkn.Lnke_typ_text).Lnke_rng_(0, 7));
	}
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
	@Test  public void Brace_xnde_end() {// NOTE: compare to Brace_lt
		fxt.Test_parse_page_wiki("<span>irc://a</span>"
			,	fxt.tkn_xnde_(0, 20).Subs_
			(		fxt.tkn_lnke_(6, 13)
			)
			);
	}
	@Test  public void Brace_xnde_bgn() {// PURPOSE: occurred at ref of UK; a {{cite web|url=http://www.abc.gov/{{dead link|date=December 2011}}|title=UK}} b
		fxt.Test_parse_page_wiki_str
			(	"[http://b.org<sup>c</sup>]"
			,	"<a href=\"http://b.org\" class=\"external text\" rel=\"nofollow\"><sup>c</sup></a>"
			);
	}
	@Test  public void TxtOnly_txt() {
		fxt.Test_parse_page_wiki("irc://a b c", fxt.tkn_lnke_(0, 7).Lnke_rng_(0, 7), fxt.tkn_space_(7, 8), fxt.tkn_txt_(8, 9), fxt.tkn_space_(9, 10), fxt.tkn_txt_(10, 11));
	}
	@Test  public void Brace_newLine() {
		fxt.Test_parse_page_wiki("[irc://a\n]", fxt.tkn_txt_(0, 8), fxt.tkn_nl_char_len1_(8), fxt.tkn_txt_(9, 10));
	}
	@Test  public void Dangling_eos() {
		fxt.Test_parse_page_wiki("[irc://a b"
			,	fxt.tkn_lnke_(0, 8).Lnke_typ_(Xop_lnke_tkn.Lnke_typ_brack_dangling)
			,	fxt.tkn_txt_(9, 10)
			);
	}
	@Test  public void Dangling_newLine() {
		fxt.Test_parse_page_wiki("[irc://a b\nc]"
			,	fxt.tkn_lnke_(0, 8).Lnke_typ_(Xop_lnke_tkn.Lnke_typ_brack_dangling)
			,	fxt.tkn_txt_(9, 10)
			,	fxt.tkn_nl_char_len1_(10)
			,	fxt.tkn_txt_(11, 13)
			);
	}
	@Test  public void Dangling_gt() {
		fxt.Test_parse_page_wiki("[irc://a>b c]", fxt.tkn_lnke_(0, 13).Lnke_typ_(Xop_lnke_tkn.Lnke_typ_brack).Subs_(fxt.tkn_txt_(8, 10), fxt.tkn_space_(10, 11), fxt.tkn_txt_(11, 12)));
	}
	@Test  public void Err_multiple() {
		fxt.Test_parse_page_wiki("[irc://a][irc://b]"
			, fxt.tkn_lnke_(0,  9)
			, fxt.tkn_lnke_(9, 18)
			);
	}
	@Test  public void Err_txt_is_protocol() {
		fxt.Test_parse_page_wiki("[irc://a irc://b]"
			, fxt.tkn_lnke_(0, 17).Lnke_rng_(1, 8).Subs_(fxt.tkn_txt_(9, 16))
			);
	}
	@Test  public void Lnke_should_precede_lnki() { // PURPOSE: [[ should not be interpreted as lnki if [irc is available
		fxt.Test_parse_page_wiki("[[irc://a/b c]]"
			,	fxt.tkn_txt_(0, 1)
			,	fxt.tkn_lnke_(1, 14).Subs_
				(	fxt.tkn_txt_(12, 13)
				)
			,	fxt.tkn_txt_(14, 15)
			);
	}
	@Test  public void List() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"*irc://a"
			,	"*irc://b"	
			),String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"  <li><a href=\"irc://a\" class=\"external text\" rel=\"nofollow\">irc://a</a>"
			,	"  </li>"
			,	"  <li><a href=\"irc://b\" class=\"external text\" rel=\"nofollow\">irc://b</a>"
			,	"  </li>"
			,	"</ul>"
			));
	}
	@Test  public void Html_text() {
		fxt.Test_parse_page_wiki_str("irc://a", "<a href=\"irc://a\" class=\"external text\" rel=\"nofollow\">irc://a</a>");
	}
	@Test  public void Html_brack() {
		fxt.Test_parse_page_wiki_str("[irc://a]", "<a href=\"irc://a\" class=\"external text\" rel=\"nofollow\">[1]</a>");
	}
	@Test  public void Defect_reverse_caption_link() { // PURPOSE: bad lnke formatting (caption before link); ] should show up at end, but only [ shows up; EX.WP: Paul Philippoteaux; [caption http://www.americanheritage.com]
		fxt.Test_parse_page_wiki_str("[caption irc://a]"
				, "[caption <a href=\"irc://a\" class=\"external text\" rel=\"nofollow\">irc://a</a>]");
	}
	@Test  public void Defect_2nd_consecutive_lnke() {	// PURPOSE: bad code that was causing lnkes to show up; EX.WP:Template:Infobox_country;
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	"[[http://a.org a]] [[http://b.org b]]"
		), String_.Concat_lines_nl_skipLast
		(	"[<a href=\"http://a.org\" class=\"external text\" rel=\"nofollow\">a</a>] [<a href=\"http://b.org\" class=\"external text\" rel=\"nofollow\">b</a>]"
		));
	}
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
	@Test  public void Xwiki() {
		fxt.App().User().Wiki().Xwiki_mgr().Add_full(ByteAry_.new_utf8_("en.wikipedia.org"), ByteAry_.new_utf8_("en.wikipedia.org"));
		fxt.Test_parse_page_wiki_str("[http://en.wikipedia.org/wiki/A a]", "<a href=\"/site/en.wikipedia.org/wiki/A\">a</a>");
	}
	@Test  public void Xwiki_relative() {
		fxt.App().User().Wiki().Xwiki_mgr().Add_full(ByteAry_.new_utf8_("en.wikipedia.org"), ByteAry_.new_utf8_("en.wikipedia.org"));
		fxt.Test_parse_page_wiki_str("[//en.wikipedia.org/ a]", "<a href=\"/site/en.wikipedia.org/wiki/\">a</a>");
	}
	@Test  public void Xwiki_qarg() {// DATE:2013-02-02
		fxt.Init_xwiki_add_user_("en.wikipedia.org");
		fxt.Test_parse_page_wiki_str("http://en.wikipedia.org/wiki/Special:Allpages?from=Earth", "<a href=\"/site/en.wikipedia.org/wiki/Special:Allpages?from=Earth\">http://en.wikipedia.org/wiki/Special:Allpages?from=Earth</a>");
	}
	@Test  public void Lang_prefix() {
		fxt.App().User().Wiki().Xwiki_mgr().Add_full(ByteAry_.new_utf8_("en.wikipedia.org"), ByteAry_.new_utf8_("en.wikipedia.org"));
		fxt.Wiki().Xwiki_mgr().Add_full(ByteAry_.new_ascii_("fr"), ByteAry_.new_ascii_("fr.wikipedia.org"));
		fxt.Test_parse_page_wiki_str("[http://en.wikipedia.org/wiki/fr:A a]", "<a href=\"/site/fr.wikipedia.org/wiki/A\">a</a>");
	}
	@Test  public void Xwiki_query_arg() {
		fxt.App().User().Wiki().Xwiki_mgr().Add_full(ByteAry_.new_utf8_("en.wikipedia.org"), ByteAry_.new_utf8_("en.wikipedia.org"));
		fxt.Test_parse_page_wiki_str("[http://en.wikipedia.org/wiki/A?action=edit a]", "<a href=\"/site/en.wikipedia.org/wiki/A?action=edit\">a</a>");
	}
	@Test   public void Relurl() {
		fxt.App().User().Wiki().Xwiki_mgr().Add_full(ByteAry_.new_utf8_("en.wikipedia.org"), ByteAry_.new_utf8_("en.wikipedia.org"));
		fxt.Test_parse_page_wiki_str("[[//en.wikipedia.org/ a]]", "[<a href=\"/site/en.wikipedia.org/wiki/\">a</a>]");
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
}

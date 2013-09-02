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
package gplx.xowa.html.tocs; import gplx.*; import gplx.xowa.*; import gplx.xowa.html.*;
import org.junit.*;
public class Xop_toc_mgr_tst {
	Xop_toc_mgr_fxt fxt = new Xop_toc_mgr_fxt();
	@Before public void init() {}
	@Test  public void Basic() {
		fxt.Test_html_toc(String_.Concat_lines_nl_skipLast
			(	"==a=="
			,	"==b=="
			,	"==c=="
			,	"==d=="
			), TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        </li>"
			,	"        <li class=\"toclevel-1 tocsection-2\"><a href=\"#b\"><span class=\"tocnumber\">2</span> <span class=\"toctext\">b</span></a>"
			,	"        </li>"
			,	"        <li class=\"toclevel-1 tocsection-3\"><a href=\"#c\"><span class=\"tocnumber\">3</span> <span class=\"toctext\">c</span></a>"
			,	"        </li>"
			,	"        <li class=\"toclevel-1 tocsection-4\"><a href=\"#d\"><span class=\"tocnumber\">4</span> <span class=\"toctext\">d</span></a>"
			,	"        </li>"
			,	"      </ul>"
			));
	}
	@Test  public void Hier_down() {
		fxt.Test_html_toc(String_.Concat_lines_nl_skipLast
			(	"==a=="
			,	"===b==="
			,	"====c===="
			,	"=====d====="
			), TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        <ul>"
			,	"          <li class=\"toclevel-2 tocsection-2\"><a href=\"#b\"><span class=\"tocnumber\">1.1</span> <span class=\"toctext\">b</span></a>"
			,	"          <ul>"
			,	"            <li class=\"toclevel-3 tocsection-3\"><a href=\"#c\"><span class=\"tocnumber\">1.1.1</span> <span class=\"toctext\">c</span></a>"
			,	"            <ul>"
			,	"              <li class=\"toclevel-4 tocsection-4\"><a href=\"#d\"><span class=\"tocnumber\">1.1.1.1</span> <span class=\"toctext\">d</span></a>"
			,	"              </li>"
			,	"            </ul>"
			,	"            </li>"
			,	"          </ul>"
			,	"          </li>"
			,	"        </ul>"
			,	"        </li>"
			,	"      </ul>"
			));
	}
	@Test  public void Hier_up() {
		fxt.Test_html_toc(String_.Concat_lines_nl_skipLast
			(	"==a=="
			,	"===b==="
			,	"===c==="
			,	"==d=="
			), TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        <ul>"
			,	"          <li class=\"toclevel-2 tocsection-2\"><a href=\"#b\"><span class=\"tocnumber\">1.1</span> <span class=\"toctext\">b</span></a>"
			,	"          </li>"
			,	"          <li class=\"toclevel-2 tocsection-3\"><a href=\"#c\"><span class=\"tocnumber\">1.2</span> <span class=\"toctext\">c</span></a>"
			,	"          </li>"
			,	"        </ul>"
			,	"        </li>"
			,	"        <li class=\"toclevel-1 tocsection-4\"><a href=\"#d\"><span class=\"tocnumber\">2</span> <span class=\"toctext\">d</span></a>"
			,	"        </li>"
			,	"      </ul>"
			));
	}
	@Test  public void Down_up() {
		fxt.Test_html_toc(String_.Concat_lines_nl_skipLast
			(	"==a=="
			,	"===b==="
			,	"==c=="
			,	"===d==="
			), TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        <ul>"
			,	"          <li class=\"toclevel-2 tocsection-2\"><a href=\"#b\"><span class=\"tocnumber\">1.1</span> <span class=\"toctext\">b</span></a>"
			,	"          </li>"
			,	"        </ul>"
			,	"        </li>"
			,	"        <li class=\"toclevel-1 tocsection-3\"><a href=\"#c\"><span class=\"tocnumber\">2</span> <span class=\"toctext\">c</span></a>"
			,	"        <ul>"
			,	"          <li class=\"toclevel-2 tocsection-4\"><a href=\"#d\"><span class=\"tocnumber\">2.1</span> <span class=\"toctext\">d</span></a>"
			,	"          </li>"
			,	"        </ul>"
			,	"        </li>"
			,	"      </ul>"
			));
	}
	@Test  public void D1_D1_D1_U2() {
		fxt.Test_html_toc(String_.Concat_lines_nl_skipLast
			(	"==a=="
			,	"===b==="
			,	"====c===="
			,	"==d=="
			), TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        <ul>"
			,	"          <li class=\"toclevel-2 tocsection-2\"><a href=\"#b\"><span class=\"tocnumber\">1.1</span> <span class=\"toctext\">b</span></a>"
			,	"          <ul>"
			,	"            <li class=\"toclevel-3 tocsection-3\"><a href=\"#c\"><span class=\"tocnumber\">1.1.1</span> <span class=\"toctext\">c</span></a>"
			,	"            </li>"
			,	"          </ul>"
			,	"          </li>"
			,	"        </ul>"
			,	"        </li>"
			,	"        <li class=\"toclevel-1 tocsection-4\"><a href=\"#d\"><span class=\"tocnumber\">2</span> <span class=\"toctext\">d</span></a>"
			,	"        </li>"
			,	"      </ul>"
			));
	}
	@Test  public void Exc() {	// PURPOSE: models strange case wherein jumping down does not work
		fxt.Test_html_toc(String_.Concat_lines_nl_skipLast
			(	"==a=="
			,	"====b===="
			,	"===c==="
			,	"====d===="
			), TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        <ul>"
			,	"          <li class=\"toclevel-2 tocsection-2\"><a href=\"#b\"><span class=\"tocnumber\">1.1</span> <span class=\"toctext\">b</span></a>"
			,	"          </li>"
			,	"          <li class=\"toclevel-2 tocsection-3\"><a href=\"#c\"><span class=\"tocnumber\">1.2</span> <span class=\"toctext\">c</span></a>"
			,	"          <ul>"
			,	"            <li class=\"toclevel-3 tocsection-4\"><a href=\"#d\"><span class=\"tocnumber\">1.2.1</span> <span class=\"toctext\">d</span></a>"
			,	"            </li>"
			,	"          </ul>"
			,	"          </li>"
			,	"        </ul>"
			,	"        </li>"
			,	"      </ul>"
			));
	}
	@Test  public void Repeat_name() {
		fxt.Test_html_toc(String_.Concat_lines_nl_skipLast
			(	"==a=="
			,	"==a=="
			,	"==a=="
			,	"==a=="
			), TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        </li>"
			,	"        <li class=\"toclevel-1 tocsection-2\"><a href=\"#a_2\"><span class=\"tocnumber\">2</span> <span class=\"toctext\">a</span></a>"
			,	"        </li>"
			,	"        <li class=\"toclevel-1 tocsection-3\"><a href=\"#a_3\"><span class=\"tocnumber\">3</span> <span class=\"toctext\">a</span></a>"
			,	"        </li>"
			,	"        <li class=\"toclevel-1 tocsection-4\"><a href=\"#a_4\"><span class=\"tocnumber\">4</span> <span class=\"toctext\">a</span></a>"
			,	"        </li>"
			,	"      </ul>"
			));
	}
	@Test  public void Encode() {
		fxt.Test_html_toc(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==a+b=="
			), TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a.2Bb\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a+b</span></a>"
			,	"        </li>"
			,	"      </ul>"
			));
	}
	@Test  public void Ws() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"== a b =="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a_b\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a b</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='a_b'> a b </span></h2>"
			));
	}
	@Test  public void Apos_italic() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==''a''=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\"><i>a</i></span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='a'><i>a</i></span></h2>"
			));
	}
	@Test  public void Xnde_italic() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==<i>a</i>=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\"><i>a</i></span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='a'><i>a</i></span></h2>"
			));
	}
	@Test  public void Xnde_small() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==<small>a</small>=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='a'><small>a</small></span></h2>"
			));
	}
	@Test  public void Xnde_dangling() {	// PURPOSE: do not render dangling xndes; EX: Casualties_of_the_Iraq_War; ===<small>Iraqi Health Ministry<small>===
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==<small>a<small>=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='a'><small>a<small></small></small></span></h2>"
			));
	}
	@Test  public void Xnde_nest_xnde() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==a <sup>b<small>c</small>d</sup> e=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a_bcd_e\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a b<small>c</small>d e</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='a_bcd_e'>a <sup>b<small>c</small>d</sup> e</span></h2>"
			));
	}
	@Test  public void Xnde_nest_lnki() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==<small>[[a|b]]</small>=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#b\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">b</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='b'><small><a href=\"/wiki/A\">b</a></small></span></h2>"
			));
	}
	@Test  public void Xnde_nest_inline() {	// PURPOSE: do not render inline xndes; EX: Magnetic_resonance_imaging
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==a<span id='b'>b<br/></span>=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#ab\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">ab</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='ab'>a<span id='b'>b<br/></span></span></h2>"
			));
	}
	@Test  public void Lnki_link() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==[[a]]=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='a'><a href=\"/wiki/A\">a</a></span></h2>"
			));
	}
	@Test  public void Lnki_caption() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==[[a|b]]=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#b\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">b</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='b'><a href=\"/wiki/A\">b</a></span></h2>"
			));
	}
	@Test  public void Lnki_caption_nest() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==[[a|b<i>c</i>d]]=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#bcd\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">b<i>c</i>d</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='bcd'><a href=\"/wiki/A\">b<i>c</i>d</a></span></h2>"
			));
	}
	@Test  public void Html_ncr() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==&#91;a&#93;=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#.5Ba.5D\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">[a]</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='.5Ba.5D'>[a]</span></h2>"
			));
	}
	@Test   public void Fix_large_before_small() {	// PURPOSE.fix: "===a===\n===b===\n" followed by "==c==" causes improper formatting; DATE:2013-05-16
		fxt.Test_html_toc(String_.Concat_lines_nl_skipLast
		(	"===a==="
		,	"===b==="
		,	"==c=="
		,	"==d=="
		), TocTable	// NOTE: should all be level 2
		(	"      <ul>"
		,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#a\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
		,	"        </li>"
		,	"        <li class=\"toclevel-1 tocsection-2\"><a href=\"#b\"><span class=\"tocnumber\">2</span> <span class=\"toctext\">b</span></a>"
		,	"        </li>"
		,	"        <li class=\"toclevel-1 tocsection-3\"><a href=\"#c\"><span class=\"tocnumber\">3</span> <span class=\"toctext\">c</span></a>"
		,	"        </li>"
		,	"        <li class=\"toclevel-1 tocsection-4\"><a href=\"#d\"><span class=\"tocnumber\">4</span> <span class=\"toctext\">d</span></a>"
		,	"        </li>"
		,	"      </ul>"
		));
	}
	@Test   public void Translate_and_comment() {	// PURPOSE: <translate> is an xtn and parses its innerText separately; meanwhile, toc_mgr defaults to using the innerText to build toc; EX:Wikidata:Introduction; DATE:2013-07-16
		fxt.Test_html_toc(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==<translate><!--b-->ac</translate>=="
			), TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#.3C.21--b--.3Eac\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">ac</span></a>"
			,	"        </li>"
			,	"      </ul>"
			));
	}
	@Test   public void Ref() { // PURPOSE: ref contents should not print in TOC; DATE:2013-07-23
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
			(	"__FORCETOC__"
			,	"==a<ref>b</ref>=="
			)
			, String_.Concat_lines_nl
			(	TocTable
			(	"      <ul>"
			,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#ab\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">a</span></a>"
			,	"        </li>"
			,	"      </ul>"
			)
			,	"<h2><span class='mw-headline' id='ab'>a<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup></span></h2>"
			));
	}
	String TocTable(String... ary) {
		return String_.Concat_lines_nl_skipLast
		(	"<table id=\"toc\" class=\"toc\">"
		,	"  <tr>"
		,	"    <td>"
		,	"      <div id=\"toctitle\"><h2>Contents</h2></div>"
		,	String_.Concat_lines_nl_skipLast(ary)
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		);
	}
}
class Xop_toc_mgr_fxt {
	Xop_fxt fxt = new Xop_fxt();
	Xop_toc_mgr toc_mgr = new Xop_toc_mgr();
	ByteAryBfr tmp = ByteAryBfr.new_();
	public void Test_html_toc(String raw, String expd) {
		toc_mgr.Clear();
		byte[] raw_bry = ByteAry_.new_utf8_(raw);
		Xop_root_tkn root = fxt.Ctx().Tkn_mkr().Root(raw_bry);
		fxt.Parser().Parse_page_all_clear(root, fxt.Ctx(), fxt.Ctx().Tkn_mkr(), raw_bry);
		toc_mgr.Html(fxt.Ctx().Page(), raw_bry, tmp);
		Tfds.Eq_ary(String_.SplitLines_nl(expd), String_.SplitLines_nl(tmp.XtoStrAndClear()), raw);
	}
	public void Test_html_all(String raw, String expd) {
		fxt.Hctx().Toc_show_(true);
		toc_mgr.Clear();
		fxt.tst_Parse_page_all_str(raw, expd);
		fxt.Hctx().Toc_show_(false);
	}
}

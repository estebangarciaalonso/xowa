/*
XOWA: the extensible offline wiki application
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
public class Xop_under_lxr_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {fxt.Reset();}
	@Test   public void Toc_basic() {
		fxt.tst_Parse_page_all_str("a__TOC__b", "ab");
	}
	@Test   public void Toc_match_failed() {
		fxt.tst_Parse_page_all_str("a__TOCA__b", "a__TOCA__b");
	}
	@Test   public void Notoc_basic() {
		fxt.Hctx().Toc_show_(true);	// NOTE: must enable in order for TOC to show (and to make sure NOTOC suppresses) 
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	"__NOTOC__"
		,	"==a=="
		,	"==b=="
		,	"==c=="
		,	"==d=="
		), String_.Concat_lines_nl
		(	"<h2><span class='mw-headline' id='a'>a</span></h2>"
		,	""
		,	"<h2><span class='mw-headline' id='b'>b</span></h2>"
		,	""
		,	"<h2><span class='mw-headline' id='c'>c</span></h2>"
		,	""
		,	"<h2><span class='mw-headline' id='d'>d</span></h2>"
		));
		fxt.Hctx().Toc_show_(false);
	}
	@Test   public void Ignore_pre() {
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str("a\n   __NOTOC__\n", String_.Concat_lines_nl
		(	"<p>a"
		,	"   </p>"
		,	""
		,	"<p><br/>"
		,	"</p>"
		));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test   public void Toc_works() {	// PURPOSE: make sure "suppressed" pre does not somehow suppress TOC
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str("a\n__TOC__\n==b==\n", String_.Concat_lines_nl
		(	"<p>a"
		,	"<table id=\"toc\" class=\"toc\">"
		,	"  <tr>"
		,	"    <td>"
		,	"      <div id=\"toctitle\"><h2>Contents</h2></div>"
		,	"      <ul>"
		,	"        <li class=\"toclevel-1 tocsection-1\"><a href=\"#b\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">b</span></a>"
		,	"        </li>"
		,	"      </ul>"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		,	"</p>"
		,	""
		,	"<h2>b</h2>"
		));
		fxt.Ctx().Para().Enabled_n_();
	}		
	@Test  public void Ignore_pre_after() {	// PURPOSE: "__TOC__\s\n" must be trimmed at end, else false pre; assertion only (no code exists to handle this test);  DATE:2013-07-08
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	"a"
		,	"__NOTOC__ "
		,	"b"
		), String_.Concat_lines_nl
		(	"<p>a"
		,	" </p>"	// NOTE: space after __NOTOC__ gets put here
		,	""
		,	"<p>b"
		,	"</p>"
		));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Disambig() {	// PURPOSE: ignore "__DISAMBIG__"; EX:{{disambiguation}} DATE:2013-07-24
		fxt.tst_Parse_page_all_str("__DISAMBIG__", "");
	}
}

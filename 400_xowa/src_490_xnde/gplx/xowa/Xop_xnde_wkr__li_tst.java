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
public class Xop_xnde_wkr__li_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@After public void term() {fxt.Init_para_n_();}
	@Test  public void Inside_tblx() {	// PURPOSE: auto-close <li> (EX: "<li>a<li>") was causing 3rd <li> to close incorrectly
		fxt.Test_parse_page_wiki_str
			(	"<table><tr><td><ul><li>a</li><li>b</li><li>c</li></ul></td></tr></table>"
			,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td><ul>"
			,	"<li>a</li>"
			,	"<li>b</li>"
			,	"<li>c</li></ul>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test   public void Li_nested_inside_ul() {	// PURPOSE: nested li in ul should not be escaped; DATE:2013-12-04
		fxt.Test_parse_page_wiki_str
		(	"<ul><li>a<ul><li>b</li></ul></li></ul>"
		,	String_.Concat_lines_nl_skipLast
		(	"<ul>"
		,	"<li>a<ul>"
		,	"<li>b</li></ul></li></ul>"	// note that <li><li>b becomes <li>&lt;li>b but <li><ul><li>b should stay the same
		));
	}
	@Test  public void Empty_ignored() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>a"
			,	"</li><li>"
			,	"</li><li>b"
			,	"</li>"
			,	"</ul>"
			), String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>a"
			,	"</li>"
			,	"<li>b"
			,	"</li>"
			,	"</ul>"
			));
	}
	@Test  public void Empty_ignored_error() { // EX:WP:Sukhoi Su-47; "* </li>" causes error b/c </li> tries to close non-existent node
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"* a"
			,	"* </li>"
			), String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"  <li> a"
			,	"  </li>"
			,	"  <li> "
			,	"  </li>"
			,	"</ul>"
			));
	}
	@Test  public void Ul_implied() {
		fxt.Test_parse_page_wiki_str
			(	"<li>a</li>", String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>a</li></ul>"
			));
	}
	@Test  public void Insert_nl() {// PURPOSE: <li> should always be separated by nl, or else items will merge, creating long horizontal scroll bar; EX:w:Music
		fxt.Init_para_y_();
		fxt.Test_parse_page_all_str("<ul><li>a</li><li>b</li></ul>"
			,	String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>a</li>"
			,	"<li>b</li></ul>"
			,	""
			));
		fxt.Init_para_n_();
	}
	@Test  public void Dupe_list() {	// PURPOSE: ignore redundant li; EX: "* <li>"; http://it.wikipedia.org/wiki/Milano#Bibliographie; DATE:2013-07-23
		fxt.Test_parse_page_all_str("* <li>x</li>", String_.Concat_lines_nl_skipLast
		(	"<ul>"
		,	"  <li> "
		,	"x"
		,	"  </li>"
		,	"</ul>"
		));
	}
}

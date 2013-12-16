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
package gplx.xowa.xtns.poems; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Xtn_poem_nde_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Lines() {	// NOTE: first \n (poem\n) and last \n (\n</poem>)ignored
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<poem>"
			,	"a"
			,	"b"
			,	"c"
			,	"d"
			,	"</poem>"
			), String_.Concat_lines_nl_skipLast
			(	"<div class=\"poem\">"
			,	"a<br/>"
			,	"b<br/>"
			,	"c<br/>"
			,	"d"
			,	"</div>"
			));
	}
	@Test  public void Indent() {
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
				(	"<poem>"
				,	"a 1"
				,	"  b 1"
				,	"c 1"
				,	"  d 1"
				,	"</poem>"
				), String_.Concat_lines_nl_skipLast
				(	"<div class=\"poem\">"
				,	"a 1<br/>"
				,	"&#160;&#160;b 1<br/>"
				,	"c 1<br/>"
				,	"&#160;&#160;d 1"
				,	"</div>"
				));
	}
	@Test  public void Indent_2() {	// PURPOSE: indent on 1st line caused page_break
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<poem>"
			,	"  a"
			,	"  b"
			,	"  c"
			,	"  d"
			,	"</poem>"
			), String_.Concat_lines_nl_skipLast
			(	"<div class=\"poem\">"
			,	"&#160;&#160;a<br/>"
			,	"&#160;&#160;b<br/>"
			,	"&#160;&#160;c<br/>"
			,	"&#160;&#160;d"
			,	"</div>"
			));
	}
	@Test  public void List() {
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
				(	"<poem>"
				,	":a"
				,	":b"
				,	"</poem>"
				), String_.Concat_lines_nl_skipLast
				(	"<div class=\"poem\">"
				,	""			// NOTE: this blank line occurs b/c <div>\n and \n<dl>; TODO: remove; WHEN: whitespace
				,	"<dl>"
				,	"  <dd>a"
				,	"  </dd>"
				,	"  <dd>b"
				,	"  </dd>"
				,	"</dl>"
				,	"</div>"
				));
	}
	@Test  public void Xtn() {
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_print", "{{{1}}}");
		fxt.ini_defn_add("test_outer", String_.Concat_lines_nl_skipLast
			(	"{{test_print|a <poem>b}}c</poem>}}"
			));
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
				(	"{{test_outer}}"
//					(	"{{test_print|a <poem>b}}c</poem>}}"
				), String_.Concat_lines_nl_skipLast
				(	"a <div class=\"poem\">"
				,	"b}}c"
				,	"</div>"
				));
		fxt.ini_defn_clear();
	}
	@Test  public void Comment() {
		fxt.tst_Parse_page_wiki_str("<poem>a<!-- b --> c</poem>", String_.Concat_lines_nl_skipLast
			(	"<div class=\"poem\">"
			,	"a c"
			,	"</div>"
			));
	}
	@Test  public void Err_empty_line() {
		fxt.tst_Parse_page_wiki_str("<poem>\n</poem>", String_.Concat_lines_nl_skipLast
			(	"<div class=\"poem\">"
			,	""
			,	"</div>"
			));
	}
	@Test  public void Ref() {
		fxt.tst_Parse_page_all_str
			(	String_.Concat_lines_nl_skipLast
			(	"<poem>a<ref>b</ref></poem>"
			,	"<references/>"), String_.Concat_lines_nl_skipLast
			(	"<div class=\"poem\">"
			,	"a<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
			,	"</div>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">b</span></li>"
			,	"</ol>"
			,	""
			));
	}
}

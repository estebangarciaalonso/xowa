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
package gplx.xowa.xtns.refs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Xtn_references_nde_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {fxt.Ctx().Page().Ref_mgr().Grps_clear();}
	@Test  public void Basic() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref>x</ref>"
			,	"<ref>y</ref>"
			,	"<ref>z</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
			,	"<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[2]</a></sup>"
			,	"<sup id=\"cite_ref-2\" class=\"reference\"><a href=\"#cite_note-2\">[3]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">x</span></li>"
			,	"<li id=\"cite_note-1\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-1\">^</a></span> <span class=\"reference-text\">y</span></li>"
			,	"<li id=\"cite_note-2\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-2\">^</a></span> <span class=\"reference-text\">z</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Name_dif() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref name='r_x'>x</ref>"
			,	"<ref name='r_y'>y</ref>"
			,	"<ref name='r_z'>z</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-r_x_0-0\" class=\"reference\"><a href=\"#cite_note-r_x-0\">[1]</a></sup>"
			,	"<sup id=\"cite_ref-r_y_1-0\" class=\"reference\"><a href=\"#cite_note-r_y-1\">[2]</a></sup>"
			,	"<sup id=\"cite_ref-r_z_2-0\" class=\"reference\"><a href=\"#cite_note-r_z-2\">[3]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-r_x-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-r_x_0-0\">^</a></span> <span class=\"reference-text\">x</span></li>"
			,	"<li id=\"cite_note-r_y-1\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-r_y_1-0\">^</a></span> <span class=\"reference-text\">y</span></li>"
			,	"<li id=\"cite_note-r_z-2\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-r_z_2-0\">^</a></span> <span class=\"reference-text\">z</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Name_same() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref name='r_x'>x</ref>"
			,	"<ref name='r_y'>y</ref>"
			,	"<ref name='r_x'>z</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-r_x_0-0\" class=\"reference\"><a href=\"#cite_note-r_x-0\">[1]</a></sup>"
			,	"<sup id=\"cite_ref-r_y_1-0\" class=\"reference\"><a href=\"#cite_note-r_y-1\">[2]</a></sup>"
			,	"<sup id=\"cite_ref-r_x_0-1\" class=\"reference\"><a href=\"#cite_note-r_x-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-r_x-0\"><span class=\"mw-cite-backlink\">^ <sup><a href=\"#cite_ref-r_x_0-0\">a</a></sup> <sup><a href=\"#cite_ref-r_x_0-1\">b</a></sup></span> <span class=\"reference-text\">x</span></li>"
			,	"<li id=\"cite_note-r_y-1\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-r_y_1-0\">^</a></span> <span class=\"reference-text\">y</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Name_same_text_in_last_ref() {	// WP:Hundred Years' War
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref name='ref_a'></ref>"
			,	"<ref name='ref_a'></ref>"
			,	"<ref name='ref_a'>x</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-ref_a_0-0\" class=\"reference\"><a href=\"#cite_note-ref_a-0\">[1]</a></sup>"
			,	"<sup id=\"cite_ref-ref_a_0-1\" class=\"reference\"><a href=\"#cite_note-ref_a-0\">[1]</a></sup>"
			,	"<sup id=\"cite_ref-ref_a_0-2\" class=\"reference\"><a href=\"#cite_note-ref_a-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-ref_a-0\"><span class=\"mw-cite-backlink\">^ <sup><a href=\"#cite_ref-ref_a_0-0\">a</a></sup> <sup><a href=\"#cite_ref-ref_a_0-1\">b</a></sup> <sup><a href=\"#cite_ref-ref_a_0-2\">c</a></sup></span> <span class=\"reference-text\">x</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Group() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref>x</ref>"
			,	"<ref group='group_a'>y</ref>"
			,	"<ref>z</ref>"
			,	"<references group='group_a'/>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
			,	"<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[group_a 1]</a></sup>"
			,	"<sup id=\"cite_ref-2\" class=\"reference\"><a href=\"#cite_note-2\">[2]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-1\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-1\">^</a></span> <span class=\"reference-text\">y</span></li>"
			,	"</ol>"
			,	""
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">x</span></li>"
			,	"<li id=\"cite_note-2\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-2\">^</a></span> <span class=\"reference-text\">z</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Pre_ignored() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref> x</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\"> x</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Pre_ignored_2() {	// PURPOSE: <ref> creates <li> which will effectively disable all pre; EX.WP: Robert Browning
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref>x"
			,	" y"
			,	"</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">x"
			,	" y"
			,	"</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void List_ignored() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref>*x</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">*x</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Name_mixed_case() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref NAME=A>x</ref>"
			,	"<ref Name=A>y</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-A_0-0\" class=\"reference\"><a href=\"#cite_note-A-0\">[1]</a></sup>"
			,	"<sup id=\"cite_ref-A_0-1\" class=\"reference\"><a href=\"#cite_note-A-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-A-0\"><span class=\"mw-cite-backlink\">^ <sup><a href=\"#cite_ref-A_0-0\">a</a></sup> <sup><a href=\"#cite_ref-A_0-1\">b</a></sup></span> <span class=\"reference-text\">x</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Name2() { // PURPOSE: make sure inline tag matches open tag
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"a<ref name=name_0>b</ref>"
			,	"b<ref name=name_0/>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"a<sup id=\"cite_ref-name_0_0-0\" class=\"reference\"><a href=\"#cite_note-name_0-0\">[1]</a></sup>"
			,	"b<sup id=\"cite_ref-name_0_0-1\" class=\"reference\"><a href=\"#cite_note-name_0-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-name_0-0\"><span class=\"mw-cite-backlink\">^ <sup><a href=\"#cite_ref-name_0_0-0\">a</a></sup> <sup><a href=\"#cite_ref-name_0_0-1\">b</a></sup></span> <span class=\"reference-text\">b</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void References_refs() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"a<ref group=group_0 name=name_0/>"
			,	"<references group=group_0>"
			,	"  <ref name=name_0>b</ref>"
			,	"</references>"
			), String_.Concat_lines_nl_skipLast
			(	"a<sup id=\"cite_ref-name_0_0-0\" class=\"reference\"><a href=\"#cite_note-name_0-0\">[group_0 1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-name_0-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-name_0_0-0\">^</a></span> <span class=\"reference-text\">b</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Nested() {	// PURPOSE: nested ref was creating 3rd [1]
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"a<ref name=r_x/>"
			,	"b<ref name=r_x/>"
			,	"<references>"
			,	"<ref name=r_x>c</ref>"
			,	"</references>"
			), String_.Concat_lines_nl_skipLast
			(	"a<sup id=\"cite_ref-r_x_0-0\" class=\"reference\"><a href=\"#cite_note-r_x-0\">[1]</a></sup>"
			,	"b<sup id=\"cite_ref-r_x_0-1\" class=\"reference\"><a href=\"#cite_note-r_x-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-r_x-0\"><span class=\"mw-cite-backlink\">^ <sup><a href=\"#cite_ref-r_x_0-0\">a</a></sup> <sup><a href=\"#cite_ref-r_x_0-1\">b</a></sup></span> <span class=\"reference-text\">c</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Key_ignore_nl_tab() {	// PURPOSE: \n in ref_name should be escaped to \s; EX.WP:Self-Transcendence 3100 Mile Race
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref name=\"name\na\">b</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-name_a_0-0\" class=\"reference\"><a href=\"#cite_note-name_a-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-name_a-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-name_a_0-0\">^</a></span> <span class=\"reference-text\">b</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Empty_name() {	// PURPOSE: <references group=""/> is same as <references/>; DATE:2013-02-06
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref>b</ref>"
			,	"<references group=\"\"/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">b</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Multiple_same_name_groups() {	// PURPOSE: multiple groups with same name "clears" out references; DATE:2013-02-11
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref>a</ref>"
			,	"<references/>"
			,	"<ref>b</ref>"
			,	"<references/>"
			,	"<ref>c</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">a</span></li>"
			,	"</ol>"
			,	""
			,	"<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-1\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-1\">^</a></span> <span class=\"reference-text\">b</span></li>"
			,	"</ol>"
			,	""
			,	"<sup id=\"cite_ref-2\" class=\"reference\"><a href=\"#cite_note-2\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-2\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-2\">^</a></span> <span class=\"reference-text\">c</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test   public void Empty_group() {	// PURPOSE: group without items should be blank; should not throw error; DATE:2013-02-12
		fxt.Test_parse_page_wiki_str("<references name='group_a'/>", "");
	}
	@Test   public void Empty_group_before_ref() {	// PURPOSE: empty grp before itm should not throw error; DATE:2013-02-18; EX: w:Help:External links and references; Johnstown,_Colorado
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl
			(	"<references/><references/>a<ref>test</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl
			(	"a<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">test</span></li>"
			,	"</ol>"
			,	""
			));
	}
	@Test  public void Follow() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ref name='ref_a'>x</ref>"
			,	"<ref>y</ref>"
			,	"<ref follow='ref_a'>z</ref>"
			,	"<references/>"
			), String_.Concat_lines_nl_skipLast
			(	"<sup id=\"cite_ref-ref_a_0-0\" class=\"reference\"><a href=\"#cite_note-ref_a-0\">[1]</a></sup>"
			,	"<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[2]</a></sup>"
			,	""
			,	"<ol class=\"references\">"
			,	"<li id=\"cite_note-ref_a-0\"><span class=\"mw-cite-backlink\">^ <sup><a href=\"#cite_ref-ref_a_0-0\">a</a></sup></span> <span class=\"reference-text\">x z</span></li>"
			,	"<li id=\"cite_note-1\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-1\">^</a></span> <span class=\"reference-text\">y</span></li>"
			,	"</ol>"
			,	""
			));
	}

//		@Test  public void Tag() { // PURPOSE: #tag can create nested refs; TODO: WHEN: nested ref test
//			fxt.Test_parse_wiki(String_.Concat_lines_nl_skipLast
//				(	"{{#tag:ref|x<ref>y</ref>}}" //"<ref>x<ref>y</ref></ref>"
//				,	"<references/>"
//				), String_.Concat_lines_nl_skipLast
//				(	"<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[2]</a></sup>"
//				,	"<ol class=\"references\">"
//				,	"<li id=\"cite_note-0\"><a href=\"#cite_ref-0\">^</a> y</li>"
//				,	"<li id=\"cite_note-1\"><a href=\"#cite_ref-1\">^</a> x<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup></li>"
//				,	"</ol>"
//				,	""
//				));
//		}
//		@Test  public void Tag_2() { // PURPOSE: more involved nested refs; EX.WP:Battle of Midway; TODO: WHEN: nested ref test
//			fxt.Test_parse_wiki(String_.Concat_lines_nl_skipLast
//				(	"a<ref name='itm_0'/> b {{#tag:ref|c<ref name='itm_0'>d</ref>}}"	// <ref>c<ref name='itm_0'>d</ref></ref>
//				,	"<references/>"
//				), String_.Concat_lines_nl_skipLast
//				(	"a<sup id=\"cite_ref-ref_a_0-0\" class=\"reference\"><a href=\"#cite_note-ref_a-0\">[1]</a></sup> b <sup id=\"cite_ref-2\" class=\"reference\"><a href=\"#cite_note-2\">[2]</a></sup>"
//				,	"<ol class=\"references\">"
//				,	"<li id=\"cite_note-ref_a-0\">^ <sup><a href=\"#cite_ref-ref_a_0-0\">a</a></sup> <sup><a href=\"#cite_ref-ref_a_0-1\">b</a></sup> d</li>"
//				,	"<li id=\"cite_note-2\"><a href=\"#cite_ref-2\">^</a> c<sup id=\"cite_ref-ref_a_0-0\" class=\"reference\"><a href=\"#cite_note-ref_a-0\">[1]</a></sup></li>"
//				,	"</ol>"
//				,	""
//				));
//		}
}
/*
*/
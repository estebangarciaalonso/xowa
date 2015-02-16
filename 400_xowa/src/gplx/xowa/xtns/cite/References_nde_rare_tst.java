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
package gplx.xowa.xtns.cite; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class References_nde_rare_tst {		
	@Before public void init() {fxt.Clear_ref_mgr(); fxt.Reset();} private Xop_fxt fxt = new Xop_fxt();
	@After public void term() {fxt.Init_para_n_();}
	@Test  public void Recursive() {	// PURPOSE: handle recursive situations; EX: ja.w:Kソリューション ; ja.w:Template:cite web。; DATE:2014-03-05
		fxt.Init_page_create("Template:Recursive", "<ref>{{Recursive}}</ref>");
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skip_last
		(	"<ref>{{Recursive}}</ref>"
		,	"<references/>"
		), String_.Concat_lines_nl_skip_last
		(	"<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[2]</a></sup>"
		,	"<ol class=\"references\">"
		,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\"><span class=\"error\">Template loop detected:Recursive</span></span></li>"
		,	"<li id=\"cite_note-1\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-1\">^</a></span> <span class=\"reference-text\"><sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup></span></li>"
		,	"</ol>"
		,	""
		));
	}
	@Test  public void Backlabel_out_of_range() {	// PURPOSE: handle more backlabels than expected; PAGE:en.w:List_of_Russula_species; DATE:2014-06-07
		Ref_html_wtr_cfg cfg = fxt.Wiki().Html_mgr().Html_wtr().Ref_wtr().Cfg();
		byte[][] old = cfg.Backlabels();
		cfg.Backlabels_(Bry_.Ary("a"));
		fxt.Wiki().Msg_mgr().Get_or_make(Ref_html_wtr_cfg.Msg_backlabels_err).Atrs_set(Bry_.new_ascii_("Ran out of custom link labels for group ~{0}."), true, false);
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skip_last
		(	"<ref name='ref_1'>a</ref><ref name='ref_1'>b</ref>"
		,	"<references/>"
		), String_.Concat_lines_nl_skip_last
		(	"<sup id=\"cite_ref-ref_1_0-0\" class=\"reference\"><a href=\"#cite_note-ref_1-0\">[1]</a></sup><sup id=\"cite_ref-ref_1_0-1\" class=\"reference\"><a href=\"#cite_note-ref_1-0\">[1]</a></sup>"
		,	"<ol class=\"references\">"
		,	"<li id=\"cite_note-ref_1-0\"><span class=\"mw-cite-backlink\">^ <sup><a href=\"#cite_ref-ref_1_0-0\">a</a></sup> <sup><a href=\"#cite_ref-ref_1_0-1\">Ran out of custom link labels for group 1.</a></sup></span> <span class=\"reference-text\">a</span></li>"
		,	"</ol>"
		,	""
		));
		cfg.Backlabels_(old);
	}
	@Test  public void Tag() { // PURPOSE: #tag can create nested refs; PAGE:en.w:Battle_of_Midway DATE:2014-06-27
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skip_last
		( "{{#tag:ref|x<ref>y</ref>}}" //"<ref>x<ref>y</ref></ref>"
		, "<references/>"
		), String_.Concat_lines_nl_skip_last
		( "<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[2]</a></sup>"
		, "<ol class=\"references\">"
		, "<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">y</span></li>"
		, "<li id=\"cite_note-1\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-1\">^</a></span> <span class=\"reference-text\">x<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup></span></li>"
		, "</ol>"
		, ""
		));
	}
	@Test  public void Tag_2() { // PURPOSE: more involved nested refs; PAGE:en.w:Battle_of_Midway DATE:2014-06-27
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skip_last
		( "a<ref name='itm_0'/> b {{#tag:ref|c<ref name='itm_0'>d</ref>}}"	// <ref>c<ref name='itm_0'>d</ref></ref>
		, "<references/>"
		), String_.Concat_lines_nl_skip_last
		( "a<sup id=\"cite_ref-itm_0_0-0\" class=\"reference\"><a href=\"#cite_note-itm_0-0\">[1]</a></sup> b <sup id=\"cite_ref-2\" class=\"reference\"><a href=\"#cite_note-2\">[2]</a></sup>"
		, "<ol class=\"references\">"
		, "<li id=\"cite_note-itm_0-0\"><span class=\"mw-cite-backlink\">^ <sup><a href=\"#cite_ref-itm_0_0-0\">a</a></sup> <sup><a href=\"#cite_ref-itm_0_0-1\">b</a></sup></span> <span class=\"reference-text\">d</span></li>"
		, "<li id=\"cite_note-2\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-2\">^</a></span> <span class=\"reference-text\">c<sup id=\"cite_ref-itm_0_0-1\" class=\"reference\"><a href=\"#cite_note-itm_0-0\">[1]</a></sup></span></li>"
		, "</ol>"
		, ""
		));
	}
	@Test  public void Dangling_references__nested() { // PURPOSE: handle nested <references/>; PAGE:en.w:Hwair; DATE:2014-06-27
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skip_last
		( "<ref>a</ref>"
		, "<references>"
		, "  <references/>"		// must be ignored, else it will become owner for <ref>
		, "</references>"
		), String_.Concat_lines_nl_skip_last
		( "<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
		, "<ol class=\"references\">"
		, "<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">a</span></li>"
		, "</ol>"
		, ""
		));
	}
}

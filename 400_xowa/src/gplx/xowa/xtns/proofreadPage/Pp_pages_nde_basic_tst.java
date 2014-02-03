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
package gplx.xowa.xtns.proofreadPage; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Pp_pages_nde_basic_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void Init() {
		Io_mgr._.InitEngine_mem();
		fxt.Wiki().Db_mgr().Load_mgr().Clear(); // must clear; otherwise fails b/c files get deleted, but wiki.data_mgr caches the Xowd_regy_mgr (the .reg file) in memory;
		fxt.Wiki().Ns_mgr().Add_new(Xowc_xtn_pages.Ns_page_id_default, "Page").Add_new(Xowc_xtn_pages.Ns_index_id_default, "Index").Ords_sort();
	}
	@Test  public void Basic() {
		fxt.ini_page_create("Page:A/1", "abc");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=1 />", String_.Concat_lines_nl
		(	"<p>abc "
		,	"</p>"
		,	""
		));
	}
	@Test  public void Multiple() {
		fxt.ini_page_create("Page:A/1", "a");
		fxt.ini_page_create("Page:A/2", "b");
		fxt.ini_page_create("Page:A/3", "c");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=3 />", String_.Concat_lines_nl
		(	"<p>a b c "
		,	"</p>"
		,	""
		));
	}
	@Test  public void Section() {
		fxt.ini_page_create("Page:A/1", "a<section begin=\"sect_0\"/>b");
		fxt.ini_page_create("Page:A/2", "cd");
		fxt.ini_page_create("Page:A/3", "e<section end=\"sect_2\"/>f");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=3 fromsection='sect_0' tosection='sect_2' />", String_.Concat_lines_nl
		(	"<p>b cd e "
		,	"</p>"
		));
	}
	@Test  public void Noinclude() {
		fxt.ini_page_create("Page:A/1", "<noinclude>a</noinclude>{|\n|b\n|}");
		fxt.ini_page_create("Page:A/2", "<noinclude>c</noinclude>''d''");
		fxt.ini_page_create("Page:A/3", "<noinclude>e</noinclude>f");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=3 />", String_.Concat_lines_nl_skipLast
		(	"<table>"
		,	"  <tr>"
		,	"    <td>b"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		,	" <i>d</i> f "
		));
	}
	@Test  public void Err_page_ns_doesnt_exist() {
		fxt.Wiki().Ns_mgr_(Xow_ns_mgr_.default_());
		fxt.Wiki().Cfg_parser().Xtns().Itm_pages().Reset();	// must reset to clear cached valid ns_page from previous tests
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=3 />", "&lt;pages index=&quot;A&quot; from=1 to=3 /&gt;");
		fxt.Wiki().Cfg_parser().Xtns().Itm_pages().Reset();	// must reset to clear cached invalid ns_page for next tests
	}
	@Test  public void Subpage() {	// PURPOSE: [[/Page]] should be relative to current page; EX: Flatland and [[/First World]]; DATE:2013-04-29
		fxt.ini_page_create("Page:A/1", "[[/Sub1|Sub 1]]");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=1 />", String_.Concat_lines_nl
		(	"<p><a href=\"/wiki/Test_page/Sub1\">Sub 1</a> "	// NOTE: / is relative to Page_name (in this case Test_page)
		,	"</p>"
		));
	}
	@Test  public void Recursive_page() {	// PURPOSE: handle recursive calls on page; EX: fr.s:Page:NRF_19.djvu/19; DATE:2014-01-01
		fxt.ini_page_create("Page:A/1", "<pages index=\"A\" from=1 to=1 />abc");	// NOTE: recursive call to self
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=1 />", String_.Concat_lines_nl
		(	"<p>abc "
		,	"</p>"
		,	""
		));
	}
	@Test  public void Recursive_index() {	// PURPOSE: handle recursive calls on index; EX: en.s:Poems_of_Italy:_selections_from_the_Odes_of_Giosue_Carducci/Before_the_Old_Castle_of_Verona; DATE:2014-01-19
		fxt.ini_page_create("Index:A", "<pages index=A/>");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=1 />", "<p> \n</p>");
	}
	@Test  public void Repeated() {	// PURPOSE: repeated pages should still show (and not be excluded by recursive logic); DATE:2014-01-01
		fxt.ini_page_create("Page:A/1", "<pages index=\"A\" from=1 to=1 />abc");	// NOTE: recursive call to self
		fxt.ini_page_create("Page:D/1", "d");
		String main_txt = String_.Concat_lines_nl
		(	"<pages index=\"A\" from=1 to=1 />"
		,	"text_0"
		,	"<pages index=\"D\" from=1 to=1/>"
		,	"text_1"
		,	"<pages index=\"D\" from=1 to=1/>"
		); 
		fxt.tst_Parse_page_wiki_str(main_txt, String_.Concat_lines_nl
		(	"<p>abc "
		,	"</p>"
		,	""
		,	"text_0"
		,	"<p>d "
		,	"</p>"
		,	""
		,	"text_1"
		,	"<p>d "
		,	"</p>"
		));
	}
	@Test  public void Index() {
		fxt.ini_page_create("Index:A", String_.Concat_lines_nl
		(	"[[ignore]]"
		,	"[[Page:A b/1]]"
		,	"[[Page:A b/2]]"
		,	"[[Page:A b/3]]"
		,	"[[Page:A b/4]]"
		,	"[[Page:A b/5]]"
		));
		fxt.ini_page_create("Page:A_b/1", "A_b/1\n");
		fxt.ini_page_create("Page:A_b/2", "A_b/2\n");
		fxt.ini_page_create("Page:A_b/3", "A_b/3\n");
		fxt.ini_page_create("Page:A_b/4", "A_b/4\n");
		fxt.ini_page_create("Page:A_b/5", "A_b/5\n");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from='A b/2' to='A_b/4' />", String_.Concat_lines_nl
		(	"<p>A_b/2"
		,	" A_b/3"
		,	" A_b/4"
		,	" "
		,	"</p>"
		));
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from='A b/2' />", String_.Concat_lines_nl		// to missing
		(	"<p>A_b/2"
		,	" A_b/3"
		,	" A_b/4"
		,	" A_b/5"
		,	" "
		,	"</p>"
		));
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" to='A b/4' />", String_.Concat_lines_nl		// from missing
		(	"<p>A_b/1"
		,	" A_b/2"
		,	" A_b/3"
		,	" A_b/4"
		,	" "
		,	"</p>"
		));
	}
	@Test  public void Index_amp_encoded() {	// handle ampersand encoded strings; EX: en.s:Team_Work_Wins!; DATE:2014-01-19
		fxt.ini_page_create("Index:\"A\"", "[[Page:\"A\"]]");
		fxt.ini_page_create("Page:\"A\"", "a");
		fxt.tst_Parse_page_wiki_str("<pages index=\"&quot;A&quot;\" from='&quot;A&quot;' />", "<p>a \n</p>");
	}
//		@Test  public void Index_all() {	// PURPOSE: if from / to not specified, add all titles
//			fxt.ini_page_create("Index:A", String_.Concat_lines_nl
//			(	"[[Page:A b/1]]"
//			,	"[[Page:A b/2]]"
//			));
//			fxt.ini_page_create("Page:A_b/1", "A_b/1\n");
//			fxt.ini_page_create("Page:A_b/2", "A_b/2\n");
//			String main_txt = String_.Concat_lines_nl
//			(	"<pages index=\"A\" />"
//			); 
//			fxt.tst_Parse_page_wiki_str(main_txt, String_.Concat_lines_nl
//			(	"<p>A_b/1"
//			,	"A_b/2"
//			,	"</p>"
//			));
//		}
	@Test  public void Section_failed_when_xnde() {	// PURPOSE: section failed to be retrieved if preceding xnde; DATE:2014-01-15
		fxt.ini_page_create("Page:A/1", "<b>a</b><section begin=\"sect_0\"/>b<section end=\"sect_0\"/>");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=1 fromsection='sect_0' tosection='sect_0' />", String_.Concat_lines_nl
		(	"<p>b "
		,	"</p>"
		));
	}
	@Test  public void Index_to_missing() {	// PURPOSE: if no to, get rest of pages
		fxt.ini_page_create("Index:A", String_.Concat_lines_nl
		(	"[[Page:A b/1]]"
		,	"[[Page:A b/2]]"
		));
		fxt.ini_page_create("Page:A_b/1", "A_b/1\n");
		fxt.ini_page_create("Page:A_b/2", "A_b/2\n");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from='A b/1' />", String_.Concat_lines_nl
		(	"<p>A_b/1"
		,	" A_b/2"
		,	" "
		,	"</p>"
		));
	}
	@Test  public void Various() {
		fxt.ini_page_create("Page:A/1", "a");
		fxt.ini_page_create("Page:A/2", "b");
		fxt.ini_page_create("Page:A/3", "c");
		fxt.ini_page_create("Page:A/4", "d");
		fxt.ini_page_create("Page:A/5", "e");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" include='1-2,4' />", "<p>a b d \n</p>\n");				// include
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=5 exclude='3' />", "<p>a b d e \n</p>\n");	// exclude
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" include=5 from=2 to=4 />", "<p>b c d e \n</p>\n");		// include should be sorted
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=5 step=2 />", "<p>a c e \n</p>\n");			// step
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=5 step=4 />", "<p>a e \n</p>\n");				// step
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=5 step=10 />", "<p>a \n</p>\n");				// step
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=3 to=3 />", "<p>c \n</p>\n");						// from = to
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" to=3/>", "<p> a b c \n</p>\n");							// from omitted
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=3/>", "<p>c d e \n</p>\n");						// to omitted
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from='' to=3 />", "<p> a b c \n</p>\n");					// from is blank
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=3 to=''/>", "<p>c d e \n</p>\n");					// to is blank
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=3 to='4.' />", "<p>c d \n</p>\n");					// allow decimal-like number; EX:en.w:Haworth's/Chapter_XIX; DATE:2014-01-19
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=5 exclude=''3' />", "<p>a b c d e \n</p>\n");	// exclude is invalid; EX:fr.s:Sanguis_martyrum/Premi�re_partie/I DATE:2014-01-18
	}
	@Test  public void Ref() {	// PURPOSE: ref on page should show; DATE:2014-01-18
		fxt.ini_page_create("Page:A/1", "a<ref>b</ref>c");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=1 /><references/>", String_.Concat_lines_nl
		( "<p>a<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>c "
		, "</p>"
		, "<ol class=\"references\">"
		, "<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">b</span></li>"
		, "</ol>"
		));
	}
	@Test  public void Tblw() {	// PURPOSE: if page begins with *, {|, etc, , automatically prepend \n (just like templates); DATE:2014-01-23
		fxt.ini_page_create("Page:A/1", "a");
		fxt.ini_page_create("Page:A/2", "* b");
		fxt.ini_page_create("Page:A/3", "c");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=3 />", String_.Concat_lines_nl
		(	"<p>a "
		,	"</p>"
		,	""
		,	"<ul>"
		,	"  <li> b c "
		,	"  </li>"
		,	"</ul>"
		,	""
		));
	}
	@Test  public void Index_various() {// varying logic depending on whether [[Index:]] has [[Page]] or <pagelist> DATE:2014-01-27
		fxt.ini_page_create("Page:A/0", "A/0");
		fxt.ini_page_create("Page:A/1", "A/1");
		fxt.ini_page_create("Page:A/2", "A/2");
		fxt.ini_page_create("Index:A", "");

		// [[Index:]] has no [[Page:]] links; interpret to=1 as [[Page:A/1]]
		fxt.ini_page_update("Index:A" , String_.Concat_lines_nl
		( "no links"
		));
		fxt.tst_Parse_page_wiki_str("<pages index='A' to=1 />", String_.Concat_lines_nl
		( "<p>A/0 A/1 "
		, "</p>"
		));

		// [[Index:]] has [[Page:]] links; interpret to=1 as 1st [[Page:]] in [[Index:]]'s [[Page:]] links
		fxt.ini_page_update("Index:A" , String_.Concat_lines_nl
		( "[[Page:A/0]]"
		));
		fxt.tst_Parse_page_wiki_str("<pages index='A' to=1 />", String_.Concat_lines_nl
		( "<p>A/0 "
		, "</p>"
		));

		// [[Index:]] has [[Page:]] links but also <pagelist>; interpret to=1 as [[Page:A/1]]
		fxt.ini_page_update("Index:A" , String_.Concat_lines_nl
		( "[[Page:A/0]]"
		, "<pagelist/>"
		));
		fxt.tst_Parse_page_wiki_str("<pages index='A' to=1 />", String_.Concat_lines_nl
		( "<p>A/0 A/1 "
		, "</p>"
		));
	}
}

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
package gplx.xowa.xtns.lst; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Xtn_pages_nde_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Before public void Init() {
		Io_mgr._.InitEngine_mem();
		fxt.Wiki().Db_mgr().Load_mgr().Clear(); // must clear; otherwise fails b/c files get deleted, but wiki.data_mgr caches the Xowd_regy_mgr (the .reg file) in memory;
		fxt.Wiki().Ns_mgr().Add_new(Xowc_xtn_pages.Ns_page_id_default, "Page").Ords_sort();
	}
	@Test  public void Basic() {
		fxt.ini_page_create("Page:A/1", "abc");
		String main_txt = String_.Concat_lines_nl
		(	"<pages index=\"A\" from=1 to=1 />"
		); 
		fxt.tst_Parse_page_wiki_str(main_txt, String_.Concat_lines_nl
		(	"<p>abc"
		,	"</p>"
		,	""
		));
	}
	@Test  public void Multiple() {
		fxt.ini_page_create("Page:A/1", "a");
		fxt.ini_page_create("Page:A/2", "b");
		fxt.ini_page_create("Page:A/3", "c");
		String main_txt = String_.Concat_lines_nl
		(	"<pages index=\"A\" from=1 to=3 />"
		); 
		fxt.tst_Parse_page_wiki_str(main_txt, String_.Concat_lines_nl
		(	"<p>abc"
		,	"</p>"
		,	""
		));
	}
	@Test  public void Section() {
		fxt.ini_page_create("Page:A/1", "a<section begin=\"sect_0\"/>b");
		fxt.ini_page_create("Page:A/2", "cd");
		fxt.ini_page_create("Page:A/3", "e<section end=\"sect_2\"/>e");
		String main_txt = "<pages index=\"A\" from=1 to=3 fromsection='sect_0' tosection='sect_2' />"; 
		fxt.tst_Parse_page_wiki_str(main_txt, String_.Concat_lines_nl
		(	"<p>bcde"
		,	"</p>"
		));
	}
	@Test  public void Noinclude() {
		fxt.ini_page_create("Page:A/1", "<noinclude>a</noinclude>{|\n|b\n|}");
		fxt.ini_page_create("Page:A/2", "<noinclude>c</noinclude>''d''");
		fxt.ini_page_create("Page:A/3", "<noinclude>e</noinclude>f");
		String main_txt = "<pages index=\"A\" from=1 to=3 />"; 
		fxt.tst_Parse_page_wiki_str(main_txt, String_.Concat_lines_nl_skipLast
		(	"<table>"
		,	"  <tr>"
		,	"    <td>b"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		,	"<i>d</i>f"
		));
	}
	@Test  public void Err_page_ns_doesnt_exist() {
		fxt.Wiki().Ns_mgr_(Xow_ns_mgr_.default_());
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\" from=1 to=3 />", "&lt;pages index=&quot;A&quot; from=1 to=3 /&gt;");
	}
	@Test  public void Subpage() {	// PURPOSE: [[/Page]] should be relative to current page; EX: Flatland and [[/First World]]; DATE:2013-04-29
		fxt.ini_page_create("Page:A/1", "[[/Sub1|Sub 1]]");
		String main_txt = "<pages index=\"A\" from=1 to=1 />"; 
		fxt.tst_Parse_page_wiki_str(main_txt, String_.Concat_lines_nl
		(	"<p><a href=\"/wiki/Test_page/Sub1\">Sub 1</a>"	// NOTE: / is relative to Page_name (in this case Test_page)
		,	"</p>"
		));
	}
}

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
public class References_nde_rare_tst {		
	@Before public void init() {fxt.Ctx().Page().Ref_mgr().Grps_clear(); fxt.Reset();} private Xop_fxt fxt = new Xop_fxt();
	@After public void term() {fxt.Init_para_n_();}
	@Test  public void Recursive() {	// PURPOSE: handle recursive situations; EX: ja.w:Kソリューション ; ja.w:Template:cite web。; DATE:2014-03-05
		fxt.Init_page_create("Template:Recursive", "<ref>{{Recursive}}</ref>");
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	"<ref>{{Recursive}}</ref>"
		,	"<references/>"
		), String_.Concat_lines_nl_skipLast
		(	"<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[2]</a></sup>"
		,	"<ol class=\"references\">"
		,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\"><span class=\"error\">Template loop detected:Recursive</span></span></li>"
		,	"<li id=\"cite_note-1\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-1\">^</a></span> <span class=\"reference-text\"><sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup></span></li>"
		,	"</ol>"
		,	""
		));
	}
}

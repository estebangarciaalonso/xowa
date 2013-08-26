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
public class Xop_hdr_wkr_tst {
	@Before public void init() {fxt.Reset();} private Xop_fxt fxt = new Xop_fxt();
	@Test  public void H2()							{fxt.tst_Parse_page_wiki_str("==a=="				, "<h2>a</h2>\n");}
	@Test  public void H3()							{fxt.tst_Parse_page_wiki_str("===a==="				, "<h3>a</h3>\n");}
	@Test  public void H6_limit()					{fxt.tst_Parse_page_wiki_str("=======a======="		, "<h6>=a=</h6>\n");}
	@Test  public void Mismatch_bgn()				{fxt.tst_Parse_page_wiki_str("=====a=="				, "<h2>===a</h2>\n");}
	@Test  public void Mismatch_end()				{fxt.tst_Parse_page_wiki_str("==a====="				, "<h2>a===</h2>\n");}
	@Test  public void Dangling()					{fxt.tst_Parse_page_wiki_str("==a"					, "==a");}
	@Test  public void Comment_bgn()				{fxt.tst_Parse_page_all_str ("<!--b-->==a=="		, "<h2>a</h2>\n");}
	@Test  public void Comment_end()				{fxt.tst_Parse_page_all_str ("==a==<!--b-->"		, "<h2>a</h2>\n");}
	@Test  public void Ws_end() {	// PURPOSE: "==\n" merges all ws following it; \n\n\n is not transformed by Para_wkr to "<br/>"
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	"==a== \t"
		,	""
		,	""
		,	""
		,	"b"
		), String_.Concat_lines_nl_skipLast
		(	"<h2>a</h2>"
		,	""	// NOTE: multiple \n above gets combined into 1
		,	"b"
		));
	}
	@Test  public void Many() {
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	"==a=="
		,	"===b==="
		), String_.Concat_lines_nl_skipLast
		(	"<h2>a</h2>"
		,	""
		,	"<h3>b</h3>"
		,	""
		));
	}
	@Test  public void Hdr_w_tblw() {
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	"==a=="
		,	"{|"
		,	"|+"
		,	"|}"
		), String_.Concat_lines_nl_skipLast
		(	"<h2>a</h2>"
		,	"<table>"
		,	"  <caption>"
		,	"  </caption>"
		,	"</table>"
		,	""
		));
	}
	@Test  public void Hdr_w_hr() {
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	"==a=="
		,	"----"
		), String_.Concat_lines_nl_skipLast
		(	"<h2>a</h2>"
		,	"<hr/>"
		));
	}
	@Test  public void Mix_apos_dangling()			{fxt.tst_Parse_page_wiki_str("==''a=="				, "<h2><i>a</i></h2>\n");}
	@Test  public void Mix_xnde_dangling()			{fxt.tst_Parse_page_wiki_str("==<i>a=="				, "<h2><i>a</i></h2>\n");}
	@Test  public void Mix_tblw_cell()				{fxt.tst_Parse_page_wiki_str("==a!!=="				, "<h2>a!!</h2>\n");}
	@Test  public void Ws()							{fxt.tst_Parse_page_wiki_str("== a b =="			, "<h2> a b </h2>\n");}
	@Test  public void Err_hdr()					{fxt.ini_Log_(Xop_hdr_log.Mismatched)					.tst_Parse_page_wiki_str("====a== =="	, "<h2>==a== </h2>\n").tst_Log_check();}
	@Test  public void Err_end_hdr_is_1()			{fxt.ini_Log_(Xop_hdr_log.Mismatched, Xop_hdr_log.Len_1).tst_Parse_page_wiki_str("==a="			, "<h1>=a</h1>\n").tst_Log_check();}
	@Test  public void Html_hdr_many() {
		fxt.Hctx().Toc_show_(true);
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	"==a=="
		,	"==a=="
		,	"==a=="
		), String_.Concat_lines_nl_skipLast
		(	"<h2><span class='mw-headline' id='a'>a</span></h2>"
		,	""
		,	"<h2><span class='mw-headline' id='a_2'>a</span></h2>"
		,	""
		,	"<h2><span class='mw-headline' id='a_3'>a</span></h2>"
		,	""
		));
		fxt.Hctx().Toc_show_(false);
	}
}

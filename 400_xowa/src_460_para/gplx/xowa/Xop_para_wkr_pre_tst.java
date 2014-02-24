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
public class Xop_para_wkr_pre_tst {
	@Before public void init() {fxt.Reset(); fxt.Init_para_y_();} private Xop_fxt fxt = new Xop_fxt();
	@After public void teardown() {fxt.Init_para_n_();}
	@Test  public void Pre_ignore_bos() {			// PURPOSE: ignore pre at bgn; DATE:2013-07-09
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl
		(	" "
		,	"b"
		), String_.Concat_lines_nl
		(	"<p>"
		,	"b"
		,	"</p>"
		));
	}
	@Test  public void Pre_ignore_bos_tblw() {		// PURPOSE: ignore pre at bgn shouldn't break tblw; EX:commons.wikimedia.org; DATE:2013-07-11
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl
		(	" "
		,	"{|"
		,	"|-"
		,	"|a"
		,	"|}"
		), String_.Concat_lines_nl
		(	"<table>"
		,	"  <tr>"
		,	"    <td>a"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		));
	}
	@Test  public void Ignore_bos_xnde() {		// PURPOSE: space at bgn shouldn't create pre; EX:commons.wikimedia.org; " <center>a\n</center>"; DATE:2013-11-28
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	" <center>a"
		,	"</center>"
		),	String_.Concat_lines_nl_skipLast
		(	" <center>a"
		,	"</center>"
		,	""
		));
	}
}

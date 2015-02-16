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
package gplx.xowa.parsers.tblws; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import org.junit.*;
public class Xop_tblw_wkr__dangling_tst {
	@Before public void init() {fxt.Reset(); fxt.Init_para_y_();} private Xop_fxt fxt = new Xop_fxt();
	@After public void term() {fxt.Init_para_n_();}
	@Test  public void Dangling_tb_in_xnde() {// PURPOSE: dangling tblw incorrectly auto-closed by </xnde>; PAGE:en.w:Atlanta_Olympics; DATE:2014-03-18
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skip_last
		(	"<div align='center'>"
		,	"{|"
		,	"|-"
		,	"|"
		,	"{|"
		,	"|-"
		,	"|a"
		,	"|}"
		,	"</div>"
		,	"b"
		)		
		, String_.Concat_lines_nl
		(	"<div align='center'>"
		,	"<table>"
		,	"  <tr>"
		,	"    <td>"
		,	"      <table>"
		,	"        <tr>"
		,	"          <td>a"
		,	"          </td>"
		,	"        </tr>"
		,	"      </table>"
		,	"</div>"	// TIDY.dangling: tidy will correct dangling node; DATE:2014-07-22
		,	""
		,	"<p>b"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		,	"</div>"
		,	"</p>"
		));
	}
}

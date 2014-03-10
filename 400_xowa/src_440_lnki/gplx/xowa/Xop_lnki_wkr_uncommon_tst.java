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
import gplx.xowa.langs.casings.*;
public class Xop_lnki_wkr_uncommon_tst {		
	@Before public void init() {fxt.Reset(); fxt.Init_para_n_();} private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Double_bracket() {	// PURPOSE: handle [[[[A]]]] constructions; EX:ru.w:Италия; DATE:2014-02-04
		fxt.Test_parse_page_all_str("[[[[Test_1]]]]"		, "[[<a href=\"/wiki/Test_1\">Test_1</a>]]");
	}
	@Test  public void Failed_should_still_parse_xml() {	// PURPOSE: failed lnki should still parse xml in caption; EX:ar.w:الصومال; DATE:2014-03-04
		fxt.Test_parse_page_all_str("[[|<i>a</i>]]"			, "[[|<i>a</i>]]");
	}
	@Test  public void Pipe_only() {
		fxt.Test_parse_page_wiki("[[|]]", fxt.tkn_txt_(0, 2), fxt.tkn_pipe_(2), fxt.tkn_txt_(3, 5));
	}
	@Test  public void Nl_with_apos_shouldnt_fail() {	// PURPOSE: apos, lnki and nl will cause parser to fail; DATE:2013-10-31
		fxt.Test_parse_page_all_str("''[[\n]]", "<i>[[</i>\n]]");
	}
	@Test  public void Module() {	// PURPOSE: handle lnki_wkr parsing Module text (shouldn't happen); apos, tblw, lnki, and nl will cause parser to fail; EX:Module:Taxobox; DATE:2013-11-10
		fxt.Init_para_y_();
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl
		(	" [[''"
		,	" [["
		,	"  |"
		,	"]]"
		)
		,	String_.Concat_lines_nl_skipLast
		(	"<pre>[[<i>"
		,	"[["
		,	""
		,	"<p>"
		,	"  |]]</i>"
		,	"</p>"
		,	""
		));
		fxt.Init_para_n_();
	}
}


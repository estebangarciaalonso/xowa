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
package gplx.xowa.xtns.syntaxHighlight; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Xtn_syntaxHighlight_nde_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Test   public void Basic() {
		fxt.tst_Parse_page_all_str("<syntaxHighlight>abc</syntaxHighlight>", "<pre style=\"overflow:auto;\">abc</pre>");
	}
	@Test   public void Text() {
		fxt.tst_Parse_page_all_str("<syntaxHighlight lang=\"text\">abc</syntaxHighlight>", "<code>abc</code>");
	}
	@Test   public void Style_pre() {
		fxt.tst_Parse_page_all_str("<syntaxHighlight style=\"color:red;\">abc</syntaxHighlight>", "<pre style=\"overflow:auto;color:red;\">abc</pre>");
	}
	@Test   public void Style_code() {
		fxt.tst_Parse_page_all_str("<syntaxHighlight lang=\"text\" style=\"color:red;\">abc</syntaxHighlight>", "<code style=\"color:red;\">abc</code>");
	}
	@Test   public void Trim_ws() {
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	"<syntaxHighlight>"
		,	"abc"
		,	"</syntaxHighlight>"
		), String_.Concat_lines_nl
		(	"<pre style=\"overflow:auto;\">"
		,	"abc"
		,	"</pre>"
		));
	}
	@Test   public void Line() {
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	"<syntaxHighlight line>"
		,	"a"
		,	"b"
		,	"</syntaxHighlight>"
		), String_.Concat_lines_nl
		(	"<pre style=\"overflow:auto;\">"
		,	"<span style=\"-moz-user-select:none;\">1 </span><span>a</span>"
		,	"<span style=\"-moz-user-select:none;\">2 </span><span>b</span>"
		,	"</pre>"
		));
	}
	@Test   public void Start() {
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	"<syntaxHighlight line start=3>"
		,	"a"
		,	"b"
		,	"</syntaxHighlight>"
		), String_.Concat_lines_nl
		(	"<pre style=\"overflow:auto;\">"
		,	"<span style=\"-moz-user-select:none;\">3 </span><span>a</span>"
		,	"<span style=\"-moz-user-select:none;\">4 </span><span>b</span>"
		,	"</pre>"
		));
	}
	@Test   public void Highlight() {
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	"<syntaxHighlight line highlight='1,3'>"
		,	"a"
		,	"b"
		,	"c"
		,	"</syntaxHighlight>"
		), String_.Concat_lines_nl
		(	"<pre style=\"overflow:auto;\">"
		,	"<span style=\"-moz-user-select:none;\">1 </span><span style=\"background-color: #FFFFCC;\">a</span>"
		,	"<span style=\"-moz-user-select:none;\">2 </span><span>b</span>"
		,	"<span style=\"-moz-user-select:none;\">3 </span><span style=\"background-color: #FFFFCC;\">c</span>"
		,	"</pre>"
		));
	}
	@Test   public void Enclose_none() {
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	"<syntaxHighlight enclose=none>"
		,	"a"
		,	"b"
		,	"c"
		,	"</syntaxHighlight>"
		), String_.Concat_lines_nl
		(	"<span>"
		,	"<span>a</span><br/>"
		,	"<span>b</span><br/>"
		,	"<span>c</span><br/>"
		,	"</span>"
		));
	}
	@Test   public void Line_padded() {
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	"<syntaxHighlight line start=9>"
		,	"a"
		,	"b"
		,	"</syntaxHighlight>"
		), String_.Concat_lines_nl
		(	"<pre style=\"overflow:auto;\">"
		,	"<span style=\"-moz-user-select:none;\"> 9 </span><span>a</span>"
		,	"<span style=\"-moz-user-select:none;\">10 </span><span>b</span>"
		,	"</pre>"
		));
	}
}

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
public class Xop_para_wkr_tst {
	private Xop_fxt fxt = new Xop_fxt(); String raw;
	@Before public void init() {fxt.Reset(); fxt.Ctx().Para().Enabled_y_();}
	@After public void teardown() {fxt.Ctx().Para().Enabled_n_();}
	@Test  public void Nl_0() {
		raw = "a";
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)													//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 1), fxt.tkn_para_end_para_(1)		// t2/x1: a     -> a\n</p>		1=bgn    2=blank
			);
	}
	@Test  public void Nl_1() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	"b"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"b"			// NOTE: depend on html editor removing \n between a and b
			,	"</p>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)													//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2), fxt.tkn_para_blank_(2)			// t2/x1: a\n   -> a\n<-p>		1=bgn    2=blank
			,	fxt.tkn_txt_(2, 3), fxt.tkn_nl_char_(3, 3), fxt.tkn_para_end_para_(3)		// t3/x1: b     -> b\n</p>		2=blank  3=blank
			);
	}
	@Test  public void Nl_2() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	"b"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<p>b"
			,	"</p>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)													//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2),	fxt.tkn_para_blank_(2)			// t2/x1: a\n   -> a\n<-p>		1=bgn
			,	fxt.tkn_para_mid_para_(3)													// n3   : \n    -> </p><p>      3=mid    NOTE: </p><p> doesn't get converted until t1
			,	fxt.tkn_txt_(3, 4), fxt.tkn_nl_char_len0_(4), fxt.tkn_para_end_para_(4)		// t1/x1: b     -> b\n</p>		2=mid    3=blank
			);
	}
	@Test  public void Nl_3() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	""
			,	"b"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<p><br/>"	// NOTE: this looks strange, but it emulates MW
			,	"b"
			,	"</p>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)													//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2), fxt.tkn_para_blank_(2)			// t2/x1: a\n   -> a\n<-p>		1=bgn 
			,	fxt.tkn_para_mid_para_(3)													// n3   : \n    -> </p><p>      3=mid    NOTE: </p><p> doesn't get converted until n1
			,	fxt.tkn_xnde_br_(3), fxt.tkn_nl_char_(3, 4), fxt.tkn_para_blank_(4)			// n1/x1: \n	-> <br/>\n<-p>	2=mid    3=blank
			,	fxt.tkn_txt_(4, 5), fxt.tkn_nl_char_len0_(5), fxt.tkn_para_end_para_(5)		// t1/x1: b     -> b\n</p>		4=blank
			);
	}
	@Test  public void Nl_4() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	"b"
			,	""
			,	"c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<p>b"
			,	"</p>"
			,	""
			,	"<p>c"
			,	"</p>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)													//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2), fxt.tkn_para_blank_(2)			// t2/x1: a\n   -> a\n<-p>		1=bgn
			,	fxt.tkn_para_mid_para_(3)													// n3   : \n    -> </p><p>      3=mid    NOTE: </p><p> doesn't get converted until n1
			,	fxt.tkn_txt_(3, 4), fxt.tkn_nl_char_(4, 5), fxt.tkn_para_blank_(5)
			,	fxt.tkn_para_mid_para_(6)
			,	fxt.tkn_txt_(6, 7), fxt.tkn_nl_char_len0_(7), fxt.tkn_para_end_para_(7)
			);
	}
	@Test  public void Nl_5() {
		raw = String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"b"
			,	"</p>"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"b"
			,	"</p>"
			));
	}
	@Test  public void Pre() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	" b"
			,	"c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<pre>b"
			,	"</pre>"
			,	""
			,	"<p>c"
			,	"</p>"
			,	""
			));			
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2), fxt.tkn_para_end_para_bgn_pre_(2)
			,	fxt.tkn_txt_(3, 4), fxt.tkn_nl_char_(4, 5),	fxt.tkn_para_end_pre_bgn_para_(5)
			,	fxt.tkn_txt_(5, 6), fxt.tkn_nl_char_len0_(6), fxt.tkn_para_end_para_(6)
			);
	}
	@Test  public void Pre_2() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	" b"
			,	" c"
			,	"d"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<pre>b"
			,	"c"
			,	"</pre>"
			,	""
			,	"<p>d"
			,	"</p>"
			,	""
			));			
	}
	@Test  public void Pre_3() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	"b"
			,	" c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<p>b"
			,	"</p>"
			,	""
			,	"<pre>c"
			,	"</pre>"
			,	""
			));			
	}
	@Test  public void Pre_4() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	" b"
			,	""
			,	"c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<pre>b"
			,	"</pre>"
			,	""
			,	"<p>c"
			,	"</p>"
			,	""
			));			
	}
	@Test  public void Pre_5() {	// EX.WP: SHA-2
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"a"
			,	" b"
			,	""
			,	" c"
			), String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<pre>b"
			,	"</pre>"
			,	""
			,	"<pre>c"
			,	"</pre>"
			,	""
			));			
	}
	@Test  public void Pre_6() {	// PURPOSE: close list if open; EX.WP: SHA-2
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"*a"
			,	" b"
			), String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"  <li>a"
			,	"  </li>"
			,	"</ul>"
			,	""
			,	"<pre>b"
			,	"</pre>"
			,	""
			));			
	}
	@Test  public void Pre_init() {
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	" a"
			), String_.Concat_lines_nl_skipLast
			(	"<pre>a"
			,	"</pre>"
			,	""
			));
	}
	@Test  public void Pre_leading_ws() {	// PURPOSE: preserve leading ws; EX.WP:Merge sort
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	" a"
			,	"   b"
			,	"     c"
			), String_.Concat_lines_nl_skipLast
			(	"<pre>a"
			,	"  b"
			,	"    c"
			,	"</pre>"
			,	""
			));			
	}
	@Test  public void Pre_newLine() {	// PURPOSE: "\n \n" inside pre should be ignored; trims to ""
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	" b"
			,	" "
			,	" c"
			,	"d"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<pre>b"
			,	" "		// NOTE: should be trimmed as per $t = substr( $t, 1 );
			,	"c"
			,	"</pre>"
			,	""
			,	"<p>d"
			,	"</p>"
			,	""
			));
	}
	@Test  public void Pre_xnde() {	// PURPOSE: <div> and other xndes should invalidate pre on same line; EX: "\n a<div>b"; SEE: any {{refimprove}} article
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	" b<div>c</div>d"
			,	"e"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	" b<div>c</div>d"
			,	""
			,	"<p>e"
			,	"</p>"
			,	""
			));
	}
	@Test  public void Pre_xnde_gallery() {	// PURPOSE: <gallery> should invalidate pre; EX.WP: Mary, Queen of Scots
		fxt.Wiki().Xtn_mgr().Xtn_gallery().Parser().Init_by_wiki(fxt.Wiki());
		raw = String_.Concat_lines_nl_skipLast
			(	" <gallery>"
			,	"File:A.png|b"
			,	"</gallery>"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>" + Gallery_html()
			,	"</p>"
			,	""
			));
	}
	String Gallery_html() {
		return String_.Concat_lines_nl_skipLast
			(	"<ul class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
			,	"  <li class=\"gallerybox\" style=\"width:155px;\">"
			,	"    <div style=\"width:155px;\">"
			,	"      <div class=\"thumb\" style=\"width:150px;\">"
			,	"        <div id=\"xowa_file_gallery_div_0\" style=\"margin:15px auto;\">"
			,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
			,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
			,	"          </a>"
			,	"        </div>"
			,	"      </div>"
			,	"      <div class=\"gallerytext\">b"
			,	"      </div>"
			,	"    </div>"
			,	"  </li>"
			,	"</ul>"
			);
	}
	@Test  public void Pre_lnki_in_td() {	// PURPOSE: ]] in td causes strange parsing; SEE: any {{Commons category}} article
		raw = String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"<tr>"
			,	"<td>[[File:A.png|alt="
			,	" ]]</td>"
			,	"<td>b"
			,	"</td>"
			,	"</tr>"
			,	"</table>"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td><a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\" \" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"
			,	"    </td>"
			,	"    <td>b"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void Pre_trailing_end() {	// PURPOSE: "\n " at end was failing
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	" "
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			));
	}
	@Test  public void Pre_xnde_code() {	// EX.WP: cURL
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	" <code>b</code>"
			,	""
			,	"c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<pre><code>b</code>"
			,	"</pre>"
			,	""
			,	"<p>c"
			,	"</p>"
			,	""
			));
	}
	@Test  public void Pre_xnde_pre() {
		raw = String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>"
			,	"<pre>"
			,	"*a"
			,	"</pre>"
			,	"</li>"
			,	"</ul>"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>"
			,	"<pre>"
			,	"*a"
			,	"</pre>"
			,	"</li></ul>"	// NOTE: the missing \n is a byproduct of block-mode; SEE:NOTE_1
			));
	}
	@Test  public void Pre_td() {	// PURPOSE: EX: "\n a</td>"; </td> deactivates pre; EX.WP: AGPLv3
		raw = String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"<tr>"
			,	"<td>"
			,	" [[File:A.png|30x30px]]</td>"
			,	"</tr>"
			,	"</table>"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td> <a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/30px.png\" width=\"30\" height=\"30\" /></a>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void Nl_2_2_space() {	// test trim
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	"b"
			,	" "
			,	"c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<p>b"
			,	" </p>"
			,	""
			,	"<p>c"
			,	"</p>"
			,	""
			));
	}
	@Test  public void File_1() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	"[[Image:Test.png|thumb|caption]]"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	File_html()
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2), fxt.tkn_para_end_para_(2)
			,	fxt.tkn_lnki_(2, 34)
			,	fxt.tkn_para_blank_(34)	// NOTE: this is unnecessary, but will not output to html; DATE:2013-02-03
			);
	}
	@Test  public void File_2() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	"[[Image:Test.png|thumb|caption]]"
			,	"b"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	File_html()
			,	""
			,	"<p>b"
			,	"</p>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)													//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2), fxt.tkn_para_end_para_(2)
			,	fxt.tkn_lnki_(2, 34)
			,	fxt.tkn_para_blank_(34), fxt.tkn_para_bgn_para_(35)
			,	fxt.tkn_txt_(35, 36), fxt.tkn_nl_char_len0_(36), fxt.tkn_para_end_para_(36)
			);
	}
	@Test  public void File_3() {
		raw = String_.Concat_lines_nl_skipLast
			(	"[[Image:Test.png|thumb|caption]]"
			,	""
			,	"c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	File_html()
			,	""
			,	"<p>c"
			,	"</p>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_blank_(0)													//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_lnki_(0, 32)
			,	fxt.tkn_para_blank_(32), fxt.tkn_para_blank_(33)
			,	fxt.tkn_para_bgn_para_(34)
			,	fxt.tkn_txt_(34, 35), fxt.tkn_nl_char_len0_(35), fxt.tkn_para_end_para_(35)
			);
	}
	@Test  public void File_4() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	"[[Image:Test.png|thumb|caption]]"
			,	""
			,	"c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	File_html()
			,	""
			,	"<p>c"
			,	"</p>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)													//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2), fxt.tkn_para_blank_(2)
			,	fxt.tkn_para_end_para_(3)
			,	fxt.tkn_lnki_(3, 35)
			,	fxt.tkn_para_blank_(35), fxt.tkn_para_blank_(36)
			,	fxt.tkn_para_bgn_para_(37)
			,	fxt.tkn_txt_(37, 38), fxt.tkn_nl_char_len0_(38), fxt.tkn_para_end_para_(38)
			);
	}
	@Test  public void File_5() {	// PURPOSE: \n in caption should not force paragraph
		raw = String_.Concat_lines_nl_skipLast
			(	"[[Image:Test.png|thumb|"
			,	"caption]]"
			,	"a"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	File_html()
			,	""
			,	"<p>a"
			,	"</p>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_blank_(0)
			,	fxt.tkn_lnki_(0, 33)
			,	fxt.tkn_para_blank_(33), fxt.tkn_para_bgn_para_(34)
			,	fxt.tkn_txt_(34, 35), fxt.tkn_nl_char_len0_(35), fxt.tkn_para_end_para_(35)
			);
	}
	@Test  public void File_6() {	// EX.WP: Pyotr Ilyich Tchaikovsky
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	" [[Image:Test.png|thumb|caption]]"
			,	""
			,	"b"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	File_html()
			,	""
			,	"<p>b"
			,	"</p>"
			,	""
			));
	}
	@Test  public void List() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	"*b"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<ul>"
			,	"  <li>b"
			,	"  </li>"
			,	"</ul>"
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)												//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2), fxt.tkn_para_end_para_(2)	// t2/x1: a\n   -> a\n</p>		1=bgn    2=blank
			,	fxt.tkn_list_bgn_(1, 3, Xop_list_tkn_.List_itmTyp_ul)
			,	fxt.tkn_txt_(3, 4)
			,	fxt.tkn_list_end_(4)
			);
	}
	@Test  public void Hdr_1() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	"==b=="
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<h2>b</h2>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)													//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2), fxt.tkn_para_end_para_(2)		// t2/x1: a\n   -> a\n</p>		1=bgn    2=blank
			,	fxt.tkn_hdr_(1, 7, 2).Subs_
			(	fxt.tkn_txt_(4, 5)
			,	fxt.tkn_para_blank_(7)
			)
			,	fxt.tkn_para_blank_(7)	// NOTE: this is redundant, but will not output to html; DATE:2013-02-03
			);
	}
	@Test  public void Hdr_2() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	"==b=="
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<h2>b</h2>"
			,	""
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_bgn_para_(0)													//    x0: <bos> -> <p>			1=blank
			,	fxt.tkn_txt_(0, 1), fxt.tkn_nl_char_(1, 2), fxt.tkn_para_blank_(2)
			,	fxt.tkn_para_end_para_(3)
			,	fxt.tkn_hdr_(2, 8, 2).Subs_
			(	fxt.tkn_txt_(5, 6)
			,	fxt.tkn_para_blank_(8)
			)	
			,	fxt.tkn_para_blank_(8)	// NOTE: this is redundant, but will not output to html; DATE:2013-02-03
			);
	}
	@Test  public void Hdr_list() {
		raw = String_.Concat_lines_nl_skipLast
			(	"==a=="
			,	"*b"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<h2>a</h2>"
			,	""
			,	"<ul>"
			,	"  <li>b"
			,	"  </li>"
			,	"</ul>"
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_blank_(0),	fxt.tkn_para_blank_(1)
			,	fxt.tkn_hdr_(0, 5, 2).Subs_
			(	fxt.tkn_txt_(2, 3)
			,	fxt.tkn_para_blank_(5)
			)
			,	fxt.tkn_para_blank_(6)
			,	fxt.tkn_list_bgn_(5, 7, Xop_list_tkn_.List_itmTyp_ul)
			,	fxt.tkn_txt_(7, 8)
			,	fxt.tkn_list_end_(8)
			);
	}
	@Test  public void Hdr_list_multi() {
		raw = String_.Concat_lines_nl_skipLast
			(	"==a=="
			,	""
			,	""
			,	"*b"
			,	"*c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<h2>a</h2>"
			,	""
			,	"<ul>"
			,	"  <li>b"
			,	"  </li>"
			,	"  <li>c"
			,	"  </li>"
			,	"</ul>"
			));
		fxt.tst_Parse_page_wiki(raw
			,	fxt.tkn_para_blank_(0), fxt.tkn_para_blank_(1)
			,	fxt.tkn_hdr_(0, 7, 2).Subs_
			(		fxt.tkn_txt_(2, 3)
			,		fxt.tkn_para_blank_(7)
			)
			,	fxt.tkn_para_blank_(8)
			,	fxt.tkn_list_bgn_(7, 9, Xop_list_tkn_.List_itmTyp_ul)
			,	fxt.tkn_txt_(9, 10)
			,	fxt.tkn_list_end_(10)
			,	fxt.tkn_list_bgn_(10, 12, Xop_list_tkn_.List_itmTyp_ul)
			,	fxt.tkn_txt_(12, 13)
			,	fxt.tkn_list_end_(13)
			);
	}
	@Test  public void Para_hdr() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	"b"
			,	""
			,	"==c=="
			,	"d"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<p>b"
			,	"</p>"
			,	""
			,	"<h2>c</h2>"
			,	""
			,	"<p>d"
			,	"</p>"
			,	""
			));
	}
	@Test  public void Div() {	// PURPOSE: <div> should not go into para
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	"==b=="
			,	"<div>c</div>"
			,	"d"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<h2>b</h2>"
			,	"<div>c</div>"
			,	""
			,	"<p>d"
			,	"</p>"
			,	""
			));
	}	
	@Test  public void Div_para() {	// PURPOSE: </p> inserted between closing divs
		raw = String_.Concat_lines_nl_skipLast
			(	"<div>"
			,	"<div>"
			,	"a"
			,	"</div>"
			,	"</div>"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<div><div>"
			,	""
			,	"<p>a"
			,	"</p>"
			,	"</div></div>"
			));
	}
	@Test  public void Tbl() {
		raw = String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"<tr>"
			,	"<td>a"
			,	"</td>"
			,	"<td>b"
			,	"</td>"
			,	"</tr>"
			,	"</table>"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"    <td>b"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void Tbl_leading_ws() { // REF: [[Corneal dystrophy (human)]]
		raw = String_.Concat_lines_nl_skipLast
			(	" {|"
			,	" |-"
			,	" |a"
			,	" |}"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			( "<table>"
			, "  <tr>"
			, "    <td>a"
			, "    </td>"
			, "  </tr>"
			, "</table>"
			, ""
			));
	}
	@Test  public void Bos_pipe() {	// PURPOSE: | is interpreted as a tblw dlm and calls a different section of code
		fxt.tst_Parse_page_wiki_str("|", String_.Concat_lines_nl_skipLast
			(	"<p>|"
			,	"</p>"
			,	""
			));
	}
	@Test  public void Category() {	// PURPOSE: Category strips all preceding ws; EX.WP: NYC (in external links)
		raw = String_.Concat_lines_nl_skipLast
			(	"*a"
			,	"*b"
			,	" [[Category:c]]"
			,	"*d"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			( "<ul>"
			, "  <li>a"
			, "  </li>"
			, "  <li>b"
			, "  </li>"
			, "  <li>d"
			, "  </li>"
			, "</ul>"
			));
	}
	@Test   public void Ws_mistakenly_ignored() {// PURPOSE: ws before ''' somehow gets ignored; EX.WP: Vacuum tube; {{Unreferenced section|date=July 2010|reason=date taken from existing cn}}
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"<table><tr><td><span>a"
			,	" '''b'''</span></td></tr></table>"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td><span>a <b>b</b></span>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void Pre_then_xnde_pre() {	// PURPOSE: if ws_pre is in effect, xnde_pre should end it; EX: b:Knowing Knoppix/Other applications
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
		(	" a"
		,	"b<pre>c"
		,	"d</pre>"
		,	"e"
		), String_.Concat_lines_nl_skipLast
		( 	"<pre>a"
		,	"</pre>"
		,	""
		,	"<p>b<pre>c"	// NOTE: this should probably be <p>b</p>
		,	"d</pre>"
		,	"e"
		,	"</p>"
		,	""
		));
	}
	@Test   public void Pre_7() {	// PURPOSE: alternating pres; EX:w:World Wide Web
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	"a"
		,	" b"
		,	"c"
		,	" d"
		,	"e"
		), String_.Concat_lines_nl_skipLast
		( 	"<p>a"
		,	"</p>"
		,	""
		,	"<pre>b"
		,	"</pre>"
		,	""
		,	"<p>c"
		,	"</p>"
		,	""
		,	"<pre>d"
		,	"</pre>"
		,	""
		,	"<p>e"
		,	"</p>"
		,	""
		));
	}
	@Test  public void Nowiki() {
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
		(	"a"
		,	" <nowiki>b</nowiki>"
		,	"c"
		), String_.Concat_lines_nl_skipLast
		( 	"<p>a"
		,	"</p>"
		,	""
		,	"<pre>b"
		,	"</pre>"
		,	""
		,	"<p>c"
		,	"</p>"
		,	""
		));
	}
	@Test  public void Nowiki_tbl() {	// EX: de.wikipedia.org/wiki/Hilfe:Vorlagenprogrammierung
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
		(	"a"
		,	" <nowiki>b</nowiki>c"
		,	""
		,	"{|"
		,	"|-"
		,	"|d"
		,	"|}"
		), String_.Concat_lines_nl_skipLast
		( 	"<p>a"
		,	"</p>"
		,	""
		,	"<pre>bc"
		,	"</pre>"
//			,	""
		,	"<table>"
		,	"  <tr>"
		,	"    <td>d"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		,	""
		));
	}
	@Test  public void Pre_at_end_should_autoclose() { // PURPOSE: as per proc_name
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
		(	"a"
		,	" b"
		,	""
		), String_.Concat_lines_nl_skipLast
		( 	"<p>a"
		,	"</p>"
		,	""
		,	"<pre>b"
		,	"</pre>"
		,	""
		));
	}
	@Test  public void Pre_xtn() {	// PURPOSE: <ref> (and other xtn) was unnecessarily canceling pre; EX: en.wikipedia.org/wiki/MD5 Hash
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
		(	" <span>a</span>"
		,	" <ref>b</ref>"
		,	" c"
		,	""
		,	"d"
		), String_.Concat_lines_nl_skipLast
		( 	"<pre><span>a</span>"
		,	"<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
		,	"c"
		,	"</pre>"
		,	""
		,	"<p>d"
		,	"</p>"
		,	""
		));
	}
	@Test   public void Pre_tbl() {	// PURPOSE: pre was being garbled by tables b/c table was ignoring whitespace; DATE:2013-02-11
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
		(	"{|"
		,	"|a"
		,	"|}"
		,	""
		,	"c"
		,	""
		,	" d"
		,	""
		,	"{|"
		,	"| e"
		,	"|}"
		,	""
		,	"f"
		), String_.Concat_lines_nl_skipLast
		( 	"<table>"
		,	"  <tr>"
		,	"    <td>a"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		,	""
		,	"<p>c"
		,	"</p>"
		,	""
		,	"<pre>d"
		,	"</pre>"
		,	"<table>"
		,	"  <tr>"
		,	"    <td> e"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		,	""
		,	"<p>f"
		,	"</p>"
		,	""
		));
	}
	@Test  public void Nowiki_tbl2() {	// EX: de.wikipedia.org/wiki/Hilfe:Vorlagenprogrammierung
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	"{|"
		,	"|a"
		,	"|}"
		,	""
		,	"b"
		,	""
		,	" <nowiki>c</nowiki> d"
		,	""
		,	"{|"
		,	"|e"
		,	"|}"
		), String_.Concat_lines_nl
		( 	"<table>"
		,	"  <tr>"
		,	"    <td>a"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		,	""
		,	"<p>b"
		,	"</p>"
		,	""
		,	"<pre>c d"
		,	"</pre>"
		,	"<table>"
		,	"  <tr>"
		,	"    <td>e"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		));
	}
	@Test  public void Pre_nowiki() {	// PURPOSE: nowikis inside pre should be ignored; DATE:2013-03-30
		fxt.tst_Parse_page_all_str("\n a<nowiki>&lt;</nowiki>b"	, "\n<pre>a&lt;b\n</pre>\n");										// basic
	}
	@Test  public void Para_bos_tblw() {
		fxt.Wiki().Html_mgr().Tbl_para_y_();
		raw = String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|-"
			,	"|a"
			,	"|}"
			);
		fxt.tst_Parse_page_all_str(raw, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
		fxt.Wiki().Html_mgr().Tbl_para_y_();
	}
	@Test  public void Para_bos_tblx() {
		fxt.Wiki().Html_mgr().Tbl_para_y_();
		raw = String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"<tr>"
			,	"<td>a"
			,	"</td>"
			,	"</tr>"
			,	"</table>"
			);
		fxt.tst_Parse_page_all_str(raw, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
		fxt.Wiki().Html_mgr().Tbl_para_y_();
	}
	@Test   public void Pre_tblw_td() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	" b"
			,	"|"
			,	"c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<pre>b"
			,	"</pre>"
			,	""
			,	"<p>|"
			,	"c"
			,	"</p>"
			,	""
			));
	}
	@Test   public void Pre_tblw_tr() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	" b"
			,	"|-"
			,	"c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<pre>b"
			,	"</pre>"
			,	""
			,	"<p>|-"
			,	"c"
			,	"</p>"
			,	""
			));
	}
	@Test   public void Pre_tblw_te() {
		raw = String_.Concat_lines_nl_skipLast
			(	"a"
			,	" b"
			,	"|}"
			,	"c"
			);
		fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<pre>b"
			,	"</pre>"
			,	""
			,	"<p>|}"
			,	"c"
			,	"</p>"
			,	""
			));
	}
	@Test  public void Pre_ignore_bos() {			// PURPOSE: ignore pre at bgn; DATE:2013-07-09
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	" "
		,	"b"
		), String_.Concat_lines_nl
		(	"<p>"
		,	"b"
		,	"</p>"
		));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Pre_ignore_bos_tblw() {		// PURPOSE: ignore pre at bgn shouldn't break tblw; EX:commons.wikimedia.org; DATE:2013-07-11
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
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
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Pre_ignore_bos_xnde() {		// PURPOSE: space at bgn shouldn't create pre; EX:commons.wikimedia.org; " <center>a\n</center>"; DATE:2013-11-28
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
		(	" <center>a"
		,	"</center>"
		), String_.Concat_lines_nl_skipLast	
		(	"<p><center>a"
		,	"</p>"			// FIXME: para logic is incorrect, but firefox handles correctly; DATE:2013-11-28
		,	"</center>"
		));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void List_ignore_pre_lines() {	// PURPOSE: "\s\n" should create new list; was continuing previous list; DATE:2013-07-12
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	": a"
		,	":* b"
		,	" "
		,	": c"
		,	":* d"
		)
		,	String_.Concat_lines_nl_skipLast
		(	"<dl>"
		,	"  <dd> a"
		,	"    <ul>"
		,	"      <li> b"
		,	"      </li>"
		,	"    </ul>"
		,	"  </dd>"
		,	"</dl>"
		,	"<dl>"
		,	"  <dd> c"
		,	"    <ul>"
		,	"      <li> d"
		,	"      </li>"
		,	"    </ul>"
		,	"  </dd>"
		,	"</dl>"

		));		
		fxt.Ctx().Para().Enabled_n_();
	}

//		@Test   public void Pre_in_lnki() {
//			raw = String_.Concat_lines_nl_skipLast
//				(	"[[A|"
//				,	" b]]"
//				);
//			fxt.tst_Parse_page_wiki_str(raw, String_.Concat_lines_nl_skipLast
//				(	"<p>a"
//				,	"</p>"
//				,	""
//				,	"<pre>b"
//				,	"</pre>"
//				,	""
//				,	"<p>|}"
//				,	"c"
//				,	"</p>"
//				,	""
//				));
//		}

	String File_html() {return File_html("Image", "Test.png", "d/9", "caption");}
	public static String File_html(String ns, String ttl, String md5, String caption) {
		return String_.Concat_lines_nl_skipLast
			(	"<div class=\"thumb tright\">"
			,	"  <div id=\"xowa_file_div_0\" class=\"thumbinner\" style=\"width:220px;\">"
			,	"    <a href=\"/wiki/" + ns + ":" + ttl + "\" class=\"image\" xowa_title=\"" + ttl + "\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/" + md5 + "/" + ttl + "/220px.png\" width=\"0\" height=\"0\" /></a>"
			,	"    <div class=\"thumbcaption\">"
			,	"      <div class=\"magnify\">"
			,	"        <a href=\"/wiki/" + ns + ":" + ttl + "\" class=\"internal\" title=\"Enlarge\">"
			,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
			,	"        </a>"
			,	"      </div>"
			,	"      " + caption + ""
			,	"    </div>"
			,	"  </div>"
			,	"</div>"
			);
	}
	String File_html_nl() {
		return String_.Concat_lines_nl_skipLast
			(	"<div class=\"thumb tright\">"
			,	"  <div class=\"thumbinner\" style=\"width:202px;\">"
			,	"    <a href=\"Image:Test.png\" class=\"image\">"
			,	"      <img id=\"xowa_file_img_0\" alt=\"\" src=\"\" width=\"200\" height=\"200\" />"
			,	"    </a>"
			,	"    <div class=\"thumbcaption\">"
			,	"      <div class=\"magnify\">"
			,	"        <a href=\"\" class=\"internal\" title=\"Enlarge\">"
			,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
			,	"        </a>"
			,	"      </div>"
			,	""
			,	"caption"
			,	"    </div>"
			,	"  </div>"
			,	"</div>"
			);
	}
}
/*
NOTE_1:issue with new-lines and xndes
Process_nl in Block_mode_end will swallow the \n. This can be fixed "easily" by putting in a \n just like Block_mode_bgn
The problem arises b/c of controlled output formatting for tblw tkns. Currently XOWA is taking all table tkns (both "{|" and "<table>") and outputting a controlled indented format
EX:
<table>
  <tr>
    <td>
	etc..
This formatting puts new-lines at the end of the tag, which will be "redundant" if \n is instated above.
This behavior will probably need to be fixed later by redoing the table-output-formatting, but for now, defer it, as I've spent enough time looking at para/pre
*/
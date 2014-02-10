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
public class Xop_xnde_wkr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Escape_lt() {	// PURPOSE: some templates have unknown tags; EX.WP:PHP
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str("a<code><?</code>b", String_.Concat_lines_nl_skipLast
			(	"<p>a<code>&lt;?</code>b"
			,	"</p>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Inline() {
		fxt.Test_parse_page_wiki("<ref/>"	, fxt.tkn_xnde_(0, 6).CloseMode_(Xop_xnde_tkn.CloseMode_inline).Name_rng_(1, 4));
	}
	@Test  public void Pair() {
		fxt.Test_parse_page_wiki("<div></div>", fxt.tkn_xnde_(0, 11).CloseMode_(Xop_xnde_tkn.CloseMode_pair).Name_rng_(1, 4));
	}
	@Test  public void Pair_text() {
		fxt.Test_parse_page_wiki("<div>b</div>", fxt.tkn_xnde_(0, 12).Subs_(fxt.tkn_txt_(5, 6)));
	}
	@Test  public void Deep1_pair1() {
		fxt.Test_parse_page_wiki("<div><div></div></div>", fxt.tkn_xnde_(0, 22).Name_rng_(1, 4)
			.Subs_(fxt.tkn_xnde_(5, 16).Name_rng_(6, 9)));
	}
	@Test  public void Deep1_inline1() {
		fxt.Test_parse_page_wiki("<div><ref/></div>", fxt.tkn_xnde_(0, 17).Name_rng_(1, 4)
			.Subs_(fxt.tkn_xnde_(5, 11).Name_rng_(6, 9)) );
	}
	@Test  public void Deep1_pair2() {
		fxt.Test_parse_page_wiki("<div><div></div><div></div></div>", fxt.tkn_xnde_(0, 33).Name_rng_(1, 4)
			.Subs_
			(	fxt.tkn_xnde_( 5, 16).Name_rng_( 6,  9)
			,	fxt.tkn_xnde_(16, 27).Name_rng_(17, 20)
			));
	}
	@Test  public void Deep2_pair1() {
		fxt.Test_parse_page_wiki("<div><div><div></div></div></div>", fxt.tkn_xnde_(0, 33).Name_rng_(1, 4)
			.Subs_
			(	fxt.tkn_xnde_( 5, 27).Name_rng_( 6,  9)
			.Subs_
			(	fxt.tkn_xnde_(10, 21).Name_rng_(11, 14))
			));
	}
	@Test  public void Atrs_inline() {
		fxt.Test_parse_page_wiki("<ref cd=\"ef\" />"		, fxt.tkn_xnde_(0, 15).Atrs_rng_(5, 13));
		fxt.Test_parse_page_wiki("<ref   cd = \"e f\"  />"	, fxt.tkn_xnde_(0, 21).Atrs_rng_(5, 19)); // ws
	}
	@Test  public void Atrs_bgn() {
		fxt.Test_parse_page_wiki("<div cd=\"ef\"></div>"	, fxt.tkn_xnde_(0, 19).Atrs_rng_(5, 12)); // basic
	}
	@Test  public void Atrs_repeated() {	// PURPOSE: if atr is repeated, take 1st, not last; EX: it.u:Dipartimento:Fisica_e_Astronomia; DATE:2014-02-09
		fxt.Test_parse_page_all_str("<span style='color:red' style='color:green'>a</span>"						, "<span style='color:green'>a</span>");	// two
		fxt.Test_parse_page_all_str("<span style='color:red' style='color:green' style='color:blue'>a</span>"	, "<span style='color:blue'>a</span>");		// three
	}
	@Test  public void Errs() {
		fxt.Test_parse_page_wiki("<", fxt.tkn_txt_(0, 1));
	}
	@Test  public void Err_eos_while_closing_tag() {
		fxt.Init_log_(Xop_xnde_log.Eos_while_closing_tag).Test_parse_page_wiki("<ref [[a]]", fxt.tkn_txt_(0, 4), fxt.tkn_space_(4, 5), fxt.tkn_lnki_(5, 10));
	}
	@Test  public void Err_end_tag_broken() {	// chk that name_bgn is less than src_len else arrayIndex error; EX: <ref><p></p<<ref/>;  DATE:2014-01-18
		fxt.Wiki().Xtn_mgr().Init_by_wiki(fxt.Wiki());
		fxt.Test_parse_page_all_str("<poem><p></p<</poem>", String_.Concat_lines_nl_skipLast
		( "<div class=\"poem\">"
		, "<p>&lt;/p&lt;</p>"
		, "</div>"
		));
	}
	@Test  public void Slash_exc() {// b/c mw allows unquoted attributes
		fxt.Test_parse_page_wiki("<ref / >a</ref>", fxt.tkn_xnde_(0, 15).Atrs_rng_(5, 7).Subs_(fxt.tkn_txt_(8, 9)));
		fxt.Test_parse_page_wiki("<ref name=a/b/>", fxt.tkn_xnde_(0, 15).Atrs_rng_(5, 13));
	}
	@Test  public void Auto_close_() {
		fxt.Init_log_(Xop_xnde_log.Dangling_xnde).Test_parse_page_wiki("<div>", fxt.tkn_xnde_(0, 5));
	}
	@Test  public void Auto_close__many() {
		fxt.Init_log_(Xop_xnde_log.Dangling_xnde, Xop_xnde_log.Dangling_xnde, Xop_xnde_log.Dangling_xnde).Test_parse_page_wiki("<div><div><div>", fxt.tkn_xnde_(0, 15).Subs_(fxt.tkn_xnde_(5, 15).Subs_(fxt.tkn_xnde_(10, 15))));
	}
	@Test  public void Escaped() {
		fxt.Init_log_(Xop_xnde_log.Escaped_xnde).Test_parse_page_wiki("<div></span></div>", fxt.tkn_xnde_(0, 18).Subs_(fxt.tkn_ignore_(5, 12, Xop_ignore_tkn.Ignore_tid_xnde_dangling)));
	}
	@Test  public void Nest() {// REVISIT: 2nd <b> should be converted to </b>; other </b> ignored; WHEN: with example
		fxt.Init_log_(Xop_xnde_log.Invalid_nest, Xop_xnde_log.Escaped_xnde).Test_parse_page_wiki("a<b>b<b>c</b>d</b>e"
			, fxt.tkn_txt_	( 0,  1)
			, fxt.tkn_xnde_	( 1, 13).Subs_(fxt.tkn_txt_(4, 9))
			, fxt.tkn_txt_	(13, 14)
			, fxt.tkn_ignore_(14, 18, Xop_ignore_tkn.Ignore_tid_xnde_dangling)
			, fxt.tkn_txt_	(18, 19)
			);
	}
	@Test  public void Xtn() {
		fxt.Test_parse_page_wiki("<math><div></math>", fxt.tkn_xnde_(0, 18).Subs_(fxt.tkn_txt_(6, 11)));	// NOTE: no dangling nde b/c .Xtn skips
	}
	@Test  public void Xtn_ref() {
		fxt.Test_parse_page_wiki("<ref name=\"a\">b</ref>", fxt.tkn_xnde_(0, 21).Name_rng_(1, 4).Atrs_rng_(5, 13).Subs_(fxt.tkn_txt_(14, 15)));
	}
	@Test  public void CaseSensitivity() {
		fxt.Test_parse_page_wiki("<DiV></dIv>", fxt.tkn_xnde_(0, 11).CloseMode_(Xop_xnde_tkn.CloseMode_pair).Name_rng_(1, 4));
	}
	@Test   public void CaseSensitivity_xtn() {	// PURPOSE: xtn end_tag may not match bgn_tag; EX: w:Ehrenfest_paradox; <References></references>
		fxt.Test_parse_page_all_str("a<ref name=b /><References><ref name=b>c</ref></references>", String_.Concat_lines_nl
		(	"a<sup id=\"cite_ref-b_0-0\" class=\"reference\"><a href=\"#cite_note-b-0\">[1]</a></sup><ol class=\"references\">"
		,	"<li id=\"cite_note-b-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-b_0-0\">^</a></span> <span class=\"reference-text\">c</span></li>"
		,	"</ol>"
		));
	}
	@Test   public void CaseSensitivity_xtn2() {// PURPOSE: xtn xnde must do case-insensitive match DATE:2013-12-02
		fxt.Test_parse_page_all_str
			(	"<matH>a</math> b <math>c</matH>"	// <matH> should match </math> not </matH>
			,	"<span id='xowa_math_txt_0'>a</span> b <span id='xowa_math_txt_0'>c</span>"
			);
	}
	@Test  public void Lnki() {
		fxt.Test_parse_page_wiki("[[Image:a|b<br/>d]]"
			, fxt.tkn_lnki_().Ns_id_(Xow_ns_.Id_file).Trg_tkn_(fxt.tkn_arg_nde_().Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(2, 7), fxt.tkn_colon_(7), fxt.tkn_txt_(8, 9))))
			.Caption_tkn_(fxt.tkn_arg_nde_(10, 17).Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(10, 11), fxt.tkn_xnde_(11, 16), fxt.tkn_txt_(16, 17))))
			);
	}
	@Test  public void Br_bgn_does_not_dangle() {
		fxt.Test_parse_page_wiki("<br>a"	, fxt.tkn_xnde_(0, 4), fxt.tkn_txt_(4, 5));
		fxt.Test_parse_page_wiki("a<br name=b>c", fxt.tkn_txt_(0, 1), fxt.tkn_xnde_(1, 12), fxt.tkn_txt_(12, 13));
	}
	@Test  public void Br_converted_to_reguar_br() {
		fxt.Test_parse_page_wiki("</br>a"	, fxt.tkn_xnde_(0, 5), fxt.tkn_txt_(5, 6));
		fxt.Test_parse_page_wiki("<br/>a"	, fxt.tkn_xnde_(0, 5), fxt.tkn_txt_(5, 6));
		fxt.Test_parse_page_wiki("</br/>a"	, fxt.tkn_xnde_(0, 6), fxt.tkn_txt_(6, 7));
	}
	@Test  public void Table() {
		fxt.Test_parse_page_wiki("a<table><tr><td>b</td></tr></table>c"
			, fxt.tkn_txt_ ( 0,  1)
			, fxt.tkn_tblw_tb_(1, 35).Subs_
			(	fxt.tkn_tblw_tr_(8, 27).Subs_
			(		fxt.tkn_tblw_td_(12, 22).Subs_(fxt.tkn_txt_(16, 17))
			)
			)
			, fxt.tkn_txt_ (35, 36)
			);
	}
	@Test  public void Auto_close_td() {
		fxt.Test_parse_page_wiki("<table><tr><td>a<td></tr><tr><td>b</td></tr></table>"
			, fxt.tkn_tblw_tb_(0, 52).Subs_
			(	fxt.tkn_tblw_tr_(7, 25).Subs_
			(		fxt.tkn_tblw_td_(11, 16).Subs_(fxt.tkn_txt_(15, 16))	// FUTURE: change to 11,20
			,		fxt.tkn_tblw_td_(16, 25)	// FUTURE: change this to 16, 20
			)
			,	fxt.tkn_tblw_tr_(25, 44).Subs_
			(		fxt.tkn_tblw_td_(29, 39).Subs_(fxt.tkn_txt_(33, 34))
			)
			)				
			);
	}
	@Test  public void Auto_close_tr() {// PURPOSE: <tr> should auto-close |-; EX:fr.wikipedia.org/wiki/Napoléon_Ier; DATE:2013-12-09
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl
		(	"{|"
		,	"|-"
		,	"<td>row1</td>"
		,	"<tr><td>row2</td>"
		,	"|}"
		)
		,	String_.Concat_lines_nl
		(	"<table>"
		,	"  <tr>"
		,	"    <td>row1"
		,	"    </td>"
		,	""
		,	"  </tr>"
		,	"  <tr>"
		,	"    <td>row2"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		)
		);
	}
	@Test  public void Auto_close_b() {
		fxt.Init_log_(Xop_xnde_log.Dangling_xnde).Test_parse_page_wiki("<table><tr><td><b>a<td></tr></table>"
			, fxt.tkn_tblw_tb_(0, 36).Subs_
			(	fxt.tkn_tblw_tr_(7, 28).Subs_
			(		fxt.tkn_tblw_td_(11, 19).Subs_	// FUTURE: change to 11,23
					(	fxt.tkn_xnde_(15, 36).Subs_(fxt.tkn_txt_(18, 19))	// FUTURE: should be 19, but xnde.Close() is passing in src_len
					)
			,		fxt.tkn_tblw_td_(19, 28)	// FUTURE: should be 23
			)
			)
			);
	}
	@Test  public void Auto_close_p() {	// WP:
		fxt.Init_log_(Xop_xnde_log.Auto_closing_section).Test_parse_page_wiki("a<p>b<p>c</p>"
			, fxt.tkn_txt_	(0, 1)
			, fxt.tkn_xnde_	(1, 4).Subs_(fxt.tkn_txt_(4, 5))
			, fxt.tkn_xnde_	(5, 13).Subs_(fxt.tkn_txt_(8, 9))
			);
	}
	@Test  public void Auto_close_p_blockquote() {	// WP:
		fxt.Init_log_(Xop_xnde_log.Auto_closing_section).Test_parse_page_wiki("<blockquote>a<p>b</blockquote>"
			, fxt.tkn_xnde_(0, 30).Subs_
			(	fxt.tkn_txt_(12, 13)
			,	fxt.tkn_xnde_(13, 17).Subs_(fxt.tkn_txt_(16, 17))
			));
	}
	@Test  public void Auto_close_center() {
		fxt.Init_log_(Xop_xnde_log.Dangling_xnde).Test_parse_page_wiki("a<center>b"
			, fxt.tkn_txt_(0, 1)
			, fxt.tkn_xnde_(1, 10).CloseMode_(Xop_xnde_tkn.CloseMode_open).Subs_(fxt.tkn_txt_(9, 10))
			);
	}
	@Test  public void Auto_close_list() {
		fxt.Init_log_(Xop_xnde_log.Dangling_xnde).Test_parse_page_wiki("*<b>a\n*"
			, fxt.tkn_list_bgn_(0, 1, Xop_list_tkn_.List_itmTyp_ul).List_path_(0)
			, fxt.tkn_xnde_(1, 7).Subs_(fxt.tkn_txt_(4, 5))
			, fxt.tkn_list_end_(5).List_path_(0)
			, fxt.tkn_list_bgn_(5, 7, Xop_list_tkn_.List_itmTyp_ul).List_path_(1)
			, fxt.tkn_list_end_(7).List_path_(1)
			);
	}
	@Test  public void Auto_close_inner_node() {
		fxt.Test_parse_page_wiki_str
			(	"<div><div><center>a</div></div>"
			,	"<div><div><center>a</center></div></div>"
			);
	}
	@Test  public void SubSupMismatch() {
		fxt.Test_parse_page_wiki_str("<sub>a</sup>", "<sub>a</sub>");
		fxt.Test_parse_page_wiki_str("<sup>a</sub>", "<sup>a</sup>");
	}
	@Test  public void Xnde_td_list() {	// PURPOSE: </td> should close list; see Stamp Act 1765
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table><tr><td>"
			,	"*abc</td></tr><tr><td>bcd</td></tr>"
			,	"</table>"
//				), String_.Concat_lines_nl_skipLast
//				(	"<table><tr><td>"
//				,	"<ul>"
//				,	"  <li>abc"
//				,	"  </li>"
//				,	"</ul></td></tr><tr><td>bcd</td></tr></table>"
//				)
			), String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>"
			,	"      <ul>"
			,	"        <li>abc"
			,	"        </li>"
			,	"      </ul>"
			,	"    </td>"
			,	"  </tr>"
			,	"  <tr>"
			,	"    <td>bcd"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Xnde_td_small_list() {	// PURPOSE: </td> should close <small> correctly; see Stamp Act 1765
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table><tr><td>"
			,	"<small>abc</td></tr><tr><td>bcd</td></tr>"
			,	"</table>"
			), String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td><small>abc</small>"
			,	"    </td>"
			,   "  </tr>"
			,	"  <tr>"
			,	"    <td>bcd"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void No_wiki() {
		fxt.Test_parse_page_wiki_str
			(	"<nowiki>''a''</nowiki>b"
			,	"''a''b"
			);
	}
	@Test  public void No_wiki2() {
		fxt.Ctx().Para().Enabled_y_();
		fxt.Init_defn_add("nowiki_test", "<nowiki>#</nowiki>a");
		fxt.Test_parse_page_all_str
			(	"{{nowiki_test}}"
			,	String_.Concat_lines_nl_skipLast
			(	"<p>#a"
			,	"</p>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void No_wiki_h2() {	// EX.WP: HTML
		fxt.Test_parse_page_all_str
			(	"a<nowiki><h1>b<h6></nowiki>c"
			,	String_.Concat_lines_nl_skipLast
			(	"a&lt;h1&gt;b&lt;h6&gt;c"
			));
	}
	@Test  public void No_wiki_atrs() {
		fxt.Test_parse_page_all_str
			(	"<div id=<nowiki>a</nowiki>>b</div>"
			,	String_.Concat_lines_nl_skipLast
			(	"<div id=\"a\">b</div>"
			));
	}
	@Test  public void No_wiki_atrs_quote() {
		fxt.Test_parse_page_all_str
			(	"<div id='a<nowiki>b</nowiki>c'>d</div>"
			,	String_.Concat_lines_nl_skipLast
			(	"<div id='abc'>d</div>"
			));
	}
	@Test  public void Tblw_nl() {
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str
			(	"<table>\n\n\n\n\n</table>"
			,	"<table>\n"
			+	"</table>\n"
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void MixedCase_xtn() {
		fxt.Test_parse_page_wiki_str
			(	"<Inputbox>a</Inputbox>b<inputbox>c</inputbox>"
			,	"b"
			);
	}
	@Test  public void Bug_multiple_li_in_table() {	// PURPOSE: auto-close <li> (EX: "<li>a<li>") was causing 3rd <li> to close incorrectly
		fxt.Test_parse_page_wiki_str
			(	"<table><tr><td><ul><li>a</li><li>b</li><li>c</li></ul></td></tr></table>"
			,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td><ul>"
			,	"<li>a</li>"
			,	"<li>b</li>"
			,	"<li>c</li></ul>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test   public void Li_nested_inside_ul() {	// PURPOSE: nested li in ul should not be escaped; DATE:2013-12-04
		fxt.Test_parse_page_wiki_str
		(	"<ul><li>a<ul><li>b</li></ul></li></ul>"
		,	String_.Concat_lines_nl_skipLast
		(	"<ul>"
		,	"<li>a<ul>"
		,	"<li>b</li></ul></li></ul>"	// note that <li><li>b becomes <li>&lt;li>b but <li><ul><li>b should stay the same
		));
	}
	@Test  public void Ws_bgn_tbl() {	// PURPOSE: some templates return leading ws; EX.WP:UK
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"  <table>"
			,	"    <tr>"
			,	"      <td>a"
			,	"      </td>"
			,	"    </tr>"
			,	"  </table>"
			), String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,   ""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void SyntaxHighlight_endTag_has_ws() {	// PURPOSE: </syntaxhighlight > not being closed correctly; EX.WP:Mergesort
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"a"
			,	"<syntaxhighlight>"
			,	"b"
			,	"</syntaxhighlight >"
			,	"c"
			,	"<syntaxhighlight>"
			,	"d"
			,	"</syntaxhighlight>"
			), String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"<pre style=\"overflow:auto;\">"
			,	"b"
			,	"</pre>"
			,	"c"
			,	"<pre style=\"overflow:auto;\">"
			,	"d"
			,	"</pre>"
			,   "</p>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Source_escape() {
		fxt.Test_parse_page_wiki_str("<source><b></source>", "<pre>&lt;b&gt;</pre>");
	}
	@Test  public void Code_Auto_close_() {	// EX.WP: HTML; <code>&lt;i&gt;<code> and <code>&lt;center&gt;<code> tags. There are
		fxt.Test_parse_page_wiki_str("a<code>b<code>c", "a<code>b</code>c");
	}
	@Test  public void Error_br_removed() {
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <th><span>a</span><br/><span>b</span>"
			,	"    </th>"
			,	"  </tr>"
			,	"</table>"
			), String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <th><span>a</span><br/><span>b</span>"
			,	"    </th>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Pre_disabled_in_blockquote() { // EX.WP: Tenerife airport disaster
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<blockquote>"
			,	" a"
			,	"</blockquote>"
			), String_.Concat_lines_nl_skipLast
			(	"<blockquote>"
			,	" a"
			,	"</blockquote>"
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Empty_ignored() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>a"
			,	"</li><li>"
			,	"</li><li>b"
			,	"</li>"
			,	"</ul>"
			), String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>a"
			,	"</li>"
			,	"<li>b"
			,	"</li>"
			,	"</ul>"
			));
	}
	@Test  public void Empty_ignored_error() { // EX:WP:Sukhoi Su-47; "* </li>" causes error b/c </li> tries to close non-existent node
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"* a"
			,	"* </li>"
			), String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"  <li> a"
			,	"  </li>"
			,	"  <li> "
			,	"  </li>"
			,	"</ul>"
			));
	}
	@Test  public void Div_should_not_pop_past_td() {	// PURPOSE: extra </div> should not close <div> that is outside of <td>; EX.WP:Rome and Ankara
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table>"
			,		"<tr>"
			,			"<td>"
			,				"<div>"
			,					"<table>"
			,						"<tr>"
			,							"<td>"
			,								"<div>"
			,									"<div>"
			,										"a"
			,									"</div>"
			,							"</td>"
			,							"<td>"
			,									"<div>"
			,										"b"
			,									"</div>"
			,								"</div>"
			,							"</td>"
			,							"<td>"
			,								"<div>"
			,									"c"
			,								"</div>"
			,							"</td>"
			,						"</tr>"
			,					"</table>"
			,				"</div>"
			,			"</td>"
			,		"</tr>"
			,	"</table>"
			), String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td><div>"
			,	"      <table>"
			,	"        <tr>"
			,	"          <td><div><div>"
			,	""
			,	"<p>a"
			,	"</p>"
			,	"</div></div>"
			,	"          </td>"
			,	"          <td><div>"
			,	""
			,	"<p>b"
			,	"</p>"
			,	"</div>"
			,	"          </td>"
			,	"          <td><div>"
			,	""
			,	"<p>c"
			,	"</p>"
			,	"</div>"
			,	"          </td>"
			,	"        </tr>"
			,	"      </table>"
			,	"</div>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Xnde_pops() {	// PURPOSE: somehow xnde pops upper nde; EX.WP: Greek government debt crisis; "History of government debt"
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<i>"
			,	"{|"
			,	"|-"
			,	"|<i>a</i>"
			,	"|}"
			,	"</i>"
			), String_.Concat_lines_nl_skipLast
			(	"<i>"
			,	"<table>"
			,	"  <tr>"
			,	"    <td><i>a</i>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			,	"</i>"
			));
	}
	@Test  public void List_Auto_close_() {
		fxt.Test_parse_page_wiki_str
			(	"<li>a<li>b"
			,	String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>a</li>"
			,	"<li>b</li></ul>"
			));
	}
	@Test  public void List_autoadd_ul() {
		fxt.Test_parse_page_wiki_str
			(	"<li>a</li>", String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>a</li></ul>"
			));
	}
	@Test  public void Pre_and_html_chars() {// PURPOSE: <pre> should handle '"<> according to context
		fxt.Test_parse_page_all_str("<pre>a&#09;b</pre>"			, "<pre>a&#09;b</pre>");					// known ncr/dec; embed and depend on browser transforming; EX: de.w:Wikipedia:Technik/Skin/Werkstatt
		fxt.Test_parse_page_all_str("<pre>a&#9999999999;b</pre>"	, "<pre>a&amp;#9999999999;b</pre>");		// unknown ncr/dec; escape & (since browser cannot render);
		fxt.Test_parse_page_all_str("<pre>a&#af ;b</pre>"		, "<pre>a&amp;#af ;b</pre>");				// unknown ncr/dec 2
		fxt.Test_parse_page_all_str("<pre>a&#x9;b</pre>"			, "<pre>a&#x9;b</pre>");					// known ncr/hex
		fxt.Test_parse_page_all_str("<pre>a&apos;b</pre>"		, "<pre>a&apos;b</pre>");					// known name; embed
		fxt.Test_parse_page_all_str("<pre>a&apox;b</pre>"		, "<pre>a&amp;apox;b</pre>");				// unknown name; escape
		fxt.Test_parse_page_all_str("<pre>&\"<></pre>"			, "<pre>&amp;&quot;&lt;&gt;</pre>");		// no ncr or name; escape; needed for <pre><img ...></pre>; EX.WP: Alt attribute
	}
	@Test  public void Nowiki_lnke() {	// EX.WP: Doomsday argument; <nowiki>[0,&nbsp;1]</nowiki>
		fxt.Test_parse_page_wiki_str("a <nowiki>[0,&nbsp;1]</nowiki> b", "a [0,&nbsp;1] b");	// NOTE: not "0" + Byte_.XtoStr(160) + "1"; depend on browser to translate &nbsp;
	}
	@Test  public void Err_inline_extension() {
		fxt.Test_parse_page_all_str
			(	"<poem/>"
			,	""
			);
	}
	@Test  public void Code_do_not_escape() { // PURPOSE: <code> was mistakenly marked as escape, causing inner tags to be rendered incorrectly; EX.WP:UTF8
		fxt.Test_parse_page_all_str
			(	"<code><span style=\"color:red;\">0100100</span></code>"
			,	"<code><span style=\"color:red;\">0100100</span></code>"
			);
	}
	@Test  public void Li_xatrs_repeated() {	// PURPOSE: inserted ul somehow getting attributes of li; EX.WP:Main Page
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"<div>"
			,	"<li style=\"-moz-float-edge: content-box\">"
			,	"test"
			,	"</li>"
			,	"</div>"
			), String_.Concat_lines_nl_skipLast
			(	"<div>"
			,	"<ul>"
			,	"<li style=\"-moz-float-edge: content-box\">"
			,	"test"
			,	"</li>"
			,	"</ul></div>"
			));
	}

	@Test  public void Xnde_whitelist() {
		fxt.Test_parse_page_all_str("<span onload='alert()'></span>", "<span></span>");
	}
	@Test  public void Xnde_whitelist_pre() { // PURPOSE: <pre style="overflow:auto">a</pre> somehow becoming <prestyle="overflow:auto">a</pre>; Template:Infobox_country; ISSUE: old xatr code being triggered; PURPOSE:(2) style being stripped when it shouldn't be
		fxt.Test_parse_page_all_str("<pre style=\"overflow:auto\">a</pre>", "<pre style=\"overflow:auto\">a</pre>");
	}
	@Test  public void Atrs_non_ws() {			// PURPOSE: <br$2/> is valid; symbols function as ws
		fxt.Init_log_(Xop_xatr_parser.Log_invalid_atr).Test_parse_page_wiki("<br$2/>"	, fxt.tkn_xnde_(0, 7).Atrs_rng_(3, 5));
	}
	@Test  public void Atrs_bad_match() {		// PURPOSE: make sure brx does not match br
		fxt.Test_parse_page_wiki("<brx/>"	, fxt.tkn_bry_(0, 1), fxt.tkn_txt_(1, 6));
	}
	@Test   public void Atr_xnde_defect() {	// PURPOSE: < in atrs was causing premature termination; EX.WP:en.wikipedia.org/wiki/Wikipedia:List of hoaxes on Wikipedia
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|id=\"a<b\""
			,	"|a"
			,	"|}"), String_.Concat_lines_nl_skipLast
			( "<table id=\"a.3Cb\">"
			, "  <tr>"
			, "    <td>a"
			, "    </td>"
			, "  </tr>"
			, "</table>"
			, ""
			));
	}
	@Test  public void Xnde_para() {	// PURPOSE: buggy code caused </p> to close everything; keeping test b/c of <p> logic
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"<tr>"
			,	"<td>"
			,	"<div>"
			,	"<p>"
			,	"<span>"
			,	"</span>"
			,	"</p>"
			,	"</div>"
			,	"</td>"
			,	"</tr>"
			,	"</table>"
			), String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>"
			,	"<div>"
			,	"<p>"
			,	"<span>"
			,	"</span>"
			,	"</p>"
			,	"</div>"
			,	"    </td>"
			,	""
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
	}
	@Test  public void Sup_bug() {	// PURPOSE: occurred at ref of UK; a {{cite web|url=http://www.abc.gov/{{dead link|date=December 2011}}|title=UK}} b
		fxt.Test_parse_page_wiki_str("x <b><sup>y</b> z", "x <b><sup>y</sup></b> z");
	}

	@Test  public void Tblw_nowiki() {// PURPOSE: nowiki breaks token
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"{|style=\"background-color:<nowiki>#</nowiki>FFCC99\""
			,	"|a"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table style=\"background-color:#FFCC99\">"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}	
	@Test  public void Invalid_tag() {// PURPOSE: invalid tag shouldn't break parser; EX:w:Cullen_(surname); "http://www.surnamedb.com/Surname/Cullen<ref"
		fxt.Test_parse_page_all_str("a<ref", "a&lt;ref");
	}	
	@Test  public void Xatr_nowiki() {
		fxt.Test_parse_page_all_str("<ul id<nowiki>=</nowiki>\"a\" class<nowiki>=</nowiki>\"b\"><li><span class=\"c\">d</li></ul>", String_.Concat_lines_nl_skipLast
			(	"<ul id=\"a\" class=\"b\">"
			,	"<li><span class=\"c\">d</span></li></ul>"
			));
	}
	@Test  public void List_always_separated_by_nl() {// PURPOSE: <li> should always be separated by nl, or else items will merge, creating long horizontal scroll bar; EX:w:Music
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_all_str("<ul><li>a</li><li>b</li></ul>"
			,	String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"<li>a</li>"
			,	"<li>b</li></ul>"
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Br_backslash() {	// PURPOSE: allow <br\>; EX:w:Mosquito; [[Acalyptratae|A<br\>c<br\>a<br\>l<br\>y<br\>p<br\>t<br\>r<br\>a<br\>t<br\>a<br\>e]]
		fxt.Test_parse_page_all_str("<br\\>", "<br/>");
	}
	@Test  public void Tt_does_not_repeat() {	// PURPOSE: handle <tt>a<tt>; EX:w:Domain name registry
		fxt.Test_parse_page_all_str("<tt>a<tt>", "<tt>a</tt>");
	}
	@Test  public void Loose_xnde_names() {	// PURPOSE: MW allows <font-> and other variations; EX:w:2012_in_film
		fxt.Test_parse_page_all_str("<font-size='100%'>a</font>", "<font>a</font>");
	}
	@Test  public void Inline_tag_fix() {	// PURPOSE: force <b/> to be <b></b>; EX: w:Exchange_value
		fxt.Init_log_(Xop_xnde_log.No_inline);
		fxt.Test_parse_page_all_str("<b/>", "<b></b>");
	}
	@Test  public void Time() {	// HTML5; should output self (i.e.: must be whitelisted)
		fxt.Test_parse_page_wiki_str("<time class=\"dtstart\" datetime=\"2010-10-10\">10 October 2010</time>", "<time class=\"dtstart\" datetime=\"2010-10-10\">10 October 2010</time>");
	}
	@Test  public void Pre_nowiki() {	// PURPOSE: nowikis inside pre should be ignored; DATE:2013-03-30
		fxt.Test_parse_page_all_str("<pre>a<nowiki>&lt;</nowiki>b</pre>"		, "<pre>a&lt;b</pre>");											// basic
		fxt.Test_parse_page_all_str("<pre>a<nowiki>&lt;<nowiki>b</pre>"		, "<pre>a&lt;nowiki&gt;&lt;&lt;nowiki&gt;b</pre>");				// not closed
		fxt.Test_parse_page_all_str("<pre><nowiki>a<nowiki>b</nowiki>c</nowiki></pre>"	, "<pre>&lt;nowiki&gt;abc&lt;/nowiki&gt;</pre>");	// nested; this is wrong, but leave for now; should be a<nowiki>b</nowiki>c
	}
	@Test  public void Id_encode() {
		fxt.Test_parse_page_all_str("<div id=\"a b c\"></div>", "<div id=\"a_b_c\"></div>");
	}
	@Test  public void Style_invalid() {
		fxt.Test_parse_page_all_str("<div style=\"url(bad)\"></div>", "<div></div>");
	}
	@Test   public void Javascript_in_syntaxhighlight() {
		fxt.Test_parse_page_all_str("<syntaxhighlight><script>alert('fail');</script></syntaxhighlight>", "<pre style=\"overflow:auto;\">&lt;script&gt;alert('fail');&lt;/script&gt;</pre>");
	}
	@Test   public void Javascript_in_math() {
		fxt.App().File_mgr().Math_mgr().Renderer_is_mathjax_(false);
		fxt.Test_parse_page_all_str("<math><script>alert('fail');</script></math>", "<img id='xowa_math_img_0' src='' width='' height=''/><span id='xowa_math_txt_0'>&lt;script>alert('fail');</script></span>");
		fxt.App().File_mgr().Math_mgr().Renderer_is_mathjax_(true);
	}
	@Test   public void Anchor_nested() {
		fxt.Test_parse_page_all_str("b<a>c<a>d [[e]] f", "b&lt;a>c&lt;a>d <a href=\"/wiki/E\">e</a> f");
	}
	@Test  public void Htmltidy_move_ws_char() {
		fxt.Test_parse_page_all_str("a<i> b </i>c", "a <i>b</i> c");
	}
	@Test  public void Htmltidy_move_ws_ent() {
		fxt.Test_parse_page_all_str("a<i>&#32;b&#32;</i>c", "a&#32;<i>b</i>&#32;c");
	}
	@Test  public void Nowiki_inside_code() {	// PURPOSE.fix:HtmlNcr-escaped refs were being ignored; caused by HtmlTidy fix for frwiki templates;DATE:2013-06-27
		fxt.Test_parse_page_all_str("<code><nowiki>|:</nowiki></code>", "<code>|:</code>");
	}
	@Test  public void Li_dupe_list() {	// PURPOSE: ignore redundant li; EX: "* <li>"; http://it.wikipedia.org/wiki/Milano#Bibliographie; DATE:2013-07-23
		fxt.Test_parse_page_all_str("* <li>x</li>", String_.Concat_lines_nl_skipLast
		(	"<ul>"
		,	"  <li> "
		,	"x"
		,	"  </li>"
		,	"</ul>"
		));
	}
	@Test  public void Html5_bdi() {// PURPOSE: HTML5; should output self (i.e.: must be whitelisted); DATE:2013-12-07
		fxt.Test_parse_page_wiki_str("<bdi lang=\"en\">a</bdi>", "<bdi lang=\"en\">a</bdi>");
	}
	@Test  public void Html5_mark() {// PURPOSE: HTML5; should output self (i.e.: must be whitelisted); DATE:2014-01-03
		fxt.Test_parse_page_wiki_str("<mark lang=\"en\">a</mark>", "<mark lang=\"en\">a</mark>");
	}
	@Test  public void Html5_mark_span() {// PURPOSE: </span> should close <mark> tag; EX: zh.wikipedia.org/wiki/异体字; DATE:2014-01-03
		fxt.Test_parse_page_wiki_str("<mark>a</span>", "<mark>a</mark>");
	}
	@Test  public void Html5_wbr() {// PURPOSE: HTML5; should output self (i.e.: must be whitelisted); DATE:2014-01-03
		fxt.Test_parse_page_wiki_str("a<wbr>b<wbr>c", "a<wbr></wbr>b<wbr></wbr>c");
	}
	@Test  public void Html5_bdo() {// PURPOSE: HTML5; should output self (i.e.: must be whitelisted); DATE:2014-01-03
		fxt.Test_parse_page_wiki_str("<bdo>a</bdo>", "<bdo>a</bdo>");
	}
	@Test  public void Script() { // PURPOSE: nested script should (a) write attributes; (b) write close tag; DATE:2014-01-24
		fxt.Test_parse_page_all_str("<code><script src='a'>b</script></code>", "<code>&lt;script src='a'>b&lt;/script></code>");
	}
	@Test  public void Broken_tag() { // PURPOSE: handle broken tags; EX: <div a </div> -> &lt;div a; DATE:2014-02-03
		fxt.Test_parse_page_all_str("<div a </div>", "&lt;div a ");	// note that "<div a " is escaped (not considered xnde; while "</div>" is dropped (dangling end xndes are ignored)
	}


//		@Test  public void Small_repeat_ends() {	// PURPOSE: <small><small> -> <small></small>; EX: w:Wikipedia:Size_comparisons; DATE:2013-12-10
//			fxt.Test_parse_page_all_str("<small>a<small>b</small>c", "<small>a</small>bc");
//		}

//		@Test  public void Atrs_invalid_quote() {	// EX.WP:Palace of Versailles
//			fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
//				(	"<span id=\"1”>a"
//				,	"</span>"
//				), String_.Concat_lines_nl_skipLast
//				(	"<span>a"
//				,	"</span>"
//				));
//		}
//		@Test  public void Code_escape() {
//			fxt.Test_parse_page_wiki_str("a<code><h1><code>b", "a<code>&lt;h1&gt;</code>b");
//		}
//		@Test  public void Ul_li_close_auto() {
//			fxt.Init_log_(Xop_xnde_log.Auto_closing_section).Test_parse_page_wiki("<ul><li>a<li>b</ul>"
//				, fxt.tkn_xnde_( 0, 19).Subs_
//				( fxt.tkn_xnde_( 4,  8).Subs_(fxt.tkn_txt_( 8,  9))
//				, fxt.tkn_xnde_( 9, 14).Subs_(fxt.tkn_txt_(13, 14))
//				));
//		}
//		@Test  public void Ul_li() {	// BUG: was being rendered as <ul><li></li><a></li></ul>
//			fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
//				(	"<ul>"
//				,	"<li>a</li>"
//				,	"</ul>"
//				) ,	String_.Concat_lines_nl_skipLast
//				(	"<ul>"
//				,	"<li>a</li>"
//				,	"</ul>"
//				));
//		}
//		@Test  public void Escaped_div() {	// NOTE: WP <div><span>a</span></div><span>b</span>; MW: <div><span>a</div>b</span> // REVISIT: 2012-05-11; WP does harder split-span
//			fxt.Init_log_(Xop_xnde_log.Auto_closing_section, Xop_xnde_log.Escaped_xnde).Test_parse_page_wiki("<div><span></div></span>"
//				, fxt.tkn_xnde_(0, 17).Subs_
//				(	fxt.tkn_xnde_(5, 11))
//				, fxt.tkn_txt_(17, 24)
//				);
//		}
}
/*
*/
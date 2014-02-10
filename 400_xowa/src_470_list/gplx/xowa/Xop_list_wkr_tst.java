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
public class Xop_list_wkr_tst {		
	private Xop_fxt fxt = new Xop_fxt();
	@Test  public void List_1() {
		fxt.Test_parse_page_wiki("\n*a"
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,	fxt.tkn_txt_(2, 3)
			,	fxt.tkn_list_end_(3).List_path_(0).List_uid_(0)
			);
	}
	@Test  public void Bos() {
		fxt.Test_parse_page_wiki("*a"
			,	fxt.tkn_list_bgn_(0, 1, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,	fxt.tkn_txt_(1, 2)
			,	fxt.tkn_list_end_(2).List_path_(0).List_uid_(0)
			);
	}
	@Test  public void List_1_2() {
		fxt.Test_parse_page_wiki("\n*a\n**b"
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(2, 3)
			,		fxt.tkn_list_bgn_(3, 6, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(0)
			,			fxt.tkn_txt_(6, 7)
			,		fxt.tkn_list_end_(7).List_path_(0, 0)
			,	fxt.tkn_list_end_(7).List_path_(0)
			);
	}
	@Test  public void List_1_2_2() {
		fxt.Test_parse_page_wiki("\n*a\n**b\n**c"
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(2, 3)
			,		fxt.tkn_list_bgn_(3, 6, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(0)
			,			fxt.tkn_txt_(6, 7)
			,		fxt.tkn_list_end_(7).List_path_(0, 0)
			,		fxt.tkn_list_bgn_(7, 10, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 1).List_uid_(0)
			,			fxt.tkn_txt_(10, 11)
			,		fxt.tkn_list_end_(11).List_path_(0, 1)
			,	fxt.tkn_list_end_(11).List_path_(0)
			);
	}
	@Test  public void List_1_2_3() {
		fxt.Test_parse_page_wiki("\n*a\n**b\n***c"
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(2, 3)
			,		fxt.tkn_list_bgn_(3, 6, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(0)
			,			fxt.tkn_txt_(6, 7)
			,			fxt.tkn_list_bgn_(7, 11, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0, 0).List_uid_(0)
			,				fxt.tkn_txt_(11, 12)
			,			fxt.tkn_list_end_(12).List_path_(0, 0, 0)
			,		fxt.tkn_list_end_(12).List_path_(0, 0)
			,	fxt.tkn_list_end_(12).List_path_(0)
			);
	}
	@Test  public void List_2() {
		fxt.Test_parse_page_wiki("\n**a"
			,	fxt.tkn_list_bgn_(0,  3, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_list_bgn_(0,  3, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(0)
			,		fxt.tkn_txt_(3, 4)
			,		fxt.tkn_list_end_(4).List_path_(0, 0)
			,	fxt.tkn_list_end_(4).List_path_(0)
			);
	}
	@Test  public void List_1_3() {
		fxt.Test_parse_page_wiki("\n*a\n***b"
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(2, 3)
			,		fxt.tkn_list_bgn_(3, 7, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(0)
			,			fxt.tkn_list_bgn_(3, 7, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0, 0).List_uid_(0)
			,				fxt.tkn_txt_(7, 8)
			,			fxt.tkn_list_end_(8).List_path_(0, 0, 0)
			,		fxt.tkn_list_end_(8).List_path_(0, 0)
			,	fxt.tkn_list_end_(8).List_path_(0)
			);
	}
	@Test  public void List_1_2_1() {
		fxt.Test_parse_page_wiki("\n*a\n**b\n*c"
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(2, 3)
			,		fxt.tkn_list_bgn_(3, 6, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(0)
			,			fxt.tkn_txt_(6, 7)
			,		fxt.tkn_list_end_(7).List_path_(0, 0)
			,	fxt.tkn_list_end_(7).List_path_(0)
			,	fxt.tkn_list_bgn_(7, 9, Xop_list_tkn_.List_itmTyp_ul).List_path_(1).List_uid_(0)
			,		fxt.tkn_txt_(9, 10)
			,	fxt.tkn_list_end_(10).List_path_(1)
			);
	}
	@Test  public void List_1_1_1() {
		fxt.Test_parse_page_wiki("\n*a\n*b\n*c"
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(2, 3)
			,	fxt.tkn_list_end_(3).List_path_(0)
			,	fxt.tkn_list_bgn_(3, 5, Xop_list_tkn_.List_itmTyp_ul).List_path_(1).List_uid_(0)
			,		fxt.tkn_txt_(5, 6)
			,	fxt.tkn_list_end_(6).List_path_(1)
			,	fxt.tkn_list_bgn_(6, 8, Xop_list_tkn_.List_itmTyp_ul).List_path_(2).List_uid_(0)
			,		fxt.tkn_txt_(8, 9)
			,	fxt.tkn_list_end_(9).List_path_(2)
			);
	}
	@Test  public void List_1___1() {
		fxt.Test_parse_page_wiki("\n*a\n\n*b"
			, fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			, fxt.tkn_txt_(2, 3)
			, fxt.tkn_list_end_(3).List_path_(0)
			, fxt.tkn_nl_char_len1_(3)
			, fxt.tkn_list_bgn_(4, 6, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(1)
			, fxt.tkn_txt_(6, 7)
			, fxt.tkn_list_end_(7).List_path_(0)
			);
	}
	@Test  public void List_1_3_1() {
		fxt.Test_parse_page_wiki("\n*a\n***b\n*c"
			, fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			, fxt.tkn_txt_(2, 3)
			,	fxt.tkn_list_bgn_(3, 7, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(0)
			,		fxt.tkn_list_bgn_(3, 7, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0, 0).List_uid_(0)
			,			fxt.tkn_txt_(7, 8)
			,		fxt.tkn_list_end_(8).List_path_(0, 0, 0)
			,	fxt.tkn_list_end_(8).List_path_(0, 0)
			, fxt.tkn_list_end_(8).List_path_(0)
			, fxt.tkn_list_bgn_(8, 10, Xop_list_tkn_.List_itmTyp_ul).List_path_(1).List_uid_(0)
			, fxt.tkn_txt_(10, 11)
			, fxt.tkn_list_end_(11).List_path_(1)
			);
	}
	@Test  public void Mix_2o_2u() {
		fxt.Test_parse_page_wiki("\n**a\n##b"
			,	fxt.tkn_list_bgn_(0,  3, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_list_bgn_(0,  3, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(0)
			,			fxt.tkn_txt_(3, 4)
			,		fxt.tkn_list_end_(4).List_path_(0, 0)
			,	fxt.tkn_list_end_(4).List_path_(0)
			,	fxt.tkn_list_bgn_(4,  7, Xop_list_tkn_.List_itmTyp_ol).List_path_(0).List_uid_(1)
			,		fxt.tkn_list_bgn_(4,  7, Xop_list_tkn_.List_itmTyp_ol).List_path_(0, 0).List_uid_(1)
			,			fxt.tkn_txt_(7, 8)
			,		fxt.tkn_list_end_(8).List_path_(0, 0)
			,	fxt.tkn_list_end_(8).List_path_(0)
			);
	}
	@Test  public void Dt_dd() {
		fxt.Test_parse_page_wiki(";a\n:b"
			,	fxt.tkn_list_bgn_(0,  1, Xop_list_tkn_.List_itmTyp_dt).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(1, 2)
			,	fxt.tkn_list_end_(2).List_path_(0)
			,	fxt.tkn_list_bgn_(2,  4, Xop_list_tkn_.List_itmTyp_dd).List_path_(1).List_uid_(0)
			,		fxt.tkn_txt_(4, 5)
			,	fxt.tkn_list_end_(5).List_path_(1)
			);
	}
	@Test  public void Dt_dd_inline() {
		fxt.Test_parse_page_wiki(";a:b" // NOTE: no line break
			,	fxt.tkn_list_bgn_(0, 1, Xop_list_tkn_.List_itmTyp_dt).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(1, 2)
			,	fxt.tkn_list_end_(2).List_path_(0)
			,	fxt.tkn_list_bgn_(2, 3, Xop_list_tkn_.List_itmTyp_dd).List_path_(1).List_uid_(0)
			,		fxt.tkn_txt_(3, 4)
			,	fxt.tkn_list_end_(4).List_path_(1)
			);
	}
	@Test  public void Mix_1dd_1ul() {
		fxt.Test_parse_page_wiki(":*a"
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_dd).List_path_(0).List_uid_(0)
			,		fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(0)
			,			fxt.tkn_txt_(2, 3)
			,		fxt.tkn_list_end_(3).List_path_(0, 0)
			,	fxt.tkn_list_end_(3).List_path_(0)
			);
	}
	@Test  public void Mix_1ul__1dd_1ul() {
		fxt.Test_parse_page_wiki("*a\n:*b"
			,	fxt.tkn_list_bgn_(0, 1, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(1, 2)
			,	fxt.tkn_list_end_(2).List_path_(0).List_uid_(0)
			,	fxt.tkn_list_bgn_(2, 5, Xop_list_tkn_.List_itmTyp_dd).List_path_(0).List_uid_(1)
			,		fxt.tkn_list_bgn_(2, 5, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(1)
			,			fxt.tkn_txt_(5, 6)
			,		fxt.tkn_list_end_(6).List_path_(0, 0)
			,	fxt.tkn_list_end_(6).List_path_(0)
			);
	}
	@Test  public void Mix_1dd_1ul__1dd_1ul() {
		fxt.Test_parse_page_wiki(":*a\n:*b"
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_dd).List_path_(0).List_uid_(0)
			,		fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 0).List_uid_(0)
			,			fxt.tkn_txt_(2, 3)
			,		fxt.tkn_list_end_(3).List_path_(0, 0)
			,		fxt.tkn_list_bgn_(3, 6, Xop_list_tkn_.List_itmTyp_ul).List_path_(0, 1).List_uid_(0)
			,			fxt.tkn_txt_(6, 7)
			,		fxt.tkn_list_end_(7).List_path_(0, 1)
			,	fxt.tkn_list_end_(7).List_path_(0)
			);
	}
	@Test  public void Mix_1ul_1hdr() {
		fxt.Test_parse_page_wiki("*a\n==a==\n"
			,	fxt.tkn_list_bgn_(0, 1, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(1, 2)
			,	fxt.tkn_list_end_(2).List_path_(0).List_uid_(0)
			,	fxt.tkn_hdr_(2, 9, 2).Hdr_ws_trailing_(1).Subs_
			(		fxt.tkn_txt_(5, 6)
			)
			);	
	}
	@Test  public void Mix_1ul_1hdr_1ul() {
		fxt.Test_parse_page_wiki("*a\n==a==\n*b"
			,	fxt.tkn_list_bgn_(0, 1, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(1, 2)
			,	fxt.tkn_list_end_(2).List_path_(0).List_uid_(0)
			,	fxt.tkn_hdr_(2, 8, 2).Subs_
			(	fxt.tkn_txt_(5, 6)
			)
			,	fxt.tkn_list_bgn_(8, 10, Xop_list_tkn_.List_itmTyp_ul).List_path_(0).List_uid_(1)
			,		fxt.tkn_txt_(10, 11)
			,	fxt.tkn_list_end_(11).List_path_(0)
			);			
	}
	@Test  public void Mix_1ol_1hr_1ol() {
		fxt.Test_parse_page_wiki("#a\n----\n#b"
			,	fxt.tkn_list_bgn_(0, 1, Xop_list_tkn_.List_itmTyp_ol).List_path_(0).List_uid_(0)
			,		fxt.tkn_txt_(1, 2)
			,	fxt.tkn_list_end_(2)
			,	fxt.tkn_hr_(2, 7)
			,	fxt.tkn_list_bgn_(7, 9, Xop_list_tkn_.List_itmTyp_ol).List_path_(0).List_uid_(1)
			,		fxt.tkn_txt_(9, 10)
			,	fxt.tkn_list_end_(10)
			);			
	}
	@Test  public void Mix_tblw() {
		fxt.Test_parse_page_wiki("::{|\n|a\n|}"
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_dd).List_path_(0).List_uid_(0)
			,	fxt.tkn_list_bgn_(0, 2, Xop_list_tkn_.List_itmTyp_dd).List_path_(0, 0).List_uid_(0)
			,	fxt.tkn_tblw_tb_(2, 10).Subs_
			(		fxt.tkn_tblw_tr_(4, 7).Subs_
			(			fxt.tkn_tblw_td_(4, 7).Subs_(fxt.tkn_txt_(6, 7), fxt.tkn_para_(8, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none)))
			
			)
			,	fxt.tkn_list_end_(10).List_path_(0, 0)
			,	fxt.tkn_list_end_(10).List_path_(0)
			);
	}
	@Test  public void Dif_lvls_1_3_1() {
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"*1"
			,	"***3"
			,	"*1"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"  <li>1"
			,	"    <ul>"
			,	"      <li>"
			,	"        <ul>"
			,	"          <li>3"
			,	"          </li>"
			,	"        </ul>"
			,	"      </li>"
			,	"    </ul>"
			,	"  </li>"
			,	"  <li>1"
			,	"  </li>"
			,	"</ul>"
			));
	}
	@Test  public void Dif_lvls_1_3_2() {// uneven lists
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"*1"
			,	"***3"
			,	"**2"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"  <li>1"
			,	"    <ul>"
			,	"      <li>"
			,	"        <ul>"
			,	"          <li>3"
			,	"          </li>"
			,	"        </ul>"
			,	"      </li>"
			,	"      <li>2"
			,	"      </li>"
			,	"    </ul>"
			,	"  </li>"
			,	"</ul>"
			));
	}
	@Test  public void New_lines() {
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"*a"
			,	""
			,	"**b"
			,	""
			,	"**c"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"  <li>a"
			,	"  </li>"
			,	"</ul>"
			,	""
			,	"<ul>"
			,	"  <li>"
			,	"    <ul>"
			,	"      <li>b"
			,	"      </li>"
			,	"    </ul>"
			,	"  </li>"
			,	"</ul>"
			,	""
			,	"<ul>"
			,	"  <li>"
			,	"    <ul>"
			,	"      <li>c"
			,	"      </li>"
			,	"    </ul>"
			,	"  </li>"
			,	"</ul>"
			));
	}
	@Test  public void Bug_specified_div() {	// FIX: </div> was not clearing state for lnki; EX.WP:Ananke (moon)
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"<div>"
			,	"#<i>a"
			,	"</div>"
			,	"*b"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<div>"
			,	"<ol>"
			,	"  <li><i>a"
			,	"</i>"
			,	"  </li>"
			,	"</ol></div>"
			,	"<ul>"
			,	"  <li>b"
			,	"  </li>"
			,	"</ul>"
			));
	}
	@Test  public void Bug_mismatched() {	// FIX: </div> was not clearing state for lnki; EX.WP:Ananke (moon)
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"::a"
			,	":::1"
			,	"::::11"
			,	":::::111"
			,	"::b"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<dl>"
			,	"  <dd>"
			,	"    <dl>"
			,	"      <dd>a"
			,	"        <dl>"
			,	"          <dd>1"
			,	"            <dl>"
			,	"              <dd>11"
			,	"                <dl>"
			,	"                  <dd>111"
			,	"                  </dd>"
			,	"                </dl>"
			,	"              </dd>"
			,	"            </dl>"
			,	"          </dd>"
			,	"        </dl>"
			,	"      </dd>"
			,	"      <dd>b"
			,	"      </dd>"
			,	"    </dl>"
			,	"  </dd>"
			,	"</dl>"
			));
	}
	@Test  public void Empty_li_ignored() {	// PURPOSE: inner template can cause dupe li; EX.WP: any Calendar day and NYT link; EX: 1/1
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"*a"
			,	"*    "
			,	"*b"
			,	"*c"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"  <li>a"
			,	"  </li>"
			,	"  <li>b"
			,	"  </li>"
			,	"  <li>c"
			,	"  </li>"
			,	"</ul>"
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void List_in_tblw() {	// PURPOSE: list inside table should not be close outer list; EX.WP: Cato the Elder
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"*a"
			,	"{|"
			,	"|b"
			,	"::c"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"  <li>a"
			,	"  </li>"
			,	"</ul>"
			,	"<table>"
			,	"  <tr>"
			,	"    <td>b"
			,	"      <dl>"
			,	"        <dd>"
			,	"          <dl>"
			,	"            <dd>c"
			,	"            </dd>"
			,	"          </dl>"
			,	"        </dd>"
			,	"      </dl>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Dt_dd_colon_at_eol() {		// PURPOSE: dangling ":" should not put next line in <dt>; EX.WP: Stein; b was being wrapped in <dt>b</dt>
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	";a:"
			,	"*b"
			,	""
			,	";c"
			,	"*d"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<dl>"
			,	"  <dt>a"
			,	"  </dt>"
			,	"</dl>"
			,	"<ul>"
			,	"  <li>b"
			,	"  </li>"
			,	"</ul>"
			,	"<dl>"
			,	"  <dt>c"
			,	"  </dt>"
			,	"</dl>"
			,	"<ul>"
			,	"  <li>d"
			,	"  </li>"
			,	"</ul>"
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Dd_should_not_print_colon() {// PURPOSE: ;a:\n should show as ";a" not ";a:". colon should still be considered as part of empty list; DATE:2013-11-07
		fxt.Test_parse_page_all_str(";a:\nb"
		,	String_.Concat_lines_nl_skipLast
		(	"<dl>"
		,	"  <dt>a"
		,	"  </dt>"
		,	"</dl>"
		,	"b"
		));		
	}
	@Test  public void Dt_dd_colon_in_lnki() {	// PURPOSE: "; [[Portal:a]]" should not split lnki; EX.WP: Wikipedia:WikiProject Military history/Operation Majestic Titan; "; [[Wikipedia:WikiProject Military history/Operation Majestic Titan/Phase I|Phase I]]: a b"
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	";[[Portal:a]]"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<dl>"
			,	"  <dt><a href=\"/wiki/Portal:A\">Portal:A</a>"
			,	"  </dt>"
			,	"</dl>"
			));
		fxt.Ctx().Para().Enabled_n_();
	}			
	@Test  public void Max_list_depth() {	// PURPOSE: 256+ * caused list parser to fail; ignore; EX.WP:Bariatric surgery
		String multiple = String_.Repeat("*", 300);
		fxt.Test_parse_page_all_str(multiple
				,	String_.Concat_lines_nl_skipLast
				(	multiple
				));
	}
	@Test  public void Numbered_list_resets_incorrectly() {	// PURPOSE: as description
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"#A"
			,	"#*Aa"
			,	"#**Aaa"
			,	"#*Ab"
			,	"#B"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<ol>"
			,	"  <li>A"
			,	"    <ul>"
			,	"      <li>Aa"
			,	"        <ul>"
			,	"          <li>Aaa"
			,	"          </li>"
			,	"        </ul>"
			,	"      </li>"
			,	"      <li>Ab"
			,	"      </li>"
			,	"    </ul>"	// was showing as </ol>
			,	"  </li>"
			,	"  <li>B"
			,	"  </li>"
			,	"</ol>"
			));
		fxt.Ctx().Para().Enabled_n_();
	}			
	@Test   public void List_should_not_end_indented_table() {// PURPOSE: :{| was being closed by \n*; EX:w:Maxwell's equations; DATE:20121231
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	":{|"
			,	"|-"
			,	"|"
			,	"*a"
			,	"|b"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<dl>"
			,	"  <dd>"
			,	"    <table>"
			,	"      <tr>"
			,	"        <td>"
			,	"          <ul>"
			,	"            <li>a"
			,	"            </li>"
			,	"          </ul>"
			,	"        </td>"
			,	"        <td>b"
			,	"        </td>"
			,	"      </tr>"
			,	"    </table>"
			,	""
			,	"  </dd>"
			,	"</dl>"
			));
	}
	@Test  public void Dt_dd_broken_by_xnde() {	// PURPOSE.fix: xnde was resetting dl incorrectly; EX:w:Virus; DATE:2013-01-31 
		fxt.Test_parse_page_all_str(";<b>a</b>:c"
		,	String_.Concat_lines_nl_skipLast
		(	"<dl>"
		,	"  <dt><b>a</b>"
		,	"  </dt>"
		,	"  <dd>c"
		,	"  </dd>"
		,	"</dl>"
		));		
	}
	@Test   public void Trim_empty_list_items() {	// PURPOSE: empty list items should be ignored; DATE:2013-07-02
		fxt.Test_parse_page_all_str("***   \n"
		,	String_.Concat_lines_nl
		(	""
		));		
	}
	@Test   public void Trim_empty_list_items_error() {	// PURPOSE.fix: do not add empty itm's nesting to current list; DATE:2013-07-07
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl
		(	"* a"
		,	"** "	// do not add ** to nest
		,	"*** b"
		,	"* c"
		) ,	String_.Concat_lines_nl
		(	"<ul>"
		,	"  <li> a"
		,	"    <ul>"
		,	"      <li>"
		,	"        <ul>"
		,	"          <li> b"
		,	"          </li>"
		,	"        </ul>"
		,	"      </li>"
		,	"    </ul>"
		,	"  </li>"
		,	"  <li> c"
		,	"  </li>"
		,	"</ul>"
		));		
	}
	@Test   public void Tblw_should_autoclose() {// PURPOSE: tblw should auto-close open list
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"#a"
			,	"{|"
			,	"|b"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<ol>"
			,	"  <li>a"
			,	"  </li>"
			,	"</ol>"
			,	"<table>"
			,	"  <tr>"
			,	"    <td>b"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test   public void Tblx_should_not_autoclose() {	// PURPOSE: do not auto-close list if table is xnde; DATE:2014-02-05
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl
		(	"#a"
		,	"# <table><tr><td>b</td></tr></table>"
		,	"c"
		) ,	String_.Concat_lines_nl
		(	"<ol>"
		,	"  <li>a"
		,	"  </li>"
		,	"  <li> "
		,	"    <table>"
		,	"      <tr>"
		,	"        <td>b"
		,	"        </td>"
		,	"      </tr>"
		,	"    </table>"
		,	""
		,	"  </li>"
		,	"</ol>"
		,	"c"
		));		
	}
}

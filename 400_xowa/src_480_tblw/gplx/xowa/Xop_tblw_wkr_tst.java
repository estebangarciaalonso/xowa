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
public class Xop_tblw_wkr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Td() {					// Tb_tr_td_te
		fxt.tst_Parse_page_wiki("{|\n|-\n|a\n|}"
			, fxt.tkn_tblw_tb_(0, 11).Subs_
			(	fxt.tkn_tblw_tr_(2, 8).Subs_
			(		fxt.tkn_tblw_td_(5, 8).Subs_(fxt.tkn_txt_(7, 8), fxt.tkn_para_(9, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))))
			);
	}
	@Test  public void Td2() {					// Tb_tr_td_td2_te
		fxt.tst_Parse_page_wiki("{|\n|-\n|a||b\n|}"
			, fxt.tkn_tblw_tb_(0, 14).Subs_
			(	fxt.tkn_tblw_tr_(2, 11).Subs_
			(		fxt.tkn_tblw_td_(5,  8).Subs_(fxt.tkn_txt_( 7,  8))
			,		fxt.tkn_tblw_td_(8, 11).Subs_(fxt.tkn_txt_(10, 11), fxt.tkn_para_(12, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			)));
	}
	@Test  public void Tc() {					// Tb_tc_te
		fxt.tst_Parse_page_wiki("{|\n|+a\n|}"
			, fxt.tkn_tblw_tb_(0, 9).Caption_count_(1).Subs_
			(	fxt.tkn_tblw_tc_(2, 6).Subs_
			(		fxt.tkn_txt_(5, 6)))
			);
	}
	@Test  public void Tc_longer() {			// Tb_tc_tr_td_te
		fxt.tst_Parse_page_wiki("{|\n|+a\n|-\n|b\n|}"
			, fxt.tkn_tblw_tb_(0, 15).Caption_count_(1).Subs_
			(	fxt.tkn_tblw_tc_(2,  6).Subs_(fxt.tkn_txt_(5, 6))
			,	fxt.tkn_tblw_tr_(6, 12).Subs_
			(		fxt.tkn_tblw_td_(9, 12).Subs_(fxt.tkn_txt_(11, 12), fxt.tkn_para_(13, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			)
			));
	}
	@Test  public void Th() {					// Tb_th_te
		fxt.tst_Parse_page_wiki("{|\n|-\n!a\n|}"
			, fxt.tkn_tblw_tb_(0, 11).Subs_
			(	fxt.tkn_tblw_tr_(2, 8).Subs_
			(		fxt.tkn_tblw_th_(5, 8).Subs_(fxt.tkn_txt_(7, 8), fxt.tkn_para_(9, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			)));
	}
	@Test  public void Th2() {					// Tb_th_th2_te
		fxt.tst_Parse_page_wiki("{|\n|-\n!a!!b\n|}"
			, fxt.tkn_tblw_tb_(0, 14).Subs_
			(	fxt.tkn_tblw_tr_(2, 11).Subs_
			(		fxt.tkn_tblw_th_(5,  8).Subs_(fxt.tkn_txt_( 7,  8))
			,		fxt.tkn_tblw_th_(8, 11).Subs_(fxt.tkn_txt_(10, 11), fxt.tkn_para_(12, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			)));
	}
	@Test  public void Th2_td_syntax() {		// Tb_th_td; || should be treated as th
		fxt.tst_Parse_page_wiki("{|\n|-\n!a||b\n|}"
			, fxt.tkn_tblw_tb_(0, 14).Subs_
			(	fxt.tkn_tblw_tr_(2, 11).Subs_
			(		fxt.tkn_tblw_th_(5,  8).Subs_(fxt.tkn_txt_( 7,  8))
			,		fxt.tkn_tblw_th_(8, 11).Subs_(fxt.tkn_txt_(10, 11), fxt.tkn_para_(12, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			)));
	}
	@Test  public void Tb_td2() {	// EX.WP: Hectare; {| class="wikitable" || style="border: 1px solid #FFFFFF;"
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|id='1' || class='a'"
			,	"|-"
			,	"|a"
			,	"|}")
			, String_.Concat_lines_nl_skipLast
			(	"<table id='1' class='a'>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void Atrs_tr() {				// Tb_tr_td_te
		fxt.tst_Parse_page_wiki("{|\n|-style='a'\n|b\n|}"
			, fxt.tkn_tblw_tb_(0, 20).Subs_
			(	fxt.tkn_tblw_tr_(2, 17).Atrs_rng_(5, 14).Subs_
			(		fxt.tkn_tblw_td_(14, 17).Subs_(fxt.tkn_txt_(16, 17), fxt.tkn_para_(18, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			))
			);
	}
	@Test  public void Atrs_td() {				// Tb_tr_td_te
		fxt.tst_Parse_page_wiki("{|\n|-\n|style='a'|b\n|}"
			, fxt.tkn_tblw_tb_(0, 21).Subs_
			(	fxt.tkn_tblw_tr_(2, 18).Subs_
			(		fxt.tkn_tblw_td_(5, 18).Atrs_rng_(7, 16).Subs_(fxt.tkn_txt_(17, 18), fxt.tkn_para_(19, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			))
			);
	}
	@Test  public void Atrs_td_mult() {
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|-"
			,	"|"
			,	"  {|"
			,	"  |-"
			,	"  |  id='1'|"
			,	"  |  id='2'|a"
			,	"  |  id='3'|"
			,	"  |}"
			,	"|}"
			)
			, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>"
			,	"      <table>"
			,	"        <tr>"
			,	"          <td id='1'>"
			,	"          </td>"
			,	"          <td id='2'>a"
			,	"          </td>"
			,	"          <td id='3'>"
			,	"          </td>"
			,	"        </tr>"
			,	"      </table>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Atrs_tc() {	// REF:WP:[[1920 Palm Sunday tornado outbreak]]
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|id='1'"
			,	"|+id='2'|a"
			,	"|}"
			)
			, String_.Concat_lines_nl_skipLast
			(	"<table id='1'>"
			,	"  <caption id='2'>a"
			,	"  </caption>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Atrs_td_mixed() {		// Tb_tr_td_td2_te
		fxt.tst_Parse_page_wiki("{|\n|-\n|style='a'|b||c\n|}"
			, fxt.tkn_tblw_tb_(0, 24).Subs_
			(	fxt.tkn_tblw_tr_(2, 21).Subs_
			(		fxt.tkn_tblw_td_( 5, 18).Atrs_rng_(7, 16).Subs_(fxt.tkn_txt_(17, 18))
			,		fxt.tkn_tblw_td_(18, 21).Subs_(fxt.tkn_txt_(20, 21), fxt.tkn_para_(22, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			))
			);
	}
	@Test  public void Atrs_th() {
		fxt.tst_Parse_page_wiki("{|\n|-\n!style='a'|b\n|}"
			, fxt.tkn_tblw_tb_(0, 21).Subs_
			(	fxt.tkn_tblw_tr_(2, 18).Subs_
			(		fxt.tkn_tblw_th_(5, 18).Atrs_rng_(7, 16).Subs_(fxt.tkn_txt_(17, 18), fxt.tkn_para_(19, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			))
			);
	}
	@Test  public void Atrs_th_cap() {
		fxt.tst_Parse_page_wiki("{|\n|+b\n!style='a'|b\n|}"
			, fxt.tkn_tblw_tb_(0, 22).Caption_count_(1).Subs_
			(	fxt.tkn_tblw_tc_(2, 6).Subs_(fxt.tkn_txt_( 5, 6))
			,	fxt.tkn_tblw_tr_(6, 19).Subs_
			(		fxt.tkn_tblw_th_(6, 19).Atrs_rng_(8, 17).Subs_(fxt.tkn_txt_(18, 19), fxt.tkn_para_(20, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			)				
			));
	}
	@Test  public void Atrs_skip_hdr() {
		fxt.tst_Parse_page_wiki("{|\n|+b\n!style='a'|b\n|}"
			, fxt.tkn_tblw_tb_(0, 22).Caption_count_(1).Subs_
			(	fxt.tkn_tblw_tc_(2, 6).Subs_(fxt.tkn_txt_( 5, 6))
			,	fxt.tkn_tblw_tr_(6, 19).Subs_
			(		fxt.tkn_tblw_th_(6, 19).Atrs_rng_(8, 17).Subs_(fxt.tkn_txt_(18, 19), fxt.tkn_para_(20, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			)				
			));
	}
	@Test  public void Atrs_td_bg_color() {	// PURPOSE: atr_parser should treat # as valid character in unquoted val; EX.WP:UTF8; |bgcolor=#eeeeee|<small>Indic</small><br/><small>0800*</small><br/>'''''224'''''
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|bgcolor=#eeeeee|a"
			,	"|}"
			)
			, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td bgcolor=\"#eeeeee\">a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void AutoClose_td_when_new_tr() {	// retain; needed for de.w:Main_Page; DATE:2013-12-09
		fxt.tst_Parse_page_wiki("{|\n==b==\n|}"
			, fxt.tkn_tblw_tb_(0, 8).Subs_
			(	fxt.tkn_hdr_(2, 8, 2).Subs_
			(	fxt.tkn_txt_(5, 6)
			)
			,	fxt.tkn_tblw_tr_(8,  8).Subs_
			(		fxt.tkn_tblw_td_( 8,  8))
			)
			);
	}
	@Test  public void NestedTbl() {
		fxt.tst_Parse_page_wiki(String_.Concat_lines_nl_skipLast
			( "{|"
			,	"|-"
			,		"|"
			,			"{|"
			,				"|-"
			,					"|a"
			,			"|}"
			,		"|b"
			, "|}"
			)
			, fxt.tkn_tblw_tb_(0, 25).Subs_
			(	fxt.tkn_tblw_tr_(2, 22).Subs_
				(	fxt.tkn_tblw_td_(5, 19).Subs_
					(	fxt.tkn_tblw_tb_(7, 19).Subs_
						(	fxt.tkn_tblw_tr_(10, 16).Subs_
							(	fxt.tkn_tblw_td_(13, 16).Subs_(fxt.tkn_txt_(15, 16), fxt.tkn_para_(17, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
							)
						)
					)
				,	fxt.tkn_tblw_td_(19, 22).Subs_(fxt.tkn_txt_(21, 22), fxt.tkn_para_(23, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
				)
			)
			);
	}
	@Test  public void NestedTbl_leading_ws() {
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|id='a'"
			,	"|-"
			,	"|a"
			,	"|-"
			,	"|id='b'|"
			,	"  {|id='c'"
			,	"  |-"
			,	"  |d"
			,	"  |}"
			,	"|}"
			)
			, String_.Concat_lines_nl_skipLast
			(	"<table id='a'>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"  <tr>"
			,	"    <td id='b'>"
			,	"      <table id='c'>"
			,	"        <tr>"
			,	"          <td>d"
			,	"          </td>"
			,	"        </tr>"
			,	"      </table>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Ws_leading() {	// EX.WP: AGPLv3
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	" !a"
			,	" !b"
			,	"|}"
			)
			, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <th>a"
			,	"    </th>"
			,	"    <th>b"
			,	"    </th>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Atr_close() {	// PURPOSE: issue with </tr> somehow rolling up everything after <td>; EX.WP: 20th century; {{Decades and years}}
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table><tr><td>a"
			,	"{|id=1"
			,	"|-"
			,	"|b"
			,	"|}</td></tr></table>"
			)
			, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"      <table id=\"1\">"
			,	"        <tr>"
			,	"          <td>b"
			,	"          </td>"
			,	"        </tr>"
			,	"      </table>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Err_row_empty() {
		fxt.tst_Parse_page_wiki("{|\n|-\n|-\n|a\n|}"
			,	fxt.tkn_tblw_tb_(0, 14).Subs_
			(		fxt.tkn_tblw_tr_(2,  5)
			,		fxt.tkn_tblw_tr_(5, 11).Subs_
			(			fxt.tkn_tblw_td_(8, 11).Subs_(fxt.tkn_txt_(10, 11), fxt.tkn_para_(12, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			))
			);
	}
	@Test  public void Err_row_trailing() {
		fxt.tst_Parse_page_wiki("{|\n|-\n|a\n|-\n|}"
			, fxt.tkn_tblw_tb_(0, 14).Subs_
			(	fxt.tkn_tblw_tr_(2, 8).Subs_
			(		fxt.tkn_tblw_td_(5, 8).Subs_(fxt.tkn_txt_(7, 8), fxt.tkn_para_(9, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			))
			);
	}
	@Test  public void Err_caption_after_tr() {
		fxt.tst_Parse_page_wiki("{|\n|-\n|+a\n|}"
			, fxt.tkn_tblw_tb_(0, 12).Caption_count_(1).Subs_
			(	fxt.tkn_tblw_tr_(2, 5)
			,	fxt.tkn_tblw_tc_(5, 9).Subs_(fxt.tkn_txt_(8, 9)))
			);
	}
	@Test  public void Err_caption_after_td() {
		fxt.ini_Log_(Xop_tblw_log.Caption_after_td).tst_Parse_page_wiki("{|\n|-\n|a\n|+b\n|}"
			, fxt.tkn_tblw_tb_(0, 15).Caption_count_(1).Subs_
			(	fxt.tkn_tblw_tr_(2,  8).Subs_
			(		fxt.tkn_tblw_td_(5, 8).Subs_(fxt.tkn_txt_(7, 8)))
			,	fxt.tkn_tblw_tc_(8, 12).Subs_(fxt.tkn_txt_(11, 12), fxt.tkn_para_(13, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none)))
			);
	}
	@Test  public void Err_caption_after_tc() {
		fxt.ini_Log_(Xop_tblw_log.Caption_after_tc).tst_Parse_page_wiki("{|\n|+a\n|+b\n|}"
			, fxt.tkn_tblw_tb_(0, 13).Caption_count_(2).Subs_
			(	fxt.tkn_tblw_tc_(2,  6).Subs_(fxt.tkn_txt_( 5, 6))
			,	fxt.tkn_tblw_tc_(6, 10).Subs_(fxt.tkn_txt_( 9, 10)))
			);
	}
	@Test  public void Err_row_auto_opened() {
		fxt.tst_Parse_page_wiki("{|\n|a\n|}"
			, fxt.tkn_tblw_tb_(0, 8).Subs_
			(	fxt.tkn_tblw_tr_(2, 5).Subs_
			(		fxt.tkn_tblw_td_(2, 5).Subs_(fxt.tkn_txt_(4, 5), fxt.tkn_para_(6, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			)));
	}
	@Test  public void Err_caption_auto_closed() {
		fxt.tst_Parse_page_wiki("{|\n|+a\n|b\n|}"
			, fxt.tkn_tblw_tb_(0, 12).Caption_count_(1).Subs_
			(	fxt.tkn_tblw_tc_(2, 6).Subs_(fxt.tkn_txt_(5, 6))
			,	fxt.tkn_tblw_tr_(6, 9).Subs_
			(		fxt.tkn_tblw_td_(6, 9).Subs_(fxt.tkn_txt_(8, 9), fxt.tkn_para_(10, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))
			)));
	}
	@Test  public void Td_lnki() {
		fxt.tst_Parse_page_wiki("{|\n|-\n|[[a|b]]\n|}"
			, fxt.tkn_tblw_tb_(0, 17).Subs_
			(	fxt.tkn_tblw_tr_(2, 14).Subs_
			(		fxt.tkn_tblw_td_(5, 14).Subs_(fxt.tkn_lnki_(7, 14), fxt.tkn_para_(15, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none))))
			);
	}

	@Test  public void Err_Atrs_dumped_into_text() {	// PURPOSE: [[Prawn]] and {{Taxobox}} was dumping text
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|-"
			,	"|-id='a'"
			,	"|b"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr id='a'>"
			,	"    <td>b"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
	}
	@Test  public void Tr_dupe_xnde() {	// PURPOSE: redundant tr should not be dropped; see [[Jupiter]]
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|-"
			,	"<tr><td>a</td></tr>"
			,	"|-"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
	}
	@Test  public void Tr_dupe_xnde_2() {	// <td></th> causes problems
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"<tr><th>a</td></tr>"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <th>a"
			,	"    </th>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
	}
	@Test  public void Tblw_Para() {	// para causing strange breaks; SEE:[[John F. Kennedy]] and "two Supreme Court appointments"
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"<p></p>"
			,	"|a"
			,	"<p></p>"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table><p></p>"
			,	"  <tr>"
			,	"    <td>a"
			,	"<p></p>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
	}
	@Test  public void Tblw_nl() {	// para causing strange breaks; SEE:[[John F. Kennedy]] and "two Supreme Court appointments"
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|-"
			,	"!a"
			,	""
			,	"|-"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <th>a"
			,	"    </th>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
	}
	@Test  public void Bang_should_not_make_cell_td_1_bang() {	// PURPOSE: "| a! b" ! should not separate cell
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast("{|", "|-", "|a!b", "|}"),	String_.Concat_lines_nl_skipLast("<table>", "  <tr>", "    <td>a!b"	, "    </td>", "  </tr>", "</table>", ""));
	}
	@Test  public void Bang_should_not_make_cell_td_2_bang() {
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast("{|", "|-", "|a!!b", "|}"),	String_.Concat_lines_nl_skipLast("<table>", "  <tr>", "    <td>a!!b"	, "    </td>", "  </tr>", "</table>", ""));
	}
	@Test  public void Bang_should_not_make_cell_th_1_bang() {
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast("{|", "|-", "!a!b", "|}"),	String_.Concat_lines_nl_skipLast("<table>", "  <tr>", "    <th>a!b"	, "    </th>", "  </tr>", "</table>", ""));
	}
	@Test  public void Bang_should_not_make_cell_th_2_bang() {
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast("{|", "|-", "!a!!b", "|}")	
			, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <th>a"
			,	"    </th>"
			,	"    <th>b"
			,	"    </th>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void Bang_should_not_make_cell_th_mult_line() {	// FIX: make sure code does not disable subsequent bangs
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast("{|", "|-", "!a", "!b", "|}")	
			, String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <th>a"
			,	"    </th>"
			,	"    <th>b"
			,	"    </th>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void Fix_extra_cell() {	// PURPOSE: trim should not affect td; WP:Base32
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"!id='1'|a"
			,	"|"
			,	"!id='2'|b"
			,	"|-"
			,	"|a1|| ||b1"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <th id='1'>a"
			,	"    </th>"
			,	"    <td>"
			,	"    </td>"
			,	"    <th id='2'>b"
			,	"    </th>"
			,	"  </tr>"
			,	"  <tr>"
			,	"    <td>a1"
			,	"    </td>"
			,	"    <td> "
			,	"    </td>"
			,	"    <td>b1"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Lnki_double_pipe() {	// "||" in "[[File:A.png||a]]" shouldn't be considered cell separator
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|[[B||b]]"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td><a href=\"/wiki/B\">b</a>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
	}
	@Test  public void Nl_td() {	// PURPOSE: <p> inside <td> does not get enclosed
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"<tr>"
			,	"<td>"
			,	""
			,	""
			,	"a"
			,	""
			,	""
			,	"</td>"
			,	"</tr>"
			,	"</table>"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>"
			,	""
			,	"<p><br/>"
			,	"a"
			,	"</p>"
			,	""
			,	"<p><br/>"
			,	"</p>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Trim_ws() {	// PURPOSE: trim should be done from both sides
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"<tr>"
			,	"<td>"
			,	"</td>"
			,	"</tr>"
			,	""
			,	""
			,	"a"
			,	""
			,	""
			,	"</table>"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>"
			,	"    </td>"
			,	"  </tr>"
			,	"a"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Trim_ws_tr() {	// PURPOSE: trim should be done from both sides
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"<tr>"
			,	"<td>"
			,	"</td>"
			,	"</tr>"
			,	""
			,	""
			,	""
			,	""
			,	"<tr>"
			,	"<td>"
			,	"</td>"
			,	"</tr>"
			,	"</table>"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>"
			,	"    </td>"
			,	"  </tr>"
			,	"  <tr>"
			,	"    <td>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Trim_ws_td() {	// PURPOSE: trim should not affect td
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"<tr>"
			,	"<td>"
			,	""
			,	""
			,	"a"
			,	""
			,	""
			,	"</td>"
			,	"</tr>"
			,	"</table>"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>"
			,	""
			,	"<p><br/>"
			,	"a"
			,	"</p>"
			,	""
			,	"<p><br/>"
			,	"</p>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			)
			);
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Atr_xnde() {	// PURPOSE: xnde should close any open xatrs; EX.WP: Western Front (World War I); stray > after == Dramatizations ==
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|id='1'<p></p>"
			,	"|a"
			,	"|}"), String_.Concat_lines_nl_skipLast
			( "<table id='1'><p></p>"
			, "  <tr>"
			, "    <td>a"
			, "    </td>"
			, "  </tr>"
			, "</table>"
			, ""
			));
	}
	@Test  public void No_wiki_3() {
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|style=<nowiki>'a[b]c'</nowiki>|d"
			,	"|}"
			), String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td style='a[b]c'>d"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void Mixed_tbls() {	// PURPOSE: if two consecutive tbs, ignore attributes on 2nd; en.wikibooks.org/wiki/Wikibooks:Featured books
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<table cellpadding=\"0\">"
			,	"{| cellspacing=\"0\""
			,	"|a"
			,	"|}"
			,	"</table>"
			), String_.Concat_lines_nl_skipLast
			(	"<table cellpadding=\"0\">"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void Tblw_absorbed_by_list() {// PURPOSE.fix: tblw immediately following list becomes part of list
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
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
	@Test  public void Trailing_tr_breaks_para_mode() {// PURPOSE.fix: empty trailing tr breaks para mode; EX:w:Sibelius
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|a"
			,	"|-"	// causes lines below not to be put in paras
			,	"|}"
			,	"b"
			,	""
			,	"c"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			,	"<p>b"
			,	"</p>"
			,	""
			,	"<p>c"
			,	"</p>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Blank_line_should_be_own_para() {// PURPOSE.fix: caption does not begin on own line; EX:w:Old St. Peter's Basilica
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|a"
			,	"b"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	""
			,	"<p>b"
			,	"</p>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Blank_line_should_be_own_para_2() {// PURPOSE.fix: caption does not begin on own line; EX:w:Old St. Peter's Basilica
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|a"
			,	"b"
			,	"|-"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	""
			,	"<p>b"
			,	"</p>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Bold_stops_at_table() {	// PURPOSE: do not allow unclosed bold to extend over tables;
		fxt.tst_Parse_page_all_str("'''<table><tr><td>a</td></tr></table>", String_.Concat_lines_nl_skipLast
			(	"<b></b>"
			,	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
		fxt.ini_defn_clear();
	}
	@Test  public void Orphaned_tr_breaks_nested_tables() {	// PUPRPOSE: </tr> should not match <tr> outside scope; EX:w:Enthalpy_of_fusion; {{States of matter}}
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
		(	"<table>"
		,	  "<tr>"
		,	    "<td>"
		,	      "<table>"
		,	        "</tr>"
		,	      "</table>"
		,	    "</td>"
		,	    "<td>a"
		,	    "</td>"
		,	  "</tr>"
		,	"</table>"
		),
		String_.Concat_lines_nl_skipLast
		(	"<table>"
		,	"  <tr>"
		,	"    <td>"
		,	"      <table>"
		,	"      </table>"
		,	"    </td>"
		,	""
		,	"    <td>a"
		,	"    </td>"
		,	""
		,	"  </tr>"
		,	"</table>"
		,	""
		)
		);
	}
	@Test  public void Space_causes_extra_p() {// PURPOSE: "\n\s</td>" should be equivalent to "\n</td>"; EX: w:Earth
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"<table><tr><td>"
			,	"b"
			,	"<br/>c"
			,	" </td></tr></table>"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>"
			,	""
			,	"<p>b"			// used to close <p> here; <p>b</p>
			,	"<br/>c"
			,	"</p>"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Br_should_not_be_ignored() {// PURPOSE: document <br />'s should not be ignored between tables; 20121226
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|-"
			,	"|a"
			,	"|}"
			,	"<br />"
			,	"{|"
			,	"|-"
			,	"|b"
			,	"|}"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	"<br />"	// was being ignored
			,	"<table>"
			,	"  <tr>"
			,	"    <td>b"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Auto_create_table() {// PURPOSE: <td> should create table; EX:w:Hatfield-McCoy_feud; DATE:20121226
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"<td>a"
			,	"</td>"
			) ,	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void List_and_orphaned_td2_should_not_create_tblw() {// PURPOSE: !! was creating table; DATE:2013-04-28
		fxt.tst_Parse_page_all_str("*a !! b", String_.Concat_lines_nl_skipLast
			(	"<ul>"
			,	"  <li>a !! b"
			,	"  </li>"
			,	"</ul>"
			));
	}
	@Test  public void Tr_trailing_dashes_should_be_stripped() {// PURPOSE: trailing dashes should be stripped; |--- -> |-; EX: |--style="x" was being ignored; DATE:2013-06-21
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|-----style='a'"
			,	"|b"
			,	"|}"
			), String_.Concat_lines_nl
			(	"<table>"
			,	"  <tr style='a'>"
			,	"    <td>b"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			));
	}
	@Test  public void Th_without_tr() {	// PURPOSE: !! without preceding ! should not create table-cell; DATE:2013-12-18
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"{|"
			,	"|-"
			,	"|"
			,	"a!!b"
			,	"|}"
			), String_.Concat_lines_nl
			(	"<table>"
			,	"  <tr>"
			,	"    <td>"
			,	"a!!b"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			));
	}
}
/*
*/
//		@Test  public void Tb_under_tr_is_ignored() {	// PURPOSE: table directly under tr is ignored; EX.WP:Category:Dessert stubs; TODO: complicated, especially to handle 2nd |}
//			fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
//				(	"{|"
//				,	"|-id='a'"
//				,	"{|style='border:1px;'"
//				,	"|-id='b'"
//				,	"|b"
//				,	"|}"
//				,	"|}"
//				), String_.Concat_lines_nl_skipLast
//				(	"<table>"
//				,	"  <tr id=\"b\">"
//				,	"    <td>b"
//				,	"    </td>"
//				,	"  </tr>"
//				,	"</table>"
//				,	""
//				));
//		}
//		@Test  public void Leading_ws() { // EX.WP:Corneal dystrophy (human)
//			fxt.tst_Parse_page_wiki(String_.Concat_lines_nl_skipLast
//				( " {|"
//				, " |-"
//				, " |a"
//				, " |}"
//				)
//				, fxt.tkn_tblw_tb_(1, 15).Subs_
//				(	fxt.tkn_tblw_tr_(3, 11).Subs_
//					(	fxt.tkn_tblw_td_(7, 11).Subs_
//						(	fxt.tkn_txt_())
//					)
//				)
//				);
//		}
//		@Test  public void Atrs_tb() {				// Tb_te		// FUTURE: reinstate; WHEN: Template
//			fxt.ini_Log_(Xop_tblw_log.Tbl_empty).tst_Parse_page_wiki("{|style='a'\n|}"
//				, fxt.tkn_tblw_tb_(0, 14).Atrs_rng_(2, 11).Subs_
//				(	fxt.tkn_tblw_tr_(11, 11).Subs_
//				(		fxt.tkn_tblw_td_(11, 11)
//				)));
//		}
//		@Test  public void Td_p() {	// PURPOSE: <p> not being closed correctly
//			fxt.Ctx().Para().Enabled_y_();
//			fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
//				(	"{|"
//				,	"|-"
//				,	"|"
//				,	"a"
//				,	"|}"), String_.Concat_lines_nl_skipLast
//				(	"<table>"
//				,	"  <tr>"
//				,	"    <td>"
//				,	""
//				,	"<p>a"
//				,	"</p>"
//				,	"    </td>"
//				,	"  </tr>"
//				,	"</table>"
//				,	""
//				));
//			fxt.Ctx().Para().Enabled_n_();
//		}
//		@Test  public void Tb_tb() {
//			fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
//				(	"{|id='1'"
//				, 	"{|id='2'"
//				,	"|-id='3'"
//				,	"|a"
//				,	"|}"
//				,	"|}"), String_.Concat_lines_nl_skipLast
//				( "<table id='1'>"
//				, "  <tr id='3'>"
//				, "    <td>a"
//				, "    </td>"
//				, "  </tr>"
//				, "</table>"
//				, ""
//				));
//		}
//		@Test  public void Tb_tb_2() {
//			fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
//				(	"{|id='1'"
//				, 	"{|id='2' <table id='3'>"
//				,	"|a"
//				,	"</table>"
//				,	"|}"
//				,	"|}"), String_.Concat_lines_nl_skipLast
//				( "<table id='1'>"
//				, "  <tr id='3'>"
//				, "    <td>a"
//				, "    </td>"
//				, "  </tr>"
//				, "</table>"
//				, ""
//				));
//		}

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
public class Xop_lnki_wkr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {fxt.Reset();}
	@Test  public void Basic() {
		fxt.tst_Parse_page_wiki("[[a]]", fxt.tkn_lnki_().Trg_tkn_(fxt.tkn_arg_val_txt_(2, 3)));
	}
	@Test  public void HtmlRef() {
		fxt.tst_Parse_page_wiki("[[a&amp;b]]", fxt.tkn_lnki_().Trg_tkn_(fxt.tkn_arg_nde_().Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(2, 3), fxt.tkn_html_ref_("&amp;"), fxt.tkn_txt_(8, 9)))));
	}
	@Test  public void Url_encode() {	// PURPOSE:title should automatically do url decoding; DATE:2013-08-26
		fxt.tst_Parse_page_all_str("[[A%20b]]", "<a href=\"/wiki/A_b\">A b</a>");
	}
	@Test  public void Url_encode_plus() {	// PURPOSE:do not decode plus; DATE:2013-08-26
		fxt.tst_Parse_page_all_str("[[A+b]]", "<a href=\"/wiki/A%2Bb\">A+b</a>");
	}
	@Test  public void Caption() {
		fxt.tst_Parse_page_wiki("[[a|b]]"	, fxt.tkn_lnki_().Trg_tkn_(fxt.tkn_arg_val_txt_(2, 3)).Caption_tkn_(fxt.tkn_arg_val_txt_(4, 5)));
		fxt.tst_Parse_page_wiki("[[a|b:c]]", fxt.tkn_lnki_().Trg_tkn_(fxt.tkn_arg_val_txt_(2, 3)).Caption_tkn_(fxt.tkn_arg_val_(fxt.tkn_txt_(4, 5), fxt.tkn_colon_(5), fxt.tkn_txt_(6, 7))));
	}
	@Test  public void Caption_equal() {	// should ignore = if only caption arg (2 args)
		fxt.tst_Parse_page_wiki("[[a|=]]", fxt.tkn_lnki_().Trg_tkn_(fxt.tkn_arg_val_txt_(2, 3)).Caption_tkn_(fxt.tkn_arg_val_(fxt.tkn_eq_(4))));
	}
	@Test  public void Tail() {
		fxt.tst_Parse_page_wiki("[[a|b]]c" , fxt.tkn_lnki_(0, 8).Tail_bgn_(7).Tail_end_(8));
		fxt.tst_Parse_page_wiki("[[a|b]] c", fxt.tkn_lnki_(0, 7).Tail_bgn_(-1), fxt.tkn_space_(7, 8), fxt.tkn_txt_(8, 9));
		fxt.tst_Parse_page_wiki("[[a|b]]'c", fxt.tkn_lnki_(0, 7).Tail_bgn_(-1), fxt.tkn_txt_(7, 9));
	}
	@Test  public void Tail_number()	{fxt.tst_Parse_page_wiki("[[a|b]]1" , fxt.tkn_lnki_(0, 7).Tail_bgn_(-1), fxt.tkn_txt_(7, 8));}	
	@Test  public void Tail_upper()		{fxt.tst_Parse_page_wiki("[[a|b]]A" , fxt.tkn_lnki_(0, 7).Tail_bgn_(-1), fxt.tkn_txt_(7, 8));}	// make sure trie is case-insensitive
	@Test  public void Tail_image()		{fxt.tst_Parse_page_wiki("[[Image:a|b]]c", fxt.tkn_lnki_(0, 13).Tail_bgn_(-1).Tail_end_(-1), fxt.tkn_txt_(13, 14));}	// NOTE: this occurs on some pages;
	@Test  public void Image() {
		fxt.tst_Parse_page_wiki("[[Image:a]]"				, fxt.tkn_lnki_().Ns_id_(Xow_ns_.Id_file).Trg_tkn_(fxt.tkn_arg_val_(fxt.tkn_txt_(2, 7), fxt.tkn_colon_(7), fxt.tkn_txt_(8, 9))));
		fxt.tst_Parse_page_wiki("[[Image:a|border]]"		, fxt.tkn_lnki_().Border_(Bool_.Y_byte));
		fxt.tst_Parse_page_wiki("[[Image:a|thumb]]"			, fxt.tkn_lnki_().ImgType_(Xop_lnki_type.Id_thumb));
		fxt.tst_Parse_page_wiki("[[Image:a|left]]"			, fxt.tkn_lnki_().HAlign_(Xop_lnki_halign.Left));
		fxt.tst_Parse_page_wiki("[[Image:a|top]]"			, fxt.tkn_lnki_().VAlign_(Xop_lnki_valign.Top));
		fxt.tst_Parse_page_wiki("[[Image:a|10px]]"			, fxt.tkn_lnki_().Width_(10).Height_(-1));
		fxt.tst_Parse_page_wiki("[[Image:a|20x30px]]"		, fxt.tkn_lnki_().Width_(20).Height_(30));
		fxt.tst_Parse_page_wiki("[[Image:a|alt=b]]"			, fxt.tkn_lnki_().Alt_tkn_(fxt.tkn_arg_nde_().Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(10, 13))).Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(14, 15)))));
		fxt.tst_Parse_page_wiki("[[Image:a|link=a]]"		, fxt.tkn_lnki_().Link_tkn_(fxt.tkn_arg_nde_().Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(10, 14))).Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(15, 16)))));
		fxt.tst_Parse_page_wiki("[[Image:a|thumb|alt=b|c d]]"
			, fxt.tkn_lnki_().Ns_id_(Xow_ns_.Id_file)
			.Trg_tkn_(fxt.tkn_arg_nde_().Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(2, 7), fxt.tkn_colon_(7), fxt.tkn_txt_(8, 9))))
			.Alt_tkn_(fxt.tkn_arg_nde_().Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(16, 19))).Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(20, 21))))
			.Caption_tkn_(fxt.tkn_arg_nde_(22, 25).Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(22, 23), fxt.tkn_space_(23, 24), fxt.tkn_txt_(24, 25)))))
			;
	}
	@Test  public void Thumbtime() {
		fxt.tst_Parse_page_wiki("[[File:A.ogv|thumbtime=123]]", fxt.tkn_lnki_().Thumbtime_(123));
		fxt.tst_Parse_page_wiki("[[File:A.ogv|thumbtime=1:23]]", fxt.tkn_lnki_().Thumbtime_(83));
		fxt.tst_Parse_page_wiki("[[File:A.ogv|thumbtime=1:01:01]]", fxt.tkn_lnki_().Thumbtime_(3661));
		fxt.ini_Log_(Xop_lnki_log.Upright_val_is_invalid).tst_Parse_page_wiki("[[File:A.ogv|thumbtime=:1:1]]", fxt.tkn_lnki_().Thumbtime_(-1));
	}
	@Test  public void Width_ws() {
		fxt.tst_Parse_page_wiki("[[Image:a| 123 px]]"		, fxt.tkn_lnki_().Width_(123));
	}
	@Test  public void Height() {
		fxt.tst_Parse_page_wiki("[[Image:a|x40px]]"			, fxt.tkn_lnki_().Height_(40).Width_(-1));
	}
	@Test  public void Size_double_px_ignored() {
		fxt.tst_Parse_page_wiki("[[Image:a|40pxpx]]"		, fxt.tkn_lnki_().Width_(40).Height_(-1));
	}
	@Test  public void Size_px_px_ignored() {
		fxt.tst_Parse_page_wiki("[[Image:a|40px20px]]"		, fxt.tkn_lnki_().Width_(-1).Height_(-1));
	}
	@Test  public void Size_double_px_ws_allowed() {
		fxt.tst_Parse_page_wiki("[[Image:a|40pxpx  ]]"		, fxt.tkn_lnki_().Width_(40).Height_(-1));
	}
	@Test  public void Image_upright() {
		fxt.tst_Parse_page_wiki("[[Image:a|upright=.123]]"	, fxt.tkn_lnki_().Upright_(.123));
		fxt.tst_Parse_page_wiki("[[Image:a|upright]]"		, fxt.tkn_lnki_().Upright_(1));		// no eq tokn
		fxt.tst_Parse_page_wiki("[[Image:a|upright=.42190046219457]]", fxt.tkn_lnki_().Upright_(.42190046219457));	// many decimal places breaks upright
		fxt.ini_Log_(Xop_lnki_log.Upright_val_is_invalid)
		  .tst_Parse_page_wiki("[[Image:a|upright=y]]"		, fxt.tkn_lnki_().Upright_(-1));		// invalid
	}
	@Test  public void Recurse() {
		fxt.tst_Parse_page_wiki("[[Image:a|b-[[c]]-d]]"
			, fxt.tkn_lnki_().Ns_id_(Xow_ns_.Id_file)
			.Trg_tkn_(fxt.tkn_arg_nde_().Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(2, 7), fxt.tkn_colon_(7), fxt.tkn_txt_(8, 9))))
			.Caption_tkn_(fxt.tkn_arg_nde_(10, 19).Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(10, 12), fxt.tkn_lnki_(12, 17), fxt.tkn_txt_(17, 19))))
			);
	}
	@Test  public void Outliers() {
		fxt.tst_Parse_page_wiki("[[Image:a=b.svg|thumb|10px]]", fxt.tkn_lnki_().Ns_id_(Xow_ns_.Id_file).Trg_tkn_(fxt.tkn_arg_nde_().Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(2, 7), fxt.tkn_colon_(7), fxt.tkn_txt_(8, 9), fxt.tkn_eq_(9), fxt.tkn_txt_(10, 15)))));
		fxt.tst_Parse_page_wiki("[[Category:a|b]]", fxt.tkn_lnki_().Ns_id_(Xow_ns_.Id_category));
	}
	@Test  public void Exc_caption_has_eq() {
		fxt.tst_Parse_page_wiki("[[Image:a.svg|thumb|a=b]]", fxt.tkn_lnki_().Ns_id_(Xow_ns_.Id_file)
			.Caption_tkn_(fxt.tkn_arg_nde_(20, 23).Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(20, 21), fxt.tkn_eq_(21), fxt.tkn_txt_(22, 23)))));
	}
	@Test  public void Border_with_space_should_not_be_caption() {// happens with {{flag}}; EX: [[Image:Flag of Argentina.svg|22x20px|border |alt=|link=]]
		Xop_root_tkn root = fxt.tst_Parse_page_wiki_root("[[Image:a.svg|22x20px|border |alt=|link=]]");
		Xop_lnki_tkn lnki = (Xop_lnki_tkn)root.Subs_get(0);
		Tfds.Eq(Xop_tkn_itm_.Tid_null, lnki.Caption_tkn().Tkn_tid());
	}
	@Test  public void Ws_key_bgn() {
		fxt.tst_Parse_page_wiki("[[Image:a| alt=b]]", fxt.tkn_lnki_()
			.Alt_tkn_(fxt.tkn_arg_nde_()
			.	Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_space_(10, 11).Ignore_y_(), fxt.tkn_txt_(11, 14)))
			.	Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(15, 16)))
			));
	}
	@Test  public void Ws_key_end() {
		fxt.tst_Parse_page_wiki("[[Image:a|alt =b]]", fxt.tkn_lnki_()
			.Caption_tkn_(fxt.tkn_arg_nde_(10, 16)
			.	Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(10, 13), fxt.tkn_space_(13, 14), fxt.tkn_eq_(14), fxt.tkn_txt_(15, 16)))
			));
	}
	@Test  public void Ws_val_bgn() {
		fxt.tst_Parse_page_wiki("[[Image:a|alt= b]]", fxt.tkn_lnki_()
			.Alt_tkn_(fxt.tkn_arg_nde_()
			.	Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(10, 13)))
			.	Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_space_(14, 15), fxt.tkn_txt_(15, 16)))
			));
	}
	@Test  public void Ws_val_end() {
		fxt.tst_Parse_page_wiki("[[Image:a|alt=b ]]", fxt.tkn_lnki_()
			.Alt_tkn_(fxt.tkn_arg_nde_()
			.	Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(10, 13)))
			.	Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(14, 15), fxt.tkn_space_(15, 16).Ignore_y_()))
			));
	}
	@Test  public void Ws_val_only() {	// simpler variation of Ws_val_size
		fxt.tst_Parse_page_wiki("[[Image:a| b ]]", fxt.tkn_lnki_()
			.Caption_tkn_(fxt.tkn_arg_nde_()
			.	Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_space_(10, 11), fxt.tkn_txt_(11, 12), fxt.tkn_space_(12, 13)))
			));
	}
	@Test  public void Ws_val_size() {
		fxt.tst_Parse_page_wiki("[[Image:a| 20x30px ]]"	, fxt.tkn_lnki_().Width_(20).Height_(30));
	}
	@Test  public void Nl_pipe() {		// PURPOSE: "\n|" triggers tblw; EX.WP.FR: France; [[Fichier:Carte demographique de la France.svg
		fxt.tst_Parse_page_wiki("[[Image:A.png|thumb\n|alt=test]]", fxt.tkn_lnki_()
			.Alt_tkn_(fxt.tkn_arg_nde_()
			.	Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(21, 24)))
			.	Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(25, 29)))
			));
	}
	@Test  public void Nl_pipe_2() {	// PURPOSE: "\n|" still triggers tblw if "dangling" xnde; in this case, sole <li> automatically inserts <ul> beforehand
		fxt.ini_Log_(Xop_xnde_log.Dangling_xnde).tst_Parse_page_wiki("[[Image:A.png|thumb\n<li>a</li>\n|alt=test]]", fxt.tkn_lnki_()
			.Alt_tkn_(fxt.tkn_arg_nde_()
			.	Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(32, 35)))
			.	Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(36, 40)))
			));
	}
	@Test  public void Exc_empty_caption() {
		fxt.tst_Parse_page_wiki("[[a|]]", fxt.tkn_lnki_().Trg_tkn_(fxt.tkn_arg_val_txt_(2, 3)));
	}
	@Test  public void Exc_empty() {
		fxt.tst_Parse_page_wiki("[[]]", fxt.tkn_txt_(0, 4));
		fxt.tst_Parse_page_wiki("[[ ]]", fxt.tkn_txt_(0, 5));
	}
	@Test  public void Exc_nl_with_apos() {			// PURPOSE: apos, lnki and nl will cause parser to fail; DATE:2013-10-31
		fxt.tst_Parse_page_all_str("''[[\n]]", "<i>[[\n]]</i>");
	}
	@Test  public void Exc_nl_multiple_lines() {	// PURPOSE: apos, tblw, lnki, and nl will cause parser to fail; EX:Module:Taxobox; DATE:2013-11-10
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	" [[''"
		,	" [["
		,	"  |"
		,	"]]"
		)
		,	String_.Concat_lines_nl_skipLast
		(	"<pre>[[<i>"
		,	"[["
		,	"  |"
		,	"]] "
		,	"</p>"
		,	"</i>"
		));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Exc_pipeOnly() {
		fxt.tst_Parse_page_wiki("[[|]]", fxt.tkn_txt_(0, 5));
	}
	@Test  public void Exc_invalid_utf8() {	// PURPOSE: "%DO" is an invalid UTF-8 sequence (requires 2 bytes, not just %D0); DATE:2013-11-11
		fxt.Ctx().Lang().Case_mgr().Add_bulk(Xol_case_itm_.Universal);	// NOTE: only occurs during Universal
		fxt.tst_Parse_page_all_str("[[%D0]]", "[[%D0]]");				// invalid titles render literally
		fxt.Ctx().Lang().Case_mgr().Add_bulk(Xol_case_itm_.English);
	}
	@Test  public void Ex_eq() {	// make sure that eq is not evaluated for kv delimiter
		fxt.tst_Parse_page_wiki("[[=]]", fxt.tkn_lnki_(0, 5));
		fxt.tst_Parse_page_wiki("[[a|=]]", fxt.tkn_lnki_(0, 7));
	}
	@Test  public void Unclosed() {	// PURPOSE: unclosed lnki skips rendering of next table; EX.WP:William Penn (Royal Navy officer)
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"a [[b|c]"
			,	""
			,	"{|"
			,	"|-"
			,	"|d"
			,	"|}"
			),String_.Concat_lines_nl_skipLast
			(	"a [[b|c] "	// NOTE: \n is converted to \s b/c caption does not allow \n
			,	"<table>"
			,	"  <tr>"
			,	"    <td>d"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}
	@Test  public void Caption_nl() {	// PURPOSE: \n in caption should be rendered as space; EX.WP:Schwarzschild radius; and the stellar [[Velocity dispersion|velocity\ndispersion]]
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"a [[b|c"
			,	""
			,	""
			,	"d]]"
			),	String_.Concat_lines_nl_skipLast
			(	"<p>a <a href=\"/wiki/B\">c   d</a>"	// NOTE: this depends on html viewer to collapse multiple spaces into 1
			,	"</p>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Caption_nl_2() {	// PURPOSE: unclosed lnki breaks paragraph unexpectedly; EX.WP:Oldsmobile;
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"a"
			,	""
			,	"b [[c"
			,	""	// NOTE: this new line is needed to produce strange behavior
			), String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"</p>"
			,	""
			,	"<p>b [[c "	// NOTE: \n is converted to \s b/c caption does not allow \n
			,	"</p>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Caption_ref() {	// PURPOSE: <ref> not handled in lnki; EX.WP:WWI
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"[[File:A.png|thumb|b <ref>c</ref>]]"
			), String_.Concat_lines_nl_skipLast
			(	"<div class=\"thumb tright\">"
			,	"  <div id=\"xowa_file_div_0\" class=\"thumbinner\" style=\"width:220px;\">"
			,	"    <a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/220px.png\" width=\"0\" height=\"0\" /></a>"
			,	"    <div class=\"thumbcaption\">"
			,	"      <div class=\"magnify\">"
			,	"        <a href=\"/wiki/File:A.png\" class=\"inte" +"rnal\" title=\"Enlarge\">"
			,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
			,	"        </a>"
			,	"      </div>"
			,	"      b <sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup>"
			,	"    </div>"
			,	"  </div>"
			,	"</div>"
			,	""
			));
	}
	@Test  public void Category_trim() { // PURPOSE: Category should trim preceding nl; EX:w:Mount Kailash
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"a"
			,	" [[Category:b]]"
			,	"c"
			), String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"c"
			,	"</p>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Category_trim_2() {	// FUTURE: needs more para rework; conflicts with Category() test in para_wkr_tst; WHEN: when issue is found
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"a"
			,	"x [[Category:b]]"
			,	"c"
			), String_.Concat_lines_nl_skipLast
			(	"<p>a"
			,	"x "
			,	"c"
			,	"</p>"
			,	""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
//		@Test   public void Subpage_parent() {	// DISABLED: MW does not allow ../ to go past root; DATE:2014-01-02
//			fxt.Page_ttl_("A");
//			fxt.tst_Parse_page_wiki_str
//				(	"[[b/../c]]"
//				,	"<a href=\"/wiki/C\">c</a>"
//				);
//		}
	@Test  public void Subpage_disabled() {	// PURPOSE: slash being interpreted as subpage; EX.WP:[[/dev/null]]
		fxt.Wiki().Ns_mgr().Get_by_id_or_null(Xow_ns_.Id_main).Subpages_enabled_(false);
		fxt.tst_Parse_page_all_str("[[/dev/null]]", "<a href=\"/wiki//dev/null\">/dev/null</a>");
		fxt.Wiki().Ns_mgr().Get_by_id_or_null(Xow_ns_.Id_main).Subpages_enabled_(true);
	}
	@Test  public void Subpage_false_match() {// EX.WP:en.wiktionary.org/wiki/Wiktionary:Requests for cleanup/archive/2006
		fxt.tst_Parse_page_wiki_str
			(	"[[.../compare ...]]"
			,	"<a href=\"/wiki/.../compare_...\">.../compare ...</a>"
			);
	}
	@Test  public void Subpage_owner() {	// PURPOSE: ../c does "A/c", not "c"; DATE:2014-01-02
		fxt.Page_ttl_("A/b");
		fxt.tst_Parse_page_wiki_str
			(	"[[../c]]"
			,	"<a href=\"/wiki/A/c\">A/c</a>"
			);
	}
	@Test  public void Subpage_owner_w_slash() {	// PURPOSE: ../c/ does "c", not "A/c"; DATE:2014-01-02
		fxt.Page_ttl_("A/b");
		fxt.tst_Parse_page_wiki_str
			(	"[[../c/]]"
			,	"<a href=\"/wiki/A/c\">c</a>"
			);
	}
	@Test  public void Subpage_slash() {	// PURPOSE: /B should show /B, not A/B; DATE:2014-01-02
		fxt.Page_ttl_("A");
		fxt.tst_Parse_page_wiki_str
			(	"[[/B]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/wiki/A/B\">/B</a>"
			));
	}
	@Test  public void Subpage_slash_w_slash() {	// PURPOSE: /B/ should show B, not /B; DATE:2014-01-02
		fxt.Page_ttl_("A");
		fxt.tst_Parse_page_wiki_str
			(	"[[/B/]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/wiki/A/B\">B</a>"
			));
	}
	@Test  public void Link() {
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=File:B.png|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/wiki/File:B.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_blank() {
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_external() {
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=http://www.b.org|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"http://www.b.org\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_file_system() {
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=file:///C/B.png|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"file:///C/B.png\" class=\"image\" xowa_title=\"B.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_file_ns() {
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=File:B.png|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/wiki/File:B.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_external_relative() {
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=//fr.wikipedia.org/wiki/|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"http://fr.wikipedia.org/wiki/\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_external_absolute() {
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=http://fr.wikipedia.org/wiki/|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"http://fr.wikipedia.org/wiki/\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_external_double_http() {// PURPOSE.fix: link=http://a.org?b=http://c.org breaks lnki; DATE:2013-02-03
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=//a.org?b=http://c.org]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"http://a.org?b=http://c.org\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_xwiki_relative() {	// NOTE: changed href to return "wiki/" instead of "wiki"; DATE:2013-02-18
		fxt.ini_xwiki_add_user_("fr.wikipedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=//fr.wikipedia.org/wiki/|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/fr.wikipedia.org/wiki/\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_xwiki_relative_domain_only() {	// lnki_wtr fails if link is only domain; EX: wikimediafoundation.org; [[Image:Wikispecies-logo.png|35px|link=//species.wikimedia.org]]; // NOTE: changed href to return "/wiki/" instead of ""; DATE:2013-02-18
		fxt.ini_xwiki_add_user_("fr.wikipedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=//fr.wikipedia.org]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/fr.wikipedia.org/wiki/\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_xwiki_absolute() {	// NOTE: changed href to return "wiki/" instead of "wiki"; DATE:2013-02-18
		fxt.ini_xwiki_add_user_("fr.wikipedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=http://fr.wikipedia.org/wiki/|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/fr.wikipedia.org/wiki/\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_xwiki_absolute_upload() {	// PURPOSE: link to upload.wikimedia.org omits /wiki/; EX: wikimediafoundation.org: [[File:Page1-250px-WMF_AR11_SHIP_spreads_15dec11_72dpi.png|right|125px|border|2010–2011 Annual Report|link=https://upload.wikimedia.org/wikipedia/commons/4/48/WMF_AR11_SHIP_spreads_15dec11_72dpi.pdf]]
		fxt.ini_xwiki_add_user_("commons.wikimedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=http://upload.wikimedia.org/wikipedia/commons/7/70/A.png|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/commons.wikimedia.org/wiki/File:A.png\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_xwiki_relative_deep() {
		fxt.ini_xwiki_add_user_("fr.wikipedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=//fr.wikipedia.org/wiki/A/b|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/fr.wikipedia.org/wiki/A/b\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Link_xwiki_alias() {	// [[File:Commons-logo.svg|25x25px|link=http://en.wikipedia.org/wiki/commons:Special:Search/Earth|alt=|Search Commons]]
		fxt.ini_xwiki_add_wiki_and_user_("commons", "commons.wikimedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=http://en.wikipedia.org/wiki/commons:B|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/commons.wikimedia.org/wiki/B\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
		fxt.App().Url_alias_mgr().Clear();
	}
	@Test  public void Link_xwiki_alias_sub_page() {	// same as above, but for sub-page; [[File:Commons-logo.svg|25x25px|link=http://en.wikipedia.org/wiki/commons:Special:Search/Earth|alt=|Search Commons]]
		fxt.ini_xwiki_add_wiki_and_user_("commons", "commons.wikimedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=http://en.wikipedia.org/wiki/commons:Special:Search/B|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/commons.wikimedia.org/wiki/Special:Search/B\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
		fxt.App().Url_alias_mgr().Clear();
	}
	@Test  public void Link_xwiki_alias_only() {	// only alias; [[File:Commons-logo.svg|25x25px|link=commons:Special:Search/Earth]]; fictitious example; DATE:2013-02-18
		fxt.ini_xwiki_add_wiki_and_user_("commons", "commons.wikimedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=commons:Special:Search/B|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/commons.wikimedia.org/wiki/Special:Search/B\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
		fxt.App().Url_alias_mgr().Clear();
	}
	@Test  public void Link_xwiki_alias_only_colon() {	// only alias, but prepended with ":"; [[File:Wikipedia-logo.svg|40px|link=:w:|Wikipedia]]; DATE:2013-05-06
		fxt.ini_xwiki_add_wiki_and_user_("commons", "commons.wikimedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link=:commons:A/B|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/commons.wikimedia.org/wiki/A/B\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
		fxt.App().Url_alias_mgr().Clear();
	}
	@Test  public void Xwiki_file() {	// PURPOSE: if xwiki and File, ignore xwiki (hackish); DATE:2013-12-22
		Reg_xwiki_alias("test", "test.wikimedia.org");													// must register xwiki, else ttl will not parse it
		fxt.Wiki().Cfg_parser().Lnki_cfg().Xwiki_repo_mgr().Add_or_mod(ByteAry_.new_ascii_("test"));	// must add to xwiki_repo_mgr
		fxt.tst_Parse_page_wiki_str
			(	"[[test:File:A.png|12x10px]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
		fxt.Wiki().Cfg_parser_lnki_xwiki_repos_enabled_(false);	// disable for other tests
	}
	@Test  public void Xwiki_anchor() {
		Reg_xwiki_alias("test", "test.wikimedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[test:A#b]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/test.wikimedia.org/wiki/A#b\">test:A#b</a>"
			));
	}
	@Test  public void Xwiki_empty() {
		Reg_xwiki_alias("test", "test.wikimedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[test:]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/test.wikimedia.org/wiki/\">test:</a>"
			));
	}
	@Test  public void Xwiki_empty_literal() {
		Reg_xwiki_alias("test", "test.wikimedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[:test:]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/test.wikimedia.org/wiki/\">test:</a>"
			));
	}
	@Test  public void Xwiki_not_registered() {
		fxt.App().User().Wiki().Xwiki_mgr().Clear();
		fxt.Wiki().Xwiki_mgr().Add_full(ByteAry_.new_ascii_("test"), ByteAry_.new_ascii_("test.wikimedia.org"));	// register alias only, but not in user_wiki
		fxt.tst_Parse_page_wiki_str
			(	"[[test:A|A]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"http://test.wikimedia.org/wiki/A\">A</a>"
			));
	}
	@Test  public void Literal_lang() {
		Reg_xwiki_alias("fr", "fr.wikipedia.org");
		fxt.tst_Parse_page_wiki_str
			(	"[[:fr:A]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/site/fr.wikipedia.org/wiki/A\">A</a>"
			));
		Tfds.Eq(0, fxt.Page().Langs().Count());
	}
	@Test  public void Html_ent_pound() {
		fxt.tst_Parse_page_wiki_str
			(	"[[A&#35;b|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/wiki/A#b\">c</a>"
			));
	}
	@Test  public void Html_ent_ltr_a() {
		fxt.tst_Parse_page_wiki_str
			(	"[[A&#98;|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/wiki/Ab\">c</a>"
			));
	}
	@Test  public void Pipe_trick() {
		fxt.tst_Parse_page_wiki_str("[[Page1|]]"		, "<a href=\"/wiki/Page1\">Page1</a>");
		fxt.tst_Parse_page_wiki_str("[[Help:Page1|]]"	, "<a href=\"/wiki/Help:Page1\">Page1</a>");
	}
	@Test  public void Lnki_should_end_pre() {	// PURPOSE: if pre is already in effect, end it; EX: b:Knowing Knoppix/Other applications
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
		(	"a"
		,	" b"
		,	"[[File:A.png|thumb]]"
		,	"c"
		), String_.Concat_lines_nl_skipLast
		( 	"<p>a"
		,	"</p>"
		,	""
		,	"<pre>b"
		,	"</pre>"
		,	""
		,	"<div class=\"thumb tright\">"
		,	"  <div id=\"xowa_file_div_0\" class=\"thumbinner\" style=\"width:220px;\">"
		,	"    <a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/220px.png\" width=\"0\" height=\"0\" /></a>"
		, 	"    <div class=\"thumbcaption\">"
		,	"      <div class=\"magnify\">"
		,	"        <a href=\"/wiki/File:A.png\" class=\"internal\" title=\"Enlarge\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      "
		,	"    </div>"
		,	"  </div>"
		,	"</div>"
		,	""
		,	"<p>c"
		,	"</p>"
		,	""
		));
		fxt.Ctx().Para().Enabled_n_();		
	}
	private void Reg_xwiki_alias(String alias, String domain) {
		Xop_fxt.Reg_xwiki_alias(fxt.Wiki(), alias, domain);
	}
	@Test  public void Thumb_first_align_trumps_all() {	// PURPOSE: if there are multiple alignment instructions, take the first EX:[[File:A.png|thumb|center|left]] DATE:20121226
		fxt.tst_Parse_page_wiki_str("[[File:A.png|thumb|right|center]]"	// NOTE: right trumps center
			, String_.Concat_lines_nl_skipLast
			(	Xop_para_wkr_tst.File_html("File", "A.png", "7/0", "")
			,	""
			));
	}
	@Test  public void Eq() {
		fxt.tst_Parse_page_all_str("[[B|=]]", "<a href=\"/wiki/B\">=</a>");		
	}
	@Test  public void Href_encode_anchor() {	// PURPOSE: test separate encoding for ttl (%) and anchor (.)
		fxt.tst_Parse_page_all_str("[[^#^]]", "<a href=\"/wiki/%5E#.5E\">^#^</a>");
	}
	@Test  public void Href_question() {	// PURPOSE.fix: ttl with ? at end should not be considered qarg; DATE:2013-02-08
		fxt.tst_Parse_page_all_str("[[A?]]", "<a href=\"/wiki/A%3F\">A?</a>");
	}
	@Test  public void Href_question_2() {	// PURPOSE: ?action=edit should be encoded; DATE:2013-02-10
		fxt.tst_Parse_page_all_str("[[A?action=edit]]", "<a href=\"/wiki/A%3Faction%3Dedit\">A?action=edit</a>");
	}
	@Test  public void Encoded_url() {	// PURPOSE.fix: url-encoded characters broke parser when embedded in link; DATE:2013-03-01
		fxt.ini_xwiki_add_user_("commons.wikimedia.org");
		fxt.tst_Parse_page_wiki_str("[[File:A.png|link=//commons.wikimedia.org/wiki/%D0%97%D0%B0%D0%B3%D0%BB%D0%B0%D0%B2%D0%BD%D0%B0%D1%8F_%D1%81%D1%82%D1%80%D0%B0%D0%BD%D0%B8%D1%86%D0%B0?uselang=ru|b]]"
		, "<a href=\"/site/commons.wikimedia.org/wiki/Заглавная_страница?uselang=ru\" rel=\"nofollow\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"b\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>");
	}
	@Test  public void Link_invalid() {	// PURPOSE.fix: do not render invalid text; EX: link={{{1}}}; [[Fil:Randers_-_Hadsund_railway.png|120x160px|link={{{3}}}|Randers-Hadsund Jernbane]]; DATE:2013-03-04
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|12x10px|link={{{1}}}|c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/12px.png\" width=\"12\" height=\"10\" /></a>"
			));
	}
	@Test  public void Href_anchor_leading_space() {	// PURPOSE: ?action=edit should be encoded; DATE:2013-02-10
		fxt.tst_Parse_page_all_str("[[A #b]]", "<a href=\"/wiki/A#b\">A #b</a>");
	}
	@Test   public void Anchor_not_shown_for_wikipedia_ns() {	// PURPOSE: Help:A#b was omitting anchor; showing text of "Help:A"; DATE:2013-06-21
		fxt.tst_Parse_page_all_str("[[Help:A#b]]", "<a href=\"/wiki/Help:A#b\">Help:A#b</a>");
	}
	@Test   public void Anchor_shown_for_main_ns() {	// PURPOSE: counterpart to Anchor_not_shown_for_wikipedia_ns; DATE:2013-06-21
		fxt.tst_Parse_page_all_str("[[A#b]]", "<a href=\"/wiki/A#b\">A#b</a>");
	}
	@Test   public void Trail_en() {
		fxt.tst_Parse_page_all_str("[[Ab]]cd e", "<a href=\"/wiki/Ab\">Abcd</a> e");
	}
	@Test   public void Trail_fr() {
		byte[] c = ByteAry_.new_utf8_("ç");
		Xol_lnki_trail_mgr lnki_trail_mgr = fxt.Wiki().Lang().Lnki_trail_mgr();
		lnki_trail_mgr.Add(c);
		fxt.tst_Parse_page_all_str("[[Ab]]çd e", "<a href=\"/wiki/Ab\">Abçd</a> e");
		lnki_trail_mgr.Del(c);
	}
	@Test   public void Trim_category_normal() {	// PURPOSE: leading spaces / nls should be removed from normal Category, else false pre's or excessive line breaks
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	" [[Category:A]]"	// removes \s
		,	" [[Category:B]]"	// removes \n\s
		),	String_.Concat_lines_nl
		(	"<p>"
		,	"</p>"
		));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test   public void Trim_category_literal() {	// PURPOSE: do not trim ws if literal Category; EX:fr.wikiquote.org/wiki/Accueil; REF: https://sourceforge.net/p/xowa/tickets/167/; DATE:2013-07-10
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
		(	"[[:Category:A]]"
		,	"[[:Category:B]]"
		),	String_.Concat_lines_nl
		(	"<p><a href=\"/wiki/Category:A\">Category:A</a>"	// NOTE: technically WP converts to "</a> <a>" not "</a>\n<a>" (via HtmlTidy?)
		,	"<a href=\"/wiki/Category:B\">Category:B</a>"
		,	"</p>"
		));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Link_html_ent() {// PURPOSE:html entities should be converted to chars; EX:&nbsp; -> _; DATE:2013-12-16
		fxt.tst_Parse_page_wiki_str
			(	"[[File:A.png|link=b&nbsp;c]]", String_.Concat_lines_nl_skipLast
			(	"<a href=\"/wiki/B_c\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"
			));
	}
	@Test  public void Page() {
		fxt.tst_Parse_page_wiki("[[File:A.pdf|page=12]]"		, fxt.tkn_lnki_().Page_(12));
	}

//		@Test  public void Errs() {// FUTURE: restore; WHEN: lnki redo
//			fxt.tst_Parse_wiki("[[a] [[b]]"	, fxt.tkn_txt_(0, 2), fxt.tkn_txt_(2, 4), fxt.tkn_space_(4, 5), fxt.tkn_lnki_(5, 10));
//			fxt.tst_Parse_wiki("[[a\nb]]"	, fxt.tkn_lnki_(0, 2), fxt.tkn_nl_char_(1), fxt.tkn_txt_(3, 7));
//			fxt.IgnoreErrs_().tst_Parse_wiki("[[a] [[b]]"	, fxt.tkn_txt_(0, 5), fxt.tkn_lnki_(5, 10));
//			fxt.IgnoreErrs_().tst_Parse_wiki("[[a\nb]]"		, fxt.tkn_txt_(0, 3), fxt.tkn_txt_(3, 7));
//			fxt.IgnoreErrs_().tst_Parse_wiki("[[a|b|c]]"	, fxt.tkn_txt_(0, 5), fxt.tkn_txt_(5, 9)).tst_Msg("lnk.many");
//		}
}


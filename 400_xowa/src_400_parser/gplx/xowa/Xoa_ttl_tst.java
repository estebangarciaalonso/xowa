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
public class Xoa_ttl_tst {
	private Xop_fxt fxt = new Xop_fxt(); //Mwl_parser_fxt fxt = new Mwl_parser_fxt();
	@Before public void init() {
		fxt.Wiki().Xwiki_mgr().Add_full(ByteAry_.new_utf8_("fr"), ByteAry_.new_utf8_("fr.wikipedia.org"));
		test_raw = "Test page";
		this.Clear();
	}
	@Test  public void Ns()							{ttl_("Help:Test").NmsId_(Xow_ns_.Id_help).Page_txt_("Test").Run();}
	@Test  public void Ns_false()					{ttl_("Helpx:Test").NmsId_(Xow_ns_.Id_main).Page_txt_("Helpx:Test").Run();}
	@Test  public void Ns_multiple()				{ttl_("Help:Talk:test").Page_txt_("Talk:test").Run();}
	@Test  public void Full_txt()					{ttl_("Help:a & b").Full_txt_("Help:A & b").Run();}							// preserve
	@Test  public void Full_url()					{ttl_("Help:a & b").Full_url_("Help:A_%26_b").Run();}						// escape
	@Test  public void Page_txt()					{ttl_("a & b").Page_txt_("A & b").Run();}									// preserve;
	@Test  public void Page_txt_underline()			{ttl_("a_b").Page_txt_("A b").Page_url_("A_b").Run();}						// NOTE: raw is "a_b" but txt is "a b"
	@Test  public void Page_url()					{ttl_("a & b").Page_url_("A_%26_b").Run();}									// escape
	@Test  public void Page_url_w_ns()				{ttl_("Help:a & b").Page_url_("A_%26_b").Run();}							// remove ns
	@Test  public void Page_url_symbols()			{ttl_(";:@&=$ -_.+!*'(),/").Page_url_(";:@%26%3D$_-_.%2B!*%27(),/").Run();}	// symbols + space
	@Test  public void Leaf_txt()					{ttl_("root/mid/leaf").Leaf_txt_("leaf").Run();}
	@Test  public void Leaf_txt_slash_is_last()		{ttl_("root/mid/leaf/").Leaf_txt_("").Run();}
	@Test  public void Leaf_txt_no_slash()			{ttl_("root").Leaf_txt_("Root").Run();}
	@Test  public void Leaf_url()					{ttl_("root/mid/a & b").Leaf_url_("a_%26_b").Run();}						// NOTE: a not capitalized ("root" would be)
	@Test  public void Base_txt()					{ttl_("a & b/mid/leaf").Base_txt_("A & b/mid").Run();}
	@Test  public void Base_url()					{ttl_("a & b/mid/leaf").Base_url_("A_%26_b/mid").Run();}
	@Test  public void Root_txt()					{ttl_("root/mid/leaf").Root_txt_("Root").Run();}
	@Test  public void Rest_txt()					{ttl_("root/mid/leaf").Rest_txt_("mid/leaf").Run();}
	@Test  public void Talk_txt()					{ttl_("Help:test").Talk_txt_("Help talk:Test").Run();}
	@Test  public void Talk_txt_identity()			{ttl_("Help talk:test").Talk_txt_("Help talk:Test").Run();}
	@Test  public void Talk_url()					{ttl_("Help:a & b").Talk_url_("Help_talk:A_%26_b").Run();}
	@Test  public void Talk_txt_main()				{ttl_("test").Talk_txt_("Talk:Test").Run();}
	@Test  public void Subj_txt()					{ttl_("Help talk:test").Subj_txt_("Help:Test").Run();}
	@Test  public void Subj_txt_identity()			{ttl_("Help:test").Subj_txt_("Help:Test").Run();}
	@Test  public void Subj_url()					{ttl_("Help talk:a & b").Subj_url_("Help:A_%26_b").Run();}
	@Test  public void Subj_txt_main()				{ttl_("Talk:test").Subj_txt_("Test").Run();}
	@Test  public void ForceNormalLink_y()			{ttl_(":Help:test").ForceNormalLink_(1).Page_txt_("Test").Run();}
	@Test  public void ForceNormalLink_n()			{ttl_( "Help:test").ForceNormalLink_(0).Page_txt_("Test").Run();}
	@Test  public void Ws()							{ttl_("  a  b  ").Page_url_("A_b").Page_txt_("A b").Run();}
	@Test  public void Ws_ns()						{ttl_("  Help  :  a  b  ").Page_url_("A_b").Page_txt_("A b").Run();}
	@Test  public void Wik_y()						{ttl_("fr:test").Wik_txt_("fr").NmsId_(Xow_ns_.Id_main).Page_txt_("test").Run();}
	@Test  public void Wik_ctg_y()					{ttl_("fr:Category:test").Wik_txt_("fr").NmsId_(Xow_ns_.Id_main).Page_txt_("Category:test").Run();}
	@Test  public void Wik_n()						{ttl_("frx:test").Wik_txt_("").NmsId_(Xow_ns_.Id_main).Page_txt_("Frx:test").Run();}
	@Test  public void Wik_ctg_n()					{ttl_("frx:Category:test").Wik_txt_("").NmsId_(Xow_ns_.Id_main).Page_txt_("Frx:Category:test").Run();}
	@Test  public void Anch_y()						{ttl_("a#b").Full_txt_("A").Page_txt_("A").Anch_txt_("b").Run();}
	@Test  public void Anch_only()					{ttl_("#a").Full_txt_("").Page_txt_("").Anch_txt_("a").Run();}
	@Test  public void All_page()					{ttl_("test").Wik_txt_("").NmsId_(Xow_ns_.Id_main).Page_txt_("Test").Leaf_txt_("Test").Anch_txt_("").Run();}
	@Test  public void All_wik()					{ttl_("fr:test").Wik_txt_("fr").NmsId_(Xow_ns_.Id_main).Page_txt_("test").Leaf_txt_("test").Anch_txt_("").Run();}
	@Test  public void All_ns()						{ttl_("Help:test").Wik_txt_("").NmsId_(Xow_ns_.Id_help).Page_txt_("Test").Leaf_txt_("Test").Anch_txt_("").Run();}
	@Test  public void All_wik_ns()					{ttl_("fr:Help:test").Wik_txt_("fr").NmsId_(Xow_ns_.Id_main).Page_txt_("Help:test").Leaf_txt_("Help:test").Anch_txt_("").Run();}
	@Test  public void All_wik_ns_leaf_anch()		{ttl_("fr:Help:a/b/c#d").Wik_txt_("fr").NmsId_(Xow_ns_.Id_main).Page_txt_("Help:a/b/c").Leaf_txt_("c").Anch_txt_("d").Full_txt_("Help:a/b/c").Run();}
	@Test  public void Full_examples() {
		ttl_("fr:Category:a").Wik_txt_("fr").Full_txt_("Category:a").Page_txt_("Category:a").Run(); // NOTE: Page is "Category:a" b/c ns are unknowable in foreign wiki
	}
	@Test  public void Exc_Len_0() {
		ttl_("").Err_(Xop_ttl_log.Len_0).Run();
		ttl_(" ").Err_(Xop_ttl_log.Len_0).Run();
		ttl_("_").Err_(Xop_ttl_log.Len_0).Run();
		ttl_("_ _").Err_(Xop_ttl_log.Len_0).Run();
	}
	@Test  public void Exc_invalid() {
		ttl_("<!--A").Err_(Xop_ttl_log.Comment_eos).Run();
	}
	@Test  public void Exc_Len_max() {
		ttl_(String_.Repeat("A", 512)).Page_txt_(String_.Repeat("A", 512)).Run();
//			ttl_("File:" + String_.Repeat("A", 255)).Page_txt_(String_.Repeat("A", 255)).Run();	// DELETE: removing multi-byte check; DATE:2013-02-02
//			ttl_(String_.Repeat("A", 256)).Err_(Xop_ttl_log.Len_max).Run();
//			ttl_("Special:" + String_.Repeat("A", 255)).NmsId_(Xow_ns_.Id_special).Page_txt_(String_.Repeat("A", 255)).Run();
//			ttl_("Special:" + String_.Repeat("A", 512 + 8)).Err_(Xop_ttl_log.Len_max).Run();	// 8="Special:".length
	}
	@Test  public void Exc_colon_is_last_ns() {ttl_("Help:").Err_(Xop_ttl_log.Ttl_is_ns_only).Run();}
	@Test  public void Exc_colon_is_last_wik() {
		ttl_("fr:Help:").Wik_txt_("fr").Page_txt_("Help:").Run();
		ttl_("fr:_ _").Wik_txt_("fr").Page_txt_("").Run();	// NOTE: fr:_ _ is invalid (resolves to "fr:");
		ttl_("fr:Help:_ _").Wik_txt_("fr").Page_txt_("Help:").Run();
	}
	@Test  public void Exc_multiple_colon() {
		ttl_("fr::Help:Test").Wik_txt_("fr").Page_txt_(":Help:Test").Run();
		ttl_("fr::Test").Wik_txt_("fr").Page_txt_(":Test").Run();
		ttl_(":fr:Test").Wik_txt_("fr").Page_txt_("Test").ForceNormalLink_(1).Run();
		ttl_("::fr:Test").Wik_txt_("").Page_txt_(":fr:Test").ForceNormalLink_(1).Run();
	}
	@Test  public void Comment()				{ttl_("Ab<!--b-->").Page_txt_("Ab").Run();}
	@Test  public void Comment_eos()			{ttl_("Ab<!--b--").Page_txt_(null).Run();}
	@Test  public void Invalid_brace()			{ttl_("[[a]]").Err_(Xop_ttl_log.Invalid_char).Run();}
	@Test  public void Invalid_curly()			{ttl_("{{a}}").Err_(Xop_ttl_log.Invalid_char).Run();}
	@Test  public void Nl_trim()				{ttl_("\n\na\n\n").Page_txt_("A").Run();}
	@Test  public void Nl_mid()					{ttl_("a\nb").Page_txt_("A b").Run();}
	@Test  public void Trim_space()				{ttl_(" a ").Page_txt_("A").Run();}
	@Test  public void Trim_tab()				{ttl_("\ta\t").Page_txt_("A").Run();}
	@Test  public void HtmlEnt_eacute()			{ttl_("&eacute;").Page_txt_("é").Run();}
	@Test  public void HtmlEnt_endsInAmp()		{ttl_("Bisc &").Page_txt_("Bisc &").Run();}
	@Test  public void HtmlEnt_ltr_a()			{ttl_("A&#98;").Page_txt_("Ab").Run();}
	@Test  public void HtmlEnt_nbsp()			{ttl_("A&nbsp;b").Page_txt_("A b").Run();}	// NOTE: &nbsp must convert to space; EX:w:United States [[Image:Dust Bowl&nbsp;- Dallas, South Dakota 1936.jpg|220px|alt=]]
	@Test  public void Special()				{ttl_("Special:Random").NmsId_(Xow_ns_.Id_special).Page_txt_("Random").Run();}
	@Test  public void Special_xowa()			{ttl_("Special:xowa/Search/Ttl").NmsId_(Xow_ns_.Id_special).Page_txt_("Xowa/Search/Ttl").Run();}
	@Test  public void Bidi() {
		ttl_("A" + String_.new_utf8_(ByteAry_.ints_(226, 128, 142)) + "B").Page_txt_("AB").Run();
		ttl_("A" + String_.new_utf8_(ByteAry_.ints_(226, 128,  97)) + "B").Page_txt_("A" + String_.new_utf8_(ByteAry_.ints_(226, 128,  97)) + "B").Run();
	}
	@Test  public void Ns_should_precede_xwiki() {// PURPOSE: the "Wikipedia" in "Wikipedia:Main page" should be interpreted as namespace, not an alias
		fxt.Wiki().Xwiki_mgr().Add_full(ByteAry_.new_ascii_("Wikipedia"), ByteAry_.new_ascii_("en.wikipedia.org"));
		ttl_("Wikipedia:Test").Wik_txt_("").Full_txt_("Wikipedia:Test").Run();
	}
	@Test  public void Anchor_fails() {	// PURPOSE: :#batch:Main Page causes ttl to fail b/c # is treated as anchor; DATE:2013-01-02
		fxt.Wiki().Xwiki_mgr().Add_full(ByteAry_.new_ascii_("#batch"), ByteAry_.new_ascii_("none"));
		ttl_(":#batch:Main Page").Full_txt_("Main Page").Run();
	}
	@Test  public void Anchor_angles() {// PURPOSE: angles in anchor should be encoded; DATE: 2013-01-23
		ttl_("A#b<c>d").Full_txt_("A").Anch_txt_("b.3Cc.3Ed").Run();
	}
	@Test  public void Ns_case() {// PURPOSE: lowercase ns should be converted to proper case; EX: fr.w:Project:Sandbox (redirect link); en.w:Periclimenes imperator; [[commons:category:Periclimenes imperator|''Periclimenes imperator'']]; DATE: 2013-01-27
		ttl_("help:A").Full_txt_("Help:A").Run();
		ttl_("help talk:A").Full_txt_("Help talk:A").Run();
	}
	@Test  public void Base_txt_wo_qarg() {
		ttl_("Special:Search/A?b=c").Base_txt_wo_qarg("Search").Qarg_txt_("b=c").Run();
	}
	@Test  public void Leaf_txt_wo_qarg() {
		ttl_("Special:Search/A?b=c").Leaf_txt_wo_qarg("A").Qarg_txt_("b=c").Run();
	}
	@Test  public void Anchor_arg_in_non_main_ns() {
		ttl_("Help:A#B").Full_txt_("Help:A").Anch_txt_("B").Run();
	}
	Xoa_ttl_tst ttl_(String raw) {test_raw = raw; return this;} private String test_raw = "";
	Xoa_ttl_tst NmsId_(int v) {expd_nsId = v; return this;} private int expd_nsId = Int_.MinValue;
	Xoa_ttl_tst Page_txt_(String v) {expd_page_txt = v; return this;} private String expd_page_txt;
	Xoa_ttl_tst Page_url_(String v) {expd_page_url = v; return this;} private String expd_page_url;
	Xoa_ttl_tst Full_txt_(String v) {expd_full_txt = v; return this;} private String expd_full_txt;
	Xoa_ttl_tst Full_url_(String v) {expd_full_url = v; return this;} private String expd_full_url;
	Xoa_ttl_tst Leaf_txt_(String v) {expd_leaf_txt = v; return this;} private String expd_leaf_txt;
	Xoa_ttl_tst Leaf_url_(String v) {expd_leaf_url = v; return this;} private String expd_leaf_url;
	Xoa_ttl_tst Base_txt_(String v) {expd_base_txt = v; return this;} private String expd_base_txt;
	Xoa_ttl_tst Base_url_(String v) {expd_base_url = v; return this;} private String expd_base_url;
	Xoa_ttl_tst Root_txt_(String v) {expd_root_txt = v; return this;} private String expd_root_txt;
	Xoa_ttl_tst Rest_txt_(String v) {expd_rest_txt = v; return this;} private String expd_rest_txt;
	Xoa_ttl_tst Talk_txt_(String v) {expd_talk_txt = v; return this;} private String expd_talk_txt;
	Xoa_ttl_tst Talk_url_(String v) {expd_talk_url = v; return this;} private String expd_talk_url;
	Xoa_ttl_tst Subj_txt_(String v) {expd_subj_txt = v; return this;} private String expd_subj_txt;
	Xoa_ttl_tst Subj_url_(String v) {expd_subj_url = v; return this;} private String expd_subj_url;
	Xoa_ttl_tst Qarg_txt_(String v) {expd_qarg_txt = v; return this;} private String expd_qarg_txt;
	Xoa_ttl_tst ForceNormalLink_(int v) {expd_forceNormalLink = v; return this;} private int expd_forceNormalLink = -1;
	Xoa_ttl_tst Wik_txt_(String v) {expd_wik_txt = v; return this;} private String expd_wik_txt;
	Xoa_ttl_tst Anch_txt_(String v) {expd_anch_txt = v; return this;} private String expd_anch_txt;
	Xoa_ttl_tst Base_txt_wo_qarg(String v) {expd_base_txt_wo_qarg = v; return this;} private String expd_base_txt_wo_qarg;
	Xoa_ttl_tst Leaf_txt_wo_qarg(String v) {expd_leaf_txt_wo_qarg = v; return this;} private String expd_leaf_txt_wo_qarg;
	Xoa_ttl_tst Err_(Gfo_msg_itm v) {expd_err = v; return this;} Gfo_msg_itm expd_err;
	void Run() {
		Xoa_ttl actl = Xoa_ttl.parse_(fxt.Wiki(), ByteAry_.new_utf8_(test_raw));
		if (expd_err == null) {
			if (expd_nsId != Int_.MinValue) Tfds.Eq(expd_nsId, actl.Ns().Id(), "ns");
			if (expd_wik_txt != null) Tfds.Eq(expd_wik_txt, String_.new_utf8_(actl.Wik_txt()), "Wiki");
			if (expd_page_txt != null) Tfds.Eq(expd_page_txt, String_.new_utf8_(actl.Page_txt()), "Page_txt");
			if (expd_page_url != null) Tfds.Eq(expd_page_url, String_.new_utf8_(actl.Page_url()), "Page_url");
			if (expd_full_txt != null) Tfds.Eq(expd_full_txt, String_.new_utf8_(actl.Full_txt()), "Full_txt");
			if (expd_full_url != null) Tfds.Eq(expd_full_url, String_.new_utf8_(actl.Full_url()), "Full_url");
			if (expd_leaf_txt != null) Tfds.Eq(expd_leaf_txt, String_.new_utf8_(actl.Leaf_txt()), "Leaf_txt");
			if (expd_leaf_url != null) Tfds.Eq(expd_leaf_url, String_.new_utf8_(actl.Leaf_url()), "Leaf_url");
			if (expd_base_txt != null) Tfds.Eq(expd_base_txt, String_.new_utf8_(actl.Base_txt()), "Base_txt");
			if (expd_base_url != null) Tfds.Eq(expd_base_url, String_.new_utf8_(actl.Base_url()), "Base_url");
			if (expd_root_txt != null) Tfds.Eq(expd_root_txt, String_.new_utf8_(actl.Root_txt()), "Root_txt");
			if (expd_rest_txt != null) Tfds.Eq(expd_rest_txt, String_.new_utf8_(actl.Rest_txt()), "Rest_txt");
			if (expd_talk_txt != null) Tfds.Eq(expd_talk_txt, String_.new_utf8_(actl.Talk_txt()), "Talk_txt");
			if (expd_talk_url != null) Tfds.Eq(expd_talk_url, String_.new_utf8_(actl.Talk_url()), "Talk_url");
			if (expd_subj_txt != null) Tfds.Eq(expd_subj_txt, String_.new_utf8_(actl.Subj_txt()), "Subj_txt");
			if (expd_subj_url != null) Tfds.Eq(expd_subj_url, String_.new_utf8_(actl.Subj_url()), "Subj_url");
			if (expd_anch_txt != null) Tfds.Eq(expd_anch_txt, String_.new_utf8_(actl.Anch_txt()), "Anch_txt");
			if (expd_qarg_txt != null) Tfds.Eq(expd_qarg_txt, String_.new_utf8_(actl.Qarg_txt()), "Qarg_txt");
			if (expd_base_txt_wo_qarg != null) Tfds.Eq(expd_base_txt_wo_qarg, String_.new_utf8_(actl.Base_txt_wo_qarg()), "Base_txt_wo_qarg");
			if (expd_leaf_txt_wo_qarg != null) Tfds.Eq(expd_leaf_txt_wo_qarg, String_.new_utf8_(actl.Leaf_txt_wo_qarg()), "Leaf_txt_wo_qarg");
			if (expd_forceNormalLink != -1) Tfds.Eq(expd_forceNormalLink == 1, actl.ForceLiteralLink(), "ForceLiteralLink");
		}
		else {
			Tfds.Eq_ary(String_.Ary(String_.new_utf8_(expd_err.Owner().Path()) + "." + String_.new_utf8_(expd_err.Key_bry())), fxt.Log_xtoAry());
		}
		Clear();
	}
	void Clear() {
		expd_nsId = Int_.MinValue;
		expd_wik_txt = expd_full_txt = expd_full_url = expd_page_txt = expd_page_url = expd_leaf_txt = expd_leaf_url = expd_base_txt = expd_base_url 
			= expd_root_txt = expd_rest_txt = expd_talk_txt = expd_talk_url = expd_subj_txt = expd_subj_url = expd_anch_txt 
			= expd_base_txt_wo_qarg = expd_leaf_txt_wo_qarg = expd_qarg_txt = null;
		expd_err = null;
		expd_forceNormalLink = -1;			
		fxt.Log_clear();
	}
}

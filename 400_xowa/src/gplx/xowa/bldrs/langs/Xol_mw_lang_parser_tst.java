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
package gplx.xowa.bldrs.langs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import org.junit.*;
import gplx.intl.*;
public class Xol_mw_lang_parser_tst {
	Xol_mw_lang_parser_fxt fxt = new Xol_mw_lang_parser_fxt();
	@Before public void init() {fxt.Clear();}
	@Test  public void Core_keywords() {
		fxt.Parse_core("$magicWords = array('toc' => array(0, 'a1', 'a2', 'a3'), 'notoc' => array(1, 'b1', 'b2', 'b3'));")
			.Tst_keyword(Xol_kwd_grp_.Id_toc, false, "a1", "a2", "a3")
			.Tst_keyword(Xol_kwd_grp_.Id_notoc, true, "b1", "b2", "b3")
			;
	}
	@Test  public void Core_keywords_img_thumb() {
		fxt.Parse_core("$magicWords = array('img_thumbnail' => array(1, 'thumb', 'thmb'));")
			.Tst_keyword_img("thumb", Xol_lnki_arg_parser.Tid_thumb)
			.Tst_keyword_img("thmb"	, Xol_lnki_arg_parser.Tid_thumb)
			.Tst_keyword_img("thum"	, Xol_lnki_arg_parser.Tid_caption)
			;
	}
	@Test  public void Core_keywords_img_upright() {
		fxt.Parse_core("$magicWords = array('img_upright' => array(1, 'upright', 'upright=$1', 'upright $1'));")
			.Tst_keyword_img("upright"	, Xol_lnki_arg_parser.Tid_upright)
			.Tst_keyword_img("upright "	, Xol_lnki_arg_parser.Tid_upright)
			;
	}
	@Test  public void Core_keywords_func_currentmonth() {
		Tfds.Now_enabled_y_();
		fxt.Parse_core("$magicWords = array('currentmonth' => array(0, 'MOISACTUEL'));")
			.Tst_parse("{{MOISACTUEL}}", "01")
			;
		Tfds.Now_enabled_n_();
	}
	@Test  public void Core_keywords_func_ns() {
		fxt.Parse_core("$magicWords = array('ns' => array(0, 'ESPACEN'));")
			.Tst_parse("{{ESPACEN:6}}", "File")
			;
	}
	@Test  public void Keywords_img_dim() {
		fxt.Parse_core("$magicWords = array('img_width' => array(1, '$1pxl'));")
			.Tst_keyword_img("50pxl", Xol_lnki_arg_parser.Tid_dim)
			.Tst_keyword_img("50pxlpxl", Xol_lnki_arg_parser.Tid_dim)
			.Tst_keyword_img("50xl", Xol_lnki_arg_parser.Tid_caption)
			;
	}
	@Test  public void Core_namespaces_names() {
		fxt.Parse_core("$namespaceNames = array(NS_FILE => 'Fichier');")
			.Sync_ns()
			.Tst_ns_lkp("Fichier", Xow_ns_.Id_file)
			.Tst_ns_lkp("File"	, Xow_ns_.Id_unknown)
			;
	}
	@Test  public void Core_namespaces_aliases() {
		fxt.Parse_core("$namespaceAliases = array('Discussion_Fichier' => NS_FILE_TALK);")
			.Sync_ns()
			.Tst_ns_lkp("Discussion Fichier", Xow_ns_.Id_file_talk)
			.Tst_ns_lkp("Discussion Fichierx", Xow_ns_.Id_unknown)
			;
	}
	@Test  public void Core_specialPageAliases() {
		fxt.Parse_core("$specialPageAliases = array('Randompage' => array('PageAuHasard', 'Page_au_hasard'));")
			.Test_specialPageAliases("Randompage", "PageAuHasard", "Page_au_hasard")
			;
	}
	@Test  public void Xtn_keywords_fr() {fxt.Parse_xtn("$magicWords['fr'] = array('if' => array(0, 'si' ));").Tst_parse("{{si:|y|n}}", "n");}
	@Test  public void Xtn_keywords_de() {fxt.Parse_xtn("$magicWords['de'] = array('if' => array(0, 'si' ));").Tst_parse("{{si:|y|n}}", "{{:si:}}");}
	@Test  public void Core_messages() {
		fxt.Parse_core("$messages = array('sunday' => 'dimanche');")
			.Tst_message("sunday", 0, "dimanche", false)
			;
	}
	@Test  public void Fallback() {
		fxt.Parse_core("$fallback = 'zh-hans';");
		Tfds.Eq("zh-hans", String_.new_utf8_(fxt.Lang().Fallback_bry()));
	}
	@Test  public void Separator_transform_table() {
//			fxt.Lang().Num_fmt_mgr().Clear().Dec_dlm_(ByteAry_.new_ascii_(".")).Grps_add(new Gfo_num_fmt_grp(ByteAry_.new_utf8_(","), 3, true));
		fxt.Parse_core("$separatorTransformTable = array( ',' => '.', '.' => ',' );");
		fxt.Num_fmt_tst("1234,56", "1.234,56");
	}
	@Test  public void Separator_transform_table_fr() {
//			fxt.Lang().Num_fmt_mgr().Clear().Dec_dlm_(ByteAry_.new_ascii_(".")).Grps_add(new Gfo_num_fmt_grp(ByteAry_.new_utf8_(","), 3, true));
		fxt.Parse_core("$separatorTransformTable = array( ',' => '\\xc2\\xa0', '.' => ',' );");
		fxt.Num_fmt_tst("1234,56", "1 234,56");	// NOTE: nbsp;
	}
	@Test   public void Bld() {
		//             mem/xowa/user/test_user/lang/mediawiki/messages/
		fxt.Save_file("mem/xowa/user/test_user/lang/mediawiki/messages/MessagesFr.php"
			,	"$fallback = 'zh-hans';"
			,	"$rtl = true;"
			,	"$namespaceNames = array(NS_FILE => 'Filex');"
			,	"$namespaceAliases = array('File Discussion' => NS_FILE_TALK);"
			,	"$magicWords = array('currentmonth' => array(0, 'CUR_MONTH'));"
			,	"$messages = array('sunday' => 'sunday');"
			);
		fxt.Save_file("mem/xowa/user/test_user/lang/mediawiki/extensions/Test.il8n.php"
			,	"$magicWords['fr'] = array('currentyear' => array(0, 'CUR_YEAR'));"
			,	"$messages['fr'] = array('monday' => 'monday');"
			);
		fxt.Run_bld_all();
		fxt.Tst_file("mem/xowa/user/test_user/lang/xowa/fr.gfs", String_.Concat_lines_nl
			(	"this"
			,	".fallback_load('zh-hans')"
			,	".dir_rtl_('y')"
			,	".ns_names"
			,	"  .load_text("
			,	"<:['"
			,	"6|Filex"
			,	"']:>"
			,	").lang"
			,	".ns_aliases"
			,	"  .load_text("
			,	"<:['"
			,	"7|File Discussion"
			,	"']:>"
			,	").lang"
			,	".keywords"
			,	"  .load_text("
			,	"<:['"
			,	"currentmonth|0|CUR_MONTH~"
			,	"currentyear|0|CUR_YEAR~"
			,	"']:>"
			,	").lang"
			,	".messages"
			,	"  .load_text("
			,	"<:['"
			,	"sunday|sunday"
			,	"monday|monday"
			,	"']:>"
			,	").lang"
			,	";"
			));
	}
	@Test   public void Dir_ltr() {
		fxt.Parse_core("$rtl = 'true';");
		Tfds.Eq(false, fxt.Lang().Dir_ltr());
	}
}
class Xol_mw_lang_parser_fxt {
	Xoa_app app; Xow_wiki wiki; Xop_fxt fxt;
	Xol_mw_lang_parser parser = new Xol_mw_lang_parser(new Gfo_msg_log("test")); ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255);
	public void Clear() {
		if (app == null) {
			app = Xoa_app_fxt.app_();
			wiki = Xoa_app_fxt.wiki_tst_(app);
			fxt = new Xop_fxt(app, wiki);
		}
		app.Lang_mgr().Clear();// NOTE: always clear the lang
		lang = app.Lang_mgr().Get_by_key_or_new(ByteAry_.new_ascii_("fr"));
		GfoInvkAble_.InvkCmd_val(wiki, Xow_wiki.Invk_lang_, ByteAry_.new_ascii_("fr"));
	}
	public Xol_lang Lang() {return lang;} private Xol_lang lang;
	public void Run_smoke(Io_url user_root) {
		parser.Bld_all(app, user_root);
	}
	public void Num_fmt_tst(String raw, String expd) {Tfds.Eq(expd, String_.new_utf8_(lang.Num_fmt_mgr().Fmt(ByteAry_.new_utf8_(raw))));}
	public void Run_bld_all() {parser.Bld_all(app, app.User().Fsys_mgr().Root_dir());}
	public void Save_file(String path, String... lines) {
		Io_mgr._.SaveFilStr(Io_url_.mem_fil_(path), String_.Concat_lines_nl(lines));
	}
	public void Tst_file(String path, String expd) {
		Io_url url = Io_url_.mem_fil_(path);
		String actl = Io_mgr._.LoadFilStr(url);
//            Tfds.Write(actl);
		Tfds.Eq_str_lines(expd, actl);
	}
	public Xol_mw_lang_parser_fxt Parse_core(String raw)				{parser.Parse_core(raw, lang, tmp_bfr, Xol_lang_transform_null._); return this;}
	public Xol_mw_lang_parser_fxt Parse_xtn (String raw)				{parser.Parse_xtn(raw, Io_url_.Null, app, tmp_bfr, false, Xol_lang_transform_null._); lang.Evt_lang_changed(); return this;}
	public Xol_mw_lang_parser_fxt Tst_keyword(int id, boolean case_sensitive, String... words) {
		Xol_kwd_grp lst = lang.Kwd_mgr().Get_at(id); if (lst == null) throw Err_.new_("list should not be null");
		Tfds.Eq(case_sensitive, lst.Case_match());
		int actl_len = lst.Itms().length;
		String[] actl = new String[actl_len];
		for (int i = 0; i < actl_len; i++)
			actl[i] = String_.new_utf8_(lst.Itms()[i].Bry());
		Tfds.Eq_ary_str(words, actl);
		return this;
	}
	public Xol_mw_lang_parser_fxt Tst_keyword_img(String key_str, byte tid) {
		byte[] key = ByteAry_.new_utf8_(key_str);
		IntRef width = IntRef.zero_(), height = IntRef.zero_();
		Tfds.Eq(tid, lang.Lnki_arg_parser().Identify_tid(key, 0, key.length, width, height));
		return this;
	}
	public Xol_mw_lang_parser_fxt Tst_parse(String raw, String expd) {
		fxt.Reset();
		fxt.tst_Parse_page_all_str(raw, expd);
		return this;
	}
	public Xol_mw_lang_parser_fxt Tst_ns_lkp(String key_str, int id) {
		byte[] key = ByteAry_.new_utf8_(key_str);
		Xow_ns ns = (Xow_ns)wiki.Ns_mgr().Trie_match_exact(key, 0, key.length);
		int actl = ns == null ? Xow_ns_.Id_unknown : ns.Id();
		Tfds.Eq(id, actl);
		return this;
	}
	public Xol_mw_lang_parser_fxt Test_specialPageAliases(String special, String... expd_aliases) {
		Xol_specials_itm actl_aliases = lang.Specials_mgr().Get_by_key(ByteAry_.new_utf8_(special));
		Tfds.Eq_ary_str(expd_aliases, To_str_ary(actl_aliases));
		return this;
	}
	private String[] To_str_ary(Xol_specials_itm itm) {
		int len = itm.Aliases().length;
		String[] rv = new String[len];
		for (int i = 0; i < len; i++) {
			rv[i] = String_.new_utf8_((byte[])itm.Aliases()[i]);
		}
		return rv;
	}
	public Xol_mw_lang_parser_fxt Sync_ns() {
		Xow_ns_mgr_.rebuild_(lang, wiki.Ns_mgr());
		return this;
	}
	public Xol_mw_lang_parser_fxt Tst_message(String key, int id, String val, boolean fmt) {
		Xol_msg_itm itm = lang.Msg_mgr().Itm_by_key_or_new(ByteAry_.new_ascii_(key));
		Tfds.Eq(id, itm.Id());
		Tfds.Eq(val, String_.new_utf8_(itm.Val()));
		Tfds.Eq(fmt, itm.Has_fmt_arg());
		return this;
	}
}

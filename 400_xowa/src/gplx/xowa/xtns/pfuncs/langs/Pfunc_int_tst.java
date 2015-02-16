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
package gplx.xowa.xtns.pfuncs.langs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import org.junit.*;
public class Pfunc_int_tst {
	@Before public void init() {fxt.Reset();} private Pf_msg_mgr_fxt fxt = new Pf_msg_mgr_fxt();
	@Test  public void Basic()					{fxt.Test_parse_en("{{int:january}}"								, "January");}
	@Test  public void Upper()					{fxt.Test_parse_en("{{int:JANUARY}}"								, "January");}
	@Test  public void Unknown()				{fxt.Test_parse_en("{{int:unknown_msg}}"							, "<unknown_msg>");}
	@Test  public void Fmt()					{fxt.Test_parse_en("{{int:pfunc_expr_unrecognised_word|1a}}"		, "Expression error: Unrecognised word \"1a\"");}
	@Test  public void Tmpl_txt() {
		fxt.Init_msg_gfs("tst_msg", "{{#expr:1}}", false, true);
		fxt.Test_parse_en("{{int:tst_msg}}", "1");
	}
	@Test  public void Tmpl_html_entity() {	// PURPOSE: check that &nbsp; is swapped out correctly for (194,160); PAGE:fr.s:Main_Page DATE:2014-08-17
		fxt.Init_msg_gfs("tst_msg", "A&nbsp;B", false, true);
		fxt.Test_parse_en("{{int:tst_msg}}", "A B");	// NOTE: &nbsp;
	}
	@Test  public void Lang_current_defaults_to_en() {	// PURPOSE: specifying same language as current returns same; ie: int:january/en -> int:january
		fxt.Test_parse_en("{{int:january/en}}", "January");
	}
	@Test  public void Lang_specified_by_page() {
		fxt.Test_parse_lang("fr", "{{int:Lang}}", "fr");	// NOTE: "Lang" msg is added by Xol_lang; message_mgr.Itm_by_key_or_new(Bry_.new_utf8_("Lang")).Atrs_set(key_bry, false, false);
	}
	@Test  public void Lang_missing_msg_return_en() {	// PURPOSE: if key does not exist in non-english language, use English; EX: la.w:Fasciculus:HannibalFrescoCapitolinec1510.jpg; DATE:2013-09-10
		fxt.Init_msg_gfs("en_only_key", "en_only_val", false, false);
		fxt.Test_parse_lang("fr", "{{int:en_only_key}}", "en_only_val");
	}
	@Test  public void Err_fmt_failed() {	// PURPOSE: if no args passed to msg, return "$1", not "~{0}"
		fxt.Init_msg_gfs("tst_msg", "a~{0}b", true, false);
		fxt.Test_parse_en("{{int:tst_msg}}"							, "a$1b");
	}
	@Test  public void Mediawiki_overrides_gfs() {
		fxt.Init_msg_gfs("mw_overrides", "gfs", false, false);
		fxt.Init_msg_db("mw_overrides", "mw");
		fxt.Test_parse_en("{{int:mw_overrides}}", "mw");
	}
	@Test  public void Convert_php() {
		fxt.Init_msg_db("convert_php", "a\\\\b\\$c$1e");
		fxt.Test_parse_en("{{int:convert_php|d}}", "a\\b$cde");
	}
	@Test  public void Convert_php_tilde() {	// PURPOSE: tildes should be escaped, else will fail inside ByteAryBfrFmtr; DATE:2013-11-11
		fxt.Init_msg_db("convert_php_tilde", "$1~u");
		fxt.Test_parse_en("{{int:convert_php_tilde|a}}", "a~u");
	}
	@Test  public void Unknown_val_returns_en() {	// PURPOSE: if no "january" in "fr.gfs" and no "january/fr" in mw, use january in "en.gfs"; EX:none
		fxt.Test_parse_lang("fr", "{{int:january/fr}}", "January");
	}
	@Test  public void Unknown_lang_returns_en() {	// PURPOSE: unknown lang default to english; EX:none; DATE:2014-05-09
		fxt.Test_parse_en("{{int:january/unknown}}", "January");
	}
	@Test  public void Transclude_mw_do_not_strip_lang() {	// PURPOSE: if /lang matches wiki.lang, do not strip it, else stack overflow; EX:pl.d:Wikislownik:Bar/Archiwum_6; newarticletext/pl; DATE:2014-05-13
		fxt.Init_msg_db("january/en", "January_en");
		fxt.Test_parse_en("{{MediaWiki:january/en}}", "January_en");
	}
	@Test  public void Transclude_gfs() {	// PURPOSE: transclusion of {{MediaWiki}} pages should call {{int}} instead; EX:zh.w:Main_Page; DATE:2014-05-09
		fxt.Test_parse_en("{{MediaWiki:january/en}}", "January");	// NOTE: no page exists called "MediaWiki:january/en", but returns "{{int:january/en}}" value
	}
	@Test  public void Create_msg_in_wiki_not_lang() {	// PURPOSE: if two wikis share same language and msg is missing from one, do not mark it missing in the other; EX: home/wiki/Main_Page and en.w:Main_Page; DATE:2014-05-13
		Xow_wiki enwiktionary = fxt.Make_wiki("en.wiktionary.org");
		fxt.Init_msg_db(enwiktionary, "wiki_only_msg", "enwiktionary_msg");
		fxt.Init_msg_gfs("wiki_only_msg", "en_gfs_msg", false, false);
		fxt.Test_parse_en("{{int:wiki_only_msg}}", "en_gfs_msg");
		fxt.Test_parse_wiki(enwiktionary, "{{int:wiki_only_msg}}", "enwiktionary_msg");
	}
}
class Pf_msg_mgr_fxt {
	private Xop_fxt fxt;
	private Xol_lang en_lang;
	private Xow_wiki en_wiki;
	public void Reset() {
		fxt = new Xop_fxt();	// new fxt, else transclude tests will fail
		en_wiki = fxt.Wiki();
		en_lang = en_wiki.Lang();
	}
	public void Init_msg_gfs(String key, String val, boolean fmt, boolean tmpl) {Init_msg_gfs(en_lang, key, val, fmt, tmpl);}
	public void Init_msg_gfs(Xol_lang lang, String key, String val, boolean fmt, boolean tmpl) {
		Xol_msg_itm msg_itm = lang.Msg_mgr().Itm_by_key_or_new(Bry_.new_utf8_(key));
		msg_itm.Atrs_set(Bry_.new_utf8_(val), fmt, tmpl);
	}
	public void Init_msg_db(String ttl, String val) {Init_msg_db(en_wiki, ttl, val);}
	public void Init_msg_db(Xow_wiki wiki, String ttl, String val) {
		fxt.Init_page_create(wiki, "MediaWiki:" + ttl, val);
	}
	public Xow_wiki Make_wiki(String domain) {return fxt.App().Wiki_mgr().Get_by_key_or_make(Bry_.new_utf8_(domain));}
	public void Test_parse_en(String raw, String expd) {
		fxt.Test_parse_tmpl_str_test(raw, "{{test}}"	, expd);
	}
	public void Test_parse_wiki(Xow_wiki alt_wiki, String raw, String expd) {
		Xop_fxt alt_fxt = new Xop_fxt(fxt.App(), alt_wiki);
		alt_fxt.Test_parse_tmpl_str_test(raw, "{{test}}"	, expd);
	}
	public void Test_parse_lang(String other_lang_id, String raw, String expd) {
		Xol_lang other_lang = fxt.App().Lang_mgr().Get_by_key_or_new(Bry_.new_ascii_(other_lang_id));
		other_lang.Init_by_load();
		fxt.Page().Lang_(other_lang); 
		fxt.Test_parse_tmpl_str_test(raw, "{{test}}", expd);
		fxt.Page().Lang_(en_lang);	// NOTE: must reset back to en_lang, else rest of tests will look up under fr
	}
}


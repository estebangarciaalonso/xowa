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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Scrib_lib_language_tst {
	@Before public void init() {
		fxt.Clear();
		fxt.Init_page("{{#invoke:Mod_0|Func_0}}");
		lib = fxt.Engine().Lib_language();
	}	Scrib_pf_invoke_fxt fxt = new Scrib_pf_invoke_fxt(); Scrib_lib lib;
	@Test  public void GetContLangCode() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_getContLangCode, Object_.Ary_empty, "en");
	}
	@Test  public void IsSupportedLanguage() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isSupportedLanguage, Object_.Ary("fr"), "true");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isSupportedLanguage, Object_.Ary("qq"), "false");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isSupportedLanguage, Object_.Ary("EN"), "false");
	}
	@Test  public void IsKnownLanguageTag() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isKnownLanguageTag, Object_.Ary("fr"), "true");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isKnownLanguageTag, Object_.Ary("qq"), "false");
	}
	@Test  public void IsValidCode() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isValidCode, Object_.Ary("a,b"), "true");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isValidCode, Object_.Ary("a'b"), "false");
	}
	@Test  public void IsValidBuiltInCode() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isValidBuiltInCode, Object_.Ary("e-N"), "true");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isValidBuiltInCode, Object_.Ary("e n"), "false");
	}
	@Test  public void FetchLanguageName() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_fetchLanguageName, Object_.Ary("en"), "English");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_fetchLanguageName, Object_.Ary("fr"), "Français");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_fetchLanguageName, Object_.Ary("enx"), "");
	}
	@Test  public void FormatNum() {
		Xol_lang other_lang = fxt.Engine().App().Lang_mgr().Get_by_key_or_new(ByteAry_.new_ascii_("de"));
		other_lang.Num_fmt_mgr().Grps_add(new gplx.intl.Gfo_num_fmt_grp(new byte[] {Byte_ascii.Dot}, 3, true));
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_formatNum, Object_.Ary("en", 1234), "1,234");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_formatNum, Object_.Ary("de", 1234), "1.234");
	}
	@Test  public void FormatDate() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_formatDate, Object_.Ary("en", "Y-m-d", "2013-03-17", false), "2013-03-17");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_formatDate, Object_.Ary("en", "Y-m-d"), DateAdp_.Now().XtoStr_fmt_yyyy_MM_dd());	// empty date should default to today;
	}
	@Test  public void FormatDate_date_omitted() {	// PURPOSE: some calls skip the date; retrieve arg_4 by int; pl.w:L._Frank_Baum
		Tfds.Now_enabled_y_();
		Tfds.Now_set(DateAdp_.new_(2013, 12, 19, 1, 2, 3, 4));
		fxt.Test_lib_proc_kv(lib, Scrib_lib_language.Invk_formatDate, new KeyVal[] {KeyVal_.int_(1, "en"), KeyVal_.int_(2, "Y-m-d"), KeyVal_.int_(4, false)}, "2013-12-19");
		Tfds.Now_enabled_n_();
	}
	@Test  public void Lc() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_lc, Object_.Ary("en", "ABC"), "abc");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_lc, Object_.Ary("en"), "");
	}
	@Test  public void Uc() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_uc, Object_.Ary("en", "abc"), "ABC");
	}
	@Test  public void LcFirst() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_lcfirst, Object_.Ary("en", "ABC"), "aBC");
	}
	@Test  public void UcFirst() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_uc, Object_.Ary("en", "abc"), "ABC");
	}
	@Test  public void ParseFormattedNumber() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_parseFormattedNumber, Object_.Ary("en", "1,234.56"), "1234.56");
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_parseFormattedNumber, Object_.Ary("en"), Scrib_pf_invoke_fxt.Null_rslt);		// PURPOSE: missing arg should not fail; EX: ru.w:Туйон DATE:2014-01-06
	}
	@Test  public void ConvertGrammar() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_convertGrammar, Object_.Ary("fi", "talo", "elative"), "talosta");
	}
	@Test  public void ConvertPlural() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_convertPlural, Object_.Ary("ru", 5, "a", "b", "c"), "c");
	}
	@Test  public void IsRTL() {
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isRTL, Object_.Ary("en"), "false");
		Xol_lang other_lang = fxt.Engine().App().Lang_mgr().Get_by_key_or_new(ByteAry_.new_ascii_("ar"));
		GfoInvkAble_.InvkCmd_val(other_lang, Xol_lang.Invk_dir_rtl_, true);
		fxt.Test_lib_proc(lib, Scrib_lib_language.Invk_isRTL, Object_.Ary("ar"), "true");
	}
}	

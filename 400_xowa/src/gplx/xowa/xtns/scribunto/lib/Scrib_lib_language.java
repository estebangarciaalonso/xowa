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
package gplx.xowa.xtns.scribunto.lib; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*;
public class Scrib_lib_language implements Scrib_lib {
	public Scrib_lib_language(Scrib_core core) {this.core = core;} Scrib_core core;
	public Scrib_lua_mod Mod() {return mod;} Scrib_lua_mod mod;
	public Scrib_lib Init() {procs.Init_by_lib(this, Proc_names); return this;}
	public Scrib_lua_mod Register(Scrib_core core, Io_url script_dir) {
		Init();
		mod = core.RegisterInterface(this, script_dir.GenSubFil("mw.language.lua"));
		notify_lang_changed_fnc = mod.Fncs_get_by_key("notify_lang_changed");
		return mod;
	}	private Scrib_lua_proc notify_lang_changed_fnc;
	public Scrib_proc_mgr Procs() {return procs;} private Scrib_proc_mgr procs = new Scrib_proc_mgr();
	public boolean Procs_exec(int key, Scrib_proc_args args, Scrib_proc_rslt rslt) {
		switch (key) {
			case Proc_getContLangCode:								return GetContLangCode(args, rslt);
			case Proc_isSupportedLanguage:							return IsSupportedLanguage(args, rslt);
			case Proc_isKnownLanguageTag:							return IsKnownLanguageTag(args, rslt);
			case Proc_isValidCode:									return IsValidCode(args, rslt);
			case Proc_isValidBuiltInCode:							return IsValidBuiltInCode(args, rslt);
			case Proc_fetchLanguageName:							return FetchLanguageName(args, rslt);
			case Proc_lcfirst:										return Lcfirst(args, rslt);
			case Proc_ucfirst:										return Ucfirst(args, rslt);
			case Proc_lc:											return Lc(args, rslt);
			case Proc_uc:											return Uc(args, rslt);
			case Proc_caseFold:										return CaseFold(args, rslt);
			case Proc_formatNum:									return FormatNum(args, rslt);
			case Proc_formatDate:									return FormatDate(args, rslt);
			case Proc_formatDuration:								return formatDuration(args, rslt);
			case Proc_getDurationIntervals:							return getDurationIntervals(args, rslt);
			case Proc_parseFormattedNumber:							return ParseFormattedNumber(args, rslt);
			case Proc_convertPlural:								return ConvertPlural(args, rslt);
			case Proc_convertGrammar:								return ConvertGrammar(args, rslt);
			case Proc_gender:										return gender(args, rslt);
			case Proc_isRTL:										return IsRTL(args, rslt);
			default: throw Err_.unhandled(key);
		}
	}
	private static final int
	  Proc_getContLangCode = 0, Proc_isSupportedLanguage = 1, Proc_isKnownLanguageTag = 2
	, Proc_isValidCode = 3, Proc_isValidBuiltInCode = 4, Proc_fetchLanguageName = 5
	, Proc_lcfirst = 6, Proc_ucfirst = 7, Proc_lc = 8, Proc_uc = 9, Proc_caseFold = 10
	, Proc_formatNum = 11, Proc_formatDate = 12, Proc_formatDuration = 13, Proc_getDurationIntervals = 14, Proc_parseFormattedNumber = 15
	, Proc_convertPlural = 16, Proc_convertGrammar = 17, Proc_gender = 18, Proc_isRTL = 19
	;
	public static final String 
	  Invk_getContLangCode = "getContLangCode", Invk_isSupportedLanguage = "isSupportedLanguage", Invk_isKnownLanguageTag = "isKnownLanguageTag"
	, Invk_isValidCode = "isValidCode", Invk_isValidBuiltInCode = "isValidBuiltInCode", Invk_fetchLanguageName = "fetchLanguageName"
	, Invk_lcfirst = "lcfirst", Invk_ucfirst = "ucfirst", Invk_lc = "lc", Invk_uc = "uc", Invk_caseFold = "caseFold"
	, Invk_formatNum = "formatNum", Invk_formatDate = "formatDate", Invk_formatDuration = "formatDuration", Invk_getDurationIntervals = "getDurationIntervals", Invk_parseFormattedNumber = "parseFormattedNumber"
	, Invk_convertPlural = "convertPlural", Invk_convertGrammar = "convertGrammar", Invk_gender = "gender", Invk_isRTL = "isRTL"
	;
	private static final String[] Proc_names = String_.Ary
	( Invk_getContLangCode, Invk_isSupportedLanguage, Invk_isKnownLanguageTag
	, Invk_isValidCode, Invk_isValidBuiltInCode, Invk_fetchLanguageName
	, Invk_lcfirst, Invk_ucfirst, Invk_lc, Invk_uc, Invk_caseFold
	, Invk_formatNum, Invk_formatDate, Invk_formatDuration, Invk_getDurationIntervals, Invk_parseFormattedNumber
	, Invk_convertPlural, Invk_convertGrammar, Invk_gender, Invk_isRTL
	);
	public void Notify_lang_changed() {if (notify_lang_changed_fnc != null) core.Interpreter().CallFunction(notify_lang_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public boolean GetContLangCode(Scrib_proc_args args, Scrib_proc_rslt rslt)		{return rslt.Init_obj(core.Ctx().Lang().Key_str());}
	public boolean IsSupportedLanguage(Scrib_proc_args args, Scrib_proc_rslt rslt)	{return IsKnownLanguageTag(args, rslt);}// NOTE: checks if "MessagesXX.php" exists; note that xowa has all "MessagesXX.php"; for now, assume same functionality as IsKnownLanguageTag (worst case is that a small wiki depends on a lang not being there; will need to put in a "wiki.Langs()" then)
	public boolean IsKnownLanguageTag(Scrib_proc_args args, Scrib_proc_rslt rslt) {	// NOTE: checks if in languages/Names.php
		String lang_code = args.Cast_str_or_null(0);
		boolean exists = false;
		if (	lang_code != null									// null check; protecting against Module passing in nil from lua
			&&	String_.Eq(lang_code, String_.Lower(lang_code))		// must be lower-case; REF.MW: $code === strtolower( $code )
			&&	Xol_lang_itm_.Exists(ByteAry_.new_ascii_(lang_code))
			)
			exists = true;
		return rslt.Init_obj(exists);
	}
	public boolean IsValidCode(Scrib_proc_args args, Scrib_proc_rslt rslt) {	// REF.MW: Language.php!isValidCode
		byte[] lang_code = args.Pull_bry(0);
		boolean valid = Xoa_ttl.parse_(core.Wiki(), lang_code) != null;	// NOTE: MW calls Title::getTitleInvalidRegex()
		if (valid) {
			int len = lang_code.length;
			for (int i = 0; i < len; i++) {
				byte b = lang_code[i];
				switch (b) {	// NOTE: snippet from MW follows; also \000 assumed to be Nil --> :/\\\000&<>'\" 
					case Byte_ascii.Colon: case Byte_ascii.Slash: case Byte_ascii.Backslash: case Byte_ascii.Nil: case Byte_ascii.Amp: case Byte_ascii.Lt: case Byte_ascii.Gt: case Byte_ascii.Apos: case Byte_ascii.Quote:
						valid = false;
						i = len;
						break;
				}
			}
		}
		return rslt.Init_obj(valid);
	}
	public boolean IsValidBuiltInCode(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte[] lang_code = args.Pull_bry(0);
		int len = lang_code.length;
		boolean valid = true;
		for (int i = 0; i < len; i++) {	// REF.MW: '/^[a-z0-9-]+$/i'
			byte b = lang_code[i];
			if (b == Byte_ascii.Dash) {}
			else {
				byte tid = Xol_lang_.Char_tid(b);
				switch (tid) {
					case Xol_lang_.Char_tid_ltr_l: case Xol_lang_.Char_tid_ltr_u: case Xol_lang_.Char_tid_num:
						break;
					default:
						valid = false;
						i = len;
						break;
				}
			}
		}
		return rslt.Init_obj(valid);
	}
	public boolean FetchLanguageName(Scrib_proc_args args, Scrib_proc_rslt rslt) {	
		byte[] lang_code = args.Pull_bry(0);
		// byte[] trans_code = args.Get_bry_or_null(1);	// TODO: FetchLanguageName("en", "fr") -> Anglais; WHEN: needs global database of languages;
		Xol_lang_itm lang_itm = Xol_lang_itm_.Get_by_key(lang_code);
		return rslt.Init_obj(lang_itm == null ? String_.Empty : String_.new_utf8_(lang_itm.Canonical_name()));
	}
	public boolean Lcfirst(Scrib_proc_args args, Scrib_proc_rslt rslt) {return Case_1st(args, rslt, Bool_.N);}
	public boolean Ucfirst(Scrib_proc_args args, Scrib_proc_rslt rslt) {return Case_1st(args, rslt, Bool_.Y);}
	private boolean Case_1st(Scrib_proc_args args, Scrib_proc_rslt rslt, boolean upper) {
		Xol_lang lang = lang_(args);
		byte[] word = args.Pull_bry(1);
		ByteAryBfr bfr = core.Wiki().App().Utl_bry_bfr_mkr().Get_b128().Mkr_rls();
		return rslt.Init_obj(lang.Case_mgr().Case_build_1st(bfr, upper, word, 0, word.length));
	}
	public boolean Lc(Scrib_proc_args args, Scrib_proc_rslt rslt) {return Case_all(args, rslt, Bool_.N);}
	public boolean Uc(Scrib_proc_args args, Scrib_proc_rslt rslt) {return Case_all(args, rslt, Bool_.Y);}
	private boolean Case_all(Scrib_proc_args args, Scrib_proc_rslt rslt, boolean upper) {
		Xol_lang lang = lang_(args);
		byte[] word = args.Pull_bry(1);
		return rslt.Init_obj(lang.Case_mgr().Case_build(upper, word, 0, word.length));
	}
	public boolean CaseFold(Scrib_proc_args args, Scrib_proc_rslt rslt) {return Uc(args, rslt);}	// REF.MW:Language.php!caseFold; http://www.w3.org/International/wiki/Case_folding
	public boolean FormatNum(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xol_lang lang = lang_(args);
		byte[] num = args.Form_bry_or_null(1);
		// boolean commafy = args.Get_bool_from_tableor_fail(1); // TODO: WHEN: when encountered
		byte[] rv = Pf_str_formatnum.Format_num(lang, num, ByteAry_.Empty);
		return rslt.Init_obj(rv);
	}
	public boolean FormatDate(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xol_lang lang = lang_(args);
		byte[] fmt_bry = args.Pull_bry(1);
		byte[] date_bry = args.Cast_bry_or_empty(2);	// NOTE: optional empty is required b/c date is sometimes null; use ByteAry_.Empty b/c this is what Pf_xtn_time.ParseDate takes; DATE:2013-04-05
		boolean utc = args.Cast_bool_or_n(3);
		ByteAryBfr tmp_bfr = core.App().Utl_bry_bfr_mkr().Get_b512();
		DateAdpFormatItm[] fmt_ary = Pf_xtn_time.Parse(core.Wiki().Ctx(), fmt_bry);
		DateAdp date = Pf_xtn_time.ParseDate(date_bry, utc, tmp_bfr);	// NOTE: MW is actually more strict about date; however, not sure about PHP's date parse, so using more lax version
		if (date == null) {tmp_bfr.Mkr_rls(); return rslt.Init_obj(date_bry);}		// date parse failed; return raw_date
		Pf_str_formatdate.Date_bldr().Format(core.Wiki(), lang, fmt_ary, date, tmp_bfr);
		byte[] rv = tmp_bfr.Mkr_rls().XtoAryAndClear();
		return rslt.Init_obj(rv);
	}
	public boolean ParseFormattedNumber(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xol_lang lang = lang_(args);
		byte[] num = args.Form_bry_or_null(1);
		if (num == null) return rslt.Init_null(); // ParseFormattedNumber can sometimes take 1 arg ({'en'}), or null arg ({'en', null}); return null (not ""); DATE:2014-01-07
		byte[] rv = lang.Num_fmt_mgr().Raw(gplx.intl.Gfo_num_fmt_mgr.Tid_raw, num);
		return rslt.Init_obj(rv);
	}
	public boolean formatDuration(Scrib_proc_args args, Scrib_proc_rslt rslt) {throw Err_.not_implemented_();}
	public boolean getDurationIntervals(Scrib_proc_args args, Scrib_proc_rslt rslt) {throw Err_.not_implemented_();}
	public boolean ConvertPlural(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xol_lang lang = lang_(args);
		int count = args.Pull_int(1);
		byte[][] words = args.Cast_params_as_bry_ary_or_empty(2);
		byte[] rv = lang.Plural().Plural_eval(lang, count, words);
		return rslt.Init_obj(rv);
	}
	public boolean ConvertGrammar(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xol_lang lang = lang_(args);
		byte[] word = args.Pull_bry(1);
		byte[] type = args.Pull_bry(2);
		ByteAryBfr bfr = core.Wiki().Utl_bry_bfr_mkr().Get_b512();
		lang.Grammar().Grammar_eval(bfr, lang, word, type);
		return rslt.Init_obj(bfr.Mkr_rls().XtoStrAndClear());
	}
	public boolean gender(Scrib_proc_args args, Scrib_proc_rslt rslt) {throw Err_.not_implemented_();}
	public boolean IsRTL(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xol_lang lang = lang_(args);
		return rslt.Init_obj(!lang.Dir_ltr());
	}
	private Xol_lang lang_(Scrib_proc_args args) {
		byte[] lang_code = args.Cast_bry_or_null(0);
		Xol_lang lang = lang_code == null ? null : core.App().Lang_mgr().Get_by_key_or_load(lang_code);
		if (lang == null) throw Err_.new_fmt_("lang_code is not valid: {0}", String_.new_utf8_(lang_code));
		return lang;
	}
}

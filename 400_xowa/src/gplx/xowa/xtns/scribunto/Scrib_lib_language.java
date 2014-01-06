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
class Scrib_lib_language implements Scrib_lib {
	public Scrib_lib_language(Scrib_engine engine) {this.engine = engine;} Scrib_engine engine;
	public Scrib_mod Mod() {return mod;} Scrib_mod mod;
	public Scrib_mod Register(Scrib_engine engine, Io_url script_dir) {
		mod = engine.RegisterInterface(script_dir.GenSubFil("mw.language.lua"), this, String_.Ary
			( Invk_getContLangCode, Invk_isSupportedLanguage, Invk_isKnownLanguageTag, Invk_isValidCode, Invk_isValidBuiltInCode, Invk_fetchLanguageName
			, Invk_lcfirst, Invk_ucfirst, Invk_lc, Invk_uc, Invk_caseFold, Invk_formatNum, Invk_formatDate, Invk_parseFormattedNumber, Invk_convertPlural, Invk_convertGrammar, Invk_gender, Invk_isRTL)
			);
		notify_lang_changed_fnc = mod.Fncs_get_by_key("notify_lang_changed");
		return mod;
	}	private Scrib_fnc notify_lang_changed_fnc;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_getContLangCode))			return GetContLangCode((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_isSupportedLanguage))		return IsSupportedLanguage((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_isKnownLanguageTag))			return IsKnownLanguageTag((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_isValidCode))				return IsValidCode((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_isValidBuiltInCode))			return IsValidBuiltInCode((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_fetchLanguageName))			return FetchLanguageName((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_lcfirst))					return Lcfirst((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_ucfirst))					return Ucfirst((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_lc))							return Lc((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_uc))							return Uc((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_caseFold))					return CaseFold((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_formatDuration))				return formatDuration((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_getDurationIntervals))		return getDurationIntervals((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_formatNum))					return FormatNum((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_formatDate))					return FormatDate((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_parseFormattedNumber))		return ParseFormattedNumber((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_convertPlural))				return ConvertPlural((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_convertGrammar))				return ConvertGrammar((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_gender))						return gender((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_isRTL))						return IsRTL((KeyVal[])m.CastObj("v"));
		else	return GfoInvkAble_.Rv_unhandled;
	}
	public static final String 
		  Invk_getContLangCode = "getContLangCode", Invk_isSupportedLanguage = "isSupportedLanguage", Invk_isKnownLanguageTag = "isKnownLanguageTag"
		, Invk_isValidCode = "isValidCode", Invk_isValidBuiltInCode = "isValidBuiltInCode", Invk_fetchLanguageName = "fetchLanguageName"
		, Invk_lcfirst = "lcfirst", Invk_ucfirst = "ucfirst", Invk_lc = "lc", Invk_uc = "uc", Invk_caseFold = "caseFold"
		, Invk_formatNum = "formatNum", Invk_formatDate = "formatDate", Invk_formatDuration = "formatDuration", Invk_getDurationIntervals = "getDurationIntervals", Invk_parseFormattedNumber = "parseFormattedNumber"
		, Invk_convertPlural = "convertPlural", Invk_convertGrammar = "convertGrammar", Invk_gender = "gender", Invk_isRTL = "isRTL"
		;
	public void Notify_lang_changed() {if (notify_lang_changed_fnc != null) engine.Interpreter().CallFunction(notify_lang_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public KeyVal[] GetContLangCode(KeyVal[] values) {return Scrib_kv_utl.base1_obj_(engine.Ctx().Lang().Key_str());}
	public KeyVal[] IsSupportedLanguage(KeyVal[] values) {return IsKnownLanguageTag(values);}	// NOTE: checks if "MessagesXX.php" exists; note that xowa has all "MessagesXX.php"; for now, assume same functionality as IsKnownLanguageTag (worst case is that a small wiki depends on a lang not being there; will need to put in a "wiki.Langs()" then)
	public KeyVal[] IsKnownLanguageTag(KeyVal[] values) {	// NOTE: checks if in languages/Names.php
		String lang_code = Scrib_kv_utl.Val_to_str(values, 0);
		boolean exists = false;
		if (String_.Eq(lang_code, String_.Lower(lang_code)))	// must be lower-case; REF.MW: $code === strtolower( $code )
			exists = Xol_lang_itm_.Exists(ByteAry_.new_ascii_(lang_code));
		return Scrib_kv_utl.base1_obj_(exists);
	}
	public KeyVal[] IsValidCode(KeyVal[] values) {	// REF.MW: Language.php!isValidCode
		byte[] lang_code = Scrib_kv_utl.Val_to_bry(values, 0);
		boolean valid = Xoa_ttl.parse_(engine.Wiki(), lang_code) != null;	// NOTE: MW calls Title::getTitleInvalidRegex()
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
		return Scrib_kv_utl.base1_obj_(valid);
	}
	public KeyVal[] IsValidBuiltInCode(KeyVal[] values) {
		byte[] lang_code = Scrib_kv_utl.Val_to_bry(values, 0);
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
		return Scrib_kv_utl.base1_obj_(valid);
	}
	public KeyVal[] FetchLanguageName(KeyVal[] values) {	
		byte[] lang_code = Scrib_kv_utl.Val_to_bry(values, 0);
		// byte[] trans_code = Scrib_kv_utl.Val_to_bry_or(values, 1, null);	// FUTURE.7: FetchLanguageName("en", "fr") -> Anglais; WHEN: needs global database of languages;
		Xol_lang_itm lang_itm = Xol_lang_itm_.Get_by_key(lang_code);
		return Scrib_kv_utl.base1_obj_(lang_itm == null ? String_.Empty : String_.new_utf8_(lang_itm.Canonical_name()));
	}
	public KeyVal[] Lcfirst(KeyVal[] values) {
		Xol_lang lang = lang_(values);
		byte[] word = Scrib_kv_utl.Val_to_bry(values, 1);
		ByteAryBfr bfr = engine.Wiki().App().Utl_bry_bfr_mkr().Get_b128().Mkr_rls();
		return Scrib_kv_utl.base1_obj_(lang.Case_mgr().Case_build_1st_lower(bfr, word, 0, word.length));
	}
	public KeyVal[] Ucfirst(KeyVal[] values) {
		Xol_lang lang = lang_(values);
		byte[] word = Scrib_kv_utl.Val_to_bry(values, 1);
		ByteAryBfr bfr = engine.Wiki().App().Utl_bry_bfr_mkr().Get_b128().Mkr_rls();
		return Scrib_kv_utl.base1_obj_(lang.Case_mgr().Case_build_1st_upper(bfr, word, 0, word.length));
	}
	public KeyVal[] Lc(KeyVal[] values) {
		Xol_lang lang = lang_(values);
		byte[] word = Scrib_kv_utl.Val_to_bry(values, 1);
		return Scrib_kv_utl.base1_obj_(lang.Case_mgr().Case_build_lower(word, 0, word.length));
	}
	public KeyVal[] Uc(KeyVal[] values) {
		Xol_lang lang = lang_(values);
		byte[] word = Scrib_kv_utl.Val_to_bry(values, 1);
		return Scrib_kv_utl.base1_obj_(lang.Case_mgr().Case_build_upper(word, 0, word.length));
	}
	public KeyVal[] CaseFold(KeyVal[] values) {return Uc(values);}	// REF.MW:Language.php!caseFold; http://www.w3.org/International/wiki/Case_folding
	public KeyVal[] FormatNum(KeyVal[] values) {
		Xol_lang lang = lang_(values);
		Object num_obj = Scrib_kv_utl.Val_to_obj(values, 1);
		byte[] num_bry = ByteAry_.new_utf8_(Object_.XtoStr_OrEmpty(num_obj));
		byte[] rv = Pf_str_formatnum.Format_num(lang, num_bry, ByteAry_.Empty);
		return Scrib_kv_utl.base1_obj_(rv);
	}
	public KeyVal[] FormatDate(KeyVal[] values) {
		Xol_lang lang = lang_(values);
		byte[] fmt_bry = null;
		byte[] date_bry = ByteAry_.Empty;
		boolean utc = false;
		int values_len = values.length;
		for (int i = 1; i < values_len; i++) {
			KeyVal kv = values[i];
			int kv_key = Int_.cast_(kv.Key_as_obj());
			Object kv_val = kv.Val();
			switch (kv_key) {
				case 2: fmt_bry = ByteAry_.new_utf8_((String)kv_val); break;
				case 3: date_bry = ByteAry_.new_utf8_((String)kv_val); break;
				case 4: utc = Bool_.cast_(kv_val);  break;
				default: throw Err_.unhandled(kv_key);
			}
		}
//			byte[] date_bry = Scrib_kv_utl.Val_to_bry_or(values, 2, ByteAry_.Empty);	// NOTE: must supply or (date sometimes null); use ByteAry_.Empty b/c this is what Pf_xtn_time.ParseDate takes; DATE:2013-04-05
//			boolean utc = Scrib_kv_utl.Val_to_bool_or(values, 3, false);
		ByteAryBfr tmp_bfr = engine.App().Utl_bry_bfr_mkr().Get_b512();
		DateAdpFormatItm[] fmt_ary = Pf_xtn_time.Parse(engine.Wiki().Ctx(), fmt_bry);
		DateAdp date = Pf_xtn_time.ParseDate(date_bry, utc, tmp_bfr);	// NOTE: MW is actually more strict about date; however, not sure about PHP's date parse, so using more lax version
		if (date == null) {tmp_bfr.Mkr_rls(); return Scrib_kv_utl.base1_obj_(date_bry);}		// date parse failed; return raw_date
		Pf_str_formatdate.Date_bldr().Format(engine.Wiki(), lang, fmt_ary, date, tmp_bfr);
		byte[] rv = tmp_bfr.Mkr_rls().XtoAryAndClear();
		return Scrib_kv_utl.base1_obj_(rv);
	}
	public KeyVal[] ParseFormattedNumber(KeyVal[] values) {
		Xol_lang lang = lang_(values);
		byte[] num = Scrib_kv_utl.Val_to_bry(values, 1);
		byte[] rv = lang.Num_fmt_mgr().Raw(gplx.intl.Gfo_num_fmt_mgr.Tid_raw, num);
		return Scrib_kv_utl.base1_obj_(rv);
	}
	public KeyVal[] formatDuration(KeyVal[] values) {throw Err_.not_implemented_();}
	public KeyVal[] getDurationIntervals(KeyVal[] values) {throw Err_.not_implemented_();}
	public KeyVal[] ConvertPlural(KeyVal[] values) {
		Xol_lang lang = lang_(values);
		int count = Scrib_kv_utl.Val_to_int(values, 1);
		int values_len = values.length;
		byte[][] words = new byte[values_len - 2][];
		for (int i = 2; i < values_len; i++) {
			words[i - 2] = Scrib_kv_utl.Val_to_bry(values, i);
		}
		byte[] rv = lang.Plural().Plural_eval(lang, count, words);
		return Scrib_kv_utl.base1_obj_(rv);
	}
	public KeyVal[] ConvertGrammar(KeyVal[] values) {
		Xol_lang lang = lang_(values);
		byte[] word = Scrib_kv_utl.Val_to_bry(values, 1);
		byte[] type = Scrib_kv_utl.Val_to_bry(values, 2);
		ByteAryBfr bfr = engine.Wiki().Utl_bry_bfr_mkr().Get_b512();
		lang.Grammar().Grammar_eval(bfr, lang, word, type);
		return Scrib_kv_utl.base1_obj_(bfr.Mkr_rls().XtoStrAndClear());
	}
	public KeyVal[] gender(KeyVal[] values) {throw Err_.not_implemented_();}
	public KeyVal[] IsRTL(KeyVal[] values) {
		Xol_lang lang = lang_(values);
		return Scrib_kv_utl.base1_obj_(!lang.Dir_ltr());
	}
	Xol_lang lang_(KeyVal[] values) {
		byte[] lang_code = Scrib_kv_utl.Val_to_bry(values, 0);
		Xol_lang lang = engine.App().Lang_mgr().Get_by_key_or_load(lang_code);
		if (lang == null) throw Err_.new_fmt_("lang_code is not valid: {0}", String_.new_utf8_(lang_code));
		return lang;
	}
}

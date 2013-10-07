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
import gplx.intl.*; import gplx.php.*;
public class Xol_mw_lang_parser {
	Php_parser parser = new Php_parser(); Php_evaluator evaluator;
	public Xol_mw_lang_parser(Gfo_msg_log msg_log) {evaluator = new Php_evaluator(msg_log);}
	public void Bld_all(Xoa_app app, Io_url user_root) {Bld_all(app, user_root, Xol_lang_transform_null._);}
	public static final String Dir_name_xowa = Xoa_app_.App_name;
	public void Bld_all(Xoa_app app, Io_url user_root, Xol_lang_transform lang_transform) {
		Io_url lang_root = user_root.GenSubDir("lang");
		Parse_mediawiki(app, lang_root.GenSubDir("mediawiki"), lang_transform);
		Save_langs(app, lang_root.GenSubDir(Xol_mw_lang_parser.Dir_name_xowa), OrderedHash_.new_bry_());
	}
	public void Save_langs(Xoa_app app, Io_url xowa_root, OrderedHash manual_texts) {
		Xoa_lang_mgr lang_mgr = app.Lang_mgr();
		int len = lang_mgr.Len();
		Gfs_bldr bldr = new Gfs_bldr();
		for (int i = 0; i < len; i++) {
			Xol_lang lang = lang_mgr.Get_at(i);
			String lang_key = lang.Key_str();
			Io_url lang_url = xowa_root.GenSubFil(lang_key + ".gfs");
			Xol_lang_srl.Save_num_fmt(bldr, lang.Num_fmt_mgr());
			bldr.Add_proc_init_many("this").Add_nl();
			if (lang.Fallback_bry() != null)	// NOTE: fallback will often be null; EX: en
				bldr.Add_proc_cont_one(Xol_lang.Invk_fallback_load).Add_parens_str(lang.Fallback_bry()).Add_nl();
			if (!lang.Dir_ltr())				// NOTE: only save dir_ltr if false; EX: en
				bldr.Add_proc_cont_one(Xol_lang.Invk_dir_rtl_).Add_parens_str(Yn.XtoStr(!lang.Dir_ltr())).Add_nl();
			Xol_lang_srl.Save_ns_grps(bldr, lang.Ns_names(), Xol_lang.Invk_ns_names);
			Xol_lang_srl.Save_ns_grps(bldr, lang.Ns_aliases(), Xol_lang.Invk_ns_aliases);
			Xol_lang_srl.Save_specials(bldr, lang.Specials_mgr());
			Xol_lang_srl.Save_keywords(bldr, lang.Kwd_mgr());
			Xol_lang_srl.Save_messages(bldr, lang.Msg_mgr(), true);
			bldr.Add_term_nl();

			byte[][] itm = (byte[][])manual_texts.Fetch(ByteAry_.new_utf8_(lang_key));
			if (itm != null) bldr.Bfr().Add(itm[1]);
			Io_mgr._.SaveFilBfr(lang_url, bldr.Bfr());
		}
	}
	public void Parse_mediawiki(Xoa_app app, Io_url mediawiki_root, Xol_lang_transform lang_transform) {
		ByteAryBfr bfr = ByteAryBfr.new_();
		Parse_file_messages(app, mediawiki_root, bfr, lang_transform);
		Parse_file_extensions(app, mediawiki_root, bfr, lang_transform);
	}
	private void Parse_file_messages(Xoa_app app, Io_url mediawiki_root, ByteAryBfr bfr, Xol_lang_transform lang_transform) {
		Io_url dir = mediawiki_root.GenSubDir("messages");
		Io_url[] urls = Io_mgr._.QueryDir_fils(dir);
		int len = urls.length;
		for (int i = 0; i < len; i++) {
			Io_url url = urls[i];
			try {
			String lang_key = String_.Replace(String_.Lower(String_.Mid(url.NameOnly(), 8)), "_", "-");	// 8=Messages.length; lower b/c format is MessagesEn.php (need "en")
			if (String_.In(lang_key, "qqq", "enrtl", "bbc", "bbc-latn")) continue;
			String text = Io_mgr._.LoadFilStr(url);
			Xol_lang lang = app.Lang_mgr().Get_by_key_or_new(ByteAry_.new_utf8_(lang_key));
			this.Parse_core(text, lang, bfr, lang_transform);
			} catch (Exception exc) {Err_.Noop(exc); Tfds.WriteText("failed to parse " + url.Raw() + Err_.Message_gplx_brief(exc) + "\n");}
		}
	}
	private void Parse_file_extensions(Xoa_app app, Io_url mediawiki_root, ByteAryBfr bfr, Xol_lang_transform lang_transform) {
		Io_url dir = mediawiki_root.GenSubDir("extensions");
		Io_url[] urls = Io_mgr._.QueryDir_fils(dir);
		int len = urls.length;
		for (int i = 0; i < len; i++) {
			Io_url url = urls[i];
			try {
			String text = Io_mgr._.LoadFilStr(url);
			boolean prepend_hash = String_.Eq("ParserFunctions.i18n.magic", url.NameOnly());
			this.Parse_xtn(text, url, app, bfr, prepend_hash, lang_transform);
			} catch (Exception exc) {Err_.Noop(exc); Tfds.WriteText("failed to parse " + url.Raw() + Err_.Message_gplx_brief(exc));}
		}
	}
	public void Parse_core(String text, Xol_lang lang, ByteAryBfr bfr, Xol_lang_transform lang_transform) {
		evaluator.Clear();
		parser.Parse_tkns(text, evaluator);
		Php_line[] lines = (Php_line[])evaluator.List().XtoAry(Php_line.class);
		int lines_len = lines.length;
		ListAdp bry_list = ListAdp_.new_();
		for (int i = 0; i < lines_len; i++) {
			Php_line_assign line = (Php_line_assign)lines[i];
			byte[] key = line.Key().Val_obj_bry();
			Object o = Tid_hash.Get_by_bry(key);
			if (o != null) {
				ByteVal stub = (ByteVal)o;
				switch (stub.Val()) {
					case Tid_namespaceNames:
						Parse_array_kv(bry_list, line);
						lang.Ns_names().Ary_set_(Parse_namespace_strings(bry_list, true));
						break;
					case Tid_namespaceAliases:
						Parse_array_kv(bry_list, line);
						lang.Ns_aliases().Ary_set_(Parse_namespace_strings(bry_list, false));
						break;
					case Tid_messages:		
						Parse_array_kv(bry_list, line);
						Parse_messages(bry_list, lang, bfr);
						break;
					case Tid_magicwords:
						Parse_magicwords(line, lang.Key_bry(), lang.Kwd_mgr(), false, lang_transform);
						break;
					case Tid_specialPageAliases:
						Parse_specials(line, lang.Key_bry(), lang.Specials_mgr());
						break;
					case Tid_dateFormats:
//							Parse_array_kv(line);
						break;
					case Tid_fallback:
						byte[] fallback_bry = Parse_bry(line.Val());
						if (!ByteAry_.Eq(fallback_bry, Bool_.True_bry))	// MessagesEn.php has fallback = false; ignore
							lang.Fallback_bry_(fallback_bry);
						break;
					case Tid_separatorTransformTable:
						Parse_separatorTransformTable(line, lang.Num_fmt_mgr());
						break;
					case Tid_rtl:
						byte[] rtl_bry = Parse_bry(line.Val());
						boolean dir_rtl = ByteAry_.Eq(rtl_bry, Bool_.True_bry);
						lang.Dir_ltr_(!dir_rtl);
						break;
				}
			}
		}
		lang.Evt_lang_changed();
	}	
	public void Parse_xtn(String text, Io_url url, Xoa_app app, ByteAryBfr bfr, boolean prepend_hash, Xol_lang_transform lang_transform) {
		evaluator.Clear();
		parser.Parse_tkns(text, evaluator);
		ListAdp bry_list = ListAdp_.new_();
		Php_line[] lines = (Php_line[])evaluator.List().XtoAry(Php_line.class);
		int lines_len = lines.length;
		for (int i = 0; i < lines_len; i++) {
			Php_line_assign line = (Php_line_assign)lines[i];
			byte[] key = line.Key().Val_obj_bry();
			Object o = Tid_hash.Get_by_bry(key);
			if (o != null) {
				Php_key[] subs = line.Key_subs();
				if (subs.length == 0) continue;	// ignore if no lang_key; EX: ['en']
				byte[] lang_key = subs[0].Val_obj_bry();
				try {
				if (String_.In(String_.new_utf8_(lang_key), "qqq", "enrtl", "akz", "sxu", "test", "mwv", "rup", "hu-formal", "tzm", "bbc", "bbc-latn")) continue;
				Xol_lang lang = app.Lang_mgr().Get_by_key_or_new(lang_key);
				ByteVal stub = (ByteVal)o;
				switch (stub.Val()) {
					case Tid_messages:		
						Parse_array_kv(bry_list, line);
						Parse_messages(bry_list, lang, bfr);
						break;
					case Tid_magicwords:
						if (line.Key_subs().length == 0) continue;	// ignore lines like $magicWords = array();
						if (line.Key_subs().length > 1) throw Err_mgr._.fmt_(GRP_KEY, "parse_xtn", "magicWords in xtn must have only 1 accessor", line.Key_subs().length);
						Php_key accessor = line.Key_subs()[0];
						byte[] accessor_bry = accessor.Val_obj_bry();
						if (ByteAry_.Eq(accessor_bry, lang_key))	// accessor must match lang_key
							Parse_magicwords(line, lang.Key_bry(), lang.Kwd_mgr(), prepend_hash, lang_transform); 
						break;
				}
			} catch (Exception exc) {Err_.Noop(exc); Tfds.WriteText("failed to parse " + url.Raw() + Err_.Message_gplx_brief(exc) + "\n");}
			}
//			else {
//				Tfds.Write_ary(key);
//			}
		}
//		if (trie_rebuild) wiki.Ns_mgr().Trie_rebuild();
//		lang.Evt_lang_changed();
	}
	private void Parse_array_kv(ListAdp rv, Php_line_assign line) {
		rv.Clear();
		Php_itm_ary ary = (Php_itm_ary)line.Val();
		int subs_len = ary.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Php_itm_kv kv = (Php_itm_kv)ary.Subs_get(i);
			rv.Add(Parse_bry(kv.Key()));
			rv.Add(Parse_bry(kv.Val()));
		}		
	}
	public Xow_ns[] Parse_namespace_strings(ListAdp list, boolean ns_names) {
		byte[][] brys = (byte[][])list.XtoAry(byte[].class);
		int brys_len = brys.length;
		Xow_ns[] rv = new Xow_ns[brys_len / 2];
		int key_dif = ns_names ? 0 : 1;
		int val_dif = ns_names ? 1 : 0;
		for (int i = 0; i < brys_len; i+=2) {
			byte[] kv_key = brys[i + key_dif];
			byte[] kv_val = brys[i + val_dif];
			ByteAry_.Replace_all_direct(kv_val, Byte_ascii.Underline, Byte_ascii.Space); // NOTE: siteInfo.xml names have " " not "_" (EX: "User talk"). for now, follow that convention
			int ns_id = Id_by_mw_name(kv_key);
//				if (ns_id == Xow_ns_.Id_null) throw Err_mgr._.fmt_auto_(GRP_KEY, "namespace_names", String_.new_utf8_(kv_key));
			rv[i / 2] = new Xow_ns(ns_id, Xow_ns_.Case_match_1st, kv_val, false);	// note that Xow_ns is being used as glorified id-name struct; case_match and alias values do not matter
		}
		return rv;
	}
	private void Parse_messages(ListAdp rv, Xol_lang lang, ByteAryBfr bfr) {
		byte[][] brys = (byte[][])rv.XtoAry(byte[].class);
		int brys_len = brys.length;
		Xol_msg_mgr mgr = lang.Msg_mgr();
		ListAdp quote_itm_list = ListAdp_.new_(); ByteRef quote_parse_result = ByteRef.zero_(); 
		for (int i = 0; i < brys_len; i+=2) {
			byte[] kv_key = brys[i];
			Xol_msg_itm itm = mgr.Itm_by_key_or_new(kv_key);
			if (itm == null) continue;
			byte[] kv_val = brys[i + 1];
			kv_val = php_quote_parser.Parse_as_bry(quote_itm_list, kv_val, quote_parse_result, bfr);
			Xol_msg_itm_.update_val_(itm, kv_val);
			itm.Dirty_(true);
		}
	}	Php_text_itm_parser php_quote_parser = new Php_text_itm_parser();
	private void Parse_magicwords(Php_line_assign line, byte[] lang_key, Xol_kwd_mgr mgr, boolean prepend_hash, Xol_lang_transform lang_transform) {
		Php_itm_ary ary = (Php_itm_ary)line.Val();
		int subs_len = ary.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Php_itm_sub sub = ary.Subs_get(i);
			Php_itm_kv kv = (Php_itm_kv)sub;
			byte[] kv_key = kv.Key().Val_obj_bry();
			Php_itm_ary kv_ary = (Php_itm_ary)kv.Val();
			int kv_ary_len = kv_ary.Subs_len();
			if (kv_ary_len < 2) throw Err_mgr._.fmt_(Err_scope_keywords, "invalid_ary", "array must have 2 or more items", String_.new_utf8_(kv_key));
			boolean case_match = Parse_int_as_bool(kv_ary.Subs_get(0));
			int words_len = kv_ary_len - 1;
			byte[][] words = new byte[words_len][];
			for (int j = 1; j < kv_ary_len; j++) {
				Php_itm_sub kv_sub = kv_ary.Subs_get(j);
				byte[] word = Parse_bry(kv_sub);
//					if (prepend_hash && word[0] != Byte_ascii.Hash) word = ByteAry_.Add(Byte_ascii.Hash, word);
				words[j - 1] = lang_transform.Kwd_transform(lang_key, kv_key, word);
			}
			int keyword_id = Xol_kwd_grp_.Id_by_bry(kv_key); if (keyword_id == -1) continue; //throw Err_mgr._.fmt_(Err_scope_keywords, "invalid_key", "key does not have id", String_.new_utf8_(kv_key));
			Xol_kwd_grp grp = mgr.Get_or_new(keyword_id);
			grp.Srl_load(case_match, words);
		}
	}
	private void Parse_specials(Php_line_assign line, byte[] lang_key, Xol_specials_mgr specials_mgr) {
		specials_mgr.Clear(); // NOTE: always clear, else will try to readd to en.gfs
		Php_itm_ary ary = (Php_itm_ary)line.Val();
		int subs_len = ary.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Php_itm_sub sub = ary.Subs_get(i);
			Php_itm_kv kv = (Php_itm_kv)sub;
			byte[] kv_key = kv.Key().Val_obj_bry();
			Php_itm_ary kv_ary = (Php_itm_ary)kv.Val();
			int kv_ary_len = kv_ary.Subs_len();
			byte[][] aliases = new byte[kv_ary_len][];
			for (int j = 0; j < kv_ary_len; j++) {
				Php_itm_sub kv_sub = kv_ary.Subs_get(j);
				aliases[j] = Parse_bry(kv_sub);
			}
			specials_mgr.Add(kv_key, aliases);
		}
	}
	boolean Parse_int_as_bool(Php_itm itm) {
		int rv = -1;
		switch (itm.Itm_tid()) {
			case Php_itm_.Tid_int:
				rv = ((Php_itm_int)itm).Val_obj_int();
				break;
			case Php_itm_.Tid_quote:
				byte[] bry = ((Php_itm_quote)itm).Val_obj_bry();
				rv = ByteAry_.XtoIntOr(bry, -1);
				if (rv == -1) throw Err_mgr._.fmt_(GRP_KEY, "parse_int_as_bool", "value must be 0 or 1", String_.new_utf8_(bry));
				break;
			default:
				throw Err_mgr._.unhandled_(itm.Itm_tid());
		}
		return rv == 1;
	}
	byte[] Parse_bry(Php_itm itm) {
		switch (itm.Itm_tid()) {
			case Php_itm_.Tid_kv:
			case Php_itm_.Tid_ary:
				throw Err_mgr._.unhandled_(itm.Itm_tid());
			default:
				return itm.Val_obj_bry();
		}
	}
	private void Parse_separatorTransformTable(Php_line_assign line, Gfo_num_fmt_mgr num_fmt_mgr) {
		if (line.Val().Itm_tid() == Php_itm_.Tid_null) return;// en is null; $separatorTransformTable = null;
		Php_itm_ary ary = (Php_itm_ary)line.Val();
		int subs_len = ary.Subs_len();
		byte[] dec_dlm = Bry_separatorTransformTable_dot;
		byte[] grp_dlm = Bry_separatorTransformTable_comma;
		ListAdp tmp_list = ListAdp_.new_(); ByteRef tmp_result = ByteRef.zero_(); ByteAryBfr tmp_bfr = ByteAryBfr.reset_(16); 
		for (int i = 0; i < subs_len; i++) {
			Php_itm_kv kv = (Php_itm_kv)ary.Subs_get(i);
			byte[] key_bry = Parse_bry(kv.Key()), val_bry = Parse_bry(kv.Val());
			val_bry = php_quote_parser.Parse_as_bry(tmp_list, val_bry, tmp_result, tmp_bfr);
			Xol_csv_parser._.Load(tmp_bfr, val_bry);
			val_bry = tmp_bfr.XtoAryAndClear();
			if 		(ByteAry_.Eq(key_bry, Bry_separatorTransformTable_dot)) 	dec_dlm = val_bry;
			else if (ByteAry_.Eq(key_bry, Bry_separatorTransformTable_comma)) 	grp_dlm = val_bry;
			else throw Err_mgr._.unhandled_(String_.new_utf8_(key_bry));
		}
		num_fmt_mgr.Clear().Dec_dlm_(dec_dlm).Grps_add(new Gfo_num_fmt_grp(grp_dlm, 3, true));
	}	static final byte[] Bry_separatorTransformTable_comma = new byte[] {Byte_ascii.Comma}, Bry_separatorTransformTable_dot = new byte[] {Byte_ascii.Dot};
	private static final String GRP_KEY = "xowa.lang.parser"; private static final String Err_scope_keywords = GRP_KEY + ".keywords";
	private static final byte Tid_messages = 0, Tid_magicwords = 1, Tid_namespaceNames = 2, Tid_namespaceAliases = 3, Tid_specialPageAliases = 4
		, Tid_linkTrail = 5, Tid_dateFormats = 6, Tid_fallback = 7, Tid_separatorTransformTable = 8, Tid_rtl = 9;
	private static Hash_adp_bry Tid_hash = new Hash_adp_bry(true)
		.Add_str_byteVal("namespaceNames", Tid_namespaceNames).Add_str_byteVal("namespaceAliases", Tid_namespaceAliases).Add_str_byteVal("messages", Tid_messages)
		.Add_str_byteVal("magicWords", Tid_magicwords).Add_str_byteVal("specialPageAliases", Tid_specialPageAliases)
		.Add_str_byteVal("linkTrail", Tid_linkTrail).Add_str_byteVal("dateFormats", Tid_dateFormats).Add_str_byteVal("fallback", Tid_fallback)
		.Add_str_byteVal("separatorTransformTable", Tid_separatorTransformTable).Add_str_byteVal("rtl", Tid_rtl);
	public static int Id_by_mw_name(byte[] src) {
		if (mw_names == null) {
			mw_names = ByteTrieMgr_slim.cs_();
			mw_names.Add("NS_MEDIA", IntVal.new_(Xow_ns_.Id_media));
			mw_names.Add("NS_SPECIAL", IntVal.new_(Xow_ns_.Id_special));
			mw_names.Add("NS_MAIN", IntVal.new_(Xow_ns_.Id_main));
			mw_names.Add("NS_TALK", IntVal.new_(Xow_ns_.Id_talk));
			mw_names.Add("NS_USER", IntVal.new_(Xow_ns_.Id_user));
			mw_names.Add("NS_USER_TALK", IntVal.new_(Xow_ns_.Id_user_talk));
			mw_names.Add("NS_PROJECT", IntVal.new_(Xow_ns_.Id_project));
			mw_names.Add("NS_PROJECT_TALK", IntVal.new_(Xow_ns_.Id_project_talk));
			mw_names.Add("NS_FILE", IntVal.new_(Xow_ns_.Id_file));
			mw_names.Add("NS_FILE_TALK", IntVal.new_(Xow_ns_.Id_file_talk));
			mw_names.Add("NS_MEDIAWIKI", IntVal.new_(Xow_ns_.Id_mediaWiki));
			mw_names.Add("NS_MEDIAWIKI_TALK", IntVal.new_(Xow_ns_.Id_mediaWiki_talk));
			mw_names.Add("NS_TEMPLATE", IntVal.new_(Xow_ns_.Id_template));
			mw_names.Add("NS_TEMPLATE_TALK", IntVal.new_(Xow_ns_.Id_template_talk));
			mw_names.Add("NS_HELP", IntVal.new_(Xow_ns_.Id_help));
			mw_names.Add("NS_HELP_TALK", IntVal.new_(Xow_ns_.Id_help_talk));
			mw_names.Add("NS_CATEGORY", IntVal.new_(Xow_ns_.Id_category));
			mw_names.Add("NS_CATEGORY_TALK", IntVal.new_(Xow_ns_.Id_category_talk));
		}
		Object o = mw_names.MatchAtCurExact(src, 0, src.length);
		return o == null ? Xow_ns_.Id_null : ((IntVal)o).Val();
	}	static ByteTrieMgr_slim mw_names;
}

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
import gplx.intl.*; import gplx.xowa.langs.numFormats.*;
public class Xol_lang_srl {
	public static Xow_ns[] Load_ns_grps(byte[] src) {
		int src_len = src.length, pos = 0, fld_bgn = 0;
		int cur_id = -1;
		ListAdp rv = ListAdp_.new_(); Xol_csv_parser csv_parser = Xol_csv_parser._;
		while (true) {
			boolean last = pos == src_len;	// NOTE: logic occurs b/c of \n}~-> dlm which gobbles up last \n
			byte b = last ? Byte_ascii.NewLine : src[pos];
			switch (b) {
				case Byte_ascii.Pipe:
					cur_id = ByteAry_.X_to_int_or(src, fld_bgn, pos, Int_.MinValue);
					if (cur_id == Int_.MinValue) throw Err_mgr._.fmt_(GRP_KEY, "invalid_id", "invalid_id: ~{0}", String_.new_utf8_(src, fld_bgn, pos));					
					fld_bgn = pos + 1;
					break;
				case Byte_ascii.NewLine:
					byte[] cur_name = csv_parser.Load(src, fld_bgn, pos);
					Xow_ns ns = new Xow_ns(cur_id, Xow_ns_case_.Id_1st, cur_name, false);
					rv.Add(ns);
					fld_bgn = pos + 1;
					cur_id = -1;
					break;
				default:
					break;
			}
			if (last) break;
			++pos;
		}
		return (Xow_ns[])rv.XtoAry(Xow_ns.class);
	}	private static final String GRP_KEY = "xowa.lang.srl";
	public static void Load_keywords(Xol_kwd_mgr keyword_mgr, byte[] src) {
		int src_len = src.length, pos = 0, fld_bgn = 0, fld_idx = 0;
		boolean cur_cs = false; byte[] cur_key = ByteAry_.Empty;
		ListAdp cur_words = ListAdp_.new_();
		Xol_csv_parser csv_parser = Xol_csv_parser._;
		while (true) {
			boolean last = pos == src_len;	// NOTE: logic occurs b/c of \n}~-> dlm which gobbles up last \n
			byte b = last ? Byte_ascii.NewLine : src[pos];
			switch (b) {
				case Byte_ascii.Pipe:
					switch (fld_idx) {
						case 0:
							cur_key = csv_parser.Load(src, fld_bgn, pos);
							break;
						case 1:
							byte cs_byte = src[pos - 1]; 
							switch (cs_byte) {
								case Byte_ascii.Num_0: cur_cs = false; break;
								case Byte_ascii.Num_1: cur_cs = true; break;
								default: throw Err_mgr._.fmt_(GRP_KEY, "invalid_cs", "case sensitive should be 0 or 1: ~{0}", Byte_.XtoStr(cs_byte)); 
							}
							break;
					}
					fld_bgn = pos + 1;
					++fld_idx;
					break;
				case Byte_ascii.Tilde:
					byte[] word = csv_parser.Load(src, fld_bgn, pos);
					cur_words.Add(word);
					fld_bgn = pos + 1;
					break;
				case Byte_ascii.NewLine:
					if (cur_words.Count() > 0) {	// guard against blank line wiping out entries; EX: "toc|0|toc1\n\n"; 2nd \n will get last grp and make 0 entries
						int cur_id = Xol_kwd_grp_.Id_by_bry(cur_key); if (cur_id == -1) throw Err_mgr._.fmt_(GRP_KEY, "invalid_keyword", "key does not have id: ~{0}", cur_id);
						Xol_kwd_grp grp = keyword_mgr.Get_or_new(cur_id);
						grp.Srl_load(cur_cs, (byte[][])cur_words.XtoAry(byte[].class));
					}
					fld_bgn = pos + 1;
					fld_idx = 0;
					cur_words.Clear();
					break;
				default:
					break;
			}
			if (last) break;
			++pos;
		}
//			return (Xol_kwd_grp[])rv.XtoAry(typeof(Xol_kwd_grp));
	}
	public static void Load_messages(Xol_msg_mgr msg_mgr, byte[] src) {
		int src_len = src.length, pos = 0, fld_bgn = 0;
		byte[] cur_key = ByteAry_.Empty;
		Xol_csv_parser csv_parser = Xol_csv_parser._;
		while (true) {
			boolean last = pos == src_len;	// NOTE: logic occurs b/c of \n}~-> dlm which gobbles up last \n
			byte b = last ? Byte_ascii.NewLine : src[pos];
			switch (b) {
				case Byte_ascii.Pipe:
					cur_key = csv_parser.Load(src, fld_bgn, pos);
					fld_bgn = pos + 1;
					break;
				case Byte_ascii.NewLine:
					byte[] cur_val = csv_parser.Load(src, fld_bgn, pos);
					Xol_msg_itm itm = msg_mgr.Itm_by_key_or_new(cur_key).Src_(Xol_msg_itm.Src_lang);	// NOTE: this proc should only be called when loading lang.gfs
					Xol_msg_itm_.update_val_(itm, cur_val);
					itm.Dirty_(true);
					fld_bgn = pos + 1;
					break;
				default:
					break;
			}
			if (last) break;
			++pos;
		}
	}
	public static void Load_specials(Xol_specials_mgr special_mgr, byte[] src) {
		int src_len = src.length, pos = 0, fld_bgn = 0;
		byte[] cur_key = ByteAry_.Empty;
		Xol_csv_parser csv_parser = Xol_csv_parser._;
		while (true) {
			boolean last = pos == src_len;	// NOTE: logic occurs b/c of \n}~-> dlm which gobbles up last \n
			byte b = last ? Byte_ascii.NewLine : src[pos];
			switch (b) {
				case Byte_ascii.Pipe:
					cur_key = csv_parser.Load(src, fld_bgn, pos);
					fld_bgn = pos + 1;
					break;
				case Byte_ascii.NewLine:
					byte[] cur_val_raw = csv_parser.Load(src, fld_bgn, pos);
					byte[][] cur_vals = ByteAry_.Split(cur_val_raw, Byte_ascii.Tilde);
					special_mgr.Add(cur_key, cur_vals);
					fld_bgn = pos + 1;
					break;
				default:
					break;
			}
			if (last) break;
			++pos;
		}
	}
	public static void Save_num_fmt(Gfs_bldr bldr, Xol_num_fmtr_base num_fmt_mgr) {
		if (num_fmt_mgr.Standard()) return;
		bldr.Add_proc_init_many(Xol_lang.Invk_num_fmt, Xol_num_fmtr_base.Invk_clear).Add_nl();
		bldr.Add_proc_cont_one(Xol_num_fmtr_base.Invk_dec_dlm_).Add_parens_str(num_fmt_mgr.Dec_dlm()).Add_nl();
		Xol_num_grp itm = num_fmt_mgr.Grps_len() == 0 ? Xol_num_grp.Default : num_fmt_mgr.Grps_get(0);
		bldr.Add_proc_cont_one(Xol_num_fmtr_base.Invk_grps_add).Add_paren_bgn()
			.Add_arg_str(itm.Dlm()).Add_comma().Add_arg_int(itm.Digits()).Add_comma().Add_arg_yn(true)
			.Add_paren_end().Add_nl();
		bldr.Add_term_nl();
	}
	public static void Save_ns_grps(Gfs_bldr bldr, Xol_ns_grp ns_grp, String proc_invk) {
		int ns_grp_len = ns_grp.Len(); Xol_csv_parser csv_parser = Xol_csv_parser._;
		if (ns_grp_len == 0) return;
		ByteAryBfr bfr = bldr.Bfr();
		bldr.Add_proc_cont_one(proc_invk).Add_nl();
		bldr.Add_indent().Add_proc_cont_one(Invk_load_text).Add_paren_bgn().Add_nl();		//   .load_text(\n
		bldr.Add_quote_xtn_bgn();															//	<~{\n'
		for (int i = 0; i < ns_grp_len; i++) {
			Xow_ns ns = ns_grp.Get_at(i);
			bfr.Add_int_variable(ns.Id()).Add_byte_pipe();									// id|
			csv_parser.Save(bfr, ns.Name_bry());											// name
			bfr.Add_byte_nl();																// \n
		}
		bldr.Add_quote_xtn_end();															// ']:>\n
		bldr.Add_paren_end().Add_proc_cont_one(Invk_lang).Add_nl();							// ).lang\n
	}
	public static void Save_specials(Gfs_bldr bldr, Xol_specials_mgr specials_mgr) {
		int specials_len = specials_mgr.Count(); Xol_csv_parser csv_parser = Xol_csv_parser._;
		if (specials_len == 0) return;
		ByteAryBfr bfr = bldr.Bfr();
		bldr.Add_proc_cont_one(Xol_lang.Invk_specials).Add_nl();
		bldr.Add_indent().Add_proc_cont_one(Xol_specials_mgr.Invk_clear).Add_nl();
		bldr.Add_indent().Add_proc_cont_one(Invk_load_text).Add_paren_bgn().Add_nl();		//   .load_text(\n
		bldr.Add_quote_xtn_bgn();															//	<~{\n'
		for (int i = 0; i < specials_len; i++) {
			Xol_specials_itm itm = specials_mgr.Get_at(i);
			bfr.Add(itm.Special()).Add_byte_pipe();											// id|
			int aliases_len = itm.Aliases().length;
			for (int j = 0; j < aliases_len; j++) {
				if (j != 0) bfr.Add_byte(Byte_ascii.Tilde);
				csv_parser.Save(bfr, itm.Aliases()[j]);										// name
			}
			bfr.Add_byte_nl();																// \n
		}
		bldr.Add_quote_xtn_end();															// ']:>\n
		bldr.Add_paren_end().Add_proc_cont_one(Invk_lang).Add_nl();							// ).lang\n
	}
	public static void Save_keywords(Gfs_bldr bldr, Xol_kwd_mgr kwd_mgr) {
		int len = kwd_mgr.Len(); Xol_csv_parser csv_parser = Xol_csv_parser._;
		int count = 0;
		for (int i = 0; i < len; i++) {
			Xol_kwd_grp grp = kwd_mgr.Get_at(i); if (grp == null) continue; // some items may not be loaded/set by lang
			++count;
		}
		if (count == 0) return;
		ByteAryBfr bfr = bldr.Bfr();
		bldr.Add_proc_cont_one(Xol_lang.Invk_keywords).Add_nl();							// .keywords\n
		bldr.Add_indent().Add_proc_cont_one(Invk_load_text).Add_paren_bgn().Add_nl();		//   .load_text(\n
		bldr.Add_quote_xtn_bgn();															//	<~{\n'
		for (int i = 0; i < len; i++) {
			Xol_kwd_grp grp = kwd_mgr.Get_at(i); if (grp == null) continue;					// some items may not be loaded/set by lang
			csv_parser.Save(bfr, grp.Key());												// key
			bfr.Add_byte_pipe();															// |
			bfr.Add_int_bool(grp.Case_match()).Add_byte_pipe();								// 1|
			int word_len = grp.Itms().length;
			for (int j = 0; j < word_len; j++) {
				Xol_kwd_itm word = grp.Itms()[j];
				csv_parser.Save(bfr, word.Bry());											// word
				bfr.Add_byte(Byte_ascii.Tilde);												// ~
			}
			bldr.Add_nl();																	// \n
		}
		bldr.Add_quote_xtn_end();															// ']:>\n
		bldr.Add_paren_end().Add_proc_cont_one(Invk_lang).Add_nl();							// ).lang\n
	}
	public static void Save_messages(Gfs_bldr bldr, Xol_msg_mgr msg_mgr, boolean dirty) {
		int len = msg_mgr.Itms_max(); Xol_csv_parser csv_parser = Xol_csv_parser._;
		int count = 0;
		for (int i = 0; i < len; i++) {
			Xol_msg_itm itm = msg_mgr.Itm_by_id_or_null(i); if (itm == null) continue;		// some items may not be loaded/set by lang			
			if (dirty && !itm.Dirty()) continue;	// TEST:
			++count;
		}
		if (count == 0) return;
		ByteAryBfr bfr = bldr.Bfr();
		bldr.Add_proc_cont_one(Xol_lang.Invk_messages).Add_nl();							// .keywords\n
		bldr.Add_indent().Add_proc_cont_one(Invk_load_text).Add_paren_bgn().Add_nl();		//   .load_text(\n
		bldr.Add_quote_xtn_bgn();															//	<~{\n'
		for (int i = 0; i < len; i++) {
			Xol_msg_itm itm = msg_mgr.Itm_by_id_or_null(i); if (itm == null) continue;		// some items may not be loaded/set by lang			
			if (dirty && !itm.Dirty()) continue;	// TEST:
			csv_parser.Save(bfr, itm.Key());												// key
			bfr.Add_byte_pipe();															// |
			csv_parser.Save(bfr, itm.Val());												// val
			bfr.Add_byte_nl();																// \n
		}
		bldr.Add_quote_xtn_end();															// ']:>\n
		bldr.Add_paren_end().Add_proc_cont_one(Invk_lang).Add_nl();							// ).lang\n
	}
	public static final String Invk_load_text = "load_text", Invk_lang = "lang";
}

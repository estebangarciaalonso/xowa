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
import gplx.php.*;
public class Pf_msg_mgr {
	public static byte[] Get_msg_by_key(Xow_wiki wiki, Xol_lang page_lang, byte[] msg_key, byte[][] fmt_args) {
		// if "a/b" check if b is lang; if it is, then switch to "b"'s lang (rather than using current page's lang)
		int slash_pos = ByteAry_.FindBwd(msg_key, Byte_ascii.Slash);
		if (slash_pos != ByteAry_.NotFound) {	// key is of format "key/lang"; EX: "January/en"
			int msg_key_len = msg_key.length;
			if (slash_pos != msg_key_len) {		// get text after slash; EX: "en"
				Object o = Xol_lang_itm_.Regy().Get_by_mid(msg_key, slash_pos + 1, msg_key_len);
				if (o != null) {				// text is known lang_code;
					Xol_lang_itm lang_itm = (Xol_lang_itm)o;
					page_lang = wiki.App().Lang_mgr().Get_by_key_or_new(lang_itm.Key());	// set lang
					msg_key = ByteAry_.Mid(msg_key, 0, slash_pos);							// set msg to "a" (discarding "/b")
				}
			}
		}
		Xol_msg_mgr msg_mgr = page_lang.Msg_mgr();
		Xol_msg_itm msg_itm = msg_mgr.Itm_by_key_or_new(msg_key);
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
		// given {{int:message_key}} look for page of "MediaWiki:message_name/lang_code"
		if (msg_itm.Search_mediawiki()) {											// NOTE: all msgs will enter this branch once, even if msg is in lang.gfs file; (since MediaWiki msgs override lang.gfs msg); EX: de.w:{{int:Autosumm-replace}}; DATE:2013-01-25
			Search_wiki_for_msg(tmp_bfr, wiki, page_lang, msg_itm, msg_key);
		}
		// if msg has fmt dlms, format it; EX: Expression error: Unrecognised word "~{0}"
		byte[] tmp_val = msg_itm.Val();
		boolean has_fmt = msg_itm.Has_fmt_arg(), has_tmpl = msg_itm.Has_tmpl_txt();
		if (!has_fmt && !has_tmpl) {	// no fmt or tmpl; just add val
			tmp_bfr.Mkr_rls();
			return tmp_val;
		}
		if (has_fmt) {	// fmt exists; fmt first (before tmpl text)
			ByteAryFmtr tmp_fmtr = ByteAryFmtr.tmp_().Missing_bgn_(Missing_bry).Missing_end_(ByteAry_.Empty).Missing_adj_(1);
			tmp_fmtr.Fmt_(tmp_val);
			tmp_fmtr.Bld_bfr(tmp_bfr, fmt_args);
			tmp_val = tmp_bfr.XtoAryAndClear();
		}
		if (has_tmpl) {
			Xop_ctx inner_ctx = Xop_ctx.new_sub_(wiki);
			Xop_ctx ctx = wiki.Ctx();
			Xop_root_tkn inner_root = ctx.Tkn_mkr().Root(tmp_val);
			tmp_val = wiki.Parser().Parse_page_tmpl(inner_root, inner_ctx, ctx.Tkn_mkr(), tmp_val);
		}
		tmp_bfr.Mkr_rls();
		return tmp_val;
	}	static final byte[] Missing_bry = ByteAry_.new_ascii_("$");
	public static boolean Search_wiki_for_msg(ByteAryBfr tmp_bfr, Xow_wiki wiki, Xol_lang page_lang, Xol_msg_itm msg_itm, byte[] msg_key) {
		Xow_ns ns = wiki.Ns_mgr().Ns_mediawiki();
		tmp_bfr	.Add(ns.Name_db_w_colon())										// "MediaWiki:"
				.Add(msg_key);													// "message_key"
		byte[] wiki_lang_key = wiki.Lang_key();
		if (ByteAry_.Len_eq_0(wiki_lang_key)) wiki_lang_key = Xol_lang_.Key_en;
		if (!ByteAry_.Eq(page_lang.Key_bry(), wiki_lang_key))					// only add lang code if not en; ASSUME: default message is en; EX:commons.wikimedia.org/wiki/Seealso does not have a "/en"
			tmp_bfr.Add_byte(Byte_ascii.Slash).Add(page_lang.Key_bry());		// "/fr"
		byte[] ttl_bry = tmp_bfr.XtoAryAndClear();
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);							// find page with ttl of "MediaWiki:message_name/lang_code"
		Xoa_page msg_page = ttl == null ? null : wiki.Data_mgr().Get_page(ttl, false);
		boolean found = false;
		byte[] msg_val = ByteAry_.Empty;
		if (msg_page == null) {													// page not found
			msg_val = msg_itm.Val();
			if (ByteAry_.Len_eq_0(msg_val)) {									// no msg in lang.gfs; this is not good; set .Val to "<msg_key>" in order to avoids subsequent lookups
				tmp_bfr.Add_byte(Byte_ascii.Lt).Add(msg_key).Add_byte(Byte_ascii.Gt);
				msg_val = tmp_bfr.XtoAryAndClear();
			}
		}
		else {																	// page found; dump entire contents
			msg_val = Convert_php_to_gfs(msg_page.Data_raw(), tmp_bfr);
			found = true;
		}
		Xol_msg_itm_.update_val_(msg_itm, msg_val);
		msg_itm.Search_mediawiki_(false);
		return found;
	}
	public static byte[] Convert_php_to_gfs(byte[] raw, ByteAryBfr bfr) {
		int raw_len = raw.length;
		for (int i = 0; i < raw_len; i++) {
			byte b = raw[i];
			switch (b) {
				case Byte_ascii.Backslash:
					++i;
					if (i < raw_len)
						bfr.Add_byte(raw[i]);
					else
						bfr.Add_byte(Byte_ascii.Backslash);
					break;
				case Byte_ascii.Dollar:
					int end_pos = Php_text_itm_parser.Find_fwd_non_int(raw, i + 1, raw_len);
					int int_val = ByteAry_.XtoIntByPos(raw, i + 1, end_pos, -1);
					bfr.Add_byte(ByteAryFmtr.char_escape).Add_byte(ByteAryFmtr.char_arg_bgn).Add_int_variable(int_val - 1).Add_byte(ByteAryFmtr.char_arg_end);
					i = end_pos - 1;
					break;
				default:
					bfr.Add_byte(b);
					break;
			}
		}
		return bfr.XtoAryAndClear();
	}
}

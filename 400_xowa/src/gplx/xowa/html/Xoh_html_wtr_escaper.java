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
package gplx.xowa.html; import gplx.*; import gplx.xowa.*;
public class Xoh_html_wtr_escaper {
	public static byte[] Escape(Xoa_app app, ByteAryBfr tmp_bfr, byte[] src) {
		Escape(app, tmp_bfr, src, 0, src.length, true, false);
		return tmp_bfr.XtoAryAndClear();
	}
	public static void Escape(Xoa_app app, ByteAryBfr bfr, byte[] src, int bgn, int end, boolean interpret_amp, boolean nowiki_skip) {
		ByteTrieMgr_slim amp_trie = app.Amp_trie();
		ncr_fail.Val_n_();
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Lt:
					if (nowiki_skip) {
						byte[] nowiki_name = Xop_xnde_tag_.Tag_nowiki.Name_bry();
						int nowiki_name_len = nowiki_name.length;
						if (ByteAry_.Eq(nowiki_name, src, i + 1, i + 1 + nowiki_name_len)) {	// <nowiki found;
							int end_gt = Escape_nowiki_skip(app, bfr, src, i, end, nowiki_name, nowiki_name_len);
							if (end_gt != ByteAry_.NotFound) {
								i = end_gt;
								continue;
							}
						}
					}
					bfr.Add(Xop_xnde_wkr.Bry_escape_lt);
					break;
				case Byte_ascii.Gt:
					bfr.Add(Xop_xnde_wkr.Bry_escape_gt);
					break;
				case Byte_ascii.Amp:
					if (interpret_amp) {
						int text_bgn = i + 1;	// i is &; i + 1 is first char after amp
						Object o = (text_bgn < end) ? amp_trie.MatchAtCur(src, text_bgn, end) : null;	// check if this is a valid &; note must check that text_bgn < end or else arrayIndex error; occurs when src is just "&"; DATE:2013-12-19
						if (o == null)										// nope; invalid; EX: "a&b"; "&bad;"; "&#letters;"; 
							bfr.Add(Xop_xnde_wkr.Bry_escape_amp);			// escape & and continue
						else {												// is either (1) a name or (2) an ncr (hex/dec)
							Xop_amp_trie_itm itm = (Xop_amp_trie_itm)o;
							int match_pos = amp_trie.Match_pos();
							int itm_tid = itm.Tid();
							if (itm_tid == Xop_amp_trie_itm.Tid_name) {		// name
								bfr.Add_mid(src, i, match_pos);				// embed entire name
								i = match_pos - 1;
							}
							else {											// ncr: dec/hex
								int end_pos = Xop_amp_wkr.CalcNcr(app.Msg_log(), itm_tid == Xop_amp_trie_itm.Tid_num_hex, src, end, bgn, match_pos, ncr_rslt, ncr_fail); // parse hex/dec
								if (ncr_fail.Val())					// parse failed; escape and continue
									bfr.Add(Xop_xnde_wkr.Bry_escape_amp);
								else {										// parse worked; embed entire ncr
									bfr.Add_mid(src, i, end_pos);
									i = end_pos - 1;
								}
							}
						}
					}
					else
						bfr.Add(Xop_xnde_wkr.Bry_escape_amp);
					break;
				case Byte_ascii.Quote:
					bfr.Add(Xop_xnde_wkr.Bry_escape_quote);
					break;
				default:
					bfr.Add_byte(b);
					break;
			}
		}
	}
	private static int Escape_nowiki_skip(Xoa_app app, ByteAryBfr bfr, byte[] src, int bgn, int end, byte[] nowiki_name, int nowiki_name_len) {
		try {
			boolean tag_is_bgn = true;
			int bgn_gt = -1, end_lt = -1, end_gt = -1;
			for (int i = bgn + nowiki_name_len; i < end; i++) {
				byte b = src[i];
				switch (b) {
					case Byte_ascii.Gt:
						if	(tag_is_bgn)	{bgn_gt = i; tag_is_bgn = false;}
						else				return ByteAry_.NotFound;								// <nowiki>> found
						break;
					case Byte_ascii.Lt:
						if (	tag_is_bgn															// <nowiki < found
							||	(i + nowiki_name_len + 2 > end) 									// not enough chars for "/nowiki>"
							||	src[i + 1] != Byte_ascii.Slash 										// / 
							||	!ByteAry_.Eq(nowiki_name, src, i + 2, i + 2 + nowiki_name_len)		//  nowiki
							||	src[i + 2 + nowiki_name_len] != Byte_ascii.Gt						//        >
							)	return ByteAry_.NotFound;
						end_lt = i;
						end_gt = i + 2 + nowiki_name_len;
						i = end;
						break;
				}
			}
			if (end_gt == -1) return ByteAry_.NotFound;	// ">" of </nowiki> not found
			bfr.Add_mid(src, bgn_gt + 1, end_lt);
			return end_gt;
		}
		catch (Exception e) {
			app.Usr_dlg().Warn_many("", "", "unknown error in escape.nowiki: ~{0} ~{1}", Err_.Message_gplx_brief(e), String_.new_utf8_(src, bgn, end));
			return ByteAry_.NotFound;
		}
	}
	private static IntRef ncr_rslt = IntRef.zero_(); private static BoolRef ncr_fail = BoolRef.n_();
}

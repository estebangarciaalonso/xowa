/*
XOWA: the extensible offline wiki application
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
public class Xop_amp_wkr implements Xop_ctx_wkr {
	public void Ctor_ctx(Xop_ctx ctx) {}
	public void Page_bgn(Xop_ctx ctx) {}
	public void Page_end(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int src_len) {}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn, int cur_pos) {
		if (cur_pos == src_len) return ctx.LxrMake_txt_(cur_pos);	// NOTE: & is last char in page; strange and rare, but don't raise error
		Object o = ctx.App().AmpTrie().MatchAtCur(src, cur_pos, src_len);
		cur_pos = ctx.App().AmpTrie().Match_pos();
		if (o == null) return ctx.LxrMake_txt_(cur_pos);
		Xop_amp_trie_itm itm = (Xop_amp_trie_itm)o;
		if (itm.Tid() == Xop_amp_trie_itm.Tid_name) {
			ctx.Subs_add(tkn_mkr.HtmlRef(bgn, cur_pos, itm));
			return cur_pos;
		}
		else {
			boolean ncr_is_hex = itm.Tid() == Xop_amp_trie_itm.Tid_num_hex;
			fail.Val_false();
			int rv = CalcNcr(ctx.Msg_log(), ncr_is_hex, src, src_len, bgn, cur_pos, ncr_val, fail);
			if (fail.Val()) return ctx.LxrMake_txt_(cur_pos);
			ctx.Subs_add(tkn_mkr.HtmlNcr(bgn, rv, ncr_val.Val()));
			return rv;
		}
	}
	public static int CalcNcr(Gfo_msg_log msg_log, boolean ncr_is_hex, byte[] src, int src_len, int bgn, int int_bgn, IntRef ncr_val, BoolRef fail) {
		int cur_pos = int_bgn, int_end = -1;			
		int max_len = ncr_is_hex ? Max_len_hex : Max_len_dec;
		int max_pos = cur_pos + max_len;
		byte b = 0;
		while (true) {	// find semicolon
			if		(cur_pos == src_len)		{fail.Val_true(); msg_log.Add_itm_none(Xop_amp_log.Eos, src, int_bgn, src_len); return bgn + 1;}	// eos
			else if (cur_pos == max_pos)		{fail.Val_true(); return bgn + 1;} // looks like ncr, but no semic found; EX.WP:Afghanistan "&#af |title="
			b = src[cur_pos];
			if (b == Byte_ascii.Semic) {
				int_end = cur_pos - Char_.CharLen;	// int_end = pos before semicolon
				cur_pos++;							// cur_pos = pos after  semicolon
				break;
			}
			cur_pos++;
		}
		int multiple = ncr_is_hex ? 16 : 10, val = 0, factor = 1, cur = 0;
		for (int i = int_end; i >= int_bgn; i--) {
			b = src[i];
			if (ncr_is_hex) {
				if		(b >=  48 && b <=  57)	cur = b - 48;
				else if	(b >=  65 && b <=  70)	cur = b - 55;
				else if	(b >=  97 && b <= 102)	cur = b - 87;
				else if((b >=  71 && b <=  90)
					||  (b >=  91 && b <= 122))	continue;	// NOTE: wiki discards letters G-Z; EX.WP:Miscellaneous Symbols "{{Unicode|&#xx26D0;}}"; NOTE 2nd x is discarded
				else							{return Fail(fail, msg_log, src, int_bgn, i, Xop_amp_log.Invalid_hex, bgn);}
			}
			else {
				cur = b - Byte_ascii.Num_0;
				if (cur < 0 || cur > 10)		{return Fail(fail, msg_log, src, int_bgn, i, Xop_amp_log.Invalid_dec, bgn);}
			}
			val += cur * factor;
			factor *= multiple;
		}
		ncr_val.Val_(val);
		return cur_pos;
	}
	private static int Fail(BoolRef fail, Gfo_msg_log msg_log, byte[] src, int int_bgn, int cur_pos, Gfo_msg_itm itm, int bgn) {
		fail.Val_true();
		msg_log.Add_itm_none(itm, src, int_bgn, cur_pos);
		return bgn + 1;
	}
	IntRef ncr_val = IntRef.neg1_(); BoolRef fail = BoolRef.new_(false); static final int Max_len_hex = 8, Max_len_dec = 10;
}

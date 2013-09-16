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
public class Xop_lxr_ {
	public static final byte Tid_pipe = 0, Tid_space = 1, Tid_nbsp = 2, Tid_tab = 3, Tid_nl = 4, Tid_amp = 5, Tid_apos = 6, Tid_colon = 7, Tid_lnki_bgn = 8, Tid_lnki_end = 9
		, Tid_list = 10, Tid_hdr = 11, Tid_hr = 12, Tid_xnde = 13, Tid_lnke_bgn = 14, Tid_lnke_end = 15, Tid_tblw = 16, Tid_pre = 17, Tid_under = 18, Tid_comment = 19
		, Tid_eq = 20, Tid_curly_bgn = 21, Tid_curly_end = 22, Tid_brack_bgn = 23, Tid_brack_end = 24, Tid_poem = 25, Tid_variant = 26, Tid_tvar = 27;
	public static int Find_fwd_while_non_ws(byte[] src, int bgn, int end) {
		int cur = bgn;
		while (true) {
			if (cur == end) return cur;
			byte b = src[cur];
			switch (b) {
				case Byte_ascii.Space:
				case Byte_ascii.NewLine:
				case Byte_ascii.Tab:
					++cur;
					break;
				default:
					return cur;
			}
		}
	}
	public static int Find_fwd_while(byte[] src, int srcLen, int curPos, byte continueByte) {
		while (true) {
			if (curPos == srcLen
				|| src[curPos] != continueByte) return curPos;
			curPos++;
		}
	}
	public static int Find_fwd_while_ws(byte[] src, int srcLen, int curPos) {
		while (true) {
			if (curPos == srcLen) return curPos;
			switch (src[curPos]) {
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
					break;
				default:
					return curPos;
			}
			curPos++;
		}
	}
	public static int Find_fwd_while_ws_hdr_version(byte[] src, int srcLen, int curPos) {
		int last_nl = -1;
		while (true) {
			if (curPos == srcLen) return curPos;
			byte b = src[curPos];
			switch (b) {
				case Byte_ascii.NewLine:
					curPos++;
					last_nl = curPos;
					break;
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
					curPos++;
					break;
				default:
					return last_nl == -1 ? curPos : last_nl - 1;
			}
		}
	}
	public static int FindFwdUntil(byte[] src, int srcLen, int curPos, byte stopByte) {
		while (true) {
			if (curPos == srcLen
				|| src[curPos] == stopByte) return curPos;
			curPos++;
		}
	}
	public static int FindFwdUntilPunctuationOrWs(byte[] src, int srcLen, int curPos) {
		while (true) {
			if (curPos == srcLen) return curPos;
			byte b = src[curPos];
			if (b < 48 || (b > 57 && b < 65) || (b > 90 && b < 97) || (b > 122 && b < 127)) return curPos;
			curPos++;
		}
	}
	public static int FindNonLetter(byte[] src, int cur, int len) {
		while (cur < len) {
			switch (src[cur]) {
				case Byte_ascii.Ltr_A: case Byte_ascii.Ltr_B: case Byte_ascii.Ltr_C: case Byte_ascii.Ltr_D: case Byte_ascii.Ltr_E:
				case Byte_ascii.Ltr_F: case Byte_ascii.Ltr_G: case Byte_ascii.Ltr_H: case Byte_ascii.Ltr_I: case Byte_ascii.Ltr_J:
				case Byte_ascii.Ltr_K: case Byte_ascii.Ltr_L: case Byte_ascii.Ltr_M: case Byte_ascii.Ltr_N: case Byte_ascii.Ltr_O:
				case Byte_ascii.Ltr_P: case Byte_ascii.Ltr_Q: case Byte_ascii.Ltr_R: case Byte_ascii.Ltr_S: case Byte_ascii.Ltr_T:
				case Byte_ascii.Ltr_U: case Byte_ascii.Ltr_V: case Byte_ascii.Ltr_W: case Byte_ascii.Ltr_X: case Byte_ascii.Ltr_Y: case Byte_ascii.Ltr_Z:
				case Byte_ascii.Ltr_a: case Byte_ascii.Ltr_b: case Byte_ascii.Ltr_c: case Byte_ascii.Ltr_d: case Byte_ascii.Ltr_e:
				case Byte_ascii.Ltr_f: case Byte_ascii.Ltr_g: case Byte_ascii.Ltr_h: case Byte_ascii.Ltr_i: case Byte_ascii.Ltr_j:
				case Byte_ascii.Ltr_k: case Byte_ascii.Ltr_l: case Byte_ascii.Ltr_m: case Byte_ascii.Ltr_n: case Byte_ascii.Ltr_o:
				case Byte_ascii.Ltr_p: case Byte_ascii.Ltr_q: case Byte_ascii.Ltr_r: case Byte_ascii.Ltr_s: case Byte_ascii.Ltr_t:
				case Byte_ascii.Ltr_u: case Byte_ascii.Ltr_v: case Byte_ascii.Ltr_w: case Byte_ascii.Ltr_x: case Byte_ascii.Ltr_y: case Byte_ascii.Ltr_z:
					break;
				default:
					return cur;
			}
			++cur;
		}
		return cur;
	}
	public static int Find_fwd_non_ws(byte[] src, int bgn, int end) {
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
				case Byte_ascii.NewLine:
				case Byte_ascii.CarriageReturn:
					break;
				default:
					return i;
			}
		}
		return end;
	}
	public static int Find_fwd_skip_ws(byte[] src, int bgn, int end, byte find) {
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
				case Byte_ascii.NewLine:
				case Byte_ascii.CarriageReturn:
					break;
				default:
					return (b == find) ? i : ByteAry_.NotFound;
			}
		}
		return ByteAry_.NotFound;
	}
	public static byte FindFwdNextNonWs(byte[] src, int src_len, int cur_pos) {
		for (int i = cur_pos; i < src_len; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
				case Byte_ascii.NewLine:
				case Byte_ascii.CarriageReturn:
					break;
				default:
					return b;
			}
		}
		return Byte_ascii.Nil;
	}
}

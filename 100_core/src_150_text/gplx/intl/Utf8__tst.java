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
package gplx.intl; import gplx.*;
import org.junit.*;
public class Utf8__tst {
	@Test  public void EncodeDecode() {
		Tst_EncodeDecode(162, 194, 162);				// cent
		Tst_EncodeDecode(8364, 226, 130, 172);			// euro
		Tst_EncodeDecode(150370, 240, 164, 173, 162);	// example from [[UTF-8]]
	}
	@Test  public void Char_bgn() {
		Tst_Char_bgn("abcd", 3);		// len=1; (note that bry.len = 4)
		Tst_Char_bgn("a", 0);			// len=1; short-String
		Tst_Char_bgn("abc¢", 3);		// len=2; (note that bry.len = 5)
		Tst_Char_bgn("abc€", 3);		// len=3; (note that bry.len = 6)
		Tst_Char_bgn("abc" + String_.new_utf8_(Byte_.Ary_by_ints(240, 164, 173, 162)), 3);		// len=4; (note that bry.len = 7)
	}
	@Test  public void Increment_char_last() {
		Tst_Increment_char_last("a", "b");
		Tst_Increment_char_last("abc", "abd");
		Tst_Increment_char_last("É", "Ê");	// len=2
		Tst_Increment_char_last("€", "₭");	// len=3
	}
//		@Test  public void Increment_char_last_check() {
//			ByteAryBfr bfr = ByteAryBfr.new_();
//			int bgn = 32;
//			while (true) {
//				byte[] bgn_bry = Utf8_.EncodeCharAsAry(bgn);
//				int end = Utf8_.Codepoint_next(bgn);
//				if (end == Utf8_.Codepoint_max) break;
////				if (bgn > 1024 * 1024) break;
//				byte[] end_by_codepoint_next = Utf8_.EncodeCharAsAry(end);
//				byte[] end_by_increment_char = Utf8_.Increment_char_last(bgn_bry);
//				if (!ByteAry_.Eq(end_by_codepoint_next, end_by_increment_char)) {
//					Tfds.Write(bgn);
//				}				
////				bfr	.Add_int_variable(bgn).Add_byte(Byte_ascii.Tab)
////					.Add(bgn_bry).Add_byte(Byte_ascii.Tab)
////					.Add(end_by_codepoint_next).Add_byte(Byte_ascii.Tab)
////					.Add(end_by_increment_char).Add_byte(Byte_ascii.Tab)
////					.Add_byte_nl()
////					;
//				bgn = end;
//				bgn_bry = end_by_codepoint_next;
//			}
////			Tfds.WriteText(bfr.XtoStrAndClear());
//		}
	@Test  public void Encode_as_bry_by_hex() {
		tst_Encode_as_bry_by_hex("00", 0);
		tst_Encode_as_bry_by_hex("41", 65);
		tst_Encode_as_bry_by_hex("0041", 65);
		tst_Encode_as_bry_by_hex("00C0", 195, 128);
	}
	private void tst_Encode_as_bry_by_hex(String raw, int... expd) {
		byte[] actl = Utf8_.Encode_as_bry_by_hex(raw);
		Tfds.Eq_ary(Byte_.Ary_by_ints(expd), actl);
	}
	private void Tst_EncodeDecode(int expd_c_int, int... expd_int) {
		byte[] expd = ByteAry_.ints_(expd_int);
		byte[] bfr = new byte[10];
		int bfr_len = Utf8_.EncodeChar(expd_c_int, bfr, 0);
		byte[] actl = ByteAry_.Mid_by_len(bfr, 0, bfr_len);
		Tfds.Eq_ary(expd, actl);
		int actl_c_int = Utf8_.DecodeChar(bfr, 0);
		Tfds.Eq(expd_c_int, actl_c_int);
	}
	private void Tst_Char_bgn(String str, int expd) {
		byte[] bry = ByteAry_.new_utf8_(str);
		int pos = bry.length - 1;	// always start from last char
		Tfds.Eq(expd, Utf8_.Char_bgn(bry, pos));
	}
	private void Tst_Increment_char_last(String str, String expd) {
		Tfds.Eq(expd, String_.new_utf8_(Utf8_.Increment_char_last(ByteAry_.new_utf8_(str))));
	}
}

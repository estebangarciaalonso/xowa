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
package gplx;
import org.junit.*;
public class Int__tst {
	@Test  public void XtoStr_PadBgn() {
//			tst_XtoStr_PadLeft_Zeroes(1		, 3, "001");		// pad
//			tst_XtoStr_PadLeft_Zeroes(123	, 3, "123");		// no pad
//			tst_XtoStr_PadLeft_Zeroes(1234	, 3, "1234");		// val exceeds pad; confirm noop
		tst_XtoStr_PadLeft_Zeroes(-1	, 3, "-01");		// negative
		tst_XtoStr_PadLeft_Zeroes(-12	, 3, "-12");		// negative
		tst_XtoStr_PadLeft_Zeroes(-123	, 3, "-123");		// negative
		tst_XtoStr_PadLeft_Zeroes(-1234	, 3, "-1234");		// negative
	}	void tst_XtoStr_PadLeft_Zeroes(int val, int zeros, String expd) {Tfds.Eq(expd, Int_.Xto_str_pad_bgn(val, zeros));}
	@Test  public void parseOr_() {
		tst_ParseOr("", -1);		// empty
		tst_ParseOr("123", 123);	// single
		tst_ParseOr("1a", -1);		// fail
	}	void tst_ParseOr(String raw, int expd) {Tfds.Eq(expd, Int_.parse_or_(raw, -1));}
	@Test  public void Between() {
		tst_Between(1, 0, 2, true);		// simple true
		tst_Between(3, 0, 2, false);	// simple false
		tst_Between(0, 0, 2, true);		// bgn true
		tst_Between(2, 0, 2, true);		// end true
	}	void tst_Between(int val, int lhs, int rhs, boolean expd) {Tfds.Eq(expd, Int_.Between(val, lhs, rhs));}
	@Test  public void Xto_fmt() {
		tst_XtoStr_fmt(1, "1");
		tst_XtoStr_fmt(1000, "1,000");
	}	void tst_XtoStr_fmt(int v, String expd) {Tfds.Eq(expd, Int_.Xto_str_fmt(v, "#,###"));}
	@Test  public void AryRng() {
		tst_AryRng(1, 3, Int_.Ary(1, 2, 3));
	}	void tst_AryRng(int bgn, int end, int[] expd) {Tfds.Eq_ary(expd, Int_.AryRng(bgn, end));}
	@Test  public void Log10_pos() {
		tst_Log10(0, 0);
		tst_Log10(1, 0);
		tst_Log10(9, 0);
		tst_Log10(10, 1);
		tst_Log10(100, 2);
		tst_Log10(1000000, 6);
		tst_Log10(1000000000, 9);
		tst_Log10(Int_.MaxValue, 9);
	}
	@Test  public void Log10_neg() {
		tst_Log10(-1, 0);
		tst_Log10(-10, -1);
		tst_Log10(-100, -2);
		tst_Log10(-1000000, -6);
		tst_Log10(-1000000000, -9);
		tst_Log10(Int_.MinValue, -9);
		tst_Log10(Int_.MinValue + 1, -9);
	}
	void tst_Log10(int val, int expd) {Tfds.Eq(expd, Int_.Log10(val));}
	@Test  public void DigitCount() {
		tst_DigitCount(0, 1);
		tst_DigitCount(9, 1);
		tst_DigitCount(100, 3);
		tst_DigitCount(-1, 2);
		tst_DigitCount(-100, 4);
	}	void tst_DigitCount(int val, int expd) {Tfds.Eq(expd, Int_.DigitCount(val), Int_.Xto_str(val));}
	@Test  public void Log10() {
		tst_Log10(            0,  0);
		tst_Log10(            1,  0);
		tst_Log10(            2,  0);
		tst_Log10(           10,  1);
		tst_Log10(           12,  1);
		tst_Log10(          100,  2);
		tst_Log10(          123,  2);
		tst_Log10(         1000,  3);
		tst_Log10(         1234,  3);
		tst_Log10(        10000,  4);
		tst_Log10(        12345,  4);
		tst_Log10(       100000,  5);
		tst_Log10(       123456,  5);
		tst_Log10(      1000000,  6);
		tst_Log10(      1234567,  6);
		tst_Log10(     10000000,  7);
		tst_Log10(     12345678,  7);
		tst_Log10(    100000000,  8);
		tst_Log10(    123456789,  8);
		tst_Log10(   1000000000,  9);
		tst_Log10(   1234567890,  9);
		tst_Log10(Int_.MaxValue,  9);
	}
	@Test  public void Xto_int_hex_tst() {
		Xto_int_hex("007C", 124);
	}	void Xto_int_hex(String raw, int expd) {Tfds.Eq(expd, Int_.Xto_int_hex(Bry_.new_ascii_(raw)));}
	@Test  public void Ary_parse() {
		Ary_parse__tst("1,2,3"							, 3, Int_.Ary_empty,   1,   2,   3);
		Ary_parse__tst("123,321,213"					, 3, Int_.Ary_empty, 123, 321, 213);
		Ary_parse__tst(" 1,  2,3"						, 3, Int_.Ary_empty,   1,   2,   3);
		Ary_parse__tst("-1,+2,-3"						, 3, Int_.Ary_empty,  -1,   2,  -3);
		Ary_parse__tst(Int_.Xto_str(Int_.MinValue)		, 1, Int_.Ary_empty, Int_.MinValue);
		Ary_parse__tst(Int_.Xto_str(Int_.MaxValue)		, 1, Int_.Ary_empty, Int_.MaxValue);
		Ary_parse__tst("1,2"							, 1, Int_.Ary_empty);
		Ary_parse__tst("1"								, 2, Int_.Ary_empty);
		Ary_parse__tst("a"								, 1, Int_.Ary_empty);
		Ary_parse__tst("1-2,"							, 1, Int_.Ary_empty);
	}
	void Ary_parse__tst(String raw, int reqd_len, int[] or, int... expd) {Tfds.Eq_ary(expd, Int_.Ary_parse(raw, reqd_len, or));}
}

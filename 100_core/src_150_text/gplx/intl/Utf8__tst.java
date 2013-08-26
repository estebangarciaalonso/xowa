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
package gplx.intl; import gplx.*;
import org.junit.*;
public class Utf8__tst {
	@Test  public void EncodeDecode() {
		tst_EncodeDecode(162, 194, 162);				// cent
		tst_EncodeDecode(8364, 226, 130, 172);			// euro
		tst_EncodeDecode(150370, 240, 164, 173, 162);	// example from [[UTF-8]]
	}
	void tst_EncodeDecode(int expd_c_int, int... expd_int) {
		byte[] expd = ByteAry_.ints_(expd_int);
		byte[] bfr = new byte[10];
		int bfr_len = Utf8_.EncodeChar(expd_c_int, bfr, 0);
		byte[] actl = ByteAry_.Mid_by_len(bfr, 0, bfr_len);
		Tfds.Eq_ary(expd, actl);
		int actl_c_int = Utf8_.DecodeChar(bfr, 0);
		Tfds.Eq(expd_c_int, actl_c_int);
	}
}

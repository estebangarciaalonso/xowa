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
package gplx.texts; import gplx.*;
import org.junit.*;
public class Encoding__tst {
	@Test public void XtoByteAry() {
		tst_Convert("a", ary_(97));
		tst_Convert("a b", ary_(97, 32, 98));
		tst_Convert("©", ary_(194, 169));
	}
	byte[] ary_(int... ary) {
		int len = Array_.Len(ary);
		byte[] rv = new byte[len];
		for (int i = 0; i < len; i++)
			rv[i] = (byte)ary[i];
		return rv;
	}
	void tst_Convert(String text, byte... ary) {
		Tfds.Eq_ary_str(ary, Encoding_.XtoByteAry(text)); // ary_str b/c using ints in cs b/c java may not handle unsigned bytes
		Tfds.Eq(text, Encoding_.XtoStr(ary));
	}	
}

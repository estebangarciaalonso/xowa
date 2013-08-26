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
package gplx;
public class ByteAryRef {
	public byte[] Val() {return val;} public ByteAryRef Val_(byte[] v) {val = v; return this;} private byte[] val;
	@Override public int hashCode() {return CalcHashCode(val, 0, val.length);}
	@Override public boolean equals(Object obj) {return obj == null ? false : ByteAry_.Eq(val, ((ByteAryRef)obj).Val());}	// NOTE: strange, but null check needed; throws null error; EX.WP: File:Eug�ne Delacroix - La libert� guidant le peuple.jpg
	public static int CalcHashCode(byte[] ary, int bgn, int end) {
		int rv = 0;
		for (int i = bgn; i < end; i++)
			rv = (31 * rv) + ary[i];
		return rv;
	}
	public static ByteAryRef null_() {return new_(null);}
        public static ByteAryRef new_(byte[] val) {
		ByteAryRef rv = new ByteAryRef();
		rv.val = val;
		return rv;
	}	private ByteAryRef() {}
}

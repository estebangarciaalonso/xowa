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
public class ByteRef {
	public byte Val() {return val;} private byte val;
	public ByteRef Val_(byte v) {val = v; return this;}
	@Override public int hashCode() {return val;}
	@Override public boolean equals(Object obj) {return obj == null ? false : val == ((ByteRef)obj).Val();}
	@Override public String toString() {return Int_.XtoStr(val);}
        public static ByteRef zero_() {return new_(Byte_.Zero);}
        public static ByteRef new_(byte val) {
		ByteRef rv = new ByteRef();
		rv.val = val;
		return rv;
	}	private ByteRef() {}
}

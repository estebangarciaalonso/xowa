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
package gplx.byteAryFmtrArgs; import gplx.*;
public class ByteAryFmtrArg_ary_dim2 implements ByteAryFmtrArg {
	public ByteAryFmtrArg_ary_dim2 Data_(byte[][] v) {ary_dim2 = v; return this;}
	public ByteAryFmtrArg_ary_dim2 Ary_dim2_(byte[][] v) {this.ary_dim2 = v; return this;}
	public void XferAry(ByteAryBfr trg, int idx) {
		for (byte[] ary : ary_dim2)
			trg.Add(ary);
	}
	public ByteAryFmtrArg_ary_dim2() {} byte[][] ary_dim2 = new byte[0][];
}

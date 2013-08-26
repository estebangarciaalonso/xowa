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
public class KeyVal implements XtoStrAble {
	public String Key() {return Object_.XtoStr_OrNull(key);}
	public Object Key_as_obj() {return key;} Object key;
	public KeyVal Key_(Object v) {this.key = v; return this;}
	public Object Val() {return val;} public KeyVal Val_(Object v) {val = v; return this;} Object val;
	public String Val_to_str_or_empty() {return Object_.XtoStr_OrEmpty(val);}
	public String Val_to_str_or_null() {return Object_.XtoStr_OrNull(val);}
	public byte[] Val_to_bry() {return ByteAry_.new_utf8_(Object_.XtoStr_OrNull(val));}
	@Override public String toString() {return XtoStr();}
	public String XtoStr() {return Key() + "=" + Object_.XtoStr_OrNullStr(val);}
	@gplx.Internal protected KeyVal(Object k, Object v) {key = k; val = v;}
}

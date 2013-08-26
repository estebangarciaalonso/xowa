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
public class StringBldr_kv {
	public StringBldr_kv Add(String k, Object v) {
		if (sb.Count() != 0) sb.Add(" ");
		sb.Add_fmt("{0}={1}", k, Object_.XtoStr_OrNullStr(v));
		return this;
	}
	public String XtoStr() {return sb.XtoStrAndClear();}
	StringBldr sb = StringBldr.new_();
        public static StringBldr_kv new_(String k, Object v) {return new StringBldr_kv().Add(k, v);} StringBldr_kv() {}
}
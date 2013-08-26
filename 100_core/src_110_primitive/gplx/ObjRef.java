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
public class ObjRef {
	public Object Val() {return val;} public ObjRef Val_(Object v) {val = v; return this;} Object val;
	public ObjRef Val_null_() {return Val_(null);}
	@Override public String toString() {return Object_.XtoStr_OrNullStr(val);}
	public static ObjRef null_() {return new_(null);}
        public static ObjRef new_(Object val) {
		ObjRef rv = new ObjRef();
		rv.val = val;
		return rv;
	}	ObjRef() {}
}

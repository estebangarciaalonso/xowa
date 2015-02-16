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
package gplx.core.primitives; import gplx.*; import gplx.core.*;
public class Int_obj_val implements CompareAble {
	public int Val() {return val;} int val;
	@Override public String toString() {return Int_.Xto_str(val);}
	@Override public int hashCode() {return val;}
	@Override public boolean equals(Object obj) {return obj == null ? false : val == ((Int_obj_val)obj).Val();}
	public int compareTo(Object obj) {Int_obj_val comp = (Int_obj_val)obj; return Int_.Compare(val, comp.val);}
        public static Int_obj_val neg1_() {return new_(-1);}
        public static Int_obj_val zero_() {return new_(0);}
        public static Int_obj_val new_(int val) {
		Int_obj_val rv = new Int_obj_val();
		rv.val = val;
		return rv;
	}	Int_obj_val() {}
}

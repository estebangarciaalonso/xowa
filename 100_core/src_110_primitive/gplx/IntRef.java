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
public class IntRef {
	public int Val() {return val;} public IntRef Val_(int v) {val = v; return this;} int val;
	public int Val_add() {val++; return val;}
	public int Val_add_post() {return val++;}
	public int Val_add(int v) {val += v; return val;}		
	public void Val_zero_() {val = 0;}
	public IntRef Val_neg1_() {val = -1; return this;}
	@Override public String toString() {return Int_.XtoStr(val);}
	@Override public int hashCode() {return val;}
	@Override public boolean equals(Object obj) {return val == ((IntRef)obj).Val();}
        public static IntRef neg1_() {return new_(-1);}
        public static IntRef zero_() {return new_(0);}
        public static IntRef new_(int val) {
		IntRef rv = new IntRef();
		rv.val = val;
		return rv;
	}	IntRef() {}
}

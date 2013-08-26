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
public class BoolRef {
	public boolean Val() {return val;} private boolean val;
	public BoolRef Val_true() {val = true; return this;}
	public BoolRef Val_false() {val = false; return this;}
	public BoolRef Val_(boolean v) {val = v; return this;}
	public BoolRef Val_toggle() {val = !val; return this;}
	@Override public String toString() {return Bool_.XtoStr_lower(val);}
        public static BoolRef false_() {return new_(false);}
        public static BoolRef true_() {return new_(true);}
        public static BoolRef new_(boolean val) {
		BoolRef rv = new BoolRef();
		rv.val = val;
		return rv;
	}	BoolRef() {}
}

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
public class BoolVal {
	public boolean Val() {return val == 1;}
	BoolVal(int v) {val = v;} int val;
	public static final BoolVal
		  Null	= new BoolVal(-1)
		, False = new BoolVal(0)
		, True	= new BoolVal(1);
	public static BoolVal read_(Object o) {String s = String_.as_(o); return s == null ? (BoolVal)o : parse_(s);}
	public static BoolVal parse_(String raw) {
		if		(String_.Eq(raw, "y"))	return BoolVal.True;
		else if	(String_.Eq(raw, "n"))	return BoolVal.False;
		else if	(String_.Eq(raw, ""))	return BoolVal.Null;
		else	throw Err_.parse_type_(BoolVal.class, raw);
	}
}

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
public class StringBldr {
	public boolean Has_none() {return this.Count() == 0;}
	public boolean Has_some() {return this.Count() > 0;}
	public StringBldr Add_many(String... array) {for (String s : array) Add(s); return this;}
	public StringBldr Add_fmt(String format, Object... args) {Add(String_.Format(format, args)); return this;}
	public StringBldr Add_fmt_line(String format, Object... args) {Add_line(String_.Format(format, args)); return this;}
	public StringBldr Add_line() {Add(String_.CrLf); return this;}
	public StringBldr Add_line(String line) {Add(line); Add(String_.CrLf); return this;}
	public StringBldr Add_char_pipe() {return Add("|");}
	public StringBldr Add_line_only() {Add(String_.CrLf); return this;}
	public StringBldr Add_line_nl() {Add(Op_sys.Lnx.Nl_str()); return this;}
	public StringBldr Add_line_any(Object... array) {
		Add(String_.Concat_any(array));
		Add(String_.CrLf);
		return this;
	}
	public StringBldr Add_lines_crlf(String... ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			Add(ary[i]);
			Add(String_.CrLf);
		}
		return this;
	}
	public StringBldr Add_spr_unless_first(String s, String spr, int i) {
		if (i != 0) Add(spr);
		Add(s);
		return this;
	}
	public StringBldr Add_keyVal(String hdr, String val) {
		if (String_.Len_eq_0(val)) return this;
		if (this.Count() != 0) this.Add(' ');
		this.Add(hdr);
		this.Add(val);
		return this;
	}
	public StringBldr Clear() {Del(0, Count()); return this;}
	public String XtoStrAndClear() {
		String rv = XtoStr();
		Clear();
		return rv;
	}
	@Override public String toString() {return XtoStr();}
	java.lang.StringBuilder sb = new java.lang.StringBuilder();
	public String XtoStr() {return sb.toString();}
	public int Count() {return sb.length();}
	public StringBldr AddAt(int idx, String s) {sb.insert(idx, s); return this;}
	public StringBldr Add(byte[] v) {sb.append(String_.new_utf8_(v)); return this;}		
	public StringBldr Add(String s) {sb.append(s); return this;}						
	public StringBldr Add(char c) {sb.append(c); return this;}							
	public StringBldr Add(int i) {sb.append(i); return this;}							
	public StringBldr Add_aryByPos(char[] ary, int bgn, int count) {sb.append(ary, bgn, count); return this;}
	public StringBldr Add_any(Object o) {sb.append(o); return this;}	
	public StringBldr Del(int bgn, int len) {sb.delete(bgn, len); return this;}
	public static StringBldr new_() {return new StringBldr();}
	public static StringBldr raw_(String s) {
		StringBldr sb = new StringBldr();
		sb.Add(s);
		return sb;
	}
}

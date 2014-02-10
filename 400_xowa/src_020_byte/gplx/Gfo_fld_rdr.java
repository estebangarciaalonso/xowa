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
public class Gfo_fld_rdr extends Gfo_fld_base {
	public byte[] Bry() {return bry;} public Gfo_fld_rdr Bry_(byte[] v) {bry = v; bry_len = v.length; pos = 0; return this;} private byte[] bry; int bry_len;
	public int Pos() {return pos;} public Gfo_fld_rdr Pos_(int v) {pos = v; return this;} private int pos;
	public int Fld_bgn() {return fld_bgn;} public Gfo_fld_rdr Fld_bgn_(int v) {fld_bgn = v; return this;} private int fld_bgn;
	public int Fld_end() {return fld_end;} public Gfo_fld_rdr Fld_end_(int v) {fld_end = v; return this;} private int fld_end;
	public int Fld_idx() {return fld_idx;} private int fld_idx;
	public int Row_idx() {return row_idx;} private int row_idx;
	public void Ini(byte[] bry, int pos) {this.bry = bry; this.bry_len = bry.length; this.pos = pos;}

	public String Read_str_simple()	{Move_next_simple(); return String_.new_utf8_(bry, fld_bgn, fld_end);}
	public byte[] Read_bry_simple() {Move_next_simple(); return ByteAry_.Mid(bry, fld_bgn, fld_end);}	// was Mid_by_len???; 20120915
	public int Read_int_base85_lenN(int len)	{fld_bgn = pos; fld_end = pos + len - 1	; pos = pos + len + 1	; return Base85_utl.XtoIntByAry(bry, fld_bgn, fld_end);}
	public int Read_int_base85_len5()			{fld_bgn = pos; fld_end = pos + 4		; pos = pos + 6			; return Base85_utl.XtoIntByAry(bry, fld_bgn, fld_end);}
	public int Read_int() 			{Move_next_simple(); return ByteAry_.X_to_int_or(bry, fld_bgn, fld_end, -1);}
	public byte Read_int_as_byte() 	{Move_next_simple(); return (byte)ByteAry_.X_to_int_or(bry, fld_bgn, fld_end, -1);}
	public byte Read_byte() 		{Move_next_simple(); return bry[fld_bgn];}
	public double Read_double() 	{Move_next_simple(); return ByteAry_.XtoDoubleByPos(bry, fld_bgn, fld_end);}
	public DateAdp Read_dte() {// NOTE: fmt = yyyyMMdd HHmmss.fff
		int y = 0, M = 0, d = 0, H = 0, m = 0, s = 0, f = 0;
		if (pos < bry_len && bry[pos] == row_dlm) {++pos; ++row_idx; fld_idx = 0;} fld_bgn = pos;
		y += (bry[fld_bgn +  0] - Byte_ascii.Num_0) * 1000;
		y += (bry[fld_bgn +  1] - Byte_ascii.Num_0) *  100;
		y += (bry[fld_bgn +  2] - Byte_ascii.Num_0) *   10;
		y += (bry[fld_bgn +  3] - Byte_ascii.Num_0);
		M += (bry[fld_bgn +  4] - Byte_ascii.Num_0) *   10;
		M += (bry[fld_bgn +  5] - Byte_ascii.Num_0);
		d += (bry[fld_bgn +  6] - Byte_ascii.Num_0) *   10;
		d += (bry[fld_bgn +  7] - Byte_ascii.Num_0);
		H += (bry[fld_bgn +  9] - Byte_ascii.Num_0) *   10;
		H += (bry[fld_bgn + 10] - Byte_ascii.Num_0);
		m += (bry[fld_bgn + 11] - Byte_ascii.Num_0) *   10;
		m += (bry[fld_bgn + 12] - Byte_ascii.Num_0);
		s += (bry[fld_bgn + 13] - Byte_ascii.Num_0) *   10;
		s += (bry[fld_bgn + 14] - Byte_ascii.Num_0);
		f += (bry[fld_bgn + 16] - Byte_ascii.Num_0) *  100;
		f += (bry[fld_bgn + 17] - Byte_ascii.Num_0) *   10;
		f += (bry[fld_bgn + 18] - Byte_ascii.Num_0);
		if (bry[fld_bgn + 19] != fld_dlm) throw Err_.new_("csv date is invalid").Add("txt", String_.new_utf8_len_safe_(bry, fld_bgn, 20));
		fld_end = pos + 20;
		pos = fld_end + 1; ++fld_idx;
		return DateAdp_.new_(y, M, d, H, m, s, f);
	}
	public void Move_next_simple() {
		if (pos < bry_len) {
			byte b_cur = bry[pos];
			if (b_cur == row_dlm) {
				fld_bgn = fld_end = pos;
				++pos; ++row_idx;
				fld_idx = 0;
				return;
			}
		}
		fld_bgn = pos; 
		if (fld_bgn == bry_len) {fld_end = bry_len; return;}
		for (int i = fld_bgn; i < bry_len; i++) {
			byte b = bry[i];
			if 	(b == fld_dlm || b == row_dlm) {
				fld_end = i; pos = i + 1; ++fld_idx;	// position after dlm
				return;
			}
		}
		throw Err_.new_("fld_dlm failed").Add("fld_dlm", (char)fld_dlm).Add("bgn", fld_bgn);
	}
	public String Read_str_escape()	{Move_next_escaped(bfr); return String_.new_utf8_(bfr.XtoAryAndClear());}
	public byte[] Read_bry_escape()	{Move_next_escaped(bfr); return bfr.XtoAryAndClear();}
	public void Move_1() {++pos;}
	public void Move_next_escaped() {Move_next_escaped(bfr); bfr.Clear();}
	public int Move_next_simple_fld() {
		Move_next_simple();
		return fld_end;
	}
	public int Move_next_escaped(ByteAryBfr trg) {
		//if (pos < bry_len && bry[pos] == row_dlm) {++pos; ++row_idx; fld_idx = 0;}	// REMOVE:20120919: this will fail for empty fields at end of line; EX: "a|\n"; intent was probably to auto-advance to new row, but this intent should be explicit
		fld_bgn = pos;
		boolean quote_on = false;
		for (int i = fld_bgn; i < bry_len; i++) {
			byte b = bry[i];
			if 		((b == fld_dlm || b == row_dlm) && !quote_on) {
				fld_end = i; pos = i + 1; ++fld_idx;	// position after dlm
				return pos;
			}
			else if (b == escape_dlm) {	
				++i;
//					if (i == bry_len) throw Err_.new_("escape char at end of String");
				b = bry[i];
				byte escape_val = decode_regy[b];
				if (escape_val == Byte_ascii.Nil)	{trg.Add_byte(escape_dlm).Add_byte(b);} //throw Err_.new_fmt_("unknown escape key: key={0}", bry[i]);
				else								trg.Add_byte(escape_val);
			}
			else if (b == Byte_ascii.Nil) {
				trg.Add(Bry_nil);
			}
			else if (b == quote_dlm) {
				quote_on = !quote_on;
			}
			else
				trg.Add_byte(b);
		}
		return -1;
	}	private ByteAryBfr bfr = ByteAryBfr.new_(); static final byte[] Bry_nil = ByteAry_.new_ascii_("\\0");
	public Gfo_fld_rdr Ctor_xdat() {return (Gfo_fld_rdr)super.Ctor_xdat_base();}
	public Gfo_fld_rdr Ctor_sql()  {return (Gfo_fld_rdr)super.Ctor_sql_base();}
	public static Gfo_fld_rdr xowa_()	{return new Gfo_fld_rdr().Ctor_xdat();}
	public static Gfo_fld_rdr sql_()	{return new Gfo_fld_rdr().Ctor_sql();}
}

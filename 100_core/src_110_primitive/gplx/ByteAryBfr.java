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
public class ByteAryBfr {
	public byte[] Bry() {return bry;} private byte[] bry;
	public int Bry_len() {return bry_len;} int bry_len;
	public void Bry_init(byte[] bry, int bry_len) {this.bry = bry; this.bry_len = bry_len;}
	@gplx.Internal protected int Mkr_itm() {return mkr_itm;} int mkr_itm = -1;
	@gplx.Internal protected Bry_bfr_mkr_mgr Mkr_mgr() {return mkr_mgr;} Bry_bfr_mkr_mgr mkr_mgr;
	@gplx.Internal protected ByteAryBfr Mkr_(Bry_bfr_mkr_mgr mkr_mgr, int itm) {this.mkr_mgr = mkr_mgr; this.mkr_itm = itm; return this;} 
	public ByteAryBfr Mkr_rls() {mkr_mgr.Rls(this); return this;}
	void Mkr_clear() {
		if (mkr_mgr != null) mkr_mgr.Rls(this);
		mkr_mgr = null;
		mkr_itm = -1;
	}
	@gplx.Internal protected int Bry_max() {return bry_max;} int bry_max;
	ByteAryBfr Reset_(int v) {reset = v; return this;} int reset;
	public ByteAryBfr Reset_if_gt(int limit) {
		if (bry_max > limit) {
			this.bry_max = limit;
			this.bry = new byte[limit];
		}
		bry_len = 0;
		return this;
	}
	public ByteAryBfr Clear() {bry_len = 0; return this;}
	public ByteAryBfr ClearAndReset() {bry_len = 0; if (reset > 0) Reset_if_gt(reset); return this;}
	public ByteAryBfr Add(byte[] val) {
		int val_len = val.length;
		if (bry_len + val_len > bry_max) Resize((bry_max + val_len) * 2);
		Array_.CopyTo(val, 0, bry, bry_len, val_len);
		bry_len += val_len;
		return this;
	}
	public ByteAryBfr Add_mid(byte[] val, int bgn, int end) {
		int len = end - bgn;
		if (bry_len + len > bry_max) Resize((bry_max + len) * 2);
		Array_.CopyTo(val, bgn, bry, bry_len, len);
		bry_len += len;
		return this;
	}
	public ByteAryBfr Add_bfr(ByteAryBfr src) {
		int len = src.bry_len;
		if (bry_len + len > bry_max) Resize((bry_max + len) * 2);
		Array_.CopyTo(src.bry, 0, bry, bry_len, len);
		bry_len += len;
		return this;
	}
	public ByteAryBfr Add_bfr_and_clear(ByteAryBfr src) {
		Add_bfr(src);
		src.ClearAndReset();
		return this;
	}
	public ByteAryBfr Add_bfr_trim_and_clear(ByteAryBfr src, boolean trim_bgn, boolean trim_end) {return Add_bfr_trim_and_clear(src, trim_bgn, trim_end, ByteAry_.Trim_ary_ws);}
	public ByteAryBfr Add_bfr_trim_and_clear(ByteAryBfr src, boolean trim_bgn, boolean trim_end, byte[] trim_ary) {
		int src_len = src.bry_len;
		if (bry_len + src_len > bry_max) Resize((bry_max + src_len) * 2);
		byte[] src_bry = src.Bry();
		int src_bgn = 0, src_end = src_len;
		boolean all_ws = true;
		if (trim_bgn) {
			for (int i = 0; i < src_len; i++) {
				byte b = src_bry[i];
				if (trim_ary[b & 0xFF] == Byte_ascii.Nil) {
					src_bgn = i;
					i = src_len;
					all_ws = false;
				}
			}
			if (all_ws) return this;
		}
		if (trim_end) {
			for (int i = src_len - 1; i > -1; i--) {
				byte b = src_bry[i];
				if (trim_ary[b & 0xFF] == Byte_ascii.Nil) {
					src_end = i + 1;
					i = -1;
					all_ws = false;
				}
			}
			if (all_ws) return this;
		}
		src_len = src_end - src_bgn;
		Array_.CopyTo(src.bry, src_bgn, bry, bry_len, src_len);
		bry_len += src_len;
		src.Clear();
		return this;
	}

	public ByteAryBfr Add_byte_pipe() {return Add_byte(Byte_ascii.Pipe);}
	public ByteAryBfr Add_byte(byte val) {
		int newPos = bry_len + 1;
		if (newPos > bry_max) Resize(bry_len * 2);
		bry[bry_len] = val;
		bry_len = newPos;
		return this;
	}
	public ByteAryBfr Add_byte_repeat(byte b, int len) {
		if (bry_len + len > bry_max) Resize((bry_max + len) * 2);
		for (int i = 0; i < len; i++)
			bry[i + bry_len] = b;
		bry_len += len;
		return this;
	}
	public ByteAryBfr Add_utf8_int(int val) {
		if (bry_len + 4 > bry_max) Resize((bry_max + 4) * 2);
		int utf8_len = gplx.intl.Utf8_.EncodeChar(val, bry, bry_len);
		bry_len += utf8_len;
		return this;
	}
	public ByteAryBfr Add_byte_space() {return Add_byte(Byte_ascii.Space);}
	public ByteAryBfr Add_byte_nl() {return Add_byte(Byte_ascii.NewLine);}
	public ByteAryBfr Add_int_bool(boolean v) {return Add_int_fixed(v ? 1 : 0, 1);}
	public ByteAryBfr Add_int_variable(int val) {
		int log10 = Int_.Log10(val);
		int slots = val > -1 ? log10 + 1 : log10 * -1 + 2;
		return Add_int(val, log10, slots);
	}
	public ByteAryBfr Add_int_fixed(int val, int digits) {return Add_int(val, Int_.Log10(val), digits);}
	public ByteAryBfr Add_int(int val, int valLog, int arySlots) {
		int aryBgn = bry_len, aryEnd = bry_len + arySlots;
		if (aryEnd > bry_max) Resize((aryEnd) * 2);
		if (val < 0) {
			bry[aryBgn++] = Byte_ascii.Dash;
			val *= -1;		// make positive
			valLog *= -1;	// valLog will be negative; make positive
			arySlots -= 1;	// reduce slot by 1
		}
		if (valLog >= arySlots) {
			val %= Int_.Log10Ary[arySlots];
		}
		for (int i = 0; i < arySlots; i++) {
			int logIdx = arySlots - i - 1;
			int div = logIdx < Int_.Log10AryLen ? Int_.Log10Ary[logIdx] : Int_.MaxValue;
			bry[aryBgn + i] = (byte)((val / div) + 48);
			val %= div;
		}
		bry_len = aryEnd;
		return this;
	}
	public ByteAryBfr Add_long_variable(long v) {int digitCount = Long_.DigitCount(v); return Add_long(v, digitCount, digitCount);}
	public ByteAryBfr Add_long_fixed(long val, int digits) {return Add_long(val, Long_.DigitCount(val), digits);}
	protected ByteAryBfr Add_long(long val, int digitCount, int arySlots) {
		int aryBgn = bry_len, aryEnd = bry_len + arySlots;
		if (aryEnd > bry_max) Resize((aryEnd) * 2);
		if (val < 0) {
			bry[aryBgn++] = Byte_ascii.Dash;
			val *= -1;		// make positive
			arySlots -= 1;	// reduce slot by 1
		}
		if (digitCount >= arySlots) {
			val %= Long_.Log10Ary[arySlots];
		}
		for (int i = 0; i < arySlots; i++) {
			int logIdx = arySlots - i - 1;
			long div = logIdx < Long_.Log10Ary_len ? Long_.Log10Ary[logIdx] : Long_.MaxValue;
			bry[aryBgn + i] = (byte)((val / div) + 48);
			val %= div;
		}
		bry_len = aryEnd;
		return this;
	}
	public ByteAryBfr Add_str(String v) {return Add(ByteAry_.new_utf8_(v));}
	public ByteAryBfr Add_float(float f) {Add_str(Float_.XtoStr(f)); return this;}
	public ByteAryBfr Add_double(double v) {Add_str(Double_.XtoStr(v)); return this;}
	public ByteAryBfr Add_dte(DateAdp val) {return Add_dte_segs(val.Year(), val.Month(),val.Day(), val.Hour(), val.Minute(), val.Second(), val.Frac());}
	public ByteAryBfr Add_dte_segs(int y, int M, int d, int H, int m, int s, int f) {		// yyyyMMdd HHmmss.fff
		if (bry_len + 19      > bry_max) Resize((bry_len + 19) * 2);
		bry[bry_len +  0] = (byte)((y / 1000) + ByteAry_.Ascii_zero); y %= 1000;
		bry[bry_len +  1] = (byte)((y /  100) + ByteAry_.Ascii_zero); y %=  100;
		bry[bry_len +  2] = (byte)((y /   10) + ByteAry_.Ascii_zero); y %=   10;
		bry[bry_len +  3] = (byte)( y		 + ByteAry_.Ascii_zero);
		bry[bry_len +  4] = (byte)((M /   10) + ByteAry_.Ascii_zero); M %=  10;
		bry[bry_len +  5] = (byte)( M		 + ByteAry_.Ascii_zero);
		bry[bry_len +  6] = (byte)((d /   10) + ByteAry_.Ascii_zero); d %=  10;
		bry[bry_len +  7] = (byte)( d		 + ByteAry_.Ascii_zero);
		bry[bry_len +  8] = Byte_ascii.Space;
		bry[bry_len +  9] = (byte)((H /   10) + ByteAry_.Ascii_zero); H %=  10;
		bry[bry_len + 10] = (byte)( H		 + ByteAry_.Ascii_zero);
		bry[bry_len + 11] = (byte)((m /   10) + ByteAry_.Ascii_zero); m %=  10;
		bry[bry_len + 12] = (byte)( m		 + ByteAry_.Ascii_zero);
		bry[bry_len + 13] = (byte)((s /   10) + ByteAry_.Ascii_zero); s %=  10;
		bry[bry_len + 14] = (byte)( s		 + ByteAry_.Ascii_zero);
		bry[bry_len + 15] = Byte_ascii.Dot;
		bry[bry_len + 16] = (byte)((f /  100) + ByteAry_.Ascii_zero); f %= 100;
		bry[bry_len + 17] = (byte)((f /   10) + ByteAry_.Ascii_zero); f %=  10;
		bry[bry_len + 18] = (byte)( f		 + ByteAry_.Ascii_zero);
		bry_len += 19;
		return this;
	}
	public ByteAryBfr Add_dte_utc(int y, int M, int d, int H, int m, int s, int f) {	// yyyy-MM-ddTHH:mm:ssZ
		if (bry_len + 20      > bry_max) Resize((bry_len + 20) * 2);
		bry[bry_len +  0] = (byte)((y / 1000) + ByteAry_.Ascii_zero); y %= 1000;
		bry[bry_len +  1] = (byte)((y /  100) + ByteAry_.Ascii_zero); y %=  100;
		bry[bry_len +  2] = (byte)((y /   10) + ByteAry_.Ascii_zero); y %=   10;
		bry[bry_len +  3] = (byte)( y		 + ByteAry_.Ascii_zero);
		bry[bry_len +  4] = Byte_ascii.Dash;
		bry[bry_len +  5] = (byte)((M /   10) + ByteAry_.Ascii_zero); M %=  10;
		bry[bry_len +  6] = (byte)( M		 + ByteAry_.Ascii_zero);
		bry[bry_len +  7] = Byte_ascii.Dash;
		bry[bry_len +  8] = (byte)((d /   10) + ByteAry_.Ascii_zero); d %=  10;
		bry[bry_len +  9] = (byte)( d		 + ByteAry_.Ascii_zero);
		bry[bry_len + 10] = Byte_ascii.Ltr_T;
		bry[bry_len + 11] = (byte)((H /   10) + ByteAry_.Ascii_zero); H %=  10;
		bry[bry_len + 12] = (byte)( H		 + ByteAry_.Ascii_zero);
		bry[bry_len + 13] = Byte_ascii.Colon;
		bry[bry_len + 14] = (byte)((m /   10) + ByteAry_.Ascii_zero); m %=  10;
		bry[bry_len + 15] = (byte)( m		 + ByteAry_.Ascii_zero);
		bry[bry_len + 16] = Byte_ascii.Colon;
		bry[bry_len + 17] = (byte)((s /   10) + ByteAry_.Ascii_zero); s %=  10;
		bry[bry_len + 18] = (byte)( s		 + ByteAry_.Ascii_zero);
		bry[bry_len + 19] = Byte_ascii.Ltr_Z;
		bry_len += 20;
		return this;
	}
	public ByteAryBfr Add_if_not_last(byte b) {
		if (bry_len == 0 || (bry_len > 0 && bry[bry_len - 1] == b)) return this;
		this.Add_byte(b);
		return this;
	}
	public ByteAryBfr Add_swap_ws(byte[] src) {return Add_swap_ws(src, 0, src.length);}
	public ByteAryBfr Add_swap_ws(byte[] src, int bgn, int end) {
		int len = end - bgn;
		if (bry_len + (len * 2) > bry_max) Resize((bry_max + (len * 2)) * 2);		
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.NewLine: 	bry[bry_len] = Byte_ascii.Backslash; bry[bry_len + 1] = Byte_ascii.Ltr_n;		bry_len += 2; break;
				case Byte_ascii.Tab: 		bry[bry_len] = Byte_ascii.Backslash; bry[bry_len + 1] = Byte_ascii.Ltr_t;		bry_len += 2; break;
				case Byte_ascii.Backslash: 	bry[bry_len] = Byte_ascii.Backslash; bry[bry_len + 1] = Byte_ascii.Backslash; bry_len += 2; break;
				default:					bry[bry_len] = b; ++bry_len; break;
			}
		}
		return this;
	}
	public ByteAryBfr Add_str_pad_space_bgn(String v, int pad_max) {return Add_str_pad_space(v, pad_max, Bool_.N);}
	public ByteAryBfr Add_str_pad_space_end(String v, int pad_max) {return Add_str_pad_space(v, pad_max, Bool_.Y);}
	ByteAryBfr Add_str_pad_space(String v, int pad_max, boolean pad_end) {
		byte[] v_bry = ByteAry_.new_utf8_(v); 
		if (pad_end)	Add(v_bry);
		int pad_len = pad_max - v_bry.length;
		if (pad_len > 0) 
			Add_byte_repeat(Byte_ascii.Space, pad_len);
		if (!pad_end)	Add(v_bry);
		return this;
	}

	public ByteAryBfr Add_obj(Object o) {
		if (o == null) return this;	// treat null as empty String;
		Class<?> o_type = o.getClass();
		if		(o_type == byte[].class)          Add((byte[])o);
		else if	(o_type == Integer.class)         Add_int_variable(Int_.cast_(o));    
		else if	(o_type == Byte.class)            Add_byte(Byte_.cast_(o));           
		else if	(o_type == Long.class)            Add_long_variable(Long_.cast_(o));  
		else if	(o_type == String.class)          Add_str((String)o);
		else if	(o_type == ByteAryBfr.class)      Add_bfr((ByteAryBfr)o);
		else if	(o_type == DateAdp.class)         Add_dte((DateAdp)o);
		else if	(o_type == Io_url.class)			Add(((Io_url)o).RawBry());
		else if	(o_type == boolean.class)			Add_yn(Bool_.cast_(o));
		else if	(o_type == Double.class)			Add_double(Double_.cast_(o));		
		else if	(o_type == Float.class)			Add_float(Float_.cast_(o));			
		else										((ByteAryFmtrArg)o).XferAry(this, 0);
		return this;
	}
	public ByteAryBfr Add_yn(boolean v) {Add_byte(v ? Byte_ascii.Ltr_y : Byte_ascii.Ltr_n); return this;}
	public ByteAryBfr Add_base85_len_5(int v) {return Add_base85(v, 5);}
	public ByteAryBfr Add_base85(int v, int pad)	{
		int new_len = bry_len + pad;
		if (new_len > bry_max) Resize((new_len) * 2);
		Base85_utl.XtoStrByAry(v, bry, bry_len, pad);
		bry_len = new_len;
		return this;
	}
	public boolean Match_end_byt(byte b)		{return bry_len == 0 ? false : bry[bry_len - 1] == b;}
	public boolean Match_end_byt_nl_or_bos()	{return bry_len == 0 ? true : bry[bry_len - 1] == Byte_ascii.NewLine;}
	public boolean Match_end_ary(byte[] ary)	{return ByteAry_.Match(bry, bry_len - ary.length, bry_len, ary);}
	public ByteAryBfr InsertAt_str(int i, String s) {
		byte[] insert = ByteAry_.new_utf8_(s);
		int insertLen = insert.length;
		int trgMax = bry_max + insertLen;
		byte[] trg = new byte[trgMax];
		Array_.CopyTo(insert, trg, 0);
		Array_.CopyTo(bry, 0, trg, insertLen, bry_len);
		bry = trg;
		bry_len = bry_len + insertLen;
		bry_max = trgMax;
		return this;
	}
	public ByteAryBfr InsertAt(int i, byte b) {
		int insertLen = 1;
		int trgMax = bry_max + insertLen;
		byte[] trg = new byte[trgMax];
		trg[0] = b;
		Array_.CopyTo(bry, 0, trg, insertLen, bry_len);
		bry = trg;
		bry_len = bry_len + insertLen;
		bry_max = trgMax;
		return this;
	}
	public ByteAryBfr Del_by_1() {
		bry_len -= 1; bry[bry_len] = 0; return this;
	}
	public ByteAryBfr Del_by(int count) {
		int new_len = bry_len - count;
		if (new_len > -1) bry_len = new_len;
		return this;
	}
	public byte[] XtoAry() {return bry_len == 0 ? ByteAry_.Empty : ByteAry_.Mid(bry, 0, bry_len);}
	public byte[] XtoAryAndReset(int v) {
		byte[] rv = XtoAry();
		this.Clear().Reset_if_gt(v);
		return rv;
	}
	public byte[] XtoAryAndClearAndTrim() {return XtoAryAndClearAndTrim(true, true, ByteAry_.Trim_ary_ws);}
	public byte[] XtoAryAndClearAndTrim(boolean trim_bgn, boolean trim_end, byte[] trim_bry) {
		byte[] rv = ByteAry_.Trim(bry, 0, bry_len, trim_bgn, trim_end, trim_bry);
		this.Clear();
		return rv;
	}
	public byte[] XtoAryAndClear() {
		byte[] rv = XtoAry();
		this.Clear();
		if (reset > 0) Reset_if_gt(reset);
		return rv;
	}
	public String XtoStr()							{return String_.new_utf8_(XtoAry());}
	public String XtoStrByPos(int bgn, int end)		{return String_.new_utf8_(XtoAry(), bgn, end);}
	public String XtoStrAndClear()					{return String_.new_utf8_(XtoAryAndClear());}
	public String XtoStrAndReset(int v)				{return String_.new_utf8_(XtoAryAndReset(v));}
	public int XtoIntAndClear(int or) {int rv = XtoInt(or); this.Clear(); return rv;}
	public int XtoInt(int or) {
		switch (bry_len) {
			case 0: return or;
			case 1: return bry[0] - Byte_ascii.Num_0;
			default:
				int rv = 0, mult = 1;
				for (int i = bry_len - 1; i > -1; i--) {
					rv += (bry[i] - Byte_ascii.Num_0 ) * mult;
					mult *= 10;
				}
				return rv;
		}
	}
	public void Rls() {
		bry = null;
		Mkr_clear();
	}
	@Override public int hashCode() {return ByteAryRef.CalcHashCode(bry, 0, bry_len);}
	@Override public boolean equals(Object obj) {return obj == null ? false : ByteAry_.Match(bry, 0, bry_len, ((ByteAryRef)obj).Val());}	// NOTE: strange, but null check needed; throws null error; EX.WP: File:Eug�ne Delacroix - La libert� guidant le peuple.jpg
	public void Resize(int newSize) {
		bry_max = newSize;
		bry = ByteAry_.Resize(bry, 0, newSize);
	}
        public static ByteAryBfr new_()			{return new ByteAryBfr(16);}
        public static ByteAryBfr reset_(int v)	{return new ByteAryBfr(v).Reset_(v);}
	public ByteAryBfr() {}
	public ByteAryBfr(int bry_max) {
		this.bry_max = bry_max;
		this.bry = new byte[bry_max];
	}
}

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
public class Long_ {
	public static final String Cls_val_name = "long";
	public static final Class<?> Cls_ref_type = Long.class; 
	public static final long
	  MinValue	= Long.MIN_VALUE		
	, MaxValue	= Long.MAX_VALUE		
	;
	public static final int Log10Ary_len = 21;
	public static long[] Log10Ary = new long[] 
  			{ 1, 10, 100, 1000, 10000
		, 100000, 1000000, 10000000, 100000000, 1000000000
  			, Long_.Pow(10, 10), Long_.Pow(10, 11), Long_.Pow(10, 12), Long_.Pow(10, 13), Long_.Pow(10, 14)
  			, Long_.Pow(10, 15), Long_.Pow(10, 16), Long_.Pow(10, 17), Long_.Pow(10, 18), Long_.Pow(10, 19)
  			, Long_.MaxValue
  			};
	public static long parse_(String raw)			{try {return Long.parseLong(raw);} catch(Exception e) {throw Err_.parse_type_exc_(e, long.class, raw);}} 
	public static long cast_(Object obj) {try {return (Long)obj;} catch(Exception e) {throw Err_.type_mismatch_exc_(e, long.class, obj);}}
	public static long coerce_(Object v) {
		try {String s = String_.as_(v); return s == null ? Long_.cast_(v) : Long_.parse_(s);}
		catch (Exception e) {throw Err_.cast_(e, long.class, v);}
	}
	public static long Xby_int(int v) {return (long)v;}
	public static String Xto_str(long v) {return Long.toString(v);}	
	public static String Xto_str_PadBgn(long v, int reqdPlaces) {return String_.Pad(Xto_str(v), reqdPlaces, "0", true);}	// ex: 1, 3 returns 001
	public static long parse_or_(String raw, long or) {
		if (raw == null) return or;
		try {
			int rawLen = String_.Len(raw);
			if (raw == null || rawLen == 0) return or;
			long rv = 0, factor = 1; int tmp = 0;
			for (int i = rawLen; i > 0; i--) {
				tmp = Char_.To_int_or(String_.CharAt(raw, i - 1), Int_.MinValue);
				if (tmp == Int_.MinValue) return or;
				rv += (tmp * factor);
				factor *= 10;
			}
			return rv;
		} catch (Exception e) {Err_.Noop(e); return or;}
	}
	public static int Compare(long lhs, long rhs) {
		if		(lhs == rhs) 	return CompareAble_.Same;
		else if (lhs < rhs)		return CompareAble_.Less;
		else 					return CompareAble_.More;
	}
 		public static int FindIdx(long[] ary, long find_val) {
		int ary_len = ary.length;
		int adj = 1;
		int prv_pos = 0;
		int prv_len = ary_len;
		int cur_len = 0;
		int cur_idx = 0;
		long cur_val = 0;
		while (true) {
			cur_len = prv_len / 2;
			if (prv_len % 2 == 1) ++cur_len;
			cur_idx = prv_pos + (cur_len * adj);
			if		(cur_idx < 0)			cur_idx = 0;
			else if (cur_idx >= ary_len)	cur_idx = ary_len - 1;
			cur_val = ary[cur_idx];
			if		(find_val <	 cur_val) adj = -1;
			else if (find_val >	 cur_val) adj =	1;
			else if (find_val == cur_val) return cur_idx;
			if (cur_len == 1) {
				if (adj == -1 && cur_idx > 0)
					return --cur_idx;
				return cur_idx;
			}
			prv_len = cur_len;
			prv_pos = cur_idx;
		}
	}		
	public static int DigitCount(long v) {
		int adj = Int_.Base1;
		if (v < 0) {
			if (v == Long_.MinValue) return 19;	// NOTE: Long_.MinValue * -1 = Long_.MinValue
			v *= -1;
			++adj;
		}
		return FindIdx(Log10Ary, v) + adj;
	}
	public static long Pow(int val, int exp) {
		long rv = val;
		for (int i = 1; i < exp; i++)
			rv *= val;
		return rv;
	}
	public static long Int_merge(int hi, int lo)	{return (long)hi << 32 | (lo & 0xFFFFFFFFL);}
	public static int  Int_split_lo(long v)			{return (int)(v);}
	public static int  Int_split_hi(long v)			{return (int)(v >> 32);}
}
/* alternate for Int_merge; does not work in java
		public static long MergeInts(int lo, int hi)	{return (uint)(hi << 32) | (lo & 0xffffffff);}
		public static int  SplitLo(long v)				{return (int)(((ulong)v & 0x00000000ffffffff));}
		public static int  SplitHi(long v)				{return (int)(((ulong)v & 0xffffffff00000000)) >> 32;}
*/

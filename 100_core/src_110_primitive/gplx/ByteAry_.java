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
import gplx.ios.*;
public class ByteAry_ {
	public static final int NotFound = -1;
	public static final byte[] Empty = new byte[0];
	public static final byte[][] Ary_empty = new byte[0][];
	public static byte[] bytes_(byte... ary) {return ary;}
	public static byte[] ints_ (int... ary) {
		int len = ary.length;
		byte[] rv = new byte[len];
		for (int i = 0; i < len; i++)
			rv[i] = (byte)ary[i];
		return rv;
	}
	public static byte[] new_utf8_(String s)	{return gplx.texts.Encoding_.XtoByteAry(s);}
	public static byte[] new_ascii_(String s)	{return gplx.texts.Encoding_.XtoByteAryAscii(s);}
	public static byte[] new_ascii_safe_null_(String s)	{return s == null ? null : gplx.texts.Encoding_.XtoByteAryAscii(s);}
	public static byte[] Coalesce(byte[] orig, byte[] val_if_not_blank) {return ByteAry_.Len_eq_0(val_if_not_blank) ? orig : val_if_not_blank;}
	public static int While_fwd(byte[] src, byte while_byte, int bgn, int end) {
		for (int i = bgn; i < end; i++)
			if (src[i] != while_byte) return i;
		return end;
	}
	public static byte[][] Ary_add(byte[][] lhs, byte[][] rhs) {
		int lhs_len = lhs.length, rhs_len = rhs.length;
		if		(lhs_len == 0) return rhs;
		else if	(rhs_len == 0) return lhs;
		else {
			byte[][] rv = new byte[lhs_len + rhs_len][];
			for (int i = 0; i < lhs_len; i++)
				rv[i]			= lhs[i];
			for (int i = 0; i < rhs_len; i++)
				rv[i + lhs_len] = rhs[i];
			return rv;
		}
	}
	public static byte[][] Ary(byte[]... ary) {return ary;}
	public static byte[][] Ary(String... ary) {
		int ary_len = ary.length;
		byte[][] rv = new byte[ary_len][];
		for (int i = 0; i < ary_len; i++) {
			String itm = ary[i];
			rv[i] = itm == null ? null : ByteAry_.new_utf8_(itm);
		}
		return rv;
	}
	public static byte[][] Ary_obj(Object... ary) {
		if (ary == null) return ByteAry_.Ary_empty;
		int ary_len = ary.length;
		byte[][] rv = new byte[ary_len][];
		for (int i = 0; i < ary_len; i++) {
			Object itm = ary[i];
			rv[i] = itm == null ? null : ByteAry_.new_utf8_(Object_.XtoStr_OrEmpty(itm));
		}
		return rv;
	}
	public static byte[] Repeat_space(int len) {return Repeat(Byte_ascii.Space, len);}
	public static byte[] Repeat(byte b, int len) {
		byte[] rv = new byte[len];
		for (int i = 0; i < len; i++)
			rv[i] = b;
		return rv;
	}
	public static byte[] Copy(byte[] src) {
		int src_len = src.length;
		byte[] trg = new byte[src_len];
		for (int i = 0; i < src_len; i++)
			trg[i] = src[i];
		return trg;
	}
	public static void Copy_by_pos(byte[] src, int src_bgn, int src_end, byte[] trg, int trg_bgn) {
		int trg_adj = trg_bgn - src_bgn;
		for (int i = src_bgn; i < src_end; i++)
			trg[i + trg_adj] = src[i];
	}
	public static void Copy_by_len(byte[] src, int src_bgn, int src_len, byte[] trg, int trg_bgn) {
		for (int i = 0; i < src_len; i++)
			trg[i + trg_bgn] = src[i + src_bgn];
	}
	public static byte[][] XtoByteAryAry(String... strAry) {
		int strAryLen = strAry.length;
		byte[][] rv = new byte[strAryLen][];
		for (int i = 0; i < strAryLen; i++)
			rv[i] = ByteAry_.new_utf8_(strAry[i]);
		return rv;
	}
	public static byte[] Xto_str_lower(byte[] src, int bgn, int end) {
		int len = end - bgn;
		byte[] rv = new byte[len];
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
  					case Byte_ascii.Ltr_A: case Byte_ascii.Ltr_B: case Byte_ascii.Ltr_C: case Byte_ascii.Ltr_D: case Byte_ascii.Ltr_E:
  					case Byte_ascii.Ltr_F: case Byte_ascii.Ltr_G: case Byte_ascii.Ltr_H: case Byte_ascii.Ltr_I: case Byte_ascii.Ltr_J:
  					case Byte_ascii.Ltr_K: case Byte_ascii.Ltr_L: case Byte_ascii.Ltr_M: case Byte_ascii.Ltr_N: case Byte_ascii.Ltr_O:
  					case Byte_ascii.Ltr_P: case Byte_ascii.Ltr_Q: case Byte_ascii.Ltr_R: case Byte_ascii.Ltr_S: case Byte_ascii.Ltr_T:
  					case Byte_ascii.Ltr_U: case Byte_ascii.Ltr_V: case Byte_ascii.Ltr_W: case Byte_ascii.Ltr_X: case Byte_ascii.Ltr_Y: case Byte_ascii.Ltr_Z:
					b += 32;
					break;
			}
			rv[i - bgn] = b;
		}
		return rv;
	}
	public static byte[] Replace_one(byte[] src, byte[] find, byte[] repl) {
		int src_len = src.length;
		int findPos = Find(src, find, 0, src_len, true); if (findPos == ByteAry_.NotFound) return src;
		int findLen = find.length, replLen = repl.length;
		int rvLen = src_len + replLen - findLen;
		byte[] rv = new byte[rvLen];
		Copy_by_len(src	, 0					, findPos						, rv, 0		);
		Copy_by_len(repl, 0					, replLen						, rv, findPos	);
		Copy_by_len(src	, findPos + findLen	, src_len - findPos - findLen	, rv, findPos + replLen);
		return rv;
	}
	public static void Replace_all_direct(byte[] src, byte find, byte repl) {Replace_all_direct(src, find, repl, 0, src.length);}
	public static void Replace_all_direct(byte[] src, byte find, byte repl, int bgn, int end) {
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			if (b == find) src[i] = repl;			
		}
	}
	public static byte[] Add_w_dlm(byte dlm, byte[]... ary) {
		int ary_len = ary.length;
		if (ary_len == 0) return ByteAry_.Empty;
		int rv_len = ary_len - 1;	// rv will have at least as many dlms as itms - 1	
		for (int i = 0; i < ary_len; i++) {
			byte[] itm = ary[i];
			if (itm != null) rv_len += itm.length;
		}
		int rv_pos = 0;
		byte[] rv = new byte[rv_len];
		for (int i = 0; i < ary_len; i++) {
			byte[] itm = ary[i];
			if (i != 0) rv[rv_pos++] = dlm;
			if (itm == null) continue;
			int itm_len = itm.length;
			for (int j = 0; j < itm_len; j++) {
				rv[rv_pos++] = itm[j]; 
			}
		}
		return rv;
	}
	public static byte[] Add(byte[] ary, byte b) {
		int ary_len = ary.length;
		byte[] rv = new byte[ary_len + 1];
		for (int i = 0; i < ary_len; i++)
			rv[i] = ary[i];
		rv[ary_len] = b;
		return rv;
	}
	public static byte[] Add(byte b, byte[] ary) {
		int ary_len = ary.length + 1;
		byte[] rv = new byte[ary_len];
		for (int i = 1; i < ary_len; i++)
			rv[i] = ary[i - 1];
		rv[0] = b;
		return rv;
	}
	public static byte[] Add(byte[]... all) {
		int all_len = all.length, rv_len = 0;
		for (int i = 0; i < all_len; i++) {
			byte[] cur = all[i]; if (all[i] == null) continue;
			rv_len += cur.length;
		}
		byte[] rv = new byte[rv_len];
		int rv_idx = 0;
		for (int i = 0; i < all_len; i++) {
			byte[] cur = all[i]; if (all[i] == null) continue;
			int cur_len = cur.length;
			for (int j = 0; j < cur_len; j++)
				rv[rv_idx++] = cur[j];
		}
		return rv;
	}
	public static int LastIdx(byte[] src) {return src.length - 1;}
	public static byte[] Mid_by_nearby(byte[] src, int pos, int around) {
		int bgn = pos - around; if (bgn <  0) bgn = 0;
		int src_len = src.length;
		int end = pos + around; if (end > src_len) end = src_len;
		return Mid(src, bgn, end);
	}
	public static byte[] Mid_by_len(byte[] src, int bgn, int len) {return Mid(src, bgn, bgn + len);}
	public static byte[] Mid_by_len_safe(byte[] src, int bgn, int len) {
		if (len > src.length) len = src.length;
		return Mid(src, bgn, bgn + len);
	}
	public static String MidByLenToStr(byte[] src, int bgn, int len) {
		int end = bgn + len; end = Int_.BoundEnd(end, src.length);
		byte[] ary = ByteAry_.Mid(src, bgn, end);
		return String_.new_utf8_(ary);
	}
	public static byte[] Mid(byte[] src, int bgn, int end) {
		try {
			byte[] rv = new byte[end - bgn];
			for (int i = bgn; i < end; i++)
				rv[i - bgn] = src[i];
			return rv;
		}	catch (Exception e) {
			Err err = Err_.new_("").Add("bgn", bgn).Add("end", end);
			if (src != null) err.Add("src", String_.new_utf8_len_safe_(src, bgn, 32));
			if		(src == null)					err.Hdr_("src is null");
			else if (bgn < 0 || bgn > src.length)	err.Hdr_("invalid bgn");
			else if (end < 0 || end > src.length)	err.Hdr_("invalid end");
			else if (end < bgn)						err.Hdr_("end < bgn");
			else									err.Hdr_(Err_.Message_lang(e));
			throw err;
		}
	}
	public static byte[] mask_(int len, byte... itms) {
		byte[] rv = new byte[len];
		int itms_len = itms.length;
		for (int i = 0; i < itms_len; i++) {
			byte itm = itms[i];
			rv[itm & 0xFF] = itm; // PATCH.JAVA:need to convert to unsigned byte
		}
		return rv;
	}
	public static final byte[] Trim_ary_ws = mask_(256, Byte_ascii.Tab, Byte_ascii.NewLine, Byte_ascii.CarriageReturn, Byte_ascii.Space);
	public static byte[] Trim(byte[] src, int bgn, int end) {return Trim(src, bgn, end, true, true, Trim_ary_ws);}
	public static byte[] Trim(byte[] src, int bgn, int end, boolean trim_bgn, boolean trim_end, byte[] trim_ary) {
		int txt_bgn = bgn, txt_end = end;
		boolean all_ws = true;
		if (trim_bgn) {
			for (int i = bgn; i < end; i++) {
				byte b = src[i];
				if (trim_ary[b & 0xFF] == Byte_ascii.Nil) {
					txt_bgn = i;
					i = end;
					all_ws = false;
				}
			}
			if (all_ws) return ByteAry_.Empty;
		}
		if (trim_end) {
			for (int i = end - 1; i > -1; i--) {
				byte b = src[i];
				if (trim_ary[b & 0xFF] == Byte_ascii.Nil) {
					txt_end = i + 1;
					i = -1;
					all_ws = false;
				}
			}
			if (all_ws) return ByteAry_.Empty;
		}
		return ByteAry_.Mid(src, txt_bgn, txt_end);
	}
	public static boolean Has(byte[] src, byte lkp) {
		int len = src.length;
		for (int i = 0; i < len; i++)
			if (src[i] == lkp) return true;
		return false;
	}
	public static boolean HasAtEnd(byte[] src, byte[] lkp) {int src_len = src.length; return HasAtEnd(src, lkp, src_len - lkp.length, src_len);}
	public static boolean HasAtEnd(byte[] src, byte[] lkp, int src_bgn, int src_end) {
		int lkp_len = lkp.length;
		if (src_bgn < 0) return false;
		int pos = src_end - lkp_len; if (pos < src_bgn) return false; // lkp is longer than src
		for (int i = 0; i < lkp_len; i++) {
			if (lkp[i] != src[i + pos]) return false;
		}
		return true;
	}
	public static boolean HasAtBgn(byte[] src, byte[] lkp) {return HasAtBgn(src, lkp, 0, src.length);}
	public static boolean HasAtBgn(byte[] src, byte[] lkp, int src_bgn, int srcEnd) {
		int lkpLen = lkp.length;
		if (lkpLen + src_bgn > srcEnd) return false; // lkp is longer than src
		for (int i = 0; i < lkpLen; i++) {
			if (lkp[i] != src[i + src_bgn]) return false;
		}
		return true;
	}
	public static int Skip_fwd(byte[] src, int src_bgn, byte skip) {
		int src_len = src.length;
		for (int i = src_bgn; i < src_len; i++) {
			byte b = src[i];
			if (b != skip) return i;
		}
		return 0;
	}
	public static int Skip_bwd(byte[] src, int src_bgn, byte skip) {
		for (int i = src_bgn; i > -1; i--) {
			byte b = src[i];
			if (b != skip) return i;
		}
		return src.length;
	}
	public static int Skip_fwd_nl(byte[] src, int src_bgn) {
		int src_len = src.length;
		for (int i = src_bgn; i < src_len; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.NewLine: case Byte_ascii.CarriageReturn:
					break;
				default:
					return i;
			}
		}
		return 0;
	}
	public static int Skip_bwd_nl(byte[] src, int src_bgn) {
		for (int i = src_bgn; i > -1; i--) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.NewLine: case Byte_ascii.CarriageReturn:
					break;
				default:
					return i;
			}
		}
		return src.length;
	}
	public static int FindFwd(byte[] src, byte[] lkp)								{return Find(src, lkp, 0	  , src.length, true);}
	public static int FindFwd(byte[] src, byte[] lkp, int src_bgn)					{return Find(src, lkp, src_bgn, src.length, true);}
	public static int FindFwd(byte[] src, byte[] lkp, int src_bgn, int srcEnd)		{return Find(src, lkp, src_bgn,	   srcEnd, true);}
	public static int FindBwd_last_ws(byte[] src, int src_bgn)						{
		if (src_bgn < 0) return ByteAry_.NotFound;
		int rv = ByteAry_.NotFound;
		for (int i = src_bgn; i > -1; i--) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Space: case Byte_ascii.Tab: case Byte_ascii.NewLine: case Byte_ascii.CarriageReturn:
					rv = i;
					break;
				default:
					i = -1;
					break;
			}
		}
		return rv;
	}
	public static int FindFwd_last_ws(byte[] src, int src_bgn)						{
		int src_len = src.length;
		if (src_bgn >= src_len) return ByteAry_.NotFound;
		int rv = ByteAry_.NotFound;
		for (int i = src_bgn; i < src_len; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Space: case Byte_ascii.Tab: case Byte_ascii.NewLine: case Byte_ascii.CarriageReturn:
					rv = i;
					break;
				default:
					i = -1;
					break;
			}
		}
		return rv;
	}
	public static int FindBwd(byte[] src, byte[] lkp, int src_bgn)					{return FindBwd(src, lkp, src_bgn,          0);}
	public static int FindBwd(byte[] src, byte[] lkp, int src_bgn, int src_end)	{
		if (src_bgn < 0) return ByteAry_.NotFound;
		int bgn = src_bgn;
		int end = src_end - 1;
		int src_len = src.length;
		int lkp_len = lkp.length;
		for (int i = bgn; i > end; i--) {
			if (i + lkp_len > src_len) continue;	// lkp too small for pos; EX: src=abcde; lkp=bcd; pos=4
			boolean match = true;
			for (int j = 0; j < lkp_len; j++) {
				if (lkp[j] != src[i + j]) {
					match = false;
					break;
				} 
			}
			if (match) return i;
		}
		return ByteAry_.NotFound;
	}
	static int Find(byte[] src, byte[] lkp, int src_bgn, int srcEnd, boolean fwd) {
		if (src_bgn < 0 || src.length == 0) return NotFound;
		int dif, lkpLen = lkp.length, lkpBgn, lkpEnd, srcEndChk; //int loops = 0;
		if (fwd)	{if (src_bgn > srcEnd) return ByteAry_.NotFound; 
			dif =  1; lkpBgn = 0;			lkpEnd = lkpLen; srcEndChk = srcEnd - CompareAble_.OffsetCompare;
		}
		else		{if (src_bgn < srcEnd) return ByteAry_.NotFound; 
			dif = -1; lkpBgn = lkpLen - 1;	lkpEnd = -1;     srcEndChk = src.length - CompareAble_.OffsetCompare;}	// srcEndChk needed when going bwd, b/c lkpLen may be > 1
		while (src_bgn != srcEnd) {										// while src is not done;
			int lkpPos = lkpBgn;
			while (lkpPos != lkpEnd) {									// while lkp is not done
				int pos = src_bgn + lkpPos;
				if (	pos > srcEndChk									// outside bounds; occurs when lkpLen > 1
					||	src[pos] != lkp[lkpPos])						// srcByte doesn't match lkpByte 
					break;
				else
					lkpPos += dif;
			}
			if (lkpPos == lkpEnd) return src_bgn;						// lkp matches src; exit
			src_bgn += dif;
		}
		return ByteAry_.NotFound;
	}
	public static int FindFwd(byte[] src, byte lkp)				{return FindFwd(src, lkp, 0, src.length);}
	public static int FindFwd(byte[] src, byte lkp, int bgn)	{return FindFwd(src, lkp, bgn, src.length);}
	public static int FindFwd(byte[] src, byte lkp, int bgn, int len) {
		for (int i = bgn; i < len; i++)
			if (src[i] == lkp) return i;
		return ByteAry_.NotFound;
	}
	public static int FindBwd(byte[] src, byte lkp)				{return FindBwd(src, lkp, src.length - 1, 0);}
	public static int FindBwd(byte[] src, byte lkp, int bgn)	{return FindBwd(src, lkp, bgn, 0);}
	public static int FindBwd(byte[] src, byte lkp, int bgn, int end) {
		for (int i = bgn; i >= end; i--)
			if (src[i] == lkp) return i;
		return ByteAry_.NotFound;
	}
	public static byte[] Resize_manual(byte[] src, int rvLen) {
		byte[] rv = new byte[rvLen];
		int src_len = src.length;
		if (src_len > rvLen) src_len = rvLen; // resizing smaller; only copy as many elements as in rvLen
		for (int i = 0; i < src_len; i++)
			rv[i] = src[i];
		return rv;
	}
	public static byte[] Resize(byte[] src, int trgLen) {return Resize(src, 0, trgLen);}
	public static byte[] Resize(byte[] src, int src_bgn, int trgLen) {
		byte[] trg = new byte[trgLen];
		int src_len = src.length > trgLen ? trgLen : src.length;	// trgLen can either expand or shrink
		Copy_by_len(src, src_bgn, src_len, trg, 0);
		return trg;
	}
	public static boolean Match(byte[] src, byte[] find) {return Match(src, 0, src.length, find, 0, find.length);}
	public static boolean Match(byte[] src, int src_bgn, byte[] find) {return Match(src, src_bgn, src.length, find, 0, find.length);}
	public static boolean Match(byte[] src, int src_bgn, int src_end, byte[] find) {return Match(src, src_bgn, src_end, find, 0, find.length);}
	public static boolean Match(byte[] src, int src_bgn, int src_end, byte[] find, int find_bgn, int find_end) {
		int find_len = find_end - find_bgn;
		if (find_len != src_end - src_bgn) return false;
		if (find_len == 0) return src_end - src_bgn == 0; // "" only matches ""
		for (int i = 0; i < find_len; i++) {
			int pos = src_bgn + i;
			if (pos >= src_end) return false;	// ran out of src; exit; EX: src=ab; find=abc
			if (src[pos] != find[i + find_bgn]) return false;
		}
		return true;
	}
	public static int Compare(byte[] lhs, byte[] rhs) {
		if		(lhs == null)	return CompareAble_.More;
		else if (rhs == null)	return CompareAble_.Less;
		else					return Compare(lhs, 0, lhs.length, rhs, 0, rhs.length);
	}
	public static int Compare(byte[] lhs, int lhs_bgn, int lhs_end, byte[] rhs, int rhs_bgn, int rhs_end) {
		int lhs_len = lhs_end - lhs_bgn, rhs_len = rhs_end - rhs_bgn;
		int min = lhs_len < rhs_len ? lhs_len : rhs_len;
		int rv = CompareAble_.Same;
		for (int i = 0; i < min; i++) {
			rv = (lhs[i + lhs_bgn] & 0xff) - (rhs[i + rhs_bgn] & 0xff);	// PATCH.JAVA:need to convert to unsigned byte
			if (rv != CompareAble_.Same) return rv > CompareAble_.Same ? CompareAble_.More : CompareAble_.Less; // NOTE: changed from if (rv != CompareAble_.Same) return rv; DATE:2013-04-25
		}
		return Int_.Compare(lhs_len, rhs_len);	// lhs and rhs share same beginning bytes; return len comparisons
	}
	public static boolean Len_gt_0(byte[] v) {return v != null && v.length > 0;}
	public static boolean Len_eq_0(byte[] v) {return v == null || v.length == 0;}
	public static void Set(byte[] src, int bgn, int end, byte[] repl) {
		int repl_len = repl.length;
		for (int i = 0; i < repl_len; i++)
			src[i + bgn] = repl[i];
	}
	public static boolean Eq(byte[] lhs, byte[] rhs) {
		if		(lhs == null && rhs == null) return true;
		else if (lhs == null || rhs == null) return false;
		int lhs_len = lhs.length;
		if (lhs_len != rhs.length) return false;
		for (int i = 0; i < lhs_len; i++)	// NOTE: lhs_len == rhsLen
			if (lhs[i] != rhs[i]) return false;
		return true;
	}
	public static boolean Eq(byte[] lhs, byte[] rhs, int rhs_bgn, int rhs_end) {
		if		(lhs == null && rhs == null) return true;
		else if (lhs == null || rhs == null) return false;
		int lhs_len = lhs.length;
		int rhs_len = rhs_end - rhs_bgn;
		if (lhs_len != rhs_len) return false;
		for (int i = 0; i < lhs_len; i++)
			if (lhs[i] != rhs[i + rhs_bgn]) return false;
		return true;
	}
	public static byte[] XtoStrBytesByInt(int val, int padLen) {return XtoStrBytesByInt(val, null, 0, padLen);}
	public static byte[] XtoStrBytesByInt(int val, byte[] ary, int aryPos, int padLen) {
		int neg = 0;
		if	(val < 0) {
			val *= -1;
			neg = 1;
		}
		int digits = val == 0 ? 0 : Math_.Log10(val);
		digits += 1;						// digits = log + 1; EX: Log(1-9) = 0, Log(10-99) = 1
		int aryLen = digits + neg, aryBgn = aryPos, pad = 0;
		if (aryLen < padLen) {			// padding specified
			pad = padLen - aryLen;
			aryLen = padLen;
		}
		if (ary == null) ary = new byte[aryLen];
		long factor = 1;					// factor needs to be long to handle 1 billion (for which factor would be 10 billion)
		for (int i = 0; i < digits; i++)	// calc maxFactor
			factor *= 10;
		if (neg == 1) ary[0] = Byte_NegSign;

		for (int i = 0; i < pad; i++)		// fill ary with pad
			ary[i + aryBgn] = XtoStrByte(0);
		aryBgn += pad;						// advance aryBgn by pad
		for (int i = neg; i < aryLen - pad; i++) {
			int denominator = (int)(factor / 10); // cache denominator to check for divide by 0
			int digit = denominator == 0 ? 0 : (int)((val % factor) / denominator);
			ary[aryBgn + i] = XtoStrByte(digit);
			factor /= 10;
		}
		return ary;
	}
	public static int XtoIntOr(byte[] ary, int or) {return XtoIntByPos(ary, 0, ary.length, or);}
	public static int XtoIntByPos_lax(byte[] ary, int bgn, int end, int or) {
		if (end == bgn) return or;	// null-len
		int end_num = end;
		for (int i = bgn; i < end; i++) {
			byte b = ary[i];
			switch (b) {
				case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
				case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
					break;
				case Byte_ascii.Dash:
					if (i != bgn) {
						end_num = i;
						i = end;
					}
					break;
				default:
					end_num = i;
					i = end;
					break;
			}
		}
		return XtoIntByPos(ary, bgn, end_num, or);
	}
	public static byte Xto_byte_by_int(byte[] ary, int bgn, int end, byte or) {return (byte)XtoIntByPos(ary, bgn, end, or);}
	public static int XtoInt(byte[] ary) {return XtoIntByPos(ary, 0, ary.length, -1);}
	public static int XtoIntByPos(byte[] ary, int bgn, int end, int or) {
		if (end == bgn) return or;	// null-len
		int rv = 0, multiple = 1;
		for (int i = end - 1; i >= bgn; i--) {	// -1 b/c end will always be next char; EX: {{{1}}}; bgn = 3, end = 4
			byte b = ary[i];
			switch (b) {
				case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
				case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
					rv += multiple * (b - Byte_ascii.Num_0);
					multiple *= 10;
					break;
				case Byte_ascii.Dash:
					return i == bgn ? rv * -1 : or;
				default: return or;
			}
		}
		return rv;
	}
	public static float XtoFloatByPos(byte[] ary, int bgn, int end) {return Float_.parse_(String_.new_utf8_(ary, bgn, end));}
	public static double XtoDoubleByPos(byte[] ary, int bgn, int end) {return Double_.parse_(String_.new_utf8_(ary, bgn, end));}
	public static DecimalAdp XtoDecimalByPos(byte[] ary, int bgn, int end) {return DecimalAdp_.parse_(String_.new_utf8_(ary, bgn, end));}
	public static final byte Dlm_fld = (byte)'|', Dlm_row = (byte)'\n', Dlm_quote = (byte)'"', Dlm_null = 0, Ascii_zero = 48;
	public static final String Fmt_csvDte = "yyyyMMdd HHmmss.fff";
	public static DateAdp ReadCsvDte(byte[] ary, IntRef posRef, byte lkp) {// ASSUME: fmt = yyyyMMdd HHmmss.fff
		int y = 0, M = 0, d = 0, H = 0, m = 0, s = 0, f = 0;
		int bgn = posRef.Val();
		y += (ary[bgn +  0] - Ascii_zero) * 1000;
		y += (ary[bgn +  1] - Ascii_zero) *  100;
		y += (ary[bgn +  2] - Ascii_zero) *   10;
		y += (ary[bgn +  3] - Ascii_zero);
		M += (ary[bgn +  4] - Ascii_zero) *   10;
		M += (ary[bgn +  5] - Ascii_zero);
		d += (ary[bgn +  6] - Ascii_zero) *   10;
		d += (ary[bgn +  7] - Ascii_zero);
		H += (ary[bgn +  9] - Ascii_zero) *   10;
		H += (ary[bgn + 10] - Ascii_zero);
		m += (ary[bgn + 11] - Ascii_zero) *   10;
		m += (ary[bgn + 12] - Ascii_zero);
		s += (ary[bgn + 13] - Ascii_zero) *   10;
		s += (ary[bgn + 14] - Ascii_zero);
		f += (ary[bgn + 16] - Ascii_zero) *  100;
		f += (ary[bgn + 17] - Ascii_zero) *   10;
		f += (ary[bgn + 18] - Ascii_zero);
		if (ary[bgn + 19] != lkp) throw Err_.new_("csv date is invalid").Add("txt", String_.new_utf8_len_safe_(ary, bgn, 20));
		posRef.Val_add(19 + 1); // +1=lkp.len
		return DateAdp_.new_(y, M, d, H, m, s, f);
	}
	public static String ReadCsvStr(byte[] ary, IntRef posRef, byte lkp)				{return String_.new_utf8_(ReadCsvBry(ary, posRef, lkp, true));}
	public static byte[] ReadCsvBry(byte[] ary, IntRef posRef, byte lkp)				{return ReadCsvBry(ary, posRef, lkp, true);}
	public static byte[] ReadCsvBry(byte[] ary, IntRef posRef, byte lkp, boolean make)	{
		int bgn = posRef.Val(), aryLen = ary.length;
		ByteAryBfr bb = null;
		if (aryLen > 0 && ary[0] == Dlm_quote) {
			int pos = bgn + 1;	// +1 to skip quote
			if (make) bb = new ByteAryBfr(16);
			while (true) {
				if (pos == aryLen) throw Err_.new_("endOfAry reached, but no quote found").Add("txt", String_.new_utf8_len_safe_(ary, bgn, pos));
				byte b = ary[pos];
				if (b == Dlm_quote) {                            
					if (pos == aryLen - 1) throw Err_.new_("endOfAry reached, quote found but lkp not").Add("txt", String_.new_utf8_len_safe_(ary, bgn, pos));
					byte next = ary[pos + 1];
					if		(next == Dlm_quote) {	// byte followed by quote
						if (make) bb.Add_byte(b);
						pos += 2;
					}
					else if (next == lkp) {
						posRef.Val_(pos + 2);	// 1=endQuote;1=lkp;
						return make ? bb.XtoAry() : ByteAry_.Empty;
					}
					else throw Err_.new_("quote found, but not doubled").Add("txt", String_.new_utf8_len_safe_(ary, bgn, pos + 1));
				}
				else {
					if (make) bb.Add_byte(b);
					pos++;
				}
			}				
		}
		else {
			for (int i = bgn; i < aryLen; i++) {
				if (ary[i] == lkp) {
					posRef.Val_(i + 1);	// +1 = lkp.Len
					return make ? ByteAry_.Mid(ary, bgn, i) : ByteAry_.Empty;
				}
			}
			throw Err_.new_("lkp failed").Add("lkp", (char)lkp).Add("txt", String_.new_utf8_len_safe_(ary, bgn, aryLen));
		}
	}
	public static int ReadCsvInt(byte[] ary, IntRef posRef, byte lkp) {
		int bgn = posRef.Val();
		int pos = ByteAry_.FindFwd(ary, lkp, bgn, ary.length);
		if (pos == ByteAry_.NotFound) throw Err_.new_("lkp failed").Add("lkp", (char)lkp).Add("bgn", bgn);
		int rv = ByteAry_.XtoIntByPos(ary, posRef.Val(), pos, -1);
		posRef.Val_(pos + 1);	// +1 = lkp.Len
		return rv;
	}
	public static double ReadCsvDouble(byte[] ary, IntRef posRef, byte lkp) {
		int bgn = posRef.Val();
		int pos = ByteAry_.FindFwd(ary, lkp, bgn, ary.length);
		if (pos == ByteAry_.NotFound) throw Err_.new_("lkp failed").Add("lkp", (char)lkp).Add("bgn", bgn);
		double rv = ByteAry_.XtoDoubleByPos(ary, posRef.Val(), pos);
		posRef.Val_(pos + 1);	// +1 = lkp.Len
		return rv;
	}
	public static void ReadCsvNext(byte[] ary, IntRef posRef, byte lkp) {
		int bgn = posRef.Val();
		int pos = ByteAry_.FindFwd(ary, lkp, bgn, ary.length);
		posRef.Val_(pos + 1);	// +1 = lkp.Len
	}
	public static byte Byte_NegSign = (byte)'-';
	public static int XtoIntBy4Bytes(byte[] v) {
		int v_len = v.length;
		int mod = 8 * (v_len - 1);
		int rv = 0;
		for (int i = 0; i < v_len; i++) {
			rv |= v[i] << mod;
			mod -= 8;
		}
		return rv;
//			return	((0xFF & v[0]) << 24) 
//				|	((0xFF & v[1]) << 16)
//				|	((0xFF & v[2]) <<  8)
//				|	(0xFF  & v[3]);
	}
	public static byte[] XbyInt(int v) {
		byte b0 = (byte)(v >> 24);
		byte b1 = (byte)(v >> 16);
		byte b2 = (byte)(v >>  8);
		byte b3 = (byte)(v);
		if		(b0 != 0)	return new byte[] {b0, b1, b2, b3};
		else if	(b1 != 0)	return new byte[] {b1, b2, b3};
		else if	(b2 != 0)	return new byte[] {b2, b3};
		else				return new byte[] {b3};
	}
	public static byte XtoStrByte(int digit) {
		switch (digit) {
			case 0: return Byte_ascii.Num_0; case 1: return Byte_ascii.Num_1; case 2: return Byte_ascii.Num_2; case 3: return Byte_ascii.Num_3; case 4: return Byte_ascii.Num_4;
			case 5: return Byte_ascii.Num_5; case 6: return Byte_ascii.Num_6; case 7: return Byte_ascii.Num_7; case 8: return Byte_ascii.Num_8; case 9: return Byte_ascii.Num_9;
			default: throw Err_.new_("unknown digit").Add("digit", digit);
		}
	}
	public static byte[][] Split(byte[] src, byte dlm) {
		if (ByteAry_.Len_eq_0(src)) return ByteAry_.Ary_empty;
		int src_len = src.length, src_pos = 0, fld_bgn = 0;
		ListAdp rv = ListAdp_.new_();
		while (true) {
			boolean last = src_pos == src_len;
			byte b = last ? dlm : src[src_pos];
			if (b == dlm) {
				if (last && (src_pos - fld_bgn == 0)) {}
				else
					rv.Add(ByteAry_.Mid(src, fld_bgn, src_pos));
				fld_bgn = src_pos + 1;
			}
			if (last) break;
			++src_pos;
		}
		return (byte[][])rv.XtoAry(byte[].class);
	}
	public static void Replace_reuse(byte[] src, byte find, byte replace) {
		int src_len = src.length;
		for (int i = 0; i < src_len; i++) {
			if (src[i] == find) src[i] = replace;
		}
	}
	public static byte[] Replace(byte[] src, byte find, byte replace) {
		int src_len = src.length;
		byte[] rv = new byte[src_len];
		for (int i = 0; i < src_len; i++) {
			byte b = src[i];
			rv[i] = b == find ? replace : b;
		}
		return rv;
	}
	public static byte[] Replace(byte[] src, byte[] find, byte[] replace) {return Replace_between(src, find, null, replace);}
	public static byte[] Replace_between(byte[] src, byte[] bgn, byte[] end, byte[] replace) {
		ByteAryBfr bfr = ByteAryBfr.new_();
		boolean replace_all = end == null;
		int src_len = src.length, bgn_len = bgn.length, end_len = replace_all ? 0 : end.length;
		int pos = 0;
		while (true) {
			if (pos >= src_len) break;
			int bgn_pos = ByteAry_.FindFwd(src, bgn, pos);
			if (bgn_pos == ByteAry_.NotFound) {
				bfr.Add_mid(src, pos, src_len);
				break;
			}
			else {
				int bgn_rhs = bgn_pos + bgn_len;
				int end_pos = replace_all ? bgn_rhs : ByteAry_.FindFwd(src, end, bgn_rhs);
				if (end_pos == ByteAry_.NotFound) {
					bfr.Add_mid(src, pos, src_len);
					break;
				}
				else {
					bfr.Add_mid(src, pos, bgn_pos);
					bfr.Add(replace);
					pos = end_pos + end_len;
				}
			}
		}
		return bfr.XtoAryAndClear();
	}
	public static int Trim_end_pos(byte[] src, int end) {
		for (int i = end - 1; i > -1; i--) {
			switch (src[i]) {
				case Byte_ascii.Tab: case Byte_ascii.NewLine: case Byte_ascii.CarriageReturn: case Byte_ascii.Space:
					break;
				default:
					return i + 1;
			}
		}
		return 0;
	}
	public static byte[][] Split(byte[] src, byte[] dlm) {
		if (ByteAry_.Len_eq_0(src)) return ByteAry_.Ary_empty;
		int cur_pos = 0, src_len = src.length, dlm_len = dlm.length;
		ListAdp rv = ListAdp_.new_();
		while (true) {
			int find_pos = FindFwd(src, dlm, cur_pos);
			if (find_pos == ByteAry_.NotFound) {
				if (cur_pos == src_len) break;	// dlm is last sequence in src; do not create empty itm
				find_pos = src_len;			
			}
			rv.Add(ByteAry_.Mid(src, cur_pos, find_pos));
			if (find_pos == src_len) break;
			cur_pos = find_pos + dlm_len;
		}
		return (byte[][])rv.XtoAry(byte[].class);
	}
	public static byte[][] Split_lines(byte[] src) {
		if (ByteAry_.Len_eq_0(src)) return ByteAry_.Ary_empty;
		int src_len = src.length, src_pos = 0, fld_bgn = 0;
		ListAdp rv = ListAdp_.new_();
		while (true) {
			boolean last = src_pos == src_len;
			byte b = last ? Byte_ascii.NewLine : src[src_pos];
			int nxt_bgn = src_pos + 1; 
			switch (b) {
				case Byte_ascii.CarriageReturn:
				case Byte_ascii.NewLine:
					if (	b == Byte_ascii.CarriageReturn		// check for crlf
						&& nxt_bgn < src_len && src[nxt_bgn] == Byte_ascii.NewLine) {
							++nxt_bgn;
					}
					if (last && (src_pos - fld_bgn == 0)) {}	// ignore trailing itms
					else
						rv.Add(ByteAry_.Mid(src, fld_bgn, src_pos));
					fld_bgn = nxt_bgn;
					break;
			}
			if (last) break;
			src_pos = nxt_bgn;
		}
		return (byte[][])rv.XtoAry(byte[].class);
	}
	public static byte[] Increment_last(byte[] ary) {return Increment_last(ary, ary.length - 1);}
	public static byte[] Increment_last(byte[] ary, int end_idx) {
		for (int i = end_idx; i > -1; i--) {
			byte end_val_old = ary[i];
			byte end_val_new = (byte)(end_val_old + 1);	
			ary[i] = end_val_new;
			if (end_val_new > (end_val_old & 0xff)) break;	// PATCH.JAVA:need to convert to unsigned byte
		}
		return ary;
	}
	public static byte[] Upper_ascii(byte[] ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			byte b = ary[i];
			if (b > 96 && b < 123)
				ary[i] = (byte)(b - 32);
		}
		return ary;
	}
	public static int Find_bwd_non_ws(byte[] src, int bgn, int end) { // get pos of 1st char that is not ws; 
		if (bgn >= src.length) return ByteAry_.NotFound;
		for (int i = bgn; i >= end; i--) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Space: case Byte_ascii.Tab: case Byte_ascii.NewLine: case Byte_ascii.CarriageReturn:
					break;
				default:
					return i;
			}
		}
		return ByteAry_.NotFound;
	}
}

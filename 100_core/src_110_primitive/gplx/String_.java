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
import java.lang.*;
import gplx.core.strings.*;
public class String_ implements GfoInvkAble {
	public static final Class<?> Cls_ref_type = String.class;
	public static final String Cls_val_name = "str" + "ing";
	public static final int Find_none = -1, Pos_neg1 = -1;
	public static final String Null = null, Empty = "", Null_mark = "<<NULL>>", Tab = "\t", Lf = "\n", CrLf = "\r\n";
	public static String cast_(Object v) {return (String)v;}
	public static String as_(Object obj) {return obj instanceof String ? (String)obj : null;}
	public static String new_ascii_(byte[] v) {return v == null ? null : new_ascii_(v, 0, v.length);}
	public static String new_ascii_(byte[] v, int bgn, int end) {
		try {
			return v == null 
				? null
				: new String(v, bgn, end - bgn, "ASCII");		
		}
		catch (Exception e) {throw Err_.err_(e, "unsupported encoding");}
	}	
	public static String new_utf8_(byte[] v)							{return v == null ? null : new_utf8_(v, 0, v.length);}
	public static String new_utf8_mid_safe_(byte[] v, int bgn, int end)	{return v == null ? null : new_utf8_(v, bgn, end);}
	public static String new_utf8_(byte[] v, int bgn, int end) {
		try {
			return v == null
				? null
				: new String(v, bgn, end - bgn, "UTF-8");		
		}
		catch (Exception e) {throw Err_.err_(e, "unsupported encoding");}
	}
	public static String new_utf8_len_safe_(byte[] v, int bgn, int len)	{
		int v_len = v.length;
		if (bgn + len > v_len) len = v_len - bgn;
		return new_utf8_(v, bgn, bgn + len);
	}
	public static String[] Ary_add(String[]... arys) {
		if (arys == null) return String_.Ary_empty;
		int arys_len = arys.length;
		int rv_len = 0;
		for (int i = 0; i < arys_len; i++) {
			String[] ary = arys[i];
			rv_len += ary.length;
		}
		int rv_idx = 0;
		String[] rv = new String[rv_len];
		for (int i = 0; i < arys_len; i++) {
			String[] ary = arys[i];
			int ary_len = ary.length;
			for (int j = 0; j < ary_len; j++)
				rv[rv_idx++] = ary[j];
		}
		return rv;
	}
	public static boolean Len_gt_0(String s)									{return s != null && s.length() >  0;}	
	public static boolean Len_eq_0(String s)									{return s == null || s.length() == 0;}	
	public static int Len(String s)											{return s.length();}						
	public static String Lower(String s)									{return s.toLowerCase();}					
	public static String Upper(String s)									{return s.toUpperCase();}					
	public static String CaseNormalize(boolean caseSensitive, String s)		{return caseSensitive ? s : String_.Lower(s);}
	public static String Trim(String s)										{return s.trim();}						
	public static String Mid(String s, int bgn)								{return s.substring(bgn);}				
	public static String Replace(String s, String find, String replace)		{return s.replace(find, replace);}		
	public static char[] XtoCharAry(String s)								{return s.toCharArray();}				
	public static char CharAt(String s, int i)								{return s.charAt(i);}							
	public static int CodePointAt(String s, int i)							{return s.codePointAt(i);}
	public static boolean Has(String s, String find)							{return s.indexOf(find) != String_.Find_none;}	
	public static boolean HasAtBgn(String s, String v)							{return s.startsWith(v);}				
	public static boolean HasAtEnd(String s, String v)							{return s.endsWith(v);}					
	public static int FindFwd(String s, String find)						{return s.indexOf(find);}				
	public static int FindFwd(String s, String find, int pos)				{return s.indexOf(find, pos);}			
	public static int FindBwd(String s, String find)						{return s.lastIndexOf(find);}			
	public static int FindBwd(String s, String find, int pos)				{
				return s.lastIndexOf(find, pos);
			}
	public static int FindBetween(String s, String find, int bgn, int end) {
		int rv = FindFwd(s, find, bgn);
		return (rv > end) ? String_.Find_none : rv;
	}
	public static int FindAfter(String s, String find, int bgn) {
		int rv = FindFwd(s, find, bgn);
		return rv == String_.Find_none ? String_.Find_none : rv + Len(find);
	}
	public static int FindAfterRev(String s, String find, int pos) {
		int rv = FindBwd(s, find, pos);
		return rv == String_.Find_none ? String_.Find_none : rv + Len(find);
	}
	public static int Count(String s, String part) {
		int count = 0, pos = -1;	// -1 b/c first pass must be 0 (see pos + 1 below)
		do {
			pos = FindFwd(s, part, pos + 1);
			if (pos == String_.Find_none) break;
			count++;
		}	while (true);
		return count;
	}
	public static boolean Eq(String lhs, String rhs) {return lhs == null ? rhs == null : lhs.equals(rhs);} 
	public static boolean EqAny(String lhs, String... rhsAry) {
		for (int i = 0; i < rhsAry.length; i++)
			if (Eq(lhs, rhsAry[i])) return true;
		return false;
	}
	public static boolean EqNot(String lhs, String rhs) {return !Object_.Eq(lhs, rhs);}
	public static boolean EqEmpty(String lhs, String rhs) {return lhs.equals("");} 
	public static String IfNullOrEmpty(String s, String or) {return s == null || s.length() == 0 ? or : s;}	
	public static int Compare(String lhs, String rhs) {return lhs.compareTo(rhs);} // NOTE: Compare instead of compareTo b/c javafy lowercases compareTo
	public static int Compare_ignoreCase(String lhs, String rhs) {
				if		(lhs == null && rhs != null) 	return CompareAble_.Less;
		else if (lhs != null && rhs == null) 	return CompareAble_.More;
		else if (lhs == null && rhs == null) 	return CompareAble_.Same;
		else									return lhs.compareToIgnoreCase(rhs);
		//#-
	/*
	if		(lhs == null && rhs != null) 	return CompareAble_.Less;
	else if (lhs != null && rhs == null) 	return CompareAble_.More;
	else if (lhs == null && rhs == null) 	return CompareAble_.Same;
	else									return lhs.compareToIgnoreCase(rhs);
	*/
		}
	public static int Compare_strict(String lhs, String rhs) {
		int compare = String_.Compare(lhs, rhs);
		if 		(compare == CompareAble_.Same) 		return CompareAble_.Same;
		else if (compare <  CompareAble_.Same) 		return CompareAble_.Less;
		else /* (compare  > CompareAble_.Same) */	return CompareAble_.More;
	}
	public static int Compare_byteAry(String lhs, String rhs) {
		int lhsLen = lhs.length(), rhsLen = rhs.length();		
		int aryLen = lhsLen < rhsLen ? lhsLen : rhsLen;
		int[] lhsAry = XtoIntAry(lhs, aryLen), rhsAry = XtoIntAry(rhs, aryLen);
		for (int i = 0; i < aryLen; i++) {
			int comp = Int_.Compare(lhsAry[i], rhsAry[i]);
			if (comp != CompareAble_.Same) return comp;
		}
		return Int_.Compare(lhsLen, rhsLen);
	}
	public static int[] XtoIntAry(String s, int len) {
		int[] rv = new int[len];
		for (int i = 0; i < len; i++)
			rv[i] = (int)s.charAt(i);		
		return rv;
	}
	public static String Coalesce(String s, String alt) {return Len_eq_0(s) ? alt : s;}
	public static boolean In(String s, String... ary) {
		for (String itm : ary)
			if (String_.Eq(s, itm)) return true;
		return false;
	}

	public static String new_charAry_(char[] ary, int bgn, int len) {return new String(ary, bgn, len);}
	public static String Mid(String s, int bgn, int end) {
		try {return Mid_lang(s, bgn, end - bgn);}
		catch (Exception e) {
			int len = s == null ? 0 : Len(s);
			Err err = Err_.new_("unknown error").Add("s", s).Add("bgn", bgn).Add("end", end).Add("len", len).Add("e", Err_.Message_lang(e));
			if		(s == null)					err.Hdr_("s is null");
			else if (bgn > end)					err.Hdr_("@bgn > @end");
			else if (bgn < 0 || bgn >= len)		err.Hdr_("@bgn is invalid");
			else if (end < 0 || end >  len)		err.Hdr_("@end is invalid");
			throw err;
		}
	}
	public static String MidByLenSafe(String s, int bgn, int len) {
		if (bgn + len >= Len(s)) len = Len(s) - bgn;
		return Mid_lang(s, bgn, len);
	}
	public static String MidByLen(String s, int bgn, int len) {return Mid_lang(s, bgn, len);}
	public static String GetStrBefore(String s, String spr) {
		int sprPos = String_.FindFwd(s, spr); if (sprPos == String_.Find_none) throw Err_.new_("could not find spr").Add("s", s).Add("spr", spr);
		return Mid(s, 0, sprPos);
	}
	public static String GetStrAfter(String s, String spr) {
		int sprPos = String_.FindFwd(s, spr); if (sprPos == String_.Find_none) throw Err_.new_("could not find spr").Add("s", s).Add("spr", spr);
		return Mid(s, sprPos + 1);
	}
	public static String LimitToFirst(String s, int len) {
		if (len < 0) throw Err_arg.cannotBe_("< 0", "len", len);
		int sLen = Len(s); if (len > sLen) return s;
		return Mid_lang(s, 0, len);
	}
	public static String LimitToLast(String s, int len) {
		if (len < 0) throw Err_arg.cannotBe_("< 0", "len", len);
		int sLen = Len(s); if (len > sLen) return s;
		return Mid_lang(s, sLen - len, len);
	}
	public static String DelBgn(String s, int count) {
		if (count < 0) throw Err_arg.cannotBe_("< 0", "count", count);
		if (s == null) throw Err_arg.null_("s");
		int len = Len(s); if (count > len) throw Err_arg.cannotBe_("> @len", "count", count).Add("len", len);
		return String_.Mid(s, count);
	}
	public static String DelBgnIf(String s, String find) {
		if (s == null) throw Err_arg.null_("s"); if (find == null) throw Err_arg.null_("find");
		return HasAtBgn(s, find) ? String_.Mid(s, Len(find)) : s;
	}
	public static String DelEnd(String s, int count) {
		if (count < 0) throw Err_arg.cannotBe_("< 0", "count", count);
		if (s == null) throw Err_arg.null_("s");
		int len = Len(s); if (count > len) throw Err_arg.cannotBe_("> len", "count", count).Add("len", len);
		return Mid_lang(s, 0, len + -count);
	}
	public static String DelEndIf(String s, String find) {
		if (s == null) throw Err_arg.null_("s"); if (find == null) throw Err_arg.null_("find");
		return HasAtEnd(s, find) ? Mid_lang(s, 0, Len(s) - Len(find)) : s;
	}
	public static String LowerFirst(String s) {
		int len = Len(s); if (len == 0) return String_.Empty;
		String char0 = Lower(Mid_lang(s, 0, 1));
		return len == 1 ? char0 : char0 + Mid(s, 1);
	}
	public static String UpperFirst(String s) {
		int len = Len(s); if (len == 0) return String_.Empty;
		String char0 = Upper(Mid_lang(s, 0, 1)); 
		return len == 1 ? char0 : char0 + Mid(s, 1);
	}
	public static String PadBgn(String s, int totalLen, String pad) {return Pad(s, totalLen, pad, true);}
	public static String PadEnd(String s, int totalLen, String pad) {return Pad(s, totalLen, pad, false);}
	@gplx.Internal protected static String Pad(String s, int totalLen, String pad, boolean bgn) {
		int sLen = Len(s);
		int padLen = totalLen - sLen; if (padLen < 0) return s;
		String_bldr sb = String_bldr_.new_();
		if (!bgn) sb.Add(s);
		for (int i = 0; i < padLen; i++)
			sb.Add(pad);
		if (bgn) sb.Add(s);
		return sb.XtoStr();
	}
	public static String TrimEnd(String s) {if (s == null) return null;
		int len = String_.Len(s);
		if (len == 0) return s;
		int last = len;
		for (int i = len; i > 0; i--) {
			char c = s.charAt(i - 1);	
			last = i;
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				break;
			}
		}
		return (last == len) ? s : Mid_lang(s, 0, last);
	}
	public static String Repeat(String s, int count) {
		if (count < 0) throw Err_.new_("count cannot be negative").Add("count", count).Add("s", s);
		String_bldr sb = String_bldr_.new_();
		for (int i = 0; i < count; i++)
			sb.Add(s);
		return sb.XtoStr();
	}
	public static String Insert(String s, int pos, String toInsert) {
		if (pos < 0 || pos >= String_.Len(s)) throw Err_.new_("String_.Insert failed; pos invalid").Add("pos", pos).Add("s", s).Add("toInsert", toInsert);
		return s.substring(0, pos) + toInsert + s.substring(pos);
	}
	public static String Format(String fmt, Object... args) {return Format_do(fmt, args);}
	public static String FormatOrEmptyStrIfNull(String fmt, Object arg) {return arg == null ? "" : Format(fmt, arg);}
	public static String Concat(char... ary) {return new String(ary);}
	public static String Concat(String s1, String s2, String s3) {return s1 + s2 + s3;}
	public static String Concat(String... ary) {
		String_bldr sb = String_bldr_.new_();
		for (String val : ary)
			sb.Add(val);
		return sb.XtoStr();
	}
	public static String Concat_any(Object... ary) {
		String_bldr sb = String_bldr_.new_();
		for (Object val : ary)
			sb.Add_obj(val);
		return sb.XtoStr();
	}
	public static String ConcatWith_any(String separator, Object... ary) {
		String_bldr sb = String_bldr_.new_();
		int aryLen = Array_.Len(ary);
		for (int i = 0; i < aryLen; i++) {
			if (i != 0) sb.Add(separator);
			Object val = ary[i];
			sb.Add_obj(Object_.Xto_str_strict_or_empty(val));
		}
		return sb.XtoStr();			
	}
	public static String Concat_with_str(String spr, String... ary) {
		String_bldr sb = String_bldr_.new_();
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			if (i != 0) sb.Add(spr);
			sb.Add_obj(ary[i]);
		}
		return sb.XtoStr();			
	}
	public static String Concat_lines_crlf(String... values) {
		String_bldr sb = String_bldr_.new_();
		for (String val : values)
			sb.Add(val).Add(String_.CrLf);
		return sb.XtoStr();
	}
	public static String Concat_lines_crlf_skipLast(String... values) {
		String_bldr sb = String_bldr_.new_();
		for (String val : values) {
			if (sb.Count() != 0) sb.Add(String_.CrLf);
			sb.Add(val);
		}
		return sb.XtoStr();
	}
	public static String Concat_lines_nl(String... values) {
		String_bldr sb = String_bldr_.new_();
		for (String val : values)
			sb.Add(val).Add("\n");
		return sb.XtoStr();
	}
	public static String Concat_lines_nl_skip_last(String... ary) {
		String_bldr sb = String_bldr_.new_();
		int ary_len = ary.length; int ary_end = ary_len - 1;
		for (int i = 0; i < ary_len; i++) {
			sb.Add(ary[i]);
			if (i != ary_end) sb.Add("\n");
		}
		return sb.XtoStr();
	}

	public static String[] Ary(String... ary) {return ary;}
	public static String[] Ary_wo_null(String... ary) {
		ListAdp list = ListAdp_.new_();
		int len = ary.length;
		for (int i = 0; i < len; ++i) {
			String itm = ary[i];
			if (itm == null) continue;
			list.Add(itm);
		}
		return list.XtoStrAry();
	}
	public static String AryXtoStr(String... ary) {
		String_bldr sb = String_bldr_.new_();
		for (String s : ary)
			sb.Add(s).Add(";");
		return sb.XtoStr();
	}
	public static final String[] Ary_empty = new String[0];
	public static String[] Split(String raw, char dlm) {return Split(raw, dlm, false);}
	public static String[] Split(String raw, char dlm, boolean addEmptyIfDlmIsLast) {
		ListAdp list = ListAdp_.new_(); String_bldr sb = String_bldr_.new_();
		int rawLen = String_.Len(raw); char c = '\0';
		for (int i = 0; i < rawLen; i++) {
			c = String_.CharAt(raw, i);
			if (c == dlm) {
				if (!addEmptyIfDlmIsLast && sb.Count() == 0 && i == rawLen - 1) {}
				else list.Add(sb.Xto_str_and_clear());
			}
			else
				sb.Add(c);
		}
		if (sb.Count() > 0)
			list.Add(sb.Xto_str_and_clear());
		return list.XtoStrAry();
	}
	public static String[] Split(String s, String separator) {return Split_do(s, separator, false);}
	public static String[] SplitLines_crlf(String s) {return Split(s, Op_sys.Wnt.Nl_str());}
	public static String[] SplitLines_nl(String s) {return Split(s, Op_sys.Lnx.Nl_str());}
	public static String[] SplitLines_any(String s) {return Split_do(s, Op_sys.Lnx.Nl_str(), true);}

	static String Format_do(String s, Object[] ary) {
		int aryLength = Array_.LenAry(ary); if (aryLength == 0) return s; // nothing to format
		String_bldr sb = String_bldr_.new_();
		char bracketBgn = '{', bracketEnd = '}';
		String aryVal = null; char c, next;
		int pos = 0; int textLength = Len(s); String numberStr = ""; boolean bracketsOn = false;
		while (true) {
			if (pos == textLength) break; 
			c = CharAt(s, pos);				
			if (bracketsOn) {	// mode=bracketsOn
				if (c == bracketBgn) {	// first bracketBgn is fake; add bracketBgn and whatever is in numberStr
					sb.Add(bracketBgn).Add(numberStr);
					numberStr = "";
				}
				else if (c == bracketEnd) {
					int aryIdx = Int_.parse_or_(numberStr, Int_.MinValue);
					if (aryIdx != Int_.MinValue && Int_.Between(aryIdx, 0, aryLength - 1))	// check (a) aryIdx is num; (b) aryIdx is in bounds
						aryVal = Object_.Xto_str_strict_or_empty(ary[aryIdx]);
					else
						aryVal = String_.Concat_any(bracketBgn, numberStr, bracketEnd);		// not valid, just add String
					sb.Add(aryVal);
					bracketsOn = false;
					numberStr = "";
				}
				else	// char=anythingElse
					numberStr += c;
			}
			else {	// mode=bracketsOff
				if (c == bracketBgn || c == bracketEnd) {
					boolean isEnd = pos == textLength - 1;
					if (isEnd)
						sb.Add(c);
					else {
						next = CharAt(s, pos + 1);
						if (next == c) {	// "{{" or "}}": escape by doubling
							sb.Add(c);
							pos++;
						}
						else
							bracketsOn = true;
					}
				}
				else // char=anythingElse
					sb.Add(c);
			}
			pos++;
		}
		if (Len(numberStr) > 0)	// unclosed bracket; add bracketBgn and whatever is in numberStr; ex: "{0"
			sb.Add(bracketBgn).Add(numberStr);
		return sb.XtoStr();
	}
	static String[] Split_do(String s, String spr, boolean skipChar13) {
		if (String_.Eq(s, "")			// "".Split('a') return array with one member: ""
			|| String_.Eq(spr, ""))		// "a".Split('\0') returns array with one member: "a"
			return new String[] {s};
		ListAdp list = ListAdp_.new_(); String_bldr sb = String_bldr_.new_();
		int i = 0, sprPos = 0; boolean sprMatched = false; char spr0 = CharAt(spr, 0);
		int textLength = Len(s); int sprLength = Len(spr);
		while (true) {
			if (sprMatched
				|| i == textLength) {	// last pass; add whatever's in sb to list
				list.Add(sb.Xto_str_and_clear());
				if (sprMatched && i == textLength) list.Add(""); // if s ends with spr and last pass, add emptyString as last
				sprMatched = false;
			}
			if (i == textLength) break;
			char c = CharAt(s, i);
			if (skipChar13 && c == (char)13) {i++; continue;}
			if (c == spr0) {	// matches first char of spr
				sprPos = 1;
				while (true) {
					if (sprPos == sprLength) { // made it to end, must be match
						sprMatched = true;
						break;
					}
					if (i + sprPos == textLength) break; // ran out of s; handles partial match at end of String; ab+, +-
					if (CharAt(s, i + sprPos) != CharAt(spr, sprPos)) break; // no match
					sprPos++;
				}
				if (!sprMatched)	// add partial match to sb
					sb.Add(Mid_lang(s, i, sprPos));
				i += sprPos;
			}
			else {	// no spr match; just add char, increment pos
				sb.Add(c);
				i++;
			}
		}		
		return (String[])list.Xto_ary(String.class);
	}
	static String Mid_lang(String s, int bgn, int len) {return s.substring(bgn, bgn + len);}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_Replace)) {
			String s = m.ReadStr(GfsCore_.Arg_primitive), find = m.ReadStr("find"), replace = m.ReadStr("replace");
			if (ctx.Deny()) return this;
			return Replace(s, find, replace);
		}
		else if	(ctx.Match(k, Invk_Len)) {
			String s = m.ReadStr(GfsCore_.Arg_primitive);
			if (ctx.Deny()) return this;
			return Len(s);
		}
		else if (ctx.Match(k, Invk_PadBgn)) {
			String s = m.ReadStr(GfsCore_.Arg_primitive); int totalLen = m.ReadInt("totalLen"); String pad = m.ReadStr("pad"); 
			if (ctx.Deny()) return this;
			return PadBgn(s, totalLen, pad);
		}
		else return GfoInvkAble_.Rv_unhandled;
	}	public static final String Invk_Replace = "Replace", Invk_Len = "Len", Invk_PadBgn = "PadBgn";
        public static final String_ Gfs = new String_();
	public static String Extract_after_bwd(String src, String dlm) {
		int dlm_pos = String_.FindBwd(src, dlm); if (dlm_pos == String_.Find_none) return String_.Empty;
		int src_len = String_.Len(src); if (dlm_pos == src_len - 1) return String_.Empty;
		return String_.Mid(src, dlm_pos + 1, src_len);
	}
	public static String Replace_by_pos(String v, int del_bgn, int del_end, String repl) {
		return String_.Mid(v, 0, del_bgn) + repl + String_.Mid(v, del_end, String_.Len(v));
	}
	public static String read_(Object obj) {// NOTE: same as cast_; for consistent readability only
		String rv = as_(obj);
		if (rv == null && obj != null) throw Err_.type_mismatch_(String.class, obj); // NOTE: obj != null needed; EX: cast_(null) --> null
		return rv;
	}
	public static String[] Ary(byte[]... ary) {
		if (ary == null) return String_.Ary_empty;
		int ary_len = ary.length;
		String[] rv = new String[ary_len];
		for (int i = 0; i < ary_len; i++)
			rv[i] = String_.new_utf8_(ary[i]);
		return rv;
	}
	public static String [] Ary_filter(String[] src, String[] filter) {
		HashAdp hash = HashAdp_.new_();
		int len = filter.length;
		for (int i = 0; i < len; i++) {
			String itm = filter[i];
			hash.AddReplace(itm, itm);				
		}
		ListAdp rv = ListAdp_.new_();
		len = src.length;
		for (int i = 0; i < len; i++) {
			String itm = src[i];
			if (hash.Has(itm)) rv.Add(itm);
		}
		return rv.XtoStrAry();
	}
	public static String[] Ary_flatten(String[][] src_ary) {
		int trg_len = 0;
		int src_len = Array_.Len(src_ary);
		for (int i = 0; i < src_len; i++) {
			String[] itm = src_ary[i];
			if (itm != null) trg_len += Array_.Len(itm);
		}
		String[] trg_ary = new String[trg_len];
		trg_len = 0;
		for (int i = 0; i < src_len; i++) {
			String[] itm = src_ary[i];
			if (itm == null) continue;
			int itm_len = Array_.Len(itm);
			for (int j = 0; j < itm_len; j++)
				trg_ary[trg_len++] = itm[j];
		}
		return trg_ary;
	}
}

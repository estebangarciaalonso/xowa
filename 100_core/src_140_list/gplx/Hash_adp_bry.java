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
import gplx.core.primitives.*;
import gplx.intl.*;
public class Hash_adp_bry extends gplx.lists.HashAdp_base implements HashAdp {
	private final Hash_adp_bry_itm_base proto, key_ref;
	Hash_adp_bry(Hash_adp_bry_itm_base proto) {
		this.proto = proto;
		key_ref = proto.New();
	}
	@Override protected Object Fetch_base(Object key)				{return super.Fetch_base(key_ref.Init((byte[])key));}
	@Override protected void Del_base(Object key)					{super.Del_base(key_ref.Init((byte[])key));}
	@Override protected boolean Has_base(Object key)					{return super.Has_base(key_ref.Init((byte[])key));}
	public Object Get_by_bry(byte[] src)							{return super.Fetch_base(key_ref.Init(src));}
	public Object Get_by_mid(byte[] src, int bgn, int end)			{return super.Fetch_base(key_ref.Init(src, bgn, end));}
	public Hash_adp_bry Add_bry_byte(byte[] key, byte val)			{this.Add_base(key, Byte_obj_val.new_(val)); return this;}
	public Hash_adp_bry Add_bry_bry(byte[] key)						{this.Add_base(key, key); return this;}
	public Hash_adp_bry Add_str_byte(String key, byte val)			{this.Add_base(Bry_.new_utf8_(key), Byte_obj_val.new_(val)); return this;}
	public Hash_adp_bry Add_str_obj(String key, Object val)			{this.Add_base(Bry_.new_utf8_(key), val); return this;}
	public Hash_adp_bry Add_bry_obj(byte[] key, Object val)			{this.Add_base(key, val); return this;}
	public Hash_adp_bry Add_many_str(String... ary) {
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			String itm = ary[i];
			byte[] bry = Bry_.new_utf8_(itm);
			Add_bry_bry(bry);
		}
		return this;
	}
	@Override protected void Add_base(Object key, Object val) {
		byte[] key_bry = (byte[])key;
		Hash_adp_bry_itm_base key_itm = proto.New();
		key_itm.Init(key_bry, 0, key_bry.length);
		super.Add_base(key_itm, val);
	}
	public static Hash_adp_bry cs_()								{return new Hash_adp_bry(Hash_adp_bry_itm_cs._);}
	public static Hash_adp_bry ci_ascii_()							{return new Hash_adp_bry(Hash_adp_bry_itm_ci_ascii._);}
	public static Hash_adp_bry ci_utf8_(Gfo_case_mgr case_mgr)		{return new Hash_adp_bry(Hash_adp_bry_itm_ci_utf8.get_or_new(case_mgr));}
	public static Hash_adp_bry c__utf8_(boolean case_match, Gfo_case_mgr case_mgr)	{return case_match ? cs_() : ci_utf8_(case_mgr);}
}
abstract class Hash_adp_bry_itm_base {
	public abstract Hash_adp_bry_itm_base New();
	public Hash_adp_bry_itm_base Init(byte[] src) {return this.Init(src, 0, src.length);}
	public abstract Hash_adp_bry_itm_base Init(byte[] src, int src_bgn, int src_end);
}
class Hash_adp_bry_itm_cs extends Hash_adp_bry_itm_base {
	private byte[] src; int src_bgn, src_end;
	@Override public Hash_adp_bry_itm_base New() {return new Hash_adp_bry_itm_cs();}
	@Override public Hash_adp_bry_itm_base Init(byte[] src, int src_bgn, int src_end) {this.src = src; this.src_bgn = src_bgn; this.src_end = src_end; return this;}
	@Override public int hashCode() {
		int rv = 0;
		for (int i = src_bgn; i < src_end; i++) {
			int b_int = src[i] & 0xFF;	// JAVA: patch
			rv = (31 * rv) + b_int;
		}
		return rv;	
	}
	@Override public boolean equals(Object obj) {
		if (obj == null) return false;
		Hash_adp_bry_itm_cs comp = (Hash_adp_bry_itm_cs)obj;
		byte[] comp_src = comp.src; int comp_bgn = comp.src_bgn, comp_end = comp.src_end;
		int comp_len = comp_end - comp_bgn, src_len = src_end - src_bgn;
		if (comp_len != src_len) return false;
		for (int i = 0; i < comp_len; i++) {
			int src_pos = src_bgn + i;
			if (src_pos >= src_end) return false;	// ran out of src; exit; EX: src=ab; find=abc
			if (src[src_pos] != comp_src[i + comp_bgn]) return false;
		}
		return true;
	}
        public static final Hash_adp_bry_itm_cs _ = new Hash_adp_bry_itm_cs(); Hash_adp_bry_itm_cs() {}
}
class Hash_adp_bry_itm_ci_ascii extends Hash_adp_bry_itm_base {
	private byte[] src; int src_bgn, src_end;
	@Override public Hash_adp_bry_itm_base New() {return new Hash_adp_bry_itm_ci_ascii();}
	@Override public Hash_adp_bry_itm_base Init(byte[] src, int src_bgn, int src_end) {this.src = src; this.src_bgn = src_bgn; this.src_end = src_end; return this;}
	@Override public int hashCode() {
		int rv = 0;
		for (int i = src_bgn; i < src_end; i++) {
			int b_int = src[i] & 0xFF;		// JAVA: patch
			if (b_int > 64 && b_int < 91)	// 64=before A; 91=after Z; NOTE: lowering upper-case on PERF assumption that there will be more lower-case letters than upper-case
				b_int += 32;
			rv = (31 * rv) + b_int;
		}
		return rv;	
	}
	@Override public boolean equals(Object obj) {
		if (obj == null) return false;
		Hash_adp_bry_itm_ci_ascii comp = (Hash_adp_bry_itm_ci_ascii)obj;
		byte[] comp_src = comp.src; int comp_bgn = comp.src_bgn, comp_end = comp.src_end;
		int comp_len = comp_end - comp_bgn, src_len = src_end - src_bgn;
		if (comp_len != src_len) return false;
		for (int i = 0; i < comp_len; i++) {
			int src_pos = src_bgn + i;
			if (src_pos >= src_end) return false;	// ran out of src; exit; EX: src=ab; find=abc
			byte src_byte = src[src_pos];
			if (src_byte > 64 && src_byte < 91) src_byte += 32;
			byte comp_byte = comp_src[i + comp_bgn];
			if (comp_byte > 64 && comp_byte < 91) comp_byte += 32;
			if (src_byte != comp_byte) return false;
		}
		return true;
	}
        public static final Hash_adp_bry_itm_ci_ascii _ = new Hash_adp_bry_itm_ci_ascii(); Hash_adp_bry_itm_ci_ascii() {}
}
class Hash_adp_bry_itm_ci_utf8 extends Hash_adp_bry_itm_base {
	private final Gfo_case_mgr case_mgr;
	Hash_adp_bry_itm_ci_utf8(Gfo_case_mgr case_mgr) {this.case_mgr = case_mgr;}
	private byte[] src; int src_bgn, src_end;
	@Override public Hash_adp_bry_itm_base New() {return new Hash_adp_bry_itm_ci_utf8(case_mgr);}
	@Override public Hash_adp_bry_itm_base Init(byte[] src, int src_bgn, int src_end) {this.src = src; this.src_bgn = src_bgn; this.src_end = src_end; return this;}
	@Override public int hashCode() {
		int rv = 0;
		for (int i = src_bgn; i < src_end; i++) {
			byte b = src[i];
			int b_int = b & 0xFF;			// JAVA: patch
			Gfo_case_itm itm = case_mgr.Get_or_null(b, src, i, src_end);
			if (itm == null) {				// unknown itm; byte is a number, symbol, or unknown; just use the existing byte
			}
			else {							// known itm; use its hash_code
				b_int = itm.Hashcode_lo();
				i += itm.Len_lo() - 1;
			}
			rv = (31 * rv) + b_int;
		}
		return rv;	
	}
	@Override public boolean equals(Object obj) {
		if (obj == null) return false;
		Hash_adp_bry_itm_ci_utf8 trg_itm = (Hash_adp_bry_itm_ci_utf8)obj;
		byte[] trg = trg_itm.src; int trg_bgn = trg_itm.src_bgn, trg_end = trg_itm.src_end;
		int src_c_bgn = src_bgn;
		int trg_c_bgn = trg_bgn;
		while	(	src_c_bgn < src_end
				&&	trg_c_bgn < trg_end) {			// exit once one goes out of bounds
			byte src_c = src[src_c_bgn];
			byte trg_c = trg[trg_c_bgn];
			int src_c_len = Utf8_.Len_of_char_by_1st_byte(src_c);
			int trg_c_len = Utf8_.Len_of_char_by_1st_byte(trg_c);
			int src_c_end = src_c_bgn + src_c_len;
			int trg_c_end = trg_c_bgn + trg_c_len;
			Gfo_case_itm src_c_itm = case_mgr.Get_or_null(src_c, src, src_c_bgn, src_c_end);
			Gfo_case_itm trg_c_itm = case_mgr.Get_or_null(trg_c, trg, trg_c_bgn, trg_c_end);
			if		(src_c_itm != null && trg_c_itm == null)	return false;						// src == ltr; trg != ltr; EX: a, 1
			else if (src_c_itm == null && trg_c_itm != null)	return false;						// src != ltr; trg == ltr; EX: 1, a
			else if (src_c_itm == null && trg_c_itm == null) {										// src != ltr; trg != ltr; EX: 1, 2; ＿, Ⓐ
				if (!Bry_.Match(src, src_c_bgn, src_c_end, trg, trg_c_bgn, trg_c_end)) return false;// syms do not match; return false;
			}
			else {
				if (!src_c_itm.Eq_lo(trg_c_itm)) return false;										// lower-case hash-codes don't match; return false;
			}
			src_c_bgn = src_c_end;
			trg_c_bgn = trg_c_end;
		}
		return src_c_bgn == src_end && trg_c_bgn == trg_end;										// only return true if both src and trg read to end of their brys, otherwise "a","ab" will match
	}
        public static Hash_adp_bry_itm_ci_utf8 get_or_new(Gfo_case_mgr case_mgr) {
		switch (case_mgr.Tid()) {
			case Gfo_case_mgr_.Tid_ascii:		if (Itm_ascii == null) Itm_ascii = new Hash_adp_bry_itm_ci_utf8(case_mgr); return Itm_ascii;
			case Gfo_case_mgr_.Tid_utf8:		if (Itm_utf8  == null) Itm_utf8  = new Hash_adp_bry_itm_ci_utf8(case_mgr); return Itm_utf8;
			case Gfo_case_mgr_.Tid_custom:		return new Hash_adp_bry_itm_ci_utf8(case_mgr);
			default:							throw Err_.unhandled(case_mgr.Tid());
		}
	}
	private static Hash_adp_bry_itm_ci_utf8 Itm_ascii, Itm_utf8;
}

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
import gplx.core.primitives.*; import gplx.core.strings.*;
public class Bry_fmtr {
	public byte[] Fmt() {return fmt;} private byte[] fmt = Bry_.Empty;
	public boolean Fmt_null() {return fmt.length == 0;}
	public Bry_fmtr_eval_mgr Eval_mgr() {return eval_mgr;} public Bry_fmtr Eval_mgr_(Bry_fmtr_eval_mgr v) {eval_mgr = v; return this;} Bry_fmtr_eval_mgr eval_mgr = Bry_fmtr_eval_mgr_gfs._;
	public Bry_fmtr Fmt_(byte[] v) {fmt = v; dirty = true; return this;} public Bry_fmtr Fmt_(String v) {return Fmt_(Bry_.new_utf8_(v));}
	public Bry_fmtr Keys_(String... ary) {
		if (keys == null)	keys = HashAdp_.new_();
		else				keys.Clear();
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++)
			keys.Add(Bry_obj_ref.new_(Bry_.new_utf8_(ary[i])), Int_obj_val.new_(i));
		dirty = true;
		return this;
	}	HashAdp keys = null;
	public void Bld_bfr(Bry_bfr bfr, byte[]... args) {
		if (dirty) Compile(); 
		int args_len = args.length;
		for (int i = 0; i < itms_len; i++) {
			Bry_fmtr_itm itm = itms[i];
			if (itm.Arg) {
				int arg_idx = itm.ArgIdx;
				if (arg_idx < args_len)
					bfr.Add(args[arg_idx]);
				else
					bfr.Add(missing_bgn).Add_int_variable(arg_idx + missing_adj).Add(missing_end);
			}
			else
				bfr.Add(itm.Dat);
		}
	}
	public void Bld_bfr_none(Bry_bfr bfr) {
		if (dirty) Compile(); 
		for (int i = 0; i < itms_len; i++) {
			Bry_fmtr_itm itm = itms[i];
			if (itm.Arg)
				bfr.Add_byte(char_escape).Add_byte(char_arg_bgn).Add_int_variable(itm.ArgIdx).Add_byte(char_arg_end);
			else
				bfr.Add(itm.Dat);
		}
	}
	public void Bld_bfr(Bry_bfr bfr, Bry_fmtr_arg... args) {
		if (dirty) Compile(); 
		for (int i = 0; i < itms_len; i++) {
			Bry_fmtr_itm itm = itms[i];
			if (itm.Arg)
				args[itm.ArgIdx].XferAry(bfr, itm.ArgIdx);
			else
				bfr.Add(itm.Dat);
		}
	}
	public void Bld_bfr_one(Bry_bfr bfr, Object val) {
		Bld_bfr_one_ary[0] = val;
		Bld_bfr_ary(bfr, Bld_bfr_one_ary);
	}	Object[] Bld_bfr_one_ary = new Object[1];
	public void Bld_bfr_many(Bry_bfr bfr, Object... args) {Bld_bfr_ary(bfr, args);}
	public void Bld_bfr_ary(Bry_bfr bfr, Object[] args) {
		if (dirty) Compile(); 
		int args_len = args.length;
		for (int i = 0; i < itms_len; i++) {
			Bry_fmtr_itm itm = itms[i];
			if (itm.Arg) {
				int arg_idx = itm.ArgIdx;
				if (arg_idx > -1 && arg_idx < args_len)
					bfr.Add_obj(args[itm.ArgIdx]);
				else
					bfr.Add_byte(char_escape).Add_byte(char_arg_bgn).Add_int_variable(arg_idx).Add_byte(char_arg_end);
			}
			else
				bfr.Add(itm.Dat);
		}
	}
	public byte[] Bld_bry_none(Bry_bfr bfr) {Bld_bfr_ary(bfr, Object_.Ary_empty); return bfr.Xto_bry_and_clear();}
	public byte[] Bld_bry_many(Bry_bfr bfr, Object... args) {
		Bld_bfr_ary(bfr, args);
		return bfr.Xto_bry_and_clear();
	}
	public String Bld_str_many(Bry_bfr bfr, String fmt, Object... args) {
		this.Fmt_(fmt).Bld_bfr_many(bfr, args);
		return bfr.Xto_str_and_clear();
	}

	public String Bld_str_many(String... args) {
		if (dirty) Compile(); 
		String_bldr rv = String_bldr_.new_();
		int args_len = args.length;
		for (int i = 0; i < itms_len; i++) {
			Bry_fmtr_itm itm = itms[i];
			if (itm.Arg) {
				int arg_idx = itm.ArgIdx;
				if (arg_idx < args_len)
					rv.Add(args[arg_idx]);
				else
					rv.Add(missing_bgn).Add(arg_idx + missing_adj).Add(missing_end);
			}
			else
				rv.Add(itm.DatStr());
		}
		return rv.XtoStr();
	}	private Bry_fmtr_itm[] itms; int itms_len;
	public byte[] Missing_bgn() {return missing_bgn;} public Bry_fmtr Missing_bgn_(byte[] v) {missing_bgn = v; return this;} private byte[] missing_bgn = missing_bgn_static; static byte[] missing_bgn_static = Bry_.new_utf8_("~{"), missing_end_static = Bry_.new_utf8_("}");
	public byte[] Missing_end() {return missing_end;} public Bry_fmtr Missing_end_(byte[] v) {missing_end = v; return this;} private byte[] missing_end = missing_end_static;
	public int Missing_adj() {return missing_adj;} public Bry_fmtr Missing_adj_(int v) {missing_adj = v; return this;} int missing_adj;
	public boolean Fail_when_invalid_escapes() {return fail_when_invalid_escapes;} public Bry_fmtr Fail_when_invalid_escapes_(boolean v) {fail_when_invalid_escapes = v; return this;} private boolean fail_when_invalid_escapes = true;
	public Bry_fmtr Compile() {
		Bry_bfr lkp_bfr = Bry_bfr.new_(16);
		int fmt_len = fmt.length; int fmt_end = fmt_len - 1; int fmt_pos = 0;
		byte[] trg_bry = new byte[fmt_len]; int trg_pos = 0;
		boolean lkp_is_active = false, lkp_is_numeric = true;
		byte nxt_byte, tmp_byte;
		ListAdp list = ListAdp_.new_();
		fmt_args_exist = false;
		while (true) {
			if (fmt_pos > fmt_end) break;
			byte cur_byte = fmt[fmt_pos];
			if		(lkp_is_active) {
				if (cur_byte == char_arg_end) {
					if (lkp_is_numeric)
						list.Add(Bry_fmtr_itm.arg_(lkp_bfr.XtoInt(0) - baseInt));
					else {
						byte[] key_fmt = lkp_bfr.Xto_bry();
						Object idx_ref = keys.Fetch(Bry_obj_ref.new_(key_fmt));
						if (idx_ref == null) {
							int lkp_bfr_len = lkp_bfr.Len();
							byte[] lkp_bry = lkp_bfr.Bfr();
							trg_bry[trg_pos++] = char_escape;
							trg_bry[trg_pos++] = char_arg_bgn;
							for (int i = 0; i < lkp_bfr_len; i++)
								trg_bry[trg_pos++] = lkp_bry[i];
							trg_bry[trg_pos++] = char_arg_end;
						}
						else {
							list.Add(Bry_fmtr_itm.arg_(((Int_obj_val)idx_ref).Val() - baseInt));
						}
					}
					lkp_is_active = false;
					lkp_bfr.Clear();
					fmt_args_exist = true;
				}
				else {
					lkp_bfr.Add_byte(cur_byte);
					switch (cur_byte) {
						case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
						case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
							break;
						default:
							lkp_is_numeric = false;
							break;
					}
				}
				fmt_pos += 1;
			}
			else if	(cur_byte == char_escape) {
				if (fmt_pos == fmt_end) {
					if (fail_when_invalid_escapes)
						throw Err_.new_("escape char encountered but no more chars left");
					else {
						trg_bry[trg_pos] = cur_byte;
						break;
					}
				}
				nxt_byte = fmt[fmt_pos + 1];
				if (nxt_byte == char_arg_bgn) {
					if (trg_pos > 0) {list.Add(Bry_fmtr_itm.dat_(trg_bry, trg_pos)); trg_pos = 0;}	// something pending; add it to list
					int eval_lhs_bgn = fmt_pos + 2;
					if (eval_lhs_bgn < fmt_len && fmt[eval_lhs_bgn] == char_eval_bgn) {	// eval found
						fmt_pos = Compile_eval_cmd(fmt, fmt_len, eval_lhs_bgn, list);
						continue;
					}
					else {
						lkp_is_active = true;
						lkp_is_numeric = true;
					}
				}
				else {	// ~{0}; ~~ -> ~; ~n -> newLine; ~t -> tab
					if		(nxt_byte == char_escape)		tmp_byte = char_escape;
					else if	(nxt_byte == char_escape_nl)	tmp_byte = Byte_ascii.NewLine;
					else if (nxt_byte == char_escape_tab)	tmp_byte = Byte_ascii.Tab;
					else {
						if (fail_when_invalid_escapes) throw Err_.new_("unknown escape code").Add("code", Char_.XbyInt(nxt_byte)).Add("fmt_pos", fmt_pos + 1);
						else
							tmp_byte = cur_byte;
					}
					trg_bry[trg_pos++] = tmp_byte;
				}
				fmt_pos += 2;
			}
			else {
				trg_bry[trg_pos++] = cur_byte;
				fmt_pos += 1;
			}
		}
		if (lkp_is_active) throw Err_.new_("idx mode not closed");
		if (trg_pos > 0) {list.Add(Bry_fmtr_itm.dat_(trg_bry, trg_pos)); trg_pos = 0;}
		itms = (Bry_fmtr_itm[])list.Xto_ary(Bry_fmtr_itm.class);
		itms_len = itms.length;
		return this;
	}
	int Compile_eval_cmd(byte[] fmt, int fmt_len, int eval_lhs_bgn, ListAdp list) {
		int eval_lhs_end = Bry_finder.Find_fwd(fmt, char_eval_end, eval_lhs_bgn + Int_.Const_dlm_len, fmt_len); if (eval_lhs_end == Bry_.NotFound) throw Err_mgr._.fmt_(GRP_KEY, "eval_lhs_end_invalid", "could not find eval_lhs_end: ~{0}", String_.new_utf8_(fmt, eval_lhs_bgn, fmt_len));
		byte[] eval_dlm = Bry_.Mid(fmt, eval_lhs_bgn		, eval_lhs_end + Int_.Const_dlm_len);
		int eval_rhs_bgn = Bry_finder.Find_fwd(fmt, eval_dlm		, eval_lhs_end + Int_.Const_dlm_len, fmt_len); if (eval_rhs_bgn == Bry_.NotFound) throw Err_mgr._.fmt_(GRP_KEY, "eval_rhs_bgn_invalid", "could not find eval_rhs_bgn: ~{0}", String_.new_utf8_(fmt, eval_lhs_end, fmt_len));
		byte[] eval_cmd = Bry_.Mid(fmt, eval_lhs_end + Int_.Const_dlm_len, eval_rhs_bgn);
		byte[] eval_rslt = eval_mgr.Eval(eval_cmd);
		int eval_rhs_end = eval_rhs_bgn + Int_.Const_dlm_len + eval_dlm.length;
		if (eval_rslt == null) eval_rslt = Bry_.Mid(fmt, eval_lhs_bgn - 2, eval_rhs_end);	// not found; return original argument
		list.Add(Bry_fmtr_itm.dat_bry_(eval_rslt));
		return eval_rhs_end;
	}
	static final String GRP_KEY = "gplx.Bry_fmtr";
	public boolean Fmt_args_exist() {return fmt_args_exist;} private boolean fmt_args_exist;
	boolean dirty = true;
	int baseInt = 0;
	public static final byte char_escape = Byte_ascii.Tilde, char_arg_bgn = Byte_ascii.Curly_bgn, char_arg_end = Byte_ascii.Curly_end, char_escape_nl = Byte_ascii.Ltr_n, char_escape_tab = Byte_ascii.Ltr_t, char_eval_bgn = Byte_ascii.Lt, char_eval_end = Byte_ascii.Gt;
	public static final Bry_fmtr Null = new Bry_fmtr().Fmt_("");
	public static Bry_fmtr tmp_() {return new Bry_fmtr().Fmt_("").Keys_();}
	public static Bry_fmtr new_(String fmt, String... keys) {return new Bry_fmtr().Fmt_(fmt).Keys_(keys);}	// NOTE: keys may seem redundant, but are needed to align ordinals with proc; EX: fmt may be "~{A} ~{B}" or "~{B} ~{A}"; call will always be Bld(a, b); passing in "A", "B" guarantees A is 0 and B is 1;
	public static Bry_fmtr new_(byte[] fmt, String... keys) {return new Bry_fmtr().Fmt_(fmt).Keys_(keys);}	// NOTE: keys may seem redundant, but are needed to align ordinals with proc; EX: fmt may be "~{A} ~{B}" or "~{B} ~{A}"; call will always be Bld(a, b); passing in "A", "B" guarantees A is 0 and B is 1;
	public static Bry_fmtr new_() {return new Bry_fmtr();}
	public static Bry_fmtr keys_(String... keys) {return new Bry_fmtr().Keys_(keys);}
	public static Bry_fmtr new_bry_(byte[] fmt, String... keys) {return new Bry_fmtr().Fmt_(fmt).Keys_(keys);}
	public static String New_fmt_str(String key, Object[] args) {
		tmp_bfr.Clear();
		tmp_bfr.Add_str(key);
		tmp_bfr.Add_byte(Byte_ascii.Colon);
		int args_len = args.length;
		for (int i = 0; i < args_len; i++) {	// add " 0='~{0}'"
			tmp_bfr.Add_byte(Byte_ascii.Space);
			tmp_bfr.Add_int_variable(i);
			tmp_bfr.Add_byte(Byte_ascii.Eq);
			tmp_bfr.Add_byte(Byte_ascii.Apos);
			tmp_bfr.Add_byte(Byte_ascii.Tilde);
			tmp_bfr.Add_byte(Byte_ascii.Curly_bgn);
			tmp_bfr.Add_int_variable(i);
			tmp_bfr.Add_byte(Byte_ascii.Curly_end);
			tmp_bfr.Add_byte(Byte_ascii.Apos);
		}
		return tmp_bfr.Xto_str_and_clear();
	}	static Bry_bfr tmp_bfr = Bry_bfr.reset_(255); 
	public void Bld_bfr_many_and_set_fmt(Object... args) {
		Bry_bfr bfr = Bry_bfr.new_();
		this.Bld_bfr_many(bfr, args);
		byte[] bry = bfr.Xto_bry_and_clear();
		this.Fmt_(bry).Compile();
	}
	public static String Escape_tilde(String v) {return String_.Replace(v, "~", "~~");}
}

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
package gplx.xowa; import gplx.*;
class Pf_xtn_switch extends Pf_func_base {
	@Override public boolean Func_require_colon_arg() {return true;}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {// REF.MW:ParserFunctions_body.php
		int self_args_len = self.Args_len(); if (self_args_len == 0) return;	// no cases; return; effectively "empty"
		byte[] argx = Eval_argx(ctx, src, caller, self);

		boolean fall_thru_found = false, dflt_found = false;
		byte[] found = null; Arg_nde_tkn arg = null; Arg_itm_tkn dflt_val = null;
		ByteAryBfr tmp = ByteAryBfr.new_();
		for (int i = 0; i < self_args_len; i++) {
			arg = self.Args_get_by_idx(i);
			byte[] key = Get_or_eval(ctx, src, caller, self, bfr, arg.Key_tkn(), tmp);
			if (arg.KeyTkn_exists()) {	// = exists
				if		(fall_thru_found || Pf_func_.Eq_(key, argx)) {
					found = Get_or_eval(ctx, src, caller, self, bfr, arg.Val_tkn(), tmp);
					break;
				}
				else if (dflt_found || Pf_func_.Eq_(key, Dflt_keyword)) {
					dflt_val = arg.Val_tkn();
				}
				else {} // argx != key; continue
			}
			else {
				byte[] val = Get_or_eval(ctx, src, caller, self, bfr, arg.Val_tkn(), tmp);
				if		(Pf_func_.Eq_(val, argx))
					fall_thru_found = true;
				else if (Pf_func_.Eq_(key, Dflt_keyword))
					dflt_found = true;
			}
		}
		if (found == null) {
			if		(!arg.KeyTkn_exists())	found = Get_or_eval(ctx, src, caller, self, bfr, arg.Val_tkn(), tmp);
			else if (dflt_val != null)		found = Get_or_eval(ctx, src, caller, self, bfr, dflt_val, tmp);
			else return;
		}
		bfr.Add(found);
	}
	private byte[] Get_or_eval(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bb, Arg_itm_tkn itm, ByteAryBfr tmp) {
		if (itm.Itm_static() == Bool_.Y_byte)
			return ByteAry_.Trim(src, itm.Dat_bgn(), itm.Dat_end());
		else {
			itm.Tmpl_evaluate(ctx, src, caller, tmp);
			return tmp.XtoAryAndClearAndTrim();
		}
	}
	public static final byte[] Dflt_keyword = ByteAry_.new_utf8_("#default");
	@Override public int Id() {return Xol_kwd_grp_.Id_xtn_switch;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_xtn_switch().Name_(name);}
}
class Pf_func_switch_itm {
	public byte[] Key;
	public byte[] Val;
	public Pf_func_switch_itm(byte[] key, byte[] val) {this.Key = key; this.Val = val;}
}

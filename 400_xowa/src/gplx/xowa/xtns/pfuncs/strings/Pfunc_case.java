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
package gplx.xowa.xtns.pfuncs.strings; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
public class Pfunc_case extends Pf_func_base {	// EX: {{lc:A}} -> a
	private boolean first; private int case_type;
	public Pfunc_case(int case_type, boolean first) {this.case_type = case_type; this.first = first;}
	@Override public int Id() {return Xol_kwd_grp_.Id_str_lc;}
	@Override public Pf_func New(int id, byte[] name) {return new Pfunc_case(case_type, first).Name_(name);}
	@Override public boolean Func_require_colon_arg() {return true;}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, Bry_bfr trg) {
		byte[] val_dat_ary = Eval_argx(ctx, src, caller, self); if (val_dat_ary == Bry_.Empty) return;
		int val_dat_ary_len = val_dat_ary.length; if (val_dat_ary_len == 0) return; // nothing to uc / lc; just return
		Xol_lang lang = ctx.Wiki().Lang();
		boolean upper = case_type == Xol_lang.Tid_upper;
		if (first) {
			Bry_bfr tmp_bfr = ctx.App().Utl_bry_bfr_mkr().Get_b512();
			val_dat_ary = lang.Case_mgr().Case_build_1st(tmp_bfr, upper, val_dat_ary, 0, val_dat_ary_len);
			tmp_bfr.Mkr_rls();
		}
		else
			val_dat_ary = lang.Case_mgr().Case_build(upper, val_dat_ary, 0, val_dat_ary_len);
		trg.Add(val_dat_ary);
	}
}	

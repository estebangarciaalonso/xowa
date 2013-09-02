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
public class Pf_str_formatnum extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_str_formatnum;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_str_formatnum().Name_(name);}
	@Override public boolean Func_require_colon_arg() {return true;}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bb) {
		Xol_lang lang = ctx.Wiki().Lang();
		int self_args_len = self.Args_len();
		byte[] val_dat_ary = Eval_argx(ctx, src, caller, self);
		byte[] arg1 = Pf_func_.EvalArgOrEmptyAry(ctx, src, caller, self, self_args_len, 0);
		bb.Add(Format_num(lang, val_dat_ary, arg1));
	}	static ByteTrieMgr_slim trie;
	public static byte[] Format_num(Xol_lang lang, byte[] num, byte[] option_raw) {
		if (trie == null) trie = Xol_kwd_mgr.trie_(lang.Kwd_mgr(), Xol_kwd_grp_.Id_str_rawsuffix);
		return option_raw == ByteAry_.Empty || trie.MatchAtCurExact(option_raw, 0, option_raw.length) == null
			? lang.Num_fmt_mgr().Fmt(num) : lang.Num_fmt_mgr().Raw(num);
	}
}	

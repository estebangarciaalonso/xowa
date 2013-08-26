/*
XOWA: the extensible offline wiki application
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
class Pf_xtn_expr_shunter {
	ByteTrieMgr_fast trie = expression_();
	ValStack val_stack = new ValStack();
	PrcStack prc_stack = new PrcStack();
	public static final DecimalAdp Null_rslt = null;
	public ByteAryBfr Err() {return err_bfr;} ByteAryBfr err_bfr = ByteAryBfr.new_();
	public DecimalAdp Err_set(Xop_ctx ctx, int msgId) {return Err_set(ctx, msgId, ByteAry_.Empty);}
	public DecimalAdp Err_set(Xop_ctx ctx, int msg_id, byte[] arg) {
		byte[] msg_val = ctx.Wiki().Msg_mgr().Val_by_id(msg_id);
		err_bfr.Clear().Add(Err_bgn_ary);
		tmp_fmtr.Fmt_(msg_val).Bld_bfr_one(err_bfr, arg);
		err_bfr.Add(Err_end_ary);
		return Null_rslt;
	}	static final byte[] Err_bgn_ary = ByteAry_.new_ascii_("<strong class=\"error\">"), Err_end_ary = ByteAry_.new_ascii_("</strong>"); ByteAryFmtr tmp_fmtr = ByteAryFmtr.tmp_();
	public void Rslt_set(byte[] bry) {
		err_bfr.Add(bry);
	}
	public DecimalAdp Evaluate(Xop_ctx ctx, byte[] src) {	// REF.MW: Expr.php
		int src_len = src.length; if (src_len == 0) return Null_rslt;
		int cur_pos = 0; byte cur_byt = src[0];
		boolean mode_expr = true; PrcTkn prv_prc = null;
		val_stack.Clear(); prc_stack.Clear();
		while (true) {
			// can't think of a way for this to happen; note that operators will automatically push values/operators off stack that are lower; can't get up to 100 
			// if (val_stack.Len() > 100 || prc_stack.Len() > 100) return Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_err__stack_exhausted);
			Object o = trie.Match(cur_byt, src, cur_pos, src_len);
			int bgn_pos = cur_pos;
			if (o == null) {	// letter or unknown symbol
				while (cur_pos < src_len) {
					byte b = src[cur_pos++];
					if (Byte_ascii.Is_ltr(b))
						continue;
					else
						break;
				}
				return Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_unrecognised_word, ByteAry_.Mid(src, bgn_pos, cur_pos));
			}
			else {
				ExprTkn t = (ExprTkn)o;
				cur_pos = trie.Match_pos();
				switch (t.TypeId()) {
					case ExprTkn_.TypeId_space: break;
					case ExprTkn_.TypeId_number:
						if (!mode_expr) return Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_unexpected_number);
						int numBgn = cur_pos - 1;
						boolean loop = true;
						while (loop) {
							if (cur_pos == src_len) break;
							switch (src[cur_pos]) {
								case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
								case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
								case Byte_ascii.Dot:
									++cur_pos;
									break;
								default: loop = false; break;
							}
						}
						DecimalAdp num = Null_rslt;
						try {num = ByteAry_.XtoDecimalByPos(src, numBgn, cur_pos);}
						catch (Exception exc) {
							// NOTE: PATCH.PHP: 65.5.5 can evaluate to 65.5; EX "{{Geological eras|-600|height=2|border=none}}" eventually does "|10-to={{#ifexpr:{{{1|-4567}}}<-65.5|-65.5|{{{1}}}}}.5" which is 65.5.5
							Err_.Noop(exc); 
							int dot_count = 0;
							for (int i = numBgn; i < cur_pos; i++) {
								if (src[i] == Byte_ascii.Dot) {
									switch (dot_count) {
										case 0: dot_count = 1; break;
										case 1: 
											try {
												num = ByteAry_.XtoDecimalByPos(src, numBgn, i);
											}
											catch (Exception exc_inner) {Err_.Noop(exc_inner);}
											break;
									}
								}
							}
							if (num == null) return Null_rslt;
						}
						val_stack.Push(num);
						mode_expr = false;
						break;
					case ExprTkn_.TypeId_paren_lhs:
						if (!mode_expr) return Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_unexpected_operator, ByteAry_.new_ascii_("("));
						prc_stack.Push((PrcTkn)t);
						break;
					case ExprTkn_.TypeId_operator:
						PrcTkn cur_prc = (PrcTkn)t;
						if (Byte_ascii.Is_ltr(cur_byt)) {
							int nxt_pos = Xop_lxr_.FindNonLetter(src, cur_pos, src_len);
							if (nxt_pos > cur_pos)
								return Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_unrecognised_word, ByteAry_.Mid(src, bgn_pos, nxt_pos));
						}
						if (mode_expr) {	// NOTE: all the GetAlts have higher precedence; "break;" need to skip evaluation below else will fail for --1
							PrcTkn alt_prc = cur_prc.GetAlt();
//								if (alt_prc == cur_prc && cur_prc.ArgCount() > 1) return Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_err__unexpected_operator);
							prc_stack.Push(alt_prc);
							break;
						}
						if (cur_prc.ArgCount() == 0) return Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_unexpected_number);	// NOTE: mode_expr is false, so expecting operator but got pi/e instead 
						prv_prc = prc_stack.GetLast();
						while (prv_prc != null && (cur_prc.Precedence() <= prv_prc.Precedence())) {
							if (!prv_prc.Calc(ctx, this, val_stack)) return Null_rslt;
							prc_stack.Pop();
							prv_prc = prc_stack.GetLast();
						}
						prc_stack.Push(cur_prc);
						mode_expr = true;
						break;
					case ExprTkn_.TypeId_paren_rhs: {
						prv_prc = prc_stack.GetLast();
						while (prv_prc != null && prv_prc.TypeId() != ExprTkn_.TypeId_paren_lhs) {
							if (!prv_prc.Calc(ctx, this, val_stack)) return Null_rslt;
							prc_stack.Pop();
							prv_prc = prc_stack.GetLast();
						}
						if (prv_prc == ParenBgnTkn._)
							prc_stack.Pop();
						else
							return Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_unexpected_closing_bracket);
						mode_expr = false;
						break;
					}
				}
			}
			if (cur_pos == src_len) break;
			cur_byt = src[cur_pos];
		}
		while (prc_stack.Len() > 0) {
			PrcTkn cur_prc = prc_stack.Pop();
			if (cur_prc.TypeId() == ExprTkn_.TypeId_paren_lhs) return Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_unclosed_bracket);
			if (!cur_prc.Calc(ctx, this, val_stack)) return Null_rslt;
		}
		return val_stack.Len() == 0 ? Null_rslt : val_stack.Pop();	// HACK: for [[List of Premiers of South Australia by time in office]] and {{#expr:\n{{age in days
	}
	private static ByteTrieMgr_fast expression_() {
		ByteTrieMgr_fast rv = ByteTrieMgr_fast.ci_();
		Trie_add(rv, new WsTkn(Byte_ascii.Space));
		Trie_add(rv, new WsTkn(Byte_ascii.Tab));
		Trie_add(rv, new WsTkn(Byte_ascii.NewLine));
		Trie_add(rv, ParenBgnTkn._);
		Trie_add(rv, ParenEndTkn._);
		Trie_add(rv, new PrcTkn_plus("+"));
		Trie_add(rv, new PrcTkn_minus("-"));
		Trie_add(rv, new PrcTkn_minus(Char_.XtoStr((char)8722)));
		Trie_add(rv, new PrcTkn_times("*"));
		Trie_add(rv, new PrcTkn_divide("/"));
		Trie_add(rv, new PrcTkn_divide("div"));
		Trie_add(rv, new PrcTkn_pow("^"));
		Trie_add(rv, new PrcTkn_mod("mod"));
		Trie_add(rv, new PrcTkn_eq("="));
		Trie_add(rv, new PrcTkn_neq("<>"));
		Trie_add(rv, new PrcTkn_neq("!="));
		Trie_add(rv, new PrcTkn_gt(">"));
		Trie_add(rv, new PrcTkn_lt("<"));
		Trie_add(rv, new PrcTkn_gte(">="));
		Trie_add(rv, new PrcTkn_lte("<="));
		Trie_add(rv, new PrcTkn_and("and"));
		Trie_add(rv, new PrcTkn_or("or"));
		Trie_add(rv, new PrcTkn_not("not"));
		Trie_add(rv, new PrcTkn_e_op("e"));
		Trie_add(rv, new PrcTkn_pi("pi"));
		Trie_add(rv, new PrcTkn_ceil("ceil"));
		Trie_add(rv, new PrcTkn_trunc("trunc"));
		Trie_add(rv, new PrcTkn_floor("floor"));
		Trie_add(rv, new PrcTkn_abs("abs"));
		Trie_add(rv, new PrcTkn_exp("exp"));
		Trie_add(rv, new PrcTkn_ln("ln"));
		Trie_add(rv, new PrcTkn_sin("sin"));
		Trie_add(rv, new PrcTkn_cos("cos"));
		Trie_add(rv, new PrcTkn_tan("tan"));
		Trie_add(rv, new PrcTkn_asin("asin"));
		Trie_add(rv, new PrcTkn_acos("acos"));
		Trie_add(rv, new PrcTkn_atan("atan"));
		Trie_add(rv, new PrcTkn_round("round"));
		Trie_add(rv, new NumTkn(0));
		Trie_add(rv, new NumTkn(1));
		Trie_add(rv, new NumTkn(2));
		Trie_add(rv, new NumTkn(3));
		Trie_add(rv, new NumTkn(4));
		Trie_add(rv, new NumTkn(5));
		Trie_add(rv, new NumTkn(6));
		Trie_add(rv, new NumTkn(7));
		Trie_add(rv, new NumTkn(8));
		Trie_add(rv, new NumTkn(9));
		Trie_add(rv, new DotTkn());
		Trie_add(rv, new PrcTkn_gt("&gt;"));
		Trie_add(rv, new PrcTkn_lt("&lt;"));
		Trie_add(rv, new PrcTkn_minus("&minus;"));
		return rv;
	}
	private static void Trie_add(ByteTrieMgr_fast trie, ExprTkn tkn) {trie.Add(tkn.Val_ary(), tkn);}
	public static final Pf_xtn_expr_shunter _ = new Pf_xtn_expr_shunter(); Pf_xtn_expr_shunter() {}
}

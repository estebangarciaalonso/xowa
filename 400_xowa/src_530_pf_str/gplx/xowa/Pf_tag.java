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
class Pf_tag extends Pf_func_base {
	@Override public boolean Func_require_colon_arg() {return true;}
	public static final byte[] Bry_tag_hdr = ByteAry_.new_ascii_("xowa_tag_");
	public static final int Bry_tag_hdr_len = Bry_tag_hdr.length;
	private static final byte[] Bry_tag_hdr_lhs = ByteAry_.new_ascii_("<xowa_tag_"), Bry_tag_hdr_rhs = ByteAry_.new_ascii_("</xowa_tag_");
	public void Func_evaluate2(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {
		byte[] tag_name = Eval_argx(ctx, src, caller, self); if (tag_name.length == 0) return;
		int args_len = self.Args_len();
		ByteAryBfr tmp = ctx.App().Utl_bry_bfr_mkr().Get_b512();
		try {
			int tag_idx = ctx.Tag_idx++;
			tmp.Add(Bry_tag_hdr_lhs).Add_int_variable(tag_idx).Add_byte(Byte_ascii.Gt);
			tmp.Add_byte(Byte_ascii.Lt).Add(tag_name);	// DEFER: tag_name should be lowered; ISSUE: adds more complication (language-specific/performance); EXCUSE: xml node names are case-insensitive
			if (args_len > 1) {
				for (int i = 1; i < args_len; i++) {
					byte[] arg = Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, args_len, i);
					if (arg.length == 0) continue;	// if atr is empty, skip; see test
					tmp.Add_byte(Byte_ascii.Space);
					if (!AddHtmlArg(arg, tmp)) {
						ctx.Msg_log().Add_itm_none(Xop_tag_log.Invalid, arg, 0, arg.length);
						tmp.Clear();
	//						return;
					}
				}
			}
			tmp.Add_byte(Byte_ascii.Gt);
			if (args_len > 0) {	// FUTURE: trim should not be called on content; WHEN: adding src[] back to tmpl_eval  
				tmp.Add(Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, args_len, 0));
			}
			tmp.Add_byte(Byte_ascii.Lt).Add_byte(Byte_ascii.Slash).Add(tag_name).Add_byte(Byte_ascii.Gt);
			tmp.Add(Bry_tag_hdr_rhs).Add_int_variable(tag_idx).Add_byte(Byte_ascii.Gt);
			bfr.Add_bfr_and_clear(tmp);
		}
		finally {tmp.Mkr_rls();}
	}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {
		byte[] tag_name = Eval_argx(ctx, src, caller, self); if (tag_name.length == 0) return;
		int args_len = self.Args_len();
		ByteAryBfr tmp = ctx.App().Utl_bry_bfr_mkr().Get_b512();
		try {
			tmp.Add_byte(Byte_ascii.Lt).Add(tag_name);	// DEFER: tag_name should be lowered; ISSUE: adds more complication (language-specific/performance); EXCUSE: xml node names are case-insensitive
			if (args_len > 1) {
				for (int i = 1; i < args_len; i++) {
					byte[] arg = Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, args_len, i);
					if (arg.length == 0) continue;	// if atr is empty, skip; see test
					tmp.Add_byte(Byte_ascii.Space);
					if (!AddHtmlArg(arg, tmp)) {
						ctx.Msg_log().Add_itm_none(Xop_tag_log.Invalid, arg, 0, arg.length);
						tmp.Clear();
					}
				}
			}
			tmp.Add_byte(Byte_ascii.Gt);
			if (args_len > 0) {
				byte[] content = Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, args_len, 0);
				Xop_ctx sub_ctx = Xop_ctx.new_sub_(ctx.Wiki()); Xop_tkn_mkr sub_tkn_mkr = sub_ctx.Tkn_mkr();
				content = ctx.Wiki().Parser().Parse_page_tmpl(sub_tkn_mkr.Root(content), sub_ctx, sub_tkn_mkr, content);	// NOTE: force reparse; needed for {{#tag:ref|{{A{{!}}B}}}}; EX: de.wikipedia.org/wiki/Freiburg_im_Breisgau; DATE:2013-12-18
				tmp.Add(content);
			}
			tmp.Add_byte(Byte_ascii.Lt).Add_byte(Byte_ascii.Slash).Add(tag_name).Add_byte(Byte_ascii.Gt);
			bfr.Add_bfr_and_clear(tmp);
		}
		finally {tmp.Mkr_rls();}
	}	
	boolean AddHtmlArg(byte[] src, ByteAryBfr tmp) {
		ParseKeyVal(src, kv_bldr);
		//if (!kv_bldr.Valid()) return false;
		if (kv_bldr.Key_bgn() != -1)
			tmp.Add(ByteAry_.Mid(src, kv_bldr.Key_bgn(), kv_bldr.KeyEnd()));
		if (kv_bldr.Val_bgn() != -1) {
			if (kv_bldr.Key_bgn() != -1) {
				tmp.Add_byte(Byte_ascii.Eq);
			}
			tmp.Add_byte(Byte_ascii.Quote);
			tmp.Add(ByteAry_.Mid(src, kv_bldr.Val_bgn(), kv_bldr.Val_end()));
			tmp.Add_byte(Byte_ascii.Quote);
		}
		kv_bldr.Clear();
		return true;
	}	KeyValBldr kv_bldr = new KeyValBldr();
	private static void ParseKeyVal(byte[] src, KeyValBldr kv_bldr) {
		int itm_bgn = -1, itm_end = -1, src_len = src.length;
		boolean mode_is_key = true;
		for (int i = 0; i < src_len; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Eq:
					if (mode_is_key) {
						mode_is_key = false;
						if (itm_end == -1) itm_end = i;
						kv_bldr.Key_rng_(itm_bgn, itm_end);
						itm_bgn = itm_end = -1;
					}					
					break;
				case Byte_ascii.Quote:
				case Byte_ascii.Apos:	// NOTE: quotes cannot be escaped; regx takes first two quotes; REF:MW:CoreParserFunctions.php|tagObj
					if (itm_bgn == -1)
						itm_bgn = i + 1;
					else if (itm_end == -1)
						itm_end = i;
					break;
				case Byte_ascii.Space: case Byte_ascii.Tab: case Byte_ascii.NewLine:// NOTE: do not need to handle ws, b/c argBldr will trim it EX: {{#tag|a| b = c }}; " b " and " c " are automatically trimmed
					break;
				default:
					if		(itm_bgn == -1)	itm_bgn = i;
					break;
			}
		}
		if (itm_end == -1) itm_end = src_len;
		kv_bldr.Val_rng_(itm_bgn, itm_end);
	}
	@Override public int Id() {return Xol_kwd_grp_.Id_misc_tag;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_tag().Name_(name);}
}
class KeyValBldr {
	public int Key_bgn() {return key_bgn;} private int key_bgn;
	public int KeyEnd() {return key_end;} private int key_end;
	public KeyValBldr Key_rng_(int bgn, int end) {key_bgn = bgn; key_end = end; return this;}
	public int Val_bgn() {return val_bgn;} private int val_bgn;
	public int Val_end() {return val_end;} private int val_end;
	public KeyValBldr Val_rng_(int bgn, int end) {val_bgn = bgn; val_end = end; return this;}
	public boolean Valid() {
		return key_bgn != -1 && key_end != -1 && val_bgn != -1 && val_end != -1 && key_bgn <= key_end && val_bgn <= val_end;
	}
	public void Clear() {
		key_bgn = key_end = val_bgn = val_end = -1;
	}
}

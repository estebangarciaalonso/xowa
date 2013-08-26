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
public class Pf_url_anchorencode extends Pf_func_base {	// EX: {{anchorencode:a b}} -> a+b
	@Override public boolean Func_require_colon_arg() {return true;}
	@Override public int Id() {return Xol_kwd_grp_.Id_url_anchorencode;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_url_anchorencode().Name_(name);}
	public static void Func_init(Xop_ctx ctx) {
		if (anchorCtx != null) return;// NOTE: called by Scrib_uri
		encode_trie.Add(Byte_ascii.Colon, ByteAryFmtrArg_.byt_(Byte_ascii.Colon));
		encode_trie.Add(Byte_ascii.Space, ByteAryFmtrArg_.byt_(Byte_ascii.Underline));
		anchorCtx = Xop_ctx.new_sub_(ctx.Wiki());
		anchorCtx.Para().Enabled_n_();
		anchorTknMkr = anchorCtx.Tkn_mkr();
		anchorParser = Xop_parser.anchorencode_(ctx.Wiki());
	}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {
		if (anchorCtx == null) Func_init(ctx);
		byte[] val_ary = Eval_argx(ctx, src, caller, self); if (val_ary == ByteAry_.Empty) return;
		AnchorEncode(val_ary, bfr, ctx.App().Utl_bry_bfr_mkr().Get_b512().Mkr_rls());
	}		
	public static void AnchorEncode(byte[] src, ByteAryBfr bfr, ByteAryBfr tmp_bfr) {
		Xop_root_tkn root = anchorCtx.Tkn_mkr().Root(src);
		anchorParser.Parse_page_wiki(root, anchorCtx, anchorTknMkr, src, Xop_parser_.Doc_bgn_bos);
		int subs_len = root.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Xop_tkn_itm sub = root.Subs_get(i);
			Tkn(src, sub, root, i, tmp_bfr);
		}
		byte[] unencoded = tmp_bfr.XtoAryAndClear();
		encoder.Encode(tmp_bfr, unencoded);
		bfr.Add_bfr_and_clear(tmp_bfr);
	}
	private static Url_encoder encoder = Url_encoder.new_html_id_();
	private static void Tkn(byte[] src, Xop_tkn_itm sub, Xop_tkn_grp grp, int sub_idx, ByteAryBfr tmp_bfr) {
		switch (sub.Tkn_tid()) {
			case Xop_tkn_itm_.Tid_lnke: Lnke(src, (Xop_lnke_tkn)sub, tmp_bfr); break;	// FUTURE: need to move number to lnke_tkn so that number will be correct/consistent? 
			case Xop_tkn_itm_.Tid_lnki: Lnki(src, (Xop_lnki_tkn)sub, tmp_bfr); break;
			case Xop_tkn_itm_.Tid_apos: break; // noop
			case Xop_tkn_itm_.Tid_xnde: Xnde(src, (Xop_xnde_tkn)sub, tmp_bfr); break;
			case Xop_tkn_itm_.Tid_html_ncr: tmp_bfr.Add_utf8_int(((Xop_html_ncr_tkn)sub).Html_ncr_val()); break;
			case Xop_tkn_itm_.Tid_html_ref: tmp_bfr.Add_utf8_int(((Xop_html_ref_tkn)sub).HtmlRef_val()); break;
			default: tmp_bfr.Add_mid(src, sub.Src_bgn_grp(grp, sub_idx), sub.Src_end_grp(grp, sub_idx)); break;
		}		
	}
	private static void Lnke(byte[] src, Xop_lnke_tkn lnke, ByteAryBfr tmp_bfr) {
		int subs_len = lnke.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Xop_tkn_itm lnke_sub = lnke.Subs_get(i);
			tmp_bfr.Add_mid(src, lnke_sub.Src_bgn_grp(lnke, i), lnke_sub.Src_end_grp(lnke, i));
		}
	}
	private static void Lnki(byte[] src, Xop_lnki_tkn lnki, ByteAryBfr tmp_bfr) {
		int src_end = lnki.Src_end();
		int trg_end = lnki.Trg_tkn().Src_end();
		
		if (trg_end == src_end - Xop_tkn_.Lnki_end_len) {	// only trg
			tmp_bfr.Add_mid(src, lnki.Trg_tkn().Src_bgn(), trg_end);			
		}
		else {
			tmp_bfr.Add_mid(src, trg_end + 1, src_end - Xop_tkn_.Lnki_end_len); //+1 is len of pipe
		}
	}
	private static void Xnde(byte[] src, Xop_xnde_tkn xnde, ByteAryBfr tmp_bfr) {
		int subs_len = xnde.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Tkn(src, xnde.Subs_get(i), xnde, i, tmp_bfr);
		}		
	}
	private static ByteTrieMgr_fast encode_trie = ByteTrieMgr_fast.cs_();
	private static Xop_ctx anchorCtx;  static Xop_tkn_mkr anchorTknMkr;
	private static Xop_parser anchorParser; 
}

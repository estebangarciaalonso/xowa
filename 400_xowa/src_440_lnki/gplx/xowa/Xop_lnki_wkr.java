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
import gplx.xowa.parsers.lnkis.*;
public class Xop_lnki_wkr implements Xop_ctx_wkr, Xop_arg_wkr {
	public void Ctor_ctx(Xop_ctx ctx) {}
	public void Page_bgn(Xop_ctx ctx) {}
	public void Page_end(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int srcLen) {}
	public Xop_lnki_logger File_wkr() {return file_wkr;} public Xop_lnki_wkr File_wkr_(Xop_lnki_logger v) {file_wkr = v; return this;} private Xop_lnki_logger file_wkr;
	public Xop_lnki_logger Ctg_wkr() {return ctg_wkr;} public Xop_lnki_wkr Ctg_wkr_(Xop_lnki_logger v) {ctg_wkr = v; return this;} private Xop_lnki_logger ctg_wkr;
	public void AutoClose(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos, Xop_tkn_itm tkn) {
		Xop_lnki_tkn lnki = (Xop_lnki_tkn)tkn;
		ctx.Msg_log().Add_itm_none(Xop_misc_log.Eos, src, lnki.Src_bgn(), lnki.Src_end());	// FUTURE: autoclose
		lnki.TypeId_toText();
	}
	private static Arg_bldr arg_bldr = Arg_bldr._;
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {
		if (ctx.Cur_tkn_tid() == Xop_tkn_itm_.Tid_lnke) {	// if lnke then take 1st ] in "]]" and use it close lnke
			int lnke_end_pos = bgnPos + 1;
			ctx.Lnke().MakeTkn_end(ctx, tkn_mkr, src, srcLen, bgnPos, lnke_end_pos);
			return lnke_end_pos;
		}
		int stackPos = ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_lnki);
		if (stackPos == -1) {return ctx.LxrMake_txt_(curPos);}
		Xop_lnki_tkn lnki = (Xop_lnki_tkn)ctx.Stack_pop_til(stackPos, false, bgnPos, curPos);
		lnki.Src_end_(curPos);	// NOTE: must happen after ChkForTail
		int loop_bgn = lnki.Tkn_sub_idx() + 1, loop_end = root.Subs_len();
		if (loop_bgn == loop_end) {	// NOTE: fixes empty template; EX: [[]]
			lnki.TypeId_toText();
			return curPos;
		}
		if (!arg_bldr.Bld(ctx, tkn_mkr, this, Xop_arg_wkr_.Typ_lnki, root, lnki, bgnPos, curPos, loop_bgn, loop_end, src)) { // NOTE: fixes blank template; EX: [[ ]]
			lnki.TypeId_toText();
			root.Subs_del_after(lnki.Tkn_sub_idx() + 1);	// all tkns should now be converted to args in owner; delete everything in root
			lnki.Subs_clear();
			return curPos;
		}
		curPos = this.ChkForTail(ctx.Lang(), src, curPos, srcLen, lnki);
		lnki.Src_end_(curPos);	// NOTE: must happen after ChkForTail; redundant with above, but above needed b/c of returns
		root.Subs_del_after(lnki.Tkn_sub_idx() + 1);	// all tkns should now be converted to args in owner; delete everything in root
		switch (lnki.NmsId()) {
			case Xow_ns_.Id_file:
				switch (lnki.Lnki_type()) {
					case Xop_lnki_type.Id_thumb:
					case Xop_lnki_type.Id_frame:
						ctx.Para().Process_lnki_file_div(lnki.Src_bgn(), curPos);
						break;
				}
				if (file_wkr != null) file_wkr.Wkr_exec(ctx, lnki);
				break;
			case Xow_ns_.Id_category:
				if (!lnki.Ttl().ForceLiteralLink())					// NOTE: do not remove ws if literal; EX:[[Category:A]]\n[[Category:B]] should stay the same; DATE:2013-07-10
					ctx.Para().Process_lnki_category(ctx, curPos);	// removes excessive ws between categories; EX: [[Category:A]]\n\s[[Category:B]] -> [[Category:A]][[Category:B]] (note that both categories will not be rendered directly in html, but go to the bottom of the page)
				if (ctg_wkr != null) ctg_wkr.Wkr_exec(ctx, lnki);
				break;
		}
		return curPos;
	}	static byte[] RelPath_bry = ByteAry_.new_utf8_("../");
	public boolean Args_add(Xop_ctx ctx, Xop_tkn_itm tkn, Arg_nde_tkn arg, int arg_idx) {
		Xop_lnki_tkn lnki = (Xop_lnki_tkn)tkn;
		byte[] src = ctx.Src();
		try {
			if (arg_idx == 0) {							// 1st arg; assume trg; process ns;
				if (arg.Val_tkn().Dat_end() - arg.Val_tkn().Dat_bgn() == 0) {	// blank trg; EX: [[]],[[ ]]
					lnki.TypeId_toText();
				}
				else {
					Arg_itm_tkn name_tkn = arg.Val_tkn();
					byte[] name_bry = ByteAry_.Mid(src, name_tkn.Dat_bgn(), name_tkn.Dat_end());
					name_bry = ctx.App().Url_converter_url_ttl().Decode(name_bry);
					int name_bry_len = name_bry.length;
					if (ctx.Page().Page_ttl().Ns().Subpages_enabled()
						&& Pf_xtn_rel2abs.Rel2abs_ttl(name_bry, 0, name_bry_len)) { // Linker.php|normalizeSubpageLink
						ByteAryBfr tmp_bfr = ctx.App().Utl_bry_bfr_mkr().Get_b512();
						name_bry = Pf_xtn_rel2abs.Rel2abs(tmp_bfr, name_bry, ctx.Page().Page_ttl().Raw());
						tmp_bfr.Mkr_rls();
					}
					Xoa_ttl ttl = Xoa_ttl.parse_(ctx.Wiki(), name_bry);
					//Xoa_ttl ttl = Xoa_ttl.new_(ctx.Wiki(), ctx.Msg_log(), name_bry, src, name_tkn.Dat_bgn(), name_tkn.Dat_end());
					if (ttl == null) {lnki.TypeId_toText(); return false;}
					lnki.Ttl_(ttl);
					lnki.NmsId_(lnki.Ttl().Ns().Id());
					lnki.Trg_tkn_(arg);
				}
			}
			else {									// nth arg; guess arg type
				int arg_tid = -1;
				int bgn = arg.Val_tkn().Dat_bgn(), end = arg.Val_tkn().Dat_end();
				if (arg.KeyTkn_exists()) {bgn = arg.Key_tkn().Dat_bgn(); end = arg.Key_tkn().Dat_end();}
				arg_tid = ctx.Wiki().Lang().Lnki_arg_parser().Identify_tid(src, bgn, end, lnki.Width(), lnki.Height());
				switch (arg_tid) {
					case Xol_lnki_arg_parser.Tid_none:			lnki.HAlign_(Xop_lnki_type.Id_none); break;
					case Xol_lnki_arg_parser.Tid_border:		lnki.Border_(Bool_.Y_byte); break;
					case Xol_lnki_arg_parser.Tid_thumb:			lnki.Lnki_type_(Xop_lnki_type.Id_thumb); break;
					case Xol_lnki_arg_parser.Tid_frame:			lnki.Lnki_type_(Xop_lnki_type.Id_frame); break;
					case Xol_lnki_arg_parser.Tid_frameless:		lnki.Lnki_type_(Xop_lnki_type.Id_frameless); break;
					case Xol_lnki_arg_parser.Tid_left:			lnki.HAlign_(Xop_lnki_halign.Left); break;
					case Xol_lnki_arg_parser.Tid_center:		lnki.HAlign_(Xop_lnki_halign.Center); break;
					case Xol_lnki_arg_parser.Tid_right:			lnki.HAlign_(Xop_lnki_halign.Right); break;
					case Xol_lnki_arg_parser.Tid_top:			lnki.VAlign_(Xop_lnki_valign.Top); break;
					case Xol_lnki_arg_parser.Tid_middle:		lnki.VAlign_(Xop_lnki_valign.Middle); break;
					case Xol_lnki_arg_parser.Tid_bottom:		lnki.VAlign_(Xop_lnki_valign.Bottom); break;
					case Xol_lnki_arg_parser.Tid_super:			lnki.VAlign_(Xop_lnki_valign.Super); break;
					case Xol_lnki_arg_parser.Tid_sub:			lnki.VAlign_(Xop_lnki_valign.Sub); break;
					case Xol_lnki_arg_parser.Tid_text_top:		lnki.VAlign_(Xop_lnki_valign.TextTop); break;
					case Xol_lnki_arg_parser.Tid_text_bottom:	lnki.VAlign_(Xop_lnki_valign.TextBottom); break;
					case Xol_lnki_arg_parser.Tid_baseline:		lnki.VAlign_(Xop_lnki_valign.Baseline); break;
					case Xol_lnki_arg_parser.Tid_alt:			lnki.Alt_tkn_(arg); 
						lnki.Alt_tkn().Tkn_ini_pos(false, arg.Src_bgn(), arg.Src_end());
						break;
					case Xol_lnki_arg_parser.Tid_caption:
						lnki.Caption_tkn_(arg);
						if (arg.Eq_tkn() != Xop_tkn_null.Null_tkn) {	// equal tkn exists; add val tkns to key and then swap key with val
							Arg_itm_tkn key_tkn = arg.Key_tkn(), val_tkn = arg.Val_tkn();
							key_tkn.Subs_add(arg.Eq_tkn());
							for (int i = 0; i < val_tkn.Subs_len(); i++) {
								Xop_tkn_itm sub = val_tkn.Subs_get(i);
								key_tkn.Subs_add(sub);
							}
							key_tkn.Dat_end_(val_tkn.Dat_end());
							val_tkn.Subs_clear();
							arg.Key_tkn_(Arg_itm_tkn_null.Null_arg_itm);
							arg.Val_tkn_(key_tkn);
						}
						else	// no equal tkn 
							lnki.Caption_tkn_pipe_trick_(end - bgn == 0);	// NOTE: pipe_trick check must go here; checks for val_tkn.Bgn == val_tkn.End; if there is an equal token but no val, then Bgn == End which would trigger false pipe trick (EX:"[[A|B=]]")
						break;
					case Xol_lnki_arg_parser.Tid_link:			lnki.Link_tkn_(arg); break;
					case Xol_lnki_arg_parser.Tid_dim:			break;// NOOP: Fetch does actual setting
					case Xol_lnki_arg_parser.Tid_upright:
						if (arg.KeyTkn_exists()) {
							int valTknBgn = arg.Val_tkn().Src_bgn(), valTknEnd = arg.Val_tkn().Src_end();
							numberParser.Parse(src, valTknBgn, valTknEnd);
							if (numberParser.HasErr())
								ctx.Msg_log().Add_itm_none(Xop_lnki_log.Upright_val_is_invalid, src, valTknBgn, valTknEnd);
							else
								lnki.Upright_(numberParser.AsDec().XtoDouble());
						}
						else	// no =; EX: [[Image:a|upright]]
							lnki.Upright_(1);
						break;
					case Xol_lnki_arg_parser.Tid_noicon:			lnki.Media_icon_n_();  break;
					case Xol_lnki_arg_parser.Tid_thumbtime:		{
						int valTknBgn = arg.Val_tkn().Src_bgn(), valTknEnd = arg.Val_tkn().Src_end();
						byte[] bry = ByteAry_.Trim(src, valTknBgn, valTknEnd);	// some tkns have trailing space; EX.WWI: [[File:Bombers of WW1.ogg|thumb |thumbtime=3]]
						numberParser.Parse(bry, 0, bry.length);
						if (numberParser.HasErr())
							ctx.Msg_log().Add_itm_none(Xop_lnki_log.Upright_val_is_invalid, src, valTknBgn, valTknEnd);
						else
							lnki.Thumbtime_(numberParser.AsInt());
						break;
					}
				}
			}
			return true;
		} catch (Exception e) {
			ctx.App().Usr_dlg().Warn_many("", "", "fatal error in lnki: page=~{0} src=~{1} err=~{2}", String_.new_utf8_(ctx.Page().Page_ttl().Full_db()), String_.new_utf8_(src, lnki.Src_bgn(), lnki.Src_end()), Err_.Message_gplx(e));
			return false;
		}
	}
	public int ChkForTail(Xol_lang lang, byte[] src, int curPos, int srcLen, Xop_lnki_tkn lnki) {
		int bgnPos = curPos;
		ByteTrieMgr_slim lnki_trail = lang.Lnki_trail_mgr().Trie();
		while (true) {	// loop b/c there can be multiple consecutive lnki_trail_chars; EX: [[A]]bcde
			if (curPos == srcLen) break;
			byte[] lnki_trail_bry = (byte[])lnki_trail.Match(src[curPos], src, curPos, srcLen);
			if (lnki_trail_bry == null) break;	// no longer a lnki_trail char; stop
			curPos += lnki_trail_bry.length;	// lnki_trail char; add
		}
		if (bgnPos != curPos && lnki.NmsId() == Xow_ns_.Id_main) {	// only mark trail if Main ns (skip trail for Image)
			lnki.Tail_bgn_(bgnPos).Tail_end_(curPos);
			return curPos;
		}
		else
			return bgnPos;
	}
	NumberParser numberParser = new NumberParser();
}

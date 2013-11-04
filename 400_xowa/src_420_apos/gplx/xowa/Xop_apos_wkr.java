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
public class Xop_apos_wkr implements Xop_ctx_wkr {
	public Xop_apos_dat Dat() {return dat;} private Xop_apos_dat dat = new Xop_apos_dat();
	public void Ctor_ctx(Xop_ctx ctx) {}
	public void Page_bgn(Xop_ctx ctx) {
		Reset();
	}	ListAdp stack = ListAdp_.new_(); int boldCount, italCount; Xop_apos_tkn dualTkn = null;
	public void Page_end(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int srcLen) {
		this.EndFrame(ctx, src, srcLen, false);
	}
	public int Stack_len() {return stack.Count();}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {
		int aposLen = Apos_len;
		while (curPos < srcLen) {
			byte cur = src[curPos];
			if (cur != Byte_ascii.Apos) break;
			++aposLen; ++curPos;
		}
		dat.Ident(ctx, aposLen, curPos);
		Xop_apos_tkn aposTkn = tkn_mkr.Apos(bgnPos, curPos, curPos - bgnPos, dat.Typ(), dat.Cmd(), dat.LitApos(), ctx.Cur_tkn_tid());
		ctx.Subs_add(aposTkn);
		ctx.Apos().RegTkn(aposTkn, curPos);
		return curPos;
	}	private static final int Apos_len = 2;
	public void AutoClose(Xop_ctx ctx, byte[] src, int srcLen, int bgnPos, int curPos, Xop_tkn_itm tkn) {
	}
	public void RegTkn(Xop_apos_tkn tkn, int curPos) { // REF.MW: Parser|doQuotes
		stack.Add(tkn);			
		switch (tkn.Apos_tid()) {
			case Xop_apos_tkn_.CmdLen_ital: italCount++; break; 
			case Xop_apos_tkn_.CmdLen_bold: boldCount++; break;
			case Xop_apos_tkn_.CmdLen_dual: //boldCount++; italCount++;	// NOTE: removed b/c of '''''a''b'' was trying to convert ''''' to bold
				dualTkn = tkn;
				break; 
		}
		if (dat.DualCmd() != 0) {	// earlier dual tkn assumed to be <i><b>; </i> encountered so change dual to <b><i>
			if (dualTkn == null) throw Err_.new_("dual tkn is null");	// should never happen
			dualTkn.Apos_cmd_(dat.DualCmd());
			dualTkn = null;
		}
	}
	public void EndFrame(Xop_ctx ctx, byte[] src, int curPos, boolean from_nl_lxr) {
		int state = dat.State();
		if (state == 0) {Reset(); return;}
		if (boldCount % 2 == 1 && italCount % 2 == 1) ConvertBoldToItal(ctx, src);

		state = dat.State();
		int closeCmd = 0, closeTyp = 0;
		if (state == 0) {Reset(); return;}	// all closed: return
		byte cur_tkn_tid = ctx.Cur_tkn_tid();
		Xop_apos_tkn prv = Previous_bgn(closeTyp);
		if (from_nl_lxr) {											// NOTE: if \n
			if (	cur_tkn_tid			== Xop_tkn_itm_.Tid_lnki	// and cur scope is lnki
				&&	prv.Ctx_tkn_tid()	!= Xop_tkn_itm_.Tid_lnki	// but apos_bgn is not lnki
				) {
//					prv.Apos_cmd_(Xop_apos_tkn_.Cmd_nil);				// render invalid; EX: ''[[\n]]''DATE:2013-10-31
//					Reset();
				return;
			}
		}
		switch (state) {
			case Xop_apos_tkn_.State_i:		closeTyp = Xop_apos_tkn_.Typ_ital; closeCmd = Xop_apos_tkn_.Cmd_i_end; break;
			case Xop_apos_tkn_.State_b:		closeTyp = Xop_apos_tkn_.Typ_bold; closeCmd = Xop_apos_tkn_.Cmd_b_end; break;
			case Xop_apos_tkn_.State_dual:
			case Xop_apos_tkn_.State_ib:	closeTyp = Xop_apos_tkn_.Typ_dual; closeCmd = Xop_apos_tkn_.Cmd_bi_end; break;
			case Xop_apos_tkn_.State_bi:	closeTyp = Xop_apos_tkn_.Typ_dual; closeCmd = Xop_apos_tkn_.Cmd_ib_end; break;
		}
		ctx.Msg_log().Add_itm_none(Xop_apos_log.Dangling_apos, src, prv.Src_bgn(), curPos);
		ctx.Subs_add(ctx.Tkn_mkr().Apos(curPos, curPos, 0, closeTyp, closeCmd, 0, cur_tkn_tid));
		Reset();
	}
	private void ConvertBoldToItal(Xop_ctx ctx, byte[] src) {
		Xop_apos_tkn idxNeg1 = null, idxNeg2 = null, idxNone = null; // look at previous tkn for spaces; EX: "a '''" -> idxNeg1; " a'''" -> idxNeg2; "ab'''" -> idxNone
	    int tknsLen = stack.Count(); 
		for (int i = 0; i < tknsLen; i++) {
			Xop_apos_tkn apos = (Xop_apos_tkn)stack.FetchAt(i);
			if (apos.Apos_tid() != Xop_apos_tkn_.Typ_bold) continue;	// only look for bold
			int tknBgn = apos.Src_bgn();
			boolean idxNeg1Space = tknBgn > 0 && src[tknBgn - 1] == Byte_ascii.Space;
			boolean idxNeg2Space = tknBgn > 1 && src[tknBgn - 2] == Byte_ascii.Space;
			if		(idxNeg1 == null && idxNeg1Space)					{idxNeg1 = apos;}
			else if (idxNeg2 == null && idxNeg2Space)					{idxNeg2 = apos;}
			else if (idxNone == null && !idxNeg1Space && !idxNeg2Space)	{idxNone = apos;}
		}
		if		(idxNeg2 != null) ConvertBoldToItal(ctx, idxNeg2); // 1st single letter word
		else if (idxNone != null) ConvertBoldToItal(ctx, idxNone); // 1st multi letter word
		else if	(idxNeg1 != null) ConvertBoldToItal(ctx, idxNeg1); // everything else

		// now recalc all cmds for stack
		dat.State_clear();
		for (int i = 0; i < tknsLen; i++) {
			Xop_apos_tkn apos = (Xop_apos_tkn)stack.FetchAt(i);
			dat.Ident(ctx, apos.Apos_tid(), apos.Src_end());	// NOTE: apos.Typ() must map to aposLen
			int newCmd = dat.Cmd();
			if (newCmd == apos.Apos_cmd()) continue;
			apos.Apos_cmd_(newCmd);
		}
	}
	private void ConvertBoldToItal(Xop_ctx ctx, Xop_apos_tkn oldTkn) {
		ctx.Msg_log().Add_itm_none(Xop_apos_log.Bold_converted_to_ital, ctx.Src(), oldTkn.Src_bgn(), oldTkn.Src_end());
		oldTkn.Apos_tid_(Xop_apos_tkn_.Typ_ital).Apos_cmd_(Xop_apos_tkn_.Cmd_i_bgn).Apos_lit_(oldTkn.Apos_lit() + 1);// NOTE: Cmd_i_bgn may be overridden later
	}
	private void Reset() {
		boldCount = italCount = 0;
		dualTkn = null;
		stack.Clear();
		dat.State_clear();
	}
	private Xop_apos_tkn Previous_bgn(int typ) {
		for (int i = stack.Count() - 1; i > -1; i--) {
			Xop_apos_tkn apos = (Xop_apos_tkn)stack.FetchAt(i);
			int cmd = apos.Apos_cmd();
			switch (typ) {
				case Xop_apos_tkn_.Typ_ital:
					switch (cmd) {
						case Xop_apos_tkn_.Cmd_i_bgn:
						case Xop_apos_tkn_.Cmd_ib_bgn:
						case Xop_apos_tkn_.Cmd_bi_bgn:
						case Xop_apos_tkn_.Cmd_ib_end__i_bgn:
						case Xop_apos_tkn_.Cmd_b_end__i_bgn:
							return apos;
					}
					break;
				case Xop_apos_tkn_.Typ_bold:
					switch (cmd) {
						case Xop_apos_tkn_.Cmd_b_bgn:
						case Xop_apos_tkn_.Cmd_ib_bgn:
						case Xop_apos_tkn_.Cmd_bi_bgn:
						case Xop_apos_tkn_.Cmd_bi_end__b_bgn:
						case Xop_apos_tkn_.Cmd_i_end__b_bgn:
							return apos;
					}
					break;
				default:	// NOTE: this is approximate; will not be exact in most dual situations; EX: <b>a<i>b will return <i>; should return <b> and <i>
					switch (cmd) {
						case Xop_apos_tkn_.Cmd_b_bgn:
						case Xop_apos_tkn_.Cmd_i_bgn:
						case Xop_apos_tkn_.Cmd_ib_bgn:
						case Xop_apos_tkn_.Cmd_bi_bgn:
						case Xop_apos_tkn_.Cmd_bi_end__b_bgn:
						case Xop_apos_tkn_.Cmd_i_end__b_bgn:
						case Xop_apos_tkn_.Cmd_ib_end__i_bgn:
						case Xop_apos_tkn_.Cmd_b_end__i_bgn:
							return apos;
					}
					break;
			}
		}
		return null;
	}
}

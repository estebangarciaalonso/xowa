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
class Xop_tblw_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_tblw;}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		if (wlxr_type == Xop_tblw_wkr.Tblw_type_td2) {	// "||" found; check if in lnki and validate ttl; DATE:2014-03-29
			Xop_tkn_itm last_tkn = ctx.Stack_get_last();	// BLOCK:invalid_ttl_check
			if (	last_tkn != null
				&&	last_tkn.Tkn_tid() == Xop_tkn_itm_.Tid_lnki) {
				Xop_lnki_tkn lnki = (Xop_lnki_tkn)last_tkn;
				if (	lnki.Pipe_count_is_zero()
					&&	!Xop_lnki_wkr_.Parse_ttl(ctx, src, lnki, bgn_pos)) {
					ctx.Stack_pop_last();
					return Xop_lnki_wkr_.Invalidate_lnki(ctx, src, root, lnki, bgn_pos);
				}
			}
		}
		// NOTE: repeated in tblw_lxr_ws
		// CASE_1: standalone "!" should be ignored if no tblw present; EX: "a b! c" should not trigger ! for header
		switch (wlxr_type) {
			case Xop_tblw_wkr.Tblw_type_th:		// !
			case Xop_tblw_wkr.Tblw_type_th2:	// !!	
			case Xop_tblw_wkr.Tblw_type_td:		// |
				Xop_tkn_itm owner_tblw_tb = ctx.Stack_get_typ(Xop_tkn_itm_.Tid_tblw_tb);	// check entire stack for tblw; DATE:2014-03-11
				if (owner_tblw_tb == null) {	// no tblw in stack; highly probably that current sequence is not tblw tkn
					int lnki_pos = ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_lnki);
					if (lnki_pos != Xop_ctx.Stack_not_found && wlxr_type == Xop_tblw_wkr.Tblw_type_td) {// lnki present;// NOTE: added Xop_tblw_wkr.Tblw_type_td b/c th should not apply when tkn_mkr.Pipe() is called below; DATE:2013-04-24
						Xop_tkn_itm lnki_tkn = ctx.Stack_pop_til(root, src, lnki_pos, false, bgn_pos, cur_pos);	// pop any intervening nodes until lnki
						ctx.Stack_add(lnki_tkn);												// push lnki back onto stack; TODO: combine these 2 lines into 1
						// NOTE: this is a "\n|" inside a [[ ]]; must create two tokens for lnki to build correctly;
						ctx.Subs_add(root, tkn_mkr.NewLine(bgn_pos, bgn_pos + 1, Xop_nl_tkn.Tid_char, 1));
						ctx.Subs_add(root, tkn_mkr.Pipe(bgn_pos + 1 , bgn_pos + 2));
						return cur_pos;
					}
					else {	// \n| or \n! but no tbl
						if (	bgn_pos != Xop_parser_.Doc_bgn_bos		// avoid ! at BOS
							&&	src[bgn_pos] == Byte_ascii.NewLine)		// handle "!" etc.
							return Xop_tblw_wkr.Handle_false_tblw_match(ctx, root, src, bgn_pos, cur_pos, tkn_mkr.Txt(bgn_pos + 1, cur_pos));	// +1 to ignore \n of "\n!", "\n!!", "\n|"; DATE:2014-02-19
						else											// handle "!!" only
							return ctx.LxrMake_txt_(cur_pos);
					}
				}
				break;
		}

		// CASE_2: Cur_tkn_tid() is lnki; always interpret "|" as separator for lnki and return; EX: UTF-8 [[Plus and minus signs|+]]; |+ is not a tblw symbol
		if (ctx.Cur_tkn_tid() == Xop_tkn_itm_.Tid_lnki) {
			switch (wlxr_type) {
				case Xop_tblw_wkr.Tblw_type_tc:		// |+
				case Xop_tblw_wkr.Tblw_type_tr:		// |-
				case Xop_tblw_wkr.Tblw_type_td2:	// ||
				//case Xop_tblw_wkr.Tblw_type_te:	// |} // NOTE: ignore "|}"; needed for incomplete lnkis; EX: |[[a\n|}; EX:w:Wikipedia:Changing_attribution_for_an_edit; DATE:2014-03-16
				case Xop_tblw_wkr.Tblw_type_td:		// |
					ctx.Subs_add(root, tkn_mkr.NewLine(bgn_pos, bgn_pos + 1, Xop_nl_tkn.Tid_char, 1));
					ctx.Subs_add(root, tkn_mkr.Pipe(bgn_pos + 1, bgn_pos + 2));
					return bgn_pos + 2;				// +2 to skip "\n|", but still look at 3rd char; ("+", "-", or "}")
				case Xop_tblw_wkr.Tblw_type_th2:	// !!
				case Xop_tblw_wkr.Tblw_type_th:		// !
					ctx.Subs_add(root, tkn_mkr.Txt(bgn_pos, cur_pos));	// NOTE: cur_pos should handle ! and !!
					return cur_pos;
			}
		}
		return ctx.Tblw().Make_tkn_bgn(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, false, wlxr_type, false, -1, -1);
	}
	public Xop_tblw_lxr(byte wlxr_type) {this.wlxr_type = wlxr_type;} private byte wlxr_type;
	public static final Xop_tblw_lxr _ = new Xop_tblw_lxr(); Xop_tblw_lxr() {}
	public void Init_by_wiki(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {
		core_trie.Add(Hook_tb,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_tb));
		core_trie.Add(Hook_te,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_te));
		core_trie.Add(Hook_tr,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_tr));
		core_trie.Add(Hook_td,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_td));
		core_trie.Add(Hook_th,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_th));
		core_trie.Add(Hook_tc,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_tc));
		core_trie.Add(Hook_td2,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_td2));
		core_trie.Add(Hook_th2,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_th2));
	}
	public void Init_by_lang(Xol_lang lang, ByteTrieMgr_fast core_trie) {}
	public static final byte[] Hook_tb = ByteAry_.new_ascii_("\n{|"), Hook_te = ByteAry_.new_ascii_("\n|}"), Hook_tr = ByteAry_.new_ascii_("\n|-")
		, Hook_td = ByteAry_.new_ascii_("\n|"), Hook_th = ByteAry_.new_ascii_("\n!"), Hook_tc = ByteAry_.new_ascii_("\n|+")
		, Hook_td2 = ByteAry_.new_ascii_("||"), Hook_th2 = ByteAry_.new_ascii_("!!");
}

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
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		// NOTE: repeated in tblw_lxr_ws
		// CASE_1: standalone "!" should be ignored if no tblw present; EX: "a b! c" should not trigger ! for header
		switch (wlxr_type) {
			case Xop_tblw_wkr.Tblw_type_th:		// !
			case Xop_tblw_wkr.Tblw_type_th2:	// !!	
			case Xop_tblw_wkr.Tblw_type_td:		// |
				Xop_tblw_tkn owner_tblw = ctx.Stack_get_tblw();
				if (owner_tblw == null) {		// no tblw in stack; highly probably that current sequence is not tblw tkn
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
						if (ctx.Para().Pre_at_line_bgn())	// HACK:pre_section_begun_and_failed_tblw
							return ctx.Para().Hack_pre_and_false_tblw(ctx, root, src, bgn_pos);
						else								// interpret as text
							return ctx.LxrMake_txt_(cur_pos);
					}
				}
				else {
					if (wlxr_type == Xop_tblw_wkr.Tblw_type_th2) {
						int nl_pos = ByteAry_.FindBwd(src, Byte_ascii.NewLine, cur_pos, 0);	// search for preceding nl
						if (nl_pos != ByteAry_.NotFound) {	
							if (src[nl_pos + 1] != Byte_ascii.Bang) {
								return ctx.LxrMake_txt_(cur_pos);
							}
						}
					}
				}
				break;
		}

		// CASE_2: Cur_tkn_tid() is lnki; interpret "|" as separator for lnki and return; EX: UTF-8 [[Plus and minus signs|+]]; |+ is not a tblw sym
		if (ctx.Cur_tkn_tid() == Xop_tkn_itm_.Tid_lnki) {
			switch (wlxr_type) {
				case Xop_tblw_wkr.Tblw_type_tc:		// |+
				case Xop_tblw_wkr.Tblw_type_tr:		// |-
				case Xop_tblw_wkr.Tblw_type_td2:	// ||
				case Xop_tblw_wkr.Tblw_type_te:		// |}
					ctx.Subs_add(root, tkn_mkr.Pipe(bgn_pos, bgn_pos + 1));	// 1=pipe.length
					return cur_pos - 1;								// -1 to ignore 2nd char of "+", "-", or "}"
				case Xop_tblw_wkr.Tblw_type_th2:// !!
				case Xop_tblw_wkr.Tblw_type_th: // !
					ctx.Subs_add(root, tkn_mkr.Txt(bgn_pos, cur_pos));	// NOTE: cur_pos should handle ! and !!
					return cur_pos;
//					case Xop_tblw_wkr.Tblw_type_tb:	// {| tblw not allowed, even in caption	// REMOVED: 2012-05-12: MW allows entire table to be put inside anchor; see lnki test; EX.WP:William Penn (Royal Navy officer)
//						return ctx.LxrMake_txt_(cur_pos);
			}
		}
		return ctx.Tblw().MakeTkn_bgn(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, wlxr_type, false, false, -1, -1);
	}
	public Xop_tblw_lxr(byte wlxr_type) {this.wlxr_type = wlxr_type;} private byte wlxr_type;
	public static final Xop_tblw_lxr Bldr = new Xop_tblw_lxr(); Xop_tblw_lxr() {}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {
		core_trie.Add(Hook_tb,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_tb));
		core_trie.Add(Hook_te,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_te));
		core_trie.Add(Hook_tr,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_tr));
		core_trie.Add(Hook_td,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_td));
		core_trie.Add(Hook_th,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_th));
		core_trie.Add(Hook_tc,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_tc));
		core_trie.Add(Hook_td2,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_td2));
		core_trie.Add(Hook_th2,	new Xop_tblw_lxr(Xop_tblw_wkr.Tblw_type_th2));
	}
	public static final byte[] Hook_tb = ByteAry_.new_ascii_("\n{|"), Hook_te = ByteAry_.new_ascii_("\n|}"), Hook_tr = ByteAry_.new_ascii_("\n|-")
		, Hook_td = ByteAry_.new_ascii_("\n|"), Hook_th = ByteAry_.new_ascii_("\n!"), Hook_tc = ByteAry_.new_ascii_("\n|+")
		, Hook_td2 = ByteAry_.new_ascii_("||"), Hook_th2 = ByteAry_.new_ascii_("!!");
}

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
class Xop_pre_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_pre;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Hook_ary, this);} static final byte[] Hook_ary = new byte[] {Byte_ascii.NewLine, Byte_ascii.Space};
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		if (!ctx.Para().Enabled()) {		// NOTE: para disabled in <gallery>
			if (bgn_pos != Xop_parser_.Doc_bgn_bos)	// NOTE: guard against <BOS>" a" where bgn_pos would be -1 and no nl is available
				ctx.Subs_add(tkn_mkr.NewLine(bgn_pos, bgn_pos + 1, Xop_nl_tkn.Tid_char, 1));
			ctx.Subs_add(tkn_mkr.Space(ctx.Root(), cur_pos - 1, cur_pos));
			return cur_pos;
		}
		// trim is needed for tblw_ws; EX: "  {|"
		int txt_pos = cur_pos;
		boolean loop = true;
		while (loop) {
			if (txt_pos == src_len) break;
			byte b = src[txt_pos];
			switch (b) {
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
					++txt_pos;
					break;
				default:
					loop = false;
					break;
			}
		}
		if (txt_pos == src_len) return cur_pos;	// NOTE: "\n\s" at eos; treat as \n only; EX: "a\n ";
		if (	bgn_pos == Xop_parser_.Doc_bgn_bos		// bos
			&& 	txt_pos < src_len						// bounds check
			&&	src[txt_pos] == Byte_ascii.NewLine) {	// next char is nl
			cur_pos = txt_pos;							// position at nl; NOTE: do not position after nl, else may break wikitext; EX: "\s\n{|" needs to preserve "\n" for tblw
			ctx.Subs_add(tkn_mkr.Ignore(bgn_pos, cur_pos, Xop_ignore_tkn.Ignore_tid_pre_at_bos));
			return cur_pos;	// ignore pre if blank line at bos; EX: "\s\n"
		}
		switch (ctx.Cur_tkn_tid()) {
			case Xop_tkn_itm_.Tid_tblw_tb: case Xop_tkn_itm_.Tid_tblw_tr: case Xop_tkn_itm_.Tid_tblw_th:
				Xop_tblw_wkr.Atrs_close(ctx, src, root);
				break;
		}

		// NOTE: \n ! can be table;
		switch (src[txt_pos]) {
			case Byte_ascii.Bang:
				switch (ctx.Cur_tkn_tid()) {
					case Xop_tkn_itm_.Tid_tblw_tb:
					case Xop_tkn_itm_.Tid_tblw_tc:
					case Xop_tkn_itm_.Tid_tblw_tr:
					case Xop_tkn_itm_.Tid_tblw_th:
					case Xop_tkn_itm_.Tid_tblw_td:
					case Xop_tkn_itm_.Tid_tblw_te:
						Xop_tblw_lxr_ws.Make(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, Xop_tblw_wkr.Tblw_type_th);
						return txt_pos + 1;	// +1 to skip Byte_ascii.Bang
				}
				break;
		}
		return ctx.Para().Process_pre(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, txt_pos);
	}
	public static final Xop_pre_lxr Bldr = new Xop_pre_lxr(); Xop_pre_lxr() {}
}

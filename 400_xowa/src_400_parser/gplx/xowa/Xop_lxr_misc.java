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
class Xop_colon_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_colon;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {core_trie.Add(Byte_ascii.Colon, this);}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		Xop_list_wkr listCtx = ctx.List();
		if (listCtx.Dd_chk()) {	// handle ";a:b" construct; REF.MW: Parser.php|doBlockLevels|; title : definition text
			if (ctx.Cur_tkn_tid() != Xop_tkn_itm_.Tid_lnki && cur_pos < src_len && src[cur_pos] != Byte_ascii.NewLine) {	// FUTURE: emulate Parser.php|findColonNoLinks which does much more logic to see if ";a:b" construct should apply
				listCtx.Dd_chk_(false);
				return listCtx.MakeTkn_bgn(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos);
			}
		}
		ctx.Subs_add(root, tkn_mkr.Colon(bgn_pos, cur_pos));
		return cur_pos;
	}
	public static final Xop_colon_lxr _ = new Xop_colon_lxr();
}

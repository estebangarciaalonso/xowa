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
package gplx.xowa.xtns.translates; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xop_tvar_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_tvar;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {
		core_trie.Add(Hook_bgn, this);
	}
	private static final byte[] Hook_bgn = ByteAry_.new_ascii_("<tvar|")
		,	Close_nde = ByteAry_.new_ascii_("</>")
		;
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		int rhs_end = ByteAry_.FindFwd(src, Byte_ascii.Gt, cur_pos); if (rhs_end == ByteAry_.NotFound) return ctx.LxrMake_txt_(cur_pos);
		int lhs_bgn = ByteAry_.FindFwd(src, Close_nde    , rhs_end); if (lhs_bgn == ByteAry_.NotFound) return ctx.LxrMake_txt_(cur_pos);
//		byte[] key = ByteAry_.Mid(src, cur_pos, rhs_end);
//		byte[] body = ByteAry_.Mid(src, rhs_end + Int_.Const_position_after_char, lhs_bgn);
		int end_pos = lhs_bgn + Close_nde.length;
		ctx.Subs_add(tkn_mkr.Tvar(bgn_pos, end_pos, cur_pos, rhs_end, rhs_end + Int_.Const_position_after_char, lhs_bgn));
		return end_pos;
	}
	public static final Xop_tvar_lxr _ = new Xop_tvar_lxr(); Xop_tvar_lxr() {}
}

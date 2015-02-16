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
import gplx.core.btries.*;
public class Xop_space_tkn extends Xop_tkn_itm_base {
	public Xop_space_tkn(boolean immutable, int bgn, int end) {this.Tkn_ini_pos(immutable, bgn, end);}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_space;}
	@Override public Xop_tkn_itm Tkn_clone(Xop_ctx ctx, int bgn, int end) {
		return ctx.Tkn_mkr().Space_mutable(bgn, end);
	}
	@Override public boolean Tmpl_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Bry_bfr bfr) {
		if (this.Tkn_immutable()) {
			bfr.Add_byte(Byte_ascii.Space);
			return true;
		}
		else
			return super.Tmpl_evaluate(ctx, src, caller, bfr);
	}
	Xop_space_tkn() {}
}
class Xop_space_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_space;}
	public void Init_by_wiki(Xow_wiki wiki, Btrie_fast_mgr core_trie) {core_trie.Add(Byte_ascii.Space, this);}
	public void Init_by_lang(Xol_lang lang, Btrie_fast_mgr core_trie) {}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		cur_pos = Bry_finder.Find_fwd_while(src, cur_pos, src_len, Byte_ascii.Space);
		ctx.Subs_add(root, tkn_mkr.Space(root, bgn_pos, cur_pos));
		return cur_pos;
	}
	public static final Xop_space_lxr _ = new Xop_space_lxr();
}

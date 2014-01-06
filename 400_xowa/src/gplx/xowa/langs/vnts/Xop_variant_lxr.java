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
package gplx.xowa.langs.vnts; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
public class Xop_variant_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_variant;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {
	}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		int end_pos = ByteAry_.FindFwd(src, Hook_end, cur_pos, src_len); if (end_pos == ByteAry_.NotFound) return cur_pos;
		return end_pos + Hook_end.length;	// FUTURE: for now, just ignore the whole String
	}
	public static final byte[] Hook_bgn = new byte[] {Byte_ascii.Dash, Byte_ascii.Curly_bgn}, Hook_end = new byte[] {Byte_ascii.Curly_end, Byte_ascii.Dash};
	public static void set_(Xow_wiki wiki, byte[] grp) {
		ByteTrieMgr_fast tmpl_trie = wiki.Parser().Tmpl_trie();
		if (grp == null) {	// del
			tmpl_trie.Del(Hook_bgn);
			tmpl_trie.Del(Hook_end);
		}
		else {				// add
			Object exists = tmpl_trie.MatchAtCur(Hook_bgn, 0, Hook_bgn.length);
			if (exists == null) {
				tmpl_trie.Add(Hook_bgn, _);
				tmpl_trie.Add(Hook_end, _);
			}
		}
	}
        private static final Xop_variant_lxr _ = new Xop_variant_lxr(); Xop_variant_lxr() {}
}

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
class Xop_variant_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_variant;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {
		if (wiki.Lang().Variants_enabled()) {
			coreTrie.Add(Hook_bgn, this);
			coreTrie.Add(Hook_end, this);
		}
	}	static final byte[] Hook_bgn = new byte[] {Byte_ascii.Dash, Byte_ascii.Curly_bgn}, Hook_end = new byte[] {Byte_ascii.Curly_end, Byte_ascii.Dash};
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {
		int end_pos = ByteAry_.FindFwd(src, Hook_end, curPos, srcLen); if (end_pos == ByteAry_.NotFound) return curPos;
		return end_pos + Hook_end.length;	// FUTURE: for now, just ignore the whole String
	}
	public static final Xop_variant_lxr Bldr = new Xop_variant_lxr();
}

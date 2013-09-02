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
class Xop_apos_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_apos;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Apos_ary, this);} static final byte[] Apos_ary = new byte[] {Byte_ascii.Apos, Byte_ascii.Apos};
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {return ctx.Apos().MakeTkn(ctx, tkn_mkr, root, src, srcLen, bgnPos, curPos);}
	public static final Xop_apos_lxr _ = new Xop_apos_lxr(); Xop_apos_lxr() {}
}

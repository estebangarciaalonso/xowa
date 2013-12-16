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
public class Xop_space_tkn extends Xop_tkn_itm_base {
	public Xop_space_tkn(boolean immutable, int bgn, int end) {this.Tkn_ini_pos(immutable, bgn, end);}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_space;}
	@Override public Xop_tkn_itm Tkn_clone(Xop_ctx ctx, int bgn, int end) {
		return ctx.Tkn_mkr().Space_mutable(bgn, end);
	}
	@Override public boolean Tmpl_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, ByteAryBfr bfr) {
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
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Byte_ascii.Space, this);}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {
		curPos = Xop_lxr_.Find_fwd_while(src, srcLen, curPos, Byte_ascii.Space);
		ctx.Subs_add(root, tkn_mkr.Space(root, bgnPos, curPos));
		return curPos;
	}
	public static final Xop_space_lxr _ = new Xop_space_lxr();
}
class Xop_nbsp_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_nbsp;}
	private static final byte[] Nbsp_0 = new byte[] {(byte)194, (byte)160};
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Nbsp_0, this);}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {
//			curPos = Xop_lxr_.Find_fwd_while(src, srcLen, curPos, (byte)160);
		ctx.Subs_add(root, tkn_mkr.Space(root, bgnPos, curPos));
		return curPos;
	}
	public static final Xop_nbsp_lxr _ = new Xop_nbsp_lxr(); Xop_nbsp_lxr() {}
}

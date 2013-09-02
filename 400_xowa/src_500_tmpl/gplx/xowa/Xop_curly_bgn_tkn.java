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
public class Xop_curly_bgn_tkn extends Xop_tkn_itm_base {
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_tmpl_curly_bgn;}
	public Xop_curly_bgn_tkn(int bgn, int end) {this.Tkn_ini_pos(false, bgn, end);}
}
class Xop_curly_bgn_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_curly_bgn;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Hook, this);} public static final byte[] Hook = new byte[] {Byte_ascii.Curly_bgn, Byte_ascii.Curly_bgn};
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {return ctx.Curly().MakeTkn_bgn(ctx, tkn_mkr, root, src, srcLen, bgnPos, curPos);}
	public static final Xop_curly_bgn_lxr _ = new Xop_curly_bgn_lxr(); Xop_curly_bgn_lxr() {}
	public static ByteTrieMgr_fast tmpl_bgn_trie_() {
		ByteTrieMgr_fast rv = ByteTrieMgr_fast.cs_();
		rv.Add(Xop_tblw_lxr_ws.Hook_tb, ByteAry_.Empty);//{| : ; # *
		rv.Add(ByteAry_.new_ascii_("|-"), ByteAry_.Empty);
		rv.Add(Byte_ascii.Colon, ByteAry_.Empty);
		rv.Add(Byte_ascii.Semic, ByteAry_.Empty);
		rv.Add(Byte_ascii.Hash, ByteAry_.Empty);
		rv.Add(Byte_ascii.Asterisk, ByteAry_.Empty);
		return rv;
	}

}
class Xop_curly_end_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_curly_end;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Hook, this);} public static final byte[] Hook = new byte[] {Byte_ascii.Curly_end, Byte_ascii.Curly_end};
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {return ctx.Curly().MakeTkn_end(ctx, tkn_mkr, root, src, srcLen, bgnPos, curPos);}
	public static final Xop_curly_end_lxr _ = new Xop_curly_end_lxr(); Xop_curly_end_lxr() {}
}
class Xop_brack_bgn_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_brack_bgn;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Xop_tkn_.Lnki_bgn, this);}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {
		Xop_tkn_itm tkn = tkn_mkr.Brack_bgn(bgnPos, curPos);
		ctx.Subs_add_and_stack(tkn);			
		return curPos;
	}
	public static final Xop_brack_bgn_lxr _ = new Xop_brack_bgn_lxr(); Xop_brack_bgn_lxr() {}
}
class Xop_brack_end_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_brack_end;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Xop_tkn_.Lnki_end, this);}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {
		int acs_pos = ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_brack_bgn);
		if (acs_pos != -1 && ctx.Cur_tkn_tid() != Xop_tkn_itm_.Tid_tmpl_curly_bgn)	// NOTE: do not pop tkn if inside tmpl; EX: [[a|{{#switch:{{{1}}}|b=c]]|d=e]]|f]]}}
			ctx.Stack_pop_til(acs_pos, true, bgnPos, curPos);
		Xop_tkn_itm tkn = tkn_mkr.Brack_end(bgnPos, curPos);
		ctx.Subs_add(tkn);
		return curPos;
	}
	public static final Xop_brack_end_lxr _ = new Xop_brack_end_lxr(); Xop_brack_end_lxr() {}
}

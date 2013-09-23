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
class Xop_list_lxr implements Xop_lxr {//20111222
	public byte Lxr_tid() {return Xop_lxr_.Tid_list;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {Add_ary(coreTrie, this, Xop_list_tkn_.Hook_ul, Xop_list_tkn_.Hook_ol, Xop_list_tkn_.Hook_dt, Xop_list_tkn_.Hook_dd);}
	private void Add_ary(ByteTrieMgr_fast coreTrie, Object val, byte[]... ary) {for (byte[] itm : ary) coreTrie.Add(itm, val);}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {return ctx.List().MakeTkn_bgn(ctx, tkn_mkr, root, src, srcLen, bgnPos, curPos);}
	public static final Xop_list_lxr _ = new Xop_list_lxr(); Xop_list_lxr() {}
}

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
package gplx.xowa.xtns.lst; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xtn_lstx extends Pf_func_base {
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {
		ctx.Msg_log().Add_itm_none(Pf_xtn_lst_log.Lstx_found, src, self.Src_bgn(), self.Src_end());
		bfr.Add_mid(src, self.Src_bgn(), self.Src_end());
	}
	@Override public int Id() {return Xol_kwd_grp_.Id_lstx;}
	@Override public Pf_func New(int id, byte[] name) {return new Xtn_lstx().Name_(name);}
	public static final Xtn_lstx _ = new Xtn_lstx(); Xtn_lstx() {}
}
class Pf_xtn_lst_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "lst");
	public static final Gfo_msg_itm
		  Lst_found						= Gfo_msg_itm_.new_note_(owner, "Lst_found")
		, Lstx_found					= Gfo_msg_itm_.new_note_(owner, "Lstx_found")
		, Lst_err						= Gfo_msg_itm_.new_note_(owner, "Lst_err")
		;
//		public final RscStrItm_arg
//			  Dangling_apos_typ					= new RscStrItm_arg(_mgr, "closing_typ")
//			;
}

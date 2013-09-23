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
class Pf_xtn_property extends Pf_func_base {
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {}	// NOOP: MW uses to save coordinates for API retrieval
	@Override public int Id() {return Xol_kwd_grp_.Id_xtn_geodata_coordinates;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_xtn_property().Name_(name);}
	public static final Pf_xtn_property _ = new Pf_xtn_property(); Pf_xtn_property() {}
}

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
package gplx.xowa.html.modules; import gplx.*; import gplx.xowa.*; import gplx.xowa.html.*;
import gplx.html.*;
public class Xoh_module_list implements ByteAryFmtrArg {
	private OrderedHash itms;
	public void Add(Xoh_module_itm itm) {
		if (itms == null) itms = OrderedHash_.new_();
		if (itms.Has(itm.Key())) return;
		itms.Add(itm.Key(), itm);
	}
	public void Clear() {
		if (itms == null) return;
		itms.Clear();
		itms = null;
	}
	public void XferAry(ByteAryBfr bfr, int idx) {
		if (itms == null) return;
		int len = itms.Count();			
		bfr.Add_byte_nl().Add(Html_consts.Script_bgn_bry);
		for (int i = 0; i < len; i++) {
			Xoh_module_itm itm = (Xoh_module_itm)itms.FetchAt(i);
			itm.Bld_js(bfr);
		}
		bfr.Add_byte_nl().Add(Html_consts.Script_end_bry);
	}
}

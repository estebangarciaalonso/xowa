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
package gplx.xowa.files.qrys; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.xowa.files.fsdb.*;
public class Xof_qry_mgr {
	private Xof_qry_wkr[] wkrs = null; private int wkrs_len;
	private Int_2_ref actl_size = new Int_2_ref();
	public void Wkrs_(Xof_qry_wkr[] wkrs) {this.wkrs = wkrs; wkrs_len = wkrs.length;}
	public boolean Calc(Xof_fsdb_itm itm) {
		boolean rv = false;
		for (int i = 0; i < wkrs_len; i++) {
			Xof_qry_wkr wkr = wkrs[i];
			if (wkr.Qry_file(itm)) {
				rv = true;
				break;
			}
		}
		if (!rv) return false;	// not found in any wkr; exit;
		int lnki_w = itm.Lnki_w(), lnki_h = itm.Lnki_h();
		boolean lnki_thumb = Xof_xfer_itm.Lnki_thumbable_calc(itm.Lnki_type(), lnki_w, lnki_h);
		Xof_xfer_itm_.Calc_xfer_size(actl_size, Xof_img_mgr.Thumb_w_img_const, itm.Orig_w(), itm.Orig_h(), lnki_w, lnki_h, lnki_thumb, itm.Lnki_upright());
		itm.Actl_img_w_(actl_size.Val_0()).Actl_img_h_(actl_size.Val_1());
		return true;
	}
}

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
package gplx.xowa.files.bins; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.xowa.files.fsdb.*;
public class Xof_bin_mgr {
	private Xof_bin_wkr[] wkrs = null; private int wkrs_len;
	private StringRef resize_warning = StringRef.null_();
	private Xof_url_bldr url_bldr = new Xof_url_bldr();
	public void Resizer_(Xof_img_wkr_resize_img v) {resizer = v;} Xof_img_wkr_resize_img resizer;
	public void Wkrs_(Xof_bin_wkr[] wkrs) {this.wkrs = wkrs; wkrs_len = wkrs.length;}
	public boolean Find(Xof_fsdb_itm itm) {
		boolean type_is_thumb = Xop_lnki_type.Id_is_thumb_like(itm.Lnki_type());
		for (int i = 0; i < wkrs_len; i++) {
			Xof_bin_wkr wkr = wkrs[i];
			Xof_repo_itm repo = null;
			Io_url trg = url_bldr.Set_trg_file_(Xof_repo_itm.Mode_orig, repo, itm.Ttl(), itm.Md5(), itm.Ext(), itm.Lnki_w(), itm.Lnki_thumbtime()).Xto_url();
			boolean found = wkr.Get_bin(itm, trg, type_is_thumb);
			if (found) return true;
			if (type_is_thumb) {	// thumb not found; try to get orig and make;
				found = wkr.Get_bin(itm, trg, Bool_.N);
				if (!found) return false;
				Io_url src = null; // make src in temp dir
				resizer.Exec(src, trg, itm.Actl_img_w(), itm.Actl_img_h(), itm.Ext().Id(), resize_warning);
			}
		}
		return false;
	}
}

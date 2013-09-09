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
public class Xof_qry_wkr_wmf_api {
	private Xow_wiki wiki;
	private Xof_img_wkr_api_size_base api_wkr;
	private Xof_img_wkr_api_size_base_rslts api_rv = new Xof_img_wkr_api_size_base_rslts();
	private Xof_qry_wkr_wmf_api(Xow_wiki wiki, Xof_img_wkr_api_size_base api_wkr) {this.wiki = wiki; this.api_wkr = api_wkr;}
	public boolean Qry_file(Xof_fsdb_itm itm) {
		byte[] itm_ttl = itm.Lnki_ttl();
		boolean found = api_wkr.Api_query_size(api_rv, wiki, itm_ttl, itm.Lnki_w(), itm.Lnki_h());
		if (!found) return false;	// ttl not found by api; return
		itm.Orig_wiki_(api_rv.Reg_wiki());
		if (!ByteAry_.Eq(itm_ttl, api_rv.Reg_page()))	// ttl is different; must be redirect
			itm.Orig_redirect_(itm_ttl, api_rv.Reg_page());
		itm.Orig_size_(api_rv.Orig_w(), api_rv.Orig_h());
		return true;
	}
}

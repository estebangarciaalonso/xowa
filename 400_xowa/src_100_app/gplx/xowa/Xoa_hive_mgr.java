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
import gplx.core.primitives.*;
public class Xoa_hive_mgr {
	public Xoa_hive_mgr(Xoa_app app) {this.app = app;} private Xoa_app app;
	public Xob_xdat_itm Itm() {return xdat_itm;}
	public int Find_fil(Io_url hive_root, byte[] ttl) {
		Io_url hive_url = hive_root.GenSubFil(Xow_dir_info_.Name_reg_fil);
		if (!hive_url.Eq(regy_mgr.Fil()))
			regy_mgr.Init(hive_url);
		return regy_mgr.Files_find(ttl);
	}	private Xowd_regy_mgr regy_mgr = new Xowd_regy_mgr(); Int_obj_ref bry_len = Int_obj_ref.zero_(); Xob_xdat_file xdat_rdr = new Xob_xdat_file(); Xob_xdat_itm xdat_itm = new Xob_xdat_itm();
	public Xowd_regy_mgr Regy_mgr() {return regy_mgr;}
	public Xob_xdat_file Get_rdr(Io_url hive_root, byte[] fil_ext_bry, int fil_idx) {
		Bry_bfr tmp_bfr = app.Utl_bry_bfr_mkr().Get_m001();
		byte[] tmp_bry = tmp_bfr.Bfr(); bry_len.Val_zero_();
		Io_url file = Xow_fsys_mgr.Url_fil(hive_root, fil_idx, fil_ext_bry);
		tmp_bry = Io_mgr._.LoadFilBry_reuse(file, tmp_bry, bry_len);
		xdat_rdr.Clear().Parse(tmp_bry, bry_len.Val(), file);
		tmp_bfr.Mkr_rls().Clear();
		return xdat_rdr;
	}
}

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
import gplx.dbs.*; import gplx.cache.*; import gplx.fsdb.*; import gplx.xowa.files.fsdb.*;
class Xof_bin_wkr_fsdb implements Xof_bin_wkr {
	private Db_provider provider;
	public Gfo_cache_mgr Dir_cache() {return dir_cache;} private Gfo_cache_mgr dir_cache = new Gfo_cache_mgr(); StringRef dir_cache_key = StringRef.null_();
	public Gfo_cache_mgr Fil_cache() {return fil_cache;} private Gfo_cache_mgr fil_cache = new Gfo_cache_mgr(); StringRef fil_cache_key = StringRef.null_();
	public Xof_bin_wkr_fsdb(Xof_fsdb_mgr fsdb_mgr) {this.fsdb_mgr = fsdb_mgr;} private Xof_fsdb_mgr fsdb_mgr;
	public byte Tid() {return Xof_bin_wkr_.Tid_fsdb;}
	public boolean Get_bin(Xof_fsdb_itm itm, Io_url trg_url, boolean is_thumb) {
		byte[] ttl_bry = itm.Ttl();
		String wiki = String_.new_utf8_(itm.Actl_wiki());
		Fsdb_vlm_db_data vlm = fsdb_mgr.Vlm_find(ttl_bry);
		provider = vlm.Provider();
		IntVal dir_id = (IntVal)dir_cache.Get_val_or_null(dir_cache_key.Val_(wiki));
		if (dir_id == null) {
			Fsdb_dir_itm dir_itm = Fsdb_dir_tbl.Select_itm(provider, wiki);
			if (dir_itm == Fsdb_dir_itm.Null) return false;
			dir_id = IntVal.new_(dir_itm.Id());
			dir_cache.Add(wiki, dir_id);
		}
		String ttl_str = String_.new_utf8_(ttl_bry);
		IntVal fil_id = (IntVal)fil_cache.Get_val_or_null(fil_cache_key.Val_(ttl_str));
		if (fil_id == null) {
			Fsdb_fil_itm fil_itm = Fsdb_fil_tbl.Select_itm_by_name(provider, dir_id.Val(), ttl_str);
			if (fil_itm == Fsdb_fil_itm.Null) return false;
			fil_id = IntVal.new_(fil_itm.Id());
			fil_cache.Add(ttl_str, fil_id);
		}
		Fsdb_xtn_thm_itm img = Fsdb_xtn_thm_tbl.Select_itm_by_fil_width(provider, fil_id.Val(), itm.Actl_img_w(), itm.Lnki_w());
		if (img == Fsdb_xtn_thm_itm.Null) return false;
		Fsdb_bin_itm bin = Fsdb_bin_tbl.Select_itm(provider, img.Id());
		if (bin == Fsdb_bin_itm.Null) return false;
		itm.Db_data_(bin.Data());
		return true;
	}
}

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
public class Xof_bin_wkr_fsdb_sql implements Xof_bin_wkr {
	private Db_provider provider;
	private byte[] select_bfr;
	private int select_flush = Io_mgr.Len_mb;
	public int Select_bfr_len() {return select_bfr_len;} public Xof_bin_wkr_fsdb_sql Select_bfr_len_(int v) {select_bfr_len = v; return this;} private int select_bfr_len = 4096 * 16;
	public Gfo_cache_mgr Dir_cache() {return dir_cache;} private Gfo_cache_mgr dir_cache = new Gfo_cache_mgr();
	public Gfo_cache_mgr Fil_cache() {return fil_cache;} private Gfo_cache_mgr fil_cache = new Gfo_cache_mgr();
	public Xof_bin_wkr_fsdb_sql(Xof_fsdb_mgr_sql fsdb_mgr) {this.fsdb_mgr = fsdb_mgr;} private Xof_fsdb_mgr_sql fsdb_mgr;
	public byte Bin_wkr_tid() {return Xof_bin_wkr_.Tid_fsdb;}
	public boolean Bin_wkr_get(Xof_fsdb_itm itm, Io_url bin_url, boolean is_thumb, int w) {
		if (select_bfr == null) select_bfr = new byte[select_bfr_len];
		byte[] ttl_bry = itm.Lnki_ttl();
		String wiki = String_.new_utf8_(itm.Orig_wiki());
		Fsdb_vlm_db_data vlm = fsdb_mgr.Vlm_find(ttl_bry);
		provider = vlm.Provider();
		IntVal dir_id = (IntVal)dir_cache.Get_val_or_null(wiki);
		if (dir_id == null) {
			Fsdb_dir_itm dir_itm = Fsdb_dir_tbl.Select_itm(provider, wiki);
			if (dir_itm == Fsdb_dir_itm.Null) return false;
			dir_id = IntVal.new_(dir_itm.Id());
			dir_cache.Add(wiki, dir_id);
		}
		String ttl_str = String_.new_utf8_(ttl_bry);
		IntVal fil_id = (IntVal)fil_cache.Get_val_or_null(ttl_str);
		if (fil_id == null) {
			Fsdb_fil_itm fil_itm = Fsdb_fil_tbl.Select_itm_by_name(provider, dir_id.Val(), ttl_str);
			if (fil_itm == Fsdb_fil_itm.Null) return false;
			fil_id = IntVal.new_(fil_itm.Id());
			fil_cache.Add(ttl_str, fil_id);
		}
		int bin_id = fil_id.Val();
		if (is_thumb || itm.Lnki_ext().Orig_is_not_image()) {
			Fsdb_xtn_thm_itm img = Fsdb_xtn_thm_tbl.Select_itm_by_fil_width(provider, fil_id.Val(), w, itm.Lnki_thumbtime());
			if (img == Fsdb_xtn_thm_itm.Null) return false;
			bin_id = img.Id();
		}
		return Fsdb_bin_tbl.Select_to_fsys(provider, bin_id, bin_url, select_bfr, select_flush);
	}
}
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
package gplx.xowa.files.fsdb.caches; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*; import gplx.xowa.files.fsdb.*;
import gplx.dbs.*;
class Cache_fil_mgr {
	private Cache_mgr cache_mgr;
	private OrderedHash fil_hash = OrderedHash_.new_();
	private Cache_fil_tbl fil_tbl = new Cache_fil_tbl();
	public Cache_fil_mgr(Cache_mgr v) {this.cache_mgr = v;}
	public void Db_init(Db_provider p) {fil_tbl.Db_init(p);}
	public void Db_when_new(Db_provider provider) {fil_tbl.Db_when_new();}
	public void Db_save() {
		int len = fil_hash.Count();
		for (int i = 0; i < len; i++) {
			Cache_fil_itm fil_itm = (Cache_fil_itm)fil_hash.FetchAt(i);
			fil_tbl.Db_save(fil_itm);
		}
	}
	public void Db_term() {
		this.Db_save();
		fil_tbl.Db_term();
	}
	public Cache_fil_itm Get_or_new(int dir_id, String fil_name, boolean fil_is_orig, int fil_w, int fil_h, int fil_thumbtime, int fil_ext, long fil_size) {
		String fil_key = "";
		Cache_fil_itm fil_itm = (Cache_fil_itm)fil_hash.Fetch(fil_key);
		if (fil_itm == null)			{								// not in memory
			fil_itm = fil_tbl.Select(dir_id, fil_name, fil_is_orig, fil_w, fil_h, fil_thumbtime);
			if (fil_itm == Cache_fil_itm.Null) {						// not in db
				int fil_id = cache_mgr.Next_id();
				fil_itm = new Cache_fil_itm().Init_by_make(fil_id, dir_id, fil_name, fil_is_orig, fil_w, fil_h, fil_thumbtime, fil_ext, fil_size);
			}
			fil_hash.Add(fil_key, fil_itm);
		}
		return fil_itm;
	}
}

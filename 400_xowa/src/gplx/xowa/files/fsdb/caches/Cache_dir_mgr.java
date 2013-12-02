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
class Cache_dir_mgr {
	private Cache_mgr cache_mgr;
	private OrderedHash name_hash = OrderedHash_.new_bry_(); private HashAdp id_hash = HashAdp_.new_();
	private Cache_dir_tbl dir_tbl = new Cache_dir_tbl();
	public Cache_dir_mgr(Cache_mgr v) {this.cache_mgr = v;}
	public void Db_init(Db_provider p) {dir_tbl.Db_init(p);}
	public void Db_when_new(Db_provider p) {dir_tbl.Db_when_new(p);}
	public void Db_save() {
		int len = name_hash.Count();
		for (int i = 0; i < len; i++) {
			Cache_dir_itm dir_itm = (Cache_dir_itm)name_hash.FetchAt(i);
			dir_tbl.Db_save(dir_itm);
		}
	}
	public void Db_term() {
		dir_tbl.Db_term();
	}
	public Cache_dir_itm Get_itm_by_name(byte[] dir_bry) {
		Cache_dir_itm dir_itm = (Cache_dir_itm)name_hash.Fetch(dir_bry);
		if (dir_itm == null)			{								// not in memory
			dir_itm = dir_tbl.Select(dir_bry);							// check db
			if (dir_itm == Cache_dir_itm.Null) {						// not in db
				int dir_id = cache_mgr.Next_id();
				dir_itm = new Cache_dir_itm().Init_by_make(dir_id, dir_bry);
			}
			name_hash.Add(dir_bry, dir_itm);
			id_hash.Add(dir_itm.Id(), dir_itm);
		}
		return dir_itm;
	}
	public Cache_dir_itm Get_itm_by_id(int id) {
		return (Cache_dir_itm)id_hash.Fetch(id);
	}
	public void Load_all() {
		ListAdp list = ListAdp_.new_();
		dir_tbl.Select_all(list);
		int len = list.Count();
		id_hash.Clear();
		name_hash.Clear();
		for (int i = 0; i < len; i++) {
			Cache_dir_itm dir_itm = (Cache_dir_itm)list.FetchAt(i);
			name_hash.Add(dir_itm.Dir_bry(), dir_itm);
			id_hash.Add(dir_itm.Id(), dir_itm);
		}
	}
}

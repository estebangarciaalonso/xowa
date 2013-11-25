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
import gplx.dbs.*; import gplx.xowa.users.data.*;
public class Cache_mgr implements Xou_data_wkr {
	private Cache_cfg_mgr cfg_mgr;
	private Cache_dir_mgr dir_mgr;
	private Cache_fil_mgr fil_mgr;
	public Cache_mgr() {
		cfg_mgr = new Cache_cfg_mgr(this);
		dir_mgr = new Cache_dir_mgr(this);
		fil_mgr = new Cache_fil_mgr(this);
	}
	public int Next_id() {return cfg_mgr.Next_id();}
	public void Db_init(Xou_data_mgr data_mgr) {
		Db_provider provider = data_mgr.Provider();
		cfg_mgr.Db_init(provider);
		dir_mgr.Db_init(provider);
		fil_mgr.Db_init(provider);
		data_mgr.Wkr_reg(this);
	}
	public void Db_when_new(Xou_data_mgr data_mgr) {
		Db_provider provider = data_mgr.Provider();
		cfg_mgr.Db_when_new(provider);
		dir_mgr.Db_when_new(provider);
		fil_mgr.Db_when_new(provider);
	}
	public void Db_save(Xou_data_mgr data_mgr) {
		cfg_mgr.Db_save();
		dir_mgr.Db_save();
		fil_mgr.Db_save();
	}
	public void Db_term(Xou_data_mgr data_mgr) {
		cfg_mgr.Db_term();
		dir_mgr.Db_term();
		fil_mgr.Db_term();
	}
	public void Add(Io_url url, String dir, String fil, int ext_id, DateAdp modified, String hash, long bin_len, boolean fil_is_orig, int fil_w, int fil_h, int fil_thumbtime) {
		int dir_id = dir_mgr.Get_dir_id(dir);
		Cache_fil_itm fil_itm = fil_mgr.Get_or_new(dir_id, fil, fil_is_orig, fil_w, fil_h, fil_thumbtime, ext_id, bin_len);
		fil_itm.Cache_time_now_();
		if (fil_itm.Cmd_mode() == Db_cmd_mode.Create)
			cfg_mgr.Cache_len_add(bin_len);
		if (cfg_mgr.Cache_len() > cfg_mgr.Cache_max())
			Compress();
	}
	public void Compress() {
		// sort by cache time
		// delete from fs
		// update entries in mem
	}
}
/*
xowa_cfg: grp,key,val
xowa_xtn: xtn_id,xtn_name,xtn_created_in
cache_dir: dir_id,dir_name
cache_fil: uid,dir_id,...
if (!data_mgr.Xtn_created('fsdb.cache'))
create()
*/

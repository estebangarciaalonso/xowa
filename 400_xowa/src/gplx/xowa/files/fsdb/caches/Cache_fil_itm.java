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
class Cache_fil_itm {
	public int Uid() {return uid;} private int uid;
	public int Dir_id() {return dir_id;} private int dir_id;
	public String Fil_name() {return fil_name;} private String fil_name;
	public boolean Fil_is_orig() {return fil_is_orig;} private boolean fil_is_orig;
	public int Fil_w() {return fil_w;} private int fil_w;
	public int Fil_h() {return fil_h;} private int fil_h;
	public int Fil_thumbtime() {return fil_thumbtime;} private int fil_thumbtime;
	public int Fil_ext() {return fil_ext;} private int fil_ext;
	public long Fil_size() {return fil_size;} private long fil_size;
	public long Cache_time() {return cache_time;} private long cache_time;
	public void Cache_time_now_() {
		this.cache_time = DateAdp_.Now().XtoUtc().Timestamp_unix();
		cmd_mode = Db_cmd_mode.X_to_update(cmd_mode);
	}
	public byte Cmd_mode() {return cmd_mode;} public Cache_fil_itm Cmd_mode_(byte v) {cmd_mode = v; return this;} private byte cmd_mode;
	public Cache_fil_itm Init_by_load(DataRdr rdr) {
		uid = rdr.ReadInt(Cache_fil_tbl.Fld_uid);
		dir_id = rdr.ReadInt(Cache_fil_tbl.Fld_dir_id);
		fil_name = rdr.ReadStr(Cache_fil_tbl.Fld_fil_name);
		fil_is_orig = rdr.ReadByte(Cache_fil_tbl.Fld_fil_is_orig) != Byte_.Zero;
		fil_w = rdr.ReadInt(Cache_fil_tbl.Fld_fil_w);
		fil_h = rdr.ReadInt(Cache_fil_tbl.Fld_fil_h);
		fil_thumbtime = rdr.ReadInt(Cache_fil_tbl.Fld_fil_thumbtime);
		fil_ext = rdr.ReadInt(Cache_fil_tbl.Fld_fil_ext);
		fil_size = rdr.ReadLong(Cache_fil_tbl.Fld_fil_size);
		cache_time = rdr.ReadLong(Cache_fil_tbl.Fld_cache_time);
		cmd_mode = Db_cmd_mode.Ignore;
		return this;
	}
	public Cache_fil_itm Init_by_make(int uid, int dir_id, String fil_name, boolean fil_is_orig, int fil_w, int fil_h, int fil_thumbtime, int fil_ext, long fil_size) {
		this.uid = uid;
		this.dir_id = dir_id;
		this.fil_name = fil_name;
		this.fil_is_orig = fil_is_orig;
		this.fil_w = fil_w;
		this.fil_h = fil_h;
		this.fil_thumbtime = fil_thumbtime;
		this.fil_ext = fil_ext;
		this.fil_size = fil_size;
		this.Cache_time_now_();
		cmd_mode = Db_cmd_mode.Create;
		return this;
	}
        public static final Cache_fil_itm Null = new Cache_fil_itm(); public Cache_fil_itm() {}
}

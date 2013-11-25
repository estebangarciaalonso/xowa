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
class Cache_dir_itm {
	public int Id() {return id;} private int id;
	public String Dir() {return dir;} private String dir;
	public byte Cmd_mode() {return cmd_mode;} public Cache_dir_itm Cmd_mode_(byte v) {cmd_mode = v; return this;} private byte cmd_mode;
	public Cache_dir_itm Init_by_load(DataRdr rdr) {
		id = rdr.ReadInt(Cache_dir_tbl.Fld_dir_id);
		dir = rdr.ReadStr(Cache_dir_tbl.Fld_dir_name);
		cmd_mode = Db_cmd_mode.Ignore;
		return this;
	}
	public Cache_dir_itm Init_by_make(int id, String dir) {
		this.id = id;
		this.dir = dir;
		cmd_mode = Db_cmd_mode.Create;
		return this;
	}
	public static final Cache_dir_itm Null = new Cache_dir_itm();
}

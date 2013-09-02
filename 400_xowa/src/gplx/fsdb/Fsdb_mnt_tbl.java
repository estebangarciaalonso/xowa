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
package gplx.fsdb; import gplx.*;
import gplx.dbs.*;
public class Fsdb_mnt_tbl {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);
	}
	private static Db_stmt Insert_stmt(Db_provider p) {
		return Db_stmt_.new_insert_(p, Tbl_name, Fld_vlm_key, Fld_vlm_url);
	}
	public static void Insert(Db_provider p, String key, String url) {
		Db_stmt stmt = Insert_stmt(p);
		Insert(stmt, key, url);
		stmt.Rls();
	}
	public static void Insert(Db_stmt stmt, String key, String url) {
		stmt.Clear()
		.Val_str_(key)
		.Val_str_(url)
		.Exec_insert();
	}	
	public static Fsdb_mnt_itm[] Select_all(Db_provider p, Io_url dir) {
		ListAdp rv = ListAdp_.new_();
		Db_qry qry = Db_qry_select.new_().From_(Tbl_name).Cols_all_().OrderBy_asc_(Fld_vlm_key);
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			while (rdr.MoveNextPeer()) {
				String key = rdr.ReadStr(Fld_vlm_key);
				String url = rdr.ReadStr(Fld_vlm_url);
				Fsdb_mnt_itm mnt = new Fsdb_mnt_itm().Init(key, url);
				rv.Add(mnt);
			}
		}	finally {rdr.Rls();}
		return (Fsdb_mnt_itm[])rv.XtoAry(Fsdb_mnt_itm.class);
	}
	public static final String Tbl_name = "fsdb_mnt", Fld_vlm_key = "vlm_key", Fld_vlm_url = "vlm_url";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_mnt"
	,	"( vlm_key               varchar(255)        NOT NULL    PRIMARY KEY"
	,	", vlm_url               varchar(255)        NOT NULL"
	,	");"
	);
}

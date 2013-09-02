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
public class Fsdb_dir_tbl {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(p, Idx_name);
	}
	public static void Insert(Db_provider p, int id, String name, int owner_id) {
		Db_stmt stmt = Insert_stmt(p);
		try {Insert(stmt, id, name, owner_id);}
		finally {stmt.Rls();}
	}
	private static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_dir_id, Fld_dir_owner, Fld_dir_name);}
	private static void Insert(Db_stmt stmt, int id, String name, int owner_id) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(owner_id)
		.Val_str_(name)
		.Exec_insert();
	}	
	public static Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_dir_id), Fld_dir_owner, Fld_dir_name);}
	public static void Update(Db_stmt stmt, int id, String name, int owner_id) {
		stmt.Clear()
		.Val_int_(id)
		.Val_str_(name)
		.Val_int_(owner_id)
		.Exec_update();
	}	
	public static Fsdb_dir_itm Select_itm(Db_provider p, String dir_name) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_dir_id, Fld_dir_owner).Where_(Db_crt_.eq_(Fld_dir_name, dir_name));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			while (rdr.MoveNextPeer()) {
				return new Fsdb_dir_itm(rdr.ReadInt(Fld_dir_id), rdr.ReadInt(Fld_dir_owner), dir_name);
			}
			return Fsdb_dir_itm.Null;
		}
		finally {rdr.Rls();}
	}
	public static final String Tbl_name = "fsdb_dir", Fld_dir_id = "dir_id", Fld_dir_owner = "dir_owner", Fld_dir_name = "dir_name";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_dir"
	,	"( dir_id            integer             NOT NULL    PRIMARY KEY"
	,	", dir_owner         integer             NOT NULL"
	,	", dir_name          varchar(255)        NOT NULL"
	,	");"
	);
	public static final Db_idx_itm
	  Idx_name = Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS fsdb_dir__name       ON fsdb_dir (dir_name, dir_owner, dir_id);")
	;
}

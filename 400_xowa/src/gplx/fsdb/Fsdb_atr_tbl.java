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
public class Fsdb_atr_tbl {
	public Fsdb_atr_tbl Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_atr_id, Fld_atr_owner, Fld_atr_key, Fld_atr_val);}
	public void Insert(Db_provider p, int id, int owner, int key, String val) {
		Db_stmt stmt = Insert_stmt(p);
		Insert(stmt, id, owner, key, val);
		stmt.Rls();
	}
	public void Insert(Db_stmt stmt, int id, int owner, int key, String val) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(owner)
		.Val_int_(key)
		.Val_str_(val)
		.Exec_insert();
	}	
	public Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_atr_id), Fld_atr_owner, Fld_atr_key, Fld_atr_val);}
	public void Update(Db_stmt stmt, int id, int owner, int key, String val) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(owner)
		.Val_int_(key)
		.Val_str_(val)
		.Exec_update();
	}
	public static final String Tbl_name = "fsdb_atr", Fld_atr_id = "atr_id", Fld_atr_owner = "atr_owner", Fld_atr_key = "atr_key", Fld_atr_val = "atr_val";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_atr"
	,	"( atr_id                integer             NOT NULL    PRIMARY KEY"
	,	", atr_owner             integer             NOT NULL"
	,	", atr_key               integer             NOT NULL"
	,	", atr_val               varchar(1024)       NOT NULL"
	,	");"
	);
	public static final Db_idx_itm
	  Idx_owner = Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS fsdb_atr__owner      ON fsdb_atr (atr_owner, atr_id);")
	;
}

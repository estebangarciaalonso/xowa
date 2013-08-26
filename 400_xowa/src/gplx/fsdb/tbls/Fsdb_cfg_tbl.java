/*
XOWA: the extensible offline wiki application
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
package gplx.fsdb.tbls; import gplx.*; import gplx.fsdb.*;
import gplx.dbs.*;
public class Fsdb_cfg_tbl {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(p, Idx_main);
		Insert(p, "core", "next_id", "1");
	}
	public static void Insert(Db_provider p, String grp, String key, String val) {
		Db_stmt stmt = Insert_stmt(p);
		try {Insert(stmt, grp, key, val);}
		finally {stmt.Rls();}
	}
	private static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_cfg_grp, Fld_cfg_key, Fld_cfg_val);}
	private static void Insert(Db_stmt stmt, String grp, String key, String val) {
		stmt.Clear()
		.Val_str_(grp)
		.Val_str_(key)
		.Val_str_(val)
		.Exec_insert();
	}	
	public static void Update(Db_provider p, String grp, String key, String val) {		
		Db_stmt stmt = Update_stmt(p);
		try {Update(stmt, grp, key, val);}
		finally {stmt.Rls();}
	}
	private static Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_cfg_grp, Fld_cfg_key), Fld_cfg_val);}
	private static void Update(Db_stmt stmt, String grp, String key, String val) {
		stmt.Clear()
		.Val_str_(val)
		.Val_str_(grp)
		.Val_str_(key)
		.Exec_update();
	}
	public static int Update_next_id(Db_provider p) {
		int rv = Select_next_id(p);
		Update(p, "core", "next_id", Int_.XtoStr(rv + 1));
		return rv;
	}
	public static int Select_next_id(Db_provider p) {return Select_as_int(p, "core", "next_id");}
	public static int Select_as_int(Db_provider p, String grp, String key) {return Int_.parse_or_(Select_as_str(p, grp, key), Int_.MinValue);}
	public static String Select_as_str(Db_provider p, String grp, String key) {
		Db_qry_select qry = Db_qry_.select_val_(Tbl_name, Fld_cfg_val, gplx.criterias.Criteria_.And_many(Db_crt_.eq_(Fld_cfg_grp, grp), Db_crt_.eq_(Fld_cfg_key, key)));
		return (String)qry.ExecRdr_val(p);
	}
	public static final String Tbl_name = "fsdb_cfg", Fld_cfg_grp = "cfg_grp", Fld_cfg_key = "cfg_key", Fld_cfg_val = "cfg_val";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_cfg"
	,	"( cfg_grp           varchar(255)        NOT NULL" 
	,	", cfg_key           varchar(255)        NOT NULL"
	,	", cfg_val           varchar(1024)       NOT NULL"
	,	");"
	);
	public static final Db_idx_itm
	  Idx_main = Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS fsdb_cfg__main       ON fsdb_cfg (cfg_grp, cfg_key, cfg_val);")
	;
}

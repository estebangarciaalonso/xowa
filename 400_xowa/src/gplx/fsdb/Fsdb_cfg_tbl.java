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
public class Fsdb_cfg_tbl {
	private Db_provider provider;
	private Db_stmt stmt_insert, stmt_update, stmt_select;
	public Fsdb_cfg_tbl(Db_provider provider, boolean created) {
		this.provider = provider;
		if (created) Create_table();
	}
	public void Rls() {
		if (stmt_insert != null) {stmt_insert.Rls(); stmt_insert = null;}
		if (stmt_update != null) {stmt_update.Rls(); stmt_update = null;}
		if (stmt_select != null) {stmt_select.Rls(); stmt_select = null;}
	}
	public void Create_table() {
		Sqlite_engine_.Tbl_create(provider, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(provider, Idx_main);
	}
	private Db_stmt Insert_stmt() {return Db_stmt_.new_insert_(provider, Tbl_name, Fld_cfg_grp, Fld_cfg_key, Fld_cfg_val);}
	public void Insert(String grp, String key, String val) {
		if (stmt_insert == null) stmt_insert = Insert_stmt();
		stmt_insert.Clear()
		.Val_str_(grp)
		.Val_str_(key)
		.Val_str_(val)
		.Exec_insert();
	}	
	private Db_stmt Update_stmt() {return Db_stmt_.new_update_(provider, Tbl_name, String_.Ary(Fld_cfg_grp, Fld_cfg_key), Fld_cfg_val);}
	public void Update(String grp, String key, String val) {
		if (stmt_update == null) stmt_update = Update_stmt();
		stmt_update.Clear()
		.Val_str_(val)
		.Val_str_(grp)
		.Val_str_(key)
		.Exec_update();
	}
	public int Select_as_int(String grp, String key) {return Int_.parse_or_(Select_as_str(grp, key), Int_.MinValue);}
	private Db_stmt Select_stmt() {
		Db_qry_select qry = Db_qry_.select_val_(Tbl_name, Fld_cfg_val, gplx.criterias.Criteria_.And_many(Db_crt_.eq_(Fld_cfg_grp, ""), Db_crt_.eq_(Fld_cfg_key, "")));
		return provider.Prepare(qry);
	}
	public String Select_as_str(String grp, String key) {
		if (stmt_select == null) stmt_select = Select_stmt();
		return (String)stmt_select.Clear()
			.Val_str_(grp)
			.Val_str_(key)
			.Exec_select_val();
	}
	public DataRdr Select_by_grp(String grp) {
		Db_qry_select qry = Db_qry_.select_cols_(Tbl_name, gplx.criterias.Criteria_.And_many(Db_crt_.eq_(Fld_cfg_grp, "")), Fld_cfg_key, Fld_cfg_val);
		return provider.Prepare(qry).Clear()
			.Val_str_(grp)
			.Exec_select();
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

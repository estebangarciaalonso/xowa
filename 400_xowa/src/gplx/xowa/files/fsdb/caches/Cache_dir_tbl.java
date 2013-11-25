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
class Cache_dir_tbl {
	private Db_provider provider;
	private Db_stmt select_stmt;
	private Db_stmt_bldr stmt_bldr = new Db_stmt_bldr(Tbl_name, String_.Ary(Fld_dir_id), Fld_dir_name);
	public void Db_init(Db_provider provider) {this.provider = provider;}
	public void Db_save(Cache_dir_itm itm) {
		if (stmt_bldr == null) stmt_bldr.Init(provider);
		Db_stmt stmt = stmt_bldr.Get(itm.Cmd_mode());
		switch (itm.Cmd_mode()) {
			case Db_cmd_mode.Create:	stmt.Clear().Val_int_(itm.Id())	.Val_str_(itm.Dir()).Exec_insert(); break;
			case Db_cmd_mode.Update:	stmt.Clear()					.Val_str_(itm.Dir()).Exec_update(); break;
			case Db_cmd_mode.Delete:	stmt.Clear().Val_int_(itm.Id()).Exec_delete();	break;
			case Db_cmd_mode.Ignore:	break;
			default:					throw Err_.unhandled(itm.Cmd_mode());
		}
		itm.Cmd_mode_(Db_cmd_mode.Ignore);
	}
	public void Db_term() {
		if (select_stmt != null) select_stmt.Rls();
		if (stmt_bldr != null) stmt_bldr.Rls();
	}
	public void Db_when_new() {
		Sqlite_engine_.Tbl_create(provider, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(provider, Idx_name);
	}
	public Cache_dir_itm Select(String name) {
		if (select_stmt == null) select_stmt = Db_stmt_.new_select_(provider, Tbl_name, String_.Ary(Fld_dir_name));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = select_stmt.Clear()
			.Val_str_(name)
			.Exec_select();
			if (rdr.MoveNextPeer()) {
				return new Cache_dir_itm().Init_by_load(rdr);
			}
			else
				return Cache_dir_itm.Null;
		}
		catch (Exception e) {select_stmt = null; throw Err_.err_(e, "stmt failed");}
		finally {rdr.Rls();}
	}		
	private static final String Tbl_sql = String_.Concat_lines_nl
	( "CREATE TABLE cache_dir"
	, "( dir_id            integer       NOT NULL        PRIMARY KEY"
	, ", dir_name          varchar(255)"
	, ");"
	);
	public static final String Tbl_name = "cache_dir"
	, Fld_dir_id = "dir_id", Fld_dir_name = "dir_name"
	;
	private static final Db_idx_itm
		Idx_name     		= Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS cache_dir__name ON cache_dir (dir_name);")
	;
}

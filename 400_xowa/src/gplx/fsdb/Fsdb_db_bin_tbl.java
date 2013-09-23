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
public class Fsdb_db_bin_tbl {
	public static void Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);}
	public static void Commit_all(Db_provider provider, Fsdb_db_bin_fil[] ary) {
		stmt_bldr.Init(provider);
		try {
			int len = ary.length;
			for (int i = 0; i < len; i++)
				Commit_itm(ary[i]);
			stmt_bldr.Commit();
		}	finally {stmt_bldr.Rls();}
	}
	private static void Commit_itm(Fsdb_db_bin_fil itm) {
		Db_stmt stmt = stmt_bldr.Get(itm.Cmd_mode());
		switch (itm.Cmd_mode()) {
			case Db_cmd_mode.Create:	stmt.Clear().Val_int_(itm.Id())	.Val_str_(itm.Url().NameAndExt()).Val_long_(itm.Bin_len()).Exec_insert(); break;
			case Db_cmd_mode.Update:	stmt.Clear()					.Val_str_(itm.Url().NameAndExt()).Val_long_(itm.Bin_len()).Val_int_(itm.Id()).Exec_update(); break;
			case Db_cmd_mode.Delete:	stmt.Clear().Val_int_(itm.Id()).Exec_delete();	break;
			case Db_cmd_mode.Ignore:	break;
			default:					throw Err_.unhandled(itm.Cmd_mode());
		}
		itm.Cmd_mode_(Db_cmd_mode.Ignore);
	}
	public static Fsdb_db_bin_fil[] Select_all(Db_provider p, Io_url dir) {
		ListAdp rv = ListAdp_.new_();
		Db_qry qry = Db_qry_select.new_().From_(Tbl_name).Cols_all_().OrderBy_asc_(Fld_fdb_id);
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			while (rdr.MoveNextPeer()) {
				Fsdb_db_bin_fil itm = Fsdb_db_bin_fil.load_(rdr, dir);
				rv.Add(itm);
			}
		} finally {rdr.Rls();}
		return (Fsdb_db_bin_fil[])rv.XtoAry(Fsdb_db_bin_fil.class);
	}
	public static final String Tbl_name = "fsdb_db_bin", Fld_fdb_id = "fdb_id", Fld_fdb_url = "fdb_url", Fld_fdb_bin_len = "fdb_bin_len";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_db_bin"
	,	"( fdb_id                integer             NOT NULL    PRIMARY KEY"
	,	", fdb_url               varchar(255)        NOT NULL"
	,	", fdb_bin_len           bigint              NOT NULL"
	,	");"
	);	
	private static Db_stmt_bldr stmt_bldr = new Db_stmt_bldr(Tbl_name, String_.Ary(Fld_fdb_id), Fld_fdb_url, Fld_fdb_bin_len);
}

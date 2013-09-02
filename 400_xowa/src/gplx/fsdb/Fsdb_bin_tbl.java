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
public class Fsdb_bin_tbl {
	public static void Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);}
	public static void Insert(Db_provider p, int id, byte[] data) {
		Db_stmt stmt = Insert_stmt(p);
		try {Insert(stmt, id, data);}
		finally {stmt.Rls();}
	}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_bin_owner, Fld_bin_data);}
	public static void Insert(Db_stmt stmt, int id, byte[] data) {
		stmt.Clear()
		.Val_int_(id)
		.Val_bry_(data)
		.Exec_insert();
	}	
	public static void Update(Db_provider p, int id, byte[] data) {
		Db_stmt stmt = Update_stmt(p);
		try {Update(stmt, id, data);}
		finally {stmt.Rls();}
	}
	private static Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_bin_owner), Fld_bin_data);}
	private static void Update(Db_stmt stmt, int id, byte[] data) {
		stmt.Clear()
		.Val_int_(id)
		.Val_bry_(data)
		.Exec_update();
	}	
	public static void Delete(Db_provider p, int id) {
		Db_stmt stmt = Delete_stmt(p);
		try {Delete(stmt, id);}
		finally {stmt.Rls();}
	}
	private static Db_stmt Delete_stmt(Db_provider p) {return Db_stmt_.new_delete_(p, Tbl_name, Fld_bin_owner);}
	private static void Delete(Db_stmt stmt, int id) {
		stmt.Clear()
		.Val_int_(id)
		.Exec_delete();
	}	
	public static Fsdb_bin_itm Select_itm(Db_provider p, int owner) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_bin_data).Where_(Db_crt_.eq_(Fld_bin_owner, owner));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			if (rdr.MoveNextPeer()) {
				return new Fsdb_bin_itm(owner, rdr.ReadBry(Fld_bin_data));
			}
			else
				return Fsdb_bin_itm.Null;
		}
		finally {rdr.Rls();}
	}
	public static gplx.ios.Input_stream_adp Select_stream(Db_provider p, int owner) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_bin_data).Where_(Db_crt_.eq_(Fld_bin_owner, owner));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			if (rdr.MoveNextPeer()) 
				return rdr.ReadInputStream(Fld_bin_data);
			else
				return gplx.ios.Input_stream_adp_.Null;
		}
		finally {rdr.Rls();}
	}
	public static final String Tbl_name = "fsdb_bin", Fld_bin_owner = "bin_owner", Fld_bin_data = "bin_data";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_bin"
	,	"( bin_owner             integer             NOT NULL    PRIMARY KEY"
	,	", bin_data              mediumblob          NOT NULL"
	,	");"
	);
}

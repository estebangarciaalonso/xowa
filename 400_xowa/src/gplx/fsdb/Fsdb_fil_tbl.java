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
public class Fsdb_fil_tbl {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); 
		Sqlite_engine_.Idx_create(p, Idx_name, Idx_owner);
	}
	public static void Insert(Db_provider p, int id, int owner_id, String name, int xtn_id, int ext_id, long size, String modified, String hash) {
		Db_stmt stmt = Insert_stmt(p);
		try {Insert(stmt, id, owner_id, name, xtn_id, ext_id, size, modified, hash);}
		finally {stmt.Rls();}
	}
	private static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_fil_id, Fld_fil_owner, Fld_fil_name, Fld_fil_xtn_id, Fld_fil_ext_id, Fld_fil_size, Fld_fil_modified, Fld_fil_hash);}
	private static void Insert(Db_stmt stmt, int id, int owner_id, String name, int xtn_id, int ext_id, long size, String modified, String hash) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(owner_id)
		.Val_str_(name)
		.Val_int_(xtn_id)
		.Val_int_(ext_id)
		.Val_long_(size)
		.Val_str_(modified)
		.Val_str_(hash)
		.Exec_insert();
	}	
	public static Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_fil_id), Fld_fil_owner, Fld_fil_ext_id, Fld_fil_name);}
	public static void Update(Db_stmt stmt, int id, int owner_id, String name, int ext_id) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(owner_id)
		.Val_int_(ext_id)
		.Val_str_(name)
		.Exec_update();
	}	
	public static void Delete_by_id(Db_provider p, int id) {
		Db_stmt stmt = Delete_by_id_stmt(p);
		try {Delete_by_id(stmt, id);}
		finally {stmt.Rls();}
	}
	private static Db_stmt Delete_by_id_stmt(Db_provider p) {return Db_stmt_.new_delete_(p, Tbl_name, Fld_fil_id);}
	private static void Delete_by_id(Db_stmt stmt, int id) {
		stmt.Clear()
		.Val_int_(id)
		.Exec_delete();
	}	
	public static Fsdb_fil_itm Select_itm_by_name(Db_provider p, int dir_id, String fil_name) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_fil_id, Fld_fil_ext_id).Where_(gplx.criterias.Criteria_.And_many(Db_crt_.eq_(Fld_fil_owner, dir_id), Db_crt_.eq_(Fld_fil_name, fil_name)));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			if (rdr.MoveNextPeer())
				return new Fsdb_fil_itm(rdr.ReadInt(Fld_fil_id), dir_id, rdr.ReadInt(Fld_fil_ext_id), fil_name);
			else
				return Fsdb_fil_itm.Null;
		}
		finally {rdr.Rls();}
	}
	public static Fsdb_fil_itm Select_itm_by_id(Db_provider p, int fil_id) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_fil_id, Fld_fil_owner, Fld_fil_ext_id, Fld_fil_name).Where_(Db_crt_.eq_(Fld_fil_id, fil_id));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			if (rdr.MoveNextPeer())
				return new Fsdb_fil_itm(fil_id, rdr.ReadInt(Fld_fil_owner), rdr.ReadInt(Fld_fil_ext_id), rdr.ReadStr(Fld_fil_name));
			else
				return Fsdb_fil_itm.Null;
		}
		finally {rdr.Rls();}
	}
	public static final String Tbl_name = "fsdb_fil", Fld_fil_id = "fil_id", Fld_fil_owner = "fil_owner", Fld_fil_name = "fil_name", Fld_fil_xtn_id = "fil_xtn_id", Fld_fil_ext_id = "fil_ext_id"
	, Fld_fil_size = "fil_size", Fld_fil_modified = "fil_modified", Fld_fil_hash = "fil_hash"
	;
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_fil"
	,	"( fil_id            integer             NOT NULL    PRIMARY KEY"
	,	", fil_owner         integer             NOT NULL"
	,	", fil_xtn_id        integer             NOT NULL"
	,	", fil_ext_id        integer             NOT NULL"	// group ints at beginning of table
	,	", fil_size          bigint              NOT NULL"
	,	", fil_name          varchar(255)        NOT NULL"
	,	", fil_modified      varchar(14)         NOT NULL"	// stored as yyyyMMddHHmmss
	,	", fil_hash          varchar(40)         NOT NULL"
	,	");"
	);
	public static final Db_idx_itm
	  Idx_name = Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS fsdb_fil__name       ON fsdb_fil (fil_name, fil_owner, fil_id, fil_ext_id);")
	, Idx_owner = Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS fsdb_fil__owner     ON fsdb_fil (fil_owner, fil_name, fil_id);")
	;
}

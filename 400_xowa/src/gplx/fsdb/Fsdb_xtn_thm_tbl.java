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
public class Fsdb_xtn_thm_tbl {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(p, Idx_name);
	}
	public static void Insert(Db_provider p, int id, int thm_owner, int width, int thumbtime, boolean thm_is_orig, int height, long size, String modified, String sha1) {
		Db_stmt stmt = Insert_stmt(p);
		try {Insert(stmt, id, thm_owner, width, thumbtime, thm_is_orig, height, size, modified, sha1);}
		finally {stmt.Rls();}
	}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_thm_id, Fld_thm_owner, Fld_thm_width, Fld_thm_thumbtime, Fld_thm_is_orig, Fld_thm_height, Fld_thm_size, Fld_thm_modified, Fld_thm_hash);}
	public static void Insert(Db_stmt stmt, int id, int thm_owner, int width, int thumbtime, boolean thm_is_orig, int height, long size, String modified, String sha1) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(thm_owner)
		.Val_int_(width)
		.Val_int_(thumbtime * Thumbtime_multiplier)
		.Val_byte_by_bool_(thm_is_orig)	
		.Val_int_(height)
		.Val_long_(size)
		.Val_str_(modified)
		.Val_str_(sha1)
		.Exec_insert();
	}
	public static void Update(Db_provider p, int id, int thm_owner, int width, int thumbtime, boolean thm_is_orig, long size, int height, String modified, String sha1) {
		Db_stmt stmt = Insert_stmt(p);
		Update(stmt, id, thm_owner, width, thumbtime, thm_is_orig, height, size, modified, sha1);
		stmt.Rls();
	}
	public static Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_thm_id), Fld_thm_owner, Fld_thm_width, Fld_thm_thumbtime, Fld_thm_is_orig, Fld_thm_height, Fld_thm_size, Fld_thm_modified, Fld_thm_hash);}
	public static void Update(Db_stmt stmt, int id, int thm_owner, int width, int thumbtime, boolean thm_is_orig, int height, long size, String modified, String sha1) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(thm_owner)
		.Val_int_(width)
		.Val_int_(thumbtime * Thumbtime_multiplier)
		.Val_byte_by_bool_(thm_is_orig)
		.Val_int_(height)
		.Val_long_(size)
		.Val_str_(modified)
		.Val_str_(sha1)
		.Exec_update();
	}
	public static void Delete_by_id(Db_provider p, int id) {
		Db_stmt stmt = Delete_by_id_stmt(p);
		try {Delete_by_id(stmt, id);}
		finally {stmt.Rls();}
	}
	public static Db_stmt Delete_by_id_stmt(Db_provider p) {return Db_stmt_.new_delete_(p, Tbl_name, Fld_thm_id);}
	public static void Delete_by_id(Db_stmt stmt, int thm_id) {
		stmt.Clear()
		.Val_int_(thm_id)
		.Exec_delete();
	}
	public static Fsdb_xtn_thm_itm Select_itm_by_id(Db_provider p, int thm_id) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_thm_id, Fld_thm_owner, Fld_thm_width, Fld_thm_thumbtime, Fld_thm_is_orig, Fld_thm_height, Fld_thm_size, Fld_thm_modified, Fld_thm_hash)
			.Where_(Db_crt_.eq_(Fld_thm_id, thm_id));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			if (rdr.MoveNextPeer()) {
				Fsdb_xtn_thm_itm itm = new Fsdb_xtn_thm_itm
				( rdr.ReadInt(Fld_thm_id), rdr.ReadInt(Fld_thm_owner), rdr.ReadInt(Fld_thm_width), rdr.ReadInt(Fld_thm_thumbtime) / Thumbtime_multiplier, rdr.ReadByte(Fld_thm_is_orig) == Bool_.Y_byte
				, rdr.ReadInt(Fld_thm_height), rdr.ReadLong(Fld_thm_size), rdr.ReadStr(Fld_thm_modified), rdr.ReadStr(Fld_thm_hash)
				);
				return itm;
			}
			else
				return Fsdb_xtn_thm_itm.Null;
		}
		finally {rdr.Rls();}
	}
	public static Fsdb_xtn_thm_itm Select_itm_by_fil_width(Db_provider p, int owner, int width, int thumbtime) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_thm_id, Fld_thm_is_orig, Fld_thm_size, Fld_thm_modified, Fld_thm_hash, Fld_thm_width, Fld_thm_height, Fld_thm_thumbtime)
			.Where_(gplx.criterias.Criteria_.And_many(Db_crt_.eq_(Fld_thm_owner, owner), Db_crt_.eq_(Fld_thm_width, width), Db_crt_.eq_(Fld_thm_thumbtime, thumbtime * Thumbtime_multiplier)));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			if (rdr.MoveNextPeer())
				return new Fsdb_xtn_thm_itm
				( rdr.ReadInt(Fld_thm_id), owner, rdr.ReadInt(Fld_thm_width), rdr.ReadInt(Fld_thm_thumbtime) / Thumbtime_multiplier, rdr.ReadByte(Fld_thm_is_orig) == Bool_.Y_byte
				, rdr.ReadInt(Fld_thm_height), rdr.ReadLong(Fld_thm_size), rdr.ReadStr(Fld_thm_modified), rdr.ReadStr(Fld_thm_hash)
				);
			else
				return Fsdb_xtn_thm_itm.Null;
		}
		finally {rdr.Rls();}
	}
	public static Fsdb_xtn_thm_itm[] Select_itms_by_owner(Db_provider p, int owner) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_thm_id, Fld_thm_owner, Fld_thm_width, Fld_thm_thumbtime, Fld_thm_is_orig, Fld_thm_height, Fld_thm_size, Fld_thm_modified, Fld_thm_hash)
			.Where_(Db_crt_.eq_(Fld_thm_owner, owner));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			ListAdp rv = ListAdp_.Null;
			while (rdr.MoveNextPeer()) {
				if (rv == ListAdp_.Null) rv = ListAdp_.new_();
				Fsdb_xtn_thm_itm itm = new Fsdb_xtn_thm_itm
				( rdr.ReadInt(Fld_thm_id), rdr.ReadInt(Fld_thm_owner), rdr.ReadInt(Fld_thm_width), rdr.ReadInt(Fld_thm_thumbtime) / Thumbtime_multiplier, rdr.ReadByte(Fld_thm_is_orig) == Bool_.Y_byte
				, rdr.ReadInt(Fld_thm_height), rdr.ReadLong(Fld_thm_size), rdr.ReadStr(Fld_thm_modified), rdr.ReadStr(Fld_thm_hash)
				);
				rv.Add(itm);
			}
			return rv == ListAdp_.Null ? Fsdb_xtn_thm_itm.Ary_empty : (Fsdb_xtn_thm_itm[])rv.XtoAry(Fsdb_xtn_thm_itm.class);
		}
		finally {rdr.Rls();}
	}
	public static final String Tbl_name = "fsdb_xtn_thm"
	, Fld_thm_id = "thm_id", Fld_thm_owner = "thm_owner", Fld_thm_width = "thm_width", Fld_thm_thumbtime = "thm_thumbtime"
	, Fld_thm_is_orig = "thm_is_orig", Fld_thm_height = "thm_height"
	, Fld_thm_size = "thm_size", Fld_thm_modified = "thm_modified", Fld_thm_hash = "thm_hash";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_xtn_thm"
	,	"( thm_id            integer             NOT NULL    PRIMARY KEY"
	,	", thm_owner         integer             NOT NULL"
	,	", thm_width         integer             NOT NULL"
	,	", thm_thumbtime     integer             NOT NULL"	// stored in ms
	,	", thm_is_orig       tinyint             NOT NULL"
	,	", thm_height        integer             NOT NULL"
	,	", thm_size          bigint              NOT NULL"
	,	", thm_modified      varchar(14)         NOT NULL"	// stored as yyyyMMddHHmmss
	,	", thm_hash          varchar(40)         NOT NULL"
	,	");"
	);
	public static final Db_idx_itm
	  Idx_name = Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS fsdb_xtn_thm__owner      ON fsdb_xtn_thm (thm_owner, thm_id, thm_width, thm_thumbtime, thm_is_orig);")
	;
	private static final int Thumbtime_multiplier = 100;	// store thumbtime in ms; EX: 2 secs -> 2000
}

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
public class Fsdb_wmf_img_tbl {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(p, Idx_name);
	}
	public static void Insert(Db_provider p, int id, int img_owner, int width, int thumbtime, boolean img_is_orig, int height, long size, String modified, String sha1) {
		Db_stmt stmt = Insert_stmt(p);
		try {Insert(stmt, id, img_owner, width, thumbtime, img_is_orig, height, size, modified, sha1);}
		finally {stmt.Rls();}
	}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_img_id, Fld_img_owner, Fld_img_width, Fld_img_thumbtime, Fld_img_is_orig, Fld_img_height, Fld_img_size, Fld_img_modified, Fld_img_sha1);}
	public static void Insert(Db_stmt stmt, int id, int img_owner, int width, int thumbtime, boolean img_is_orig, int height, long size, String modified, String sha1) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(img_owner)
		.Val_int_(width)
		.Val_int_(thumbtime * Thumbtime_multiplier)
		.Val_byte_by_bool_(img_is_orig)	
		.Val_int_(height)
		.Val_long_(size)
		.Val_str_(modified)
		.Val_str_(sha1)
		.Exec_insert();
	}
	public static void Update(Db_provider p, int id, int img_owner, int width, int thumbtime, boolean img_is_orig, long size, int height, String modified, String sha1) {
		Db_stmt stmt = Insert_stmt(p);
		Update(stmt, id, img_owner, width, thumbtime, img_is_orig, height, size, modified, sha1);
		stmt.Rls();
	}
	public static Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_img_id), Fld_img_owner, Fld_img_width, Fld_img_thumbtime, Fld_img_is_orig, Fld_img_height, Fld_img_size, Fld_img_modified, Fld_img_sha1);}
	public static void Update(Db_stmt stmt, int id, int img_owner, int width, int thumbtime, boolean img_is_orig, int height, long size, String modified, String sha1) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(img_owner)
		.Val_int_(width)
		.Val_int_(thumbtime * Thumbtime_multiplier)
		.Val_byte_by_bool_(img_is_orig)
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
	public static Db_stmt Delete_by_id_stmt(Db_provider p) {return Db_stmt_.new_delete_(p, Tbl_name, Fld_img_id);}
	public static void Delete_by_id(Db_stmt stmt, int img_id) {
		stmt.Clear()
		.Val_int_(img_id)
		.Exec_delete();
	}
	public static Fsdb_wmf_img_itm Select_itm_by_id(Db_provider p, int img_id) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_img_id, Fld_img_owner, Fld_img_width, Fld_img_thumbtime, Fld_img_is_orig, Fld_img_height, Fld_img_size, Fld_img_modified, Fld_img_sha1)
			.Where_(Db_crt_.eq_(Fld_img_id, img_id));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			if (rdr.MoveNextPeer()) {
				Fsdb_wmf_img_itm itm = new Fsdb_wmf_img_itm
				( rdr.ReadInt(Fld_img_id), rdr.ReadInt(Fld_img_owner), rdr.ReadInt(Fld_img_width), rdr.ReadInt(Fld_img_thumbtime), rdr.ReadByte(Fld_img_is_orig) == Bool_.Y_byte
				, rdr.ReadInt(Fld_img_height), rdr.ReadLong(Fld_img_size), rdr.ReadStr(Fld_img_modified), rdr.ReadStr(Fld_img_sha1)
				);
				return itm;
			}
			else
				return Fsdb_wmf_img_itm.Null;
		}
		finally {rdr.Rls();}
	}
	public static Fsdb_wmf_img_itm Select_itm_by_fil_width(Db_provider p, int owner, int width, int thumbtime) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_img_id, Fld_img_is_orig, Fld_img_size, Fld_img_modified, Fld_img_sha1, Fld_img_width, Fld_img_height, Fld_img_thumbtime)
			.Where_(gplx.criterias.Criteria_.And_many(Db_crt_.eq_(Fld_img_owner, owner), Db_crt_.eq_(Fld_img_width, width), Db_crt_.eq_(Fld_img_thumbtime)));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			if (rdr.MoveNextPeer())
				return new Fsdb_wmf_img_itm
				( rdr.ReadInt(Fld_img_id), owner, rdr.ReadInt(Fld_img_width), rdr.ReadInt(Fld_img_thumbtime), rdr.ReadByte(Fld_img_is_orig) == Bool_.Y_byte
				, rdr.ReadInt(Fld_img_height), rdr.ReadLong(Fld_img_size), rdr.ReadStr(Fld_img_modified), rdr.ReadStr(Fld_img_sha1)
				);
			else
				return Fsdb_wmf_img_itm.Null;
		}
		finally {rdr.Rls();}
	}
	public static Fsdb_wmf_img_itm[] Select_itms_by_owner(Db_provider p, int owner) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_(Fld_img_id, Fld_img_owner, Fld_img_width, Fld_img_thumbtime, Fld_img_is_orig, Fld_img_height, Fld_img_size, Fld_img_modified, Fld_img_sha1)
			.Where_(Db_crt_.eq_(Fld_img_owner, owner));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			ListAdp rv = ListAdp_.Null;
			while (rdr.MoveNextPeer()) {
				if (rv == ListAdp_.Null) rv = ListAdp_.new_();
				Fsdb_wmf_img_itm itm = new Fsdb_wmf_img_itm
				( rdr.ReadInt(Fld_img_id), rdr.ReadInt(Fld_img_owner), rdr.ReadInt(Fld_img_width), rdr.ReadInt(Fld_img_thumbtime), rdr.ReadByte(Fld_img_is_orig) == Bool_.Y_byte
				, rdr.ReadInt(Fld_img_height), rdr.ReadLong(Fld_img_size), rdr.ReadStr(Fld_img_modified), rdr.ReadStr(Fld_img_sha1)
				);
				rv.Add(itm);
			}
			return rv == ListAdp_.Null ? Fsdb_wmf_img_itm.Ary_empty : (Fsdb_wmf_img_itm[])rv.XtoAry(Fsdb_wmf_img_itm.class);
		}
		finally {rdr.Rls();}
	}
	public static final String Tbl_name = "fsdb_wmf_img"
	, Fld_img_id = "img_id", Fld_img_owner = "img_owner", Fld_img_width = "img_width", Fld_img_thumbtime = "img_thumbtime"
	, Fld_img_is_orig = "img_is_orig", Fld_img_height = "img_height"
	, Fld_img_size = "img_size", Fld_img_modified = "img_modified", Fld_img_sha1 = "img_sha1";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_wmf_img"
	,	"( img_id            integer             NOT NULL    PRIMARY KEY"
	,	", img_owner         integer             NOT NULL"
	,	", img_width         integer             NOT NULL"
	,	", img_thumbtime     integer             NOT NULL"	// stored in ms
	,	", img_is_orig       tinyint             NOT NULL"
	,	", img_height        integer             NOT NULL"
	,	", img_size          bigint              NOT NULL"
	,	", img_modified      varchar(14)         NOT NULL"	// stored as yyyyMMddHHmmss
	,	", img_sha1          varchar(40)         NOT NULL"
	,	");"
	);
	public static final Db_idx_itm
	  Idx_name = Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS fsdb_wmf_img__owner      ON fsdb_wmf_img (img_owner, img_id, img_width, img_thumbtime, img_is_orig);")
	;
	private static final int Thumbtime_multiplier = 100;	// store thumbtime in ms; EX: 2 secs -> 2000
}

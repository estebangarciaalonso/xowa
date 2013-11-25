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
	private Db_provider provider;
	private Db_stmt stmt_insert, stmt_delete, stmt_select_by_id, stmt_select_by_fil_w, stmt_select_by_owner;
	public Fsdb_xtn_thm_tbl(Db_provider provider, boolean created) {
		this.provider = provider;
		if (created) Create_table();
	}
	private void Create_table() {
		Sqlite_engine_.Tbl_create(provider, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(provider, Idx_name);
	}
	public void Rls() {
		if (stmt_insert != null) {stmt_insert.Rls(); stmt_insert = null;}
		if (stmt_delete != null) {stmt_delete.Rls(); stmt_delete = null;}
		if (stmt_select_by_id != null) {stmt_select_by_id.Rls(); stmt_select_by_id = null;}
		if (stmt_select_by_fil_w != null) {stmt_select_by_fil_w.Rls(); stmt_select_by_fil_w = null;}
		if (stmt_select_by_id != null) {stmt_select_by_id.Rls(); stmt_select_by_id = null;}
		if (stmt_select_by_owner != null) {stmt_select_by_owner.Rls(); stmt_select_by_owner = null;}
	}
	private Db_stmt Insert_stmt() {return Db_stmt_.new_insert_(provider, Tbl_name, Fld_thm_id, Fld_thm_owner_id, Fld_thm_w, Fld_thm_h, Fld_thm_thumbtime, Fld_thm_bin_db_id, Fld_thm_size, Fld_thm_modified, Fld_thm_hash);}
	public void Insert(int id, int thm_owner_id, int width, int height, int thumbtime, int bin_db_id, long size, DateAdp modified, String hash) {
		if (stmt_insert == null) stmt_insert = Insert_stmt();
		try {
			stmt_insert.Clear()
			.Val_int_(id)
			.Val_int_(thm_owner_id)
			.Val_int_(width)
			.Val_int_(height)
			.Val_int_(thumbtime)
			.Val_int_(bin_db_id)
			.Val_long_(size)
			.Val_str_(Sqlite_engine_.X_date_to_str(modified))
			.Val_str_(hash)
			.Exec_insert();
		}	catch (Exception exc) {stmt_insert = null; throw Err_.err_(exc, "stmt failed");} // must reset stmt, else next call will fail
	}
	private Db_stmt Delete_stmt() {return Db_stmt_.new_delete_(provider, Tbl_name, Fld_thm_id);}
	public void Delete_by_id(int thm_id) {
		if (stmt_delete == null) stmt_delete = Delete_stmt();
		try {
			stmt_delete.Clear()
			.Val_int_(thm_id)
			.Exec_delete();
		}	catch (Exception exc) {stmt_delete = null; throw Err_.err_(exc, "stmt failed");} // must reset stmt, else next call will fail
	}
	private Db_stmt Select_by_itm_stmt() {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_all_().Where_(Db_crt_.eq_(Fld_thm_id, 0));
		return provider.Prepare(qry);
	}
	public Fsdb_xtn_thm_itm Select_itm_by_id(int thm_id) {
		if (stmt_select_by_id == null) stmt_select_by_id = Select_by_itm_stmt();
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = stmt_select_by_id.Clear()
				.Val_int_(thm_id)
				.Exec_select()
				;
			if (rdr.MoveNextPeer()) {
				return Fsdb_xtn_thm_itm.load_(rdr);
			}
			else
				return Fsdb_xtn_thm_itm.Null;
		}
		finally {rdr.Rls();}
	}
	private Db_stmt Select_by_fil_w_stmt() {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_all_()
			.Where_(gplx.criterias.Criteria_.And_many(Db_crt_.eq_(Fld_thm_owner_id, Int_.MinValue), Db_crt_.eq_(Fld_thm_w, Int_.MinValue), Db_crt_.eq_(Fld_thm_thumbtime, Int_.MinValue)));
		return provider.Prepare(qry);
	}
	public Fsdb_xtn_thm_itm Select_itm_by_fil_width(int owner, int width, int thumbtime) {
		if (stmt_select_by_fil_w == null) stmt_select_by_fil_w = Select_by_fil_w_stmt();
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = stmt_select_by_fil_w.Clear()
				.Val_int_(owner)
				.Val_int_(width)
				.Val_int_(thumbtime)
				.Exec_select()
				;
			if (rdr.MoveNextPeer())
				return Fsdb_xtn_thm_itm.load_(rdr);
			else
				return Fsdb_xtn_thm_itm.Null;
		}
		finally {rdr.Rls();}
	}
	private Db_stmt Select_itms_by_owner_stmt() {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_all_()
			.Where_(Db_crt_.eq_(Fld_thm_owner_id, Int_.MinValue));
		return provider.Prepare(qry);
	}
	public Fsdb_xtn_thm_itm[] Select_itms_by_owner(int owner) {
		if (stmt_select_by_owner == null) stmt_select_by_owner = Select_itms_by_owner_stmt();
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = stmt_select_by_fil_w.Clear()
				.Val_int_(owner)
				.Exec_select()
				;
			ListAdp rv = ListAdp_.Null;
			while (rdr.MoveNextPeer()) {
				if (rv == ListAdp_.Null) rv = ListAdp_.new_();
				rv.Add(Fsdb_xtn_thm_itm.load_(rdr));
			}
			return rv == ListAdp_.Null ? Fsdb_xtn_thm_itm.Ary_empty : (Fsdb_xtn_thm_itm[])rv.XtoAry(Fsdb_xtn_thm_itm.class);
		}
		finally {rdr.Rls();}
	}
	public static final String Tbl_name = "fsdb_xtn_thm"
	, Fld_thm_id = "thm_id", Fld_thm_owner_id = "thm_owner_id", Fld_thm_w = "thm_w", Fld_thm_h = "thm_h", Fld_thm_thumbtime = "thm_thumbtime"
	, Fld_thm_bin_db_id = "thm_bin_db_id", Fld_thm_size = "thm_size", Fld_thm_modified = "thm_modified", Fld_thm_hash = "thm_hash";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_xtn_thm"
	,	"( thm_id            integer             NOT NULL    PRIMARY KEY"
	,	", thm_owner_id      integer             NOT NULL"
	,	", thm_w             integer             NOT NULL"
	,	", thm_h             integer             NOT NULL"
	,	", thm_thumbtime     integer             NOT NULL"	// stored in ms
	,	", thm_bin_db_id     integer             NOT NULL"
	,	", thm_size          bigint              NOT NULL"
	,	", thm_modified      varchar(14)         NOT NULL"	// stored as yyyyMMddHHmmss
	,	", thm_hash          varchar(40)         NOT NULL"
	,	");"
	);
	public static final Db_idx_itm
	  Idx_name = Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS fsdb_xtn_thm__owner      ON fsdb_xtn_thm (thm_owner_id, thm_id, thm_w, thm_thumbtime);")
	;
//		public static final int Thumbtime_multiplier = 1000;	// store thumbtime in ms; EX: 2 secs -> 2000
	public static final DateAdp Modified_null = null;
	public static final String Hash_null = "";
}

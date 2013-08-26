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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*;
class Xodb_tbl_oimg_xfer_temp {
	public Xodb_tbl_oimg_xfer_temp Create_table(Db_provider p)		{Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_oxt_id, Fld_oxt_wiki_id, Fld_oxt_page_id, Fld_oxt_ttl, Fld_oxt_ext_id, Fld_oxt_width, Fld_oxt_height, Fld_oxt_time);}
	public void Insert(Db_stmt stmt, int id, byte wiki_id, int page_id, String ttl, int ext_id, int width, int height, double time) {
		stmt.Clear()
		.Val_int_(id)
		.Val_byte_(wiki_id)
		.Val_int_(page_id)
		.Val_str_(ttl)
		.Val_int_(ext_id)
		.Val_int_(width)
		.Val_int_(height)
		.Val_double_(time)
		.Exec_insert();
	}
	public static final String Tbl_name = "oimg_xfer_temp"
	, Fld_oxt_id = "oxt_id", Fld_oxt_wiki_id = "oxt_wiki_id", Fld_oxt_page_id = "oxt_page_id", Fld_oxt_ttl = "oxt_ttl", Fld_oxt_ext_id = "oxt_ext_id"
	, Fld_oxt_width = "oxt_width", Fld_oxt_height = "oxt_height", Fld_oxt_time = "oxt_time"
	;
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS oimg_xfer_temp"
	,	"( oxt_id              integer             NOT NULL"
	,	", oxt_wiki_id         tinyint             NOT NULL"
	,	", oxt_page_id         tinyint             NOT NULL"
	,	", oxt_ttl             varchar(255)        NOT NULL"
	,	", oxt_ext_id          tinyint             NOT NULL"
	,	", oxt_width           integer             NOT NULL"
	,	", oxt_height          integer             NOT NULL"
	,	", oxt_time            double              NOT NULL"
	,	");"
	);
}
class Xodb_tbl_oimg_xfer_regy {
	public Xodb_tbl_oimg_xfer_regy Create_table(Db_provider p)		{Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public void Create_data(Gfo_usr_dlg usr_dlg, Db_provider p) {
		p.Exec_sql(Sql_create_data);
		Sqlite_engine_.Idx_create(usr_dlg, p, "oimg", Idx_select);
	}
	public void Create_indexes(Gfo_usr_dlg usr_dlg, Db_provider p)	{Sqlite_engine_.Idx_create(usr_dlg, p, Xob_dump_mgr_lnki_temp.Db_name, Idx_select);}
	public Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_oxr_id), Fld_oxr_done, Fld_oxr_status, Fld_oxr_status_msg);}
	public void Update(Db_stmt stmt, int id, boolean done, String status, String status_msg) {
		stmt.Clear()
		.Val_int_(id)
		.Val_byte_by_bool_(done)
		.Val_bry_by_str_(status)
		.Val_bry_by_str_(status_msg)
		.Exec_update();
	}
	public DataRdr Select(Db_provider p, byte wiki_id, byte[] ttl, int limit) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_all_()
			.Where_(gplx.criterias.Criteria_.And(Db_crt_.mt_(Fld_oxr_wiki_id, wiki_id), Db_crt_.mt_(Fld_oxr_ttl, String_.new_utf8_(ttl))))
			.OrderBy_many_(Fld_oxr_done, Fld_oxr_wiki_id, Fld_oxr_ttl, Fld_oxr_width)
			.Limit_(limit)
			;
		return p.Exec_qry_as_rdr(qry);
	}
	public static final String Tbl_name = "oimg_xfer_regy"
	, Fld_oxr_id = "oxr_id", Fld_oxr_wiki_id = "oxr_wiki_id", Fld_oxr_ttl = "oxr_ttl", Fld_oxr_ext_id = "oxr_ext_id"
	, Fld_oxr_width = "oxr_width", Fld_oxr_height = "oxr_height", Fld_oxr_time = "oxr_time"
	, Fld_oxr_done = "oxr_done", Fld_oxr_status = "oxr_status", Fld_oxr_status_msg = "oxr_status_msg";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS oimg_xfer_regy"
	,	"( oxr_id              integer             NOT NULL"
	,	", oxr_wiki_id         tinyint             NOT NULL"
	,	", oxr_page_id         int                 NOT NULL"
	,	", oxr_ttl             varchar(255)        NOT NULL"
	,	", oxr_ext_id          tinyint             NOT NULL"
	,	", oxr_width           integer             NOT NULL"
	,	", oxr_height          integer             NOT NULL"
	,	", oxr_time            double              NOT NULL"
	,	", oxr_done            tinyint             NOT NULL"
	,	", oxr_status          varchar(32)         NOT NULL"
	,	", oxr_status_msg      varchar(255)        NOT NULL"
	,	");"
	);
	private static final String Sql_create_data = String_.Concat_lines_nl
	( "INSERT INTO oimg_xfer_regy (oxr_id, oxr_wiki_id, oxr_page_id, oxr_ttl, oxr_ext_id, oxr_width, oxr_height, oxr_time, oxr_done, oxr_status, oxr_status_msg)"
	, "SELECT oxt_id, oxt_wiki_id, oxt_page_id, oxt_ttl, oxt_ext_id, oxt_width, oxt_height, oxt_time, 0, '', ''"
	, "FROM oimg_xfer_temp"
	, "GROUP BY oxt_wiki_id, oxt_page_id, oxt_ttl, oxt_ext_id, oxt_width, oxt_height, oxt_time"
	);
	private static final Db_idx_itm
		Idx_select	= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_xfer_regy__select        ON oimg_xfer_regy (oxr_done, oxr_wiki_id, oxr_ttl, oxr_width);")
	;
}

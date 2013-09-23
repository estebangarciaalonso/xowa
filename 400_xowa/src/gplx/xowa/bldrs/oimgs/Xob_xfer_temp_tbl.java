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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*;
class Xob_xfer_temp_tbl {
	public Xob_xfer_temp_tbl Create_table(Db_provider p)		{Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_oxt_id, Fld_oxt_wiki_id, Fld_oxt_page_id, Fld_oxt_ttl, Fld_oxt_ext_id, Fld_oxt_lnki_type, Fld_oxt_width, Fld_oxt_height, Fld_oxt_time);}
	public void Insert(Db_stmt stmt, int id, byte wiki_id, int page_id, String ttl, int ext_id, byte lnki_type, int width, int height, double time) {
		stmt.Clear()
		.Val_int_(id)
		.Val_byte_(wiki_id)
		.Val_int_(page_id)
		.Val_str_(ttl)
		.Val_int_(ext_id)
		.Val_byte_(lnki_type)
		.Val_int_(width)
		.Val_int_(height)
		.Val_double_(time)
		.Exec_insert();
	}
	public static final String Tbl_name = "oimg_xfer_temp"
	, Fld_oxt_id = "oxt_id", Fld_oxt_wiki_id = "oxt_wiki_id", Fld_oxt_page_id = "oxt_page_id", Fld_oxt_ttl = "oxt_ttl", Fld_oxt_ext_id = "oxt_ext_id"
	, Fld_oxt_lnki_type = "oxt_lnki_type", Fld_oxt_width = "oxt_width", Fld_oxt_height = "oxt_height", Fld_oxt_time = "oxt_time"
	;
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS oimg_xfer_temp"
	,	"( oxt_id              integer             NOT NULL"
	,	", oxt_wiki_id         tinyint             NOT NULL"
	,	", oxt_page_id         tinyint             NOT NULL"
	,	", oxt_ttl             varchar(255)        NOT NULL"
	,	", oxt_ext_id          tinyint             NOT NULL"
	,	", oxt_lnki_type       tinyint             NOT NULL"
	,	", oxt_width           integer             NOT NULL"
	,	", oxt_height          integer             NOT NULL"
	,	", oxt_time            double              NOT NULL"
	,	");"
	);
}

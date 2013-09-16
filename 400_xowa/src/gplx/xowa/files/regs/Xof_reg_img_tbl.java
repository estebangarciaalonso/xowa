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
package gplx.xowa.files.regs; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.dbs.*;
class Xof_reg_img_tbl {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(p, Idx_key);
	}
	public static void Insert(Db_provider p, int id, int w, int h, int thumbtime, byte status, long size, long viewed) {
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = Xof_reg_img_tbl.Insert_stmt(p);
			Insert(stmt, id, w, h, thumbtime, status, size, viewed);
		}	finally {stmt.Rls();}
	}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_ri_id, Fld_ri_html_w, Fld_ri_html_h, Fld_ri_thumbtime, Fld_ri_status, Fld_ri_size, Fld_ri_viewed);}
	public static void Insert(Db_stmt stmt, int id, int w, int h, int thumbtime, byte status, long size, long viewed) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(w)
		.Val_int_(h)
		.Val_int_(thumbtime)
		.Val_byte_(status)
		.Val_long_(size)
		.Val_long_(viewed)
		.Exec_insert();
	}	
	public static final String Tbl_name = "reg_img"
	, Fld_ri_id = "ri_id", Fld_ri_status = "ri_status"
	, Fld_ri_html_w = "ri_html_w", Fld_ri_html_h = "ri_html_h", Fld_ri_thumbtime = "ri_thumbtime"
	, Fld_ri_size = "ri_size", Fld_ri_viewed = "ri_viewed"
	;
	static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS reg_img"
	,	"( ri_id             integer             NOT NULL"
	,	", ri_html_w         integer             NOT NULL"
	,	", ri_html_h         integer             NOT NULL"
	,	", ri_thumbtime      integer             NOT NULL"
	,	", ri_status         tinyint             NOT NULL"	// status of thumb; 0=missing; 1=present
	,	", ri_size           bigint              NOT NULL"	// needed for caching
	,	", ri_viewed         bigint              NOT NULL"	// needed for caching
	,	");"
	);
	static final Db_idx_itm
	Idx_key     = Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS reg_img__page ON reg_img (ri_id, ri_html_w, ri_html_thumbtime);")
	;
}

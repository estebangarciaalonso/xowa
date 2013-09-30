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
import gplx.dbs.*;
class Xob_lnki_temp_tbl {
	public static void Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_olt_lnki_page_id, Fld_olt_lnki_ttl, Fld_olt_lnki_ext, Fld_olt_lnki_type, Fld_olt_lnki_w, Fld_olt_lnki_h, Fld_olt_lnki_upright, Fld_olt_lnki_thumbtime);}
	public static void Insert(Db_stmt stmt, int page_id, byte[] ttl, byte ext_id, byte img_type, int w, int h, double upright, int thumbtime) {
		stmt.Clear()
		.Val_int_(page_id)
		.Val_str_by_bry_(ttl)
		.Val_byte_(ext_id)
		.Val_byte_(img_type)
		.Val_int_(w)
		.Val_int_(h)
		.Val_double_(upright)
		.Val_double_(thumbtime)
		.Exec_insert();
	}
	public static final String Tbl_name = "oimg_lnki_temp"
	, Fld_olt_lnki_page_id = "olt_lnki_page_id", Fld_olt_lnki_ttl = "olt_lnki_ttl", Fld_olt_lnki_ext = "olt_lnki_ext", Fld_olt_lnki_type = "olt_lnki_type"
	, Fld_olt_lnki_w = "olt_lnki_w", Fld_olt_lnki_h = "olt_lnki_h", Fld_olt_lnki_upright = "olt_lnki_upright", Fld_olt_lnki_thumbtime = "olt_lnki_thumbtime";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(  "CREATE TABLE IF NOT EXISTS oimg_lnki_temp"	// NOTE: autoincrement ID column not needed as data will be aggregated
	,	"( olt_lnki_page_id    integer             NOT NULL"
	,	", olt_lnki_ttl        varchar(1024)       NOT NULL"
	,	", olt_lnki_ext        tinyint             NOT NULL"
	,	", olt_lnki_type       tinyint             NOT NULL"
	,	", olt_lnki_w          integer             NOT NULL"
	,	", olt_lnki_h          integer             NOT NULL"
	,	", olt_lnki_upright    double              NOT NULL"
	,	", olt_lnki_thumbtime  double              NOT NULL"
	,	");"
	);
}

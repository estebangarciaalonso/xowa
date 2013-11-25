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
package gplx.xowa.bldrs.files; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*;
class Xob_lnki_regy_tbl {
	public static void Create_table(Db_provider p) {Sqlite_engine_.Tbl_create_and_delete(p, Tbl_name, Tbl_sql);}
	public static void Create_data(Gfo_usr_dlg usr_dlg, Db_provider p) {
		p.Exec_sql(Sql_create_data);
		Sqlite_engine_.Idx_create(usr_dlg, p, "lnki_regy", Idx_ttl);
	}
	public static final String Tbl_name = "lnki_regy"
	, Fld_lnki_id = "lnki_id", Fld_lnki_page_id = "lnki_page_id", Fld_lnki_ttl = "lnki_ttl", Fld_lnki_ext = "lnki_ext", Fld_lnki_type = "lnki_type"
	, Fld_lnki_w = "lnki_w", Fld_lnki_h = "lnki_h", Fld_lnki_upright = "lnki_upright", Fld_lnki_thumbtime = "lnki_thumbtime", Fld_lnki_count = "lnki_count"
	;
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS lnki_regy"
	,	"( lnki_id         integer             NOT NULL			    PRIMARY KEY"
	,	", lnki_page_id    integer             NOT NULL"
	,	", lnki_ttl        varchar(255)        NOT NULL"
	,	", lnki_ext        integer             NOT NULL"
	,	", lnki_type       integer             NOT NULL"
	,	", lnki_w          integer             NOT NULL"
	,	", lnki_h          integer             NOT NULL"
	,	", lnki_upright    double              NOT NULL"
	,	", lnki_thumbtime  double              NOT NULL"
	,	", lnki_count      integer             NOT NULL"
	,	");"
	);
	private static final String Sql_create_data = String_.Concat_lines_nl
	(	"INSERT INTO lnki_regy (lnki_id, lnki_page_id, lnki_ttl, lnki_ext, lnki_type, lnki_w, lnki_h, lnki_upright, lnki_thumbtime, lnki_count)"
	,	"SELECT  Min(lnki_id)"
	,	",       Min(lnki_page_id)"
	,	",       lnki_ttl"
	,	",       lnki_ext"
	,	",       lnki_type"
	,	",       lnki_w"
	,	",       lnki_h"
	,	",       lnki_upright"
	,	",       lnki_thumbtime"
	,	",       Count(lnki_ttl)"
	,	"FROM    lnki_temp"
	,	"GROUP BY"
	,	"        lnki_ttl"
	,	",       lnki_ext"
	,	",       lnki_type"
	,	",       lnki_w"
	,	",       lnki_h"
	,	",       lnki_upright"
	,	",       lnki_thumbtime"
	,	";"
	);
	private static final Db_idx_itm 
		Idx_ttl     = Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS lnki_regy__ttl ON lnki_regy (lnki_ttl, lnki_ext, lnki_id, lnki_page_id);")
	;
}

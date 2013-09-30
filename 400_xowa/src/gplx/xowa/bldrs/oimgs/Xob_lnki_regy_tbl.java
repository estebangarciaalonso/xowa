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
class Xob_lnki_regy_tbl {
	public static void Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);}
	public static void Create_data(Gfo_usr_dlg usr_dlg, Db_provider p) {
		p.Exec_sql(Sql_create_data);
		Sqlite_engine_.Idx_create(usr_dlg, p, "oimg", Idx_ttl);
	}
	public static final String Tbl_name = "oimg_lnki_regy"
	, Fld_olr_lnki_id = "olr_lnki_id", Fld_olr_page_id = "olr_page_id", Fld_olr_lnki_ttl = "olr_lnki_ttl", Fld_olr_lnki_ext = "olr_lnki_ext", Fld_olr_lnki_type = "olr_lnki_type"
	, Fld_olr_lnki_w = "olr_lnki_w", Fld_olr_lnki_h = "olr_lnki_h", Fld_olr_lnki_upright = "olr_lnki_upright", Fld_olr_lnki_thumbtime = "olr_lnki_thumbtime", Fld_olr_lnki_count = "olr_lnki_count";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS oimg_lnki_regy"
	,	"( olr_lnki_id         integer             NOT NULL    PRIMARY KEY AUTOINCREMENT"
	,	", olr_lnki_ttl        varchar(255)        NOT NULL"
	,	", olr_lnki_ext        integer             NOT NULL"
	,	", olr_lnki_type       tinyint             NOT NULL"
	,	", olr_lnki_w          integer             NOT NULL"
	,	", olr_lnki_h          integer             NOT NULL"
	,	", olr_lnki_upright    double              NOT NULL"
	,	", olr_lnki_thumbtime  double              NOT NULL"
	,	", olr_lnki_count      integer             NOT NULL"
	,	");"
	);
	private static final String Sql_create_data = String_.Concat_lines_nl
	(	"INSERT INTO oimg_lnki_regy (olr_lnki_ttl, olr_lnki_ext, olr_lnki_type, olr_lnki_w, olr_lnki_h, olr_lnki_upright, olr_lnki_thumbtime, olr_lnki_count)"
	,	"SELECT  olt_lnki_ttl"
	,	",       olt_lnki_ext"
	,	",       olt_lnki_type"
	,	",       olt_lnki_w"
	,	",       olt_lnki_h"
	,	",       olt_lnki_upright"
	,	",       olt_lnki_thumbtime"
	,	",       Count(olt_lnki_ttl)"
	,	"FROM    oimg_lnki_temp"
	,	"GROUP BY "
	,	"        olt_lnki_ttl"
	,	",       olt_lnki_ext"
	,	",       olt_lnki_type"
	,	",       olt_lnki_w"
	,	",       olt_lnki_h"
	,	",       olt_lnki_upright"
	,	",       olt_lnki_thumbtime"
	,	";"
	);
	private static final Db_idx_itm 
		Idx_ttl     = Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_lnki_regy__ttl           ON oimg_lnki_regy (olr_lnki_ttl);")
	;
}

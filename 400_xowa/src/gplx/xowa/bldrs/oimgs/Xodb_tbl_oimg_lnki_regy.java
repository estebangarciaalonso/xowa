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
class Xodb_tbl_oimg_lnki_regy {
	public Xodb_tbl_oimg_lnki_regy Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public void Create_data(Gfo_usr_dlg usr_dlg, Db_provider p) {
		p.Exec_sql(Sql_create_data);
		Sqlite_engine_.Idx_create(usr_dlg, p, "oimg", Idx_ttl);
	}
	public static final String Tbl_name = "oimg_lnki_regy"
	, Fld_olr_id = "olr_id", Fld_olr_page_id = "olr_page_id", Fld_olr_ttl = "olr_ttl", Fld_olr_ext_id = "olr_ext_id", Fld_olr_type = "olr_type"
	, Fld_olr_width = "olr_width", Fld_olr_height = "olr_height", Fld_olr_upright = "olr_upright", Fld_olr_time = "olr_time", Fld_olr_count = "olr_count";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS oimg_lnki_regy"
	,	"( olr_id              integer             NOT NULL    PRIMARY KEY AUTOINCREMENT"
	,	", olr_ttl             varchar(255)        NOT NULL"
	,	", olr_ext_id          tinyint             NOT NULL"
	,	", olr_type            tinyint             NOT NULL    -- thumb,full"
	,	", olr_width           integer             NOT NULL"
	,	", olr_height          integer             NOT NULL"
	,	", olr_upright         double              NOT NULL"
	,	", olr_time            double              NOT NULL"
	,	", olr_count           integer             NOT NULL"
	,	");"
	);
	private static final String Sql_create_data = String_.Concat_lines_nl
	(	"INSERT INTO oimg_lnki_regy (olr_ttl, olr_ext_id, olr_type, olr_width, olr_height, olr_upright, olr_time, olr_count)"
	,	"SELECT  olt_title"
	,	",       olt_ext_id"
	,	",       olt_type"
	,	",       olt_width"
	,	",       olt_height"
	,	",       olt_upright"
	,	",       olt_time"
	,	",       Count(olt_title)"
	,	"FROM    oimg_lnki_temp"
	,	"GROUP BY "
	,	"        olt_title"
	,	",       olt_ext_id"
	,	",       olt_type"
	,	",       olt_width"
	,	",       olt_height"
	,	",       olt_upright"
	,	",       olt_time"
	,	";"
	);
	private static final Db_idx_itm 
		Idx_ttl     = Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_lnki_regy__ttl           ON oimg_lnki_regy (olr_ttl);")
	;
}

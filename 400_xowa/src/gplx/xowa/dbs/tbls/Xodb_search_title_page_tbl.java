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
package gplx.xowa.dbs.tbls; import gplx.*; import gplx.xowa.*; import gplx.xowa.dbs.*;
import gplx.dbs.*;
public class Xodb_search_title_page_tbl {
	public static void Create_table(Db_provider p)						{Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);}
	public static void Create_index(Gfo_usr_dlg usr_dlg, Db_provider p)	{Sqlite_engine_.Idx_create(usr_dlg, p, "search", Indexes_main);}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_stp_word_id, Fld_stp_page_id);}
	public static void Insert(Db_stmt stmt, int word_id, int page_id) {
		stmt.Clear()
		.Val_int_(word_id)
		.Val_int_(page_id)
		.Exec_insert();
	}	
	public static final String Tbl_name = "search_title_page", Fld_stp_word_id = "stp_word_id", Fld_stp_page_id = "stp_page_id";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS search_title_page"
	,	"( stp_word_id         integer             NOT NULL"
	,	", stp_page_id         integer             NOT NULL"
	,	");"
	);
	public static final Db_idx_itm
	Indexes_main						= Db_idx_itm.sql_("CREATE UNIQUE INDEX IF NOT EXISTS search_title_page__main       ON search_title_page (stp_word_id, stp_page_id);"); 
}

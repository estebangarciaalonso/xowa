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
class Xodb_tbl_redirect {
	public Xodb_tbl_redirect Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public void Create_indexes(Gfo_usr_dlg usr_dlg, Db_provider p) {
		Sqlite_engine_.Idx_create(usr_dlg, p, Xob_dump_mgr_redirect.Db_name, Idx_trg_id, Idx_trg_ttl);
	}
	public void Update_redirects(Db_provider p, Io_url core_url, int max_redirected_depth) {
		Sqlite_engine_.Db_attach(p, "join_db", core_url.Raw());	// link database with page table 
		p.Exec_sql(Sql_get_page_data);							// fill in page_id, page_ns, page_is_redirect for trg_ttl; EX: Page_A has "#REDIRECT Page_B"; Page_B is in redirect tbl; find its id, ttl, redirect status
		for (int i = 0; i < max_redirected_depth; i++) {		// loop to find redirected redirects; note that it is bounded by depth (to guard against circular redirects)
			int affected = p.Exec_sql(Sql_get_redirect_redirects);		// find redirects that are also redirects
			if (affected == 0) break;									// no more redirected redirects; stop
			p.Exec_sql(Sql_get_redirect_page_data);						// get page data for redirects
		}
		Sqlite_engine_.Db_detach(p, "join_db");
	}
	public Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_r_src_id, Fld_r_trg_id, Fld_r_trg_ns, Fld_r_trg_ttl, Fld_r_trg_anchor, Fld_r_trg_is_redirect, Fld_r_count);}
	public void Insert(Db_stmt stmt, int src_id, int trg_id, int trg_ns, byte[] trg_ttl, byte[] trg_anchor, int count) {
		stmt.Clear()
		.Val_int_(src_id)
		.Val_int_(trg_id)
		.Val_int_(trg_ns)
		.Val_str_by_bry_(trg_ttl)
		.Val_str_by_bry_(trg_anchor)
		.Val_byte_((byte)1)
		.Val_int_(count)
		.Exec_insert();
	}
	private static final String Tbl_name = "redirect", Fld_r_src_id = "r_src_id", Fld_r_trg_id = "r_trg_id", Fld_r_trg_ns = "r_trg_ns", Fld_r_trg_ttl = "r_trg_ttl", Fld_r_trg_anchor = "r_trg_anchor", Fld_r_trg_is_redirect = "r_trg_is_redirect", Fld_r_count = "r_count";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS redirect"
	,	"( r_src_id            integer             NOT NULL       PRIMARY KEY"
	,	", r_trg_id            integer             NOT NULL"
	,	", r_trg_ns            integer             NOT NULL"
	,	", r_trg_ttl           varchar(255)        NOT NULL"
	,	", r_trg_anchor        varchar(255)        NOT NULL"
	,	", r_trg_is_redirect   tinyint             NOT NULL"
	,	", r_count             integer             NOT NULL"
	,	");"
	);
	private static final Db_idx_itm 
		Idx_trg_ttl = Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS redirect__trg_ttl             ON redirect (trg_ttl);")
	,	Idx_trg_id  = Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS redirect__trg_id              ON redirect (trg_id);")
	;
	private static final String
		Sql_get_page_data = String_.Concat_lines_nl				// get data from page table for initial redirect dump 
		(	"REPLACE INTO redirect "
		,	"SELECT  t.r_src_id"
		,	",       j.page_id"
		,	",       t.r_trg_ns"
		,	",       t.r_trg_ttl"
		,	",       t.r_trg_anchor"
		,	",       j.page_is_redirect"
		,	",       t.r_count"
		,	"FROM    redirect t"
		,	"        JOIN join_db.page j "
		,	"          ON  t.r_trg_ns = j.page_namespace"
		,	"          AND t.r_trg_ttl = j.page_title"
		,	"          AND t.r_trg_is_redirect = 1  -- limit to redirects"
		,	";"
		)
	,	Sql_get_redirect_redirects = String_.Concat_lines_nl	// find redirects that are redirected
		(	"REPLACE INTO redirect"
		,	"SELECT  t.r_src_id"
		,	",       j.r_trg_id"
		,	",       -1"
		,	",       ''"
		,	",       ''"
		,	",       1"
		,	",       t.r_count + 1"
		,	"FROM    redirect t"
		,	"        JOIN redirect j "
		,	"          ON  t.r_trg_id = j.r_src_id"
		,	"          AND t.r_trg_is_redirect = 1"
		,	";"
		,	""
		)
	,	Sql_get_redirect_page_data = String_.Concat_lines_nl	// get data from page table for redirected redirects
		(	"REPLACE INTO redirect"
		,	"SELECT  t.r_src_id"
		,	",       t.r_trg_id"
		,	",       j.page_namespace"
		,	",       j.page_title"
		,	",       t.r_trg_anchor"
		,	",       j.page_is_redirect"
		,	",       t.r_count"
		,	"FROM    redirect t"
		,	"        JOIN join_db.page j "
		,	"          ON  t.r_trg_id = j.page_id "
		,	"          AND t.r_trg_is_redirect = 1  -- limit to redirects"
		,	";"
		);
}

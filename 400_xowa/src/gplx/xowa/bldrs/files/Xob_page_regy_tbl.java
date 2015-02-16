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
import gplx.dbs.*; import gplx.dbs.engines.sqlite.*; import gplx.xowa.dbs.*; import gplx.xowa.bldrs.oimgs.*;
class Xob_page_regy_tbl {
	public static void Reset_table(Db_conn p) {Sqlite_engine_.Tbl_create_and_delete(p, Tbl_name, Tbl_sql);}
	public static void Create_data(Gfo_usr_dlg usr_dlg, Db_conn p, byte repo_tid, Xow_wiki wiki) {
		Create_data__insert_page(usr_dlg, p, repo_tid, wiki.Db_mgr_as_sql().Fsys_mgr().Get_url(Xodb_file_tid.Tid_core));
		Create_data__insert_redirect(usr_dlg, p, repo_tid, wiki.Fsys_mgr().Root_dir().GenSubFil(Xodb_db_file.Name__wiki_redirect));
	}
	public static void Delete_local(Db_conn p) {
		p.Exec_sql("DELETE FROM page_regy WHERE repo_id = " + Xof_repo_itm.Repo_local);
	}
	private static void Create_data__insert_page(Gfo_usr_dlg usr_dlg, Db_conn cur, byte repo_tid, Io_url join) {
		usr_dlg.Note_many("", "", "inserting page: ~{0}", join.NameOnly());
		Sqlite_engine_.Db_attach(cur, "page_db", join.Raw());
		cur.Exec_sql(String_.Format(Sql_create_page, repo_tid));
		Sqlite_engine_.Db_detach(cur, "page_db");
	}
	private static void Create_data__insert_redirect(Gfo_usr_dlg usr_dlg, Db_conn cur, byte repo_tid, Io_url join) {
		usr_dlg.Note_many("", "", "inserting redirect: ~{0}", join.OwnerDir().NameOnly());
		Sqlite_engine_.Db_attach(cur, "redirect_db", join.Raw());
		cur.Exec_sql(String_.Format(Sql_create_redirect, repo_tid));
		Sqlite_engine_.Db_detach(cur, "redirect_db");
	}
	public static final String Tbl_name = "page_regy"
	, Fld_uid = "uid", Fld_repo_id = "repo_id", Fld_itm_tid = "itm_tid"
	, Fld_src_id = "src_id", Fld_src_ttl = "src_ttl"
	, Fld_trg_id = "trg_id", Fld_trg_ttl = "trg_ttl"
	;
	public static final Db_idx_itm
		Idx_main     		= Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS page_regy__main           ON page_regy (repo_id, itm_tid, src_ttl, src_id, trg_id, trg_ttl);")
	;
	private static final String
		Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS page_regy"
	,	"( uid                     integer             NOT NULL			PRIMARY KEY           AUTOINCREMENT"	// NOTE: must be PRIMARY KEY, else later REPLACE INTO will create dupe rows
	,	", repo_id                 integer             NOT NULL"
	,	", itm_tid                 tinyint             NOT NULL"
	,	", src_id                  integer             NOT NULL"
	,	", src_ttl                 varchar(255)        NOT NULL"
	,	", trg_id                  integer             NOT NULL"
	,	", trg_ttl                 varchar(255)        NOT NULL"
	,	");"
	)
	,	Sql_create_page = String_.Concat_lines_nl
	(	"INSERT INTO page_regy (repo_id, itm_tid, src_id, src_ttl, trg_id, trg_ttl)"
	,	"SELECT  {0}"
	,	",       0"		// 0=page
	,	",       p.page_id"
	,	",       p.page_title"
	,	",       -1"
	,	",       ''"
	,	"FROM    page_db.page p"
	,	";"
	)
	,	Sql_create_redirect = String_.Concat_lines_nl
	(	"INSERT INTO page_regy (repo_id, itm_tid, src_id, src_ttl, trg_id, trg_ttl)"
	,	"SELECT  {0}"
	,	",       1"		// 1=redirect
	,	",       r.src_id"
	,	",       r.src_ttl"
	,	",       r.trg_id"
	,	",       r.trg_ttl"
	,	"FROM    redirect_db.redirect r"
	,	";"
	)
	;
}

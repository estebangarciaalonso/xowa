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
class Xob_orig_regy_tbl {
	public static void Create_table(Db_provider p) {Sqlite_engine_.Tbl_create_and_delete(p, Tbl_name, Tbl_sql);}
	public static void Create_data(Gfo_usr_dlg usr_dlg, Db_provider p, Xow_wiki cur_wiki, Xow_wiki commons_wiki) {
		p.Exec_sql(Sql_create_data);
		Sqlite_engine_.Idx_create(usr_dlg, p, "oimg", Idx_ttl, Idx_page_id);
		Io_url wiki_dir = cur_wiki.Fsys_mgr().Root_dir();
		Io_url commons_dir = commons_wiki.Fsys_mgr().Root_dir();
		Create_data__update_page(usr_dlg, p, Byte_.int_(Xof_repo_itm.Repo_remote), commons_wiki.Db_mgr_as_sql().Fsys_mgr().Get_url(Xodb_file.Tid_core));
		Create_data__update_page(usr_dlg, p, Byte_.int_(Xof_repo_itm.Repo_local), cur_wiki.Db_mgr_as_sql().Fsys_mgr().Get_url(Xodb_file.Tid_core));
		Create_data__update_redirect(usr_dlg, p, Byte_.int_(Xof_repo_itm.Repo_remote), commons_dir.GenSubFil("oimg_redirect.sqlite3"));
		Create_data__update_redirect(usr_dlg, p, Byte_.int_(Xof_repo_itm.Repo_local), wiki_dir.GenSubFil("oimg_redirect.sqlite3"));
		p.Exec_sql(Sql_update_join_flds);
		Sqlite_engine_.Idx_create(usr_dlg, p, "oimg", Idx_wiki_join_id, Idx_wiki_join_ttl, Idx_join_ttl);
		Create_data__update_image(usr_dlg, p, Byte_.int_(Xof_repo_itm.Repo_remote), commons_dir.GenSubFil("oimg_image.sqlite3"));
		Create_data__update_image(usr_dlg, p, Byte_.int_(Xof_repo_itm.Repo_local), wiki_dir.GenSubFil("oimg_image.sqlite3"));
	}
	private static void Create_data__update_page(Gfo_usr_dlg usr_dlg, Db_provider cur, byte wiki_tid, Io_url join) {
		usr_dlg.Prog_many("", "", "creating temp_page: ~{0}", join.NameOnly());
		Sqlite_engine_.Db_attach(cur, "join_db", join.Raw());
		cur.Exec_sql(String_.Format(Sql_update_page_fmt, wiki_tid));
		Sqlite_engine_.Db_detach(cur, "join_db");
	}
	private static void Create_data__update_redirect(Gfo_usr_dlg usr_dlg, Db_provider cur, byte wiki_tid, Io_url join) {
		usr_dlg.Prog_many("", "", "creating temp_redirect: ~{0}", join.NameOnly());
		Sqlite_engine_.Db_attach(cur, "join_db", join.Raw());
		cur.Exec_sql(String_.Format(Sql_update_redirect_fmt, wiki_tid));
		Sqlite_engine_.Db_detach(cur, "join_db");
	}
	private static void Create_data__update_image(Gfo_usr_dlg usr_dlg, Db_provider cur, byte wiki_tid, Io_url join) {
		usr_dlg.Prog_many("", "", "creating temp_image: ~{0}", join.NameOnly());
		Sqlite_engine_.Db_attach(cur, "join_db", join.Raw());
		cur.Exec_sql(String_.Format(Sql_update_image_fmt, wiki_tid));
		Sqlite_engine_.Db_detach(cur, "join_db");
	}
	public static final String Tbl_name = "oimg_orig_regy"
	, Fld_oor_lnki_id = "oor_lnki_id", Fld_oor_lnki_ttl = "oor_lnki_ttl", Fld_oor_lnki_ext = "oor_lnki_ext", Fld_oor_lnki_count = "oor_lnki_count"
	, Fld_oor_orig_repo = "oor_orig_repo", Fld_oor_orig_page_id = "oor_orig_page_id", Fld_oor_orig_redirect_id = "oor_orig_redirect_id", Fld_oor_orig_redirect_ttl = "oor_orig_redirect_ttl"
	, Fld_oor_orig_join_id = "oor_orig_join_id", Fld_oor_orig_join_ttl = "oor_orig_join_ttl"
	, Fld_oor_orig_size = "oor_orig_size", Fld_oor_orig_w = "oor_orig_w", Fld_oor_orig_h = "oor_orig_h", Fld_oor_orig_bits = "oor_orig_bits", Fld_oor_orig_media_type = "oor_orig_media_type";
	private static final Db_idx_itm
		Idx_ttl     		= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_orig_regy__ttl           ON oimg_orig_regy (oor_lnki_ttl);")
	,   Idx_page_id 		= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_orig_regy__page_id       ON oimg_orig_regy (oor_orig_page_id);")
	,   Idx_wiki_join_id 	= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_orig_regy__wiki_join_id  ON oimg_orig_regy (oor_orig_repo, oor_orig_join_id);")
	,   Idx_wiki_join_ttl 	= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_orig_regy__wiki_join_ttl ON oimg_orig_regy (oor_orig_repo, oor_orig_join_ttl);")	    
	,   Idx_join_ttl 		= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_orig_regy__join_ttl      ON oimg_orig_regy (oor_orig_join_ttl);")	    
	;
	private static final String
		Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS oimg_orig_regy"
	,	"( oor_lnki_id             integer             NOT NULL    PRIMARY KEY"
	,	", oor_lnki_ttl            varchar(256)        NOT NULL"
	,	", oor_lnki_ext            integer             NOT NULL"
	,	", oor_lnki_count          integer             NOT NULL"
	,	", oor_orig_repo           tinyint             NULL"
	,	", oor_orig_page_id        integer             NULL"
	,	", oor_orig_redirect_id    integer             NULL"
	,	", oor_orig_redirect_ttl   varchar(256)        NULL"
	,	", oor_orig_join_id        integer             NULL"
	,	", oor_orig_join_ttl       varchar(256)        NULL"
	,	", oor_orig_size           integer             NULL"
	,	", oor_orig_w              integer             NULL"
	,	", oor_orig_h              integer             NULL"
	,	", oor_orig_bits           smallint            NULL"
	,	", oor_orig_media_type     varchar(64)         NULL"
	,	");"
	)
	,	Sql_create_data = String_.Concat_lines_nl
	(	"INSERT INTO oimg_orig_regy (oor_lnki_id, oor_lnki_ttl, oor_lnki_ext, oor_lnki_count)"
	,	"SELECT  Min(olr_lnki_id)"
	,	",       olr_lnki_ttl"
	,	",       olr_lnki_ext"
	,	",       Count(olr_lnki_id)"
	,	"FROM    oimg_lnki_regy"
	,	"GROUP BY olr_lnki_ttl"
	,	",       olr_lnki_ext"
	,	";"
	)
	,	Sql_update_page_fmt = String_.Concat_lines_nl
	(	"REPLACE INTO oimg_orig_regy"
	,	"SELECT  o.oor_lnki_id"
	,	",       o.oor_lnki_ttl"
	,	",       o.oor_lnki_ext"
	,	",       o.oor_lnki_count"
	,	",       {0}"
	,	",       p.page_id"
	,	",       NULL"
	,	",       NULL"
	,	",       NULL"
	,	",       NULL"
	,	",       NULL"
	,	",       NULL"
	,	",       NULL"
	,	",       NULL"
	,	",       NULL"
	,	"FROM    oimg_orig_regy o"
	,	"        JOIN join_db.page p ON p.page_namespace = 6 AND p.page_title = o.oor_lnki_ttl"
	,	"WHERE   oor_orig_page_id IS NULL"
	,	"; "
	)
	,	Sql_update_redirect_fmt = String_.Concat_lines_nl
	(	"REPLACE INTO oimg_orig_regy"
	,	"SELECT  t.oor_lnki_id"
	,	",       t.oor_lnki_ttl"
	,	",       t.oor_lnki_ext"
	,	",       t.oor_lnki_count"
	,	",       t.oor_orig_repo"
	,	",       t.oor_orig_page_id"
	,	",       j.r_trg_id"
	,	",       j.r_trg_ttl"
	,	",       t.oor_orig_join_id"
	,	",       t.oor_orig_join_ttl"
	,	",       t.oor_orig_size"
	,	",       t.oor_orig_w"
	,	",       t.oor_orig_h"
	,	",       t.oor_orig_bits"
	,	",       t.oor_orig_media_type"
	,	"FROM    oimg_orig_regy t"
	,	"        JOIN join_db.redirect j"
	,	"          ON  t.oor_orig_repo = {0}"
	,	"          AND t.oor_orig_page_id = j.r_src_id"
	,	"; "
	)
	,	Sql_update_join_flds = String_.Concat_lines_nl
	(	"UPDATE  oimg_orig_regy"
	,	"SET     oor_orig_join_id  = Coalesce(oor_orig_redirect_id , oor_orig_page_id)"
	,	",       oor_orig_join_ttl = Coalesce(oor_orig_redirect_ttl, oor_lnki_ttl)"
	,	";"
	)
	,	Sql_update_image_fmt = String_.Concat_lines_nl
	(	"REPLACE INTO oimg_orig_regy"
	,	"SELECT  t.oor_lnki_id"
	,	",       t.oor_lnki_ttl"
	,	",       t.oor_lnki_ext"
	,	",       t.oor_lnki_count"
	,	",       t.oor_orig_repo"
	,	",       t.oor_orig_page_id"
	,	",       t.oor_orig_redirect_id"
	,	",       t.oor_orig_redirect_ttl"
	,	",       t.oor_orig_join_id"
	,	",       t.oor_orig_join_ttl"
	,	",       j.img_size"
	,	",       j.img_width"
	,	",       j.img_height"
	,	",       j.img_bits"
	,	",       j.img_media_type"
	,	"FROM    oimg_orig_regy t"
	,	"        JOIN join_db.image j"
	,	"          ON  t.oor_orig_repo = {0}"
	,	"          AND t.oor_orig_join_ttl = j.img_name"
	,	"; "
	)
	;
}

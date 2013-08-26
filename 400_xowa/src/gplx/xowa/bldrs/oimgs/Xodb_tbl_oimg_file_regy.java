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
class Xodb_tbl_oimg_file_regy {
	public Xodb_tbl_oimg_file_regy Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public void Create_data(Gfo_usr_dlg usr_dlg, Db_provider p, Xow_wiki cur_wiki, Xow_wiki commons_wiki) {
		p.Exec_sql(Sql_create_data);
		Sqlite_engine_.Idx_create(usr_dlg, p, "oimg", Idx_ttl);
		Io_url wiki_dir = cur_wiki.Fsys_mgr().Root_dir();
		Io_url commons_dir = commons_wiki.Fsys_mgr().Root_dir();
		Create_data__update_page(p, Byte_.int_(0), cur_wiki.Db_mgr_as_sql().Fsys_mgr().Get_url(Xodb_file.Tid_core));
		Create_data__update_page(p, Byte_.int_(1), commons_wiki.Db_mgr_as_sql().Fsys_mgr().Get_url(Xodb_file.Tid_core));
		Sqlite_engine_.Idx_create(usr_dlg, p, "oimg", Idx_page_id);
		Create_data__update_redirect(p, Byte_.int_(0), wiki_dir.GenSubFil("oimg_redirect.sqlite3"));
		Create_data__update_redirect(p, Byte_.int_(1), commons_dir.GenSubFil("oimg_redirect.sqlite3"));
		p.Exec_sql(Sql_update_join_flds);
		Sqlite_engine_.Idx_create(usr_dlg, p, "oimg", Idx_wiki_join_id, Idx_wiki_join_ttl, Idx_join_ttl);
		Create_data__update_image(p, Byte_.int_(0), wiki_dir.GenSubFil("oimg_image.sqlite3"));
		Create_data__update_image(p, Byte_.int_(1), commons_dir.GenSubFil("oimg_image.sqlite3"));
	}
	private void Create_data__update_page(Db_provider cur, byte wiki_tid, Io_url join) {
		Sqlite_engine_.Db_attach(cur, "join_db", join.Raw());
		cur.Exec_sql(String_.Format(Sql_update_page_fmt, wiki_tid));
		Sqlite_engine_.Db_detach(cur, "join_db");
	}
	private void Create_data__update_redirect(Db_provider cur, byte wiki_tid, Io_url join) {
		Sqlite_engine_.Db_attach(cur, "join_db", join.Raw());
		cur.Exec_sql(String_.Format(Sql_update_redirect_fmt, wiki_tid));
		Sqlite_engine_.Db_detach(cur, "join_db");
	}
	private void Create_data__update_image(Db_provider cur, byte wiki_tid, Io_url join) {
		Sqlite_engine_.Db_attach(cur, "join_db", join.Raw());
		cur.Exec_sql(String_.Format(Sql_update_image_fmt, wiki_tid));
		Sqlite_engine_.Db_detach(cur, "join_db");
	}
	public static final String Tbl_name = "oimg_file_regy", Fld_ofr_id = "ofr_id", Fld_ofr_page_id = "ofr_page_id", Fld_ofr_ttl = "ofr_ttl", Fld_ofr_ext_id = "ofr_ext_id", Fld_ofr_type = "ofr_type"
	, Fld_ofr_width = "ofr_width", Fld_ofr_height = "ofr_height", Fld_ofr_upright = "ofr_upright", Fld_ofr_time = "ofr_time", Fld_ofr_count = "ofr_count";
	private static final Db_idx_itm
		Idx_ttl     		= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_file_regy__ttl           ON oimg_file_regy (ofr_ttl);")
	,   Idx_page_id 		= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_file_regy__page_id       ON oimg_file_regy (ofr_page_id);")	    
	,   Idx_wiki_join_id 	= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_file_regy__wiki_join_id  ON oimg_file_regy (ofr_wiki_id, ofr_join_id);")
	,   Idx_wiki_join_ttl 	= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_file_regy__wiki_join_ttl ON oimg_file_regy (ofr_wiki_id, ofr_join_ttl);")	    
	,   Idx_join_ttl 		= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_file_regy__join_ttl      ON oimg_file_regy (ofr_join_ttl);")	    
	;
	private static final String
		Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS oimg_file_regy"
	,	"( ofr_id              integer             NOT NULL    PRIMARY KEY"
	,	", ofr_ttl             varchar(256)        NOT NULL"
	,	", ofr_ext_id          tinyint             NOT NULL"
	,	", ofr_count           integer             NOT NULL"
	,	", ofr_wiki_id         tinyint             NULL"
	,	", ofr_page_id         integer             NULL"
	,	", ofr_redirect_id     integer             NULL"
	,	", ofr_redirect_ttl    varchar(256)        NULL"
	,	", ofr_join_id         integer             NULL"
	,	", ofr_join_ttl        varchar(256)        NULL"
	,	", ofr_size            integer             NULL"
	,	", ofr_width           integer             NULL"
	,	", ofr_height          integer             NULL"
	,	", ofr_bits            smallint            NULL"
	,	");"
	)
	,	Sql_create_data = String_.Concat_lines_nl
	(	"INSERT INTO oimg_file_regy (ofr_id, ofr_ttl, ofr_ext_id, ofr_count)"
	,	"SELECT  Min(olr_id)"
	,	",       olr_ttl"
	,	",       olr_ext_id"
	,	",       Count(olr_id)"
	,	"FROM    oimg_lnki_regy"
	,	"GROUP BY olr_ttl"
	,	",       olr_ext_id"
	,	";"
	)
	,	Sql_update_page_fmt = String_.Concat_lines_nl
	(	"REPLACE INTO oimg_file_regy"
	,	"SELECT  o.ofr_id"
	,	",       o.ofr_ttl"
	,	",       o.ofr_ext_id"
	,	",       o.ofr_count"
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
	,	"FROM    oimg_file_regy o"
	,	"        JOIN join_db.page p ON p.page_namespace = 6 AND p.page_title = o.ofr_ttl"
	,	"WHERE   ofr_page_id IS NULL"
	,	"; "
	)
	,	Sql_update_redirect_fmt = String_.Concat_lines_nl
	(	"REPLACE INTO oimg_file_regy"
	,	"SELECT  t.ofr_id"
	,	",       t.ofr_ttl"
	,	",       t.ofr_ext_id"
	,	",       t.ofr_count"
	,	",       t.ofr_wiki_id"
	,	",       t.ofr_page_id"
	,	",       j.r_trg_id"
	,	",       j.r_trg_ttl"
	,	",       t.ofr_join_id"
	,	",       t.ofr_join_ttl"
	,	",       t.ofr_size"
	,	",       t.ofr_width"
	,	",       t.ofr_height"
	,	",       t.ofr_bits"
	,	"FROM    oimg_file_regy t"
	,	"        JOIN join_db.redirect j"
	,	"          ON  t.ofr_wiki_id = {0}"
	,	"          AND t.ofr_page_id = j.r_src_id"
	,	"; "
	)
	,	Sql_update_join_flds = String_.Concat_lines_nl
	(	"UPDATE  oimg_file_regy"
	,	"SET     ofr_join_id  = Coalesce(ofr_redirect_id , ofr_page_id)"
	,	",       ofr_join_ttl = Coalesce(ofr_redirect_ttl, ofr_ttl)"
	,	";"
	)
	,	Sql_update_image_fmt = String_.Concat_lines_nl
	(	"REPLACE INTO oimg_file_regy"
	,	"SELECT  t.ofr_id"
	,	",       t.ofr_ttl"
	,	",       t.ofr_ext_id"
	,	",       t.ofr_count"
	,	",       t.ofr_wiki_id"
	,	",       t.ofr_page_id"
	,	",       t.ofr_redirect_id"
	,	",       t.ofr_redirect_ttl"
	,	",       t.ofr_join_id"
	,	",       t.ofr_join_ttl"
	,	",       j.img_size"
	,	",       j.img_width"
	,	",       j.img_height"
	,	",       j.img_bits"
	,	"FROM    oimg_file_regy t"
	,	"        JOIN join_db.image j"
	,	"          ON  t.ofr_wiki_id = {0}"
	,	"          AND t.ofr_join_ttl = j.img_name"
	,	"; "
	)
	;
}

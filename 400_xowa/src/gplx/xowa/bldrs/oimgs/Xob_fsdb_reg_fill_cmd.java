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
import gplx.dbs.*; import gplx.xowa.bldrs.*; import gplx.xowa.files.fsdb.*; import gplx.xowa.files.qrys.*;
public class Xob_fsdb_reg_fill_cmd extends Xob_itm_basic_base implements Xob_cmd {
	public Xob_fsdb_reg_fill_cmd(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_ctor(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.fsdb_reg_fill";
	private Db_provider wiki_reg_provider;
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		wiki.Init_assert();
		wiki_reg_provider = Xof_fsdb_mgr_sql.Init_img_regy_provider(wiki.App().Fsys_mgr().File_dir().GenSubDir(wiki.Domain_str())); 
		Io_url oimg_lnki_url = Xodb_db_file.init__oimg_lnki(wiki).Url();
		Sqlite_engine_.Db_attach(wiki_reg_provider, "oimg_lnki", oimg_lnki_url.Raw());
	}
	public void Cmd_run() {Exec();}
	public void Cmd_end() {}
	public void Cmd_print() {}
	private void Exec() {
//			gplx.xowa.files.main.orig.Xof_orig_fil_tbl.Create_table(wiki_reg_provider);
		wiki_reg_provider.Exec_sql(Sql_create_xfer_direct);
		wiki_reg_provider.Exec_sql(Sql_create_xfer_redirect);
		wiki_reg_provider.Exec_sql(Sql_create_orig_direct);
		wiki_reg_provider.Exec_sql(Sql_create_orig_redirect);
	}
	private static final String 
	Sql_create_xfer_direct = String_.Concat_lines_nl
	(	"INSERT INTO file_orig "
	,	"(fo_ttl, fo_status, fo_orig_repo, fo_orig_ext, fo_orig_w, fo_orig_h, fo_orig_redirect)"
	,	"SELECT DISTINCT"
	,	"    oxr_xfer_ttl"
	,	",   1 --pass"
	,	",   oxr_xfer_repo"
	,	",   oxr_xfer_ext"
	,	",   oxr_orig_w"
	,	",   oxr_orig_h"
	,	",   ''"
	,	"FROM    oimg_lnki.oimg_xfer_regy xfer"
	,	"        LEFT JOIN file_orig cur ON xfer.oxr_xfer_ttl = cur.fo_ttl"
	,	"WHERE   cur.fo_ttl IS NULL"
	)
	, Sql_create_xfer_redirect = String_.Concat_lines_nl
	(	"INSERT INTO file_orig "
	,	"(fo_ttl, fo_status, fo_orig_repo, fo_orig_ext, fo_orig_w, fo_orig_h, fo_orig_redirect)"
	,	"SELECT DISTINCT"
	,	"    oxr_xfer_redirect_src"
	,	",   1 --pass"
	,	",   oxr_xfer_repo"
	,	",   oxr_xfer_ext"
	,	",   oxr_orig_w"
	,	",   oxr_orig_h"
	,	",   oxr_xfer_ttl"
	,	"FROM    oimg_lnki.oimg_xfer_regy xfer"
	,	"        LEFT JOIN file_orig cur ON xfer.oxr_xfer_redirect_src = cur.fo_ttl"
	,	"WHERE   cur.fo_ttl IS NULL"
	,	"AND     Coalesce(oxr_xfer_redirect_src, '') != ''"
	) 
	, Sql_create_orig_direct = String_.Concat_lines_nl
	( "INSERT INTO file_orig "
	, "(fo_ttl, fo_status, fo_orig_repo, fo_orig_ext, fo_orig_w, fo_orig_h, fo_orig_redirect)"
	, "SELECT DISTINCT"
	, "    oor_lnki_ttl"
	, ",   0 --unknown"
	, ",   oor_orig_repo"
	, ",   oor_lnki_ext"
	, ",   oor_orig_w"
	, ",   oor_orig_h"
	, ",   ''"
	, "FROM    oimg_lnki.oimg_orig_regy xfer"
	, "        LEFT JOIN file_orig cur ON xfer.oor_lnki_ttl = cur.fo_ttl"
	, "WHERE   cur.fo_ttl IS NULL"							// not already in file_orig
	, "AND     oor_orig_repo IS NOT NULL"					// not found in oimg_image.sqlite3
	, "AND     Coalesce(oor_orig_w, -1) != -1"				// ignore entries that are either ext_id = 0 ("File:1") or don't have any width / height info (makes it useless); need to try to get again from wmf_api
	, "AND     Coalesce(oor_orig_redirect_ttl, '') == ''"	// direct
	)
	, Sql_create_orig_redirect = String_.Concat_lines_nl
	( "INSERT INTO file_orig "
	, "(fo_ttl, fo_status, fo_orig_repo, fo_orig_ext, fo_orig_w, fo_orig_h, fo_orig_redirect)"
	, "SELECT DISTINCT"
	, "    oor_orig_redirect_ttl"
	, ",   0 --unknown"
	, ",   oor_orig_repo"
	, ",   oor_lnki_ext"
	, ",   oor_orig_w"
	, ",   oor_orig_h"
	, ",   ''"
	, "FROM    oimg_lnki.oimg_orig_regy xfer"
	, "        LEFT JOIN file_orig cur ON xfer.oor_orig_redirect_ttl = cur.fo_ttl"
	, "WHERE   cur.fo_ttl IS NULL"							// not already in file_orig
	, "AND     oor_orig_repo IS NOT NULL"					// not found in oimg_image.sqlite3
	, "AND     Coalesce(oor_orig_w, -1) != -1"				// ignore entries that are either ext_id = 0 ("File:1") or don't have any width / height info (makes it useless); need to try to get again from wmf_api
	, "AND     Coalesce(oor_orig_redirect_ttl, '') != ''"	// redirect
	)
	; 
}

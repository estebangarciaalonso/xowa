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
import gplx.dbs.*; import gplx.fsdb.*; import gplx.xowa.bldrs.oimgs.*;
public class Xob_xfer_regy_update_cmd extends Xob_itm_basic_base implements Xob_cmd {
	public Xob_xfer_regy_update_cmd(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_ctor(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "file.xfer_regy_update";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {}
	public void Cmd_run() {Exec_main();}
	public void Cmd_end() {}
	public void Cmd_print() {}
	private void Exec_main() {
		Db_provider make_db_provider = Xodb_db_file.init__file_make(wiki.Fsys_mgr().Root_dir()).Provider();
		this.Copy_atrs_to_make_db(make_db_provider);
		this.Update_status(make_db_provider);
	}
	private void Copy_atrs_to_make_db(Db_provider make_db_provider) {
		wiki.File_mgr().Fsdb_mgr().Init_by_wiki(wiki);
		Fsdb_db_abc_mgr fsdb_abc_mgr = wiki.File_mgr().Fsdb_mgr().Mnt_mgr().Abc_mgr_at(0);	// 0 = fsdb.main
		Io_url fsdb_atr_url = fsdb_abc_mgr.Atr_mgr().Get_at(0).Url();						// 0 = fsdb.atr.00
		Sqlite_engine_.Tbl_create_and_delete(make_db_provider, Xob_fsdb_regy_tbl.Tbl_name, Xob_fsdb_regy_tbl.Tbl_sql);
		Sqlite_engine_.Db_attach(make_db_provider, "fsdb_db", fsdb_atr_url.Raw());
		make_db_provider.Txn_mgr().Txn_bgn();
		make_db_provider.Exec_sql(Xob_fsdb_regy_tbl.Insert_fsdb_fil);
		make_db_provider.Exec_sql(Xob_fsdb_regy_tbl.Insert_fsdb_thm);
		make_db_provider.Txn_mgr().Txn_end();
		Sqlite_engine_.Idx_create(make_db_provider, Xob_fsdb_regy_tbl.Idx_main);
		Sqlite_engine_.Db_detach(make_db_provider, "fsdb_db");
	}
	private void Update_status(Db_provider make_db_provider) {
		make_db_provider.Txn_mgr().Txn_bgn();
		make_db_provider.Exec_sql(Xob_fsdb_regy_tbl.Update_regy_fil);
		make_db_provider.Exec_sql(Xob_fsdb_regy_tbl.Update_regy_thm);
		make_db_provider.Txn_mgr().Txn_end();
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		return this;
	}
}
class Xob_fsdb_regy_tbl {
	public static final String Tbl_name = "fsdb_regy";
	public static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE fsdb_regy "
	,	"( fil_name             varchar(255)        NOT NULL"
	,	", fil_is_orig          tinyint             NOT NULL"
	,	", fil_w                int                 NOT NULL"
	,	", fil_thumbtime        int                 NOT NULL"
	,	");"
	);
	public static final Db_idx_itm Idx_main = Db_idx_itm.sql_("CREATE INDEX fsdb_regy__main ON fsdb_regy (fil_name, fil_is_orig, fil_w, fil_thumbtime);");
	public static final String 
		Insert_fsdb_fil = String_.Concat_lines_nl
	(	"INSERT INTO fsdb_regy (fil_name, fil_is_orig, fil_w, fil_thumbtime)"
	,	"SELECT  fil_name"
	,	",       1"
	,	",       -1"
	,	",       -1"
	,	"FROM    fsdb_db.fsdb_fil"
	,	"WHERE   fil_bin_db_id != -1"
	,	";"
	)
	,	Insert_fsdb_thm = String_.Concat_lines_nl
	(	"INSERT INTO fsdb_regy (fil_name, fil_is_orig, fil_w, fil_thumbtime)"
	,	"SELECT  f.fil_name"
	,	",       0"
	,	",       t.thm_w"
	,	",       t.thm_thumbtime"
	,	"FROM    fsdb_db.fsdb_fil f"
	,	"        JOIN fsdb_db.fsdb_xtn_thm t ON f.fil_id = t.thm_owner_id"
	,	";"
	)
	,	Update_regy_fil = String_.Concat_lines_nl
	(	"REPLACE INTO xfer_regy "
	,	"( lnki_id, lnki_page_id, orig_page_id, orig_repo, lnki_ttl, orig_redirect_src, lnki_ext, orig_media_type"
	,	", file_is_orig, orig_w, orig_h, file_w, file_h, lnki_thumbtime, lnki_count"
	,	", xfer_status"
	,	")"
	,	"SELECT "
	,	"  lnki_id, lnki_page_id, orig_page_id, orig_repo, lnki_ttl, orig_redirect_src, lnki_ext, orig_media_type"
	,	", file_is_orig, orig_w, orig_h, file_w, file_h, lnki_thumbtime, lnki_count"
	,	", CASE WHEN f.fil_name IS NOT NULL THEN 1 ELSE 0 END"
	,	"FROM    xfer_regy x"
	,	"        LEFT JOIN fsdb_regy f ON x.lnki_ttl = f.fil_name"
	,	"WHERE   x.file_is_orig = 1"
	,	"AND     f.fil_is_orig  = 1"
	,	";"
	)
	,	Update_regy_thm = String_.Concat_lines_nl
	(	"REPLACE INTO xfer_regy "
	,	"( lnki_id, lnki_page_id, orig_page_id, orig_repo, lnki_ttl, orig_redirect_src, lnki_ext, orig_media_type"
	,	", file_is_orig, orig_w, orig_h, file_w, file_h, lnki_thumbtime, lnki_count"
	,	", xfer_status"
	,	")"
	,	"SELECT "
	,	"  lnki_id, lnki_page_id, orig_page_id, orig_repo, lnki_ttl, orig_redirect_src, lnki_ext, orig_media_type"
	,	", file_is_orig, orig_w, orig_h, file_w, file_h, lnki_thumbtime, lnki_count"
	,	", CASE WHEN f.fil_name IS NOT NULL THEN 1 ELSE 0 END"
	,	"FROM    xfer_regy x"
	,	"        LEFT JOIN fsdb_regy f ON x.lnki_ttl = f.fil_name AND x.file_w = f.fil_w AND x.lnki_thumbtime = f.fil_thumbtime"
	,	"WHERE   x.file_is_orig = 0"
	,	"AND     f.fil_is_orig  = 0"
	,	";"
	)
	;
}

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
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.files.*;
public class Xob_xfer_update_cmd extends Xob_itm_basic_base implements Xob_cmd {
	private Io_url prv_url;
	public Xob_xfer_update_cmd(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_ctor(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.xfer_update";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {}
	public void Cmd_run() {
		// init vars
		Xodb_db_file cur_file = Xodb_db_file.init__oimg_lnki(wiki);
		Db_provider provider = cur_file.Provider();
		if (prv_url == null) {
			prv_url = wiki.App().Fsys_mgr().File_dir().GenSubFil_nest(wiki.Domain_str(), "bldr", Xodb_db_file.Name__oimg_lnki + ".sqlite3");
		}

		// run sql
		Sqlite_engine_.Tbl_rename(provider, "oimg_xfer_regy", "oimg_xfer_regy_old");
		Xob_xfer_regy_tbl.Create_table(provider);
		Sqlite_engine_.Db_attach(provider, "old_db", prv_url.Raw());
		provider.Exec_sql(Sql_update);
		Sqlite_engine_.Db_detach(provider, "old_db");
		Sqlite_engine_.Tbl_delete(provider, "oimg_xfer_regy_old");
		Xob_xfer_regy_tbl.Create_index(usr_dlg, provider);

//			// rotate db
//			DateAdp wiki_date = wiki.Db_mgr().Dump_date_query();
//			Io_url archive_url = prv_url.GenNewNameOnly("oimg_lnki_" + wiki_date.XtoStr_fmt("yyyyMMdd"));
//			Io_mgr._.CopyFil(cur_file.Url(), archive_url, true);
//			Io_mgr._.CopyFil(cur_file.Url(), prv_url, true);
	}
	public void Cmd_end() {}
	public void Cmd_print() {}
	public static final String Sql_update = String_.Concat_lines_nl
	( "INSERT INTO oimg_xfer_regy"
        , "SELECT  cur.oxr_lnki_id"
	, ",       cur.oxr_orig_page_id"
	, ",       cur.oxr_xfer_repo"
	, ",       cur.oxr_xfer_ttl"
	, ",       cur.oxr_xfer_redirect_src"
	, ",       cur.oxr_xfer_ext"
	, ",       cur.oxr_orig_media_type"
	, ",       cur.oxr_file_is_orig"
	, ",       cur.oxr_orig_w"
	, ",       cur.oxr_orig_h"
	, ",       cur.oxr_file_w"
	, ",       cur.oxr_file_h"
	, ",       cur.oxr_xfer_thumbtime"
	, ",       cur.oxr_xfer_count"
	, ",       CASE"
	, "          WHEN old.oxr_xfer_ttl IS NULL THEN"	// not in old table; mark todo
	, "            " + Byte_.XtoStr(Xob_xfer_regy_tbl.Status_todo)
	, "          ELSE"									// in old table; mark processed
	, "            " + Byte_.XtoStr(Xob_xfer_regy_tbl.Status_ignore_processed)
	, "        END"
	, ",       cur.oxr_xfer_bin_tid"
	, ",       cur.oxr_xfer_bin_msg"
	, "FROM    oimg_xfer_regy_old cur"
	, "        LEFT JOIN old_db.oimg_xfer_regy old ON cur.oxr_xfer_ttl = old.oxr_xfer_ttl AND cur.oxr_file_w = old.oxr_file_w"
	);
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_previous_url_))				prv_url = m.ReadIoUrl("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_previous_url_ = "previous_url_";
}

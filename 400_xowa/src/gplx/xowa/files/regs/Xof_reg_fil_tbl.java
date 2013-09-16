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
package gplx.xowa.files.regs; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.dbs.*; import gplx.xowa.files.fsdb.*;
public class Xof_reg_fil_tbl {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(p, Idx_key);
	}
	public static void Select_list(Db_provider p, byte exec_tid, ListAdp itms, Xof_url_bldr url_bldr, Xow_repo_mgr repo_mgr) {
		Xof_reg_fil_tbl_in_wkr in_wkr = new Xof_reg_fil_tbl_in_wkr();
		in_wkr.Init(itms);
		in_wkr.Select_in(p, Cancelable_.Never, 0, itms.Count());
		Xof_reg_fil_tbl_evaluator.Rdr_done(exec_tid, itms, in_wkr.Itms_by_ttl(), url_bldr, repo_mgr);
	}
	public static boolean Select_itm_exists(Db_provider p, byte[] ttl) {
		Object o =  Db_qry_.select_val_(Tbl_name, Fld_rf_id, Db_crt_.eq_(Fld_rf_ttl, String_.new_utf8_(ttl))).ExecRdr_val(p);
		return o != null;
	}
	public static void Insert(Db_provider p, int id, byte[] ttl, byte status, byte orig_repo, byte[] orig_redirect, int orig_ext, int orig_w, int orig_h) {
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = Xof_reg_fil_tbl.Insert_stmt(p);
			Insert(stmt, id, ttl, status, orig_repo, orig_redirect, orig_ext, orig_w, orig_h);
		}	finally {stmt.Rls();}
	}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_rf_id, Fld_rf_ttl, Fld_rf_status, Fld_rf_orig_repo, Fld_rf_orig_ext, Fld_rf_orig_w, Fld_rf_orig_h, Fld_rf_orig_redirect);}
	public static void Insert(Db_stmt stmt, int id, byte[] ttl, byte status, byte orig_repo, byte[] orig_redirect, int orig_ext, int orig_w, int orig_h) {
		stmt.Clear()
		.Val_int_(id)
		.Val_str_by_bry_(ttl)
		.Val_byte_(status)
		.Val_byte_(orig_repo)
		.Val_int_(orig_ext)
		.Val_int_(orig_w)
		.Val_int_(orig_h)
		.Val_str_by_bry_(orig_redirect)
		.Exec_insert();
	}	
	public static final String Tbl_name = "reg_fil"
	, Fld_rf_id = "rf_id", Fld_rf_ttl = "rf_ttl", Fld_rf_status = "rf_status"
	, Fld_rf_orig_repo = "rf_orig_repo", Fld_rf_orig_ext = "rf_orig_ext", Fld_rf_orig_w = "rf_orig_w", Fld_rf_orig_h = "rf_orig_h", Fld_rf_orig_redirect = "rf_orig_redirect"
	;
	static final Db_idx_itm
	  Idx_key     = Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS reg_fil__page ON reg_fil (rf_ttl);")
	;
	static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS reg_fil"
	,	"( rf_id             integer             NOT NULL    PRIMARY KEY AUTOINCREMENT"
	,	", rf_ttl            varchar(1024)       NOT NULL"
	,	", rf_status         tinyint             NOT NULL"	// status of qry; 0=fail; 1=pass
	,	", rf_orig_repo      tinyint             NOT NULL"
	,	", rf_orig_ext       integer             NOT NULL"
	,	", rf_orig_w         integer             NOT NULL"
	,	", rf_orig_h         integer             NOT NULL"
	,	", rf_orig_redirect  varchar(1024)       NOT NULL"
//		,	", rf_thumbs         varchar(1024)       NOT NULL"	// format of "width@thumbtime;"; EX: "220@10;240@12;300"
	,	");"
	);
}

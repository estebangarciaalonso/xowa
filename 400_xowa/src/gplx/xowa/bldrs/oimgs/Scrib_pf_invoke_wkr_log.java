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
import gplx.dbs.*; import gplx.xowa.xtns.scribunto.*;
class Scrib_pf_invoke_wkr_log implements Scrib_pf_invoke_wkr, GfoInvkAble {
	private Db_provider provider; private Db_stmt stmt;
	private boolean reset_db = false;
	private int exec_count = 0;
	private int commit_interval = 100;
	private Hash_adp_bry exclude_mod_names = new Hash_adp_bry(true);
	public void Init_by_wiki(Xow_wiki wiki) {
		Xodb_db_file db_file = Xodb_db_file.init_(wiki, "log_scribunto");
		provider = db_file.Provider();
		if (db_file.Created()) {
			Xob_log_scribunto_temp_tbl.Create_table(provider);
			db_file.Created_clear();
		}
		if (reset_db)
			provider.Exec_qry(Db_qry_delete.new_all_(Xob_log_scribunto_temp_tbl.Tbl_name));
		Xob_log_scribunto_temp_tbl.Delete(provider);
		stmt = Xob_log_scribunto_temp_tbl.Insert_stmt(provider);
		provider.Txn_mgr().Txn_bgn_if_none();
		Scrib_pf_invoke.Invoke_wkr = this;
	}
	public boolean Eval_bgn(Xoa_page page, byte[] mod_name, byte[] fnc_name) {return !exclude_mod_names.Has(mod_name);}
	public void Eval_end(Xoa_page page, byte[] mod_name, byte[] fnc_name, long invoke_time_bgn) {
		int eval_time = (int)(Env_.TickCount() - invoke_time_bgn);
		Xob_log_scribunto_temp_tbl.Insert(stmt, page.Page_ttl().Rest_txt(), mod_name, fnc_name, eval_time);
		++exec_count;
		if ((exec_count % commit_interval) == 0) {
			provider.Txn_mgr().Txn_end_all_bgn_if_none();
		}
	}
	public void Term() {
		provider.Txn_mgr().Txn_end_all();
		provider.Rls();
	}
	private void Exclude_mod_names_add(String[] v) {
		int len = v.length;
		for (int i = 0; i < len; i++) {
			byte[] bry = ByteAry_.new_utf8_(v[i]);
			exclude_mod_names.Add_bry_bry(bry);
		}
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_exclude_mod_names_add))	Exclude_mod_names_add(m.ReadStrAry("v", "|"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_exclude_mod_names_add = "exclude_mod_names_add";
}
class Xob_log_scribunto_temp_tbl {
	public static void Create_table(Db_provider provider)		{Sqlite_engine_.Tbl_create(provider, Tbl_name, Tbl_sql);}
	public static void Delete(Db_provider provider) {provider.Exec_qry(Db_qry_delete.new_all_(Tbl_name));}
	public static Db_stmt Insert_stmt(Db_provider provider) {return Db_stmt_.new_insert_(provider, Tbl_name, Fld_lst_page_ttl, Fld_lst_mod_name, Fld_lst_fnc_name, Fld_lst_eval_time);}
	public static void Insert(Db_stmt stmt, byte[] page_ttl, byte[] mod_name, byte[] fnc_name, int eval_time) {
		stmt.Clear()
		.Val_str_by_bry_(page_ttl)
		.Val_str_by_bry_(mod_name)
		.Val_str_by_bry_(fnc_name)
		.Val_int_(eval_time)
		.Exec_insert();
	}
	public static final String Tbl_name = "log_scribunto_temp", Fld_lst_page_ttl = "lst_page_ttl", Fld_lst_mod_name = "lst_mod_name", Fld_lst_fnc_name = "lst_fnc_name", Fld_lst_eval_time = "lst_eval_time";
	private static final String Tbl_sql = String_.Concat_lines_nl
		(	"CREATE TABLE IF NOT EXISTS log_scribunto_temp"
		,	"( lst_id                  integer             NOT NULL    PRIMARY KEY AUTOINCREMENT"
		,	", lst_page_ttl            varchar(255)        NOT NULL"
		,	", lst_mod_name            varchar(255)        NOT NULL"
		,	", lst_fnc_name            varchar(255)        NOT NULL"
		,	", lst_eval_time           integer             NOT NULL"
		,	");"
		);
}

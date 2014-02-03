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
package gplx.xowa.parsers.logs; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import gplx.dbs.*; import gplx.xowa.bldrs.oimgs.*; import gplx.xowa.parsers.logs.*;
public class Xop_log_invoke_wkr implements GfoInvkAble {
	private Xop_log_mgr log_mgr;
	private Db_provider provider; private Db_stmt stmt;
	private boolean log_enabled = true;
	private Hash_adp_bry exclude_mod_names = new Hash_adp_bry(true);
	public Xop_log_invoke_wkr(Xop_log_mgr log_mgr, Db_provider provider) {
		this.log_mgr = log_mgr;
		this.provider = provider;
		if (log_enabled) {
			Xop_log_invoke_tbl.Create_table(provider);
			stmt = Xop_log_invoke_tbl.Insert_stmt(provider);
		}
	}
	public void Init_reset() {
		Xop_log_invoke_tbl.Delete(provider);
	}
	public boolean Eval_bgn(Xoa_page page, byte[] mod_name, byte[] fnc_name) {return !exclude_mod_names.Has(mod_name);}
	public void Eval_end(Xoa_page page, byte[] mod_name, byte[] fnc_name, long invoke_time_bgn) {
		if (log_enabled && stmt != null) {
			int eval_time = (int)(Env_.TickCount() - invoke_time_bgn);
			Xop_log_invoke_tbl.Insert(stmt, page.Ttl().Rest_txt(), mod_name, fnc_name, eval_time);
			log_mgr.Commit_chk();
		}
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
		else if	(ctx.Match(k, Invk_log_enabled_))			log_enabled = m.ReadYn("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_exclude_mod_names_add = "exclude_mod_names_add", Invk_log_enabled_ = "log_enabled_";
}
class Xop_log_invoke_tbl {
	public static void Create_table(Db_provider provider)		{Sqlite_engine_.Tbl_create(provider, Tbl_name, Tbl_sql);}
	public static void Delete(Db_provider provider) {provider.Exec_qry(Db_qry_delete.new_all_(Tbl_name));}
	public static Db_stmt Insert_stmt(Db_provider provider) {return Db_stmt_.new_insert_(provider, Tbl_name, Fld_invk_page_ttl, Fld_invk_mod_name, Fld_invk_fnc_name, Fld_invk_eval_time);}
	public static void Insert(Db_stmt stmt, byte[] page_ttl, byte[] mod_name, byte[] fnc_name, int eval_time) {
		stmt.Clear()
		.Val_str_by_bry_(page_ttl)
		.Val_str_by_bry_(mod_name)
		.Val_str_by_bry_(fnc_name)
		.Val_int_(eval_time)
		.Exec_insert();
	}
	public static final String Tbl_name = "log_invoke_temp", Fld_invk_page_ttl = "invk_page_ttl", Fld_invk_mod_name = "invk_mod_name", Fld_invk_fnc_name = "invk_fnc_name", Fld_invk_eval_time = "invk_eval_time";
	private static final String Tbl_sql = String_.Concat_lines_nl
		(	"CREATE TABLE IF NOT EXISTS log_invoke_temp"
		,	"( invk_id                  integer             NOT NULL    PRIMARY KEY AUTOINCREMENT"
		,	", invk_page_ttl            varchar(255)        NOT NULL"
		,	", invk_mod_name            varchar(255)        NOT NULL"
		,	", invk_fnc_name            varchar(255)        NOT NULL"
		,	", invk_eval_time           integer             NOT NULL"
		,	");"
		);
}

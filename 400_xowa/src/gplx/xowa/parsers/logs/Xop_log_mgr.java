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
import gplx.dbs.*; import gplx.xowa.bldrs.oimgs.*;
public class Xop_log_mgr implements GfoInvkAble {
	private Db_conn conn;
	private Xoa_app app; private Xop_log_basic_tbl log_tbl;
	private int exec_count = 0, commit_interval = 1000;
	public Xop_log_mgr(Xoa_app app) {this.app = app;}
	public Io_url Log_dir() {return log_dir;}
	public Xop_log_mgr Log_dir_(Io_url v) {
		log_dir = v;
//			if (conn != null) {	// COMMENTED: need to implement a conn.Renew()
//				conn.Rls(); // invalidate conn; note that during build other cmds will bind Conn which will place temp.log in /temp/ dir instead of /wiki/ dir; DATE:2014-04-16
//			}
		return this;
	}	private Io_url log_dir;
	private Db_conn Conn() {
		if (conn == null) {
			if (log_dir == null) log_dir = app.User().Fsys_mgr().App_temp_dir();
			Xodb_db_file db_file = Xodb_db_file.init__temp_log(log_dir);
			conn = db_file.Conn();
		}
		return conn;
	}
	public Xop_log_invoke_wkr Make_wkr_invoke() {return new Xop_log_invoke_wkr(this, this.Conn());}
	public Xop_log_property_wkr Make_wkr_property() {return new Xop_log_property_wkr(this, this.Conn());}
	public Xop_log_basic_wkr Make_wkr() {
		if (log_tbl == null) {
			log_tbl = new Xop_log_basic_tbl(this.Conn());
			conn.Txn_mgr().Txn_bgn_if_none();
		}
		return new Xop_log_basic_wkr(this, log_tbl);
	}
	public void Commit_chk() {
		++exec_count;
		if ((exec_count % commit_interval) == 0)
			conn.Txn_mgr().Txn_end_all_bgn_if_none();
	}
	public void Delete_all() {
		log_tbl.Delete();
	}
	public void Txn_bgn() {conn.Txn_mgr().Txn_bgn_if_none();}
	public void Txn_end() {conn.Txn_mgr().Txn_end_all();}
	public void Rls() {
		if (log_tbl != null)	log_tbl.Rls();
		if (conn != null)	{conn.Conn_term(); conn = null;}
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_commit_interval_))		commit_interval = m.ReadInt("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String Invk_commit_interval_ = "commit_interval_";
}

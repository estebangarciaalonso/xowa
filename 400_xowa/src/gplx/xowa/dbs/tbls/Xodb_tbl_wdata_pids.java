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
package gplx.xowa.dbs.tbls; import gplx.*; import gplx.xowa.*; import gplx.xowa.dbs.*;
import gplx.dbs.*; import gplx.xowa.xtns.wdatas.*;
public class Xodb_tbl_wdata_pids {
	public void Purge(Db_provider p) {p.Exec_qry(Db_qry_.delete_tbl_(Tbl_name));}
	public Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_wp_src_lang, Fld_wp_src_ttl, Fld_wp_trg_ttl);}
	public void Insert(Db_stmt stmt, byte[] src_lang, byte[] src_ttl, byte[] trg_ttl) {
		stmt.Clear().Val_str_by_bry_(src_lang).Val_str_by_bry_(src_ttl).Val_str_by_bry_(trg_ttl).Exec_insert();
	}
	public int Select_pid(Db_provider p, byte[] src_lang, byte[] src_ttl) {
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = Db_stmt_.new_select_(p, Tbl_name, String_.Ary(Fld_wp_src_lang, Fld_wp_src_ttl), Fld_wp_trg_ttl);
			byte[] pid = ByteAry_.new_utf8_((String)stmt.Val_str_by_bry_(src_lang).Val_str_by_bry_(src_ttl).Exec_select_val());				
			return pid == null ?  Wdata_wiki_mgr.Pid_null : ByteAry_.XtoIntByPos(pid, 1, pid.length, Wdata_wiki_mgr.Pid_null);
		} finally {stmt.Rls();}
	}
	public static final String Tbl_name = "wdata_pids", Fld_wp_src_lang = "wp_src_lang", Fld_wp_src_ttl = "wp_src_ttl", Fld_wp_trg_ttl = "wp_trg_ttl";
}

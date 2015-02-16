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
package gplx.xowa2.wikis.data; import gplx.*; import gplx.xowa2.*; import gplx.xowa2.wikis.*;
import gplx.dbs.*; import gplx.xowa.*;
public class Xowd_ns_regy_tbl {
	private String Tbl_name = "xowa_ns";
	private final Db_meta_fld_list Flds = Db_meta_fld_list.new_();
	private String Fld_db_id, Fld_ns_id, Fld_ns_name, Fld_ns_case, Fld_ns_count, Fld_ns_is_alias;
	private String[] where_cols;
	private boolean version_is_1;
	private Db_conn conn;
	public void Conn_(Db_conn new_conn, boolean version_is_1) {
		this.conn = new_conn; Flds.Clear();
		this.version_is_1 = version_is_1;
		if (version_is_1) {
			where_cols = String_.Ary(Fld_ns_id);
			Fld_db_id = Db_meta_fld.Key_null;
		}
		else {
			where_cols = String_.Ary(Fld_db_id, Fld_ns_id);
			Tbl_name			= "wiki_ns_regy";
			Fld_db_id			= Flds.Add_int("db_id");
		}
		Fld_ns_id				= Flds.Add_int("ns_id");
		Fld_ns_name				= Flds.Add_str("ns_name", 255);
		Fld_ns_case				= Flds.Add_byte("ns_case");
		Fld_ns_is_alias			= Flds.Add_bool("ns_is_alias");
		Fld_ns_count			= Flds.Add_int("ns_count");
	}
	public void Insert(int db_id, Xow_ns_mgr ns_mgr) {
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = conn.Stmt_insert(Tbl_name, Flds);
			int len = ns_mgr.Ids_len();
			for (int i = 0; i < len; i++) {
				Xow_ns ns = ns_mgr.Ids_get_at(i);
				stmt.Clear()
					.Val_int(Fld_db_id, db_id)
					.Val_int(Fld_ns_id, ns.Id())
					.Val_str(Fld_ns_name, ns.Name_str())
					.Val_byte(Fld_ns_case, ns.Case_match())
					.Val_bool_as_byte(Fld_ns_is_alias, ns.Is_alias())
					.Val_int(Fld_ns_count, ns.Count())
					.Exec_insert();
					;
			}
		} finally {stmt.Rls();}
	}
	public void Select_all(int db_id, Xow_ns_mgr ns_mgr) {
		Db_rdr rdr = Db_rdr_.Null; Db_stmt stmt = Db_stmt_.Null;
		try {
			String[] where = version_is_1 ? Db_meta_fld.Ary_empy : String_.Ary(Fld_db_id);
			stmt = conn.Stmt_select(Tbl_name, Flds, where);
			rdr = stmt.Clear()
				.Crt_int(Fld_db_id, db_id)
				.Exec_select_as_rdr()
				;
			ns_mgr.Clear();
			while (rdr.Move_next()) {
				int ns_id			= rdr.Read_int(Fld_ns_id);
				byte[] ns_name		= rdr.Read_bry_by_str(Fld_ns_name);
				byte ns_case_match	= rdr.Read_byte(Fld_ns_case);
				int ns_count		= rdr.Read_int(Fld_ns_count);
				boolean ns_is_alias	= rdr.Read_bool_by_byte(Fld_ns_is_alias);
				ns_mgr.Add_new(ns_id, ns_name, ns_case_match, ns_is_alias);
				if (ns_id < 0) continue;			// don't load counts for Special / Media					
				Xow_ns ns = ns_mgr.Ids_get_or_null(ns_id);
				ns.Count_(ns_count);
				if (ns_count > 0) ns.Exists_(true);	// ns has article; mark it as exists, else Talk tab won't show; DATE:2013-12-04
			}
			ns_mgr.Init();
		}	finally {rdr.Rls(); stmt.Rls();}
	}
	public int Select_ns_count(int db_id, int ns_id) {
		Db_stmt stmt = conn.Stmt_select(Tbl_name, where_cols, Fld_ns_count);
		Db_rdr rdr = Db_rdr_.Null;
		try {
			rdr = stmt.Crt_int(Fld_db_id, db_id).Crt_int(Fld_ns_id, ns_id).Exec_select_as_rdr();
			return rdr.Move_next() ? rdr.Read_int(Fld_ns_count) : 0;
		}
		finally {
			rdr.Rls();
		}
	}
	public void Update_ns_count(int db_id, int ns_id, int ns_count) {
		Db_stmt stmt = conn.Stmt_update(Tbl_name, where_cols, Fld_ns_count);
		stmt.Clear()
			.Crt_int(Fld_db_id, db_id)
			.Crt_int(Fld_ns_id, ns_id).Val_int(Fld_ns_count, ns_count)
			.Exec_update();
	}
//		private static final Db_meta_tbl meta = Db_meta_tbl.new_(Tbl_name, Flds
//		, Db_meta_idx.new_unique(Tbl_name, "pkey", Fld_db_id, Fld_ns_id)
//		);
}

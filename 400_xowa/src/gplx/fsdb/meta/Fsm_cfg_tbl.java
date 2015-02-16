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
package gplx.fsdb.meta; import gplx.*; import gplx.fsdb.*;
import gplx.dbs.*;
public class Fsm_cfg_tbl {
	private String Tbl_name = "file_meta_cfg";
	private String Fld_db_id, Fld_grp, Fld_key, Fld_val;
	private final Db_meta_fld_list Flds = Db_meta_fld_list.new_(); private Db_meta_tbl meta;
	private Db_conn conn; private int db_id;
	private Db_stmt stmt_insert, stmt_update, stmt_select;
	public Fsm_cfg_tbl Conn_(Db_conn new_conn, int db_id, boolean version_is_1, boolean created) {
		this.conn = new_conn; this.db_id = db_id; Flds.Clear();
		stmt_insert = stmt_update = stmt_select = null;			
		String fld_prefix = "";
		if (version_is_1) {
			Tbl_name		= "fsdb_cfg";
			fld_prefix		= "cfg_";
			Fld_db_id		= Db_meta_fld.Key_null;
		}
		else {
			Fld_db_id		= Flds.Add_int("db_id");
		}
		Fld_grp				= Flds.Add_str(fld_prefix + "grp", 255);
		Fld_key				= Flds.Add_str(fld_prefix + "key", 255);
		Fld_val				= Flds.Add_str(fld_prefix + "val", 1024);
		meta = Db_meta_tbl.new_(Tbl_name, Flds
		, Db_meta_idx.new_unique_by_tbl_wo_null(Tbl_name, "main", Fld_db_id, Fld_grp, Fld_key, Fld_val)
		);
		if (created) Create_table();
		return this;
	}
	public void Create_table() {conn.Exec_create_tbl(meta); conn.Exec_create_idx(Gfo_usr_dlg_._, meta.Idxs());}
	public void Insert(String grp, String key, String val) {
		if (stmt_insert == null) stmt_insert = conn.Rls_reg(conn.Stmt_insert(Tbl_name, Flds));
		stmt_insert.Clear().Val_int(Fld_db_id, db_id).Val_str(Fld_grp, grp).Val_str(Fld_key, key).Val_str(Fld_val, val).Exec_insert();
	}	
	public void Update(String grp, String key, String val) {
		if (stmt_update == null) stmt_update = conn.Rls_reg(conn.Stmt_update_exclude(Tbl_name, Flds,  String_.Ary_wo_null(Fld_db_id, Fld_grp, Fld_key)));
		stmt_update.Clear().Val_str(Fld_val, val).Crt_int(Fld_db_id, db_id).Crt_str(Fld_grp, grp).Crt_str(Fld_key, key).Exec_update();
	}
	public int Select_as_int_or_fail(String grp, String key) {
		int rv = Select_as_int_or(grp, key, Int_.MinValue);
		if (rv == Int_.MinValue) throw Err_.new_fmt_("fsdb_cfg did not have itm: grp={0} key={1}", grp, key);
		return rv;
	}
	public int Select_as_int_or(String grp, String key, int or) {return Int_.parse_or_(Select_as_str_or(grp, key, null), or);}
	public String Select_as_str_or(String grp, String key, String or) {
		if (stmt_select == null) stmt_select = conn.Rls_reg(conn.Stmt_select(Tbl_name, String_.Ary(Fld_val), String_.Ary_wo_null(Fld_db_id, Fld_grp, Fld_key)));
		Db_rdr rdr = Db_rdr_.Null;
		try {
			rdr = stmt_select.Clear()
				.Crt_int(Fld_db_id, db_id)
				.Crt_str(Fld_grp, grp)
				.Crt_str(Fld_key, key)
				.Exec_select_as_rdr();
			return rdr.Move_next() ? rdr.Read_str(Fld_val) : or;
		} finally {rdr.Rls();}
	}
	public Fsm_cfg_grp Select_as_grp(String grp) {
		Fsm_cfg_grp rv = null;
		Db_stmt stmt = conn.Stmt_select(Tbl_name, Flds, String_.Ary_wo_null(Fld_db_id, Fld_grp));
		Db_rdr rdr = Db_rdr_.Null;
		try {
			rdr = stmt.Clear().Crt_str(Fld_grp, grp).Exec_select_as_rdr();
			while (rdr.Move_next()) {
				if (rv == null) rv = new Fsm_cfg_grp(grp);
				rv.Upsert(rdr.Read_str(Fld_key), rdr.Read_str(Fld_val));
			}
		}
		finally {rdr.Rls();}
		return rv == null ? Fsm_cfg_grp.Null : rv;
	}
	public void Rls() {
		stmt_insert = Db_stmt_.Rls(stmt_insert);
		stmt_update = Db_stmt_.Rls(stmt_update);
		stmt_select = Db_stmt_.Rls(stmt_select);
	}
}

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
public class Fsm_mnt_tbl {
	private String Tbl_name = "file_meta_mnt";
	private String Fld_db_id, Fld_id, Fld_name, Fld_url;
	private final Db_meta_fld_list Flds = Db_meta_fld_list.new_(); private Db_meta_tbl meta;
	private Db_conn conn; private int db_id;
	public void Conn_(Db_conn new_conn, int db_id, boolean version_is_1) {
		this.conn = new_conn; this.db_id = db_id; Flds.Clear();
		String fld_prefix = "";
		if (version_is_1) {
			Tbl_name		= "fsdb_mnt";
			fld_prefix		= "mnt_";
			Fld_db_id		= Db_meta_fld.Key_null;
		}
		else {
			Fld_db_id		= Flds.Add_int("db_id");
		}
		Fld_id				= Flds.Add_int(fld_prefix + "id");
		Fld_name			= Flds.Add_str(fld_prefix + "name", 255);
		Fld_url				= Flds.Add_str(fld_prefix + "url", 255);
		meta = Db_meta_tbl.new_(Tbl_name, Flds
		, Db_meta_idx.new_unique_by_tbl_wo_null(Tbl_name, "pkey", Fld_db_id, Fld_id)
		);
	}
	public void Create_table() {conn.Exec_create_tbl(meta); conn.Exec_create_idx(Gfo_usr_dlg_._, meta.Idxs());}
	public void Insert(int id, String name, String url) {
		Db_stmt stmt = conn.Stmt_insert(Tbl_name, Flds);
		stmt.Clear().Val_int(Fld_db_id, db_id).Val_int(Fld_id, id).Val_str(Fld_name, name).Val_str(Fld_url, url).Exec_insert();
	}
	public void Update(int id, String name, String url) {
		Db_stmt stmt = conn.Stmt_update_exclude(Tbl_name, Flds, String_.Ary_wo_null(Fld_db_id, Fld_id));
		stmt.Clear().Val_str(Fld_name, name).Val_str(Fld_url, url).Crt_int(Fld_db_id, db_id).Crt_int(Fld_id, id).Exec_update();
	}	
	public Fsm_mnt_itm[] Select_all() {
		Db_stmt stmt = conn.Stmt_select(Tbl_name, Flds, String_.Ary_wo_null(Fld_db_id));
		Db_rdr rdr = Db_rdr_.Null;
		ListAdp list = ListAdp_.new_();
		try {
			rdr = stmt.Clear().Crt_int(Fld_db_id, db_id).Exec_select_as_rdr();
			while (rdr.Move_next()) {
				Fsm_mnt_itm itm = new Fsm_mnt_itm(rdr.Read_int(Fld_id), rdr.Read_str(Fld_name), rdr.Read_str(Fld_url));
				list.Add(itm);
			}
		}
		finally {rdr.Rls();}
		return (Fsm_mnt_itm[])list.Xto_ary_and_clear(Fsm_mnt_itm.class);
	}
}

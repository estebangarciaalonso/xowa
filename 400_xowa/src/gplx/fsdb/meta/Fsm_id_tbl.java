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
public class Fsm_id_tbl {
	private String Tbl_name = "db_regy";
	private String Fld_key, Fld_id, Fld_version;
	private final Db_meta_fld_list Flds = Db_meta_fld_list.new_(); private Db_meta_tbl meta;
	private Db_conn conn;
	public void Conn_(Db_conn new_conn, String tbl_name) {
		this.conn = new_conn; this.Tbl_name = tbl_name; Flds.Clear();
		Fld_key					= Flds.Add_str("key", 255);
		Fld_id					= Flds.Add_int("id");
		Fld_version				= Flds.Add_int("version");
		meta = Db_meta_tbl.new_(Tbl_name, Flds
		, Db_meta_idx.new_unique_by_tbl_wo_null(Tbl_name, "pkey", Fld_key)
		);
	}
	public void Create_table() {conn.Exec_create_tbl(meta); conn.Exec_create_idx(Gfo_usr_dlg_._, meta.Idxs());}
	public void Insert(String key, int id, int version) {
		Db_stmt stmt = conn.Stmt_insert(Tbl_name, Flds);
		stmt.Clear().Val_str(Fld_key, key).Val_int(Fld_id, id).Val_int(Fld_version, version).Exec_insert();
	}
	public Fsm_id_itm[] Select_all() {
		Db_stmt stmt = conn.Stmt_select(Tbl_name, Flds, Db_meta_fld.Ary_empy);
		Db_rdr rdr = Db_rdr_.Null;
		ListAdp list = ListAdp_.new_();
		try {
			rdr = stmt.Clear().Exec_select_as_rdr();
			while (rdr.Move_next()) {
				Fsm_id_itm itm = new Fsm_id_itm(rdr.Read_str(Fld_key), rdr.Read_int(Fld_id), rdr.Read_int(Fld_version));
				list.Add(itm);
			}
		}
		finally {rdr.Rls();}
		return (Fsm_id_itm[])list.Xto_ary_and_clear(Fsm_id_itm.class);
	}
}

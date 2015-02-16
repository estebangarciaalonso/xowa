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
package gplx.fsdb.data; import gplx.*; import gplx.fsdb.*;
import gplx.dbs.*; import gplx.dbs.qrys.*; import gplx.dbs.engines.sqlite.*;
public class Fsd_fil_tbl {
	private String Tbl_name = "file_data_fil";
	private String Fld_db_id, Fld_id, Fld_owner_id, Fld_name, Fld_xtn_id, Fld_ext_id, Fld_size, Fld_modified, Fld_hash, Fld_bin_db_id;
	private String Idx_owner;
	private final Db_meta_fld_list Flds = Db_meta_fld_list.new_(); private Db_meta_tbl meta;
	private Db_conn conn; private int db_id;
	private Db_stmt stmt_insert, stmt_update, stmt_select_by_name;		
	public void Conn_(Db_conn new_conn, int db_id, boolean version_is_1) {
		this.conn = new_conn; this.db_id = db_id; Flds.Clear();
		stmt_insert = stmt_update = stmt_select_by_name = null;			
		String fld_prefix = "";
		if (version_is_1) {
			Tbl_name			= "fsdb_fil";
			fld_prefix			= "fil_";
			Fld_db_id			= Db_meta_fld.Key_null;
		}
		else {
			Fld_db_id			= Flds.Add_int("db_id");
		}
		Fld_id					= Flds.Add_int(fld_prefix + "id");
		Fld_owner_id			= Flds.Add_int(fld_prefix + "owner_id");
		Fld_xtn_id				= Flds.Add_int(fld_prefix + "xtn_id");
		Fld_ext_id				= Flds.Add_int(fld_prefix + "ext_id");
		Fld_bin_db_id			= Flds.Add_int(fld_prefix + "bin_db_id");		// group ints at beginning of table
		Fld_name				= Flds.Add_str(fld_prefix + "name", 255);
		Fld_size				= Flds.Add_long(fld_prefix + "size");
		Fld_modified			= Flds.Add_str(fld_prefix + "modified", 14);	// stored as yyyyMMddHHmmss
		Fld_hash				= Flds.Add_str(fld_prefix + "hash", 40);
		Idx_owner				= Db_meta_idx.Bld_idx_name(Tbl_name, "owner");
		meta = Db_meta_tbl.new_(Tbl_name, Flds
		, Db_meta_idx.new_unique_by_tbl(Tbl_name, "pkey", String_.Ary_wo_null(Fld_db_id, Fld_id))
		, Db_meta_idx.new_unique_by_name(Tbl_name, Idx_owner, String_.Ary_wo_null(Fld_db_id, Fld_owner_id, Fld_name, Fld_id))
		);
	}
	public void Create_table() {conn.Exec_create_tbl(meta); conn.Exec_create_idx(Gfo_usr_dlg_._, meta.Idxs());}
	public void Insert(int id, int owner_id, String name, int xtn_id, int ext_id, long size, DateAdp modified, String hash, int bin_db_id) {
		if (stmt_insert == null) stmt_insert = conn.Rls_reg(conn.Stmt_insert(Tbl_name, Flds));
		stmt_insert.Clear()
		.Val_int(Fld_db_id, db_id)
		.Val_int(Fld_id, id)
		.Val_int(Fld_owner_id, owner_id)
		.Val_int(Fld_xtn_id, xtn_id)
		.Val_int(Fld_ext_id, ext_id)
		.Val_int(Fld_bin_db_id, bin_db_id)
		.Val_str(Fld_name, name)
		.Val_long(Fld_size, size)
		.Val_str(Fld_modified, Sqlite_engine_.X_date_to_str(modified))
		.Val_str(Fld_hash, hash)
		.Exec_insert();
	}	
	public void Update(int id, int owner_id, String name, int xtn_id, int ext_id, long size, DateAdp modified, String hash, int bin_db_id) {
		if (stmt_update == null) stmt_update = conn.Rls_reg(conn.Stmt_update_exclude(Tbl_name, Flds, String_.Ary_wo_null(Fld_db_id, Fld_id)));
		stmt_update.Clear()
		.Val_int(Fld_owner_id, owner_id)
		.Val_int(Fld_xtn_id, xtn_id)
		.Val_int(Fld_ext_id, ext_id)
		.Val_int(Fld_bin_db_id, bin_db_id)
		.Val_str(Fld_name, name)
		.Val_long(Fld_size, size)
		.Val_str(Fld_modified, Sqlite_engine_.X_date_to_str(modified))
		.Val_str(Fld_hash, hash)
		.Crt_int(Fld_db_id, db_id)
		.Crt_int(Fld_id, id)
		.Exec_update();
	}	
	public Fsd_fil_itm Select_itm_by_name(int dir_id, String fil_name) {
		if (stmt_select_by_name == null) {
			Db_qry_select qry = Db_qry_select.new_().From_(Tbl_name).Cols_(Flds.To_str_ary()).Where_(Db_crt_.eq_many_(String_.Ary_wo_null(Fld_db_id, Fld_owner_id, Fld_name))).Indexed_by_(Idx_owner);
			stmt_select_by_name = conn.Rls_reg(conn.Stmt_new(qry));
		}
		Db_rdr rdr = Db_rdr_.Null;
		try {
			rdr = stmt_select_by_name.Clear()
				.Crt_int(Fld_db_id, db_id)
				.Crt_int(Fld_owner_id, dir_id)
				.Crt_str(Fld_name, fil_name)
				.Exec_select_as_rdr();
			return rdr.Move_next()
				? new Fsd_fil_itm().Init(rdr.Read_int(Fld_id), rdr.Read_int(Fld_owner_id), rdr.Read_int(Fld_ext_id), rdr.Read_str(Fld_name), rdr.Read_int(Fld_bin_db_id))
				: Fsd_fil_itm.Null;
		}
		finally {rdr.Rls();}
	}
}

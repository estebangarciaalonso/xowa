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
package gplx.xowa.files.fsdb.fs_roots; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*; import gplx.xowa.files.fsdb.*;
import gplx.dbs.*;
public class Orig_fil_tbl {
	private String Tbl_name = "orig_fil";
	private String Fld_db_id, Fld_uid, Fld_name, Fld_ext_id, Fld_w, Fld_h, Fld_dir_url;
	private final Db_meta_fld_list Flds = Db_meta_fld_list.new_(); private Db_meta_tbl meta;
	private Db_conn conn; private int db_id;
	private Db_stmt stmt_insert, stmt_select;
	public Orig_fil_tbl Conn_(Db_conn new_conn, int db_id, boolean version_is_1, boolean created) {
		this.conn = new_conn; this.db_id = db_id; Flds.Clear();
		stmt_insert = stmt_select = null;			
		String fld_prefix = "";
		if (version_is_1) {
			fld_prefix		= "fil_";
			Fld_db_id		= Db_meta_fld.Key_null;
		}
		else {
			Fld_db_id		= Flds.Add_int("db_id");
		}
		Fld_uid				= Flds.Add_int(fld_prefix + "uid");
		Fld_name			= Flds.Add_str(fld_prefix + "name", 1024);
		Fld_ext_id			= Flds.Add_int(fld_prefix + "ext_id");
		Fld_w				= Flds.Add_int(fld_prefix + "w");
		Fld_h				= Flds.Add_int(fld_prefix + "h");
		Fld_dir_url			= Flds.Add_str(fld_prefix + "dir_url", 1024);	// NOTE: don't put dir in separate table; note that entire root_dir_wkr is not built to scale due to need for recursively loading all files
		meta = Db_meta_tbl.new_(Tbl_name, Flds
		, Db_meta_idx.new_unique_by_tbl_wo_null(Tbl_name, "main", Fld_db_id, Fld_name)
		);
		if (created) Create_table();
		return this;
	}
	public void Create_table() {conn.Exec_create_tbl(meta); conn.Exec_create_idx(Gfo_usr_dlg_._, meta.Idxs());}
	public Orig_fil_itm Select_itm(byte[] ttl) {
		if (stmt_select == null) stmt_select = conn.Rls_reg(conn.Stmt_select(Tbl_name, Flds, String_.Ary_wo_null(Fld_db_id, Fld_name)));
		Orig_fil_itm rv = Orig_fil_itm.Null;
		Db_rdr rdr = Db_rdr_.Null;
		try {
			rdr = stmt_select.Clear().Crt_int(Fld_db_id, db_id).Crt_bry_as_str(Fld_name, ttl).Exec_select_as_rdr();
			if (rdr.Move_next())
				rv = Load_itm(rdr);
			return rv;
		}
		finally {rdr.Rls();}
	}
	private Orig_fil_itm Load_itm(Db_rdr rdr) {
		return new Orig_fil_itm
		( rdr.Read_int(Fld_uid)
		, rdr.Read_bry_by_str(Fld_name)
		, rdr.Read_int(Fld_ext_id)
		, rdr.Read_int(Fld_w)
		, rdr.Read_int(Fld_h)
		, rdr.Read_bry_by_str(Fld_dir_url)
		);
	}
	public void Insert(Orig_fil_itm fil_itm) {
		if (stmt_insert == null) stmt_insert = conn.Rls_reg(conn.Stmt_insert(Tbl_name, Flds));
		stmt_insert.Clear()
		.Val_int(Fld_db_id, db_id)
		.Val_int(Fld_uid, fil_itm.Fil_uid())
		.Val_bry_as_str(Fld_name, fil_itm.Fil_name())
		.Val_int(Fld_ext_id, fil_itm.Fil_ext_id())
		.Val_int(Fld_w, fil_itm.Fil_w())
		.Val_int(Fld_h, fil_itm.Fil_h())
		.Val_bry_as_str(Fld_dir_url, fil_itm.Fil_dir_url())
		.Exec_insert();
	}	
	public void Rls() {
		stmt_select = Db_stmt_.Rls(stmt_select);
		stmt_insert = Db_stmt_.Rls(stmt_insert);
	}
}

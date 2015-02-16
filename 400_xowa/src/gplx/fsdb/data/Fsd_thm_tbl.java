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
import gplx.fsdb.meta.*;
public class Fsd_thm_tbl {
	private String Tbl_name = "file_data_thm";
	private String Fld_db_id, Fld_id, Fld_owner_id, Fld_w, Fld_h, Fld_time, Fld_page, Fld_bin_db_id, Fld_size, Fld_modified, Fld_hash, Fld_thumbtime;
	private final Db_meta_fld_list Flds = Db_meta_fld_list.new_(); private Db_meta_tbl meta;
	private Db_conn conn; private int db_id; private Fsm_atr_fil atr_fil;
	private Db_stmt stmt_insert, stmt_select_by_fil_w;
	public void Conn_(Db_conn new_conn, int db_id, boolean version_is_1, Fsm_atr_fil atr_fil) {
		this.conn = new_conn; this.db_id = db_id; Flds.Clear(); this.atr_fil = atr_fil; 
		stmt_insert = stmt_select_by_fil_w = null;
		schema_thm_page_init = true;
		String fld_prefix = "";
		if (version_is_1) {
			Tbl_name		= "fsdb_xtn_thm";
			fld_prefix		= "thm_";
			Fld_db_id		= Db_meta_fld.Key_null;
		}
		else {
			Fld_db_id		= Flds.Add_int("db_id");
		}
		Fld_id				= Flds.Add_int(fld_prefix + "id");
		Fld_owner_id		= Flds.Add_int(fld_prefix + "owner_id");
		Fld_w				= Flds.Add_int(fld_prefix + "w");
		Fld_h				= Flds.Add_int(fld_prefix + "h");
		Fld_time			= Flds.Add_double(fld_prefix + "time");
		Fld_page			= Flds.Add_int(fld_prefix + "page");
		Fld_bin_db_id		= Flds.Add_int(fld_prefix + "bin_db_id");
		Fld_size			= Flds.Add_long(fld_prefix + "size");
		Fld_modified		= Flds.Add_str(fld_prefix + "modified", 14);		// stored as yyyyMMddHHmmss
		Fld_hash			= Flds.Add_str(fld_prefix + "hash", 40);
		meta = Db_meta_tbl.new_(Tbl_name, Flds
		, Db_meta_idx.new_unique_by_tbl_wo_null(Tbl_name, "pkey", Fld_db_id, Fld_id)
		, Db_meta_idx.new_unique_by_tbl_wo_null(Tbl_name, "owner", Fld_db_id, Fld_owner_id, Fld_id, Fld_w, Fld_time, Fld_page)
		);
	}
	public void Create_table() {conn.Exec_create_tbl(meta); conn.Exec_create_idx(Gfo_usr_dlg_._, meta.Idxs());}
	private boolean Schema_thm_page() {
		if (schema_thm_page_init) {
			schema_thm_page = atr_fil.Abc_mgr().Cfg_mgr().Schema_thm_page();
			schema_thm_page_init = false;
			if (schema_thm_page) {
				Fld_thumbtime	= Db_meta_fld.Key_null;
			}
			else {
				Fld_time		= Db_meta_fld.Key_null;
				Fld_page		= Db_meta_fld.Key_null;
			}
		}
		return schema_thm_page;
	}	private boolean schema_thm_page, schema_thm_page_init = true;
	public void Insert(int id, int thm_owner_id, int width, int height, double thumbtime, int page, int bin_db_id, long size, DateAdp modified, String hash) {
		if (stmt_insert == null) {
			String tmp_fld_time = this.Schema_thm_page() ? Fld_time : Fld_thumbtime;
			stmt_insert = conn.Rls_reg(conn.Stmt_insert(Tbl_name, Fld_db_id, Fld_id, Fld_owner_id, Fld_w, Fld_h, tmp_fld_time, Fld_page, Fld_bin_db_id, Fld_size, Fld_modified, Fld_hash));
		}
		stmt_insert.Clear()
		.Val_int(Fld_db_id, db_id)
		.Val_int(Fld_id, id)
		.Val_int(Fld_owner_id, thm_owner_id)
		.Val_int(Fld_w, width)
		.Val_int(Fld_h, height);
		if (this.Schema_thm_page()) {
			stmt_insert.Val_double	(Fld_time, gplx.xowa.files.Xof_doc_thumb.Db_save_double(thumbtime));
			stmt_insert.Val_int		(Fld_page, gplx.xowa.files.Xof_doc_page.Db_save_int(page));
		}
		else
			stmt_insert.Val_int		(Fld_thumbtime, gplx.xowa.files.Xof_doc_thumb.Db_save_int(thumbtime));
		stmt_insert
		.Val_int(Fld_bin_db_id, bin_db_id)
		.Val_long(Fld_size, size)
		.Val_str(Fld_modified, Sqlite_engine_.X_date_to_str(modified))
		.Val_str(Fld_hash, hash)
		.Exec_insert();
	}
	private Db_stmt Select_by_fil_w_stmt() {
		Db_qry_select qry = Db_qry_.select_().From_(Tbl_name).Cols_all_();
		gplx.core.criterias.Criteria crt 
			= this.Schema_thm_page()
			? Db_crt_.eq_many_wo_null(Fld_db_id, Fld_owner_id, Fld_w, Fld_time, Fld_page)
			: Db_crt_.eq_many_wo_null(Fld_db_id, Fld_owner_id, Fld_w, Fld_thumbtime)
			;
		qry.Where_(crt);
		return conn.Stmt_new(qry);
	}
	public boolean Select_itm_by_fil_width(int owner_id, Fsd_thm_itm thm) {
		if (stmt_select_by_fil_w == null) stmt_select_by_fil_w = conn.Rls_reg(Select_by_fil_w_stmt());
		Db_rdr rdr = Db_rdr_.Null;
		try {
			stmt_select_by_fil_w.Clear()
				.Crt_int(Fld_db_id, db_id)
				.Crt_int(Fld_owner_id, owner_id)
				.Crt_int(Fld_w, thm.Width())
				;
			if (this.Schema_thm_page())  {
				stmt_select_by_fil_w.Crt_double(Fld_time, gplx.xowa.files.Xof_doc_thumb.Db_save_double(thm.Thumbtime()));
				stmt_select_by_fil_w.Crt_int(Fld_page, gplx.xowa.files.Xof_doc_page.Db_save_int(thm.Page()));
			}
			else {
				stmt_select_by_fil_w.Crt_int(Fld_time, gplx.xowa.files.Xof_doc_thumb.Db_save_int(thm.Thumbtime()));
			}
			rdr = stmt_select_by_fil_w.Exec_select_as_rdr();
			return rdr.Move_next()
				? Ctor_by_load(thm, rdr, this.Schema_thm_page())
				: false;
		}
		finally {rdr.Rls();}
	}
	private boolean Ctor_by_load(Fsd_thm_itm itm, Db_rdr rdr, boolean schema_thm_page) {
		int id = rdr.Read_int(Fld_id);
		int owner_id = rdr.Read_int(Fld_owner_id);
		int width = rdr.Read_int(Fld_w);
		int height = rdr.Read_int(Fld_h);
		long size = rdr.Read_long(Fld_size);
		String modified = rdr.Read_str(Fld_modified);
		String hash = rdr.Read_str(Fld_hash);
		int bin_db_id = rdr.Read_int(Fld_bin_db_id);
		double time = 0;
		int page = 0;
		if (schema_thm_page) {
			time = gplx.xowa.files.Xof_doc_thumb.Db_load_double(rdr, Fld_time);
			page = gplx.xowa.files.Xof_doc_page.Db_load_int(rdr, Fld_page);
		}
		else {
			time = gplx.xowa.files.Xof_doc_thumb.Db_load_int(rdr, Fld_thumbtime);
			page = gplx.xowa.files.Xof_doc_page.Null;
		}
		itm.Ctor(id, owner_id, width, time, page, height, size, modified, hash);
		itm.Db_bin_id_(bin_db_id);
		return true;
	}
	public static final DateAdp Modified_null = null;
	public static final String Hash_null = "";
}

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
import gplx.dbs.*; import gplx.dbs.qrys.*;
public class Fsm_atr_tbl {
	private String Tbl_name = "file_meta_atr";
	private String Fld_db_id, Fld_uid, Fld_url, Fld_path_bgn;
	private final Db_meta_fld_list Flds = Db_meta_fld_list.new_(); private Db_meta_tbl meta;
	private Db_conn conn; private int db_id;
	private Db_stmt_bldr stmt_bldr;
	public void Conn_(Db_conn new_conn, int db_id, boolean version_is_1) {
		this.conn = new_conn; this.db_id = db_id; Flds.Clear();
		if (version_is_1) {
			Tbl_name		= "fsdb_db_atr";
			Fld_db_id		= Db_meta_fld.Key_null;
		}
		else {
			Fld_db_id		= Flds.Add_int("db_id");
		}
		Fld_uid				= Flds.Add_int("uid");
		Fld_url				= Flds.Add_str("url", 255);
		Fld_path_bgn		= Flds.Add_str("path_bgn", 255);
		meta = Db_meta_tbl.new_(Tbl_name, Flds
		, Db_meta_idx.new_unique_by_tbl_wo_null(Tbl_name, "pkey", Fld_db_id, Fld_uid)
		);
		stmt_bldr = new Db_stmt_bldr(Tbl_name, String_.Ary_wo_null(Fld_db_id, Fld_uid), Fld_url, Fld_path_bgn);
	}
	public void Create_table() {conn.Exec_create_tbl(meta); conn.Exec_create_idx(Gfo_usr_dlg_._, meta.Idxs());}
	public Fsm_atr_fil[] Select_all(Fsm_abc_mgr abc_mgr, Io_url dir) {
		ListAdp rv = ListAdp_.new_();
		Db_rdr rdr = Db_rdr_.Null;
		try {
			rdr = conn.Stmt_select(Tbl_name, Flds, String_.Ary_wo_null(Fld_db_id)).Exec_select_as_rdr();
			while (rdr.Move_next()) {
				Io_url url = dir.GenSubFil(rdr.Read_str(Fld_url));
				Fsm_atr_fil itm = new Fsm_atr_fil(abc_mgr, url, false);
				itm.Ctor_by_load
				( rdr.Read_int(Fld_uid)
				, url
				, rdr.Read_str(Fld_path_bgn)
				, Db_cmd_mode.Tid_ignore
				);
				rv.Add(itm);
			}
		}
		finally {rdr.Rls();}
		return (Fsm_atr_fil[])rv.Xto_ary(Fsm_atr_fil.class);
	}
	public void Commit_all(Fsm_atr_fil[] ary) {
		stmt_bldr.Init(conn);
		try {
			int len = ary.length;
			for (int i = 0; i < len; i++)
				Commit_itm(ary[i]);
			stmt_bldr.Commit();
		}	finally {stmt_bldr.Rls();}
	}
	private void Commit_itm(Fsm_atr_fil itm) {
		Db_stmt stmt = stmt_bldr.Get(itm.Cmd_mode());
		switch (itm.Cmd_mode()) {
			case Db_cmd_mode.Tid_create:	stmt.Clear().Val_int(Fld_db_id, db_id).Val_int(Fld_uid, itm.Id())	.Val_str(Fld_url, itm.Url().NameAndExt()).Val_str(Fld_path_bgn, itm.Path_bgn()).Exec_insert(); break;
			case Db_cmd_mode.Tid_update:	stmt.Clear()														.Val_str(Fld_url, itm.Url().NameAndExt()).Val_str(Fld_path_bgn, itm.Path_bgn()).Crt_int(Fld_db_id, db_id).Crt_int(Fld_uid, itm.Id()).Exec_update(); break;
			case Db_cmd_mode.Tid_delete:	stmt.Clear().Crt_int(Fld_db_id, db_id).Crt_int(Fld_uid, itm.Id()).Exec_delete();	break;
			case Db_cmd_mode.Tid_ignore:	break;
			default:					throw Err_.unhandled(itm.Cmd_mode());
		}
		itm.Cmd_mode_(Db_cmd_mode.Tid_ignore);
	}
}

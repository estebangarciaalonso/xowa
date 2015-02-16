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
import gplx.dbs.*; import gplx.ios.*;
import gplx.dbs.engines.sqlite.*;
public class Fsd_bin_tbl {
	private String Tbl_name = "file_data_bin";
	private String Fld_db_id, Fld_owner_id, Fld_owner_tid, Fld_part_id, Fld_data_url, Fld_data;
	private final Db_meta_fld_list Flds = Db_meta_fld_list.new_(); private Db_meta_tbl meta;		
	private Db_conn conn; private int db_id;
	public void Conn_(Db_conn new_conn, int db_id, boolean version_is_1) {
		this.conn = new_conn; this.db_id = db_id; Flds.Clear();
		String fld_prefix = "";
		if (version_is_1) {
			Tbl_name		= "fsdb_bin";
			Fld_db_id		= Db_meta_fld.Key_null;
			fld_prefix		= "bin_";
		}
		else {
			Fld_db_id		= Flds.Add_int("db_id");
		}
		Fld_owner_id		= Flds.Add_int(fld_prefix + "owner_id");
		Fld_owner_tid		= Flds.Add_byte(fld_prefix + "owner_tid");
		Fld_part_id			= Flds.Add_int(fld_prefix + "part_id");
		Fld_data_url		= Flds.Add_str(fld_prefix + "data_url", 255);
		Fld_data			= Flds.Add_bry(fld_prefix + "data");	// mediumblob
		meta = Db_meta_tbl.new_(Tbl_name, Flds
		, Db_meta_idx.new_unique_by_tbl_wo_null(Tbl_name, "pkey", Fld_db_id, Fld_owner_id)
		);
	}
	public void Create_table() {Sqlite_engine_.Tbl_create(conn, Tbl_name, meta.To_sql_create());}
	public long Insert_rdr(int id, byte tid, long bin_len, Io_stream_rdr bin_rdr) {
		Db_stmt stmt = conn.Stmt_insert(Tbl_name, Flds);
		long rv = bin_len;
		stmt.Clear()
		.Val_int(Fld_db_id, db_id)
		.Val_int(Fld_owner_id, id)
		.Val_byte(Fld_owner_tid, tid)
		.Val_int(Fld_part_id, Null_part_id)
		.Val_str(Fld_data_url, Null_data_url)
		;
		byte[] bin_ary = Io_stream_rdr_.Load_all_as_bry(Bry_bfr.new_(), bin_rdr);
		stmt.Val_bry(bin_ary);
		rv = bin_ary.length;
		stmt.Exec_insert();
		return rv;
	}
	public Io_stream_rdr Select_as_rdr(Db_conn conn, int owner_id) {
		Db_rdr rdr = Db_rdr_.Null;
		try {
			Db_stmt stmt = conn.Stmt_select(Tbl_name, String_.Ary(Fld_data), String_.Ary_wo_null(Fld_db_id, Fld_owner_id));
			rdr = stmt.Clear().Crt_int(Fld_db_id, db_id).Crt_int(Fld_owner_id, owner_id).Exec_select_as_rdr();
			if (rdr.Move_next())
				return gplx.ios.Io_stream_rdr_.mem_(Read_bin_data(rdr));
			else
				return gplx.ios.Io_stream_rdr_.Null;
		}
		finally {rdr.Rls();}
	}
	public boolean Select_to_url(int owner_id, Io_url url, byte[] bin_bfr, int bin_flush_when) {
		Db_rdr rdr = Db_rdr_.Null;
		try {
			Db_stmt stmt = conn.Stmt_select(Tbl_name, String_.Ary(Fld_data), String_.Ary_wo_null(Fld_db_id, Fld_owner_id));
			rdr = stmt.Clear().Crt_int(Fld_db_id, db_id).Crt_int(Fld_owner_id, owner_id).Exec_select_as_rdr();
			if (rdr.Move_next()) {
				byte[] bry = Read_bin_data(rdr);
				Io_mgr._.SaveFilBry(url, bry);
				return true;
			}
			else
				return false;
		}
		finally {rdr.Rls();}
	}
//		public boolean Select_to_fsys__stream(DataRdr rdr, Io_url url, byte[] bin_bfr, int bin_flush_when) {
//			Io_stream_rdr db_stream = Io_stream_rdr_.Null;
//			IoStream fs_stream = IoStream_.Null;
//			try {
//				db_stream = rdr.ReadRdr(Fld_data); if (db_stream == Io_stream_rdr_.Null) return false;
//				fs_stream = Io_mgr._.OpenStreamWrite(url);
//				int pos = 0, flush_nxt = bin_flush_when;
//				while (true) {
//					int read = db_stream.Read(bin_bfr, pos, bin_bfr.length); if (read == Io_stream_rdr_.Read_done) break;
//					fs_stream.Write(bin_bfr, 0, read);
//					if (pos > flush_nxt) {
//						fs_stream.Flush();
//						flush_nxt += bin_flush_when;
//					}
//				}
//				fs_stream.Flush();
//				return true;
//			} finally {
//				db_stream.Rls();
//				fs_stream.Rls();
//			}
//		}
	private byte[] Read_bin_data(Db_rdr rdr) {
		byte[] rv = rdr.Read_bry(Fld_data);
		return rv == null ? Bry_.Empty : rv;	// NOTE: bug in v0.10.1 where .ogg would save as null; return Bry_.Empty instead, else java.io.ByteArrayInputStream would fail on null
	}
	public static final byte Owner_tid_fil = 1, Owner_tid_thm = 2;
	public static final int Null_db_bin_id = -1, Null_size = -1, Null_part_id = -1;
	public static final String Null_data_url = "";
}

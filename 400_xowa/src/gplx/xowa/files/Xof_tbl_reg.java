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
package gplx.xowa.files; import gplx.*; import gplx.xowa.*;
import gplx.dbs.*; import gplx.fsdb.*;
public class Xof_tbl_reg {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(p, Idx_key);
	}
	public static int Search(Db_provider p, OrderedHash itms, int itms_len) {
		int rv = 0;
		for (int i = 0; i < itms_len; i += Max_rows_per_rdr) {
			int keys_len = itms_len - i; if (keys_len > Max_rows_per_rdr) keys_len = Max_rows_per_rdr;
			Object[] keys = new Object[keys_len];
			for (int j = 0; j < keys_len; j++) {
				Xof_itm itm = (Xof_itm)itms.FetchAt(i + j);
				keys[j] = itm.Reg_key();
			}
			rv += Search_by_grp(p, itms, keys);
		}
		return rv;
	}
	private static int Search_by_grp(Db_provider p, OrderedHash itms, Object[] keys) {
		DataRdr rdr = DataRdr_.Null;
		Db_stmt stmt = Db_stmt_.Null;
		int rv = 0;
		try {
			stmt = Db_stmt_.new_select_in_(p, Tbl_name, Fld_reg_key, keys, Fld_reg_id, Fld_reg_key, Fld_reg_url, Fld_reg_status, Fld_db_mnt_id, Fld_db_itm_id);
			int keys_len = keys.length;
			for (int i = 0; i < keys_len; i++)
				stmt.Val_str_by_bry_((byte[])keys[i]);
			rdr = stmt.Exec_select();
			while (rdr.MoveNextPeer()) {
				byte[] itm_key = rdr.ReadBryByStr(Fld_reg_key);
				byte status = rdr.ReadByte(Fld_reg_status);
				Xof_itm itm = (Xof_itm)itms.Fetch(itm_key);
				itm.Reg_status_(status);
				switch (status) {
					case Xof_itm.Reg_status_present:	itm.Reg_url_(rdr.ReadStr(Fld_reg_url)); ++rv; break;// itm present; set url
					case Xof_itm.Reg_status_deleted:	break;												// itm deleted; deleted by cache cleaner or never created locally
					case Xof_itm.Reg_status_unknown:	break;												// itm unknown; not in fsdb and not in wmf; ignore; page will just show href
					default:							throw Err_.unhandled(status);
				}
			}
		}	finally {stmt.Rls(); rdr.Rls();}
		return rv;
	}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_reg_key, Fld_reg_url, Fld_reg_status, Fld_reg_size, Fld_reg_viewed, Fld_db_mnt_id, Fld_db_itm_id);}
	public static void Insert(Db_stmt stmt, String key, String url, byte status, long size, long viewed, int mnt_id, int itm_id) {
		stmt.Clear()
		.Val_str_(key)
		.Val_str_(url)
		.Val_byte_(status)
		.Val_long_(size)
		.Val_long_(viewed)
		.Val_int_(mnt_id)
		.Val_int_(itm_id)
		.Exec_insert();
	}	
	public void Update_viewed(Db_provider p, Object[] ids) {
		long now = DateAdp_.Now().Timestamp_unix();
		Db_qry qry = Db_qry_update.new_().Where_(Db_crt_.in_(Fld_reg_id, ids)).Arg_(Fld_reg_viewed, now).From_(Tbl_name);
		p.Exec_qry(qry);
	}
	public static final String Tbl_name = "img_regy", Fld_reg_id = "reg_id", Fld_reg_key = "reg_key", Fld_reg_url = "reg_url", Fld_reg_status = "reg_status", Fld_reg_size = "reg_size", Fld_reg_viewed = "reg_viewed", Fld_db_mnt_id = "db_mnt_id", Fld_db_itm_id = "db_itm_id";
	private static final Db_idx_itm
		Idx_key     = Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS img_regy__key                   ON img_regy (reg_key, reg_id, reg_url, reg_status, db_mnt_id, db_itm_id);")
	;
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS img_regy"
	,	"( reg_id            integer             NOT NULL    PRIMARY KEY AUTOINCREMENT"
	,	", reg_key           varchar(1024)       NOT NULL"
	,	", reg_url           varchar(1024)       NOT NULL"
	,	", reg_size          bigint              NOT NULL"   // for caching
	,	", reg_viewed        bigint              NOT NULL"   // for caching
	,	", reg_status        tinyint             NOT NULL"   // 1=exists_on_fs;2=deleted_from_fs (by cache_cleaner);3=missing (not found in fs_db or in wmf)
	,	", db_mnt_id         int                 NOT NULL"
	,	", db_itm_id         int                 NOT NULL"
	,	");"
	);
	private static final int Max_rows_per_rdr = 990;
}

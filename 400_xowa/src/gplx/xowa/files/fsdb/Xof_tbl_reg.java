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
package gplx.xowa.files.fsdb; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.dbs.*; import gplx.fsdb.*;
public class Xof_tbl_reg {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(p, Idx_key);
	}
	public static void Select(Db_provider p, OrderedHash itms, int itms_len) {
		for (int i = 0; i < itms_len; i += Max_rows_per_rdr) {
			int keys_len = itms_len - i; if (keys_len > Max_rows_per_rdr) keys_len = Max_rows_per_rdr;
			Object[] keys = new Object[keys_len];
			for (int j = 0; j < keys_len; j++) {
				Xof_fsdb_itm itm = (Xof_fsdb_itm)itms.FetchAt(i + j);
				itm.Rslt_reg_(Xof_reg_wkr_.Tid_missing_reg); // default to missing
				keys[j] = itm.Lnki_key();
			}
			Select_by_grp(p, itms, keys, keys_len);
		}
	}
	private static void Select_by_grp(Db_provider p, OrderedHash itms, Object[] keys, int keys_len) {
		DataRdr rdr = DataRdr_.Null;
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = Db_stmt_.new_select_in_(p, Tbl_name, Fld_reg_key, keys, Fld_reg_id, Fld_reg_key, Fld_reg_url, Fld_reg_status, Fld_reg_mnt_id, Fld_reg_fil_id, Fld_reg_xtn_id);
			for (int i = 0; i < keys_len; i++)
				stmt.Val_str_by_bry_((byte[])keys[i]);
			rdr = stmt.Exec_select();
			while (rdr.MoveNextPeer()) {
				byte[] itm_key = rdr.ReadBryByStr(Fld_reg_key);
				byte status = rdr.ReadByte(Fld_reg_status);
				Xof_fsdb_itm itm = (Xof_fsdb_itm)itms.Fetch(itm_key);
				itm.Rslt_reg_(status);
				switch (status) {
					case Xof_reg_wkr_.Tid_found_url:	itm.Html_url_rel_(rdr.ReadStr(Fld_reg_url)); break;	// itm present; set url
					case Xof_reg_wkr_.Tid_missing_qry:
					case Xof_reg_wkr_.Tid_missing_bin:
					case Xof_reg_wkr_.Tid_null:			break;											// itm unknown; not in fsdb and not in wmf; ignore; page will just show href
					default:							throw Err_.unhandled(status);
				}
			}
		}	finally {stmt.Rls(); rdr.Rls();}
	}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_reg_key, Fld_reg_url, Fld_reg_w, Fld_reg_h, Fld_reg_status, Fld_reg_size, Fld_reg_viewed, Fld_reg_mnt_id, Fld_reg_fil_id, Fld_reg_xtn_id);}
	public static void Insert(Db_stmt stmt, String key, String url, int w, int h, byte status, long size, long viewed, int mnt_id, int fil_id, int xtn_id) {
		stmt.Clear()
		.Val_str_(key)
		.Val_str_(url)
		.Val_int_(w)
		.Val_int_(h)
		.Val_byte_(status)
		.Val_long_(size)
		.Val_long_(viewed)
		.Val_int_(mnt_id)
		.Val_int_(fil_id)
		.Val_int_(xtn_id)
		.Exec_insert();
	}	
	public void Update_viewed(Db_provider p, Object[] ids) {
		long now = DateAdp_.Now().Timestamp_unix();
		Db_qry qry = Db_qry_update.new_().Where_(Db_crt_.in_(Fld_reg_id, ids)).Arg_(Fld_reg_viewed, now).From_(Tbl_name);
		p.Exec_qry(qry);
	}
	public static final String Tbl_name = "img_regy"
	, Fld_reg_id = "reg_id", Fld_reg_key = "reg_key", Fld_reg_w = "reg_w", Fld_reg_h = "reg_h", Fld_reg_url = "reg_url"
	, Fld_reg_status = "reg_status", Fld_reg_size = "reg_size", Fld_reg_viewed = "reg_viewed"
	, Fld_reg_mnt_id = "reg_mnt_id", Fld_reg_fil_id = "reg_fil_id", Fld_reg_xtn_id = "reg_xtn_id";
	private static final Db_idx_itm
	  Idx_key     = Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS img_regy__key                   ON img_regy (reg_key, reg_id, reg_w, reg_h, reg_url, reg_status);")
	;
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS img_regy"
	,	"( reg_id            integer             NOT NULL    PRIMARY KEY AUTOINCREMENT"
	,	", reg_key           varchar(1024)       NOT NULL"
	,	", reg_url           varchar(1024)       NOT NULL"	// needed for html
	,	", reg_w             integer             NOT NULL"	// needed for html
	,	", reg_h             integer             NOT NULL"	// needed for html
	,	", reg_size          bigint              NOT NULL"	// needed for caching
	,	", reg_viewed        bigint              NOT NULL"	// needed for caching
	,	", reg_status        tinyint             NOT NULL"	// 1=exists_on_fs;2=deleted_from_fs (by cache_cleaner);3=missing (not found in fs_db or in wmf)
	,	", reg_mnt_id        int                 NOT NULL"
	,	", reg_fil_id        int                 NOT NULL"
	,	", reg_xtn_id        int                 NOT NULL"
	,	");"
	);
	private static final int Max_rows_per_rdr = Sqlite_engine_.Stmt_arg_max;
}

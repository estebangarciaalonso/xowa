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
package gplx.fsdb; import gplx.*;
import gplx.dbs.*;
public class Fsdb_vlm_tbl {
	public Fsdb_vlm_tbl Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_vlm_key, Fld_vlm_url, Fld_vlm_path_bgn);}
	public void Insert(Db_provider p, String key, String url, String path_bgn) {
		Db_stmt stmt = Insert_stmt(p);
		Insert(stmt, key, url, path_bgn);
		stmt.Rls();
	}
	public void Insert(Db_stmt stmt, String key, String url, String path_bgn) {
		stmt.Clear()
		.Val_str_(key)
		.Val_str_(url)
		.Val_str_(path_bgn)
		.Exec_insert();
	}	
	public Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_vlm_key), Fld_vlm_url, Fld_vlm_path_bgn);}
	public void Update(Db_stmt stmt, String key, String url, String path_bgn) {
		stmt.Clear()
		.Val_str_(key)
		.Val_str_(url)
		.Val_str_(path_bgn)
		.Exec_update();
	}
	public Fsdb_vlm_db_data[] Select_all(Db_provider p, Io_url dir) {
		ListAdp rv = ListAdp_.new_();
		Db_qry qry = Db_qry_select.new_().From_(Tbl_name).Cols_all_().OrderBy_asc_(Fld_vlm_key);
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = p.Exec_qry_as_rdr(qry);
			int id = 0;
			while (rdr.MoveNextPeer()) {
				String key = rdr.ReadStr(Fld_vlm_key);
//					String url = rdr.ReadStr(Fld_vlm_url);
				byte[] path_bgn = rdr.ReadBryByStr(Fld_vlm_path_bgn);
				Fsdb_vlm_db_data itm = new Fsdb_vlm_db_data().Init(null, id, key, path_bgn);
				rv.Add(itm);
				++id;
			}
		} finally {rdr.Rls();}
		return (Fsdb_vlm_db_data[])rv.XtoAry(Fsdb_vlm_db_data.class);
	}
	public static final String Tbl_name = "fsdb_vlm", Fld_vlm_key = "vlm_key", Fld_vlm_url = "vlm_url", Fld_vlm_path_bgn = "vlm_path_bgn";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_vlm"
	,	"( vlm_key               varchar(16)         NOT NULL    PRIMARY KEY"
	,	", vlm_url               varchar(255)        NOT NULL"
	,	", vlm_path_bgn          varchar(1024)       NOT NULL"
	,	");"
	);
}

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
public class Fsdb_xtn_img_tbl {
	public static void Create_table(Db_provider p) {
		Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); 
	}
	public static void Insert(Db_provider p, int id, int w, int h) {
		Db_stmt stmt = Insert_stmt(p);
		try {Insert(stmt, id, w, h);}
		finally {stmt.Rls();}
	}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_img_id, Fld_img_w, Fld_img_h);}
	public static void Insert(Db_stmt stmt, int id, int w, int h) {
		stmt.Clear()
		.Val_int_(id)
		.Val_int_(w)
		.Val_int_(h)
		.Exec_insert();
	}
	public static Db_stmt Select_itm_by_id_stmt(Db_provider p) {
		return Db_stmt_.new_select_(p, Tbl_name, String_.Ary(Fld_img_id), Fld_img_w, Fld_img_h); 
	}
	public static Fsdb_xtn_img_itm Select_itm_by_id(Db_provider p, int id) {
		Db_stmt stmt = Select_itm_by_id_stmt(p);
		try {
			return Select_itm_by_id(stmt, id);
		} finally {stmt.Rls();}
	}
	public static Fsdb_xtn_img_itm Select_itm_by_id(Db_stmt stmt, int id) {
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = stmt.Val_int_(id).Exec_select();
			if (rdr.MoveNextPeer())
				return new Fsdb_xtn_img_itm().Init_by_load(id, rdr.ReadInt(Fld_img_w), rdr.ReadInt(Fld_img_h));
			else
				return Fsdb_xtn_img_itm.Null;
		}
		finally {rdr.Rls();}
	}
	public static final String Tbl_name = "fsdb_xtn_img", Fld_img_id = "img_id", Fld_img_w = "img_w", Fld_img_h = "img_h";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS fsdb_xtn_img"
	,	"( img_id            integer             NOT NULL    PRIMARY KEY"
	,	", img_w             integer             NOT NULL"
	,	", img_h             integer             NOT NULL"
	,	");"
	);
}

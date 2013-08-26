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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*;
class Xodb_tbl_image {
	public Xodb_tbl_image Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_img_name, Fld_img_size, Fld_img_width, Fld_img_height, Fld_img_bits);}
	public void Insert(Db_stmt stmt, byte[] ttl, int size, int w, int h, int bits) {
		stmt.Clear()
		.Val_str_by_bry_(ttl)
		.Val_int_(size)
		.Val_int_(w)
		.Val_int_(h)
		.Val_int_(bits)
		.Exec_insert();
	}
	public static final String Tbl_name = "image", Fld_img_name = "img_name", Fld_img_size = "img_size", Fld_img_width = "img_width", Fld_img_height = "img_height", Fld_img_bits = "img_bits";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS image"
	,	"( img_name        varchar(255)    NOT NULL -- varbinary(255)"
	,	", img_size        integer         NOT NULL -- int(8) unsigned"
	,	", img_width       integer         NOT NULL -- int(5)"
	,	", img_height      integer         NOT NULL -- int(5)"
	,	", img_bits        smallint        NOT NULL -- int(3)"
	,	");"
	);
}

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
package gplx.xowa.dbs.tbls; import gplx.*; import gplx.xowa.*; import gplx.xowa.dbs.*;
import gplx.dbs.*;
public class Xodb_tbl_xowa_file_scan {
	public Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_olt_page_id, Fld_olt_title, Fld_olt_ext_id, Fld_olt_type, Fld_olt_width, Fld_olt_height, Fld_olt_upright, Fld_olt_time, Fld_olt_wiki_id, Fld_olt_status);}
	public void Insert(Db_stmt stmt, int page_id, byte[] ttl, byte ext_id, byte img_type, int w, int h, double upright, int time) {
		stmt.Clear()
		.Val_int_(page_id)
		.Val_str_by_bry_(ttl)
		.Val_byte_(ext_id)
		.Val_byte_(img_type)
		.Val_int_(w)
		.Val_int_(h)
		.Val_double_(upright)
		.Val_double_(time)
		.Val_byte_(Byte_.MaxValue_127)
		.Val_byte_(Byte_.MaxValue_127)
		.Exec_insert();
	}
	public static final String Tbl_name = "oimg_lnki_temp", Fld_olt_page_id = "olt_page_id", Fld_olt_title = "olt_title", Fld_olt_ext_id = "olt_ext_id", Fld_olt_type = "olt_type"
	, Fld_olt_width = "olt_width", Fld_olt_height = "olt_height", Fld_olt_upright = "olt_upright", Fld_olt_time = "olt_time", Fld_olt_wiki_id = "olt_wiki_id", Fld_olt_status = "olt_status";
}

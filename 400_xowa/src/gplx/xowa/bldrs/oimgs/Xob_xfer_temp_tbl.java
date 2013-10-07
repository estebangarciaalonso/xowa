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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*;
class Xob_xfer_temp_tbl {
	public static void Create_table(Db_provider p)		{Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);}
	public static Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_oxt_lnki_id, Fld_oxt_xfer_repo, Fld_oxt_orig_page_id, Fld_oxt_xfer_ttl, Fld_oxt_xfer_ext, Fld_oxt_xfer_type, Fld_oxt_orig_media_type, Fld_oxt_file_is_orig, Fld_oxt_orig_w, Fld_oxt_orig_h, Fld_oxt_file_w, Fld_oxt_file_h, Fld_oxt_html_w, Fld_oxt_html_h, Fld_oxt_xfer_thumbtime, Fld_oxt_xfer_count);}
	public static void Insert(Db_stmt stmt, int lnki_id, byte repo_id, int page_id, String ttl, int ext_id, byte lnki_type, String orig_media_type, boolean file_is_orig, int orig_w, int orig_h, int html_w, int html_h, int file_w, int file_h, double thumbtime, int count) {
		stmt.Clear()
		.Val_int_(lnki_id)
		.Val_byte_(repo_id)
		.Val_int_(page_id)
		.Val_str_(ttl)
		.Val_int_(ext_id)
		.Val_byte_(lnki_type)
		.Val_str_(orig_media_type)
		.Val_byte_by_bool_(file_is_orig)
		.Val_int_(orig_w)
		.Val_int_(orig_h)
		.Val_int_(file_w)
		.Val_int_(file_h)
		.Val_int_(html_w)
		.Val_int_(html_h)
		.Val_double_(thumbtime)
		.Val_int_(count)
		.Exec_insert();
	}
	public static final String Tbl_name = "oimg_xfer_temp"
	, Fld_oxt_lnki_id = "oxt_lnki_id", Fld_oxt_orig_page_id = "oxt_orig_page_id"
	, Fld_oxt_xfer_repo = "oxt_xfer_repo", Fld_oxt_xfer_ttl = "oxt_xfer_ttl", Fld_oxt_xfer_ext = "oxt_xfer_ext", Fld_oxt_xfer_type = "oxt_xfer_type", Fld_oxt_orig_media_type = "oxt_orig_media_type"
	, Fld_oxt_file_is_orig = "oxt_file_is_orig"
	, Fld_oxt_orig_w = "oxt_orig_w", Fld_oxt_orig_h = "oxt_orig_h"
	, Fld_oxt_file_w = "oxt_file_w", Fld_oxt_file_h = "oxt_file_h"
	, Fld_oxt_html_w = "oxt_html_w", Fld_oxt_html_h = "oxt_html_h", Fld_oxt_xfer_thumbtime = "oxt_xfer_thumbtime"
	, Fld_oxt_xfer_count = "oxt_xfer_count"
	;
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS oimg_xfer_temp"
	,	"( oxt_lnki_id         integer             NOT NULL"	// oimg_lnki_regy.olr_lnki_id
	,	", oxt_orig_page_id    integer             NOT NULL"
	,	", oxt_xfer_repo       tinyint             NOT NULL"
	,	", oxt_xfer_ttl        varchar(255)        NOT NULL"
	,	", oxt_xfer_ext        tinyint             NOT NULL"
	,	", oxt_orig_media_type varchar(64)         NOT NULL"
	,	", oxt_xfer_type       tinyint             NOT NULL"
	,	", oxt_file_is_orig    tinyint             NOT NULL"
	,	", oxt_orig_w          integer             NOT NULL"
	,	", oxt_orig_h          integer             NOT NULL"
	,	", oxt_file_w          integer             NOT NULL"
	,	", oxt_file_h          integer             NOT NULL"
	,	", oxt_html_w          integer             NOT NULL"
	,	", oxt_html_h          integer             NOT NULL"
	,	", oxt_xfer_thumbtime  double              NOT NULL"
	,	", oxt_xfer_count      integer             NOT NULL"
	,	");"
	);
}

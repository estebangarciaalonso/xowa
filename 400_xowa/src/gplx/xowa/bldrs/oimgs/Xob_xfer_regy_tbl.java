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
import gplx.dbs.*;
class Xob_xfer_regy_tbl {
	public static void Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql);}
	public static void Create_data(Gfo_usr_dlg usr_dlg, Db_provider p) {
		p.Exec_sql(Sql_create_data_orig);
		p.Exec_sql(Sql_create_data_thumb);
		Sqlite_engine_.Idx_create(usr_dlg, p, "oimg", Idx_select);
	}
	public static void Create_index(Gfo_usr_dlg usr_dlg, Db_provider p)	{Sqlite_engine_.Idx_create(usr_dlg, p, Xodb_db_file.Name__oimg_lnki, Idx_select);}
	public static Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_oxr_lnki_id), Fld_oxr_xfer_status, Fld_oxr_xfer_bin_tid, Fld_oxr_xfer_bin_msg);}
	public static void Update(Db_stmt stmt, byte status, int id, byte wkr_tid, String wkr_msg) {
		stmt.Clear()
		.Val_int_(id)
		.Val_byte_(status)
		.Val_byte_(wkr_tid)
		.Val_str_(wkr_msg)
		.Exec_update();
	}
	public static DataRdr Select(Db_provider p, byte repo_id, byte[] ttl, int limit) {
		Db_qry qry = Db_qry_.select_().Cols_all_()
			.From_(Tbl_name)
			.Where_(gplx.criterias.Criteria_.And_many(Db_crt_.mte_(Fld_oxr_xfer_repo, repo_id), Db_crt_.mt_(Fld_oxr_xfer_ttl, String_.new_utf8_(ttl)), Db_crt_.eq_(Fld_oxr_xfer_status, 0)))
			.OrderBy_many_(Fld_oxr_xfer_status, Fld_oxr_xfer_repo, Fld_oxr_xfer_ttl, Fld_oxr_file_w)
			.Limit_(limit)
			;
		return p.Exec_qry_as_rdr(qry);
	}
	public static final String Tbl_name = "oimg_xfer_regy"
	, Fld_oxr_lnki_id = "oxr_lnki_id", Fld_oxr_orig_page_id = "oxr_orig_page_id"
	, Fld_oxr_xfer_repo = "oxr_xfer_repo", Fld_oxr_xfer_ttl = "oxr_xfer_ttl", Fld_oxr_xfer_ext = "oxr_xfer_ext", Fld_oxr_xfer_type = "oxr_xfer_type", Fld_oxr_orig_media_type = "oxr_orig_media_type"
	, Fld_oxr_file_is_orig = "oxr_file_is_orig"
	, Fld_oxr_orig_w = "oxr_orig_w", Fld_oxr_orig_h = "oxr_orig_h"
	, Fld_oxr_file_w = "oxr_file_w", Fld_oxr_file_h = "oxr_file_h", Fld_oxr_xfer_thumbtime = "oxr_xfer_thumbtime"
	, Fld_oxr_xfer_count = "oxr_xfer_count", Fld_oxr_xfer_status = "oxr_xfer_status", Fld_oxr_xfer_bin_tid = "oxr_xfer_bin_tid", Fld_oxr_xfer_bin_msg = "oxr_xfer_bin_msg"
	;
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS oimg_xfer_regy"
	,	"( oxr_lnki_id         integer             NOT NULL"
	,	", oxr_orig_page_id    integer             NOT NULL"
	,	", oxr_xfer_repo       tinyint             NOT NULL"
	,	", oxr_xfer_ttl        varchar(255)        NOT NULL"
	,	", oxr_xfer_ext        integer             NOT NULL"
	,	", oxr_orig_media_type varchar(64)         NOT NULL"
	,	", oxr_file_is_orig    byte                NOT NULL"
	,	", oxr_orig_w          integer             NOT NULL"
	,	", oxr_orig_h          integer             NOT NULL"
	,	", oxr_file_w          integer             NOT NULL"
	,	", oxr_file_h          integer             NOT NULL"
	,	", oxr_xfer_thumbtime  integer             NOT NULL"
	,	", oxr_xfer_count      integer             NOT NULL"
	,	", oxr_xfer_status     tinyint             NOT NULL"	// 0=todo; 1=fail; 2=pass; 3=done
	,	", oxr_xfer_bin_tid    tinyint             NOT NULL"
	,	", oxr_xfer_bin_msg    varchar(255)        NOT NULL"
	,	");"
	);
	private static final String Sql_create_data_orig = String_.Concat_lines_nl
	( "INSERT INTO oimg_xfer_regy "
	, "( oxr_lnki_id, oxr_orig_page_id, oxr_xfer_repo, oxr_xfer_ttl, oxr_xfer_ext, oxr_orig_media_type"
	, ", oxr_file_is_orig, oxr_orig_w, oxr_orig_h, oxr_file_w, oxr_file_h, oxr_xfer_thumbtime, oxr_xfer_count"
	, ", oxr_xfer_status, oxr_xfer_bin_tid, oxr_xfer_bin_msg"
	, ")"
	, "SELECT "
	, "  Min(oxt_lnki_id), Min(oxt_orig_page_id), oxt_xfer_repo, oxt_xfer_ttl, oxt_xfer_ext, oxt_orig_media_type"
	, ", oxt_file_is_orig, oxt_orig_w, oxt_orig_h, -1, -1, oxt_xfer_thumbtime, Sum(oxt_xfer_count)"
	, ", 0, 0, ''"
	, "FROM    oimg_xfer_temp x"
	, "WHERE   oxt_file_is_orig = 1"
	, "GROUP BY oxt_xfer_repo, oxt_xfer_ttl, oxt_xfer_ext, oxt_orig_media_type, oxt_file_is_orig, oxt_orig_w, oxt_orig_h, oxt_xfer_thumbtime"
	);
	private static final String Sql_create_data_thumb = String_.Concat_lines_nl
	( "INSERT INTO oimg_xfer_regy "
	, "( oxr_lnki_id, oxr_orig_page_id, oxr_xfer_repo, oxr_xfer_ttl, oxr_xfer_ext, oxr_orig_media_type"
	, ", oxr_file_is_orig, oxr_orig_w, oxr_orig_h, oxr_file_w, oxr_file_h, oxr_xfer_thumbtime, oxr_xfer_count"
	, ", oxr_xfer_status, oxr_xfer_bin_tid, oxr_xfer_bin_msg"
	, ")"
	, "SELECT "
	, "  Min(oxt_lnki_id), Min(oxt_orig_page_id), oxt_xfer_repo, oxt_xfer_ttl, oxt_xfer_ext, oxt_orig_media_type"
	, ", oxt_file_is_orig, oxt_orig_w, oxt_orig_h, oxt_file_w, oxt_file_h, oxt_xfer_thumbtime, Sum(oxt_xfer_count)"
	, ", 0, 0, ''"
	, "FROM    oimg_xfer_temp x"
	, "WHERE   oxt_file_is_orig = 0"
	, "GROUP BY oxt_xfer_repo, oxt_xfer_ttl, oxt_xfer_ext, oxt_orig_media_type, oxt_file_is_orig, oxt_orig_w, oxt_orig_h, oxt_file_w, oxt_file_h, oxt_xfer_thumbtime"
	);
	private static final Db_idx_itm
		Idx_select	= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_xfer_regy__select        ON oimg_xfer_regy (oxr_xfer_status, oxr_xfer_repo, oxr_xfer_ttl, oxr_file_w);")
	;
	public static byte Status_todo = 0, Status_pass = 1, Status_fail = 2;
}

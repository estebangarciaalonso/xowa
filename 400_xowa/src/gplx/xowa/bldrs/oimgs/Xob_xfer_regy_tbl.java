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
	public Xob_xfer_regy_tbl Create_table(Db_provider p)		{Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public void Create_data(Gfo_usr_dlg usr_dlg, Db_provider p) {
		p.Exec_sql(Sql_create_data);
		Sqlite_engine_.Idx_create(usr_dlg, p, "oimg", Idx_select);
	}
	public void Create_indexes(Gfo_usr_dlg usr_dlg, Db_provider p)	{Sqlite_engine_.Idx_create(usr_dlg, p, Xob_lnki_temp_wkr.Db_name, Idx_select);}
	public Db_stmt Update_stmt(Db_provider p) {return Db_stmt_.new_update_(p, Tbl_name, String_.Ary(Fld_oxr_lnki_id), Fld_oxr_lnki_status, Fld_oxr_lnki_bin_tid, Fld_oxr_lnki_bin_msg);}
	public void Update(Db_stmt stmt, boolean done, int id, byte wkr_tid, String wkr_msg) {
		stmt.Clear()
		.Val_int_(id)
		.Val_byte_by_bool_(done)
		.Val_byte_(wkr_tid)
		.Val_str_(wkr_msg)
		.Exec_update();
	}
	public DataRdr Select(Db_provider p, byte repo_id, byte[] ttl, int limit) {
		Db_qry qry = Db_qry_.select_().From_(Tbl_name).Cols_all_()
			.Where_(gplx.criterias.Criteria_.And(Db_crt_.mte_(Fld_oxr_orig_repo, repo_id), Db_crt_.mt_(Fld_oxr_lnki_ttl, String_.new_utf8_(ttl))))
			.OrderBy_many_(Fld_oxr_lnki_status, Fld_oxr_orig_repo, Fld_oxr_lnki_ttl, Fld_oxr_lnki_w)
			.Limit_(limit)
			;
		return p.Exec_qry_as_rdr(qry);
	}
	public static final String Tbl_name = "oimg_xfer_regy"
	, Fld_oxr_lnki_id = "oxr_lnki_id", Fld_oxr_orig_repo = "oxr_orig_repo", Fld_oxr_orig_page_id = "oxr_orig_page_id"
	, Fld_oxr_orig_ttl = "oxr_orig_ttl", Fld_oxr_orig_redirect = "oxr_orig_redirect", Fld_oxr_orig_ext = "oxr_orig_ext"
	, Fld_oxr_orig_w = "oxr_orig_w", Fld_oxr_orig_h = "oxr_orig_h"
	, Fld_oxr_lnki_ttl = "oxr_lnki_ttl", Fld_oxr_lnki_type = "oxr_lnki_type", Fld_oxr_lnki_w = "oxr_lnki_w", Fld_oxr_lnki_h = "oxr_lnki_h", Fld_oxr_lnki_thumbtime = "oxr_lnki_thumbtime"
	, Fld_oxr_lnki_status = "oxr_lnki_status", Fld_oxr_lnki_bin_tid = "oxr_lnki_bin_tid", Fld_oxr_lnki_bin_msg = "oxr_lnki_bin_msg"
	, Fld_oxr_html_w = "oxr_html_w", Fld_oxr_html_h = "oxr_html_h"
	;
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS oimg_xfer_regy"
	,	"( oxr_lnki_id         integer             NOT NULL"
	,	", oxr_orig_repo       tinyint             NOT NULL"
	,	", oxr_orig_page_id    integer             NOT NULL"
	,	", oxr_orig_ttl        varchar(255)        NOT NULL"
	,	", oxr_orig_redirect   varchar(255)        NOT NULL"
	,	", oxr_orig_ext        integer             NOT NULL"
	,	", oxr_orig_w          integer             NOT NULL"
	,	", oxr_orig_h          integer             NOT NULL"
	,	", oxr_lnki_ttl        varchar(255)        NOT NULL"
	,	", oxr_lnki_type       tinyint             NOT NULL"
	,	", oxr_lnki_w          integer             NOT NULL"
	,	", oxr_lnki_h          integer             NOT NULL"
	,	", oxr_lnki_thumbtime  integer             NOT NULL"
	,	", oxr_lnki_status     tinyint             NOT NULL"	// 0=todo; 1=fail; 2=pass; 3=done
	,	", oxr_lnki_bin_tid    tinyint             NOT NULL"
	,	", oxr_lnki_bin_msg    varchar(255)        NOT NULL"
	,	", oxr_html_w          integer             NOT NULL"
	,	", oxr_html_h          integer             NOT NULL"
	,	");"
	);
	private static final String Sql_create_data = String_.Concat_lines_nl
	( "INSERT INTO oimg_xfer_regy "
	, "( oxr_lnki_id, oxr_orig_repo, oxr_orig_page_id, oxr_orig_ttl, oxr_orig_redirect, oxr_orig_ext, oxr_orig_w, oxr_orig_h"
	, ", oxr_lnki_ttl, oxr_lnki_type, oxr_lnki_w, oxr_lnki_h, oxr_lnki_thumbtime, oxr_lnki_status, oxr_lnki_bin_tid, oxr_lnki_bin_msg"
	, ", oxr_html_w, oxr_html_h)"
	, "SELECT "
	, "  oxt_id, oxt_wiki_id, oxt_page_id, oxt_ttl, Coalesce(ofr_redirect_ttl, ''), oxt_ext_id, Coalesce(ofr_width, -1), Coalesce(ofr_height, -1)"
	, ", oxt_ttl, oxt_lnki_type, oxt_width, oxt_height, oxt_time, 0, 0, ''"
	, ", oxt_width, oxt_height"
	, "FROM    oimg_xfer_temp x"
	, "        LEFT JOIN oimg_file_regy f ON x.oxt_page_id = f.ofr_page_id"
	, "GROUP BY oxt_wiki_id, oxt_page_id, oxt_ttl, oxt_ext_id, oxt_width, oxt_height, oxt_time"
	);
	private static final Db_idx_itm
		Idx_select	= Db_idx_itm.sql_("CREATE        INDEX IF NOT EXISTS oimg_xfer_regy__select        ON oimg_xfer_regy (oxr_lnki_status, oxr_orig_repo, oxr_lnki_ttl, oxr_lnki_w);")
	;
}

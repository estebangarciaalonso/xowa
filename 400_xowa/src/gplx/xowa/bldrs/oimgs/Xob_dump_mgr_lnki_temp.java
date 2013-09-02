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
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.dbs.tbls.*;
public class Xob_dump_mgr_lnki_temp extends Xob_dump_mgr_base implements Xobc_lnki_wkr {
	private Db_provider provider; private Db_stmt stmt;
	private Xodb_file db_file; private Xodb_tbl_oimg_lnki_temp tbl;
	public Xob_dump_mgr_lnki_temp(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	@Override public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.lnki_temp";
	@Override public byte Init_redirect() {return Bool_.N_byte;}	// lnki will never be found in a redirect
	@Override public int[] Init_ns_ary() {return Int_.Ary(Xow_ns_.Id_main, Xow_ns_.Id_category, Xow_ns_.Id_template);}
	@Override public Xodb_file Init_db_file() {
		ctx.Lnki().File_wkr_(this);
		db_file = wiki.Db_mgr_as_sql().Fsys_mgr().Get_or_make(Db_name);
		provider = db_file.Provider();
		tbl = new Xodb_tbl_oimg_lnki_temp().Create_table(provider);
		stmt = tbl.Insert_stmt(provider);
		provider.Txn_mgr().Txn_bgn_if_none();
		return db_file;
	}
	@Override public void Exec_page_hook(Xow_ns ns, Xodb_page page, byte[] page_src) {
		byte[] ttl_bry = ns.Gen_ttl(page.Ttl_wo_ns());
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
		ttl_bry = ttl.Page_db();
		ctx.Page().Page_ttl_(ttl).Page_id_(page.Id());
		if (ns.Id_tmpl())
			parser.Parse_tmpl(ctx, ctx.Tkn_mkr(), wiki.Ns_mgr().Ns_template(), ttl_bry, page_src);
		else {
			ctx.Tab().Display_ttl_(ttl_bry);
			parser.Parse_page_all_clear(root, ctx, ctx.Tkn_mkr(), page_src);
			root.Clear();
		}
	}
	public void Wkr_exec(Xop_ctx ctx, Xop_lnki_tkn lnki) {
		byte[] ttl = lnki.Ttl().Page_db();
		Xof_ext ext = Xof_ext_.new_by_ttl_(ttl);
		tbl.Insert(stmt, ctx.Page().Page_id(), ttl, Byte_.int_(ext.Id()), lnki.Lnki_type(), lnki.Width().Val(), lnki.Height().Val(), lnki.Upright(), lnki.Thumbtime());		
	}
	@Override public void Exec_commit_bgn(Xow_ns ns, byte[] ttl) {
		db_file.Provider().Txn_mgr().Txn_end_all_bgn_if_none();
	}
	@Override public void Exec_end() {
		db_file.Provider().Txn_mgr().Txn_end();
	}
	public static final String Db_name = "oimg_lnki";
}
class Xodb_tbl_oimg_lnki_temp {
	public Xodb_tbl_oimg_lnki_temp Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_olt_page_id, Fld_olt_title, Fld_olt_ext_id, Fld_olt_type, Fld_olt_width, Fld_olt_height, Fld_olt_upright, Fld_olt_time);}
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
		.Exec_insert();
	}
	public static final String Tbl_name = "oimg_lnki_temp", Fld_olt_page_id = "olt_page_id", Fld_olt_title = "olt_title", Fld_olt_ext_id = "olt_ext_id", Fld_olt_type = "olt_type", Fld_olt_width = "olt_width", Fld_olt_height = "olt_height", Fld_olt_upright = "olt_upright", Fld_olt_time = "olt_time";
	private static final String Tbl_sql = String_.Concat_lines_nl
	(  "CREATE TABLE IF NOT EXISTS oimg_lnki_temp"
	,	"( olt_page_id         int                 NOT NULL"
	,	", olt_title           varchar(1024)       NOT NULL"
	,	", olt_ext_id          tinyint             NOT NULL"
	,	", olt_type            tinyint             NOT NULL    -- thumb,full"
	,	", olt_width           int                 NOT NULL"
	,	", olt_height          int                 NOT NULL"
	,	", olt_upright         double              NOT NULL"
	,	", olt_time            double              NOT NULL"
	,	");"
	);
}

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
public class Xob_lnki_temp_wkr extends Xob_dump_mgr_base implements Xobc_lnki_wkr {
	private Db_provider provider; private Db_stmt stmt;
	public Xob_lnki_temp_wkr(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	@Override public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.lnki_temp";
	@Override public byte Init_redirect() {return Bool_.N_byte;}	// lnki will never be found in a redirect
	@Override public int[] Init_ns_ary() {return Int_.Ary(Xow_ns_.Id_main, Xow_ns_.Id_category, Xow_ns_.Id_template);}
	@Override public Db_provider Init_db_file() {
		ctx.Lnki().File_wkr_(this);
		provider = Xodb_db_file.init__oimg_lnki(wiki).Provider();
		Xob_lnki_temp_tbl.Create_table(provider);
		stmt = Xob_lnki_temp_tbl.Insert_stmt(provider);
		provider.Txn_mgr().Txn_bgn_if_none();
		return provider;
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
		Xob_lnki_temp_tbl.Insert(stmt, ctx.Page().Page_id(), ttl, Byte_.int_(ext.Id()), lnki.Lnki_type(), lnki.Width().Val(), lnki.Height().Val(), lnki.Upright(), lnki.Thumbtime());		
	}
	@Override public void Exec_commit_bgn(Xow_ns ns, byte[] ttl) {
		provider.Txn_mgr().Txn_end_all_bgn_if_none();
	}
	@Override public void Exec_end() {
		provider.Txn_mgr().Txn_end();
	}
}

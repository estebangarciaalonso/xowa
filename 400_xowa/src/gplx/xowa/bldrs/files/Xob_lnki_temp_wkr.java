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
package gplx.xowa.bldrs.files; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.dbs.tbls.*; import gplx.xowa.xtns.scribunto.*; import gplx.xowa.xtns.wdatas.*;
import gplx.xowa.bldrs.oimgs.*;
import gplx.xowa.parsers.lnkis.*; import gplx.xowa.parsers.logs.*;
public class Xob_lnki_temp_wkr extends Xob_dump_mgr_base implements Xop_lnki_logger {
	private Db_provider provider; private Db_stmt stmt;
	private boolean wdata_enabled = true, xtn_ref_enabled = true;
	private Xop_log_invoke_wkr invoke_wkr; private Xop_log_property_wkr property_wkr;
	private int[] ns_ids = Int_.Ary(Xow_ns_.Id_main);// , Xow_ns_.Id_category, Xow_ns_.Id_template
	public Xob_lnki_temp_wkr(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_ctor(bldr, wiki);}
	@Override public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "file.lnki_temp";
	@Override public byte Init_redirect() {return Bool_.N_byte;}	// lnki will never be found in a redirect
	@Override public int[] Init_ns_ary() {return ns_ids;}
	@Override protected void Init_reset(Db_provider p) {
		p.Exec_sql("DELETE FROM " + Xodb_xowa_cfg_tbl.Tbl_name);
		p.Exec_sql("DELETE FROM " + Xob_lnki_temp_tbl.Tbl_name);
		invoke_wkr.Init_reset();
		property_wkr.Init_reset();
	}
	@Override protected Db_provider Init_db_file() {
		ctx.Lnki().File_wkr_(this);
		Xodb_db_file db_file = Xodb_db_file.init__file_make(wiki.Fsys_mgr().Root_dir());
		provider = db_file.Provider();
		Xob_lnki_temp_tbl.Create_table(provider);
		stmt = Xob_lnki_temp_tbl.Insert_stmt(provider);
		return provider;
	}
	@Override protected void Cmd_bgn_end() {
		Xop_log_mgr log_mgr = ctx.App().Log_mgr();
		log_mgr.Log_dir_(wiki.Fsys_mgr().Root_dir());	// put log in wiki dir, instead of user.temp
		invoke_wkr = this.Invoke_wkr();	// set member reference
		invoke_wkr = log_mgr.Make_wkr_invoke();
		property_wkr = this.Property_wkr();	// set member reference
		property_wkr = log_mgr.Make_wkr_property();
		wiki.App().Wiki_mgr().Wdata_mgr().Enabled_(wdata_enabled);
		if (!xtn_ref_enabled) gplx.xowa.xtns.refs.Xtn_references_nde.Enabled = false;
		gplx.xowa.xtns.gallery.Xtn_gallery_nde.Log_wkr = log_mgr.Make_wkr().Save_src_str_(Bool_.Y);
		gplx.xowa.xtns.imageMap.Xop_imageMap_xnde.Log_wkr = log_mgr.Make_wkr();
		gplx.xowa.Xop_xnde_wkr.Timeline_log_wkr = log_mgr.Make_wkr();
		gplx.xowa.xtns.scores.Xtn_score.Log_wkr = log_mgr.Make_wkr();
		gplx.xowa.xtns.hiero.Xtn_hiero_nde.Log_wkr = log_mgr.Make_wkr();
		provider.Txn_mgr().Txn_bgn_if_none();
		log_mgr.Txn_bgn();
	}
	@Override public void Exec_page_hook(Xow_ns ns, Xodb_page page, byte[] page_src) {
		byte[] ttl_bry = ns.Gen_ttl(page.Ttl_wo_ns());
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
		ttl_bry = ttl.Page_db();
		ctx.Page().Page_ttl_(ttl).Page_id_(page.Id());
		ctx.Tab().Redlinks_mgr().Page_bgn(ctx);
		if (ns.Id_tmpl())
			parser.Parse_tmpl(ctx, ctx.Tkn_mkr(), wiki.Ns_mgr().Ns_template(), ttl_bry, page_src);
		else {
			ctx.Tab().Display_ttl_(ttl_bry);
			parser.Parse_page_all_clear(root, ctx, ctx.Tkn_mkr(), page_src);
			root.Clear();
		}
	}
	@Override public void Exec_commit_bgn() {
		provider.Txn_mgr().Txn_end_all_bgn_if_none();
	}
	@Override public void Exec_end() {
		wiki.App().Log_mgr().Txn_end();
		provider.Txn_mgr().Txn_end();
	}
	public void Wkr_exec(Xop_ctx ctx, Xop_lnki_tkn lnki) {
		if (lnki.Ttl().ForceLiteralLink()) return; // ignore literal links which creat a link to file, but do not show the image; EX: [[:File:A.png|thumb|120px]] creates a link to File:A.png, regardless of other display-oriented args
		byte[] ttl = lnki.Ttl().Page_db();
		Xof_ext ext = Xof_ext_.new_by_ttl_(ttl);
		Xob_lnki_temp_tbl.Insert(stmt, ctx.Page().Page_id(), ttl, Byte_.int_(ext.Id()), lnki.Lnki_type(), lnki.Width().Val(), lnki.Height().Val(), lnki.Upright(), lnki.Thumbtime());		
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_wdata_enabled_))				wdata_enabled = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_xtn_ref_enabled_))			xtn_ref_enabled = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_ns_ids_))					ns_ids = Int_.Ary_parse(m.ReadStr("v"), "|");
		else if	(ctx.Match(k, Invk_ns_ids_by_aliases))			Ns_ids_by_aliases(m.ReadStrAry("v", "|"));
		else if	(ctx.Match(k, Invk_property_wkr))				return this.Property_wkr();
		else if	(ctx.Match(k, Invk_invoke_wkr))					return this.Invoke_wkr();
		else	return super.Invk(ctx, ikey, k, m);
		return this;
	}	private static final String Invk_wdata_enabled_ = "wdata_enabled_", Invk_xtn_ref_enabled_ = "xtn_ref_enabled_", Invk_ns_ids_ = "ns_ids_", Invk_ns_ids_by_aliases = "ns_ids_by_aliases", Invk_invoke_wkr = "invoke_wkr", Invk_property_wkr = "property_wkr";
	private void Ns_ids_by_aliases(String[] aliases) {
		ns_ids = wiki.Ns_mgr().Ids_by_aliases(aliases);
		int aliases_len = aliases.length;
		int ids_len = ns_ids.length;
		for (int i = 0; i < aliases_len; i++) {
			String alias = aliases[i];
			int id = i < ids_len ? ns_ids[i] : -1;
			usr_dlg.Note_many("", "", "ns: ~{0} <- ~{1}", Int_.XtoStr_fmt(id, "0000"), alias);
		}
		if (aliases_len != ids_len) throw Err_.new_fmt_("mismatch in aliases and ids: {0} vs {1}", aliases_len, ids_len);
	}
	private Xop_log_invoke_wkr Invoke_wkr() {
		if (invoke_wkr == null) invoke_wkr = bldr.App().Xtn_mgr().Xtn_scribunto().Invoke_wkr_or_new();
		return invoke_wkr;
	}
	private Xop_log_property_wkr Property_wkr() {
		if (property_wkr == null) property_wkr = bldr.App().Wiki_mgr().Wdata_mgr().Property_wkr_or_new();
		return property_wkr;
	}
}

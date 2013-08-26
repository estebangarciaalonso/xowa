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
package gplx.xowa; import gplx.*;
class Pf_url_filepath extends Pf_func_base {
	@Override public boolean Func_require_colon_arg() {return true;}
	@Override public int Id() {return Xol_kwd_grp_.Id_url_filepath;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_url_filepath().Name_(name);}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bb) {
		byte[] val_ary = Eval_argx(ctx, src, caller, self); if (val_ary == ByteAry_.Empty) return;
		Xow_wiki wiki = ctx.Wiki();
		val_ary = ByteAry_.Add(Bry_file, val_ary);
		Xoa_ttl ttl = Xoa_ttl.new_(wiki, ctx.App().Msg_log_null(), val_ary, val_ary, 0, val_ary.length);  if (ttl == null) return; // text is not valid ttl; return;
		Xoa_app app = ctx.App();
		Xoa_page page = wiki.Data_mgr().Get_page(ttl, false);
		if (page == null) {	// file not found in current wiki; try commons; 
			Xow_wiki commons_wiki = app.Wiki_mgr().Get_by_key_or_null(wiki.Commons_wiki_key()); if (commons_wiki == null) return;	// commons_wiki not installed; exit; DATE:2013-06-08
			if (!Env_.Mode_testing()) commons_wiki.Init_assert();// must assert load else page_zip never detected; TODO: move to Xoa_wiki_mgr.New_wiki; DATE:2013-03-10
			page = commons_wiki.Data_mgr().Get_page(ttl, false);
		}
		if (page == null) return; // page not found in commons; exit; TODO: iterate over all repos; WHEN: when more than 2 repos are ever set up (highly unlikely)	
		byte[] ttl_bry = page.Page_ttl().Page_url();
		Xofw_file_finder_rslt tmp_rslt = wiki.File_mgr().Repo_mgr().Page_finder_locate(ttl_bry);
		if (tmp_rslt .Repo_idx() == Byte_.MaxValue_127) return;
		Xof_repo_itm trg_repo = wiki.File_mgr().Repo_mgr().Repos_get_at(tmp_rslt.Repo_idx()).Trg();
		xfer_itm.Atrs_by_ttl(ttl_bry, ByteAry_.Empty);	// redirect is empty b/c Get_page does all redirect lookups
		byte[] url = url_bldr.Set_trg_html_(Xof_repo_itm.Mode_orig, trg_repo, ttl_bry, xfer_itm.Md5(), xfer_itm.Ext(), Xof_url_.Null_size_deprecated, Xop_lnki_tkn.Thumbtime_null).Xto_bry();
		bb.Add(url);
	}	static final byte[] Bry_file = ByteAry_.new_ascii_("File:");
	private static final Xof_xfer_itm xfer_itm = new Xof_xfer_itm();
	private static final Xof_url_bldr url_bldr = new Xof_url_bldr();
}

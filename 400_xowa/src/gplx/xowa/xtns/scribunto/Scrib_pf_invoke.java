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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Scrib_pf_invoke extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_invoke;}
	@Override public Pf_func New(int id, byte[] name) {return new Scrib_pf_invoke().Name_(name);}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {// {{#invoke:mod_name|prc_name|prc_args...}}
		Xow_wiki wiki = ctx.Wiki();
		byte[] mod_name = Eval_argx(ctx, src, caller, self);
		if (ByteAry_.Len_eq_0(mod_name)) {Error(bfr, wiki.Msg_mgr(), Err_mod_missing); return;}		// {{#invoke:}}
		Scrib_engine engine = Scrib_engine.Engine();
		if (engine == null) {engine = Scrib_engine.Engine_new_(ctx.App(), ctx).Init(); engine.When_page_changed(ctx.Page());}
		Scrib_mod mod = engine.Mods_get(mod_name);
		byte[] mod_raw = null;
		if (mod == null) {
			ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b128();
			Xow_ns module_ns = wiki.Ns_mgr().Get_by_id(Ns_id_module);
			byte[] mod_ttl_bry = tmp_bfr.Add(module_ns.Name_db_w_colon()).Add(mod_name).Mkr_rls().XtoAryAndClear();
			Xoa_ttl mod_ttl = Xoa_ttl.parse_(wiki, mod_ttl_bry);
			Xoa_page mod_page = wiki.Data_mgr().Get_page(mod_ttl, false);
			if (mod_page.Missing()) {Error(bfr, wiki.Msg_mgr(), Err_mod_missing); return;}		// {{#invoke:missing_mod}}
			mod_raw = mod_page.Data_raw();
		}
		else
			mod_raw = mod.Text_bry();

		int args_len = self.Args_len();
		byte[] fnc_name = Pf_func_.EvalArgOr(ctx, src, caller, self, args_len, 0, null);
		if (ByteAry_.Len_eq_0(fnc_name)) {Error(bfr, wiki.Msg_mgr(), Err_fnc_missing); return;}		// {{#invoke:Module:Mod_0|missing_fnc}}
		if (!engine.Enabled()) {bfr.Add_mid(src, self.Src_bgn(), self.Src_end()); return;}
		try {engine.Invoke(wiki, src, caller, self, bfr, mod_name, mod_raw, fnc_name);}
		catch (Exception e) {
			bfr.Add_mid(src, self.Src_bgn(), self.Src_end());
			bfr.Add(Xoh_consts.Comm_bgn).Add_str(Err_.Message_gplx_brief(e)).Add(Xoh_consts.Comm_end);
			ctx.App().Usr_dlg().Warn_many(GRP_KEY, "invoke.fail", "invoke failed: ~{0} ~{1}", String_.new_utf8_(ctx.Page().Page_ttl().Raw()), String_.new_utf8_(src, self.Src_bgn(), self.Src_end()));
			Scrib_engine.Engine_invalidate();	// reset engine
		}
	}
	public static void Error(ByteAryBfr bfr, Xow_msg_mgr msg_mgr, String error) {Error(bfr, msg_mgr, ByteAry_.new_utf8_(error));}
	public static void Error(ByteAryBfr bfr, Xow_msg_mgr msg_mgr, byte[] error) {
		ByteAryFmtr fmtr = ByteAryFmtr.new_("<strong class=\"error\"><span class=\"scribunto-error\" id=\"mw-scribunto-error-0\">~{0}: ~{1}.</span></strong>");	// <!--~{0}: ~{1}.-->
		byte[] script_error_msg = msg_mgr.Val_by_id(Xol_msg_itm_.Id_scribunto_parser_error);
		fmtr.Bld_bfr_many(bfr, script_error_msg, error);
	}
	static final String GRP_KEY = "xowa.parser.xtn.invoke";
	public static final String Err_mod_missing = "No such module", Err_fnc_missing = "The function you specified did not exist";
	public static final int Ns_id_module = 828, Ns_id_module_talk = 829;
	public static final String Ns_name_module = "Module", Ns_name_module_talk = "Module talk";
}

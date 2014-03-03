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
import gplx.xowa.parsers.logs.*;
public class Scrib_pf_invoke extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_invoke;}
	@Override public Pf_func New(int id, byte[] name) {return new Scrib_pf_invoke().Name_(name);}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {// {{#invoke:mod_name|prc_name|prc_args...}}
		Xow_wiki wiki = ctx.Wiki();
		byte[] mod_name = Eval_argx(ctx, src, caller, self);
		if (ByteAry_.Len_eq_0(mod_name)) {Error(bfr, wiki.Msg_mgr(), Err_mod_missing); return;}		// EX: "{{#invoke:}}"
		int args_len = self.Args_len();
		byte[] fnc_name = Pf_func_.Eval_arg_or(ctx, src, caller, self, args_len, 0, null);
		Xop_log_invoke_wkr invoke_wkr = ctx.Xtn__scribunto__invoke_wkr();
		long log_time_bgn = 0;
		if (invoke_wkr != null) {
			log_time_bgn = Env_.TickCount();
			if (!invoke_wkr.Eval_bgn(ctx.Page(), mod_name, fnc_name)) return;
		}
		Scrib_engine engine = Scrib_engine.Engine();
		if (engine == null) {
			engine = Scrib_engine.Engine_new_(ctx.App(), ctx).Init();
			engine.When_page_changed(ctx.Page());
		}
		byte[] mod_raw = null;
		Scrib_mod mod = engine.Mods_get(mod_name);
		if (mod == null) {
			Xow_ns module_ns = wiki.Ns_mgr().Ids_get_or_null(Scrib_core_.Ns_id_module);
			Xoa_ttl mod_ttl = Xoa_ttl.parse_(wiki, ByteAry_.Add(module_ns.Name_db_w_colon(), mod_name));
			mod_raw = wiki.Cache_mgr().Page_cache().Get_or_load_as_src(mod_ttl);
			if (mod_raw == null) {Error(bfr, wiki.Msg_mgr(), Err_mod_missing); return;} // EX: "{{#invoke:missing_mod}}"
		}
		else
			mod_raw = mod.Text_bry();
		if (!engine.Enabled()) {bfr.Add_mid(src, self.Src_bgn(), self.Src_end()); return;}
		try {
			engine.Invoke(wiki, ctx, src, caller, self, bfr, mod_name, mod_raw, fnc_name);
			if (invoke_wkr != null)
				invoke_wkr.Eval_end(ctx.Page(), mod_name, fnc_name, log_time_bgn);
		}
		catch (Exception e) {
			bfr.Add_mid(src, self.Src_bgn(), self.Src_end());
			bfr.Add(Xoh_consts.Comm_bgn).Add_str(Err_.Message_gplx_brief(e)).Add(Xoh_consts.Comm_end);
			ctx.App().Usr_dlg().Warn_many("", "", "invoke failed: ~{0} ~{1} ~{2}", String_.new_utf8_(ctx.Page().Ttl().Raw()), String_.new_utf8_(src, self.Src_bgn(), self.Src_end()), Err_.Message_gplx_brief(e));
			Scrib_engine.Engine_invalidate();	// reset engine
		}
	}
	public static void Error(ByteAryBfr bfr, Xow_msg_mgr msg_mgr, String error) {Error(bfr, msg_mgr, ByteAry_.new_utf8_(error));}
	public static void Error(ByteAryBfr bfr, Xow_msg_mgr msg_mgr, byte[] error) {
		ByteAryFmtr fmtr = ByteAryFmtr.new_("<strong class=\"error\"><span class=\"scribunto-error\" id=\"mw-scribunto-error-0\">~{0}: ~{1}.</span></strong>");	// <!--~{0}: ~{1}.-->
		byte[] script_error_msg = msg_mgr.Val_by_id(Xol_msg_itm_.Id_scribunto_parser_error);
		fmtr.Bld_bfr_many(bfr, script_error_msg, error);
	}
	public static final String Err_mod_missing = "No such module";
}

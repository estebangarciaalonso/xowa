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
package gplx.xowa.fmtrs; import gplx.*; import gplx.xowa.*;
public class Xoa_fmtr_itm implements GfoInvkAble {
	public Xoa_fmtr_itm(Xoa_app app) {this.app = app;} private Xoa_app app;
	public String Src() {return src;} public Xoa_fmtr_itm Src_(String v) {this.src = v; return this;} private String src;
	public byte[] Fmt() {return fmt;} public Xoa_fmtr_itm Fmt_(byte[] v) {this.fmt = v; return this;} private byte[] fmt;
	public String Run() {
//		GfsCtx ctx = GfsCtx.new_().Fail_if_unhandled_(Xoa_gfs_mgr.Fail_if_unhandled).Usr_dlg_(app.Usr_dlg());
		GfoInvkAble src_invk = (GfoInvkAble)app.Gfs_mgr().Run_str(src);
		int len = Int_.cast_(GfoInvkAble_.InvkCmd(src_invk, "len"));
		ByteAryBfr bfr = ByteAryBfr.new_();
		Bfmtr_eval_invk eval_mgr = new Bfmtr_eval_invk(app);
		ByteAryFmtr fmtr = ByteAryFmtr.new_bry_(fmt).Eval_mgr_(eval_mgr);  
		for (int i = 0; i < len; i++) {
			GfoInvkAble itm_invk = (GfoInvkAble)GfoInvkAble_.InvkCmd_val(src_invk, "get_at", i);
			eval_mgr.Invk_(itm_invk);
			fmtr.Bld_bfr(bfr, ByteAry_.Ary_empty);
		}
		return bfr.XtoStrAndClear();
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_src)) 		return src;
		else if	(ctx.Match(k, Invk_src_)) 		src = m.ReadStr("v"); 
		else if	(ctx.Match(k, Invk_fmt)) 		return String_.new_utf8_(fmt);
		else if	(ctx.Match(k, Invk_fmt_)) 		fmt = m.ReadBry("v"); 
		else if	(ctx.Match(k, Invk_run)) 		return Run(); 
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String Invk_src = "src", Invk_src_ = "src_", Invk_fmt = "fmt", Invk_fmt_ = "fmt_", Invk_run = "run";
}
class Bfmtr_eval_invk implements ByteAryFmtr_eval_mgr {
	public Bfmtr_eval_invk(Xoa_app app) {this.app = app;} private Xoa_app app;
	public Bfmtr_eval_invk Invk_(GfoInvkAble invk) {this.invk = invk; return this;} private GfoInvkAble invk;
	public boolean Enabled() {return enabled;} public void Enabled_(boolean v) {enabled = v;} private boolean enabled = true;
	public byte[] Eval(byte[] cmd) {
		Object rslt = app.Gfs_mgr().Run_str_for(invk, String_.new_utf8_(cmd));
		return ByteAry_.new_utf8_(Object_.XtoStr_OrNullStr(rslt));
	}
}

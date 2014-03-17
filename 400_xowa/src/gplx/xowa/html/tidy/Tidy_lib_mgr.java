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
package gplx.xowa.html.tidy; import gplx.*; import gplx.xowa.*; import gplx.xowa.html.*;
import gplx.xowa.apps.*;
public class Tidy_lib_mgr implements GfoInvkAble {
	private Xoa_app app;
	public Tidy_lib_mgr(Xoa_app app) {
		this.app = app;
	}
	public void Init_by_app() {
		Apps_app_mgr_eval cmd_eval = app.Url_cmd_eval();
		ProcessAdp.ini_(this, app.Gui_wtr(), lib, cmd_eval, ProcessAdp.Run_mode_sync_timeout, 1 * 60, "~{<>bin_plat_dir<>}tidy" + Op_sys.Cur().Fsys_dir_spr_str() +  "tidy", Tidy_lib_process.Args_fmt, "source", "target");
	}
	public boolean Enabled() {return enabled;} private boolean enabled;
	public Tidy_lib_process Lib() {return lib;} private Tidy_lib_process lib = new Tidy_lib_process();
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_enabled))			return Yn.X_to_str(enabled);
		else if	(ctx.Match(k, Invk_enabled_))			enabled = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_enabled_toggle))		enabled = !enabled;
		else if	(ctx.Match(k, Invk_lib))				return lib;
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String
	  Invk_enabled = "enabled", Invk_enabled_ = "enabled_", Invk_enabled_toggle = "enabled_toggle"
	, Invk_lib = "lib"
	;
}

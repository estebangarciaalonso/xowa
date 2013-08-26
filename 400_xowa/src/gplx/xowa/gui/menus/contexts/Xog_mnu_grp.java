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
package gplx.xowa.gui.menus.contexts; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*; import gplx.xowa.gui.menus.*;
import gplx.gfui.*;
public class Xog_mnu_grp extends Xog_mnu_base {
	public Xog_mnu_grp(Xoa_app app, boolean mnu_is_popup) {
		this.app = app; this.mnu_is_popup = mnu_is_popup;
	}	private Xoa_app app; private boolean mnu_is_popup;
	private Gfui_mnu_grp gui_mnu; 
	public boolean Enabled() {return enabled;} private boolean enabled = true;
	public void Enabled_(boolean v) {
		this.enabled = v;
		if (gui_mnu != null)		// null when changed by cfg.gfs
			gui_mnu.Itms_clear();
		if (v)
			this.Source_exec(app.Gfs_mgr());
		else 
			this.Clear();
		if (!mnu_is_popup && app.Gui_mgr().Main_win().Win() != null)
			GfoInvkAble_.InvkCmd(app.Gui_mgr().Main_win(), gplx.gfui.Gfui_html.Evt_win_resized);
	}
	public String Source() {return source;} private String source;
	public String Source_default() {return source_default;} public Xog_mnu_grp Source_default_(String v) {source_default = source = v; return this;} private String source_default;
	private Xog_mnu_grp Source_(Xoa_gfs_mgr gfs_mgr, String v) {
		Object rslt = Source_exec(gfs_mgr, v);
		if (rslt != GfoInvkAble_.Rv_error)
			source = v;
		return this;
	} 
	public Object Source_exec(Xoa_gfs_mgr gfs_mgr) {return Source_exec(gfs_mgr, source);}
	private Object Source_exec(Xoa_gfs_mgr gfs_mgr, String v) {
		if (!enabled) return GfoInvkAble_.Rv_handled;
		String script = "clear;\n" + v + "build;";
		return gfs_mgr.Run_str_for(this, script);
	}
	public void Build() {
		Xoa_gui_mgr gui_mgr = app.Gui_mgr();
		if (!gui_mgr.Kit().Kit_init_done()) return;	// NOTE: .gfs will fire Build before Kit.Init; check that kit is inited
		if (gui_mnu == null) {
			if (mnu_is_popup)
				gui_mnu = gui_mgr.Kit().New_mnu_popup(gui_mgr.Main_win().Html_box());
			else
				gui_mnu = gui_mgr.Kit().New_mnu_bar(gui_mgr.Main_win().Win());
		}
		app.Gui_mgr().Menu_mgr().Menu_bldr().Build(gui_mnu, this);
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_build))					this.Build();
		else if	(ctx.Match(k, Invk_enabled))				return Yn.XtoStr(enabled);
		else if	(ctx.Match(k, Invk_enabled_))				this.Enabled_(m.ReadYn("v"));
		else if	(ctx.Match(k, Invk_source))					return source;
		else if	(ctx.Match(k, Invk_source_))				this.Source_(app.Gfs_mgr(), m.ReadStr("v"));
		else if	(ctx.Match(k, Invk_source_default))			return source_default;
		else if	(ctx.Match(k, Invk_source_default_))		source_default = m.ReadStr("v");
		else	return super.Invk(ctx, ikey, k, m);
		return this;
	}	private static final String Invk_build = "build", Invk_enabled = "enabled", Invk_enabled_ = "enabled_", Invk_source = "source", Invk_source_ = "source_", Invk_source_default = "source_default", Invk_source_default_ = "source_default_";
}

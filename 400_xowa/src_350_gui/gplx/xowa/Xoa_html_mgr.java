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
public class Xoa_html_mgr implements GfoInvkAble {
	public Xoa_html_mgr(Xoa_app app) {this.app = app; portal_mgr = new Xoh_portal_mgr(app);} private Xoa_app app;
	public Xoh_portal_mgr Portal_mgr() {return portal_mgr;} private Xoh_portal_mgr portal_mgr;
	public boolean Javascript_enabled() {return javascript_enabled;} private boolean javascript_enabled = true;
	void Javascript_enabled_(boolean v) {
		javascript_enabled = v;
		if (app.Gui_mgr().Main_win().Html_box() != null) {	// NOTE: Html_box may be null during Prefs_convert; will be called again when gui_mgr launches (for which member variable must be set)
			gplx.gfui.GfuiInvkCmd invk_cmd = app.Gui_mgr().Kit().New_cmd_sync(app.Gui_mgr().Main_win().Html_box());
			invk_cmd.Invk(GfsCtx._, 0, Invk_javascript_enabled_, GfoMsg_.new_cast_(Invk_javascript_enabled_).Add("v", v));
		}
	}
	public String Auto_focus_id() {return auto_focus_id;} private String auto_focus_id = "";
	public byte[] Css_xtn() {return css_xtn;} public Xoa_html_mgr Css_xtn_(byte[] v) {css_xtn = v; return this;} private byte[] css_xtn = ByteAry_.Empty;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_javascript_enabled))			return Yn.XtoStr(javascript_enabled);
		else if	(ctx.Match(k, Invk_javascript_enabled_))		Javascript_enabled_(m.ReadYn("v"));
		else if	(ctx.Match(k, Invk_auto_focus_id_))				auto_focus_id = m.ReadStr("v");
		else if	(ctx.Match(k, Invk_css_xtn))					return css_xtn;
		else if	(ctx.Match(k, Invk_css_xtn_))					css_xtn = m.ReadBry("v");
		else if	(ctx.Match(k, Invk_portal))						return portal_mgr;
		else return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_javascript_enabled = "javascript_enabled", Invk_javascript_enabled_ = "javascript_enabled_", Invk_auto_focus_id_ = "auto_focus_id_", Invk_css_xtn = "css_xtn", Invk_css_xtn_ = "css_xtn_", Invk_portal = "portal";
}

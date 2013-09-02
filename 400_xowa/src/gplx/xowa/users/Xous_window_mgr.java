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
package gplx.xowa.users; import gplx.*; import gplx.xowa.*;
public class Xous_window_mgr implements GfoInvkAble {
	public Xous_window_mgr(Xou_user user) {
		this.user = user;
		gplx.gfui.SizeAdp size = Xouc_window_mgr.Screen_maximized_calc();
		rect = new Rect_ref(0, 0, size.Width(), size.Height());
	}
	public Xou_user User() {return user;} private Xou_user user;
	public Rect_ref Rect() {return rect;} Rect_ref rect;
	public boolean Maximized() {return maximized;} private boolean maximized = false;
	public void Save_window(gplx.gfui.GfuiWin win) {
		gplx.xowa.cfgs.Xoa_cfg_mgr cfg_mgr = user.App().Cfg_mgr();
		if (user.Cfg_mgr().Startup_mgr().Window_mgr().Mode_tid() == Xouc_window_mgr.Mode_tid_previous) {
			cfg_mgr.Set_by_app("app.user.session.window.maximized"	, Yn.XtoStr(win.Maximized()));
			cfg_mgr.Set_by_app("app.user.session.window.rect"		, win.Rect().Xto_str());
		}
		cfg_mgr.Set_by_app("app.gui.html.portal.wikis.visible"	, Yn.XtoStr(user.App().Gui_mgr().Html_mgr().Portal_mgr().Wikis().Visible()));
		cfg_mgr.Db_save_txt();
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_rect))					return rect;
		else if	(ctx.Match(k, Invk_rect_))					rect = Rect_ref.parse_(m.ReadStr("v"));
		else if	(ctx.Match(k, Invk_maximized))				return Yn.XtoStr(maximized);
		else if	(ctx.Match(k, Invk_maximized_))				maximized = m.ReadYn("v");
		return this;
	}	public static final String Invk_rect = "rect", Invk_rect_ = "rect_", Invk_maximized = "maximized", Invk_maximized_ = "maximized_";
}

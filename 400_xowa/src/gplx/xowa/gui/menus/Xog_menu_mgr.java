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
package gplx.xowa.gui.menus; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
import gplx.xowa.gui.menus.contexts.*;
public class Xog_menu_mgr implements GfoInvkAble {
	public Xog_menu_mgr(Xoa_app app) {
		menu_bldr = new Xog_mnu_bldr();
		context_mnu_mgr = new Xog_popup_mnu_mgr(app, this);
		window_mnu_mgr = new Xog_window_mnu_mgr(app, this);
	}
	public Xog_popup_mnu_mgr Context_mnu_mgr() {return context_mnu_mgr;} private Xog_popup_mnu_mgr context_mnu_mgr;
	public Xog_window_mnu_mgr Window_mnu_mgr() {return window_mnu_mgr;} private Xog_window_mnu_mgr window_mnu_mgr;
	public Xog_mnu_bldr Menu_bldr() {return menu_bldr;} private Xog_mnu_bldr menu_bldr;
	public void Init_by_kit() {
		context_mnu_mgr.Init_by_kit();
		window_mnu_mgr.Init_by_kit();
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_contexts))			return context_mnu_mgr;
		else if	(ctx.Match(k, Invk_windows))			return window_mnu_mgr;
		else	return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_contexts = "contexts", Invk_windows = "windows";
}

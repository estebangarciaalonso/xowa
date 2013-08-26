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
public class Xog_popup_mnu_mgr implements GfoInvkAble {
	private OrderedHash hash = OrderedHash_.new_();
	public Xog_popup_mnu_mgr(Xoa_app app, Xog_menu_mgr menu_mgr) {
		this.app = app;
		Get_or_new("html_box").Source_default_(Xog_mnu_defaults.Html_box_mnu);	// NOTE: set default here (fires before cfg)
	}	private Xoa_app app;
	public void Init_by_kit() {
		Xog_mnu_grp mnu = Get_or_new("html_box");	// NOTE: build menu now; NOTE: do not set default here, or else will override user setting
		mnu.Source_exec(app.Gfs_mgr());
	}
	public Xog_mnu_grp Get_or_new(String key) {			
		Xog_mnu_grp rv = (Xog_mnu_grp)hash.Fetch(key);
		if (rv == null) {
			rv = new Xog_mnu_grp(app, true);
			hash.Add(key, rv);
		}
		return rv;
	}	
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_get))		return Get_or_new(m.ReadStr("v"));
		else	return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_get = "get";		
}

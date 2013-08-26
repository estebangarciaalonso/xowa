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
package gplx.xowa.users; import gplx.*; import gplx.xowa.*;
public class Xouc_startup_mgr implements GfoInvkAble {
	public Xouc_startup_mgr(Xou_cfg config) {this.config = config; window_mgr = new Xouc_window_mgr(config.User());} private Xou_cfg config;
	public Xouc_window_mgr Window_mgr() {return window_mgr;} private Xouc_window_mgr window_mgr;
	public String Page() {return page;} public Xouc_startup_mgr Page_(String v) {page = v; return this;} private String page = "xowa";
	public String Page_url() {
		byte v = Page_parse(page);
		switch (v) {
			case Page_home: return config.Pages_mgr().Home();
			case Page_last: return String_.new_utf8_(config.User().App().Gui_mgr().Main_win().Page().Page_ttl().Raw());
			case Page_xowa: return Xouc_pages_mgr.Page_xowa;
			default: throw Err_.unhandled(page);
		}
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_page))			return page;
		else if	(ctx.Match(k, Invk_page_))			page = m.ReadStr("v");
		else if	(ctx.Match(k, Invk_window))			return window_mgr;
		return this;
	}	public static final String Invk_page = "page", Invk_page_ = "page_", Invk_window = "window";
	byte Page_parse(String s) {
		if		(String_.Eq(s, "home")) return Page_home;
		else if	(String_.Eq(s, "last")) return Page_last;
		else if	(String_.Eq(s, "xowa")) return Page_xowa;
		else							return Page_xowa;
	}
	public static final byte Page_home = 0, Page_last = 1, Page_xowa = 2;
}

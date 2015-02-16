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
package gplx.xowa.apis.xowa; import gplx.*; import gplx.xowa.*; import gplx.xowa.apis.*;
import gplx.xowa.apis.xowa.gui.*;
public class Xoapi_gui implements GfoInvkAble {
	public void Init_by_kit(Xoa_app app) {
		browser.Init_by_kit(app);
		font.Init_by_kit(app);
		page.Init_by_kit(app);
	}
	public Xoapi_browser	Browser() {return browser;} private Xoapi_browser browser = new Xoapi_browser();
	public Xoapi_font		Font() {return font;} private Xoapi_font font = new Xoapi_font();
	public Xoapi_page		Page() {return page;} private Xoapi_page page = new Xoapi_page();
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_browser)) 					return browser;
		else if	(ctx.Match(k, Invk_font))	 					return font;
		else if	(ctx.Match(k, Invk_page)) 						return page;
		return this;
	}
	private static final String Invk_browser = "browser", Invk_font = "font", Invk_page = "page";
}

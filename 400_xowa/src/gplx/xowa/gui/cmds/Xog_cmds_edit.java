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
package gplx.xowa.gui.cmds; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
public class Xog_cmds_edit implements GfoInvkAble {
	private Xog_win win; private Xoa_app app;
	public void Init_by_kit(Xoa_app app) {
		this.app = app;
		win = app.Gui_mgr().Main_win();
	}
	public Object Copy() {
		win.Win().Kit().Clipboard().Copy(Html_doc_selected_get());
		return this;
	}
	public Object Select_all() {
		GfoInvkAble_.InvkCmd(win.Win().Kit().Clipboard(), gplx.gfui.Gfui_clipboard_.Invk_select_all);
		return this;
	}
	public Object Find() {
		app.Gui_mgr().Layout().Find_show();
		win.Find_box().Text_(Html_doc_selected_get());
		return this;
	}
	String Html_doc_selected_get() {
		Xoa_page page = win.Page();
		return win.Html_box().Html_doc_selected_get(page.Wiki().Key_str(), String_.new_utf8_(page.Page_ttl().Page_txt()));
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_copy)) 			this.Copy();
		else if	(ctx.Match(k, Invk_select_all)) 	this.Select_all();
		else if	(ctx.Match(k, Invk_find)) 			this.Find();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_copy = "copy", Invk_select_all = "select_all", Invk_find = "find";	
}

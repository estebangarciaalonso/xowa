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
package gplx.xowa.gui.cmds; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
public class Xog_cmds_file implements GfoInvkAble {
	public void Init_by_kit(Xoa_app app) {this.app = app;} private Xoa_app app;
	public void Print() {
		gplx.gfui.Gfui_html html_box = app.Gui_mgr().Main_win().Html_box();
		html_box.Html_window_print_preview();
	}
	public void Save_as() {
		gplx.gfui.Gfui_html html_box = app.Gui_mgr().Main_win().Html_box();
		Xoa_page page = app.Gui_mgr().Main_win().Page();
		String file_name = app.Url_converter_fsys_safe().Encode_str(String_.new_utf8_(page.Page_ttl().Full_url())) + ".html";			
		String file_url = app.Gui_mgr().Kit().New_dlg_file("Select file to save to:").Init_file_(file_name).Ask();
		if (String_.Len_eq_0(file_url)) return;
		Io_mgr._.SaveFilStr(file_url, html_box.Text());
		app.Usr_dlg().Prog_many("", "", "saved page: file=~{0}", file_url);
	}
	public void Exit() {
		app.Gui_mgr().Main_win().Exec_exit();
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_print)) 		this.Print();
		else if	(ctx.Match(k, Invk_save_as)) 	this.Save_as();
		else if	(ctx.Match(k, Invk_exit)) 		this.Exit();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_print = "print", Invk_save_as = "save_as", Invk_exit = "exit";
}

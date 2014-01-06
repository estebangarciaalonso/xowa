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
import gplx.xowa.gui.history.*;
public class Xog_cmds_bookmarks implements GfoInvkAble {
	private Xog_win win;
	public void Init_by_kit(Xoa_app app) {
		win = app.Gui_mgr().Main_win();
	}
	public void Goto(String page)	{win.Exec_url_exec(page);}
	public void Goto_main_page()	{win.Exec_url_exec(win.Page().Wiki().Domain_str());}
	public void Add()				{win.Exec_bookmarks_add();}
	public void Show()				{win.Exec_url_exec("home/wiki/Data:Bookmarks");}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_goto)) 					this.Goto(m.ReadStr("v"));
		else if	(ctx.Match(k, Invk_goto_main_page)) 		this.Goto_main_page();
		else if	(ctx.Match(k, Invk_add)) 					this.Add();
		else if	(ctx.Match(k, Invk_show)) 					this.Show();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_goto = "goto", Invk_goto_main_page = "goto_main_page", Invk_add = "add", Invk_show = "show";
}

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
public class Xog_cmds_history implements GfoInvkAble {
	private Xog_win win;
	private Xog_history_mgr history_mgr;
	public void Init_by_kit(Xoa_app app) {
		win = app.Gui_mgr().Main_win();
		history_mgr = win.History_mgr();
	}
	public void Go_bwd()			{win.Exec_page_stack(history_mgr.Go_bwd(win.Page().Wiki()));}
	public void Go_fwd()			{win.Exec_page_stack(history_mgr.Go_fwd(win.Page().Wiki()));}
	public void Show()				{win.Exec_url_exec("home/wiki/Special:XowaPageHistory");}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_go_bwd)) 				this.Go_bwd();
		else if	(ctx.Match(k, Invk_go_fwd)) 				this.Go_fwd();
		else if	(ctx.Match(k, Invk_show)) 					this.Show();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_go_bwd = "go_bwd", Invk_go_fwd = "go_fwd", Invk_show = "show";
}

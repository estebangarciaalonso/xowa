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
public class Xog_cmd_mgr implements GfoInvkAble {
	public void Init_by_kit(Xoa_app app) {
		cmds_file.Init_by_kit(app);
		cmds_edit.Init_by_kit(app);
		cmds_view.Init_by_kit(app);
		cmds_history.Init_by_kit(app);
		cmds_bookmarks.Init_by_kit(app);
	}
	public Xog_cmds_file Cmds_file() {return cmds_file;} private Xog_cmds_file cmds_file = new Xog_cmds_file();
	public Xog_cmds_edit Cmds_edit() {return cmds_edit;} private Xog_cmds_edit cmds_edit = new Xog_cmds_edit();
	public Xog_cmds_view Cmds_view() {return cmds_view;} private Xog_cmds_view cmds_view = new Xog_cmds_view();
	public Xog_cmds_history Cmds_history() {return cmds_history;} private Xog_cmds_history cmds_history = new Xog_cmds_history();
	public Xog_cmds_bookmarks Cmds_bookmarks() {return cmds_bookmarks;} private Xog_cmds_bookmarks cmds_bookmarks = new Xog_cmds_bookmarks();
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_file)) 		return cmds_file;
		else if	(ctx.Match(k, Invk_edit)) 		return cmds_edit;
		else if	(ctx.Match(k, Invk_view)) 		return cmds_view;
		else if	(ctx.Match(k, Invk_history)) 	return cmds_history;
		else if	(ctx.Match(k, Invk_bookmarks)) 	return cmds_bookmarks;
		return this;
	}	private static final String Invk_file = "file", Invk_edit = "edit", Invk_view = "view", Invk_history = "history", Invk_bookmarks = "bookmarks";
}

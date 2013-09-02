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
package gplx.xowa; import gplx.*;
public class Xoa_cur implements GfoInvkAble {
	public Xoa_cur(Xoa_app app) {this.app = app;} private Xoa_app app;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_wiki))			return app.Gui_mgr().Main_win().Page().Wiki();
		else if	(ctx.Match(k, Invk_win))			return app.Gui_mgr().Main_win();
		else if	(ctx.Match(k, Invk_user))			return app.User();
		else return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_wiki = "wiki", Invk_win = "win", Invk_user = "user";
}

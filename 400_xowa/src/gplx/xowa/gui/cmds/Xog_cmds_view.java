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
public class Xog_cmds_view implements GfoInvkAble {
	private Xoa_app app;
	public void Init_by_kit(Xoa_app app) {
		this.app = app;
	}
	public void Font_increase() {Font_adj(1);}
	public void Font_decrease() {Font_adj(-1);}
	private void Font_adj(int v) {
		float html_font_size = app.Html_mgr().Page_mgr().Font_size() + v;
		if (html_font_size <= 0) return;	// font must be positive
		app.Html_mgr().Page_mgr().Font_enabled_(true);
		app.Html_mgr().Page_mgr().Font_size_(html_font_size);
		app.Cfg_mgr().Set_by_app("app.html.page.font_enabled", "y");
		app.Cfg_mgr().Set_by_app("app.html.page.font_size", Float_.XtoStr(app.Html_mgr().Page_mgr().Font_size()));
		float gui_font_size = app.Gui_mgr().Win_opts().Font().Size() + v; // (html_font_size * .75f) - 4;	// .75f b/c 16px = 12 pt; -4 b/c gui font is currently 4 pt smaller 
		app.Gui_mgr().Win_opts().Font().Size_(gui_font_size);
		app.Cfg_mgr().Set_by_app("app.gui.win_opts.font.size", Float_.XtoStr(gui_font_size));
		app.Cfg_mgr().Db_save_txt();
		app.Gui_mgr().Main_win().Exec_page_reload();	// NOTE: force reload; needed if viewing Help:Options/HTML, else Font size won't update
	}
	public void Page_read()		{Page(Xoh_wiki_article.Tid_view_read);}
	public void Page_edit()		{Page(Xoh_wiki_article.Tid_view_edit);}
	public void Page_html()		{Page(Xoh_wiki_article.Tid_view_html);}
	private void Page(byte v)	{app.Gui_mgr().Main_win().Exec_page_view(v);}
	public void Reload()		{app.Gui_mgr().Main_win().Exec_page_reload();}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_font_increase)) 		this.Font_increase();
		else if	(ctx.Match(k, Invk_font_decrease)) 		this.Font_decrease();
		else if	(ctx.Match(k, Invk_page_read)) 			this.Page_read();
		else if	(ctx.Match(k, Invk_page_edit)) 			this.Page_edit();
		else if	(ctx.Match(k, Invk_page_html)) 			this.Page_html();
		else if	(ctx.Match(k, Invk_reload)) 			this.Reload();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String
	  Invk_font_increase = "font_increase", Invk_font_decrease = "font_decrease"
	, Invk_page_read = "page_read", Invk_page_edit = "page_edit", Invk_page_html = "page_html"
	, Invk_reload = "reload"
	;
}

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
import gplx.gfui.*; import gplx.xowa.gui.history.*;
public abstract class Xog_win_view_base implements GfoInvkAble {
	Xoa_app app;
	public void Ctor(Xoa_app app) {this.app = app;}
	byte cur_view_tid = 0;
	public abstract Xog_history_mgr History_mgr();
	public abstract Gfui_box_win Win_box();
	public abstract Xog_box_html Html_box();
	public abstract Gfui_box_text Url_box();
	public abstract Gfui_box_text Find_box();
	public abstract Gfui_box_text Prog_box(); 
	public abstract Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m);
	public abstract Xoa_page Page();
	public abstract void Page_(Xoa_page page);
	public void Launch() {
		Xow_wiki home_wiki = app.User().Wiki();
		Xoa_page launch_page = Launch_page(home_wiki, app.Sys_cfg().Launch_url());
		if (launch_page.Missing())
			//launch_page = Launch_page(home_wiki, app.User().Cfg_mgr().Startup_mgr().Page_url());
			launch_page = Launch_page(home_wiki, Xoa_sys_cfg.Launch_url_dflt);
		Show_page(launch_page);
		this.History_mgr().Add(launch_page);
		this.Html_box().Focus(); //this.Html_box().Html_doc_body_focus();	// focus the html_box so wheel scroll works; DATE:2013-02-08
		app.Gui_wtr().Prog_none(GRP_KEY, "launch.done", "");
	}
	Xoa_page Launch_page(Xow_wiki home_wiki, String launch_str) {
		Xoa_url launch_url = Xoa_url_parser.Parse_url(app, home_wiki, launch_str);
		Xow_wiki launch_wiki = launch_url.Wiki();
		Xoa_ttl launch_ttl = Xoa_ttl.parse_(launch_wiki, launch_url.Page_bry());
		Page_(Xoa_page.blank_page_(home_wiki, launch_ttl));	// set to blank page
		return launch_wiki.GetPageByTtl(launch_url, launch_ttl).Url_(launch_url);	// FUTURE: .Url_() should not be called (needed for anchor); EX: en.wikipedia.org/Earth#Biosphere
	}
	private void Show_page(Xoa_page new_page) {
//			if (reset_to_read) cur_view_tid = Xoh_wiki_article.Tid_view_read;
//			if (new_page.Url().Action_is_edit()) cur_view_tid = Xoh_wiki_article.Tid_view_edit;
//			if (page != null) page.DocPos_(html_box.Html_window_vpos());
//			status_box_wtr.Note("locating images");			
		Page_(new_page);
		byte[] bry = new_page.Wiki().Html_mgr().Output_mgr().Gen(new_page, cur_view_tid);
		this.Html_box().Text_(String_.new_utf8_(bry));
		this.Url_box().Text_(app.Url_parser().Build_str(new_page.Url()));
		this.Win_box().Text_(String_.new_utf8_(ByteAry_.Add(new_page.Page_ttl().Page_txt(), Xog_win.Xowa_titleBar_suffix)));
		Sync_font_if_needed(new_page.Lang().Gui_font());
		this.Html_box().Focus();	// focus the html_box; applies mainly to urls entered from the address box;
		if (cur_view_tid == Xoh_wiki_article.Tid_view_read)
			this.Exec_async(Xog_win.Invk_html_box_focus_previous_or_firstHeading);
		if (new_page.Url().Anchor_bry() != null)
			app.Gui_mgr().Main_win().Exec_async_arg(Xog_win.Invk_html_box_select_by_id, new_page.Url().Anchor_str());
		app.Gui_mgr().Main_win().Exec_reload_imgs();
	}
	public abstract void Exec_async(String cmd);
	public abstract void Exec_sync(String cmd);
	private void Sync_font_if_needed(Xol_font_info wiki_font) {
		FontAdp gui_font = this.Url_box().Font();
		if (wiki_font.Name() != null && wiki_font.Eq(gui_font)) {
			FontAdp new_font = FontAdp.new_(wiki_font.Name(), wiki_font.Size(), gplx.gfui.FontStyleAdp_.Plain);
			this.Url_box().Font_(new_font);
			this.Find_box().Font_(new_font);
			this.Prog_box().Font_(new_font);
		}
	}
	static final String GRP_KEY = "xowa.win_view_base";
} 

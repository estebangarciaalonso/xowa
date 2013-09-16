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
import gplx.xowa.gui.history.*;
public class Xog_win_view_mok extends Xog_win_view_base implements Xog_win_view {
	public Xog_win_view_mok(Xoa_app app) {
		this.Ctor(app);
		win_box = new Gfui_box_win_mok("main_win");
		url_box = new Gfui_box_text_mok(win_box, "url_box");
		html_box = new Gfui_box_html_mok(win_box, "html_box");
		find_box = new Gfui_box_text_mok(win_box, "find_box");
		prog_box = new Gfui_box_text_mok(win_box, "prog_box");
	}
	@Override public Xoa_page Page() {return page;} @Override public void Page_(Xoa_page v) {page = v;} private Xoa_page page;
	@Override public Xog_history_mgr History_mgr() {return history_mgr;} private Xog_history_mgr history_mgr = new Xog_history_mgr();
	@Override public Gfui_box_win Win_box()		{return win_box;} Gfui_box_win win_box;
	@Override public Gfui_box_text Url_box() 	{return url_box;} Gfui_box_text_mok url_box;
	@Override public Xog_box_html Html_box()	{return html_box;} Gfui_box_html_mok html_box;
	@Override public Gfui_box_text Find_box()	{return find_box;} Gfui_box_text_mok find_box;
	@Override public Gfui_box_text Prog_box()	{return prog_box;} Gfui_box_text_mok prog_box;
	@Override public void Exec_async(String cmd) {GfoInvkAble_.InvkCmd(this, cmd);}
	@Override public void Exec_sync(String cmd) {GfoInvkAble_.InvkCmd(this, cmd);}
	public void Html_box_focus_previous_or_firstHeading() {
//		if (page.DocPos() == null)
//			html_box.Html_elem_focus(Xog_win.Id_firstHeading);
//		else
//			html_box.Html_window_vpos_(page.DocPos());
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_link_click))								{}
		else if (ctx.Match(k, Xog_win.Invk_html_box_focus))						Html_box().Focus();
		else if (ctx.Match(k, Invk_html_box_focus_previous_or_firstHeading))	Html_box_focus_previous_or_firstHeading();
		else																	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String 
	Invk_link_click = "link_click", Invk_link_print = "link_print"
	, Invk_wiki_special_random = "wiki_special_random", Invk_page_refresh = "page_refresh", Invk_page_reload_imgs = "page_reload_imgs"
	, Invk_page_view_edit = "page_view_edit", Invk_page_view_read = "page_view_read", Invk_page_view_html = "page_view_html", Invk_page_goto = "page_goto", Invk_page_goto_recent = "page_goto_recent"
	, Invk_page_edit_save = "page_edit_save", Invk_page_edit_save_draft = "page_edit_save_draft", Invk_page_edit_preview = "page_edit_preview", Invk_page_edit_rename = "page_edit_rename", Invk_page_dbg_wiki = "page_dbg_wiki", Invk_page_dbg_html = "page_dbg_html"
	, Invk_history_fwd = "history_fwd", Invk_history_bwd = "history_bwd"
	, Invk_htmlBox_select_by_id = "htmlBox_select_by_id", Invk_page_edit_focus_box = "Invk_page_edit_focus_box", Invk_page_edit_focus_first = "Invk_page_edit_focus_first", Invk_html_box_focus_previous_or_firstHeading = "html_box_focus_previous_or_firstHeading", Invk_htmlBox_focus = "htmlBox_focus", Invk_prog_box_focus = "prog_box_focus"
	, Invk_urlBox_exec = "urlBox_exec", Invk_urlBox_focus = "urlBox_focus", Invk_urlBox_paste_and_go = "urlBox_paste_and_go"
	, Invk_findBox_focus = "findBox_focus", Invk_findBox_find = "Exec_findBox_find", Invk_findBox_dir_fwd = "findBox_dir_fwd", Invk_findBox_dir_bwd = "findBox_dir_bwd", Invk_findBox_case_toggle = "findBox_case_toggle", Invk_findBox_wrap_toggle = "findBox_wrap_toggle"
	, Invk_app_exit = "app_exit", Invk_app_history_show = "app_history_show", Invk_app_exec_cfg = "app_exec_cfg"
	, Invk_app_bookmarks_add = "app_bookmarks_add"
	, Invk_shortcuts = "shortcuts"
	, Invk_eval = "eval"
	;
}

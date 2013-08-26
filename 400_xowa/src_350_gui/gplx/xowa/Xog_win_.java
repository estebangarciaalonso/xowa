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
package gplx.xowa; import gplx.*;
import gplx.gfui.*;
class Xog_win_ {
	public static void Init_drd(Xog_win tab) {
		GfuiTextBox url_box = tab.Url_box(); GfuiElem go_bwd_btn = tab.Go_bwd_btn(), go_fwd_btn = tab.Go_fwd_btn();
		IptCfg null_cfg = IptCfg_.Null;
		IptBnd_.cmd_to_(null_cfg		, url_box	, tab, Xog_win.Invk_url_box_exec			, IptKey_.add_(IptKey_.Enter));
		IptBnd_.cmd_to_(null_cfg		, go_bwd_btn, tab, Xog_win.Invk_history_bwd				, IptMouseBtn_.Left);
		IptBnd_.cmd_to_(null_cfg		, go_fwd_btn, tab, Xog_win.Invk_history_fwd				, IptMouseBtn_.Left);
	}
	public static void Init_desktop(Xog_win tab) {
		Xoa_app app = tab.App();
		GfuiWin win = tab.Win(); Gfui_html html_box = tab.Html_box(); GfuiTextBox find_box = tab.Find_box(), url_box = tab.Url_box(), search_box = tab.Search_box();
		GfuiTextBox info_box = tab.Info_box(), prog_box = tab.Prog_box();
		GfuiElem go_bwd_btn = tab.Go_bwd_btn(), go_fwd_btn = tab.Go_fwd_btn();
		win.BackColor_(ColorAdp_.White);

		app.User().Cfg_mgr().Startup_mgr().Window_mgr().Init_window(win);
//			win.Rect_set(app.Gui_mgr().Window().Rect());
		win.Focus_able_(false);
//			Xog_win.Exec_win_resize_elem(app.Gui_mgr().Layout().Main_win(), win, Rect_ref.rectAdp_(win.Rect()), null, Xog_win.Layout_init);
		tab.Resizer().Exec_win_resize(app, win.Width(), win.Height());

		IconAdp.regy_loadDir_shallow(app.User().Fsys_mgr().Root_dir().GenSubDir_nest("app", "img", "win"));
		win.Icon_(IconAdp.regy_("xowa.app"));

		IptCfg window_cfg = app.Gui_mgr().Ipt_cfgs().GetOrNew("browser.window");
		IptCfg wikiBox_cfg = app.Gui_mgr().Ipt_cfgs().GetOrNew("browser.html_box");
		IptCfg null_cfg = IptCfg_.Null;

		IptBnd_.ipt_to_(null_cfg		, go_bwd_btn, tab, Xog_win.Invk_history_bwd				, IptEventType_.add_(IptEventType_.MouseUp, IptEventType_.KeyDown), IptMouseBtn_.Left, IptKey_.Enter, IptKey_.Space);
		IptBnd_.ipt_to_(null_cfg		, go_fwd_btn, tab, Xog_win.Invk_history_fwd				, IptEventType_.add_(IptEventType_.MouseUp, IptEventType_.KeyDown), IptMouseBtn_.Left, IptKey_.Enter, IptKey_.Space);
		IptBnd_.cmd_to_(window_cfg		, prog_box	, tab, Xog_win.Invk_html_box_focus			, IptKey_.add_(IptKey_.Escape));
		IptBnd_.cmd_to_(window_cfg		, prog_box	, tab, Xog_win.Invk_prog_box_log			, IptMouseBtn_.Middle, IptArg_.parse_chain_("mod.cs+key.p"));

		IptBnd_.cmd_to_(window_cfg		, search_box, tab, Xog_win.Invk_search					, IptKey_.add_(IptKey_.Enter));
		IptBnd_.ipt_to_(null_cfg		, tab.Search_exec_btn(), tab, Xog_win.Invk_search		, IptEventType_.add_(IptEventType_.MouseUp, IptEventType_.KeyDown), IptMouseBtn_.Left, IptKey_.Enter, IptKey_.Space);

		IptBnd_.cmd_to_(window_cfg		, url_box	, tab, Xog_win.Invk_url_box_exec			, IptKey_.add_(IptKey_.Enter));
		IptBnd_.cmd_to_(window_cfg		, url_box	, tab, Xog_win.Invk_html_box_focus			, IptKey_.add_(IptKey_.Escape));
		IptBnd_.cmd_to_(window_cfg		, url_box	, tab, Xog_win.Invk_url_box_paste_and_go	, IptMouseBtn_.Middle, IptArg_.parse_chain_("mod.cs+key.o"));
		IptBnd_.ipt_to_(null_cfg		, tab.Url_exec_btn(), tab, Xog_win.Invk_url_box_exec	, IptEventType_.add_(IptEventType_.MouseUp, IptEventType_.KeyDown), IptMouseBtn_.Left, IptKey_.Enter, IptKey_.Space);
		IptBnd_.ipt_to_(null_cfg		, tab.Find_close_btn(), tab, Xog_win.Invk_find_box_hide, IptEventType_.add_(IptEventType_.MouseUp, IptEventType_.KeyDown), IptMouseBtn_.Left, IptKey_.Enter, IptKey_.Space);

		IptBnd_.ipt_to_(window_cfg		, find_box	, tab, Xog_win.Invk_find_box_find			, IptEventType_.KeyUp, IptKey_.printableKeys_(IptKey_.Ary(IptKey_.Back, IptKey_.Escape), IptKey_.Ary()));
		IptBnd_.cmd_to_(window_cfg		, find_box	, tab, Xog_win.Invk_find_box_dir_fwd		, IptKey_.add_(IptKey_.Alt, IptKey_.N));
		IptBnd_.cmd_to_(window_cfg		, find_box	, tab, Xog_win.Invk_find_box_dir_bwd		, IptKey_.add_(IptKey_.Alt, IptKey_.P));
		IptBnd_.cmd_to_(window_cfg		, find_box	, tab, Xog_win.Invk_find_box_case_toggle	, IptKey_.add_(IptKey_.Alt, IptKey_.C));
		IptBnd_.cmd_to_(window_cfg		, find_box	, tab, Xog_win.Invk_find_box_wrap_toggle	, IptKey_.add_(IptKey_.Alt, IptKey_.W));
		IptBnd_.cmd_to_(window_cfg		, find_box	, tab, Xog_win.Invk_find_box_hide			, IptKey_.add_(IptKey_.Escape));
		IptBnd_.ipt_to_(null_cfg		, tab.Find_fwd_btn(), tab, Xog_win.Invk_find_box_dir_fwd, IptEventType_.add_(IptEventType_.MouseUp, IptEventType_.KeyDown), IptMouseBtn_.Left, IptKey_.Enter, IptKey_.Space);
		IptBnd_.ipt_to_(null_cfg		, tab.Find_bwd_btn(), tab, Xog_win.Invk_find_box_dir_bwd, IptEventType_.add_(IptEventType_.MouseUp, IptEventType_.KeyDown), IptMouseBtn_.Left, IptKey_.Enter, IptKey_.Space);

		IptBnd_.cmd_to_(window_cfg		, win		, tab, Xog_win.Invk_url_box_focus			, IptKey_.add_(IptKey_.Alt, IptKey_.D));
		IptBnd_.cmd_to_(window_cfg		, win		, tab, Xog_win.Invk_find_box_focus			, IptKey_.add_(IptKey_.Ctrl, IptKey_.F));
		IptBnd_.cmd_to_(window_cfg		, win		, tab, Xog_win.Invk_prog_box_focus			, IptArg_.parse_chain_("mod.ca+key.p"));
		IptBnd_.cmd_to_(window_cfg		, win		, tab, Xog_win.Invk_note_box_focus			, IptArg_.parse_chain_("mod.ca+key.i"));
		IptBnd_.cmd_to_(window_cfg		, win		, tab, Xog_win.Invk_search_box_focus		, IptArg_.parse_chain_("mod.ca+key.s"));
		IptBnd_.cmd_to_(window_cfg		, win		, tab, Xog_win.Invk_app_exit				, IptKey_.add_(IptKey_.Ctrl, IptKey_.Q));
		IptBnd_.cmd_to_(window_cfg		, win		, tab, Xog_win.Invk_warn_clear_all			, IptArg_.parse_chain_("mod.ca+key.c"));
		IptBnd_.cmd_to_(window_cfg		, info_box	, tab, Xog_win.Invk_warn_clear_color		, IptMouseBtn_.Right, IptArg_.parse_chain_("mod.ca+key.d"));
		IptBnd_.cmd_to_(window_cfg		, info_box	, tab, Xog_win.Invk_html_box_focus			, IptKey_.add_(IptKey_.Escape));

		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_history_bwd				, IptKey_.add_(IptKey_.Alt, IptKey_.Left));
		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_history_fwd				, IptKey_.add_(IptKey_.Alt, IptKey_.Right));
		IptBnd_.cmd_to_(window_cfg		, win		, tab, Xog_win.Invk_page_refresh			, IptArg_.parse_chain_("key.f5"));
		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_page_view_edit			, IptArg_.parse_chain_("mod.c+key.m,mod.c+key.w"));
		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_page_view_read			, IptArg_.parse_chain_("mod.c+key.m,mod.c+key.h"));
		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_page_view_html			, IptArg_.parse_chain_("mod.c+key.m,mod.c+key.s"));
		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_page_view_edit			, IptArg_.parse_chain_("mod.c+key.m,mod.c+key.w"));
		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_page_edit_focus_box		, IptArg_.parse_chain_("mod.as+key.comma"));
		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_page_edit_focus_first	, IptArg_.parse_chain_("mod.as+key.period"));
		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_page_edit_preview		, IptArg_.parse_chain_("mod.as+key.p"));
		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_page_edit_save			, IptArg_.parse_chain_("mod.as+key.s"));
		IptBnd_.cmd_to_(wikiBox_cfg		, win		, tab, Xog_win.Invk_page_edit_save_draft	, IptArg_.parse_chain_("mod.a+key.s"));
		IptBnd_.ipt_to_(wikiBox_cfg		, html_box	, tab, Xog_win.Invk_link_click				, IptEventType_.add_(IptEventType_.KeyDown, IptEventType_.MouseUp), IptKey_.add_(IptKey_.Ctrl, IptKey_.L), IptMouseBtn_.Left); // NOTE: must be MouseUp; MouseDown fires before focus is tranfserred; EX: link1 is selected; link2 is clicked; MouseDown will cause navigation to link1 b/c it is the selected link; MouseUp will cause navigation to link2; note that this is like firefox as well.
		IptBnd_.cmd_to_(wikiBox_cfg		, html_box	, tab, Xog_win.Invk_page_dbg_wiki			, IptArg_.parse_chain_("mod.c+key.e,mod.c+key.w"), IptArg_.parse_chain_("mod.c+key.e,mod.c+key.e"));
		IptBnd_.cmd_to_(wikiBox_cfg		, html_box	, tab, Xog_win.Invk_page_dbg_html			, IptArg_.parse_chain_("mod.c+key.e,mod.c+key.s"));
		IptBnd_.ipt_to_(null_cfg		, html_box	, tab, Xog_win.Invk_link_print				, IptEventType_.KeyUp, IptKey_.Up, IptKey_.Down, IptKey_.Left, IptKey_.Right);
		IptBnd_.cmd_to_(wikiBox_cfg		, html_box	, tab, Xog_win.Invk_app_exec_cfg			, IptArg_.parse_chain_("mod.as+key.o"));
	}
}
class Ipt_bnd_redirect_mgr implements GfoInvkAble {
	public Ipt_bnd_redirect_mgr() {} IptCfg cfg;
	IptBndsOwner source; GfoInvkAble target;
	public void Bind(String key, IptBndsOwner source, GfoInvkAble target) {
		cfg = IptCfg_.new_(key); this.source = source; this.target = target;
		int list_len = list.Count();
		for (int i = 0; i < list_len; i++) {
			KeyVal kv = (KeyVal)list.FetchAt(i);
			Reg(kv.Key(), kv.Val_to_str_or_empty());
		}
		list.Clear();
	}
	void Reg(String ipt_raw, String msg_raw) {
		IptArg[] ipt = IptArg_.parse_ary_(ipt_raw);
		IptBnd_.cmd_to_(cfg, source, this, msg_raw, ipt);
		hash.Add(msg_raw, msg_raw);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_reg))								Exec_reg(m.ReadStr("ipt"), m.ReadStr("msg"));
		else {
			String cmd_str = (String)hash.Fetch(k);
			if (cmd_str == null) return GfoInvkAble_.Rv_unhandled;
			GfoMsg cmd = GfsCore._.MsgParser().ParseToMsg(cmd_str).Subs_getAt(0);
			return GfoInvkAble_.InvkCmd_msg(target, cmd.Key(), cmd);
		}
		return this;
	}	private static final String Invk_reg = "reg";
	void Exec_reg(String ipt_raw, String msg_raw) {
		list.Add(KeyVal_.new_(ipt_raw, msg_raw));
	}
	ListAdp list = ListAdp_.new_();
	HashAdp hash = HashAdp_.new_();
}

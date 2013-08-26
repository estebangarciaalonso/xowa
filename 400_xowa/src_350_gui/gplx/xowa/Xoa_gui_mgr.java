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
import gplx.gfui.*; import gplx.xowa.specials.search.*; import gplx.xowa.gui.menus.*; import gplx.xowa.gui.cmds.*;
public class Xoa_gui_mgr implements GfoInvkAble {
	public Xoa_gui_mgr(Xoa_app app) {
		this.app = app;
		main_win = new Xog_win(app, this);
		win_opts = new Xoa_ui_opts(app);
		html_mgr = new Xoa_html_mgr(app);
		menu_mgr = new Xog_menu_mgr(app);
		search_suggest_mgr = new Xog_search_suggest_mgr(this);
	}
	public Xoa_app App() {return app;} private Xoa_app app;
	public Xog_win Main_win() {return main_win;} private Xog_win main_win;
	public IptCfgRegy Ipt_cfgs() {return ipt_cfgs;} IptCfgRegy ipt_cfgs = new IptCfgRegy();
	public Gfui_kit Kit() {return kit;} Gfui_kit kit = Gfui_kit_.key_(Gfui_kit_.Key_basic);
	public Xog_cmd_mgr Cmd_mgr() {return cmd_mgr;} private Xog_cmd_mgr cmd_mgr = new Xog_cmd_mgr();
	public Xoa_ui_opts Win_opts() {return win_opts;} private Xoa_ui_opts win_opts;
	public Xog_layout Layout() {return layout;} private Xog_layout layout = new Xog_layout();
	public Xoa_html_mgr Html_mgr() {return html_mgr;} private Xoa_html_mgr html_mgr;
	public Xog_search_suggest_mgr Search_suggest_mgr() {return search_suggest_mgr;} private Xog_search_suggest_mgr search_suggest_mgr;
	public Xog_menu_mgr Menu_mgr() {return menu_mgr;} private Xog_menu_mgr menu_mgr;
	public void Show_prog() {
		GfuiWin memo_win = kit.New_win_utl("memo_win", main_win.Win());
		GfuiTextBox memo_txt = kit.New_text_box("memo_txt", memo_win, KeyVal_.new_(GfuiTextBox_.Ctor_Memo, true));
		RectAdp prog_box_rect = main_win.Prog_box().Rect();
		memo_win.Rect_set(RectAdp_.new_(prog_box_rect.X(), prog_box_rect.Y() - 75, prog_box_rect.Width(), 100));
		memo_txt.Size_(memo_win.Size().Op_add(-8, -30));
		memo_txt.Text_(String_.Concat_lines_nl(main_win.Gui_wtr().Ui_wkr().Prog_msgs().Xto_str_ary()));
		memo_win.Show();
		memo_win.Focus();
	}
	public void Init() {
		layout_Init();
		main_win.Gui_wtr_(new Gfo_usr_dlg_xowa());	// NOTE: must set win_wtr for fsys_mgr to assign to process_adps
	}
	public void Kit_(Gfui_kit kit) {
		this.kit = kit;
		kit.Kit_init(main_win.Gui_wtr());
		kit.Kit_term_cbk_(GfoInvkAbleCmd.new_(app, Xoa_app.Invk_term_cbk));
		main_win.Init(kit);
		layout.Init(main_win);
		cmd_mgr.Init_by_kit(app);
		menu_mgr.Menu_bldr().Init_by_kit(app, kit, app.User().Fsys_mgr().App_img_dir().GenSubDir_nest("window", "menu"));
		menu_mgr.Init_by_kit();
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_kit))				return kit;
		else if	(ctx.Match(k, Invk_kit_))				this.kit = Gfui_kit_.key_(m.ReadStrOr("v", "default"));
		else if	(ctx.Match(k, Invk_run))				Run();
		else if	(ctx.Match(k, Invk_browser_type))		kit.Cfg_set("HtmlBox", "BrowserType", gplx.gfui.Swt_kit.Cfg_Html_BrowserType_parse(m.ReadStr("v")));
		else if	(ctx.Match(k, Invk_xul_runner_path_))	kit.Cfg_set("HtmlBox", "XulRunnerPath", ByteAryFmtr_eval_mgr_.Eval_url(app.Url_cmd_eval(), m.ReadBry("v")).Xto_api());
		else if	(ctx.Match(k, Invk_bindings))			return ipt_cfgs;
		else if	(ctx.Match(k, Invk_main_win))			return main_win;
		else if	(ctx.Match(k, Invk_win_opts))			return win_opts;
		else if	(ctx.Match(k, Invk_layout))				return layout;
		else if	(ctx.Match(k, Invk_html))				return html_mgr;
		else if	(ctx.Match(k, Invk_search_suggest))		return search_suggest_mgr;
		else if	(ctx.Match(k, Invk_menus))				return menu_mgr;
		else if	(ctx.Match(k, Invk_cmds))				return cmd_mgr;
		else throw Err_mgr._.unhandled_(k);
		return this;
	}
	private static final String Invk_kit = "kit", Invk_kit_ = "kit_", Invk_browser_type = "browser_type_", Invk_xul_runner_path_ = "xul_runner_path_", Invk_run = "run", Invk_bindings = "bindings", Invk_main_win = "main_win", Invk_win_opts = "win_opts", Invk_layout = "layout", Invk_html = "html"
		, Invk_search_suggest = "search_suggest", Invk_menus = "menus", Invk_cmds = "cmds";
	public void Run() {
		Xoa_app_.Run_ui(app);
	}
	void layout_Init() {
		Op_sys os = Op_sys.Cur();
		int html_box_w = -8; int html_box_h = -30;	// default adjustments since version 0.0.0.0; seems to work on XP and LNX
		switch (os.Tid()) {
			case Op_sys.Tid_wnt:
				switch (os.Sub_tid()) {
					case Op_sys.Sub_tid_win_xp:	break;	// NOOP; will keep values as above
					default:							// for all else, use Windows 7 border; note that Windows 8 is being detected as "Windows NT (unknown)", so need to use default; this may not work with Windows 2000
						html_box_w = -16; html_box_h = -40;	// Windows 7 has a thicker windows border and the html_box must be smaller else scroll bar gets cut off
						break;
				}
				break;
			default:
				break;
		}
		layout.Html_box().W_rel_(html_box_w).H_rel_(html_box_h);
	}
}

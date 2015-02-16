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
package gplx.xowa.gui.views; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
import gplx.threads.*; import gplx.gfui.*; import gplx.xowa.gui.history.*; import gplx.xowa.gui.bnds.*;
import gplx.xowa.parsers.*; import gplx.xowa.parsers.lnkis.redlinks.*; import gplx.xowa.cfgs2.*; import gplx.xowa.pages.*;
public class Xog_tab_itm implements GfoInvkAble {
	private Xog_win_itm win; private Xocfg_tab_mgr cfg_tab_mgr;
	public Xog_tab_itm(Xog_tab_mgr tab_mgr, Gfui_tab_itm_data tab_data, Xoa_page page) {
		this.tab_mgr = tab_mgr; this.tab_data = tab_data; this.page = page;
		this.win = tab_mgr.Win(); this.cfg_tab_mgr = win.App().Cfg_regy	().App().Gui_mgr().Tab_mgr();
		html_itm = new Xog_html_itm(this);
		cmd_sync = win.Kit().New_cmd_sync(this);
	}
	public GfoInvkAble Cmd_sync() {return cmd_sync;} private GfoInvkAble cmd_sync;
	public void Make_html_box(int uid, Gfui_tab_itm tab_box, Xog_win_itm win, GfuiElem owner) {
		this.tab_box = tab_box;
		Xoa_app app = win.App(); Xoa_gui_mgr gui_mgr = win.Gui_mgr(); Gfui_kit kit = win.Kit();
		Gfui_html html_box	= kit.New_html("html_box" + Int_.Xto_str(uid), owner);
		html_box.Html_js_enabled_(gui_mgr.Html_mgr().Javascript_enabled());
		html_box.Html_invk_src_(win);
		html_itm.Html_box_(html_box);
		if (app.Mode() == Xoa_app_.Mode_gui) {	// NOTE: only run for gui; will cause firefox addon to fail; DATE:2014-05-03
			html_box.Html_doc_html_load_by_mem("");	// NOTE: must set source, else control will be empty, and key events will not be raised; DATE:2014-04-30
			IptBnd_.ipt_to_(IptCfg_.Null, html_box, this, "popup", IptEventType_.MouseDown, IptMouseBtn_.Right);
			GfoEvMgr_.SubSame(html_box, GfuiElemKeys.Evt_menu_detected, html_itm);
			gui_mgr.Bnd_mgr().Bind(Xog_bnd_box_.Tid_browser_html, html_box);
			if (!Env_.Mode_testing())
				kit.Set_mnu_popup(html_box, gui_mgr.Menu_mgr().Popup().Html_page().Under_mnu());
		}
	}
	public void Switch_mem(Xog_tab_itm comp) {
		html_itm.Switch_mem(comp.html_itm);					// switch html_itm.owner_tab reference only
		Xog_html_itm temp_html_itm = html_itm;				// switch .html_itm, since the underlying CTabFolder has reparented the control
		this.html_itm = comp.html_itm;
		comp.html_itm = temp_html_itm;

		Xoa_page temp_page = page;							// switch .page, since its underlying html_box has changed and .page must reflect html_box
		this.page = comp.page;
		comp.page = temp_page;
		page.Tab_(this); comp.Page().Tab_(comp);

		byte temp_view_mode = view_mode;					// switch .view_mode to match .page
		this.view_mode = comp.view_mode;
		comp.view_mode = temp_view_mode;

		Xog_history_mgr temp_history_mgr = history_mgr;		// switch .history_mgr along with .page
		this.history_mgr = comp.history_mgr;
		comp.history_mgr = temp_history_mgr;
	}
	public Gfui_tab_itm_data	Tab_data() {return tab_data;} private Gfui_tab_itm_data tab_data;
	public String				Tab_key() {return tab_data.Key();}
	public int					Tab_idx() {return tab_data.Idx();} public void Tab_idx_(int v) {tab_data.Idx_(v);}
	public Xog_tab_mgr			Tab_mgr() {return tab_mgr;} private Xog_tab_mgr tab_mgr;
	public Gfui_tab_itm			Tab_box() {return tab_box;} private Gfui_tab_itm tab_box;
	public boolean					Tab_is_loading() {return tab_is_loading;} private boolean tab_is_loading;
	public Xog_html_itm			Html_itm() {return html_itm;} private Xog_html_itm html_itm;
	public Gfui_html			Html_box() {return html_itm.Html_box();}
	public Xoa_page				Page() {return page;}
	public void Page_(Xoa_page page) {
		this.page = page;
		this.Page_update_ui();	// force tab button to update when page changes
	}	private Xoa_page page;		
	public void Page_update_ui() {
		this.Tab_name_();
		tab_box.Tab_tip_text_(page.Url().Xto_full_str());
	}
	public void Tab_name_() {
		byte[] tab_name = page.Html_data().Custom_name();
		if (tab_name == null) tab_name = page.Ttl().Full_txt();
		Tab_name_(String_.new_utf8_(tab_name));
	}
	public void Tab_name_(String tab_name) {
		Xocfg_tab_btn_mgr cfg_tab_btn_mgr = cfg_tab_mgr.Btn_mgr();
		tab_name = Xog_tab_itm_.Tab_name_min(tab_name, cfg_tab_btn_mgr.Text_min_chars());
		tab_name = Xog_tab_itm_.Tab_name_max(tab_name, cfg_tab_btn_mgr.Text_max_chars());
		tab_box.Tab_name_(tab_name);
	}
	public Xog_history_mgr		History_mgr() {return history_mgr;} private Xog_history_mgr history_mgr = new Xog_history_mgr();
	public byte					View_mode() {return view_mode;} public Xog_tab_itm View_mode_(byte v) {view_mode = v; return this;} private byte view_mode = Xopg_view_mode.Tid_read;
	public void Pin_toggle() {}
	public void Show_url_bgn(Xoa_url url) {
		this.tab_is_loading = true;
		Xoa_app app = win.App(); Gfo_usr_dlg usr_dlg = app.Usr_dlg();
		Xoa_page page = Xoa_page.Empty; 
		if (	url.Anchor_str() != null				// url has anchor
			&&	url.Eq_page(page.Url())					// url has same page_name as existing page
			&&	url.Args().length == 0) {				// url has no args; needed for Category:A?from=b#mw-pages
			html_itm.Scroll_page_by_id_gui(url.Anchor_str());	// skip page_load and jump to anchor
			return;
		}
		if (win.Page__async__working(url)) return;
		app.Gui_mgr().Search_suggest_mgr().Cancel();	// cancel pending search_suggest calls
		app.Log_wtr().Queue_enabled_(true);
		usr_dlg.Clear();
		Xow_wiki wiki = app.Wiki_mgr().Get_by_key_or_null(url.Wiki_bry());
		wiki.Init_assert();	// NOTE: assert wiki.Init before parsing; needed b/c lang (with lang-specific ns) is only loaded on init, and parse Xoa_ttl.parse_ will fail below; EX:pt.wikipedia.org/wiki/Wikipedia:P�gina principal
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, url.Page_bry());
		if (ttl == null) {usr_dlg.Prog_one("", "", "title is invalid: ~{0}", String_.new_utf8_(url.Raw())); return;}
		usr_dlg.Prog_one("", "", "loading: ~{0}", String_.new_utf8_(ttl.Raw()));
		if (app.Api_root().Html().Modules().Popups().Enabled())
			this.Html_box().Html_js_eval_script("if (window.xowa_popups_hide_all != null) window.xowa_popups_hide_all();");	// should be more configurable; DATE:2014-07-09
		app.Thread_mgr().Page_load_mgr().Add_at_end(new Load_page_wkr(this, wiki, url, ttl)).Run();
	}
	public void Show_url_loaded(Xoa_page page) {
		Xow_wiki wiki = page.Wiki(); Xoa_url url = page.Url(); Xoa_ttl ttl = page.Ttl();
		Xoa_app app = wiki.App(); Gfo_usr_dlg usr_dlg = app.Usr_dlg();
		try {
			wiki.Ctx().Cur_page_(page);
			if (page.Missing()) {
				if (wiki.Db_mgr().Save_mgr().Create_enabled()) {
					page = Xoa_page.create_(wiki, ttl);
					view_mode = Xopg_view_mode.Tid_edit;
					history_mgr.Add(page);	// NOTE: must put new_page on stack so that pressing back will pop new_page, not previous page
					Xog_tab_itm_read_mgr.Show_page(this, page, false);
				}
				else {
					if (page.Redirected_ttls().Count() > 0)
						usr_dlg.Prog_many("", "", "could not find: ~{0} (redirected from ~{1})", String_.new_utf8_(page.Url().Page_bry()), String_.new_utf8_((byte[])page.Redirected_ttls().FetchAt(0)));
					else {
						if (ttl.Ns().Id_file())
							usr_dlg.Prog_one("", "", "commons.wikimedia.org must be installed in order to view the file. See [[Help:Wikis/Commons]]: ~{0}", String_.new_utf8_(url.Raw()));
						else
							usr_dlg.Prog_one("", "", "could not find: ~{0}", String_.new_utf8_(url.Raw()));
					}
				}
				app.Log_wtr().Queue_enabled_(false);
				return;
			}
			if (!page.Redirected()) page.Url_(url);	// NOTE: handle redirect from commons
			if (page.Ttl().Anch_bgn() != Bry_.NotFound) page.Url().Anchor_bry_(page.Ttl().Anch_txt());	// NOTE: occurs when page is a redirect to an anchor; EX: w:Duck race -> Rubber duck#Races
			history_mgr.Add(page);
			Xog_tab_itm_read_mgr.Show_page(this, page, true);
			if (app.Api_root().Usr().History().Enabled()) {
				app.User().History_mgr().Add(page);
				app.User().Data_mgr().History_mgr().Update_async(app.Async_mgr(), ttl, url);
			}
			usr_dlg.Prog_none("", "", "rendering html");
			//	win.Page__async__bgn(this);
			app.Thread_mgr().File_load_mgr().Add_at_end(new Load_files_wkr(this)).Run();
		}
		finally {
			app.Thread_mgr().Page_load_mgr().Resume();
			this.tab_is_loading = false;
		}
	}
	@gplx.Internal protected void Show_url_failed(Load_page_wkr wkr) {
		try {
			Xog_tab_itm_read_mgr.Show_page_err(win, this, wkr.Wiki(), wkr.Url(), wkr.Ttl(), wkr.Exc());
		} finally {
			wkr.Wiki().App().Thread_mgr().Page_load_mgr().Resume();
		}
	}
	public void Async() {
		if (page == null) return;	// TEST: occurs during Xog_win_mgr_tst
		Xow_wiki wiki = page.Wiki(); Xoa_app app = wiki.App(); Xog_win_itm win_itm = tab_mgr.Win(); Gfo_usr_dlg usr_dlg = win_itm.Usr_dlg();
		app.Usr_dlg().Log_many("", "", "page.async: url=~{0}", page.Url().Xto_full_str_safe());
		if (page.Url().Anchor_str() != null) html_itm.Scroll_page_by_id_gui(page.Url().Anchor_str());
		if (usr_dlg.Canceled()) {usr_dlg.Prog_none("", "", ""); app.Log_wtr().Queue_enabled_(false); return;}
		int xfer_len = 0;
		xfer_len = page.File_queue().Count();
		String page_ttl_str = String_.new_utf8_(page.Ttl().Raw());
		if (xfer_len > 0){
			usr_dlg.Prog_one("", "", "downloading images: ~{0}", xfer_len);
			try {
				page.File_queue().Exec(gplx.xowa.files.Xof_exec_tid.Tid_wiki_page, usr_dlg, wiki, page);
				if (page.Html_data().Xtn_gallery_packed_exists())	// packed_gallery exists; fire js once; PAGE:en.w:National_Sculpture_Museum_(Valladolid); DATE:2014-07-21
					html_itm.Html_gallery_packed_exec();
				if (	page.Html_data().Xtn_imap_exists()		// imap exists; DATE:2014-08-07
					&&	page.Html_data().Module_mgr().Itm_popups().Enabled()
					)
					html_itm.Html_popups_bind_hover_to_doc();	// rebind all elements to popup
			}
			catch (Exception e) {usr_dlg.Warn_many("", "", "page.thread.image: page=~{0} err=~{1}", page_ttl_str, Err_.Message_gplx_brief(e));}
		}
		xfer_len = page.File_math().Count();
		if (xfer_len > 0){
			try {
				usr_dlg.Prog_one("", "", "generating math: ~{0}", xfer_len);
				for (int i = 0; i < xfer_len; i++) {
					if (usr_dlg.Canceled()) {usr_dlg.Prog_none("", "", ""); app.Log_wtr().Queue_enabled_(false); return;}
					gplx.xowa.xtns.math.Xof_math_itm itm = (gplx.xowa.xtns.math.Xof_math_itm)page.File_math().FetchAt(i);
					String queue_msg = usr_dlg.Prog_many("", "", "generating math ~{0} of ~{1}: ~{2}", i + ListAdp_.Base1, xfer_len, String_.new_utf8_(itm.Math()));
					app.File_mgr().Math_mgr().MakePng(itm.Math(), itm.Hash(), itm.Png_url(), queue_msg);
					gplx.gfui.SizeAdp size = app.File_mgr().Img_mgr().Wkr_query_img_size().Exec(itm.Png_url());
					html_itm.Html_img_update("xowa_math_img_" + itm.Id(), itm.Png_url().To_http_file_str(), size.Width(), size.Height());
					html_itm.Html_elem_delete("xowa_math_txt_" + itm.Id());
				}
				page.File_math().Clear();
			}
			catch (Exception e) {usr_dlg.Warn_many("", "", "page.thread.math: page=~{0} err=~{1}", page_ttl_str, Err_.Message_gplx_brief(e));}
		}
		if (page.Html_cmd_mgr().Count() > 0) {
			try {page.Html_cmd_mgr().Exec(app, page);}
			catch (Exception e) {usr_dlg.Warn_many("", "", "page.thread.cmds: page=~{0} err=~{1}", page_ttl_str, Err_.Message_gplx_brief(e));}
		}
		try {
			if (page.Tab() != null) {	// needed b/c Preview has page.Tab of null which causes null_ref error in redlinks
				Xop_lnki_logger_redlinks_wkr redlinks_wkr = new Xop_lnki_logger_redlinks_wkr(win_itm, page);
				ThreadAdp_.invk_(redlinks_wkr, gplx.xowa.parsers.lnkis.redlinks.Xop_lnki_logger_redlinks_wkr.Invk_run).Start();
				usr_dlg.Prog_none("", "imgs.done", "");
			}
		}	catch (Exception e) {usr_dlg.Warn_many("", "", "page.thread.redlinks: page=~{0} err=~{1}", page_ttl_str, Err_.Message_gplx_brief(e));}
		try {app.File_mgr().Cache_mgr().Compress_check();}
		catch (Exception e) {usr_dlg.Warn_many("", "", "page.thread.cache: page=~{0} err=~{1}", page_ttl_str, Err_.Message_gplx_brief(e));}
		if (wiki.Db_mgr().Hdump_mgr().Enabled()) {
			wiki.Db_mgr().Hdump_mgr().Save_if_missing(page);
		}
		app.Log_wtr().Queue_enabled_(false);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_show_url_loaded_swt))	this.Show_url_loaded((Xoa_page)m.ReadObj("v"));
		else if	(ctx.Match(k, Invk_show_url_failed_swt))	this.Show_url_failed((Load_page_wkr)m.ReadObj("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_show_url_loaded_swt = "show_url_loaded_swt", Invk_show_url_failed_swt = "show_url_failed_swt";
}
class Load_page_wkr implements Gfo_thread_wkr {
	private Xog_tab_itm tab;
	public Load_page_wkr(Xog_tab_itm tab, Xow_wiki wiki, Xoa_url url, Xoa_ttl ttl) {this.tab = tab; this.wiki = wiki; this.url = url; this.ttl = ttl;}
	public String Name() {return "xowa.load_page_wkr";}
	public boolean Resume() {return false;}
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public Xoa_url Url() {return url;} private Xoa_url url;
	public Xoa_ttl Ttl() {return ttl;} private Xoa_ttl ttl;
	public Exception Exc() {return exc;} private Exception exc;
	public void Exec() {
		try {
			Xoa_app app = wiki.App();
			app.Usr_dlg().Log_many("", "", "page.load: url=~{0}", url.Xto_full_str_safe());
			if (Env_.System_memory_free() < app.Sys_cfg().Free_mem_when())	// check if low in memory
				app.Free_mem(false);										// clear caches (which will clear bry_bfr_mk)
			else															// not low in memory
				app.Utl_bry_bfr_mkr().Clear();								// clear bry_bfr_mk only; NOTE: call before page parse, not when page is first added, else threading errors; DATE:2014-05-30
			Xoa_page page = wiki.GetPageByTtl(url, ttl, wiki.Lang(), tab, false);
			int html_db_id = page.Revision_data().Html_db_id();
			if (wiki.Db_mgr().Hdump_mgr().Enabled() && html_db_id != -1)
				wiki.Db_mgr().Hdump_mgr().Load(wiki, page, html_db_id);
			else
				wiki.ParsePage(page, false);
			GfoInvkAble_.InvkCmd_val(tab.Cmd_sync(), Xog_tab_itm.Invk_show_url_loaded_swt, page);
		}
		catch (Exception e) {
			this.exc = e;
			GfoInvkAble_.InvkCmd_val(tab.Cmd_sync(), Xog_tab_itm.Invk_show_url_failed_swt, this);
		}
	}
}
class Load_files_wkr implements Gfo_thread_wkr {
	private Xog_tab_itm tab;
	public Load_files_wkr(Xog_tab_itm tab) {this.tab = tab;}
	public String Name() {return "xowa.load_files_wkr";}
	public boolean Resume() {return true;}
	public void Exec() {
		try {tab.Async();}
		catch (Exception e) {
			tab.Tab_mgr().Win().App().Usr_dlg().Warn_many("error while running file wkr; page=~{0} err=~{1}", tab.Page().Url().Xto_full_str(), Err_.Message_gplx_brief(e));
		}
	}
}

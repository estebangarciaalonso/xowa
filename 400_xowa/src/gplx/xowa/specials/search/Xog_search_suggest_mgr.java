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
package gplx.xowa.specials.search; import gplx.*; import gplx.xowa.*; import gplx.xowa.specials.*;
import gplx.gfui.*;
public class Xog_search_suggest_mgr implements GfoInvkAble {
	public Xog_search_suggest_mgr(Xoa_gui_mgr gui_mgr) {
		this.gui_mgr = gui_mgr;
		this.app = gui_mgr.App();
		this.main_win = gui_mgr.Main_win();
		cur_cmd = new Xog_search_suggest_cmd(app, this);
	}	private Xoa_app app; Xoa_gui_mgr gui_mgr; Xog_win main_win; Js_wtr wtr = new Js_wtr();
	private boolean enabled = true; private int results_max = 10; private boolean log_enabled = false;
	public byte Search_mode() {return search_mode;} public Xog_search_suggest_mgr Search_mode_(byte v) {search_mode = v; return this;} private byte search_mode;
	public int All_pages_extend() {return all_pages_extend;} private int all_pages_extend = 1000;	// look ahead by 1000
	public int All_pages_min() {return all_pages_min;} private int all_pages_min = 10000;			// only look at pages > 10 kb
	public boolean Auto_wildcard() {return auto_wildcard;} private boolean auto_wildcard = false;			// automatically add wild-card; EX: Earth -> *Earth*
	public void Cancel() {
		cur_cmd.Cancel();
		long prv_time = Env_.TickCount();
		while (cur_cmd.Working()) {
			if (Env_.TickCount() - prv_time > 4000) {
				if (log_enabled) app.Usr_dlg().Log_many("", "", "search cancel timeout: word=~{0}", String_.new_utf8_(search_bry));
				cur_cmd.Working_(false);
				break;
			}
		}
	}
	public void Search(Xow_wiki wiki, byte[] search_bry, byte[] cbk_func) {
		this.wiki = wiki; this.search_bry = search_bry; this.cbk_func = cbk_func;
		ThreadAdp_.invk_(this, Invk_search_async).Start();
	}	private Xow_wiki wiki; private byte[] search_bry, cbk_func;
	Object thread_guard = new Object();
	private void Search_async() {
		if (!enabled) return;
		if (search_bry.length == 0) return;
		this.Cancel();
		synchronized (thread_guard) {
			if (ByteAry_.Eq(search_bry , last_search_bry)) {
				if (log_enabled) app.Usr_dlg().Log_many("", "", "search repeated?: word=~{0}", String_.new_utf8_(search_bry));
				return;
			}
			cur_cmd.Init(wiki, search_bry, cbk_func, results_max, search_mode, all_pages_extend, all_pages_min);
			this.last_search_bry = search_bry;
			if (log_enabled) app.Usr_dlg().Log_many("", "", "search bgn: word=~{0}", String_.new_utf8_(search_bry));
			cur_cmd.Search();
		}
	}	private Xog_search_suggest_cmd cur_cmd; byte[] last_search_bry;
	public void Notify() {// EX: receiveSuggestions('search_word', ['result_1', 'result_2']);
			// synchronized (thread_guard) NOTE: never use synchronized here; will synchronized search; DATE:2013-09-24
			byte[] search_bry = cur_cmd.Search_bry();
			if (!ByteAry_.Eq(search_bry, last_search_bry)) {
				if (log_enabled) app.Usr_dlg().Log_many("", "", "search does not match?: expd=~{0} actl=~{1}", String_.new_utf8_(last_search_bry), String_.new_utf8_(search_bry));
				return;	// do not notify if search terms do not match
			}
			ListAdp found = cur_cmd.Results();
			wtr.Add_str(cbk_func);
			wtr.Add_paren_bgn();
				wtr.Add_str_quote(search_bry).Add_comma();				
				wtr.Add_brack_bgn();
					int len = found.Count();
					for (int i = 0; i < len; i++) {
						Xodb_page p = (Xodb_page)found.FetchAt(i);
						byte[] ttl = Xoa_ttl.Replace_unders(p.Ttl_wo_ns());
						wtr.Add_str_arg(i, ttl);
					}
				wtr.Add_brack_end();
			wtr.Add_paren_end_semic();
			if (log_enabled) app.Usr_dlg().Log_many("", "", "search end: word=~{0}", String_.new_utf8_(search_bry));
			main_win.Html_box().Html_js_eval_script(wtr.Xto_str_and_clear());
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_search_async))			Search_async();
		else if	(ctx.Match(k, Invk_notify))					Notify();
		else if	(ctx.Match(k, Invk_enabled))				return Yn.XtoStr(enabled);
		else if	(ctx.Match(k, Invk_enabled_))				enabled = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_results_max))			return results_max;
		else if	(ctx.Match(k, Invk_results_max_))			results_max = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_search_mode))			return Search_mode_str(search_mode);
		else if	(ctx.Match(k, Invk_search_mode_))			search_mode = Search_mode_parse(m.ReadStr("v"));
		else if	(ctx.Match(k, Invk_search_mode_list))		return Options_search_mode_list;
		else if	(ctx.Match(k, Invk_all_pages_extend))		return all_pages_extend;
		else if	(ctx.Match(k, Invk_all_pages_extend_))		all_pages_extend = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_all_pages_min))			return all_pages_min;
		else if	(ctx.Match(k, Invk_all_pages_min_))			all_pages_min = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_auto_wildcard))			return Yn.XtoStr(auto_wildcard);
		else if	(ctx.Match(k, Invk_auto_wildcard_))			auto_wildcard = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_log_enabled))		return Yn.XtoStr(log_enabled);
		else if	(ctx.Match(k, Invk_log_enabled_))		log_enabled = m.ReadYn("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_search_async = "search_async", Invk_notify = "notify", Invk_enabled = "enabled", Invk_enabled_ = "enabled_", Invk_results_max = "results_max", Invk_results_max_ = "results_max_"
	, Invk_search_mode = "search_mode", Invk_search_mode_ = "search_mode_", Invk_search_mode_list = "search_mode_list"
	, Invk_all_pages_extend = "all_pages_extend", Invk_all_pages_extend_ = "all_pages_extend_"
	, Invk_all_pages_min = "all_pages_min", Invk_all_pages_min_ = "all_pages_min_"
	, Invk_auto_wildcard = "auto_wildcard", Invk_auto_wildcard_ = "auto_wildcard_"
	, Invk_log_enabled = "log_enabled", Invk_log_enabled_ = "log_enabled_"
	;
	private static KeyVal[] Options_search_mode_list = KeyVal_.Ary(KeyVal_.new_("Search"), KeyVal_.new_("AllPages")); 
	private static byte Search_mode_parse(String v) {
		if		(String_.Eq(v, Str_search_mode_search))			return Tid_search_mode_search;
		else if	(String_.Eq(v, Str_search_mode_all_pages))		return Tid_search_mode_all_pages;
		else													throw Err_.unhandled(v);
	}
	private static String Search_mode_str(byte v) {
		switch (v) {
			case Tid_search_mode_search:						return Str_search_mode_search;
			case Tid_search_mode_all_pages:						return Str_search_mode_all_pages;
			default:											throw Err_.unhandled(v);
		}
	}
	public static final byte Tid_search_mode_all_pages = 0, Tid_search_mode_search = 1;
	static final String Str_search_mode_search = "Search", Str_search_mode_all_pages = "AllPages";
}

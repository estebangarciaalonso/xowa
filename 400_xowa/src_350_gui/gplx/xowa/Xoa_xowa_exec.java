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
public class Xoa_xowa_exec implements GfoInvkAble {
	public Xoa_xowa_exec(Xoa_app app) {this.app = app;} private Xoa_app app;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_parse_to_html))			return Parse_to_html(m);
		else if	(ctx.Match(k, Invk_wikidata_get_label))		return Wikidata_get_label(m);
		else if	(ctx.Match(k, Invk_get_page))				return Get_page(m);
		else if	(ctx.Match(k, Invk_scripts_exec))			return Scripts_exec(m);
		else if	(ctx.Match(k, Invk_get_search_suggestions))	return Get_search_suggestions(m);
		else if	(ctx.Match(k, Invk_get_titles_meta))		return Get_titles_meta(m);
		else if	(ctx.Match(k, Invk_get_titles_exists))		return Get_titles_exists(m);
		else if	(ctx.Match(k, Invk_get_current_url))		return String_.new_utf8_(app.Gui_mgr().Main_win().Page().Url().Raw());
		else if	(ctx.Match(k, Invk_xowa_exec_test))			return Xowa_exec_test(m);
		else if	(ctx.Match(k, Invk_xowa_exec_test_as_array))return Xowa_exec_test_as_array(m);
		else	return GfoInvkAble_.Rv_unhandled;
	}
	public static final String Invk_parse_to_html = "parse_to_html", Invk_wikidata_get_label = "wikidata_get_label", Invk_get_page = "get_page", Invk_scripts_exec = "scripts_exec"
		, Invk_get_search_suggestions = "get_search_suggestions", Invk_get_titles_meta = "get_titles_meta", Invk_get_titles_exists = "get_titles_exists", Invk_get_current_url = "get_current_url"
		, Invk_xowa_exec_test = "xowa_exec_test", Invk_xowa_exec_test_as_array = "xowa_exec_test_as_array"
		;
	private String Xowa_exec_test(GfoMsg m) {	// concat args with pipe; EX: xowa_exec('proc', 'arg0', 'arg1'); -> proc|arg0|arg1
		bfr.Clear();
		bfr.Add_str(m.Key());
		int len = m.Args_count();
		for (int i = 0; i < len; i++)
			bfr.Add_str("|").Add_str(m.Args_getAt(i).Val_to_str_or_empty());
		return bfr.XtoStrAndClear();
	}
	private String[] Xowa_exec_test_as_array(GfoMsg m) {// return args as array; EX: xowa_exec('proc', 'arg0', 'arg1'); -> proc,arg0,arg1
		bfr.Clear();
		int len = m.Args_count();
		String[] rv = new String[len + 1];
		rv[0] = Invk_xowa_exec_test_as_array;
		for (int i = 0; i < len; i++)
			rv[i + 1] = Object_.XtoStr_OrEmpty(m.ReadValAt(i));
		return rv;
	}
	private String Parse_to_html(GfoMsg m) {
		Xow_wiki wiki = app.Gui_mgr().Main_win().Page().Wiki();
		Xop_ctx ctx = wiki.Ctx();
		boolean old_para_enabled = ctx.Para().Enabled();
		byte[] raw = ByteAry_.new_utf8_(m.Args_getAt(0).Val_to_str_or_empty());
		boolean para_enabled = m.Args_count() < 2 ? false : Bool_.parse_(m.Args_getAt(1).Val_to_str_or_empty());
		try {
			wiki.Ctx().Para().Enabled_(para_enabled);
			wiki.Parser().Parse_page_all(root, wiki.Ctx(), wiki.Ctx().Tkn_mkr(), raw, 0);
			byte[] data = root.Data_mid();
			wiki.Html_wtr().Write_all(wiki.Ctx(), root, data, bfr);
			return bfr.XtoStrAndClear();
		}
		finally {
			wiki.Ctx().Para().Enabled_(old_para_enabled);
		}
	}
	private String Get_page(GfoMsg m) {
		Xow_wiki wiki = app.Gui_mgr().Main_win().Page().Wiki();
		try {
			Xoa_ttl ttl = Xoa_ttl.parse_(wiki, m.Args_getAt(0).Val_to_bry());
			Xoa_page page = wiki.Data_mgr().Get_page(ttl, false);
			return String_.new_utf8_(page.Data_raw());
		} catch (Exception e) {Err_.Noop(e); return null;}
	}
	private String[] Get_title_meta(Xow_wiki wiki, byte[] ttl) {
		Pf_xtn_ifexist.Exists(tmp_page, wiki.Ctx(), ttl);
		return String_.Ary(tmp_page.Exists() ? "1" : "0", Int_.XtoStr(tmp_page.Id()), Int_.XtoStr(tmp_page.Ns_id()), String_.new_utf8_(tmp_page.Ttl_wo_ns()), Bool_.XtoStr_lower(tmp_page.Type_redirect()), tmp_page.Modified_on().XtoStr_fmt("yyyy-MM-dd HH:mm:ss"), Int_.XtoStr(tmp_page.Text_len()));
	}	private static final Xodb_page tmp_page = Xodb_page.tmp_();
	private String[][] Get_titles_meta(GfoMsg m) {
		Xow_wiki wiki = app.Gui_mgr().Main_win().Page().Wiki();
		try {
			byte[][] ttls = ByteAry_.Split(ByteAry_.new_utf8_((String)m.ReadValAt(0)), Byte_ascii.NewLine);
			int ttls_len = ttls.length;
			String[][] rv = new String[ttls_len][];
			for (int i = 0; i < ttls_len; i++) {
				byte[] ttl = ttls[i];
				rv[i] = Get_title_meta(wiki, ttl);
			}
			return rv;
		} catch (Exception e) {Err_.Noop(e); return null;}
	}
	private boolean Get_title_exists(Xow_wiki wiki, byte[] ttl) {
		return Pf_xtn_ifexist.Exists(tmp_page, wiki.Ctx(), ttl);
	}		
	private String[] Get_titles_exists(GfoMsg m) {
		Xow_wiki wiki = app.Gui_mgr().Main_win().Page().Wiki();
		try {
			byte[][] ttls = ByteAry_.Ary_obj((Object[])m.ReadValAt(0));
			int ttls_len = ttls.length;
			String[] rv = new String[ttls_len];
			for (int i = 0; i < ttls_len; i++) {
				byte[] ttl = ttls[i];
				rv[i] = Get_title_exists(wiki, ttl) ? "1" : "0";
			}
			return rv;
		} catch (Exception e) {Err_.Noop(e); return null;}
	}	
//		private String[] Get_titles_exists(GfoMsg m) {
//			Xow_wiki wiki = app.Gui_mgr().Main_win().Page().Wiki();
//			try {
//				byte[][] ttls = ByteAry_.Ary_obj((Object[])m.ReadValAt(0));
//				int ttls_len = ttls.length;
//				OrderedHash hash = OrderedHash_.new_bry_();
//				for (int i = 0; i < ttls_len; i++) {
//					byte[] ttl = ttls[i];
//					Xodb_page page = new Xodb_page().Ttl_(ttl, wiki.Ns_mgr());
//					Xow_ns ns = page.Ns();
//					hash.Add(ns.Gen_ttl(page.Ttl_wo_ns()), page);
//				}
//				wiki.Db_mgr().Load_mgr().Load_by_ttls(Cancelable_.Never, hash, true, 0, ttls_len);
//				String[] rv = new String[ttls_len];
//				for (int i = 0; i < ttls_len; i++) {
//					Xodb_page page = (Xodb_page)hash.FetchAt(i);
//					rv[i] = page.Exists() ? "1" : "0";
//				}
//				return rv;
//			} catch (Exception e) {Err_.Noop(e); return null;}
//		}	
	private String Get_search_suggestions(GfoMsg m) {
		Xow_wiki wiki = app.Gui_mgr().Main_win().Page().Wiki();
		byte[] search_str = ByteAry_.new_utf8_((String)m.ReadValAt(0));
		byte[] cbk_func = ByteAry_.new_utf8_((String)m.ReadValAt(1));
//			byte[] sender = null;
//			if (m.Args_count() > 2) sender = ByteAry_.new_utf8_((String)m.ReadValAt(2));
		app.Gui_mgr().Search_suggest_mgr().Search(wiki, search_str, cbk_func);
		return "";
	}
	private String[] Wikidata_get_label(GfoMsg m) {
		try {
			gplx.xowa.xtns.wdatas.Wdata_wiki_mgr wdata_mgr = app.Wiki_mgr().Wdata_mgr();
			wdata_mgr.Wdata_wiki().Init_assert();	// NOTE: must assert else ns_mgr won't load Property
			int len = m.Args_count();
			if (len < 1) return null;
			byte[][] langs = ByteAry_.Split(m.Args_getAt(0).Val_to_bry(), Byte_ascii.Semic);
			int langs_len = langs.length;
			String[] rv = new String[len - 1];
			for (int i = 1; i < len; i++) {
				try {
					byte[] ttl_bry = m.Args_getAt(i).Val_to_bry();
					gplx.xowa.xtns.wdatas.Wdata_doc page = wdata_mgr.Pages_get(ttl_bry); if (page == null) continue;
					for (int j = 0; j < langs_len; j++) {
						byte[] lang_key = langs[j];
						if		(ByteAry_.Eq(lang_key, Wikidata_get_label_xowa_ui_lang))
							lang_key = app.Sys_cfg().Lang();
						byte[] val_bry = null;
						if		(ByteAry_.Eq(lang_key, Wikidata_get_label_xowa_title))
							val_bry = ttl_bry;
						else {
							val_bry = page.Label_list_get(lang_key);
						}
						if (val_bry == null) continue;
						rv[i - 1] = String_.new_utf8_(val_bry);
						break;
					}
				}	catch (Exception e) {Err_.Noop(e); rv[i] = null;}
				finally {}
			}
			return rv;
		} catch (Exception e) {Err_.Noop(e); return null;}
	}
	private String Scripts_exec(GfoMsg m) {
		Object rv = null;
		try {
			rv = app.Gfs_mgr().Run_str(m.Args_getAt(0).Val_to_str_or_empty());
		}
		catch (Exception e) {Err_.Noop(e); return null;}
		return Object_.XtoStr_OrEmpty(rv);
	}
	private Xop_root_tkn root = new Xop_root_tkn();
	private ByteAryBfr bfr = ByteAryBfr.reset_(255);
	private static final byte[] Wikidata_get_label_xowa_ui_lang = ByteAry_.new_ascii_("xowa_ui_lang"), Wikidata_get_label_xowa_title = ByteAry_.new_ascii_("xowa_title");
}

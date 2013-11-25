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
import gplx.xowa.wikis.*; import gplx.xowa.html.*; import gplx.xowa.xtns.wdatas.*;
public class Xou_output_wkr implements ByteAryFmtrArg {		
	public Xou_output_wkr(byte output_tid, boolean raw_text, boolean wiki_text) {this.output_tid = output_tid; this.raw_text = raw_text; this.wiki_text = wiki_text;} private byte output_tid; boolean wiki_text;
	public boolean Raw_text() {return raw_text;} private boolean raw_text;
	private static final byte[] Content_editable_bry = ByteAry_.new_ascii_(" contenteditable=\"true\"");
	public Wdata_xwiki_link_wtr Wdata_lang_wtr() {return wtr_page_lang;} Wdata_xwiki_link_wtr wtr_page_lang = new Wdata_xwiki_link_wtr();
	Xoh_wiki_article mgr;
	public Xou_output_wkr Page_(Xoa_page v) {this.page = v; return this;}
	public Xou_output_wkr Mgr_(Xoh_wiki_article v) {this.mgr = v; return this;}
	public byte[] Bld_bry(Xoh_wiki_article mgr, Xoa_page page, ByteAryBfr bfr) {
		this.mgr = mgr;
		this.page = page; 			
		Xow_wiki wiki = page.Wiki(); Xoa_app app = wiki.App();
		wiki.Ctx().Page_(page); // HACK: must update page for toc_mgr; WHEN: Xoa_page rewrite
		log_wtr.Write(page, app.Msg_log());
		ByteAryFmtr fmtr = null;
		if (mgr.Html_capable()) {
			wtr_page_lang.Page_(page);
			byte view_tid = output_tid;
			switch (output_tid) {
				case Xoh_wiki_article.Tid_view_edit:	fmtr = mgr.Page_edit_fmtr(); break;
				case Xoh_wiki_article.Tid_view_html:	fmtr = mgr.Page_read_fmtr(); view_tid = Xoh_wiki_article.Tid_view_read; break; // set view_tid to read, so that "read" is highlighted in HTML
				case Xoh_wiki_article.Tid_view_read:	fmtr = mgr.Page_read_fmtr(); break;
			}
			Fmt(app, wiki, mgr, page, view_tid, bfr, fmtr, this);
			if (output_tid == Xoh_wiki_article.Tid_view_html)
				Fmt(app, wiki, mgr, page, output_tid, bfr, mgr.Page_html_fmtr(), String_.Replace(String_.Replace(String_.Replace(bfr.XtoStrAndClear(), "&", "&amp;"), "\"", "&quot;"), "<", "&lt;"));
			wtr_page_lang.Page_(null);
		}
		else
			XferAry(bfr, 0);
		this.page = null;
		return bfr.XtoAryAndClear();
	}	private Xoa_page page; ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255); App_log_wtr_html log_wtr = new App_log_wtr_html();
	private static byte[] Bry_mathjax_script, Bry_mathjax_script_bgn = ByteAry_.new_ascii_("<script src=\""), Bry_mathjax_script_end = ByteAry_.new_ascii_("bin/any/javascript/xowa/mathjax/xowa_mathjax.js\"></script>");
	private static byte[] Bry_xowa_root_dir;
	public static byte[] Page_name(ByteAryBfr tmp_bfr, Xoa_ttl ttl, byte[] display_ttl) {
		if (display_ttl != null) return display_ttl;	// display_ttl explicitly set; use it
		if (ttl.Ns().Id() == Xow_ns_.Id_special) {		// special: omit query args, else excessively long titles: EX:"Special:Search/earth?fulltext=y&xowa page index=1"
			tmp_bfr.Add(ttl.Ns().Name_txt_w_colon()).Add(ttl.Page_txt_wo_qargs());
			return tmp_bfr.XtoAryAndClear();
		}
		else
			return ttl.Full_txt();						// NOTE: include ns with ttl as per defect d88a87b3
	}
	private void Fmt(Xoa_app app, Xow_wiki wiki, Xoh_wiki_article mgr, Xoa_page page, byte view_tid, ByteAryBfr bfr, ByteAryFmtr fmtr, Object page_data) {
		DateAdp page_modified_on_dte = page.Page_date();
		byte[] page_modified_on_msg = page.Lang().Msg_mgr().Val_by_id_args(Xol_msg_itm_.Id_portal_lastmodified, tmp_bfr, page_modified_on_dte.XtoStr_fmt_yyyy_MM_dd(), page_modified_on_dte.XtoStr_fmt_HHmm());
		byte[] html_content_editable = wiki.Gui_mgr().Cfg_browser().Content_editable() ? Content_editable_bry : ByteAry_.Empty;
		byte[] page_redirected = Build_redirect_msg(app, wiki, page);
		if (Bry_xowa_root_dir == null)
			Bry_xowa_root_dir = wiki.App().Url_converter_fsys().Encode_http(app.Fsys_mgr().Root_dir());
		byte[] js_mathjax_script = ByteAry_.Empty;
		if (app.File_mgr().Math_mgr().Renderer_is_mathjax() && app.File_mgr().Math_mgr().Enabled()) {
			if (Bry_mathjax_script == null) {
				tmp_bfr.Add(Bry_mathjax_script_bgn).Add(Bry_xowa_root_dir).Add(Bry_mathjax_script_end);
				Bry_mathjax_script = tmp_bfr.XtoAryAndClear();
			}
			js_mathjax_script = Bry_mathjax_script;
		}
		Xowh_portal_mgr portal_mgr = wiki.Html_mgr().Portal_mgr().Init_assert();
		Xol_lang lang = page.Lang();
		byte[] js_wikidata_bry = Wdata_wiki_mgr.Wiki_page_is_json(wiki.Wiki_tid(), page.Page_ttl().Ns().Id()) ? app.User().Lang().Fragment_mgr().Html_js_wikidata() : ByteAry_.Empty;
		byte[] js_edit_toolbar_bry = view_tid == Xoh_wiki_article.Tid_view_edit ? wiki.Fragment_mgr().Html_js_edit_toolbar() : ByteAry_.Empty;
		byte[] css_xtn = app.Ctg_mgr().Missing_ctg_cls_css();
		if (app.Html_mgr().Page_mgr().Font_enabled())
			css_xtn = ByteAry_.Add(css_xtn, app.Html_mgr().Page_mgr().Font_css_bry());
		css_xtn = ByteAry_.Add(css_xtn, app.Gui_mgr().Html_mgr().Css_xtn());
		fmtr.Bld_bfr_many(bfr
		, Page_name(tmp_bfr, page.Page_ttl(), null)								// NOTE: page_name does not show display_title (<i>). always pass in null
		, Page_name(tmp_bfr, page.Page_ttl(), wiki.Ctx().Tab().Display_ttl())
		, page_redirected, page_data, wtr_page_lang, page_modified_on_msg, lang.Dir_bry(), log_wtr.Html()
		, mgr.Css_common_bry(), mgr.Css_wiki_bry(), css_xtn, html_content_editable
		, portal_mgr.Div_personal_bry(), portal_mgr.Div_ns_bry(app.Utl_bry_bfr_mkr(), page.Page_ttl(), wiki.Ns_mgr()), portal_mgr.Div_view_bry(app.Utl_bry_bfr_mkr(), view_tid, page.Search_text())
		, portal_mgr.Div_logo_bry(), portal_mgr.Div_home_bry(), portal_mgr.Div_wikis_bry(app.Utl_bry_bfr_mkr()), portal_mgr.Sidebar_mgr().Html_bry()
		, mgr.Edit_rename_div_bry(), page.Data_preview()
		, Xoa_app_.Version, Xoa_app_.Build_date, Bry_xowa_root_dir, js_mathjax_script, wiki.Fragment_mgr().Html_js_table(), js_wikidata_bry, js_edit_toolbar_bry, app.Server().Running_str()
		);
	}
	private byte[] Build_redirect_msg(Xoa_app app, Xow_wiki wiki, Xoa_page page) {
		ListAdp list = page.Redirect_list();
		int list_len = list.Count();
		if (list_len == 0) return ByteAry_.Empty;
		ByteAryBfr redirect_bfr = app.Utl_bry_bfr_mkr().Get_b512();
		for (int i = 0; i < list_len; i++) {
			if (i != 0) redirect_bfr.Add(Bry_redirect_dlm);
			byte[] redirect_ttl = (byte[])list.FetchAt(i);
			redirect_bfr.Add(Xoh_consts.A_bgn)		// '<a href="'
				.Add(Xoh_href_parser.Href_wiki_bry)		// '/wiki/'
				.Add(redirect_ttl)						// 'PageA'
				.Add(Bry_redirect_arg)					// ?redirect=no
				.Add(Xoh_consts.A_bgn_lnki_0)			// '" title="'
				.Add(redirect_ttl)						// 'PageA'
				.Add(Xoh_consts.__end_quote)			// '">'
				.Add(redirect_ttl)						// 'PageA'
				.Add(Xoh_consts.A_end)				// </a>
				;
		}
		Xol_msg_itm msg_itm = wiki.Lang().Msg_mgr().Itm_by_id_or_null(Xol_msg_itm_.Id_redirectedfrom);
		ByteAryBfr fmt_bfr = app.Utl_bry_bfr_mkr().Get_b512();
		app.Tmp_fmtr().Fmt_(msg_itm.Val()).Bld_bfr_one(fmt_bfr, redirect_bfr);
		redirect_bfr.Clear().Mkr_rls(); fmt_bfr.Mkr_rls();
		return fmt_bfr.XtoAryAndClear();
	}	static byte[] Bry_redirect_dlm = ByteAry_.new_ascii_(" <--- "), Bry_redirect_arg = ByteAry_.new_ascii_("?redirect=no");
	public void XferAry(ByteAryBfr bfr, int idx) {
		byte[] data_raw = page.Data_raw();
		if (wiki_text) {
			if (mgr.Html_capable())
				Xoh_html_wtr.Bfr_escape(bfr, data_raw, 0, data_raw.length, this.page.Wiki().App(), false, false);	// NOTE: must escape; assume that browser will automatically escape (&lt;) (which Mozilla does)
			else
				bfr.Add(data_raw);
			return;
		}
		Xow_wiki wiki = page.Wiki();
		Xoa_app app = wiki.App();
		int ns_id = page.Page_ttl().Ns().Id();
		int bfr_page_bgn = bfr.Bry_len();
		byte page_tid = Xow_page_tid.Identify(wiki.Wiki_tid(), ns_id, page.Page_ttl().Page_db());
		boolean page_tid_uses_pre = false;
		switch (page_tid) {
			case Xow_page_tid.Tid_js:
			case Xow_page_tid.Tid_css:
			case Xow_page_tid.Tid_lua:
				Xoh_html_wtr.Bfr_escape(tmp_bfr, data_raw, 0, data_raw.length, page.Wiki().App(), false, false);
				app.Html_mgr().Page_mgr().Content_code_fmtr().Bld_bfr_many(bfr, tmp_bfr);
				tmp_bfr.Clear();
				page_tid_uses_pre = true;
				break;
			case Xow_page_tid.Tid_json:
				app.Wiki_mgr().Wdata_mgr().Write_json_as_html(bfr, data_raw);
				break;
			case Xow_page_tid.Tid_wikitext:
				if	(ns_id == Xow_ns_.Id_file)		// if File ns, add boilerplate header
					app.File_main_wkr().Bld_html(wiki, bfr, page.Page_ttl(), wiki.Cfg_file_page(), page.File_queue());
				if (wiki.Html_mgr().Tidy_enabled())
					Tidy(wiki, bfr);
				wiki.Html_wtr().Write_all(page.Wiki().Ctx(), page.Root(), page.Root().Data_mid(), bfr);
				if (ns_id == Xow_ns_.Id_category)	// if Category, render rest of html (Subcategories; Pages; Files); note that a category may have other html which requires wikitext processing
					wiki.Html_mgr().Ns_ctg().Bld_html(page, bfr);
				int ctgs_len = page.Category_list().length;	// add Categories
				if (ctgs_len > 0) {
					app.Usr_dlg().Prog_many("", "", "loading categories: count=~{0}", ctgs_len);
					if (app.Ctg_mgr().Pagecats_grouping_enabled())
						app.Ctg_mgr().Pagectgs_wtr().Write(bfr, wiki, page);
					else
						wiki.Html_mgr().Ctg_mgr().Bld(bfr, page, ctgs_len);
				}
				break;
		}
		if (	wiki.Wiki_tid() != Xow_wiki_type_.Tid_home	// allow home wiki to use javascript
			&&  !page_tid_uses_pre) {						// if .js, .css or .lua, skip test; may have js fragments, but entire text is escaped and put in pre; don't show spurious warning; DATE:2013-11-21
			int bfr_page_end = bfr.Bry_len();
			byte[] cleaned = app.Utl_js_cleaner().Clean(wiki, bfr.Bry(), bfr_page_bgn, bfr_page_end);
			if (cleaned != null) {
				bfr.Del_by(bfr_page_end - bfr_page_bgn);
				bfr.Add(cleaned);
				app.Usr_dlg().Warn_many("", "", "javascript detected: wiki=~{0} ~{1}", wiki.Key_str(), String_.new_utf8_(page.Page_ttl().Full_txt()));
			}
		}
	}
	private void Tidy(Xow_wiki wiki, ByteAryBfr bfr) {
		Bry_bfr_mkr bfr_mkr = page.Wiki().App().Utl_bry_bfr_mkr();
		ByteAryBfr tmp_src_bfr = bfr_mkr.Get_m001();
		ByteAryBfr tmp_trg_bfr = bfr_mkr.Get_m001();
		wiki.Html_wtr().Write_all(page.Wiki().Ctx(), page.Root(), page.Root().Data_mid(), tmp_src_bfr);
		wiki.App().Fsys_mgr().App_mgr().App_tidy_html().Run_tidy_html(tmp_src_bfr, tmp_trg_bfr);
		bfr.Add_bfr_and_clear(tmp_trg_bfr);
		tmp_src_bfr.Mkr_rls(); tmp_trg_bfr.Mkr_rls();		
	} 
}

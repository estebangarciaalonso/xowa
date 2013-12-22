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
package gplx.xowa.gui.wins; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
import gplx.xowa.files.*;
public class Xog_url_wkr {
	private static Xoh_href tmp_href = new Xoh_href();
	public static Xoa_url Exec_url(Xog_win win, String href) {
		if (href == null) return Rslt_handled;	// text is not link; return;
		byte[] href_bry = ByteAry_.new_utf8_(href);
		Xoa_app app = win.App(); Xoa_page page = win.Page();
		app.Href_parser().Parse(tmp_href, href_bry, page.Wiki(), page.Page_ttl().Page_url());
		switch (tmp_href.Tid()) {
			case Xoh_href.Tid_null:			return Rslt_handled;								// url is invalid; return handled (which effectively ignores)
			case Xoh_href.Tid_xowa:			return Exec_url_xowa(app);							// xowa:app.version
			case Xoh_href.Tid_http:			return Exec_url_http(app);							// http://site.org
			case Xoh_href.Tid_anchor:		return Exec_url_anchor(win);						// #anchor
			case Xoh_href.Tid_xcmd:			return Exec_url_xcmd(win);							// /xcmd/app.version
			case Xoh_href.Tid_file:			return Exec_url_file(app, page, win, href_bry);		// file:///xowa/A.png
			default:						return Exec_url_page(app, page, win, href_bry);		// Page /wiki/Page
		}
	}
	private static Xoa_url Exec_url_xowa(Xoa_app app) {		// EX: xowa:app.version
		// NOTE: must catch exception else it will bubble to SWT browser and raise secondary exception of xowa is not a registered protocol
		try {app.Gfs_mgr().Run_str(String_.new_utf8_(tmp_href.Page()));}
		catch (Exception e) {app.Gui_mgr().Kit().Ask_ok("", "", Err_.Message_gplx_brief(e));}
		return Rslt_handled;
	}
	private static Xoa_url Exec_url_http(Xoa_app app) {		// EX: http:a.org
		app.Fsys_mgr().App_mgr().Exec_view_web(tmp_href.Raw());
		return Rslt_handled;
	}
	private static Xoa_url Exec_url_anchor(Xog_win win) {	// EX: #anchor
		win.Exec_html_box_select_by_id(String_.new_utf8_(tmp_href.Anchor()));
		return Rslt_handled;
	}
	private static Xoa_url Exec_url_xcmd(Xog_win win) {		// EX: /xcmd/
		byte[] xowa_href_bry = tmp_href.Page();
		int xowa_href_bry_len = xowa_href_bry.length;
		int slash_pos = ByteAry_.FindFwd(xowa_href_bry, Byte_ascii.Slash); if (slash_pos == ByteAry_.NotFound) slash_pos = xowa_href_bry_len;
		byte[] xowa_cmd_bry = ByteAry_.Mid(xowa_href_bry, 0, slash_pos);
		String xowa_cmd_str = String_.new_utf8_(xowa_cmd_bry);
		GfoMsg m = GfoMsg_.new_cast_(xowa_cmd_str);
		if (String_.Eq(xowa_cmd_str, Xog_win.Invk_eval))
			m.Add("cmd", String_.new_utf8_(xowa_href_bry, slash_pos + 1, xowa_href_bry_len));
		win.Invk(GfsCtx.new_(), 0, xowa_cmd_str, m);
		return Rslt_handled;
	}
	private static Xoa_url Exec_url_file(Xoa_app app, Xoa_page page, Xog_win win, byte[] href_bry) {	// EX: file:///xowa/A.png
		href_bry = app.Url_converter_url().Decode(href_bry);
		String url_str = Io_url.parse_http_file(String_.new_utf8_(href_bry), Op_sys.Cur().Tid_is_wnt());
		Io_url href_url = Io_url_.new_fil_(url_str);
		String xowa_ttl = page.Wiki().Gui_mgr().Cfg_browser().Content_editable()
			? win.Html_box().Html_active_atr_get_str(gplx.xowa.html.Xoh_html_tag.Nde_xowa_title_str, null)
			: Xoh_dom_.Title_by_href(app.Url_converter_comma(), app.Utl_bry_bfr_mkr().Get_b512().Mkr_rls(), href_bry, ByteAry_.new_utf8_(win.Html_box().Html_doc_html()));
		if (!Io_mgr._.ExistsFil(href_url)) {
			Xof_xfer_itm xfer_itm = new Xof_xfer_itm();
			byte[] title = app.Url_converter_url().Decode(ByteAry_.new_utf8_(xowa_ttl));
			xfer_itm.Atrs_by_lnki(Xop_lnki_type.Id_none, -1, -1, -1, -1).Atrs_by_ttl(title, ByteAry_.Empty);
			page.Wiki().File_mgr().Find_meta(xfer_itm);
			page.File_queue().Clear();
			page.File_queue().Add(xfer_itm);	// NOTE: set elem_id to "impossible" number, otherwise it will auto-update an image on the page with a super-large size; [[File:Alfred Sisley 062.jpg]]
			page.Wiki().File_mgr().Repo_mgr().Xfer_mgr().Force_orig_y_();
			page.File_queue().Exec(Xof_exec_tid.Tid_viewer_app, win.Gui_wtr(), page.Wiki());
			page.Wiki().File_mgr().Repo_mgr().Xfer_mgr().Force_orig_n_();
		}
		if (Io_mgr._.ExistsFil(href_url)) {
			ProcessAdp media_player = app.Fsys_mgr().App_mgr().App_by_ext(href_url.Ext());
			media_player.Run(href_url);
		}
		return Rslt_handled;
	}
	private static Xoa_url Exec_url_page(Xoa_app app, Xoa_page page, Xog_win win, byte[] href_bry) {	// EX: "Page"; "/wiki/Page"
		// HACK: code to reparse href in wiki; EX: (1) goto w:Anything; (2) click on "anything" in wikt; "anything" will be parsed by en.wiki's rules, not en.wikt; DATE:2013-01-30
		Xow_wiki tmp_href_wiki = app.Wiki_mgr().Get_by_key_or_make(tmp_href.Wiki());
		tmp_href_wiki.Init_assert();
		app.Href_parser().Parse(tmp_href, href_bry, tmp_href_wiki, page.Page_ttl().Page_url());
		Xoa_url rv = new Xoa_url();
		app.Url_parser().Parse(rv, href_bry);
		rv.Wiki_bry_(tmp_href.Wiki());
		if (!rv.Redirect_force()) {					// HACK: the href's parser page is accurate. it will pick up "en.wiki.org" and "en.wiki.org/wiki" as references to main page; however it does not handle query parameters, and will return "A?redirect=no" as page name; the url parser handles query params
//				if (!page.Page_ttl().Ns().Id_ctg()) {	
			Xoa_ttl tmp_ttl = Xoa_ttl.parse_(tmp_href_wiki, tmp_href.Page());	// HACK: make ttl to get "real title" with subpage; else links like en.wikipedia.org/wiki/Page:A/B will not work  DATE:2013-06-22
			rv.Page_bry_(tmp_ttl.Full_txt_wo_qarg());	// HACK: Category has args like ?from=; do not set Page else Page will be "Category:A?from=B" (needs to fix this hack); DATE:2013-05-01; DATE:2013-06-22
//				}
//				else {									// HACK: remove anchor from args
				for (int i = 0; i < rv.Args().length; i++) {
					Gfo_url_arg arg = rv.Args()[i];
					int anchor_pos = ByteAry_.FindBwd(arg.Val_bry(), Byte_ascii.Hash);	// NOTE: must .FindBwd to handle Category args like de.wikipedia.org/wiki/Kategorie:Begriffskl%C3%A4rung?pagefrom=#::12%20PANZERDIVISION#mw-pages; DATE:2013-06-18
					if (anchor_pos != ByteAry_.NotFound)
						arg.Val_bry_(ByteAry_.Mid(arg.Val_bry(), 0, anchor_pos));
				}
//				}
			if (tmp_href.Anchor() != null) {
				byte[] anchor_bry = app.Url_converter_id().Encode(tmp_href.Anchor());	// reencode for anchors (which use . encoding, not % encoding); EX.WP: Enlightenment_Spain#Enlightened_despotism_.281759%E2%80%931788.29
				rv.Anchor_bry_(anchor_bry);
			}
		}
		rv.Wiki_(app.Wiki_mgr().Get_by_key_or_make(tmp_href.Wiki()));
		return rv;
	}
	public static Xoa_url Rslt_handled = null;
}

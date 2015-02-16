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
package gplx.xowa.xtns.wdatas.specials; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wdatas.*;
import gplx.xowa.wikis.*; import gplx.xowa.specials.*;
public class Wdata_itemByTitle_page implements Xows_page {
	private Xoa_url_arg_hash arg_hash = new Xoa_url_arg_hash();
	private static final byte[] Arg_site = Bry_.new_ascii_("site"), Arg_page = Bry_.new_ascii_("page");
	public Bry_fmtr Html_fmtr() {return html_fmtr;}
	private Wdata_itemByTitle_cfg cfg;
	public void Special_gen(Xoa_url calling_url, Xoa_page page, Xow_wiki wiki, Xoa_ttl ttl) {
		if (cfg == null) cfg = (Wdata_itemByTitle_cfg)wiki.App().Special_mgr().Get_or_null(Wdata_itemByTitle_cfg.Key);
		// Special:ItemByTitle/enwiki/Earth -> www.wikidata.org/wiki/Q2
		Gfo_usr_dlg usr_dlg = wiki.App().Usr_dlg();
		byte[] site_bry = cfg.Site_default();
		byte[] page_bry = Bry_.Empty;
		byte[] raw_bry = ttl.Full_txt_wo_qarg(); 					// EX: enwiki/Earth
		int args_len = calling_url.Args().length;
		if (args_len > 0) {
			arg_hash.Load(calling_url);
			site_bry = arg_hash.Get_val_bry_or(Arg_site, Bry_.Empty);
			page_bry = arg_hash.Get_val_bry_or(Arg_page, Bry_.Empty);
		}
		int site_bgn = Bry_finder.Find_fwd(raw_bry, Xoa_ttl.Subpage_spr);
		if (site_bgn != Bry_.NotFound) {						// leaf arg is available
			int page_bgn = Bry_finder.Find_fwd(raw_bry, Xoa_ttl.Subpage_spr, site_bgn + 1);			
			int raw_bry_len = raw_bry.length;
			if (page_bgn != Bry_.NotFound && page_bgn < raw_bry_len) {	// pipe is found and not last char (EX: "enwiki/" is invalid
				site_bry = Bry_.Mid(raw_bry, site_bgn + 1, page_bgn);
				page_bry = Bry_.Mid(raw_bry, page_bgn + 1, raw_bry_len);
			}
		}
		Xoa_app app = wiki.App();
		if (Bry_.Len_gt_0(site_bry) && Bry_.Len_gt_0(page_bry))
			if (Navigate(usr_dlg, app, app.Wiki_mgr().Wdata_mgr(), page, site_bry, page_bry)) return;
		Bry_bfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_k004();
		html_fmtr.Bld_bfr_many(tmp_bfr, "Search for items by site and title", "Site", site_bry, "Page", page_bry, "Search");
		page.Data_raw_(tmp_bfr.Mkr_rls().Xto_bry_and_clear());
		page.Html_data().Html_restricted_n_();		// [[Special:]] pages allow all HTML
	}
	private static boolean Navigate(Gfo_usr_dlg usr_dlg, Xoa_app app, Wdata_wiki_mgr wdata_mgr, Xoa_page page, byte[] site_bry, byte[] page_bry) {
		page_bry = app.Encoder_mgr().Url().Decode(page_bry);				// NOTE: space is converted to + on postback to url; decode
		byte[] wiki_domain = Xow_wiki_alias.Parse_wmf_key(site_bry); 			if (wiki_domain == null) {usr_dlg.Warn_many("", "", "site_bry parse failed; site_bry:~{0}", String_.new_utf8_(site_bry)); return false;}
		Xow_wiki wiki = app.Wiki_mgr().Get_by_key_or_make(wiki_domain);		if (wiki == null) {usr_dlg.Warn_many("", "", "wiki_domain does not exist; wiki_domain:~{0}", String_.new_utf8_(wiki_domain)); return false;}
		Xoa_ttl wdata_ttl = Xoa_ttl.parse_(wiki, page_bry);					if (wdata_ttl == null) {usr_dlg.Warn_many("", "", "ttl is invalid; ttl:~{0}", String_.new_utf8_(page_bry)); return false;}
		Wdata_doc doc = wdata_mgr.Pages_get(wiki, wdata_ttl); 				if (doc == null) {usr_dlg.Warn_many("", "", "ttl cannot be found in wikidata; ttl:~{0}", String_.new_utf8_(wdata_ttl.Raw())); return false;}		
		byte[] qid_bry = doc.Qid();
		Xoa_page qid_page = wdata_mgr.Wdata_wiki().Data_mgr().Redirect(page, qid_bry); 	if (qid_page.Missing()) {usr_dlg.Warn_many("", "", "qid cannot be found in wikidata; qid:~{0}", String_.new_utf8_(qid_bry)); return false;}
		return true;
	}
	private static Bry_fmtr html_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl
	(	"<div id=\"mw-content-text\">"
	,	"<form method=\"get\" action=\"//www.wikidata.org/wiki/Special:ItemByTitle\" name=\"itembytitle\" id=\"wb-itembytitle-form1\">"
	,	"<fieldset>"
	,	"<legend>~{legend}</legend>"
	,	"<label for=\"wb-itembytitle-sitename\">~{site_lbl}:</label>"
	,	"<input id=\"wb-itembytitle-sitename\" size=\"12\" name=\"site\" value=\"~{site_val}\" accesskey=\"s\" />"
	,	""
	,	"<label for=\"pagename\">~{page_lbl}:</label>"
	,	"<input id=\"pagename\" size=\"36\" class=\"wb-input-text\" name=\"page\" value=\"~{page_val}\" accesskey=\"p\" />"
	,	""
	,	"<input id=\"wb-itembytitle-submit\" class=\"wb-input-button\" type=\"submit\" value=\"~{search_lbl}\" name=\"submit\" />"
	,	"</fieldset>"
	,	"</form>"
	,	"</div>"
	,	"<br>To change the default site, see <a href='/site/home/wiki/Help:Options/Wikibase'>Help:Options/Wikibase</a>"
	)
	, 	"legend", "site_lbl", "site_val", "page_lbl", "page_val", "search_lbl");
}

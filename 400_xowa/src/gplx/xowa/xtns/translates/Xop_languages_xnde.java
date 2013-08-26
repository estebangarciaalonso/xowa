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
package gplx.xowa.xtns.translates; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.langs.*;
public class Xop_languages_xnde implements Xop_xnde_xtn {
	public boolean Xtn_literal() {return false;}
	public Xop_root_tkn Xtn_root() {return null;}
	public Xop_xnde_tkn Xnde() {return xnde;} private Xop_xnde_tkn xnde;
	public void Xtn_compile(Xop_ctx ctx, Xow_wiki wiki, Xop_tkn_mkr tkn_mkr, byte[] src, Xop_xnde_tkn xnde) {
		this.xnde = xnde;
		langs = Find_lang_pages(ctx, wiki);
	}
	public ListAdp Langs() {return langs;} private ListAdp langs;
	public Xoa_ttl Root_ttl() {return root_ttl;} private Xoa_ttl root_ttl;
	private Xoa_ttl Root_ttl_of(Xow_wiki wiki, Xoa_ttl ttl) {
		byte[] page_bry = ttl.Page_db();
		int slash_pos = ByteAry_.FindBwd(page_bry, Xoa_ttl.Subpage_spr);
		if (slash_pos == ByteAry_.NotFound) return ttl;
		byte[] root_bry = ByteAry_.Mid(page_bry, 0, slash_pos);
		return Xoa_ttl.parse_(wiki, ttl.Ns().Id(), root_bry);
	}
	private ListAdp Find_lang_pages(Xop_ctx ctx, Xow_wiki wiki) {
		this.root_ttl = Root_ttl_of(wiki, ctx.Page().Page_ttl());
		ListAdp rslts = ListAdp_.new_(); 
		IntRef rslt_count = IntRef.new_(0);
		Xow_ns page_ns = root_ttl.Ns();
		wiki.Db_mgr().Load_mgr().Load_ttls_starting_with(Cancelable_.Never, rslts, null, null, rslt_count, page_ns, root_ttl.Page_db(), Int_.MaxValue, 0, Int_.MaxValue, true, false);
		int len = rslt_count.Val();
		if (len == 0) return ListAdp_.Null;				// no lang pages; return;
		ListAdp rv = ListAdp_.new_();
		byte[] root_ttl_bry = root_ttl.Page_db();		// get root_ttl_bry; do not use ns
		int lang_bgn = root_ttl_bry.length + 1;			// lang starts after /; EX: "Page" will have subpage of "Page/fr" and lang_bgn of 5
		boolean english_needed = true;
		for (int i = 0; i < len; i++) {
			Xodb_page page = (Xodb_page)rslts.FetchAt(i);
			byte[] page_ttl_bry = page.Ttl_wo_ns();
			int page_ttl_bry_len = page_ttl_bry.length;
			if 		(ByteAry_.Eq(root_ttl_bry, page_ttl_bry)) continue; 	// ignore self; EX: "page"
			if 		(lang_bgn < page_ttl_bry_len 							// guard against out of bounds
				&& 	page_ttl_bry[lang_bgn - 1] == Xoa_ttl.Subpage_spr		// prv char must be /; EX: "Page/fr"
				) {
				byte[] lang_key = ByteAry_.Mid(page_ttl_bry, lang_bgn, page_ttl_bry_len);
				if (ByteAry_.Eq(lang_key, Xol_lang_.Key_en))			// lang is english; mark english found;
					english_needed = false;
				Xol_lang_itm lang_itm = Xol_lang_itm_.Get_by_key(lang_key);
				if (lang_itm == null) continue; // not a known lang
				rv.Add(lang_itm);
			}
		}
		if (rv.Count() == 0) return ListAdp_.Null;	// no lang items; handles situations where just "Page" is returned
		if (english_needed)	// english not found; always add; handles situations wherein Page/fr and Page/de added, but not Page/en
			rv.Add(Xol_lang_itm_.Get_by_key(Xol_lang_.Key_en));
		rv.SortBy(Xol_lang_itm_sorter_by_key._);
		return rv;
	}
	public static void To_html(Xoh_html_wtr wtr, Xop_ctx ctx, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		Xop_languages_xnde languages_nde = (Xop_languages_xnde)xnde.Xnde_data();
		if (languages_nde.Langs().Count() == 0) return;	// no langs; don't write anything;
		fmtr_mgr_itms.Init(languages_nde.Langs(), ctx.Wiki(), languages_nde.Root_ttl(), ctx.Page().Lang().Key_bry());
		fmtr_all.Bld_bfr_many(bfr, "Other languages", fmtr_mgr_itms);
	}
	private static final Xop_languages_fmtr fmtr_mgr_itms = new Xop_languages_fmtr();
	public static final ByteAryFmtr fmtr_all = ByteAryFmtr.new_(String_.Concat_lines_nl
	(	"<table>"
	,	"  <tbody>"
	,	"    <tr valign=\"top\">"
	,	"		<td class=\"mw-pt-languages-label\">~{other_languages_hdr}:</td>"
	,	"       <td class=\"mw-pt-languages-list\">~{language_itms}"
	,	"       </td>"
	,	"    </tr>"
	,	"  </tbody>"
	,	"</table>"
	), "other_languages_hdr", "language_itms")
	,	fmtr_itm_basic = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	""
	,	"         <a href=\"~{anchor_href}\" title=\"~{anchor_title}\">~{anchor_text}</a>&#160;•"		
	), "anchor_href", "anchor_title", "anchor_text")
	,	fmtr_itm_english = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	""
	,	"         <a href=\"~{anchor_href}\" title=\"~{anchor_title}\"><span class=\"mw-pt-languages-ui\">~{anchor_text}</span></a>&#160;•"
	), "anchor_href", "anchor_title", "anchor_text")
	,	fmtr_itm_selected = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	""
	,	"         <span class=\"mw-pt-languages-selected\">~{anchor_text}</span>&#160;•"
	), "anchor_href", "anchor_title", "anchor_text")
	;
	// "<img src=\"//bits.wikimedia.org/static-1.22wmf9/extensions/Translate/resources/images/prog-1.png\" alt=\"~{img_alt}\" title=\"~{img_title}\" width=\"9\" height=\"9\" />&#160;•&#160;‎"
	// , "img_alt", "img_title"
}
class Xop_languages_fmtr implements ByteAryFmtrArg {
	public void Init(ListAdp langs, Xow_wiki wiki, Xoa_ttl root_ttl, byte[] cur_lang) {
		this.langs = langs;
		this.wiki = wiki;
		this.root_ttl = root_ttl;
		this.cur_lang = cur_lang;
	}	private ListAdp langs; private Xow_wiki wiki; private Xoa_ttl root_ttl; private byte[] cur_lang;
	public void XferAry(ByteAryBfr bfr, int idx) {
		int len = langs.Count();
		Xoh_href_parser parser = wiki.App().Href_parser();
		int ns_id = root_ttl.Ns().Id();
		byte[] root_ttl_bry = root_ttl.Page_db();	// NOTE: do not use .Full(); ns will be added in Xoa_ttl.parse below
		for (int i = 0; i < len; i++) {
			Xol_lang_itm lang = (Xol_lang_itm)langs.FetchAt(i);
			byte[] lang_key = lang.Key();
			boolean lang_is_en = ByteAry_.Eq(lang_key, Xol_lang_.Key_en);
			byte[] lang_ttl_bry = lang_is_en ? root_ttl_bry : ByteAry_.Add_w_dlm(Xoa_ttl.Subpage_spr, root_ttl_bry, lang_key);
			Xoa_ttl lang_ttl = Xoa_ttl.parse_(wiki, ns_id, lang_ttl_bry);
			byte[] lang_href = parser.Build_to_bry(lang_ttl, wiki);
			byte[] lang_title = Xoh_html_wtr.Ttl_to_title(lang_ttl.Full_txt());
			ByteAryFmtr fmtr = null;
			if		(ByteAry_.Eq(lang_key, Xol_lang_.Key_en)) 	fmtr = Xop_languages_xnde.fmtr_itm_english;
			else if	(ByteAry_.Eq(lang_key, cur_lang))			fmtr = Xop_languages_xnde.fmtr_itm_selected;
			else 												fmtr = Xop_languages_xnde.fmtr_itm_basic;
			fmtr.Bld_bfr_many(bfr, lang_href, lang_title, lang.Local_name());
		}
	}
}

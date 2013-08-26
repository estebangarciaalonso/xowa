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
package gplx.xowa.xtns.dynamicPageList; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xtn_dynamicPageList_nde implements Xop_xnde_xtn, Xop_xnde_atr_parser, gplx.lists.ComparerAble {
	public Xop_root_tkn Xtn_root() {return null;}
	public boolean Xtn_literal() {return false;}
	ListAdp category_includes = ListAdp_.new_(), category_excludes = ListAdp_.new_();
	int ns_filter = Int_.MinValue;	// NOTE: MinValue means disabled
	int count = Int_.MinValue;
	int offset = Int_.MinValue;
//		byte[] ns_include = null;
	boolean nofollow, suppress_errors, show_ns; //ctg_date = false, ctg_date_strip = false;
	byte sort_ascending = Bool_.__byte; 
	boolean gallery_filesize, gallery_filename; int gallery_imgs_per_row, gallery_img_width, gallery_img_height; byte[] gallery_caption;
//		byte[] ctg_date_fmt;
	byte redirects_mode = Redirect_mode_unknown;
	Xow_wiki wiki = null;
	public void Xtn_html(Xoh_html_wtr html_wtr, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		Html_mode_(Key_unordered);
		int itms_len = itms.length;
		if (itms_len == 0) {
			if (!suppress_errors)
				bfr.Add_str("Error: No results!");
			return;
		}
		int itms_bgn = 0;
		if (offset != Int_.MinValue) {
			itms_bgn = offset;
		}
		if (count != Int_.MinValue && itms_bgn + count < itms_len) {
			itms_len = itms_bgn + count;
			//if (itms_len - itms_bgn > count) 
		}
		bfr.Add(html_grp_bgn).Add_byte_nl();
		for (int i = itms_bgn; i < itms_len; i++) {
			Xtn_dynamicPageList_itm itm = itms[i];
			byte[] ttl_page_txt = itm.Ttl_bry();
			if (ttl_page_txt == null) continue;	// NOTE: apparently DynamicPageList allows null pages; DATE:2013-07-22
			switch (html_mode) {
				case Html_mode_list_ul:
				case Html_mode_list_ol:
					bfr.Add(Xoh_consts.Space_2).Add(html_itm_bgn).Add(Xoh_consts.A_bgn);
					bfr.Add_str("/wiki/").Add(ttl_page_txt);
					bfr.Add(Xoh_consts.A_bgn_lnki_0).Add(ttl_page_txt).Add_byte(Byte_ascii.Quote);
					if (nofollow) bfr.Add(Bry_nofollow);
					bfr.Add_byte(Byte_ascii.Gt);
					bfr.Add(ttl_page_txt);
					bfr.Add(Xoh_consts.A_end).Add(html_itm_end).Add_byte_nl();
					break;
				default:
					break;
			}
		}
		bfr.Add(html_grp_end).Add_byte_nl();
	}
	private static byte[] Bry_nofollow = ByteAry_.new_ascii_(" rel=\"nofollow\"");  
	public void Xtn_compile(Xop_ctx ctx, Xow_wiki wiki, Xop_tkn_mkr tkn_mkr, byte[] src, Xop_xnde_tkn xnde) {
		this.wiki = wiki;
		int bgn = xnde.Tag_open_end(), end = xnde.Tag_close_bgn();
		int pos = bgn; byte dlm_row = Byte_ascii.NewLine, dlm_fld = Byte_ascii.Eq;
		int fld_bgn = bgn;
		byte key_id = 0;
		while (true) {
			boolean done = pos >= end;
			byte b = done ? dlm_row : src[pos];
			if 		(b == dlm_fld) {
				Object o = keys.Get_by_mid(src, fld_bgn, pos);
				if (o == null) {
					wiki.App().Usr_dlg().Warn_many(GRP_KEY, "unknown_key", "unknown_key: ~{0}", String_.new_utf8_(src, fld_bgn, pos));
					fld_bgn = ByteAry_.FindFwd(src, Byte_ascii.NewLine);
					if (fld_bgn == ByteAry_.NotFound) break; 
				}
				else {
					key_id = ((ByteVal)o).Val();
					fld_bgn = pos + Int_.Const_dlm_len;
				}
			}
			else if (b == dlm_row) {
				if (fld_bgn != pos) {	// ignore blank line
					byte[] val = ByteAry_.Mid(src, fld_bgn, pos);
					Process(key_id, val);
				}
				fld_bgn = pos + Int_.Const_dlm_len;
			}
			if (done) break;
			++pos;
		}
		ListAdp list = ListAdp_.new_();
		int ctg_includes_len = category_includes.Count();
//			if (ns_filter != Int_.MinValue && ctg_includes_len == 0) {
//				wiki.Hive_mgr().
//			}
		gplx.xowa.ctgs.Xoctg_view_ctg view_ctg = new gplx.xowa.ctgs.Xoctg_view_ctg();
		for (int i = 0; i < ctg_includes_len; i++) {
			byte[] ctg = (byte[])category_includes.FetchAt(i);
			wiki.Db_mgr().Load_mgr().Load_ctg_v1(view_ctg, ctg);
			for (byte k = 0; k < gplx.xowa.ctgs.Xoa_ctg_mgr.Tid__max; k++) {
				gplx.xowa.ctgs.Xoctg_view_grp grp = view_ctg.Grp_by_tid(k);
				ListAdp ctg_itms = grp.Itms_list();
				if (ctg_itms.Count() == 0) continue;
				ListAdp pages = ListAdp_.new_();
				for (int m = 0; m < ctg_itms.Count(); m++) {
					gplx.xowa.ctgs.Xoctg_view_itm itm = (gplx.xowa.ctgs.Xoctg_view_itm)ctg_itms.FetchAt(m);
					Xodb_page page = new Xodb_page().Id_(itm.Id()).Text_len_(itm.Page_size());
					pages.Add(page);
				}
				pages.SortBy(Xodb_page_sorter.IdAsc);
				wiki.Db_mgr().Load_mgr().Load_ttls_by_ids(Cancelable_.Never, pages, 0, pages.Count());
				int pages_len = pages.Count();
				for (int j = 0; j < pages_len; j++) {
					Xodb_page ttl_itm = (Xodb_page)pages.FetchAt(j);
					list.Add(new Xtn_dynamicPageList_itm().Ttl_(ttl_itm.Ttl_wo_ns()));
				}
			}
		}
		itms = (Xtn_dynamicPageList_itm[])list.XtoAry(Xtn_dynamicPageList_itm.class);
		if (sort_ascending != Bool_.__byte)
			Array_.Sort(itms, this);
	}
	public Xtn_dynamicPageList_itm[] Itms() {return itms;} Xtn_dynamicPageList_itm[] itms;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {
	}
	void Process(byte key_id, byte[] val) {
		switch (key_id) {
			case Key_category: 				category_includes.Add(val); break;
			case Key_notcategory:		 	category_excludes.Add(val); break;
			case Key_ns:		 			{Xow_ns ns = (Xow_ns)wiki.Ns_mgr().Trie_match_exact(val, 0, val.length); ns_filter = ns == null ? Xow_ns_.Id_main : ns.Id(); break;}
			case Key_order:					sort_ascending = Keys_get_or_sort_order(val); break;
			case Key_suppresserrors:		suppress_errors = Keys_get_or_bool(val, Key_false); break;
			case Key_nofollow:				nofollow = Keys_get_or_bool(val, Key_true); break;	// NOTE: default to true; allows passing nofollow=nofollow; MW: if ('false' != $arg)
			case Key_shownamespace:			show_ns = Keys_get_or_bool(val, Key_true); break; // NOTE: default to true;
			case Key_redirects:				redirects_mode = Redirect_parse(val); break;
//			case Key_stablepages:			stable_pages = Redirect_parse(val); break;
			case Key_qualitypages:			quality_pages = Redirect_parse(val); break;
			case Key_addfirstcategorydate:	Parse_ctg_date(val); break;
			case Key_count:					count = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Key_offset:				offset = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Key_imagesperow:			gallery_imgs_per_row = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Key_imagewidth:			gallery_img_width = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Key_imageheight:			gallery_img_height = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Key_gallerycaption:		gallery_caption = val; break;	// FUTURE: parse for {{int:}}?
			case Key_galleryshowfilesize:	gallery_filesize = Keys_get_or_bool(val, Key_true); break;
			case Key_galleryshowfilename:	gallery_filename = Keys_get_or_bool(val, Key_true); break;
			case Key_ordermethod:			sort_tid = Parse_sort_tid(val); break;
		}
	}
	byte sort_tid = Sort_tid_categoryadd;
	//byte stable_pages;
	byte quality_pages;
	static final byte Sort_tid_null = 0, Sort_tid_lastedit = 1, Sort_tid_length = 2, Sort_tid_created = 3, Sort_tid_sortkey = 4, Sort_tid_categorysortkey = 5, Sort_tid_popularity = 6, Sort_tid_categoryadd = 7;
	byte Parse_sort_tid(byte[] val) {
		byte val_key = Keys_get_or(val, Key_categoryadd);
		switch (val_key) {
			case Key_lastedit: 			return Sort_tid_lastedit;
			case Key_length: 			return Sort_tid_length;
			case Key_created: 			return Sort_tid_created;
			case Key_sortkey: 			return Sort_tid_sortkey;
			case Key_categorysortkey: 	return Sort_tid_categorysortkey;
			case Key_popularity: 		return Sort_tid_popularity;	// FUTURE: default to categoryadd if counters disabled
			case Key_categoryadd: 		return Sort_tid_categoryadd;
			default:					throw Err_mgr._.unhandled_(val_key);
		}
	}
	public int compare(Object lhsObj, Object rhsObj) {
		Xtn_dynamicPageList_itm lhs = (Xtn_dynamicPageList_itm)lhsObj;
		Xtn_dynamicPageList_itm rhs = (Xtn_dynamicPageList_itm)rhsObj;
		int multiplier = sort_ascending == Bool_.Y_byte ? 1 : -1;
		switch (sort_tid) {
			case Sort_tid_categorysortkey: 		
			case Sort_tid_categoryadd: 			return multiplier * ByteAry_.Compare(lhs.Ttl_bry(), rhs.Ttl_bry()); 
		}
		return CompareAble_.Same;
	}
	void Parse_ctg_date(byte[] val) {
//			byte val_key = Keys_get_or(val, Key_false);
//			if (val_key == Key_true)
//				ctg_date = true;
//			else {
//				if (val.length == 8) { 	// HACK: preg_match( '/^(?:[ymd]{2,3}|ISO 8601)$/'
//					ctg_date = true;
//					ctg_date_fmt = val;
//					if (ctg_date_fmt.length == 2) {
//						ctg_date_strip = true;
//						ctg_date_fmt = ByteAry_.Add(ctg_date_fmt, new byte[] {Byte_ascii.Ltr_y});
//					}
//				}
//				else
//					ctg_date = false;
//			}
	}
	byte Redirect_parse(byte[] bry) {
		byte key = Keys_get_or(bry, Key_exclude);	// NOTE: key_exclude is default
		switch (key) {
			case Key_exclude: 			return Redirect_mode_exclude;
			case Key_include: 			return Redirect_mode_include;
			case Key_only: 				return Redirect_mode_only;
			default:					throw Err_mgr._.unhandled_(key);
		}
	}
	void Html_mode_(byte key) {
		switch (key) {
			case Key_gallery: 			html_mode = Html_mode_gallery; break;
			case Key_inline: 			html_mode = Html_mode_inline; break;
			case Key_none: 				html_mode = Html_mode_none; html_itm_end = Br; break;
			case Key_ordered: 			html_mode = Html_mode_list_ol; html_grp_bgn = Ol_bgn; html_grp_end = Ol_end; html_itm_bgn = Li_bgn; html_itm_end = Li_end; break;
			case Key_unordered: 		html_mode = Html_mode_list_ul; html_grp_bgn = Ul_bgn; html_grp_end = Ul_end; html_itm_bgn = Li_bgn; html_itm_end = Li_end; break;
			default:					throw Err_mgr._.unhandled_(key);
		}		
	}
	byte[] html_grp_bgn, html_grp_end, html_itm_bgn, html_itm_end;
	byte html_mode;
	private static byte[]
		Ul_bgn = ByteAry_.new_ascii_("<ul>"), Ul_end = ByteAry_.new_ascii_("</ul>")
		, Ol_bgn = ByteAry_.new_ascii_("<ol>"), Ol_end = ByteAry_.new_ascii_("</ol>")
		, Li_bgn = ByteAry_.new_ascii_("<li>"), Li_end = ByteAry_.new_ascii_("</li>")
		, Br = ByteAry_.new_ascii_("<br />")
		;
	byte Keys_get_or_sort_order(byte[] bry) {
		byte val = Keys_get_or(bry, Bool_.__byte);
		switch (val) {
			case Key_ascending: 	return Bool_.Y_byte;  
			case Key_descending: 	return Bool_.N_byte;
			default: 				return Bool_.__byte;
		}
	}
	boolean Keys_get_or_bool(byte[] bry, byte or) {
		byte key = Keys_get_or(bry, or);
		switch (key) {
			//case Key_no:
			case Key_false: return false;
			case Key_true: return true;
			default: throw Err_mgr._.unhandled_(key);
		}
	}
	byte Keys_get_or(byte[] bry, byte or) {
		Object o = keys.Get_by_bry(bry); if (o == null) {
			wiki.App().Usr_dlg().Warn_many(GRP_KEY, "unknown_val", "unknown_val: ~{0}", String_.new_utf8_(bry)); return or;}
		return ((ByteVal)o).Val();
	}
	static final byte Html_mode_null = 0, Html_mode_none = 1, Html_mode_list_ol = 2, Html_mode_list_ul = 3, Html_mode_gallery = 4, Html_mode_inline = 5;
	static final byte Redirect_mode_exclude = 0, Redirect_mode_include = 1, Redirect_mode_only = 2, Redirect_mode_unknown = Byte_.MaxValue_127;
	static final byte
	Key_category = 1
	, Key_notcategory = 2
	, Key_ns = 3
	, Key_count = 4
	, Key_offset = 5
	, Key_imagewidth = 6
	, Key_imageheight = 7
	, Key_imagesperow = 8
	, Key_mode = 9
	, Key_gallery = 10
	, Key_none = 11
	, Key_ordered = 12
	, Key_unordered = 13
	, Key_inline = 14
	, Key_gallerycaption = 15
	, Key_galleryshowfilesize = 16
	, Key_galleryshowfilename = 17
	, Key_order = 18
	, Key_ordermethod = 19
	, Key_lastedit = 20
	, Key_length = 21
	, Key_created = 22
	, Key_sortkey = 23
	, Key_categorysortkey = 24
	, Key_popularity = 25
	, Key_categoryadd = 26
	, Key_redirects = 27
	, Key_include = 28
	, Key_only = 29
	, Key_exclude = 30
	, Key_tablepages = 31
	, Key_qualitypages = 32
	, Key_addfirstcategorydate = 33
	, Key_shownamespace = 34
	, Key_googlehack = 35
	, Key_nofollow = 36
	, Key_descending = 37
	, Key_ascending = 38
	, Key_false = 39
	, Key_true = 40
	, Key_suppresserrors = 41
	;
	private static final Hash_adp_bry keys = new Hash_adp_bry(false)
	.Add_str_byteVal("category", Key_category)
	.Add_str_byteVal("notcategory", Key_notcategory)
	.Add_str_byteVal("namespace", Key_ns)
	.Add_str_byteVal("count", Key_count)
	.Add_str_byteVal("offset", Key_offset)
	.Add_str_byteVal("imagewidth", Key_imagewidth)
	.Add_str_byteVal("imageheight", Key_imageheight)
	.Add_str_byteVal("imagesperow", Key_imagesperow)
	.Add_str_byteVal("mode", Key_mode)
	.Add_str_byteVal("gallery", Key_gallery)
	.Add_str_byteVal("none", Key_none)
	.Add_str_byteVal("ordered", Key_ordered)
	.Add_str_byteVal("unordered", Key_unordered)
	.Add_str_byteVal("inline", Key_inline)
	.Add_str_byteVal("gallerycaption", Key_gallerycaption)
	.Add_str_byteVal("galleryshowfilesize", Key_galleryshowfilesize)
	.Add_str_byteVal("galleryshowfilename", Key_galleryshowfilename)
	.Add_str_byteVal("order", Key_order)
	.Add_str_byteVal("ordermethod", Key_ordermethod)
	.Add_str_byteVal("lastedit", Key_lastedit)
	.Add_str_byteVal("length", Key_length)
	.Add_str_byteVal("created", Key_created)
	.Add_str_byteVal("sortkey", Key_sortkey)
	.Add_str_byteVal("categorysortkey", Key_categorysortkey)
	.Add_str_byteVal("popularity", Key_popularity)
	.Add_str_byteVal("categoryadd", Key_categoryadd)
	.Add_str_byteVal("redirects", Key_redirects)
	.Add_str_byteVal("include", Key_include)
	.Add_str_byteVal("only", Key_only)
	.Add_str_byteVal("exclude", Key_exclude)
	.Add_str_byteVal("tablepages", Key_tablepages)
	.Add_str_byteVal("qualitypages", Key_qualitypages)
	.Add_str_byteVal("addfirstcategorydate", Key_addfirstcategorydate)
	.Add_str_byteVal("shownamespace", Key_shownamespace)
	.Add_str_byteVal("googlehack", Key_googlehack)
	.Add_str_byteVal("nofollow", Key_nofollow)
	.Add_str_byteVal("descending", Key_descending)
	.Add_str_byteVal("ascending", Key_ascending)
	.Add_str_byteVal("false", Key_false)
	.Add_str_byteVal("true", Key_true)
	.Add_str_byteVal("suppresserrors", Key_suppresserrors)
	;
	private static final String GRP_KEY = "xowa.xtn.dynamic_page_list"; 
}

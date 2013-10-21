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
package gplx.xowa.xtns.dynamicPageList; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.dbs.*; import gplx.xowa.ctgs.*;
public class Dpl_xnde implements Xop_xnde_xtn, Xop_xnde_atr_parser {
	private Xow_wiki wiki = null; private Dpl_cmd cmd = new Dpl_cmd(); private ListAdp pages;
	public Xop_root_tkn Xtn_root() {return null;}
	public boolean Xtn_literal() {return false;}
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {} // NOTE: <dynamicPageList> has no attributes
	public void Xtn_compile(Xop_ctx ctx, Xow_wiki wiki, Xop_tkn_mkr tkn_mkr, byte[] src, Xop_xnde_tkn xnde) {
		this.wiki = wiki;
		cmd.Parse(wiki, ctx.Page().Page_ttl().Full_txt(), src, xnde);
		pages = Find_pages();
		if (cmd.Sort_ascending() != Bool_.__byte)
			pages.SortBy(new Dpl_page_sorter(cmd));
	}
	private ListAdp Find_pages() {
		ListAdp includes = cmd.Ctg_includes();
		int includes_len = includes.Count();
		OrderedHash cur_regy = OrderedHash_.new_(), new_regy = OrderedHash_.new_(), tmp_regy = OrderedHash_.new_();
		Xodb_load_mgr load_mgr = wiki.Db_mgr().Load_mgr();
		Xodb_page tmp_page = new Xodb_page();
		IntRef tmp_id = IntRef.zero_();

		for (int i = 0; i < includes_len; i++) {
			byte[] include = (byte[])includes.FetchAt(i);
			tmp_regy.Clear();
			Find_ctg_pages(tmp_regy, load_mgr, tmp_page, tmp_id, include);
			int found_len = tmp_regy.Count();
			for (int j = 0; j < found_len; j++) {
				Xoctg_view_itm view_itm = (Xoctg_view_itm)tmp_regy.FetchAt(j);
				if (i != 0) {
					if (!cur_regy.Has(tmp_id.Val_(view_itm.Id()))) continue;
				}
				new_regy.AddKeyVal(IntRef.new_(view_itm.Id()));
			}
			cur_regy = new_regy;
			new_regy = OrderedHash_.new_();
		}

		
		int pages_len = cur_regy.Count();
		ListAdp page_list = ListAdp_.new_();
		for (int i = 0; i < pages_len; i++) {
			IntRef id_ref = (IntRef)cur_regy.FetchAt(i);
			page_list.Add(new Xodb_page().Id_(id_ref.Val()));
		}
		
		wiki.Db_mgr().Load_mgr().Load_ttls_by_ids(Cancelable_.Never, page_list, 0, pages_len);
		page_list.SortBy(Xodb_page_sorter.IdAsc);
		return page_list;
	}
	private void Find_ctg_pages(OrderedHash list, Xodb_load_mgr load_mgr, Xodb_page tmp_page, IntRef tmp_id, byte[] ctg_ttl) {
		Xoctg_view_ctg ctg = new Xoctg_view_ctg().Name_(ctg_ttl);
		load_mgr.Load_ctg_v1(ctg, ctg_ttl);

		for (byte ctg_tid = 0; ctg_tid < Xoa_ctg_mgr.Tid__max; ctg_tid++) {
			Xoctg_view_grp ctg_mgr = ctg.Grp_by_tid(ctg_tid); if (ctg_mgr == null) continue;
			int itms_len = ctg_mgr.Total();
			for (int i = 0; i < itms_len; i++) {
				Xoctg_view_itm ctg_itm = ctg_mgr.Itms()[i];					
				int ctg_itm_id = ctg_itm.Id();
				if (list.Has(tmp_id.Val_(ctg_itm_id))) continue;
				list.Add(IntRef.new_(ctg_itm_id), ctg_itm);
				if (ctg_tid == Xoa_ctg_mgr.Tid_subc) {
					load_mgr.Load_ttl_by_id(tmp_page, ctg_itm_id);
					Find_ctg_pages(list, load_mgr, tmp_page, tmp_id, tmp_page.Ttl_wo_ns());
				}
			}
		}
	}
//		private ListAdp Find_pages() {
////			if (ns_filter != Int_.MinValue && ctg_includes_len == 0) {}
//			byte[][] ctg_ttls = (byte[][])cmd.Ctg_includes().XtoAry(typeof(byte[]));
//			int ctg_ttls_len = ctg_ttls.length;
//			OrderedHash cur_regy = OrderedHash_.new_(), new_regy = OrderedHash_.new_();
//			IntRef tmp_id = IntRef.zero_();
//			for (int i = 0; i < ctg_ttls_len; i++) {
//				byte[] ctg_ttl = ctg_ttls[i];
//				Xoctg_view_ctg ctg = new Xoctg_view_ctg().Name_(ctg_ttl);
//				wiki.Db_mgr().Load_mgr().Load_ctg_v1(ctg, ctg_ttl);
//
//				for (byte j = 0; j < Xoa_ctg_mgr.Tid__max; j++) {
//					Xoctg_view_grp ctg_mgr = ctg.Grp_by_tid(j); if (ctg_mgr == null) continue;
//					int itms_len = ctg_mgr.Total();
//					for (int k = 0; k < itms_len; k++) {
//						Xoctg_view_itm ctg_itm = ctg_mgr.Itms()[k];
//						if (i != 0) {
//							if (!cur_regy.Has(tmp_id.Val_(ctg_itm.Id()))) continue;
//						}
//						new_regy.AddKeyVal(IntRef.new_(ctg_itm.Id()));
//					}
//				}
//				cur_regy = new_regy;
//				new_regy = OrderedHash_.new_();
//			}
//
//			int pages_len = cur_regy.Count();
//			ListAdp page_list = ListAdp_.new_();
//			for (int i = 0; i < pages_len; i++) {
//				IntRef id_ref = (IntRef)cur_regy.FetchAt(i);
//				page_list.Add(new Xodb_page().Id_(id_ref.Val()));
//			}
//			
//			wiki.Db_mgr().Load_mgr().Load_ttls_by_ids(Cancelable_.Never, page_list, 0, pages_len);
//			page_list.SortBy(Xodb_page_sorter.IdAsc);
//			return page_list;
//		}
	public void Xtn_html(Xoh_html_wtr html_wtr, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		Dpl_html_data html_mode = Dpl_html_data.new_(Dpl_cmd_keys.Key_unordered);
		int itms_len = pages.Count();
		if (itms_len == 0) {
			if (!cmd.Suppress_errors())
				bfr.Add_str("Error: No results!");
			return;
		}
		int itms_bgn = 0;
		if (cmd.Offset() != Int_.MinValue) {
			itms_bgn = cmd.Offset();
		}
		if (cmd.Count() != Int_.MinValue && itms_bgn + cmd.Count() < itms_len) {
			itms_len = itms_bgn + cmd.Count();
			//if (itms_len - itms_bgn > count) 
		}
		bfr.Add(html_mode.Grp_bgn()).Add_byte_nl();
		for (int i = itms_bgn; i < itms_len; i++) {
			Xodb_page itm = (Xodb_page)pages.FetchAt(i);
			byte[] ttl_page_txt = itm.Ttl_wo_ns();
			if (ttl_page_txt == null) continue;	// NOTE: apparently DynamicPageList allows null pages; DATE:2013-07-22
			switch (html_mode.Tid()) {
			case Dpl_html_data.Tid_list_ul:
			case Dpl_html_data.Tid_list_ol:
				bfr.Add(Xoh_consts.Space_2).Add(html_mode.Itm_bgn()).Add(Xoh_consts.A_bgn);
				bfr.Add_str("/wiki/").Add(ttl_page_txt);
				bfr.Add(Xoh_consts.A_bgn_lnki_0).Add(ttl_page_txt).Add_byte(Byte_ascii.Quote);
				if (cmd.No_follow()) bfr.Add(Bry_nofollow);
				bfr.Add_byte(Byte_ascii.Gt);
				bfr.Add(ttl_page_txt);
				bfr.Add(Xoh_consts.A_end).Add(html_mode.Itm_end()).Add_byte_nl();
				break;
			default:
				break;
			}
		}
		bfr.Add(html_mode.Grp_end()).Add_byte_nl();
	}
	private static byte[] Bry_nofollow = ByteAry_.new_ascii_(" rel=\"nofollow\"");  
}
class Dpl_cmd_keys {
	public static byte Parse(byte[] src, int bgn, int end, byte or) {
		Object o = keys.Get_by_mid(src, bgn, end);
		return o == null ? or : ((ByteVal)o).Val();
	}
	public static byte Parse(byte[] bry, byte or) {
		Object o = keys.Get_by_bry(bry);
		return o == null ? or : ((ByteVal)o).Val();
	}
	public static boolean Parse_as_bool(byte[] bry, boolean or) {
		byte key = Dpl_cmd_keys.Parse(bry, Key_null);
		switch (key) {
		case Dpl_cmd_keys.Key_false:	return false;
		case Dpl_cmd_keys.Key_true:		return true;
		case Dpl_cmd_keys.Key_null:		return or;
		default:						throw Err_mgr._.unhandled_(String_.new_utf8_(bry));	// shouldn't happen; should always go to or;
		}
	}
	public static final byte
	  Key_null = Byte_.MaxValue_127
	, Key_category = 1
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
	.Add_str_byteVal("category", Dpl_cmd_keys.Key_category)
	.Add_str_byteVal("notcategory", Dpl_cmd_keys.Key_notcategory)
	.Add_str_byteVal("namespace", Dpl_cmd_keys.Key_ns)
	.Add_str_byteVal("count", Dpl_cmd_keys.Key_count)
	.Add_str_byteVal("offset", Dpl_cmd_keys.Key_offset)
	.Add_str_byteVal("imagewidth", Dpl_cmd_keys.Key_imagewidth)
	.Add_str_byteVal("imageheight", Dpl_cmd_keys.Key_imageheight)
	.Add_str_byteVal("imagesperow", Dpl_cmd_keys.Key_imagesperow)
	.Add_str_byteVal("mode", Dpl_cmd_keys.Key_mode)
	.Add_str_byteVal("gallery", Dpl_cmd_keys.Key_gallery)
	.Add_str_byteVal("none", Dpl_cmd_keys.Key_none)
	.Add_str_byteVal("ordered", Dpl_cmd_keys.Key_ordered)
	.Add_str_byteVal("unordered", Dpl_cmd_keys.Key_unordered)
	.Add_str_byteVal("inline", Dpl_cmd_keys.Key_inline)
	.Add_str_byteVal("gallerycaption", Dpl_cmd_keys.Key_gallerycaption)
	.Add_str_byteVal("galleryshowfilesize", Dpl_cmd_keys.Key_galleryshowfilesize)
	.Add_str_byteVal("galleryshowfilename", Dpl_cmd_keys.Key_galleryshowfilename)
	.Add_str_byteVal("order", Dpl_cmd_keys.Key_order)
	.Add_str_byteVal("ordermethod", Dpl_cmd_keys.Key_ordermethod)
	.Add_str_byteVal("lastedit", Dpl_cmd_keys.Key_lastedit)
	.Add_str_byteVal("length", Dpl_cmd_keys.Key_length)
	.Add_str_byteVal("created", Dpl_cmd_keys.Key_created)
	.Add_str_byteVal("sortkey", Dpl_cmd_keys.Key_sortkey)
	.Add_str_byteVal("categorysortkey", Dpl_cmd_keys.Key_categorysortkey)
	.Add_str_byteVal("popularity", Dpl_cmd_keys.Key_popularity)
	.Add_str_byteVal("categoryadd", Dpl_cmd_keys.Key_categoryadd)
	.Add_str_byteVal("redirects", Dpl_cmd_keys.Key_redirects)
	.Add_str_byteVal("include", Dpl_cmd_keys.Key_include)
	.Add_str_byteVal("only", Dpl_cmd_keys.Key_only)
	.Add_str_byteVal("exclude", Dpl_cmd_keys.Key_exclude)
	.Add_str_byteVal("tablepages", Dpl_cmd_keys.Key_tablepages)
	.Add_str_byteVal("qualitypages", Dpl_cmd_keys.Key_qualitypages)
	.Add_str_byteVal("addfirstcategorydate", Dpl_cmd_keys.Key_addfirstcategorydate)
	.Add_str_byteVal("shownamespace", Dpl_cmd_keys.Key_shownamespace)
	.Add_str_byteVal("googlehack", Dpl_cmd_keys.Key_googlehack)
	.Add_str_byteVal("nofollow", Dpl_cmd_keys.Key_nofollow)
	.Add_str_byteVal("descending", Dpl_cmd_keys.Key_descending)
	.Add_str_byteVal("ascending", Dpl_cmd_keys.Key_ascending)
	.Add_str_byteVal("false", Dpl_cmd_keys.Key_false)
	.Add_str_byteVal("true", Dpl_cmd_keys.Key_true)
	.Add_str_byteVal("suppresserrors", Dpl_cmd_keys.Key_suppresserrors)
	;
}
class Dpl_cmd {
	public ListAdp Ctg_includes() {return ctg_includes;} private ListAdp ctg_includes;
	public ListAdp Ctg_excludes() {return ctg_excludes;} private ListAdp ctg_excludes;
	public int Count() {return count;} private int count = Int_.MinValue;
	public int Offset() {return offset;} private int offset = Int_.MinValue;
	public boolean No_follow() {return no_follow;} private boolean no_follow;
	public boolean Suppress_errors() {return suppress_errors;} private boolean suppress_errors;
	public boolean Show_ns() {return show_ns;} private boolean show_ns;
	public byte Sort_ascending() {return sort_ascending;} private byte sort_ascending = Bool_.__byte;
	public int Ns_filter() {return ns_filter;} private int ns_filter = Int_.MinValue;	// NOTE: MinValue means disabled
	public boolean Gallery_filesize() {return gallery_filesize;} private boolean gallery_filesize;
	public boolean Gallery_filename() {return gallery_filename;} private boolean gallery_filename;
	public int Gallery_imgs_per_row() {return gallery_imgs_per_row;} private int gallery_imgs_per_row;
	public int Gallery_img_w() {return gallery_img_w;} private int gallery_img_w;
	public int Gallery_img_h() {return gallery_img_h;} private int gallery_img_h;
	public byte[] Gallery_caption() {return gallery_caption;} private byte[] gallery_caption;
	public byte Redirects_mode() {return redirects_mode;} private byte redirects_mode = Dpl_redirect.Tid_unknown;
	public byte Sort_tid() {return sort_tid;} private byte sort_tid = Dpl_sort.Tid_categoryadd;
	public byte Quality_pages() {return quality_pages;} private byte quality_pages;
	public void Parse(Xow_wiki wiki, byte[] page_ttl, byte[] src, Xop_xnde_tkn xnde) {	// parse kvs in node; EX:<dpl>category=abc\nredirects=y\n</dpl>
		int content_bgn = xnde.Tag_open_end(), content_end = xnde.Tag_close_bgn();
		int pos = content_bgn;
		int fld_bgn = content_bgn;
		byte key_id = 0;
		while (true) {									// iterate over content
			boolean done = pos >= content_end;
			byte b = done ? Dlm_row : src[pos];			// get cur byte
			switch (b) {
			case Dlm_fld:								// dlm is fld; EX: "=" in "category="
				key_id = Dpl_cmd_keys.Parse(src, fld_bgn, pos, Dpl_cmd_keys.Key_null);
				if (key_id == Dpl_cmd_keys.Key_null) {	// unknown key; warn and set pos to end of line; EX: "unknown=";
					wiki.App().Usr_dlg().Warn_many("", "", "unknown_key: page=~{0} key=~{1}", String_.new_utf8_(page_ttl), String_.new_utf8_(src, fld_bgn, pos));
					fld_bgn = ByteAry_.FindFwd(src, Byte_ascii.NewLine);
					if (fld_bgn == ByteAry_.NotFound) break; 
				}
				else {									// known key; set pos to val_bgn
					fld_bgn = pos + Int_.Const_dlm_len;
				}
				break;
			case Dlm_row:								// dlm is nl; EX: "\n" in "category=abc\n"
				if (fld_bgn != pos) {					// ignores blank lines
					byte[] val = ByteAry_.Mid(src, fld_bgn, pos);
					Parse_cmd(wiki, key_id, val);
				}
				fld_bgn = pos + Int_.Const_dlm_len;
				break;
			}
			if (done) break;
			++pos;
		}
	}
	private static final byte Dlm_fld = Byte_ascii.Eq, Dlm_row = Byte_ascii.NewLine;
	public void Parse_cmd(Xow_wiki wiki, byte key_id, byte[] val) {
		switch (key_id) {
			case Dpl_cmd_keys.Key_category: 			if (ctg_includes == null) ctg_includes = ListAdp_.new_(); ctg_includes.Add(Xoa_ttl.Replace_spaces(val)); break;
			case Dpl_cmd_keys.Key_notcategory:		 	if (ctg_excludes == null) ctg_excludes = ListAdp_.new_(); ctg_excludes.Add(Xoa_ttl.Replace_spaces(val)); break;
			case Dpl_cmd_keys.Key_ns:		 			{Xow_ns ns = (Xow_ns)wiki.Ns_mgr().Trie_match_exact(val, 0, val.length); ns_filter = ns == null ? Xow_ns_.Id_main : ns.Id(); break;}
			case Dpl_cmd_keys.Key_order:				sort_ascending = Dpl_sort.Parse_as_bool_byte(val); break;
			case Dpl_cmd_keys.Key_suppresserrors:		suppress_errors = Dpl_cmd_keys.Parse_as_bool(val, false); break;
			case Dpl_cmd_keys.Key_nofollow:				no_follow = Dpl_cmd_keys.Parse_as_bool(val, true); break;	// NOTE: default to true; allows passing nofollow=nofollow; MW: if ('false' != $arg)
			case Dpl_cmd_keys.Key_shownamespace:		show_ns = Dpl_cmd_keys.Parse_as_bool(val, true); break; // NOTE: default to true;
			case Dpl_cmd_keys.Key_redirects:			redirects_mode = Dpl_redirect.Parse(val); break;
//			case Dpl_cmd_keys.Key_stablepages:			stable_pages = Redirect_parse(val); break;
			case Dpl_cmd_keys.Key_qualitypages:			quality_pages = Dpl_redirect.Parse(val); break;
			case Dpl_cmd_keys.Key_addfirstcategorydate:	Parse_ctg_date(val); break;
			case Dpl_cmd_keys.Key_count:				count = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Dpl_cmd_keys.Key_offset:				offset = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Dpl_cmd_keys.Key_imagesperow:			gallery_imgs_per_row = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Dpl_cmd_keys.Key_imagewidth:			gallery_img_w = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Dpl_cmd_keys.Key_imageheight:			gallery_img_h = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Dpl_cmd_keys.Key_gallerycaption:		gallery_caption = val; break;	// FUTURE: parse for {{int:}}?
			case Dpl_cmd_keys.Key_galleryshowfilesize:	gallery_filesize = Dpl_cmd_keys.Parse_as_bool(val, true); break;
			case Dpl_cmd_keys.Key_galleryshowfilename:	gallery_filename = Dpl_cmd_keys.Parse_as_bool(val, true); break;
			case Dpl_cmd_keys.Key_ordermethod:			sort_tid = Dpl_sort.Parse(val); break;
		}
	}
	private void Parse_ctg_date(byte[] val) {
//			byte val_key = Keys_get_or(val, Dpl_cmd_keys.Key_false);
//			if (val_key == Dpl_cmd_keys.Key_true)
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
	//boolean ctg_date = false, ctg_date_strip = false;
//		byte[] ns_include = null;
//		byte[] ctg_date_fmt;
//		byte stable_pages;
}
class Dpl_sort {
	public static final byte Tid_null = 0, Tid_lastedit = 1, Tid_length = 2, Tid_created = 3, Tid_sortkey = 4, Tid_categorysortkey = 5, Tid_popularity = 6, Tid_categoryadd = 7;
	public static byte Parse_as_bool_byte(byte[] bry) {
		byte val = Dpl_cmd_keys.Parse(bry, Dpl_cmd_keys.Key_null);
		switch (val) {
		case Dpl_cmd_keys.Key_ascending: 	return Bool_.Y_byte;  
		case Dpl_cmd_keys.Key_descending: 	return Bool_.N_byte;
		case Dpl_cmd_keys.Key_null:			
		default:							return Bool_.__byte;
		}
	}
	public static byte Parse(byte[] bry) {
		byte key = Dpl_cmd_keys.Parse(bry, Dpl_cmd_keys.Key_categoryadd);
		switch (key) {
		case Dpl_cmd_keys.Key_lastedit: 		return Tid_lastedit;
		case Dpl_cmd_keys.Key_length: 			return Tid_length;
		case Dpl_cmd_keys.Key_created: 			return Tid_created;
		case Dpl_cmd_keys.Key_sortkey: 			return Tid_sortkey;
		case Dpl_cmd_keys.Key_categorysortkey: 	return Tid_categorysortkey;
		case Dpl_cmd_keys.Key_popularity: 		return Tid_popularity;	// FUTURE: default to categoryadd if counters disabled
		case Dpl_cmd_keys.Key_categoryadd: 		return Tid_categoryadd;
		default:								throw Err_mgr._.unhandled_(key);
		}
	}
}
class Dpl_redirect {
	public static final byte Tid_exclude = 0, Tid_include = 1, Tid_only = 2, Tid_unknown = Byte_.MaxValue_127;
	public static byte Parse(byte[] bry) {
		byte key = Dpl_cmd_keys.Parse(bry, Dpl_redirect.Tid_exclude);	// NOTE: exclude is default value.
		switch (key) {
		case Dpl_cmd_keys.Key_exclude: 			return Tid_exclude;
		case Dpl_cmd_keys.Key_include: 			return Tid_include;
		case Dpl_cmd_keys.Key_only: 				return Tid_only;
		default:								throw Err_mgr._.unhandled_(key);
		}
	}
}
class Dpl_html_data {
	public byte Tid() {return tid;} private byte tid;
	public byte[] Grp_bgn() {return grp_bgn;} private byte[] grp_bgn;
	public byte[] Grp_end() {return grp_end;} private byte[] grp_end;
	public byte[] Itm_bgn() {return itm_bgn;} private byte[] itm_bgn;
	public byte[] Itm_end() {return itm_end;} private byte[] itm_end;

	public static final byte Tid_null = 0, Tid_none = 1, Tid_list_ol = 2, Tid_list_ul = 3, Tid_gallery = 4, Tid_inline = 5;
	public static final byte[]
	  Ul_bgn = ByteAry_.new_ascii_("<ul>"), Ul_end = ByteAry_.new_ascii_("</ul>")
	, Ol_bgn = ByteAry_.new_ascii_("<ol>"), Ol_end = ByteAry_.new_ascii_("</ol>")
	, Li_bgn = ByteAry_.new_ascii_("<li>"), Li_end = ByteAry_.new_ascii_("</li>")
	, Br = ByteAry_.new_ascii_("<br />")
	;
	private static final Dpl_html_data
	  Itm_gallery	= new_(Tid_gallery, null, null, null, null)
	, Itm_inline	= new_(Tid_inline, null, null, null, null)
	, Itm_none		= new_(Tid_none, null, null, null, Br)
	, Itm_ordered	= new_(Tid_list_ol, Ol_bgn, Ol_end, Li_bgn, Li_end)
	, Itm_unordered = new_(Tid_list_ul, Ul_bgn, Ul_end, Li_bgn, Li_end)
	;
	private static Dpl_html_data new_(byte tid, byte[] grp_bgn, byte[] grp_end, byte[] itm_bgn, byte[] itm_end) {
		Dpl_html_data rv = new Dpl_html_data();
		rv.tid = tid; rv.grp_bgn = grp_bgn; rv.grp_end = grp_end; rv.itm_bgn = itm_bgn; rv.itm_end = itm_end;
		return rv;
	}
	public static Dpl_html_data new_(byte key) {
		switch (key) {
		case Dpl_cmd_keys.Key_gallery: 			return Itm_gallery;
		case Dpl_cmd_keys.Key_inline: 			return Itm_inline;
		case Dpl_cmd_keys.Key_none: 				return Itm_none;
		case Dpl_cmd_keys.Key_ordered: 			return Itm_ordered;
		case Dpl_cmd_keys.Key_unordered: 		return Itm_unordered;
		default:								throw Err_mgr._.unhandled_(key);
		}
	}
}

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
import gplx.xowa.wikis.*;
public class Xoa_url_parser {
	Url_encoder encoder = Url_encoder.new_html_href_mw_().Itms_raw_same_many(Byte_ascii.Underline); ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255);
	public Gfo_url_parser Url_parser() {return url_parser;} Gfo_url_parser url_parser = new Gfo_url_parser(); Gfo_url gfo_url = new Gfo_url(); Xoa_url url = new Xoa_url();
	public String Build_str(Xoa_url url) {									// transform to "canonical" form that fits url box for both XOWA and Mozilla Firefox
		tmp_bfr.Add(url.Wiki_bry());										// add wiki;		EX: "en.wikipedia.org"
		tmp_bfr.Add(Xoh_href_parser.Href_wiki_bry);							// add "/wiki/"		EX: "/wiki/"
		tmp_bfr.Add(encoder.Decode(url.Page_bry()));						// add page;		EX: "A"
		int args_len = url.Args().length;
		if (args_len > 0) {
			for (int i = 0; i < args_len; i++) {
				byte dlm = i == 0 ? Byte_ascii.Question : Byte_ascii.Amp;
				tmp_bfr.Add_byte(dlm);
				Gfo_url_arg arg = url.Args()[i];
				tmp_bfr.Add(arg.Key_bry()).Add_byte(Byte_ascii.Eq).Add(arg.Val_bry());
			}
		}
		if (url.Anchor_bry() != null)
			tmp_bfr.Add_byte(Byte_ascii.Hash).Add(url.Anchor_bry());		// add anchor;		EX: "#B"
		return tmp_bfr.XtoStrAndClear();
	}		
	public boolean Parse(Xoa_url url, byte[] src, int bgn, int end) {return Parse(url, ByteAry_.Mid(src, bgn, end));}
	public boolean Parse(Xoa_url url, byte[] src) {
		url.Init(src);	// NOTE: need to call init to clear state; Xoa_url is often reused
		src = encoder.Decode(src);	// decode any url-encoded parameters
		int src_len = src.length;
		url_parser.Parse(gfo_url, src, 0, src_len);	// parse protocol
		byte protocol_tid = gfo_url.Protocol_tid();
		if (protocol_tid == Gfo_url_parser.Protocol_file_tid && src_len > 5 && src[5] != Byte_ascii.Slash) { // file ns; EX: "File:A.png"; NOTE: for file:A.png, assume "file" refers to wiki_ns (File:), not protocol; hackish as it relies on looking for / after "file:" to distinguish between MW "File:A.png" and file system "file:///C/A.png"
			url.Raw_(src);
			url.Wiki_bry_(gfo_url.Raw());
			return false;
		}
		url.Protocol_is_relative_(gfo_url.Protocol_is_relative());
		url.Protocol_tid_(gfo_url.Protocol_tid());
		url.Protocol_bry_(gfo_url.Protocol_bry());
		url.Err_(gfo_url.Err());
		url.Raw_(src);
		if (gfo_url.Site() != null && ByteAry_.Eq(gfo_url.Site(), Bry_upload_wikimedia_org)) {	// handle urls like "http://upload.wikimedia.org/wikipedia/commons/a/ab/C.svg"
			byte[][] segs_ary = gfo_url.Segs();
			byte[] domain_bry = segs_ary[0];						// type seems to be the 1st seg	; EX: "/wikipedia/"
			byte[] sub_bry = segs_ary[1];							// lang/type seems to be 2nd seg; EX: "en", "fr"; "commons"
			byte[] lang_bry = sub_bry;
			if (upload_segs_hash.Has(sub_bry)) {					// wikimedia links will have fmt of "/wikipedia/commons"; must change to wikimedia
				domain_bry = Xow_wiki_domain_.Seg_wikimedia_bry;
				lang_bry = Xol_lang_itm_.Key__unknown;
			}
			tmp_bfr.Clear().Add(sub_bry).Add_byte(Byte_ascii.Dot)	// add lang/type + .;	EX: "en."; "fr."; "commons."
				.Add(domain_bry).Add(Bry_dot_org);					// add type + .org;		EX: "wikipedia.org"; "wikimedia.org";
			url.Segs_ary_(Xoa_url_parser.Bry_wiki_name_bry);		// NOTE: add "wiki" as seg else will have "/site/commons.wikimedia.org/File:A" which will be invalid (needs to be "/site/commons.wikimedia.org/wiki/File:A")
			url.Lang_bry_(lang_bry);
			url.Wiki_bry_(tmp_bfr.XtoAryAndClear());
			byte[][] segs = gfo_url.Segs();
			byte[] page_bry = segs.length > 5 && ByteAry_.Eq(segs[2], Xof_url_bldr.Bry_thumb) ? segs[5] : gfo_url.Page();
			url.Page_bry_(tmp_bfr.Add(Bry_file).Add(page_bry).XtoAryAndClear());
			url.Anchor_bry_(ByteAry_.Empty);
		}
		else {
			url.Segs_ary_(gfo_url.Segs());
			url.Lang_bry_(gfo_url.Site_sub());
			url.Wiki_bry_(gfo_url.Site());
			url.Page_bry_(gfo_url.Page());
			url.Anchor_bry_(gfo_url.Anchor());
		}
		Gfo_url_arg[] args = gfo_url.Args();	// parse args
		int args_len = args.length;
		for (int i = 0; i < args_len; i++) {
			Gfo_url_arg arg = args[i];
			byte[] key = arg.Key_bry();
			Object o = qry_args_hash.Get_by_bry(key);
			if (o != null) {
				ByteVal id = (ByteVal)o;
				switch (id.Val()) {
					case Id_arg_redirect: 	url.Redirect_force_(true); break;
					case Id_arg_uselang: 	url.Use_lang_(arg.Val_bry()); break;
					case Id_arg_action: 	if (ByteAry_.Eq(arg.Val_bry(), Bry_arg_action_edit)) url.Action_is_edit_(true); break;
					case Id_arg_title: 		url.Page_bry_(arg.Val_bry()); url.Segs_ary_(ByteAry_.Ary_empty); break;	// handles /w/index.php?title=Earth
					case Id_arg_fulltext: 	url.Search_fulltext_(true); break;
				}
			} 
		}
		url.Args_(args);
		return url.Err() == Gfo_url.Err_none;
	}
	public static Xoa_url Parse_url(Xoa_app app, Xow_wiki cur_wiki, String raw) {Xoa_url rv = new Xoa_url(); byte[] raw_bry = ByteAry_.new_utf8_(raw); return Parse_url(rv, app, cur_wiki, raw_bry, 0, raw_bry.length);}
	public static Xoa_url Parse_url(Xoa_url rv, Xoa_app app, Xow_wiki cur_wiki, byte[] raw, int bgn, int end) {
		Xow_wiki wiki = null; Bry_bfr_mkr bfr_mkr = app.Utl_bry_bfr_mkr();
		byte[] cur_wiki_key = cur_wiki.Domain_bry();
		byte[] page_bry = ByteAry_.Empty;
		if (app.Url_parser().Parse(rv, raw, bgn, end)) {		// parse passed; take Page; EX: "http://en.wikipedia.org/wiki/Earth"
			wiki = Parse_url__wiki(app, rv.Wiki_bry());
			if (rv.Segs_ary().length == 0 && rv.Page_bry() != null && ByteAry_.Eq(rv.Page_bry(), Xoa_url_parser.Bry_wiki_name))	// wiki, but directly after site; EX:en.wikipedia.org/wiki
				page_bry = Xoa_page.Bry_main_page;
			else
				page_bry = Parse_url__combine(bfr_mkr, null, rv.Segs_ary(), rv.Page_bry());	// NOTE: pass null in for wiki b/c wiki has value, but should not be used for Page
		}
		else {											// parse failed; 
			byte[] wiki_bry = rv.Wiki_bry();
			if (rv.Page_bry() == null) {					// only 1 seg; EX: "Earth"; "fr.wikipedia.org"
				if (app.Xwiki_exists(wiki_bry)) {			// matches wiki_regy; bry_0 must be wiki; EX: "fr.wikipedia.org"
					wiki = app.Wiki_mgr().Get_by_key_or_make(wiki_bry);
					page_bry = Xoa_page.Bry_main_page;
				}
				else {									// assume page name
					wiki = Parse_url__wiki(app, cur_wiki_key);
					page_bry = wiki_bry;
				}
			}
			else {										// assume failure b/c of missing protocol; handle en.wikisource.org/Hamlet; Hamlet/Act I; en.wikisource.org/Hamlet/Act I
				if (wiki_bry != null && app.Xwiki_exists(wiki_bry)) {			// matches wiki_regy; bry_0 must be wiki; EX: "fr.wikipedia.org"
					wiki = app.Wiki_mgr().Get_by_key_or_make(wiki_bry);
					if (rv.Segs_ary().length == 0 && ByteAry_.Eq(rv.Page_bry(), Xoa_url_parser.Bry_wiki_name))
						page_bry = Xoa_page.Bry_main_page;
					else
						page_bry = Parse_url__combine(bfr_mkr, ByteAry_.Empty, rv.Segs_ary(), rv.Page_bry());
				}
				else {
					if (ByteAry_.Len_gt_0(wiki_bry)) {		// HACK: wiki_bry null when passing in Category:A from home_wiki
						Xow_xwiki_itm xwiki_itm = app.User().Default_wiki().Xwiki_mgr().Get_by_key(wiki_bry);
						if (xwiki_itm != null) 
							wiki =  app.Wiki_mgr().Get_by_key_or_make(xwiki_itm.Domain());
					}
					if (wiki == null) {						// invalid wiki; assume cur_wiki; EX: Hamlet/Act I
						page_bry = rv.Page_bry();
						byte[][] segs_ary = rv.Segs_ary();
						if (segs_ary.length > 0)
							page_bry = segs_ary[0];
						int colon_pos = ByteAry_.FindFwd(page_bry, Byte_ascii.Colon);	// check for alias; EX: w:Earth
						boolean xwiki_set = false;
						if (colon_pos != ByteAry_.NotFound) {							// alias found
							Xow_xwiki_itm xwiki = cur_wiki.Xwiki_mgr().Get_by_mid(page_bry, 0, colon_pos);
							if (xwiki != null) {
								wiki = app.Wiki_mgr().Get_by_key_or_make(xwiki.Domain());
								page_bry = ByteAry_.Mid(page_bry, colon_pos + 1, page_bry.length); 
								rv.Segs_ary()[0] = page_bry;
								page_bry = Parse_url__combine(bfr_mkr, rv.Wiki_bry(), rv.Segs_ary(), rv.Page_bry());
								xwiki_set = true;
							}
						}
						if (!xwiki_set) {
							wiki = Parse_url__wiki(app, cur_wiki_key);
							page_bry = Parse_url__combine(bfr_mkr, rv.Wiki_bry(), rv.Segs_ary(), rv.Page_bry());
						}
					}
					else {
						wiki = Parse_url__wiki(app, wiki_bry);
						page_bry = Parse_url__combine(bfr_mkr, null, rv.Segs_ary(), rv.Page_bry());
					}
				}
			}
		}
		if (rv.Anchor_bry() != null) {
			byte[] anchor_bry = app.Url_converter_id().Encode(rv.Anchor_bry());	// reencode for anchors (which use . encoding, not % encoding); EX.WP: Enlightenment_Spain#Enlightened_despotism_.281759%E2%80%931788.29
			rv.Anchor_bry_(anchor_bry);
		}
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, page_bry);
		if (ttl != null) {	// can still be empty; EX: "en.wikipedia.org"
			Xow_xwiki_itm lang_xwiki = ttl.Wik_itm();
			if (lang_xwiki != null && lang_xwiki.Type_is_lang(wiki.Lang().Lang_id())) {	// format of http://en.wikipedia.org/wiki/fr:A
				wiki = app.Wiki_mgr().Get_by_key_or_make(lang_xwiki.Domain());
				page_bry = ttl.Page_txt();
			}
		}
		rv.Wiki_(wiki);
		rv.Wiki_bry_(wiki.Domain_bry());
		rv.Page_bry_(page_bry);
		return rv;
	}
	private static Xow_wiki Parse_url__wiki(Xoa_app app, byte[] key) {
		Xow_wiki rv = null;
		Xow_xwiki_itm xwiki = app.User().Wiki().Xwiki_mgr().Get_by_key(key);
 			if (xwiki == null)
			rv = app.User().Default_wiki();
		else
			rv = app.Wiki_mgr().Get_by_key_or_make(xwiki.Domain());
		return rv;			
	}
	private static byte[] Parse_url__combine(Bry_bfr_mkr bry_bfr_mkr, byte[] wiki, byte[][] segs, byte[] page) {
		ByteAryBfr bfr = bry_bfr_mkr.Get_b512();
		if (wiki != null) bfr.Add(wiki);
		if (segs != null) {
			int segs_len = segs.length;
			for (int i = 0; i < segs_len; i++) {
				byte[] seg = segs[i];
				if (i == 0 && ByteAry_.Eq(seg, Xoa_url_parser.Bry_wiki_name)) continue; 
				if (bfr.Bry_len() > 0) bfr.Add_byte(Byte_ascii.Slash);
				bfr.Add(seg);
			}
		}
		if (page != null) {
			if (bfr.Bry_len() > 0) bfr.Add_byte(Byte_ascii.Slash);
			bfr.Add(page);
		}
		return bfr.Mkr_rls().XtoAryAndClear();
	}
	static final byte Tid_xowa = (byte)Gfo_url_parser.Protocol_file_tid + 1;
	static final byte Id_arg_redirect = 0, Id_arg_uselang = 1, Id_arg_title = 2, Id_arg_action = 3, Id_arg_fulltext = 4;
	private static final byte[] Bry_arg_redirect = ByteAry_.new_ascii_("redirect"), Bry_arg_uselang = ByteAry_.new_ascii_("uselang"), Bry_arg_title = ByteAry_.new_ascii_("title"), Bry_arg_action = ByteAry_.new_ascii_("action"), Bry_arg_fulltext = ByteAry_.new_ascii_("fulltext");
	private static final byte[] Bry_upload_wikimedia_org = ByteAry_.new_ascii_("upload.wikimedia.org"), Bry_dot_org = ByteAry_.new_ascii_(".org"), Bry_arg_action_edit = ByteAry_.new_ascii_("edit")
		, Bry_file = ByteAry_.new_ascii_("File:");	// NOTE: File does not need i18n; is a canonical namespace 
	private static final Hash_adp_bry qry_args_hash = new Hash_adp_bry(false).Add_bry_byte(Bry_arg_redirect, Id_arg_redirect).Add_bry_byte(Bry_arg_uselang, Id_arg_uselang).Add_bry_byte(Bry_arg_title, Id_arg_title).Add_bry_byte(Bry_arg_action, Id_arg_action).Add_bry_byte(Bry_arg_fulltext, Id_arg_fulltext);
	private static final Hash_adp_bry upload_segs_hash = new Hash_adp_bry(false).Add_bry_bry(Xow_wiki_domain_.Key_commons_bry);//.Add_bry_bry(Xow_wiki_domain_.Key_species_bry).Add_bry_bry(Xow_wiki_domain_.Key_meta_bry);
	public static final byte[] Bry_wiki_name = ByteAry_.new_ascii_("wiki");
	private static final byte[][] Bry_wiki_name_bry = new byte[][] {Bry_wiki_name};
	public static final byte[] Bry_arg_action_eq_edit = ByteAry_.new_ascii_("action=edit");
}

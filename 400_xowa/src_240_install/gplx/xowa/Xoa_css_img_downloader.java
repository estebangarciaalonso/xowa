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
public class Xoa_css_img_downloader {
	public Xoa_css_img_downloader Ctor(Gfo_usr_dlg usr_dlg, Xof_download_wkr download_wkr, byte[] stylesheet_prefix) {
		this.usr_dlg = usr_dlg; this.download_wkr = download_wkr; this.stylesheet_prefix = stylesheet_prefix;
		return this;
	}	private Gfo_usr_dlg usr_dlg; private Xof_download_wkr download_wkr;
	public Xoa_css_img_downloader Stylesheet_prefix_(byte[] v) {stylesheet_prefix = v; return this;} private byte[] stylesheet_prefix;	// TEST: setter exposed b/c tests can handle "mem/" but not "//mem"
	public void Chk(byte[] wiki_domain, Io_url css_fil) {
		ListAdp img_list = ListAdp_.new_();
		byte[] old_bry = Io_mgr._.LoadFilBry(css_fil);
		byte[] rel_url_prefix = Bry_.Add(Bry_fwd_slashes, wiki_domain);
		byte[] new_bry = Convert_to_local_urls(rel_url_prefix, old_bry, img_list);
		Io_url img_dir = css_fil.OwnerDir();
		Download_fils(img_dir, img_list.XtoStrAry());
		Io_mgr._.SaveFilBry(css_fil, new_bry);
	}
	public byte[] Convert_to_local_urls(byte[] rel_url_prefix, byte[] src, ListAdp list) {
		try {
			int src_len = src.length;
			int prv_pos = 0;
			Bry_bfr bfr = Bry_bfr.new_(src_len);
			HashAdp img_hash = HashAdp_.new_bry_();
			while (true) {
				int url_pos = Bry_finder.Find_fwd(src, Bry_url, prv_pos);
				if (url_pos == Bry_.NotFound) {bfr.Add_mid(src, prv_pos, src_len); break;}	// no more "url("; exit;
				int bgn_pos = url_pos + Bry_url_len;	// set bgn_pos after "url("
				byte bgn_byte = src[bgn_pos];
				byte end_byte = Byte_ascii.Nil;
				boolean quoted = true;
				switch (bgn_byte) {									// find end_byte
					case Byte_ascii.Quote: case Byte_ascii.Apos:	// quoted; end_byte is ' or "
						end_byte = bgn_byte;
						++bgn_pos;
						break;
					default:										// not quoted; end byte is ")"
						end_byte = Byte_ascii.Paren_end;
						quoted = false;
						break;
				}
				int end_pos = Bry_finder.Find_fwd(src, end_byte, bgn_pos, src_len);
				if (end_pos == Bry_.NotFound) {	// unclosed "url("; exit since nothing else will be found
					usr_dlg.Warn_many(GRP_KEY, "parse.invalid_url.end_missing", "could not find end_sequence for 'url(': bgn='~{0}' end='~{1}'", prv_pos, String_.new_utf8_len_safe_(src, prv_pos, prv_pos + 25));
					bfr.Add_mid(src, prv_pos, src_len);
					break;
				}	
				if (end_pos - bgn_pos == 0) {		// empty; "url()"; ignore
					usr_dlg.Warn_many(GRP_KEY, "parse.invalid_url.empty", "'url(' is empty: bgn='~{0}' end='~{1}'", prv_pos, String_.new_utf8_len_safe_(src, prv_pos, prv_pos + 25));
					bfr.Add_mid(src, prv_pos, bgn_pos);
					prv_pos = bgn_pos;
					continue;
				}
				byte[] img_raw = Bry_.Mid(src, bgn_pos, end_pos); int img_raw_len = img_raw.length;
				if (Bry_.HasAtBgn(img_raw, Bry_data_image, 0, img_raw_len)) {	// base64
					bfr.Add_mid(src, prv_pos, end_pos);							// nothing to download; just add entire String
					prv_pos = end_pos;
					continue;
				}
				int import_url_end = Import_url_chk(rel_url_prefix, src, src_len, prv_pos, url_pos, img_raw, bfr);	// check for embedded stylesheets via @import tag
				if (import_url_end != Bry_.NotFound)  {
					prv_pos = import_url_end;
					continue;
				}
				byte[] img_cleaned = Clean_img_url(img_raw, img_raw_len);
				if (img_cleaned == null) {	// could not clean img
					usr_dlg.Warn_many(GRP_KEY, "parse.invalid_url.clean_failed", "could not extract valid http src: bgn='~{0}' end='~{1}'", prv_pos, String_.new_utf8_(img_raw));
					bfr.Add_mid(src, prv_pos, bgn_pos); prv_pos = bgn_pos; continue;
				}
				if (!img_hash.Has(img_cleaned)) {// only add unique items for download;
					img_hash.AddKeyVal(img_cleaned);
					list.Add(String_.new_utf8_(img_cleaned));
				}
				img_cleaned = Replace_invalid_chars(Bry_.Copy(img_cleaned));	// NOTE: must call ByteAry.Copy else img_cleaned will change *inside* hash
				bfr.Add_mid(src, prv_pos, bgn_pos);
				if (!quoted) bfr.Add_byte(Byte_ascii.Quote);
				bfr.Add(img_cleaned);
				if (!quoted) bfr.Add_byte(Byte_ascii.Quote);
				prv_pos = end_pos;
			}
			return bfr.Xto_bry_and_clear();
		}
		catch (Exception e) {
			usr_dlg.Warn_many("", "", "failed to convert local_urls: ~{0} ~{1}", String_.new_utf8_(rel_url_prefix), Err_.Message_gplx(e));
			return src;
		}
	}
	public static byte[] Import_url_build(byte[] stylesheet_prefix, byte[] rel_url_prefix, byte[] css_url) {
		return Bry_.HasAtBgn(css_url, Bry_http_protocol)			// css_url already starts with "http"; return self; PAGE:tr.n:Main_Page; DATE:2014-06-04
			? css_url
			: Bry_.Add(stylesheet_prefix, css_url)
			;
	}
	private int Import_url_chk(byte[] rel_url_prefix, byte[] src, int src_len, int old_pos, int find_bgn, byte[] url_raw, Bry_bfr bfr) {
		if (find_bgn < Bry_import_len) return Bry_.NotFound;
		if (!Bry_.Match(src, find_bgn - Bry_import_len, find_bgn, Bry_import)) return Bry_.NotFound;
		byte[] css_url = url_raw; int css_url_len = css_url.length;
		if (css_url_len > 0 && css_url[0] == Byte_ascii.Slash) {		// css_url starts with "/"; EX: "/page" or "//site/page" DATE:2014-02-03
			if (css_url_len > 1 && css_url[1] != Byte_ascii.Slash)		// skip if css_url starts with "//"; EX: "//site/page"
				css_url = Bry_.Add(rel_url_prefix, css_url);			// "/w/a.css" -> "//en.wikipedia.org/w/a.css"
		}
		byte[] css_src_bry = Import_url_build(stylesheet_prefix, rel_url_prefix, css_url);
		String css_src_str = String_.new_utf8_(css_src_bry);
		download_wkr.Download_xrg().Prog_fmt_hdr_(usr_dlg.Log_many(GRP_KEY, "logo.download", "downloading import for '~{0}'", css_src_str));
		byte[] css_trg_bry = download_wkr.Download_xrg().Exec_as_bry(css_src_str);
		if (css_trg_bry == null) {
			usr_dlg.Warn_many("", "", "could not import css: url=~{0}", css_src_str);
			return Bry_.NotFound;	// css not found
		}
		bfr.Add_mid(src, old_pos, find_bgn - Bry_import_len).Add_byte_nl();
		bfr.Add(Bry_comment_bgn).Add(css_url).Add(Bry_comment_end).Add_byte_nl();			
		if (Bry_finder.Find_fwd(css_url, Wikisource_dynimg_ttl) != -1) css_trg_bry = Bry_.Replace(css_trg_bry, Wikisource_dynimg_find, Wikisource_dynimg_repl);	// FreedImg hack; PAGE:en.s:Page:Notes_on_Osteology_of_Baptanodon._With_a_Description_of_a_New_Species.pdf/3 DATE:2014-09-06
		bfr.Add(css_trg_bry).Add_byte_nl();
		bfr.Add_byte_nl();
		int semic_pos = Bry_finder.Find_fwd(src, Byte_ascii.Semic, find_bgn + url_raw.length, src_len);
		return semic_pos + Int_.Const_dlm_len;
	}
	private static final byte[]
	  Wikisource_dynimg_ttl		= Bry_.new_ascii_("en.wikisource.org/w/index.php?title=MediaWiki:Dynimg.css")
	, Wikisource_dynimg_find	= Bry_.new_ascii_(".freedImg img[src*=\"wikipedia\"], .freedImg img[src*=\"wikisource\"], .freedImg img[src*=\"score\"], .freedImg img[src*=\"math\"] {")
	, Wikisource_dynimg_repl	= Bry_.new_ascii_(".freedImg img[src*=\"wikipedia\"], .freedImg img[src*=\"wikisource\"], /*XOWA:handle file:// paths which will have /commons.wikimedia.org/ but not /wikipedia/ */ .freedImg img[src*=\"wikimedia\"], .freedImg img[src*=\"score\"], .freedImg img[src*=\"math\"] {")
	;
	public byte[] Clean_img_url(byte[] raw, int raw_len) {
		int pos_bgn = 0;
		if (Bry_.HasAtBgn(raw, Bry_fwd_slashes, 0, raw_len)) pos_bgn = Bry_fwd_slashes.length;
		if (Bry_.HasAtBgn(raw, Bry_http, 0, raw_len)) pos_bgn = Bry_http.length;
		int pos_slash = Bry_finder.Find_fwd(raw, Byte_ascii.Slash, pos_bgn, raw_len);
		if (pos_slash == Bry_.NotFound) return null; // first segment is site_name; at least one slash must be present for image name; EX: site.org/img_name.jpg
		if (pos_slash == raw_len - 1) return null; // "site.org/" is invalid
		int pos_end = raw_len;
		int pos_question = Bry_finder.Find_bwd(raw, Byte_ascii.Question);
		if (pos_question != Bry_.NotFound)
			pos_end = pos_question;	// remove query params; EX: img_name?key=val 
		return Bry_.Mid(raw, pos_bgn, pos_end);
	}
	private void Download_fils(Io_url css_dir, String[] ary) {
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			String src = ary[i];
			Io_url trg = css_dir.GenSubFil_nest(Op_sys.Cur().Fsys_http_frag_to_url_str(Replace_invalid_chars_str(src)));
			if (Io_mgr._.ExistsFil(trg)) continue;
			download_wkr.Download(true, "http://" + src, trg, "download: " + src); // ILN
		}
	}
	String Replace_invalid_chars_str(String raw_str) {return String_.new_utf8_(Replace_invalid_chars(Bry_.new_utf8_(raw_str)));}
	byte[] Replace_invalid_chars(byte[] raw_bry) {
		int raw_len = raw_bry.length;
		for (int i = 0; i < raw_len; i++) {	// convert invalid wnt chars to underscores
			byte b = raw_bry[i];
			switch (b) {
				//case Byte_ascii.Slash:
				case Byte_ascii.Backslash: case Byte_ascii.Colon: case Byte_ascii.Asterisk: case Byte_ascii.Question:
				case Byte_ascii.Quote: case Byte_ascii.Lt: case Byte_ascii.Gt: case Byte_ascii.Pipe:
					raw_bry[i] = Byte_ascii.Underline;
					break;
			}
		}
		return raw_bry;
	}
	private static final byte[] 
	  Bry_url = Bry_.new_ascii_("url("), Bry_data_image = Bry_.new_ascii_("data:image/")
	, Bry_http = Bry_.new_ascii_("http://"), Bry_fwd_slashes = Bry_.new_ascii_("//"), Bry_import = Bry_.new_ascii_("@import ")
	, Bry_http_protocol = Bry_.new_ascii_("http")
	;
	public static final byte[] 
		  Bry_comment_bgn = Bry_.new_ascii_("/*XOWA:"), Bry_comment_end = Bry_.new_ascii_("*/");
	private static final int Bry_url_len = Bry_url.length, Bry_import_len = Bry_import.length;
	static final String GRP_KEY = "xowa.wikis.init.css";
}

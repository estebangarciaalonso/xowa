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
package gplx.xowa; import gplx.*;
import gplx.xowa.gui.*; import gplx.xowa.html.*; import gplx.xowa.xtns.refs.*;
public class Xoa_page {
	public Xoa_page(Xow_wiki wiki, Xoa_ttl page_ttl) {
		this.wiki = wiki; this.page_ttl = page_ttl;
		Page_ttl_(page_ttl);
		lang = wiki.Lang();	// default to wiki.lang; can be override later in wikitext
	}
	public Xow_wiki		Wiki() {return wiki;} private Xow_wiki wiki;
	public Xoa_ttl		Page_ttl() {return page_ttl;} public Xoa_page Page_ttl_(Xoa_ttl v) {page_ttl = v; url.Wiki_bry_(wiki.Key_bry()).Page_bry_(v.Full_url()); return this;} private Xoa_ttl page_ttl;
	public int			Page_id() {return page_id;} public Xoa_page Page_id_(int v) {page_id = v; return this;} private int page_id;
	public DateAdp		Page_date() {return page_date;} public Xoa_page Page_date_(DateAdp v) {page_date = v; return this;} DateAdp page_date = DateAdp_.MinValue;
	public byte			Page_tid() {return page_tid;} private byte page_tid;
	public void			Page_tid_normal_() {page_tid = Tid_normal;}
	public Xol_lang		Lang() {return lang;} public Xoa_page Lang_(Xol_lang v) {lang = v; return this;} private Xol_lang lang;
	public byte[] Search_text() {return search_text;} public Xoa_page Search_text_(byte[] v) {search_text = v; return this;} private byte[] search_text = ByteAry_.Empty;

	public Xop_root_tkn Root() {return root;} public Xoa_page Root_(Xop_root_tkn v) {root = v; return this;} private Xop_root_tkn root;
	public byte[] Data_raw() {return data_raw;} public Xoa_page Data_raw_(byte[] v) {data_raw = v; return this;} private byte[] data_raw = ByteAry_.Empty;
	public byte[] Data_preview() {return data_preview;} public Xoa_page Data_preview_(byte[] v) {data_preview = v; return this;} private byte[] data_preview = ByteAry_.Empty;
	public byte[] Display_ttl() {return display_ttl;} public Xoa_page Display_ttl_(byte[] v) {display_ttl = v; return this;} private byte[] display_ttl;
	public String DocPos() {return docPos;} public Xoa_page DocPos_(String v) {docPos = v; return this;} private String docPos;
	public boolean Url_redirected() {return url_redirected;} public Xoa_page Url_redirected_(boolean v) {url_redirected = v; return this;} private boolean url_redirected;
	public Xoa_url Url() {return url;} public Xoa_page Url_(Xoa_url v) {url = v; return this;} private Xoa_url url = new Xoa_url();
	public boolean Allow_all_html() {return allow_all_html;} public Xoa_page Allow_all_html_(boolean v) {allow_all_html = v; return this;} private boolean allow_all_html;
	public byte[][] Category_list() {return category_list;} public Xoa_page Category_list_(byte[][] v) {category_list = v; return this;} private byte[][] category_list = new byte[0][];
	public ListAdp Langs() {return langs;} ListAdp langs = ListAdp_.new_();
	public ListAdp Redirect_list() {return redirect_list;} ListAdp redirect_list = ListAdp_.new_();
	public Xof_xfer_queue File_queue() {return file_queue;} private Xof_xfer_queue file_queue = new Xof_xfer_queue();
//		public ListAdp File_xfer_cmds() {return file_xfer_cmds;} ListAdp file_xfer_cmds = ListAdp_.new_();
	public ListAdp File_math() {return file_math;} ListAdp file_math = ListAdp_.new_();
	public Xtn_ref_mgr Ref_mgr() {return ref_mgr;} public void Ref_mgr_(Xtn_ref_mgr v) {ref_mgr = v;} Xtn_ref_mgr ref_mgr = new Xtn_ref_mgr();
	public boolean TocFlag_toc() {return tocFlag_toc;} public Xoa_page TocFlag_toc_y_() {tocFlag_toc = true; return this;} private boolean tocFlag_toc;
	public boolean TocFlag_toc_force() {return tocFlag_toc_force;} public Xoa_page TocFlag_toc_force_y_() {tocFlag_toc_force = true; return this;} private boolean tocFlag_toc_force;
	public boolean TocFlag_toc_no() {return tocFlag_toc_no;} public Xoa_page TocFlag_toc_no_y_() {tocFlag_toc_no = true; return this;} private boolean tocFlag_toc_no;
	public boolean Toc_enabled() {return !tocFlag_toc_no && hdrs_len != 0 && (hdrs_len > 3 || tocFlag_toc || tocFlag_toc_force);}
	public boolean TocPos_firstHdr() {return !tocFlag_toc;}
	public int Hdrs_len() {return hdrs_len;}
	public Xoh_cmd_mgr Html_cmd_mgr() {return html_cmd_mgr;} private Xoh_cmd_mgr html_cmd_mgr = new Xoh_cmd_mgr();
	public gplx.xowa.xtns.wdatas.Wdata_external_lang_links_data Wdata_external_lang_links() {return wdata_external_lang_links;} gplx.xowa.xtns.wdatas.Wdata_external_lang_links_data wdata_external_lang_links = new gplx.xowa.xtns.wdatas.Wdata_external_lang_links_data();
	public Xop_hdr_tkn Hdrs_get(int i) {return hdrs_ary[i];}
	public void Hdrs_add(Xop_hdr_tkn hdr, byte[] src) {
		int new_len = hdrs_len + 1;
		if (new_len > hdrs_max) {
			hdrs_max = (new_len * 2) + 1;
			hdrs_ary = (Xop_hdr_tkn[])Array_.Resize(hdrs_ary, hdrs_max);
		}
		Hdrs_reg(hdr, src);
		hdrs_ary[hdrs_len] = hdr;
		hdrs_len = new_len;
	}	Xop_hdr_tkn[] hdrs_ary = new Xop_hdr_tkn[0]; int hdrs_max, hdrs_len; HashAdp hdrs_hash = HashAdp_.new_(); ByteAryBfr hdrs_bfr = ByteAryBfr.reset_(255); ByteAryRef hdrs_ref = ByteAryRef.null_();
	public void Hdrs_reg(Xop_hdr_tkn hdr, byte[] src) {
		if (hdrs_len == 0) hdr.Hdr_html_first_y_();
		Xoa_app app = wiki.App();
		ByteAryBfr raw_bfr = app.Utl_bry_bfr_mkr().Get_b128(), enc_bfr = app.Utl_bry_bfr_mkr().Get_b128();
		Hdrs_id_bld_recurse(raw_bfr, src, hdr);
		Url_encoder encoder = wiki.App().Url_converter_id();
		encoder.Encode(enc_bfr, raw_bfr.Bry(), 0, raw_bfr.Bry_len());
		byte[] hdrs_id = enc_bfr.XtoAry();
		Object o = hdrs_hash.Fetch(hdrs_ref.Val_(hdrs_id));
		if (o != null) {
			Xop_hdr_tkn hdr_0 = (Xop_hdr_tkn)o;
			enc_bfr.Add_byte(Byte_ascii.Underline).Add_int_variable(hdr_0.Hdr_html_dupe_idx_next());
			hdrs_id = enc_bfr.XtoAryAndClear();
		}
		else {
			hdrs_bfr.Clear();
			hdrs_hash.Add(ByteAryRef.new_(hdrs_id), hdr);
		}
		hdr.Hdr_html_id_(hdrs_id);
		hdr.Hdr_toc_text_(gplx.xowa.html.tocs.Xop_toc_mgr.Toc_text(this, src, hdr));
		raw_bfr.Mkr_rls(); enc_bfr.Mkr_rls();
	}
	void Hdrs_id_bld_recurse(ByteAryBfr raw_bfr, byte[] src, Xop_tkn_itm tkn) {
		boolean txt_seen = false; int ws_pending = 0;
		int subs_len = tkn.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Xop_tkn_itm sub = tkn.Subs_get(i);
			byte sub_tid = sub.Tkn_tid();
			if (	sub_tid != Xop_tkn_itm_.Tid_space
				&&	sub_tid != Xop_tkn_itm_.Tid_para) {
				if (ws_pending > 0) {
					raw_bfr.Add_byte_repeat(Byte_ascii.Underline, ws_pending);
					ws_pending = 0;
				}
				txt_seen = true;
			}
			switch (sub.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_space:
					if (txt_seen) ws_pending = sub.Src_end() - sub.Src_bgn();
					break;
				case Xop_tkn_itm_.Tid_apos: break; // noop; ignore apos in id
				case Xop_tkn_itm_.Tid_txt:
					raw_bfr.Add_mid(src, sub.Src_bgn(), sub.Src_end());
					break;
				case Xop_tkn_itm_.Tid_xnde:
					Xop_xnde_tkn xnde = (Xop_xnde_tkn)sub;
					Hdrs_id_bld_recurse(raw_bfr, src, xnde);
					break;
				case Xop_tkn_itm_.Tid_lnki:
					Xop_lnki_tkn lnki = (Xop_lnki_tkn)sub;						
					if (lnki.Caption_exists())
						Hdrs_id_bld_recurse(raw_bfr, src, lnki.Caption_val_tkn());
					else
						raw_bfr.Add(lnki.Ttl_ary());
					break;
				case Xop_tkn_itm_.Tid_html_ncr:
					Xop_html_ncr_tkn html_ncr = (Xop_html_ncr_tkn)sub;
					raw_bfr.Add(html_ncr.Html_ncr_bry());
					break;
				default:						
					raw_bfr.Add_mid(src, sub.Src_bgn(), sub.Src_end());
					break;
			}
		}
	}
	public void Clear() {
		hdrs_len = 0;
		if (hdrs_max > 32) {
			hdrs_ary = new Xop_hdr_tkn[32];
			hdrs_max = 32;
		}
		hdrs_hash.Clear();
		file_math.Clear();
		file_queue.Clear();
		ref_mgr.Grps_clear();
		langs.Clear();
		gplx.xowa.xtns.scribunto.Scrib_engine.Engine_page_changed(this);
		wdata_external_lang_links.Reset();
		html_cmd_mgr.Clear();
		search_text = ByteAry_.Empty;
//			redirect_list.Clear();
		tocFlag_toc = tocFlag_toc_force = tocFlag_toc_no = false;
	}
	public static final Xoa_page Null = null;
	public static final byte Tid_normal = 0, Tid_create = 1;
	public static Xoa_page blank_page_(Xow_wiki wiki, Xoa_ttl ttl) {
		Xoa_page rv = new Xoa_page(wiki, ttl);
		rv.Data_raw_(ByteAry_.Empty);
		rv.Root_(wiki.Ctx().Tkn_mkr().Root(ByteAry_.Empty));
		rv.page_tid = Tid_create;
		return rv;
	}
	public static final byte[] Bry_main_page = ByteAry_.new_ascii_("Main_Page");
	public static final int Page_len_max = 2048 * Io_mgr.Len_kb;	// REF.MW: DefaultSettings.php; $wgMaxArticleSize = 2048;
}

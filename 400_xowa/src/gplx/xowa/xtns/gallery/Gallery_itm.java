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
package gplx.xowa.xtns.gallery; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.files.*; import gplx.xowa.files.gui.*; import gplx.xowa.gui.views.*; import gplx.xowa.html.*;
public class Gallery_itm implements Js_img_wkr {
	public Xoa_ttl Ttl() {return ttl;} public Gallery_itm Ttl_(Xoa_ttl v) {ttl = v; return this;} private Xoa_ttl ttl;
	public int Ttl_bgn() {return ttl_bgn;} public Gallery_itm Ttl_bgn_(int v) {ttl_bgn = v; return this;} private int ttl_bgn;
	public int Ttl_end() {return ttl_end;} public Gallery_itm Ttl_end_(int v) {ttl_end = v; return this;} private int ttl_end;
	public Xof_ext Ext() {return ext;} public Gallery_itm Ext_(Xof_ext v) {ext = v; return this;} private Xof_ext ext;
	public byte[] Caption_bry() {return caption_bry;} public Gallery_itm Caption_bry_(byte[] v) {caption_bry = v; return this;} private byte[] caption_bry;
	public Xop_root_tkn Caption_tkn() {return caption_tkn;} public Gallery_itm Caption_tkn_(Xop_root_tkn v) {caption_tkn = v; return this;} private Xop_root_tkn caption_tkn;
	public int Alt_bgn() {return alt_bgn;} public Gallery_itm Alt_bgn_(int v) {alt_bgn = v; return this;} private int alt_bgn;
	public int Alt_end() {return alt_end;} public Gallery_itm Alt_end_(int v) {alt_end = v; return this;} private int alt_end;
	public int Link_bgn() {return link_bgn;} public Gallery_itm Link_bgn_(int v) {link_bgn = v; return this;} private int link_bgn;
	public int Link_end() {return link_end;} public Gallery_itm Link_end_(int v) {link_end = v; return this;} private int link_end;
	public int Page_bgn() {return page_bgn;} public Gallery_itm Page_bgn_(int v) {page_bgn = v; return this;} private int page_bgn;
	public int Page_end() {return page_end;} public Gallery_itm Page_end_(int v) {page_end = v; return this;} private int page_end;
	public Xop_lnki_tkn Lnki_tkn() {return lnki_tkn;} public Gallery_itm Lnki_tkn_(Xop_lnki_tkn v) {lnki_tkn = v; return this;} private Xop_lnki_tkn lnki_tkn;
	public Gallery_itm Reset() {
		ttl = null;
		ttl_bgn = ttl_end = alt_bgn = alt_end = link_bgn = link_end = page_bgn = page_end = Bry_.NotFound;
		caption_bry = null;	// NOTE: use null instead of ""; more legible tests
		caption_tkn = null;
		ext = null;
		return this;
	}
	public void Html_prepare(Xow_wiki wiki, Xop_ctx ctx, byte[] src, Gallery_xnde xnde, Xof_xfer_itm xfer_itm, byte[] gallery_li_id_bry, int gallery_itm_idx) {
		this.xnde = xnde; this.xfer_itm = xfer_itm;;
		this.wiki = wiki; this.ctx = ctx; this.src = src; this.gallery_li_id_bry = gallery_li_id_bry; this.gallery_itm_idx = gallery_itm_idx;
	}	private Gallery_xnde xnde; private Xof_xfer_itm xfer_itm; private Xow_wiki wiki; private Xop_ctx ctx; private byte[] src; private byte[] gallery_li_id_bry; private int gallery_itm_idx;
	public void Html_update(Xoa_page page, Xog_html_itm html_itm, int html_uid, int html_w, int html_h, String html_src, int orig_w, int orig_h, String orig_src, byte[] lnki_ttl) {
		Gallery_mgr_base gallery_mgr = xnde.Gallery_mgr();
		Bry_bfr bfr = wiki.Utl_bry_bfr_mkr().Get_k004(), tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_k004();
		try {
			xfer_itm.Init_for_gallery_update(html_w, html_h, html_src, orig_src);
			gallery_mgr.Write_html_itm(bfr, tmp_bfr, wiki.App(), wiki, ctx.Cur_page(), ctx, wiki.Html_mgr().Html_wtr(), Xoh_wtr_ctx.Basic, src, xnde, Bry_.Empty, gallery_itm_idx, xfer_itm, false);
			String itm_html = bfr.Xto_str_and_clear();
			html_itm.Html_elem_replace_html(String_.new_utf8_(gallery_li_id_bry), itm_html);
			if (gallery_itm_idx == xnde.Itms_len() - 1 && Gallery_mgr_base_.Mode_is_packed(xnde.Mode()))
				page.Html_data().Xtn_gallery_packed_exists_y_();	// set flag for packed_gallery; don't fire multiple times; PAGE:en.html_w:National_Sculpture_Museum_(Valladolid); DATE:2014-07-21
		}
		finally {
			bfr.Mkr_rls(); tmp_bfr.Mkr_rls();
		}
	}
}

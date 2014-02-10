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
import gplx.xowa.files.*;
public class Gallery_html {
	public void Write_html(Xoa_app app, Xow_wiki wiki, Xop_ctx ctx, Xoa_page page, Xoh_html_wtr wtr, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		Gallery_nde mgr = (Gallery_nde)xnde.Xnde_xtn();
		int itms_len = mgr.Itms_len();
		int itm_div_w = Calc_itm_div_w(mgr.Itm_w_or_default());
		int itm_div_h = Calc_itm_div_h(mgr.Itm_h_or_default());
		int itm_box_w = Calc_itm_box_w(itm_div_w);
		int itm_box_w_orig = itm_box_w;
		int itms_per_row = mgr.Itms_per_row();
		int gallery_multiplier = itms_len;
		ByteAryFmtr gallery_style = html_gallery_box_style_default;
		if (itms_per_row > 0) {
			gallery_multiplier = itms_per_row;
			gallery_style = html_gallery_box_style_per_row;
		}
		int gallery_w = 0, gallery_w_count = 0;
		boolean gallery_mode_is_packed = ByteAry_.Eq(mgr.Gallery_mode(), Gallery_nde.Gallery_mode_packed);
		if (!gallery_mode_is_packed) {
			gallery_w = Calc_itm_pad_w(itm_box_w) * gallery_multiplier; // 8=Gallery Box borders; REF.MW:ImageGallery.php|GB_BORDERS; TODO:CFG
		}
		ByteAryBfr itm_bfr = ByteAryBfr.new_(), caption_bfr = ByteAryBfr.new_();
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_k004();
		int gallery_elem_id = -1;
		for (int i = 0; i < itms_len; i++) {
			Gallery_itm itm = mgr.Itms_get(i);
			byte[] lnki_caption = itm.Caption_bry();
			if (ByteAry_.Len_gt_0(lnki_caption)) {
				Xop_root_tkn caption_root = itm.Caption_tkn();
				wtr.Write_tkn(ctx, opts, caption_bfr, caption_root.Root_src(), depth + 1, caption_root, Xoh_html_wtr.Sub_idx_null, caption_root);
				lnki_caption = caption_bfr.XtoAryAndClear();
			}
			Xoa_ttl itm_ttl = itm.Ttl();
			if (itm_ttl != null && itm_ttl.Ns().Id_file()) {	// itm found; i.e.: does not have to be retrieved
				Xop_lnki_tkn lnki = ctx.Tkn_mkr().Lnki(itm.Ttl_bgn(), itm.Ttl_end()).Ttl_(itm_ttl).Width_(mgr.Itm_w()).Height_(mgr.Itm_h());
				Xof_xfer_itm xfer_itm = wtr.Lnki_wtr().Lnki_eval(ctx, page, lnki, wtr.Queue_add_ref());
				if (gallery_mode_is_packed) {
					if (gallery_w_count < itms_per_row) {
						gallery_w += Calc_itm_pad_w(itm_box_w);
						++gallery_w_count;
					}
					if (xfer_itm.Html_w() > 0) {
						itm_div_w = Calc_itm_div_w(xfer_itm.Html_w());
						itm_box_w = Calc_itm_box_w(itm_div_w);
					}
					if (xfer_itm.Html_h() > 0)
						itm_div_h = Calc_itm_div_h(xfer_itm.Html_h());
				}
				int elem_id = xfer_itm.Html_uid();
				if (gallery_elem_id == -1)
					gallery_elem_id = elem_id;	// HACK: set gallery_elem_id to first elem_id
				xfer_itm.Gallery_mgr_h_(mgr.Itm_h_or_default());
				xfer_itm.Html_elem_tid_(Xof_html_elem.Tid_gallery);
				byte[] html_src = xfer_itm.Html_view_src();
				int html_w = xfer_itm.Html_w();
				int html_h = xfer_itm.Html_h();

				int gallery_itm_w = html_w, gallery_itm_h = html_h;
				if (html_src.length == 0) {	// itm not found; default to mgr.size
					gallery_itm_w = mgr.Itm_w_or_default(); gallery_itm_h = mgr.Itm_h_or_default();
				}
				int v_pad = Calc_vpad(mgr.Itm_h(), gallery_itm_h);
				byte[] lnki_ttl = lnki.Ttl().Page_txt();
				Xoa_ttl lnki_link_ttl = itm_ttl;
				if (itm.Link_bgn() != ByteAry_.NotFound)
					lnki_link_ttl = Xoa_ttl.parse_(wiki, ByteAry_.Mid(src, itm.Link_bgn(), itm.Link_end()));
				byte[] lnki_href = app.Href_parser().Build_to_bry(lnki_link_ttl, wiki);
				byte[] lnki_alt = itm.Alt_bgn() == ByteAry_.NotFound ? lnki_ttl 
						: Xoh_html_wtr.Bfr_escape(app, tmp_bfr, ByteAry_.Mid(src, itm.Alt_bgn(), itm.Alt_end())); 
				html_gallery_itm_img.Bld_bfr_many(itm_bfr
					, itm_box_w, itm_div_w
					, v_pad
					, elem_id
					, lnki_ttl
					, lnki_href
					, html_src
					, gallery_itm_w, gallery_itm_h
					, lnki_caption
					, lnki_alt
					);
			}
			else {
				html_gallery_itm_txt.Bld_bfr_many(itm_bfr
					, itm_box_w, itm_div_h
					, ByteAry_.Mid(src, itm.Ttl_bgn(), itm.Ttl_end())
					, lnki_caption
					);
			}
		}
		int max_ul_width = Calc_itm_pad_w(itm_box_w_orig) * itms_per_row;
		if (gallery_w < max_ul_width)
			gallery_w = max_ul_width;
		
		tmp_bfr.Mkr_rls();
		html_gallery_box.Bld_bfr(bfr, ByteAryFmtrArg_.int_(gallery_elem_id), ByteAryFmtrArg_.fmtr_(gallery_style, ByteAryFmtrArg_.int_(gallery_w)), ByteAryFmtrArg_.bfr_(itm_bfr));
	}
	public static int Calc_itm_div_w(int v) {return v + 30;}	// 30=Thumb padding; REF.MW:ImageGallery.php|THUMB_PADDING; TODO:CFG
	public static int Calc_itm_div_h(int v) {return v + 30;}	// 30=Thumb padding; REF.MW:ImageGallery.php|THUMB_PADDING; TODO:CFG
	public static int Calc_itm_box_w(int v) {return v + 5;}		// 5=Gallery Box padding; REF.MW:ImageGallery.php|GB_PADDING; TODO:CFG
	public static int Calc_itm_pad_w(int v) {return v + 8;}		// 8=Gallery Box borders; REF.MW:ImageGallery.php|GB_BORDERS; TODO:CFG
	public static int Calc_vpad(int mgr_itm_height, int html_h) {
		int	min_thumb_height = html_h > 17 ? html_h : 17;						// $minThumbHeight =  $thumb->height > 17 ? $thumb->height : 17;
		return (int)Math_.Floor((30 + mgr_itm_height - min_thumb_height) / 2);	// $vpad = floor(( self::THUMB_PADDING + $this->mHeights - $minThumbHeight ) /2);
	}
	public ByteAryFmtr Html_gallery_box() {return html_gallery_box;} private ByteAryFmtr html_gallery_box = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	"<ul id=\"xowa_gallery_ul_~{img_id}\" class=\"gallery\" style=\"~{gallery_style}\">"
	,	"~{itm_list}</ul>"
	)
	,	"img_id", "gallery_style", "itm_list"
	)
	, html_gallery_box_style_default = ByteAryFmtr.new_("width:~{gallery_width}px", "gallery_width")
	, html_gallery_box_style_per_row = ByteAryFmtr.new_("max-width:~{gallery_width}px; _width:~{gallery_width}px;", "gallery_width");
	public ByteAryFmtr Html_gallery_itm_img() {return html_gallery_itm_img;} private ByteAryFmtr html_gallery_itm_img = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	"  <li id=\"xowa_gallery_li_~{img_id}\" class=\"gallerybox\" style=\"width:~{itm_box_width}px;\">"
	,	"    <div id=\"xowa_gallery_div1_~{img_id}\" style=\"width:~{itm_box_width}px;\">"
	,	"      <div id=\"xowa_gallery_div2_~{img_id}\" class=\"thumb\" style=\"width:~{itm_div_width}px;\">"
	,	"        <div id=\"xowa_gallery_div3_~{img_id}\" style=\"margin:~{itm_margin}px auto;\">"
	,	"          <a href=\"~{img_href}\" class=\"image\">"
	,	"            <img id=\"xowa_file_img_~{img_id}\" alt=\"~{img_alt}\" src=\"~{html_src}\" width=\"~{img_width}\" height=\"~{img_height}\" />"
	,	"          </a>"
	,	"        </div>"
	,	"      </div>"
	,	"      <div class=\"gallerytext\">~{itm_caption}"
	,	"      </div>"
	,	"    </div>"
	,	"  </li>"
	,	""
	),	"itm_box_width", "itm_div_width", "itm_margin", "img_id", "img_ttl", "img_href", "html_src", "img_width", "img_height", "itm_caption", "img_alt"
	)
	, html_gallery_itm_txt = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	"  <li id=\"xowa_gallery_li_~{img_id}\" class=\"gallerybox\" style=\"width:~{itm_box_width};\">"
	,	"    <div id=\"xowa_gallery_div1_~{img_id}\" style=\"width:~{itm_box_width};\">"
	,	"      <div id=\"xowa_gallery_div2_~{img_id}\" style=\"~{itm_div_height}\">"
	,	"        ~{itm_text}"
	,	"      </div>"
	,	"      <div class=\"gallerytext\">~{itm_caption}"
	,	"      </div>"
	,	"    </div>"
	,	"  </li>"
	,	""
	),	"itm_box_width", "itm_div_height", "itm_text", "itm_caption"
	);
}

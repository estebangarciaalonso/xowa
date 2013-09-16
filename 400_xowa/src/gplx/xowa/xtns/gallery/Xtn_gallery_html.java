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
public class Xtn_gallery_html {
	public void Write_html(Xoa_app app, Xow_wiki wiki, Xop_ctx ctx, Xoa_page page, Xoh_html_wtr wtr, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		Xtn_gallery_nde mgr = (Xtn_gallery_nde)xnde.Xnde_data();
		int itms_len = mgr.Itms_len();
		int itm_w = mgr.Itm_width(), itm_h = mgr.Itm_height();
		int itm_div_w = itm_w + 30;					// 30=Thumb padding; REF.MW:ImageGallery.php|THUMB_PADDING; TODO:CFG
		int itm_div_h = itm_h + 30;					// 30=Thumb padding; REF.MW:ImageGallery.php|THUMB_PADDING; TODO:CFG
		int itm_box_w = itm_div_w + 5;				// 5=Gallery Box padding; REF.MW:ImageGallery.php|GB_PADDING; TODO:CFG
		int itms_per_row = mgr.Itms_per_row();
		int gallery_multiplier = itms_len;
		ByteAryFmtr gallery_style = html_gallery_box_style_default;
		if (itms_per_row > 0) {
			gallery_multiplier = itms_per_row;
			gallery_style = html_gallery_box_style_per_row;
		}
		int gallery_w = (itm_box_w + 8) * gallery_multiplier; // 8=Gallery Box borders; REF.MW:ImageGallery.php|GB_BORDERS; TODO:CFG
		ByteAryBfr itm_bfr = ByteAryBfr.new_(), caption_bfr = ByteAryBfr.new_();
		for (int i = 0; i < itms_len; i++) {
			Xtn_gallery_itm itm = mgr.Itms_get(i);
			byte[] lnki_caption = itm.Caption_bry();
			if (lnki_caption != null) {
				Xop_root_tkn caption_root = wiki.Parser().Parse_recurse(ctx, lnki_caption, true);
				wtr.Write_tkn(opts, caption_bfr, caption_root.Root_src(), depth + 1, caption_root, Xoh_html_wtr.Sub_idx_null, caption_root);
				lnki_caption = caption_bfr.XtoAryAndClear();
			}
			Xoa_ttl itm_ttl = itm.Ttl();
			if (itm_ttl != null && itm_ttl.Ns().Id_file()) {	// && fileDownloadEnabled
				Xop_lnki_tkn lnki = ctx.Tkn_mkr().Lnki(itm.Ttl_bgn(), itm.Ttl_end()).Ttl_(itm_ttl).Width_(itm_w).Height_(itm_h);
				Xof_xfer_itm xfer_itm = wtr.Lnki_wtr().Lnki_eval(page, lnki, wtr.Queue_add_ref());
				int elem_id = xfer_itm.Html_dynamic_id();
				xfer_itm.Gallery_data_(new Xtn_gallery_dynamic_data().Init(mgr.Itm_height()));
				xfer_itm.Html_dynamic_tid_(Xof_xfer_itm.Html_dynamic_tid_gallery);
				byte[] html_src = xfer_itm.Html_view_src();
				int html_w = xfer_itm.Html_w();
				int html_h = xfer_itm.Html_h();

				int gallery_itm_w = html_w, gallery_itm_h = html_h;
				if (html_src.length == 0) {
					gallery_itm_w = mgr.Itm_width(); gallery_itm_h = mgr.Itm_height();
				}
				int	min_thumb_height = gallery_itm_h > 17 ? gallery_itm_h : 17;					// $minThumbHeight =  $thumb->height > 17 ? $thumb->height : 17;
				int v_pad = (int)Math_.Floor((30 + mgr.Itm_height() - min_thumb_height) / 2);	// $vpad = floor(( self::THUMB_PADDING + $this->mHeights - $minThumbHeight ) /2);
				byte[] lnki_ttl = lnki.Ttl().Page_txt();
				Xoa_ttl lnki_link_ttl = itm_ttl;
				if (itm.Link_bgn() != ByteAry_.NotFound)
					lnki_link_ttl = Xoa_ttl.parse_(wiki, ByteAry_.Mid(src, itm.Link_bgn(), itm.Link_end()));
				byte[] lnki_href = app.Href_parser().Build_to_bry(lnki_link_ttl, wiki);
				byte[] lnki_alt = itm.Alt_bgn() == ByteAry_.NotFound ? lnki_ttl : ByteAry_.Mid(src, itm.Alt_bgn(), itm.Alt_end()); 
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
		html_gallery_box.Bld_bfr(bfr, ByteAryFmtrArg_.fmtr_(gallery_style, ByteAryFmtrArg_.int_(gallery_w)), ByteAryFmtrArg_.bfr_(itm_bfr));
	}
	public ByteAryFmtr Html_gallery_box() {return html_gallery_box;} ByteAryFmtr html_gallery_box = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	"<ul class=\"gallery\" style=\"~{gallery_style}\">"
		,	"~{itm_list}</ul>"
		)
		,	"gallery_style", "itm_list"
		)
	, html_gallery_box_style_default = ByteAryFmtr.new_("width:~{gallery_width}px", "gallery_width")
	, html_gallery_box_style_per_row = ByteAryFmtr.new_("max-width:~{gallery_width}px; _width:~{gallery_width}px;", "gallery_width");
	public ByteAryFmtr Html_gallery_itm_img() {return html_gallery_itm_img;} ByteAryFmtr html_gallery_itm_img = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	"  <li class=\"gallerybox\" style=\"width:~{itm_box_width}px;\">"
		,	"    <div style=\"width:~{itm_box_width}px;\">"
		,	"      <div class=\"thumb\" style=\"width:~{itm_div_width}px;\">"
		,	"        <div id=\"xowa_file_gallery_div_~{img_id}\" style=\"margin:~{itm_margin}px auto;\">"
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
		(	"  <li class=\"gallerybox\" style=\"width:~{itm_box_width};\">"
		,	"    <div style=\"width:~{itm_box_width};\">"
		,	"      <div style=\"~{itm_div_height}\">"
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

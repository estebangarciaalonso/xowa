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
import gplx.html.*; import gplx.xowa.html.*;
import gplx.xowa.files.*;
abstract class Gallery_mgr_base {
	public abstract byte Tid();
	public abstract byte[] Tid_bry();
	@gplx.Virtual public boolean Tid_is_packed() {return false;}
	public int Itm_default_w() {return itm_default_w;} protected int itm_default_w;
	public int Itm_default_h() {return itm_default_h;} protected int itm_default_h;
	public int Itms_per_row() {return itms_per_row;} @gplx.Virtual public void Itms_per_row_(int v) {this.itms_per_row = v;} protected int itms_per_row;		
	@gplx.Virtual public int Get_thumb_padding() {return 30;}	// REF.MW: getThumbPadding; How much padding such the thumb have between image and inner div that that contains the border. This is both for verical and horizontal padding. (However, it is cut in half in the vertical direction).
	@gplx.Virtual public int Get_gb_padding() {return 5;}	// REF.MW: getGBPadding; GB stands for gallerybox (as in the <li class="gallerybox"> element)
	@gplx.Virtual public int Get_gb_borders() {return 8;}	// REF.MW: getGBBorders; Get how much extra space the borders around the image takes up. For this mode, it is 2px borders on each side + 2px implied padding on each side from the stylesheet, giving us 2*2+2*2 = 8.
	@gplx.Virtual public int Get_all_padding() {return this.Get_thumb_padding() + this.Get_gb_padding() + this.Get_gb_borders();} // REF.MW: getAllPadding; How many pixels of whitespace surround the thumbnail.
	@gplx.Virtual public int Get_vpad(int itm_h, int thm_h) {	// REF.MW: getVPad; Get vertical padding for a thumbnail; Generally this is the total height minus how high the thumb is.
		return (this.Get_thumb_padding() + itm_h - thm_h) / 2;
	}
	@gplx.Virtual public int Get_thumb_div_width(int thm_w) {	// REF.MW: getThumbDivWidth; Get the width of the inner div that contains the thumbnail in question. This is the div with the class of "thumb".
		return itm_default_w + this.Get_thumb_padding();
	}
	@gplx.Virtual public int Get_gb_width(int thm_w, int thm_h) {// REF.MW: getGBWidth; Width of gallerybox <li>. Generally is the width of the image, plus padding on image plus padding on gallerybox.s
		return itm_default_w + this.Get_thumb_padding() + this.Get_gb_padding();
	}
	@gplx.Virtual public String[] Get_modules() {return String_.Ary_empty;} // REF.MW: getModules; Get a list of modules to include in the page.
	@gplx.Virtual public void Init(int itms_per_row, int itm_default_w, int itm_default_h) {
		this.itms_per_row = itms_per_row;
		this.itm_default_w = itm_default_w;
		this.itm_default_h = itm_default_h;
	}
	@gplx.Virtual public void Wrap_gallery_text(ByteAryBfr bfr, byte[] gallery_text, int thm_w, int thm_h) {
		bfr	.Add(Wrap_gallery_text_bgn)
			.Add(gallery_text)
			.Add(Wrap_gallery_text_end)
			;
	}
	private static final byte[] 
	  Wrap_gallery_text_bgn = ByteAry_.new_ascii_("\n      <div class=\"gallerytext\">") // NOTE: The newline after <div class="gallerytext"> is needed to accommodate htmltidy
	, Wrap_gallery_text_end = ByteAry_.new_ascii_("\n      </div>")
	;
	@gplx.Virtual public void Get_thumb_size(IntRef thm_w, IntRef thm_h, Xof_ext ext) { // REF.MW: getThumbParams; Get the transform parameters for a thumbnail.
		thm_w.Val_(itm_default_w);
		thm_h.Val_(itm_default_h);
	}
	@gplx.Virtual public void Adjust_image_parameters(Xof_xfer_itm xfer_itm, IntRef rslt_w, IntRef rslt_h) {	// REF.MW: Adjust the image parameters for a thumbnail. Used by a subclass to insert extra high resolution images.
	}
	private static final ByteAryFmtr
	  fmtr_gb_style_max_width		= ByteAryFmtr.new_(	"max-width:~{max_width}px;_width:~{max_width}px;", "max_width")
	, fmtr_gb_cls					= ByteAryFmtr.new_(	"gallery mw-gallery-~{mode}", "mode")
	, fmtr_gb_caption				= ByteAryFmtr.new_(	"\n  <li class='gallerycaption'>~{caption}</li>", "caption")
	, fmtr_ib_caption				= ByteAryFmtr.new_(	"\n        <div class=\"thumb\" style=\"height: ~{height}px;\">~{ttl_text}</div>", "caption", "ttl_text")
	, fmtr_ib_html					= ByteAryFmtr.new_(	"\n  <li class=\"gallerybox\" style=\"width: ~{width}px\">"
													  + "\n    <div style=\"width: ~{width}px\">", "width")
	, fmtr_img_div0_html			= ByteAryFmtr.new_(	"\n      <div class=\"thumb\" style=\"width: ~{width}px;\">", "width")
	, fmtr_img_div1_html			= ByteAryFmtr.new_(	"\n        <div style=\"margin:~{vpad}px auto;\">\n          ", "vpad")
	;

	private static final byte[] 
	  bry_ib_html_end = ByteAry_.new_ascii_			  ( "\n    </div>"
													  + "\n  </li>")
	, bry_gb_html_end = ByteAry_.new_ascii_			  ( "\n</ul>")
//		, bry_show_filenames_end = ByteAry_.new_ascii_	  ( "<br />\n")
	, bry_img_div_end = ByteAry_.new_ascii_			  ( "\n        </div>\n      </div>")
	;
	private static byte[] Fmt_w_trailer(ByteAryBfr tmp_bfr, ByteAryFmtr fmtr, byte[] trailer, Object... fmtr_args) {
		fmtr.Bld_bfr_many(tmp_bfr, fmtr_args);
		if (ByteAry_.Len_gt_0(trailer)) {
			tmp_bfr.Add_byte_space();
			tmp_bfr.Add(trailer);
		}
		return tmp_bfr.XtoAryAndClear();
	}
	private static void Write_ul_tag_bgn(ByteAryBfr bfr, byte[] tag_name, byte[] cls, byte[] style, ListAdp bry_list) {
		bfr.Add_byte(Byte_ascii.Lt).Add(tag_name);			// <tag_name
		Html_wtr.Write_atr(bfr, Html_atrs.Cls_bry, cls);
		Html_wtr.Write_atr(bfr, Html_atrs.Style_bry, style);
		if (bry_list != null) {
			int bry_list_len = bry_list.Count();
			for (int i = 0; i < bry_list_len; i += 2) {
				byte[] key = (byte[])bry_list.FetchAt(i);
				byte[] val = (byte[])bry_list.FetchAt(i + 1);
				Html_wtr.Write_atr(bfr, key, val);
			}
		}
		bfr.Add_byte(Byte_ascii.Gt);
	}
	private IntRef thm_w_tmp = IntRef.neg1_(), thm_h_tmp = IntRef.neg1_();
	public void Write_html(ByteAryBfr bfr, Xow_wiki wiki, Xoa_page page, Xop_ctx ctx, byte[] src, Gallery_xnde xnde) {
		Xoh_html_wtr wtr = wiki.Html_wtr();
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
		byte[] gb_style = xnde.Atr_style();
		if (itms_per_row > 0) {
			int max_width = itms_per_row * (itm_default_w + this.Get_all_padding());
			gb_style = Fmt_w_trailer(tmp_bfr, fmtr_gb_style_max_width, gb_style, max_width);
		}
		byte[] gb_cls = Fmt_w_trailer(tmp_bfr, fmtr_gb_cls, xnde.Atr_cls(), this.Tid_bry());
		// page.Html_data().Modules().Add(this.Get_modules()); xtn_mgr

		Write_ul_tag_bgn(bfr, Html_consts.Ul_tag_bry, gb_cls, gb_style, xnde.Atrs_other());
		byte[] gb_caption = xnde.Atr_caption();
		if (gb_caption != Gallery_xnde.Null_bry)
			fmtr_gb_caption.Bld_bfr_many(bfr, gb_caption);

		int itm_len = xnde.Itms_len();
		IntRef thm_w = thm_w_tmp, thm_h = thm_h_tmp;
		Xoa_app app = wiki.App();
		for (int i = 0; i < itm_len; i++) {
			Gallery_itm itm = (Gallery_itm)xnde.Itms_get_at(i);
			Xoa_ttl ttl = itm.Ttl();
			byte[] caption = itm.Caption_bry(); if (caption == null) caption = ByteAry_.Empty;

			Xop_lnki_tkn lnki = ctx.Tkn_mkr().Lnki(itm.Ttl_bgn(), itm.Ttl_end()).Ttl_(ttl);
			Xof_ext ext = itm.Ext();
			this.Get_thumb_size(thm_w, thm_h, ext);
			lnki.Width_(thm_w.Val()).Height_(thm_h.Val());
			Xof_xfer_itm xfer_itm = wtr.Lnki_wtr().File_wtr().Lnki_eval(ctx, page, lnki, wtr.Queue_add_ref())
				.Gallery_mgr_h_(xnde.Itm_h_or_default())
				.Html_elem_tid_(Xof_html_elem.Tid_gallery)
				;
			if (!ttl.Ns().Id_file()) {	// text
				fmtr_ib_caption.Bld_bfr_many(bfr, this.Get_thumb_padding() + itm_default_h, caption);
				return;
			}
			int vpad = this.Get_vpad(itm_default_h, thm_w.Val());
			byte[] alt = itm.Alt_bgn() == ByteAry_.NotFound && ByteAry_.Len_eq_0(caption)	//	if ( $alt == '' && $text == '' )  $imageParameters['alt'] = $nt->getText();
				? itm.Ttl().Page_txt()
				: Xoh_html_wtr_escaper.Escape(app, tmp_bfr, ByteAry_.Mid(src, itm.Alt_bgn(), itm.Alt_end()))
				;
			Xoa_ttl href_ttl = itm.Link_bgn() == ByteAry_.NotFound
				? ttl
				: Xoa_ttl.parse_(wiki, ByteAry_.Mid(src, itm.Link_bgn(), itm.Link_end()))
				;
			if (href_ttl == null) href_ttl = ttl;	// occurs when link is invalid; EX: A.png|link=<invalid>
			this.Adjust_image_parameters(xfer_itm, thm_w, thm_h);

			fmtr_img_div0_html.Bld_bfr_many(tmp_bfr, this.Get_thumb_div_width(thm_w.Val()));
			fmtr_img_div1_html.Bld_bfr_many(tmp_bfr, vpad);				// <div style="margin:~{vpad}px auto;">
			wiki.Html_wtr().Lnki_wtr().Write_file(tmp_bfr, page, ctx, Xoh_html_wtr_ctx.Basic, src, lnki, alt);
			tmp_bfr.Add(bry_img_div_end);
			byte[] itm_html = tmp_bfr.XtoAryAndClear();

			byte[] show_filenames_link = ByteAry_.Empty;
			if (xnde.Show_filename()) {
				wiki.Html_wtr().Lnki_wtr().Write_plain_by_bry
				( tmp_bfr, src, lnki
				, ByteAry_.Limit(ttl.Page_txt(), 25) // 25 is defined by captionLength in DefaultSettings.php					
				);	// MW:passes know,noclasses which isn't relevant to XO
			}
			int gb_width = this.Get_gb_width(thm_w.Val(), thm_h.Val());
			fmtr_ib_html.Bld_bfr_many(bfr, gb_width);
			bfr.Add(itm_html);
			byte[] gb_text = caption;
			Xop_parser_.Parse_to_html(tmp_bfr, wiki, true, gb_text);
			gb_text = tmp_bfr.XtoAryAndClear();
			gb_text = tmp_bfr.Add(show_filenames_link).Add(gb_text).XtoAryAndClear();
			Wrap_gallery_text(bfr, gb_text, thm_w.Val(), thm_h.Val());
			bfr.Add(bry_ib_html_end);
		}
		bfr.Add(bry_gb_html_end);
		tmp_bfr.Mkr_rls();
	}
}
class Xoh_lnki_basic_wtr {
	public static byte[] Write_as_bry(ByteAryBfr tmp_bfr, Xoa_ttl href_ttl, byte[] caption, byte[][] options) {
		return ByteAry_.Empty;
	}
}
class Gallery_mgr_traditional extends Gallery_mgr_base {
	@Override public byte Tid() {return Gallery_mgr_base_.Traditional_tid;}		
	@Override public byte[] Tid_bry() {return Gallery_mgr_base_.Traditional_bry;}
        public static final Gallery_mgr_traditional _ = new Gallery_mgr_traditional(); Gallery_mgr_traditional() {}
}
class Gallery_mgr_nolines extends Gallery_mgr_base {
	@Override public byte Tid() {return Gallery_mgr_base_.Nolines_tid;}
	@Override public byte[] Tid_bry() {return Gallery_mgr_base_.Nolines_bry;}
	@Override public int Get_thumb_padding()				{return 0;}
	@Override public int Get_gb_padding()				{return 4;} // This accounts for extra space between <li> elements.
	@Override public int Get_vpad(int itm_h, int thm_h)	{return 0;}
        public static final Gallery_mgr_nolines _ = new Gallery_mgr_nolines(); Gallery_mgr_nolines() {}
}

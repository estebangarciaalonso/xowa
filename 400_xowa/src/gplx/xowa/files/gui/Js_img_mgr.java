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
package gplx.xowa.files.gui; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.xowa.xtns.gallery.*;
import gplx.xowa.files.fsdb.*;
public class Js_img_mgr {
	public static void Update_img(Xog_win_wtr wtr, Xof_xfer_itm itm) {
		Js_img_mgr.Update_img(wtr, itm.Html_uid(), itm.Lnki_type(), String_.new_utf8_(itm.Html_view_src()), itm.Html_w(), itm.Html_h(), itm.Html_elem_tid(), String_.new_utf8_(itm.Html_orig_src()), itm.Gallery_mgr_h());
	}
	public static void Update_img(Xog_win_wtr wtr, Xof_fsdb_itm itm) {
		Js_img_mgr.Update_img(wtr, itm.Html_uid(), itm.Lnki_type(), itm.Html_url().To_http_file_str(), itm.Html_w(), itm.Html_h(), itm.Html_elem_tid(), itm.Html_orig_url().To_http_file_str(), itm.Gallery_mgr_h());
	}
	public static void Update_link_missing(Xog_win_wtr wtr, String html_id) {
		wtr.Html_elem_atr_set_append(html_id, "class", " new");
	}
	public static void Update_img(Xog_win_wtr wtr, int uid, byte lnki_type, String src, int w, int h, byte elem_tid, String orig_src, int gallery_mgr_h) {
		String html_id = "xowa_file_img_" + uid;
		wtr.Html_img_update(html_id, src, w, h);
		if (Xop_lnki_type.Id_is_thumbable(lnki_type)) {	// thumb needs to set cls and width
			wtr.Html_atr_set(html_id, "class", gplx.xowa.html.Xow_html_mgr.Str_img_class_thumbimage);
			wtr.Html_atr_set("xowa_file_div_" + uid, "style", "width:" + w + "px;");
		}
		switch (elem_tid) {
			case Xof_html_elem.Tid_gallery:
				// TODO: only apply when mode="packed"
//					int div_w = Gallery_html.Calc_itm_div_w(w);
//					int box_w = Gallery_html.Calc_itm_box_w(div_w);
//					wtr.Html_atr_set("xowa_gallery_li_"   + uid, "style", "width:" + box_w + "px;");
//					wtr.Html_atr_set("xowa_gallery_div1_" + uid, "style", "width:" + box_w + "px;");
//					wtr.Html_atr_set("xowa_gallery_div2_" + uid, "style", "width:" + div_w + "px;");
				wtr.Html_atr_set("xowa_gallery_div3_" + uid, "style", "margin:" + Gallery_html.Calc_vpad(gallery_mgr_h, h) + "px auto;");
				break;
			case Xof_html_elem.Tid_vid:
				String html_id_vid = "xowa_file_play_" + uid;
				wtr.Html_atr_set(html_id_vid, "style", "width:" + w + "px;max-width:" + (w - 2) + "px;");
				wtr.Html_atr_set(html_id_vid, "href", orig_src);
				break;
		}
	}
}

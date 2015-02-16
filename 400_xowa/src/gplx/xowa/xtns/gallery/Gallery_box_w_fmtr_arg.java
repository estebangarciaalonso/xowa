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
import gplx.html.*; import gplx.xowa.hdumps.htmls.*;
public interface Gallery_box_w_fmtr_arg extends Bry_fmtr_arg {
	Gallery_box_w_fmtr_arg Init(int uid, int width);
}
class Gallery_box_w_fmtr_arg__basic implements Gallery_box_w_fmtr_arg {
	private int width;
	public Gallery_box_w_fmtr_arg Init(int uid, int width) {this.width = width; return this;}
	public void XferAry(Bry_bfr bfr, int idx) {
		bfr.Add(Style_bgn);
		bfr.Add_int_variable(width);
		bfr.Add(Style_end);
	}
	private static final byte[] Style_bgn = Bry_.new_ascii_("style=\"width: "), Style_end = Bry_.new_ascii_("px\"");
}
class Gallery_box_w_fmtr_arg__hdump implements Gallery_box_w_fmtr_arg {
	private int uid;
	public Gallery_box_w_fmtr_arg Init(int uid, int width) {this.uid = uid; return this;}
	public void XferAry(Bry_bfr bfr, int idx) {
		bfr.Add(Hdump_html_consts.Key_gallery_box_w);
		bfr.Add_int_variable(uid);
		bfr.Add_byte_apos();
	}
}
interface Gallery_img_pad_fmtr_arg extends Bry_fmtr_arg {
	Gallery_img_pad_fmtr_arg Init(int uid, int vpad);
}
class Gallery_img_pad_fmtr_arg__basic implements Gallery_img_pad_fmtr_arg {
	private int vpad;
	public Gallery_img_pad_fmtr_arg Init(int uid, int vpad) {this.vpad = vpad; return this;}
	public void XferAry(Bry_bfr bfr, int idx) {
		bfr.Add(Style_bgn);
		bfr.Add_int_variable(vpad);
		bfr.Add(Style_end);
	}
	private static final byte[] Style_bgn = Bry_.new_ascii_("style=\"margin:"), Style_end = Bry_.new_ascii_("px auto;\"");
}
class Gallery_img_pad_fmtr_arg__hdump implements Gallery_img_pad_fmtr_arg {
	private int uid;
	public Gallery_img_pad_fmtr_arg Init(int uid, int width) {this.uid = uid; return this;}
	public void XferAry(Bry_bfr bfr, int idx) {
		bfr.Add(Hdump_html_consts.Key_gallery_img_pad);
		bfr.Add_int_variable(uid);
		bfr.Add_byte_apos();
	}
}

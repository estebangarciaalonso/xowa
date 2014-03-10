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
package gplx.xowa.xtns.hiero; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.html.*;
class Hiero_html_wtr {
	private ByteAryBfr html_bfr = ByteAryBfr.reset_(Io_mgr.Len_kb);
	private ByteAryBfr tbl_content_bfr = ByteAryBfr.reset_(Io_mgr.Len_kb);
	private ByteAryBfr tbl_bfr = ByteAryBfr.reset_(Io_mgr.Len_kb);
	private ByteAryBfr content_bfr = ByteAryBfr.reset_(255);
	private ByteAryBfr option_bfr = ByteAryBfr.reset_(255);
	private ByteAryBfr temp_bfr = ByteAryBfr.reset_(255);
	private boolean cartouche_enabled = false;
	private int scale = 100;
	public void Render_blocks(Hiero_block[] blocks, int scale, boolean hr_enabled) {
		this.scale = scale;
		tbl_bfr.Clear(); content_bfr.Clear(); option_bfr.Clear(); temp_bfr.Clear();
		cartouche_enabled = false;
		if (hr_enabled)
			Add_hr(html_bfr);
		int blocks_len = blocks.length;
		for (int i = 0; i < blocks_len; i++) {
			Hiero_block block = blocks[i];
			if (block.Len() == 1)
				Render_block_single(tbl_bfr, content_bfr, hr_enabled, block);
			else
				Render_block_many(tbl_bfr, content_bfr, hr_enabled, block);
			if (content_bfr.Len_gt_0())
				tbl_content_bfr.Add_bfr_and_clear(tbl_bfr).Add_bfr_and_clear(content_bfr);
		}
		if (tbl_content_bfr.Len_gt_0())
			html_bfr.Add_bfr_and_clear(tbl_content_bfr); //	$html .= self::TABLE_START . "<tr>\n" . $tableContentHtml . '</tr></table>';
//			return "<table class='mw-hiero-table mw-hiero-outer' dir='ltr'><tr><td>\n$html\n</td></tr></table>";
	}
	private void Render_block_single(ByteAryBfr tbl_bfr, ByteAryBfr content_bfr, boolean hr_enabled, Hiero_block block) {
		byte[] bry = block.Get_at(0);
		byte b_0 = bry[0];
		if		(b_0 == Byte_ascii.Bang) { // end of line
			tbl_bfr.Add(Const_block_single_eol_1);
			if (hr_enabled)
				Add_hr(content_bfr);
		}
		else if (Byte_ary_finder.Find_fwd(bry, Byte_ascii.Lt) != Byte_ary_finder.Not_found) {	// cartouche bgn
			content_bfr.Add(Html_consts.Td_bgn_bry).Add(Render_glyph(b_0)).Add(Html_consts.Td_end_bry);
			cartouche_enabled = true;
			content_bfr.Add(Const_tr_bgn)
				.Add_int_variable((Cartouche_width * scale) / 100).Add(Const_tr_end)
				;
		}
		else if (Byte_ary_finder.Find_fwd(bry, Byte_ascii.Gt) != Byte_ary_finder.Not_found) {	// cartouche end
			content_bfr.Add(Const_tr_bgn)
				.Add_int_variable((Cartouche_width * scale) / 100)
				.Add(Const_tbl_end)
				;
			cartouche_enabled = false;
			content_bfr.Add(Html_consts.Td_bgn_bry).Add(Render_glyph(b_0, option_bfr)).Add(Html_consts.Td_end_bry);
		}
		else {// assume it's a glyph or '..' or '.']
			option_bfr.Add(Const_option_bgn).Add(Resize_glyph(b_0, cartouche_enabled)).Add(Const_option_end);
			content_bfr.Add(Html_consts.Td_bgn_bry).Add(Render_glyph(b_0, option_bfr)).Add(Html_consts.Td_end_bry);
		}
	}
	private void Render_block_many(ByteAryBfr tbl_bfr, ByteAryBfr content_bfr, boolean hr_enabled, Hiero_block block) {
		// convert all codes into '&' to test prefabs glyph
		temp_bfr.Clear();
		int block_len = block.Len();
		boolean amp = false;
		for (int i = 0; i < block_len; i++) {
			byte[] v = block.Get_at(i);
			int v_len = v.length;
			amp = false;
			if (v_len == 1) {
				switch (v[0]) {
					case Byte_ascii.Brack_bgn:
					case Byte_ascii.Brack_end:
					case Byte_ascii.Paren_bgn:
					case Byte_ascii.Paren_end:
					case Byte_ascii.Asterisk:
					case Byte_ascii.Colon:
					case Byte_ascii.Bang:
						amp = true;
						break;
				}
			}
			if (amp)
				temp_bfr.Add_byte(Byte_ascii.Amp);
			else
				temp_bfr.Add(v);
		}
	}
	private byte[] Render_glyph(byte b) {return this.Render_glyph(b, null);}
	private byte[] Render_glyph(byte b, ByteAryBfr option){
		return ByteAry_.Empty;
	}
	private byte[] Resize_glyph(byte b, boolean cartouche_enabled) {
//			int bry_len = bry.length;
//			if (bry_len == 0) return bry;
//			byte[] image_cls = bry[bry_len - 1] == Byte_ascii.Backslash	// REF.MW:isMirrored
//				? Bry_cls_mirrored
//				: ByteAry_.Empty
//				;
//			byte[] glyph = Trim_end(bry, Byte_ascii.Backslash, bry.length);
//			if (ByteAry_.Eq(glyph, Bry_dot_dot)) {
//				return ByteAry_.Add(Const_void_bgn, ByteAry_.XbyInt(MAX_HEIGHT), Const_void_end);
//			}
//			if (ByteAry_.Eq(glyph, Bry_dot)) {
//				return ByteAry_.Add(Const_void_bgn, ByteAry_.XbyInt(MAX_HEIGHT / 2), Const_void_end);
//			}
//			return image_cls;
		return null;
	}
	private void Add_hr(ByteAryBfr bfr) {
		bfr.Add(Html_consts.Hr_bry).Add_byte_nl();
	}
	private static final int Cartouche_width = 2;
//		private static final byte[] Bry_cls_mirrored = ByteAry_.new_ascii_(" class=\"mw-mirrored\" ");
//		private static final int MAX_HEIGHT = 44;
//		private static final byte[]
//		  Const_void_bgn = ByteAry_.new_ascii_("<table class=\"mw-hiero-table\" style=\"width: ")//{$width};
//		, Const_void_end = ByteAry_.new_ascii_("px;\"><tr><td>&#160;</td></tr></table>")
//		;
//		private static final byte[] Bry_dot_dot = new byte[] {Byte_ascii.Dot, Byte_ascii.Dot}, Bry_dot = new byte[] {Byte_ascii.Dot};
//		private static byte[] Trim_end(byte[] v, byte trim, int pos) {
//			boolean trimmed = false;
//			for (; pos > -1; pos--) {
//				if (v[pos] != trim) {
//					trimmed = true;
//					break;
//				}
//			}
//			return trimmed ? ByteAry_.Mid(v, 0, pos) : v;
//		}
	private static final String Const_tbl_start_str = "<table class=\"mw-hiero-table\">";
	private static final byte[]
	  Const_block_single_eol_1 = ByteAry_.new_ascii_("</tr></table>" + Const_tbl_start_str + "<tr>\n")
	, Const_tr_bgn = ByteAry_.new_ascii_("<td>" + Const_tbl_start_str + "<tr><td class='mw-hiero-box' style='height: ")
	, Const_tr_end = ByteAry_.new_ascii_("px;'></td></tr><tr><td>" +Const_tbl_start_str +  "<tr>")
	, Const_tbl_end = ByteAry_.new_ascii_("px;'></td></tr></table></td>")
	, Const_option_bgn = ByteAry_.new_ascii_("height: ")
	, Const_option_end = ByteAry_.new_ascii_("px;")
	;
}

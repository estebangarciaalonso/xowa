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
interface Srl_mgr {
	void Srl_add(int flds_len, byte[][] flds);
}
class Hiero_html_wtr {
	private Hash_adp_bry phonemes = Hash_adp_bry.cs_();
	private ByteAryBfr html_bfr = ByteAryBfr.reset_(Io_mgr.Len_kb);
	private ByteAryBfr tbl_content_bfr = ByteAryBfr.reset_(Io_mgr.Len_kb);
	private ByteAryBfr tbl_bfr = ByteAryBfr.reset_(Io_mgr.Len_kb);
	private ByteAryBfr content_bfr = ByteAryBfr.reset_(255);
	private ByteAryBfr option_bfr = ByteAryBfr.reset_(255);
	private ByteAryBfr temp_bfr = ByteAryBfr.reset_(255);
	private ByteAryBfr fmtr_bfr = ByteAryBfr.reset_(255);
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
		else if (b_0 == Byte_ascii.Lt) {	// cartouche bgn
			content_bfr.Add(Html_consts.Td_bgn_bry).Add(Render_glyph(Tkn_lt)).Add(Html_consts.Td_end_bry);
			cartouche_enabled = true;
			content_bfr.Add(Const_tr_bgn)
				.Add_int_variable((Cartouche_width * scale) / 100).Add(Const_tr_end)
				;
		}
		else if (b_0 == Byte_ascii.Gt) {	// cartouche end
			content_bfr.Add(Const_tr_bgn)
				.Add_int_variable((Cartouche_width * scale) / 100)
				.Add(Const_tbl_end)
				;
			cartouche_enabled = false;
			content_bfr.Add(Html_consts.Td_bgn_bry).Add(Render_glyph(Tkn_gt, option_bfr)).Add(Html_consts.Td_end_bry);
		}
		else {// assume it's a glyph or '.'
			option_bfr.Add(Const_option_bgn).Add(Resize_glyph(b_0, cartouche_enabled)).Add(Const_option_end);
			content_bfr.Add(Html_consts.Td_bgn_bry).Add(Render_glyph(Tkn_dot, option_bfr)).Add(Html_consts.Td_end_bry);
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
//	private function renderGlyph( $glyph, $option = '' ) {
//
//		if ( array_key_exists( $glyph, self::$phonemes ) ) {
//			$code = self::$phonemes[$glyph];
//			if ( array_key_exists( $code, self::$files ) ) {
//				return "<img {$imageClass}style='margin: " . self::IMAGE_MARGIN . "px; $option' src='" . htmlspecialchars( self::getImagePath() . self::IMAGE_PREFIX . "{$code}." . self::IMAGE_EXT ) . "' title='" . htmlspecialchars( "{$code} [{$glyph}]" ) . "' alt='" . htmlspecialchars( $glyph ) . "' />";
//			} else {
//				return htmlspecialchars( $glyph );
//			}
//		} elseif ( array_key_exists( $glyph, self::$files ) ) {
//			return "<img {$imageClass}style='margin: " . self::IMAGE_MARGIN . "px; $option' src='" . htmlspecialchars( self::getImagePath() . self::IMAGE_PREFIX . "{$glyph}." . self::IMAGE_EXT ) . "' title='" . htmlspecialchars( $glyph ) . "' alt='" . htmlspecialchars( $glyph ) . "' />";
//		} else {
//			return htmlspecialchars( $glyph );
//		}
//	}
	private byte[] Render_glyph(byte[] src)						{return Render_glyph(src, null);}
	private byte[] Render_glyph(byte[] src, ByteAryBfr option) {
		int src_len = src.length; if (src_len == 0) return src; // bounds check
		byte byte_0 = src[0];
		byte[] image_cls = byte_0 == Byte_ascii.Backslash	// REF.MW:isMirrored
			? Bry_cls_mirrored								// 'class="mw-mirrored" '
			: ByteAry_.Empty
			;
		byte[] glyph = Trim_end(src, Byte_ascii.Backslash, src_len);	// trim backslashes from end; REF.MW:extractCode
		if (ByteAry_.Eq(glyph, Tkn_dot_dot))							// render void block
			return Render_glyph_void(Bool_.Y);
		else if (ByteAry_.Eq(glyph, Tkn_dot))							// render 1/2 width void block
			return Render_glyph_void(Bool_.N);
		else if (ByteAry_.Eq(glyph, Tkn_lt))
			Render_glyph_cartouche(Bool_.Y, glyph);
		else if (ByteAry_.Eq(glyph, Tkn_lt))
			Render_glyph_cartouche(Bool_.N, glyph);
		return image_cls;
	}
	private byte[] Render_glyph_void(boolean half) { // render void
		int width = Max_height;
		if (half) width /= 2;
		return glyph_void_fmtr.Bld_bry_many(fmtr_bfr, width);
	}
	private ByteAryFmtr glyph_void_fmtr = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	( "<table class=\"mw-hiero-table\" style=\"width:~{width}px;\">"
	, "  <tr><td>&160;</td></tr>"
	, "</table>"
	)
	, "width");
	private byte[] Render_glyph_cartouche(boolean bgn, byte[] glyph) { // render open / close cartouche; note that MW has two branches, but they are both the same
		int height = (int)((Max_height * scale) / 100);
		byte[] code = (byte[])phonemes.Get_by_bry(glyph);
		byte[] title = bgn ? Html_consts.Lt : Html_consts.Gt;
		return glyph_cartouche_fmtr.Bld_bry_many(fmtr_bfr, Images_dir, code, height, title);
	}
	private ByteAryFmtr glyph_cartouche_fmtr = ByteAryFmtr.new_(String_.Concat
	( "<img src='~{path}hiero_~{code}.png'"
	, " height='~{height}' title='~{title}'"
	, " alt='~{title}' />"
	)
	, "path", "code", "height", "title");
	private byte[] Images_dir = ByteAry_.Empty;
	private byte[] Resize_glyph(byte b, boolean cartouche_enabled) {return null;}
	private void Add_hr(ByteAryBfr bfr) {
		bfr.Add(Html_consts.Hr_bry).Add_byte_nl();
	}
	private static final int Cartouche_width = 2;
	private static final int Max_height = 44;
	private static final byte[] Bry_cls_mirrored = ByteAry_.new_ascii_(" class=\"mw-mirrored\" ");
	private static byte[] Trim_end(byte[] v, byte trim, int pos) {
		boolean trimmed = false;
		for (; pos > -1; pos--) {
			if (v[pos] != trim) {
				trimmed = true;
				break;
			}
		}
		return trimmed ? ByteAry_.Mid(v, 0, pos) : v;
	}
	private static final byte[]
	  Tkn_lt		= new byte[] {Byte_ascii.Lt}
	, Tkn_gt		= new byte[] {Byte_ascii.Gt}
	, Tkn_dot		= new byte[] {Byte_ascii.Dot}
	, Tkn_dot_dot	= new byte[] {Byte_ascii.Dot, Byte_ascii.Dot}
	;
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
class Hiero_phenome_mgr implements GfoInvkAble, Srl_mgr {
	private Hash_adp_bry hash = Hash_adp_bry.cs_();
	private Srl_wkr srl_wkr;
	public Hiero_phenome_mgr() {
		srl_wkr = new Srl_wkr().Mgr_(this);
	}
	public void Srl_add(int flds_len, byte[][] flds) {
		if (flds_len != 2) throw Err_.new_("phenome requires two fields");
		byte[] key = flds[0];
		Hiero_phenome_itm itm = new Hiero_phenome_itm(key, flds[1]);
		hash.Add(key, itm);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_srl_wkr))			return srl_wkr;
		else	return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_srl_wkr = "srl_wkr";
}
class Hiero_phenome_itm {
	public Hiero_phenome_itm(byte[] key, byte[] gardiner_code) {this.key = key; this.gardiner_code = gardiner_code;}
	public byte[] Key() {return key;} private byte[] key;
	public byte[] Gardiner_code() {return gardiner_code;} private byte[] gardiner_code;
}
class Hiero_prefab_mgr implements GfoInvkAble, Srl_mgr {
	private Hash_adp_bry hash = Hash_adp_bry.cs_();
	private Srl_wkr srl_wkr;
	public Hiero_prefab_mgr() {
		srl_wkr = new Srl_wkr().Mgr_(this);
	}
	public void Srl_add(int flds_len, byte[][] flds) {
		if (flds_len != 1) throw Err_.new_("prefab requires one field");
		byte[] key = flds[0];
		Hiero_prefab_itm itm = new Hiero_prefab_itm(key);
		hash.Add(key, itm);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_srl_wkr))			return srl_wkr;
		else	return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_srl_wkr = "srl_wkr";
}
class Hiero_prefab_itm {
	public Hiero_prefab_itm(byte[] key) {this.key = key;}
	public byte[] Key() {return key;} private byte[] key;
}

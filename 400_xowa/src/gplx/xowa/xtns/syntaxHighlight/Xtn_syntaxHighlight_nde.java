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
package gplx.xowa.xtns.syntaxHighlight; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xtn_syntaxHighlight_nde implements Xop_xnde_xtn, Xop_xnde_atr_parser {
	public boolean Xtn_literal() {return false;}
	public byte[] Lang() {return lang;} private byte[] lang = ByteAry_.Empty;
	public byte[] Style() {return style;} private byte[] style = null;
	public boolean Line_enabled() {return line_enabled;} private boolean line_enabled = false;
	public int Start() {return start;} private int start = 1;
	public int[] Highlight() {return highlight;} private int[] highlight = null;
	public byte[] Enclose() {return enclose;} private byte[] enclose = ByteAry_.Empty;
	public Int_rng_mgr Highlight_idxs() {return highlight_idxs;} Int_rng_mgr highlight_idxs = Int_rng_mgr_null._;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_obj) {
		if (xatr_obj == null) return;
		byte xatr_tid = ((ByteVal)xatr_obj).Val();
		switch (xatr_tid) {
			case Xatr_enclose:		enclose = xatr.Val_as_bry(src); break;
			case Xatr_lang:			lang = xatr.Val_as_bry(src); break;
			case Xatr_style:		style = xatr.Val_as_bry(src); break;
			case Xatr_line:			line_enabled = true; break;
			case Xatr_start:		start = xatr.Val_as_int_or(src, 1); break;
			case Xatr_highlight:	highlight_idxs = new Int_rng_mgr_base(); highlight_idxs.Parse(xatr.Val_as_bry(src)); break;
		}
	}
	public Xop_root_tkn Xtn_root() {return null;}
	public Xop_xnde_tkn Xnde() {return xnde;} private Xop_xnde_tkn xnde;
	public void Xtn_compile(Xop_ctx ctx, Xow_wiki wiki, Xop_tkn_mkr tkn_mkr, byte[] src, Xop_xnde_tkn xnde) {
		Xoa_app app = ctx.App();
		Xop_xatr_itm[] atrs = Xop_xatr_itm.Xatr_parse(app, this, xatrs_syntaxHighlight, wiki, src, xnde);
		this.xnde = xnde;
		xnde.Atrs_ary_(atrs);
	}
	public static void To_html(Xoh_html_wtr wtr, Xop_ctx ctx, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		Xtn_syntaxHighlight_nde nde = (Xtn_syntaxHighlight_nde)xnde.Xnde_data();
		boolean lang_is_text = ByteAry_.Eq(nde.Lang(), Lang_text);
		boolean enclose_is_none = ByteAry_.Eq(nde.Enclose(), Enclose_none);
		if 		(enclose_is_none) {
			bfr.Add(Xoh_consts.Span_bgn);			
		}
		else if (lang_is_text) {
			bfr.Add(Xoh_consts.Code_bgn_open);
			if (nde.Style() != null) 
				bfr.Add(Xoh_consts.Style_atr).Add(nde.style).Add_byte(Byte_ascii.Quote);
			bfr.Add_byte(Byte_ascii.Gt);
		}
		else {
			bfr.Add(Xoh_consts.Pre_bgn_open);
			bfr.Add(Xoh_consts.Style_atr).Add(Xoh_consts.Pre_style_overflow_auto);
			if (nde.Style() != null) bfr.Add(nde.Style());
			bfr.Add(Xoh_consts.__end_quote);
		}
		int text_bgn = xnde.Tag_open_end();
		int text_end = xnde.Tag_close_bgn();
		Int_rng_mgr highlight_idxs = nde.Highlight_idxs();
		boolean line_enabled = nde.Line_enabled();
		if (line_enabled || enclose_is_none) {
			bfr.Add_byte_nl();
			byte[][] lines = ByteAry_.Split_lines(ByteAry_.Mid(src, text_bgn, text_end));
			int lines_len = lines.length;
			int line_idx = nde.Start();
			int line_end = (line_idx + lines_len) - 1; // EX: line_idx=9 line_len=1; line_end=9
			int digits_max = Int_.DigitCount(line_end);
			for (int i = 0; i < lines_len; i++) {
				byte[] line = lines[i]; if (i == 0 && ByteAry_.Len_eq_0(line)) continue;
				if (line_enabled) {
					bfr.Add(Xoh_consts.Span_bgn_open).Add(Xoh_consts.Style_atr).Add(Style_line).Add(Xoh_consts.__end_quote);
					int pad = digits_max - Int_.DigitCount(line_idx);
					if (pad > 0) bfr.Add_byte_repeat(Byte_ascii.Space, pad);
					bfr.Add_int_variable(line_idx++).Add_byte(Byte_ascii.Space);
					bfr.Add(Xoh_consts.Span_end);
				}
				bfr.Add(Xoh_consts.Span_bgn_open);
				if (highlight_idxs.Match(i))
					bfr.Add(Xoh_consts.Style_atr).Add(Style_highlight).Add(Xoh_consts.__end_quote);
				else
					bfr.Add(Xoh_consts.__end);
				Xoh_html_wtr.Bfr_escape(bfr, line, 0, line.length, ctx.App(), true, false);
				bfr.Add(Xoh_consts.Span_end);
				if (enclose_is_none)
					bfr.Add(Xoh_consts.Br);
				bfr.Add_byte_nl();
			}
		}
		else
			Xoh_html_wtr.Bfr_escape(bfr, src, text_bgn, text_end, ctx.App(), true, false);
		if 		(enclose_is_none)		bfr.Add(Xoh_consts.Span_end);
		else if (lang_is_text)			bfr.Add(Xoh_consts.Code_end);
		else							bfr.Add(Xoh_consts.Pre_end);
	}
	private static final byte[] Lang_text = ByteAry_.new_ascii_("text"), Style_line = ByteAry_.new_ascii_("-moz-user-select:none;"), Style_highlight = ByteAry_.new_ascii_("background-color: #FFFFCC;"), Enclose_none = ByteAry_.new_ascii_("none");
	public static final byte Xatr_enclose = 2, Xatr_lang = 3, Xatr_style = 4, Xatr_line = 5, Xatr_start = 6, Xatr_highlight = 7;
	private static final Hash_adp_bry xatrs_syntaxHighlight = new Hash_adp_bry(false).Add_str_byteVal("enclose", Xatr_enclose).Add_str_byteVal("lang", Xatr_lang).Add_str_byteVal("style", Xatr_style).Add_str_byteVal("line", Xatr_line).Add_str_byteVal("start", Xatr_start).Add_str_byteVal("highlight", Xatr_highlight);
}

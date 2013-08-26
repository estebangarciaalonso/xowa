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
package gplx.xowa.xtns.gallery; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xtn_gallery_nde implements Xop_xnde_xtn, Xop_xnde_atr_parser {
	public Xop_root_tkn Xtn_root() {return null;}
	public boolean Xtn_literal() {return false;}
	public int Itm_width() {return itm_width;} private int itm_width = 120;
	public int Itm_height() {return itm_height;} private int itm_height = 120;
	public int Itms_per_row() {return itms_per_row;} public Xtn_gallery_nde Itms_per_row_(int v) {itms_per_row = v; return this;} private int itms_per_row = -1;
	public int Itms_len() {return itms.Count();}
	public Xtn_gallery_itm_data Itms_get(int i) {return (Xtn_gallery_itm_data)itms.FetchAt(i);}
	public Xtn_gallery_nde Itms_add_ary(Xtn_gallery_itm_data... ary) {
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++)
			itms.Add(ary[i]);
		return this;
	}	ListAdp itms = ListAdp_.new_();
	public static final byte Xatr_id_perrow = 0, Xatr_id_widths = 1, Xatr_id_heights = 2;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {
		if (xatr_key_obj == null) return;
		ByteVal xatr_key = (ByteVal)xatr_key_obj;
		switch (xatr_key.Val()) {
			case Xatr_id_perrow:		itms_per_row = xatr.Val_as_int_or(src, 4); break;
			case Xatr_id_widths:		itm_width = xatr.Val_as_int_or(src, 120); break;
			case Xatr_id_heights:		itm_height = xatr.Val_as_int_or(src, 120); break;
		}
	}
	public void Xtn_compile(Xop_ctx ctx, Xow_wiki wiki, Xop_tkn_mkr tkn_mkr, byte[] src, Xop_xnde_tkn xnde) {
		if (itms_per_row == -1) itms_per_row = wiki.Cfg_gallery().Imgs_per_row();
		int content_bgn = xnde.Tag_open_end(), content_end = xnde.Tag_close_bgn();
		int ws_bgn_idx = -1, ws_end_idx = -1; boolean ws_bgn_chk = true;
		int lnki_bgn = -1, lnki_end = -1;
		Xtn_gallery_itm_data itm = null;
		int cur_pos = content_bgn;
		Xop_xatr_itm.Xatr_parse(wiki.App(), this, wiki.Lang().Xatrs_gallery(), wiki, src, xnde);

		int nl_0_pos = content_bgn, nl_1_pos = -1, pipe_pos = -1;
		while (true) {
			boolean cur_is_end = cur_pos == content_end;
			boolean nl_1_pos_set = nl_1_pos != -1;
			if (cur_is_end || nl_1_pos_set) {
				if (!ws_bgn_chk) {	// ws_bgn_chk == true if blank_line
					if (pipe_pos == -1) {	// no caption, just image;
						lnki_bgn = ws_bgn_idx == -1 ? nl_0_pos	: ws_bgn_idx;
						lnki_end = ws_end_idx == -1 ? nl_1_pos	: ws_end_idx;
						if (lnki_end == -1) lnki_end = cur_pos;	// lnki_end can be -1; EX: <gallery>File:A.png</gallery>
					}
					byte[] lnki_raw = ByteAry_.Mid(src, lnki_bgn, lnki_end);
					Xoa_ttl ttl = Xoa_ttl.new_(wiki, ctx.Msg_log(), lnki_raw, src, lnki_bgn, lnki_end);
					if (ttl != null) {	// NOTE: some ttls can be invalid; ignore
						itm = new Xtn_gallery_itm_data();
						this.Itms_add_ary(itm);
						itm.Lnki_(ttl);

						if (pipe_pos != -1) {
							int itm_end = nl_1_pos_set ? nl_1_pos : cur_pos;	// if eos, use cur_pos; else newLine; NOTE: must use nl_1_pos else cur_pos will include newLine
							int html_bgn = ws_bgn_idx == -1 ? pipe_pos + 1	: ws_bgn_idx;
							int html_end = ws_end_idx == -1 ? itm_end		: ws_end_idx;
							Xop_root_tkn html = wiki.Parser().Parse_recurse(ctx, ByteAry_.Mid(src, html_bgn, html_end), true);
							itm.Html_data_(html.Root_src()).Html_root_(html);	// NOTE: need to extract copy of html.Src() b/c <gallery> is xtn and node never gets parsed;
						}
					}
				}
				nl_0_pos = nl_1_pos + 1; // +1 to set after nl
				ws_bgn_idx = ws_end_idx = pipe_pos = nl_1_pos = -1; ws_bgn_chk = true;
				lnki_bgn = cur_pos;
			}
			if (cur_is_end) break;
			byte b = src[cur_pos];
			switch (b) {
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
					if	(ws_bgn_chk) ws_bgn_idx = cur_pos;							// definite ws at bgn; set ws_bgn_idx, and keep setting until text tkn reached; handles mixed sequence of \s\n\t where last tkn should be ws_bgn_idx
					else			{if (ws_end_idx == -1) ws_end_idx = cur_pos;};	// possible ws at end; may be overriden later; see AdjustWsForTxtTkn
					break;
				case Byte_ascii.Pipe:
					if (pipe_pos == -1) {
						lnki_bgn = ws_bgn_idx == -1 ? nl_0_pos	: ws_bgn_idx;
						lnki_end = ws_end_idx == -1 ? cur_pos	: ws_end_idx;
						pipe_pos = cur_pos;
						ws_bgn_chk = true; ws_bgn_idx = -1; ws_end_idx = -1;
					}
					break;
				case Byte_ascii.NewLine:
					nl_1_pos = cur_pos;
					break;
				default:
					if (ws_bgn_chk) ws_bgn_chk = false; else ws_end_idx = -1;		// INLINE: AdjustWsForTxtTkn
					break;
			}
			++cur_pos;
		}
	}
}

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
package gplx.xowa.html.tocs; import gplx.*; import gplx.xowa.*; import gplx.xowa.html.*;
public class Xop_toc_mgr implements ByteAryFmtrArg {
	public Xop_toc_mgr() {
		path_ary = new Xop_toc_itm[32];
		for (int i = 0; i < 32; i++)
			path_ary[i] = new Xop_toc_itm();
	}	private Xoa_page page; byte[] src; Xop_toc_itm[] path_ary; ByteAryBfr path_bfr = new ByteAryBfr(32);	// assume 6 max levels * 5 max heading (9999.); add 2 for good measure 		
	public void Clear() {
		for (int i = 0; i < 32; i++)
			path_ary[i].Lvl_idx_(0);
	}
	public void XferAry(ByteAryBfr trg, int idx) {
		int path_idx = 0, toc_idx = 0, lvl_idx = 1, eq_prv = 0;
		path_bfr.Clear();
		Xop_hdr_mgr hdr_mgr = page.Hdr_mgr();
		int hdrs_len = hdr_mgr.Len();
		for (int i = 0; i < hdrs_len; i++) {
			Xop_hdr_tkn hdr = hdr_mgr.Get_at(i);
			int eq_cur = hdr.Hdr_len();
			switch (CompareAble_.Compare(eq_cur, eq_prv)) {
				case CompareAble_.More:	// always increase slot
					if (eq_prv != 0) {	// add to path_bfr, unless 1st
						path_bfr.Add_int_variable(lvl_idx).Add_byte(Byte_ascii.Dot);		// build path; EX: 1.2.3
					}
					path_ary[path_idx].Lvl_idx_(lvl_idx).Eq_len_((byte)eq_cur);			// store cur value in path ary; EX: path_ary[1] = 2
					trg.Add_byte_repeat(Byte_ascii.Space, path_idx * 2).Add(Bry_list_bgn);	// <ul>
					++path_idx;
					lvl_idx = 1;
					eq_prv = eq_cur;
					break;
				case CompareAble_.Less:	// decrease slot, only if <= last slot
					int path_new = -1;
					for (int j = path_idx - 1; j > -1; j--) {
						if 		(eq_cur == path_ary[j].Eq_len()) {
							path_new = j;
							break;
						}
						else if (eq_cur > path_ary[j].Eq_len()) {
							path_new = j + 1;
							break;
						}
					}
					if (path_new == -1) {	// NOTE: no prior levels found; occurs when 1st hdr is 3 and 2nd is 2; assume 1st is actually 2; do not close level; DATE:20130516
						trg.Add_byte_repeat(Byte_ascii.Space, path_idx * 2).Add(Bry_item_end);	// add "</li>"
						++lvl_idx;
					}
					else {
						if (path_new == path_idx) {
							trg.Add_byte_repeat(Byte_ascii.Space, path_idx * 2).Add(Bry_item_end);	// add "</li>"
							++lvl_idx;
						}
						else {
							for (int j = path_idx; j > path_new + 1; j--) {
								trg.Add_byte_repeat(Byte_ascii.Space, path_idx * 2).Add(Bry_item_end);	// </li>
								--path_idx;
								if (path_idx == -1) path_idx = 0;
								trg.Add_byte_repeat(Byte_ascii.Space, path_idx * 2).Add(Bry_list_end);	// </ul>
								lvl_idx = path_ary[path_idx].Lvl_idx();
								path_bfr.Del_by(Int_.DigitCount(lvl_idx) + 1);	// + 1 for dot
							}
							++lvl_idx;
							trg.Add_byte_repeat(Byte_ascii.Space, path_idx * 2).Add(Bry_item_end);	// <li>
						}
					}
					eq_prv = eq_cur;
					break;
				case CompareAble_.Same:	// keep slot the same
					trg.Add_byte_repeat(Byte_ascii.Space, path_idx * 2).Add(Bry_item_end);	// add "</li>"
					++lvl_idx;
					break;
			}
//				byte[] text = Toc_text(page, src, hdr);
			byte[] text = hdr.Hdr_toc_text();// Toc_text(page, src, hdr);
			int lvl_idx_digits = Int_.DigitCount(lvl_idx);
			path_bfr.Add_int_fixed(lvl_idx, lvl_idx_digits);			// add current level
			trg.Add_byte_repeat(Byte_ascii.Space, (path_idx - 1) * 2);	// indent
			bfmtr_line.Bld_bfr_many(trg, path_idx, ++toc_idx, hdr.Hdr_html_id(), path_bfr, text);
			path_bfr.Del_by(lvl_idx_digits);
		}
		for (int i = path_idx - 1; i > -1; i--) {
			trg.Add_byte_repeat(Byte_ascii.Space, (i + 1) * 2);
			trg.Add(Bry_item_end);
			trg.Add_byte_repeat(Byte_ascii.Space, i * 2);
			trg.Add(Bry_list_end);
		}
	}
	public static byte[] Toc_text(Xop_ctx ctx, Xoa_page page, byte[] src, Xop_hdr_tkn hdr) {
		Xow_wiki wiki = page.Wiki();
		ByteAryBfr bfr = wiki.Utl_bry_bfr_mkr().Get_b128();
		Xoh_html_wtr html_wtr = wiki.Html_wtr(); html_wtr.Page_(wiki.Ctx().Page());
		Xoh_html_wtr_ctx html_wtr_opts = Xoh_html_wtr_ctx.Basic;
		Toc_text_recurse(ctx, page, bfr, src, html_wtr, html_wtr_opts, hdr, 0);
		bfr.Mkr_rls();
		return bfr.XtoAryAndClear();
	}
	private static void Toc_text_recurse(Xop_ctx ctx, Xoa_page page, ByteAryBfr bfr, byte[] src, Xoh_html_wtr html_wtr, Xoh_html_wtr_ctx html_wtr_opts, Xop_tkn_itm tkn, int depth) {
		int subs_len = tkn.Subs_len();
		boolean txt_seen = false; int ws_pending = 0;
		for (int i = 0; i < subs_len; i++) {
			Xop_tkn_itm sub = tkn.Subs_get(i);
			byte sub_tid = sub.Tkn_tid();
			if (sub_tid != Xop_tkn_itm_.Tid_space) {
				if (ws_pending > 0) {
					bfr.Add_byte_repeat(Byte_ascii.Space, ws_pending);
					ws_pending = 0;
				}
				txt_seen = true;
			}
			switch (sub.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_space:
					if (txt_seen) ws_pending = sub.Src_end() - sub.Src_bgn();
					break;
				case Xop_tkn_itm_.Tid_lnki:
					Xop_lnki_tkn lnki = (Xop_lnki_tkn)sub;
					if (lnki.Ns_id() == Xow_ns_.Id_category
						&& !lnki.Ttl().ForceLiteralLink())		{}	// Category text should not print; DATE:2013-12-09
					else {
						if (lnki.Caption_exists())
							Toc_text_recurse(ctx, page, bfr, src, html_wtr, html_wtr_opts, lnki.Caption_val_tkn(), depth);
						else
							bfr.Add(lnki.Ttl_ary());
					}
					break;
				case Xop_tkn_itm_.Tid_xnde:
					Xop_xnde_tkn xnde = (Xop_xnde_tkn)sub;
					switch (xnde.Tag().Id()) {
						case Xop_xnde_tag_.Tid_br:	// always ignore in TOC text; EX: en.wikipedia.org/wiki/Magnetic_resonance_imaging; ====''T''<span style="display:inline-block; margin-bottom:-0.3em; vertical-align:-0.4em; line-height:1.2em;  font-size:85%; text-align:left;">*<br />2</span>-weighted MRI====
						case Xop_xnde_tag_.Tid_hr:	// assumed, based on above
						case Xop_xnde_tag_.Tid_h1: case Xop_xnde_tag_.Tid_h2: case Xop_xnde_tag_.Tid_h3: case Xop_xnde_tag_.Tid_h4: case Xop_xnde_tag_.Tid_h5: case Xop_xnde_tag_.Tid_h6:
						case Xop_xnde_tag_.Tid_ul: case Xop_xnde_tag_.Tid_ol: case Xop_xnde_tag_.Tid_dd: case Xop_xnde_tag_.Tid_dt: case Xop_xnde_tag_.Tid_li:
						case Xop_xnde_tag_.Tid_table: case Xop_xnde_tag_.Tid_tr: case Xop_xnde_tag_.Tid_td: case Xop_xnde_tag_.Tid_th: case Xop_xnde_tag_.Tid_thead: case Xop_xnde_tag_.Tid_tbody: case Xop_xnde_tag_.Tid_caption:
						case Xop_xnde_tag_.Tid_ref:	// NOTE: don't bother printing references
//							case Xop_xnde_tag_.Tid_pre: case Xop_xnde_tag_.Tid_blockquote:
							break;
						case Xop_xnde_tag_.Tid_b:
						case Xop_xnde_tag_.Tid_i:
							html_wtr.Write_tkn(bfr, ctx, html_wtr_opts, src, tkn, Xoh_html_wtr.Sub_idx_null, sub);
							break;
						case Xop_xnde_tag_.Tid_translate:
							gplx.xowa.xtns.translates.Xop_translate_xnde translate_xnde = (gplx.xowa.xtns.translates.Xop_translate_xnde)xnde.Xnde_xtn();
							html_wtr.Write_tkn(bfr, ctx, html_wtr_opts, translate_xnde.Xtn_root().Data_mid(), tkn, Xoh_html_wtr.Sub_idx_null, translate_xnde.Xtn_root());
							break;
						default:
							if (depth > 0 && (xnde.CloseMode() == Xop_xnde_tkn.CloseMode_pair || xnde.CloseMode() == Xop_xnde_tkn.CloseMode_inline))	// do not render dangling xndes; EX: Casualties_of_the_Iraq_War; ===<small>Iraqi Health Ministry<small>===
								bfr.Add_mid(src, xnde.Tag_open_bgn(), xnde.Tag_open_end());
							Toc_text_recurse(ctx, page, bfr, src, html_wtr, html_wtr_opts, xnde, depth + 1);
							if (depth > 0 && xnde.CloseMode() == Xop_xnde_tkn.CloseMode_pair)	// do not render (a) dangling xndes or (b) inline (which will have negative bgn)
								bfr.Add_mid(src, xnde.Tag_close_bgn(), xnde.Tag_close_end());
							break;
					}
					break;
				default:
					html_wtr.Write_tkn(bfr, ctx, html_wtr_opts, src, tkn, Xoh_html_wtr.Sub_idx_null, sub);
					break;
			}
		}
	}
	public void Html(Xoa_page page, byte[] src, ByteAryBfr bfr) {
		if (!page.Hdr_mgr().Toc_enabled()) return;	// REF.MW: Parser.php|formatHeadings
		this.src = src; this.page = page;
		boolean tbl_para = page.Wiki().Html_mgr().Tbl_para();
		byte[] tbl_para_bgn = tbl_para ? Xoh_consts.P_bgn : ByteAry_.Empty;
		byte[] tbl_para_end = tbl_para ? Xoh_consts.P_end : ByteAry_.Empty;
		byte[] bry_contents = page.Wiki().Msg_mgr().Val_by_id(Xol_msg_itm_.Id_toc);
		bfmtr_main.Bld_bfr_many(bfr, ByteAryFmtrArg_.bry_(bry_contents), this, tbl_para_bgn, tbl_para_end);
		this.src = null;
	}
	private byte[]
	  Bry_list_bgn = ByteAry_.new_utf8_("  <ul>\n")
	, Bry_list_end = ByteAry_.new_utf8_("  </ul>\n")
	, Bry_item_end = ByteAry_.new_utf8_("  </li>\n")
	;
	private ByteAryFmtr
	bfmtr_main = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	( "~{tbl_para_bgn}<div id=\"toc\" class=\"toc\">"
	, "  <div id=\"toctitle\">"
	, "    <h2>~{contents_title}</h2>"
	, "  </div>"
	, "~{toc_list}</div>"
	, "~{tbl_para_end}"
	)
	, "contents_title", "toc_list", "tbl_para_bgn", "tbl_para_end"
	)
	, bfmtr_line = ByteAryFmtr.new_
	( "    <li class=\"toclevel-~{level} tocsection-~{toc_idx}\"><a href=\"#~{anchor}\"><span class=\"tocnumber\">~{heading}</span> <span class=\"toctext\">~{text}</span></a>\n"
	, "level", "toc_idx", "anchor", "heading", "text"
	);
}
class Xop_toc_itm {
	public int Eq_len() {return eq_len;} public Xop_toc_itm Eq_len_(int v) {eq_len = v; return this;} private int eq_len;
	public int Lvl_idx() {return lvl_idx;} public Xop_toc_itm Lvl_idx_(int v) {lvl_idx = v; return this;} private int lvl_idx;
}

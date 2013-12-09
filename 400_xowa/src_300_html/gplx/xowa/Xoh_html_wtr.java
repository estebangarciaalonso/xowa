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
package gplx.xowa; import gplx.*;
import gplx.xowa.wikis.*; import gplx.xowa.users.history.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.dynamicPageList.*; import gplx.xowa.xtns.math.*;
import gplx.xowa.parsers.lnkis.*;
public class Xoh_html_wtr {
	Xow_wiki wiki; Xoa_app app; Xop_ctx ctx; Xoa_page page; Xop_root_tkn root; 
	private gplx.xowa.xtns.refs.Xoh_ref_wtr ref_wtr = new gplx.xowa.xtns.refs.Xoh_ref_wtr();  Url_encoder href_encoder; ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255);
	private Xop_lnki_logger_redlinks_mgr redlinks_mgr;
	public Xoh_lnki_wtr Lnki_wtr() {return lnki_wtr;} private Xoh_lnki_wtr lnki_wtr;
	public Xoh_html_wtr(Xow_wiki wiki) {
		this.wiki = wiki; app = wiki.App(); this.wiki_key = wiki.Key_bry();
		lnki_wtr = new Xoh_lnki_wtr(wiki, this);
		history_mgr = app.User().History_mgr();
		this.href_encoder = wiki.App().Url_converter_href();
		redlinks_mgr = wiki.Ctx().Tab().Lnki_redlinks_mgr();
	}	private byte[] wiki_key; Xou_history_mgr history_mgr;
	public Xoh_ctx Hctx() {return hctx;} private Xoh_ctx hctx = new Xoh_ctx();
	public void Page_(Xoa_page v) {this.page = v;}
	public void Write_all(Xop_ctx ctx, Xop_root_tkn root, byte[] src, ByteAryBfr main_bfr) {
		try {
			tbl_para = wiki.Html_mgr().Tbl_para();
			this.ctx = ctx; this.root = root; indent_level = 0; this.page = ctx.Page();
			this.page.Langs().Clear();	// HACK: always clear langs; necessary for reload
			lnki_wtr.Page_bgn(page);
			Write_tkn(Xoh_opts.root_(), main_bfr, src, 0, null, -1, root);
		}
		finally {
			page.Category_list_(hctx.Lnki_ctg_xto_ary());
			this.ctx = null; this.root = null; this.page = null;
			hctx.Clear();
		}
	}	boolean tbl_para = false;
	public void Write_tkn(Xoh_opts opts, ByteAryBfr bfr, byte[] src, int depth, Xop_tkn_grp grp, int sub_idx, Xop_tkn_itm tkn) {
		if (tkn.Ignore()) return;
		switch (tkn.Tkn_tid()) {
			case Xop_tkn_itm_.Tid_arg_itm:
			case Xop_tkn_itm_.Tid_root:
				int subs_len = tkn.Subs_len();
				for (int i = 0; i < subs_len; i++)
					Write_tkn(opts, bfr, src, depth + 1, tkn, i, tkn.Subs_get(i));
				break;
			case Xop_tkn_itm_.Tid_ignore:	break;
			case Xop_tkn_itm_.Tid_html_ncr:	Html_ncr(opts, bfr, src, (Xop_html_ncr_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_html_ref:	Html_ref(opts, bfr, src, (Xop_html_ref_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_hr:		Hr(opts, bfr, src, (Xop_hr_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_hdr:		Hdr(opts, bfr, src, (Xop_hdr_tkn)tkn, depth); break;
			case Xop_tkn_itm_.Tid_apos:		Apos(opts, bfr, src, (Xop_apos_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_lnke:		Lnke(opts, bfr, src, (Xop_lnke_tkn)tkn, depth); break;
			case Xop_tkn_itm_.Tid_lnki:		Lnki(opts, bfr, src, (Xop_lnki_tkn)tkn, depth); break;
			case Xop_tkn_itm_.Tid_list:		List(opts, bfr, src, (Xop_list_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_xnde:		Xnde(opts, bfr, src, (Xop_xnde_tkn)tkn, depth); break;
			case Xop_tkn_itm_.Tid_under:	Under(opts, bfr, src, (Xop_under_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_tblw_tb:	Tblw(opts, bfr, src, (Xop_tblw_tkn)tkn, Tag_tblw_tb_bgn_atr, Tag_tblw_tb_end, depth, true); break;
			case Xop_tkn_itm_.Tid_tblw_tr:	Tblw(opts, bfr, src, (Xop_tblw_tkn)tkn, Tag_tblw_tr_bgn_atr, Tag_tblw_tr_end, depth, false); break;
			case Xop_tkn_itm_.Tid_tblw_td:	Tblw(opts, bfr, src, (Xop_tblw_tkn)tkn, Tag_tblw_td_bgn_atr, Tag_tblw_td_end, depth, false); break;
			case Xop_tkn_itm_.Tid_tblw_th:	Tblw(opts, bfr, src, (Xop_tblw_tkn)tkn, Tag_tblw_th_bgn_atr, Tag_tblw_th_end, depth, false); break;
			case Xop_tkn_itm_.Tid_tblw_tc:	Tblw(opts, bfr, src, (Xop_tblw_tkn)tkn, Tag_tblw_tc_bgn_atr, Tag_tblw_tc_end, depth, false); break;
			case Xop_tkn_itm_.Tid_para:		Para(opts, bfr, src, (Xop_para_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_space:	Space(opts, bfr, src, grp, sub_idx, (Xop_space_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_pre:		Pre(opts, bfr, src, (Xop_pre_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_newLine:	NewLine(opts, bfr, src, (Xop_nl_tkn)tkn); break;
			case Xop_tkn_itm_.Tid_bry:		Bry(opts, bfr, src, (Xop_bry_tkn)tkn); break;
//				case Xop_tkn_itm_.Tid_tab:		bfr.Add_byte_repeat(Byte_ascii.Tab, tkn.Src_end() - tkn.Src_bgn()); break;
			default:
				Bfr_escape(bfr, src, tkn.Src_bgn(), tkn.Src_end(), app, true, false);	// NOTE: always escape text including (a) lnki_alt text; and (b) any other text, especially failed xndes; DATE:2013-06-18
				break;
		}
	}
	@gplx.Virtual public void Html_ncr(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_html_ncr_tkn tkn)	{bfr.Add(tkn.Html_ncr_bry());}
	@gplx.Virtual public void Html_ref(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_html_ref_tkn tkn)	{tkn.HtmlRef_itm().Print_to_html(bfr);}
	private static final byte[] Bry_hdr_bgn = ByteAry_.new_ascii_("<span class='mw-headline' id='"), Bry_hdr_end = ByteAry_.new_ascii_("</span>");
	@gplx.Virtual public void Hr(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_hr_tkn tkn)				{bfr.Add(Tag_hr);}
	@gplx.Virtual public void Hdr(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_hdr_tkn hdr, int depth) {
//			page.Hdrs_id_bld(hdr, src);
		if (hdr.Hdr_html_first() && hctx.Toc_show() && page.TocPos_firstHdr()) {
			app.Toc_mgr().Html(ctx.Page(), src, bfr);
		}
		int hdr_len = hdr.Hdr_len();
		if (hdr_len > 0) {	// NOTE: need to check hdr_len b/c it could be dangling
			Para_assert_tag_starts_on_nl(bfr, hdr.Src_bgn()); 
			bfr.Add(Tag_hdr_bgn).Add_int(hdr_len, 1, 1);
			if (hctx.Toc_show()) {
				bfr.Add_byte(Tag__end);
				bfr.Add(Bry_hdr_bgn);
				bfr.Add(hdr.Hdr_html_id());
				bfr.Add_byte(Byte_ascii.Apos);
			}
			bfr.Add_byte(Tag__end);
		}	
		if (hdr.Hdr_bgn_manual() > 0) bfr.Add_byte_repeat(Byte_ascii.Eq, hdr.Hdr_bgn_manual());
		int subs_len = hdr.Subs_len();
		for (int i = 0; i < subs_len; i++)
			Write_tkn(opts, bfr, src, depth + 1, hdr, i, hdr.Subs_get(i));
		if (hdr_len > 0) {	// NOTE: need to check hdr_len b/c it could be dangling
			if (hdr.Hdr_end_manual() > 0) bfr.Add_byte_repeat(Byte_ascii.Eq, hdr.Hdr_end_manual());
			if (hctx.Toc_show()) {
				bfr.Add(Bry_hdr_end);
			}
			bfr.Add(Tag_hdr_end).Add_int(hdr_len, 1, 1).Add_byte(Tag__end).Add_byte_nl();// NOTE: do not need to check hdr_len b/c it is impossible for end to occur without bgn
		}
	}
	private void Apos(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_apos_tkn apos) {
		if (opts.Lnki_alt()) return;	// ignore apos if alt; EX: [[File:A.png|''A'']] should have alt of A; DATE:2013-10-25
		switch (apos.Apos_cmd()) {
			case Xop_apos_tkn_.Cmd_b_bgn:			bfr.Add(Xoh_consts.B_bgn); break;
			case Xop_apos_tkn_.Cmd_b_end:			bfr.Add(Xoh_consts.B_end); break;
			case Xop_apos_tkn_.Cmd_i_bgn:			bfr.Add(Xoh_consts.I_bgn); break;		
			case Xop_apos_tkn_.Cmd_i_end:			bfr.Add(Xoh_consts.I_end); break;
			case Xop_apos_tkn_.Cmd_bi_bgn:			bfr.Add(Xoh_consts.B_bgn).Add(Xoh_consts.I_bgn); break;		
			case Xop_apos_tkn_.Cmd_ib_end:			bfr.Add(Xoh_consts.I_end).Add(Xoh_consts.B_end); break;
			case Xop_apos_tkn_.Cmd_ib_bgn:			bfr.Add(Xoh_consts.I_bgn).Add(Xoh_consts.B_bgn); break;		
			case Xop_apos_tkn_.Cmd_bi_end:			bfr.Add(Xoh_consts.B_end).Add(Xoh_consts.I_end);; break;
			case Xop_apos_tkn_.Cmd_bi_end__b_bgn:	bfr.Add(Xoh_consts.B_end).Add(Xoh_consts.I_end).Add(Xoh_consts.B_bgn); break;
			case Xop_apos_tkn_.Cmd_ib_end__i_bgn:	bfr.Add(Xoh_consts.I_end).Add(Xoh_consts.B_end).Add(Xoh_consts.I_bgn); break;
			case Xop_apos_tkn_.Cmd_b_end__i_bgn:	bfr.Add(Xoh_consts.B_end).Add(Xoh_consts.I_bgn); break;		
			case Xop_apos_tkn_.Cmd_i_end__b_bgn:	bfr.Add(Xoh_consts.I_end).Add(Xoh_consts.B_bgn); break;
			case Xop_apos_tkn_.Cmd_nil:				break;
			default: throw Err_.unhandled(apos.Apos_cmd());
		}
	}
	private void Lnke(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_lnke_tkn lnke, int depth) {
		int lnke_bgn = lnke.Lnke_bgn(), lnke_end = lnke.Lnke_end();
		byte[] lnke_xwiki_wiki = lnke.Lnke_xwiki_wiki();
		boolean proto_is_xowa = lnke.Proto_tid() == Xow_cfg_lnke.Tid_xowa;
		if (!opts.Lnki_alt()) {
			if (lnke_xwiki_wiki == null) {
				if (lnke.Lnke_relative())		// relative; EX: //a.org
					bfr.Add(Xoh_consts.A_bgn).Add(app.Url_parser().Url_parser().Relative_url_protocol_bry()).Add_mid(src, lnke_bgn, lnke_end).Add(Xoh_consts.A_bgn_lnke_0);
				else {							// xowa or regular; EX: http://a.org
					if (proto_is_xowa) {
						bfr.Add(Xoh_consts.A_bgn).Add(Xop_lnke_wkr.Bry_xowa_protocol);
						ctx.App().Url_converter_gfs().Encode(bfr, src, lnke_bgn, lnke_end);
						bfr.Add(Xoh_consts.A_bgn_lnke_0_xowa);	
					}
					else						// regular; add href
						bfr.Add(Xoh_consts.A_bgn).Add_mid(src, lnke_bgn, lnke_end).Add(Xoh_consts.A_bgn_lnke_0);
				}
			}
			else {
				bfr.Add(Xoh_consts.A_bgn).Add(Xoh_href_parser.Href_site_bry).Add(lnke_xwiki_wiki).Add(Xoh_href_parser.Href_wiki_bry).Add(lnke.Lnke_xwiki_page());
				if (lnke.Lnke_xwiki_qargs() != null)
					Xoa_url_arg_hash.Concat_bfr(bfr, lnke.Lnke_xwiki_qargs());
				bfr.Add(Xoh_consts.__end_quote);
			}
		}
		int subs_len = lnke.Subs_len();
		if (subs_len == 0) {																				// no text; auto-number; EX: "[1]"
			if (lnke.Lnke_typ() == Xop_lnke_tkn.Lnke_typ_text)
				bfr.Add_mid(src, lnke.Lnke_bgn(), lnke.Lnke_end());
			else
				bfr.Add_byte(Byte_ascii.Brack_bgn).Add_int_variable(hctx.Lnke_autonumber_next()).Add_byte(Byte_ascii.Brack_end);
		}
		else {																							// text available
			for (int i = 0; i < subs_len; i++)
				Write_tkn(opts, bfr, src, depth + 1, lnke, i, lnke.Subs_get(i));
		}
		if (!opts.Lnki_alt()) {
			if (proto_is_xowa)	// add <img />
				bfr.Add(Xoh_consts.Img_bgn).Add(wiki.Html_mgr().Img_xowa_protocol()).Add(Xoh_consts.__inline_quote);
			bfr.Add(Xoh_consts.A_end);
		}
	}
	private void Lnki(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_lnki_tkn lnki, int depth) {
		Xow_xwiki_itm lang = lnki.Ttl().Wik_itm();
		if (lang != null && lang.Type_is_lang(wiki.Lang().Lang_id()) && !lnki.Ttl().ForceLiteralLink()) {
			page.Langs().Add(lnki.Ttl());
			return;
		}
		boolean literal_link = lnki.Ttl().ForceLiteralLink();	// NOTE: if literal link, then override ns behavior; for File, do not show image; for Ctg, do not display at bottom of page
		redlinks_mgr.Lnki_add(ctx, lnki);
		switch (lnki.NmsId()) {
			case Xow_ns_.Id_media:		lnki_wtr.Write_or_queue(page, opts, bfr, src, lnki, depth); return; // NOTE: literal ":" has no effect; EX.WP:Beethoven and [[:Media:De-Ludwig_van_Beethoven.ogg|listen]]
			case Xow_ns_.Id_file:		if (!literal_link) {lnki_wtr.Write_or_queue(page, opts, bfr, src, lnki, depth); return;} break;
			case Xow_ns_.Id_category:	if (!literal_link) {hctx.Lnki_ctg_add(lnki); return;} break;
		}
		Lnki_generic(opts, lnki, bfr, src, depth);
	}
	private void Lnki_generic(Xoh_opts opts, Xop_lnki_tkn lnki, ByteAryBfr bfr, byte[] src, int depth) {
		byte[] ttl_bry = lnki.Ttl_ary();
		Xoa_ttl lnki_ttl = lnki.Ttl();
		if (ByteAry_.Len_eq_0(ttl_bry)) ttl_bry = lnki_ttl.Full_txt_raw();		// NOTE: handles ttls like [[fr:]] and [[:fr;]] which have an empty Page_txt, but a valued Full_txt_raw
		if (ByteAry_.Eq(lnki_ttl.Full_txt(), page.Page_ttl().Full_txt())) {		// lnki is same as pagename; bold; SEE: Month widget on day pages will bold current day; EX.WP: January 1
			if (lnki_ttl.Anch_bgn() == -1 && ByteAry_.Eq(lnki_ttl.Wik_txt(), page.Page_ttl().Wik_txt())) {		// only bold if lnki is not pointing to anchor on same page; EX.WP: Comet; [[Comet#Physical characteristics|ion tail]]
				bfr.Add(Xoh_consts.B_bgn);
				Lnki_caption(opts, bfr, src, lnki, ttl_bry, true, depth);
				bfr.Add(Xoh_consts.B_end);
				return;
			}
		}
		if (opts.Lnki_alt()) {
			Lnki_caption(opts, bfr, src, lnki, ttl_bry, true, depth);
		}
		else {
			bfr.Add(Xoh_consts.A_bgn);
			app.Href_parser().Build_to_bfr(lnki_ttl, wiki, bfr);
			if (hctx.Lnki_id()) {
				int lnki_html_id = lnki.Html_id();
				if (lnki_html_id > 0)	// skipped lnkis will have 0; EX: anchors and interwiki
					bfr.Add(Xoh_consts.A_mid_id).Add_int_variable(lnki_html_id);
			}
			if (hctx.Lnki_title()) bfr.Add(Xoh_consts.A_bgn_lnki_0).Add(lnki_ttl.Page_txt());	// NOTE: use Page_txt to (a) replace underscores with spaces; (b) get title casing; EX:[[roman_empire]] -> Roman empire
			if (hctx.Lnki_visited() && history_mgr.Has(wiki_key, ttl_bry)) bfr.Add(A_xowa_visited);
			bfr.Add(Xoh_consts.__end_quote);
			if (lnki_ttl.Anch_bgn() != -1 && !lnki_ttl.Ns().Id_main()) {	// anchor exists and not main_ns; anchor must be manually added
				byte[] anch_txt = lnki_ttl.Anch_txt();
				byte anch_spr = (anch_txt.length > 0 && anch_txt[0] == Byte_ascii.Hash)	// 1st char is #; occurs when page_txt has trailing space; causes 1st letter of anch_txt to start at # instead of 1st letter
				? Byte_ascii.Space	// ASSUME: 1 space ("Help:A #b"); does not handle multiple spaces like ("Help:A   #b"); needs change to Xoa_ttl 
				: Byte_ascii.Hash;	// Anch_txt bgns at 1st letter, so add # for caption; 
				ttl_bry = ByteAry_.Add_w_dlm(anch_spr, ttl_bry, anch_txt);	// manually add anchor; else "Help:A#b" becomes "Help:A". note that lnki.Ttl_ary() uses .Full_txt (wiki + page but no anchor) to captialize 1st letter of page otherwise "Help:A#b" shows as "Help:A" (so Help:a -> Help:A); DATE:2013-06-21				
			}
			Lnki_caption(opts, bfr, src, lnki, ttl_bry, true, depth);
			bfr.Add(Xoh_consts.A_end);
		}
	}	static final byte[] A_xowa_visited = ByteAry_.new_ascii_("\" class=\"xowa-visited"); static final byte[] Bry_anchor = new byte[] {Byte_ascii.Hash};
	public static byte[] Ttl_to_title(byte[] ttl) {return ttl;}	// FUTURE: swap html chars?
	private void Lnki_caption(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_lnki_tkn lnki, byte[] href, boolean tail_enabled, int depth) {
		if	(lnki.Caption_exists()) {
			if (lnki.Caption_tkn_pipe_trick())	// "pipe trick"; [[A|]] is same as [[A|A]]; also, [[Help:A|]] -> [[Help:A|A]]
				bfr.Add(lnki.Ttl().Page_txt());
			else 
				Write_tkn(opts, bfr, src, depth + 1, lnki, Sub_idx_null, lnki.Caption_val_tkn());
		}
		else							// trg only; write trg as caption;
			bfr.Add(href);
		if (tail_enabled) {
			int tail_bgn = lnki.Tail_bgn();
			if (tail_bgn != -1) bfr.Add_mid(src, tail_bgn, lnki.Tail_end());
		}
	}
	public void List(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_list_tkn list) {
		if (opts.Lnki_alt()) {						// alt; add literally; EX: "*" for "\n*"; note that \n is added in NewLine()
			if (list.List_bgn() == Bool_.Y_byte) {	// bgn tag
				bfr.Add_byte(list.List_itmTyp());	// add literal byte
			}
			else {}									// end tag; ignore
		}
		else {
			byte list_itm_type = list.List_itmTyp();
			if (list.List_bgn() == Bool_.Y_byte) {
				if (list.List_sub_first()) List_grp_bgn(opts, bfr, src, list_itm_type);
				List_itm_bgn(opts, bfr, src, list_itm_type);
			}
			else {
				List_itm_end(opts, bfr, src, list_itm_type);
				if (list.List_sub_last() == Bool_.Y_byte) List_grp_end(opts, bfr, src, list_itm_type);
			}
		}
	}
	@gplx.Virtual public void List_grp_bgn(Xoh_opts opts, ByteAryBfr bfr, byte[] src, byte type) {
		byte[] tag = null;
		switch (type) {
			case Xop_list_tkn_.List_itmTyp_ol: tag = Tag_list_grp_ol_bgn; break;
			case Xop_list_tkn_.List_itmTyp_ul: tag = Tag_list_grp_ul_bgn; break;
			case Xop_list_tkn_.List_itmTyp_dd:
			case Xop_list_tkn_.List_itmTyp_dt: tag = Tag_list_grp_dl_bgn; break;
			default: throw Err_.unhandled(type);
		}
		if (bfr.Bry_len() > 0) bfr.Add_byte_nl();	// NOTE: do not add newLine if start 
		if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
		bfr.Add(tag);
		++indent_level;
	}
	@gplx.Virtual public void List_itm_bgn(Xoh_opts opts, ByteAryBfr bfr, byte[] src, byte type) {
		byte[] tag = null;
		switch (type) {
			case Xop_list_tkn_.List_itmTyp_ol:
			case Xop_list_tkn_.List_itmTyp_ul: tag = Tag_list_itm_li_bgn; break;
			case Xop_list_tkn_.List_itmTyp_dt: tag = Tag_list_itm_dt_bgn; break;
			case Xop_list_tkn_.List_itmTyp_dd: tag = Tag_list_itm_dd_bgn; break;
			default: throw Err_.unhandled(type);
		}
		bfr.Add_byte_nl();
		if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
		bfr.Add(tag);
		++indent_level;
	}
	@gplx.Virtual public void List_grp_end(Xoh_opts opts, ByteAryBfr bfr, byte[] src, byte type) {
		--indent_level;
		byte[] tag = null;
		switch (type) {
			case Xop_list_tkn_.List_itmTyp_ol: tag = Tag_list_grp_ol_end; break;
			case Xop_list_tkn_.List_itmTyp_ul: tag = Tag_list_grp_ul_end; break;
			case Xop_list_tkn_.List_itmTyp_dd:
			case Xop_list_tkn_.List_itmTyp_dt: tag = Tag_list_grp_dl_end; break;
			default: throw Err_.unhandled(type);
		}
		bfr.Add_byte_nl();
		if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
		bfr.Add(tag);
	}
	@gplx.Virtual public void List_itm_end(Xoh_opts opts, ByteAryBfr bfr, byte[] src, byte type) {
		--indent_level;
		byte[] tag = null;
		switch (type) {
			case Xop_list_tkn_.List_itmTyp_ol:
			case Xop_list_tkn_.List_itmTyp_ul: tag = Tag_list_itm_li_end; break;
			case Xop_list_tkn_.List_itmTyp_dt: tag = Tag_list_itm_dt_end; break;
			case Xop_list_tkn_.List_itmTyp_dd: tag = Tag_list_itm_dd_end; break;
			default: throw Err_.unhandled(type);
		}
		bfr.Add_byte_nl();
		if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
		bfr.Add(tag);
	}
	@gplx.Virtual public void NewLine(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_nl_tkn tkn) {
		if (opts.Lnki_alt())
			bfr.Add_byte_space();
		else {
			if (tkn.Nl_typeId() == Xop_nl_tkn.Tid_char) {
				if (tkn.Nl_ws() == Bool_.Y_byte)
					bfr.Add_byte(Byte_ascii.Space);
				else
					bfr.Add_byte(Byte_ascii.NewLine);
			}
		}
	}
	public void Space(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_tkn_grp grp, int sub_idx, Xop_space_tkn space) {
		bfr.Add_byte_repeat(Byte_ascii.Space, space.Src_end_grp(grp, sub_idx) - space.Src_bgn_grp(grp, sub_idx));	// NOTE: lnki.caption will convert \n to \s; see Xop_nl_lxr; EX.WP: Schwarzschild radius
	}
	@gplx.Virtual public void Para(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_para_tkn para) {
//			if (opts.Lnki_alt()) {
//				if (para.Para_bgn() != Xop_para_tkn.Para_typeId_none)	// always add space, uncless tid is none
//					bfr.Add_byte_space();
//			}
//			else {
			switch (para.Para_end()) {
				case Xop_para_tkn.Para_typeId_none:		break;
				case Xop_para_tkn.Para_typeId_para:		bfr.Add(Tag_para_end).Add_byte_nl(); break;
				case Xop_para_tkn.Para_typeId_pre:		bfr.Add(Tag_pre_end).Add_byte_nl(); break;
				case Xop_para_tkn.Para_typeId_space:	bfr.Add_byte(Byte_ascii.Space); break;
				default:								throw Err_.unhandled(para.Para_end());
			}
			switch (para.Para_bgn()) {
				case Xop_para_tkn.Para_typeId_none:		break;
				case Xop_para_tkn.Para_typeId_para:		Para_assert_tag_starts_on_nl(bfr, para.Src_bgn()); bfr.Add(Tag_para_bgn); break;
				case Xop_para_tkn.Para_typeId_pre:		Para_assert_tag_starts_on_nl(bfr, para.Src_bgn()); bfr.Add(Tag_pre_bgn); break;
				case Xop_para_tkn.Para_typeId_space:	bfr.Add_byte(Byte_ascii.Space); break;
				default:								throw Err_.unhandled(para.Para_bgn());
			}
//			}
	}
	private void Para_assert_tag_starts_on_nl(ByteAryBfr bfr, int src_bgn) {
		if (!bfr.Match_end_byt_nl_or_bos()) bfr.Add_byte_nl();
		if (src_bgn != 0) bfr.Add_byte_nl();
	}
	@gplx.Virtual public void Pre(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_pre_tkn pre) {
		switch (pre.Pre_typeId()) {
			case Xop_pre_tkn.Pre_typeId_bgn:
				if (pre.Pre_enable() == Bool_.N_byte)
					bfr.Add_byte(Byte_ascii.Space);
				else
					bfr.Add(Tag_pre_bgn);
				break;
			case Xop_pre_tkn.Pre_typeId_end:
				bfr.Add_byte_nl().Add(Tag_pre_end).Add_byte_nl().Add_byte_nl();
				break;
		}
	}
	@gplx.Virtual public void Bry(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_bry_tkn bry) {
		bfr.Add(bry.Bry());
	}
	@gplx.Virtual public void Under(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_under_tkn under) {
		if (opts.Lnki_alt()) return;
		switch (under.Under_typeId()) {
			case Xol_kwd_grp_.Id_toc:
				app.Toc_mgr().Html(page, src, bfr);
				break;
			case Xol_kwd_grp_.Id_notoc:	case Xol_kwd_grp_.Id_forcetoc:	// NOTE: skip output; changes flag on page only
				break;
		}
	}
	@gplx.Virtual public void Xnde(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		if (opts.Lnki_alt()) {
			if (xnde.Tag_close_bgn() > 0) // NOTE: some tags are not closed; WP.EX: France; <p>
				Bfr_escape(bfr, src, xnde.Tag_open_end(), xnde.Tag_close_bgn(), app, true, false);
			else
				Xnde_subs(opts, bfr, src, xnde, depth + 1);
			return;
		}
		Xop_xnde_tag tag = xnde.Tag();
		int tag_id = tag.Id();
		switch (tag_id) {
			case Xop_xnde_tag_.Tid_br:
				if (xnde.Src_end() - xnde.Src_bgn() < 4
					|| xnde.Src_bgn() == -1) 
					bfr.Add(Tag_br); else bfr.Add_mid(src, xnde.Src_bgn(), xnde.Src_end()); break;
			case Xop_xnde_tag_.Tid_hr: bfr.Add(Tag_hr); break;
			case Xop_xnde_tag_.Tid_includeonly:	// NOTE: do not write tags or content
				break;
			case Xop_xnde_tag_.Tid_noinclude:		// NOTE: do not write tags
			case Xop_xnde_tag_.Tid_onlyinclude:	
				Xnde_subs_escape(opts, bfr, src, xnde, depth + 1, false, false);
				break;
			case Xop_xnde_tag_.Tid_nowiki:
				Xnde_subs_escape(opts, bfr, src, xnde, depth + 1, false, false);
				break;
			case Xop_xnde_tag_.Tid_b: case Xop_xnde_tag_.Tid_strong:
			case Xop_xnde_tag_.Tid_i: case Xop_xnde_tag_.Tid_em: case Xop_xnde_tag_.Tid_cite: case Xop_xnde_tag_.Tid_dfn: case Xop_xnde_tag_.Tid_var:
			case Xop_xnde_tag_.Tid_u: case Xop_xnde_tag_.Tid_ins: case Xop_xnde_tag_.Tid_abbr:
			case Xop_xnde_tag_.Tid_strike: case Xop_xnde_tag_.Tid_s: case Xop_xnde_tag_.Tid_del:
			case Xop_xnde_tag_.Tid_sub: case Xop_xnde_tag_.Tid_sup: case Xop_xnde_tag_.Tid_big: case Xop_xnde_tag_.Tid_small:
			case Xop_xnde_tag_.Tid_code: case Xop_xnde_tag_.Tid_tt: case Xop_xnde_tag_.Tid_kbd: case Xop_xnde_tag_.Tid_samp: case Xop_xnde_tag_.Tid_blockquote:
			case Xop_xnde_tag_.Tid_font: case Xop_xnde_tag_.Tid_center:
			case Xop_xnde_tag_.Tid_p: case Xop_xnde_tag_.Tid_span: case Xop_xnde_tag_.Tid_div:
			case Xop_xnde_tag_.Tid_h1: case Xop_xnde_tag_.Tid_h2: case Xop_xnde_tag_.Tid_h3: case Xop_xnde_tag_.Tid_h4: case Xop_xnde_tag_.Tid_h5: case Xop_xnde_tag_.Tid_h6:
			case Xop_xnde_tag_.Tid_dt: case Xop_xnde_tag_.Tid_dd: case Xop_xnde_tag_.Tid_ol: case Xop_xnde_tag_.Tid_ul: case Xop_xnde_tag_.Tid_dl:
			case Xop_xnde_tag_.Tid_table: case Xop_xnde_tag_.Tid_tr: case Xop_xnde_tag_.Tid_td: case Xop_xnde_tag_.Tid_th: case Xop_xnde_tag_.Tid_caption: case Xop_xnde_tag_.Tid_tbody:
			case Xop_xnde_tag_.Tid_time: case Xop_xnde_tag_.Tid_bdi:
			case Xop_xnde_tag_.Tid_ruby: case Xop_xnde_tag_.Tid_rt: case Xop_xnde_tag_.Tid_rb: case Xop_xnde_tag_.Tid_rp:  {
//					byte[] name = tag.Name_bry();
//					bfr.Add_byte(Tag__bgn).Add(name);
//					if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, opts, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
//					bfr.Add_byte(Tag__end);
//					Xnde_subs(opts, bfr, src, xnde, depth);					// NOTE: do not escape; <p>, <table> etc may have nested nodes
//					bfr.Add(Tag__end_bgn).Add(name).Add_byte(Tag__end);		// NOTE: inline is never written as <b/>; will be written as <b></b>; SEE: NOTE_1
				Write_xnde(bfr, opts, xnde, tag, tag_id, src, depth);
				break;
			}
			case Xop_xnde_tag_.Tid_pre: {
				byte[] name = tag.Name_bry();
				bfr.Add_byte(Tag__bgn).Add(name);
				if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, opts, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
				bfr.Add_byte(Tag__end);
				Xnde_subs_escape(opts, bfr, src, xnde, depth, false, true);	// NOTE: this is the only difference from above block
				bfr.Add(Tag__end_bgn).Add(name).Add_byte(Tag__end);
				break;
			}
			case Xop_xnde_tag_.Tid_source: {							// convert <source> and <syntaxhighlight> to <pre>;
				byte[] name = Xop_xnde_tag_.Tag_pre.Name_bry();
				bfr.Add_byte(Tag__bgn).Add(name);
				if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, opts, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
				bfr.Add_byte(Tag__end);
				Xnde_subs_escape(opts, bfr, src, xnde, depth, false, false);
				bfr.Add(Tag__end_bgn).Add(name).Add_byte(Tag__end);
				break;
			}
			case Xop_xnde_tag_.Tid_li: {
				byte[] name = tag.Name_bry();
				int bfr_len = bfr.Bry_len();
				if (bfr_len > 0 && bfr.Bry()[bfr_len - 1] != Byte_ascii.NewLine) bfr.Add_byte_nl();	// NOTE: always add nl before li else some lists will merge and force long horizontal bar; EX:w:Music
				if (xnde.Tag_visible()) {
					bfr.Add_byte(Tag__bgn).Add(name);
					if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, opts, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
					bfr.Add_byte(Tag__end);
				}
				Xnde_subs(opts, bfr, src, xnde, depth);
				if (xnde.Tag_visible())
					bfr.Add(Tag__end_bgn).Add(name).Add_byte(Tag__end);	// NOTE: inline is never written as <b/>; will be written as <b></b>; SEE: NOTE_1
				break;
			}
			case Xop_xnde_tag_.Tid_math:
				app.File_mgr().Math_mgr().Html_wtr().Write(this, ctx, opts, bfr, src, xnde, depth);
				break;
			case Xop_xnde_tag_.Tid_syntaxHighlight:			gplx.xowa.xtns.syntaxHighlight.Xtn_syntaxHighlight_nde.To_html(this, ctx, opts, bfr, src, xnde, depth); break;
			case Xop_xnde_tag_.Tid_score:					gplx.xowa.xtns.scores.Xtn_score.To_html(this, ctx, opts, bfr, src, xnde, depth); break;
			case Xop_xnde_tag_.Tid_dynamicPageList:			Xnde_dynamic_page_list(opts, bfr, src, xnde, depth); break;
			case Xop_xnde_tag_.Tid_gallery:					wiki.Html_mgr().Gallery_mgr().Write_html(app, wiki, ctx, page, this, opts, bfr, src, xnde, depth); break;
			case Xop_xnde_tag_.Tid_poem:					Xnde_poem(opts, bfr, src, xnde, depth); break;
			case Xop_xnde_tag_.Tid_ref:						ref_wtr.Xnde_ref(opts, bfr, src, xnde); break;
			case Xop_xnde_tag_.Tid_references:				ref_wtr.Xnde_references(this, ctx, opts, bfr, src, xnde, depth); break;
			case Xop_xnde_tag_.Tid_translate:				gplx.xowa.xtns.translates.Xop_translate_xnde.To_html(this, ctx, opts, bfr, src, xnde, depth); break;
			case Xop_xnde_tag_.Tid_languages:				gplx.xowa.xtns.translates.Xop_languages_xnde.To_html(this, ctx, opts, bfr, src, xnde, depth); break;
			case Xop_xnde_tag_.Tid_templateData:			gplx.xowa.xtns.templateData.Xtn_templateData_nde.To_html(this, ctx, opts, bfr, src, xnde, depth); break;
			case Xop_xnde_tag_.Tid_timeline: {
				bfr.Add_str("<pre class='xowa-timeline'>");
				bfr.Add_mid(src, xnde.Src_bgn(), xnde.Src_end());
				bfr.Add_str("</pre>");
				break;
			}
			case Xop_xnde_tag_.Tid_inputBox:
			case Xop_xnde_tag_.Tid_imageMap:
			case Xop_xnde_tag_.Tid_pages:
			case Xop_xnde_tag_.Tid_pagequality:
			case Xop_xnde_tag_.Tid_pagelist:
			case Xop_xnde_tag_.Tid_section:
			case Xop_xnde_tag_.Tid_xowa_cmd:				Xnde_xtn(opts, bfr, src, xnde, depth); break;
			default:
				if (tag.Restricted()) {	// a; img; script; etc..
					if (page.Allow_all_html() || page.Wiki().Wiki_tid() == Xow_wiki_type_.Tid_home) {
						bfr.Add_mid(src, xnde.Src_bgn(), xnde.Src_end());
						return;
					}
//							else {
//								Bfr_escape(bfr, src, xnde.Src_bgn(), xnde.Src_end(), app, true, true);
//							}
				}
				bfr.Add(Ary_escape_bgn).Add(tag.Name_bry());
				if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, opts, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
				switch (xnde.CloseMode()) {
					case Xop_xnde_tkn.CloseMode_inline:
						bfr.Add_byte(Byte_ascii.Slash).Add(Ary_escape_end);
						break;
					default:	// NOTE: close tag, even if dangling; EX: <center>a -> <center>a</center>
						bfr.Add_byte(Byte_ascii.Gt);
						Xnde_subs(opts, bfr, src, xnde, depth);
						bfr.Add(Ary_escape_bgn).Add_byte(Byte_ascii.Slash).Add(tag.Name_bry()).Add(Ary_escape_end);
						break;
				}
				break;
		}
	}
	private void Write_xnde(ByteAryBfr bfr, Xoh_opts opts, Xop_xnde_tkn xnde, Xop_xnde_tag tag, int tag_id, byte[] src, int depth) {
		byte[] name = tag.Name_bry();
		boolean at_bgn = true;
		ByteAryBfr ws_bfr = wiki.Utl_bry_bfr_mkr().Get_b512();					// create separate ws_bfr to handle "a<b> c </b>d" -> "a <b>c</b> d"
		int subs_len = xnde.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Xop_tkn_itm sub = xnde.Subs_get(i);
			byte tkn_tid = sub.Tkn_tid();
			switch (tkn_tid) {
				case Xop_tkn_itm_.Tid_space:									// space; add to ws_bfr;
					ws_bfr.Add_mid(src, sub.Src_bgn(), sub.Src_end());
					break;
				default:
					if (tkn_tid == Xop_tkn_itm_.Tid_html_ncr) {					// html_entity &#32; needed for fr.wikipedia.org and many spans with <span>&#32;</span>; DATE:2013-06-18
						Xop_html_ncr_tkn ncr_tkn = (Xop_html_ncr_tkn)sub;
						if (ncr_tkn.Html_ncr_val() == Byte_ascii.Space) {
							ws_bfr.Add_mid(src, ncr_tkn.Src_bgn(), ncr_tkn.Src_end());
							continue;											// just add entity; don't process rest;
						}
					}
					if (ws_bfr.Bry_len() > 0) bfr.Add_bfr_and_clear(ws_bfr);	// dump ws_bfr to real bfr
					if (at_bgn) {												// 1st non-ws tkn; add open tag; <b>
						at_bgn = false;
						bfr.Add_byte(Tag__bgn).Add(name);
						if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, opts, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
						bfr.Add_byte(Tag__end);
					}
					Write_tkn(opts, bfr, src, depth + 1, xnde, i, sub);			// NOTE: never escape; <p>, <table>, <center> etc may have nested nodes
					break;
			}
		}
		if (at_bgn) {	// occurs when xnde is empty; EX: <b></b>
			bfr.Add_byte(Tag__bgn).Add(name);
			if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(tag_id, opts, src, xnde.Atrs_bgn(), xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
			bfr.Add_byte(Tag__end);
		}
		bfr.Add(Tag__end_bgn).Add(name).Add_byte(Tag__end);						// NOTE: inline is never written as <b/>; will be written as <b></b>; SEE: NOTE_1
		if (ws_bfr.Bry_len() > 0) bfr.Add_bfr_and_clear(ws_bfr);				// dump any leftover ws to bfr; handles "<b>c </b>" -> "<b>c</b> "
		ws_bfr.Mkr_rls();
	}
	private static Xop_xatr_whitelist_mgr whitelist_mgr = new Xop_xatr_whitelist_mgr().Ini();
	public void Xnde_atrs(int tag_id, Xoh_opts opts, byte[] src, int bgn, int end, Xop_xatr_itm[] ary, ByteAryBfr bfr) {
		if (ary == null) return;	// NOTE: some nodes will have null xatrs b/c of whitelist; EX: <pre style="overflow:auto">a</pre>; style is not on whitelist so not xatr generated, but xatr_bgn will != -1
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			Xop_xatr_itm atr = ary[i];
			if (!whitelist_mgr.Chk(tag_id, src, atr)) continue;
			if (atr.Invalid()) continue;
			bfr.Add_byte(Byte_ascii.Space);	// add space before every attribute
			Xnde_atr_write(bfr, src, atr);
		}
	}
	private void Xnde_atr_write(ByteAryBfr bfr, byte[] src, Xop_xatr_itm atr) {
		int key_bgn = atr.Key_bgn();
		if (atr.Key_bry() != null) {
			bfr.Add(atr.Key_bry());
			bfr.Add_byte(Byte_ascii.Eq);
		}
		else if (key_bgn != -1) {
			bfr.Add_mid(src, atr.Key_bgn(), atr.Key_end());
			bfr.Add_byte(Byte_ascii.Eq);
		}
		byte quote_byte = atr.Quote_byte(); if (quote_byte == Byte_ascii.Nil) quote_byte = Byte_ascii.Quote;
		bfr.Add_byte(quote_byte);
		if (atr.Key_tid() == Xop_xatr_itm.Key_tid_id) {	// ids should not have spaces; DATE:2013-04-01
			if (atr.Val_bry() == null)
				Xnde_atr_write_id(bfr, src, atr.Val_bgn(), atr.Val_end());
			else {
				byte[] atr_val = atr.Val_bry();
				Xnde_atr_write_id(bfr, atr_val, 0, atr_val.length);
			}
		}
		else {
			if (atr.Val_bry() == null)
				bfr.Add_mid(src, atr.Val_bgn(), atr.Val_end());
			else
				bfr.Add(atr.Val_bry());
		}
		bfr.Add_byte(quote_byte);
	}
	private void Xnde_atr_write_id(ByteAryBfr bfr, byte[] bry, int bgn, int end) {
		app.Url_converter_id().Encode(bfr, bry, bgn, end);
	}
	private static IntRef bfr_escape_ncr = IntRef.zero_(); static BoolRef bfr_escape_fail = BoolRef.n_();
	private static int Bfr_escape_nowiki_skip(Xoa_app app, ByteAryBfr bfr, byte[] src, int bgn, int end, byte[] nowiki_name, int nowiki_name_len) {
		try {
			boolean tag_is_bgn = true;
			int bgn_gt = -1, end_lt = -1, end_gt = -1;
			for (int i = bgn + nowiki_name_len; i < end; i++) {
				byte b = src[i];
				switch (b) {
					case Byte_ascii.Gt:
						if	(tag_is_bgn)	{bgn_gt = i; tag_is_bgn = false;}
						else				return ByteAry_.NotFound;								// <nowiki>> found
						break;
					case Byte_ascii.Lt:
						if (	tag_is_bgn															// <nowiki < found
							||	(i + nowiki_name_len + 2 > end) 									// not enough chars for "/nowiki>"
							||	src[i + 1] != Byte_ascii.Slash 										// / 
							||	!ByteAry_.Eq(nowiki_name, src, i + 2, i + 2 + nowiki_name_len)		//  nowiki
							||	src[i + 2 + nowiki_name_len] != Byte_ascii.Gt						//        >
							)	return ByteAry_.NotFound;
						end_lt = i;
						end_gt = i + 2 + nowiki_name_len;
						i = end;
						break;
				}
			}
			if (end_gt == -1) return ByteAry_.NotFound;	// ">" of </nowiki> not found
			bfr.Add_mid(src, bgn_gt + 1, end_lt);
			return end_gt;
		}
		catch (Exception e) {
			app.Usr_dlg().Warn_many(GRP_KEY, "escape.nowiki.fail", "unknown error in escape.nowiki: ~{0} ~{1}", Err_.Message_gplx_brief(e), String_.new_utf8_(src, bgn, end));
			return ByteAry_.NotFound;
		}
	}	private static final String GRP_KEY = "xowa.wiki.html_wtr";
	public static byte[] Bfr_escape(Xoa_app app, ByteAryBfr tmp_bfr, byte[] src) {
		Bfr_escape(tmp_bfr, src, 0, src.length, app, true, false);
		return tmp_bfr.XtoAryAndClear();
	}
	public static void Bfr_escape(ByteAryBfr bfr, byte[] src, int bgn, int end, Xoa_app app, boolean interpret_amp, boolean nowiki_skip) {
		ByteTrieMgr_slim amp_trie = app.AmpTrie();
		bfr_escape_fail.Val_n_();
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Lt:
					if (nowiki_skip) {
						byte[] nowiki_name = Xop_xnde_tag_.Tag_nowiki.Name_bry();
						int nowiki_name_len = nowiki_name.length;
						if (ByteAry_.Eq(nowiki_name, src, i + 1, i + 1 + nowiki_name_len)) {	// <nowiki found;
							int end_gt = Bfr_escape_nowiki_skip(app, bfr, src, i, end, nowiki_name, nowiki_name_len);
							if (end_gt != ByteAry_.NotFound) {
								i = end_gt;
								continue;
							}
						}
					}
					bfr.Add(Xop_xnde_wkr.Bry_escape_lt);
					break;
				case Byte_ascii.Gt:
					bfr.Add(Xop_xnde_wkr.Bry_escape_gt);
					break;
				case Byte_ascii.Amp:
					if (interpret_amp) {
						Object o = amp_trie.MatchAtCur(src, i + 1, end);	// is this a valid &....
						if (o == null)										// nope; invalid; EX: "a&b"; "&bad;"; "&#letters;"; 
							bfr.Add(Xop_xnde_wkr.Bry_escape_amp);			// escape & and continue
						else {												// is either (1) a name or (2) an ncr (hex/dec)
							Xop_amp_trie_itm itm = (Xop_amp_trie_itm)o;
							int match_pos = amp_trie.Match_pos();
							int itm_tid = itm.Tid();
							if (itm_tid == Xop_amp_trie_itm.Tid_name) {		// name
								bfr.Add_mid(src, i, match_pos);				// embed entire name
								i = match_pos - 1;
							}
							else {											// ncr: dec/hex
								int end_pos = Xop_amp_wkr.CalcNcr(app.Msg_log(), itm_tid == Xop_amp_trie_itm.Tid_num_hex, src, end, bgn, match_pos, bfr_escape_ncr, bfr_escape_fail); // parse hex/dec
								if (bfr_escape_fail.Val())					// parse failed; escape and continue
									bfr.Add(Xop_xnde_wkr.Bry_escape_amp);
								else {										// parse worked; embed entire ncr
									bfr.Add_mid(src, i, end_pos);
									i = end_pos - 1;
								}
							}
						}
					}
					else
						bfr.Add(Xop_xnde_wkr.Bry_escape_amp);
					break;
				case Byte_ascii.Quote:
					bfr.Add(Xop_xnde_wkr.Bry_escape_quote);
					break;
				default:
					bfr.Add_byte(b);
					break;
			}
		}
	}
	private void Xnde_subs(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		int subs_len = xnde.Subs_len();
		for (int i = 0; i < subs_len; i++)
			Write_tkn(opts, bfr, src, depth + 1, xnde, i, xnde.Subs_get(i));
	}
	private void Xnde_subs_escape(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth, boolean amp_enable, boolean nowiki) {
		int xndesubs_len = xnde.Subs_len();
		for (int i = 0; i < xndesubs_len; i++) {
			Xop_tkn_itm sub = xnde.Subs_get(i);
			switch (sub.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_xnde:
					Xop_xnde_tkn sub_xnde = (Xop_xnde_tkn)sub;
					switch (sub_xnde.Tag().Id()) {
						case Xop_xnde_tag_.Tid_noinclude:
						case Xop_xnde_tag_.Tid_onlyinclude:
						case Xop_xnde_tag_.Tid_includeonly:
							break;
						default:
							byte[] tag_name = sub_xnde.Tag().Name_bry();
							bfr.Add(Xop_xnde_wkr.Bry_escape_lt).Add(tag_name);
							if (xnde.Atrs_bgn() > Xop_tblw_wkr.Atrs_ignore_check) Xnde_atrs(sub_xnde.Tag().Id(), opts, src, sub_xnde.Atrs_bgn(), sub_xnde.Atrs_end(), xnde.Atrs_ary(), bfr);
							bfr.Add(Xop_xnde_wkr.Bry_escape_gt);
							break;
					}
					Xnde_subs_escape(opts, bfr, src, sub_xnde, depth, amp_enable, false);
					break;
				case Xop_tkn_itm_.Tid_txt:
					if (amp_enable)
						bfr.Add_mid(src, sub.Src_bgn(), sub.Src_end());
					else
						Bfr_escape(bfr, src, sub.Src_bgn(), sub.Src_end(), app, true, nowiki);
					break;
				default:
					Write_tkn(opts, bfr, src, depth + 1, xnde, i, sub);
					break;
			}
		}
	}
	public BoolRef Queue_add_ref() {return queue_add_ref;} BoolRef queue_add_ref = BoolRef.n_();
	private void Xnde_dynamic_page_list(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {		
		Dpl_xnde nde = (Dpl_xnde)xnde.Xnde_data();
		nde.Xtn_html(this, opts, bfr, src, xnde, depth);
	}
	public void Xnde_poem(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		gplx.xowa.xtns.poems.Xtn_poem_nde mgr = (gplx.xowa.xtns.poems.Xtn_poem_nde)xnde.Xnde_data();
		if (mgr.Xtn_root() == null) return;	// NOTE: only happens with <poem/>
		bfr.Add(Bry_poem_bgn);
		Write_tkn(opts, bfr, mgr.Xtn_root().Root_src(), depth + 1, xnde, Xoh_html_wtr.Sub_idx_null, mgr.Xtn_root());
		bfr.Add(Bry_poem_end);
	}	static byte[] Bry_poem_bgn = ByteAry_.new_ascii_("<div class=\"poem\">\n"), Bry_poem_end = ByteAry_.new_ascii_("\n</div>"); 
	public void Tblw(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_tblw_tkn tkn, byte[] bgn, byte[] end, int depth, boolean tblw_bgn) {
		if (opts.Lnki_alt())			// add \s for each \n
			bfr.Add_byte_space();
		else {
			if (bfr.Bry_len() != 0 && !bfr.Match_end_byt(Byte_ascii.NewLine)) bfr.Add_byte_nl();
			if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
//			boolean para_mode = tblw_bgn && tbl_para && depth == 1;	// DELETE: old code; adding <p> to handle strange mozilla key down behavior on linux; DATE:2013-03-30
//			if (para_mode) {bfr.Add(Xoh_consts.P_bgn);}

			bfr.Add(bgn);
			int atrs_bgn = tkn.Atrs_bgn();
			if (atrs_bgn != -1) Xnde_atrs(tkn.Tag_id(), opts, src, atrs_bgn, tkn.Atrs_end(), tkn.Atrs_ary(), bfr); //bfr.Add_byte(Byte_ascii.Space).Add_mid(src, atrs_bgn, tkn.Atrs_end());
			bfr.Add_byte(Tag__end);
			++indent_level;
		}
		int subs_len = tkn.Subs_len();
		for (int i = 0; i < subs_len; i++)
			Write_tkn(opts, bfr, src, depth + 1, tkn, i, tkn.Subs_get(i));
		if (opts.Lnki_alt()) {
			if (tblw_bgn)			// only add \s for closing table; |} -> "\s"
				bfr.Add_byte_space();
		}
		else {
			--indent_level;
			if (bfr.Bry_len() != 0 && !bfr.Match_end_byt(Byte_ascii.NewLine)) bfr.Add_byte_nl();
			if (indent_level > 0) bfr.Add_byte_repeat(Byte_ascii.Space, indent_level * 2);
			bfr.Add(end);
//			if (para_mode) {bfr.Add(Xoh_consts.P_end);}				// DELETE: old code; adding <p> to handle strange mozilla key down behavior on linux; DATE:2013-03-30
			bfr.Add_byte_nl();
		}
	}
	private void Xnde_xtn(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		Xop_xnde_xtn xtn = xnde.Xnde_data(); if (xtn == null) return;
		if (xtn.Xtn_literal()) {
			Bfr_escape(bfr, src, xnde.Src_bgn(), xnde.Src_end(), app, true, false);
			return;
		}
		Xop_root_tkn root = xtn.Xtn_root(); if (root == null) return;	// NOTE: some roots will be null; EX: <section/> which has no data to parse/show
		Write_tkn(opts, bfr, root.Root_src(), depth + 1, xnde, Xoh_html_wtr.Sub_idx_null, root);
	}
	public static final byte[] Tag__end_quote = ByteAry_.new_ascii_("\">"), Tag__end_bgn = ByteAry_.new_ascii_("</")
		, Tag_hdr_bgn = ByteAry_.new_ascii_("<h"), Tag_hdr_end = ByteAry_.new_ascii_("</h"), Tag_hr = ByteAry_.new_ascii_("<hr/>"), Tag_br = ByteAry_.new_ascii_("<br/>")
		, Tag_list_grp_ul_bgn = ByteAry_.new_ascii_("<ul>"), Tag_list_grp_ul_end = ByteAry_.new_ascii_("</ul>")
		, Tag_list_grp_ol_bgn = ByteAry_.new_ascii_("<ol>"), Tag_list_grp_ol_end = ByteAry_.new_ascii_("</ol>")
		, Tag_list_itm_li_bgn = ByteAry_.new_ascii_("<li>"), Tag_list_itm_li_end = ByteAry_.new_ascii_("</li>")
		, Tag_list_itm_dt_bgn = ByteAry_.new_ascii_("<dt>"), Tag_list_itm_dt_end = ByteAry_.new_ascii_("</dt>")
		, Tag_list_itm_dd_bgn = ByteAry_.new_ascii_("<dd>"), Tag_list_itm_dd_end = ByteAry_.new_ascii_("</dd>")
		, Tag_list_grp_dl_bgn = ByteAry_.new_ascii_("<dl>"), Tag_list_grp_dl_end = ByteAry_.new_ascii_("</dl>")
		, File_divider = ByteAry_.new_ascii_("---------------------------------")
		, Tag_tblw_tb_bgn = ByteAry_.new_ascii_("<table>"), Tag_tblw_tb_bgn_atr = ByteAry_.new_ascii_("<table"), Tag_tblw_tb_end = ByteAry_.new_ascii_("</table>")
		, Tag_tblw_tr_bgn = ByteAry_.new_ascii_("<tr>"), Tag_tblw_tr_bgn_atr = ByteAry_.new_ascii_("<tr"), Tag_tblw_tr_end = ByteAry_.new_ascii_("</tr>")
		, Tag_tblw_td_bgn = ByteAry_.new_ascii_("<td>"), Tag_tblw_td_bgn_atr = ByteAry_.new_ascii_("<td"), Tag_tblw_td_end = ByteAry_.new_ascii_("</td>")
		, Tag_tblw_th_bgn = ByteAry_.new_ascii_("<th>"), Tag_tblw_th_bgn_atr = ByteAry_.new_ascii_("<th"), Tag_tblw_th_end = ByteAry_.new_ascii_("</th>")
		, Tag_tblw_tc_bgn = ByteAry_.new_ascii_("<caption>"), Tag_tblw_tc_bgn_atr = ByteAry_.new_ascii_("<caption"), Tag_tblw_tc_end = ByteAry_.new_ascii_("</caption>")
		, Ary_escape_bgn = ByteAry_.new_ascii_("&lt;"), Ary_escape_end = ByteAry_.new_ascii_("&gt;"), Ary_escape_end_bgn = ByteAry_.new_ascii_("&lt;/")
		, Tag_para_bgn = ByteAry_.new_ascii_("<p>"), Tag_para_end = ByteAry_.new_ascii_("</p>"), Tag_para_mid = ByteAry_.new_ascii_("</p>\n\n<p>")
		, Tag_image_end = ByteAry_.new_ascii_("</img>")
		, Tag_pre_bgn = ByteAry_.new_ascii_("<pre>"), Tag_pre_end = ByteAry_.new_ascii_("</pre>")
		;
	public static final byte Tag__bgn = Byte_ascii.Lt, Tag__end = Byte_ascii.Gt;
	public static final byte Dir_spr_http = Byte_ascii.Slash;
	int indent_level = 0;
	public static final int Sub_idx_null = -1;	// nonsense placeholder fo
}
class ByteAryFmtrArg_html_tkn implements ByteAryFmtrArg {
	public ByteAryFmtrArg_html_tkn(Xoh_html_wtr wtr, Xoh_opts opts, byte[] src, Xop_tkn_itm tkn, int depth) {this.wtr = wtr; this.opts = opts; this.src = src; this.tkn = tkn; this.depth = depth;}
	public void XferAry(ByteAryBfr trg, int idx) {
		wtr.Write_tkn(opts, trg, src, depth + 1, null, Xoh_html_wtr.Sub_idx_null, tkn);
	}
	Xoh_html_wtr wtr; Xoh_opts opts; byte[] src; Xop_tkn_itm tkn; int depth;
}
class ByteAryFmtrArg_html_fmtr implements ByteAryFmtrArg {
	public ByteAryFmtrArg_html_fmtr Set(Xoh_html_wtr wtr, Xoh_opts opts, byte[] src, Xop_tkn_itm tkn, int depth, ByteAryFmtr fmtr) {this.wtr = wtr; this.opts = opts; this.src = src; this.tkn = tkn; this.depth = depth; this.fmtr = fmtr; return this;}
	public void XferAry(ByteAryBfr trg, int idx) {
		ByteAryBfr tmp_bfr = ByteAryBfr.new_();
		wtr.Write_tkn(opts, tmp_bfr, src, depth + 1, null, Xoh_html_wtr.Sub_idx_null, tkn);
		byte[] bry = tmp_bfr.XtoAryAndClear();
		if (bry.length == 0) return;
		fmtr.Bld_bfr_many(trg, bry);
	}
	ByteAryFmtr fmtr; Xoh_html_wtr wtr; Xoh_opts opts; byte[] src; Xop_tkn_itm tkn; int depth;
}
class Xoh_html_wtr_env {
	public Xop_lnki_caption_mgr LnkiMgr() {return lnkiMgr;} private Xop_lnki_caption_mgr lnkiMgr = new Xop_lnki_caption_mgr();
	public HashAdp HtmlNcrs() {return htmlNcrs;} HashAdp htmlNcrs = HashAdp_.new_();
	public byte[][] AposCmds() {return aposCmds;} private byte[][] aposCmds = new byte[13][];
	public void AposCmds_reg(int i, String s) {aposCmds[i] = ByteAry_.new_ascii_(s);}
	public boolean CommentVisible() {return commentVisible;} public Xoh_html_wtr_env CommentVisible_(boolean v) {commentVisible = v; return this;} private boolean commentVisible = true;
	public byte[] NewLine() {return newLine;} public Xoh_html_wtr_env NewLine_(byte[] v) {newLine = v; return this;} private byte[] newLine;
}

/*
NOTE_1:inline always written as <tag></tag>, not <tag/>
see WP:Permian�Triassic extinction event
this will cause firefox to swallow up rest of text
<div id="ScaleBar" style="width:1px; float:left; height:38em; padding:0; background-color:#242020" />
this will not
<div id="ScaleBar" style="width:1px; float:left; height:38em; padding:0; background-color:#242020"  ></div>
*/
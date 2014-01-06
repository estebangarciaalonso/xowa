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
package gplx.xowa.xtns.lst; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xtn_pages_nde implements Xop_xnde_xtn, Xop_xnde_atr_parser {
	public Xop_root_tkn Xtn_root() {return xtn_root;} private Xop_root_tkn xtn_root;
	public boolean Xtn_literal() {return xtn_literal;} private boolean xtn_literal = false;
	public static final byte Xatr_root_page = 0, Xatr_bgn_page = 1, Xatr_end_page = 2, Xatr_bgn_sect = 3, Xatr_end_sect = 4;
	int bgn_page, end_page; byte[] bgn_sect, end_sect, root_page;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {
		if (xatr_key_obj == null) return;
		ByteVal xatr_key = (ByteVal)xatr_key_obj;
		switch (xatr_key.Val()) {
			case Xatr_root_page:	root_page = xatr.Val_as_bry(src); break;
			case Xatr_bgn_page:		bgn_page = xatr.Val_as_int_or(src, -1); break;
			case Xatr_end_page:		end_page = xatr.Val_as_int_or(src, -1); break;
			case Xatr_bgn_sect:		bgn_sect = xatr.Val_as_bry(src); break;
			case Xatr_end_sect:		end_sect = xatr.Val_as_bry(src); break;
		}
	}
	public void Xtn_compile(Xow_wiki wiki, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		Xowc_xtn_pages cfg_pages = wiki.Cfg_parser().Xtns().Itm_pages();
		if (cfg_pages.Init_needed()) cfg_pages.Init(wiki.Ns_mgr());
		int Ns_page = cfg_pages.Ns_page_id();
		Xoa_app app = wiki.App();
		Xop_xatr_itm.Xatr_parse(app, this, xtn_atrs, wiki, src, xnde);
		if (root_page == null)		{xtn_literal = true; app.Msg_log().Add_str_warn_fmt_many(GRP_KEY, "attribute_missing_index", "pages nodes requires an index attribute: ~{0}", String_.new_utf8_(src)); return;}
		if (bgn_page == -1)			{xtn_literal = true; app.Msg_log().Add_str_warn_fmt_many(GRP_KEY, "attribute_missing_from", "pages nodes requires a from attribute: ~{0}", String_.new_utf8_(src)); return;}
		if (end_page == -1)			{xtn_literal = true; app.Msg_log().Add_str_warn_fmt_many(GRP_KEY, "attribute_missing_to", "pages nodes requires a to attribute: ~{0}", String_.new_utf8_(src)); return;}
		if (bgn_page > end_page)	{xtn_literal = true; app.Msg_log().Add_str_warn_fmt_many(GRP_KEY, "attribute_invalid_to", "from must be less than to: from=~{0} to=~{0}", bgn_page, end_page); return;}
		Xow_ns page_ns = wiki.Ns_mgr().Get_by_id(Ns_page);
		if (page_ns == null)		{xtn_literal = true; app.Msg_log().Add_str_warn_fmt_many(GRP_KEY, "attribute_invalid_ns", "pages ns does not exist: ~{0}", String_.new_utf8_(src)); return;}	// occurs when <pages> used in a wiki without a "Page:" ns; EX: de.w: Help:Buchfunktion/Feedback
		// FUTURE: find canonical ns; proofreadpage_namespace; there's an index ns????
		ByteAryBfr page_bfr = app.Utl_bry_bfr_mkr().Get_m001();
		ByteAryBfr ttl_bfr = app.Utl_bry_bfr_mkr().Get_b512();
		Hash_adp_bry lst_page_regy = ctx.Lst_page_regy();
		if (lst_page_regy == null) lst_page_regy = new Hash_adp_bry(true);	// SEE:NOTE:page_regy; DATE:2014-01-01
		for (int i = bgn_page; i <= end_page; i++) {
			ttl_bfr.Add(page_ns.Name_db_w_colon())		// EX: 'Page:'
				.Add(root_page)							// EX: 'File.djvu'
				.Add_byte(Byte_ascii.Slash)				// EX: '/'
				.Add_int_variable(i)					// EX: '123'
				;
			byte[] ttl_bry = ttl_bfr.XtoAryAndClear();
			Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
			Xoa_page page = wiki.Data_mgr().Get_page(ttl, false); if (page == null) {page_bfr.Mkr_rls(); ttl_bfr.Mkr_rls(); return;}
			if (lst_page_regy.Get_by_bry(ttl_bry) == null)	// check if page was already added; avoids recursive <page> calls which will overflow stack; DATE:2014-01-01
				lst_page_regy.Add(ttl_bry, ttl_bry);
			else
				continue;
			byte[] page_bry = page.Data_raw();
			if 		(i == bgn_page && bgn_sect != null) {
				byte[] page_rhs = Parse_and_extract(wiki, page_bry, Bry_atr_begin, bgn_sect, false);
				if (page_rhs == null) {
					app.Msg_log().Add_str_warn_fmt_many(GRP_KEY, "attribute_missing_bgn_page", "fromsection was not found: section=~{0}", String_.new_utf8_(bgn_sect));
					continue;
				}
				page_bfr.Add(page_rhs);
			}
			else if	(i == end_page && end_sect != null) {
				byte[] page_lhs = Parse_and_extract(wiki, page_bry, Bry_atr_end, end_sect, true);
				if (page_lhs == null) {
					app.Msg_log().Add_str_warn_fmt_many(GRP_KEY, "attribute_missing_end_page", "tosection was not found: section=~{0}", String_.new_utf8_(end_sect));
					continue;
				}
				page_bfr.Add(page_lhs);
			}
			else
				page_bfr.Add(page_bry);
		}
		// FUTURE: include lnkis to page; '[[' . $title->getPrefixedText() . "|$view]] "
		byte[] combined = page_bfr.XtoAry();
		Xop_ctx tmp_ctx = Xop_ctx.new_sub_page_(wiki, lst_page_regy);
		xtn_root = tmp_ctx.Tkn_mkr().Root(combined);

		tmp_ctx.Parse_tid_(Xop_parser_.Parse_tid_tmpl);
		tmp_ctx.Page().Page_ttl_(ctx.Page().Page_ttl());	// NOTE: must set tmp_ctx.Ttl to ctx.Ttl; EX: Flatland and First World; DATE:2013-04-29
		Xot_defn_tmpl tmp_tmpl = wiki.Parser().Parse_tmpl(tmp_ctx, tmp_ctx.Tkn_mkr(), wiki.Ns_mgr().Ns_template(), ByteAry_.Empty, combined); 
		page_bfr.Clear();
		tmp_tmpl.Tmpl_evaluate(tmp_ctx, Xot_invk_temp.PageIsCaller, page_bfr);
		byte[] compiled = page_bfr.XtoAryAndClear();
		wiki.Parser().Parse_page_all(xtn_root, tmp_ctx, ctx.Tkn_mkr(), compiled, -1);
		xtn_root.Root_src_(xtn_root.Root_src());

		page_bfr.Mkr_rls().Clear();
		ttl_bfr.Mkr_rls().Clear();
	}
	byte[] Parse_and_extract(Xow_wiki wiki, byte[] src, byte[] section_name, byte[] section_val, boolean before) {
		Xop_ctx tmp_ctx = Xop_ctx.new_sub_(wiki); Xop_tkn_mkr tmp_tkn_mkr = tmp_ctx.Tkn_mkr();
		Xop_root_tkn tmp_root = tmp_tkn_mkr.Root(src);
		wiki.Parser().Parse_page_wiki(tmp_root, tmp_ctx, tmp_tkn_mkr, src, Xop_parser_.Doc_bgn_bos);
		Xop_xnde_tkn section_tkn = Find_section(tmp_root, section_name, section_val, src, before);
		if (section_tkn == null) return null; // section not found
		return before 
				? ByteAry_.Mid(src, 0						, section_tkn.Src_bgn())
				: ByteAry_.Mid(src, section_tkn.Src_bgn()	, src.length)
				;
	}
	Xop_xnde_tkn Find_section(Xop_tkn_itm tkn, byte[] section_name, byte[] section_val, byte[] src, boolean before) {
		int subs_len = tkn.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Xop_tkn_itm sub_tkn = tkn.Subs_get(i);
			if (sub_tkn.Tkn_tid() == Xop_tkn_itm_.Tid_xnde) {
				Xop_xnde_tkn sub_tkn_xnde = (Xop_xnde_tkn)sub_tkn;
				if (sub_tkn_xnde.Tag().Id() == Xop_xnde_tag_.Tid_section) {
					if (sub_tkn_xnde.Atrs_ary() != null) {	// guard against empty; "<section/>"
						int atrs_len = sub_tkn_xnde.Atrs_ary().length;
						for (int j = 0; j < atrs_len; j++) {					
							Xop_xatr_itm atr = sub_tkn_xnde.Atrs_ary()[j];
							if (ByteAry_.Match(src, atr.Key_bgn(), atr.Key_end(), section_name))
								if (ByteAry_.Match(src, atr.Val_bgn(), atr.Val_end(), section_val))
									return sub_tkn_xnde;
							
						}
					}
				}
			}
			if (sub_tkn.Subs_len() > 0)
				return Find_section(sub_tkn, section_name, section_val, src, before);
		}
		return null;
	}
	private static final String GRP_KEY = "xowa.xtns.pages";
	private static Hash_adp_bry xtn_atrs = new Hash_adp_bry(false)	// NOTE: these do not seem to be i18n'd; no ProofreadPage.magic.php; ProofreadPage.i18n.php only has messages; ProofreadPage.body.php refers to names literally
		.Add_str_obj("index", ByteVal.new_(Xtn_pages_nde.Xatr_root_page))
		.Add_str_obj("from", ByteVal.new_(Xtn_pages_nde.Xatr_bgn_page))
		.Add_str_obj("to", ByteVal.new_(Xtn_pages_nde.Xatr_end_page))
		.Add_str_obj("fromsection", ByteVal.new_(Xtn_pages_nde.Xatr_bgn_sect))
		.Add_str_obj("tosection", ByteVal.new_(Xtn_pages_nde.Xatr_end_sect))
	;
	private static final byte[] Bry_atr_begin = ByteAry_.new_utf8_("begin"), Bry_atr_end = ByteAry_.new_utf8_("end"); 
}
/*
NOTE:page_regy
. original implmentation was following
in Xop_ctx
	public Hash_adp_bry			Lst_page_regy()		{if (lst_page_regy == null) lst_page_regy = new Hash_adp_bry(true); return lst_page_regy;} 
in Xtn_pages_nde
	Hash_adp_bry lst_page_regy = ctx.Lst_page_regy();
. current implementation is following
in Xop_ctx
	public Hash_adp_bry			Lst_page_regy()		{return lst_page_regy;} 
in Xtn_pages_nde
	Hash_adp_bry lst_page_regy = ctx.Lst_page_regy();
	if (lst_page_regy == null) lst_page_regy = new Hash_adp_bry(true);
. note that this only skips transcluded <pages> within a given <pages> call, not across the entire page
EX: Page:A/1 has the following text
<pages index="A" from=1 to=3 />
<pages index="B" from=1 to=1 />
text
<pages index="B" from=1 to=1 />
. original implementation would correctly include <pages index="A" from=1 to=3 /> only once, but would also include <pages index="B" from=1 to=1 /> once
. current implmentation would include <pages index="B" from=1 to=1 /> twice
. also, side-effect of only having Lst_page_regy only be non-null on sub_ctx, which means nothing needs to be cleared on main_ctx
*/

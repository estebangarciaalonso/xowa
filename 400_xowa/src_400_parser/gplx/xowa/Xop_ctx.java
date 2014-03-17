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
import gplx.xowa.gui.*; import gplx.xowa.xtns.lst.*;
import gplx.xowa.xtns.scribunto.*;
import gplx.xowa.xtns.wdatas.*;
import gplx.xowa.parsers.logs.*;
public class Xop_ctx {
	Xop_ctx(Xow_wiki wiki) {
		this.app = wiki.App(); this.msg_log = app.Msg_log();
		this.wiki = wiki;
		page = new Xoa_page(wiki, Xoa_ttl.parse_(wiki, Xoa_page_.Main_page_bry));	// HACK: use "Main_Page" to put in valid page title
		wkrs = wkrs_(para, apos, xnde, list, lnki, hdr, amp, lnke, tblw, invk);
		for (Xop_ctx_wkr wkr : wkrs) wkr.Ctor_ctx(this);
	}
	public Xoa_app				App()				{return app;} private Xoa_app app;
	public Xow_wiki				Wiki()				{return wiki;} private Xow_wiki wiki;
	public Xog_tab				Tab()				{return tab;} private Xog_tab tab;
	public Xol_lang				Lang()				{return wiki.Lang();}
	public Xoa_page				Page()				{return page;} public Xop_ctx Page_(Xoa_page v) {page = v; return this;} private Xoa_page page;
	public Xop_tkn_mkr			Tkn_mkr()			{return app.Tkn_mkr();}
	public Xop_amp_wkr			Amp()				{return amp;}	private Xop_amp_wkr  amp  = new Xop_amp_wkr();
	public Xop_apos_wkr			Apos()				{return apos;}	private Xop_apos_wkr apos = new Xop_apos_wkr();
	public Xop_lnke_wkr			Lnke()				{return lnke;}	private Xop_lnke_wkr lnke = new Xop_lnke_wkr();
	public Xop_lnki_wkr			Lnki()				{return lnki;}	private Xop_lnki_wkr lnki = new Xop_lnki_wkr();
	public Xop_hdr_wkr			Hdr()				{return hdr;}	private Xop_hdr_wkr  hdr  = new Xop_hdr_wkr();
	public Xop_para_wkr			Para()				{return para;}	private Xop_para_wkr para = new Xop_para_wkr();
	public Xop_list_wkr			List()				{return list;}	private Xop_list_wkr list = new Xop_list_wkr();
	public Xop_tblw_wkr			Tblw()				{return tblw;}	private Xop_tblw_wkr tblw = new Xop_tblw_wkr();
	public Xop_xnde_wkr			Xnde()				{return xnde;}	private Xop_xnde_wkr xnde = new Xop_xnde_wkr();
	public Xot_invk_wkr			Invk()				{return invk;}	private Xot_invk_wkr invk = new Xot_invk_wkr();
	public Xop_curly_wkr		Curly() 			{return curly;} private Xop_curly_wkr curly = new Xop_curly_wkr();
	public Gfo_msg_log			Msg_log()			{return msg_log;} Gfo_msg_log msg_log;
	public boolean					Sys_load_tmpls()	{return sys_load_tmpls;} public Xop_ctx Sys_load_tmpls_(boolean v) {sys_load_tmpls = v; return this;} private boolean sys_load_tmpls = true;
	public Xow_wiki_revs		Page_revData()		{return page_revData;} private Xow_wiki_revs page_revData = new Xow_wiki_revs();
	public boolean					Ref_nested()		{return ref_nested;} public Xop_ctx Ref_nested_(boolean v) {ref_nested = v; return this;} private boolean ref_nested;
	public Xot_defn_trace		Defn_trace()		{return defn_trace;} public Xop_ctx Defn_trace_(Xot_defn_trace v) {defn_trace = v; return this;} private Xot_defn_trace defn_trace = Xot_defn_trace_null._;
	public byte					Parse_tid()			{return parse_tid;} public Xop_ctx Parse_tid_(byte v) {parse_tid = v; return this;} private byte parse_tid = Xop_parser_.Parse_tid_null;
	public byte					Cur_tkn_tid()		{return cur_tkn_tid;} private byte cur_tkn_tid = Xop_tkn_itm_.Tid_null;
	public boolean					Lxr_make()			{return lxr_make;} public Xop_ctx Lxr_make_(boolean v) {lxr_make = v; return this;} private boolean lxr_make = false;
	public boolean					Only_include_evaluate() {return only_include_evaluate;} public Xop_ctx Only_include_evaluate_(boolean v) {only_include_evaluate = v; return this;} private boolean only_include_evaluate;
	public Lst_section_nde_mgr	Lst_section_mgr()	{if (lst_section_mgr == null) lst_section_mgr = new Lst_section_nde_mgr(); return lst_section_mgr;} Lst_section_nde_mgr lst_section_mgr;
	public Hash_adp_bry			Lst_page_regy()		{return lst_page_regy;} private Hash_adp_bry lst_page_regy;
	public Xop_log_invoke_wkr	Xtn__scribunto__invoke_wkr() {
		if (scrib_invoke_wkr == null)
			scrib_invoke_wkr = ((Scrib_xtn_mgr)(app.Xtn_mgr().Get_or_fail(Scrib_xtn_mgr.XTN_KEY))).Invoke_wkr();
		return scrib_invoke_wkr;
	}	private Xop_log_invoke_wkr scrib_invoke_wkr;
	public Xop_log_property_wkr Xtn__wikidata__property_wkr() {return app.Wiki_mgr().Wdata_mgr().Property_wkr();}
	public ByteAryBfr Tmpl_output() {return tmpl_output;} public Xop_ctx Tmpl_output_(ByteAryBfr v) {tmpl_output = v; return this;} private ByteAryBfr tmpl_output;
	private Xop_ctx_wkr[] wkrs = new Xop_ctx_wkr[] {}; private Xop_ctx_wkr[] wkrs_(Xop_ctx_wkr... ary) {return ary;}
	public int Tag_idx;
	public Xop_ctx Clear() {
		page.Clear();
		stack = Xop_tkn_itm_.Ary_empty;
		stack_len = stack_max = 0;
		Tag_idx = 0;
		app.Wiki_mgr().Wdata_mgr().Clear();
		if (lst_section_mgr != null) lst_section_mgr.Clear();
		if (lst_page_regy != null) lst_page_regy.Clear();
		tmpl_output = null;
		return this;
	}
	public void Page_bgn(Xop_root_tkn root, byte[] src) {
		this.Msg_log().Clear(); cur_tkn_tid = Xop_tkn_itm_.Tid_null;
		empty_ignored = false;
		for (Xop_ctx_wkr wkr : wkrs) wkr.Page_bgn(this, root);
	}
	public void Page_end(Xop_root_tkn root, byte[] src, int src_len) {
		Stack_pop_til(root, src, 0, true, src_len, src_len);
		for (Xop_ctx_wkr wkr : wkrs) wkr.Page_end(this, root, src, src_len);
	}
	public int LxrMake_txt_(int pos)									{lxr_make = false; return pos;}
	public int LxrMake_log_(Gfo_msg_itm itm, byte[] src, int bgn_pos, int cur_pos)	{lxr_make = false; msg_log.Add_itm_none(itm, src, bgn_pos, cur_pos); return cur_pos;}
	public void StackTkn_add(Xop_root_tkn root, Xop_tkn_itm sub) {
		root.Subs_add(sub);
		this.Stack_add(sub);
	}
	public boolean Empty_ignored() {return empty_ignored;}
	public void Empty_ignored_y_() {empty_ignored = true;} private boolean empty_ignored = false;
	public void Empty_ignored_n_() {empty_ignored = false;}
	public void Empty_ignore(Xop_root_tkn root, int empty_bgn) {
		int empty_end = root.Subs_len();
		for (int i = empty_bgn; i < empty_end; i++) {
			Xop_tkn_itm sub_tkn = root.Subs_get(i);
			sub_tkn.Ignore_y_grp_(this, root, i);
		}
		empty_ignored = false;
	}
	public void Subs_add_and_stack_tblw(Xop_root_tkn root, Xop_tblw_tkn owner_tkn, Xop_tkn_itm sub) {
		if (owner_tkn != null) owner_tkn.Tblw_subs_len_add_();	// owner_tkn can be null;EX: "{|" -> prv_tkn is null
		Subs_add_and_stack(root, sub);
	}
	public void Subs_add_and_stack(Xop_root_tkn root, Xop_tkn_itm sub) {this.Subs_add(root, sub); this.Stack_add(sub);}
	public void Subs_add(Xop_root_tkn root, Xop_tkn_itm sub) {
		switch (sub.Tkn_tid()) {
			case Xop_tkn_itm_.Tid_space: case Xop_tkn_itm_.Tid_tab: case Xop_tkn_itm_.Tid_newLine:
			case Xop_tkn_itm_.Tid_para:
				break;
			default:
				empty_ignored = false;
				break;
		}
		root.Subs_add(sub);
	}
	public void Stack_add(Xop_tkn_itm tkn) {
		int newLen = stack_len + 1;
		if (newLen > stack_max) {
			stack_max = newLen * 2;
			stack = (Xop_tkn_itm[])Array_.Resize(stack, stack_max);
		}
		stack[stack_len] = tkn;
		cur_tkn_tid = tkn.Tkn_tid();
		stack_len = newLen;
	}	private Xop_tkn_itm[] stack = Xop_tkn_itm_.Ary_empty; int stack_len = 0, stack_max = 0;
	public int Stack_len() {return stack_len;}
	public Xop_tkn_itm Stack_get_last()		{return stack_len == 0 ? null : stack[stack_len - 1];}
	public Xop_tkn_itm Stack_get(int i)		{return i < 0 || i >= stack_len ? null : stack[i];}
	public Xop_tblw_tkn Stack_get_tbl_tb() {
		for (int i = stack_len - 1; i > -1; i--) {
			Xop_tkn_itm tkn = stack[i];
			switch (tkn.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_tblw_tb:
					return (Xop_tblw_tkn)tkn;
				case Xop_tkn_itm_.Tid_xnde:
					Xop_xnde_tkn xnde_tkn = (Xop_xnde_tkn)tkn;
					switch (xnde_tkn.Tag().Id()) {
						case Xop_xnde_tag_.Tid_table:
							return (Xop_tblw_tkn)tkn;
					}
					break;
			}
		}
		return null;
	}
	public Xop_tblw_tkn Stack_get_tblw() {
		for (int i = stack_len - 1; i > -1; i--) {
			Xop_tkn_itm tkn = stack[i];
			switch (tkn.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_tblw_tb:
				case Xop_tkn_itm_.Tid_tblw_tr:
				case Xop_tkn_itm_.Tid_tblw_td:
				case Xop_tkn_itm_.Tid_tblw_th:
				case Xop_tkn_itm_.Tid_tblw_tc:
					return (Xop_tblw_tkn)tkn;
				case Xop_tkn_itm_.Tid_xnde:
					Xop_xnde_tkn xnde_tkn = (Xop_xnde_tkn)tkn;
					switch (xnde_tkn.Tag().Id()) {
						case Xop_xnde_tag_.Tid_table:
						case Xop_xnde_tag_.Tid_tr:
						case Xop_xnde_tag_.Tid_td:
						case Xop_xnde_tag_.Tid_th:
						case Xop_xnde_tag_.Tid_caption:
							return (Xop_tblw_tkn)tkn;
					}
					break;
			}
		}
		return null;
	}
	public static final int Stack_not_found = -1;
	public boolean Stack_has(int typeId) {return Stack_idx_typ(typeId) != Stack_not_found;}
	public int Stack_idx_typ(int typeId)	{
		for (int i = stack_len - 1; i > -1; i--)
			if (stack[i].Tkn_tid() == typeId)
				return i; 
		return Stack_not_found;
	}
	public int Stack_idx_typ_tblw(int typeId) { // NOTE: separate proc for xnde which may try to pop past a td/th/tc (and thus break a table unnecessarily)
		for (int i = stack_len - 1; i > -1	; i--) {
			int itm_typeId = stack[i].Tkn_tid();				
			switch (itm_typeId) {
				case Xop_tkn_itm_.Tid_tblw_td:
				case Xop_tkn_itm_.Tid_tblw_th:
				case Xop_tkn_itm_.Tid_tblw_tc:
					return -1;
			}
			if (itm_typeId == typeId)
				return i;
		}
		return -1;
	}
	public Xop_tkn_itm Stack_get_typ(int tid) {
		for (int i = stack_len - 1; i > -1	; i--) {
			Xop_tkn_itm tkn = stack[i];
			if (tkn.Tkn_tid() == tid) return tkn;
		}
		return null;
	}
	public void Stack_del(Xop_tkn_itm del) {
		if (stack_len == 0) return;
		for (int i = stack_len - 1; i > -1; i--) {
			Xop_tkn_itm tkn = stack[i];
			if (tkn == del) {
				for (int j = i + 1; j < stack_len; j++) {
					stack[j - 1] = stack[j];
				}
				--stack_len;
				break;
			}
		}
	}
	public Xop_tkn_itm Stack_pop_til(Xop_root_tkn root, byte[] src, int til_idx, boolean include, int bgn_pos, int cur_pos) {
		if (stack_len == 0) return null;
		int min_idx = include ? til_idx - 1 : til_idx;
		if (min_idx < -1) min_idx = -1;
		Xop_tkn_itm rv = null;
		for (int i = stack_len - 1; i > min_idx; i--) {
			rv = stack[i];
			Stack_autoClose(root, src, rv, bgn_pos, cur_pos);
		}
		Stack_pop_idx(til_idx);
		return include ? rv : stack[stack_len]; // if include, return poppedTkn; if not, return tkn before poppedTkn
	}
	public Xop_tkn_itm Stack_pop_before(Xop_root_tkn root, byte[] src, int til_idx, boolean include, int bgn_pos, int cur_pos) {	// used by Xop_tblw_lxr to detect \n| in lnki; seems useful as well
		if (stack_len == 0) return null;
		int min_idx = include ? til_idx - 1 : til_idx;
		if (min_idx < -1) min_idx = -1;
		Xop_tkn_itm rv = null;
		for (int i = stack_len - 1; i > min_idx; i--) {
			rv = stack[i];
			Stack_autoClose(root, src, rv, bgn_pos, cur_pos);
		}
		return include ? rv : stack[stack_len]; // if include, return poppedTkn; if not, return tkn before poppedTkn
	}
	public void Stack_autoClose(Xop_root_tkn root, byte[] src, Xop_tkn_itm tkn, int bgn_pos, int cur_pos) {
		int src_len = src.length;
		switch (tkn.Tkn_tid()) {
			case Xop_tkn_itm_.Tid_newLine: break;	// NOOP: just a marker
			case Xop_tkn_itm_.Tid_list: list.AutoClose(this, app.Tkn_mkr(), root, src, src_len, bgn_pos, cur_pos, tkn); break;
			case Xop_tkn_itm_.Tid_xnde: xnde.AutoClose(this, root, src, src_len, bgn_pos, cur_pos, tkn); break;
			case Xop_tkn_itm_.Tid_apos: apos.AutoClose(this, src, src_len, bgn_pos, cur_pos, tkn); break;
			case Xop_tkn_itm_.Tid_lnke: lnke.AutoClose(this, src, src_len, bgn_pos, cur_pos, tkn); break;
			case Xop_tkn_itm_.Tid_hdr:  hdr.AutoClose(this, app.Tkn_mkr(), root, src, src_len, bgn_pos, cur_pos, tkn); break;
			case Xop_tkn_itm_.Tid_tblw_tb:
			case Xop_tkn_itm_.Tid_tblw_tr:
			case Xop_tkn_itm_.Tid_tblw_td:
			case Xop_tkn_itm_.Tid_tblw_th:
			case Xop_tkn_itm_.Tid_tblw_tc: tblw.AutoClose(this, root, src, src_len, bgn_pos, cur_pos, tkn); break;
			case Xop_tkn_itm_.Tid_lnki: lnki.Auto_close(this, app.Tkn_mkr(), root, src, src_len, bgn_pos, cur_pos, tkn); break;
			case Xop_tkn_itm_.Tid_pre: para.AutoClose(this, app.Tkn_mkr(), root, src, src_len, bgn_pos, cur_pos, tkn); break;
		}
	}
	public void Stack_pop_idx(int tilIdx) {
		stack_len = tilIdx < 0 ? 0 : tilIdx;
		cur_tkn_tid = stack_len == 0 ? Xop_tkn_itm_.Tid_null : stack[stack_len - 1].Tkn_tid();
	}
	public void Stack_pop_lnki() {	// used primarily by lnke to remove lnke from stack
		--stack_len;
		cur_tkn_tid = stack_len == 0 ? Xop_tkn_itm_.Tid_null : stack[stack_len - 1].Tkn_tid();
	}
	public void CloseOpenItms(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		int stack_pos = -1, stack_len = ctx.Stack_len(); boolean stop = false;
		for (int i = 0; i < stack_len; i++) {			// loop over stack
			Xop_tkn_itm prv_tkn = ctx.Stack_get(i);
			switch (prv_tkn.Tkn_tid()) {					// find first list/hdr; close everything until this
				case Xop_tkn_itm_.Tid_list:
				case Xop_tkn_itm_.Tid_hdr:
					stack_pos = i; stop = true; break;
			}
			if (stop) break;
		}
		if (stack_pos == -1) return;
		ctx.Stack_pop_til(root, src, stack_pos, true, bgn_pos, cur_pos);
	}
	public void Tmpl_prepend_nl(ByteAryBfr cur, byte[] bry, int bry_len) {
		if (	bry_len == 0													// bry is empty
			||	tmpl_prepend_nl_trie.MatchAtCur(bry, 0, bry_len) == null		// bry does not start with {| : ; # *; REF.MW:Parser.php|braceSubstitution
			) return;												
		ByteAryBfr prv_bfr														// note that cur_bfr should be checked first before tmpl_output
			= cur.Len() == 0 //&& tmpl_output != null
			? tmpl_output
			: cur
			;
		if (	prv_bfr != null													// note that prv_bfr can be null when called from a subparse, as in Scrib.Preprocess; EX:w:Portal:Canada; DATE:2014-02-13
			&& !prv_bfr.Match_end_byt(Byte_ascii.NewLine))						// previous char is not \n;
			cur.Add_byte(Byte_ascii.NewLine);
	}
	public static Xop_ctx new_(Xow_wiki wiki) {
		Xop_ctx rv = new Xop_ctx(wiki);
		rv.tab = new Xog_tab();
		return rv;
	}
	public static Xop_ctx new_sub_(Xow_wiki wiki) {
		Xop_ctx rv = new Xop_ctx(wiki);
		Xop_ctx ctx = wiki.Ctx();
		new_copy(ctx, rv);
		return rv;
	}
	public static Xop_ctx new_sub_page_(Xow_wiki wiki, Xop_ctx ctx, Hash_adp_bry lst_page_regy) {
		Xop_ctx rv = new_sub_(wiki);
		new_copy(ctx, rv);
		rv.lst_page_regy = lst_page_regy;			// NOTE: must share ref for lst only (do not share for sub_(), else stack overflow)
		rv.Page().Ref_mgr_(ctx.Page().Ref_mgr());	// NOTE: must share ref_mgr, else references in sub_ctx will not be picked up in root_ctx; EX: en.wikisource.org/wiki/Flatland_(first_edition)/This_World; DATE:2014-01-18
		return rv;
	}
	private static void new_copy(Xop_ctx src, Xop_ctx trg) {
		trg.Lnki().File_wkr_(src.Lnki().File_wkr());
		trg.Page().Id_(src.Page().Id());
		trg.Page().Ttl_(src.Page().Ttl());				// NOTE: sub_ctx must have same page_ttl as owner; see Lst_pfunc_lst_tst!Fullpagename
		trg.Page().Lnki_list_(src.Page().Lnki_list());	// FIXED: must share lnki_list, else image_map won't render; DATE:2014-02-14
		trg.tab = src.Tab();							// NOTE: tab should be same instance across all sub_ctxs; otherwise CallParserFunction will not set DISPLAYTITLE correctly; DATE:2013-08-05
		trg.tmpl_output = src.tmpl_output;
	}
	private static final ByteTrieMgr_fast tmpl_prepend_nl_trie = Xop_curly_bgn_lxr.tmpl_bgn_trie_();
}

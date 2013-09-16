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
public class Xop_ctx {
	Xop_ctx(Xow_wiki wiki) {
		this.app = wiki.App(); this.msg_log = app.Msg_log();
		this.wiki = wiki;
		page = new Xoa_page(wiki, Xoa_ttl.parse_(wiki, Xoa_page.Bry_main_page));
		wkrs = wkrs_(para, apos, xnde, list, lnki, hdr, amp, lnke, tblw, invk);
		for (Xop_ctx_wkr wkr : wkrs) wkr.Ctor_ctx(this);			
	}	private Xop_ctx_wkr[] wkrs = new Xop_ctx_wkr[] {}; Xop_ctx_wkr[] wkrs_(Xop_ctx_wkr... ary) {return ary;}
	public Xoa_app				App()				{return app;} private Xoa_app app;
	public Xow_wiki				Wiki()				{return wiki;} private Xow_wiki wiki;
	public Xog_tab Tab() {return tab;} public Xop_ctx Tab_(Xog_tab v) {tab = v; return this;} private Xog_tab tab = new Xog_tab();
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
	public byte[]				Src()				{return src;} private byte[] src; int src_len;
	public Xop_root_tkn			Root()				{return root;} private Xop_root_tkn root;
	public Gfo_msg_log			Msg_log()			{return msg_log;} Gfo_msg_log msg_log;
	public boolean					Sys_load_tmpls()	{return sys_load_tmpls;} public Xop_ctx Sys_load_tmpls_(boolean v) {sys_load_tmpls = v; return this;} private boolean sys_load_tmpls = true;
	public Xow_wiki_revs		Page_revData()		{return page_revData;} private Xow_wiki_revs page_revData = new Xow_wiki_revs();
	public boolean					Ref_nested()		{return ref_nested;} public Xop_ctx Ref_nested_(boolean v) {ref_nested = v; return this;} private boolean ref_nested;
	public Xot_defn_trace		Defn_trace()		{return defn_trace;} public Xop_ctx Defn_trace_(Xot_defn_trace v) {defn_trace = v; return this;} private Xot_defn_trace defn_trace = Xot_defn_trace_null._;
	public byte					Parse_tid()			{return parse_tid;} public Xop_ctx Parse_tid_(byte v) {parse_tid = v; return this;} private byte parse_tid = Xop_parser_.Parse_tid_null;
	public int					Cur_tkn_tid()		{return cur_tkn_tid;} public Xop_ctx Cur_tkn_tid_(int v) {cur_tkn_tid = v; return this;} private int cur_tkn_tid = Xop_tkn_itm_.Tid_null;
	public boolean					Lxr_make()			{return lxr_make;} public Xop_ctx Lxr_make_(boolean v) {lxr_make = v; return this;} private boolean lxr_make = false;
	public boolean Only_include_evaluate() {return only_include_evaluate;} public Xop_ctx Only_include_evaluate_(boolean v) {only_include_evaluate = v; return this;} private boolean only_include_evaluate;
	public Xtn_lst_section_mgr	Lst_section_mgr() {if (lst_section_mgr == null) lst_section_mgr = new Xtn_lst_section_mgr(); return lst_section_mgr;} Xtn_lst_section_mgr lst_section_mgr;
	public int Tag_idx;
	public Xop_ctx Clear() {
		page.Clear();
		ary = Xop_tkn_itm_.Ary_empty;
		aryLen = aryMax = 0;
		Tag_idx = 0;
		app.Wiki_mgr().Wdata_mgr().Clear();
		return this;
	}
	public void Page_bgn(Xop_root_tkn root, byte[] src) {
		this.root = root; this.Msg_log().Clear(); cur_tkn_tid = -1;
		this.src = src; src_len = src.length;
		empty_ignored = false;
		for (Xop_ctx_wkr wkr : wkrs) wkr.Page_bgn(this);
	}
	public void Page_end(byte[] src, int srcLen) {
		Stack_pop_til(0, true, srcLen, srcLen);
		for (Xop_ctx_wkr wkr : wkrs) wkr.Page_end(this, root, src, srcLen);
	}
	public int LxrMake_txt_(int pos)									{lxr_make = false; return pos;}
	public int LxrMake_log_(Gfo_msg_itm itm, int bgn_pos, int cur_pos)	{lxr_make = false; msg_log.Add_itm_none(itm, src, bgn_pos, cur_pos); return cur_pos;}
	public void StackTkn_add(Xop_tkn_itm sub) {
		root.Subs_add(sub);
		this.Stack_add(sub);
	}
	public boolean Empty_ignored() {return empty_ignored;}
	public void Empty_ignored_y_() {empty_ignored = true;} private boolean empty_ignored = false;
	public void Empty_ignored_n_() {empty_ignored = false;}
	public void Empty_ignore(int empty_bgn) {
		int empty_end = root.Subs_len();
		for (int i = empty_bgn; i < empty_end; i++) {
			Xop_tkn_itm sub_tkn = root.Subs_get(i);
			sub_tkn.Ignore_y_grp_(this, root, i);
		}
		empty_ignored = false;
	}
	public void Subs_add_and_stack(Xop_tkn_itm sub) {this.Subs_add(sub); this.Stack_add(sub);}
	public void Subs_add(Xop_tkn_itm sub) {
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
		int newLen = aryLen + 1;
		if (newLen > aryMax) {
			aryMax = newLen * 2;
			ary = (Xop_tkn_itm[])Array_.Resize(ary, aryMax);
		}
		ary[aryLen] = tkn;
		cur_tkn_tid = tkn.Tkn_tid();
		aryLen = newLen;
	}	private Xop_tkn_itm[] ary = Xop_tkn_itm_.Ary_empty; int aryLen = 0, aryMax = 0;
	public int Stack_len() {return aryLen;}
	public Xop_tkn_itm Stack_get(int i)		{return i < 0 || i >= aryLen ? null : ary[i];}
	public Xop_tblw_tkn Stack_get_tblw() {
		for (int i = aryLen - 1; i > -1	; i--) {
			Xop_tkn_itm tkn = ary[i];
			switch (tkn.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_tblw_tb:
				case Xop_tkn_itm_.Tid_tblw_tr:
				case Xop_tkn_itm_.Tid_tblw_td:
				case Xop_tkn_itm_.Tid_tblw_th:
				case Xop_tkn_itm_.Tid_tblw_tc:
					return (Xop_tblw_tkn)tkn;
			}
		}
		return null;
	}
	public static final int Stack_not_found = -1;
	public boolean Stack_has(int typeId) {return Stack_idx_typ(typeId) != Stack_not_found;}
	public int Stack_idx_typ(int typeId)	{
		for (int i = aryLen - 1; i > -1; i--)
			if (ary[i].Tkn_tid() == typeId)
				return i; 
		return Stack_not_found;
	}
	public int Stack_idx_typ_tblw(int typeId) { // NOTE: separate proc for xnde which may try to pop past a td/th/tc (and thus break a table unnecessarily)
		for (int i = aryLen - 1; i > -1	; i--) {
			int itm_typeId = ary[i].Tkn_tid();				
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
	public Xop_tkn_itm Stack_get_typ(int typeId) {
		for (int i = aryLen - 1; i > -1	; i--) {
			Xop_tkn_itm tkn = ary[i];
			if (tkn.Tkn_tid() == typeId) return tkn;
		}
		return null;
	}
	public void Stack_del(Xop_tkn_itm del) {
		if (aryLen == 0) return;
		for (int i = aryLen - 1; i > -1; i--) {
			Xop_tkn_itm tkn = ary[i];
			if (tkn == del) {
				for (int j = i + 1; j < aryLen; j++) {
					ary[j - 1] = ary[j];
				}
				--aryLen;
				break;
			}
		}
	}
	public Xop_tkn_itm Stack_pop_til(int tilIdx, boolean include, int bgnPos, int curPos) {
		if (aryLen == 0) return null;
		int minIdx = include ? tilIdx - 1 : tilIdx;
		if (minIdx < -1) minIdx = -1;
		Xop_tkn_itm rv = null;
		for (int i = aryLen - 1; i > minIdx; i--) {
			rv = ary[i];
			Stack_autoClose(rv, bgnPos, curPos);
		}
		Stack_pop_idx(tilIdx);
		return include ? rv : ary[aryLen]; // if include, return poppedTkn; if not, return tkn before poppedTkn
	}
	public Xop_tkn_itm Stack_pop_before(int tilIdx, boolean include, int bgnPos, int curPos) {	// used by Xop_tblw_lxr to detect \n| in lnki; seems useful as well
		if (aryLen == 0) return null;
		int minIdx = include ? tilIdx - 1 : tilIdx;
		if (minIdx < -1) minIdx = -1;
		Xop_tkn_itm rv = null;
		for (int i = aryLen - 1; i > minIdx; i--) {
			rv = ary[i];
			Stack_autoClose(rv, bgnPos, curPos);
		}
		return include ? rv : ary[aryLen]; // if include, return poppedTkn; if not, return tkn before poppedTkn
	}
	public void Stack_autoClose(Xop_tkn_itm tkn, int bgnPos, int curPos) {
		switch (tkn.Tkn_tid()) {
			case Xop_tkn_itm_.Tid_newLine: break;	// NOOP: just a marker
			case Xop_tkn_itm_.Tid_list: list.AutoClose(this, app.Tkn_mkr(), root, src, src_len, bgnPos, curPos, tkn); break;
			case Xop_tkn_itm_.Tid_xnde: xnde.AutoClose(this, src, src_len, bgnPos, curPos, tkn); break;
			case Xop_tkn_itm_.Tid_apos: apos.AutoClose(this, src, src_len, bgnPos, curPos, tkn); break;
			case Xop_tkn_itm_.Tid_lnke: lnke.AutoClose(this, src, src_len, bgnPos, curPos, tkn); break;
			case Xop_tkn_itm_.Tid_hdr:  hdr.AutoClose(this, app.Tkn_mkr(), root, src, src_len, bgnPos, curPos, tkn); break;
			case Xop_tkn_itm_.Tid_tblw_tb:
			case Xop_tkn_itm_.Tid_tblw_tr:
			case Xop_tkn_itm_.Tid_tblw_td:
			case Xop_tkn_itm_.Tid_tblw_th:
			case Xop_tkn_itm_.Tid_tblw_tc: tblw.AutoClose(this, src, src_len, bgnPos, curPos, tkn); break;
			case Xop_tkn_itm_.Tid_lnki: lnki.AutoClose(this, app.Tkn_mkr(), root, src, src_len, bgnPos, curPos, tkn); break;
			case Xop_tkn_itm_.Tid_pre: para.AutoClose(this, app.Tkn_mkr(), root, src, src_len, bgnPos, curPos, tkn); break;
		}
	}
	public void Stack_pop_idx(int tilIdx) {
		aryLen = tilIdx < 0 ? 0 : tilIdx;
		cur_tkn_tid = aryLen == 0 ? -1 : ary[aryLen - 1].Tkn_tid();
	}
	public void Stack_pop_lnki() {	// used primarily by lnke to remove lnke from stack
		--aryLen;
		cur_tkn_tid = aryLen == 0 ? -1 : ary[aryLen - 1].Tkn_tid();
	}
	public void CloseOpenItms(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {
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
		ctx.Stack_pop_til(stack_pos, true, bgnPos, curPos);
	}
	public static Xop_ctx new_(Xow_wiki wiki) {return new Xop_ctx(wiki);}
	public static Xop_ctx new_sub_(Xow_wiki wiki) {
		Xop_ctx rv = new Xop_ctx(wiki);
		rv.Page().Page_ttl_(wiki.Ctx().Page().Page_ttl());	// NOTE: sub_ctx must have same page_ttl as owner; see Xtn_lst_tst!Fullpagename
		rv.Tab_(wiki.Ctx().Tab());	// NOTE: tab should be same instance across all sub_ctxs; otherwise CallParserFunction will not set DISPLAYTITLE correctly; DATE:2013-08-05
		return rv;
	}
}

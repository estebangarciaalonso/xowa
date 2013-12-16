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
public class Xop_tblw_wkr implements Xop_ctx_wkr {
	public boolean Cell_pipe_seen() {return cell_pipe_seen;} public Xop_tblw_wkr Cell_pipe_seen_(boolean v) {cell_pipe_seen = v; return this;} private boolean cell_pipe_seen; // status of 1st cell pipe; EX: \n| a | b | c || -> flag pipe between a and b but ignore b and c
	public void Ctor_ctx(Xop_ctx ctx) {}
	public void Page_bgn(Xop_ctx ctx, Xop_root_tkn root) {cell_pipe_seen = false; tblw_tb_suppressed = 0;} private int tblw_tb_suppressed = 0;
	public void Page_end(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int src_len) {}
	public void AutoClose(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, Xop_tkn_itm tkn) {
		tkn.Subs_move(root);
		tkn.Src_end_(cur_pos);
	}
	public int MakeTkn_bgn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, byte wlxr_type, boolean ws_enabled, boolean tblw_xml, int atrs_bgn, int atrs_end) {// REF.MW: Parser|doTableStuff
		if (bgn_pos == Xop_parser_.Doc_bgn_bos) {
			bgn_pos = 0;	// do not allow -1 pos
			ctx.Para().Prv_para_disable(bgn_pos);
		}

		if (	ctx.Cur_tkn_tid() == Xop_tkn_itm_.Tid_list		// list is in effect
			&& !ws_enabled)										// only close list if tblw starts new line; EX: "* a {|" does not close list, but "* a\n{|" does
			Xop_list_wkr_.Close_list_if_present(ctx, root, src, bgn_pos, cur_pos);
		if (ctx.Apos().Stack_len() > 0)							// open apos; note that apos keeps its own stack, as they are not "structural" (not sure about this)
			ctx.Apos().EndFrame(ctx, root, src, cur_pos, true);		// close it

		Xop_tblw_tkn prv_tkn = ctx.Stack_get_tblw();
		if (prv_tkn == null) {									// prv_tkn not found; i.e.: no earlier "{|" or "<table>"
			switch (wlxr_type) {
				case Tblw_type_tb:								// "{|";
					break;										//		noop; by definition "{|" does not need to have a previous "{|"
				case Tblw_type_td:								// "|"
				case Tblw_type_td2:								// "||"
					if (tblw_xml) {
						ctx.Subs_add_and_stack_tblw(root, prv_tkn, tkn_mkr.Tblw_tb(bgn_pos, bgn_pos, tblw_xml));
						prv_tkn = tkn_mkr.Tblw_tr(bgn_pos, bgn_pos, tblw_xml);
						ctx.Subs_add_and_stack_tblw(root, prv_tkn, prv_tkn);
						break;
					}					
					else {
						if (ctx.Para().Pre_at_line_bgn())	// HACK:pre_section_begun_and_failed_tblw
							return ctx.Para().Hack_pre_and_false_tblw(ctx, root, src, bgn_pos);
						else								// interpret as text
							ctx.Subs_add(root, tkn_mkr.Pipe(bgn_pos, cur_pos));	//		create pipe_tkn; return;
						return cur_pos;
					}
				case Tblw_type_th:								// "!"
				case Tblw_type_th2:								// "!!"
				case Tblw_type_tc:								// "|+"
				case Tblw_type_tr:								// "|-"
					if (ctx.Para().Pre_at_line_bgn())	// HACK:pre_section_begun_and_failed_tblw
						return ctx.Para().Hack_pre_and_false_tblw(ctx, root, src, bgn_pos);
					else									// interpret as text
						return ctx.LxrMake_txt_(cur_pos);			//		create txt_tkn; return;
				case Tblw_type_te:								// "|}"
					if (tblw_tb_suppressed > 0) {
						--tblw_tb_suppressed;
						return cur_pos;
					}
					else {
						if (ctx.Para().Pre_at_line_bgn())	// HACK:pre_section_begun_and_failed_tblw
							return ctx.Para().Hack_pre_and_false_tblw(ctx, root, src, bgn_pos);
						else									// interpret as text
							return ctx.LxrMake_txt_(cur_pos);		//		create txt_tkn; return;
					}
				default: throw Err_.unhandled(wlxr_type);
			}
		}

		int prv_tid = prv_tkn == null ? Xop_tkn_itm_.Tid_null : prv_tkn.Tkn_tid();
		if (prv_tkn != null && !prv_tkn.Tblw_xml()) {			// note that this logic is same as Atrs_close; repeated here for "perf"
			switch (prv_tid) {
				case Xop_tkn_itm_.Tid_tblw_tb: case Xop_tkn_itm_.Tid_tblw_tr:
					Atrs_make(ctx, src, root, this, prv_tkn);
					break;
			}
		}
		if (wlxr_type == Tblw_type_te)
			return MakeTkn_end_tblw(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, Xop_tkn_itm_.Tid_tblw_te, wlxr_type, prv_tkn, prv_tid, tblw_xml);
		else
			return MakeTkn_bgn_tblw(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, wlxr_type, tblw_xml, atrs_bgn, atrs_end, prv_tkn, prv_tid, ws_enabled);
	}
	int Find_tblw_tb_end(byte[] src, int src_len, int cur_pos) {
		while (cur_pos < src_len) {
			switch (src[cur_pos++]) {
				case Byte_ascii.NewLine:
				case Byte_ascii.Lt:
					return cur_pos - 1;
				default:
					break;
			}
		}
		return cur_pos;
	}
	int MakeTkn_bgn_tblw(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, byte wlxr_type, boolean tblw_xml, int atrs_bgn, int atrs_end, Xop_tblw_tkn prv_tkn, int prv_tid, boolean ws_enabled) {
		if (wlxr_type != Tblw_type_tb)	// NOTE: do not ignore ws if {|; will cause strange behavior with pre; DATE:2013-02-12
			IgnoreWs(ctx, root);
		Xop_tblw_tkn new_tkn = null;
		switch (wlxr_type) {
			case Tblw_type_tb:								// <table>
				switch (prv_tid) {
					case Xop_tkn_itm_.Tid_null:				// noop; <table>
						break;
					case Xop_tkn_itm_.Tid_tblw_td:			// noop; <td><table>
					case Xop_tkn_itm_.Tid_tblw_th:			// noop; <th><table>
						break;
					case Xop_tkn_itm_.Tid_tblw_tb:			// fix;  <table><table>	    -> <table><tr><td><table>
						ctx.Subs_add(root, tkn_mkr.Ignore(bgn_pos, cur_pos, Xop_ignore_tkn.Ignore_tid_htmlTidy_tblw));
						++tblw_tb_suppressed;
						cur_pos = Xop_lxr_.FindFwdUntil(src, src_len, cur_pos, Byte_ascii.NewLine);	// NOTE: minor hack; this tblw tkn will be ignored, so ignore any of its attributes as well; gobble up all chars till nl. see:  if two consecutive tbs, ignore attributes on 2nd; en.wikibooks.org/wiki/Wikibooks:Featured books
						return cur_pos;
					case Xop_tkn_itm_.Tid_tblw_tr:			// fix:  <tr><table>        -> <tr><td><table>
						ctx.Subs_add_and_stack_tblw(root, prv_tkn, tkn_mkr.Tblw_td(bgn_pos, bgn_pos, tblw_xml));
						break;
					case Xop_tkn_itm_.Tid_tblw_tc:			// fix;  <caption><table>   -> <caption></caption><tr><td><table>
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tc), true, bgn_pos, bgn_pos);
						ctx.Subs_add_and_stack_tblw(root, prv_tkn, tkn_mkr.Tblw_tr(bgn_pos, bgn_pos, tblw_xml));
						ctx.Subs_add_and_stack_tblw(root, prv_tkn, tkn_mkr.Tblw_td(bgn_pos, bgn_pos, tblw_xml));
						break;
				}
				Xop_tblw_tb_tkn tb_tkn = tkn_mkr.Tblw_tb(bgn_pos, cur_pos, tblw_xml);
				new_tkn = tb_tkn;
				break;
			case Tblw_type_tr:								// <tr>
				switch (prv_tid) {
					case Xop_tkn_itm_.Tid_tblw_tb: break;	// noop; <table><tr>
					case Xop_tkn_itm_.Tid_tblw_tc:			// fix;  <caption><tr>      -> <caption></caption><tr>
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tc), true, bgn_pos, bgn_pos);
						break;
					case Xop_tkn_itm_.Tid_tblw_td:			// fix;  <td><tr>           -> <td></td></tr><tr>
					case Xop_tkn_itm_.Tid_tblw_th:			// fix;  <th><tr>           -> <th></th></tr><tr>
						ctx.Para().Process_nl(ctx, root, src, bgn_pos, bgn_pos + 1, false);	// DUBIOUS: starting tr; process para (which will create paras for cells) 2012-12-08
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tr), true, bgn_pos, bgn_pos);
						break;
					case Xop_tkn_itm_.Tid_tblw_tr:			// fix;  <tr><tr>           -> <tr>							
						if (prv_tkn.Tblw_subs_len() == 0) {	// NOTE: set prv_row to ignore, but do not pop; see Tr_dupe_xnde and [[Jupiter]]; only invoke if same type; EX: <tr><tr> but not |-<tr>; DATE:2013-12-09
							Xop_tkn_itm prv_row = ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tr), false, bgn_pos, bgn_pos);
							prv_row.Ignore_y_();
						}
						else
							ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tr), true, bgn_pos, bgn_pos);						
						//							}
//							else	// types are mixed; close previous <tr>; EX: |-<tr>; DATE:2013-12-09
						break;
				}					
				Xop_tblw_tr_tkn tr_tkn = tkn_mkr.Tblw_tr(bgn_pos, cur_pos, tblw_xml);
				new_tkn = tr_tkn;
				break;
			case Tblw_type_td:								// <td>
			case Tblw_type_td2:
				boolean create_th = false;
				switch (prv_tid) {
					case Xop_tkn_itm_.Tid_tblw_tr: break;	// noop; <tr><td>
					case Xop_tkn_itm_.Tid_tblw_td:			// fix;  <td><td>           -> <td></td><td>
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(prv_tid), true, bgn_pos, bgn_pos);
						break;
					case Xop_tkn_itm_.Tid_tblw_th:			// fix;  <th><td>           -> <th></th><td>
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(prv_tid), true, bgn_pos, bgn_pos);
						if (wlxr_type == Tblw_type_td2) create_th = true;	// !a||b -> <th><th>; but !a|b -> <th><td>
						break;
					case Xop_tkn_itm_.Tid_tblw_tb:			// fix;  <table><td>        -> <table><tr><td>
						if (wlxr_type == Tblw_type_td2) {	// NOTE: ignore || if preceded by {|; {|a||b\n
							prv_tkn.Atrs_rng_set(-1, -1);	// reset atrs_bgn; remainder of line will become part of tb atr
							return cur_pos;
						}
						else {
							new_tkn = tkn_mkr.Tblw_tr(bgn_pos, cur_pos, tblw_xml);
							ctx.Subs_add_and_stack_tblw(root, prv_tkn, new_tkn);
							prv_tid = new_tkn.Tkn_tid();
						}
						break;
					case Xop_tkn_itm_.Tid_tblw_tc:			// fix;  <caption><td>      -> <caption></caption><tr><td>
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tc), true, bgn_pos, bgn_pos);
						new_tkn = tkn_mkr.Tblw_tr(bgn_pos, cur_pos, tblw_xml);
						ctx.Subs_add_and_stack_tblw(root, prv_tkn, new_tkn);
						prv_tid = new_tkn.Tkn_tid();
						break;
				}
				if (create_th)	new_tkn = tkn_mkr.Tblw_th(bgn_pos, cur_pos, tblw_xml);
				else			new_tkn = tkn_mkr.Tblw_td(bgn_pos, cur_pos, tblw_xml);
				cell_pipe_seen = false;
				break;
			case Tblw_type_th:								// <th>
			case Tblw_type_th2:
				switch (prv_tid) {
					case Xop_tkn_itm_.Tid_tblw_tr: break;	// noop; <tr><th>
					case Xop_tkn_itm_.Tid_tblw_th:			// fix;  <th><th>           -> <th></th><th>
						if (tblw_xml																// tblw_xml always closes previous token
							|| (wlxr_type == Tblw_type_th2 || wlxr_type == Tblw_type_th))	// ! always closes; EX: "! !!"; "!! !!"; REMOVE: 2012-05-07; had (&& !ws_enabled) but caused "\n !" to fail; guard is no longer necessary since tblw_ws changed...
							ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(prv_tid), true, bgn_pos, bgn_pos);
						else {
							ctx.Subs_add(root, tkn_mkr.Txt(bgn_pos, cur_pos));
							return cur_pos;
						}
						break;
					case Xop_tkn_itm_.Tid_tblw_td:			// fix;  <td><th>           -> <td></td><th> NOTE: common use of using <th> after <td> for formatting
						if (tblw_xml												// tblw_xml always closes previous token
							|| (wlxr_type == Tblw_type_th && !ws_enabled))	// "| !" closes; "| !!" does not;
							ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(prv_tid), true, bgn_pos, bgn_pos);
						else {
							ctx.Subs_add(root, tkn_mkr.Txt(bgn_pos, cur_pos));
							return cur_pos;
						}
						break;
					case Xop_tkn_itm_.Tid_tblw_tb:			// fix;  <table><th>        -> <table><tr><th>
						ctx.Subs_add_and_stack_tblw(root, prv_tkn, tkn_mkr.Tblw_tr(bgn_pos, cur_pos, tblw_xml));
						break;
					case Xop_tkn_itm_.Tid_tblw_tc:			// fix;  <caption><th>      -> <caption></caption><tr><th>
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tc), true, bgn_pos, bgn_pos);
						ctx.Subs_add_and_stack_tblw(root, prv_tkn, tkn_mkr.Tblw_tr(bgn_pos, cur_pos, tblw_xml));
						break;
				}
				new_tkn = tkn_mkr.Tblw_th(bgn_pos, cur_pos, tblw_xml);
				cell_pipe_seen = false;
				break;					
			case Tblw_type_tc:								// <caption>
				switch (prv_tid) {
					case Xop_tkn_itm_.Tid_tblw_tb: break;	// noop; <table><caption>
					case Xop_tkn_itm_.Tid_tblw_tr:			// fix;  <tr><caption>      -> <tr></tr><caption>  TODO: caption should be ignored and placed in quarantine
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tr), true, bgn_pos, bgn_pos);
						break;
					case Xop_tkn_itm_.Tid_tblw_td:			// fix;  <td><caption>      -> <td></td><caption>
					case Xop_tkn_itm_.Tid_tblw_th:			// fix;  <th><caption>		-> <th></th><caption>
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tr), true, bgn_pos, bgn_pos);	// NOTE: closing <tr> in order to close <td>/<th>
						ctx.Msg_log().Add_itm_none(Xop_tblw_log.Caption_after_td, src, prv_tkn.Src_bgn(), bgn_pos);
						break;
					case Xop_tkn_itm_.Tid_tblw_tc:			// fix;  <caption><caption> -> <caption></caption><caption>
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tc), true, bgn_pos, bgn_pos);
						ctx.Msg_log().Add_itm_none(Xop_tblw_log.Caption_after_tc, src, prv_tkn.Src_bgn(), bgn_pos);
						break;
				}
				new_tkn = tkn_mkr.Tblw_tc(bgn_pos, cur_pos, tblw_xml);
				((Xop_tblw_tb_tkn)ctx.Stack_get_typ(Xop_tkn_itm_.Tid_tblw_tb)).Caption_count_add_1();
				cell_pipe_seen = false; // NOTE: always mark !seen; see Atrs_tc()
				break;
		}
		ctx.Subs_add_and_stack_tblw(root, prv_tkn, new_tkn);
		if (atrs_bgn > Xop_tblw_wkr.Atrs_ignore_check) {
			new_tkn.Atrs_rng_set(atrs_bgn, atrs_end);
			if (ctx.Parse_tid() == Xop_parser_.Parse_tid_page_wiki) {
				Xop_xatr_itm[] atrs = ctx.App().Xatr_parser().Parse(ctx.Msg_log(), src, atrs_bgn, atrs_end);
				new_tkn.Atrs_ary_as_tblw_(atrs);
			}
		}
		switch (wlxr_type) {
			case Tblw_type_tb:
			case Tblw_type_tr:
				if (ctx.Para().Cur_mode() == Xop_para_wkr.Mode_pre) {
					ctx.Para().Cur_mode_end_and_bgn(Xop_para_wkr.Mode_none);
				}
				ctx.Para().Process_xml_block(Xop_xnde_tag.Block_bgn, cur_pos);
				break;
			case Tblw_type_td:
			case Tblw_type_th:
				ctx.Para().Process_xml_block(Xop_xnde_tag.Block_end, cur_pos);
				break;
		}
		return cur_pos;
	}
	public int MakeTkn_end_tblw(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, int typeId, byte wlxr_type, Xop_tblw_tkn prv_tkn, int prv_tid, boolean tblw_xml) {
		if (!tblw_xml) {
			ctx.Para().Process_nl(ctx, root, src, bgn_pos, bgn_pos + 1, false);	// DUBIOUS: starting tr; process para (which will create paras for cells) 2012-12-08
//				ctx.Para().LastSection_process(bgn_pos + 1);
//				ctx.Para().Process_nl_sect_end(bgn_pos + 1);
		}
		IgnoreWs(ctx, root);
		if (wlxr_type == Tblw_type_te) {
			switch (prv_tid) {
				case Xop_tkn_itm_.Tid_tblw_td:			// fix;  <td></table>       -> <td></td></tr></table>
				case Xop_tkn_itm_.Tid_tblw_th:			// fix;  <th></table>       -> <th></th></tr></table>
					ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tr), true, bgn_pos, bgn_pos);
					break;
				case Xop_tkn_itm_.Tid_tblw_tc:			// fix;  <caption></table>  -> <caption></caption></table>
					ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tc), true, bgn_pos, bgn_pos);
					break;
				case Xop_tkn_itm_.Tid_tblw_tr:			// fix;  <tr></table>       -> </table>  : tr but no tds; remove tr
					if (prv_tkn.Tkn_sub_idx() + 1 == root.Subs_len()) {	// previous tkn was tr; no point creating empty <tr> so delete
						root.Subs_del_after(prv_tkn.Tkn_sub_idx());
					}
					break;
				case Xop_tkn_itm_.Tid_tblw_tb:			// fix;  <table></table>    -> <table><tr><td></td></tr></table>
					boolean has_subs = false;
					for (int i = prv_tkn.Tkn_sub_idx() + 1; i < root.Subs_len(); i++) {
						int cur_id = root.Subs_get(i).Tkn_tid();
						switch (cur_id) {
							case Xop_tkn_itm_.Tid_tblw_tc:
							case Xop_tkn_itm_.Tid_tblw_td:
							case Xop_tkn_itm_.Tid_tblw_th:
							case Xop_tkn_itm_.Tid_tblw_tr:
								has_subs = true;
								i = root.Subs_len();
								break;
						}
					}
					if (!has_subs) {
						Xop_tkn_itm new_tkn = tkn_mkr.Tblw_tr(bgn_pos, bgn_pos, tblw_xml);
						ctx.Subs_add_and_stack_tblw(root, prv_tkn, new_tkn);
						new_tkn = tkn_mkr.Tblw_td(bgn_pos, bgn_pos, tblw_xml);
						ctx.Subs_add_and_stack_tblw(root, prv_tkn, new_tkn);
						ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tb), true, bgn_pos, bgn_pos);
						return cur_pos;
					}
					break;
			}
			int tb_idx = ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tblw_tb); 
			if (tb_idx == -1) return cur_pos;	// NOTE: tb_idx can be -1 when called from Pipe in Tmpl mode
			Xop_tblw_tb_tkn tb = (Xop_tblw_tb_tkn)ctx.Stack_pop_til(root, src, tb_idx, false, bgn_pos, bgn_pos);	// NOTE: need to pop manually in order to set all intermediate node ends to bgn_pos, but tb ent to cur_pos; EX: for stack of "tb,tr,td" tr and td get End_() of bgn_pos but tb gets End_() of cur_pos
			tb.Subs_move(root);
			tb.Src_end_(cur_pos);
			ctx.Para().Process_xml_block(Xop_para_wkr.Block_mode_end, cur_pos);	// NOTE: must clear block state that was started by <tr>; code implicitly relies on td clearing block state, but no td was created
			return cur_pos;
		}
		int acs_typeId = typeId;
		if (prv_tid != typeId	// NOTE: special logic to handle auto-close of <td></th> or <th></td>
			&& (	(prv_tid == Xop_tkn_itm_.Tid_tblw_td && typeId == Xop_tkn_itm_.Tid_tblw_th)
				||	(prv_tid == Xop_tkn_itm_.Tid_tblw_th && typeId == Xop_tkn_itm_.Tid_tblw_td)
				)
			)
			acs_typeId = prv_tid;

		int acs_pos = -1, acs_len = ctx.Stack_len();
		for (int i = acs_len - 1; i > -1; i--) {						// find auto-close pos
			byte cur_acs_tid = ctx.Stack_get(i).Tkn_tid();
			switch (acs_typeId) {
				case Xop_tkn_itm_.Tid_tblw_tb:							// if </table>, match <table> only; note that it needs to be handled separately b/c of tb logic below
					if (acs_typeId == cur_acs_tid) {
						acs_pos = i;
						i = -1;	// force break;
					}
					break;
				default:												// if </t*>, match <t*> but stop at <table>; do not allow </t*> to close <t*> outside <table>
					if		(cur_acs_tid == Xop_tkn_itm_.Tid_tblw_tb)	// <table>; do not allow </t*> to close any <t*>'s above <table>; EX:w:Enthalpy_of_fusion; {{States of matter}}
						i = -1;											// this will skip acs_pos != -1 below and discard token
					else if (cur_acs_tid == acs_typeId) {				// </t*> matches <t*>
						acs_pos = i;
						i = -1; // force break
					}
					break;
			}
		}
		if (acs_pos != -1) {
			Xop_tblw_tkn bgn_tkn = (Xop_tblw_tkn)ctx.Stack_pop_til(root, src, acs_pos, false, bgn_pos, cur_pos);
			switch (wlxr_type) {
				case Tblw_type_tb:
					ctx.Para().Process_xml_block(Xop_xnde_tag.Block_end, cur_pos);
					break;
				case Tblw_type_td:
				case Tblw_type_th:
					ctx.Para().Process_xml_block(Xop_xnde_tag.Block_bgn, cur_pos);
					break;
			}
			bgn_tkn.Subs_move(root);
			bgn_tkn.Src_end_(cur_pos);
		}
		return cur_pos;
	}
	public static void Atrs_close(Xop_ctx ctx, byte[] src, Xop_root_tkn root) {
		Xop_tblw_tkn prv_tkn = ctx.Stack_get_tblw();
		if (prv_tkn == null || prv_tkn.Tblw_xml()) return;			// no tblw or tblw_xnde (which does not have tblw atrs)
		switch (prv_tkn.Tkn_tid()) {
			case Xop_tkn_itm_.Tid_tblw_tb: case Xop_tkn_itm_.Tid_tblw_tr:	// only tb and tr have tblw atrs (EX: "{|id=1\n"); td/th use pipes for atrs (EX: "|id=1|a"); tc has no atrs; te is never on stack
				Xop_tblw_wkr.Atrs_make(ctx, src, root, ctx.Tblw(), prv_tkn);
				break;
		}
	}
	public static boolean Atrs_make(Xop_ctx ctx, byte[] src, Xop_root_tkn root, Xop_tblw_wkr wkr, Xop_tblw_tkn prv_tblw) {
		if (prv_tblw.Atrs_bgn() != Xop_tblw_wkr.Atrs_null) return false; // atr_bgn/end is empty or has value; ignore;				
		int subs_bgn = prv_tblw.Tkn_sub_idx() + 1, subs_end = root.Subs_len() - 1;
		int subs_pos = subs_bgn;
		Xop_tkn_itm last_atr_tkn = null;
		boolean loop = true;
		while (loop) {											// loop over tkns after prv_tkn to find last_atr_tkn
			if (subs_pos > subs_end) break;
			Xop_tkn_itm tmp_tkn = root.Subs_get(subs_pos);
			switch (tmp_tkn.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_newLine:						// nl stops; EX: "{| a b c \nd"; bgn at {| and pick up " a b c " as atrs
				case Xop_tkn_itm_.Tid_hdr: case Xop_tkn_itm_.Tid_hr:	// hdr/hr incorporate nl into tkn so include these as well; EX: "{|a\n==b==" becomes tblw,txt,hdr (note that \n is part of hdr
					loop = false;
					break;
				default:
					++subs_pos;
					last_atr_tkn = tmp_tkn;
					break;
			}
		}
		if (last_atr_tkn == null) {								// no atrs found; mark tblw_tkn as Atrs_empty
			prv_tblw.Atrs_rng_set(Xop_tblw_wkr.Atrs_empty, Xop_tblw_wkr.Atrs_empty);
			return false;
		}
		root.Subs_del_between(ctx, subs_bgn, subs_pos);
		int atrs_bgn = prv_tblw.Src_end(), atrs_end = last_atr_tkn.Src_end();
		if (prv_tblw.Tkn_tid() == Xop_tkn_itm_.Tid_tblw_tr)	// NOTE: if "|-" gobble all trailing dashes; REF: Parser.php!doTableStuff; $line = preg_replace( '#^\|-+#', '', $line ); DATE:2013-06-21
			atrs_bgn = Xop_lxr_.Find_fwd_while(src, src.length, atrs_bgn, Byte_ascii.Dash);
		prv_tblw.Atrs_rng_set(atrs_bgn, atrs_end);
		if (ctx.Parse_tid() == Xop_parser_.Parse_tid_page_wiki && atrs_bgn != -1) {
			Xop_xatr_itm[] atrs = ctx.App().Xatr_parser().Parse(ctx.Msg_log(), src, atrs_bgn, atrs_end);
			prv_tblw.Atrs_ary_as_tblw_(atrs);
		}
		wkr.Cell_pipe_seen_(true);
		return true;
	}
	private void IgnoreWs(Xop_ctx ctx, Xop_root_tkn root) {
		int end = root.Subs_len() - 1;
		// get last tr, tc, tb; cannot use ctx.Stack_get_tblw b/c this gets last open tblw, and we want last tblw; EX: "<table><tr></tr>";  Stack_get_tblw gets <table> want </tr>
		boolean found = false;
		Xop_tkn_itm prv_tkn = null;
		for (int i = end; i > -1; i--) {
			prv_tkn = root.Subs_get(i);
			switch (prv_tkn.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_tblw_tr:
				case Xop_tkn_itm_.Tid_tblw_tc:
				case Xop_tkn_itm_.Tid_tblw_tb:
					found = true;
					i = -1;
					break;
				case Xop_tkn_itm_.Tid_tblw_td:	// exclude td
				case Xop_tkn_itm_.Tid_tblw_th:	// exclude th
					i = -1;
					break;
			}
		}
		if (!found) return;
		int bgn = prv_tkn.Tkn_sub_idx() + 1;
		int rv = IgnoreWs_rng(ctx, root, bgn, end, true);
		if (rv == -1) return; // entire range is ws; don't bother trimming end 
		IgnoreWs_rng(ctx, root, end, bgn, false);
	}
	int IgnoreWs_rng(Xop_ctx ctx, Xop_root_tkn root, int bgn, int end, boolean fwd) {
		int cur = bgn, adj = fwd ? 1 : -1;
		while (true) {			
			if (fwd) {
				if (cur > end) return -1;
			}
			else {
				if (cur < end) return -1;			
			}			
			Xop_tkn_itm ws_tkn = root.Subs_get(cur);
			switch (ws_tkn.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_space: case Xop_tkn_itm_.Tid_tab: case Xop_tkn_itm_.Tid_newLine:
				case Xop_tkn_itm_.Tid_para:
					ws_tkn.Ignore_y_grp_(ctx, root, cur);
					break;
				case Xop_tkn_itm_.Tid_xnde:
					if (ws_tkn.Src_bgn() == ws_tkn.Src_end()							// NOTE: para_wkr inserts <br/>. these should be disabled in IgnoreWs_rng; they are identified as having bgn == end; normal <br/>s will have bgn < end
						&& ((Xop_xnde_tkn)ws_tkn).Tag().Id() == Xop_xnde_tag_.Tid_br)
						ws_tkn.Ignore_y_grp_(ctx, root, cur);
					break;
				default:
					return cur;
			}
			cur += adj;
		}
	}
	public static final int Atrs_null = -1, Atrs_empty = -2, Atrs_ignore_check = -1;
	public static final byte Tblw_type_tb = 0, Tblw_type_te = 1, Tblw_type_tr = 2, Tblw_type_td = 3, Tblw_type_th = 4, Tblw_type_tc = 5, Tblw_type_td2 = 6, Tblw_type_th2 = 7;
}
/*
NOTE_1:
Code tries to emulate HTML tidy behavior. Specifically:
- ignore <table> when directly under <table>
- if tblw, scan to end of line to ignore attributes
- ignore any closing tblws
EX:
{|id=1
{|id=2		<- ignore id=2
|}
|}
*/
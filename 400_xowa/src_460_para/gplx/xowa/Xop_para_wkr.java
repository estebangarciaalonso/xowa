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
public class Xop_para_wkr implements Xop_ctx_wkr {
	public boolean Enabled() {return enabled;} public Xop_para_wkr Enabled_(boolean v) {enabled = v; return this;} private boolean enabled = true;
	public Xop_para_wkr Enabled_y_() {enabled = true; return this;} public Xop_para_wkr Enabled_n_() {enabled = false; return this;}		
	public void Ctor_ctx(Xop_ctx ctx) {}
	public void Page_bgn(Xop_ctx ctx, Xop_root_tkn root) {
		para_enabled = enabled && ctx.Parse_tid() == Xop_parser_.Parse_tid_page_wiki;
		block_mode = Block_mode_none;
		pre_at_line_bgn = false;
		if (para_enabled)
			Prv_para_new(ctx, root, 0);
	}	private boolean para_enabled;
	public void AutoClose(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, Xop_tkn_itm tkn) {
		if (((Xop_pre_tkn)tkn).Pre_enable() != Bool_.N_byte)
			ctx.Subs_add(root, tkn_mkr.Para_pre_end(bgn_pos, tkn));
	}
	public void Page_end(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int src_len) {
//			if (this.src == null) this.src = src;	// HACK: sometimes needed in home wiki when option page tag is not closed
		if (para_enabled) {
			if (block_mode != Block_mode_end)			// ignore Block_end b/c it will insert unneeded para_blank
				Process_nl(ctx, root, src, src_len, src_len, false);	// eos simulates a nl; needed to evaluate all text on line; EX: "a"
			this.Cur_mode_end();						// close anything created by Process_nl()
		}
		this.Clear();
	}
	private void Clear() {
		cur_mode_(Mode_none);
		para_stack = ParaStack_none;
		prv_nl_pos = -1;
		prv_para = null;
	}
	private int Close_list_if_not_category(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {// SEE:NOTE_4; EX.WP: SHA-2
		if (ctx.Cur_tkn_tid() != Xop_tkn_itm_.Tid_list) return -1;	
		int txt_pos = Xop_lxr_.Find_fwd_while_ws(src, src_len, cur_pos);
		if (ByteAry_.FindFwd(src, Xop_tkn_.Lnki_bgn, txt_pos, src_len) == txt_pos) {
			txt_pos += Xop_tkn_.Lnki_bgn.length;
			if (ByteAry_.FindFwd(src, ctx.Wiki().Ns_mgr().Ns_category().Name_db_w_colon(), txt_pos, src_len) == txt_pos)
				return -1;
		}
		ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_list), true, bgn_pos, cur_pos);
		if (txt_pos < src_len && src[txt_pos] == Byte_ascii.NewLine) {	// NOTE: handle \n\s between lists; DATE:2013-07-12
			Xop_list_wkr_.Close_list_if_present(ctx, root, src, bgn_pos, cur_pos);	// NOTE: above line only closes one list; should probably change to close all lists, but for now, close all lists only if "\n\s", not "\n"; DATE:2013-07-12
			return txt_pos; 
		}
		return -1;
	}	
	public int Process_pre(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, int txt_pos) {
		Dd_clear(ctx);
		int new_pos = Close_list_if_not_category(ctx, root, src, src_len, bgn_pos, cur_pos);
		if (new_pos != -1) {
			Process_nl(ctx, root, src, bgn_pos, new_pos, true, true);	// add blank line for truncated "\n\s"; DATE:2013-07-12
			return new_pos;								// must exit early; do not process pre
		}
		Object o = ctx.App().TblwWsTrie().MatchAtCur(src, txt_pos, src_len);
		if (o != null) {	// tblw_ws found
			Xop_tblw_ws_itm ws_itm = (Xop_tblw_ws_itm)o;
			byte tblw_type = ws_itm.Tblw_type();
			switch (tblw_type) {
				case Xop_tblw_ws_itm.Type_nl:	// \n\s\n
					if (cur_mode == Mode_pre)
						ctx.Subs_add(root, tkn_mkr.NewLine(bgn_pos, bgn_pos, Xop_nl_tkn.Tid_char, 1));
					else {
						Process_nl(ctx, root, src, bgn_pos, bgn_pos + 1, true);	// +1 = newLineLen
						ctx.Subs_add(root, tkn_mkr.Space(root, bgn_pos + 1, txt_pos));
						pre_at_line_bgn = true;		// NOTE: need to set pre_at_line_bgn
					}
					return txt_pos;
				case Xop_tblw_ws_itm.Type_xnde:
					if (cur_mode != Xop_para_wkr.Mode_none || para_stack != ParaStack_none)	// DUBIOUS: if there is a para, create another para for "\n\s"; handles "\n\s</td>" should be equivalent to "\n</td>"; note that without "if" empty para would be be created for " <table>"
						ctx.Para().Process_nl(ctx, root, src, bgn_pos, cur_pos, true);
					return ctx.Xnde().Make_tkn(ctx, tkn_mkr, root, src, src_len, txt_pos, txt_pos + 1);
			}
			return ctx.Tblw().MakeTkn_bgn(ctx, tkn_mkr, root, src, src_len, bgn_pos, txt_pos + ws_itm.Hook_len(), tblw_type, true, false, -1, -1);
		}
		if (block_mode == Block_mode_bgn) {	// block_mode cancels pre; REF.MW: Parser.php|doBlockLevels|$inBlockElem
			ctx.Subs_add(root, tkn_mkr.NewLine(bgn_pos, bgn_pos, Xop_nl_tkn.Tid_char, 1));
			ctx.Subs_add(root, tkn_mkr.Space(root, bgn_pos + 1, cur_pos));
			return cur_pos; 
		}
		int nl_end_pos = bgn_pos + 1;	// capture the pos after the \n
		switch (cur_mode) {				// NOTE: pre lxr emulates MW for "\n\s" by (1) calling Process nl for "\n"; (2) anticipating next line by setting pre_at_line_bgn
			case Mode_pre:
			case Mode_none:
			case Mode_para:													// EX: "\na\n b\n"; note that "\n " is cur
				if (bgn_pos != Xop_parser_.Doc_bgn_bos)						// if bos, then don't close 1st para
					Process_nl(ctx, root, src, bgn_pos, nl_end_pos, true, true);
				pre_at_line_bgn = true;										// NOTE: anticipates next line as pre
				break;
		}
		if (txt_pos - cur_pos > 0)	// capture ws also
			ctx.Subs_add(root, tkn_mkr.Space(root, cur_pos, txt_pos));
		return txt_pos;
	}
	public void Process_nl(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int bgn_pos, int cur_pos, boolean nl_char) {
		Process_nl(ctx, root, src, bgn_pos, cur_pos, nl_char, false);
	}
	public void Process_nl(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int bgn_pos, int cur_pos, boolean nl_char, boolean called_from_pre) {	// REF.MW:Parser.php|doBlockLevels
		Dd_clear(ctx);
		/*	TODO:
			$output .= $this->closeLastSection();
			if ( $preOpenMatch and !$preCloseMatch ) {
				$this->mInPre = true;
			}
		*/
		switch (block_mode) {
			case Block_mode_bgn:
				para_stack = ParaStack_none;	// per MW; DATE:2013-01-20
				prv_nl_pos = bgn_pos; 
				if (nl_char) ctx.Subs_add(root, ctx.Tkn_mkr().NewLine(bgn_pos, cur_pos, Xop_nl_tkn.Tid_char, 1));
				return;
			case Block_mode_end:
				para_stack = ParaStack_none;	// per MW; DATE:2013-01-20
				prv_nl_pos = bgn_pos; 
				block_mode = Block_mode_none;	// roughly $inBlockElem = !$closematch;
				Prv_para_new(ctx, root, cur_pos);
				return;
		}
		boolean trim = PseudoTrim(src, bgn_pos);
		if (pre_at_line_bgn && (cur_mode == Mode_pre || !trim)) {	// per MW; DATE:2013-02-12
			if (cur_mode != Mode_pre) {								// per MW; DATE:2013-02-12
				para_stack = ParaStack_none;
				this.Cur_mode_end_and_bgn(Xop_para_tkn.Para_typeId_pre);
				cur_mode_(Mode_pre);
			}
			pre_at_line_bgn = false;
		}
		else {
			if (bgn_pos - prv_nl_pos == 1 || trim) {		// line is blank ("b" for blank)
				if (para_stack != ParaStack_none) {			//		"b1"; stack has "<p>" or "</p><p>"; output "<br/>";
					ParaStack_end(cur_pos); ctx.Subs_add(root, ctx.Tkn_mkr().Xnde(bgn_pos, bgn_pos).Tag_(Xop_xnde_tag_.Tag_br)); 
					cur_mode_(Mode_para);
				}
				else {										//		stack is empty
					if (cur_mode != Mode_para) {			//			"b2"; cur is '' or <pre>
						Cur_mode_end_and_bgn(Xop_para_tkn.Para_typeId_none);
						para_stack = ParaStack_bgn;			//				put <p> on stack 
					}
					else									//			"b3"; cur is p
						para_stack = ParaStack_mid;			//				put </p><p> on stack
				}
			}
			else {											// line has text ("t" for text); NOTE: tkn already added before \n, so must change prv_para; EX: "a\n" -> this code is called for "\n" but "a" already processed
				if (para_stack != ParaStack_none) {			//		"t1"
					ParaStack_end(cur_pos);
					cur_mode_(Mode_para);
				}
				else if (cur_mode != Mode_para) {			//		"t2"; cur is '' or <pre>
					Cur_mode_end_and_bgn(Xop_para_tkn.Para_typeId_para);
					cur_mode_(Mode_para);
				}
				else {}										//		"t3"
			}
			pre_at_line_bgn = false; // NOTE: disable pre since line is either text or blank
		}
		if (para_stack == ParaStack_none) {					// "x1"
			ctx.Subs_add(root, ctx.Tkn_mkr().NewLine(bgn_pos, cur_pos, Xop_nl_tkn.Tid_char , 1));
		}
		Prv_para_new(ctx, root, cur_pos);
		prv_nl_pos = bgn_pos;
	}
	public void Process_lnki_category(Xop_ctx ctx, Xop_root_tkn root, int pos) {	// REF.MW:Parser.php|replaceInternalLinks2|Strip the whitespace Category links produce; 
		if (!para_enabled) return;
		boolean para_init = true, nl_init = true;
		int subs_len = root.Subs_len();
		for (int i = subs_len - 2; i > -1; i--) {	// -2: -1 for last; -1 to skip current lnki
			Xop_tkn_itm sub_tkn = root.Subs_get(i);
			switch (sub_tkn.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_para:		// 1st para: remove end </p>; 2nd para; mark current prv_para
					Xop_para_tkn p = (Xop_para_tkn)sub_tkn;
					if (para_init) {
						p.Para_end_(Xop_para_tkn.Para_typeId_none);
						para_init = false;						
					}
					else {
						prv_para.Para_bgn_(Xop_para_tkn.Para_typeId_none);
						prv_para = p;
						Pre_disable();	// disable pre (categories will never be put inside pre)
						return;
					}
					break;
				case Xop_tkn_itm_.Tid_newLine:	// nl found; ignore
					sub_tkn.Ignore_y_grp_(ctx, root, i);
					sub_tkn.Ignore_y_();
					nl_init = false;
					break;
				default:						// exit if nl not found
					if (nl_init) {				// indicates category is not preceded by \n; EX: "\na[[Category:B]]" (vs. "\n[[Category:B]]");
						i = -1;
						if (pre_at_line_bgn) {	// SEE:NOTE_3
							Pre_disable();
							prv_para.Para_bgn_(Xop_para_tkn.Para_typeId_none);

							para_stack = ParaStack_none;
							cur_mode_(Mode_none);
							prv_nl_pos = pos + 1;	// SEE:NOTE_2
						}
						return;	
					}
					break;
			}
		}
	}
	public void Process_lnki_file_div(Xop_ctx ctx, Xop_root_tkn root, int bgn_lhs, int end_lhs) {	// bgn_lhs is pos of [[; end_lhs is pos of ]]
		if (!para_enabled) return;
		Dd_clear(ctx);
		block_mode = Block_mode_bgn;
		if (cur_mode == Mode_pre) {						// pre is in effect; DUBIOUS: not sure if 2nd else should be handled elsewhere; alternative would be to eval at [[ instead of ]]
			if (bgn_lhs - 1 == prv_nl_pos)							//   lnki started at bgn of line; EX: \n\sa\n[[File:A.png]]
				this.Cur_mode_end();					//     close pre
			else													//   lnki started at pre; EX: \n\s[[File:A.png]]
				Pre_disable();										//     cancel pre
		}
		this.Cur_mode_end();
		para_stack = ParaStack_none;
		Prv_para_new(ctx, root, end_lhs);
		block_mode = Block_mode_end;
	}
	public void Process_xml_block(byte mode, int pos) {	// REF.MW:Parser.php|doBlockLevels|if ( $openmatch or $closematch ) {
		switch (mode) {
			case Xop_xnde_tag.Block_noop:	return;
			case Xop_xnde_tag.Block_bgn:
				block_mode = Block_mode_bgn;
				if (cur_mode == Mode_para)	// NOTE: this emulates MW and $this->closeLastSection(); however, xowa does pre logic differently, so exclude it
					Cur_mode_end_and_bgn(Xop_para_tkn.Para_typeId_none);
				break;
			case Xop_xnde_tag.Block_end:
				if (cur_mode == Mode_para)	// NOTE: this emulates MW and $this->closeLastSection(); however, xowa does pre logic differently, so exclude it
					Cur_mode_end_and_bgn(Xop_para_tkn.Para_typeId_none);
				block_mode = Block_mode_end;
				break;
		}
		para_stack = ParaStack_none;
		Pre_disable();
	}	
	public void Process_nl_sect_bgn(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int bgn_pos, int cur_pos, int blockType) {
		if (!para_enabled) return;
		Dd_clear(ctx);
		Process_nl(ctx, root, src, bgn_pos, bgn_pos + 1, false);	// NOTE: called by \n== and \n*; nl is at rng of bgn_pos to bgn_pos + 1 (not cur_pos)
		this.Cur_mode_end();						// MW: NOTE that cur_mode = Mode_none
		para_stack = ParaStack_none;				// MW: clear stack
		cur_mode_(Mode_none);
		prv_nl_pos = bgn_pos;			// update nl_pos
	}
	public void Process_nl_sect_end(Xop_ctx ctx, int pos) { // NOTE: hdrs/lists have no <p>s to close
		if (!para_enabled) return;
		Dd_clear(ctx);
		para_stack = ParaStack_none;
		cur_mode_(Mode_none);
		prv_nl_pos = pos;
		Process_xml_block(Xop_xnde_tag.Block_end, pos);
	}
	public void Process_hdr_end(Xop_ctx ctx, Xop_root_tkn root, int pos) {
		if (!para_enabled) return;
		Dd_clear(ctx);
		para_stack = ParaStack_none;
		cur_mode_(Mode_none);
		prv_nl_pos = pos;
		Prv_para_new(ctx, root, pos);
	}
	public void Prv_para_new(Xop_ctx ctx, Xop_root_tkn root, int pos) {
		prv_para = ctx.Tkn_mkr().Para(pos, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none);
		ctx.Subs_add(root, prv_para);
	}
	public void Cur_mode_end() {
		switch (cur_mode) {
			case Mode_none:	return;
			case Mode_pre:	prv_para.Para_end_(Xop_para_tkn.Para_typeId_pre); break;
			case Mode_para: prv_para.Para_end_(Xop_para_tkn.Para_typeId_para); break;
		}
		cur_mode_(Mode_none);
	}
	public void Cur_mode_end_and_bgn(byte bgn) {
		Cur_mode_end();
		if (prv_para != null) prv_para.Para_bgn_(bgn);	// NOTE: need (prv_para != null) check for <BOS>"==a=="
	}
	public static final byte Block_mode_none = 0, Block_mode_bgn = 1, Block_mode_end = 2;
	public byte Block_mode() {return block_mode;} public Xop_para_wkr Block_mode_(byte v) {block_mode = v; return this;} private byte block_mode = Block_mode_none;
	public void Prv_para_disable(int pos) {
		if (prv_para == null) return;
		prv_para.Ignore_y_();
		para_stack = ParaStack_none;
		cur_mode = Mode_none;
	}
	public void Prv_para_x_pre(int pos) {
		if (prv_para == null) return;
//			para_stack = ParaStack_none;
//			cur_mode = Mode_para;
		pre_at_line_bgn = false;
		block_mode = Block_mode_none;
		prv_para.Para_bgn_(Xop_para_tkn.Para_typeId_none);
		prv_para.Para_end_(Xop_para_tkn.Para_typeId_none);
	}
	private void ParaStack_end(int cur_pos) {
		switch (para_stack) {
			case ParaStack_none:	break;
			case ParaStack_bgn:		prv_para.Para_end_(Xop_para_tkn.Para_typeId_none).Para_bgn_(Xop_para_tkn.Para_typeId_para); break;
			case ParaStack_mid:		prv_para.Para_end_(Xop_para_tkn.Para_typeId_para).Para_bgn_(Xop_para_tkn.Para_typeId_para); break;
		}
		para_stack = ParaStack_none;
		cur_mode_(Mode_para);
	}
	private boolean PseudoTrim(byte[] src, int pos) {
		if (prv_nl_pos == -1) return false;
		boolean ws = true;
		for (int i = prv_nl_pos + 1; i < pos; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Tab:
				case Byte_ascii.Space:
					break;
				default:
					ws = false;
					i = pos;
					break;
			}
		}
		return ws;
	}
	public void Pre_disable() {
		if (cur_mode == Mode_pre || pre_at_line_bgn) {
			if (prv_para != null) prv_para.Para_bgn_(Xop_para_tkn.Para_typeId_space);	// NOTE:2012-11-27: handles text like <td>a\n\sb</td> where \n\s should not be pre, but \s should not be discarded (else "a" and "b" become one word); EX:w:Vaccuum tube
			cur_mode_(Mode_none);
			pre_at_line_bgn = false;
		}
	}
	public void Prv_nl_pos_(int v) {prv_nl_pos = v;}
	public boolean Pre_at_line_bgn() {return pre_at_line_bgn;} private boolean pre_at_line_bgn = false;
	private void Dd_clear(Xop_ctx ctx) {ctx.List().Dd_chk_(false);}
	private int prv_nl_pos = -1;
	private Xop_para_tkn prv_para = null;
	public byte Cur_mode() {return cur_mode;}
	byte cur_mode = Mode_none; int para_stack = ParaStack_none;
	private void cur_mode_(byte v) {cur_mode = v;}
	static final int 
		  ParaStack_none = 0	// false
		, ParaStack_bgn  = 1	// <p>
		, ParaStack_mid  = 2	// </p><p>
		;
	public static final byte
		  Mode_none = 0	// ''
		, Mode_para = 1	// p
		, Mode_pre  = 2	// pre
		;
	public int Hack_pre_and_false_tblw(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int nl_pos) {// DATE:2013-04-08
		/* HACK: handle the following
			a
			 b
			|
			c
		. the "\n" in "\n|" is intercepted by the tblw
		. the "\n|" is confirmed to be an invalid tblw, but the "\n" needs to be processed correctly
		*/
		Process_nl(ctx, root, src, nl_pos, nl_pos, false, false);													// (1) marks prv_para.bgn as <pre> b/c of pre_at_line_bgn; (2) also create a new blank para (which is fortunately after "b")
		prv_para.Para_end_(Xop_para_tkn.Para_typeId_pre).Para_bgn_(Xop_para_tkn.Para_typeId_para);	// manually set new para to </pre><p>
		prv_nl_pos = nl_pos;																		// set prv_nl_pos to pos of "\n" (else "this-line-is-blank" code is triggered)
		return nl_pos + 1;																			// -1 to position before pipe (so that pipe lxr can pick it up)
	}
}
/*
NOTE_1:
xowa uses \n as the leading character for multi-character hooks; EX: "\n*","\n{|","\n==",etc..
For this section of code, xowa treats \n separately from the rest of the hook for the purpose of emulating MW code.
EX: a\n==b==
MW:
- split into two lines: "a", "==b=="
- call process_nl on "a"
- call process_nl on "==b=="
XO:
- split into "tkns": "a", "\n==", "b", "=="
- add "a"
- add "\n=="
	- since there is a "\n", call process_nl, which will effectively call it for "a"
- note that page_end will effectively call process_nl on "==b=="

NOTE_2: Category needs to "trim" previous line
EX:
* a
* b
  [[Category:c]]
* d

MW does the following: (REF.MW:Parser.php|replaceInternalLinks2|Strip the whitespace Category links produce;)
- removes the \n after b (REF: $s = rtrim( $s . "\n" ); # bug 87)
- trims all space " " in front of [[ (NOTE: this makes it a non-pre line)
- plucks out the [[Category:c]]
- joins everything after ]] (starting with the \n) to the * b (REF: $s .= trim( $prefix . $trail, "\n" ) == '' ? '': $prefix . $trail;)
This effectively "blanks" out the entire line "\n  [[Category:c]]" -> ""

XOWA tries to emulate this by doing the following
- mark the para_tkn after \b as blank
- disable pre for the line
- keep the [[Category:c]], but *simulate* a blank line by moving the prv_nl_pos to after the ]]

NOTE_3: if (last_section_is_pre)
PURPOSE: if Category trims previous nl, but nl was part of pre, deactivate it
REASON: occurs b/c MW does separate passes for pre and Category while XO does one pass.
EX: "a\n [[Category:c]]"
- pre is activated by \n\s
- [[Category:c]] indicates that \n\s should be trimmed
  so, disable_pre, etc.

NOTE_4: close_list_if_not_category
PURPOSE: \n should ordinarily close list. However, if \n[[Category:A]], then don't close list since [[Category:A]] will trim preceding \n
REASON: occurs b/c MW does separate passes for list and Category while XO does one pass.

EX: closes *a list
*a

*b

EX: does not close
*a
[[Category:A]]
*b
*/
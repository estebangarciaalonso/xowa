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
package gplx.xowa.parsers.paras; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import gplx.core.btries.*; import gplx.xowa.parsers.lists.*; import gplx.xowa.parsers.tblws.*;
public class Xop_pre_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_pre;}
	public void Init_by_wiki(Xow_wiki wiki, Btrie_fast_mgr core_trie) {core_trie.Add(Hook_space, this);}	// NOTE: do not treat \n\t as shorthand pre; EX:pl.w:Main_Page; DATE:2014-05-06
	public void Init_by_lang(Xol_lang lang, Btrie_fast_mgr core_trie) {}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		if (!ctx.Para().Enabled()) {					// para disabled; "\n\s" should just be "\n\s"; NOTE: para disabled in <gallery>
			if (bgn_pos != Xop_parser_.Doc_bgn_bos)		// don't add \n if BOS; EX: "<BOS> a" should be " ", not "\n "
				ctx.Subs_add(root, tkn_mkr.NewLine(bgn_pos, bgn_pos + 1, Xop_nl_tkn.Tid_char, 1));
			ctx.Subs_add(root, tkn_mkr.Space(root, cur_pos - 1, cur_pos));
			return cur_pos;
		}
		int txt_pos = Bry_finder.Find_fwd_while(src, cur_pos, src_len, Byte_ascii.Space);	// NOTE: was Find_fwd_while_tab_or_space, which incorrectly converted tabs to spaces; PAGE:en.w:Cascading_Style_Sheets; DATE:2014-06-23
		if (txt_pos == src_len) return cur_pos;			// "\n\s" at EOS; treat as \n only; EX: "a\n   " -> ""; also bounds check
		byte b = src[txt_pos];
		if (bgn_pos == Xop_parser_.Doc_bgn_bos) {		// BOS; gobble up all \s\t; EX: "BOS\s\s\sa" -> "BOSa"
			if		(b == Byte_ascii.NewLine) {			// next char is nl
				cur_pos = txt_pos;						// position at nl; NOTE: do not position after nl, else may break hdr, tblw, list, etc; EX: "\s\n{|" needs to preserve "\n" for tblw
				ctx.Subs_add(root, tkn_mkr.Ignore(bgn_pos, cur_pos, Xop_ignore_tkn.Ignore_tid_pre_at_bos));
				return cur_pos;							// ignore pre if blank line at bos; EX: "BOS\s\s\n" -> "BOS\n"
			}
			if (b == Byte_ascii.Lt)							// next char is <; possible xnde; flag so that xnde can escape; DATE:2013-11-28; moved outside Doc_bgn_bos block above; PAGE:en.w:Comment_(computer_programming); DATE:2014-06-23
				ctx.Xnde().Pre_at_bos_(true);
		}
		switch (ctx.Cur_tkn_tid()) {					// close tblw attrs; NOTE: after BOS (since no tblw at BOS) but before "\n !" check
			case Xop_tkn_itm_.Tid_tblw_tb: case Xop_tkn_itm_.Tid_tblw_tr: case Xop_tkn_itm_.Tid_tblw_th:
				Xop_tblw_wkr.Atrs_close(ctx, src, root, Bool_.N);
				break;
			case Xop_tkn_itm_.Tid_list:
				if (Close_list(ctx, root, src, src_len, bgn_pos, cur_pos, txt_pos)) {
					// ctx.Para().Process_nl(ctx, root, src, bgn_pos, new_pos, true);	// add blank line for truncated "\n\s"; DATE:2013-07-12; DELETE: DATE:2014-02-18; doesn't seem necessary; doesn't break tests; devised for www.mediawiki.org/wiki/MediaWiki which loads fine
					return txt_pos;														// must exit early; do not process pre
				}
				break;
		}
		switch (b) {									// handle "\n !" which can be tbl
			case Byte_ascii.Bang:
				switch (ctx.Cur_tkn_tid()) {
					case Xop_tkn_itm_.Tid_tblw_tb:
					case Xop_tkn_itm_.Tid_tblw_tc:
					case Xop_tkn_itm_.Tid_tblw_tr:
					case Xop_tkn_itm_.Tid_tblw_th:
					case Xop_tkn_itm_.Tid_tblw_td:
					case Xop_tkn_itm_.Tid_tblw_te:
						int new_cur_pos = txt_pos + 1; // +1 to skip Byte_ascii.Bang
						Xop_tblw_lxr_ws.Make(ctx, tkn_mkr, root, src, src_len, bgn_pos, new_cur_pos, Xop_tblw_wkr.Tblw_type_th, true);
						return new_cur_pos;
				}
				break;
		}
		return ctx.Para().Process_pre(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, txt_pos);
	}
	private static boolean Close_list(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, int txt_pos) {// SEE:NOTE_4; EX.en.w:SHA-2
		if (Bry_finder.Find_fwd(src, Xop_tkn_.Lnki_bgn, txt_pos, src_len) == txt_pos) {		// look for "[["
			txt_pos += Xop_tkn_.Lnki_bgn.length;
			if (Bry_finder.Find_fwd(src, ctx.Wiki().Ns_mgr().Ns_category().Name_db_w_colon(), txt_pos, src_len) == txt_pos)	// look for "Category:"
				return false;														// "[[Category:" found; "\n\s[[Category:" should not close list; note that [[Category]] is invisible
		}
		ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_list), true, bgn_pos, cur_pos, Xop_tkn_itm_.Tid_pre);	// "* a\n\sb" found; close *a
		if (	txt_pos < src_len									// bounds check
			&&	src[txt_pos] == Byte_ascii.NewLine) {				// NOTE: handle "*a\n\s\n" between lists; DATE:2013-07-12
			Xop_list_wkr_.Close_list_if_present(ctx, root, src, bgn_pos, cur_pos);	// NOTE: above line only closes one list; should probably change to close all lists, but for now, close all lists only if "\n\s", not "\n"; DATE:2013-07-12
			return true; 
		}
		return false;
	}	
	public static final Xop_pre_lxr _ = new Xop_pre_lxr(); Xop_pre_lxr() {}
	private static final byte[]
	  Hook_space	= new byte[] {Byte_ascii.NewLine, Byte_ascii.Space}
	;
}
/*
NOTE_4: Close_list
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
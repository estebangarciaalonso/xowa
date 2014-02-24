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
class Xop_under_lxr implements Xop_lxr {
	private ByteTrieMgr_slim under_words_cs, under_words_ci;
	public byte Lxr_tid() {return Xop_lxr_.Tid_under;}
	public void Init_by_wiki(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {}
	public void Init_by_lang(Xol_lang lang, ByteTrieMgr_fast core_trie) {
		Xol_kwd_mgr kwd_mgr = lang.Kwd_mgr();
		int under_kwds_len = under_kwds.length;
		Xop_under_lxr lxr = new Xop_under_lxr();
		lxr.under_words_cs = ByteTrieMgr_slim.cs_();
		lxr.under_words_ci = ByteTrieMgr_slim.ci_();
		core_trie.Add(Hook, lxr);
		for (int i = 0; i < under_kwds_len; i++) {
			int kwd_id = under_kwds[i];
			Xol_kwd_grp kwd_grp = kwd_mgr.Get_or_new(kwd_id);
			Xol_kwd_itm[] kwd_itms = kwd_grp.Itms(); if (kwd_itms == null) continue;
			int kwd_itms_len = kwd_itms.length;
			boolean kwd_case_match = kwd_grp.Case_match();
			for (int j = 0; j < kwd_itms_len; j++) {
				Xol_kwd_itm kwd_itm = kwd_itms[j];
				byte[] kwd_bry = kwd_itm.Bry();
				if (ByteAry_.HasAtBgn(kwd_bry, Hook)) {	// kwd starts with __; EX: __NOTOC__
					ByteTrieMgr_slim under_words = kwd_case_match ? lxr.under_words_cs : lxr.under_words_ci;						
					under_words.Add(ByteAry_.Mid(kwd_bry, Hook_len, kwd_bry.length), IntVal.new_(kwd_id));
				}
				else {									// kwd doesn't start with __; no known examples, but just in case; EX: "NOTOC"; DATE:2014-02-14
					Xop_word_lxr word_lxr = new Xop_word_lxr(kwd_id);
					if (kwd_case_match)					// cs; add word directly to trie
						core_trie.Add(kwd_bry, word_lxr);
					else {								// NOTE: next part is imprecise; XOWA parser is cs, but kwd is ci; for now, just add all upper and all lower
						lang.App().Usr_dlg().Warn_many("", "", "under keyword does not start with __; id=~{0} word=~{1}", kwd_id, String_.new_utf8_(kwd_bry));
						core_trie.Add(lang.Case_mgr().Case_build_lower(kwd_bry), word_lxr);
						core_trie.Add(lang.Case_mgr().Case_build_upper(kwd_bry), word_lxr);	
					}
				}
			}
		}
	}
	private static final int[] under_kwds = new int[] // REF.MW:MagicWord.php
	{ Xol_kwd_grp_.Id_toc, Xol_kwd_grp_.Id_notoc, Xol_kwd_grp_.Id_forcetoc
	, Xol_kwd_grp_.Id_nogallery, Xol_kwd_grp_.Id_noheader, Xol_kwd_grp_.Id_noeditsection
	, Xol_kwd_grp_.Id_notitleconvert, Xol_kwd_grp_.Id_nocontentconvert, Xol_kwd_grp_.Id_newsectionlink, Xol_kwd_grp_.Id_nonewsectionlink
	, Xol_kwd_grp_.Id_hiddencat, Xol_kwd_grp_.Id_index, Xol_kwd_grp_.Id_noindex, Xol_kwd_grp_.Id_staticredirect
	, Xol_kwd_grp_.Id_disambig
	};
	private static final byte[] Hook = new byte[] {Byte_ascii.Underline, Byte_ascii.Underline};
	private static final int Hook_len = Hook.length;
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		if (cur_pos == src_len) return ctx.LxrMake_txt_(cur_pos);			// eos
		int rv = cur_pos;
		Object o = under_words_cs.MatchAtCur(src, cur_pos, src_len);		// check cs
		if (o == null) {
			o = under_words_ci.MatchAtCur(src, cur_pos, src_len);			// check ci
			if (o != null)
				rv = under_words_ci.Match_pos();
		}
		else
			rv = under_words_cs.Match_pos();
		if (o == null) return ctx.LxrMake_txt_(cur_pos);					// kwd not found; EX: "TOCA__"
		int kwd_id = ((IntVal)(o)).Val();
		Xop_under_lxr.Make_tkn(ctx, tkn_mkr, root, src, src_len, bgn_pos, rv, kwd_id);

		int skip = 0;
		for (int i = rv; i < src_len; i++) {// trim all ws from end; EX: "__TOC__ " -> "" (gobble up last ws)
			switch (src[i]) {
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
					++skip;
					break;
				case Byte_ascii.NewLine:
					rv += skip;
					i = src_len;
					break;
				default:
					i = src_len;
					break;
			}
		}
		return rv;
	}
	public static void Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, int kwd_id) {
		Xoa_page page = ctx.Page();
		Xop_hdr_mgr hdr_mgr = page.Hdr_mgr();
		switch (kwd_id) {
			case Xol_kwd_grp_.Id_toc:				hdr_mgr.Toc_manual_(); ctx.Subs_add(root, tkn_mkr.Under(bgn_pos, cur_pos, kwd_id)); break;	// NOTE: only save under_tkn for TOC (b/c its position is needed for insertion); DATE:2013-07-01
			case Xol_kwd_grp_.Id_forcetoc:			hdr_mgr.Toc_force_(); break;
			case Xol_kwd_grp_.Id_notoc:				hdr_mgr.Toc_hide_(); break;
			case Xol_kwd_grp_.Id_noeditsection:		break;	// ignore; not handling edit sections
			case Xol_kwd_grp_.Id_nocontentconvert:	page.Lang_convert_content_(false); break;
			case Xol_kwd_grp_.Id_notitleconvert:	page.Lang_convert_title_(false); break;
			default:								break;	// ignore anything else
		}				
	}
        public static final Xop_under_lxr _ = new Xop_under_lxr(); Xop_under_lxr() {}
}
class Xop_word_lxr implements Xop_lxr {
	private int kwd_id;
	public Xop_word_lxr(int kwd_id) {this.kwd_id = kwd_id;}
	public byte Lxr_tid() {return Xop_lxr_.Tid_word;}
	public void Init_by_wiki(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {}
	public void Init_by_lang(Xol_lang lang, ByteTrieMgr_fast core_trie) {}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		Xop_under_lxr.Make_tkn(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, kwd_id);	// for now, all word_lxrs only call the under_lxr; DATE:2014-02-14
		return cur_pos;
	}
}

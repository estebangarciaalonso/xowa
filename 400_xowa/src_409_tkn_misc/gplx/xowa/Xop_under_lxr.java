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
	private Hash_adp_bry under_words;
	public byte Lxr_tid() {return Xop_lxr_.Tid_under;}
	public void Init_by_wiki(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {}
	public void Init_by_lang(Xol_lang lang, ByteTrieMgr_fast core_trie) {
		Xol_kwd_mgr kwd_mgr = lang.Kwd_mgr();
		int under_kwds_len = under_kwds.length;
		Xop_under_lxr lxr = new Xop_under_lxr();
		lxr.under_words = new Hash_adp_bry(false);
		for (int i = 0; i < under_kwds_len; i++) {
			int kwd_id = under_kwds[i];
			Xol_kwd_grp kwd_grp = kwd_mgr.Get_or_new(kwd_id);
			Xol_kwd_itm[] kwd_itms = kwd_grp.Itms(); if (kwd_itms == null) continue;
			int kwd_itms_len = kwd_itms.length;
			for (int j = 0; j < kwd_itms_len; j++) {
				Xol_kwd_itm kwd_itm = kwd_itms[j];
				byte[] kwd_bry = kwd_itm.Bry();
				core_trie.Add(kwd_bry, lxr);
				lxr.under_words.Add(kwd_bry, IntVal.new_(kwd_id));
			}
		}
	}
	private static final int[] under_kwds = new int[] // REF.MW:MagicWord.php
	{	Xol_kwd_grp_.Id_toc, Xol_kwd_grp_.Id_notoc, Xol_kwd_grp_.Id_forcetoc, Xol_kwd_grp_.Id_nogallery, Xol_kwd_grp_.Id_noheader, Xol_kwd_grp_.Id_noeditsection
	,	Xol_kwd_grp_.Id_notitleconvert, Xol_kwd_grp_.Id_nocontentconvert, Xol_kwd_grp_.Id_newsectionlink, Xol_kwd_grp_.Id_nonewsectionlink
	,	Xol_kwd_grp_.Id_hiddencat, Xol_kwd_grp_.Id_index, Xol_kwd_grp_.Id_noindex, Xol_kwd_grp_.Id_staticredirect
	,	Xol_kwd_grp_.Id_disambig
	};
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		Object o = under_words.Get_by_mid(src, bgn_pos, cur_pos);
		if (o != null) {
			IntVal id_val = (IntVal)o;
			Xoa_page page = ctx.Page();
			switch (id_val.Val()) {
				case Xol_kwd_grp_.Id_toc:				page.TocFlag_toc_y_(); ctx.Subs_add(root, tkn_mkr.Under(bgn_pos, cur_pos, id_val.Val())); break;	// NOTE: only save under_tkn for TOC (b/c its position is needed for insertion); DATE:2013-07-01
				case Xol_kwd_grp_.Id_forcetoc:			page.TocFlag_toc_force_y_(); break;
				case Xol_kwd_grp_.Id_notoc:				page.TocFlag_toc_no_y_(); break;
				case Xol_kwd_grp_.Id_noeditsection:		break;	// ignore; not handling edit sections
				default:								break;	// ignore anything else
			}				
			return cur_pos;
		}
		return ctx.LxrMake_txt_(cur_pos);
	}
        public static final Xop_under_lxr _ = new Xop_under_lxr(); Xop_under_lxr() {}
}

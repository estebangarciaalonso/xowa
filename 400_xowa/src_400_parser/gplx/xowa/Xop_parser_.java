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
public class Xop_parser_ {
	public static final byte Parse_tid_null = 0, Parse_tid_tmpl = 1, Parse_tid_page_tmpl = 2, Parse_tid_page_wiki = 3;
	public static final int Doc_bgn_bos = -1, Doc_bgn_char_0 = 0;
	private static ByteTrieMgr_fast tmpl_trie, anchorencode_trie, poem_trie;
	public static byte[] Parse_fragment(Xow_wiki wiki, byte[] bry) {
		Xop_ctx inner_ctx = Xop_ctx.new_sub_(wiki);
		Xop_root_tkn inner_root = inner_ctx.Tkn_mkr().Root(bry);
		return wiki.Parser().Parse_page_tmpl(inner_root, inner_ctx, inner_ctx.Tkn_mkr(), bry);
	}
	public static ByteTrieMgr_fast Tmpl_trie(Xow_wiki wiki) {
		if (tmpl_trie != null) return tmpl_trie;
		tmpl_trie = ByteTrieMgr_fast.cs_();
		Ctor_trie_lxr(wiki, tmpl_trie
			, Xop_pipe_lxr._, new Xop_eq_lxr(true), Xop_colon_lxr._, Xop_space_lxr._, Xop_nbsp_lxr._, Xop_tab_lxr._, Xop_nl_lxr.Bldr
			, Xop_curly_bgn_lxr._, Xop_curly_end_lxr._
			, Xop_brack_bgn_lxr._, Xop_brack_end_lxr._
			, Xop_comm_lxr._
			, Xop_xnde_lxr._	// needed for xtn, noinclude, etc.
			, Xop_under_lxr.Bldr
			, gplx.xowa.xtns.translates.Xop_tvar_lxr._
			);
		return tmpl_trie;
	}
	public static ByteTrieMgr_fast Main_trie(Xow_wiki wiki) {
//			if (main_trie != null) return main_trie;
		ByteTrieMgr_fast rv = ByteTrieMgr_fast.cs_();
		Ctor_trie_lxr(wiki, rv
			, Xop_pipe_lxr._, new Xop_eq_lxr(false), Xop_space_lxr._, Xop_nbsp_lxr._, Xop_tab_lxr._, Xop_nl_lxr.Bldr
			, Xop_amp_lxr._, Xop_apos_lxr._, Xop_colon_lxr._
			, Xop_lnki_lxr_bgn._, Xop_lnki_lxr_end._
			, Xop_list_lxr._
			, Xop_hdr_lxr._
			, Xop_hr_lxr._
			, Xop_xnde_lxr._
			, Xop_lnke_lxr._, Xop_lnke_end_lxr._
			, Xop_tblw_lxr.Bldr //, Xop_tblw_lxr_ws.Bldr
			, Xop_pre_lxr.Bldr
			, Xop_comm_lxr._
			, Xop_variant_lxr.Bldr
			, Xop_under_lxr.Bldr
			);
		return rv;
	}
	public static ByteTrieMgr_fast Anchorencode_trie(Xow_wiki wiki) {
		if (anchorencode_trie != null) return anchorencode_trie;
		anchorencode_trie = ByteTrieMgr_fast.cs_();
		Ctor_trie_lxr(wiki, anchorencode_trie
			, Xop_pipe_lxr._, new Xop_eq_lxr(false), Xop_space_lxr._, Xop_tab_lxr._, Xop_nl_lxr.Bldr
			, Xop_amp_lxr._, Xop_colon_lxr._
			, Xop_apos_lxr._
			, Xop_lnki_lxr_bgn._, Xop_lnki_lxr_end._
			, Xop_lnke_lxr._, Xop_lnke_end_lxr._
			, Xop_xnde_lxr._
			);
		return anchorencode_trie;
	}
	public static ByteTrieMgr_fast Poem_trie(Xow_wiki wiki) {
		if (poem_trie != null) return poem_trie;
		poem_trie = ByteTrieMgr_fast.cs_();
		Ctor_trie_lxr(wiki, poem_trie
			, Xop_pipe_lxr._, new Xop_eq_lxr(false), Xop_space_lxr._, Xop_tab_lxr._
			, Xop_amp_lxr._, Xop_apos_lxr._, Xop_colon_lxr._
			, Xop_lnki_lxr_bgn._, Xop_lnki_lxr_end._
			, Xop_list_lxr._
			, Xop_hdr_lxr._
			, Xop_hr_lxr._
			, Xop_xnde_lxr._
			, Xop_lnke_lxr._, Xop_lnke_end_lxr._
			, Xop_tblw_lxr.Bldr //, Xop_tblw_lxr_ws.Bldr
			, Xop_nl_lxr.Poem
			, gplx.xowa.xtns.poems.Xtn_poem_lxr.Bldr
			);
		return poem_trie;
	}
	private static void Ctor_trie_lxr(Xow_wiki wiki, ByteTrieMgr_fast trie, Xop_lxr... ary) {for (Xop_lxr lxr : ary) lxr.Ctor_lxr(wiki, trie);}
}

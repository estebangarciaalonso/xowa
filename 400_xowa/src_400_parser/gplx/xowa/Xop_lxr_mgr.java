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
import gplx.core.btries.*;
import gplx.xowa.parsers.apos.*; import gplx.xowa.parsers.amps.*; import gplx.xowa.parsers.lnkes.*; import gplx.xowa.parsers.hdrs.*; import gplx.xowa.parsers.lists.*; import gplx.xowa.parsers.tblws.*; import gplx.xowa.parsers.paras.*;
public class Xop_lxr_mgr {
	private Xop_lxr[] ary;
	public Xop_lxr_mgr(Xop_lxr[] ary) {this.ary = ary;}
	public Btrie_fast_mgr Trie() {return trie;} private Btrie_fast_mgr trie = Btrie_fast_mgr.cs_();
	public void Init_by_wiki(Xow_wiki wiki) {
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			Xop_lxr lxr = ary[i];
			lxr.Init_by_wiki(wiki, trie);
		}
	}
	public void Init_by_lang(Xol_lang lang) {
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			Xop_lxr lxr = ary[i];
			lxr.Init_by_lang(lang, trie);
		}
	}
	public static Xop_lxr_mgr new_tmpl_() {
		return new Xop_lxr_mgr(new Xop_lxr[] 
		{ Xop_pipe_lxr._, new Xop_eq_lxr(true), Xop_colon_lxr._, Xop_space_lxr._, Xop_tab_lxr._, Xop_nl_lxr._
		, Xop_curly_bgn_lxr._, Xop_curly_end_lxr._
		, Xop_brack_bgn_lxr._, Xop_brack_end_lxr._
		, Xop_comm_lxr._
		, Xop_xnde_lxr._	// needed for xtn, noinclude, etc.
		, Xop_under_lxr._
		, gplx.xowa.xtns.translates.Xop_tvar_lxr._
		, Xop_cr_lxr._		// always ignore \r; DATE:2014-03-02
		});
	}
	public static Xop_lxr_mgr new_wiki_() {
		return new Xop_lxr_mgr(new Xop_lxr[] 
		{ Xop_pipe_lxr._, new Xop_eq_lxr(false), Xop_space_lxr._, Xop_tab_lxr._, Xop_nl_lxr._
		, Xop_amp_lxr._, Xop_apos_lxr._, Xop_colon_lxr._
		, Xop_lnki_lxr_bgn._, Xop_lnki_lxr_end._
		, Xop_list_lxr._
		, Xop_hdr_lxr._
		, Xop_hr_lxr._
		, Xop_xnde_lxr._
		, Xop_lnke_lxr._, Xop_lnke_end_lxr._
		, Xop_tblw_lxr._
		, Xop_pre_lxr._, Xop_nl_tab_lxr._
		, Xop_comm_lxr._
		, Xop_under_lxr._
		});
	}
	public static final Xop_lxr_mgr Popup_lxr_mgr	// same as orig_page, except apos_lxr added
		= new Xop_lxr_mgr(new Xop_lxr[] 
		{ Xop_pipe_lxr._, new Xop_eq_lxr(true), Xop_colon_lxr._, Xop_space_lxr._, Xop_tab_lxr._, Xop_nl_lxr._
		, Xop_curly_bgn_lxr._, Xop_curly_end_lxr._
		, Xop_brack_bgn_lxr._, Xop_brack_end_lxr._
		, Xop_comm_lxr._
		, Xop_xnde_lxr._	// needed for xtn, noinclude, etc.
		, Xop_under_lxr._
		, gplx.xowa.xtns.translates.Xop_tvar_lxr._
		, Xop_cr_lxr._		// always ignore \r; DATE:2014-03-02
		, gplx.xowa.parsers.apos.Xop_apos_lxr._	// needed else multiple apos may be split across blocks;
		});
}

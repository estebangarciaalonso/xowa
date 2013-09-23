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
public class Xtn_lst extends Pf_func_base {
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {
		Xow_wiki wiki = ctx.Wiki();
		byte[] src_ttl_bry = Eval_argx(ctx, src, caller, self); if (ByteAry_.Len_eq_0(src_ttl_bry)) return;		// {{#lst:}} -> ""
		int args_len = self.Args_len(); if (args_len == 0) return;												// {{#lst:page}} -> ""
		byte[] lst_bgn = Pf_func_.EvalArgOr(ctx, src, caller, self, args_len, 0, null); if (ByteAry_.Len_eq_0(lst_bgn)) return;	// {{#lst:page|}} -> ""
		byte[] lst_end = Pf_func_.EvalArgOr(ctx, src, caller, self, args_len, 1, null);
		if (ctx.Wiki().View_data().Lst_recursed()) return;
		ctx.Wiki().View_data().Lst_recursed_(true);
		Xoa_ttl src_ttl = Xoa_ttl.parse_(wiki, src_ttl_bry); if (src_ttl == null) return;						// {{#lst:<>}} -> ""
		Xoa_page src_page = wiki.Data_mgr().Get_page(src_ttl, false); if (src_page.Missing()) return;	// {{#lst:missing}} -> ""
		Xop_parser src_parser = Xop_parser.new_(wiki);
		Xop_ctx src_ctx = Xop_ctx.new_sub_(wiki);
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_m001();
		Xot_defn_tmpl defn_tmpl = src_parser.Parse_tmpl(src_ctx, src_ctx.Tkn_mkr(), src_ttl.Ns(), src_ttl_bry, src_page.Data_raw());	// NOTE: parse as tmpl to ignore <noinclude>
		defn_tmpl.Tmpl_evaluate(src_ctx, caller, tmp_bfr);
		byte[] src_page_bry = tmp_bfr.XtoAryAndClear();
		Xop_root_tkn src_root = src_parser.Parse_recurse(src_ctx, src_ctx, src_page_bry, true);	// NOTE: pass src_ctx as old_ctx b/c entire document will be parsed, and references outside the section should be ignored;
		src_page_bry = src_root.Data_mid();	// NOTE: must override src_page_bry with result of parse; else <nowiki> will break text; DATE:2013-07-11
		Xtn_lst_section_mgr section_mgr = src_ctx.Lst_section_mgr();	// get section_mgr from Parse
		int sections_len = section_mgr.Count();
		byte[] bgn_key = lst_bgn, end_key = lst_bgn;
		if (!ByteAry_.Len_eq_0(lst_end)) end_key = lst_end;
		int bgn_pos = -1; boolean bgn_found = false;
		for (int i = 0; i < sections_len; i++) {
			Xtn_section_nde section = section_mgr.Get_at(i);
			byte section_tid = section.Name_tid();
			byte[] section_key = section.Section_name();
			if		(section_tid == Xtn_section_nde.Xatr_begin && ByteAry_.Eq(section_key, bgn_key) && !bgn_found) {	// NOTE: !bgn_found to prevent "resetting" of dupe; EX: <s begin=key0/>a<s begin=key0/>b; should start from a not b
				bgn_pos = section.Xnde().Tag_close_end();
				bgn_found = true;
			}
			else if (section_tid == Xtn_section_nde.Xatr_end   && ByteAry_.Eq(section_key, end_key) && bgn_found) {
				tmp_bfr.Add_mid(src_page_bry, bgn_pos, section.Xnde().Tag_open_bgn());
				bgn_found = false;
			}
		}
		if (bgn_found)	// bgn_found, but no end; write to end of page
			tmp_bfr.Add_mid(src_page_bry, bgn_pos, src_page_bry.length);
		bfr.Add_bfr_and_clear(tmp_bfr);
		tmp_bfr.Mkr_rls();
		ctx.Wiki().View_data().Lst_recursed_(false);
	}
	@Override public int Id() {return Xol_kwd_grp_.Id_lst;}
	@Override public Pf_func New(int id, byte[] name) {return new Xtn_lst().Name_(name);}
	public static final Xtn_lst _ = new Xtn_lst(); Xtn_lst() {}
	static final String GRP_KEY = "xowa.parser.xtn.lst";
	public static Hash_adp_bry new_xatrs_(Xol_lang lang) {
		Hash_adp_bry rv = new Hash_adp_bry(false);
		rv.Add_str_byteVal("name", Xtn_section_nde.Xatr_name);
		Xatrs_add(rv, "begin", "end");
		switch (lang.Lang_id()) {	// NOTE: as of v315572b, i18n is done directly in code, not in magic.php; am wary of adding keywords for general words like begin/end, so adding them manually per language; DATE:2013-02-09
			case Xol_lang_itm_.Id_de: Xatrs_add(rv, "Anfang", "Ende"); break;
			case Xol_lang_itm_.Id_he: Xatrs_add(rv, "התחלה", "סוף"); break;
			case Xol_lang_itm_.Id_pt: Xatrs_add(rv, "começo", "fim"); break;
		}
		return rv;
	}
	private static void Xatrs_add(Hash_adp_bry hash, String key_begin, String key_end) {
		hash.Add_str_byteVal(key_begin	, Xtn_section_nde.Xatr_begin);
		hash.Add_str_byteVal(key_end	, Xtn_section_nde.Xatr_end);
	}
}

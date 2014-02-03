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
package gplx.xowa.xtns.poems; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Poem_nde implements Xop_xnde_xtn {
	public Xop_root_tkn Xtn_root() {return body;} private Xop_root_tkn body;
	public boolean Xtn_literal() {return false;}
	public void Xtn_compile(Xow_wiki wiki, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {// REF.MW: Poem.php|wfRenderPoemTag
		int itm_bgn = xnde.Tag_open_end(), itm_end = xnde.Tag_close_bgn();
		if (itm_bgn == src.length)	return;  // NOTE: handle inline where there is no content to parse; EX: <poem/>
		if (itm_bgn >= itm_end)		return;  // NOTE: handle inline where there is no content to parse; EX: a<poem/>b
		if (src[itm_bgn] 		== Byte_ascii.NewLine)	++itm_bgn;	// ignore 1st \n; 
		if (src[itm_end - 1] 	== Byte_ascii.NewLine				// ignore last \n; 
			&& itm_end != itm_bgn)						--itm_end;	// ...if not same as 1st \n; EX: <poem>\n</poem>
		body = wiki.Xtn_mgr().Xtn_poem().Parser().Parse_recurse(ctx, ByteAry_.Mid(src, itm_bgn, itm_end), true); // NOTE: ignoring paragraph mode; technically MW enables para mode, but by replacing "\n" with "<br/>\n" all the logic with para/pre mode is skipped

//			Xop_ctx sub_ctx = Xop_ctx.new_sub_(wiki);
//			Xot_defn_tmpl defn_tmpl = wiki.Parser().Parse_tmpl(sub_ctx, sub_ctx.Tkn_mkr(), src_ttl.Ns(), src_ttl_bry, src_page.Data_raw());	// NOTE: parse as tmpl to ignore <noinclude>
//			ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_m001();
//			defn_tmpl.Tmpl_evaluate(sub_ctx, Xot_invk_temp.PageIsCaller, tmp_bfr);
//			byte[] src = tmp_bfr.Mkr_rls().XtoAryAndClear();
//			body = wiki.Parser().Parse_recurse(sub_ctx, sub_ctx, src, true);	// NOTE: pass sub_ctx as old_ctx b/c entire document will be parsed, and references outside the section should be ignored;
	}
}

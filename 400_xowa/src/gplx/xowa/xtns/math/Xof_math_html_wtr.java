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
package gplx.xowa.xtns.math; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xof_math_html_wtr {
	private Xof_math_itm tmp_math_itm = new Xof_math_itm();
	private ByteAryFmtr math_fmtr_latex		= ByteAryFmtr.new_("<img id='xowa_math_img_~{math_idx}' src='' width='' height=''/><span id='xowa_math_txt_~{math_idx}'>~{math_text}</span>", "math_idx", "math_text");
	private ByteAryFmtr math_fmtr_mathjax	= ByteAryFmtr.new_("<span id='xowa_math_txt_~{math_idx}'>~{math_text}</span>", "math_idx", "math_text");
	public void Write(Xoh_html_wtr wtr, Xop_ctx ctx, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		Xoa_app app = ctx.App(); Xow_wiki wiki = ctx.Wiki(); Xoa_page page = ctx.Page();
		byte[] math_bry = ByteAry_.Mid(src, xnde.Tag_open_end(), xnde.Tag_close_bgn());
		byte[] math_bry_clean = app.Utl_js_cleaner().Clean(wiki, math_bry, 0, math_bry.length);	// check for js; 
		if (math_bry_clean != null) math_bry = math_bry_clean;	// js found; use clean version; DATE:2013-08-26
		boolean enabled = app.File_mgr().Math_mgr().Enabled();
		boolean renderer_is_latex = !app.File_mgr().Math_mgr().Renderer_is_mathjax();
		if (renderer_is_latex && app.File_mgr().Math_mgr().Find_itm(tmp_math_itm, page.Wiki().Key_str(), math_bry)) {
			bfr.Add(Xoh_consts.Img_bgn);
			bfr.Add_str(tmp_math_itm.Png_url().To_http_file_str());
			bfr.Add(Xoh_consts.__inline_quote);
		}
		else {
			int id = page.File_math().Count();
			Xof_math_itm new_math_itm = tmp_math_itm.Clone().Id_(id);
			ByteAryFmtr math_fmtr = renderer_is_latex ? math_fmtr_latex : math_fmtr_mathjax;
			ByteAryBfr tmp_bfr = app.Utl_bry_bfr_mkr().Get_b512().Mkr_rls();
			math_fmtr.Bld_bfr_many(tmp_bfr, id, math_bry);
			bfr.Add_bfr_and_clear(tmp_bfr);
			if (enabled && renderer_is_latex)	// NOTE: only generate images if math is enabled; otherwise "downloading" prints at bottom of screen, but no action (also a lot of file IO)
				page.File_math().Add(new_math_itm);
		}
	}
}

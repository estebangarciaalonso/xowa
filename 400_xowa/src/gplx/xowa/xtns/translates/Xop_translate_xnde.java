/*
XOWA: the extensible offline wiki application
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
package gplx.xowa.xtns.translates; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xop_translate_xnde implements Xop_xnde_xtn, Xop_xnde_atr_parser {
	public boolean Xtn_literal() {return false;}
	public Xop_root_tkn Xtn_root() {return root_tkn;} private Xop_root_tkn root_tkn;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_obj) {
		if (xatr_obj == null) return;
	}
	public void Xtn_compile(Xop_ctx ctx, Xow_wiki wiki, Xop_tkn_mkr tkn_mkr, byte[] src, Xop_xnde_tkn xnde) {
		byte[] translate_src = ByteAry_.Mid(src, xnde.Tag_open_end(), xnde.Tag_close_bgn());
		translate_src = ByteAry_.Trim(translate_src, 0, translate_src.length);
		Xop_ctx sub_ctx = Xop_ctx.new_sub_(wiki);
		root_tkn = wiki.Parser().Parse_recurse(sub_ctx, translate_src, true);
	}
	public static void To_html(Xoh_html_wtr wtr, Xop_ctx ctx, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		Xop_translate_xnde translate_nde = (Xop_translate_xnde)xnde.Xnde_data();
		Xop_root_tkn root_tkn = translate_nde.Xtn_root();
		wtr.Write_tkn(opts, bfr, root_tkn.Root_src(), depth + 1, xnde, Xoh_html_wtr.Sub_idx_null, root_tkn);
	}
}

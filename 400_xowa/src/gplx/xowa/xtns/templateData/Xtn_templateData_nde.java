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
package gplx.xowa.xtns.templateData; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xtn_templateData_nde implements Xop_xnde_xtn, Xop_xnde_atr_parser {
	public boolean Xtn_literal() {return false;}
	public Xop_root_tkn Xtn_root() {return null;}
	public Xop_xnde_tkn Xnde() {return xnde;} private Xop_xnde_tkn xnde;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_obj) {
	}
	public void Xtn_compile(Xow_wiki wiki, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		this.xnde = xnde;
	}
	public static void To_html(Xoh_html_wtr wtr, Xop_ctx ctx, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		bfr.Add(Xoh_consts.Pre_bgn_overflow);
		Xoh_html_wtr.Bfr_escape(bfr, src, xnde.Tag_open_end(), xnde.Tag_close_bgn(), ctx.App(), true, false);
		bfr.Add(Xoh_consts.Pre_end);
	}
}

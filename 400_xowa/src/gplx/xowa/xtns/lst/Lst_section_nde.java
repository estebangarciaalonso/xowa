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
public class Lst_section_nde implements Xop_xnde_xtn, Xop_xnde_atr_parser {
	public boolean Xtn_literal() {return false;}
	public byte[] Section_name() {return section_name;} private byte[] section_name;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_obj) {
		if (xatr_obj == null) return;
		byte xatr_tid = ((ByteVal)xatr_obj).Val();
		switch (xatr_tid) {
			case Xatr_name: case Xatr_bgn: case Xatr_end:
				section_name = xatr.Val_as_bry(src); name_tid = xatr_tid; break;
		}
	}
	public Xop_root_tkn Xtn_root() {return null;}
	public Xop_xnde_tkn Xnde() {return xnde;} private Xop_xnde_tkn xnde;
	public byte Name_tid() {return name_tid;} private byte name_tid;
	public void Xtn_compile(Xow_wiki wiki, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		Xoa_app app = ctx.App();
		Xop_xatr_itm[] atrs = Xop_xatr_itm.Xatr_parse(app, this, wiki.Lang().Xatrs_section(), wiki, src, xnde);
		this.xnde = xnde;
		xnde.Atrs_ary_(atrs);
		ctx.Lst_section_mgr().Add(this);
	}
	public static final byte Xatr_name = 0, Xatr_bgn = 1, Xatr_end = 2;
}
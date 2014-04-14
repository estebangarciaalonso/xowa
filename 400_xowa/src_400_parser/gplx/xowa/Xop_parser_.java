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
import gplx.xowa.langs.vnts.*;
public class Xop_parser_ {
	public static final byte Parse_tid_null = 0, Parse_tid_tmpl = 1, Parse_tid_page_tmpl = 2, Parse_tid_page_wiki = 3;
	public static final int Doc_bgn_bos = -1, Doc_bgn_char_0 = 0;
	public static byte[] Parse_fragment(Xow_wiki wiki, byte[] bry) {
		Xop_ctx sub_ctx = Xop_ctx.new_sub_(wiki);
		Xop_root_tkn sub_root = sub_ctx.Tkn_mkr().Root(bry);
		return wiki.Parser().Parse_page_tmpl(sub_root, sub_ctx, sub_ctx.Tkn_mkr(), bry);
	}
	public static void Parse_to_html(ByteAryBfr trg, Xow_wiki wiki, Xoa_page page, boolean para_enabled, byte[] bry) {
		Xop_ctx ctx = Xop_ctx.new_sub_(wiki, page);
		Xop_root_tkn root = new Xop_root_tkn();
		byte[] mid = wiki.Parser().Parse_page_tmpl(root, ctx, ctx.Tkn_mkr(), bry);
		root.Reset();
		ctx.Para().Enabled_(para_enabled);
		wiki.Parser().Parse_page_wiki(root, ctx, ctx.Tkn_mkr(), mid, Xop_parser_.Doc_bgn_bos);
		wiki.Html_wtr().Write_all(trg, ctx, mid, root);
	}
}

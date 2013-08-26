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
package gplx.xowa.xtns.poems; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xtn_poem_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_poem;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Hook_ary, this);} static final byte[] Hook_ary = new byte[] {Byte_ascii.NewLine, Byte_ascii.Space};
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {
		int space_count = 1;
		while (curPos < srcLen) {
			if (src[curPos++] == Byte_ascii.Space) {
				++space_count;
			}
			else {
				--curPos;
				break;
			}
		}
		if (bgnPos != Xop_parser_.Doc_bgn_bos) {	// do not add xnde/nl if \n is BOS \n; EX.WP: Teresa of Ávila; "<poem>\n\s\s"
			ctx.Subs_add(tkn_mkr.Xnde(curPos, curPos).Tag_(Xop_xnde_tag_.Tag_br));
			ctx.Subs_add(tkn_mkr.NewLine(curPos, curPos, Xop_nl_tkn.Tid_char, 1));
		}
		for (int i = 0; i < space_count; i++)
			ctx.Subs_add(tkn_mkr.HtmlNcr(bgnPos + 1, curPos, 160, Nbsp_bry));
		return curPos;
	}
	private static final byte[] Nbsp_bry = gplx.intl.Utf8_.EncodeCharAsAry(160);
	public static final Xtn_poem_lxr Bldr = new Xtn_poem_lxr(); Xtn_poem_lxr() {}
}

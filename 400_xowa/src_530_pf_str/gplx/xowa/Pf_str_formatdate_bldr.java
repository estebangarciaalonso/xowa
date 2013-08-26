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
package gplx.xowa; import gplx.*;
public class Pf_str_formatdate_bldr {
	public int Idx_cur() {return idx_cur;} private int idx_cur;
	public int Idx_nxt() {return idx_nxt;} public Pf_str_formatdate_bldr Idx_nxt_(int v) {idx_nxt = v; return this;} private int idx_nxt;
	public DateAdpFormatItm[] Fmt_itm_ary() {return fmt_itm_ary;} DateAdpFormatItm[] fmt_itm_ary;
	public void Format(Xow_wiki wiki, Xol_lang lang, DateAdpFormatItm fmt_itm, DateAdp date, ByteAryBfr bfr) {
		fmt_itm.Fmt(wiki, lang, this, date, bfr);
	}
	public void Format(Xow_wiki wiki, Xol_lang lang, DateAdpFormatItm[] fmt_itm_ary, DateAdp date, ByteAryBfr bfr) {
		this.fmt_itm_ary = fmt_itm_ary;
		int fmt_itm_ary_len = fmt_itm_ary.length;
		idx_cur = 0; idx_nxt = -1;
		while (idx_cur < fmt_itm_ary_len) {
			DateAdpFormatItm fmt_itm = fmt_itm_ary[idx_cur];
			fmt_itm.Fmt(wiki, lang, this, date, bfr);
			if (idx_nxt == -1)
				++idx_cur;
			else {
				idx_cur = idx_nxt;
				idx_nxt = -1;
			}
		}
	}
}

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
public class Xop_html_ncr_tkn extends Xop_tkn_itm_base {
	public Xop_html_ncr_tkn(int bgn, int end, int html_ncr_val, byte[] html_ncr_bry) {this.Tkn_ini_pos(false, bgn, end); this.html_ncr_val = html_ncr_val; this.html_ncr_bry = html_ncr_bry;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_html_ncr;}
	public int Html_ncr_val() {return html_ncr_val;} private int html_ncr_val;
	public byte[] Html_ncr_bry() {return html_ncr_bry;} private byte[] html_ncr_bry;
}

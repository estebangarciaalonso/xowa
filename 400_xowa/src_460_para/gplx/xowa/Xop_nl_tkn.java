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
public class Xop_nl_tkn extends Xop_tkn_itm_base {
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_newLine;}
	int nl_len;
	public byte Nl_typeId() {return nl_typeId;} private byte nl_typeId = Xop_nl_tkn.Tid_unknown; public Xop_nl_tkn Nl_typeId_(byte v) {nl_typeId = v; return this;} 
	public byte Nl_ws() {return nl_ws;} public Xop_nl_tkn Nl_ws_y_() {nl_ws = Bool_.Y_byte; return this;} private byte nl_ws = Bool_.__byte;
	public Xop_nl_tkn(int bgn, int end, byte nl_typeId, int nl_len) {
		this.Tkn_ini_pos(false, bgn, end);
		this.nl_typeId = nl_typeId;
		this.nl_len = nl_len;
	}
	public static final byte Tid_unknown = 0, Tid_char = 1, Tid_auto = 2, Tid_hdr = 3, Tid_hr = 4, Tid_list = 5, Tid_tblw = 6, Tid_file = 7;
}

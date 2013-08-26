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
public class Xop_pre_tkn extends Xop_tkn_itm_base {
	public static final byte Pre_typeId_null = 0, Pre_typeId_bgn = 1, Pre_typeId_end = 2;
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_pre;}
	public byte Pre_typeId() {return pre_typeId;} private byte pre_typeId = Pre_typeId_null;
	public byte Pre_enable() {return pre_enable;} public Xop_pre_tkn Pre_enable_(byte v) {pre_enable = v; return this;} private byte pre_enable = Bool_.__byte;
	public Xop_tkn_itm Pre_bgnTkn() {return pre_bgnTkn;} public Xop_tkn_itm Pre_bgnTkn_(Xop_pre_tkn v) {pre_bgnTkn = v; return this;} private Xop_tkn_itm pre_bgnTkn;
	public Xop_pre_tkn(int bgn, int end, byte pre_typeId, Xop_tkn_itm pre_bgnTkn) {
		this.Tkn_ini_pos(false, bgn, end);
		this.pre_typeId = pre_typeId;
		this.pre_bgnTkn = pre_bgnTkn;
	}
}

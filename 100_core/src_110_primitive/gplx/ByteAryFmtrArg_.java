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
package gplx;
import gplx.byteAryFmtrArgs.*;
public class ByteAryFmtrArg_ {
	public static ByteAryFmtrArg_bry bry_(String v)				{return new ByteAryFmtrArg_bry(ByteAry_.new_utf8_(v));}
	public static ByteAryFmtrArg_bry bry_(byte[] v)				{return new ByteAryFmtrArg_bry(v);}
	public static ByteAryFmtrArg_byt byt_(byte v)				{return new ByteAryFmtrArg_byt(v);}
	public static ByteAryFmtrArg_int int_(int v)				{return new ByteAryFmtrArg_int(v);}
	public static ByteAryFmtrArg_bfr bfr_(ByteAryBfr v)			{return new ByteAryFmtrArg_bfr(v);}
	public static ByteAryFmtrArg_bfr_retain bfr_retain_(ByteAryBfr v)		{return new ByteAryFmtrArg_bfr_retain(v);}
	public static ByteAryFmtrArg fmtr_(ByteAryFmtr v, ByteAryFmtrArg... arg_ary) {return new ByteAryFmtrArg_fmtr(v, arg_ary);}
	public static ByteAryFmtrArg_fmtr_objs fmtr_null_() {return new ByteAryFmtrArg_fmtr_objs(null, null);}
	public static final ByteAryFmtrArg Null = new ByteAryFmtrArg_null();
}
class ByteAryFmtrArg_null implements ByteAryFmtrArg {
	public void XferAry(ByteAryBfr trg, int idx) {}
}

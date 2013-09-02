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
public class Xop_para_tkn extends Xop_tkn_itm_base {
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_para;}
	public byte Para_end() {return para_end;} public Xop_para_tkn Para_end_(byte v) {para_end = v; return this;} private byte para_end = Para_typeId_none;
	public Xop_para_tkn Para_end_para_()	{para_end = Para_typeId_para; return this;}
	public Xop_para_tkn Para_end_pre_()		{para_end = Para_typeId_pre; return this;}
	public byte Para_bgn() {return para_bgn;} public Xop_para_tkn Para_bgn_(byte v) {para_bgn = v; return this;} private byte para_bgn = Para_typeId_none;
	public Xop_para_tkn Para_bgn_para_()	{para_bgn = Para_typeId_para; return this;}
	public Xop_para_tkn Para_bgn_pre_()		{para_bgn = Para_typeId_pre; return this;}
	public boolean Para_blank() {return para_bgn == Para_typeId_none && para_end == Para_typeId_none;}
	public Xop_para_tkn(int pos, byte para_bgn, byte para_end) {this.Tkn_ini_pos(false, pos, pos); this.para_bgn = para_bgn; this.para_end = para_end;}
	public static final byte 
			Para_typeId_none = 0		//
		,	Para_typeId_para = 1		// </p>
		,	Para_typeId_pre  = 2		// </pre>
		,	Para_typeId_space= 3		// \s; handles disabled pre
		;
}

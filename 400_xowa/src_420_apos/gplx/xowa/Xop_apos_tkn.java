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
public class Xop_apos_tkn extends Xop_tkn_itm_base {
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_apos;}
	public int Apos_len() {return apos_len;} private int apos_len;
	public int Apos_lit() {return apos_lit;} public Xop_apos_tkn Apos_lit_(int v) {apos_lit = v; return this;} private int apos_lit;
	public int Apos_tid() {return apos_tid;} public Xop_apos_tkn Apos_tid_(int v) {apos_tid = v; return this;} private int apos_tid;
	public int Apos_cmd() {return apos_cmd;} public Xop_apos_tkn Apos_cmd_(int v) {apos_cmd = v; return this;} private int apos_cmd;
	public Xop_apos_tkn(int bgn, int end, int apos_len, int apos_tid, int apos_cmd, int apos_lit) {this.apos_len = apos_len; this.apos_tid = apos_tid; this.apos_cmd = apos_cmd; this.apos_lit = apos_lit; this.Tkn_ini_pos(false, bgn, end);}
}
class Xop_apos_tkn_ {
	public static final int Cmd_nil = 0
		, Cmd_i_bgn = 1, Cmd_i_end = 2, Cmd_b_bgn = 3, Cmd_b_end = 4
		, Cmd_bi_bgn = 5, Cmd_ib_bgn = 6, Cmd_ib_end = 7, Cmd_bi_end = 8
		, Cmd_bi_end__b_bgn = 9, Cmd_ib_end__i_bgn = 10, Cmd_b_end__i_bgn = 11, Cmd_i_end__b_bgn = 12;
	public static final byte[][] Cmds 
		= new byte[][] 
		{ ByteAry_.new_ascii_("nil")
		, ByteAry_.new_ascii_("i+"), ByteAry_.new_ascii_("i-"), ByteAry_.new_ascii_("b+"), ByteAry_.new_ascii_("b-")
		, ByteAry_.new_ascii_("bi+"), ByteAry_.new_ascii_("ib+"), ByteAry_.new_ascii_("ib-"), ByteAry_.new_ascii_("bi-")
		, ByteAry_.new_ascii_("bi-b+"), ByteAry_.new_ascii_("ib-i+"), ByteAry_.new_ascii_("b-i+"), ByteAry_.new_ascii_("i-b+")};
	public static String Cmd_str(int id) {return String_.new_utf8_(Cmds[id]);}
	public static final int CmdLen_ital = 2, CmdLen_bold = 3, CmdLen_dual = 5, CmdLen_aposBold = 4;
	public static final int Typ_ital = 2, Typ_bold = 3, Typ_dual = 5;
	public static final int State_nil = 0, State_i = 1, State_b = 2, State_bi = 3, State_ib = 4, State_dual = 5;
}

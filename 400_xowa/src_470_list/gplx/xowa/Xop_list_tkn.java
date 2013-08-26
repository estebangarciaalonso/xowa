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
public class Xop_list_tkn extends Xop_tkn_itm_base {
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_list;}
	public int	List_uid() {return list_uid;} public Xop_list_tkn List_uid_(int v) {list_uid = v; return this;} private int list_uid = -1;
	public byte List_bgn() {return list_bgn;} private byte list_bgn;
	public byte List_itmTyp() {return list_itmTyp;} public Xop_list_tkn List_itmTyp_(byte v) {list_itmTyp = v; return this;} private byte list_itmTyp = Xop_list_tkn_.List_itmTyp_null;
	public int[] List_path() {return path;} public Xop_list_tkn List_path_(int... v) {path = v; return this;} private int[] path = Int_.Ary_empty;
	public int List_path_idx() {return path[path.length - 1];}
	public boolean List_sub_first() {return List_path_idx() == 0;}
	public byte List_sub_last() {return list_sub_last;} public Xop_list_tkn List_sub_last_(byte v) {list_sub_last = v; return this;} private byte list_sub_last = Bool_.__byte;
	public static Xop_list_tkn bgn_(int bgn, int end, byte list_itmTyp, int symLen) {return new Xop_list_tkn(bgn, end, Bool_.Y_byte, list_itmTyp);}
	public static Xop_list_tkn end_(int pos, byte list_itmTyp) {return new Xop_list_tkn(pos, pos, Bool_.N_byte, list_itmTyp);}
	public Xop_list_tkn(int bgn, int end, byte bgnEndType, byte list_itmTyp) {this.Tkn_ini_pos(false, bgn, end); this.list_bgn = bgnEndType; this.list_itmTyp = list_itmTyp;}		
	public static final Xop_list_tkn Null = new Xop_list_tkn(); Xop_list_tkn() {}
}
class Xop_list_tkn_ {
	public static final byte[]
		  Hook_ul = new byte[] {Byte_ascii.NewLine, Byte_ascii.Asterisk}, Hook_ol = new byte[] {Byte_ascii.NewLine, Byte_ascii.Hash}
		, Hook_dt = new byte[] {Byte_ascii.NewLine, Byte_ascii.Semic}	, Hook_dd = new byte[] {Byte_ascii.NewLine, Byte_ascii.Colon};
	public static final byte List_itmTyp_null = 0, List_itmTyp_ul = Byte_ascii.Asterisk, List_itmTyp_ol = Byte_ascii.Hash, List_itmTyp_dt = Byte_ascii.Semic, List_itmTyp_dd = Byte_ascii.Colon;
	public static final String Str_li = "li", Str_ol = "ol", Str_ul = "ul", Str_dl = "dl", Str_dt = "dt", Str_dd = "dd";
	public static final byte[] Byt_li = ByteAry_.new_ascii_(Str_li), Byt_ol = ByteAry_.new_ascii_(Str_ol), Byt_ul = ByteAry_.new_ascii_(Str_ul)
								, Byt_dl = ByteAry_.new_ascii_(Str_dl), Byt_dt = ByteAry_.new_ascii_(Str_dt), Byt_dd = ByteAry_.new_ascii_(Str_dd);
	public static byte[] XmlTag_lst(byte b) {
		switch (b) {
			case List_itmTyp_ul:	return Byt_ul;
			case List_itmTyp_ol:	return Byt_ol;
			case List_itmTyp_dt:
			case List_itmTyp_dd:	return Byt_dl;
			default:				throw Err_.unhandled(b);
		}
	}
	public static byte[] XmlTag_itm(byte b) {
		switch (b) {
			case List_itmTyp_ul:
			case List_itmTyp_ol:	return Byt_li;
			case List_itmTyp_dt:	return Byt_dt;
			case List_itmTyp_dd:	return Byt_dd;
			default:				throw Err_.unhandled(b);
		}
	}
	public static byte Char_lst(byte b) {
		switch (b) {
			case List_itmTyp_ul:	return Byte_ascii.Asterisk;
			case List_itmTyp_ol:	return Byte_ascii.Hash;
			case List_itmTyp_dt:	return Byte_ascii.Semic;
			case List_itmTyp_dd:	return Byte_ascii.Colon;
			default:				throw Err_.unhandled(b);
		}
	}
}

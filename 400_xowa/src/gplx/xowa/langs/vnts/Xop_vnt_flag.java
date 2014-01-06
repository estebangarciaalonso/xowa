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
package gplx.xowa.langs.vnts; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
class Xop_vnt_flag {
	public Xop_vnt_flag(byte tid) {this.tid = tid; this.langs = ByteAry_.Ary_empty;}
	public Xop_vnt_flag(byte tid, byte[][] langs) {this.tid = tid; this.langs = langs;}
	public byte Tid() {return tid;} private byte tid;
	public byte[][] Langs() {return langs;} private byte[][] langs;
}
class Xop_vnt_flag_ {
	public static final byte
	  Tid_unknown	=  0
	, Tid_show		=  1
	, Tid_all		=  2
	, Tid_err		=  3
	, Tid_add		=  4
	, Tid_title		=  5
	, Tid_raw		=  6
	, Tid_descrip	=  7
	, Tid_del 		=  8
	, Tid_macro 	=  9
	, Tid_name 		= 10
	, Tid_lang		= 11
	;
	public static final byte Tid__max = 12;
	public static final byte
	  Key_show		= Byte_ascii.Ltr_S
	, Key_all		= Byte_ascii.Plus
	, Key_err		= Byte_ascii.Ltr_E
	, Key_add		= Byte_ascii.Ltr_A
	, Key_title		= Byte_ascii.Ltr_T
	, Key_raw		= Byte_ascii.Ltr_R
	, Key_descrip	= Byte_ascii.Ltr_D
	, Key_del		= Byte_ascii.Dash
	, Key_macro		= Byte_ascii.Ltr_H
	, Key_name		= Byte_ascii.Ltr_N
	;
	public static final Xop_vnt_flag
	  Flag_unknown	= new Xop_vnt_flag(Tid_unknown)
	, Flag_show		= new Xop_vnt_flag(Tid_show)
	, Flag_all		= new Xop_vnt_flag(Tid_all)
	, Flag_err		= new Xop_vnt_flag(Tid_err)
	, Flag_add		= new Xop_vnt_flag(Tid_add)
	, Flag_title	= new Xop_vnt_flag(Tid_title)
	, Flag_raw		= new Xop_vnt_flag(Tid_raw)
	, Flag_descrip	= new Xop_vnt_flag(Tid_descrip)
	, Flag_del		= new Xop_vnt_flag(Tid_del)
	, Flag_macro	= new Xop_vnt_flag(Tid_macro)
	, Flag_name		= new Xop_vnt_flag(Tid_name)
	;
	public static final ByteTrieMgr_fast Trie = ByteTrieMgr_fast.ci_()	// NOTE: match either lc or uc; EX: -{D}- or -{d}-
	.Add(Byte_ascii.Ltr_S			, Xop_vnt_flag_.Flag_show)
	.Add(Byte_ascii.Plus			, Xop_vnt_flag_.Flag_all)
	.Add(Byte_ascii.Ltr_E			, Xop_vnt_flag_.Flag_err)
	.Add(Byte_ascii.Ltr_A			, Xop_vnt_flag_.Flag_add)
	.Add(Byte_ascii.Ltr_T			, Xop_vnt_flag_.Flag_title)
	.Add(Byte_ascii.Ltr_R			, Xop_vnt_flag_.Flag_raw)
	.Add(Byte_ascii.Ltr_D			, Xop_vnt_flag_.Flag_descrip)
	.Add(Byte_ascii.Dash			, Xop_vnt_flag_.Flag_del)
	.Add(Byte_ascii.Ltr_H			, Xop_vnt_flag_.Flag_macro)
	.Add(Byte_ascii.Ltr_N			, Xop_vnt_flag_.Flag_name)
	;
	public static Xop_vnt_flag new_langs_(byte[][] langs) {return new Xop_vnt_flag(Tid_lang, langs);}
}
class Xop_vnt_flag_ary_bldr {
	private Xop_vnt_flag[] ary = new Xop_vnt_flag[Xop_vnt_flag_.Tid__max];
	private int add_count = 0;
	public void Clear() {
		for (int i = 0; i < Xop_vnt_flag_.Tid__max; i++)
			ary[i] = null;
		add_count = 0;
	}
	public void Add(Xop_vnt_flag flag) {
		int idx = flag.Tid();
		if (ary[idx] == null) {
			ary[idx] = flag;
			++add_count;
		}
	}
	public Xop_vnt_flag[] Bld() {
		Xop_vnt_flag[] rv = new Xop_vnt_flag[add_count];
		int rv_idx = 0;
		for (int i = 0; i < Xop_vnt_flag_.Tid__max; i++) {
			Xop_vnt_flag itm = ary[i];
			if (itm != null)
				rv[rv_idx++] = itm;
		}
		return rv;
	}
}

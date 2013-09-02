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
public class Xop_lnki_tkn extends Xop_tkn_itm_base {
	@Override public byte Tkn_tid() {return typeId;} private byte typeId = Xop_tkn_itm_.Tid_lnki;
	public void TypeId_toText() {typeId = Xop_tkn_itm_.Tid_txt;}
	public boolean Dangling() {return this.Src_end() - this.Src_bgn() == Xop_tkn_.Lnki_bgn_len;}
	public int NmsId() {return nsId;} public Xop_lnki_tkn NmsId_(int v) {nsId = v; return this;} private int nsId;
	public int Tail_bgn() {return tail_bgn;} public Xop_lnki_tkn Tail_bgn_(int v) {tail_bgn = v; return this;} private int tail_bgn = -1;
	public int Tail_end() {return tail_end;} public Xop_lnki_tkn Tail_end_(int v) {tail_end = v; return this;} private int tail_end = -1;
	public byte Lnki_type() {return lnki_type;} public Xop_lnki_tkn Lnki_type_(byte v) {lnki_type = (byte)Enm_.AddInt(lnki_type, v); return this;} private byte lnki_type = Xop_lnki_type.Id_null;
	public byte Border() {return border;} public Xop_lnki_tkn Border_(byte v) {border = v; return this;} private byte border = Bool_.__byte;
	public byte HAlign() {return hAlign;} public Xop_lnki_tkn HAlign_(byte v) {if (hAlign == Xop_lnki_halign.Null) hAlign = v; return this;} private byte hAlign = Xop_lnki_halign.Null;
	public byte VAlign() {return vAlign;} public Xop_lnki_tkn VAlign_(byte v) {vAlign = v; return this;} private byte vAlign = Byte_.MaxValue_127;
	public IntRef Width() {return width;} public Xop_lnki_tkn Width_(int v) {width.Val_(v); return this;} IntRef width = IntRef.neg1_();
	public IntRef Height() {return height;} public Xop_lnki_tkn Height_(int v) {height.Val_(v); return this;} IntRef height = IntRef.neg1_();		
	public double Upright() {return upright;} public Xop_lnki_tkn Upright_(double v) {upright = v; return this;} double upright = Upright_null;
	public boolean Media_icon() {return media_icon;} public Xop_lnki_tkn Media_icon_n_() {media_icon = false; return this;} private boolean media_icon = true;
	public int Thumbtime() {return thumbtime;} public Xop_lnki_tkn Thumbtime_(int v) {thumbtime = v; return this;} private int thumbtime = Thumbtime_null;
	public int Args_len() {return args_len;} private int args_len = 0;
	public Xop_tkn_itm Trg_tkn() {return trg_tkn;} public Xop_lnki_tkn Trg_tkn_(Xop_tkn_itm v) {trg_tkn = v; return this;} private Xop_tkn_itm trg_tkn = Xop_tkn_null.Null_tkn;
	public Xop_tkn_itm Caption_tkn() {return caption_tkn;} public Xop_lnki_tkn Caption_tkn_(Xop_tkn_itm v) {caption_tkn = v; return this;} private Xop_tkn_itm caption_tkn = Xop_tkn_null.Null_tkn;
	public Arg_itm_tkn Caption_val_tkn() {return caption_tkn == Xop_tkn_null.Null_tkn ? (Arg_itm_tkn)Arg_itm_tkn_null.Null_arg_itm : ((Arg_nde_tkn)caption_tkn).Val_tkn();}
	public boolean Caption_tkn_pipe_trick() {return caption_tkn_pipe_trick;} public Xop_lnki_tkn Caption_tkn_pipe_trick_(boolean v) {caption_tkn_pipe_trick = v; return this;} private boolean caption_tkn_pipe_trick;
	public Arg_nde_tkn Link_tkn() {return link_tkn;} public Xop_lnki_tkn Link_tkn_(Arg_nde_tkn v) {link_tkn = v; return this;} Arg_nde_tkn link_tkn = Arg_nde_tkn.Null;
	public Arg_nde_tkn Alt_tkn() {return alt_tkn;} public Xop_lnki_tkn Alt_tkn_(Arg_nde_tkn v) {alt_tkn = v; return this;} Arg_nde_tkn alt_tkn = Arg_nde_tkn.Null;
	public boolean Alt_exists() {return alt_tkn != Arg_nde_tkn.Null;}
	public Xoa_ttl Ttl() {return ttl;} public Xop_lnki_tkn Ttl_(Xoa_ttl v) {ttl = v; return this;} private Xoa_ttl ttl;
	public byte[] Ttl_ary() {
		return ttl.ForceLiteralLink() || nsId != Xow_ns_.Id_main		// if [[:]] or non-main (Category, Template)
			? ttl.Full_txt()												// use full_txt (no initial colon; capitalize first)
			: ttl.Raw();													// use raw (preserve case, white-spaces)
	}
	public boolean Caption_exists() {
		return !((caption_tkn == Xop_tkn_null.Null_tkn)		// trg only; no caption: EX: [[a]] vs. [[a|b]] which has a trg of a and a caption of b
				||	(nsId == Xow_ns_.Id_category			// a Category only has a target; any caption is ignored; EX: [[Category:a|b], b is ignored			
					&& !ttl.ForceLiteralLink()));				
	}
	public static final double Upright_null = -1;
	public static final int Thumbtime_null = -1, Width_null = -1, Height_null = -1;
}

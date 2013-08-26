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
class Xop_lnki_caption_mgr {
	public void Clear() {caption_regy.Clear(); caption_trg_regy.Clear();}
	public String GetTrgByCaption(String caption) {
		ByteAryRef caption_ary = ByteAryRef.new_(ByteAry_.new_utf8_(caption));
		byte[] rv = (byte[])caption_trg_regy.Fetch(caption_ary);
		return rv == null ? null : String_.new_utf8_(rv);
	}
	public void Reg(Xop_ctx ctx, byte[] src, Xop_tkn_itm trg, int caption_pos_bgn, int caption_pos_end, ByteAryBfr bb) {
		caption_bb.Add(ByteAry_.Mid(bb.Bry(), caption_pos_bgn, caption_pos_end));	// NOTE: need new byte[], b/c captions can be expanded; EX:[[a|''b'']] -> [[a|<i>b</i>]]
		ByteAryRef caption_ary = ByteAryRef.new_(caption_bb.XtoAry());
		ListAdp caption_grp = GetTrgGrp(caption_ary);
		byte[] trg_ary = trg_bb.Add_mid(src, trg.Src_bgn(), trg.Src_end()).XtoAry();	// NOTE: do not need new byte[], but makes code easier
		Xop_lnki_caption_itm itm = GetCaptionItm(caption_grp, trg_ary);
		if (itm != null) {caption_bb.Clear(); trg_bb.Clear(); return;}	// caption/trg pair exists; no need to register; EX: [[a|b]] [[a|b]]
		itm = new Xop_lnki_caption_itm(caption_ary, trg_ary);			// trg does not exist; either (a) new or (b) overlap; EX: [[a|b]] [[c|b]]
		caption_grp.Add(itm);											// reg itm in caption_grp; EX: [[a]], [[b|a]], [[c|a]] need to be in "a" grp for proper numbering: a (2)
		int caption_grp_idx = caption_grp.Count();
		if (caption_grp_idx > 1) {										// NOTE: caption already exists, so make new unique one; EX: from above, [[a]], [[a (1)]] etc..
			caption_bb.Add(Paren_bgn).Add_int_variable(caption_grp_idx).Add(Paren_end);
			bb.Add(Paren_bgn).Add_int_variable(caption_grp_idx).Add(Paren_end);
			caption_ary = ByteAryRef.new_(caption_bb.XtoAry());
		}
		caption_trg_regy.Add(caption_ary, trg_ary);
		caption_bb.Clear(); trg_bb.Clear();
	}
	ListAdp GetTrgGrp(ByteAryRef caption_ary) {
		ListAdp rv = (ListAdp)caption_regy.Fetch(caption_ary);
		if (rv == null) {
			rv = ListAdp_.new_();
			caption_regy.Add(caption_ary, rv);
		}
		return rv;
	}
	Xop_lnki_caption_itm GetCaptionItm(ListAdp trg_grp, byte[] trg_ary) {
		Xop_lnki_caption_itm rv = null;
		for (int i = 0; i < trg_grp.Count(); i++) {
			rv = (Xop_lnki_caption_itm)trg_grp.FetchAt(i);
			if (ByteAry_.Eq(trg_ary, rv.Trg)) return rv;
		}
		return null;
	}
	HashAdp caption_regy = HashAdp_.new_(); HashAdp caption_trg_regy = HashAdp_.new_();
	ByteAryBfr trg_bb = ByteAryBfr.new_(), caption_bb = ByteAryBfr.new_();
	private static final byte[] Paren_bgn = ByteAry_.new_ascii_(" ("), Paren_end = ByteAry_.new_ascii_(")");
}
class Xop_lnki_caption_itm {
	public ByteAryRef Caption;
	public byte[] Trg;
	public Xop_lnki_caption_itm(ByteAryRef caption, byte[] trg) {this.Caption = caption; this.Trg = trg;}
}

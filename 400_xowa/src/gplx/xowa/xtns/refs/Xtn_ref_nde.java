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
package gplx.xowa.xtns.refs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xtn_ref_nde implements Xop_xnde_xtn, Xop_xnde_atr_parser {
	public Xop_root_tkn Xtn_root() {return null;}
	public boolean Xtn_literal() {return false;}
	public static final int Idx_minor_follow = -2;
	public byte[] Name() {return name;} public Xtn_ref_nde Name_(byte[] v) {name = v; return this;} private byte[] name = ByteAry_.Empty;
	public byte[] Group() {return group;} public Xtn_ref_nde Group_(byte[] v) {group = v; return this;} private byte[] group = ByteAry_.Empty;
	public byte[] Follow() {return follow;} public Xtn_ref_nde Follow_(byte[] v) {follow = v; return this;} private byte[] follow = ByteAry_.Empty;
	public boolean Follow_y() {return follow != ByteAry_.Empty;}
	public Xop_root_tkn Body() {return body;} public Xtn_ref_nde Body_(Xop_root_tkn v) {body = v; return this;} private Xop_root_tkn body;
	public Xop_xnde_tkn Xnde() {return xnde;} private Xop_xnde_tkn xnde;
	public int Uid() {return uid;} public Xtn_ref_nde Uid_(int v) {uid = v; return this;} private int uid;
	public boolean Head() {return head;} public Xtn_ref_nde Head_(boolean v) {head = v; return this;} private boolean head;
	public boolean Nested() {return nested;} private boolean nested;
	public int Idx_major() {return idx_major;} public Xtn_ref_nde Idx_major_(int v) {idx_major = v; return this;} private int idx_major;
	public int Idx_minor() {return idx_minor;} public Xtn_ref_nde Idx_minor_(int v) {idx_minor = v; return this;} private int idx_minor;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {
		if (xatr_key_obj == null) return;
		ByteVal xatr_key = (ByteVal)xatr_key_obj;
		switch (xatr_key.Val()) {
			case Xatr_id_name:		name = wiki.App().Sanitizer().Escape_id(xatr.Val_as_bry(src)); break;
			case Xatr_id_group:		group = xatr.Val_as_bry(src); break;
			case Xatr_id_follow:	follow = xatr.Val_as_bry(src); break;
		}
	}
	public void Xtn_compile(Xow_wiki wiki, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		Xop_xatr_itm.Xatr_parse(wiki.App(), this, wiki.Lang().Xatrs_ref(), wiki, src, xnde);
		if (xnde.CloseMode() == Xop_xnde_tkn.CloseMode_pair) {
			body = wiki.Parser().Parse_recurse(ctx, ByteAry_.Mid(src, xnde.Tag_open_end(), xnde.Tag_close_bgn()), false);
		}
		ctx.Page().Ref_mgr().Grps_add(group, name, follow, this);
		nested = ctx.Ref_nested();
		this.xnde = xnde;
	}
	private static final Xtn_ref_nde[] Ary_empty = new Xtn_ref_nde[0];
	public Xtn_ref_nde[] Related() {return related;} Xtn_ref_nde[] related = Ary_empty;
	public int Related_len() {return related_len;} private int related_len;
	public Xtn_ref_nde Related_get(int i) {return related[i];}
	public void Related_add(Xtn_ref_nde itm, int idx_minor) {
		int new_len = related_len + 1;
		int related_max = related.length;
		if (new_len > related_max)
			related = (Xtn_ref_nde[])Array_.Resize(related, related_max == 0 ? 1 : related_max * 2);
		itm.Idx_minor_(idx_minor);
		related[related_len] = itm;
		related_len = new_len;
	}
	public static final byte Xatr_id_name = 0, Xatr_id_group = 1, Xatr_id_follow = 2;
}

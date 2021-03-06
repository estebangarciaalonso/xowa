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
import gplx.xowa.html.*;
public class Ref_nde implements Xox_xnde, Xop_xnde_atr_parser {
	public static final int Idx_minor_follow = -2;
	public boolean Exists_in_lnki_title() {return exists_in_lnki_title;} public Ref_nde Exists_in_lnki_title_(boolean v) {exists_in_lnki_title = v; return this;} private boolean exists_in_lnki_title;
	public byte[] Name() {return name;} public Ref_nde Name_(byte[] v) {name = v; return this;} private byte[] name = ByteAry_.Empty;
	public byte[] Group() {return group;} public Ref_nde Group_(byte[] v) {group = v; return this;} private byte[] group = ByteAry_.Empty;
	public byte[] Follow() {return follow;} public Ref_nde Follow_(byte[] v) {follow = v; return this;} private byte[] follow = ByteAry_.Empty;
	public boolean Follow_y() {return follow != ByteAry_.Empty;}
	public Xop_root_tkn Body() {return body;} public Ref_nde Body_(Xop_root_tkn v) {body = v; return this;} private Xop_root_tkn body;
	public Xop_xnde_tkn Xnde() {return xnde;} private Xop_xnde_tkn xnde;
	public int Uid() {return uid;} public Ref_nde Uid_(int v) {uid = v; return this;} private int uid;
	public boolean Head() {return head;} public Ref_nde Head_(boolean v) {head = v; return this;} private boolean head;
	public boolean Nested() {return nested;} private boolean nested;
	public int Idx_major() {return idx_major;} public Ref_nde Idx_major_(int v) {idx_major = v; return this;} private int idx_major;
	public int Idx_minor() {return idx_minor;} public Ref_nde Idx_minor_(int v) {idx_minor = v; return this;} private int idx_minor;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {
		if (xatr_key_obj == null) return;
		ByteVal xatr_key = (ByteVal)xatr_key_obj;
		switch (xatr_key.Val()) {
			case Xatr_id_name:		name = wiki.App().Sanitizer().Escape_id(xatr.Val_as_bry(src)); break;
			case Xatr_id_group:		group = xatr.Val_as_bry(src); break;
			case Xatr_id_follow:	follow = xatr.Val_as_bry(src); break;
		}
	}
	public void Xtn_parse(Xow_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		Xop_xatr_itm.Xatr_parse(wiki.App(), this, wiki.Lang().Xatrs_ref(), wiki, src, xnde);
		if (xnde.CloseMode() == Xop_xnde_tkn.CloseMode_pair)
			body = wiki.Parser().Parse_recurse(ctx, ByteAry_.Mid(src, xnde.Tag_open_end(), xnde.Tag_close_bgn()), false);
		ctx.Page().Ref_mgr().Grps_add(group, name, follow, this);
		nested = ctx.Ref_nested();
		this.xnde = xnde;
	}
	public void Xtn_write(Xoa_app app, Xoh_html_wtr html_wtr, Xoh_html_wtr_ctx opts, Xop_ctx ctx, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde) {
		html_wtr.Ref_wtr().Xnde_ref(opts, bfr, src, xnde);
	}
	private static final Ref_nde[] Ary_empty = new Ref_nde[0];
	public Ref_nde[] Related() {return related;} Ref_nde[] related = Ary_empty;
	public int Related_len() {return related_len;} private int related_len;
	public Ref_nde Related_get(int i) {return related[i];}
	public void Related_add(Ref_nde itm, int idx_minor) {
		int new_len = related_len + 1;
		int related_max = related.length;
		if (new_len > related_max)
			related = (Ref_nde[])Array_.Resize(related, related_max == 0 ? 1 : related_max * 2);
		itm.Idx_minor_(idx_minor);
		related[related_len] = itm;
		related_len = new_len;
	}
	public static final byte Xatr_id_name = 0, Xatr_id_group = 1, Xatr_id_follow = 2;
}

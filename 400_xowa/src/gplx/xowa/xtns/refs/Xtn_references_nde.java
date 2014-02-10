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
public class Xtn_references_nde implements Xox_xnde, Xop_xnde_atr_parser {
	public byte[] Group() {return group;} public Xtn_references_nde Group_(byte[] v) {group = v; return this;} private byte[] group = ByteAry_.Empty;
	public int List_idx() {return list_idx;} public Xtn_references_nde List_idx_(int v) {list_idx = v; return this;} private int list_idx;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {
		if (xatr_key_obj == null) return;
		ByteVal xatr_key = (ByteVal)xatr_key_obj;
		switch (xatr_key.Val()) {
			case Xatr_id_group:		{
				group = xatr.Val_as_bry(src);
				if (ByteAry_.Match(group, Bry_group)) group = ByteAry_.Empty;  // HACK: reflist returns <references group>
				break;
			}
		}
	}	private static byte[] Bry_group = ByteAry_.new_ascii_("group");
	public void Xtn_parse(Xow_wiki wiki, Xop_ctx ctx, Xop_root_tkn cur_root, byte[] src, Xop_xnde_tkn xnde) {
		Xop_xatr_itm.Xatr_parse(wiki.App(), this, wiki.Lang().Xatrs_references(), wiki, src, xnde);
		if (xnde.CloseMode() == Xop_xnde_tkn.CloseMode_pair) {
			int itm_bgn = xnde.Tag_open_end(), itm_end = xnde.Tag_close_bgn();
			Xop_ctx inner_ctx = Xop_ctx.new_sub_(wiki);	// NOTE: was static member (for PERF); removed; DATE:2014-01-03
			inner_ctx.Para().Enabled_n_();
			byte[] references_src = ByteAry_.Mid(src, itm_bgn, itm_end);
			Xop_tkn_mkr tkn_mkr = ctx.Tkn_mkr();
			Xop_root_tkn root = tkn_mkr.Root(src);
			wiki.Parser().Parse_page_all(root, inner_ctx, tkn_mkr, references_src, Xop_parser_.Doc_bgn_char_0);
			int xnde_subs = root.Subs_len();
			ctx.Ref_nested_(true);
			for (int i = 0; i < xnde_subs; i++) {
				Xop_tkn_itm tkn = root.Subs_get(i);
				if (tkn.Tkn_tid() == Xop_tkn_itm_.Tid_xnde) {
					Xop_xnde_tkn ref_xnde = (Xop_xnde_tkn)tkn;
					if (ref_xnde.Tag().Id() == Xop_xnde_tag_.Tid_ref) {
						Xtn_ref_nde ref_itm = new Xtn_ref_nde().Head_(true).Group_(group);
						ref_itm.Xtn_parse(wiki, ctx, root, root.Root_src(), ref_xnde);
					}
				}
			}
			ctx.Ref_nested_(false);
		}
		list_idx = ctx.Page().Ref_mgr().Grps_get(group).Grp_seal();	// NOTE: needs to be sealed at end; else inner refs will end up in new group; EX: <references><ref>don't seal prematurely</ref></references>
	}
	public void Xtn_write(Xoa_app app, Xoh_html_wtr html_wtr, Xoh_opts opts, Xop_ctx ctx, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		html_wtr.Ref_wtr().Xnde_references(html_wtr, ctx, opts, bfr, src, xnde, depth);
	}
	public static final byte Xatr_id_group = 0;
	public static boolean Enabled = true;
}

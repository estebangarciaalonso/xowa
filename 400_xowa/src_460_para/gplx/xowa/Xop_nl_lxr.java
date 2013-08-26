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
class Xop_nl_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_nl;}
	public Xop_nl_lxr Poem_(boolean v) {poem = v; return this;} private boolean poem = false;
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Byte_ascii.NewLine, this);}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		Xop_para_wkr para_wkr = ctx.Para();
		if (bgn_pos == Xop_parser_.Doc_bgn_bos) {return ctx.LxrMake_txt_(cur_pos);} // simulated nl at beginning of every parse
		ctx.Apos().EndFrame(ctx, src, cur_pos);
		ctx.Tblw().Cell_pipe_seen_(false);

		// if hdr is open, then close it
		int acs_pos = ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_hdr);
		if (acs_pos != -1) {
			ctx.Stack_pop_til(acs_pos, true, bgn_pos, cur_pos);
			para_wkr.Process_nl_sect_end(cur_pos);
		}

		// if list/lnke is open/close it
		switch (ctx.Cur_tkn_tid()) {
			case Xop_tkn_itm_.Tid_lnki: // NOTE: \n in caption or other multipart lnki; don't call para_wkr.Process
//					para_wkr.Process_nl_sect_end(cur_pos);
				Xop_tkn_itm nl_tkn = tkn_mkr.Space(ctx.Root(), bgn_pos, cur_pos);	// convert \n to \s. may result in multiple \s, but rely on htmlViewer to suppress; EX.WP: Schwarzschild radius; and the stellar [[Velocity dispersion|velocity\ndispersion]]
				ctx.Subs_add(nl_tkn);
				return cur_pos;
			case Xop_tkn_itm_.Tid_list:
				Xop_list_wkr_.Close_list_if_present(ctx, bgn_pos, cur_pos);
				para_wkr.Process_nl_sect_end(cur_pos);
				break;
			case Xop_tkn_itm_.Tid_lnke:
				if (ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tmpl_invk) == -1) {// only pop if no tmpl; MWR: [[SHA-2]]; * {{cite journal|title=Proposed 
					ctx.Stack_pop_til(ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_lnke), true, bgn_pos, cur_pos);
				}
				break;
//				case Xop_tkn_itm_.Tid_tblw_tc: case Xop_tkn_itm_.Tid_tblw_td:	// tc/td should not have attributes
			case Xop_tkn_itm_.Tid_tblw_tb: case Xop_tkn_itm_.Tid_tblw_tr: case Xop_tkn_itm_.Tid_tblw_th:	// nl should close previous tblw's atrs range; EX {{Infobox planet}} and |-\n<tr>
				Xop_tblw_wkr.Atrs_close(ctx, src, root);
				break;
		}
		if (para_wkr.Enabled() && ctx.Parse_tid() == Xop_parser_.Parse_tid_page_wiki)
			para_wkr.Process_nl(bgn_pos, cur_pos, true);
		else {
			if (poem) {
				ctx.Subs_add(tkn_mkr.Xnde(bgn_pos, cur_pos).Tag_(Xop_xnde_tag_.Tag_br));
				ctx.Subs_add(tkn_mkr.NewLine(cur_pos, cur_pos, Xop_nl_tkn.Tid_char, 1));
			}
			else {
				Xop_nl_tkn nl_tkn = tkn_mkr.NewLine(bgn_pos, cur_pos, Xop_nl_tkn.Tid_char, 1);
				ctx.Subs_add(nl_tkn);
			}
		}
		return cur_pos;
	}
	public static final Xop_nl_lxr Bldr = new Xop_nl_lxr().Poem_(false); Xop_nl_lxr() {}
	public static final Xop_nl_lxr Poem = new Xop_nl_lxr().Poem_(true);
}

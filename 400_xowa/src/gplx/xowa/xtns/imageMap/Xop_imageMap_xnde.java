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
package gplx.xowa.xtns.imageMap; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xop_imageMap_xnde implements Xop_xnde_xtn {
	public byte[] Xtn_src() {return lnki_src;} private byte[] lnki_src;
	public boolean Xtn_literal() {return false;}
	public Xop_root_tkn Xtn_root() {return lnki_root;} private Xop_root_tkn lnki_root;
	public ListAdp Shape_list() {return shape_list;} ListAdp shape_list = ListAdp_.new_(); 
	public void Xtn_compile(Xop_ctx ctx, Xow_wiki wiki, Xop_tkn_mkr tkn_mkr, byte[] src, Xop_xnde_tkn xnde) {
		int content_bgn = xnde.Tag_open_end(), content_end = xnde.Tag_close_bgn();
		int nl_0_pos = Xop_lxr_.Find_fwd_while_non_ws(src, content_bgn, content_end);
		int cur_pos = nl_0_pos, nl_1_pos = -1;//, ws_pos_bgn = -1;
		Xop_root_tkn root = ctx.Root();
		int src_len = src.length;
		Xop_ctx imageMap_ctx = Xop_ctx.new_sub_(wiki);
		imageMap_ctx.Para().Enabled_n_();
		while (true) {
			boolean last = cur_pos == content_end;
			if (last) nl_1_pos = cur_pos;
			if (nl_1_pos != -1 || last) {
				Object typeId_obj = TypeTrie.MatchAtCur(src, nl_0_pos, nl_1_pos);
				if (typeId_obj == null) {	// flag itm					
					if (!first && nl_1_pos - nl_0_pos > 0)
						ctx.Msg_log().Add_itm_none(Xtn_imageMap_msg.Line_type_unknown, src, nl_0_pos, nl_1_pos);
				}
				else {
					byte typeId = ((ByteVal)typeId_obj).Val();
					switch (typeId) {
						case TypeId_comment: break;	// NOOP
						case TypeId_desc: break;	// FUTURE: flag; TODO: desc show info icon; top-right, bottom-right, bottom-left, top-left, none
						case TypeId_default:
						case TypeId_rect:
						case TypeId_circle:
						case TypeId_poly:
//								GfoLogWkr logWkr = new GfoLogWkr();
//								logWkr.Add_args(Xtn_imageMap_msg.Coords_count_invalid, TypeName[typeId], TypeCoords[typeId], coords.length);
							Xtn_imageMap_shape shape = Xtn_imageMap_shape.parse_(typeId, src, nl_0_pos + TypeTrie.Match_pos(), nl_1_pos);
							shape_list.Add(shape);
							break;
					}
				}
				//int bgn = ws_pos_bgn = -1 ? nl_0_pos : ws
				ParseLine(imageMap_ctx, wiki, tkn_mkr, root, src, src_len, xnde, nl_0_pos, nl_1_pos);
				nl_0_pos = nl_1_pos + 1;
				nl_1_pos = -1;
			}
			if (last) break;
			byte b = src[cur_pos];
			switch (b) {
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
					break;
				case Byte_ascii.NewLine:
					nl_1_pos = cur_pos;
					break;
				default:
//						ws_pos_bgn = cur_pos;;
					break;
			}
			++cur_pos;
		}
	}	boolean first = true;
	void ParseLine(Xop_ctx ctx, Xow_wiki wiki, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, Xop_xnde_tkn xnde, int nl_0_pos, int nl_1_pos) {
		int line_len = nl_1_pos - nl_0_pos; 
		if (line_len == 0 || src[nl_0_pos + 1] == Byte_ascii.Hash) return;
		if (first) {
			byte[] lnki_raw = ByteAry_.Add(Xop_tkn_.Lnki_bgn, ByteAry_.Mid(src, nl_0_pos, nl_1_pos), Xop_tkn_.Lnki_end);
			lnki_root = tkn_mkr.Root(lnki_src);
			ctx.Wiki().Parser().Parse_page_all(lnki_root, ctx, tkn_mkr, lnki_raw, 0);
			lnki_src = lnki_root.Root_src();	// NOTE: html_wtr will write based on parsed mid (not raw)
			lnki_root.Root_src_(lnki_src);	// HACK: Xoh_html_wtr uses raw (instead of mid); put data in raw in order to conform to other xtns
			first = false;
		}
		else {
		}
	}
	public static final byte TypeId_default = 0, TypeId_rect = 4, TypeId_circle = 3, TypeId_poly = 5, TypeId_desc = 6, TypeId_comment = 7;
	public static ByteTrieMgr_slim TypeTrie = ByteTrieMgr_slim.ci_()	// NOTE: names are not i18n'd
		.Add("default"	, ByteVal.new_(TypeId_default))
		.Add("rect"		, ByteVal.new_(TypeId_rect))
		.Add("circle"	, ByteVal.new_(TypeId_circle))
		.Add("poly"		, ByteVal.new_(TypeId_poly))
		.Add("desc"		, ByteVal.new_(TypeId_desc))
		.Add("#"		, ByteVal.new_(TypeId_comment))
	; 
}
class Xtn_imageMap_shape {
	public byte TypeId() {return typeId;} private byte typeId;
	public int[] Coord_ary() {return coord_ary;} private int[] coord_ary = null;
	public Xop_lnki_tkn Lnki_tkn() {return lnki_tkn;} private Xop_lnki_tkn lnki_tkn = null;
	public static Xtn_imageMap_shape parse_(byte typeId, byte[] src, int bgn, int end) {
		Xtn_imageMap_shape rv = new Xtn_imageMap_shape();
		rv.typeId = typeId;
		return rv;
	}
}
class Xtn_imageMap_msg {
	public static final Gfo_msg_grp Nde = Gfo_msg_grp_.new_(Xoa_app_.Nde, "image_map");
	public static final Gfo_msg_itm
		  Line_type_unknown		= Gfo_msg_itm_.new_warn_(Nde, "line_type_unknown", "Line type is unknown")
		, Coords_count_invalid	= Gfo_msg_itm_.new_warn_(Nde, "coords_count_invalid", "Coordinate counts are invalid for shape: shape=~{0} expd=~{1} actl=~{2}")
		;
}

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
package gplx.xowa.xtns.gallery; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.parsers.logs.*;
public class Xtn_gallery_nde implements Xop_xnde_xtn, Xop_xnde_atr_parser {
	public Xop_root_tkn Xtn_root() {return null;}
	public boolean Xtn_literal() {return false;}
	public int Itm_width() {return itm_width;} private int itm_width = Itm_default_w;
	public int Itm_height() {return itm_height;} private int itm_height = Itm_default_h;
	public int Itms_per_row() {return itms_per_row;} public Xtn_gallery_nde Itms_per_row_(int v) {itms_per_row = v; return this;} private int itms_per_row = -1;
	public int Itms_len() {return itms.Count();} private ListAdp itms = ListAdp_.new_();
	public Xtn_gallery_itm Itms_get(int i) {return (Xtn_gallery_itm)itms.FetchAt(i);}	
	public static final byte Xatr_id_perrow = 0, Xatr_id_widths = 1, Xatr_id_heights = 2;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {
		if (xatr_key_obj == null) return;
		ByteVal xatr_key = (ByteVal)xatr_key_obj;
		switch (xatr_key.Val()) {
			case Xatr_id_perrow:		itms_per_row = xatr.Val_as_int_or(src, 4); break;
			case Xatr_id_widths:		itm_width = xatr.Val_as_int_or(src, 120); break;
			case Xatr_id_heights:		itm_height = xatr.Val_as_int_or(src, 120); break;
		}
	}
	public void Xtn_compile(Xop_ctx ctx, Xow_wiki wiki, Xop_tkn_mkr tkn_mkr, byte[] src, Xop_xnde_tkn xnde) {
		if (itms_per_row == -1) itms_per_row = wiki.Cfg_gallery().Imgs_per_row();
		Xop_xatr_itm.Xatr_parse(wiki.App(), this, wiki.Lang().Xatrs_gallery(), wiki, src, xnde);
		wiki.Xtn_mgr().Xtn_gallery().Parser().Parse_all(itms, src, xnde.Tag_open_end(), xnde.Tag_close_bgn(), itm_width, itm_height);
		boolean log_wkr_enabled = Log_wkr != Xop_log_basic_wkr.Null; if (log_wkr_enabled) Log_wkr.Log_end_xnde(ctx.Page(), Xop_log_basic_wkr.Tid_gallery, src, xnde);
	}	public static Xop_log_basic_wkr Log_wkr = Xop_log_basic_wkr.Null;
	public static final int Itm_default_w = 120, Itm_default_h = 120;		
}

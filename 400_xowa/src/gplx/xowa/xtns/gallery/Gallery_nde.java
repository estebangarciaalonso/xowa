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
public class Gallery_nde implements Xox_xnde, Xop_xnde_atr_parser {
	public int Itm_w()				{return itm_w;} private int itm_w = Null;
	public int Itm_h()				{return itm_h;} private int itm_h = Null;
	public int Itm_w_or_default()	{return itm_w == Null ? Default : itm_w;}
	public int Itm_h_or_default()	{return itm_h == Null ? Default : itm_h;}
	public int Itms_per_row()		{return itms_per_row;} private int itms_per_row = Null;
	public int Itms_len()			{return itms.Count();} private ListAdp itms = ListAdp_.new_();
	public byte[] Gallery_mode()	{return gallery_mode;} private byte[] gallery_mode;
	public Gallery_itm Itms_get(int i) {return (Gallery_itm)itms.FetchAt(i);}	
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {
		if (xatr_key_obj == null) return;
		ByteVal xatr_key = (ByteVal)xatr_key_obj;
		switch (xatr_key.Val()) {
			case Xatr_id_perrow:		itms_per_row = xatr.Val_as_int_or(src, Null); break;
			case Xatr_id_widths:		itm_w = xatr.Val_as_int_or(src, Null); break;
			case Xatr_id_heights:		itm_h = xatr.Val_as_int_or(src, Null); break;
			case Xatr_id_mode:			gallery_mode = xatr.Val_as_bry(src); break;
		}
	}
	public void Xtn_parse(Xow_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		Xop_xatr_itm.Xatr_parse(wiki.App(), this, wiki.Lang().Xatrs_gallery(), wiki, src, xnde);
		if (wiki.File_mgr().Cfg_get(Fsdb_cfg_grp).Get_yn_or_n(Fsdb_cfg_key_gallery_fix_defaults)) {	// v2
			if (itm_w == Gallery_nde.Null && itm_h == Gallery_nde.Null) {			// if no w/h specified, set both to default (just like v1)
				itm_w = itm_h = Gallery_nde.Default;
			}
		}
		else {																		// v1
			if (itm_w == Gallery_nde.Null) itm_w = Gallery_nde.Default;
			if (itm_h == Gallery_nde.Null) itm_h = Gallery_nde.Default;
		}
		if (itms_per_row == Gallery_nde.Null) itms_per_row = wiki.Cfg_gallery().Imgs_per_row();
		Gallery_xtn_mgr xtn_mgr = (Gallery_xtn_mgr)wiki.Xtn_mgr().Get_or_fail(Gallery_xtn_mgr.XTN_KEY);
		xtn_mgr.Parser().Parse_all(itms, src, xnde.Tag_open_end(), xnde.Tag_close_bgn(), itm_w, itm_h);
		boolean log_wkr_enabled = Log_wkr != Xop_log_basic_wkr.Null; if (log_wkr_enabled) Log_wkr.Log_end_xnde(ctx.Page(), Xop_log_basic_wkr.Tid_gallery, src, xnde);
	}	public static Xop_log_basic_wkr Log_wkr = Xop_log_basic_wkr.Null;
	public void Xtn_write(Xoa_app app, Xoh_html_wtr html_wtr, Xoh_opts opts, Xop_ctx ctx, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		Xow_wiki wiki = ctx.Wiki();
		wiki.Html_mgr().Gallery_xtn_mgr().Write_html(app, wiki, ctx, ctx.Page(), html_wtr, opts, bfr, src, xnde, depth); 
	}
	public static final byte Xatr_id_perrow = 0, Xatr_id_widths = 1, Xatr_id_heights = 2, Xatr_id_mode = 3;
	public static final int Default = 120, Null = -1;
	public static final byte[] Gallery_mode_packed = ByteAry_.new_ascii_("packed");
	public static final String Fsdb_cfg_grp = "xowa", Fsdb_cfg_key_gallery_fix_defaults = "gallery.fix_defaults";
}

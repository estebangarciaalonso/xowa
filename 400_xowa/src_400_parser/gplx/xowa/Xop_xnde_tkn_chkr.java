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
public class Xop_xnde_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_xnde_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_xnde;}
	public String Xnde_tagId() {return xnde_tagId;}
	public Xop_xnde_tkn_chkr Xnde_tagId_(int v) {xnde_tagId = Xop_xnde_tag_.Ary[v].Name_str(); return this;} private String xnde_tagId = null;
	public Tst_chkr Xnde_data() {return xnde_data;} public Xop_xnde_tkn_chkr Xnde_data_(Tst_chkr v) {xnde_data = v; return this;} Tst_chkr xnde_data = null;
	public byte CloseMode() {return closeMode;} public Xop_xnde_tkn_chkr CloseMode_(byte v) {closeMode = v; return this;} private byte closeMode = Xop_xnde_tkn.CloseMode_null;		
	public Xop_xnde_tkn_chkr Name_rng_(int bgn, int end) {name_bgn = bgn; name_end = end; return this;} private int name_bgn = String_.Neg1_pos; int name_end = String_.Neg1_pos;
	public Xop_xnde_tkn_chkr Atrs_rng_(int bgn, int end) {atrs_bgn = bgn; atrs_end = end; return this;} private int atrs_bgn = String_.Neg1_pos; int atrs_end = String_.Neg1_pos;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_xnde_tkn actl = (Xop_xnde_tkn)actl_obj;
		err += mgr.Tst_val(xnde_tagId == null, path, "xnde_tagId", xnde_tagId, Xop_xnde_tag_.Ary[actl.Tag().Id()].Name_str());
		err += mgr.Tst_val(closeMode == Xop_xnde_tkn.CloseMode_null, path, "close_mode", closeMode, actl.CloseMode());
		err += mgr.Tst_val(name_bgn == String_.Neg1_pos, path, "name_bgn", name_bgn, actl.Name_bgn());
		err += mgr.Tst_val(name_end == String_.Neg1_pos, path, "name_end", name_end, actl.Name_end());
		err += mgr.Tst_val(atrs_bgn == String_.Neg1_pos, path, "atrs_bgn", atrs_bgn, actl.Atrs_bgn());
		err += mgr.Tst_val(atrs_end == String_.Neg1_pos, path, "atrs_end", atrs_end, actl.Atrs_end());
		if (xnde_data != null)
			err += mgr.Tst_sub_obj(xnde_data, actl.Xnde_data(), path + "." + "xndeData", err);
		return err;
	}
}
class Xop_tblw_tb_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_tblw_tb_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_tblw_tb;}
	public int Caption_count() {return caption_count;} public Xop_tblw_tb_tkn_chkr Caption_count_(int v) {caption_count = v; return this;} private int caption_count = Int_.Neg1_count;
	public Xop_tblw_tb_tkn_chkr Atrs_rng_(int bgn, int end) {this.atrs_bgn = bgn; this.atrs_end = end; return this;} private int atrs_bgn = Xop_tblw_wkr.Atrs_null, atrs_end = Xop_tblw_wkr.Atrs_null;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_tblw_tb_tkn actl = (Xop_tblw_tb_tkn)actl_obj;
		err += mgr.Tst_val(caption_count == Int_.Neg1_count, path, "caption_count", caption_count, actl.Caption_count());
		err += mgr.Tst_val(atrs_bgn == Xop_tblw_wkr.Atrs_null, path, "atrs_bgn", atrs_bgn, actl.Atrs_bgn());
		err += mgr.Tst_val(atrs_end == Xop_tblw_wkr.Atrs_null, path, "atrs_end", atrs_end, actl.Atrs_end());
		return err;
	}
}
class Xop_tblw_tc_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_tblw_tc_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_tblw_tc;}
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
//			Xop_tblw_tc_tkn actl = (Xop_tblw_tc_tkn)actl_obj;
		return err;
	}
}
class Xop_tblw_td_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_tblw_td_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_tblw_td;}
	public Xop_tblw_td_tkn_chkr Atrs_rng_(int bgn, int end) {this.atrs_bgn = bgn; this.atrs_end = end; return this;} private int atrs_bgn = Xop_tblw_wkr.Atrs_null, atrs_end = Xop_tblw_wkr.Atrs_null;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_tblw_td_tkn actl = (Xop_tblw_td_tkn)actl_obj;
		err += mgr.Tst_val(atrs_bgn == Xop_tblw_wkr.Atrs_null, path, "atrs_bgn", atrs_bgn, actl.Atrs_bgn());
		err += mgr.Tst_val(atrs_end == Xop_tblw_wkr.Atrs_null, path, "atrs_end", atrs_end, actl.Atrs_end());
		return err;
	}
}
class Xop_tblw_tr_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_tblw_tr_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_tblw_tr;}
	public Xop_tblw_tr_tkn_chkr Atrs_rng_(int bgn, int end) {this.atrs_bgn = bgn; this.atrs_end = end; return this;} private int atrs_bgn = Xop_tblw_wkr.Atrs_null, atrs_end = Xop_tblw_wkr.Atrs_null;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_tblw_tr_tkn actl = (Xop_tblw_tr_tkn)actl_obj;
		err += mgr.Tst_val(atrs_bgn == -1, path, "atrs_bgn", atrs_bgn, actl.Atrs_bgn());
		err += mgr.Tst_val(atrs_end == -1, path, "atrs_end", atrs_end, actl.Atrs_end());
		return err;
	}
}
class Xop_tblw_th_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_tblw_th_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_tblw_th;}
	public Xop_tblw_th_tkn_chkr Atrs_rng_(int bgn, int end) {this.atrs_bgn = bgn; this.atrs_end = end; return this;} private int atrs_bgn = Xop_tblw_wkr.Atrs_null, atrs_end = Xop_tblw_wkr.Atrs_null;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_tblw_th_tkn actl = (Xop_tblw_th_tkn)actl_obj;
		err += mgr.Tst_val(atrs_bgn == -1, path, "atrs_bgn", atrs_bgn, actl.Atrs_bgn());
		err += mgr.Tst_val(atrs_end == -1, path, "atrs_end", atrs_end, actl.Atrs_end());
		return err;
	}
}
class Xop_tkn_chkr_lnke extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_lnke_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_lnke;}
	public Xop_tkn_chkr_lnke(int bgn, int end) {super.Src_rng_(bgn, end);}
	public byte Lnke_typ() {return lnke_typ;} public Xop_tkn_chkr_lnke Lnke_typ_(byte v) {lnke_typ = v; return this;} private byte lnke_typ = Xop_lnke_tkn.Lnke_typ_null;
	public Xop_tkn_chkr_lnke Lnke_rng_(int bgn, int end) {lnke_bgn = bgn; lnke_end = end; return this;} private int lnke_bgn = -1; int lnke_end = -1;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_lnke_tkn actl = (Xop_lnke_tkn)actl_obj;
		err += mgr.Tst_val(lnke_typ == Xop_lnke_tkn.Lnke_typ_null, path, "lnke_typ", lnke_typ, actl.Lnke_typ());
		err += mgr.Tst_val(lnke_bgn == -1, path, "lnke_bgn", lnke_bgn, actl.Lnke_bgn());
		err += mgr.Tst_val(lnke_end == -1, path, "lnke_end", lnke_end, actl.Lnke_end());
		return err;
	}
}
class Xop_para_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_para_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_para;}
	public byte Para_end() {return para_end;} public Xop_para_tkn_chkr Para_typeId_(byte v) {para_end = v; return this;} private byte para_end = Byte_.MaxValue_127;
	public byte Para_bgn() {return para_bgn;} public Xop_para_tkn_chkr Para_bgn_(byte v) {para_bgn = v; return this;} private byte para_bgn = Byte_.MaxValue_127;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_para_tkn actl = (Xop_para_tkn)actl_obj;
		err += mgr.Tst_val(para_end == Byte_.MaxValue_127, path, "para_end", para_end, actl.Para_end());
		err += mgr.Tst_val(para_bgn == Byte_.MaxValue_127, path, "para_bgn", para_bgn, actl.Para_bgn());
		return err;
	}
}
class Xop_nl_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_nl_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_newLine;}
	public byte Nl_typeId() {return nl_typeId;} public Xop_nl_tkn_chkr Nl_typeId_(byte v) {nl_typeId = v; return this;} private byte nl_typeId = Xop_nl_tkn.Tid_unknown;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_nl_tkn actl = (Xop_nl_tkn)actl_obj;
		err += mgr.Tst_val(nl_typeId == Xop_nl_tkn.Tid_unknown, path, "nl_typeId", nl_typeId, actl.Nl_typeId());
		return err;
	}
}
class Xop_list_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_list_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_list;}
	public int	List_uid() {return list_uid;} public Xop_list_tkn_chkr List_uid_(int v) {list_uid = v; return this;} private int list_uid = -1;
	public byte List_bgn() {return list_bgn;} public Xop_list_tkn_chkr List_bgn_(byte v) {list_bgn = v; return this;} private byte list_bgn;
	public byte List_itmTyp() {return list_itmTyp;} public Xop_list_tkn_chkr List_itmTyp_(byte v) {list_itmTyp = v; return this;} private byte list_itmTyp = Xop_list_tkn_.List_itmTyp_null;
	public int[] List_path() {return list_path;} public Xop_list_tkn_chkr List_path_(int... v) {list_path = v; return this;} private int[] list_path = Int_.Ary_empty;
	public byte List_sub_last() {return list_sub_last;} public Xop_list_tkn_chkr List_sub_last_(byte v) {list_sub_last = v; return this;} private byte list_sub_last = Bool_.__byte;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_list_tkn actl = (Xop_list_tkn)actl_obj;
		err += mgr.Tst_val(list_uid == -1, path, "list_uid", list_uid, actl.List_uid());
		err += mgr.Tst_val(list_bgn == 0, path, "list_bgn", list_bgn, actl.List_bgn());
		err += mgr.Tst_val(list_itmTyp == Xop_list_tkn_.List_itmTyp_null, path, "list_itmTyp", list_itmTyp, actl.List_itmTyp());
		err += mgr.Tst_val(list_sub_last == Bool_.__byte, path, "list_sub_last", list_sub_last, actl.List_sub_last());
		err += mgr.Tst_val(list_path == Int_.Ary_empty, path, "list_path", Array_.XtoStr(list_path), Array_.XtoStr(actl.List_path()));
		return err;
	}
}
class Xop_hdr_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_hdr_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_hdr;}
	public int Hdr_len() {return hdr_len;} public Xop_hdr_tkn_chkr Hdr_len_(int v) {hdr_len = v; return this;} private int hdr_len = -1;
	public int Hdr_ws_bgn() {return hdr_ws_bgn;} public Xop_hdr_tkn_chkr Hdr_ws_bgn_(int v) {hdr_ws_bgn = v; return this;} private int hdr_ws_bgn = -1;
	public int Hdr_ws_end() {return hdr_ws_end;} public Xop_hdr_tkn_chkr Hdr_ws_end_(int v) {hdr_ws_end = v; return this;} private int hdr_ws_end = -1;
	public int Hdr_ws_trailing() {return hdr_ws_trailing;} public Xop_hdr_tkn_chkr Hdr_ws_trailing_(int v) {hdr_ws_trailing = v; return this;} private int hdr_ws_trailing = -1;	
	public Xop_hdr_tkn_chkr Hdr_html_id_(String v) {hdr_html_id = ByteAry_.new_ascii_(v); return this;} private byte[] hdr_html_id = ByteAry_.Empty;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_hdr_tkn actl = (Xop_hdr_tkn)actl_obj;
		err += mgr.Tst_val(hdr_len == -1, path, "hdr_len", hdr_len, actl.Hdr_len());
		err += mgr.Tst_val(hdr_html_id == ByteAry_.Empty, path, "hdr_html_id", String_.new_ascii_(hdr_html_id), String_.new_ascii_(actl.Hdr_html_id()));
		return err;
	}
}
class Xop_html_ref_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_html_ref_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_html_ref;}
	public String Html_ref_key() {return html_ref_key;} public Xop_html_ref_tkn_chkr Html_ref_key_(String v) {html_ref_key = v; return this;} private String html_ref_key;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_html_ref_tkn actl = (Xop_html_ref_tkn)actl_obj;
		err += mgr.Tst_val(html_ref_key == null, path, "html_ref_key", html_ref_key, String_.new_utf8_(actl.HtmlRef_itm().Xml_name_bry()));
		return err;
	}
}
class Xop_html_ncr_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_html_ncr_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_html_ncr;}
	public int Html_ncr_val() {return html_ncr_val;} public Xop_html_ncr_tkn_chkr Html_ncr_val_(int v) {html_ncr_val = v; return this;} private int html_ncr_val = -1;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_html_ncr_tkn actl = (Xop_html_ncr_tkn)actl_obj;
		err += mgr.Tst_val(html_ncr_val == -1, path, "html_ncr_val", html_ncr_val, actl.Html_ncr_val());
		return err;
	}
}
class Xop_apos_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_apos_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_apos;}
	public int Apos_cmd() {return apos_cmd;} public Xop_apos_tkn_chkr Apos_cmd_(int v) {apos_cmd = v; return this;} private int apos_cmd = Xop_apos_tkn_.Cmd_nil;
	public int Apos_lit() {return apos_lit;} public Xop_apos_tkn_chkr Apos_lit_(int v) {apos_lit = v; return this;} private int apos_lit = -1;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_apos_tkn actl = (Xop_apos_tkn)actl_obj;
		err += mgr.Tst_val(apos_cmd == Xop_apos_tkn_.Cmd_nil, path, "apos_cmd", Xop_apos_tkn_.Cmd_str(apos_cmd), Xop_apos_tkn_.Cmd_str(actl.Apos_cmd()));
		err += mgr.Tst_val(apos_lit == -1, path, "apos_lit", apos_lit, actl.Apos_lit());
		return err;
	}
}
class Xop_ignore_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_ignore_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_ignore;}
	public byte Ignore_type() {return ignore_type;} public Xop_ignore_tkn_chkr Ignore_tid_(byte v) {ignore_type = v; return this;} private byte ignore_type = Xop_ignore_tkn.Ignore_tid_null;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_ignore_tkn actl = (Xop_ignore_tkn)actl_obj;
		err += mgr.Tst_val(ignore_type == Xop_ignore_tkn.Ignore_tid_null, path, "ignore_type", ignore_type, actl.Ignore_type());
		return err;
	}
}
class Xop_lnki_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_lnki_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_lnki;}
	public int Ns_id() {return nsId;} public Xop_lnki_tkn_chkr Ns_id_(int v) {nsId = v; return this;} private int nsId = Int_.MinValue;
	public byte ImgType() {return imgType;} public Xop_lnki_tkn_chkr ImgType_(byte v) {imgType = v; return this;} private byte imgType = Byte_.MaxValue_127;
	public int Width() {return width;} public Xop_lnki_tkn_chkr Width_(int v) {width = v; return this;} private int width = Int_.MinValue;
	public int Height() {return height;} public Xop_lnki_tkn_chkr Height_(int v) {height = v; return this;} private int height = Int_.MinValue;
	public byte HAlign() {return hAlign;} public Xop_lnki_tkn_chkr HAlign_(byte v) {hAlign = v; return this;} private byte hAlign = Byte_.MaxValue_127;
	public byte VAlign() {return vAlign;} public Xop_lnki_tkn_chkr VAlign_(byte v) {vAlign = v; return this;} private byte vAlign = Byte_.MaxValue_127;
	public byte Border() {return border;} public Xop_lnki_tkn_chkr Border_(byte v) {border = v; return this;} private byte border = Bool_.__byte;
	public double Upright() {return upright;} public Xop_lnki_tkn_chkr Upright_(double v) {upright = v; return this;} double upright = Xop_lnki_tkn.Upright_null;
	public int Thumbtime() {return thumbtime;} public Xop_lnki_tkn_chkr Thumbtime_(int v) {thumbtime = v; return this;} int thumbtime = Xop_lnki_tkn.Thumbtime_null;
	public int Page() {return page;} public Xop_lnki_tkn_chkr Page_(int v) {page = v; return this;} int page = Xop_lnki_tkn.Page_null;
	public int Tail_bgn() {return tail_bgn;} public Xop_lnki_tkn_chkr Tail_bgn_(int v) {tail_bgn = v; return this;} private int tail_bgn = String_.Neg1_pos;
	public int Tail_end() {return tail_end;} public Xop_lnki_tkn_chkr Tail_end_(int v) {tail_end = v; return this;} private int tail_end = String_.Neg1_pos;
	public Xop_tkn_chkr_base Trg_tkn() {return trg_tkn;} public Xop_lnki_tkn_chkr Trg_tkn_(Xop_tkn_chkr_base v) {trg_tkn = v; return this;} private Xop_tkn_chkr_base trg_tkn;
	public Xop_tkn_chkr_base Caption_tkn() {return caption_tkn;} public Xop_lnki_tkn_chkr Caption_tkn_(Xop_tkn_chkr_base v) {caption_tkn = v; return this;} private Xop_tkn_chkr_base caption_tkn;
	public Xop_tkn_chkr_base Alt_tkn() {return alt_tkn;} public Xop_lnki_tkn_chkr Alt_tkn_(Xop_tkn_chkr_base v) {alt_tkn = v; return this;} private Xop_tkn_chkr_base alt_tkn;
	public Xop_tkn_chkr_base Link_tkn() {return link_tkn;} public Xop_lnki_tkn_chkr Link_tkn_(Xop_tkn_chkr_base v) {link_tkn = v; return this;} private Xop_tkn_chkr_base link_tkn;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_lnki_tkn actl = (Xop_lnki_tkn)actl_obj;
		err += mgr.Tst_val(nsId == Int_.MinValue, path, "nsId", nsId, actl.Ns_id());
		err += mgr.Tst_val(imgType == Byte_.MaxValue_127, path, "imgType", imgType, actl.Lnki_type());
		err += mgr.Tst_val(width == Int_.MinValue, path, "width", width, actl.Width());
		err += mgr.Tst_val(height == Int_.MinValue, path, "height", height, actl.Height());
		err += mgr.Tst_val(hAlign == Byte_.MaxValue_127, path, "halign", hAlign, actl.HAlign());
		err += mgr.Tst_val(vAlign == Byte_.MaxValue_127, path, "valign", vAlign, actl.VAlign());
		err += mgr.Tst_val(border == Bool_.__byte, path, "border", border, actl.Border());
		err += mgr.Tst_val(tail_bgn == String_.Neg1_pos, path, "tail_bgn", tail_bgn, actl.Tail_bgn());
		err += mgr.Tst_val(tail_end == String_.Neg1_pos, path, "tail_end", tail_end, actl.Tail_end());
		err += mgr.Tst_val(upright == Xop_lnki_tkn.Upright_null, path, "upright", upright, actl.Upright());
		err += mgr.Tst_val(thumbtime == Xop_lnki_tkn.Thumbtime_null, path, "thumbtime", thumbtime, actl.Thumbtime());
		err += mgr.Tst_val(page == Xop_lnki_tkn.Page_null, path, "page", page, actl.Page());
		if (trg_tkn != null) err += mgr.Tst_sub_obj(trg_tkn, actl.Trg_tkn(), path + "." + "trg", err);
		if (caption_tkn != null) err += mgr.Tst_sub_obj(caption_tkn, actl.Caption_tkn(), path + "." + "caption", err);
		if (alt_tkn != null) err += mgr.Tst_sub_obj(alt_tkn, actl.Alt_tkn(), path + "." + "alt", err);
		if (link_tkn != null) err += mgr.Tst_sub_obj(link_tkn, actl.Link_tkn(), path + "." + "link", err);
		return err;
	}
}
class Xop_arg_itm_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Arg_itm_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_arg_itm;}
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
//			Arg_itm_tkn actl = (Arg_itm_tkn)actl_obj;
		return err;
	}
}
class Xop_arg_nde_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Arg_nde_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_arg_nde;}
	public Xop_tkn_chkr_base Key_tkn() {return key_tkn;} public Xop_arg_nde_tkn_chkr Key_tkn_(Xop_tkn_chkr_base v) {key_tkn = v; return this;} private Xop_tkn_chkr_base key_tkn;
	public Xop_tkn_chkr_base Val_tkn() {return val_tkn;} public Xop_arg_nde_tkn_chkr Val_tkn_(Xop_tkn_chkr_base v) {val_tkn = v; return this;} private Xop_tkn_chkr_base val_tkn;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Arg_nde_tkn actl = (Arg_nde_tkn)actl_obj;
		if (key_tkn != null) err += mgr.Tst_sub_obj(key_tkn, actl.Key_tkn(), path + "." + "key", err);
		if (val_tkn != null) err += mgr.Tst_sub_obj(val_tkn, actl.Val_tkn(), path + "." + "val", err);
		return err;
	}
}
class Xop_tkn_chkr_hr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xop_hr_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_hr;}
	public Xop_tkn_chkr_hr(int bgn, int end) {super.Src_rng_(bgn, end);}
	public int Hr_len() {return hr_len;} public Xop_tkn_chkr_hr Hr_len_(int v) {hr_len = v; return this;} private int hr_len = -1;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xop_hr_tkn actl = (Xop_hr_tkn)actl_obj;
		err += mgr.Tst_val(hr_len == -1, path, "hr_len", hr_len, actl.Hr_len());
		return err;
	}
}
class Xot_invk_tkn_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xot_invk_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_tmpl_invk;}
	public Xop_tkn_chkr_base Name_tkn() {return name_tkn;} public Xot_invk_tkn_chkr Name_tkn_(Xop_tkn_chkr_base v) {name_tkn = v; return this;} private Xop_tkn_chkr_base name_tkn;
	public Xop_tkn_chkr_base[] Args() {return args;} public Xot_invk_tkn_chkr Args_(Xop_tkn_chkr_base... v) {args = v; return this;} private Xop_tkn_chkr_base[] args = Xop_tkn_chkr_base.Ary_empty;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xot_invk_tkn actl = (Xot_invk_tkn)actl_obj;
		if (name_tkn != null) err += mgr.Tst_sub_obj(name_tkn, actl.Name_tkn(), path + "." + "name", err);
		err += mgr.Tst_sub_ary(args, Args_xtoAry(actl), path + "." + "args", err);
		return err;
	}
	Arg_nde_tkn[] Args_xtoAry(Xot_invk_tkn tkn) {
		int args_len = tkn.Args_len();
		Arg_nde_tkn[] rv = new Arg_nde_tkn[args_len];
		for (int i = 0; i < args_len; i++)
			rv[i] = tkn.Args_get_by_idx(i);
		return rv;
	}
}
class Xot_prm_chkr extends Xop_tkn_chkr_base {
	@Override public Class<?> TypeOf() {return Xot_prm_tkn.class;}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_tmpl_prm;}
	public Xop_tkn_chkr_base Find_tkn() {return find_tkn;} public Xot_prm_chkr Find_tkn_(Xop_arg_itm_tkn_chkr v) {find_tkn = v; return this;} private Xop_arg_itm_tkn_chkr find_tkn;
	public Xop_tkn_chkr_base[] Args() {return args;} public Xot_prm_chkr Args_(Xop_tkn_chkr_base... v) {args = v; return this;} private Xop_tkn_chkr_base[] args = Xop_tkn_chkr_base.Ary_empty;
	@Override public int Chk_hook(Tst_mgr mgr, String path, Object actl_obj, int err) {
		Xot_prm_tkn actl = (Xot_prm_tkn)actl_obj;
		if (find_tkn != null) err += mgr.Tst_sub_obj(find_tkn, actl.Find_tkn(), path + "." + "find", err);
		return err;
	}
}

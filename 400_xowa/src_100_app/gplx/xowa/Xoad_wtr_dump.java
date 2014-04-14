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
public class Xoad_wtr_dump {
	public Xoad_wtr_dump(Io_url log_dir) {this.log_dir = log_dir;} Io_url log_dir;
	//Xoad_dump_itm[] fil_ary = new Xoad_dump_itm[64]; int fil_ary_len = 0;
	int fil_ary_size = 0; 
	final int bfr_ary_size_max = 1024 * 1024 * 1;
	public void Write(byte[] ttl, int page_idx, Gfo_msg_log msg_log) {
		int ary_len = msg_log.Ary_len(); boolean flush_me = false;
		for (int i = 0; i < ary_len; i++) {
			Gfo_msg_data data = msg_log.Ary_get(i);
			Xoad_dump_itm rv = GetByItm(data);
			if (rv.BfrLen() > bfr_ary_size_max) flush_me = true;
			fil_ary_size += rv.Write(ttl, page_idx, data);
		}
		if (fil_ary_size > bfr_ary_size_max || flush_me)
			Flush();
	}
	public void Flush() {
		int len = dump_itms.Count();
		for (int i = 0; i < len; i++) {
			Xoad_dump_itm data = (Xoad_dump_itm)dump_itms.FetchAt(i);
			if (data != null) {
				data.Flush(log_dir);
				data.Rls();
			}
		}
		dump_itms.Clear();
		fil_ary_size = 0;	
	}
	Xoad_dump_itm GetByItm(Gfo_msg_data data) {
		byte[] path_bry = data.Item().Path_bry();
		Xoad_dump_itm rv = (Xoad_dump_itm)dump_itms.Fetch(path_bry);
		if (rv == null) {
			rv = new Xoad_dump_itm(path_bry, data.Item().Key_bry());
			dump_itms.Add(path_bry, rv);
		}
		return rv;
	}	private OrderedHash dump_itms = OrderedHash_.new_bry_();
}
class Xoad_dump_itm {
	ByteAryBfr bfr = new ByteAryBfr(4096);
	int pageIdx_last;
	public int BfrLen() {return bfr.Len();}
	public Xoad_dump_itm(byte[] ownerKey, byte[] itmKey) {
		fil_name = String_.new_utf8_(ownerKey) + "__" + String_.new_utf8_(itmKey);
	}	String fil_name;
	public void Flush(Io_url log_dir) {
		Io_url fil_url = log_dir.GenSubFil_ary(fil_name, ".txt");
		Io_mgr._.AppendFilByt(fil_url, bfr.Bry(), bfr.Len());
		bfr.Reset_if_gt(Io_mgr.Len_kb);
	}
	public void Rls() {
		bfr.Rls();
		bfr = null;
	}
	public int Write(byte[] ttl, int page_idx, Gfo_msg_data eny) {
		int old = bfr.Len();
		if (page_idx != pageIdx_last) {pageIdx_last = page_idx; bfr.Add(ttl).Add_byte_nl();}
		bfr.Add_byte_repeat(Byte_ascii.Space, 4);
		byte[] src = eny.Src_bry();
		if (src.length != 0) {
			int mid_bgn = eny.Src_bgn(), mid_end = eny.Src_end();
			int all_bgn = mid_bgn - 40; if (all_bgn < 0)			all_bgn = 0;
			int all_end = mid_end + 40; if (all_end > src.length)	all_end = src.length;
			Write_mid(src, all_bgn, mid_bgn);
			bfr.Add_byte(Byte_ascii.Tilde).Add_byte(Byte_ascii.Num_0).Add_byte(Byte_ascii.Tilde);
			Write_mid(src, mid_bgn, mid_end);
			bfr.Add_byte(Byte_ascii.Tilde).Add_byte(Byte_ascii.Num_1).Add_byte(Byte_ascii.Tilde);
			Write_mid(src, mid_end, all_end);
			bfr.Add_byte_nl();
		}
		return bfr.Len() - old;
	}
	private void Write_mid(byte[] src, int bgn, int end) {
		if (end - bgn == 0) return;
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.NewLine:	bfr.Add_byte(Byte_ascii.Backslash); bfr.Add_byte(Byte_ascii.Ltr_n); break;
				case Byte_ascii.Tab:		bfr.Add_byte(Byte_ascii.Backslash); bfr.Add_byte(Byte_ascii.Ltr_t); break;
				default:					bfr.Add_byte(b); break;
			}
		}
	}
}
class App_log_wtr_html implements ByteAryFmtrArg {
	ByteAryFmtr tmp_fmtr = ByteAryFmtr.tmp_(); ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255);
	public void XferAry(ByteAryBfr trg, int idx) {
		int ary_len = msg_log.Ary_len();
		for (int i = 0; i < ary_len; i++) {
			Gfo_msg_data eny = msg_log.Ary_get(i);
			byte[] excerpt_mid = BuildExcerpt(eny);
			if (eny.Vals() != null) {
				tmp_fmtr.Fmt_(eny.Item().Fmt());
				tmp_fmtr.Bld_bfr_ary(tmp_bfr, eny.Vals());
			}
			itm_fmtr.Bld_bfr_many(html_bfr, page.Ttl().Page_txt(), eny.Item().Path_bry(), eny.Item().Key_bry(), Gfo_msg_itm_.CmdBry[eny.Item().Cmd()], tmp_bfr.XtoAryAndClear(), excerpt_mid);
			msg_bfr.Clear();
			excerpt_bfr.Clear();
		}
	}

	public int Excerpt_pad() {return excerpt_pad;} private int excerpt_pad = 10;
	public int Warn_len() {return warn_len;} private int warn_len = 0;
	public byte[] Html() {return html;} private byte[] html = null;
	public byte[] Css() {return css;}
	byte[] css = ByteAry_.new_ascii_(String_.Concat_lines_nl
		(	".xowa_log_cmd_warn {border-left:solid 20px tomato; border-right:10px solid white; padding:0px;}"
		,	".xowa_log_excerpt {font-family:courier; background; border-right:10px solid white; background:whitesmoke; padding:0px;}"
		,	".xowa_log_excerpt_span {color:red;}"
		));
	ByteAryBfr msg_bfr = ByteAryBfr.new_(), html_bfr = ByteAryBfr.new_(), excerpt_bfr = ByteAryBfr.new_();
	ByteAryFmtr tbl_fmtr = ByteAryFmtr.new_(String_.Concat_lines_nl
		(	"<table style='border-spacing:2px;'>"
		,	"~{0}</table>"
		));
	ByteAryFmtr itm_fmtr = ByteAryFmtr.new_(String_.Concat_lines_nl
		(	"  <tr>"
		,	"    <td class='xowa_log_cmd_~{itm_cmd}'>~{owner_path}</td>"
		,	"    <td class='xowa_log_msg'>~{itm_msg}</td>"
		,	"    <td class='xowa_log_excerpt'>~{itm_excerpt_all}</td>"
//			,	"    <td class='xowa_log_todo'>/ ~{owner_path}: ~{page_name}; ~{itm_key} \"~{itm_excerpt_mid}\"</td>"
		,	"  </tr>"
		)
		,	"page_name", "owner_path", "itm_key", "itm_cmd", "itm_msg", "itm_excerpt_all", "itm_excerpt_mid"
		);
	ByteAryFmtr excerpt_fmtr = ByteAryFmtr.new_("~{0}<span class='xowa_log_excerpt_span'>~{1}</span>~{2}");
	public void Write(Xoa_page page, Gfo_msg_log msg_log) {
		this.page = page; this.msg_log = msg_log;
		tbl_fmtr.Bld_bfr(html_bfr, this);
		html = html_bfr.XtoAryAndClear();
		msg_log.Clear();
		this.page = null; this.msg_log = null;
	}	private Xoa_page page; Gfo_msg_log msg_log;
	private byte[] BuildExcerpt(Gfo_msg_data eny) {
		if (eny.Src_bgn() == -1) return ByteAry_.Empty; // added via String message; no src excerpt available;
		byte[] src = eny.Src_bry();
		int src_len = src.length;
		int mid_bgn = eny.Src_bgn(), mid_end = eny.Src_end();
		int all_bgn = mid_bgn - excerpt_pad; if (all_bgn < 0)			all_bgn = 0;
		int all_end = mid_end + excerpt_pad; if (all_end > src_len)		all_end = src.length;
		if (mid_bgn >= src_len) mid_bgn = src_len;
		if (mid_end >= src_len) mid_end = src_len;
		byte[] bry_bgn = BuildBry(excerpt_bfr, src, all_bgn, mid_bgn);
		byte[] bry_mid = BuildBry(excerpt_bfr, src, mid_bgn, mid_end);
		byte[] bry_end = BuildBry(excerpt_bfr, src, mid_end, all_end);
		excerpt_fmtr.Bld_bfr(excerpt_bfr, bry_bgn, bry_mid, bry_end);
		return excerpt_bfr.XtoAryAndClear();
	}
	private byte[] BuildBry(ByteAryBfr bfr, byte[] src, int bgn, int end) {
		if (end - bgn == 0) return ByteAry_.Empty;
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.NewLine:	bfr.Add(Bry_br); break;
				case Byte_ascii.Space:		bfr.Add(Bry_nbsp); break;
				case Byte_ascii.Lt:			bfr.Add(Bry_lt); break;
				case Byte_ascii.Gt:			bfr.Add(Xop_xnde_wkr.Bry_escape_gt); break;
				case Byte_ascii.Amp:		bfr.Add(Xop_xnde_wkr.Bry_escape_amp); break;
				case Byte_ascii.Quote:		bfr.Add(Xop_xnde_wkr.Bry_escape_quote); break;
				default:					bfr.Add_byte(b); break;
			}
		}
		return bfr.XtoAryAndClear();
	}
	private static final byte[] Bry_br = ByteAry_.new_ascii_("<br/>"), Bry_nbsp = ByteAry_.new_ascii_("&nbsp;"), Bry_lt = ByteAry_.new_ascii_("&lt;");
}

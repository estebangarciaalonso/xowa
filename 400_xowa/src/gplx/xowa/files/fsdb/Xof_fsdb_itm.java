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
package gplx.xowa.files.fsdb; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
public class Xof_fsdb_itm {
	public byte[]		Lnki_ttl() {return lnki_ttl;} private byte[] lnki_ttl;
	public byte[]		Lnki_md5() {return lnki_md5;} private byte[] lnki_md5;
	public Xof_ext		Lnki_ext() {return lnki_ext;} private Xof_ext lnki_ext;
	public byte			Lnki_type() {return lnki_type;} private byte lnki_type;
	public byte			Lnki_type_as_mode() {return lnki_type_as_mode;} private byte lnki_type_as_mode;
	public int			Lnki_w() {return lnki_w;} public Xof_fsdb_itm Lnki_w_(int v) {lnki_w = v; return this;} private int lnki_w;
	public int			Lnki_h() {return lnki_h;} public Xof_fsdb_itm Lnki_h_(int v) {lnki_h = v; return this;} private int lnki_h;
	public double		Lnki_upright() {return lnki_upright;} private double lnki_upright;
	public int			Lnki_thumbtime() {return lnki_thumbtime;} public Xof_fsdb_itm Lnki_thumbtime_(int v) {lnki_thumbtime = v; return this;} private int lnki_thumbtime;
	public Xof_fsdb_itm	Init_by_lnki(byte[] lnki_ttl, Xof_ext ext, byte[] md5, byte lnki_type, int lnki_w, int lnki_h, double lnki_upright, int lnki_thumbtime) {			
		this.lnki_ttl = lnki_ttl; this.lnki_ext = ext; this.lnki_md5 = md5;
		this.lnki_w = lnki_w; this.lnki_h = lnki_h; this.lnki_upright = lnki_upright; this.lnki_thumbtime = lnki_thumbtime;
		this.Lnki_type_(lnki_type);
		this.orig_ttl = lnki_ttl;
		return this;
	}
	public void Lnki_type_(byte v) {
		this.lnki_type = v;
		this.file_is_orig = !(Xop_lnki_type.Id_defaults_to_thumb(lnki_type) || lnki_w != Xop_lnki_tkn.Width_null || lnki_h != Xop_lnki_tkn.Height_null);
		this.lnki_type_as_mode = file_is_orig ? Xof_repo_itm.Mode_orig : Xof_repo_itm.Mode_thumb;
	} 
	public Xof_fsdb_itm	Lnki_ttl_(byte[] v) {
		lnki_ttl = v;
		lnki_ext = Xof_ext_.new_by_ttl_(v);
		lnki_md5 = Xof_xfer_itm.Md5_calc(v);
		return this;
	}
	public Xof_fsdb_itm	Init_by_redirect(byte[] redirect_ttl) {
		orig_redirect = redirect_ttl;
		lnki_ttl = redirect_ttl;
		lnki_ext = Xof_ext_.new_by_ttl_(lnki_ttl);
		lnki_md5 = Xof_xfer_itm.Md5_calc(lnki_ttl);
		return this;
	}
	public int Orig_w() {return orig_w;} private int orig_w = Xop_lnki_tkn.Width_null;
	public int Orig_h() {return orig_h;} private int orig_h = Xop_lnki_tkn.Height_null;
	public Xof_fsdb_itm Orig_size_(int w, int h) {orig_w = w; orig_h = h; return this;} 
	public byte[] Orig_wiki() {return orig_wiki;} public Xof_fsdb_itm Orig_wiki_(byte[] v) {orig_wiki = v; return this;} private byte[] orig_wiki;
	public byte[] Orig_ttl() {return orig_ttl;} public Xof_fsdb_itm Orig_ttl_(byte[] v) {orig_ttl = v; return this;} private byte[] orig_ttl;
	public byte[] Orig_redirect() {return orig_redirect;} public Xof_fsdb_itm Orig_redirect_(byte[] v) {orig_redirect = v; return this;} private byte[] orig_redirect = ByteAry_.Empty;
	public byte Orig_repo() {return orig_repo;} public Xof_fsdb_itm Orig_repo_(byte v) {orig_repo = v; return this;} private byte orig_repo = Xof_repo_itm.Repo_null;
	public int Reg_id() {return reg_id;} public Xof_fsdb_itm Reg_id_(int v) {reg_id = v; return this;} private int reg_id;
	public Io_url Html_url() {return html_url;} public Xof_fsdb_itm Html_url_(Io_url v) {html_url = v; return this;} private Io_url html_url;
	public int Html_w() {return html_w;} private int html_w;
	public int Html_h() {return html_h;} private int html_h;
	public Xof_fsdb_itm Html_size_(int w, int h) {html_w = w; html_h = h; return this;} 
	public boolean File_is_orig() {return file_is_orig;} public Xof_fsdb_itm File_is_orig_(boolean v) {file_is_orig = v; return this;} private boolean file_is_orig;
	public int File_w() {return file_w;} private int file_w;
	public void Html_size_calc(Xof_img_size img_size, byte exec_tid) {
		img_size.Html_size_calc(exec_tid, lnki_w, lnki_h, lnki_type, lnki_upright, lnki_ext.Id(), orig_w, orig_h, Xof_img_size.Thumb_width_img);
		html_w = img_size.Html_w(); html_h = img_size.Html_h(); file_w = img_size.File_w();
		file_is_orig = img_size.File_is_orig();
	}
	public int Html_ids_len() {return html_ids_len;} private int html_ids_len;
	public int Html_ids_get(int idx) {
		switch (html_ids_len) {
			case 0: return -1;// throw Err_.new_fmt_("no html ids available: {0}", String_.new_utf8_(lnki_key));
			case 1: return html_ids_itm_0;
			default: return html_ids_ary[idx];
		}
	}
	public void Html_ids_add(int v) {
		switch (html_ids_len) {
			case 0:		html_ids_itm_0 = v; break;
			case 1:	
				Html_ids_expand(4);
				html_ids_ary[0] = html_ids_itm_0;
				html_ids_ary[1] = v;
				break;
			default:
				if (html_ids_len == html_ids_ary_max) Html_ids_expand(html_ids_ary_max * 2);
				html_ids_ary[html_ids_len] = v;
				break;
		}
		++html_ids_len;
	}	private int html_ids_itm_0; private int[] html_ids_ary; int html_ids_ary_max = 0;
	private void Html_ids_expand(int new_len) {
		int[] new_ary = new int[new_len];
		for (int i = 0; i < html_ids_len; i++)
			new_ary[i] = html_ids_ary[i];
		html_ids_ary = new_ary;
		html_ids_ary_max = new_len;
	}
	public byte Rslt_reg() {return rslt_reg;} public Xof_fsdb_itm Rslt_reg_(byte v) {this.rslt_reg = v; return this;} private byte rslt_reg = gplx.xowa.files.regs.Xof_reg_wkr_.Tid_null;
	public byte Rslt_qry() {return rslt_qry;} public Xof_fsdb_itm Rslt_qry_(byte v) {this.rslt_qry = v; return this;} private byte rslt_qry;
	public byte Rslt_bin() {return rslt_bin;} public Xof_fsdb_itm Rslt_bin_(byte v) {this.rslt_bin = v; return this;} private byte rslt_bin;
	public byte Rslt_cnv() {return rslt_cnv;} public Xof_fsdb_itm Rslt_cnv_(byte v) {this.rslt_cnv = v; return this;} private byte rslt_cnv;
	public boolean Rslt_bin_fsys() {return rslt_bin_fsys;} public Xof_fsdb_itm Rslt_bin_fsys_(boolean v) {rslt_bin_fsys = v; return this;} private boolean rslt_bin_fsys;
}

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
	public byte[]		Lnki_ttl() {return lnki_ttl;} public Xof_fsdb_itm Lnki_ttl_(byte[] v) {lnki_ttl = v; return this;} private byte[] lnki_ttl;
	public byte[]		Lnki_md5() {return lnki_md5;} public Xof_fsdb_itm Lnki_md5_(byte[] v) {lnki_md5 = v; return this;} private byte[] lnki_md5;
	public Xof_ext		Lnki_ext() {return lnki_ext;} public Xof_fsdb_itm Lnki_ext_(Xof_ext v) {lnki_ext = v; return this;} private Xof_ext lnki_ext;
	public byte			Lnki_type() {return lnki_type;} public Xof_fsdb_itm Lnki_type_(byte v) {this.lnki_type = v; return this;} private byte lnki_type;
	public boolean			Lnki_type_is_thumb() {return Xop_lnki_type.Id_is_thumb_like(lnki_type);}
	public int			Lnki_w() {return lnki_w;} public Xof_fsdb_itm Lnki_w_(int v) {lnki_w = v; return this;} private int lnki_w;
	public int			Lnki_h() {return lnki_h;} private int lnki_h;
	public double		Lnki_upright() {return lnki_upright;} private double lnki_upright;
	public int			Lnki_thumbtime() {return lnki_thumbtime;} private int lnki_thumbtime;
	public byte[] Lnki_key() {return lnki_key;} public Xof_fsdb_itm Lnki_key_(byte[] v) {lnki_key = v; return this;} private byte[] lnki_key;
	public Xof_fsdb_itm	Init_by_lnki(Xof_fsdb_itm_key_bldr lnki_key_bldr, byte[] lnki_ttl, byte lnki_type, int lnki_w, int lnki_h, double lnki_upright, int lnki_thumbtime) {
		this.lnki_key = lnki_key_bldr.Bld_as_bry(lnki_ttl, lnki_type, lnki_w, lnki_h, lnki_upright, lnki_thumbtime);
		this.lnki_ttl = lnki_ttl; this.lnki_type = lnki_type; this.lnki_w = lnki_w; this.lnki_h = lnki_h; this.lnki_upright = lnki_upright; this.lnki_thumbtime = lnki_thumbtime;
		return this;
	}
	public int Orig_w() {return orig_w;} private int orig_w;
	public int Orig_h() {return orig_h;} private int orig_h;
	public Xof_fsdb_itm Orig_size_(int w, int h) {orig_w = w; orig_h = h; return this;} 
	public byte[] Orig_wiki() {return orig_wiki;} public Xof_fsdb_itm Orig_wiki_(byte[] v) {orig_wiki = v; return this;} private byte[] orig_wiki;
	public boolean Orig_redirected() {return orig_redirected;} boolean orig_redirected;
	public byte[] Orig_redirected_src() {return orig_redirected_src;} byte[] orig_redirected_src;
	public byte[] Orig_redirected_trg() {return orig_redirected_trg;} byte[] orig_redirected_trg;
	public void Orig_redirect_(byte[] src, byte[] trg) {
		orig_redirected = true; orig_redirected_src = src; orig_redirected_trg = trg;
	} 
	public String Html_url_rel() {return html_url_rel;} public Xof_fsdb_itm Html_url_rel_(String v) {html_url_rel = v; return this;} private String html_url_rel;
	public Io_url Html_url_abs() {return html_url_abs;} public Xof_fsdb_itm Html_url_abs_(Io_url v) {html_url_abs = v; return this;} private Io_url html_url_abs;
	public int Html_w() {return html_w;} private int html_w;
	public int Html_h() {return html_h;} private int html_h;
	public Xof_fsdb_itm Html_size_(int w, int h) {html_w = w; html_h = h; return this;} 
	public byte Rslt_reg() {return rslt_reg;} public Xof_fsdb_itm Rslt_reg_(byte v) {this.rslt_reg = v; return this;} private byte rslt_reg = Xof_reg_wkr_.Tid_null;
	public byte Rslt_qry() {return rslt_qry;} public Xof_fsdb_itm Rslt_qry_(byte v) {this.rslt_qry = v; return this;} private byte rslt_qry;
	public byte Rslt_bin() {return rslt_bin;} public Xof_fsdb_itm Rslt_bin_(byte v) {this.rslt_bin = v; return this;} private byte rslt_bin;
	public byte Rslt_cnv() {return rslt_cnv;} public Xof_fsdb_itm Rslt_cnv_(byte v) {this.rslt_cnv = v; return this;} private byte rslt_cnv;
}
class Xof_reg_wkr_ {
	public static final byte Tid_null = Byte_.MaxValue_127, Tid_noop = 0, Tid_found_url = 1, Tid_missing_reg = 2, Tid_missing_qry = 3, Tid_missing_bin = 4;
}

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
package gplx.srls.dsvs; import gplx.*; import gplx.srls.*;
public class Dsv_tbl_parser implements GfoInvkAble, RlsAble {
	private Dsv_wkr_base mgr;
	private Dsv_fld_parser[] fld_parsers = new Dsv_fld_parser[2];
	public byte[] Src() {return src;} private byte[] src;
	public int Fld_bgn() {return fld_bgn;} private int fld_bgn = 0;
	public int Fld_idx() {return fld_idx;} private int fld_idx = 0;
	public int Row_bgn() {return row_bgn;} private int row_bgn = 0;
	public int Row_idx() {return row_idx;} private int row_idx = 0;
	public boolean Skip_blank_lines() {return skip_blank_lines;} public Dsv_tbl_parser Skip_blank_lines_(boolean v) {skip_blank_lines = v; return this;} private boolean skip_blank_lines = true;
	public byte Fld_dlm() {return fld_dlm;} public Dsv_tbl_parser Fld_dlm_(byte v) {fld_dlm = v; return this;} private byte fld_dlm = Byte_ascii.Pipe;
	public byte Row_dlm() {return row_dlm;} public Dsv_tbl_parser Row_dlm_(byte v) {row_dlm = v; return this;} private byte row_dlm = Byte_ascii.NewLine;
	public void Init(Dsv_wkr_base mgr, Dsv_fld_parser... fld_parsers) {
		this.mgr = mgr;
		this.fld_parsers = fld_parsers;
		int fld_parsers_len = fld_parsers.length;
		for (int i = 0; i < fld_parsers_len; i++)
			fld_parsers[i].Init(fld_dlm, row_dlm);
	}
	public void Clear() {
		fld_bgn = fld_idx = row_bgn = row_idx = 0;
	}
	public Err Err_row_bgn(String fmt, int pos) {
		return Err_.new_fmt_(fmt + "; line={0}", String_.new_utf8_(src, row_bgn, pos));
	}
	public void Update_by_fld(int pos) {
		fld_bgn = pos;
		++fld_idx;
	}
	public void Update_by_row(int pos) {
		row_bgn = fld_bgn = pos;
		++row_idx;
		fld_idx = 0;
	}
	public void Parse(byte[] src) {
		this.src = src;
		int src_len = src.length;			
		int pos = 0;
		while (pos < src_len) {
			if (fld_idx == 0 && skip_blank_lines) {	// row committed; skip blank lines
				while (pos < src_len) {
					if (src[pos] == row_dlm) {
						++pos;
						row_bgn = fld_bgn = pos;
					}
					else
						break;
				}
			}
			Dsv_fld_parser fld_parser = fld_parsers[fld_idx];
			pos = fld_parser.Parse(this, mgr, src, pos, src_len, fld_idx, fld_bgn);
		}
	}
	public void Rls() {
		src = null; fld_parsers = null; mgr = null;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_load_by_str))		Parse(m.ReadBry("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_load_by_str = "load_by_str";
}

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
public class Dsv_fld_parser_ {
	public static final Dsv_fld_parser Bry_parser = Dsv_fld_parser_bry._;
	public static final Dsv_fld_parser Int_parser = Dsv_fld_parser_int._;
	public static Err err_fld_unhandled(Dsv_fld_parser parser, Dsv_wkr_base wkr, int fld_idx, byte[] src, int bgn, int end) {
		throw Err_.new_fmt_("fld unhandled; parser={0} wkr={1} fld_idx={2} val={3}", ClassAdp_.NameOf_obj(parser), ClassAdp_.NameOf_obj(wkr), fld_idx, String_.new_utf8_(src, bgn, end));
	}
}
class Dsv_fld_parser_bry implements Dsv_fld_parser {
	private byte fld_dlm = Byte_ascii.Pipe, row_dlm = Byte_ascii.NewLine;
	public void Init(byte fld_dlm, byte row_dlm) {
		this.fld_dlm = fld_dlm; this.row_dlm = row_dlm;
	}
	public int Parse(Dsv_tbl_parser parser, Dsv_wkr_base wkr, byte[] src, int pos, int src_len, int fld_idx, int fld_bgn) {
		while (true) {
			boolean pos_is_last = pos == src_len;				
			byte b = pos_is_last ? row_dlm : src[pos];
			if		(b == fld_dlm) {
				boolean pass = wkr.Write_bry(parser, fld_idx, src, fld_bgn, pos);
				if (!pass) throw Dsv_fld_parser_.err_fld_unhandled(this, wkr, fld_idx, src, fld_bgn, pos);
				int rv = pos + 1; // fld_dlm is always 1 byte
				parser.Update_by_fld(rv);
				return rv;
			}
			else if (b == row_dlm) {
				boolean pass = wkr.Write_bry(parser, fld_idx, src, fld_bgn, pos);
				if (!pass) throw Dsv_fld_parser_.err_fld_unhandled(this, wkr, fld_idx, src, fld_bgn, pos);
				wkr.Commit_itm(parser, pos);
				int rv = pos + 1; // row_dlm is always 1 byte
				parser.Update_by_row(rv);
				return rv; 
			}
			else
				++pos;
		}
	}
	public static final Dsv_fld_parser_bry _ = new Dsv_fld_parser_bry(); Dsv_fld_parser_bry() {}
}
class Dsv_fld_parser_int implements Dsv_fld_parser {
	private byte fld_dlm = Byte_ascii.Pipe, row_dlm = Byte_ascii.NewLine;
	public void Init(byte fld_dlm, byte row_dlm) {
		this.fld_dlm = fld_dlm; this.row_dlm = row_dlm;
	}
	public int Parse(Dsv_tbl_parser parser, Dsv_wkr_base wkr, byte[] src, int pos, int src_len, int fld_idx, int fld_bgn) {
		while (true) {
			boolean pos_is_last = pos == src_len;				
			byte b = pos_is_last ? row_dlm : src[pos];
			if		(b == fld_dlm) {
				boolean pass = wkr.Write_int(parser, fld_idx, pos, Bry_.Xto_int_or(src, fld_bgn, pos, -1));
				if (!pass) throw Dsv_fld_parser_.err_fld_unhandled(this, wkr, fld_idx, src, fld_bgn, pos);
				int rv = pos + 1; // fld_dlm is always 1 byte
				parser.Update_by_fld(rv);
				return rv;
			}
			else if (b == row_dlm) {
				boolean pass = wkr.Write_int(parser, fld_idx, pos, Bry_.Xto_int_or(src, fld_bgn, pos, -1));
				if (!pass) throw Dsv_fld_parser_.err_fld_unhandled(this, wkr, fld_idx, src, fld_bgn, pos);
				wkr.Commit_itm(parser, pos);
				int rv = pos + 1; // row_dlm is always 1 byte
				parser.Update_by_row(rv);
				return rv; 
			}
			else
				++pos;
		}
	}
	public static final Dsv_fld_parser_int _ = new Dsv_fld_parser_int(); Dsv_fld_parser_int() {}
}

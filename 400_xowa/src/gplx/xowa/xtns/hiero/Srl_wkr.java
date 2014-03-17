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
package gplx.xowa.xtns.hiero; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
/*
.srl.load_by_str();
.srl.init('String', 'escaped', 'number')
Srl_fld_parser custom = custom_ary[fld_idx];
if (custom != null) {
	pos = custom.Parse(itm, src, pos, len);
}
else {
	standard.Parse(itm);
	itm.Fld();
}
if (state.Row_end)
	mgr.Create(i);
else if (state.End())
	break;

void Parse(itm, src, pos, len) {
}
*/
interface Srl_itm {
	void Write_bry(Srl_tbl_parser ctx, int fld_idx, byte[] src, int bgn, int end);
}
class Srl_itm_basic {
	
}
//	class Hiero_phenome_srl {
//		private byte[] key;
//		private byte[] val;
//		public void Init() {
//			// this.Set_flds(bry, bry);
//		}
//		public void Write_bry(Srl_tbl_parser ctx, int fld_idx, byte[] src, int bgn, int end, byte[] fld_bry) {
//			this.key = null;
//			this.val = null;
//		}
//		public void Add() {
////			Hiero_phenome_itm itm = new Hiero_phenome_itm(key, val);
////			mgr.Add(key, itm);
//		}
//	}
class Srl_tbl_parser {
	public int Fld_bgn() {return fld_bgn;} private int fld_bgn = 0;
	public int Fld_idx() {return fld_idx;} private int fld_idx = 0;
	public int Row_idx() {return row_idx;} private int row_idx = 0;
	public void Init(Srl_fld_parser... fld_parsers) {
	}
	public void Clear() {
		fld_bgn = fld_idx = row_idx = 0;
	}
	public void Update_by_fld(int pos) {
		++fld_idx;
	}
	public void Update_by_row(int pos) {
		++row_idx;
		fld_idx = 0;
	}
	private Srl_fld_parser[] parser_ary = new Srl_fld_parser[2];
	public void Parse(Srl_itm itm, byte[] src) {
		int src_len = src.length;			
		int pos = 0;
		while (true) {
			Srl_fld_parser parser = parser_ary[fld_idx];
			pos = parser.Parse(itm, this, src, pos, src_len, fld_idx, fld_bgn);
		}
	}
}
interface Srl_fld_parser {
	int Parse(Srl_itm itm, Srl_tbl_parser ctx, byte[] src, int pos, int src_len, int fld_idx, int fld_bgn);
}
class Srl_fld_parser_bry {
	private byte fld_dlm = Byte_ascii.Pipe, row_dlm = Byte_ascii.NewLine;
	public Srl_fld_parser_bry(byte fld_dlm, byte row_dlm) {
		this.fld_dlm = fld_dlm;
		this.row_dlm = row_dlm;
	}
	public int Parse(Srl_itm itm, Srl_tbl_parser ctx, byte[] src, int pos, int src_len, int fld_idx, int fld_bgn) {
		while (true) {
			boolean pos_is_last = pos == src_len;				
			byte b = pos_is_last ? row_dlm : src[pos];
			if		(b == fld_dlm) {
				itm.Write_bry(ctx, fld_idx, src, fld_bgn, pos);
				ctx.Update_by_fld(pos);
				return pos + 1;	// fld_dlm is always 1 byte
			}
			else if (b == row_dlm) {
				itm.Write_bry(ctx, fld_idx, src, fld_bgn, pos);
				ctx.Update_by_row(pos);
				return pos + 1; // row_dlm is always 1 byte
			}
			else
				++pos;
		}
	}
}
class Srl_parser {
	public void Parse() {
//			byte[] src = ByteAry_.new_utf8_(src_str);
//			int src_len = src.length;			
//			while (true) {
// break;				
//			int row_bgn = 0, fld_bgn = 0, fld_idx = 0;
//			boolean fld_bfr_dirty = false;
//			int src_pos = 0;
//			}
	}
}
//	class Srl_itm {
//		// 
//		public int Fld_bgn() {return fld_bgn;} public Srl_itm Fld_bgn_(int v) {fld_bgn = v; return this;} private int fld_bgn;
//		public int Fld_end() {return fld_end;} public Srl_itm Fld_end_(int v) {fld_end = v; return this;} private int fld_end;
//		public byte[] Fld_bry() {return fld_bry;} public Srl_itm Fld_bry_(byte[] v) {fld_bry = v; return this;} private byte[] fld_bry;
//		public void Clear() {
//			fld_bgn = fld_end = -1;
//			fld_bry = null;
//		}
//	}
class Srl_wkr implements GfoInvkAble {
	private Srl_mgr mgr;
	private byte[][] temp_bry;
	public Srl_wkr Mgr_(Srl_mgr mgr) {this.mgr = mgr; return this;}
	private byte fld_dlm = Byte_ascii.Pipe, row_dlm = Byte_ascii.NewLine, esc_tkn = Byte_ascii.Backslash;
	private byte esc_fld = Byte_ascii.Pipe, esc_row = Byte_ascii.Ltr_n;
	private ByteAryBfr fld_bfr = ByteAryBfr.reset_(255);
	public void Clear() {
		int len = temp_bry.length;
		for (int i = 0; i < len; i++)
			temp_bry[i] = null;
	}
	public int Flds_len() {return flds_len;} public void Flds_len_(int v) {flds_len = v; temp_bry = new byte[v][];} private int flds_len;
	public void Load_by_str(String src_str) {
		byte[] src = ByteAry_.new_utf8_(src_str);
		int src_len = src.length;
		int row_bgn = 0, fld_bgn = 0, fld_idx = 0;
		boolean fld_bfr_dirty = false;
		int src_pos = 0;
		while (true) {
			boolean last = src_pos == src_len;
			byte b = last ? row_dlm : src[src_pos];
			if		(b == fld_dlm) {
				temp_bry[fld_idx++] = fld_bfr_dirty ? fld_bfr.XtoAryAndClear() : ByteAry_.Mid(src, fld_bgn, src_pos);
				fld_bgn = src_pos + 1;			// update fld_bgn; set after fld_dlm;
				fld_bfr_dirty = false;
			}
			else if (b == row_dlm) {
				if (src_pos - row_bgn > 0) {	// guard against blank line; \n\n
					temp_bry[fld_idx++] = fld_bfr_dirty ? fld_bfr.XtoAryAndClear() : ByteAry_.Mid(src, fld_bgn, src_pos);
					mgr.Srl_add(fld_idx, temp_bry);
				}
				row_bgn = src_pos +  1;		// update row_bgn; set after row_dlm;
				fld_bgn = row_bgn;			// update fld_bgn;
				fld_idx = 0;				// reset fld_idx;
				fld_bfr_dirty = false;
			}
			else if (b == esc_tkn) {
				if (src_pos == src_len) throw Err_.new_("end of stream");
				byte next = src[src_pos + 1];
				byte lit_tkn = Byte_ascii.Nil;
				if		(next == esc_fld) 
					lit_tkn = fld_dlm;
				else if	(next == esc_row) 
					lit_tkn = row_dlm;
				else
					throw Err_.new_("unknown escape: " + Byte_.XtoStr(next));
				if (!fld_bfr_dirty) {
					fld_bfr.Add_mid(src, fld_bgn, src_pos);
					fld_bfr_dirty = true;
				}
				fld_bfr.Add_byte(lit_tkn);
				++src_pos;
			}
			else {
				if (fld_bfr_dirty)
					fld_bfr.Add_byte(b);
			}
			++src_pos;
			if (last) break;
		}
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_load_by_str))		Load_by_str(m.ReadStr("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_load_by_str = "load_by_str";
}

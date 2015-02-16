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
package gplx.core.flds; import gplx.*; import gplx.core.*;
import gplx.ios.*;
public class Gfo_fld_wtr extends Gfo_fld_base {
	public Bry_bfr Bfr() {return bfr;} public Gfo_fld_wtr Bfr_(Bry_bfr v) {bfr = v; return this;} Bry_bfr bfr;
	public Gfo_fld_wtr() {this.bfr = Bry_bfr.new_();}
	public Gfo_fld_wtr Write_int_base85_len5_fld(int v)						{bfr.Add_base85(v, Base85_utl.Len_int);			bfr.Add_byte(fld_dlm); return this;}
	public Gfo_fld_wtr Write_int_base85_lenN_fld(int v, int len)			{bfr.Add_base85(v, len);						bfr.Add_byte(fld_dlm); return this;}
	public Gfo_fld_wtr Write_int_variable_fld(int v)						{bfr.Add_int_variable(v);						bfr.Add_byte(fld_dlm); return this;}
	public Gfo_fld_wtr Write_int_fixed_fld(int v, int len)					{bfr.Add_int_fixed(v, len);						bfr.Add_byte(fld_dlm); return this;}
	public Gfo_fld_wtr Write_double_fld(double v)							{bfr.Add_double(v);								bfr.Add_byte(fld_dlm); return this;}
	public Gfo_fld_wtr Write_byte_fld(byte v)								{bfr.Add_byte(v);								bfr.Add_byte(fld_dlm); return this;}
	public Gfo_fld_wtr Write_bry_escape_fld(byte[] val)						{Write_bry_escape(val, 0, val.length);			bfr.Add_byte(fld_dlm); return this;}
	public Gfo_fld_wtr Write_bry_escape_fld(byte[] val, int bgn, int end)	{Write_bry_escape(val, bgn, end);				bfr.Add_byte(fld_dlm); return this;}
	public Gfo_fld_wtr Write_dlm_row()										{												bfr.Add_byte(row_dlm); return this;}
	public Gfo_fld_wtr Write_dlm_fld()										{												bfr.Add_byte(fld_dlm); return this;}
	public Gfo_fld_wtr Write_int_base85_lenN_row(int v, int len)			{bfr.Add_base85(v, len);						bfr.Add_byte(row_dlm); return this;}
	public Gfo_fld_wtr Write_int_base85_len5_row(int v)						{bfr.Add_base85(v, Base85_utl.Len_int);			bfr.Add_byte(row_dlm); return this;}
	public Gfo_fld_wtr Write_bry_escape_row(byte[] val)						{Write_bry_escape(val, 0, val.length);			bfr.Add_byte(row_dlm); return this;}
	public Gfo_fld_wtr Write_bry_escape_row(byte[] val, int bgn, int end)	{Write_bry_escape(val, bgn, end);				bfr.Add_byte(row_dlm); return this;}
	public Gfo_fld_wtr Write_double_row(double v)							{bfr.Add_double(v);								bfr.Add_byte(row_dlm); return this;}
	Gfo_fld_wtr Write_bry_escape(byte[] val, int bgn, int end) {
		for (int i = bgn; i < end; i++) {
			byte b = val[i];
			byte escape_val = encode_regy[b & 0xFF];	// PATCH.JAVA:need to convert to unsigned byte
			if (escape_val == Byte_ascii.Nil) 	bfr.Add_byte(b);
			else 								{bfr.Add_byte(escape_dlm); bfr.Add_byte(escape_val);}
		}
		return this;
	}
	public Gfo_fld_wtr Rls() {bfr.Rls(); return this;}

	public Io_url_gen Fil_gen() {return fil_gen;} public Gfo_fld_wtr Fil_gen_(Io_url_gen v) {fil_gen = v; return this;} Io_url_gen fil_gen;
	public int Bfr_max() {return bfr_max;} public Gfo_fld_wtr Bfr_max_(int v) {bfr_max = v; return this;} private int bfr_max = Io_mgr.Len_mb;
	public boolean Flush_needed(int v) {return bfr.Len() + v > bfr_max;}
	public void Flush() {
		if (Fil_gen().Cur_url() == null) fil_gen.Nxt_url();
		Io_mgr._.AppendFilBfr(fil_gen.Cur_url(), bfr);
	}
	public void Flush_nxt() {Flush(); fil_gen.Nxt_url();}
	public Gfo_fld_wtr Ctor_xdat() {return (Gfo_fld_wtr)super.Ctor_xdat_base();}
	public static Gfo_fld_wtr xowa_() {return new Gfo_fld_wtr().Ctor_xdat();}
}

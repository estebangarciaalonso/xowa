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
public class Xof_fsdb_itm_key_bldr {
	private ByteAryBfr bfr = ByteAryBfr.reset_(255);
	public byte[] Bld(byte[] lnki_ttl, byte lnki_type, int lnki_w, int lnki_h, double lnki_upright, double lnki_seek) {
		bfr.Add(lnki_ttl);
		Bfr_add_int		(lnki_type		, Xop_lnki_type.Id_null);
		Bfr_add_int		(lnki_w			, Xop_lnki_tkn.Width_null);
		Bfr_add_int		(lnki_h			, Xop_lnki_tkn.Height_null);
		Bfr_add_double	(lnki_upright	, Xop_lnki_tkn.Upright_null);
		Bfr_add_double	(lnki_seek		, Xop_lnki_tkn.Thumbtime_null);
		return bfr.XtoAryAndClear();
	}
	private void Bfr_add_int(int val, int skip) {
		bfr.Add_byte_pipe();
		if (val != skip) bfr.Add_int_variable(val);
	}
	private void Bfr_add_double(double val, double skip) {
		bfr.Add_byte_pipe();
		if (val != skip) bfr.Add_double(val);
	}
}

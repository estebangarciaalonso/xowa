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
import gplx.ios.*;
class Sql_fld_mgr {
	public int Count() {return hash.Count();}
	public Sql_fld_itm Get_by_key(String fld) {return Get_by_key(ByteAry_.new_utf8_(fld));}
	public Sql_fld_itm Get_by_key(byte[] fld) {
		return (Sql_fld_itm)hash.Fetch(fld);
	}	OrderedHash hash = OrderedHash_.new_bry_();
	public Sql_fld_mgr Parse(byte[] raw) {
		hash.Clear();
		int bgn = ByteAry_.FindFwd(raw, Tkn_create_table); if (bgn == ByteAry_.NotFound) throw Err_.new_("could not find 'CREATE TABLE'");
		bgn = ByteAry_.FindFwd(raw, Byte_ascii.NewLine, bgn); if (bgn == ByteAry_.NotFound) throw Err_.new_("could not find new line after 'CREATE TABLE'");
		bgn += Int_.Const_position_after_char;
		int end = ByteAry_.FindFwd(raw, Tkn_unique_index); if (end == ByteAry_.NotFound) throw Err_.new_("could not find 'UNIQUE KEY'");
		end = ByteAry_.FindBwd(raw, Byte_ascii.NewLine, end); if (bgn == ByteAry_.NotFound) throw Err_.new_("could not find new line before 'UNIQUE KEY'");
		Parse_lines(ByteAry_.Mid(raw, bgn, end));
		return this;
	}
	void Parse_lines(byte[] raw) {
		byte[][] lines = ByteAry_.Split(raw, Byte_ascii.NewLine);
		int lines_len = lines.length;
		int fld_idx = 0;
		for (int i = 0; i < lines_len; i++) {
			byte[] line = lines[i];
			int bgn = ByteAry_.FindFwd(line, Byte_ascii.Tick); if (bgn == ByteAry_.NotFound) continue;	// skip blank lines
			bgn += Int_.Const_position_after_char;
			int end = ByteAry_.FindFwd(line, Byte_ascii.Tick, bgn); if (end == ByteAry_.NotFound) continue;	// skip blank lines
			byte[] key = ByteAry_.Mid(line, bgn, end);
			Sql_fld_itm fld = new Sql_fld_itm(fld_idx++, key);
			hash.Add(fld.Key(), fld);
		}
	}
	private static final byte[] 
		Tkn_create_table = ByteAry_.new_ascii_("CREATE TABLE")
	,	Tkn_unique_index = ByteAry_.new_ascii_("UNIQUE KEY")
	;
	public static final int Not_found = -1;
}
class Sql_fld_itm {
	public Sql_fld_itm(int idx, byte[] key) {this.idx = idx; this.key = key;}
	public int Idx() {return idx;} private int idx;
	public byte[] Key() {return key;} private byte[] key;
}

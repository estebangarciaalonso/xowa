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
package gplx.xowa.langs.variants; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
public class Xol_variant_grp {
	public byte[] Grp() {return grp;} private byte[] grp;
	public byte[][] Subs() {return subs;} private byte[][] subs;
	public static Xol_variant_grp csv_(String raw_str) {	// EX: zh|zh-hans;zh-hant
		try {
			byte[] raw_bry = ByteAry_.new_utf8_(raw_str);
			int len = raw_bry.length; int subs_len = 0; int sub_bgn = 0; int len_plus_1 = len + 1;
			for (int i = 0; i < len_plus_1; i++) {		// count semicolons
				byte b = i == len ? Byte_ascii.Semic : raw_bry[i];
				if (b == Byte_ascii.Semic) {
					if (i - sub_bgn > 0)
						++subs_len;
					sub_bgn = i + 1;
				}
			}
			Xol_variant_grp rv = new Xol_variant_grp();
			rv.subs = new byte[subs_len][];
			int subs_pos = 0; sub_bgn = 0;
			for (int i = 0; i < len_plus_1; i++) {
				byte b = i == len ? Byte_ascii.Semic : raw_bry[i];
				switch (b) {
					case Byte_ascii.Pipe:
						if (i - sub_bgn > 0) rv.grp = ByteAry_.Mid(raw_bry, sub_bgn, i);	
						sub_bgn = i + 1; 
						break;
					case Byte_ascii.Semic:
						if (i - sub_bgn > 0) rv.subs[subs_pos++] = ByteAry_.Mid(raw_bry, sub_bgn, i);
						sub_bgn = i + 1;
						break;
				}
			}
			return rv;
		} catch (Exception e) {throw Err_.new_fmt_("failed to load lang variant: raw={0} err={1}", raw_str, Err_.Message_gplx_brief(e));}
	}
}

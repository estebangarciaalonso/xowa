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
import gplx.xowa.parsers.logs.*;
import gplx.xowa.html.*;
public class Hiero_nde implements Xox_xnde, Xop_xnde_atr_parser {
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {}
	public void Xtn_parse(Xow_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		boolean log_wkr_enabled = Log_wkr != Xop_log_basic_wkr.Null; if (log_wkr_enabled) Log_wkr.Log_end_xnde(ctx.Page(), Xop_log_basic_wkr.Tid_hiero, src, xnde);
	}	public static Xop_log_basic_wkr Log_wkr = Xop_log_basic_wkr.Null;
	public void Xtn_write(Xoa_app app, Xoh_html_wtr html_wtr, Xoh_opts opts, Xop_ctx ctx, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {}
}
class Hiero_file_itm {
	public Hiero_file_itm(byte[] key, int file_w, int file_h) {this.key = key; this.file_w = file_w; this.file_h = file_h;}
	public byte[] Key() {return key;} private byte[] key;
	public int File_w() {return file_w;} private int file_w;
	public int File_h() {return file_h;} private int file_h;
}
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
package gplx.fsdb; import gplx.*;
import gplx.dbs.*; import gplx.xowa.*; import gplx.xowa.files.fsdb.*;
public class Fsdb_mnt_itm {
	private Fsdb_vlm_db_core core_db = new Fsdb_vlm_db_core();
	private Fsdb_vlm_db_data[] vlms; private int vlms_len;
	private Fsdb_vlm_db_data vlm_temp = new Fsdb_vlm_db_data();
	public String Key() {return key;} public void Key_(String v) {this.key = v;} private String key;
	public String Url() {return url;} public void Url_(String v) {this.url = v;} private String url;
	public Fsdb_mnt_itm Init(String key, String url) {this.key = key; this.url = url; return this;}
	public void Init_vlm_mgr(Io_url root_dir) {
		core_db.Init_core_db(root_dir.GenSubFil("core.sqlite3"));
		vlms = core_db.Vlms();
		this.vlms_len = vlms.length;
	}
	public void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int width, int thumbtime, int height, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		byte[] path = path_bfr.Add(dir).Add_byte(Dir_spr).Add(fil).XtoAryAndClear();
		Fsdb_vlm_db_data vlm = Vlm_find(path);
		vlm.Thm_insert(rv, String_.new_utf8_(dir), String_.new_utf8_(fil), ext_id, width, thumbtime, height, modified, hash, bin_len, bin_rdr);
	}
	public void Img_insert(Fsdb_xtn_img_itm rv, byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr, int img_w, int img_h, int img_bits) {
		byte[] path = path_bfr.Add(dir).Add_byte(Dir_spr).Add(fil).XtoAryAndClear();
		Fsdb_vlm_db_data vlm = Vlm_find(path);
		vlm.Img_insert(rv, String_.new_utf8_(dir), String_.new_utf8_(fil), ext_id, modified, hash, bin_len, bin_rdr, img_w, img_h, img_bits);
	}
	public Fsdb_vlm_db_data Vlm_find(byte[] path) {
		vlm_temp.Rng_bgn_(path);
		int idx = Binary_search_.Search((CompareAble[])vlms, vlms.length, vlm_temp);
		return vlms[idx];
	}
	private ByteAryBfr path_bfr = ByteAryBfr.reset_(512);
	private static final byte Dir_spr = Byte_ascii.Slash;
	public void Rls() {
		for (int i = 0; i < vlms_len; i++)
			vlms[i].Rls();
	}
}

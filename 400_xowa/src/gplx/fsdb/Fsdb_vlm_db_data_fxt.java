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
import gplx.dbs.*; import gplx.xowa.*;
public class Fsdb_vlm_db_data_fxt {
	private Fsdb_vlm_db_data vlm_data; private Db_provider provider; private Fsdb_xtn_thm_itm tmp_img = Fsdb_xtn_thm_itm.new_();
	public void Init_db() {
		vlm_data = new_test_();
		provider = vlm_data.Provider();
	}
	public static Fsdb_vlm_db_data new_test_() {
		Io_url db_url = Fsdb_vlm_db_data.url_root_(Xo_test.Url_file_enwiki());
		Io_mgr._.DeleteFil_args(db_url).MissingFails_off().Exec();
		return Fsdb_vlm_db_data.make_root_(db_url);
	}
	public void Exec_thm_insert(String dir, String fil, int w, int h, String data) {Exec_thm_insert(tmp_img, dir, fil, w, h, data);}
	public void Exec_thm_insert(Fsdb_xtn_thm_itm rv, String dir, String fil, int w, int h, String data) {
		vlm_data.Thm_insert(rv, dir, fil, Xof_ext_.Id_unknown, w, Xop_lnki_tkn.Thumbtime_null, h, Fsdb_xtn_thm_tbl.Modified_null, Fsdb_xtn_thm_tbl.Hash_null, String_.Len(data), gplx.ios.Io_stream_rdr_.mem_(data));
	}
	public void Exec_thm_delete(int img_id) {vlm_data.Thm_delete(provider, img_id);}
	public void Exec_fil_delete(int fil_id) {vlm_data.Fil_delete(provider, fil_id);}
	public Fsdb_dir_itm Test_dir_select(String dir, int expd_id, int expd_owner) {
		Fsdb_dir_itm dir_itm = Fsdb_dir_tbl.Select_itm(provider, dir);
		Tfds.Eq(expd_id, dir_itm.Id(), "id");
		Tfds.Eq(expd_owner, dir_itm.Owner(), "owner");
		return dir_itm;
	}
	public Fsdb_fil_itm Test_fil_select(int dir_id, String fil, int expd_id) {
		Fsdb_fil_itm fil_itm = Fsdb_fil_tbl.Select_itm_by_name(provider, dir_id, fil);
		Tfds.Eq(expd_id, fil_itm.Id(), "id");
		return fil_itm;
	}
	public Fsdb_xtn_thm_itm Test_thm_select(int fil_id, int width, int expd_id, int expd_w, int expd_h) {return Test_thm_select(fil_id, width, Xop_lnki_tkn.Thumbtime_null, expd_id, expd_w, expd_h);}
	public Fsdb_xtn_thm_itm Test_thm_select(int fil_id, int width, int thumbtime, int expd_id, int expd_w, int expd_h) {
		Fsdb_xtn_thm_itm img = Fsdb_xtn_thm_tbl.Select_itm_by_fil_width(provider, fil_id, width, thumbtime);
		Tfds.Eq(expd_id, img.Id(), "id");
		Tfds.Eq(expd_w, img.Width(), "width");
		Tfds.Eq(expd_h, img.Height(), "height");
		return img;
	}
	public void Test_thm_exists(int img_id, boolean expd) {
		Fsdb_xtn_thm_itm itm = Fsdb_xtn_thm_tbl.Select_itm_by_id(provider, img_id);
		Tfds.Eq(expd, itm != Fsdb_xtn_thm_itm.Null, "exists: " + Int_.XtoStr(img_id));
	}
	public void Test_fil_exists(int img_id, boolean expd) {
		Fsdb_fil_itm itm = Fsdb_fil_tbl.Select_itm_by_id(provider, img_id);
		Tfds.Eq(expd, itm != Fsdb_fil_itm.Null);
	}
	public void Test_bin_select(int id, String expd_data) {
		gplx.ios.Io_stream_rdr bin_rdr = Fsdb_bin_tbl.Select_as_rdr(provider, id);
		Tfds.Eq(expd_data, gplx.ios.Io_stream_rdr_.Load_all_as_str(bin_rdr), "bin");
	}
	public void Test_bin_select_as_stream(int id, int bry_size, String expd_data) {
		gplx.ios.Io_stream_rdr stream = Fsdb_bin_tbl.Select_as_rdr(provider, id);
		ByteAryBfr bfr = ByteAryBfr.reset_(255);
		byte[] bry = new byte[bry_size];
		int pos = 0;
		while (true) {
			int read = stream.Read(bry, pos, bry_size);
			if (read == -1) break;
			bfr.Add_mid(bry, 0, read);
		}
		Tfds.Eq(expd_data, bfr.XtoStrAndClear(), "data");
	}
	public void Test_bin_select_to_fsys(int id, Io_url url, String expd_data) {
		Fsdb_bin_tbl.Select_to_fsys(provider, id, url, new byte[1], 2);
		Tfds.Eq(expd_data, Io_mgr._.LoadFilStr(url), "data");
	}
	public void Test_cfg_select_next_id(int expd_id) {
		Tfds.Eq(expd_id, Fsdb_cfg_tbl.Select_next_id(provider));
	}
	public void Rls() {
		provider.Rls();
	}
}

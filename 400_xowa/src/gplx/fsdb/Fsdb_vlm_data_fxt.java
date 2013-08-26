/*
XOWA: the extensible offline wiki application
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
import gplx.dbs.*; import gplx.fsdb.tbls.*; import gplx.xowa.*;
public class Fsdb_vlm_data_fxt {
	private Fsdb_vlm_data vlm_data; private Db_provider provider; private Fsdb_wmf_img_itm tmp_img = Fsdb_wmf_img_itm.new_();
	public void Init_db() {
		vlm_data = new_test_();
		provider = vlm_data.Provider();
	}
	public static Fsdb_vlm_data new_test_() {
		Io_url db_url = Fsdb_vlm_data.url_root_(Xo_test.Url_file_enwiki());
		Io_mgr._.DeleteFil_args(db_url).MissingFails_off().Exec();
		return Fsdb_vlm_data.make_root_(db_url);
	}
	public void Exec_img_insert(String dir, String fil, boolean orig, int w, int h, String data) {Exec_img_insert(tmp_img, dir, fil, orig, w, h, data);}
	public void Exec_img_insert(Fsdb_wmf_img_itm rv, String dir, String fil, boolean orig, int w, int h, String data) {
		vlm_data.Img_insert(rv, dir, fil, Xof_ext_.Id_unknown, w, Xop_lnki_tkn.Thumbtime_null, orig, h, null, "", ByteAry_.new_utf8_(data));
	}
	public void Exec_img_delete(int img_id) {vlm_data.Img_delete(provider, img_id);}
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
	public Fsdb_wmf_img_itm Test_img_select(int fil_id, int width, int expd_id, boolean expd_orig, int expd_w, int expd_h) {return Test_img_select(fil_id, width, Xop_lnki_tkn.Thumbtime_null, expd_id, expd_orig, expd_w, expd_h);}
	public Fsdb_wmf_img_itm Test_img_select(int fil_id, int width, int thumbtime, int expd_id, boolean expd_orig, int expd_w, int expd_h) {
		Fsdb_wmf_img_itm img = Fsdb_wmf_img_tbl.Select_itm_by_fil_width(provider, fil_id, width, thumbtime);
		Tfds.Eq(expd_id, img.Id(), "id");
		Tfds.Eq(expd_orig, img.Is_orig(), "is_orig");
		Tfds.Eq(expd_w, img.Width(), "width");
		Tfds.Eq(expd_h, img.Height(), "height");
		return img;
	}
	public void Test_img_exists(int img_id, boolean expd) {
		Fsdb_wmf_img_itm itm = Fsdb_wmf_img_tbl.Select_itm_by_id(provider, img_id);
		Tfds.Eq(expd, itm != Fsdb_wmf_img_itm.Null, "exists: " + Int_.XtoStr(img_id));
	}
	public void Test_fil_exists(int img_id, boolean expd) {
		Fsdb_fil_itm itm = Fsdb_fil_tbl.Select_itm_by_id(provider, img_id);
		Tfds.Eq(expd, itm != Fsdb_fil_itm.Null);
	}
	public void Test_bin_select(int id, String expd_data) {
		Fsdb_bin_itm bin_itm = Fsdb_bin_tbl.Select_itm(provider, id);
		Tfds.Eq(expd_data, String_.new_ascii_(bin_itm.Data()), "data");
	}
	public void Test_cfg_select_next_id(int expd_id) {
		Tfds.Eq(expd_id, Fsdb_cfg_tbl.Select_next_id(provider));
	}
	public void Rls() {
		provider.Rls();
	}
}

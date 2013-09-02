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
import org.junit.*;
import gplx.dbs.*; import gplx.xowa.*;
public class Fsdb_vlm_db_data_tst {
	private Fsdb_vlm_db_data_fxt fxt = new Fsdb_vlm_db_data_fxt();
//		private Fsdb_xtn_thm_itm tmp_img = Fsdb_xtn_thm_itm.new_();
	@Before public void init() {if (Xo_test.Db_skip) return; fxt.Init_db();}
	@After public void term() {if (Xo_test.Db_skip) return; fxt.Rls();}
	@Test   public void Insert() {
		if (Xo_test.Db_skip) return;
		fxt.Exec_img_insert("enwiki", "A.png", Bool_.Y, 1000, 800, "a");
		Fsdb_dir_itm dir = fxt.Test_dir_select("enwiki", 1, 0);
		Fsdb_fil_itm fil = fxt.Test_fil_select(dir.Id(), "A.png", 2);
		Fsdb_xtn_thm_itm img = fxt.Test_img_select(fil.Id(), 1000, 3, Bool_.Y, 1000, 800);
		fxt.Test_bin_select(img.Id(), "a");
		fxt.Test_cfg_select_next_id(4);
	}
	@Test   public void Insert_many() {
		if (Xo_test.Db_skip) return;
		fxt.Exec_img_insert("enwiki", "A.png", Bool_.Y, 1000, 800, "a");		// create orig; ignore;
		fxt.Exec_img_insert("enwiki", "A.png", Bool_.N,  500, 400, "a500");		// create thumb for retrieval
		fxt.Exec_img_insert("enwiki", "B.png", Bool_.Y,  500, 840, "b500");		// create another thumb with same w, but dif name
		Fsdb_dir_itm dir = fxt.Test_dir_select("enwiki", 1, 0);
		Fsdb_fil_itm fil = fxt.Test_fil_select(dir.Id(), "A.png", 2);
		Fsdb_xtn_thm_itm img = fxt.Test_img_select(fil.Id(), 500, 4, Bool_.N, 500, 400);
		fxt.Test_bin_select(img.Id(), "a500");
		fxt.Test_cfg_select_next_id(7);
	}
	@Test   public void Img_delete() {
		if (Xo_test.Db_skip) return;
		Fsdb_xtn_thm_itm img_400 = Fsdb_xtn_thm_itm.new_(), img_200 = Fsdb_xtn_thm_itm.new_();
		fxt.Exec_img_insert(img_400, "enwiki", "A.png", Bool_.N, 400, 200, "400,200");
		fxt.Exec_img_insert(img_200, "enwiki", "A.png", Bool_.N, 200, 100, "200,100");
		fxt.Test_img_exists(img_400.Id(), Bool_.Y);
		fxt.Exec_img_delete(img_400.Id());				// del 400
		fxt.Test_img_exists(img_400.Id(), Bool_.N);		// chk 400 deleted
		fxt.Test_img_exists(img_200.Id(), Bool_.Y);		// chk 200 exists
		fxt.Exec_img_delete(img_200.Id());				// del 200
		fxt.Test_img_exists(img_200.Id(), Bool_.N);		// chk 200 deleted
		fxt.Test_fil_exists(img_200.Owner(), Bool_.Y);	// chk fil still exists
	}
	@Test   public void Fil_delete() {
		if (Xo_test.Db_skip) return;
		Fsdb_xtn_thm_itm img_400 = Fsdb_xtn_thm_itm.new_(), img_200 = Fsdb_xtn_thm_itm.new_();
		fxt.Exec_img_insert(img_400, "enwiki", "A.png", Bool_.N, 400, 200, "400,200");
		fxt.Exec_img_insert(img_200, "enwiki", "A.png", Bool_.N, 200, 100, "200,100");
		fxt.Test_img_exists(img_400.Id(), Bool_.Y);
		fxt.Test_img_exists(img_200.Id(), Bool_.Y);
		fxt.Exec_fil_delete(img_400.Owner());			// del fil
		fxt.Test_img_exists(img_400.Id(), Bool_.N);		// chk 400 deleted
		fxt.Test_img_exists(img_200.Id(), Bool_.N);		// chk 200 deleted
		fxt.Test_fil_exists(img_200.Owner(), Bool_.N);	// chk fil deleted
	}
//		@Test   public void Bin_select() {
//			if (Xo_test.Db_skip) return;
//			Fsdb_xtn_thm_itm img_400 = Fsdb_xtn_thm_itm.new_();
//			fxt.Exec_img_insert(img_400, "enwiki", "A.png", Bool_.N, 400, 200, "400,200");
//			fxt.Test_bin_select_stream(img_400.Id(), 1, "400,200");
//		}
}

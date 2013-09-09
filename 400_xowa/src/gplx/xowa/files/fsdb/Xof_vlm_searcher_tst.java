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
//namespace gplx.xowa.files.fsdb {
//	import org.junit.*;
//	using gplx.dbs; using gplx.fsdb;
//	public class Xof_vlm_searcher_tst {
//		private Xof_vlm_searcher_fxt fxt =  new Xof_vlm_searcher_fxt();
//		@Before public void init() {if (Xo_test.Db_skip) return; fxt.Init_db();}
//		@After public void term() {if (Xo_test.Db_skip) return; fxt.Rls();}
//		@Test   public void Basic() {
//			if (Xo_test.Db_skip) return;
//			fxt.Init_img("commons", "A.png", 400, "a");
//			fxt.Test_find("commons", "A.png", 400, Xof_fsdb_itm.Db_status_bin_found);	// same name, width: binary found;
//			fxt.Test_find("commons", "A.png", 200, Xof_fsdb_itm.Db_status_fil_found);	// same name, but not width; only fil found
//		}
//	}
//	class Xof_vlm_searcher_fxt {
//		private Fsdb_vlm_db_data vlm_data; private Db_provider provider;
//		private Xof_fsdb_itm_key_bldr itm_key_bldr = new Xof_fsdb_itm_key_bldr();
//		private Fsdb_xtn_thm_itm tmp_img = Fsdb_xtn_thm_itm.new_();
//		public void Init_db() {
//			vlm_data = Fsdb_vlm_db_data_fxt.new_test_();
//			provider = vlm_data.Provider();
//		}
//		public void Init_img(String dir, String fil, int w, String bin) {	// NOTE: bin cannot be empty
//			vlm_data.Thm_insert(tmp_img, dir, fil, Xof_ext_.Id_unknown, w, Xop_lnki_tkn.Thumbtime_null, w, Fsdb_xtn_thm_tbl.Modified_null, Fsdb_xtn_thm_tbl.Hash_null, String_.Len(bin), gplx.ios.Io_stream_rdr_.mem_(bin));
//		}
//		public void Test_find(String dir, String fil, int w, byte expd_status) {
//			Xof_vlm_searcher searcher = new Xof_vlm_searcher();
//			Xof_fsdb_itm itm = new Xof_fsdb_itm();
//			itm.Atrs_by_lnki_for_fsdb(itm_key_bldr, ByteAry_.new_ascii_(fil), Xop_lnki_type.Id_null, w, w, -1, -1);
//			itm.Actl_url_(Io_url_.mem_fil_("mem/temp.png"));
//			itm.Db_status_(Xof_fsdb_itm.Db_status_vlm_found);	// start at vlm_found
//			OrderedHash hash = OrderedHash_.new_();
//			hash.Add(ByteAry_.new_ascii_(fil), itm);
//			searcher.Search(provider, hash, hash.Count());
//			Tfds.Eq(expd_status, itm.Db_status(), "status");
//		}
//		public void Rls() {
//			provider.Rls();
//		}
//	}
//}

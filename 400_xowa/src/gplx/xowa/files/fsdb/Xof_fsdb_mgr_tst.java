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
import org.junit.*;
import gplx.fsdb.*;
public class Xof_fsdb_mgr_tst {
	private Xof_fsdb_mgr_fxt fxt = new Xof_fsdb_mgr_fxt();
	@Before public void init() {if (Xo_test.Db_skip) return; fxt.Init_db();}
	@After public void term() {if (Xo_test.Db_skip) return; fxt.Rls();}
	@Test  public void Img_regy() {
		if (Xo_test.Db_skip) return;
		fxt.Init_regy(fxt.itm_("A.png", Xop_lnki_type.Id_thumb, 400), "/7/0/A.png/220px.png");
		fxt.Test_select_regy_present(fxt.itm_("A.png", Xop_lnki_type.Id_thumb, 400), "/7/0/A.png/220px.png");
		fxt.Test_select_regy_unknown(fxt.itm_("A.png", Xop_lnki_type.Id_thumb, 300));
	}
//		@Test  public void Fsdb() {
//			if (Xo_test.Db_skip) return;
//			fxt.Init_fsdb(fxt.itm_("A.png", Bool_.Y, 200, 100, "200,100"));
//			fxt.Test_select_fsdb_load(fxt.itm_("A.png", Xop_lnki_type.Id_none, 200), "200,100");
//			fxt.Test_select_fsdb_make(fxt.itm_("A.png", Xop_lnki_type.Id_none, 100), "100,50");
//		}
}
class Xof_fsdb_mgr_fxt {
	private Xof_fsdb_mgr sys_mgr;
	private Xof_fsdb_itm_key_bldr key_bldr;
	private byte[] dir_enwiki;
	private Fsdb_xtn_thm_itm tmp_img = Fsdb_xtn_thm_itm.new_();
	public void Init_db() {
		dir_enwiki = ByteAry_.new_ascii_("enwiki");
		Io_mgr._.DeleteDirDeep(Xo_test.Url_file_enwiki());
		sys_mgr = new Xof_fsdb_mgr();
		sys_mgr.Init_by_wiki(Xo_test.Url_file_enwiki(), ListAdp_.Null);
		key_bldr = new Xof_fsdb_itm_key_bldr();
	}
	public Xof_fsdb_itm itm_(String ttl, boolean is_orig, int w, int h, String data) {return new Xof_fsdb_itm().Ttl_(ByteAry_.new_ascii_(ttl)).Db_img_is_orig_(is_orig).Actl_img_w_(w).Actl_img_h_(h).Db_data_(ByteAry_.new_ascii_(data));}
	public Xof_fsdb_itm itm_(String ttl, byte type, int w) {return itm_(ttl, type, w, Xop_lnki_tkn.Height_null, Xop_lnki_tkn.Upright_null, Xop_lnki_tkn.Thumbtime_null);}
	public Xof_fsdb_itm itm_(String ttl, byte type, int w, int h, double upright, int thumbtime) {return new Xof_fsdb_itm().Atrs_by_lnki_for_fsdb(key_bldr, ByteAry_.new_ascii_(ttl), type, w, h, upright, thumbtime);}
	public void Init_regy(Xof_fsdb_itm itm, String url)	{sys_mgr.Reg_insert(String_.new_ascii_(itm.Reg_key()), url, Xof_fsdb_itm.Reg_status_present, 123, 0, 1, 1);}
	public void Init_fsdb(Xof_fsdb_itm itm)				{sys_mgr.Thm_insert(tmp_img, dir_enwiki, itm.Ttl(), itm.Actl_img_w(), itm.Db_img_is_orig(), itm.Actl_img_h(), itm.Db_data());}
	public void Test_select_regy_present(Xof_fsdb_itm itm, String expd_url) {Test_select_regy(itm, Xof_fsdb_itm.Reg_status_present, expd_url);}
	public void Test_select_regy_unknown(Xof_fsdb_itm itm) {Test_select_regy(itm, Xof_fsdb_itm.Reg_status_unknown, null);}
	public void Test_select_regy(Xof_fsdb_itm itm, byte expd_status, String expd_url) {
		OrderedHash hash = itm_hash(itm);
		sys_mgr.Bin_find(Xog_win_wtr_.Null, hash);
		Tfds.Eq(itm.Reg_status(), expd_status, "status");
		Tfds.Eq(itm.Reg_url(), expd_url, "url");
	}
	public void Test_select_fsdb_load(Xof_fsdb_itm itm, String expd) {
		OrderedHash hash = itm_hash(itm);
		sys_mgr.Bin_find(Xog_win_wtr_.Null, hash);
		Tfds.Eq(expd, String_.new_ascii_(itm.Db_data()), "data");
	}
	public void Rls() {
		sys_mgr.Rls();
	}
	private OrderedHash itm_hash(Xof_fsdb_itm itm) {
		OrderedHash rv = OrderedHash_.new_bry_();
		rv.Add(itm.Reg_key(), itm);
		return rv;
	}
}

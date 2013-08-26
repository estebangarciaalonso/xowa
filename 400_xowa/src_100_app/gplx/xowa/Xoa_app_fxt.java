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
package gplx.xowa; import gplx.*;
public class Xoa_app_fxt {
	public static Xoa_app app_() {
		Io_mgr._.InitEngine_mem();
		return app_(Io_url_.mem_dir_("mem/xowa/"), "linux");
	}
	public static Xoa_app app_(Io_url root_dir, String op_sys) {
		Io_url user_dir = root_dir.GenSubDir_nest("user", "test_user");
		Gfo_log_wtr_base._.Log_dir_(user_dir.GenSubDir_nest("tmp", "current"));			
		Xoa_app app = new Xoa_app(Gfo_usr_dlg_xowa.test_xowa_(), root_dir, user_dir, op_sys);
		app.Setup_mgr().Dump_mgr().Data_storage_format_(gplx.ios.Io_stream_.Tid_file);	// TEST: set data_storage_format to file, else bldr tests will fails (expects plain text)
		return app;
	}
	public static Xow_wiki wiki_tst_(Xoa_app app) {return wiki_(app, "en.wikipedia.org");}
	public static Xow_wiki wiki_(Xoa_app app, String key) {return wiki_(app, key, app.Lang_mgr().Lang_en());}
	public static Xow_wiki wiki_(Xoa_app app, String key, Xol_lang lang) {
		Io_url wiki_dir = app.Fsys_mgr().Wiki_dir().GenSubDir(key);
		Xow_wiki rv = new Xow_wiki(app, wiki_dir, Xow_ns_mgr_.default_(), lang);
		rv.Html_mgr().Tbl_para_n_();
		rv.File_mgr().Meta_mgr().Depth_(2);					// TEST: written for 2 depth
		rv.Props().Main_page_(Xoa_page.Bry_main_page);		// TEST: default to Main Page (nothing tests loading Main Page from wiki.gfs)			
		app.Wiki_mgr().Add(rv);
		return rv;
	}
	public static Xob_bldr bldr_(Xoa_app app) {
		Xob_bldr rv = new Xob_bldr(app);
		rv.Sort_mem_len_(Io_mgr.Len_kb).Dump_fil_len_(Io_mgr.Len_kb).Make_fil_len_(Io_mgr.Len_kb);
		return rv;
	}
	public static final Io_url Root_dir = Op_sys.Cur().Tid_is_lnx() ? Io_url_.new_dir_("/xowa/") : Io_url_.new_dir_("C:\\xowa\\");
}

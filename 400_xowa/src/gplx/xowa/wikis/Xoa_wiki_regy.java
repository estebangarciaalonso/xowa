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
package gplx.xowa.wikis; import gplx.*; import gplx.xowa.*;
public class Xoa_wiki_regy {
	private Xoa_app app;
	private boolean init_needed = true;
	private Hash_adp_bry hash = Hash_adp_bry.cs_();
	public Xoa_wiki_regy(Xoa_app app) {this.app = app;}
	public boolean Has(byte[] domain) {if (init_needed) Init(); return hash.Has(domain);}
	public boolean Url_is_invalid_domain(Xoa_url url) {
		if (!Bry_.Eq(url.Page_bry(), Xoa_page_.Main_page_bry)) return false;		// page is not "Main_Page"; assume not an invalid domain str; EX: "uk/wiki/Main_Page"
		if (	 Bry_.Eq(Xow_wiki_domain_.Key_home_bry, url.Wiki_bry())				// wiki is "home"
			&&	!Bry_.Eq(Xow_wiki_domain_.Key_home_bry, url.Raw()))					// raw is "home"; should be "home/wiki/Main_Page"; DATE:2014-02-09
			return false;																// special case to handle "home" which should mean "home" in any wiki, but "home/wiki/Main_Page" in home wiki
		return !this.Has(url.Wiki_bry());
	}
	private void Init() {
		Io_url[] wiki_dirs = Io_mgr._.QueryDir_args(app.Fsys_mgr().Wiki_dir()).DirInclude_(true).Recur_(false).ExecAsUrlAry();
		int wiki_dirs_len = wiki_dirs.length;
		for (int i = 0; i < wiki_dirs_len; i++) {
			Io_url wiki_dir = wiki_dirs[i];
			byte[] domain_bry = Bry_.new_utf8_(wiki_dir.NameOnly());
			hash.Add(domain_bry, domain_bry);
		}
		init_needed = true;
	}
	public static void Make_wiki_dir(Xoa_app app, String domain_str) {	// TEST: fake wiki_dir for Parse_from_url_bar; DATE:2014-02-16
		Io_url wiki_dir = app.Fsys_mgr().Wiki_dir();
		Io_mgr._.CreateDir(wiki_dir.GenSubDir(domain_str));
	}
}

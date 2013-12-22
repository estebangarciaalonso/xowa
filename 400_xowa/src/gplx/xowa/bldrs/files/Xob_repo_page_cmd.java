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
package gplx.xowa.bldrs.files; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.bldrs.oimgs.*;
public class Xob_repo_page_cmd extends Xob_itm_basic_base implements Xob_cmd {
	public Xob_repo_page_cmd(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_ctor(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "file.repo_page";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		Db_provider provider = Xodb_db_file.init__file_make(wiki.Fsys_mgr().Root_dir()).Provider();
		Xob_repo_page_tbl.Create_table(provider);
		Xow_wiki commons_wiki = bldr.App().Wiki_mgr().Get_by_key_or_make(Xow_wiki_.Domain_commons_bry).Init_assert();
		wiki.Init_assert(); commons_wiki.Init_assert();
		Xob_repo_page_tbl.Create_data(bldr.Usr_dlg(), provider, wiki, commons_wiki);
	}
	public void Cmd_run() {}
	public void Cmd_end() {}
	public void Cmd_print() {}
}

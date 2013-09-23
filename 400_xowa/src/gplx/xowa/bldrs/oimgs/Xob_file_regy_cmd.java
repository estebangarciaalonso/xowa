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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*;
public class Xob_file_regy_cmd extends Xob_itm_basic_base implements Xob_cmd {
	public Xob_file_regy_cmd(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.file_regy";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		wiki.Init_assert();
		Gfo_usr_dlg usr_dlg = bldr.Usr_dlg();
		Xodb_fsys_mgr fsys_mgr = wiki.Db_mgr_as_sql().Fsys_mgr(); 
		Xodb_file db_file = fsys_mgr.Get_or_make("oimg_lnki");
		Db_provider provider = db_file.Provider();
		Xob_lnki_regy_tbl tbl_lnki_regy = new Xob_lnki_regy_tbl().Create_table(provider);
		tbl_lnki_regy.Create_data(usr_dlg, provider);
		Xob_file_regy_tbl tbl_file_regy = new Xob_file_regy_tbl().Create_table(provider);
		Xow_wiki commons_wiki = bldr.App().Wiki_mgr().Get_by_key_or_make(Xow_wiki_.Domain_commons_bry);
		commons_wiki .Init_assert();
		tbl_file_regy.Create_data(usr_dlg, provider, wiki, commons_wiki);
	}
	public void Cmd_run() {}
	public void Cmd_end() {}
	public void Cmd_print() {}
}

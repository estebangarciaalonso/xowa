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
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.dbs.tbls.*;
public class Xodb_db_file {
	Xodb_db_file(String name, Io_url url, Db_provider provider, boolean created) {this.name = name; this.url = url; this.provider = provider; this.created = created;}
	public String Name() {return name;} private String name;
	public Io_url Url() {return url;} private Io_url url;
	public Db_provider Provider() {return provider;} private Db_provider provider;
	public boolean Created() {return created;} public void Created_clear() {created = false;} private boolean created;

	public static Xodb_db_file init__oimg_lnki(Xow_wiki wiki)		{return init_(wiki, Name__oimg_lnki);}
	public static Xodb_db_file init__oimg_image(Xow_wiki wiki)		{return init_(wiki, Name__oimg_image);}
	public static Xodb_db_file init__oimg_redirect(Xow_wiki wiki)	{return init_(wiki, Name__oimg_redirect);}
	public static Xodb_db_file init_(Xow_wiki wiki, String name) {
		wiki.Init_assert();
		Io_url url = wiki.Db_mgr_as_sql().Fsys_mgr().Trg_dir().GenSubFil(name + ".sqlite3");
		BoolRef created = BoolRef.false_();
		Db_provider provider = Sqlite_engine_.Provider_load_or_make_(url, created);
		if (created.Val()) {	// always create cfg table
			Xodb_xowa_cfg_tbl.Create_table(provider);
			Xodb_xowa_cfg_tbl.Create_index(provider);
		}
		return new Xodb_db_file(name, url, provider, created.Val());
	}
	public static final String Name__oimg_lnki = "oimg_lnki", Name__oimg_image = "oimg_image", Name__oimg_redirect = "oimg_redirect";
}

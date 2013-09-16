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
import gplx.dbs.*;
public class Fsdb_vlm_db_core {
	private Fsdb_vlm_tbl tbl_vlm = new Fsdb_vlm_tbl();
	private Db_provider provider;
	public Fsdb_vlm_db_data[] Vlms() {return vlms;} private Fsdb_vlm_db_data[] vlms;
	public Fsdb_vlm_db_core Init_core_db(Io_url url) {
		return Io_mgr._.ExistsFil(url) ? Init_by_load(url) : Init_by_make(url);
	}
	private Fsdb_vlm_db_core Init_by_load(Io_url url) {
		provider = Db_provider_.new_(Db_connect_sqlite.load_(url));
		vlms = tbl_vlm.Select_all(provider, url);
		return this;
	}
	private Fsdb_vlm_db_core Init_by_make(Io_url url) {
		Fsdb_vlm_db_data vlm = Fsdb_vlm_db_data.make_root_(url);
		provider = vlm.Provider();
		vlms = new Fsdb_vlm_db_data[] {vlm};
		tbl_vlm.Create_table(provider);
		tbl_vlm.Insert(provider, "", url.Raw(), "");
		return this;
	}
}

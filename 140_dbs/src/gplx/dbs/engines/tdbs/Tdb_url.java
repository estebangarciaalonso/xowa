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
package gplx.dbs.engines.tdbs; import gplx.*; import gplx.dbs.*; import gplx.dbs.engines.*;
public class Tdb_url extends Db_url__base {
	public Io_url Url() {return url;} Io_url url;
	@Override public String Tid() {return Tid_const;} public static final String Tid_const = "tdb";
        public static Db_url new_(Io_url url) {
		return Db_url_.parse_(Bld_raw
		( "gplx_key", Tid_const
		, "url", url.Raw()
		));
	}	Tdb_url() {}
	@Override public Db_url New_self(String raw, GfoMsg m) {
		Tdb_url rv = new Tdb_url();
		String urlStr = m.ReadStr("url");
		Io_url url = Io_url_.new_any_(urlStr);
		rv.Ctor(urlStr, url.NameOnly(), raw, BldApi(m));
		rv.url = url;
		return rv;
	}
        public static final Tdb_url _ = new Tdb_url();
}

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
package gplx.dbs.engines.postgres; import gplx.*; import gplx.dbs.*; import gplx.dbs.engines.*;
import gplx.stores.*; import gplx.dbs.engines.*; import gplx.dbs.sqls.*;
import java.sql.*; 
public class Postgres_engine extends Db_engine_sql_base {
	@Override public String Tid() {return Postgres_url.Tid_const;}
	@Override public Sql_qry_wtr SqlWtr() {return Sql_qry_wtr_.new_escape_backslash();}
	@Override public Db_engine New_clone(Db_url connectInfo) {
		Postgres_engine rv = new Postgres_engine();
		rv.Ctor(connectInfo);
		return rv;
	}
	@Override public DataRdr New_rdr(ResultSet rdr, String commandText) {return Db_data_rdr_.new_(rdr, commandText);}
		@gplx.Internal @Override protected Connection Conn_new() {
		Postgres_url url_as_postgres = (Postgres_url)url; 
		return Conn_make_by_url("jdbc:" + url_as_postgres.Tid() + "://localhost/" + url_as_postgres.Database(), url_as_postgres.Uid(), url_as_postgres.Pwd());
	}
		public static final Postgres_engine _ = new Postgres_engine(); Postgres_engine() {}
}

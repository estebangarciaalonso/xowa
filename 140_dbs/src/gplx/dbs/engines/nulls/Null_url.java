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
package gplx.dbs.engines.nulls; import gplx.*; import gplx.dbs.*; import gplx.dbs.engines.*;
public class Null_url extends Db_url__base {
	@Override public String Tid() {return Tid_const;} public static final String Tid_const = "null_db";
	@Override public Db_url New_self(String raw, GfoMsg m) {return this;}
	public static final Null_url _ = new Null_url(); Null_url() {this.Ctor("", "", "gplx_key=null_db", "");}
}

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
package gplx.xowa.hdumps; import gplx.*; import gplx.xowa.*;
import gplx.xowa.dbs.tbls.*;
public class Xowd_db_tbl_mgr {
	public Xodbv_page_tbl	Tbl__page() {return tbl__page;} private final Xodbv_page_tbl tbl__page = new Xodbv_page_tbl();
	public Xodb_xowa_ns_tbl	Tbl__ns()	{return tbl__ns;}	private final Xodb_xowa_ns_tbl tbl__ns = new Xodb_xowa_ns_tbl();
	public Xodbv_dbs_tbl	Tbl__dbs_new()	{return new Xodbv_dbs_tbl();}
}	

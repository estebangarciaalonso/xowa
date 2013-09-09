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
package gplx.xowa.files.qrys; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.dbs.*; import gplx.xowa.dbs.tbls.*;
public class Xof_img_meta_wkr_xowa implements Xof_img_meta_wkr {
	public Xodb_image_itm Find(Xow_wiki wiki, byte[] ttl) {
		Db_provider image_regy_provider = null; //wiki.Db_mgr_as_sql().Fsys_mgr().Image_provider();
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = Xodb_image_tbl.Select_ttl_stmt(image_regy_provider);
			return (Xodb_image_itm)Xodb_image_tbl.Select_ttl(stmt, String_.new_utf8_(ttl));
		} 	finally {stmt.Rls();}
	}
}

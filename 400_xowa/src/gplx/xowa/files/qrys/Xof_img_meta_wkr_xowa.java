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
import gplx.dbs.*; import gplx.xowa.bldrs.wikis.images.*;
public class Xof_img_meta_wkr_xowa implements Xof_img_meta_wkr {
	public Xob_wiki_image_itm Find(Xow_wiki wiki, byte[] ttl) {
		Db_provider image_regy_provider = gplx.xowa.bldrs.oimgs.Xodb_db_file.init__wiki_image(wiki.Fsys_mgr().Root_dir()).Provider();
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = Xob_wiki_image_tbl.Select_ttl_stmt(image_regy_provider);
			return Xob_wiki_image_tbl.Select_itm(stmt, String_.new_utf8_(ttl));
		} 	finally {stmt.Rls();}
	}
}

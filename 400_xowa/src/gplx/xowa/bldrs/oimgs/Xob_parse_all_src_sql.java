/*
XOWA: the extensible offline wiki application
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
import gplx.xowa.dbs.*; import gplx.dbs.*;
class Xob_parse_all_db_sql implements Xob_parse_all_db {
	public Xob_parse_all_db_sql Init(Xow_wiki wiki, int limit, byte redirect) {
		this.db_mgr = wiki.Db_mgr_as_sql(); this.redirect = redirect;
		page_stmt = db_mgr.Tbl_page().Select_for_parse_all_stmt(db_mgr.Fsys_mgr().Core_provider(), limit, redirect);
		text_files_ary = Init_text_files_ary(db_mgr.Fsys_mgr());
		text_files_len = text_files_ary.length;
		return this;
	}	private Xodb_mgr_sql db_mgr; private Db_stmt page_stmt; private Xodb_file[] text_files_ary; private int text_files_len; private byte redirect;
	public void Fetch_next(OrderedHash hash, int ns_id, byte[] ttl) {
		Cancelable cancelable = Cancelable_.Never;
		db_mgr.Tbl_page().Select_for_parse_all(cancelable, hash, page_stmt, ns_id, ttl, redirect);
		for (int i = 0; i < text_files_len; i++) {
			Xodb_file text_file = text_files_ary[i];
			db_mgr.Tbl_text().Select_in(cancelable, text_file, hash);
		}
	}
	private static Xodb_file[] Init_text_files_ary(Xodb_fsys_mgr fsys_mgr) {
		ListAdp text_files_list = ListAdp_.new_();
		Xodb_file[] file_ary = fsys_mgr.Ary();
		int len = file_ary.length;
		if (len == 1) return new Xodb_file[] {file_ary[0]};	// single file: return core; note that there are no Tid = Text
		for (int i = 0; i < len; i++) {
			Xodb_file file = file_ary[i];
			if (file.Tid() == Xodb_file.Tid_text)
				text_files_list.Add(file);
		}
		return (Xodb_file[])text_files_list.XtoAryAndClear(Xodb_file.class);
	}
}

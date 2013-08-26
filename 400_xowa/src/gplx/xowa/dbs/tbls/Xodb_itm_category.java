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
package gplx.xowa.dbs.tbls; import gplx.*; import gplx.xowa.*; import gplx.xowa.dbs.*;
import gplx.xowa.ctgs.*;
public class Xodb_itm_category {
	public int Id() {return id;} private int id;
	public int File_idx() {return file_idx;} private int file_idx;
	public boolean Hidden() {return hidden;} private boolean hidden;
	public int Count_all() {return count_subcs + count_files + count_pages;}
	public int Count_subcs() {return count_subcs;} private int count_subcs;
	public int Count_files() {return count_files;} private int count_files;
	public int Count_pages() {return count_pages;} private int count_pages;
	public int Count_by_tid(byte tid) {
		switch (tid) {
			case Xoa_ctg_mgr.Tid_subc: return count_subcs;
			case Xoa_ctg_mgr.Tid_page: return count_pages;
			case Xoa_ctg_mgr.Tid_file: return count_files;
			default: throw Err_.unhandled(tid);
		}		
	} 
	public static Xodb_itm_category load_(int id, int file_idx, boolean hidden, int count_subcs, int count_files, int count_pages) {
		Xodb_itm_category rv = new Xodb_itm_category();
		rv.id = id; rv.file_idx = file_idx; rv.hidden = hidden;
		rv.count_subcs = count_subcs;  rv.count_files = count_files; rv.count_pages = count_pages;
		return rv;
	}
        public static final Xodb_itm_category Null = new Xodb_itm_category(); Xodb_itm_category() {}
}

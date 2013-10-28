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
package gplx.xowa.wikis.caches; import gplx.*; import gplx.xowa.*; import gplx.xowa.wikis.*;
public class Xow_cache_mgr {
	public Xow_cache_mgr(Xow_wiki wiki) {
		page_cache = new Xow_page_cache(wiki);
		defn_cache = new Xow_defn_cache(wiki.Lang());
	}
	public Xow_page_cache Page_cache() {return page_cache;} private Xow_page_cache page_cache;
	public Xow_defn_cache Defn_cache() {return defn_cache;} private Xow_defn_cache defn_cache;
	public void Free_mem_all() {
		defn_cache.Free_mem_all();
		page_cache.Free_mem_all();
	}
}

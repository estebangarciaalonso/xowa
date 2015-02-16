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
package gplx.xowa.files.fsdb.qrys; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*; import gplx.xowa.files.fsdb.*;
public class Xof_wiki_finder {
	private Xow_wiki wiki_0, wiki_1;
	private Xodb_page db_page = new Xodb_page(); 
	public Xof_wiki_finder(Xow_wiki wiki_0, Xow_wiki wiki_1) {
		this.wiki_0 = wiki_0; this.wiki_1 = wiki_1;
	}
	public Xoa_page Get_page(int ns, byte[] ttl_bry) {
		Xoa_page rv = Get_page__by_wiki(wiki_0, ns, ttl_bry);
		if (rv.Missing())
			rv = Get_page__by_wiki(wiki_1, ns, ttl_bry);
		return rv;
	}
	private Xoa_page Get_page__by_wiki(Xow_wiki wiki, int ns_id, byte[] ttl_bry) {
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ns_id, ttl_bry) ;
		Xoa_url url = Xoa_url.new_(wiki.Domain_bry(), ttl_bry);
		return wiki.GetPageByTtl(url, ttl);
	}
	private int qry_count, qry_count_max = 1000;
	public boolean Find_page(Xof_wiki_finder_itm itm, int ns_id, byte[] ttl_bry) {
		Xow_wiki wiki = null;
		if (Find_page__by_wiki(db_page, wiki_0, ns_id, ttl_bry)) {
			wiki = wiki_0;
			itm.Orig_repo_id_(Byte_.Zero);
		}
		else {
			if (Find_page__by_wiki(db_page, wiki_1, ns_id, ttl_bry)) {
				wiki = wiki_1;
				itm.Orig_repo_id_((byte)1);
			}
			else
				return false;
		}
		itm.Orig_ttl_(ttl_bry);
		if (db_page.Type_redirect()) {
			Xoa_page page = Get_page__by_wiki(wiki, ns_id, ttl_bry);
			Xoa_ttl redirect_ttl = wiki.Redirect_mgr().Extract_redirect_loop(page.Data_raw());		
			itm.Orig_redirect_(redirect_ttl);			
			++qry_count;
			if (qry_count >= qry_count_max) {
				wiki.App().Reset_all();
				qry_count = 0;
			}
		}
		return true;
	}
	private boolean Find_page__by_wiki(Xodb_page db_page, Xow_wiki wiki, int ns_id, byte[] ttl_bry) {
		Xow_ns ns = wiki.Ns_mgr().Ids_get_or_null(ns_id);
		wiki.Db_mgr().Load_mgr().Load_page(db_page, ns, false);
		return db_page.Exists();
	}	
}

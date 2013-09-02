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
//if (ext.Id_is_ogg() && api_rv.Orig_w() == 0 && api_rv.Orig_h() == 0)	// file is ogg, but thumb has size of 0,0; assume audio and mark as oga
//meta_itm.Update_thumb_oga_();
class Xof_wiki_finder {
	private Xow_wiki wiki_0, wiki_1;
	public Xof_wiki_finder(Xow_wiki wiki_0, Xow_wiki wiki_1) {
		this.wiki_0 = wiki_0; this.wiki_1 = wiki_1;
	}
	public Xoa_page Get_page(int ns, byte[] ttl_bry) {
		Xoa_page rv = Get_page__by_wiki(wiki_0, ns, ttl_bry);
		if (rv == Xoa_page.Null)
			rv = Get_page__by_wiki(wiki_1, ns, ttl_bry);
		return rv;
	}
	private Xoa_page Get_page__by_wiki(Xow_wiki wiki, int ns_id, byte[] ttl_bry) {
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ns_id, ttl_bry) ;
		Xoa_url url = Xoa_url.new_(wiki.Domain_bry(), ttl_bry);
		return wiki.GetPageByTtl(url, ttl);
	}	
}

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
package gplx.xowa.specials.xowa.default_tab; import gplx.*; import gplx.xowa.*; import gplx.xowa.specials.*; import gplx.xowa.specials.xowa.*;
public class Default_tab_page implements Xows_page {
	public void Special_gen(Xoa_url url, Xoa_page page, Xow_wiki wiki, Xoa_ttl ttl) {
		page.Data_raw_(Bry_.Empty);
		page.Html_data().Custom_html_(Bry_.Empty);
		page.Html_data().Custom_name_(Tab_name_bry);
	}
	public static final String Ttl_name_str = "XowaDefaultTab";
	public static final byte[] Ttl_name_bry = Bry_.new_ascii_("XowaDefaultTab");
	public static final String Ttl_full_str = "Special:" + Ttl_name_str;
	public static final byte[] Ttl_full_bry = Bry_.new_ascii_(Ttl_full_str);
	public static final byte[] Tab_name_bry = Bry_.new_ascii_("New Tab");
}

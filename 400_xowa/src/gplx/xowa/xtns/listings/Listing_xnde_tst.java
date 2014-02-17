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
package gplx.xowa.xtns.listings; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Listing_xnde_tst {
	private Xop_fxt fxt = new Xop_fxt();
	private Listing_xtn_mgr listings_xtn_mgr;
	@Before public void init() {
		listings_xtn_mgr = (Listing_xtn_mgr)fxt.Wiki().Xtn_mgr().Get_or_fail(Listing_xtn_mgr.Xtn_key_static);
		listings_xtn_mgr.Enabled_y_();
		listings_xtn_mgr.Xtn_init_by_wiki(fxt.Wiki());
	}
	@Test  public void Url_name() {
		fxt.Test_parse_page_all_str
		(	"<sleep name='name_0' url='http://site.org'/>"
		,	"<a href=\"http://site.org\" class=\"external text\" rel=\"nofollow\" title=\"name_0\"><strong>sleep</strong></a>"
		);
	}
	@Test  public void Alt() {
		fxt.Test_parse_page_all_str
		(	"<sleep alt=\"''alt_0''\"/>"
		,	"<strong>sleep</strong><em><i>alt_0</i></em>"
		);
	}
	@Test  public void Address_directions_position() {
		fxt.Test_parse_page_all_str
		(	"<sleep address='address_0' directions='directions_0'/>"
		,	"<strong>sleep</strong>, address_0<em>directions_0</em>"
		);
	}
	@Test  public void Phone_tollfree() {
		fxt.Test_parse_page_all_str
		(	"<sleep phone='phone_0' tollfree='tollfree_0'/>"
		,	"<strong>sleep</strong>, <abbr title=\"phone\">☎</abbr> phone_0 (<abbr title=\"toll-free\"></abbr>: tollfree_0)"
		);
	}
	@Test  public void Fax() {
		fxt.Test_parse_page_all_str
		(	"<sleep fax='fax_0'/>"
		,	"<strong>sleep</strong>, <abbr title=\"fax\"></abbr>: fax_0"
		);
	}
	@Test  public void Email() {
		fxt.Test_parse_page_all_str
		(	"<sleep email='email_0'/>"
		,	"<strong>sleep</strong>, <abbr title=\"email\"></abbr>: <a class=\"email\" href=\"mailto:email_0\">email_0</a>"
		);
	}
	@Test  public void Hours() {
		fxt.Test_parse_page_all_str
		(	"<sleep hours='hours_0'/>"
		,	"<strong>sleep</strong>hours_0. "
		);
	}
	@Test  public void Checkin() {
		fxt.Test_parse_page_all_str
		(	"<sleep checkin='checkin_0' checkout='checkout_0'/>"
		,	"<strong>sleep</strong>Check-in: $1, check-out: $1. "
		);
	}
	@Test  public void Price() {
		fxt.Test_parse_page_all_str
		(	"<sleep price='price_0'/>"
		,	"<strong>sleep</strong>price_0. "
		);
	}
	@Test  public void Content() {
		fxt.Test_parse_page_all_str
		(	"<sleep>content_0</sleep>"
		,	"<strong>sleep</strong>content_0"
		);
	}
}

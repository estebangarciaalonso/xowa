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
package gplx.xowa.wmf.apis; import gplx.*; import gplx.xowa.*; import gplx.xowa.wmf.*;
import org.junit.*;
public class Xoapi_orig_base_tst {
	Xoapi_orig_base_fxt fxt = new Xoapi_orig_base_fxt();
	@Before public void init() {fxt.Clear();}
	@Test  public void Bld_api_url() {
		fxt.Bld_api_url_tst("A.png"		, 220, 110, "http://en.wikipedia.org/w/api.php?action=query&format=xml&prop=imageinfo&iiprop=size|url&redirects&titles=File:A.png&iiurlwidth=220&iiurlheight=110");
		fxt.Bld_api_url_tst("A.png"		, 220,   0, "http://en.wikipedia.org/w/api.php?action=query&format=xml&prop=imageinfo&iiprop=size|url&redirects&titles=File:A.png&iiurlwidth=220");
		fxt.Bld_api_url_tst("A.png"		,   0, 110, "http://en.wikipedia.org/w/api.php?action=query&format=xml&prop=imageinfo&iiprop=size|url&redirects&titles=File:A.png");	// assert that null width does not write height
		fxt.Bld_api_url_tst("A b.png"	, 220,   0, "http://en.wikipedia.org/w/api.php?action=query&format=xml&prop=imageinfo&iiprop=size|url&redirects&titles=File:A_b.png&iiurlwidth=220");
		fxt.Bld_api_url_tst("A&b.png"	, 220,   0, "http://en.wikipedia.org/w/api.php?action=query&format=xml&prop=imageinfo&iiprop=size|url&redirects&titles=File:A%26b.png&iiurlwidth=220");
	}
	@Test  public void Parse_size() {
		String raw = "<api><query><pages><page ns=\"6\" title=\"File:A.png\" missing=\"\" imagerepository=\"shared\"><imageinfo><ii size=\"1234\" width=\"220\" height=\"110\" /></imageinfo></page></pages></query></api>";
		fxt.Parse_size_tst(raw, 220, 110);
	}
	@Test  public void Parse_reg() {
		String raw = "<api><query><pages><page ns=\"6\" title=\"File:A.png\" missing=\"\" imagerepository=\"shared\"><imageinfo><ii descriptionurl=\"http://commons.wikimedia.org/wiki/File:Berkheyde-Haarlem.png\" /></imageinfo></page></pages></query></api>";
		fxt.Parse_reg_tst(raw, "commons.wikimedia.org", "Berkheyde-Haarlem.png");
	}
}
class Xoapi_orig_base_fxt {
	private Xoa_app app; private Xow_wiki wiki; private Xoapi_orig_rslts rv = new Xoapi_orig_rslts();
	public void Clear() {
		this.app = Xoa_app_fxt.app_();
		this.wiki = Xoa_app_fxt.wiki_tst_(app);
	}
	public void Bld_api_url_tst(String ttl_str, int w, int h, String expd) {
		String actl = Xoapi_orig_wmf.Bld_api_url(wiki.Domain_bry(), Bry_.new_utf8_(ttl_str), w, h);
		Tfds.Eq(expd, actl);
	}
	public void Parse_size_tst(String xml_str, int expd_w, int expd_h) {
		byte[] xml_bry = Bry_.new_utf8_(xml_str);
		Xoapi_orig_wmf.Parse_xml(rv, app.Usr_dlg(), xml_bry);
		Tfds.Eq(expd_w, rv.Orig_w());
		Tfds.Eq(expd_h, rv.Orig_h());
	}
	public void Parse_reg_tst(String xml_str, String expd_wiki, String expd_page) {
		byte[] xml_bry = Bry_.new_utf8_(xml_str);
		Xoapi_orig_wmf.Parse_xml(rv, app.Usr_dlg(), xml_bry);
		Tfds.Eq(expd_wiki, String_.new_utf8_(rv.Orig_wiki()));
		Tfds.Eq(expd_page, String_.new_utf8_(rv.Orig_page()));
	}
}

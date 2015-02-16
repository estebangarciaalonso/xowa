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
package gplx.xowa.gui.urls.url_macros; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*; import gplx.xowa.gui.urls.*;
import org.junit.*;
public class Xog_url_macro_mgr_tst {
	@Before public void init() {fxt.Clear();} private Xog_url_macro_mgr_fxt fxt = new Xog_url_macro_mgr_fxt();
	@Test  public void Custom_basic()		{fxt.Test("?:Page"				, "Special:Search/Page?fulltext=y");}
	@Test  public void Type_basic()			{fxt.Test("en.w:Page"			, "en.wikipedia.org/wiki/Page");}
	@Test  public void Type_main()			{fxt.Test("en.w:"				, "en.wikipedia.org/wiki/");}
	@Test  public void Type_tid()			{fxt.Test("w:Page"				, "en.wikipedia.org/wiki/Page");}
	@Test  public void Type_lang()			{fxt.Test("fr.w:Page"			, "fr.wikipedia.org/wiki/Page");}
	@Test  public void Type_unhandled()		{fxt.Test("x:A"					, null);}
	@Test  public void Type_unhandled_ns()	{fxt.Test("Help:A"				, null);}
	@Test  public void Type_custom()		{fxt.Test("wd.q:123"			, "www.wikidata.org/wiki/Q123");}
	@Test  public void Type_del() {
		fxt.Test("w:A", "en.wikipedia.org/wiki/A");
		fxt.Abrv_mgr().Types_mgr().Del(Bry_.new_utf8_("w"));
		fxt.Test("w:A", null);
	}
	@Test  public void Type_set() {
		fxt.Abrv_mgr().Types_mgr().Set("w", "~{0}.~{1}");
		fxt.Test("w.A", null);
	}
	@Test  public void Lang_default() {
		fxt.Abrv_mgr().Lang_default_(Bry_.new_ascii_("fr"));
		fxt.Test("w:Page", "fr.wikipedia.org/wiki/Page");
	}
	@Test  public void Precedence()	{	// PURPOSE: Custom should take precedence over type
		fxt.Abrv_mgr().Custom_mgr().Set("w", "custom:~{0}");
		fxt.Test("w:Page"				, "custom:Page");
	}
}
class Xog_url_macro_mgr_fxt {
	public void Clear() {
		abrv_mgr = new Xog_url_macro_mgr();
	}
	public Xog_url_macro_mgr Abrv_mgr() {return abrv_mgr;} private Xog_url_macro_mgr abrv_mgr;
	public void Test(String raw, String expd) {
		Tfds.Eq(expd, String_.new_ascii_(abrv_mgr.Fmt_or_null(Bry_.new_ascii_(raw))));
	}
}

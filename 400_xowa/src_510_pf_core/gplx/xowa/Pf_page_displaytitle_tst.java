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
package gplx.xowa; import gplx.*;
import org.junit.*;
public class Pf_page_displaytitle_tst {
	@Before public void init() {fxt.Reset();} private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Basic()			{fxt.tst_Parse_tmpl_str_test("{{DISPLAYTITLE:a}}"			, "{{test}}", ""); tst_Display_ttl("a");}
	@Test  public void Apos_italic()	{fxt.tst_Parse_tmpl_str_test("{{DISPLAYTITLE:''a''}}"		, "{{test}}", ""); tst_Display_ttl("<i>a</i>");}
	void tst_Display_ttl(String expd) {Tfds.Eq(expd, String_.new_utf8_(fxt.Ctx().Tab().Display_ttl()));}
}

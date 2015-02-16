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
package gplx.xowa.xtns.pfuncs.pages; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import org.junit.*;
public class Pfunc_rev_props_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before	public void setup()						{fxt.Reset(); fxt.Page().Revision_data().User_(Bry_.new_ascii_("user")).Protection_level_(Bry_.new_ascii_("normal"));}
	@After public void teardown()				{}
	@Test  public void RevisionID()					{fxt.Page().Revision_data().Id_(1); fxt.Test_parse_tmpl_str_test("{{REVISIONID}}"		, "{{test}}", "1");}
	@Test  public void PageID()						{fxt.Page().Revision_data().Id_(1); fxt.Test_parse_tmpl_str_test("{{PAGEID}}"			, "{{test}}", "1");}
	@Test  public void RevisionUser()				{fxt.Test_parse_tmpl_str_test("{{REVISIONUSER}}"									, "{{test}}", "user");}
	@Test  public void PageSize()					{fxt.Test_parse_tmpl_str_test("{{PAGESIZE:Test page}}"								, "{{test}}", "0");}
	@Test  public void ProtectionLevel()			{fxt.Test_parse_tmpl_str_test("{{PROTECTIONLEVEL}}"									, "{{test}}", "normal");}
	@Test  public void PageSize_invalid_ttl()		{
		fxt.Init_log_(Xop_ttl_log.Invalid_char);
		fxt.Test_parse_tmpl_str_test("{{PAGESIZE:{{{100}}}|R}}"		, "{{test}}", "0");
	}
}

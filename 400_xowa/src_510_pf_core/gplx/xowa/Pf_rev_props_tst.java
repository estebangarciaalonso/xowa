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
package gplx.xowa; import gplx.*;
import org.junit.*;
public class Pf_rev_props_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before	public void setup()						{fxt.Reset(); fxt.Wiki().Ctx().Page_revData().User_(ByteAry_.new_ascii_("user")).ProtectionLevel_(ByteAry_.new_ascii_("normal"));}
	@After public void teardown()				{}
	@Test  public void RevisionID()					{fxt.Wiki().Ctx().Page().Id_(1); fxt.tst_Parse_tmpl_str_test("{{REVISIONID}}"				, "{{test}}", "1");}
	@Test  public void PageID()						{fxt.Wiki().Ctx().Page().Id_(1); fxt.tst_Parse_tmpl_str_test("{{PAGEID}}"					, "{{test}}", "1");}
	@Test  public void RevisionUser()				{fxt.tst_Parse_tmpl_str_test("{{REVISIONUSER}}"				, "{{test}}", "user");}
	@Test  public void PageSize()					{fxt.tst_Parse_tmpl_str_test("{{PAGESIZE:Test page}}"		, "{{test}}", "0");}
	@Test  public void ProtectionLevel()			{fxt.tst_Parse_tmpl_str_test("{{PROTECTIONLEVEL}}"			, "{{test}}", "normal");}
	@Test  public void PageSize_invalid_ttl()		{
		fxt.ini_Log_(Xop_ttl_log.Invalid_char);
		fxt.tst_Parse_tmpl_str_test("{{PAGESIZE:{{{100}}}|R}}"		, "{{test}}", "0");
	}
}

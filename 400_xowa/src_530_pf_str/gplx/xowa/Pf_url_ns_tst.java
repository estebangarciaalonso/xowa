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
public class Pf_url_ns_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init()				{fxt.Reset();}
	@Test  public void Ns_0()				{fxt.tst_Parse_tmpl_str_test("{{ns:0}}"						, "{{test}}", "");}
	@Test  public void Ns_10()				{fxt.tst_Parse_tmpl_str_test("{{ns:10}}"					, "{{test}}", "Template");}
	@Test  public void Ns_11()				{fxt.tst_Parse_tmpl_str_test("{{ns:11}}"					, "{{test}}", "Template talk");}
	@Test  public void Ns_11_ws()			{fxt.tst_Parse_tmpl_str_test("{{ns: 11 }}"					, "{{test}}", "Template talk");}
	@Test  public void Ns_Template()		{fxt.tst_Parse_tmpl_str_test("{{ns:Template}}"				, "{{test}}", "Template");}
	@Test  public void Ns_invalid()			{fxt.tst_Parse_tmpl_str_test("{{ns:252}}"					, "{{test}}", "");}
	@Test  public void Nse_10()				{fxt.tst_Parse_tmpl_str_test("{{nse:10}}"					, "{{test}}", "Template");}
	@Test  public void Nse_11()				{fxt.tst_Parse_tmpl_str_test("{{nse:11}}"					, "{{test}}", "Template_talk");}
	@Test  public void Ns_Image()			{fxt.tst_Parse_tmpl_str_test("{{ns:Image}}"					, "{{test}}", "File");}
	@Test  public void Ns_Templatex()		{fxt.tst_Parse_tmpl_str_test("{{ns:Templatex}}"				, "{{test}}", "");}
	@Test  public void Ns_Talk() {	// PURPOSE: non-English wikis may have parameterized Project Talk ($1 talk); swap out with ns:4; REF.MW: Language.php!fixVariableInNamespace
		fxt.Wiki().Ns_mgr_(new Xow_ns_mgr().Add_new(4, "wiki").Add_new(5, "$1 talk").Add_new(10, "Template").Ords_sort());
		fxt.tst_Parse_tmpl_str_test("{{ns:5}}"					, "{{test}}", "wiki talk");
	}
}

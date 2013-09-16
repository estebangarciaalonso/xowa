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
import gplx.intl.*;
public class Pf_str_formatnum_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init()				{fxt.Reset();}
	@Test  public void Len_4()				{fxt.tst_Parse_tmpl_str_test("{{formatnum:1234}}"						, "{{test}}"	, "1,234");}
	@Test  public void Len_7()				{fxt.tst_Parse_tmpl_str_test("{{formatnum:1234567}}"					, "{{test}}"	, "1,234,567");}
	@Test  public void Len_2()				{fxt.tst_Parse_tmpl_str_test("{{formatnum:12}}"							, "{{test}}"	, "12");}
	@Test  public void Len_10()				{fxt.tst_Parse_tmpl_str_test("{{formatnum:1234567890}}"					, "{{test}}"	, "1,234,567,890");}
	@Test  public void Neg()				{fxt.tst_Parse_tmpl_str_test("{{formatnum:-1234}}"						, "{{test}}"	, "-1,234");}
	@Test  public void Decimal()			{fxt.tst_Parse_tmpl_str_test("{{formatnum:1234.5678}}"					, "{{test}}"	, "1,234.5678");}
	@Test  public void Mixed()				{fxt.tst_Parse_tmpl_str_test("{{formatnum:1234abc5678}}"				, "{{test}}"	, "1,234abc5,678");}
	@Test  public void Zeros()				{fxt.tst_Parse_tmpl_str_test("{{formatnum:0000000}}"					, "{{test}}"	, "0,000,000");}
	@Test  public void Raw()				{fxt.tst_Parse_tmpl_str_test("{{formatnum:1,234.56|R}}"					, "{{test}}"	, "1234.56");}
	@Test  public void Cs()					{fxt.tst_Parse_tmpl_str_test("{{FORMATNUM:1234}}"						, "{{test}}"	, "1,234");}
	@Test  public void Exc_ws()	{ // PURPOSE: EX: {{rnd|122835.3|(-3)}}
		fxt.tst_Parse_tmpl_str_test(String_.Concat_lines_nl_skipLast
			(	"{{formatnum:"
			,	"   {{#expr:2}}"
			,	"}}"
			)
			,	"{{test}}"
			,	"2"
			);
	}
	@Test  public void Raw_foreign() {
		fxt.Wiki().Lang().Num_fmt_mgr().Clear().Dec_dlm_(new byte[] {Byte_ascii.Comma}).Grps_add(new Gfo_num_fmt_grp(new byte[] {Byte_ascii.Dot}, 3, true));
		fxt.tst_Parse_tmpl_str_test("{{formatnum:12,34|R}}", "{{test}}"	, "12.34");
		fxt.Wiki().Lang().Num_fmt_mgr().Clear().Dec_dlm_(new byte[] {Byte_ascii.Dot}).Grps_add(new Gfo_num_fmt_grp(new byte[] {Byte_ascii.Comma}, 3, true));
	}
}
/*
*/

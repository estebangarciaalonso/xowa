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
public class Pf_xtn_switch_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init()				{fxt.Reset();}
	@Test  public void Basic_a()			{fxt.tst_Parse_tmpl_str_test("{{#switch:a|a=1|b=2|3}}"					, "{{test}}"			, "1");}
	@Test  public void Basic_b()			{fxt.tst_Parse_tmpl_str_test("{{#switch:b|a=1|b=2|3}}"					, "{{test}}"			, "2");}
	@Test  public void Basic_dflt()			{fxt.tst_Parse_tmpl_str_test("{{#switch:z|a=1|b=2|3}}"					, "{{test}}"			, "3");}
	@Test  public void FallThru_a()			{fxt.tst_Parse_tmpl_str_test("{{#switch:a|a|b|c=1|d=2|3}}"				, "{{test}}"			, "1");}
	@Test  public void FallThru_b()			{fxt.tst_Parse_tmpl_str_test("{{#switch:b|a|b|c=1|d=2|3}}"				, "{{test}}"			, "1");}
	@Test  public void FallThru_c()			{fxt.tst_Parse_tmpl_str_test("{{#switch:c|a|b|c=1|d=2|3}}"				, "{{test}}"			, "1");}
	@Test  public void FallThru_d()			{fxt.tst_Parse_tmpl_str_test("{{#switch:d|a|b|c=1|d=2|3}}"				, "{{test}}"			, "2");}
	@Test  public void FallThru_dflt()		{fxt.tst_Parse_tmpl_str_test("{{#switch:z|a|b|c=1|d=2|3}}"				, "{{test}}"			, "3");}
	@Test  public void Dflt_named()			{fxt.tst_Parse_tmpl_str_test("{{#switch:z|b=2|#default=3|a=1}}"			, "{{test}}"			, "3");}
	@Test  public void Dflt_last_idx_wins()		// even if there is a named default, if last arg is un-keyd, then use it as default
											{fxt.tst_Parse_tmpl_str_test("{{#switch:z|#default=3|9}}"				, "{{test}}"			, "9");}
	@Test  public void Dflt_last_named_wins()	// last named default wins
											{fxt.tst_Parse_tmpl_str_test("{{#switch:z|#default=3|#default=4}}"		, "{{test}}"			, "4");}
	@Test  public void Numeric()			{fxt.tst_Parse_tmpl_str_test("{{#switch:003|3.0=y|n}}"					, "{{test}}"			, "y");} //{{#switch:{{CURRENTMONTH}}|03=y|n}}
	@Test  public void NoKeys()				{fxt.tst_Parse_tmpl_str_test("{{#switch:a|a|b|c|d}}"					, "{{test}}"			, "d");}// d wins b/c it is default
	@Test  public void Prm_val()			{fxt.tst_Parse_tmpl_str_test("{{#switch:{{{1}}}|a=1|b=2|3}}"			, "{{test|b}}"			, "2");}
	@Test  public void Prm_case1v()			{fxt.tst_Parse_tmpl_str_test("{{#switch:{{{1}}}|a={{{1}}}|b=2|3}}"		, "{{test|a}}"			, "a");}
	@Test  public void Prm_case1k()			{fxt.tst_Parse_tmpl_str_test("{{#switch:{{{1}}}|{{{1}}}=1|b=2|3}}"		, "{{test|a}}"			, "1");}
	@Test  public void Null_x()				{fxt.tst_Parse_tmpl_str_test("{{#switch:|a=1|b=2|3}}"					, "{{test|b}}"			, "3");}
	@Test  public void Exc_no_cases()		{fxt.tst_Parse_tmpl_str_test("{{#switch:a}}"							, "{{test}}"			, "");}
	@Test  public void Exc_brace()			{fxt.tst_Parse_tmpl_str_test("{{#switch:a|{{{1}}}}=y|n}}"				, "{{test|a}}"			, "n");}// NOTE: deliberate 4th } brace
	@Test  public void Ex_1()				{fxt.tst_Parse_tmpl_str_test("{{#switch:{{{1}}}|off=none|def=off|{{{1|off}}}}}", "{{test|b}}"			, "b");}
	@Test  public void Ex_2() {
		fxt.tst_Parse_tmpl_str_test(String_.Concat_lines_nl_skipLast
			(	"{{#switch:{{{{{|safesubst:}}}NAMESPACE:Category:Foo}}"
			,	"|{{ns:0}}"
			,	"|{{ns:Category}}=yes"
			,	"|no"
			,	"}}"
			)
			,	"{{test}}"
			,	"yes");
	}
	@Test  public void Ws()					{
		fxt.tst_Parse_tmpl_str_test(String_.Concat_lines_nl_skipLast
			(	"{{#switch: | {{ns:0}}"
			,	"|{{ns:2}} = yes"
			,	"|no"
			,	"}}"
			)
			,	"{{test}}"
			,	"yes");
	}
	@Test  public void Do_not_call_val_unless_needed() {
		fxt.ini_defn_clear();
		Pf_xtn_xowa_dbg.Argx_list.Clear();
		fxt.ini_defn_add("fail", "{{#xowa_dbg:Fail}}");
		fxt.ini_defn_add("pass", "{{#xowa_dbg:Pass}}");
		fxt.ini_defn_add("dflt", "{{#xowa_dbg:Dflt}}");
		fxt.tst_Parse_tmpl_str_test("{{#switch:{{{1}}}|a={{fail}}|#default={{dflt}}|b={{pass}}}}", "{{test|b}}", "Pass");
		Tfds.Eq(1, Pf_xtn_xowa_dbg.Argx_list.Count());
	}
}

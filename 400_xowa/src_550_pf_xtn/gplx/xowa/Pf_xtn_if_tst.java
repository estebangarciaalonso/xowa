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
public class Pf_xtn_if_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init()				{fxt.Reset();}
	@Test  public void If_y()				{fxt.tst_Parse_tmpl_str_test("{{#if:1|a|b}}"								, "{{test}}"		, "a");}
	@Test  public void If_n()				{fxt.tst_Parse_tmpl_str_test("{{#if:|a|b}}"									, "{{test}}"		, "b");}
	@Test  public void If_n_ws()			{fxt.tst_Parse_tmpl_str_test("{{#if: |a|b}}"								, "{{test}}"		, "b");}
	@Test  public void If_y_ws()			{fxt.tst_Parse_tmpl_str_test("{{#if: |a|b \n}}"								, "{{test}}"		, "b");}
	@Test  public void If_y_ws1()			{fxt.tst_Parse_tmpl_str_test("{{#if: |a|{{#if: |a|b}}\n}}"					, "{{test}}"		, "b");}
	@Test  public void If_prm_n()			{fxt.tst_Parse_tmpl_str_test("{{#if:{{{1|}}}|{{{1}}}|b}}"					, "{{test}}"		, "b");}
	@Test  public void If_prm_y()			{fxt.tst_Parse_tmpl_str_test("{{#if:{{{1|}}}|{{{1}}}|b}}"					, "{{test|a}}"		, "a");}
	@Test  public void If_prm_n_dflt_ws()	{fxt.tst_Parse_tmpl_str_test("{{#if:{{{1| }}}|a|b}}"						, "{{test}}"		, "b");}
	@Test  public void If_prm_nest_0()		{fxt.tst_Parse_tmpl_str_test("{{#if:{{{1|}}}|{{#if:{{{2|}}}|a|b}}|c}}"		, "{{test}}"		, "c");}
	@Test  public void If_prm_nest_1()		{fxt.tst_Parse_tmpl_str_test("{{#if:{{{1|}}}|{{#if:{{{2|}}}|a|b}}|c}}"		, "{{test|1}}"		, "b");}
	@Test  public void If_prm_nest_2()		{fxt.tst_Parse_tmpl_str_test("{{#if:{{{1|}}}|{{#if:{{{2|}}}|a|b}}|c}}"		, "{{test|1|2}}"	, "a");}
	@Test  public void If_ignore_key()		{fxt.tst_Parse_tmpl_str_test("{{#if:|<i id=1|<i id=2}}"						, "{{test}}"		, "<i id=2");}
	@Test  public void If_newline()	{	// PURPOSE: new_line in comments; WP:[[redirect-distinguish|a|b]]
		fxt.tst_Parse_tmpl_str_test(String_.Concat_lines_nl_skipLast
			(	"{{#if:1<!--"
			,	"-->|a<!--"
			,	"-->|b<!--"
			,	"-->}}"
			)
			, "{{test}}", "a");
	}
	@Test  public void If_rel2abs()			{fxt.tst_Parse_tmpl_str_test("{{#if:{{{1}}}|y}}"				, "{{test|http://a.org/c/}}"		, "y");}	// PURPOSE.fix: trailing slash should not trigger rel2abs code; DATE:2013-04-06
}

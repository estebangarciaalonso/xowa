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

	@Test  public void Ifeq_y()				{fxt.tst_Parse_tmpl_str_test("{{#ifeq:1|1|a|b}}"				, "{{test}}"		, "a");}
	@Test  public void Ifeq_n()				{fxt.tst_Parse_tmpl_str_test("{{#ifeq:1|2|a|b}}"				, "{{test}}"		, "b");}
	@Test  public void Ifeq_prm_arg()		{fxt.tst_Parse_tmpl_str_test("{{#ifeq:{{{1}}}|1|a|b}}"			, "{{test|1}}"		, "a");}
	@Test  public void Ifeq_prm_arg_n()		{fxt.tst_Parse_tmpl_str_test("{{#ifeq:{{{1}}}|1|a|b}}"			, "{{test|2}}"		, "b");}
	@Test  public void Ifeq_prm_blank_y()	{fxt.tst_Parse_tmpl_str_test("{{#ifeq:||a|b}}"					, "{{test}}"		, "a");}
	@Test  public void Ifeq_prm_blank_n()	{fxt.tst_Parse_tmpl_str_test("{{#ifeq:|1|a|b}}"					, "{{test}}"		, "b");}
	@Test  public void Ifeq_numeric()		{fxt.tst_Parse_tmpl_str_test("{{#ifeq:003|3.0|y|n}}"			, "{{test}}"		, "y");}
	@Test  public void Ifeq_numeric_neg()	{fxt.tst_Parse_tmpl_str_test("{{#ifeq:-1.0|-1|y|n}}"			, "{{test}}"		, "y");}
	@Test  public void Ifeq_prm_arg0()		{fxt.tst_Parse_tmpl_str_test("{{#ifeq:1|{{{1}}}|a|b}}"			, "{{test|1}}"		, "a");}
	@Test  public void Ifeq_expr_err()		{fxt.tst_Parse_tmpl_str_test("{{#ifeq:{{#expr:a}}|0|y|n}}"		, "{{test}}"		, "n");}
	@Test  public void Ifeq_blank()			{fxt.tst_Parse_tmpl_str_test("{{#ifeq:0||y|n}}"					, "{{test}}"		, "n");}

	@Test  public void Ifeq_exc_args_0()	{fxt.tst_Parse_tmpl_str_test("{{#ifeq:}}"						, "{{test}}"		, "");}
	@Test  public void Ifeq_exc_args_1()	{fxt.tst_Parse_tmpl_str_test("{{#ifeq:1|1}}"					, "{{test}}"		, "");}
	@Test  public void Ifeq_exc_args_2()	{fxt.tst_Parse_tmpl_str_test("{{#ifeq:1|1|a}}"					, "{{test}}"		, "a");}
	@Test  public void Ifeq_exp()			{fxt.tst_Parse_tmpl_str_test("{{#ifeq:0.006|+6.0E-3|y|n}}"		, "{{test}}"		, "y");}
	@Test  public void Ifeq_plus_minus()	{fxt.tst_Parse_tmpl_str_test("{{#ifeq:+|-|y}}"					, "{{test}}"		, "");}	// PURPOSE: was evaluating to y; EX.WP:Permian-Triassic extinction
	@Test  public void Tab_ent() {	// PURPOSE: hack; tabs are materialized as "&#09;" which causes trimming problems; EX.WP: Template:Cretaceous_graphical_timeline and "|period11=    Campanian\s\t"
		fxt.tst_Parse_page_all_str("{{#ifeq:a|a &#09;|y|n}}", "y");	// note that "|a\s\t" gets trimmed to "a"
	}
	@Test  public void Ifeq_hex()			{fxt.tst_Parse_tmpl_str_test("{{#ifeq:44|0X002C|y|n}}"			, "{{test}}"		, "y");}	// PURPOSE: hex compares to int; EX:w:Comma 
	@Test  public void If_rel2abs()			{fxt.tst_Parse_tmpl_str_test("{{#if:{{{1}}}|y}}"				, "{{test|http://a.org/c/}}"		, "y");}	// PURPOSE.fix: trailing slash should not trigger rel2abs code; DATE:2013-04-06
}

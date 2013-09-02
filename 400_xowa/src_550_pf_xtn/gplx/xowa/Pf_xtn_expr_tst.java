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
public class Pf_xtn_expr_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Before public void init()				{fxt.Reset();}
	@Test  public void Null()				{fxt.tst_Parse_tmpl_str_test("{{#expr:}}"									, "{{test}}"	, "");}
	@Test  public void Num_len1()			{fxt.tst_Parse_tmpl_str_test("{{#expr:1}}"									, "{{test}}"	, "1");}
	@Test  public void Num_len3()			{fxt.tst_Parse_tmpl_str_test("{{#expr:123}}"								, "{{test}}"	, "123");}
	@Test  public void Num_decimal()		{fxt.tst_Parse_tmpl_str_test("{{#expr:1.2}}"								, "{{test}}"	, "1.2");}
	@Test  public void Num_decimal_lead()	{fxt.tst_Parse_tmpl_str_test("{{#expr:.12}}"								, "{{test}}"	, "0.12");}
	@Test  public void Num_decimal_lax()	{fxt.tst_Parse_tmpl_str_test("{{#expr:1.2.3}}"								, "{{test}}"	, "1.2");}			// PURPOSE: PHP allows 1.2.3 to be 1.2
	@Test  public void Num_neg()			{fxt.tst_Parse_tmpl_str_test("{{#expr:-1}}"									, "{{test}}"	, "-1");}
	@Test  public void Num_neg_double()		{fxt.tst_Parse_tmpl_str_test("{{#expr:--1}}"								, "{{test}}"	, "1");}
	@Test  public void Ws()					{fxt.tst_Parse_tmpl_str_test("{{#expr: 123\t\n }}"							, "{{test}}"	, "123");}
	@Test  public void Plus_op()			{fxt.tst_Parse_tmpl_str_test("{{#expr:1 + 2}}"								, "{{test}}"	, "3");}
	@Test  public void Minus_op()			{fxt.tst_Parse_tmpl_str_test("{{#expr:3 - 2}}"								, "{{test}}"	, "1");}
	@Test  public void Mult()				{fxt.tst_Parse_tmpl_str_test("{{#expr:3 * 2}}"								, "{{test}}"	, "6");}
	@Test  public void Div_sym()			{fxt.tst_Parse_tmpl_str_test("{{#expr:6 / 3}}"								, "{{test}}"	, "2");}
	@Test  public void Div_word()			{fxt.tst_Parse_tmpl_str_test("{{#expr:6 div 3}}"							, "{{test}}"	, "2");}
	@Test  public void Plus_sign()			{fxt.tst_Parse_tmpl_str_test("{{#expr:1 + + 2}}"							, "{{test}}"	, "3");}
	@Test  public void Minus_sign()			{fxt.tst_Parse_tmpl_str_test("{{#expr:3 + - 2}}"							, "{{test}}"	, "1");}
	@Test  public void Paren_1()			{fxt.tst_Parse_tmpl_str_test("{{#expr:(1 + 2) * 3}}"						, "{{test}}"	, "9");}
	@Test  public void Paren_2()			{fxt.tst_Parse_tmpl_str_test("{{#expr:((1 + 2) * 3) * 4}}"					, "{{test}}"	, "36");}
	@Test  public void Pow()				{fxt.tst_Parse_tmpl_str_test("{{#expr:2 ^ 4}}"								, "{{test}}"	, "16");}
	@Test  public void Eq_y()				{fxt.tst_Parse_tmpl_str_test("{{#expr:2 = 2}}"								, "{{test}}"	, "1");}
	@Test  public void Eq_n()				{fxt.tst_Parse_tmpl_str_test("{{#expr:2 = 3}}"								, "{{test}}"	, "0");}
	@Test  public void Neq_1()				{fxt.tst_Parse_tmpl_str_test("{{#expr:2 != 3}}"								, "{{test}}"	, "1");}
	@Test  public void Neq_2()				{fxt.tst_Parse_tmpl_str_test("{{#expr:2 <> 3}}"								, "{{test}}"	, "1");}
	@Test  public void Gt()					{fxt.tst_Parse_tmpl_str_test("{{#expr:3 > 2}}"								, "{{test}}"	, "1");}
	@Test  public void Lt()					{fxt.tst_Parse_tmpl_str_test("{{#expr:2 < 3}}"								, "{{test}}"	, "1");}
	@Test  public void Gte()				{fxt.tst_Parse_tmpl_str_test("{{#expr:2 >= 2}}"								, "{{test}}"	, "1");}
	@Test  public void Lte()				{fxt.tst_Parse_tmpl_str_test("{{#expr:2 <= 2}}"								, "{{test}}"	, "1");}
	@Test  public void Mod()				{fxt.tst_Parse_tmpl_str_test("{{#expr:3 mod 2}}"							, "{{test}}"	, "1");}
	@Test  public void And_1()				{fxt.tst_Parse_tmpl_str_test("{{#expr:1 and -1}}"							, "{{test}}"	, "1");}
	@Test  public void And_0()				{fxt.tst_Parse_tmpl_str_test("{{#expr:1 and  0}}"							, "{{test}}"	, "0");}
	@Test  public void Or_0()				{fxt.tst_Parse_tmpl_str_test("{{#expr:1 or  0}}"							, "{{test}}"	, "1");}
	@Test  public void Not_y()				{fxt.tst_Parse_tmpl_str_test("{{#expr:not 0}}"								, "{{test}}"	, "1");}
	@Test  public void Not_n()				{fxt.tst_Parse_tmpl_str_test("{{#expr:not 1}}"								, "{{test}}"	, "0");}
	@Test  public void Minus_op_neg()		{fxt.tst_Parse_tmpl_str_test("{{#expr:2 - 3}}"								, "{{test}}"	, "-1");}
	@Test  public void E_num()				{fxt.tst_Parse_tmpl_str_test("{{#expr:e}}"									, "{{test}}"	, "2.71828182845904");}
	@Test  public void Pi_num()				{fxt.tst_Parse_tmpl_str_test("{{#expr:pi}}"									, "{{test}}"	, "3.14159265358979");}
	@Test  public void E_op_pos()			{fxt.tst_Parse_tmpl_str_test("{{#expr:1.2 e 2}}"							, "{{test}}"	, "120");}
	@Test  public void E_op_neg()			{fxt.tst_Parse_tmpl_str_test("{{#expr:1.2 e -2}}"							, "{{test}}"	, "0.012");}
	@Test  public void Ceil_neg()			{fxt.tst_Parse_tmpl_str_test("{{#expr:ceil(-1.2)}}"							, "{{test}}"	, "-1");}
	@Test  public void Trunc_neg()			{fxt.tst_Parse_tmpl_str_test("{{#expr:trunc(-1.2)}}"						, "{{test}}"	, "-1");}
	@Test  public void Floor_neg()			{fxt.tst_Parse_tmpl_str_test("{{#expr:floor(-1.2)}}"						, "{{test}}"	, "-2");}
	@Test  public void Ceil_pos()			{fxt.tst_Parse_tmpl_str_test("{{#expr:ceil(1.2)}}"							, "{{test}}"	, "2");}
	@Test  public void Trunc_pos()			{fxt.tst_Parse_tmpl_str_test("{{#expr:trunc(1.2)}}"							, "{{test}}"	, "1");}
	@Test  public void Floor_pos()			{fxt.tst_Parse_tmpl_str_test("{{#expr:floor(1.2)}}"							, "{{test}}"	, "1");}
	@Test  public void Abs_pos()			{fxt.tst_Parse_tmpl_str_test("{{#expr:abs(1)}}"								, "{{test}}"	, "1");}
	@Test  public void Abs_neg()			{fxt.tst_Parse_tmpl_str_test("{{#expr:abs(-1)}}"							, "{{test}}"	, "1");}
	@Test  public void Exp()				{fxt.tst_Parse_tmpl_str_test("{{#expr:exp(10)}}"							, "{{test}}"	, "22026.46579480671789");}	// NOTE: MW returns 4807, not 480671789;
	@Test  public void Ln()					{fxt.tst_Parse_tmpl_str_test("{{#expr:ln(22026.4657948067)}}"				, "{{test}}"	, "10");}
	@Test  public void Sin()				{fxt.tst_Parse_tmpl_str_test("{{#expr:sin(1.5707963267949)}}"				, "{{test}}"	, "1");}
	@Test  public void Cos()				{fxt.tst_Parse_tmpl_str_test("{{#expr:cos(0)}}"								, "{{test}}"	, "1");}
	@Test  public void Tan()				{fxt.tst_Parse_tmpl_str_test("{{#expr:tan(45)}}"							, "{{test}}"	, "1.61977519054386");}
	@Test  public void Asin()				{fxt.tst_Parse_tmpl_str_test("{{#expr:asin(0)}}"							, "{{test}}"	, "0");}
	@Test  public void Acos()				{fxt.tst_Parse_tmpl_str_test("{{#expr:acos(0)}}"							, "{{test}}"	, "1.57079632679489");} // NOTE: MW (and C#) returns 49, not 489 
	@Test  public void Atan()				{fxt.tst_Parse_tmpl_str_test("{{#expr:atan(0)}}"							, "{{test}}"	, "0");}
	@Test  public void Round()				{fxt.tst_Parse_tmpl_str_test("{{#expr:1.5 round 0}}"						, "{{test}}"	, "2");}
	@Test  public void Round_2()			{fxt.tst_Parse_tmpl_str_test("{{#expr:(0.03937007874015)round(3)}}"			, "{{test}}"	, "0.039");}	// PURPOSE: rounding results in excessive decimal places; EX.WP:Milky Way (light year conversions)
	@Test  public void Mod_frac()			{fxt.tst_Parse_tmpl_str_test("{{#expr:0.00999999mod10}}"					, "{{test}}"	, "0");}
	@Test  public void Mod_large()			{fxt.tst_Parse_tmpl_str_test("{{#expr:39052000900mod100}}"					, "{{test}}"	, "0");}		// PURPOSE: JAVA was failing in converting to int and converted to Int_.MaxValue instead; DATE:2013-01-26
	@Test  public void Esc_xml_entRef()		{fxt.tst_Parse_tmpl_str_test("{{#expr:&minus;1 &lt; 5}}"					, "{{test}}"	, "1");}
	@Test  public void Ex_1()				{fxt.tst_Parse_tmpl_str_test("{{#expr:1e2round0}}"							, "{{test}}"	, "100");}		// PURPOSE: used in Convert
	@Test  public void Floating()			{fxt.tst_Parse_tmpl_str_test("{{#expr:27.321582}}"							, "{{test}}"	, "27.321582");}
	@Test  public void Floating_2()			{fxt.tst_Parse_tmpl_str_test("{{#expr:0.1*41}}"								, "{{test}}"	, "4.1");}		// PURPOSE: division results in expanded floating-point; EX.WP:Wikipedia
	@Test  public void Floating_3()			{fxt.tst_Parse_tmpl_str_test("{{#expr:111/10^(-1)}}"						, "{{test}}"	, "1110");}		// PURPOSE: division by pow; EX.WP:Wikipedia:Featured articles
	@Test  public void Floating_4()			{fxt.tst_Parse_tmpl_str_test("{{#expr:abs(-73.9023)}}"						, "{{test}}"	, "73.9023");}	// PURPOSE: Abs;
	@Test  public void Unicode_8722()		{fxt.tst_Parse_tmpl_str_test("{{#expr:2−1}}"								, "{{test}}"	, "1");}		// PURPOSE: handle alternate minus; EX.WP: Australian krill
	@Test  public void Exp_large_neg()		{fxt.tst_Parse_tmpl_str_test("{{#expr:418400000000000000000000E-23}}"		, "{{test}}"	, "4.184");}	// PURPOSE: handle large neg; EX: w:Chicxulub_crater; {{convert|100|TtonTNT|J|lk=on}}
	@Test  public void Exp_large_neg2()		{fxt.tst_Parse_tmpl_str_test("{{#expr:210000000000000000E-17}}"				, "{{test}}"	, "2.1");}		// PURPOSE: handle large neg2; EX: w:Chicxulub_crater; {{convert|50|MtonTNT|J|lk=on}}
	@Test  public void Fix_transclusion()	{fxt.tst_Parse_tmpl_str_test("{{#expr:{{#if:||1}}/.2}}"						, "{{test}}"	, "5");}		// PURPOSE: /. was invoking transclusion; DATE:2013-04-26
	@Test  public void Exc_unrecognised_word()					{fxt.tst_Parse_tmpl_str_test("{{#expr:abc}}"				, "{{test}}"	, "<strong class=\"error\">Expression error: Unrecognised word \"abc\"</strong>");}
	@Test  public void Exc_division_by_zero()					{fxt.tst_Parse_tmpl_str_test("{{#expr:1/0}}"				, "{{test}}"	, "<strong class=\"error\">Division by zero</strong>");}
	@Test  public void Exc_division_by_zero_mod()				{fxt.tst_Parse_tmpl_str_test("{{#expr:1 mod 0}}"			, "{{test}}"	, "<strong class=\"error\">Division by zero</strong>");}
	@Test  public void Exc_unexpected_number()					{fxt.tst_Parse_tmpl_str_test("{{#expr:0 1}}"				, "{{test}}"	, "<strong class=\"error\">Expression error: Unexpected number</strong>");}
	@Test  public void Exc_unexpected_closing_bracket()			{fxt.tst_Parse_tmpl_str_test("{{#expr:5 + 1)}}"				, "{{test}}"	, "<strong class=\"error\">Expression error: Unexpected closing bracket</strong>");}
	@Test  public void Exc_unclosed_bracket()					{fxt.tst_Parse_tmpl_str_test("{{#expr:(5 + 1}}"				, "{{test}}"	, "<strong class=\"error\">Expression error: Unclosed bracket</strong>");}
	@Test  public void Exc_unexpected_operator_paren_end()		{fxt.tst_Parse_tmpl_str_test("{{#expr:5 ( 1}}"				, "{{test}}"	, "<strong class=\"error\">Expression error: Unexpected ( operator</strong>");}
	@Test  public void Exc_unexpected_number_pi()				{fxt.tst_Parse_tmpl_str_test("{{#expr:5 pi}}"				, "{{test}}"	, "<strong class=\"error\">Expression error: Unexpected number</strong>");}
	@Test  public void Exc_missing_operand()					{fxt.tst_Parse_tmpl_str_test("{{#expr:5 *}}"				, "{{test}}"	, "<strong class=\"error\">Expression error: Missing operand for *</strong>");}
	@Test  public void Exc_invalid_argument_asin()				{fxt.tst_Parse_tmpl_str_test("{{#expr:asin(2)}}"			, "{{test}}"	, "<strong class=\"error\">Invalid argument for asin: < -1 or > 1</strong>");}
	@Test  public void Exc_invalid_argument_acos()				{fxt.tst_Parse_tmpl_str_test("{{#expr:acos(2)}}"			, "{{test}}"	, "<strong class=\"error\">Invalid argument for acos: < -1 or > 1</strong>");}
	@Test  public void Exc_invalid_argument_ln()				{fxt.tst_Parse_tmpl_str_test("{{#expr:ln(-1)}}"				, "{{test}}"	, "<strong class=\"error\">Invalid argument for ln: <= 0</strong>");}
	@Test  public void Exc_pow_nan()							{fxt.tst_Parse_tmpl_str_test("{{#expr:(-2)^1.2}}"			, "{{test}}"	, "NaN");}	// PURPOSE: handle nan; EX: w:Help:Calculation
	@Test  public void Exc_unrecognized_word_ornot()			{fxt.tst_Parse_tmpl_str_test("{{#expr:0ornot0}}"			, "{{test}}"	, "<strong class=\"error\">Expression error: Unrecognised word \"ornot\"</strong>");}	// PURPOSE: handle nan; EX: w:Help:Calculation
	@Test  public void Exc_unrecognized_word_notnot()			{fxt.tst_Parse_tmpl_str_test("{{#expr:notnot0}}"			, "{{test}}"	, "<strong class=\"error\">Expression error: Unrecognised word \"notnot\"</strong>");}	// PURPOSE: handle nan; EX: w:Help:Calculation
	@Test  public void Exc_unrecognized_word_sinln()			{fxt.tst_Parse_tmpl_str_test("{{#expr:sinln1.1}}"			, "{{test}}"	, "<strong class=\"error\">Expression error: Unrecognised word \"sinln\"</strong>");}	// PURPOSE: handle nan; EX: w:Help:Calculation
}

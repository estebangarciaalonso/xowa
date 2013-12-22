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
public class Pf_tag_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init()			{fxt.Reset();}
	@Test  public void Basic()			{fxt.tst_Parse_tmpl_str_test("{{#tag:div|a|atr1=val1|atr2=val2}}"					, "{{test}}"	, "<div atr1=\"val1\" atr2=\"val2\">a</div>");}
//		@Test  public void Missing_val()	{fxt.ini_Msg(Mwl_tag_rsc._.Invalid).tst_Parse_tmpl_str_test("{{#tag:div|a|atr1=}}"	, "{{test}}"	, "");}	// see {{Reflist|colwidth=30em}} -> <ref group=a>a</ref>{{#tag:references||group=}} -> ""
	@Test  public void Atr2_empty()		{fxt.tst_Parse_tmpl_str_test("{{#tag:div|a|atr1=val1|}}"							, "{{test}}"	, "<div atr1=\"val1\">a</div>");}	// see {{Reflist|colwidth=30em}} -> <ref group=a>a</ref>{{#tag:references||group=a|}} -> "<references group=a/>"
	@Test  public void Val_apos()		{fxt.tst_Parse_tmpl_str_test("{{#tag:div|a|atr1='val1'}}"							, "{{test}}"	, "<div atr1=\"val1\">a</div>");}
	@Test  public void Val_quote()		{fxt.tst_Parse_tmpl_str_test("{{#tag:div|a|atr1=\"val1\"}}"							, "{{test}}"	, "<div atr1=\"val1\">a</div>");}
	@Test  public void Ws_all()		    {fxt.tst_Parse_tmpl_str_test("{{#tag:div|a|   atr1   =   val1   }}"					, "{{test}}"	, "<div atr1=\"val1\">a</div>");}
	@Test  public void Ws_quoted()		{fxt.tst_Parse_tmpl_str_test("{{#tag:div|a|   atr1   = ' val1 ' }}"					, "{{test}}"	, "<div atr1=\" val1 \">a</div>");}
	@Test  public void Err_bad_key()	{fxt.tst_Parse_tmpl_str_test("{{#tag:div|a|atr1=val|b}}"							, "{{test}}"	, "<div atr1=\"val\" \"b\">a</div>");}	// PURPOSE: b was failing b/c atr1 was larger and key_end set to 4 (whereas b was len=1)
//		@Test  public void Exc()		{
//			fxt.tst_Parse_tmpl_str_test("{{#tag:ref|George Robertson announced in January 2003 that he would be stepping down in December.<ref> {{cite news|title =NATO Secretary General to Leave His Post in December After 4 Years |first = Craig | last = Smith | work = The New York Times | date = January 23, 2003| url = http://www.nytimes.com/2003/01/23/world/nato-secretary-general-to-leave-his-post-in-december-after-4-years.html?scp=2&sq=lord+robertson&st=nyt|accessdate = 2009-03-29}}</ref> Jaap de Hoop Scheffer was selected as his successor, but could not assume the office until January 2004 because of his commitment in the Dutch Parliament.<ref> {{cite news|title = Jaap de Hoop Scheffer | work = Newsmakers | issue = 1 | publisher = Thomson Gale | date = January 1, 2005}}</ref> Robertson was asked to extend his term until Scheffer was ready, but declined, so Minuto-Rizzo, the Deputy Secretary General, took over in the interim.<ref name =\"ncsd\" />  |group=N|}}"
//				, "{{test}}"	, "<div atr1=\" val1 \">a</div>");}
	@Test  public void Nested_tmpl() {	// PURPOSE: nested template must get re-evaluated; EX:de.wikipedia.org/wiki/Freiburg_im_Breisgau; DATE:2013-12-18
		fxt.ini_page_create("Template:!", "|");
		fxt.ini_page_create("Template:A", "{{#ifeq:{{{1}}}|expd|pass|fail}}");
		fxt.tst_Parse_tmpl_str_test("{{#tag:span|{{A{{!}}expd}}}}"					, "{{test}}"	, "<span>pass</span>");
	}
}

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
package gplx.xowa.xtns.pfuncs.ttls; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
import org.junit.*;
public class Pfunc_anchorencode_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init()				{fxt.Reset();}
	@Test  public void Lnke()				{fxt.Test_parse_tmpl_str_test("{{anchorencode:[irc://a b c]}}"					, "{{test}}"	, "b_c");}
	@Test  public void Apos_bold()			{fxt.Test_parse_tmpl_str_test("{{anchorencode:a ''b'' c}}"						, "{{test}}"	, "a_b_c");}
	@Test  public void Apos_1()				{fxt.Test_parse_tmpl_str_test("{{anchorencode:a 'b c}}"							, "{{test}}"	, "a_.27b_c");}
	@Test  public void Lnki_trg()			{fxt.Test_parse_tmpl_str_test("{{anchorencode:a [[b]] c}}"						, "{{test}}"	, "a_b_c");}
	@Test  public void Lnki_caption()		{fxt.Test_parse_tmpl_str_test("{{anchorencode:a [[b|c]] c}}"					, "{{test}}"	, "a_c_c");}
	@Test  public void Lnki_file()			{fxt.Test_parse_tmpl_str_test("{{anchorencode:a [[Image:b|thumb|c]] d}}"		, "{{test}}"	, "a_thumb.7Cc_d");}
	@Test  public void Xnde()				{fxt.Test_parse_tmpl_str_test("{{anchorencode:a <i>b</i> c}}"					, "{{test}}"	, "a_b_c");}
	@Test  public void Html_ncr()			{fxt.Test_parse_tmpl_str_test("{{anchorencode:a &#34; b}}"						, "{{test}}"	, "a_.22_b");}
	@Test  public void Html_ref()			{fxt.Test_parse_tmpl_str_test("{{anchorencode:a &quot; b}}"						, "{{test}}"	, "a_.22_b");}
	@Test  public void Tmpl_missing_basic() {fxt.Test_parse_tmpl_str_test("{{anchorencode:{{a}}}}"							, "{{test}}"	, "Template:a");}
	@Test  public void Tmpl_missing_colon() {fxt.Test_parse_tmpl_str_test("{{anchorencode:{{:a}}}}"							, "{{test}}"	, "Template:A");}
	@Test  public void Lnki_literal()		{fxt.Test_parse_tmpl_str_test("{{anchorencode:[[:a]]}}"							, "{{test}}"	, "a");}
}

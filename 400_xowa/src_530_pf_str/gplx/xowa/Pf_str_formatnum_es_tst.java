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
import gplx.intl.*; import gplx.xowa.langs.numFormats.*;
public class Pf_str_formatnum_es_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {
		fxt.Reset();
		fxt.Wiki().Lang().Num_fmt_mgr().Clear().Dec_dlm_(new byte[] {Byte_ascii.Comma}).Grps_add(new Xol_num_grp(new byte[] {Byte_ascii.Dot}, 3, true));
	}
	@After public void term() {
		fxt.Wiki().Lang().Num_fmt_mgr().Clear().Dec_dlm_(new byte[] {Byte_ascii.Dot}).Grps_add(new Xol_num_grp(new byte[] {Byte_ascii.Comma}, 3, true));
	}
	@Test  public void Fmt__plain()			{fxt.Test_parse_tmpl_str_test("{{formatnum:1234,56}}"					, "{{test}}"	, "1.234.56");}	// NOTE: double "." looks strange, but matches MW; DATE:2013-10-24
	@Test  public void Fmt__grp_dlm()		{fxt.Test_parse_tmpl_str_test("{{formatnum:1.234,56}}"					, "{{test}}"	, "1,234.56");}
	@Test  public void Fmt__dec_dlm()		{fxt.Test_parse_tmpl_str_test("{{formatnum:1234.56}}"					, "{{test}}"	, "1.234,56");} // NOTE: "." should be treated as decimal separator, but replaced with ","; DATE:2013-10-21
	@Test  public void Raw__grp_dlm()		{fxt.Test_parse_tmpl_str_test("{{formatnum:1.234,56|R}}"					, "{{test}}"	, "1234.56");}
	@Test  public void Raw__plain()			{fxt.Test_parse_tmpl_str_test("{{formatnum:1234,56|R}}"					, "{{test}}"	, "1234.56");}
	@Test  public void Raw__dec_dlm()		{fxt.Test_parse_tmpl_str_test("{{formatnum:12,34|R}}"					, "{{test}}"	, "12.34");}	// NOTE: dec_dlm is always ".
	@Test  public void Nosep__plain()		{fxt.Test_parse_tmpl_str_test("{{formatnum:1234,56|NOSEP}}"				, "{{test}}"	, "1234,56");}
	@Test  public void Nosep__grp_dlm()		{fxt.Test_parse_tmpl_str_test("{{formatnum:1.234,56|NOSEP}}"				, "{{test}}"	, "1.234,56");}
}

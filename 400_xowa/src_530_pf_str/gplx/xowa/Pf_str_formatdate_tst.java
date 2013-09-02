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
public class Pf_str_formatdate_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Before public void init()					{fxt.Reset();}
	@Test  public void Fmt_dmy()				{fxt.tst_Parse_tmpl_str_test("{{#formatdate:2012-01-02|dmy}}"			, "{{test}}"			, "2 January 2012");}
	@Test  public void Fmt_mdy()				{fxt.tst_Parse_tmpl_str_test("{{#formatdate:2012-01-02|mdy}}"			, "{{test}}"			, "January 2, 2012");}
	@Test  public void Fmt_ymd()				{fxt.tst_Parse_tmpl_str_test("{{#formatdate:2012-01-02|ymd}}"			, "{{test}}"			, "2012 January 2");}
	@Test  public void Fmt_ISO_8601()			{fxt.tst_Parse_tmpl_str_test("{{#formatdate:2012-01-02|ISO 8601}}"		, "{{test}}"			, "2012-01-02");}
	@Test  public void Fmt_default()			{fxt.tst_Parse_tmpl_str_test("{{#formatdate:2012-01-02|default}}"		, "{{test}}"			, "2012-01-02");}	// NOOP?
	@Test  public void Fmt_none()				{fxt.tst_Parse_tmpl_str_test("{{#formatdate:10 April 2012}}"			, "{{test}}"			, "10 April 2012");}
	@Test  public void Err_multiple_years()		{fxt.tst_Parse_tmpl_str_test("{{#formatdate:January 2, 1999, 2000|Y}}"	, "{{test}}"			, "January 2, 1999, 2000");}	// PURPOSE: check that multiple years don't fail
}
/*
{{#formatdate:2012-01-02|dmy}}<br/>
{{#formatdate:2012-01-02|mdy}}<br/>
{{#formatdate:2012-01-02|ymd}}<br/>
{{#formatdate:2012-01-02|ISO 8601}}<br/>
{{#formatdate:2012-01-02|default}}<br/>
*/

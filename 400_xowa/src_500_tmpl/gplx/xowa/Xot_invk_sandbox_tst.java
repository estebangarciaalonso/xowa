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
public class Xot_invk_sandbox_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {
		fxt.Reset();
		fxt.ini_defn_clear();
		fxt.ini_defn_add("concat", "{{{1}}}{{{2}}}");
	}
	@Test  public void Basic() {
		fxt.tst_Parse_tmpl_str("{{concat|a|b}}", "ab");
	}
	@Test  public void Basic_too_many() {	// c gets ignored
		fxt.tst_Parse_tmpl_str("{{concat|a|b|c}}", "ab");
	}
	@Test  public void Basic_too_few() {
		fxt.tst_Parse_tmpl_str("{{concat|a}}", "a{{{2}}}");
	}
	@Test  public void Basic_else() {
		fxt.ini_defn_add("concat", "{{{1}}}{{{2|?}}}");
		fxt.tst_Parse_tmpl_str("{{concat|a}}", "a?");
	}
	@Test  public void Eq_2() {
		fxt.ini_defn_add("concat", "{{{lkp1}}}");
		fxt.tst_Parse_tmpl_str("{{concat|lkp1=a=b}}", "a=b");
	}
	@Test  public void Recurse()		{fxt.tst_Parse_tmpl_str_test("<{{concat|{{{1}}}|{{{2}}}}}>"	, "{{test|a|b}}", "<ab>");}
	@Test  public void Recurse_mix()	{fxt.tst_Parse_tmpl_str_test("{{concat|.{{{1}}}.|{{{2}}}}}"	, "{{test|a|b}}", ".a.b");}
	@Test  public void Recurse_err()	{fxt.tst_Parse_tmpl_str_test("{{concat|{{{1}}}|{{{2}}}}}"	, "{{test1|a|b}}", "{{:test1}}");} // NOTE: make sure test1 does not match test
	@Test  public void KeyNewLine()		{fxt.tst_Parse_tmpl_str_test("{{\n  concat|a|b}}"			, "{{\n  test}}", "ab");}
}
/*
*/
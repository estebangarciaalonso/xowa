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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Scrib_lib_ustring_tst {
	@Before public void init() {
		fxt.Clear();
		fxt.Init_page("{{#invoke:Mod_0|Func_0}}");
		lib = fxt.Engine().Lib_ustring();
	}	Scrib_pf_invoke_fxt fxt = new Scrib_pf_invoke_fxt(); Scrib_lib lib;
	@Test  public void Find() {			
		fxt.Init_cbk(Scrib_engine.Key_mw_interface, fxt.Engine().Lib_ustring(), Scrib_lib_ustring.Invk_find);
		Exec_find("abcd"	, "b"				, 1, false, "2;2");					// basic
		Exec_find("abac"	, "a"				, 2, false, "3;3");					// bgn
		Exec_find("()()"	, "("				, 2, true , "3;3");					// plain; note that ( would "break" regx
		Exec_find("a bcd e"	, "(b(c)d)"			, 2, false, "3;5;bcd;c");			// groups
		Exec_find("a bcd e"	, "()(b)"			, 2, false, "3;3;3;b");				// groups; empty capture
		Exec_find("abcd"	, "x"				, 1, false, "");					// empty
	}
	@Test  public void Match() {
		fxt.Init_cbk(Scrib_engine.Key_mw_interface, fxt.Engine().Lib_ustring(), Scrib_lib_ustring.Invk_match);
		Exec_match("abcd"	, "bc"				, 1, "bc");							// basic
		Exec_match("abcd"	, "x"				, 1, "");							// empty
		Exec_match("abcd"	, "a"				, 2, "");							// bgn
		Exec_match("abcd"	, "b(c)"			, 1, "c");							// group
		Exec_match(" a b "	, "^%s*(.-)%s*$"	, 1, "a b");						// trim
		Exec_match("abcd"	, "a"				, 0, "a");							// handle 0; note that php/lua is super-1, but some modules pass in 0; ru.w:Module:Infocards; DATE:2013-11-08
		Exec_match("abcd"	, "."				, -1, "d");							// -1
	}
	@Test  public void Gsub() {
		fxt.Init_cbk(Scrib_engine.Key_mw_interface, fxt.Engine().Lib_ustring(), Scrib_lib_ustring.Invk_gsub);
		Exec_gsub_regx("abcd", "[a]"		, -1, "A", "Abcd;1");
		Exec_gsub_regx("abcd", "[ac]"		, -1, Scrib_kv_utl.flat_many_("a", "A", "c", "C"), "AbCd;2");
		Exec_gsub_regx("aaaa", "[a]"		, 2, "A", "AAaa;2");
		Exec_gsub_regx("a"	, "(a)"			, 1, "%%%1", "%a;1");
		Exec_gsub_regx("à{b}c", "{b}"		, 1, "b", "àbc;1");		// utf8
		Exec_gsub_regx("àbc", "^%s*(.-)%s*$", 1, "%1", "àbc;1");	// utf8; regx is for trim line
		Exec_gsub_regx("a"	, "[^]"			, 1, "b", "a;0");		// invalid regx should not fail; should return self; DATE:2013-10-20
	}
	@Test  public void Gsub_proc() {
		fxt.Init_cbk(Scrib_engine.Key_mw_interface, fxt.Engine().Lib_ustring(), Scrib_lib_ustring.Invk_gsub);
		Exec_gsub_regx_func_0("abcd", "([a])", "Abcd;1");
	}
	@Test  public void Gsub_proc_w_grouped() {	// PURPOSE: gsub_proc should pass matched String, not entire String; DATE:2013-12-01
		fxt.Init_cbk(Scrib_engine.Key_mw_interface, fxt.Engine().Lib_ustring(), Scrib_lib_ustring.Invk_gsub);
		Exec_gsub_regx_func_1("[[a]]", "%[%[([^#|%]]-)%]%]"	, "A;1");
		fxt.Test_log_rcvd(3, "000000370000006D{[\"op\"]=\"call\",[\"id\"]=1,[\"nargs\"]=1,[\"args\"]={[1]=\"a\"}}");	// should be "a", not "[[a]]"
	}
	@Test  public void Gsub_proc_w_grouped_2() {// PURPOSE: gsub_proc failed when passing multiple matches; DATE:2013-12-01
		fxt.Init_cbk(Scrib_engine.Key_mw_interface, fxt.Engine().Lib_ustring(), Scrib_lib_ustring.Invk_gsub);
		Exec_gsub_regx_func_2("[[a]] [[b]]", "%[%[([^#|%]]-)%]%]"	, "A B;2");
		fxt.Test_log_rcvd(3, "000000370000006D{[\"op\"]=\"call\",[\"id\"]=1,[\"nargs\"]=1,[\"args\"]={[1]=\"a\"}}");	// should be "a", not "[[a]]"
		fxt.Test_log_rcvd(4, "000000370000006D{[\"op\"]=\"call\",[\"id\"]=1,[\"nargs\"]=1,[\"args\"]={[1]=\"b\"}}");	// should be "b", not "[[b]]"
	}
	@Test  public void Gsub_no_replace() {// PURPOSE: gsub with no replace argument should not fail; EX:d:'orse; DATE:2013-10-14
		fxt.Init_cbk(Scrib_engine.Key_mw_interface, fxt.Engine().Lib_ustring(), Scrib_lib_ustring.Invk_gsub);
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_gsub, Scrib_kv_utl.base1_many_("text", "regx"));	// NOTE: repl, limit deliberately omitted
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke("text");
	}
	@Test  public void Gsub_int() {	// PURPOSE: gsub with integer arg should not fail; DATE:2013-11-06
		fxt.Init_cbk(Scrib_engine.Key_mw_interface, fxt.Engine().Lib_ustring(), Scrib_lib_ustring.Invk_gsub);
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_gsub, Scrib_kv_utl.base1_many_(1, "[1]", "2", 1));	// NOTE: text is integer (lua / php are type-less)
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke("2;1");
	}
	@Test  public void Gmatch_init() {
		fxt.Test_lib_proc(lib, Scrib_lib_ustring.Invk_gmatch_init, Object_.Ary("abcabc", "a(b)")					, "a(b);\n  false");
	}
	@Test  public void Gmatch_callback() {
		fxt.Test_lib_proc(lib, Scrib_lib_ustring.Invk_gmatch_callback, Object_.Ary("abcabc", "a(b)", Scrib_kv_utl.base1_obj_ary_(false), 0)	, "2;\n  b");
		fxt.Test_lib_proc(lib, Scrib_lib_ustring.Invk_gmatch_callback, Object_.Ary("abcabc", "a(b)", Scrib_kv_utl.base1_obj_ary_(false), 2)	, "5;\n  b");
		fxt.Test_lib_proc(lib, Scrib_lib_ustring.Invk_gmatch_callback, Object_.Ary("abcabc", "a(b)", Scrib_kv_utl.base1_obj_ary_(false), 8)	, "8;{}");
	}
	private void Exec_find(String text, String regx, int bgn, boolean plain, String expd) {
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_find, Scrib_kv_utl.base1_many_(text, regx, bgn, plain));
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke(expd);
	}
	private void Exec_match(String text, String regx, int bgn, String expd) {
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_match, Scrib_kv_utl.base1_many_(text, regx, bgn));
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke(expd);
	}
	private void Exec_gsub_regx(String text, String regx, int limit, Object repl, String expd) {Exec_gsub(text, regx, limit, repl, expd);}
	private void Exec_gsub(String text, String regx, int limit, Object repl, String expd) {
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_gsub, Scrib_kv_utl.base1_many_(text, regx, repl, limit));
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke(expd);
	}
	private void Exec_gsub_regx_func_0(String text, String regx, String expd) {
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_gsub, Scrib_kv_utl.base1_many_(text, regx, new Scrib_fnc("ignore_key", 1)));
		fxt.Init_lua_rcvd_raw("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:1:\"A\";}}");
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke(expd);
	}
	private void Exec_gsub_regx_func_1(String text, String regx, String expd) {
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_gsub, Scrib_kv_utl.base1_many_(text, regx, new Scrib_fnc("ignore_key", 1)));
		fxt.Init_lua_rcvd_raw("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:1:\"A\";}}");
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke(expd);
	}
	private void Exec_gsub_regx_func_2(String text, String regx, String expd) {
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_gsub, Scrib_kv_utl.base1_many_(text, regx, new Scrib_fnc("ignore_key", 1)));
		fxt.Init_lua_rcvd_raw("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:1:\"A\";}}");
		fxt.Init_lua_rcvd_raw("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:1:\"B\";}}");
		fxt.Init_lua_rcvd_rv();
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke(expd);
	}
}

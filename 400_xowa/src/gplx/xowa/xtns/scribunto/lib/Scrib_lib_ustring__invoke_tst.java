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
package gplx.xowa.xtns.scribunto.lib; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*;
import org.junit.*;
public class Scrib_lib_ustring__invoke_tst {
	@Before public void init() {
		fxt.Clear_for_invoke();
		fxt.Init_page("{{#invoke:Mod_0|Func_0}}");
		fxt.Core().Lib_ustring().Init();
	}	private Scrib_invoke_func_fxt fxt = new Scrib_invoke_func_fxt();
	@Test  public void Gsub_proc() {
		fxt.Init_cbk(Scrib_core.Key_mw_interface, fxt.Core().Lib_ustring(), Scrib_lib_ustring.Invk_gsub);
		Exec_gsub_regx_func_0("abcd", "([a])", "Abcd;1");
	}
	@Test  public void Gsub_proc_w_grouped() {	// PURPOSE: gsub_proc should pass matched String, not entire String; DATE:2013-12-01
		fxt.Init_cbk(Scrib_core.Key_mw_interface, fxt.Core().Lib_ustring(), Scrib_lib_ustring.Invk_gsub);
		Exec_gsub_regx_func_1("[[a]]", "%[%[([^#|%]]-)%]%]"	, "A;1");
		fxt.Test_log_rcvd(3, "000000370000006D{[\"op\"]=\"call\",[\"id\"]=1,[\"nargs\"]=1,[\"args\"]={[1]=\"a\"}}");	// should be "a", not "[[a]]"
	}
	@Test  public void Gsub_proc_w_grouped_2() {// PURPOSE: gsub_proc failed when passing multiple matches; DATE:2013-12-01
		fxt.Init_cbk(Scrib_core.Key_mw_interface, fxt.Core().Lib_ustring(), Scrib_lib_ustring.Invk_gsub);
		Exec_gsub_regx_func_2("[[a]] [[b]]", "%[%[([^#|%]]-)%]%]"	, "A B;2");
		fxt.Test_log_rcvd(3, "000000370000006D{[\"op\"]=\"call\",[\"id\"]=1,[\"nargs\"]=1,[\"args\"]={[1]=\"a\"}}");	// should be "a", not "[[a]]"
		fxt.Test_log_rcvd(4, "000000370000006D{[\"op\"]=\"call\",[\"id\"]=1,[\"nargs\"]=1,[\"args\"]={[1]=\"b\"}}");	// should be "b", not "[[b]]"
	}
	@Test  public void Gsub_int() {	// PURPOSE: gsub with integer arg should not fail; DATE:2013-11-06
		fxt.Init_cbk(Scrib_core.Key_mw_interface, fxt.Core().Lib_ustring(), Scrib_lib_ustring.Invk_gsub);
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_gsub, Scrib_kv_utl_.base1_many_(1, "[1]", "2", 1));	// NOTE: text is integer (lua / php are type-less)
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke("2;1");
	}
	private void Exec_gsub_regx_func_0(String text, String regx, String expd) {
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_gsub, Scrib_kv_utl_.base1_many_(text, regx, new Scrib_lua_proc("ignore_key", 1)));
		fxt.Init_lua_rcvd_raw("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:1:\"A\";}}");
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke(expd);
	}
	private void Exec_gsub_regx_func_1(String text, String regx, String expd) {
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_gsub, Scrib_kv_utl_.base1_many_(text, regx, new Scrib_lua_proc("ignore_key", 1)));
		fxt.Init_lua_rcvd_raw("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:1:\"A\";}}");
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke(expd);
	}
	private void Exec_gsub_regx_func_2(String text, String regx, String expd) {
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd(Scrib_lib_ustring.Invk_gsub, Scrib_kv_utl_.base1_many_(text, regx, new Scrib_lua_proc("ignore_key", 1)));
		fxt.Init_lua_rcvd_raw("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:1:\"A\";}}");
		fxt.Init_lua_rcvd_raw("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:1:\"B\";}}");
		fxt.Init_lua_rcvd_rv();
		fxt.Init_lua_rcvd_rv();
		fxt.Test_invoke(expd);
	}
}

/*
XOWA: the extensible offline wiki application
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
public class Scrib_lib_mw_tst {
	@Before public void init() {
		fxt.Clear();
		lib = fxt.Engine().Lib_mw();
	}	private Scrib_pf_invoke_fxt fxt = new Scrib_pf_invoke_fxt(); private Scrib_lib lib;
	@Test  public void ParentFrameExists() {
		fxt.Init_tmpl("{{#invoke:Mod_0|Prc_0}}");
		fxt.Init_page("{{test}}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_parentFrameExists, Object_.Ary_empty, "true");
	}
	@Test  public void ParentFrameExists_false() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_parentFrameExists, Object_.Ary_empty, "false");
	}
	@Test  public void GetAllExpandedArguments() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0|v0|k1=v1}}");
		fxt.Init_server_print_key_y_();
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getAllExpandedArguments, Object_.Ary("current"), "\n  1:v0;k1:v1");
		fxt.Init_server_print_key_n_();
	}
	@Test  public void GetAllExpandedArguments_parent() {
		fxt.Init_tmpl("{{#invoke:Mod_0|Prc_0|b1}}");
		fxt.Init_page("{{test|a1|a2}}");
		fxt.Init_server_print_key_y_();
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getAllExpandedArguments, Object_.Ary("parent"), "\n  1:a1;2:a2");
		fxt.Init_server_print_key_n_();
	}
	@Test  public void GetExpandedArgument() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0|val_1|key_2=val_2|val_3}}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("current", "1")		, "val_1");			// get 1st by idx
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("current", "2")		, "val_3");			// get 2nd by idx (which is "3", not "key_2)
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("current", "3")		, "");				// get 3rd by idx (which is n/a, not "3")
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("current", "key_2")	, "val_2");			// get key_2
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("current", "key_3")	, "");				// key_3 n/a
	}
	@Test  public void GetExpandedArgument_parent() {
		fxt.Init_tmpl("{{#invoke:Mod_0|Prc_0|b1}}");
		fxt.Init_page("{{test|a1|a2}}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("parent", "1"), "a1");
	}
	@Test  public void GetExpandedArgument_numeric_key() {		// PURPOSE.FIX: frame.args[1] was ignoring "1=val_1" b/c it was looking for 1st unnamed arg (and 1 is the name for "1=val_1")
		fxt.Init_page("{{#invoke:Mod_0|Prc_0|1=val_1}}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("current", "1")		, "val_1");			// get 1st by idx, even though idx is String
	}
	@Test  public void GetExpandedArgument_out_of_bounds() {
		fxt.Init_tmpl("{{#invoke:Mod_0|Prc_0|b1}}");
		fxt.Init_page("{{test}}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("parent", "2")		, "");
	}
	@Test  public void Preprocess() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0|key1=a|key2=b|key1=c}}");	// add key1 twice
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_preprocess, Object_.Ary("current", "{{#ifeq:1|1|{{{key1}}}|{{{key2}}}}}"), "c");
	}
	@Test  public void CallParserFunction() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Test_lib_proc_kv(lib, Scrib_lib_mw.Invk_callParserFunction, Scrib_kv_utl.flat_many_("frameId", "current", "name", "#tag", "args", "b")												, "<b></b>");				// named: args is scalar
		fxt.Test_lib_proc_kv(lib, Scrib_lib_mw.Invk_callParserFunction, Scrib_kv_utl.flat_many_("frameId", "current", "name", "#tag", "args", Scrib_kv_utl.base1_many_("b", "text", "id=1"))	, "<b id=\"1\">text</b>");	// named: args is table
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_callParserFunction, Object_.Ary("current", "#tag", "b", "text", "id=1")							, "<b id=\"1\">text</b>");	// list: args is ary
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_callParserFunction, Object_.Ary("current", "#tag", Scrib_kv_utl.base1_many_("b", "text", "id=1"))	, "<b id=\"1\">text</b>");	// list: args is table
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_callParserFunction, Object_.Ary("current", "#tag:b", "text", "id=1")								, "<b id=\"1\">text</b>");	// colon_in_name
		fxt.Test_lib_proc_kv(lib, Scrib_lib_mw.Invk_callParserFunction, Scrib_kv_utl.flat_many_("frameId", "current", "name", "#tag", "args", Scrib_kv_utl.flat_many_("3", "id=1", "2", "text", "1", "b")), "<b 3=\"id=1\">2=text</b>");// named: sort args; NOTE: keys should probably be stripped
	}
	@Test  public void CallParserFunction_displayTitle() {	// PURPOSE: DISPLAYTITLE not being set when called through CallParserFunction; DATE:2013-08-05
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Test_lib_proc_kv(lib, Scrib_lib_mw.Invk_callParserFunction, Scrib_kv_utl.flat_many_("frameId", "current", "name", "DISPLAYTITLE", "args", "''a''"), "");
		Tfds.Eq("<i>a</i>", String_.new_ascii_(fxt.Parser_fxt().Ctx().Tab().Display_ttl()));
	}
}

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
public class Scrib_lib_mw__invoke_tst {
	@Before public void init() {
		fxt.Clear_for_invoke();
		lib = fxt.Core().Lib_mw().Init();
	}	private Scrib_invoke_func_fxt fxt = new Scrib_invoke_func_fxt(); private Scrib_lib lib;
	@Test  public void GetAllExpandedArguments_ws_prm_key_exists() {	// PURPOSE: trim val if key exists; parameterized value ("key={{{1}}}")
		fxt.Init_tmpl("{{#invoke:Mod_0|Prc_0|key={{{1}}}}}");
		fxt.Init_page("{{test| a }}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getAllExpandedArguments, Object_.Ary("current"),  "\n  a");				// " a " -> "a"
	}
	@Test  public void GetAllExpandedArguments_ws_prm_key_missing() {	// PURPOSE: do not trim val if key missing; parameterized value ("{{{1}}}")
		fxt.Init_tmpl("{{#invoke:Mod_0|Prc_0|{{{1}}}}}");
		fxt.Init_page("{{test| a }}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getAllExpandedArguments, Object_.Ary("current"),  "\n   a ");				// " a " -> " a "
	}
	@Test  public void GetAllExpandedArguments__ignore_empty_key() {// PURPOSE: ignore arguents that have an empty key (|=8|); EX:w:Fool's_mate; DATE:2014-03-05
		fxt.Init_tmpl("{{#invoke:Mod_0|Prc_0}}");
		fxt.Init_page("{{test|a1||a2|=a3|a4}}");
		fxt.Init_server_print_key_y_();
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getAllExpandedArguments, Object_.Ary("parent"), "\n  1:a1;2:;3:a2;4:a4");	// NOTE: || is not ignored but |=a3| is
		fxt.Init_server_print_key_n_();
	}
	@Test  public void GetExpandedArgument_ws_key_exists() {	// PURPOSE: trim val if key exists; literal value
		fxt.Init_page("{{#invoke:Mod_0|Prc_0| key1 = val1 }}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("current", "key1")	, "val1");			// "key1" -> "key1"
	}
	@Test  public void GetExpandedArgument_ws_key_missing() {	// PURPOSE: do not trim val if key missing; literal value
		fxt.Init_page("{{#invoke:Mod_0|Prc_0| a }}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("current", "1")		, " a ");			// " a " -> " a "
	}
	@Test  public void GetExpandedArgument_ws_key_prm_key_exists() {	// PURPOSE: trim val if key exists; parameterized value ("key={{{1}}}")
		fxt.Init_tmpl("{{#invoke:Mod_0|Prc_0|key1={{{1}}}}}");
		fxt.Init_page("{{test| a }}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("current", "key1")	, "a");				// " a " -> "a"
	}
	@Test  public void GetExpandedArgument_ws_key_prm_key_missing() {	// PURPOSE: do not trim val if key missing; parameterized value ("{{{1}}}")
		fxt.Init_tmpl("{{#invoke:Mod_0|Prc_0|{{{1}}}}}");
		fxt.Init_page("{{test| a }}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_getExpandedArgument, Object_.Ary("current", "1")		, " a ");			// " a " -> " a "
	}
	@Test  public void Preprocess() {
		this.Init_preprocess();
		this.Exec_preprocess(Scrib_core.Frame_key_module	, "1", "c");
		this.Exec_preprocess(Scrib_core.Frame_key_module	, "2", "d");
		this.Exec_preprocess(Scrib_core.Frame_key_template	, "1", "a");
		this.Exec_preprocess(Scrib_core.Frame_key_template	, "2", "b");
	}
	@Test  public void Preprocess_duplicate_key() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0|key1=a|key2=b|key1=c}}");	// add key1 twice
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_preprocess, Object_.Ary("current", "{{#ifeq:1|1|{{{key1}}}|{{{key2}}}}}"), "c");
	}
	@Test  public void CallParserFunction() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Test_lib_proc_kv(lib, Scrib_lib_mw.Invk_callParserFunction, Scrib_kv_utl_.base1_many_ary_("current", "#expr", "1")												, "1");		// named: args is scalar
		fxt.Test_lib_proc_kv(lib, Scrib_lib_mw.Invk_callParserFunction, Scrib_kv_utl_.base1_many_ary_("current", "#if", Scrib_kv_utl_.base1_many_("", "y", "n"))			, "n");		// named: args is table
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_callParserFunction, Object_.Ary("current", "#if", "", "y", "n")							, "n");	// list: args is ary
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_callParserFunction, Object_.Ary("current", "#if", Scrib_kv_utl_.base1_many_("", "y", "n"))	, "n");	// list: args is table
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_callParserFunction, Object_.Ary("current", "#if:", "y", "n")								, "n");	// colon_in_name
	}
	@Test  public void CallParserFunction_tag() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Test_lib_proc_kv(lib, Scrib_lib_mw.Invk_callParserFunction, Scrib_kv_utl_.flat_many_(1, "current", 2, "#tag", 3, Scrib_kv_utl_.flat_many_("3", "id=1", "2", "text", "1", "pre")), "<pre 3=\"id=1\">2=text</pre>");// named: sort args; NOTE: keys should probably be stripped
	}
	@Test  public void CallParserFunction_displayTitle() {	// PURPOSE: DISPLAYTITLE not being set when called through CallParserFunction; DATE:2013-08-05
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Test_lib_proc_kv(lib, Scrib_lib_mw.Invk_callParserFunction, Scrib_kv_utl_.base1_many_ary_("current", "DISPLAYTITLE", "''a''"), "");
		Tfds.Eq("<i>a</i>", String_.new_ascii_(fxt.Parser_fxt().Ctx().Cur_page().Html_data().Display_ttl()));
	}
	@Test  public void ExpandTemplate_tmpl() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Parser_fxt().Data_create("Template:A", "b{{{key1}}}c");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_expandTemplate, Object_.Ary("current", "A", Scrib_kv_utl_.flat_many_("key1", "val1"))				, "bval1c");	// list: args is ary
	}
	@Test  public void ExpandTemplate() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Parser_fxt().Init_page_create("Template:Format", "{{{1}}}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_expandTemplate, Object_.Ary("current", "Format", Scrib_kv_utl_.base1_many_("a")), "a");
	}
	@Test  public void ExpandTemplate_ns_specified() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Parser_fxt().Init_page_create("Template:Format", "{{{1}}}");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_expandTemplate, Object_.Ary("current", "Template:Format", Scrib_kv_utl_.base1_many_("a")), "a");	// NOTE: "Template:" specified
	}
	@Test  public void ExpandTemplate_tmpl_bool() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Parser_fxt().Data_create("Template:Scribunto_bool", "bool_true={{{bool_true}}};bool_false={{{bool_false}}};");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_expandTemplate, Object_.Ary("current", "Scribunto_bool", Scrib_kv_utl_.flat_many_("bool_true", true, "bool_false", false)), "bool_true=1;bool_false={{{bool_false}}};");
	}
	@Test  public void ExpandTemplate_page() {
		fxt.Init_page("{{#invoke:Mod_0|Prc_0}}");
		fxt.Parser_fxt().Data_create("A", "b{{{key1}}}c");
		fxt.Test_lib_proc(lib, Scrib_lib_mw.Invk_expandTemplate, Object_.Ary("current", ":A", Scrib_kv_utl_.flat_many_("key1", "val1"))				, "bval1c");	// list: args is ary
	}
	@Test  public void Err_mod_blank()				{fxt.Test_parse_err("{{#invoke:}}"			, Scrib_invoke_func.Err_mod_missing);}
	@Test  public void Err_mod_missing()			{fxt.Test_parse_err("{{#invoke:Missing}}"	, Scrib_invoke_func.Err_mod_missing);}
	private void Init_preprocess() {
		fxt.Init_tmpl("{{#invoke:Mod_0|Func_0|1|c|d}}");	// current
		fxt.Init_page("{{test|1|a|b|c}}");					// parent
		fxt.Init_cbk(Scrib_core.Key_mw_interface, fxt.Core().Lib_mw(), Scrib_lib_mw.Invk_preprocess);
	}
	private void Exec_preprocess(String frame, String arg_idx, String expd) {
		fxt.Parser_fxt().Wiki().Cache_mgr().Tmpl_result_cache().Clear();
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd_preprocess(frame, "{{#ifeq:" + arg_idx + "|{{{1}}}|{{{2}}}|{{{3}}}}}");
		fxt.Test_invoke(expd);
	}
}

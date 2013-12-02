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
public class Scrib_pf_invoke_tst {
	@Before public void init() {fxt.Clear();} Scrib_pf_invoke_fxt fxt = new Scrib_pf_invoke_fxt();
	@Test  public void Err_mod_blank()				{fxt.Test_parse_err("{{#invoke:}}", Scrib_pf_invoke.Err_mod_missing);}
	@Test  public void Err_mod_missing()			{fxt.Test_parse_err("{{#invoke:Missing}}", Scrib_pf_invoke.Err_mod_missing);}
	@Test  public void Err_fnc_blank()				{fxt.Test_parse_err("{{#invoke:Mod_0|}}", Scrib_pf_invoke.Err_fnc_missing);}
	@Test  public void Preprocess() {
		this.Init_preprocess();
		this.Exec_preprocess(Scrib_engine.Frame_key_current	, "1", "c");
		this.Exec_preprocess(Scrib_engine.Frame_key_current	, "2", "d");
		this.Exec_preprocess(Scrib_engine.Frame_key_parent	, "1", "a");
		this.Exec_preprocess(Scrib_engine.Frame_key_parent	, "2", "b");
	}
	@Test  public void ExpandTemplate() {
		this.Init_expandTemplate();
		fxt.Parser_fxt().ini_page_create("Template:Format", "{{{1}}}");
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd_expandTemplate(Scrib_engine.Frame_key_current	, "Format", KeyVal_.int_(1, "a"));
		fxt.Test_invoke("a");
	}
	@Test  public void ExpandTemplate_ns_name() {
		this.Init_expandTemplate();
		fxt.Parser_fxt().ini_page_create("Template:Format", "{{{1}}}");
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd_expandTemplate(Scrib_engine.Frame_key_current	, "Template:Format", KeyVal_.int_(1, "a"));
		fxt.Test_invoke("a");
	}
	private void Init_preprocess() {
		fxt.Init_tmpl("{{#invoke:Mod_0|Func_0|1|c|d}}");	// current
		fxt.Init_page("{{test|null|1|a|b}}");				// parent
		fxt.Init_cbk(Scrib_engine.Key_mw_interface, fxt.Engine().Lib_mw(), Scrib_lib_mw.Invk_preprocess);
	}
	private void Exec_preprocess(String frame, String arg_idx, String expd) {
		fxt.Parser_fxt().App().Tmpl_result_cache().Clear();
		fxt.Init_lua_module();
		fxt.Init_lua_rcvd_preprocess(frame, "{{#ifeq:" + arg_idx + "|{{{1}}}|{{{2}}}|{{{3}}}}}");
		fxt.Test_invoke(expd);
	}
	private void Init_expandTemplate() {
		fxt.Init_tmpl("{{#invoke:Mod_0|Func_0|1|c|d}}");	// current
		fxt.Init_page("{{test|null|1|a|b}}");				// parent
		fxt.Init_cbk(Scrib_engine.Key_mw_interface, fxt.Engine().Lib_mw(), Scrib_lib_mw.Invk_expandTemplate);
	}
}	
class Scrib_pf_invoke_fxt {
	public void Clear() {
		if (fxt == null)  {
			fxt = new Xop_fxt();
			engine_fxt = new Scrib_engine_fxt();
			engine_fxt.Clear();
			engine_fxt.Init_lib_mw();
			engine = engine_fxt.Engine();
			server = engine_fxt.Server();
			engine.Interpreter().Server_(server);
		}
		Io_mgr._.InitEngine_mem();
		fxt.Reset();
		engine_fxt.Clear();
		engine.When_page_changed(fxt.Wiki().Ctx().Page());
		init_tmpl = init_page = null;
		fxt.ini_page_create("Module:" + "Mod_0", "");
		this.Init_lua_rcvd_loadModule(); 
	}	private Xop_fxt fxt; ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255); Scrib_engine engine; Process_server_mock server; Scrib_lua_rsp_bldr rsp_bldr = new Scrib_lua_rsp_bldr();
	public Xop_fxt Parser_fxt() {return fxt;}
	public Scrib_engine_fxt Engine_fxt() {return engine_fxt;} Scrib_engine_fxt engine_fxt;
	public Scrib_engine Engine() {return engine;}
	public Scrib_pf_invoke_fxt Init_module(String ttl, String text) {
		fxt.ini_page_create("Module:" + ttl, text);
		return this;
	}
	public Scrib_pf_invoke_fxt Init_cbk(String lib_name, GfoInvkAble invk, String... func_names) {
		int len = func_names.length;
		for (int i = 0; i < len; i++) {
			String func_name = func_names[i];
			if (engine.Interpreter().Cbks_get_by_key(lib_name + "-" + func_name) == null)	// allow tests to call Init_cbk multiple times
				engine.Interpreter().Cbks_add(invk, lib_name, func_name);
		}
		return this;
	}
	public Scrib_pf_invoke_fxt Init_lua_module() {this.Init_lua_rcvd_executeModule(); return this;}
	public Scrib_pf_invoke_fxt Init_tmpl(String v) {init_tmpl = v; return this;} private String init_tmpl;
	public Scrib_pf_invoke_fxt Init_page(String v) {init_page = v; return this;} private String init_page;
	public Scrib_pf_invoke_fxt Init_server_print_key_y_() {server.Print_key_(true); return this;}
	public Scrib_pf_invoke_fxt Init_server_print_key_n_() {server.Print_key_(false); return this;}
	public Scrib_pf_invoke_fxt Init_lua_rcvd_raw(String raw) {server.Prep_add(raw); return this;}
	public Scrib_pf_invoke_fxt Init_lua_rcvd(String cbk_name, KeyVal... ary) {
		server.Prep_add(rsp_bldr.Bld_mw_cbk(cbk_name, ary));
		return this;
	}
	public Scrib_pf_invoke_fxt Init_lua_rcvd_loadModule() {
		server.Prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;i:1;}}");
		return this;
	}
	public Scrib_pf_invoke_fxt Init_lua_rcvd_executeModule() {
		server.Prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;a:1:{s:5:\"Prc_0\";O:42:\"Scribunto_LuaStandaloneInterpreterFunction\":1:{s:2:\"id\";i:14;}}}}");
		return this;
	}
	public Scrib_pf_invoke_fxt Init_lua_rcvd_executeFunctionChunk() {
		server.Prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:11:\"arg_0,arg_1\";}}");
		return this;
	}
	public Scrib_pf_invoke_fxt Init_lua_rcvd_preprocess(String frame, String cmd) {
		server.Prep_add(String_.Concat_any("a:4:{s:2:\"id\";s:23:\"mw_interface-preprocess\";s:2:\"op\";s:4:\"call\";s:5:\"nargs\";i:2;s:4:\"args\";a:2:{i:1;s:", String_.Len(frame), ":\"", frame, "\";i:2;s:", String_.Len(cmd), ":\"", cmd, "\";}}"));
		this.Init_lua_rcvd_rv();
		return this;
	}
	public Scrib_pf_invoke_fxt Init_lua_rcvd_expandTemplate(String frame, String tmpl_ttl, KeyVal... ary) {
		ary = new KeyVal[] {KeyVal_.int_(1, "current"), KeyVal_.int_(2, tmpl_ttl), KeyVal_.int_(3, ary)};
		server.Prep_add(rsp_bldr.Bld_mw_cbk("expandTemplate", ary));
		this.Init_lua_rcvd_rv();
		return this;
	}
	public Scrib_pf_invoke_fxt Init_lua_rcvd_rv() {
		server.Prep_add_dynamic_val();
		return this;
	}
	public Scrib_pf_invoke_fxt Expd_lua_send(String v) {
		expd_lua_send = v;
		return this;
	}	String expd_lua_send;
	public void Test_invoke(String expd) {
		if (init_tmpl != null) fxt.ini_defn_add("test", init_tmpl);
		fxt.tst_Parse_tmpl_str(init_page, expd);
	}
	public void Test_parse_err(String raw, String expd_err_type) {
		Scrib_pf_invoke.Error(tmp_bfr, fxt.Wiki().Msg_mgr(), expd_err_type);
		byte[] expd_err = tmp_bfr.XtoAryAndClear();
		fxt.tst_Parse_page_tmpl_str(raw, String_.new_utf8_(expd_err));
	}
	public void Test_lib_proc(Scrib_lib lib, String func_name, Object[] args, String expd) {Test_lib_proc_kv(lib, func_name, Scrib_kv_utl.base1_many_(args), expd);}
	public void Test_lib_proc_kv(Scrib_lib lib, String func_name, KeyVal[] args, String expd) {
		Test_lib_proc_internal(lib, func_name, args);
		this.Test_invoke(expd);
	}
	public byte[] Test_lib_proc_rv(Scrib_lib lib, String func_name, Object[] args) {
		Test_lib_proc_internal(lib, func_name, Scrib_kv_utl.base1_many_(args));
		if (init_tmpl != null) fxt.ini_defn_add("test", init_tmpl);
		return fxt.tst_Parse_tmpl_str_rv(init_page);
	}
	private void Test_lib_proc_internal(Scrib_lib lib, String func_name, KeyVal[] args) {
		Init_lua_module();
		this.Init_cbk(Scrib_engine.Key_mw_interface, lib, func_name);
		this.Init_lua_rcvd(func_name, args);
		this.Init_lua_rcvd_rv();
	}
	public void Test_log_rcvd(int i, String expd) {
		Tfds.Eq(expd, (String)server.Log_rcvd().FetchAt(i));
	}
}
class Scrib_lua_rsp_bldr {
	ByteAryBfr bfr = ByteAryBfr.reset_(255);
	public String Bld_mw_cbk(String cbk_name, KeyVal... ary) {
		cbk_name = "mw_interface-" + cbk_name;
		bfr.Add_str("a:4:{s:2:\"id\";");
		Bld_str(bfr, cbk_name);
		bfr.Add_str("s:2:\"op\";s:4:\"call\";s:5:\"nargs\";i:3;s:4:\"args\";");
		Bld_kv_ary(bfr, ary);
		bfr.Add_str("}");
		return bfr.XtoStrAndClear();
	}
	private void Bld_obj(ByteAryBfr bfr, Object v) {
		Class<?> v_type = v.getClass();
		if		(Object_.Eq(v_type, Int_.ClassOf))				Bld_int(bfr, Int_.cast_(v));
		else if	(Object_.Eq(v_type, String_.ClassOf))			Bld_str(bfr, String_.cast_(v));
		else if	(Object_.Eq(v_type, Bool_.ClassOf))				Bld_bool(bfr, Bool_.cast_(v));
		else if	(Object_.Eq(v_type, KeyVal[].class))			Bld_kv_ary(bfr, (KeyVal[])v);
		else if	(Object_.Eq(v_type, Scrib_fnc.class))			Bld_fnc(bfr, (Scrib_fnc)v);
		else													throw Err_.unhandled(ClassAdp_.NameOf_obj(v));
	}
	private void Bld_bool(ByteAryBfr bfr, boolean v)		{bfr.Add_str("b:").Add_int_fixed(v ? 1 : 0, 1).Add_byte(Byte_ascii.Semic);}
	private void Bld_int(ByteAryBfr bfr, int v)			{bfr.Add_str("i:").Add_int_variable(v).Add_byte(Byte_ascii.Semic);}
	private void Bld_str(ByteAryBfr bfr, String v)		{bfr.Add_str("s:").Add_int_variable(ByteAry_.new_utf8_(v).length).Add_str(":\"").Add_str(v).Add_str("\";");}	// NOTE: must use ByteAry_.new_utf8_(v).length to calculate full bry len
	private void Bld_fnc(ByteAryBfr bfr, Scrib_fnc v)	{bfr.Add_str("O:42:\"Scribunto_LuaStandaloneInterpreterFunction\":1:{s:2:\"id\";i:").Add_int_variable(v.Id()).Add_byte(Byte_ascii.Semic).Add_byte(Byte_ascii.Curly_end);}
	private void Bld_kv_ary(ByteAryBfr bfr, KeyVal[] ary) {
		int len = ary.length;
		bfr.Add_str("a:").Add_int_variable(len).Add_str(":{");
		for (int i = 0; i < len; i++) {
			KeyVal kv = ary[i];
			Bld_obj(bfr, kv.Key_as_obj());
			Bld_obj(bfr, kv.Val());
		}
		bfr.Add_byte(Byte_ascii.Curly_end);
	}
}

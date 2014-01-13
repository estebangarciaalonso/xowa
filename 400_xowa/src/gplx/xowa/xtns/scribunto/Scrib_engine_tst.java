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
public class Scrib_engine_tst {
	@Before public void init() {fxt.Clear();} Scrib_engine_fxt fxt = new Scrib_engine_fxt();
	@Test  public void GetStatus() {
		fxt	.Expd_server_rcvd_add("0000001400000027{[\"op\"]=\"getStatus\"}")
			.Init_server_prep_add("a:2:{s:6:\"values\";a:1:{i:1;a:3:{s:4:\"time\";i:1;s:3:\"pid\";i:6201;s:5:\"vsize\";i:2523136;}}s:2:\"op\";s:6:\"return\";}")
			.Test_GetStatus()
			;
	}
	@Test  public void LoadString() {
		String mod_text = Mod_basic();
		fxt	.Expd_server_rcvd_add("00000067000000CD{[\"op\"]=\"loadString\",[\"text\"]=\"" + fxt.Encode(mod_text) + "\",[\"chunkName\"]=\"lib_name\"}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;i:1;}}")
			.Test_LoadString("lib_name", mod_text, 1)
			;
	}
	@Test  public void CallFunction() {
		fxt	.Expd_server_rcvd_add("000000300000005F{[\"op\"]=\"call\",[\"id\"]=1,[\"nargs\"]=0,[\"args\"]={}}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;a:1:{s:4:\"noop\";O:42:\"Scribunto_LuaStandaloneInterpreterFunction\":1:{s:2:\"id\";i:2;}}}}")
			.Test_CallFunction(1, Object_.Ary_empty, fxt.kv_ary_(fxt.kv_(1, fxt.kv_func_("noop", 2))))
			;
	}
	@Test  public void CallFunction_args() {
		fxt	.Expd_server_rcvd_add("0000004100000081{[\"op\"]=\"call\",[\"id\"]=1,[\"nargs\"]=1,[\"args\"]={[1]={[\"key\"]=123}}}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;a:1:{s:4:\"noop\";O:42:\"Scribunto_LuaStandaloneInterpreterFunction\":1:{s:2:\"id\";i:2;}}}}")
			.Test_CallFunction(1, Object_.Ary(fxt.kv_("key", 123)), fxt.kv_ary_(fxt.kv_(1, fxt.kv_func_("noop", 2))))
			;
	}
	@Test  public void RegisterLibrary() {
		fxt	.Expd_server_rcvd_add("0000006B000000D5{[\"op\"]=\"registerLibrary\",[\"name\"]=\"lib_0\",[\"functions\"]={[\"prc_0\"]=\"lib_0-prc_0\",[\"prc_1\"]=\"lib_0-prc_1\"}}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:0;s:6:\"values\";a:0:{}}")
			.Test_RegisterLibrary("lib_0", String_.Ary("prc_0", "prc_1"), String_.Ary("lib_0-prc_0", "lib_0-prc_1"))
			;
	}
	@Test  public void LoadLibraryFromFile() {
		fxt	.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;i:15;}}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;a:2:{s:5:\"prc_2\";O:42:\"Scribunto_LuaStandaloneInterpreterFunction\":1:{s:2:\"id\";i:2;}s:5:\"prc_3\";O:42:\"Scribunto_LuaStandaloneInterpreterFunction\":1:{s:2:\"id\";i:3;}}}}")
			.Test_LoadLibraryFromFile("lib_name", "doesn't matter", fxt.kv_("prc_2", 2), fxt.kv_("prc_3", 3));
			;
	}
	@Test  public void LoadLibraryFromFile__rv_has_no_values() {	// PURPOSE: "package.lua" does not return any prc_ids
		fxt	.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;i:15;}}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:0;s:6:\"values\";a:0:{}}")
			.Test_LoadLibraryFromFile("lib_name", "doesn't matter");
			;
	}
	@Test  public void CallFunction_cbk() {	// PURPOSE: 'simulates interpreter.CallFunction(mw_lib.Fncs_get_by_key("setup").Id(), "allowEnvFuncs", allow_env_funcs);'
		fxt	.Expd_server_rcvd_add("0000008C00000117{[\"op\"]=\"registerLibrary\",[\"name\"]=\"mw_interface\",[\"functions\"]={[\"loadPackage\"]=\"mw_interface-loadPackage\",[\"prc_1\"]=\"mw_interface-prc_1\"}}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:0;s:6:\"values\";a:0:{}}")
			.Test_RegisterLibrary(Scrib_engine.Key_mw_interface, String_.Ary("loadPackage", "prc_1"), String_.Ary("mw_interface-loadPackage", "mw_interface-prc_1"))
			;		
		fxt	.Init_lib_fil("package.lua", "package_text")
			.Expd_server_rcvd_add("000000470000008D{[\"op\"]=\"call\",[\"id\"]=2,[\"nargs\"]=1,[\"args\"]={[1]={[\"key_0\"]=\"val_0\"}}}")
			.Init_server_prep_add("a:4:{s:2:\"id\";s:24:\"mw_interface-loadPackage\";s:2:\"op\";s:4:\"call\";s:5:\"nargs\";i:1;s:4:\"args\";a:1:{i:1;s:7:\"package\";}}")
			.Expd_server_rcvd_add("0000004500000089{[\"op\"]=\"loadString\",[\"text\"]=\"package_text\",[\"chunkName\"]=\"package\"}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;i:14;}}")
			.Expd_server_rcvd_add("0000003B00000075{[\"op\"]=\"return\",[\"nvalues\"]=1,[\"values\"]={[1]=chunks[14]}}")				                       
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:0;s:6:\"values\";a:0:{}}")
			.Test_CallFunction(2, Object_.Ary(fxt.kv_("key_0", "val_0")));
	}
	@Test  public void Module_GetInitChunk() {	// PURPOSE: similar to LoadString except name is prepended with "="
		String mod_text = Mod_basic();
		fxt	.Expd_server_rcvd_add("0000006F000000DD{[\"op\"]=\"loadString\",[\"text\"]=\"" + fxt.Encode(mod_text) + "\",[\"chunkName\"]=\"=Module:Mod_name\"}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;i:1;}}")
			.Test_Module_GetInitChunk("Module:Mod_name", mod_text, 1)
			;
	}
	@Test  public void Module_GetInitChunk_tabs() {	// PURPOSE: swap out xowa's &#09; with literal tabs, else will break lua
		fxt	.Expd_server_rcvd_add("0000004500000089{[\"op\"]=\"loadString\",[\"text\"]=\"" + "a\tb" + "\",[\"chunkName\"]=\"=Module:Mod_name\"}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;i:1;}}")
			.Test_Module_GetInitChunk("Module:Mod_name", "a&#09;b", 1)
			;
	}
	@Test  public void ExecuteModule() {
		fxt	.Init_lib_mw();
		fxt	.Expd_server_rcvd_add("0000003E0000007B{[\"op\"]=\"call\",[\"id\"]=8,[\"nargs\"]=1,[\"args\"]={[1]=chunks[14]}}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;a:2:{s:5:\"prc_0\";O:42:\"Scribunto_LuaStandaloneInterpreterFunction\":1:{s:2:\"id\";i:15;}s:5:\"prc_1\";O:42:\"Scribunto_LuaStandaloneInterpreterFunction\":1:{s:2:\"id\";i:16;}}}}")
			.Test_ExecuteModule(14, fxt.kv_func_("prc_0", 15), fxt.kv_func_("prc_1", 16))
			;
	}
	@Test  public void Invoke() {
		fxt	.Init_lib_mw();
		fxt	.Init_Cbks_add(Scrib_engine.Key_mw_interface, "getExpandedArgument");
		fxt	.Expd_server_rcvd_add("0000004900000091{[\"op\"]=\"loadString\",[\"text\"]=\"Mod_0_code\",[\"chunkName\"]=\"=Module:Mod_0\"}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;i:13;}}")			// NOTE: 13=id of "Module:Mod_0"
			.Expd_server_rcvd_add("0000003E0000007B{[\"op\"]=\"call\",[\"id\"]=8,[\"nargs\"]=1,[\"args\"]={[1]=chunks[13]}}")	// NOTE: 8=executeModule; 13=id of "Module:Mod_0"
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;a:1:{s:5:\"Prc_0\";O:42:\"Scribunto_LuaStandaloneInterpreterFunction\":1:{s:2:\"id\";i:14;}}}}")	// NOTE: 14=id of "Prc_0"
			.Expd_server_rcvd_add("0000003E0000007B{[\"op\"]=\"call\",[\"id\"]=9,[\"nargs\"]=1,[\"args\"]={[1]=chunks[14]}}")	// NOTE: 9=executeFunction; 14=id of "Prc_0"
			.Init_server_prep_add("a:4:{s:2:\"id\";s:32:\"mw_interface-getExpandedArgument\";s:2:\"op\";s:4:\"call\";s:5:\"nargs\";i:2;s:4:\"args\";a:2:{i:1;s:7:\"current\";i:2;s:1:\"1\";}}")
			.Expd_server_rcvd_add("000000380000006F{[\"op\"]=\"return\",[\"nvalues\"]=1,[\"values\"]={[1]=\"arg_0\"}}")
			.Init_server_prep_add("a:4:{s:2:\"id\";s:32:\"mw_interface-getExpandedArgument\";s:2:\"op\";s:4:\"call\";s:5:\"nargs\";i:2;s:4:\"args\";a:2:{i:1;s:7:\"current\";i:2;s:1:\"2\";}}")
			.Expd_server_rcvd_add("000000380000006F{[\"op\"]=\"return\",[\"nvalues\"]=1,[\"values\"]={[1]=\"arg_1\"}}")
			.Init_server_prep_add("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:11:\"arg_0,arg_1\";}}")
			.Test_Invoke("Module:Mod_0", "Mod_0_code", "Prc_0", Scrib_kv_utl.base1_many_("arg_0", "arg_1"))
			;
	}
	String Mod_basic() {
		return String_.Concat
		(	"local p = {}"
		,	"function p.noop(frame)"
		,	"end"
		,	"return p"
		);
	}
}
class Scrib_engine_fxt {
	public Scrib_engine_fxt Clear() {
		if (engine == null) {
			app = Xoa_app_fxt.app_();
			wiki = Xoa_app_fxt.wiki_tst_(app);
			engine = Scrib_engine.Engine_new_(app, wiki.Ctx());
			server = new Process_server_mock();
			engine.Interpreter().Server_(server);
		}
		server.Clear();
		engine.Interpreter().Cbks_clear();
		engine.Cur_frame_owner_(null);
		engine.When_page_changed(wiki.Ctx().Page());
		expd_server_rcvd_list.Clear();
		return this;
	}	private Xoa_app app; Xow_wiki wiki; ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255);
	public Scrib_engine Engine() {return engine;} Scrib_engine engine;
	public Process_server_mock Server() {return server;} Process_server_mock server;
	public KeyVal kv_(Object key, Object val) {return KeyVal_.obj_(key, val);}
	public KeyVal[] kv_ary_(KeyVal... v) {return v;}
	public KeyVal kv_func_(String key, int id) {return KeyVal_.new_(key, new Scrib_fnc(key, id));}
	public Scrib_engine_fxt Init_lib_fil(String name, String text) {
		Io_url url = engine.Fsys_mgr().Script_dir().GenSubFil(name);
		Io_mgr._.SaveFilStr(url, text);
		return this;
	}
	public Scrib_engine_fxt Init_server_prep_add(String v) {server.Prep_add(v); return this;}
	public Scrib_engine_fxt Init_lib_mw() {
		if (engine.Lib_mw().Mod() == null) {
			Scrib_mod mod = new Scrib_mod(engine, "mw.lua");
			mod.Fncs_add(new Scrib_fnc("executeModule", 8));
			mod.Fncs_add(new Scrib_fnc("executeFunction", 9));
			engine.Lib_mw().Mod_(mod);
		}
		return this;
	}
	public Scrib_engine_fxt Init_Cbks_add(String name, String func) {
		engine.Interpreter().Cbks_add(engine.Lib_mw(), name, func);
		return this;
	}
	public Scrib_engine_fxt Expd_server_rcvd_add(String v) {expd_server_rcvd_list.Add(v); return this;} ListAdp expd_server_rcvd_list = ListAdp_.new_();
	public Scrib_engine_fxt Test_GetStatus() {
		engine.Interpreter().GetStatus();
		Test_server_logs();
		return this;
	}
	public Scrib_engine_fxt Test_LoadString(String name, String text, int expd_id) {
		int actl_id = engine.Interpreter().LoadString(name, text).Id();
		Test_server_logs();
		Tfds.Eq(expd_id, actl_id);
		return this;
	}
	public Scrib_engine_fxt Test_CallFunction(int prc_id, Object[] args, KeyVal... expd) {
		KeyVal[] actl = engine.Interpreter().CallFunction(prc_id, Scrib_kv_utl.base1_many_(args));
		Test_server_logs();
		Tfds.Eq_str_lines(KeyVal_.Xto_str(expd), KeyVal_.Xto_str(actl));
		return this;
	}
	public Scrib_engine_fxt Test_RegisterLibrary(String name, String[] funcs, String[] expd_keys) {
		engine.Interpreter().RegisterLibrary(name, engine.Lib_mw(), funcs);
		Test_server_logs();
		int len = engine.Interpreter().Cbks_len();
		String[] actl_keys = new String[len];
		for (int i = 0; i < len; i++)
			actl_keys[i] = engine.Interpreter().Cbks_get_at(i).Key();
		Tfds.Eq_ary_str(expd_keys, actl_keys);
		return this;
	}
	public Scrib_engine_fxt Test_LoadLibraryFromFile(String name, String text, KeyVal... expd) {
		Scrib_mod actl_lib = engine.LoadLibraryFromFile(name, text);
		int actl_len = actl_lib.Fncs_len();
		KeyVal[] actl = new KeyVal[actl_len];
		for (int i = 0; i < actl_len; i++) {
			Scrib_fnc itm = actl_lib.Fncs_get_at(i);
			actl[i] = KeyVal_.new_(itm.Key(), itm.Id());
		}
		Tfds.Eq_str_lines(KeyVal_.Xto_str(expd), KeyVal_.Xto_str(actl));
		return this;
	}
	public Scrib_engine_fxt Test_Module_GetInitChunk(String name, String text, int expd_id) {
		Scrib_mod mod = new Scrib_mod(engine, name);
		int actl_id = mod.LoadString(text).Id();
		Test_server_logs();
		Tfds.Eq(expd_id, actl_id);
		return this;
	}
	public Scrib_engine_fxt Test_ExecuteModule(int mod_id, KeyVal... expd) {
		KeyVal[] values = engine.Interpreter().ExecuteModule(mod_id);
		KeyVal[] actl = (KeyVal[])values[0].Val();
		Test_server_logs();
		Tfds.Eq_str_lines(KeyVal_.Xto_str(expd), KeyVal_.Xto_str(actl));
		return this;
	}
	public Scrib_engine_fxt Test_GetExpandedArgument(KeyVal[] args, String arg, String expd) {// NOTE: test is rigidly defined; (a) always same 3 arguments in frame; (b) expd={"val_1", "val_2", "val_3", ""}
		this.Expd_server_rcvd_add("0000003D00000079{[\"op\"]=\"call\",[\"id\"]=8,[\"nargs\"]=1,[\"args\"]={[1]=chunks[9]}}")
			.Init_server_prep_add("a:4:{s:2:\"id\";s:32:\"mw_interface-getExpandedArgument\";s:2:\"op\";s:4:\"call\";s:5:\"nargs\";i:2;s:4:\"args\";a:2:{i:1;s:7:\"current\";i:2;s:" + String_.Len(arg) + ":\"" + arg + "\";}}");
		if (String_.Eq(expd, ""))
			this.Expd_server_rcvd_add("0000002D00000059{[\"op\"]=\"return\",[\"nvalues\"]=0,[\"values\"]={}}");
		else
			this.Expd_server_rcvd_add("000000380000006F{[\"op\"]=\"return\",[\"nvalues\"]=1,[\"values\"]={[1]=\"" + expd + "\"}}");
		this.Init_server_prep_add("a:2:{s:6:\"values\";a:1:{i:1;s:6:\"ignore\";}s:2:\"op\";s:6:\"return\";}");
		engine.Cur_frame_invoke_(Xot_invk_mock.new_(args));
		engine.Interpreter().ExecuteModule(9);
		Test_server_logs();
		return this;
	}
	public Scrib_engine_fxt Test_GetAllExpandedArguments(KeyVal... args) {
		engine.Cur_frame_invoke_(Xot_invk_mock.new_(args));
		engine.Interpreter().ExecuteModule(9);
		Test_server_logs();
		return this;
	}
	public Scrib_engine_fxt Test_ParentFrameExists(boolean init_parent_frame) {
		if (init_parent_frame)
			engine.Cur_frame_owner_(Xot_invk_mock.new_());
		engine.Interpreter().ExecuteModule(9);
		Test_server_logs();
		return this;
	}
	public Scrib_engine_fxt Test_Invoke(String mod_name, String mod_code, String prc_name, KeyVal... args) {
		engine.Invoke(wiki, wiki.Ctx(), ByteAry_.Empty, Xot_invk_mock.Null, Xot_invk_mock.new_(args), tmp_bfr, ByteAry_.new_utf8_(mod_name), ByteAry_.new_utf8_(mod_code), ByteAry_.new_utf8_(prc_name));
		Test_server_logs();
		return this;
	}
	private void Test_server_logs() {
		if (expd_server_rcvd_list.Count() > 0) {
			Tfds.Eq_ary_str(expd_server_rcvd_list.XtoStrAry(), server.Log_rcvd().XtoStrAry());
			expd_server_rcvd_list.Clear();
			server.Log_rcvd().Clear();
		}
	}
	public String Encode(String v) {return String_.Replace(v, "\n", "\\n");}
}

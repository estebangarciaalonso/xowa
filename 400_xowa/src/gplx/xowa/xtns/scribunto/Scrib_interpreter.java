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
import gplx.texts.*;
class Scrib_interpreter {
	public Scrib_interpreter(Xoa_app app, Scrib_engine engine) {
		this.app = app;
		this.engine = engine;
		args_srl = new Scrib_lua_encoder(app.Usr_dlg()); 
		server = new Process_server_base();
		scrib_opts = (Xow_xtn_scribunto)app.Xtn_mgr().Get_or_fail(Xow_xtn_scribunto.XTN_KEY);
	}	Scrib_engine engine; Xoa_app app; Xow_xtn_scribunto scrib_opts;
	Scrib_lua_server_rsp rsp = new Scrib_lua_server_rsp(); Scrib_lua_encoder args_srl;
	public Scrib_fnc LoadString(String name, String text) {
		KeyVal[] rslt = this.Dispatch("op", "loadString", "text", text, "chunkName", name);
		return new Scrib_fnc(name, Int_.cast_(rslt[0].Val()));
	}
	public KeyVal[] GetStatus() {return this.Dispatch("op", "getStatus");}
	public KeyVal[] CallFunction(int id, KeyVal[] args) {return this.Dispatch("op", "call", "id", id, "nargs", args.length, "args", args);}
	public void RegisterLibrary(String name, GfoInvkAble invk, String... funcs) {
		int len = funcs.length;
		KeyVal[] functions_ary = new KeyVal[len];
		for (int i = 0; i < len; i++) {
			String func = funcs[i];
			Scrib_cbk cbk = Cbks_add(invk, name, func);
			functions_ary[i] = KeyVal_.new_(func, cbk.Key());
		}
		this.Dispatch("op", "registerLibrary", "name", name, "functions", functions_ary);
	}
	public KeyVal[] ExecuteModule(int mod_id) {
		return this.CallFunction(engine.Lib_mw().Mod().Fncs_get_id("executeModule"), Scrib_kv_utl.base1_obj_(new Scrib_fnc("", mod_id)));
	}
	public void Cbks_clear() {cbks.Clear();}
	public int Cbks_len() {return cbks.Count();} private OrderedHash cbks = OrderedHash_.new_();
	public Scrib_cbk Cbks_add(GfoInvkAble invk, String name, String func) {
		Scrib_cbk cbk = new Scrib_cbk(invk, name, func);
		String key = cbk.Key();
		cbks.Add(key, cbk);
		return cbk;
	}
	public Scrib_cbk Cbks_get_at(int i) {return (Scrib_cbk)cbks.FetchAt(i);}
	public Scrib_cbk Cbks_get_by_key(String s) {return (Scrib_cbk)cbks.Fetch(s);}
	public Process_server Server() {return server;} public void Server_(Process_server v) {server = v;} Process_server server;
	public boolean Dbg_print() {return dbg_print;} public Scrib_interpreter Dbg_print_(boolean v) {dbg_print = v; return this;} private boolean dbg_print;
	public KeyVal[] Dispatch(Object... ary) {
		ByteAryBfr bfr = app.Utl_bry_bfr_mkr().Get_m001();
		Dispatch_bld_send(bfr, ary);
		bfr.Mkr_rls();
		boolean log_enabled = scrib_opts.Lua_log_enabled();
		if (log_enabled) app.Usr_dlg().Log_direct("sent:" + bfr.XtoStr() + "\n");
		byte[] rsp_bry = server.Server_comm(bfr.XtoAryAndClear(), ary);
		if (log_enabled) app.Usr_dlg().Log_direct("rcvd:" + String_.new_utf8_(rsp_bry) + "\n\n");
		String op = rsp.Extract(rsp_bry);
		if		(String_.Eq(op, "return")) 
			return rsp.Values();
		else if (String_.Eq(op, "call")) {
			String id = rsp.Call_id();
			KeyVal[] args = rsp.Call_args();
			Scrib_cbk cbk = this.Cbks_get_by_key(id); if (cbk == null) throw Xow_xtn_scribunto.err_("could not find cbk with id of {0}", id);
			KeyVal[] cbk_rslts = KeyVal_.Ary_cast_(cbk.Invk(args));
			return this.Dispatch("op", "return", "nvalues", cbk_rslts.length, "values", cbk_rslts);
		}
		return KeyVal_.Ary_empty;
	}	static final byte[] Dispatch_hdr = ByteAry_.new_ascii_("0000000000000000");	// itm_len + itm_chk in 8-len HexDec
	private void Dispatch_bld_send(ByteAryBfr bfr, Object[] ary) {
		int len = ary.length; if (len % 2 != 0) throw Err_.new_fmt_("arguments must be factor of 2: {0}", len);
		bfr.Add(Dispatch_hdr);
		bfr.Add_byte(Byte_ascii.Curly_bgn);
		for (int i = 0; i < len; i++) {
			Object itm = ary[i];
			if (i % 2 == 0)	{
				if (i != 0) bfr.Add_byte(Byte_ascii.Comma);
				args_srl.Encode_key(bfr, itm);			
			}
			else
				args_srl.Encode_obj(bfr, itm);
		}
		bfr.Add_byte(Byte_ascii.Curly_end);
		int msg_len = bfr.Bry_len() - 16;	// 16 for Dispatch_hdr_len
		int chk_len = (msg_len * 2) -1;		// defined by Scribunto
		HexDecUtl.Write(bfr.Bry(), 0,  8, msg_len);
		HexDecUtl.Write(bfr.Bry(), 9, 16, chk_len);
	}
	public static final int Base_1 = 1;
}

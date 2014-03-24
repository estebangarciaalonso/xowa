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
package gplx.xowa.xtns.scribunto.engines.luaj; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*; import gplx.xowa.xtns.scribunto.engines.*;
import org.luaj.vm2.*; import org.luaj.vm2.lib.*;
public class Luaj_server_func_error extends TwoArgFunction {
	private Scrib_core core;
	private boolean debug_enabled;
	private LuaClosure attach_trace_func;
	public void Debug_enabled_(boolean v) {this.debug_enabled = v;}
	public void Init(Scrib_core core, LuaTable server) {	// NOTE: can't hook into '(String_.Eq(op, "error"))' b/c luaj always returns "error in error handling"; instead hook into attachTrace
		this.core = core;
		LuaValue mt = server.getmetatable();
		attach_trace_func = (LuaClosure)server.get("attachTrace");
		mt.set("attachTrace", this);
	}
	public LuaValue call(LuaValue server, LuaValue error) {
		String traceback = debug_enabled ? DebugLib.traceback(0) : "<luaj_debug_not_enabled>";
		core.Handle_error(error.tojstring(), traceback);
		return attach_trace_func.call(server, error);
	}
}

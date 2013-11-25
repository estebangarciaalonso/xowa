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
import gplx.xowa.parsers.logs.*;
public class Xow_xtn_scribunto implements Xow_xtn_itm {
	public byte[] Xtn_key() {return XTN_KEY;} public static final byte[] XTN_KEY = ByteAry_.new_ascii_("scribunto");
	public void Xtn_init_by_app(Xoa_app app) {this.app = app;} private Xoa_app app;
	public void Xtn_init_by_wiki(Xow_wiki wiki) {}
	public boolean Enabled() {return enabled;} private boolean enabled = true;
	public int Lua_timeout() {return lua_timeout;} private int lua_timeout = 4000;
	public int Lua_timeout_polling() {return lua_timeout_polling;} private int lua_timeout_polling = 1;
	public int Lua_timeout_busy_wait() {return lua_timeout_busy_wait;} private int lua_timeout_busy_wait = 250;
	public int Lua_timeout_loop() {return lua_timeout_loop;} private int lua_timeout_loop = 10000000;
	public boolean Lua_log_enabled() {return lua_log_enabled;} private boolean lua_log_enabled;
	public Xop_log_invoke_wkr Invoke_wkr() {return invoke_wkr;} private Xop_log_invoke_wkr invoke_wkr;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_enabled))				return Yn.XtoStr(enabled);
		else if	(ctx.Match(k, Invk_enabled_))				enabled = m.ReadBool("v");
		else if	(ctx.Match(k, Invk_lua_timeout))			return lua_timeout;
		else if	(ctx.Match(k, Invk_lua_timeout_))			lua_timeout = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_lua_timeout_polling))	return lua_timeout_polling;
		else if	(ctx.Match(k, Invk_lua_timeout_polling_))	lua_timeout_polling = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_lua_log_enabled))		return Yn.XtoStr(lua_log_enabled);
		else if	(ctx.Match(k, Invk_lua_log_enabled_))		lua_log_enabled = m.ReadBool("v");
		else if	(ctx.Match(k, Invk_lua_timeout_busy_wait))	return lua_timeout_busy_wait;
		else if	(ctx.Match(k, Invk_lua_timeout_busy_wait_))	lua_timeout_busy_wait = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_lua_timeout_loop))		return lua_timeout_loop;
		else if	(ctx.Match(k, Invk_lua_timeout_loop_))		lua_timeout_loop = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_invoke_wkr))				return m.ReadYnOrY("v") ? Invoke_wkr_or_new() : GfoInvkAble_.Null;
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String Invk_enabled = "enabled", Invk_enabled_ = "enabled_"
	, Invk_lua_timeout = "lua_timeout", Invk_lua_timeout_ = "lua_timeout_"
	, Invk_lua_timeout_polling = "lua_timeout_polling", Invk_lua_timeout_polling_ = "lua_timeout_polling_"
	, Invk_lua_log_enabled = "lua_log_enabled", Invk_lua_log_enabled_ = "lua_log_enabled_"
	, Invk_lua_timeout_loop = "lua_timeout_loop", Invk_lua_timeout_loop_ = "lua_timeout_loop_"
	, Invk_lua_timeout_busy_wait = "lua_timeout_busy_wait", Invk_lua_timeout_busy_wait_ = "lua_timeout_busy_wait_"
	, Invk_invoke_wkr = "invoke_wkr"
	;
	public Xop_log_invoke_wkr Invoke_wkr_or_new() {
		if (invoke_wkr == null) invoke_wkr = app.Log_mgr().Make_wkr_invoke();
		return invoke_wkr;
	}
	public static Err err_(String fmt, Object... args) {return Err_.new_fmt_(fmt, args);}
	public static Err err_(Exception e, String fmt, Object... args) {return Err_.new_fmt_(fmt, args);}
}

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
package gplx.xowa; import gplx.*;
public class Xowc_parser implements GfoInvkAble {
	public boolean Flag_xtns_pages_init() {return flag_xtns_pages_init;} public Xowc_parser Flag_xtns_pages_init_(boolean v) {flag_xtns_pages_init = v; return this;} private boolean flag_xtns_pages_init = true;
	public Xowc_xtns Xtns() {return xtns;} private Xowc_xtns xtns = new Xowc_xtns();
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_xtns))				return xtns;
		else return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_xtns = "xtns";
}

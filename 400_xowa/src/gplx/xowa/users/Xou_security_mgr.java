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
package gplx.xowa.users; import gplx.*; import gplx.xowa.*;
public class Xou_security_mgr implements GfoInvkAble {
	public Xou_security_mgr(Xoa_app app) {this.app = app;} private Xoa_app app;
	public boolean Web_access_enabled() {return app.Api_root().Net().Enabled();}
	public void Web_access_enabled_(boolean v) {app.Api_root().Net().Enabled_(v);}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_web_access_enabled))			return Yn.Xto_str(this.Web_access_enabled());
		else if	(ctx.Match(k, Invk_web_access_enabled_))		Web_access_enabled_(m.ReadYn("v"));
		return this;
	}
	public static final String 
	  Invk_web_access_enabled = "web_access_enabled", Invk_web_access_enabled_ = "web_access_enabled_";
}

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
package gplx.xowa.apis.xowa.html.modules; import gplx.*; import gplx.xowa.*; import gplx.xowa.apis.*; import gplx.xowa.apis.xowa.*; import gplx.xowa.apis.xowa.html.*;
public class Xoapi_navframe implements GfoInvkAble {
	public void Init_by_kit(Xoa_app app) {
	}
	public boolean Collapsed() {return collapsed;} private boolean collapsed;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_collapsed))	 		return Yn.Xto_str(collapsed);
		else if	(ctx.Match(k, Invk_collapsed_))	 		collapsed = m.ReadYn("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String
	  Invk_collapsed = "collapsed", Invk_collapsed_ = "collapsed_"
	;
}

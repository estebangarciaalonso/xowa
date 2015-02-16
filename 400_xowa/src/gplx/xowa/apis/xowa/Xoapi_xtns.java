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
package gplx.xowa.apis.xowa; import gplx.*; import gplx.xowa.*; import gplx.xowa.apis.*;
import gplx.xowa.apis.xowa.xtns.*;
public class Xoapi_xtns implements GfoInvkAble {
	public void Init_by_kit(Xoa_app app) {
		scribunto.Init_by_kit(app);
	}
	public Xoapi_scribunto		Scribunto()		{return scribunto;} private final Xoapi_scribunto scribunto = new Xoapi_scribunto();
	public Xoapi_wikibase		Wikibase()		{return wikibase;}  private final Xoapi_wikibase  wikibase = new Xoapi_wikibase();
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_scribunto))	 			return scribunto;
		else if	(ctx.Match(k, Invk_wikibase))	 			return wikibase;
		else	return GfoInvkAble_.Rv_unhandled;
	}
	private static final String
	  Invk_scribunto = "scribunto"
	, Invk_wikibase  = "wikibase"
	;
}

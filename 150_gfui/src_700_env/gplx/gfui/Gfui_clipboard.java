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
package gplx.gfui; import gplx.*;
public interface Gfui_clipboard extends GfoInvkAble, RlsAble {
	void Copy(String s);
}
class Gfui_clipboard_null implements Gfui_clipboard {
	public void Copy(String s) {}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {return this;}
	public void Rls() {}
	public static final Gfui_clipboard_null Null = new Gfui_clipboard_null(); Gfui_clipboard_null() {}
}

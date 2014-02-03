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
package gplx.xowa.gui.views; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
import gplx.gfui.*;
public class Xog_tab_box_mgr {
	public Xog_tab_box_mgr(Gfui_html html_box) {
		html_box_mgr = new Xog_html_box_mgr(this, html_box);
	}
	public byte View_mode() {return view_mode;} public Xog_tab_box_mgr View_mode_(byte v) {view_mode = v; return this;} private byte view_mode = Xog_view_mode.Id_read;
	public Xog_html_box_mgr Html_box_mgr() {return html_box_mgr;} private Xog_html_box_mgr html_box_mgr;
}

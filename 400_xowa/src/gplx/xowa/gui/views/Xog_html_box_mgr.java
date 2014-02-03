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
public class Xog_html_box_mgr {
	private Xog_tab_box_mgr tab_mgr; private Gfui_html html_box;
	public Xog_html_box_mgr(Xog_tab_box_mgr tab_mgr, Gfui_html html_box) {this.tab_mgr = tab_mgr; this.html_box = html_box;}
	public void Show(Xoa_page page) {
		byte view_mode = tab_mgr.View_mode();
		byte[] html_src = page.Wiki().Html_mgr().Output_mgr().Gen(page, view_mode);
		Html_src_(page, html_src);
		if (view_mode == Xog_view_mode.Id_read)			// used only for Xosrh test; DATE:2014-01-29
			page.Root().Data_htm_(html_src);
	}
	private void Html_src_(Xoa_page page, byte[] html_src) {
		html_box.Html_doc_html_(String_.new_utf8_(html_src));
		html_box.Html_doc_body_focus();
	}
}
/*
	@Test  public void Read() {
		fxt.Display();
		String expd_html = "<i>a</i>";
		fxt.Expd_html_doc_html(expd_html);
		fxt.Expd_html_doc_body_focus(true)
		Tfds.Eq(fxt.Page().Root().Data_html(), ByteAry_.new_(expd_html));
	}
*/
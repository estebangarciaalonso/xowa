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
package gplx.xowa; import gplx.*;
import gplx.xowa.gui.history.*;
public interface Xog_box_html extends Gfui_box {
	String Text(); void Text_(String v);
	void Html_doc_body_focus();
	String Html_elem_atr_get_str(String elem_id, String atr_key);
	boolean Html_elem_atr_get_bool(String elem_id, String atr_key);
}
interface Xog_win_view {
	Xog_history_mgr History_mgr();
	Gfui_box_win Win_box();
	Gfui_box_text Url_box();
	Xog_box_html Html_box();
	Gfui_box_text Find_box();
	Gfui_box_text Prog_box();
	void Launch();
	void Exec_async(String cmd);
}

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
import gplx.gfui.*;
public interface Xog_win_wtr extends Gfo_usr_dlg {
	void Ini(Gfui_kit kit, Xog_win win);
	void Info_box_clear(boolean clear_text);
	void Html_img_update(String elem_id, String elem_src, int elem_width, int elem_height);
	void Html_atr_set(String elem_id, String atr_key, String atr_val);
	void Html_elem_delete(String elem_id);
}

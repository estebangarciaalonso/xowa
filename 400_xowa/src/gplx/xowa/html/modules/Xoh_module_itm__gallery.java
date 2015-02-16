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
package gplx.xowa.html.modules; import gplx.*; import gplx.xowa.*; import gplx.xowa.html.*;
import gplx.xowa.gui.*;
public class Xoh_module_itm__gallery implements Xoh_module_itm {
	public boolean Enabled() {return enabled;} public void Enabled_y_() {enabled = true;} public void Enabled_(boolean v) {enabled = v;} private boolean enabled;
	public void Clear() {enabled = false;}
	public byte[] Key() {return Key_const;} private static final byte[] Key_const = Bry_.new_ascii_("gallery");
	public void Write_css_include(Xoa_app app, Xow_wiki wiki, Xoa_page page, Xoh_module_wtr wtr) {}
	public void Write_css_script(Xoa_app app, Xow_wiki wiki, Xoa_page page, Xoh_module_wtr wtr) {}
	public void Write_js_include(Xoa_app app, Xow_wiki wiki, Xoa_page page, Xoh_module_wtr wtr) {}
	public void Write_js_head_script(Xoa_app app, Xow_wiki wiki, Xoa_page page, Xoh_module_wtr wtr) {}
	public void Write_js_tail_script(Xoa_app app, Xow_wiki wiki, Xoa_page page, Xoh_module_wtr wtr) {}
	public void Write_js_head_global(Xoa_app app, Xow_wiki wiki, Xoa_page page, Xoh_module_wtr wtr) {
		if (!enabled) return;
		wtr.Write_js_global_ini_atr_val(Key_enabled			, true);
	}
	private static final byte[]
	  Key_enabled				= Bry_.new_ascii_("gallery-packed-enabled")
	;
}

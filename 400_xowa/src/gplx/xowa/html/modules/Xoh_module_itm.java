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
public class Xoh_module_itm {
	public Xoh_module_itm(String key) {this.key = key;}
//		public int Id() {return id;} private int id;
	public String Key() {return key;} private String key;
	public Xoh_script_mgr Scripts() {return scripts;} private Xoh_script_mgr scripts = new Xoh_script_mgr();
	public Xoh_module_itm Scripts_init(String script) {
		Xoh_script_itm itm = new Xoh_script_itm().Source_(script);
		scripts.Add(itm);
		return this;
	}
	public void Bld_js(ByteAryBfr bfr) {
		int scripts_len = scripts.Len();
		for (int i = 0; i < scripts_len; i++) {
			Xoh_script_itm script = scripts.Get_at(i);
			bfr.Add_str(script.Source());
		}
	}
	/*
	Position	// top, bottom
	Targets		// mobile, desktop
	Styles
	Dependencies
	Messages
	*/
	public static final Xoh_module_itm[] Ary_empty = new Xoh_module_itm[0];
}

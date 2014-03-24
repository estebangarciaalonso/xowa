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
public class Xoh_module_list {
	private Xoh_module_itm[] ary; private int ary_max;
	private Xoh_module_list(int ary_max) {this.ary_max = ary_max; ary = new Xoh_module_itm[ary_max];}
	public void Add(Xoh_module_itm itm) {
		ary[itm.Id()] = itm;
	}
	public void Clear() {
		for (int i = 0; i < ary_max; i++)
			ary[i] = null;
	}
}

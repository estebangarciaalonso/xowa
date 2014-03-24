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
public class Xoh_module_regy {
	private Xoh_module_itm[] ary; private int ary_max;
	private OrderedHash hash;
	private int id_next = 0;
	public Xoh_module_regy(Xow_wiki wiki) {}
	public Xoh_module_itm Get_or_make(String key, Xoh_module_itm_init_func init_func) {
		if (hash == null) Init();
		Xoh_module_itm rv = (Xoh_module_itm)hash.Fetch(key);
		if (rv == null) {
			int id_cur = ++id_next;
			rv = new Xoh_module_itm(id_cur, key);
			ary[id_cur] = rv;
			hash.Add(key, rv);
			init_func.Module_init(rv);
		}
		return rv;
	}
	private void Init() {
		this.ary_max = 4; ary = new Xoh_module_itm[ary_max];
		hash = OrderedHash_.new_();
	}
}

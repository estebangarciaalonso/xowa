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
public class Xog_view_data {
	private byte[][] tmpl_stack_ary = ByteAry_.Ary_empty; private int tmpl_stack_ary_len = 0, tmpl_stack_ary_max = 0;
	public void Clear() {
		tmpl_stack_ary = ByteAry_.Ary_empty;
		tmpl_stack_ary_len = tmpl_stack_ary_max = 0;
		pages_recursed = false;
	}
	public boolean Pages_recursed() {return pages_recursed;} public Xog_view_data Pages_recursed_(boolean v) {pages_recursed = v; return this;} private boolean pages_recursed;
	public void Tmpl_stack_del() {--tmpl_stack_ary_len;}
	public boolean Tmpl_stack_add(byte[] key) {
		for (int i = 0; i < tmpl_stack_ary_len; i++) {
			if (ByteAry_.Match(key, tmpl_stack_ary[i])) return false;
		}
		int new_len = tmpl_stack_ary_len + 1;
		if (new_len > tmpl_stack_ary_max) {
			tmpl_stack_ary_max = new_len * 2;
			tmpl_stack_ary = (byte[][])Array_.Resize(tmpl_stack_ary, tmpl_stack_ary_max);
		}
		tmpl_stack_ary[tmpl_stack_ary_len] = key;
		tmpl_stack_ary_len = new_len;
		return true;
	}
}

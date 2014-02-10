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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Scrib_args {		
	private KeyVal[] ary; private int ary_len;
	public Scrib_args(KeyVal[] v) {Init(v);}
	public byte[]	Get_bry_or_empty(int i)			{KeyVal rv = Get(i); return rv == null ? ByteAry_.Empty : ByteAry_.new_utf8_(String_.cast_	(rv.Val()));}
	public String	Get_str_or_null(int i)			{KeyVal rv = Get(i); return rv == null ? null			: String_.cast_	(rv.Val());}
	public int		Get_int_or(int i, int or)		{KeyVal rv = Get(i); return rv == null ? or				: Int_.coerce_	(rv.Val());}
	private void Init(KeyVal[] v) {
		int v_len = v.length;
		int v_max = -1;
		for (int i = 0; i < v_len; i++) {
			KeyVal kv = v[i];
			int idx = Int_.cast_(kv.Key_as_obj());
			if (v_max < idx) v_max = idx;
		}
		this.ary_len = v_max;
		if (v_max == v_len) {		// keys are in sequential order; EX: [1:a,2:b,3:c]
			this.ary = v;
		}
		else {						// keys are not in sequential order, or there are gaps; EX: [1:a,3:c]
			ary = new KeyVal[ary_len];
			for (int i = 0; i < v_len; i++) {
				KeyVal kv = v[i];
				int idx = Int_.cast_(kv.Key_as_obj());
				ary[idx - ListAdp_.Base1] = kv;
			}
		}
	}
	private KeyVal Get(int i) {return i > -1 && i < ary_len ? ary[i] : null;}
}

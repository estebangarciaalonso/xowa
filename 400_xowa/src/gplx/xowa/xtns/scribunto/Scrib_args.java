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
	public int Len() {return ary_len;}
	public String	Pull_str(int i)					{Object rv = Get_or_fail(i); return String_.cast_(rv);}
	public byte[]	Pull_bry(int i)					{Object rv = Get_or_fail(i); return ByteAry_.new_utf8_(String_.cast_(rv));}
	public int		Pull_int(int i)					{Object rv = Get_or_fail(i); return Int_.coerce_(rv);}	// coerce to handle "1" and 1; will still fail if "abc" is passed
	public String	Cast_str_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: String_.cast_		(rv);}
	public byte[]	Cast_bry_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: ByteAry_.new_utf8_(String_.cast_	(rv));}	// NOTE: cast is deliberate; Scrib call checkType whi
	public byte[]	Cast_bry_or_empty(int i)		{Object rv = Get_or_null(i); return rv == null ? ByteAry_.Empty : ByteAry_.new_utf8_(String_.cast_	(rv));}
	public boolean		Cast_bool_or_y(int i)			{Object rv = Get_or_null(i); return rv == null ? Bool_.Y		: Bool_.cast_(rv);}
	public boolean		Cast_bool_or_n(int i)			{Object rv = Get_or_null(i); return rv == null ? Bool_.N		: Bool_.cast_(rv);}
	public int		Cast_int_or(int i, int or)		{Object rv = Get_or_null(i); return rv == null ? or				: Int_.coerce_(rv);}	// coerce to handle "1" and 1;
	public byte[]	Form_bry_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: ByteAry_.new_utf8_(Object_.XtoStr_OrNull(rv));}	// NOTE: cast is deliberate; Scrib call checkType whi
	public byte[][]	Cast_params_as_bry_ary_or_empty(int params_idx)	{
		if (params_idx < 0 || params_idx >= ary_len) return ByteAry_.Ary_empty;
		int rv_len = ary_len - params_idx;
		byte[][] rv = new byte[rv_len][];
		for (int i = 0; i < rv_len; i++) {
			KeyVal kv = ary[i + params_idx];
			rv[i] = ByteAry_.new_utf8_(String_.cast_(kv.Val()));
		}
		return rv;
	}
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
	private Object Get_or_null(int i) {
		if (i < 0 || i >= ary_len) return null;
		KeyVal kv = ary[i];
		return kv == null ? null : kv.Val();
	}
	private Object Get_or_fail(int i) {
		if (i < 0 || i >= ary_len) throw Err_.new_fmt_("scrib arg idx out of bounds; idx={0} len={1}", i, ary_len);
		KeyVal kv = ary[i];
		Object rv = kv == null ? null : kv.Val();
		if (rv == null) throw Err_.new_fmt_("scrib arg is null; idx={0} len={1}", i, ary_len);
		return rv;
	}
}

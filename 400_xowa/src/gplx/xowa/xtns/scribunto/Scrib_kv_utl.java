/*
XOWA: the extensible offline wiki application
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
class Scrib_kv_utl {
	public static KeyVal[] base1_obj_(Object v) {return new KeyVal[] {KeyVal_.int_(0 + Scrib_interpreter.Base_1, v)};}
	public static KeyVal[] base1_many_(Object... vals) {
		int len = vals.length;
		KeyVal[] rv = new KeyVal[len];
		for (int i = 0; i < len; i++)
			rv[i] = KeyVal_.int_(i + Scrib_interpreter.Base_1, vals[i]);
		return rv;
	}
	public static KeyVal[] base1_list_(ListAdp list) {
		int len = list.Count();
		KeyVal[] rv = new KeyVal[len];
		for (int i = 0; i < len; i++)
			rv[i] = KeyVal_.int_(i + Scrib_interpreter.Base_1, list.FetchAt(i));
		list.Clear();
		return rv;
	}
	public static KeyVal[] base1_obj_ary_(Object... ary) {
		int len = ary.length;
		KeyVal[] rv = new KeyVal[len];
		for (int i = 0; i < len; i++)
			rv[i] = KeyVal_.int_(i + Scrib_interpreter.Base_1, ary[i]);
		return rv;
	}
	public static KeyVal[] flat_many_(Object... vals) {
		int len = vals.length;
		KeyVal[] rv = new KeyVal[len / 2];
		for (int i = 0; i < len; i += 2)
			rv[i / 2] = KeyVal_.new_((String)vals[i], vals[i + 1]);
		return rv;
	}
	public static Object Val_to_obj(KeyVal[] ary, int idx)					{return ary[idx].Val();}
	public static Object Val_to_obj_or(KeyVal[] ary, int idx, Object or)	{return idx < ary.length ? ary[idx].Val() : null;}
	public static byte[] Val_to_bry(KeyVal[] ary, int idx)					{return ByteAry_.new_utf8_(Val_to_str(ary, idx));}
	public static byte[] Val_to_bry_or(KeyVal[] ary, int idx, byte[] or)	{
		String rv = Val_to_str_or(ary, idx, null);
		return rv == null ? or : ByteAry_.new_utf8_(rv);
	}
	public static String Val_to_str(KeyVal[] ary, int idx) {
		if (ary == null) throw Err_.new_("ary is null"); if (idx >= ary.length) throw Err_.new_fmt_("idx is not in bounds: {0} {1}", idx, KeyVal_.Xto_str(ary));
		Object o = ary[idx].Val();
		try {return (String)o;}
		catch (Exception e) {throw Err_.cast_(e, String.class, o);}
	}
	public static String Val_to_str_or(KeyVal[] ary, int idx, String or) {
		if (ary == null) throw Err_.new_("ary is null"); if (idx >= ary.length) return or;
		Object o = ary[idx].Val();
		try {return (String)o;}
		catch (Exception e) {throw Err_.cast_(e, String.class, o);}
	}
	public static int Val_to_int_or(KeyVal[] ary, int idx, int or) {
		if (ary == null) throw Err_.new_("ary is null"); if (idx >= ary.length) return or;
		Object o = ary[idx].Val();
		try {return coerce_(o);}
		catch (Exception e) {throw Err_.err_(e, "coerce to int failed; {0} {1}", o, KeyVal_.Xto_str(ary));}
	}
	public static int Val_to_int(KeyVal[] ary, int idx) {
		if (ary == null) throw Err_.new_("ary is null"); if (idx >= ary.length) throw Err_.new_fmt_("idx is not in bounds: {0} {1}", idx, KeyVal_.Xto_str(ary));
		Object o = ary[idx].Val();
		try {return coerce_(o);}
		catch (Exception e) {throw Err_.err_(e, "coerce to int failed; {0} {1}", o, KeyVal_.Xto_str(ary));}
	}
	public static boolean Val_to_bool_or(KeyVal[] ary, int idx, boolean or) {
		if (ary == null) throw Err_.new_("ary is null"); if (idx >= ary.length) return or;
		Object o = ary[idx].Val();
		try {return Bool_.cast_(o);}
		catch (Exception e) {throw Err_.err_(e, "coerce to int failed; {0} {1}", o, KeyVal_.Xto_str(ary));}
	}
	public static boolean Val_to_bool_or(KeyVal[] ary, String key, boolean or) {
		if (ary == null) throw Err_.new_("ary is null");
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			KeyVal kv = ary[i];
			if (String_.Eq(kv.Key(), key))
				return Bool_.cast_(kv.Val());
		}
		return or;
	}
	public static double Val_to_double(KeyVal[] ary, int idx) {
		if (ary == null) throw Err_.new_("ary is null"); if (idx >= ary.length) throw Err_.new_fmt_("idx is not in bounds: {0} {1}", idx, KeyVal_.Xto_str(ary));
		Object o = ary[idx].Val();
		try {return Double_.cast_(o);}
		catch (Exception e) {throw Err_.err_(e, "coerce to double failed; {0} {1}", o, KeyVal_.Xto_str(ary));}
	}
	public static KeyVal[] Val_to_KeyVal_ary(KeyVal[] ary, int idx) {
		if (ary == null) throw Err_.new_("ary is null"); if (idx >= ary.length) throw Err_.new_fmt_("idx is not in bounds: {0} {1}", idx, KeyVal_.Xto_str(ary));
		try {return (KeyVal[])ary[idx].Val();}
		catch (Exception e) {throw Err_.cast_manual_msg_(e, KeyVal[].class, KeyVal_.Xto_str(ary));}
	}
	public static KeyVal[] Val_to_KeyVal_ary_or(KeyVal[] ary, int idx, KeyVal[] or) {
		if (ary == null) throw Err_.new_("ary is null"); if (idx >= ary.length) return or;
		try {return (KeyVal[])ary[idx].Val();}
		catch (Exception e) {throw Err_.cast_manual_msg_(e, KeyVal[].class, KeyVal_.Xto_str(ary));}
	}
	private static int coerce_(Object o) {
		try {String s = String_.as_(o); return s == null ? Int_.cast_(o) : Int_.parse_(s);}
		catch (Exception e) {throw Err_.cast_(e, int.class, o);}
	}
	public static boolean Val_is_KeyVal_ary(KeyVal kv) {return ClassAdp_.Eq_typeSafe(kv.Val(), KeyVal[].class);}
}

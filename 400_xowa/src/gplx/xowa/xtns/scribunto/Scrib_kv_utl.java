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
class Scrib_kv_utl {
	public static KeyVal[] base1_bool_false()	{return new KeyVal[] {KeyVal_.int_(0 + Scrib_interpreter.Base_1, false)};}
	public static KeyVal[] base1_obj_null()		{return base1_obj_(null);}
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
		if (ary == null) throw Err_.new_("ary is null");
		int ary_len = ary.length;
		if (ary_len == 0 && idx == 0) return "";	// NOTE: Modules can throw exceptions in which return value is nothing; do not fail; return ""; EX: -logy; DATE:2013-10-14
		if (idx >= ary_len) throw Err_.new_fmt_("idx is not in bounds: {0} {1}", idx, KeyVal_.Xto_str(ary));
		Object o = ary[idx].Val();
		try {return (String)o;}
		catch (Exception e) {throw Err_.cast_(e, String.class, o);}
	}
	public static String Val_to_str_force(KeyVal[] ary, int idx) {
		if (ary == null) throw Err_.new_("ary is null");
		int ary_len = ary.length;
		if (ary_len == 0 && idx == 0) return "";	// NOTE: Modules can throw exceptions in which return value is nothing; do not fail; return ""; EX: -logy; DATE:2013-10-14
		if (idx >= ary_len) throw Err_.new_fmt_("idx is not in bounds: {0} {1}", idx, KeyVal_.Xto_str(ary));
		Object o = ary[idx].Val();
		try {return Object_.XtoStr_OrEmpty(o);}
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
		try {return Int_.coerce_(o);}
		catch (Exception e) {throw Err_.err_(e, "coerce to int failed; {0} {1}", o, KeyVal_.Xto_str(ary));}
	}
	public static int Val_to_int(KeyVal[] ary, int idx) {
		if (ary == null) throw Err_.new_("ary is null"); if (idx >= ary.length) throw Err_.new_fmt_("idx is not in bounds: {0} {1}", idx, KeyVal_.Xto_str(ary));
		Object o = ary[idx].Val();
		try {return Int_.coerce_(o);}
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
	public static byte[] Val_to_url_qry_args(Xow_wiki wiki, KeyVal[] values, int idx) {
		Object qry_args_obj = Scrib_kv_utl.Val_to_obj_or(values, idx, null);
		if (qry_args_obj == null) return ByteAry_.Empty;
		Class<?> qry_args_cls = ClassAdp_.ClassOf_obj(qry_args_obj);
		if		(qry_args_cls == String.class)
			return ByteAry_.new_utf8_((String)qry_args_obj);
		else if (qry_args_cls == KeyVal[].class) {
			ByteAryBfr bfr = wiki.Utl_bry_bfr_mkr().Get_b128();
			KeyVal[] kvs = (KeyVal[])qry_args_obj;
			int len = kvs.length;
			for (int i = 0; i < len; i++) {
				KeyVal kv = kvs[i];
				if (i != 0) bfr.Add_byte(Byte_ascii.Amp);
				bfr.Add_str(kv.Key());
				bfr.Add_byte(Byte_ascii.Eq);
				bfr.Add_str(kv.Val_to_str_or_empty());
			}
			return bfr.Mkr_rls().XtoAryAndClear();
		}
		else {
			wiki.App().Gui_wtr().Warn_many("", "", "unknown type for GetUrl query args: ~{0}", ClassAdp_.NameOf_type(qry_args_cls));
			return ByteAry_.Empty;
		}
	}
	public static boolean Val_is_KeyVal_ary(KeyVal kv) {return ClassAdp_.Eq_typeSafe(kv.Val(), KeyVal[].class);}
}

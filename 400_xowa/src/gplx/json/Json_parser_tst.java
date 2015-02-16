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
package gplx.json; import gplx.*;
import org.junit.*;
public class Json_parser_tst {
	Json_parser_fxt fxt = new Json_parser_fxt();
	@Before public void init() {fxt.Clear();}
	@Test   public void Null()					{fxt.Test_parse_val0("{'k0':null}"			, null);}
	@Test   public void Bool_n()				{fxt.Test_parse_val0("{'k0':false}"			, false);}
	@Test   public void Bool_y()				{fxt.Test_parse_val0("{'k0':true}"			, true);}
	@Test   public void Num()					{fxt.Test_parse_val0("{'k0':123}"			, 123);}
	@Test   public void Num_neg()				{fxt.Test_parse_val0("{'k0':-123}"			, -123);}
	@Test   public void Str()					{fxt.Test_parse_val0("{'k0':'v0'}"			, "v0");}
	@Test   public void Str_esc_quote()			{fxt.Test_parse_val0("{'k0':'a\\\"b'}"		, "a\"b");}
	@Test   public void Str_esc_hex4()			{fxt.Test_parse_val0("{'k0':'a\\u0021b'}"	, "a!b");}
	@Test   public void Num_dec()				{fxt.Test_parse("{'k0':1.23}"				, fxt.itm_nde_().Subs_add_many(fxt.itm_kv_dec_("k0", "1.23")));}
	@Test   public void Num_exp()				{fxt.Test_parse("{'k0':1e+2}"				, fxt.itm_nde_().Subs_add_many(fxt.itm_kv_dec_("k0", "1e+2")));}
	@Test   public void Num_mix()				{fxt.Test_parse("{'k0':-1.23e-1}"			, fxt.itm_nde_().Subs_add_many(fxt.itm_kv_dec_("k0", "-1.23e-1")));}
	@Test   public void Str_many()				{fxt.Test_parse("{'k0':'v0','k1':'v1','k2':'v2'}", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_("k0", "v0"), fxt.itm_kv_("k1", "v1"), fxt.itm_kv_("k2", "v2")));}
	@Test   public void Ary_empty()				{fxt.Test_parse("{'k0':[]}", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_ary_int_("k0")));}
	@Test   public void Ary_int()				{fxt.Test_parse("{'k0':[1,2,3]}", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_ary_int_("k0", 1, 2, 3)));}
	@Test   public void Ary_str()				{fxt.Test_parse("{'k0':['a','b','c']}", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_ary_str_("k0", "a", "b", "c")));}
	@Test   public void Ary_ws()				{fxt.Test_parse("{'k0': [ 1 , 2 , 3 ] }", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_ary_int_("k0", 1, 2, 3)));}
	@Test   public void Subs_int()				{fxt.Test_parse("{'k0':{'k00':1}}", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_("k0", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_("k00", 1)))));}
	@Test   public void Subs_empty()			{fxt.Test_parse("{'k0':{}}", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_("k0", fxt.itm_nde_())));}
	@Test   public void Subs_ws()				{fxt.Test_parse("{'k0': { 'k00' : 1 } }", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_("k0", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_("k00", 1)))));}
	@Test   public void Ws()					{fxt.Test_parse(" { 'k0' : 'v0' } ", fxt.itm_nde_().Subs_add_many(fxt.itm_kv_("k0", "v0")));}
	public static String Replace_apos_as_str(String v) {return String_.new_utf8_(Replace_apos(Bry_.new_utf8_(v)));}
	public static byte[] Replace_apos(byte[] v) {return Bry_.Replace(v, Byte_ascii.Apos, Byte_ascii.Quote);}
}
class Json_parser_fxt {
	public void Clear() {
		if (parser == null) {
			parser = new Json_parser();
			factory = parser.Factory();
		}
	}	Json_parser parser; Json_factory factory; Bry_bfr tmp_bfr = Bry_bfr.reset_(255);
	Json_itm itm_int_(int v)								{return Json_itm_tmp.new_int_(v);}
	Json_itm itm_str_(String v)								{return Json_itm_tmp.new_str_(v);}
	public Json_itm_ary itm_ary_()							{return factory.Ary(-1, -1);}
	public Json_itm_nde itm_nde_()							{return factory.Nde(-1);}
	public Json_itm_kv itm_kv_null_(String k)				{return factory.Kv(itm_str_(k), factory.Null());}
	public Json_itm_kv itm_kv_(String k, String v)			{return factory.Kv(itm_str_(k), itm_str_(v));}
	public Json_itm_kv itm_kv_(String k, int v)				{return factory.Kv(itm_str_(k), itm_int_(v));}
	public Json_itm_kv itm_kv_(String k, boolean v)			{return factory.Kv(itm_str_(k), v ? factory.Bool_y() : factory.Bool_n());}
	public Json_itm_kv itm_kv_dec_(String k, String v)		{return factory.Kv(itm_str_(k), new Json_itm_tmp(Json_itm_.Tid_decimal, v));}
	public Json_itm_kv itm_kv_(String k, Json_itm_nde v)	{return factory.Kv(itm_str_(k), v);}
	public Json_itm_kv itm_kv_ary_int_(String k, int... v) {
		Json_itm_ary ary = factory.Ary(-1, -1);
		int len = v.length;
		for (int i = 0; i < len; i++)
			ary.Subs_add(itm_int_(v[i]));
		return factory.Kv(itm_str_(k), ary);
	}
	public Json_itm_kv itm_kv_ary_str_(String k, String... v) {
		Json_itm_ary ary = factory.Ary(-1, -1);
		int len = v.length;
		for (int i = 0; i < len; i++)
			ary.Subs_add(itm_str_(v[i]));
		return factory.Kv(itm_str_(k), ary);
	}
	public void Test_parse(String raw_str, Json_itm... expd_ary) {
		byte[] raw = Json_parser_tst.Replace_apos(Bry_.new_utf8_(raw_str));
		Json_doc doc = parser.Parse(raw);
		doc.Root().Print_as_json(tmp_bfr, 0);
		String actl = tmp_bfr.Xto_str_and_clear();
		String expd = Xto_str(raw, doc, expd_ary, 0, expd_ary.length);
		Tfds.Eq_str_lines(expd, actl, actl);
	}
	public void Test_parse_val0(String raw_str, Object expd) {
		byte[] raw = Json_parser_tst.Replace_apos(Bry_.new_utf8_(raw_str));
		Json_doc doc = parser.Parse(raw);
		Json_itm_kv kv = Json_itm_kv.cast_(doc.Root().Subs_get_at(0));	// assume root has kv as first sub; EX: {"a":"b"}
		Object actl = kv.Val().Data();	 // NOTE: Data_bry is escaped val; EX: a\"b has DataBry of a"b
		Tfds.Eq(expd, actl);
	}
	String Xto_str(byte[] raw, Json_doc doc, Json_itm[] ary, int bgn, int end) {
		for (int i = bgn; i < end; i++) {
			Json_itm itm = ary[i];
			itm.Print_as_json(tmp_bfr, 0);
		}
		return tmp_bfr.Xto_str_and_clear();
	}
}

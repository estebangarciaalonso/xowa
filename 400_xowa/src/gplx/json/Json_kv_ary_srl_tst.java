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
public class Json_kv_ary_srl_tst {
	@Before public void init() {fxt.Clear();} private Json_kv_ary_srl_fxt fxt = new Json_kv_ary_srl_fxt();
	@Test   public void Null()					{fxt.Test_parse("{'k0':null}"					, fxt.ary_(fxt.kv_str_("k0", null)));}
	@Test   public void Bool_n()				{fxt.Test_parse("{'k0':false}"					, fxt.ary_(fxt.kv_bool_("k0", false)));}
	@Test   public void Num()					{fxt.Test_parse("{'k0':123}"					, fxt.ary_(fxt.kv_int_("k0", 123)));}
	@Test   public void Str()					{fxt.Test_parse("{'k0':'v0'}"					, fxt.ary_(fxt.kv_str_("k0", "v0")));}
	@Test   public void Num_dec()				{fxt.Test_parse("{'k0':1.23}"					, fxt.ary_(fxt.kv_dec_("k0", DecimalAdp_.parse_("1.23"))));}
	@Test   public void Ary_int()				{fxt.Test_parse("{'k0':[1,2,3]}"				, fxt.ary_(fxt.kv_obj_("k0", fxt.ary_(fxt.kv_int_("1", 1), fxt.kv_int_("2", 2), fxt.kv_int_("3", 3)))));}
	@Test   public void Ary_empty()				{fxt.Test_parse("{'k0':[]}"						, fxt.ary_(fxt.kv_obj_("k0", fxt.ary_())));}
	@Test   public void Subs_int()				{fxt.Test_parse("{'k0':{'k00':1,'k01':2}}"		, fxt.ary_(fxt.kv_obj_("k0", fxt.ary_(fxt.kv_int_("k00", 1), fxt.kv_int_("k01", 2)))));}
	@Test   public void Subs_empty()			{fxt.Test_parse("{'k0':{}}"						, fxt.ary_(fxt.kv_obj_("k0", fxt.ary_())));}
}
class Json_kv_ary_srl_fxt {
	public void Clear() {
		if (parser == null) {
			parser = new Json_parser();
		}
	}	private Json_parser parser;
	public void Test_parse(String raw_str, KeyVal[] expd) {
		byte[] raw_bry = Json_parser_tst.Replace_apos(Bry_.new_utf8_(raw_str));
		Json_doc doc = parser.Parse(raw_bry);
		KeyVal[] actl = Json_kv_ary_srl.Val_by_itm_nde(doc.Root());
		Tfds.Eq_str_lines(KeyVal_.Ary_x_to_str(expd), KeyVal_.Ary_x_to_str(actl));
	}
	public KeyVal[] ary_(KeyVal... ary) {return ary;}
	public KeyVal kv_obj_(String key, Object val)		{return KeyVal_.new_(key, val);}
	public KeyVal kv_str_(String key, String val)		{return KeyVal_.new_(key, val);}
	public KeyVal kv_int_(String key, int val)			{return KeyVal_.new_(key, val);}
	public KeyVal kv_bool_(String key, boolean val)		{return KeyVal_.new_(key, Bool_.Xto_str_lower(val));}
	public KeyVal kv_dec_(String key, DecimalAdp val)	{return KeyVal_.new_(key, val.Xto_str());}
}

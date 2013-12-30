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
package gplx.xowa.langs.casings; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
import org.junit.*;
public class Xol_case_mgr_tst {
	Xol_case_mgr_fxt fxt = new Xol_case_mgr_fxt();
	@Before public void init() {fxt.Clear();}
	@Test  public void Mw_parse() {
		fxt.parse_mw__tst(fxt.itm_both_("A", "a"), fxt.itm_both_("B", "b"));
	}
	@Test  public void Xo_parse() {
		fxt.parse_xo__tst(fxt.Ini_ltrs_raw(), fxt.itm_both_("a", "A"), fxt.itm_upper_("b", "B"), fxt.itm_lower_("C", "c"));
	}
	@Test  public void Upper_a()		{fxt.Ini_ltrs().Upper("aAaz", "AAAz");}
	@Test  public void Upper_ab()		{fxt.Ini_ltrs().Upper("abac", "ABAc");}
	@Test  public void Lower_a()		{fxt.Ini_ltrs().Lower("aAaZ", "aaaZ");}
	@Test  public void Lower_ac()		{fxt.Ini_ltrs().Lower("ABAC", "aBac");}
//		@Test  public void Hack() {
//			Xol_case_itm[] ary = Xol_case_itm_.Universal;
//			ByteAryBfr bfr = ByteAryBfr.new_();
//			for (int i = 0; i < ary.length; i++) {
//				Xol_case_itm itm = ary[i];
//				bfr.Add_str("xo|");
//				bfr.Add_bry_comma(itm.Src_ary()).Add_byte_pipe();
//				bfr.Add_bry_comma(itm.Trg_ary()).Add_byte_nl();
//			}
//			Io_mgr._.SaveFilStr("C:\\test1.txt", bfr.XtoStrAndClear());
//		}
}
class Xol_case_mgr_fxt {
	Xol_case_mgr case_mgr = new Xol_case_mgr();
	public void Clear() {
		case_mgr.Clear();
	}	String_bldr sb = String_bldr_.new_();
	public Xol_case_itm_ary itm_both_(String src, String trg)	{return new Xol_case_itm_ary(Xol_case_itm_.Tid_both , ByteAry_.new_utf8_(src), ByteAry_.new_utf8_(trg));}
	public Xol_case_itm_ary itm_upper_(String src, String trg) {return new Xol_case_itm_ary(Xol_case_itm_.Tid_upper, ByteAry_.new_utf8_(src), ByteAry_.new_utf8_(trg));}
	public Xol_case_itm_ary itm_lower_(String src, String trg) {return new Xol_case_itm_ary(Xol_case_itm_.Tid_lower, ByteAry_.new_utf8_(src), ByteAry_.new_utf8_(trg));}
	public String Ini_ltrs_raw() {
		return String_.Concat_lines_nl
			(	"0|a|A"
			,	"1|b|B"
			,	"2|C|c"
			);
	}
	public Xol_case_mgr_fxt Ini_ltrs() {
		case_mgr.Add_bulk(ByteAry_.new_utf8_(Ini_ltrs_raw()));
		return this;
	}
	public Xol_case_mgr_fxt Upper(String raw_str, String expd) {return Case_build(Bool_.Y, raw_str, expd);}
	public Xol_case_mgr_fxt Lower(String raw_str, String expd) {return Case_build(Bool_.N, raw_str, expd);}
	public Xol_case_mgr_fxt Case_build(boolean upper, String raw_str, String expd) {
		byte[] raw = ByteAry_.new_utf8_(raw_str);
		byte[] actl = case_mgr.Case_build(upper, raw, 0, raw.length);
		Tfds.Eq(expd, String_.new_utf8_(actl));
		return this;
	}
	public void parse_xo__tst(String raw, Xol_case_itm_ary... expd) {
		Tfds.Eq_str_lines(Xto_str(expd), Xto_str(Xol_case_itm_.parse_xo_(ByteAry_.new_utf8_(raw))));
	}
	public void parse_mw__tst(Xol_case_itm_ary... expd) {
		String raw = raw_(expd);
		Xol_case_itm[] actl = Xol_case_itm_.parse_mw_(ByteAry_.new_utf8_(raw));
		Tfds.Eq_str_lines(Xto_str(expd), Xto_str(actl));
	}
	public String Xto_str(Xol_case_itm[] ary) {
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			Xol_case_itm itm = ary[i];
			sb.Add(Byte_.XtoStr(itm.Tid())).Add_char_pipe().Add(String_.new_utf8_(itm.Src_ary())).Add_char_pipe().Add(String_.new_utf8_(itm.Trg_ary())).Add_char_nl();
		}
		return sb.XtoStrAndClear();
	}
	String raw_(Xol_case_itm_ary[] itms) {
		int itms_len = itms.length;
		uppers_list.Clear(); lowers_list.Clear();
		for (int i = 0; i < itms_len; i++) {
			Xol_case_itm_ary itm = itms[i];
			String src = String_.new_utf8_(itm.Src_ary());
			String trg = String_.new_utf8_(itm.Trg_ary());
			switch (itm.Tid()) {
				case Xol_case_itm_.Tid_both:
					uppers_list.Add(trg); uppers_list.Add(src);
					lowers_list.Add(src); lowers_list.Add(trg);						
					break;
			}
		}
		return raw_str_(uppers_list.XtoStrAry(), lowers_list.XtoStrAry());
	}	ListAdp uppers_list = ListAdp_.new_(), lowers_list = ListAdp_.new_();
	String raw_str_(String[] uppers, String[] lowers) {
		sb.Add("a:2:{s:14:\"wikiUpperChars\";a:1046:{");
		raw_ary(sb, uppers);
		sb.Add("}");
		sb.Add("s:14:\"wikiLowerChars\";a:1038:{");
		raw_ary(sb, lowers);
		sb.Add("}}");
		return sb.XtoStrAndClear();
	}
	private void raw_ary(String_bldr sb, String[] ary) {
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			String itm = ary[i];
			int itm_len = String_.Len(itm);
			sb.Add_fmt("s:{0}:\"{1}\";", itm_len, itm);
		}
	}
}
/*
a:2:{s:14:"wikiUpperChars";a:1046:{s:1:"a";s:1:"A";s:1:"b";}s:14:"wikiLowerChars";a:1038:{s:1:"A";s:1:"a";s:1:"B";}}
*/
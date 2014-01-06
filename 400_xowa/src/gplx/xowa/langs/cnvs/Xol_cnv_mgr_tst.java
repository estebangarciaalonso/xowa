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
package gplx.xowa.langs.cnvs; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
import org.junit.*;
public class Xol_cnv_mgr_tst {
	private Xol_cnv_mgr_fxt fxt = new Xol_cnv_mgr_fxt();
	@Before public void init() {fxt.Clear();}
	@Test   public void Basic() {
		Xol_cnv_mgr_fxt.Init_convert_file(fxt.App(), "zh", "zh-hant", KeyVal_.new_("a", "A"), KeyVal_.new_("c", "C"));
		fxt.Test_convert("zh", "zh-hant", "abcd", "AbCd");
	}
//		@Test  public void Convert() {
//			Xol_cnv_mgr_fxt.Init_convert_file(fxt.App(), "zh", "zh-hans", KeyVal_.new_("a", "x"));
//			fxt.Parser_fxt().ini_defn_clear();
//			fxt.Parser_fxt().ini_defn_add("convert_x", "val");
//			fxt.Parser_fxt().tst_Parse_tmpl_str_test("{{convert_a}}", "{{test}}", "val");
//			fxt.Parser_fxt().ini_defn_clear();
//		}
}
class Xol_cnv_mgr_fxt {
	public Xoa_app App() {return app;} private Xoa_app app;
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public Xop_fxt Parser_fxt() {return parser_fxt;} private Xop_fxt parser_fxt;
	public void Clear() {
		app = Xoa_app_fxt.app_();
		wiki = Xoa_app_fxt.wiki_(app, "zh.wikipedia.org", app.Lang_mgr().Get_by_key_or_new(ByteAry_.new_utf8_("zh")));
		parser_fxt = new Xop_fxt(app, wiki);
	}
	public static void Init_convert_file(Xoa_app app, String lang, String vnt, KeyVal... ary) {
		Xol_mw_parse_grp grp = new Xol_mw_parse_grp();
		grp.Lng_(ByteAry_.new_ascii_(lang)).Vnt_(ByteAry_.new_utf8_(vnt));
		int ary_len = ary.length;
		Xol_mw_parse_itm[] itms = new Xol_mw_parse_itm[ary_len];
		for (int i = 0; i < ary_len; i++) {
			KeyVal kv = ary[i];
			itms[i] = new Xol_mw_parse_itm(ByteAry_.new_utf8_(kv.Key()), ByteAry_.new_utf8_(kv.Val_to_str_or_empty()));				
		}
		grp.Itms_(itms);
		ByteAryBfr bfr = ByteAryBfr.new_();
		grp.Write_as_gfs(bfr);
		Io_url convert_url = Xol_cnv_mgr.Bld_url(app, lang);
		Io_mgr._.SaveFilBfr(convert_url, bfr);
	}
	public void Test_convert(String lang, String vnt, String raw, String expd) {
//			Xol_cnv_grp convert_grp = app.Lang_mgr().Get_by_key_or_new(ByteAry_.new_ascii_(lang)).Cnv_mgr().Get_or_new(ByteAry_.new_ascii_(vnt));
//			ByteAryBfr bfr = ByteAryBfr.new_();
//			boolean converted = convert_grp.Convert_to_bfr(bfr, ByteAry_.new_utf8_(raw));
//			String actl = converted ? bfr.XtoStrAndClear() : raw;
//			Tfds.Eq(expd, actl);
	}
}

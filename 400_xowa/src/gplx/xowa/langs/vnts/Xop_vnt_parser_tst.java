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
package gplx.xowa.langs.vnts; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
import org.junit.*;
public class Xop_vnt_parser_tst {	// uses zh-hant as cur_vnt
	private Xop_vnt_parser_fxt fxt = new Xop_vnt_parser_fxt();
	@Before public void init() {fxt.Clear();}
	@Test  public void Literal()			{fxt.Test_parse("-{A}-", "A");}
	@Test  public void Bidi()				{fxt.Test_parse("-{zh-hans:A;zh-hant:B}-", "B");}
	@Test  public void Empty()				{fxt.Test_parse("a-{}-b", "ab");}
	@Test  public void Unknown_empty()		{fxt.Test_parse("a-{|}-c", "ac");}
	@Test  public void Unknown_text()		{fxt.Test_parse("a-{|b}-c", "abc");}
	@Test  public void Unknown_flag()		{fxt.Test_parse("a-{x|b}-c", "abc");}
	@Test  public void Lang_y()				{fxt.Test_parse("-{zh-hant|A}-", "A");}
	@Test  public void Lang_n()				{fxt.Test_parse("-{zh-hans|A}-", "");}
	@Test  public void Raw()				{fxt.Test_parse("-{R|zh-hans:A;}-", "zh-hans:A;");}
//		@Test  public void Descrip()			{fxt.Test_parse("-{D|zh-hans:A;}-", "zh-hans:A");}
	@Test  public void Tmpl() {
		fxt.Parser_fxt().ini_page_create("Template:A", "B");
		fxt.Test_parse("-{{{A}}}-", "B");
	}
	@Test  public void Tmpl_arg() {
		fxt.Parser_fxt().ini_page_create("Template:A", "-{{{{1}}}}-");
		fxt.Test_parse("{{A|B}}", "B");
	}
	@Test  public void Parser_function() {
		fxt.Test_parse("-{{{#expr:1}}}-", "1");
	}
	@Test  public void Ignore() {
		fxt.Test_parse("-{{#expr:1}}-", "-1-");
	}
}
class Xop_vnt_parser_fxt {		
	public Xop_fxt Parser_fxt() {return fxt;} private Xop_fxt fxt;
	public Xop_vnt_parser_fxt Clear() {
		Xoa_app app = Xoa_app_fxt.app_();
		Xow_wiki wiki = Xoa_app_fxt.wiki_(app, "zh.wikipedia.org");
		fxt = new Xop_fxt(app, wiki);
		Init_vnt_mgr(wiki.Lang().Vnt_mgr(), "zh-hans", "zh-hant");
		Xop_vnt_lxr_.set_(wiki);
		wiki.Lang().Vnt_mgr().Cur_vnt_(ByteAry_.new_ascii_("zh-hant"));
		return this;
	}
	private static void Init_vnt_mgr(Xol_vnt_mgr vnt_mgr, String... vnts_str) {
		byte[][] vnts_bry = ByteAry_.Ary(vnts_str);
		int vnts_bry_len = vnts_bry.length;
		for (int i = 0; i < vnts_bry_len; i++)
			vnt_mgr.Get_or_new(vnts_bry[i]);
		vnt_mgr.Convert_ttl_init();
	}
	public Xop_vnt_parser_fxt Test_parse(String raw, String expd) {
		fxt.tst_Parse_page_all_str(raw, expd);
		return this;
	}
}
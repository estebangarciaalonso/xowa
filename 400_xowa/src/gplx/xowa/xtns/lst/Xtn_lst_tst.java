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
package gplx.xowa.xtns.lst; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Xtn_lst_tst {
	Xtn_lst_fxt fxt = new Xtn_lst_fxt();
	@Before public void init() {fxt.Clear();}
	@Test  public void Basic() {
		fxt.Clear().Page_txt_("a<section begin=key0/>val0<section end=key0/> b").Test_lst("{{#lst:section_test|key0}}", "val0");
	}
	@Test  public void Multiple() {
		fxt.Clear().Page_txt_("a<section begin=key0/>val00<section end=key0/> b<section begin=key0/> val01<section end=key0/> c").Test_lst("{{#lst:section_test|key0}}", "val00 val01");
	}
	@Test  public void Range() {
		fxt.Clear().Page_txt_("a<section begin=key0/>val0<section end=key0/> b<section begin=key1/> val1<section end=key1/> c<section begin=key2/> val2<section end=key2/> d")
			.Test_lst("{{#lst:section_test|key0|key2}}", "val0 b val1 c val2");
	}
	@Test  public void Nest() {
		fxt.Clear().Page_txt_("<section begin=key0/>val0<section begin=key00/> val00<section end=key00/><section end=key0/>").Test_lst("{{#lst:section_test|key0}}", "val0 val00");
	}
	@Test  public void Wikitext() {	// PURPOSE: assert section is expanded to html
		fxt.Clear().Page_txt_("a<section begin=key0/>''val0''<section end=key0/> b").Test_lst("{{#lst:section_test|key0}}", "<i>val0</i>");
	}
	@Test  public void Refs_ignored() {	// PURPOSE: assert that nearby refs are ignored
		fxt.Clear().Page_txt_("a<section begin=key0/>val0<ref>ref1</ref><section end=key0/> b  <ref>ref2</ref>").Test_lst("{{#lst:section_test|key0}}<references/>", String_.Concat_lines_nl
		(	"val0<sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup><ol class=\"references\">"
		,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\">ref1</span></li>"
		,	"</ol>"
		));
	}
	@Test  public void Missing_end() {
		fxt.Page_txt_("a <section begin=key0/>val0<section end=key1/> b");
		fxt.Clear().Test_lst("{{#lst:section_test|key0}}", "val0 b");	// end is missing; read to end;
	}
	@Test  public void Missing_end_noinclude() {	// EX: de.wikisource.org/wiki/Versuch_einer_mokscha-mordwinischen_Grammatik/Mokscha-Texte; Seite:Ahlqvist_Forschungen_auf_dem_Gebiete_der_ural-altaischen_Sprachen_I.pdf/111
		fxt.Page_txt_("a <section begin=key0/>val0<section end=key1/> b<noinclude>c</noinclude>");
		fxt.Clear().Test_lst("{{#lst:section_test|key0}}", "val0 b");	// end is missing; ignore noinclude
	}
	@Test  public void Missing_bgn_dupe() {
		fxt.Page_txt_("a <section begin=key0/>val0<section end=key0/> b<section begin=key1/>val1<section end=key0/>");
		fxt.Clear().Test_lst("{{#lst:section_test|key0}}", "val0");
	}
	@Test  public void Nowiki() {	// PURPOSE.fix: <nowiki> was creating incorrect sections; DATE:2013-07-11
		fxt.Clear().Page_txt_("a<nowiki>''c''</nowiki><section begin=key0/>val0<section end=key0/> b").Test_lst("{{#lst:section_test|key0}}", "val0");
	}
	@Test  public void Fullpagename() {	// PURPOSE.fix: lst creates its own ctx; make sure ctx has same page_name of calling page (Test page) not default (Main page); DATE:2013-07-11
		fxt.Clear().Page_txt_("a <section begin=key0/>{{FULLPAGENAME}}<section end=key0/> b").Test_lst("{{#lst:section_test|key0}}", "Test page");
	}
//			fxt.tst("a<section begin=key0/> val0<section end=key0/> b<section begin=key1/> val1<section end=key1/> c", "{{#lstx:sections|key0}}", "a val1 c");
//			fxt.tst("a<section begin=key0/> val0<section end=key0/> b<section begin=key1/> val1<section end=key1/> c", "{{#lstx:sections|key0|key1}}", "a c");
//			fxt.tst("a<section begin=key0/> val0<section end=key0/> b<section begin=key1/> val1<section end=key1/> c", "{{#lstx:sections|key0|val9}}", "a val9 b val1 c");
//			fxt.tst("a<section begin=key0/> val0<section end=key0/> b<section begin=key1/> val1<section end=key1/> c<section begin=key0/> val2<section end=key0/> d", "{{#lstx:sections|key0| val3}}", "val3 b val1 c val3");
//			fxt.tst("a<section begin=key0/> val0<section end=key0/> b<section begin=key1/> val1<section end=key1/> c<section begin=key2/> val2<section end=key2/> d", "{{#lstx:sections|key0|valx|key2}}", "a valx d");
}
class Xtn_lst_fxt {
	public Xtn_lst_fxt Clear() {
		if (fxt == null) fxt = new Xop_fxt();
		fxt.Reset();
		Io_mgr._.InitEngine_mem();
		return this;
	}	Xop_fxt fxt;
	public Xtn_lst_fxt Page_txt_(String v) {page_txt = v; return this;} private String page_txt;
	public void Test_lst(String func, String expd) {
		fxt.ini_page_create("section_test", page_txt);
		fxt.tst_Parse_page_all_str(func, expd);
	}
}
/*
*/
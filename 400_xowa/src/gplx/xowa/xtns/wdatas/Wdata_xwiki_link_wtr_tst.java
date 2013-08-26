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
package gplx.xowa.xtns.wdatas; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Wdata_xwiki_link_wtr_tst {
	@Before public void init() {fxt.Init();} Wdata_wiki_mgr_fxt fxt = new Wdata_wiki_mgr_fxt();
	@Test  public void Skip_xwiki_lang_for_self() {	// PURPOSE: list of language links should not include self
		fxt.Init_xwikis_add("en", "fr", "de");
		fxt.Init_qids_add("en", Xow_wiki_type_.Tid_wikipedia, "Q1_en", "Q1");
		fxt.Init_pages_add(fxt.page_bldr_("Q1").Link_add("enwiki", "Q1_en").Link_add("frwiki", "Q1_fr").Link_add("dewiki", "Q1_de").Xto_page_doc());
		fxt.Test_xwiki_links("Q1_en", "Q1_fr", "Q1_de");
	}
	@Test   public void No_external_lang_links__de() {
		fxt.Init_xwikis_add("fr", "de");
		fxt.Init_qids_add("en", Xow_wiki_type_.Tid_wikipedia, "Q1_en", "Q1");
		fxt.Init_pages_add(fxt.page_bldr_("Q1").Link_add("enwiki", "Q1_en").Link_add("frwiki", "Q1_fr").Link_add("dewiki", "Q1_de").Xto_page_doc());
		fxt.Init_external_links_mgr_add("de");
		fxt.Test_xwiki_links("Q1_en", "Q1_de");
		fxt.Init_external_links_mgr_clear();
		fxt.Test_parse_langs("{{noexternallanglinks:de}}", String_.Concat_lines_nl
		(	"<div id=\"xowa-lang\">"
		,	"  <h5>In other languages (<a href=\"/site/www.wikidata.org/wiki/Q1\">wikidata</a>)</h5>"
		,	"  <h4>grp1</h4>"
		,	"  <table style='width: 100%;'>"
		,	"    <tr>"
		,	"      <td style='width: 10%; padding-bottom: 5px;'>German</td><td style='width: 20%; padding-bottom: 5px;'><a hreflang=\"de\" title=\"Q1 de\" href=\"/site/de.wikipedia.org/wiki/Q1 de\">Q1 de</a></td><td style='width: 3%; padding-bottom: 5px;'></td>"
		,	"    </tr>"
		,	"  </table>"
		,	"</div>"
		));
	}
//		@Test   public void No_wikidata_link() {
//			fxt.Init_xwikis_add("fr", "de");
//			fxt.Test_parse_langs("[[fr:A]]", String_.Concat_lines_nl
//			(	"<div id=\"xowa-lang\">"
//			,	"  <h5>In other languages</h5>"
//			,	"  <h4>grp1</h4>"
//			,	"  <ul style='-moz-column-count: 3; list-style:none;'>"
//			,	"    <li><span style='display:inline-block; min-width:150px'>French</span><a hreflang=\"fr\" title=\"A\" href=\"/site/fr.wikipedia.org/wiki/A\">A</a></li>"
//			,	"    <li><span style='display:inline-block; min-width:150px'>French</span><a hreflang=\"fr\" title=\"A\" href=\"/site/fr.wikipedia.org/wiki/A\">A</a></li>"
//			,	"  </ul>"
//			,	"</div>"
//			));
//		}

//		@Test   public void No_external_lang_links__sort() {
//			fxt.Init_xwikis_add("de", "fr");
//			fxt.Init_qids_add("en", Xow_wiki_type_.Tid_wikipedia, "Q1_en", "Q1");
//			fxt.Init_pages_add("Q1", fxt.page_bldr_("Q1").Link_add("enwiki", "Q1_en").Link_add("frwiki", "Q1_fr").Link_add("dewiki", "Q1_de").Xto_page_doc());
//			fxt.Init_external_links_mgr_add("*");
//			fxt.Test_xwiki_links("Q1_en", "Q1_de", "Q1_fr");
//			fxt.Init_external_links_mgr_clear();
//			fxt.Test_parse_langs("{{noexternallanglinks:*}}", String_.Concat_lines_nl
//			(	"<div id=\"xowa-lang\">"
//			,	"  <h5>In other languages (<a href=\"/site/www.wikidata.org/wiki/Q1\">wikidata</a>)</h5>"
//			,	"  <h4>grp1</h4>"
//			,	"  <ul style='-moz-column-count: 3; list-style:none;'>"
//			,	"    <li><span style='display:inline-block; min-width:150px'>German</span><a hreflang=\"de\" title=\"Q1 de\" href=\"/site/de.wikipedia.org/wiki/Q1 de\">Q1 de</a></li>"
//			,	"    <li><span style='display:inline-block; min-width:150px'>French</span><a hreflang=\"fr\" title=\"Q1 fr\" href=\"/site/fr.wikipedia.org/wiki/Q1 fr\">Q1 fr</a></li>"
//			,	"  </ul>"
//			,	"</div>"
//			));
//		}
}

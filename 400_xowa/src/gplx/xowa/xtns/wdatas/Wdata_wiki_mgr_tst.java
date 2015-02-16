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
package gplx.xowa.xtns.wdatas; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*; import gplx.xowa.xtns.wdatas.imports.*;
public class Wdata_wiki_mgr_tst {
	@Test  public void Basic() {
		Wdata_wiki_mgr_fxt fxt = new Wdata_wiki_mgr_fxt().Init();
		fxt.Init_links_add("enwiki", "Q1", "Q1_en");
		fxt.Test_link("Q1", "Q1_en");
		fxt.Test_link("Q2", null);
	}
	@Test  public void Case_sensitive() {	// PURPOSE: wikidata lkp should be case_sensitive; a vs A DATE:2013-09-03
		Wdata_wiki_mgr_fxt fxt = new Wdata_wiki_mgr_fxt().Init();
		fxt.Init_links_add("enwiki", "Page", "Page_data");
		fxt.Test_link("Page", "Page_data");
		fxt.Test_link("PAGE", null);
	}
	@Test  public void Non_canonical_ns() {	// PURPOSE: handle wikidata entries in non-canonical ns; EX:ukwikisource and Author; PAGE:uk.s:Автор:Богдан_Гаврилишин DATE:2014-07-23
		Wdata_wiki_mgr_fxt fxt = new Wdata_wiki_mgr_fxt().Init();
		Xow_wiki wiki = fxt.Wiki();
		wiki.Ns_mgr().Add_new(124, "Test_ns");
		fxt.Init_links_add("enwiki", "000", "Test_ns:Test_page", "pass");	// NOTE: wdata will save to "000" ns, b/c "124" ns is not canonical
		Xoa_ttl ttl = Xoa_ttl.parse_(fxt.Wiki(), 124, Bry_.new_ascii_("Test_page"));
		fxt.Test_link(ttl, "pass");
	}
	@Test   public void Write_json_as_html() {
		Wdata_wiki_mgr_fxt fxt = new Wdata_wiki_mgr_fxt().Init();
		fxt.Test_write_json_as_html("{'a':'b','c':['d','e'],'f':{'g':'<h>'}}", String_.Concat_lines_nl_skip_last
		(	"<span id=\"xowa-wikidata-json\">"
		,	"{ \"a\":\"b\""
		,	", \"c\":"
		,	"  [ \"d\""
		,	"  , \"e\""
		,	"  ]"
		,	", \"f\":"
		,	"  { \"g\":\"&lt;h&gt;\""
		,	"  }"
		,	"}"
		,	"</span>"
		));
	}
	@Test  public void Get_low_qid() {
		Wdata_wiki_mgr_fxt fxt = new Wdata_wiki_mgr_fxt();
		fxt.Test_get_low_qid("q1"	, "q1");	// return self
		fxt.Test_get_low_qid("q2|q1", "q1");	// return lowest
	}
}

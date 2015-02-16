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
package gplx.xowa.xtns.scribunto.lib; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*;
import org.junit.*;
import gplx.xowa.xtns.wdatas.*;
public class Scrib_lib_wikibase_tst {
	@Before public void init() {
		fxt.Clear_for_lib();
		lib = fxt.Core().Lib_wikibase().Init();
	}	private Scrib_invoke_func_fxt fxt = new Scrib_invoke_func_fxt(); private Scrib_lib lib;
	@Test  public void GetGlobalSiteId() {
		fxt.Test_scrib_proc_str(lib, Scrib_lib_wikibase.Invk_getGlobalSiteId, Object_.Ary_empty, "enwiki");
	}
	@Test  public void GetEntityId() {
		Wdata_wiki_mgr_fxt wdata_fxt = new Wdata_wiki_mgr_fxt().Init(fxt.Parser_fxt(), false);
		wdata_fxt.Init_links_add("enwiki", "Earth", "q2");
		fxt.Test_scrib_proc_str(lib, Scrib_lib_wikibase.Invk_getEntityId, Object_.Ary("Earth"							), "q2");
		fxt.Test_scrib_proc_str(lib, Scrib_lib_wikibase.Invk_getEntityId, Object_.Ary("missing_page"					), "");
	}
	@Test  public void GetEntity() {
		Wdata_wiki_mgr_fxt wdata_fxt = new Wdata_wiki_mgr_fxt().Init(fxt.Parser_fxt(), false);
		wdata_fxt.Init_pages_add(wdata_fxt.Wdoc_bldr("q2").Add_label("en", "b").Xto_wdoc());
		fxt.Test_scrib_proc_str_ary(lib, Scrib_lib_wikibase.Invk_getEntity, Object_.Ary("q2", false), String_.Concat_lines_nl_skip_last
		( "1="
		, "  id=q2"
		, "  type=item"
		, "  schemaVersion=2"
		, "  labels="
		, "    en="
		, "      language=en"
		, "      value=b"
		));
	}
	@Test  public void GetEntity_property() {	// PURPOSE: getEntity should be able to convert "p2" to "Property:P2"; EX:es.w:Arnold_Gesell; DATE:2014-02-18
		Wdata_wiki_mgr_fxt wdata_fxt = new Wdata_wiki_mgr_fxt().Init(fxt.Parser_fxt(), false);
		wdata_fxt.Init_pages_add(wdata_fxt.Wdoc_bldr("Property:p2").Add_label("en", "b").Xto_wdoc());
		fxt.Test_scrib_proc_str_ary(lib, Scrib_lib_wikibase.Invk_getEntity, Object_.Ary("p2", false), String_.Concat_lines_nl_skip_last
		( "1="
		, "  id=Property:p2"	// only difference from above
		, "  type=item"
		, "  schemaVersion=2"
		, "  labels="
		, "    en="
		, "      language=en"
		, "      value=b"
		));
	}
}	

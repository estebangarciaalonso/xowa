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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
import gplx.xowa.xtns.wdatas.*;
public class Scrib_lib_wikibase_tst {
	@Before public void init() {
		fxt.Clear();
		fxt.Init_page("{{#invoke:Mod_0|Func_0}}");
		lib = fxt.Engine().Lib_wikibase();
	}	private Scrib_pf_invoke_fxt fxt = new Scrib_pf_invoke_fxt(); private Scrib_lib lib;
	@Test  public void GetGlobalSiteId() {
		fxt.Test_lib_proc(lib, Scrib_lib_wikibase.Invk_getGlobalSiteId, Object_.Ary_empty, "enwiki");
	}
	@Test  public void GetEntityId() {
		Wdata_wiki_mgr_fxt wdata_fxt = new Wdata_wiki_mgr_fxt().Init(fxt.Parser_fxt(), false);
		wdata_fxt.Init_links_add("enwiki", "Earth", "q2");
		fxt.Test_lib_proc(lib, Scrib_lib_wikibase.Invk_getEntityId, Object_.Ary("Earth"							), "q2");
		fxt.Test_lib_proc(lib, Scrib_lib_wikibase.Invk_getEntityId, Object_.Ary("missing_page"					), "");
	}
	@Test  public void GetEntity() {
		Wdata_wiki_mgr_fxt wdata_fxt = new Wdata_wiki_mgr_fxt().Init(fxt.Parser_fxt(), false);
		wdata_fxt.Init_pages_add(wdata_fxt.page_bldr_("q2").Label_add("en", "b").Xto_page_doc());
		fxt.Test_lib_proc(lib, Scrib_lib_wikibase.Invk_getEntity, Object_.Ary("q2"								), String_.Concat_lines_nl
		(	""
		,	"  q2;item;"
		,	"    "
		,	"      en;b"
		));
	}
}	

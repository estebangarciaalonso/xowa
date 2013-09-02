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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Scrib_lib_title_tst {
	@Before public void init() {
		fxt.Clear();
		fxt.Init_page("{{#invoke:Mod_0|Func_0}}");
		lib = fxt.Engine().Lib_title();
	}	Scrib_pf_invoke_fxt fxt = new Scrib_pf_invoke_fxt(); Scrib_lib lib;
	@Test  public void NewTitle() {
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_newTitle, Object_.Ary("Page_0")				, "\n  true;false;;0;;Page 0;0;;wikitext;Page_0;false;false");
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_newTitle, Object_.Ary("Page_0", "Template")	, "\n  true;false;;10;Template;Page 0;0;;wikitext;Page_0;false;false");
		fxt.Parser_fxt().ini_Log_(Xop_ttl_log.Invalid_char);
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_newTitle, Object_.Ary("a[b")				, "null");	// invalid
	}
	@Test   public void GetUrl() {
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_getUrl, Object_.Ary("Main_Page", "fullUrl")								, "//en.wikipedia.org/wiki/Main_Page");
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_getUrl, Object_.Ary("Main_Page", "fullUrl", "action=edit")				, "//en.wikipedia.org/wiki/Main_Page?action=edit");
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_getUrl, Object_.Ary("Main_Page", "localUrl")							, "/wiki/Main_Page");
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_getUrl, Object_.Ary("Main_Page", "canonicalUrl")						, "http://en.wikipedia.org/wiki/Main_Page");
	//		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_getUrl, Object_.Ary("Main_Page", "fullUrl", "", "http")					, "http://en.wikipedia.org/wiki/Main_Page");
	}
	@Test   public void MakeTitle() {
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_makeTitle, Object_.Ary("Module", "A")									, ttl_data_("828", "Module", "A"));
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_makeTitle, Object_.Ary(828, "A")										, ttl_data_("828", "Module", "A"));
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_makeTitle, Object_.Ary("Template", "A", "b")							, ttl_data_("10", "Template", "A", "b"));
		fxt.Parser_fxt().Wiki().Xwiki_mgr().Add_full("fr", "fr.wikipedia.org");
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_makeTitle, Object_.Ary("Template", "A", "b", "fr")						, ttl_data_("0", "", "Template:A", "b", "fr"));
	}
	@Test   public void FileExists() {
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_fileExists, Object_.Ary("A")											, "false");
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_fileExists, Object_.Ary("Template:A")									, "false");
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_fileExists, Object_.Ary("File:A.png")									, "false");
		fxt.Parser_fxt().ini_page_create("File:A.png");
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_fileExists, Object_.Ary("File:A.png")									, "true");
	}
	@Test   public void GetContent() {
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_getContent, Object_.Ary("A")											, "null");
		fxt.Parser_fxt().ini_page_create("A", "test");
		fxt.Test_lib_proc(lib, Scrib_lib_title.Invk_getContent, Object_.Ary("A")											, "test");
	}
	private static String ttl_data_(String ns_id, String ns_str, String ttl) {return ttl_data_(ns_id, ns_str, ttl, "", "");}
	private static String ttl_data_(String ns_id, String ns_str, String ttl, String anchor) {return ttl_data_(ns_id, ns_str, ttl, anchor, "");}
	private static String ttl_data_(String ns_id, String ns_str, String ttl, String anchor, String xwiki) {
		return "\n  true;false;" + xwiki  + ";" + ns_id + ";" + ns_str + ";" + ttl + ";0;" + anchor + ";wikitext;" + ttl + ";false;false";
	}
}	

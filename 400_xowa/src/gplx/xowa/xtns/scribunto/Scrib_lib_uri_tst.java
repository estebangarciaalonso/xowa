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
public class Scrib_lib_uri_tst {
	@Before public void init() {
		fxt.Clear();
		fxt.Init_page("{{#invoke:Mod_0|Func_0}}");
		lib = fxt.Engine().Lib_uri();
	}	Scrib_pf_invoke_fxt fxt = new Scrib_pf_invoke_fxt(); Scrib_lib lib;
	@Test  public void Url() {
		fxt.Test_lib_proc(lib, Scrib_lib_uri.Invk_localUrl		, Object_.Ary("a&b! c"						), "/wiki/A%26b!_c");
		fxt.Test_lib_proc(lib, Scrib_lib_uri.Invk_fullUrl		, Object_.Ary("a&b! c"						), "//en.wikipedia.org/wiki/A%26b!_c");
		fxt.Test_lib_proc(lib, Scrib_lib_uri.Invk_canonicalUrl	, Object_.Ary("a&b! c"						), "http://en.wikipedia.org/wiki/A%26b!_c");
		fxt.Test_lib_proc(lib, Scrib_lib_uri.Invk_localUrl		, Object_.Ary("a&b! c"		, "action=edit"	), "/wiki/A%26b!_c?action=edit");
		fxt.Test_lib_proc(lib, Scrib_lib_uri.Invk_localUrl		, Object_.Ary("Media:A.png"					), "/wiki/File:A.png");
	}
	@Test  public void AnchorEncode() {
		fxt.Test_lib_proc(lib, Scrib_lib_uri.Invk_anchorEncode	, Object_.Ary("[irc://a b c]"				), "b_c");
	}
	@Test  public void Init_uri_for_page() {
		fxt.Parser_fxt().Page_ttl_("Page_1");
		fxt.Test_lib_proc(lib, Scrib_lib_uri.Invk_init_uri_for_page	, Object_.Ary_empty						, "//en.wikipedia.org/wiki/Page_1");
	}
}	

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
package gplx.xowa.xtns.xowa_cmds; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Xop_xowa_cmd_tst {
	@Before public void init() {
		Xoa_gfs_mgr.Msg_parser_init();
	} private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Basic() {
		GfsCore._.AddCmd(fxt.App(), Xoa_gfs_mgr.Invk_app);
		fxt.Wiki().Sys_cfg().Xowa_cmd_enabled_(false);
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<xowa_cmd>"
			,	"app.users.get('anonymous').name;"
			,	"</xowa_cmd>"
			), String_.Concat_lines_nl_skipLast
			(	"app.users.get('anonymous').name;"
			));
		fxt.Wiki().Sys_cfg().Xowa_cmd_enabled_(true);
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<xowa_cmd>"
			,	"app.users.get('anonymous').name;"
			,	"</xowa_cmd>"
			), String_.Concat_lines_nl_skipLast
			(	"anonymous"
			));
	}
//		@Test  public void Wiki_list_fmtrs() {
//			fxt.Wiki().Sys_cfg().Xowa_cmd_enabled_(true);
//			fxt.App().Setup_mgr().Maint_mgr().Wiki_mgr().Add(ByteAry_.new_ascii_("en.wikipedia.org"));
//			fxt.Test_parse_page_all_str(String_.Concat_lines_nl_skipLast
//			(	"{|"
//			,	"<xowa_cmd>"
//			,	"app.fmtrs.new_grp {"
//			,	"  src = 'app.setup.maint.wikis;';"
//			,	"  fmt ='"
//			,	"|-"
//			,	"|~{<>domain;<>}'"
//			,	";"
//			,	"  run;"
//			,	"}"
//			,	"</xowa_cmd>"
//			,	"|}"
//			), 	String_.Concat_lines_nl
//			(	"<table>"
//			,	"  <tr>"
//			,	"    <td>en.wikipedia.org"
//			,	"    </td>"
//			,	"  </tr>"
////			,	"  <tr>"
////			,	"    <td>home"
////			,	"    </td>"
////			,	"  </tr>"
//			,	"</table>"
//			)
//			);
//		}
}

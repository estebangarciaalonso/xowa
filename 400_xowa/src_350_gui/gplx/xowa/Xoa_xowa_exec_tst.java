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
package gplx.xowa; import gplx.*;
import org.junit.*; import gplx.xowa.xtns.wdatas.*;
public class Xoa_xowa_exec_tst {
	@Before public void init() {fxt.Clear();} private Xoa_xowa_exec_fxt fxt = new Xoa_xowa_exec_fxt();
	@Test   public void Get_title() {
		fxt.Fxt().ini_page_create("exists");
		fxt.Test_get_title("exists", "1" , "0" , Int_.XtoStr(Int_.MinValue), "Exists", "false", "0001-01-01 00:00:00", "0");
		fxt.Test_get_title("absent", "0", "-1", Int_.XtoStr(Int_.MinValue), null	, "false", "0001-01-01 00:00:00", "0");
	}
}
class Xoa_xowa_exec_fxt {
	public void Clear() {
		fxt = new Xop_fxt();
	}	Xop_fxt fxt;
	public Xop_fxt Fxt() {return fxt;}
	public void Test_get_title(String ttl, Object... expd) {
		Xoa_app app = fxt.App();
		Xow_wiki wiki = fxt.Wiki();
		Xoa_page page = new Xoa_page(wiki, Xoa_ttl.parse_(wiki, ByteAry_.new_ascii_("mock_page")));
		app.Gui_mgr().Main_win().Page_(page);
		Xoa_xowa_exec exec = app.Gui_mgr().Main_win().Js_cbk();
		GfoMsg msg = GfoMsg_.new_cast_(Xoa_xowa_exec.Invk_get_titles_meta).Add("ttl", ttl);
		String[][] actl = (String[][])GfoInvkAble_.InvkCmd_msg(exec, Xoa_xowa_exec.Invk_get_titles_meta, msg);
		Tfds.Eq_ary_str(expd, actl[0]);
	}
}

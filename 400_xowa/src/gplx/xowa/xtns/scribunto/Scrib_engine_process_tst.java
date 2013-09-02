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
public class Scrib_engine_process_tst {
	@Test  public void Init() {
//			Xoa_app app = Xoa_app_fxt.app_(Xoa_app_fxt.Root_dir, Op_sys.Cur().Os_name());
//			app.Fsys_mgr().App_mgr().App_lua().Exe_url_(app.Fsys_mgr().Bin_plat_dir().GenSubFil_nest("lua", Op_sys.Cur().Tid_is_wnt() ? "lua5.1.exe" : "lua"));
//			Xow_wiki wiki = Xoa_app_fxt.wiki_tst_(app);
//			Scrib_engine engine = new Scrib_engine(app, wiki.Ctx());
//			engine.Interpreter().Dbg_print_(true);
//			engine.Init();
//			byte[] module_text = ByteAry_.new_utf8_(String_.Concat_lines_nl
//			(	"local p = {}"
//			,	""
//			,	"function p.world(frame)"
//			,	"    local foo = \"Hello world!\""
//			,	"    local arg1 = frame.args[1]"
//			,	"    local arg2 = frame.args[2]"
//			,	"    if arg1 ~= nil then foo = foo .. \" My \" .. arg1 end"
//			,	"    if arg2 ~= nil then foo = foo .. \" is \" .. arg2 end"
//			,	"    return foo"
//			,	"end"
//			,	""
//			,	"function p.noop(frame)"
//			,	"end"
//			,	""
//			,	"return p"
//			));
//			ByteAryBfr bfr = ByteAryBfr.reset_(255);
//			engine.Invoke(wiki, module_text, Xot_invk_mock.Null, Xot_invk_mock.new_(KeyVal_.int_(1, "app"), KeyVal_.int_(2, "xowa")), bfr, ByteAry_.new_utf8_("Module:Hello"), module_text, ByteAry_.new_utf8_("world"));
//            Tfds.Write(bfr.XtoStrAndClear());
	}
}

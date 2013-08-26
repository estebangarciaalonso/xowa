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
package gplx.xowa; import gplx.*;
import org.junit.*;
public class Xoad_wtr_dump_tst {
	Xop_fxt fxt = new Xop_fxt();
	Gfo_msg_log msg_log = new Gfo_msg_log("test"); App_log_wtr_html wtr = new App_log_wtr_html();
	@Test  public void Basic() {
		Gfo_msg_grp nde = Gfo_msg_grp_.new_(Xoa_app_.Nde, "dir");
		Gfo_msg_itm itm = Gfo_msg_itm_.new_warn_(nde, "itm", "test: int=~{0} String=~{1}");
		Xoa_page page = fxt.Page();
		byte[] src = ByteAry_.new_ascii_("abcde");
		msg_log.Add_itm_many(itm, src, 1, 3, 1, "a");
		wtr.Write(page, msg_log);
		Tfds.Eq_str_lines(String_.Concat_lines_nl
			(	"<table style='border-spacing:2px;'>"
			,	"  <tr>"
			,	"    <td class='xowa_log_cmd_warn'>xowa.dir.itm</td>"
			,	"    <td class='xowa_log_msg'>test: int=1 String=a</td>"
			,	"    <td class='xowa_log_excerpt'>a<span class='xowa_log_excerpt_span'>bc</span>de</td>"
//				,	"    <td class='xowa_log_todo'>/ xowa.dir: Test; itm \"bc\"</td>"
			,	"  </tr>"
			,	"</table>"
			), String_.new_utf8_(wtr.Html()));
	}
}

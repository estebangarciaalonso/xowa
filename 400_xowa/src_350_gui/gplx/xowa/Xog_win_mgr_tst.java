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
import org.junit.*;
import gplx.gfui.*;
public class Xog_win_mgr_tst {
	private Xog_win_fxt fxt = new Xog_win_fxt();
	@Before public void init() {
		fxt.Clear();
		fxt.Fxt().App().User().Wiki().Html_mgr().Output_mgr().Html_capable_(false);
		fxt.Fxt().Init_page_create(fxt.Fxt().App().User().Wiki(), "Main Page", "''a''");
	}
	@Test  public void Launch() {
		fxt.Launch();
		fxt.Html_box().Text_lines
		(	"<p><i>a</i>"
		,	"</p>"
		);
		fxt.Url_box().Text("home/wiki/Main_Page");
		fxt.Win_box().Text("Main Page - XOWA");
		fxt.Page_stack().Len_and_top(1, "home/wiki/Main_Page");
		fxt.Win_box().Focus_box("html_box");
	}
}
class Xog_win_fxt {
	public Xop_fxt Fxt() {return fxt;} private Xop_fxt fxt = new Xop_fxt();
	Xog_win_view view;
	public void Clear() {
		fxt.Reset();
		Xoa_gfs_mgr.Msg_parser_init();
		if (view == null) {
			view = new Xog_win_view_mok(fxt.App());
			page_stack = new Xoh_page_stack_tstr(view.History_mgr());
			win_box = new Gfui_box_win_tstr(view.Win_box());
			url_box = new Gfui_box_text_tstr(view.Url_box());
			html_box  = new Gfui_box_html_tstr(view.Html_box());
		}
	}
	public Xoh_page_stack_tstr Page_stack() {return page_stack;}  Xoh_page_stack_tstr page_stack;
	public Gfui_box_win_tstr Win_box() {return win_box;} Gfui_box_win_tstr win_box;
	public Gfui_box_text_tstr Url_box() {return url_box;} Gfui_box_text_tstr url_box;
	public Gfui_box_html_tstr Html_box() {return html_box;} Gfui_box_html_tstr html_box;
	public void Launch() {view.Launch();}
}

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
import gplx.gfui.*; import gplx.xowa.gui.history.*;
class Gfui_box_win_tstr {
	public Gfui_box_win_tstr(Gfui_box_win box) {this.box = box;} Gfui_box_win box;
	public void Text(String expd) 		{Tfds.Eq(expd, box.Text());}
	public void Focus_box(String expd) 	{Tfds.Eq(expd, box.Focus_box().Id());}
}
class Gfui_box_text_tstr {
	public Gfui_box_text_tstr(Gfui_box_text box) {this.box = box;} Gfui_box_text box;
	public void Text(String expd) 				{Tfds.Eq(expd, box.Text());}
	public void Text_lines_(String... expd) 	{Tfds.Eq_str_lines(String_.Concat_lines_nl_skipLast(expd), box.Text());}
}
class Gfui_box_html_tstr {
	public Gfui_box_html_tstr(Xog_box_html box) {this.box = box;} Xog_box_html box;
	public void Text(String expd) {Tfds.Eq(expd, box.Text());}
	public void Text_lines(String... expd) 	{Tfds.Eq_str_lines(String_.Concat_lines_nl_skipLast(expd), box.Text());}
}
class Xoh_page_stack_tstr {
	public Xoh_page_stack_tstr(Xog_history_mgr history_mgr) {this.history_mgr = history_mgr;} private Xog_history_mgr history_mgr;
	public void Len_and_top(int expd_len, String expd_url) {
		Tfds.Eq(expd_len, history_mgr.Count());
		Xoa_url url = history_mgr.Cur_page(null).Url();	// NOTE: passing in null, b/c wiki is only used for Fetch, and page should never be fetched
		Tfds.Eq(expd_url, String_.new_utf8_(ByteAry_.Add(url.Wiki_bry(), Xoh_href_parser.Href_wiki_bry, url.Page_bry())));
	} 
}

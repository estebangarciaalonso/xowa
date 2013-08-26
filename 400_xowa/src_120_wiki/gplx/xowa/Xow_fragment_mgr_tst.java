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
public class Xow_fragment_mgr_tst {
	Xow_fragment_mgr_fxt fxt = new Xow_fragment_mgr_fxt();
	@Before public void init()	{fxt.Clear();}
	@Test   public void Html_js_table_fmt() {
		fxt.Test_fragment(Xow_fragment_mgr.Invk_html_js_table, String_.Concat_lines_nl
		(	"  var xowa_global_values = {"
		,	"    'collapsible-collapse'         : 'Collapse',"
		,	"    'collapsible-expand'           : 'Expand',"
		,	"    'sort-descending'              : 'Sort descending',"
		,	"    'sort-ascending'               : 'Sort ascending',"
		,	"    'wgContentLanguage'            : 'en',"
		,	"    'wgSeparatorTransformTable'    : ['.\t.', ',\t,'],"
		,	"    'wgDigitTransformTable'        : ['', ''],"
		,	"    'wgDefaultDateFormat'          : 'dmy',"
		,	"    'wgMonthNames'                 : ['', 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],"
		,	"    'wgMonthNamesShort'            : ['', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']"
		,	"  };"
		));
	}
	@Test   public void Html_js_edit_toolbar_fmt() {
		fxt.Test_fragment(Xow_fragment_mgr.Invk_html_js_edit_toolbar, String_.Concat_lines_nl
		(	"  var xowa_edit_i18n = {"
		,	"    'bold_tip'             : 'Bold text',"
		,	"    'bold_sample'          : 'Bold text',"
		,	"    'italic_tip'           : 'Italic text',"
		,	"    'italic_sample'        : 'Italic text',"
		,	"    'link_tip'             : 'Internal link',"
		,	"    'link_sample'          : 'Link title',"
		,	"    'headline_tip'         : 'Level 2 headline',"
		,	"    'headline_sample'      : 'Headline text',"
		,	"    'ulist_tip'            : 'Bulleted list',"
		,	"    'ulist_sample'         : 'Bulleted list item',"
		,	"    'olist_tip'            : 'Numbered list',"
		,	"    'olist_sample'         : 'Numbered list item'"
		,	"  };"
		));
	}
}
class Xow_fragment_mgr_fxt {
	public void Clear() {
		if (wiki == null) {
			Xoa_app app = Xoa_app_fxt.app_();
			wiki = Xoa_app_fxt.wiki_tst_(app);
		}
	}	Xow_wiki wiki;
	public void Test_fragment(String key, String expd) {
		wiki.Fragment_mgr().Evt_lang_changed(wiki.Lang());
		byte[] actl = (byte[])GfoInvkAble_.InvkCmd(wiki.Fragment_mgr(), key);
		Tfds.Eq_str_lines(expd, String_.new_utf8_(actl));
	}
}
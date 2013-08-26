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
package gplx.xowa.gui.menus.contexts; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*; import gplx.xowa.gui.menus.*;
public class Xog_mnu_defaults {
	private OrderedHash hash;
	public Xog_mnu_itm Get_or_null(String key) {
		if (hash == null) Init();
		Xog_mnu_itm rv = (Xog_mnu_itm)hash.Fetch(key);
		return rv == null ? null : rv.Clone();
	}
	public void Init() {
		hash = OrderedHash_.new_();
		Add_grp(Key_file, "File", "f", "file/save.png");
		Add_grp(Key_edit, "Edit", "e", "edit/copy.png");
		Add_grp(Key_view, "View", "v", "view/page_read.png");
		Add_grp(Key_history, "History", "s", "history/show.png");
		Add_grp(Key_bookmarks, "Bookmarks", "b", "bookmarks/show.png");
		Add_grp(Key_tools, "Tools", "t", "tools/options.png");
		Add_grp(Key_tools_wikis, "Wikis", "w", "");
		Add_grp(Key_help, "Help", "h", "help/contents.png");
		Add_btn(Key_file_save_as, "Save As", "s", "file/save.png", "app.gui.cmds.file.save_as;");
		Add_btn(Key_file_print, "Print", "v", "file/print.png", "app.gui.cmds.file.print;");
		Add_btn(Key_file_exit, "Exit", "x", "file/exit.png", "app.gui.cmds.file.exit;");
		Add_btn(Key_edit_copy, "Copy", "c", "edit/copy.png", "app.gui.cmds.edit.copy;");
		Add_btn(Key_edit_select_all, "Select All", "s", "edit/select_all.png", "app.gui.cmds.edit.select_all;");
		Add_btn(Key_edit_find, "Find", "f", "edit/find.png", "app.gui.cmds.edit.find;");
		Add_btn(Key_view_font_increase, "Font Increase", "i", "view/font_increase.png", "app.gui.cmds.view.font_increase;");
		Add_btn(Key_view_font_decrease, "Font Decrease", "d", "view/font_decrease.png", "app.gui.cmds.view.font_decrease;");
		Add_btn(Key_view_page_read, "Page Read", "r", "view/page_read.png", "app.gui.cmds.view.page_read;");
		Add_btn(Key_view_page_edit, "Page Edit", "e", "view/page_edit.png", "app.gui.cmds.view.page_edit;");
		Add_btn(Key_view_page_html, "Page Html", "h", "view/page_html.png", "app.gui.cmds.view.page_html;");
		Add_btn(Key_history_go_bwd, "Back", "b", "history/go_bwd.png", "app.gui.cmds.history.go_bwd;");
		Add_btn(Key_history_go_fwd, "Forward", "f", "history/go_fwd.png", "app.gui.cmds.history.go_fwd;");
		Add_btn(Key_history_show, "Show", "s", "history/show.png", "app.gui.cmds.history.show;");
		Add_btn(Key_bookmarks_goto_main_page, "Main Page of current wiki", "m", "bookmarks/goto_main_page.png", "app.gui.cmds.bookmarks.goto_main_page;");
		Add_btn(Key_bookmarks_add, "Bookmark this page", "b", "bookmarks/add.png", "app.gui.cmds.bookmarks.add;");
		Add_btn(Key_bookmarks_show_all, "Show all bookmarks", "s", "bookmarks/show.png", "app.gui.cmds.bookmarks.show;");
		Add_btn(Key_tools_options, "Options", "o", "tools/options.png", "app.gui.cmds.bookmarks.goto(\"home/wiki/Help:Options\");");
		Add_btn(Key_tools_wikis_import_from_list, "Import from list", "i", "", "app.gui.cmds.bookmarks.goto(\"home/wiki/Help:Import/List\");");
		Add_btn(Key_tools_wikis_import_from_script, "Import from script", "s", "", "app.gui.cmds.bookmarks.goto(\"home/wiki/Help:Import/Script\");");
		Add_btn(Key_tools_wikis_maintenance, "Wiki maintenance", "s", "", "app.gui.cmds.bookmarks.goto(\"home/wiki/Help:Wiki_maintenance\");");
		Add_btn(Key_help_help, "XOWA Help", "h", "help/contents.png", "app.gui.cmds.bookmarks.goto(\"home/wiki/Help:Contents\");");
		Add_btn(Key_help_change_log, "Change log", "l", "", "app.gui.cmds.bookmarks.goto(\"home/wiki/Help:Change_log\");");
		Add_btn(Key_help_diagnostics, "Diagnostics", "d", "", "app.gui.cmds.bookmarks.goto(\"home/wiki/Help:Diagnostics\");");
		Add_btn(Key_help_context_menu, "Configure menus", "c", "", "app.gui.cmds.bookmarks.goto(\"home/wiki/Help:Options/Menus\");");
		Add_btn(Key_help_about, "About XOWA", "a", "help/about.png", "app.gui.cmds.bookmarks.goto(\"home/wiki/Help:About\");");
	}
	void Add_grp(String key, String text, String shortcut, String img)				{Add_itm(Xog_mnu_itm.Tid_grp, key, text, shortcut, img, "");}
	void Add_btn(String key, String text, String shortcut, String img, String cmd)	{Add_itm(Xog_mnu_itm.Tid_btn, key, text, shortcut, img, cmd);}
	void Add_itm(byte tid, String key, String text, String shortcut, String img, String cmd) {
		Xog_mnu_itm itm = new Xog_mnu_itm(tid, key).Init(text, shortcut, img, cmd);
		hash.Add(key, itm);
	}
	private static final String 
		  Key_file = "xowa.file", Key_edit = "xowa.edit", Key_view = "xowa.view", Key_history = "xowa.history", Key_bookmarks = "xowa.bookmarks"
		, Key_tools = "xowa.tools", Key_tools_wikis = "xowa.tools.wikis", Key_help = "xowa.help"
		, Key_file_save_as = "xowa.file.save_as", Key_file_print = "xowa.file.print", Key_file_exit = "xowa.file.exit"
		, Key_edit_select_all = "xowa.edit.select_all", Key_edit_copy = "xowa.edit.copy", Key_edit_find = "xowa.edit.find"
		, Key_view_font_increase = "xowa.view.font.increase", Key_view_font_decrease = "xowa.view.font.decrease"
		, Key_view_page_read = "xowa.view.page.read", Key_view_page_edit = "xowa.view.page.edit", Key_view_page_html = "xowa.view.page.html"
		, Key_history_go_bwd = "xowa.history.go_bwd", Key_history_go_fwd = "xowa.history.go_fwd"
		, Key_history_show = "xowa.history.show"
		, Key_bookmarks_goto_main_page = "xowa.bookmarks.goto_main_page"
		, Key_bookmarks_add = "xowa.bookmarks.add"
		, Key_bookmarks_show_all = "xowa.bookmarks.show"
		, Key_tools_options = "xowa.tools.options"
		, Key_tools_wikis_import_from_list = "xowa.tools.wikis.import_from_list"
		, Key_tools_wikis_import_from_script = "xowa.tools.wikis.import_from_script"
		, Key_tools_wikis_maintenance = "xowa.tools.wikis.maintenance"
		, Key_help_help = "xowa.help.help"
		, Key_help_about = "xowa.help.about"
		, Key_help_change_log = "xowa.help.change_log"
		, Key_help_diagnostics = "xowa.help.diagnostics"
		, Key_help_context_menu = "xowa.help.context_menu"
		;
	public static final Xog_mnu_defaults _ = new Xog_mnu_defaults();
	public static final String Html_box_mnu = String_.Concat_lines_nl
	(	"add_btn_default('xowa.history.go_bwd');"
	,	"add_btn_default('xowa.history.go_fwd');"
	,	"add_spr;"
	,	"add_btn_default('xowa.edit.select_all');"
	,	"add_btn_default('xowa.edit.copy');"
	,	"add_spr;"
	,	"add_btn_default('xowa.edit.find');"
	,	"add_spr;"
	,	"add_btn_default('xowa.view.font.increase');"
	,	"add_btn_default('xowa.view.font.decrease');"
	,	"add_spr;"
	,	"add_grp_default('xowa.file') {"
	,	"  add_btn_default('xowa.file.save_as');"
//		,	"  add_btn_default('xowa.file.print');"
	,	"  add_btn_default('xowa.file.exit');"
	,	"}"
	,	"add_grp_default('xowa.view') {"
	,	"  add_btn_default('xowa.view.page.read');"
	,	"  add_btn_default('xowa.view.page.edit');"
	,	"  add_btn_default('xowa.view.page.html');"
	,	"}"
	,	"add_grp_default('xowa.history') {"
	,	"  add_btn_default('xowa.history.show');"
	,	"}"
	,	"add_grp_default('xowa.bookmarks') {"
	,	"  add_btn_default('xowa.bookmarks.goto_main_page');"
	,	"  add_spr;"
	,	"  add_btn_default('xowa.bookmarks.add');"
	,	"  add_btn_default('xowa.bookmarks.show');"
	,	"}"
	,	"add_grp_default('xowa.tools') {"
	,	"  add_btn_default('xowa.tools.options');"
	,	"  add_spr;"
	,	"  add_grp_default('xowa.tools.wikis') {"
	,	"    add_btn_default('xowa.tools.wikis.import_from_list');"
	,	"    add_btn_default('xowa.tools.wikis.import_from_script');"
	,	"    add_spr;"
	,	"    add_btn_default('xowa.tools.wikis.maintenance');"
	,	"  }"
	,	"}"
	,	"add_grp_default('xowa.help') {"
	,	"  add_btn_default('xowa.help.help');"
	,	"  add_btn_default('xowa.help.change_log');"
	,	"  add_btn_default('xowa.help.diagnostics');"
	,	"  add_btn_default('xowa.help.context_menu');"
	,	"  add_spr;"
	,	"  add_btn_default('xowa.help.about');"
	,	"}"
	);
	public static final String Main_win_mnu = String_.Concat_lines_nl
	(	"add_grp_default('xowa.file').img_('') {"
	,	"  add_btn_default('xowa.file.save_as');"
//		,	"  add_btn_default('xowa.file.print');"
	,	"  add_btn_default('xowa.file.exit');"
	,	"}"
	,	"add_grp_default('xowa.edit').img_('') {"
	,	"  add_btn_default('xowa.edit.select_all');"
	,	"  add_btn_default('xowa.edit.copy');"
	,	"  add_spr;"
	,	"  add_btn_default('xowa.edit.find');"
	,	"}"
	,	"add_grp_default('xowa.view').img_('') {"
	,	"  add_btn_default('xowa.view.font.increase');"
	,	"  add_btn_default('xowa.view.font.decrease');"
	,	"  add_spr;"
	,	"  add_btn_default('xowa.view.page.read');"
	,	"  add_btn_default('xowa.view.page.edit');"
	,	"  add_btn_default('xowa.view.page.html');"
	,	"}"
	,	"add_grp_default('xowa.history').img_('') {"
	,	"  add_btn_default('xowa.history.go_bwd');"
	,	"  add_btn_default('xowa.history.go_fwd');"
	,	"  add_spr;"
	,	"  add_btn_default('xowa.history.show');"
	,	"}"
	,	"add_grp_default('xowa.bookmarks').img_('') {"
	,	"  add_btn_default('xowa.bookmarks.goto_main_page');"
	,	"  add_spr;"
	,	"  add_btn_default('xowa.bookmarks.add');"
	,	"  add_btn_default('xowa.bookmarks.show');"
	,	"}"
	,	"add_grp_default('xowa.tools').img_('') {"
	,	"  add_btn_default('xowa.tools.options');"
	,	"  add_spr;"
	,	"  add_grp_default('xowa.tools.wikis') {"
	,	"    add_btn_default('xowa.tools.wikis.import_from_list');"
	,	"    add_btn_default('xowa.tools.wikis.import_from_script');"
	,	"    add_spr;"
	,	"    add_btn_default('xowa.tools.wikis.maintenance');"
	,	"  }"
	,	"}"
	,	"add_grp_default('xowa.help').img_('') {"
	,	"  add_btn_default('xowa.help.help');"
	,	"  add_btn_default('xowa.help.change_log');"
	,	"  add_btn_default('xowa.help.diagnostics');"
	,	"  add_btn_default('xowa.help.context_menu');"
	,	"  add_spr;"
	,	"  add_btn_default('xowa.help.about');"
	,	"}"
	);
}

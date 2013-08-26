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
import org.junit.*; import gplx.xowa.xtns.wdatas.*;
public class Xoa_xowa_exec_wdata_labels_tst {
	@Before public void init() {fxt.Init();} Wdata_wiki_mgr_fxt fxt = new Wdata_wiki_mgr_fxt();
	@Test   public void Basic() {
		fxt.Init_pages_add(fxt.page_bldr_("q1").Label_add("en", "en_q1").Xto_page_doc());
		fxt.Init_pages_add(fxt.page_bldr_("q2").Label_add("en", "en_q2").Xto_page_doc());
		fxt.Init_pages_add(fxt.page_bldr_("Property:P1").Label_add("en", "en_property_p1").Xto_page_doc());
		Tst_wikidata_label_get(String_.Ary("en", "q1", "q2", "Property:P1"), String_.Ary("en_q1", "en_q2", "en_property_p1"));
	}
	@Test   public void Outliers() {
		fxt.Init_pages_add(fxt.page_bldr_("q1").Label_add("en", "en_q1").Label_add("de", "de_q1").Xto_page_doc());
		Tst_wikidata_label_get(String_.Ary("fr", "q1"), String_.Ary((String)null));
		Tst_wikidata_label_get(String_.Ary("de", "q1"), String_.Ary("de_q1"));
		Tst_wikidata_label_get(String_.Ary("xowa_title", "q1"), String_.Ary("q1"));
		Tst_wikidata_label_get(String_.Ary("xowa_ui_lang", "q1"), String_.Ary("en_q1"));
		Tst_wikidata_label_get(String_.Ary("fr;de", "q1"), String_.Ary("de_q1"));
	}
	void Tst_wikidata_label_get(String[] args, String[] expd) {
		Xoa_xowa_exec exec = fxt.App().Gui_mgr().Main_win().Js_cbk();
		GfoMsg msg = GfoMsg_.new_cast_(Xoa_xowa_exec.Invk_wikidata_get_label);
		int args_len = args.length;
		for (int i = 0; i < args_len; i++)
			msg.Add("v", args[i]);
		String[] actl = (String[])GfoInvkAble_.InvkCmd_msg(exec, Xoa_xowa_exec.Invk_wikidata_get_label, msg);
		Tfds.Eq_ary_str(expd, actl);
	}
}

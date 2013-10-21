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
package gplx.xowa.xtns.dynamicPageList; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Dpl_xnde_tst {
	Xtn_dynamic_page_list_fxt fxt = new Xtn_dynamic_page_list_fxt();
	@Before public void init() {fxt.Clear();}
	@Test   public void Ctg() {
		fxt.Ctg_create("Ctg_0", "B", "A");
		fxt.Ul_pages("<DynamicPageList>category=Ctg_0</DynamicPageList>", fxt.Ul(Itm_html_null, "B", "A"));
	}
//		@Test   public void Ctg_multiple() {
//			fxt.Ctg_create("Ctg_0", "B", "A");
//			fxt.Ctg_create("Ctg_1", "A");
//			fxt.Ul_pages(String_.Concat_lines_nl
//			(	"<DynamicPageList>"
//			,	"category=Ctg_0"
//			,	"category=Ctg_1"
//			,	"</DynamicPageList>"
//			), fxt.Ul(Itm_html_null, "A"));
//		}
	@Test  public void Ctg_ascending() {
		fxt.Ctg_create("Ctg_0", "B", "A");
		fxt.Ul_pages(String_.Concat_lines_nl_skipLast
		(	"<DynamicPageList>"
		,	"category=Ctg_0"
		,	"order=ascending"
		,	"</DynamicPageList>"), fxt.Ul(Itm_html_null, "A", "B"));
	}
	@Test  public void Ctg_descending() {
		fxt.Ctg_create("Ctg_0", "A", "B");
		fxt.Ul_pages(String_.Concat_lines_nl_skipLast
		(	"<DynamicPageList>"
		,	"category=Ctg_0"
		,	"order=descending"
		,	"</DynamicPageList>"), fxt.Ul(Itm_html_null, "B", "A"));
	}
	@Test  public void Nofollow() {
		fxt.Ctg_create("Ctg_0", "A", "B");
		fxt.Ul_pages(String_.Concat_lines_nl_skipLast
		(	"<DynamicPageList>"
		,	"category=Ctg_0"
		,	"nofollow=true"
		,	"</DynamicPageList>"), fxt.Ul(" rel=\"nofollow\"", "A", "B"));
	}
	@Test  public void Invalid_key() {
		fxt.Ctg_create("Ctg_0", "A", "B");
		fxt.Warns("unknown_key: page=Test page key=invalid_key");
		fxt.Ul_pages(String_.Concat_lines_nl_skipLast
		(	"<DynamicPageList>"
		,	"invalid_key=invalid_val"
		,	"category=Ctg_0"
		,	"</DynamicPageList>"), fxt.Ul(Itm_html_null, "A", "B"));
	}
	@Test  public void No_results() {
		fxt.Ul_pages(String_.Concat_lines_nl_skipLast
		(	"<DynamicPageList>"
		,	"category=Ctg_0"
		,	"</DynamicPageList>"), "Error: No results!");
	}
	@Test  public void Suppress_errors() {
		fxt.Ul_pages(String_.Concat_lines_nl_skipLast
		(	"<DynamicPageList>"
		,	"category=Ctg_0"
		,	"suppresserrors=true"
		,	"</DynamicPageList>"), "");
	}
	@Test  public void Count() {
		fxt.Ctg_create("Ctg_0", "A", "B", "C");
		fxt.Ul_pages(String_.Concat_lines_nl_skipLast
		(	"<DynamicPageList>"
		,	"category=Ctg_0"
		,	"count=2"
		,	"</DynamicPageList>"), fxt.Ul(Itm_html_null, "A", "B"));
	}
	@Test  public void Offset() {
		fxt.Ctg_create("Ctg_0", "A", "B", "C");
		fxt.Ul_pages(String_.Concat_lines_nl_skipLast
		(	"<DynamicPageList>"
		,	"category=Ctg_0"
		,	"offset=2"
		,	"count=2"
		,	"</DynamicPageList>"), fxt.Ul(Itm_html_null, "C"));
	}
	
//	@Test  public void Ns() {
//		fxt.Page_create("Talk:A");
//		fxt.Ul_pages(String_.Concat_lines_nl_skipLast
//		(	"<DynamicPageList>"
//		,	"namespace=Talk"
//		,	"</DynamicPageList>"), fxt.Ul(Itm_html_null, "Talk:A"));
//	}
	private static final String Itm_html_null = null;
}
class Xtn_dynamic_page_list_fxt {	
	private Xop_fxt fxt = new Xop_fxt();
	public void Clear() {
		fxt.Reset();
		warns = String_.Ary_empty;
		fxt.App().Usr_dlg().Ui_wkr().Clear();
		fxt.Wiki().Hive_mgr().Clear();
//			fxt.Wiki().Db_mgr().Load_mgr().Clear();	// clear regys else earlier ctgs will remain
		Io_mgr._.InitEngine_mem();
	}
	public void Warns(String... v) {warns = v;} private String[] warns;
	public void Page_create(String page) {fxt.ini_page_create(page);}
	public void Ctg_create(String ctg, String... pages) {
		int pages_len = pages.length;
		int[] page_ids = new int[pages_len];
		for (int i = 0; i < pages_len; i++) {
			String page = pages[i];
			int page_id = i;
			Xoa_ttl page_ttl = Xoa_ttl.parse_(fxt.Wiki(), ByteAry_.new_utf8_(page));
			Xoa_page page_obj = fxt.Wiki().Data_mgr().Get_page(page_ttl, false);
			if (page_obj.Missing()) {
				fxt.ini_page_create(page);
				fxt.ini_id_create (page_id, 0, 0, false, 5, Xow_ns_.Id_main, page);
			}
			else
				page_id = page_obj.Page_id();
			page_ids[i] = page_id;
		}
		fxt.ini_ctg_create(ctg, page_ids);
	}
	public String Ul(String itm_html, String... pages) {
		bfr.Add("<ul>").Add_line_nl();
		int pages_len = pages.length;
		for (int i = 0; i < pages_len; i++) {
			String page = pages[i];
			bfr.Add("  <li><a href=\"/wiki/").Add(page).Add("\" title=\"").Add(page).Add("\"");
			if (itm_html != null) bfr.Add(itm_html);
			bfr.Add(">").Add(page).Add("</a></li>").Add_line_nl();
		}
		bfr.Add("</ul>").Add_line_nl();
		return bfr.XtoStrAndClear();
	}
	public void Ul_pages(String raw, String expd) {
		fxt.tst_Parse_page_wiki_str(raw, expd);
		fxt.tst_Warn(warns);
	}	StringBldr bfr = new StringBldr();
}

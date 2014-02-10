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
package gplx.xowa.gui.wins; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
import org.junit.*;
public class Xog_url_wkr_tst {
	@Before public void init() {fxt.Clear();} private Xog_url_wkr_fxt fxt = new Xog_url_wkr_fxt();
	@Test  public void term() {fxt.Clear();}
	@Test  public void Basic() {
		fxt.Init_exec("file:///wiki/A").Expd_tid_(Xoh_href.Tid_wiki).Expd_page_("A").Test();
	}
	@Test  public void Basic_question() {
		fxt.Init_exec("file:///wiki/A?").Expd_tid_(Xoh_href.Tid_wiki).Expd_page_("A?").Expd_qargs_("").Test();
	}
	@Test  public void Basic_question_text() {
		fxt.Init_exec("file:///wiki/A?B").Expd_tid_(Xoh_href.Tid_wiki).Expd_page_("A?B").Expd_qargs_("").Test();
	}
	@Test  public void Redirect() {
		fxt.Init_exec("file:///wiki/A?redirect=no").Expd_tid_(Xoh_href.Tid_wiki).Expd_page_("A").Expd_qargs_("?redirect=no").Test();
	}
	@Test  public void Search() {
		fxt.Init_exec("file:///wiki/Special:Search/Moon%3Ffulltext%3Dy%26xowa_page_index%3D1").Expd_tid_(Xoh_href.Tid_wiki).Expd_page_("Special:Search/Moon").Expd_qargs_("?fulltext=y&xowa_page_index=1").Test();
	}
	@Test  public void Ctg() {
		fxt.Init_exec("file:///wiki/Category:A?pagefrom=A#mw-pages").Expd_tid_(Xoh_href.Tid_wiki).Expd_page_("Category:A").Expd_qargs_("?pagefrom=A").Expd_anchor_("mw-pages").Test();
	}
	@Test  public void Xwiki() {
		fxt.App().Wiki_mgr().Get_by_key_or_make(ByteAry_.new_ascii_("en.wiktionary.org")).Ns_mgr().Ns_main().Case_match_(Xow_ns_.Case_match_all);
		fxt.Init_exec("file:///site/en.wiktionary.org/wiki/a").Expd_tid_(Xoh_href.Tid_site).Expd_wiki_("en.wiktionary.org").Expd_page_("a").Test();
	}
	@Test  public void Xwiki_site_sidebar() {// PURPOSE: make sure sidebar links don't fail; DATE:2014-01-21
		fxt.Init_exec("file:///site/en.wikipedia.org/wiki/A").Expd_tid_(Xoh_href.Tid_site).Expd_wiki_("en.wikipedia.org").Expd_page_("A").Test();
	}
	@Test  public void Xwiki_site_logo() {	// DATE:2014-01-21
		fxt.Init_exec("file:///site/en.wikipedia.org/wiki/").Expd_tid_(Xoh_href.Tid_site).Expd_wiki_("en.wikipedia.org").Expd_page_("Main_Page").Test();
		fxt.Init_exec("file:///site/en.wikipedia.org/wiki").Expd_tid_(Xoh_href.Tid_site).Expd_wiki_("en.wikipedia.org").Expd_page_("Main_Page").Test();
		fxt.Init_exec("file:///site/en.wikipedia.org/").Expd_tid_(Xoh_href.Tid_site).Expd_wiki_("en.wikipedia.org").Expd_page_("Main_Page").Test();
	}
}
class Xog_url_wkr_fxt {
	private Xoa_app app; private Xow_wiki wiki;
	private Xog_win win;
	private Xog_url_wkr url_wkr = new Xog_url_wkr();
	private String init_raw;
	public Xoa_app App() {return app;}
	public Xog_url_wkr_fxt Expd_tid_(byte v) {expd_tid = v; return this;} private byte expd_tid;
	public Xog_url_wkr_fxt Expd_wiki_(String v) {expd_wiki = v; return this;} private String expd_wiki;
	public Xog_url_wkr_fxt Expd_page_(String v) {expd_page = v; return this;} private String expd_page;
	public Xog_url_wkr_fxt Expd_anchor_(String v) {expd_anchor = v; return this;} private String expd_anchor;
	public Xog_url_wkr_fxt Expd_qargs_(String v) {expd_qargs = v; return this;} private String expd_qargs;
	public void Clear() {
		app = Xoa_app_fxt.app_();
		wiki = Xoa_app_fxt.wiki_tst_(app);
		win = app.Gui_mgr().Main_win();
		win.Page_(new Xoa_page(wiki, Xoa_ttl.parse_(wiki, ByteAry_.new_utf8_("test"))));	// TODO: remove unnecessary page init
		expd_wiki = expd_page = expd_qargs = expd_anchor = null;
	}
	public Xog_url_wkr_fxt Init_exec(String raw) {
		this.init_raw = raw;
		return this;
	}
	public void Test() {
		Xoa_url url = url_wkr.Parse(win, init_raw).Exec();
		Tfds.Eq(expd_tid, url_wkr.Href_tid());
		Tfds.Eq(expd_page, String_.new_utf8_(url.Page_bry()));
		if (expd_wiki != null)			Tfds.Eq(expd_wiki	, String_.new_utf8_(url.Wiki_bry()));
		if (expd_anchor != null)		Tfds.Eq(expd_anchor	, String_.new_utf8_(url.Anchor_bry()));
		if (expd_qargs != null)			Tfds.Eq(expd_qargs	, String_.new_utf8_(url.Args_all_as_bry()));
	}
}

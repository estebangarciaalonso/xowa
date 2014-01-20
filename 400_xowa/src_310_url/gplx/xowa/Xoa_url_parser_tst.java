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
public class Xoa_url_parser_tst {		
	@Before public void init() {fxt.Reset();} private Xoa_url_parser_chkr fxt = new Xoa_url_parser_chkr();
	@Test  public void Ttl() {
		fxt.Reset().Raw_("http://en.wikipedia.org/w/index.php?title=A").Page_("A").tst_();
	}
	@Test  public void Title_remove_w() {	// PURPOSE: fix /w/ showing up as seg 
		fxt.Reset().Raw_("http://en.wikipedia.org/w/index.php?title=A").Page_("A").tst_app();
	}
	@Test  public void Basic() {
		fxt.Reset().Raw_("en.wikipedia.org/wiki/A").Wiki_("en.wikipedia.org").Page_("A").tst_app();
	}
	@Test  public void Abrv() {	// deprecate; no longer needed with shortcuts
		fxt.Reset().Raw_("en.wikipedia.org/A").Wiki_("en.wikipedia.org").Page_("A").tst_app();
	}
	@Test  public void Http_basic() {
		fxt.Reset().Raw_("http://en.wikipedia.org/wiki/A").Wiki_("en.wikipedia.org").Page_("A").tst_app();
	}
	@Test  public void Relative() {
		fxt.Reset().Raw_("//en.wikipedia.org/wiki/A").Wiki_("en.wikipedia.org").Page_("A").tst_app();
	}
	@Test  public void Name() {
		fxt.Reset().Raw_("A").Wiki_("en.wikipedia.org").Page_("A").tst_app();
	}
	@Test  public void Sub_1() {
		fxt.Reset().Raw_("A/b").Wiki_("en.wikipedia.org").Page_("A/b").tst_app();
	}
	@Test  public void Sub_2() {
		fxt.Reset().Raw_("A/b/c").Wiki_("en.wikipedia.org").Page_("A/b/c").tst_app();
	}
	@Test  public void Sub_3() {
		fxt.Reset().Raw_("en.wikipedia.org/wiki/A/b").Wiki_("en.wikipedia.org").Page_("A/b").tst_app();
	}
	@Test  public void Ns_category() {
		fxt.Reset().Raw_("Category:A").Wiki_("en.wikipedia.org").Page_("Category:A").tst_app();
	}
	@Test  public void Ns_file() {
		fxt.Reset().Raw_("File:A").Wiki_("en.wikipedia.org").Page_("File:A").tst_app();
	}
	@Test  public void Anchor() {
		fxt.Reset().Raw_("A#b").Wiki_("en.wikipedia.org").Page_("A").Anchor_("b").tst_app();
	}
	@Test  public void Upload() { 
		fxt.App().User().Wiki().Xwiki_mgr().Add_full("commons.wikimedia.org", "commons.wikimedia.org");
		fxt.Reset().Raw_("http://upload.wikimedia.org/wikipedia/commons/a/ab/C.svg").Wiki_("commons.wikimedia.org").Page_("File:C.svg").tst_app();
		fxt.Reset().Raw_("http://upload.wikimedia.org/wikipedia/commons/thumb/7/70/A.png/220px-A.png").Wiki_("commons.wikimedia.org").Page_("File:A.png").tst_app();
	}
	@Test  public void Parse_lang() {
		Xow_xwiki_mgr xwiki_mgr = fxt.Wiki().Xwiki_mgr();
		xwiki_mgr.Add_full(ByteAry_.new_ascii_("fr"), ByteAry_.new_ascii_("fr.wikipedia.org"), ByteAry_.new_ascii_("http://fr.wikipedia.org/~{0}"));
		fxt.Reset().Raw_("http://en.wikipedia.org/wiki/fr:A").Wiki_("fr.wikipedia.org").Page_("A").tst_app();
	}
	@Test  public void Alias_wiki() {
		Xow_xwiki_mgr xwiki_mgr = fxt.Wiki().Xwiki_mgr();
		xwiki_mgr.Add_full(ByteAry_.new_ascii_("s"), ByteAry_.new_ascii_("en.wikisource.org"));
		fxt.Reset().Raw_("s:A/b/c").Wiki_("en.wikisource.org").Page_("A/b/c").tst_app();
	}
	@Test  public void Domain_only() {
		fxt.App().User().Wiki().Xwiki_mgr().Add_full("fr.wikipedia.org", "fr.wikipedia.org");
		fxt.Reset().Raw_("fr.wikipedia.org").Wiki_("fr.wikipedia.org").Page_("Main_Page").tst_app();
	}
	@Test  public void Domain_and_wiki() {
		fxt.App().User().Wiki().Xwiki_mgr().Add_full("fr.wikipedia.org", "fr.wikipedia.org");
		fxt.Reset().Raw_("fr.wikipedia.org/wiki").Wiki_("fr.wikipedia.org").Page_("Main_Page").tst_app();
	}
	@Test  public void Domain_and_wiki_w_http() {
		fxt.App().User().Wiki().Xwiki_mgr().Add_full("fr.wikipedia.org", "fr.wikipedia.org");
		fxt.Reset().Raw_("http://fr.wikipedia.org/wiki").Wiki_("fr.wikipedia.org").Page_("Main_Page").tst_app();
	}		
	@Test  public void Redirect() {
		fxt.Reset().Raw_("A?redirect=no").Wiki_("en.wikipedia.org").Page_("A").tst_app();
	}
	@Test  public void Namespace_in_different_wiki() {	// PURPOSE.fix: namespaced titles would default to default_wiki instead of current_wiki
		fxt.Reset().Raw_("Category:A").Wiki_("en.wikisource.org").Page_("Category:A").tst_app(fxt.Wiki_wikisource());
	}		
	@Test  public void Action_is_edit() {
		fxt.Reset().Raw_("A?action=edit").Wiki_("en.wikipedia.org").Page_("A").Action_is_edit_y_().tst_app();
	}
	@Test  public void Assert_state_cleared() {	// PURPOSE.fix: action_is_edit (et. al.) was not being cleared on parse even though Xoa_url reused; DATE:20121231
		Xoa_url url = new Xoa_url();
		byte[] raw = ByteAry_.new_ascii_("A?action=edit");
		Xoa_url_parser.Parse_url(url, fxt.App(), fxt.Wiki(), raw, 0, raw.length);
		Tfds.Eq(true, url.Action_is_edit());
		raw = ByteAry_.new_ascii_("B");
		Xoa_url_parser.Parse_url(url, fxt.App(), fxt.Wiki(), raw, 0, raw.length);
		Tfds.Eq(false, url.Action_is_edit());
	}
	@Test  public void Query_arg() {	// PURPOSE.fix: query args were not printing out
		Xoa_url url = new Xoa_url();
		byte[] raw = ByteAry_.new_ascii_("en.wikipedia.org/wiki/Special:Search/Earth?fulltext=yes");
		Xoa_url_parser.Parse_url(url, fxt.App(), fxt.Wiki(), raw, 0, raw.length);
		Xoa_url_parser parser = new Xoa_url_parser();
		Tfds.Eq("en.wikipedia.org/wiki/Special:Search/Earth?fulltext=yes", parser.Build_str(url));
	}
	@Test   public void Anchor_with_slash() {	// PURPOSE: A/b#c/d was not parsing correctly
		fxt.Reset().Raw_("A/b#c/d").Page_("A/b").Anchor_("c.2Fd").tst_app();
	}
	@Test  public void Slash() {
		fxt.Reset().Raw_("en.wikipedia.org/wiki//A").Wiki_("en.wikipedia.org").Page_("/A").tst_app();
		fxt.Reset().Raw_("en.wikipedia.org/wiki/A//b").Wiki_("en.wikipedia.org").Page_("A//b").tst_app();
		fxt.Reset().Raw_("en.wikipedia.org/wiki///A").Wiki_("en.wikipedia.org").Page_("//A").tst_app();
	}
	@Test  public void Question_is_page() {
		fxt.Reset().Raw_("A?B").Wiki_("en.wikipedia.org").Page_("A?B").Anchor_(null).tst_app();
	}
	@Test  public void Question_is_anchor() {
		fxt.Reset().Raw_("A#b?c").Wiki_("en.wikipedia.org").Page_("A").Anchor_("b.3Fc").tst_app();
	}
}
class Xoa_url_parser_chkr implements Tst_chkr {
	Xoa_url_parser parser;
	public Xoa_app App() {return app;} private Xoa_app app;
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public Xow_wiki Wiki_wikisource() {return wiki_wikisource;} private Xow_wiki wiki_wikisource;
	public Class<?> TypeOf() {return Xoa_url.class;}
	public Xoa_url_parser_chkr Wiki_(String v) 					{this.wiki_str = v; return this;} private String wiki_str;
	public Xoa_url_parser_chkr Page_(String v) 					{this.page = v; return this;} private String page;
	public Xoa_url_parser_chkr Anchor_(String v) 				{this.anchor = v; return this;} private String anchor;
	public Xoa_url_parser_chkr Action_is_edit_y_() 				{this.anchor_is_edit = Bool_.Y_byte; return this;} private byte anchor_is_edit = Bool_.__byte;
	public Xoa_url_parser_chkr Action_is_edit_n_() 				{this.anchor_is_edit = Bool_.N_byte; return this;}
	public Xoa_url_parser_chkr Reset() {
		if (app == null) {
			parser = new Xoa_url_parser();
			app = Xoa_app_fxt.app_();
			GfoInvkAble_.InvkCmd_val(app.User(), gplx.xowa.users.Xou_user.Invk_default_wiki_, en_wikipedia_name);
			wiki = Xoa_app_fxt.wiki_(app, "en.wikipedia.org");
			wiki_wikisource = Xoa_app_fxt.wiki_(app, "en.wikisource.org");
			app.User().Wiki().Xwiki_mgr().Add_full("en.wikipedia.org", "en.wikipedia.org");
			app.User().Wiki().Xwiki_mgr().Add_full("en.wikisource.org", "en.wikisource.org");
		}
		wiki_str = page = anchor = null;
		anchor_is_edit = Bool_.__byte;
		return this;
	}
	private static final byte[] en_wikipedia_name = ByteAry_.new_utf8_("en.wikipedia.org");
	ByteAryAry_chkr bry_ary_chkr = new ByteAryAry_chkr();
	public int Chk(Tst_mgr mgr, String path, Object actl_obj) {
		Xoa_url actl = (Xoa_url)actl_obj;
		int rv = 0;
		rv += mgr.Tst_val(wiki_str == null, path, "wiki", wiki_str, String_.new_utf8_(actl.Wiki_bry()));
		rv += mgr.Tst_val(page == null, path, "page", page, String_.new_utf8_(actl.Page_bry()));
		rv += mgr.Tst_val(anchor == null, path, "anchor", anchor, String_.new_utf8_(actl.Anchor_bry()));
		rv += mgr.Tst_val(anchor_is_edit == Bool_.__byte, path, "anchor_is_edit", anchor_is_edit == Bool_.Y_byte, actl.Action_is_edit());
		return rv;
	}
	public Xoa_url_parser_chkr Raw_(String v) 				{this.raw = v; return this;} private String raw;
	public void tst_() {
		byte[] bry = ByteAry_.new_utf8_(raw);
		Xoa_url url = new Xoa_url();
		parser.Parse(url, bry);
		Tst_mgr tst_mgr = new Tst_mgr();
		tst_mgr.Tst_obj(this, url);
	}
	public void tst_app() {tst_app(wiki);}
	public void tst_app(Xow_wiki w) {
		Xoa_url url = Xoa_url_parser.Parse_url(app, w, raw);
		Tst_mgr tst_mgr = new Tst_mgr();
		tst_mgr.Tst_obj(this, url);
	}
}

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
package gplx.xowa.xtns.gallery; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
import gplx.xowa.html.*; import gplx.xowa.files.fsdb.*;
public class Gallery_mgr_base_basic_tst {
	private Gallery_mgr_base_fxt fxt = new Gallery_mgr_base_fxt();
	@Before public void init() {fxt.Reset();}
	@Test  public void Basic() {
		fxt.Test_html_str(String_.Concat_lines_nl_skip_last
		( "<gallery widths=200px heights=300px>"
		, "A.png|''a1''"
		, "B.png|''b1''"
		, "</gallery>"
		), String_.Concat_lines_nl_skip_last
		( "<ul id=\"xowa_gallery_ul_0\" class=\"gallery mw-gallery-traditional\">"
		, "  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width: 235px\">"
		, "    <div style=\"width: 235px\">"
		, "      <div class=\"thumb\" style=\"width: 230px;\">"
		, "        <div style=\"margin:15px auto;\">"
		, "          <a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/200px.png\" width=\"200\" height=\"300\" /></a>"
		, "        </div>"
		, "      </div>"
		, "      <div class=\"gallerytext\"><p><i>a1</i>"
		, "</p>"
		, ""
		, "      </div>"
		, "    </div>"
		, "  </li>"
		, "  <li id=\"xowa_gallery_li_1\" class=\"gallerybox\" style=\"width: 235px\">"
		, "    <div style=\"width: 235px\">"
		, "      <div class=\"thumb\" style=\"width: 230px;\">"
		, "        <div style=\"margin:15px auto;\">"
		, "          <a href=\"/wiki/File:B.png\" class=\"image\" xowa_title=\"B.png\"><img id=\"xowa_file_img_1\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/5/7/B.png/200px.png\" width=\"200\" height=\"300\" /></a>"
		, "        </div>"
		, "      </div>"
		, "      <div class=\"gallerytext\"><p><i>b1</i>"
		, "</p>"
		, ""
		, "      </div>"
		, "    </div>"
		, "  </li>"
		, "</ul>"
		));
		fxt.Test_html_modules_js("");
	}
	@Test  public void Tmpl() {
		fxt.Fxt().Init_defn_add("test_tmpl", "b");
		fxt.Test_html_frag("<gallery>File:A.png|a{{test_tmpl}}c</gallery>", "<div class=\"gallerytext\"><p>abc\n</p>");
	}
	@Test  public void Itm_defaults_to_120() {
		fxt.Test_html_frag("<gallery>File:A.png|a</gallery>", "<img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />");
	}
	@Test   public void Height_fix() {
		fxt.Fxt().Wiki().File_mgr().Cfg_set(Xof_fsdb_mgr_cfg.Grp_xowa, Xof_fsdb_mgr_cfg.Key_gallery_fix_defaults, "y");
		fxt.Test_html_frag("<gallery heights=250>File:A.png|a<br/>c</gallery>", " width=\"120\" height=\"250\"");
		fxt.Fxt().Wiki().File_mgr().Cfg_set(Xof_fsdb_mgr_cfg.Grp_xowa, Xof_fsdb_mgr_cfg.Key_gallery_fix_defaults, "n");
	}
	@Test   public void Alt() {
		fxt.Test_html_frag("<gallery>File:A.png|b|alt=c</gallery>"
		, "<img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
		, "<div class=\"gallerytext\"><p>b\n</p>"
		);
	}
	@Test   public void Link() {
		fxt.Test_html_frag("<gallery>File:A.png|b|link=c</gallery>", "<a href=\"/wiki/C\" class=\"image\"");
	}
	@Test   public void Alt_caption_multiple() {
		fxt.Test_html_frag("<gallery>File:A.png|alt=b|c[[d|e]]f</gallery>", "<div class=\"gallerytext\"><p>c<a href=\"/wiki/D\">ef</a>\n</p>");
	}
	@Test   public void Alt_escape_quote() {
		fxt.Test_html_frag("<gallery>File:A.png|b|alt=c\"d'e</gallery>", "alt=\"c&quot;d'e\"");
	}
	@Test   public void Caption_null() {	// PURPOSE: null caption causes page to fail; EX: de.w:Lewis Caroll; <gallery>Datei:A.png</gallery>; DATE:2013-10-09
		fxt.Test_html_frag("<gallery>File:A.png</gallery>", "<div class=\"gallerytext\">\n");
	}
	@Test   public void Ttl_has_no_ns() {	// PURPOSE: MW allows ttl to not have ns; DATE: 2013-11-18
		fxt.Test_html_frag("<gallery>A.png|b</gallery>", "<img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />");	// make sure image is generated
	}
	@Test   public void Ref() {	// PURPOSE: <ref> inside <gallery> was not showing up in <references>; DATE:2013-10-09
		fxt.Test_html_frag("<gallery>File:A.png|<ref name='a'>b</ref></gallery><references/>"
		, "<div class=\"gallerytext\"><p><sup id=\"cite_ref-a_0-0\" class=\"reference\"><a href=\"#cite_note-a-0\">[1]</a></sup>\n</p>"
		, String_.Concat_lines_nl
		( "</ul><ol class=\"references\">"
		, "<li id=\"cite_note-a-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-a_0-0\">^</a></span> <span class=\"reference-text\">b</span></li>"
		, "</ol>"
		)
		);
	}
	@Test   public void Packed() {
		fxt.Test_html_frag("<gallery mode=packed heights=300px>File:A.png|a</gallery>", "<ul id=\"xowa_gallery_ul_0\" class=\"gallery mw-gallery-packed\">");
		fxt.Test_html_modules_js(String_.Concat_lines_nl_skip_last
		( ""
		, "  <script type='text/javascript'>"
		, "    var xowa_global_values = {"
		, "      'gallery-packed-enabled' : true,"
		, "    }"
		, "  </script>"
		));
	}
	@Test   public void Missing() {
		fxt.Init_files_missing_y_();
		fxt.Test_html_frag("<gallery>File:A.png|b</gallery>", "<div class=\"thumb\" style=\"height: 150px;\">A.png</div>");
	}
	@Test   public void Multiple() {	// PURPOSE.bug: multiple galleries should not use same gallery super; DATE:2014-04-13
		fxt.Test_html_frag("<gallery>File:A.png|a</gallery><gallery widths=180px>File:B.png|b</gallery>"
		, "src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"	// should not be 180px from gallery #2
		);
	}
	@Test   public void Link_is_empty() {	// PURPOSE: "link=" causes null pointer exception; DATE:2014-06-15
		fxt.Test_html_frag("<gallery>File:A.png|link=</gallery>", "href=\"/wiki/File:A.png\"");
	}
	@Test   public void Dangling_autcloses() {	// PURPOSE: dangling gallery should auto-close, not escape; PAGE:en.w:Wikipedia:Featured_pictures_thumbs_43 DATE:2014-08-23
		fxt.Test_html_frag("<gallery>File:A.png|b", "<ul id=\"xowa_gallery_ul_0\"");
	}
//		@Test  public void Ttl_caption() {	// TODO: category entries get rendered with name only (no ns)
//			fxt.Test_html_frag
//			( "<gallery>Category:A</gallery>"
//			, "<li class='gallerycaption'>B</li>"
//			);
//		}
}
class Gallery_mgr_base_fxt {
	public void Reset() {
		fxt.Wiki().Xtn_mgr().Init_by_wiki(fxt.Wiki());
		Gallery_mgr_base.File_found_mode = Bool_.Y_byte;
	}
	public Xop_fxt Fxt() {return fxt;} private Xop_fxt fxt = new Xop_fxt();
	public void Init_files_missing_y_() {
		Gallery_mgr_base.File_found_mode = Bool_.N_byte;
	}
	public void Test_html_str(String raw, String expd)						{fxt.Test_html_full_str(raw, expd);}
	public void Test_html_frag(String raw, String... expd_frags)		{fxt.Test_html_full_frag(raw, expd_frags);}	// TODO: change to wiki_str; currently uids do not get reset if wiki
	public void Test_html_frag_n(String raw, String... expd_frags)	{fxt.Test_html_full_frag_n(raw, expd_frags);}
	public void Test_html_modules_js(String expd)							{
		fxt.Page().Html_data().Module_mgr().Itm_globals().Enabled_n_();
		fxt.Test_html_modules_js(expd);
	}
}

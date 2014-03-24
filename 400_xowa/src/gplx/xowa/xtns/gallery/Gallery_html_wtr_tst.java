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
public class Gallery_html_wtr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {fxt.Reset(); fxt.Wiki().Xtn_mgr().Init_by_wiki(fxt.Wiki());}
	@Test   public void Basic() {
		Init_html();
		fxt.Test_parse_page_wiki_str("<gallery perrow=2 widths=200px heights=300px>File:A.png|a<br/>c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:486px; _width:486px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:235px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:235px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:230px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/200px.png\" width=\"200\" height=\"300\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">a<br/>c"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
	}
	@Test   public void Tmpl() {
		fxt.Init_defn_clear();
		fxt.Init_defn_add("test_tmpl", "b");
		Init_html();
		fxt.Test_parse_page_wiki_str("<gallery>File:A.png|a{{test_tmpl}}c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:1304px; _width:1304px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">abc"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
		fxt.Init_defn_clear();
	}
	@Test   public void Item_defaults_to_120() {
		Init_html();
		fxt.Test_parse_page_wiki_str("<gallery perrow=3>File:A.png|a<br/>c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:489px; _width:489px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">a<br/>c"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
	}
	@Test   public void Height_fix() {
		fxt.Wiki().File_mgr().Cfg_set(Gallery_xnde.Fsdb_cfg_grp, Gallery_xnde.Fsdb_cfg_key_gallery_fix_defaults, "y");
		try {
		Init_html();
		fxt.Test_parse_page_wiki_str("<gallery heights=250>File:A.png|a<br/>c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:1304px; _width:1304px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/-1px.png\" width=\"0\" height=\"250\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">a<br/>c"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
		}
		finally {
			fxt.Wiki().File_mgr().Cfg_set(Gallery_xnde.Fsdb_cfg_grp, Gallery_xnde.Fsdb_cfg_key_gallery_fix_defaults, "n");
		}
	}
	@Test   public void Alt() {
		fxt.Test_parse_page_wiki_str("<gallery>File:A.png|b|alt=c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:1304px; _width:1304px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">b"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
	}
	@Test   public void Link() {
		fxt.Test_parse_page_wiki_str("<gallery>File:A.png|b|link=c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:1304px; _width:1304px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/C\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">b"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
	}
	@Test   public void Alt_caption_multiple() {
		fxt.Test_parse_page_wiki_str("<gallery>File:A.png|alt=b|c[[d|e]]f</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:1304px; _width:1304px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"b\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">c<a href=\"/wiki/D\">ef</a>"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
	}
	@Test   public void Alt_escape_quote() {
		fxt.Test_parse_page_wiki_str("<gallery>File:A.png|b|alt=c\"d'e</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:1304px; _width:1304px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"c&quot;d'e\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">b"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
	}
	@Test   public void Caption_null() {	// PURPOSE: null caption causes page to fail; EX: de.w:Lewis Caroll; <gallery>Datei:A.png</gallery>; DATE:2013-10-09
		Init_html();
		fxt.Test_parse_page_wiki_str("<gallery>File:A.png</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:1304px; _width:1304px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
	}
	@Test   public void Ttl_has_no_ns() {	// PURPOSE: MW allows ttl to not have ns; DATE: 2013-11-18
		Init_html();
		fxt.Test_parse_page_wiki_str("<gallery>A.png|b</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:1304px; _width:1304px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">b"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
	}
	@Test   public void Ref() {	// PURPOSE: <ref> inside <gallery> was not showing up in <references>; DATE:2013-10-09
		Init_html();
		fxt.Test_parse_page_wiki_str("<gallery>File:A.png|<ref name='a'>b</ref></gallery><references/>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:1304px; _width:1304px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\"><sup id=\"cite_ref-a_0-0\" class=\"reference\"><a href=\"#cite_note-a-0\">[1]</a></sup>"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul><ol class=\"references\">"
		,	"<li id=\"cite_note-a-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-a_0-0\">^</a></span> <span class=\"reference-text\">b</span></li>"
		,	"</ol>"
		));
	}
	@Test   public void Packed() {
		Init_html();
		fxt.Test_parse_page_wiki_str("<gallery mode=packed heights=300px>File:A.png|a<br/>c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery mw-gallery-packed\" style=\"max-width:1304px; _width:1304px;\">"
		,	"  <li id=\"xowa_gallery_li_0\" class=\"gallerybox\" style=\"width:155px;\">"
		,	"    <div id=\"xowa_gallery_div1_0\" style=\"width:155px;\">"
		,	"      <div id=\"xowa_gallery_div2_0\" class=\"thumb\" style=\"width:150px;\">"
		,	"        <div id=\"xowa_gallery_div3_0\" style=\"margin:15px auto;\">"
		,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
		,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"300\" />"
		,	"          </a>"
		,	"        </div>"
		,	"      </div>"
		,	"      <div class=\"gallerytext\">a<br/>c"
		,	"      </div>"
		,	"    </div>"
		,	"  </li>"
		,	"</ul>"
		));
	}

	private void Init_html() {
		Io_mgr._.InitEngine_mem();	// clear out mem files
		Io_url rootDir = Io_url_.mem_dir_("mem").GenSubDir_nest(Xoa_app_.Name);
		fxt.App().Fsys_mgr().Temp_dir_(rootDir.OwnerDir().GenSubDir("tmp"));
	}
}

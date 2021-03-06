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
package gplx.xowa.html; import gplx.*; import gplx.xowa.*;
import org.junit.*;
public class Xoh_lnki_file_wtr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {fxt.Reset();}
	@Test  public void Img_full() {	// PURPOSE: full with title was outputting invalid html; DATE:2013-12-31
		fxt.Wtr_cfg().Lnki_title_(true);
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.png]]"
		,	String_.Concat_lines_nl_skipLast
		(	"<a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"	// NOTE: used to output class=\"image\"A.png 
		));
		fxt.Wtr_cfg().Lnki_title_(false);
	}
	@Test  public void Img_embed() {
		fxt.Test_parse_page_wiki_str("[[File:A.png|9x8px|alt=abc]]", Xop_fxt.html_img_none("File:A.png", "abc", "file:///mem/wiki/repo/trg/thumb/7/0/A.png/9px.png", "A.png"));
	}
	@Test  public void Img_none() {	// NOTE: floatnone is WP behavior; MW omits div tag
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.png|none|20x30px|b]]"
		,	String_.Concat_lines_nl_skipLast
		(	"<div class=\"floatnone\">"
		,	"<a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"b\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/20px.png\" width=\"20\" height=\"30\" /></a></div>"
		));
	}
	@Test  public void Img_thumb_none() {
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.png|thumb|none|b]]"
		,	Img_thumb_str("none")
		);
	}
	@Test  public void Img_thumb_ltr() {
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.png|thumb|b]]"
		,	Img_thumb_str("right")
		);
	}
	@Test  public void Img_thumb_rtl() {
		fxt.Wiki().Lang().Dir_ltr_(false);
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.png|thumb|b]]"
		,	Img_thumb_str("left")
		);
		fxt.Wiki().Lang().Dir_ltr_(true);
	}
	private String Img_thumb_str(String align) {
		return	String_.Concat_lines_nl_skipLast
		(	"<div class=\"thumb t" + align + "\">"
		,	"  <div id=\"xowa_file_div_0\" class=\"thumbinner\" style=\"width:220px;\">"
		,	"    <a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/220px.png\" width=\"0\" height=\"0\" /></a>"
		,	"    <div class=\"thumbcaption\">"
		,	"      <div class=\"magnify\">"
		,	"        <a href=\"/wiki/File:A.png\" class=\"internal\" title=\"Enlarge\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      b"
		,	"    </div>"
		,	"  </div>"
		,	"</div>"
		,	""
		);
	}
	@Test  public void Img_frame() {	// PURPOSE: lnki with "frame" is same as thumb; DATE:2013-12-23
		fxt.Test_parse_page_wiki_str
			(	"[[File:A.png|frame|220x110px|b]]"
			,	String_.Concat_lines_nl_skipLast
			(	"<div class=\"thumb tright\">"
			,	"  <div id=\"xowa_file_div_0\" class=\"thumbinner\" style=\"width:220px;\">"
			,	"    <a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/220px.png\" width=\"220\" height=\"110\" /></a>"
			,	"    <div class=\"thumbcaption\">"
			,	"      <div class=\"magnify\">"
			,	"        <a href=\"/wiki/File:A.png\" class=\"internal\" title=\"Enlarge\">"
			,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
			,	"        </a>"
			,	"      </div>"
			,	"      b"
			,	"    </div>"
			,	"  </div>"
			,	"</div>"
			,	""
			));
	}
	@Test  public void Border() {
		fxt.Test_parse_page_wiki_str
			(	"[[File:A.png|border]]"
			,	"<a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" class=\"thumbborder\" /></a>");
	}
	@Test  public void Lnki_full_video() {
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.ogv|400px|a|alt=b]]", String_.Concat_lines_nl_skipLast
		(	"    <div id=\"xowa_media_div\">"
		,	"      <div>"
		,	"        <a href=\"/wiki/File:A.ogv\" class=\"image\" title=\"A.ogv\">"
		,	"          <img id=\"xowa_file_img_0\" src=\"\" width=\"400\" height=\"0\" alt=\"b\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      <div>"
		,	"        <a id=\"xowa_file_play_0\" href=\"file:///mem/wiki/repo/trg/orig/d/0/A.ogv\" xowa_title=\"A.ogv\" class=\"xowa_anchor_button\" style=\"width:398px;max-width:400px;\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/play.png\" width=\"22\" height=\"22\" alt=\"Play sound\" />"
		,	"        </a>"
		,	"      </div>"
		,	"    </div>"
		));		
	}
	@Test  public void Lnki_full_video_ogg() {// PURPOSE: ogg should default to video on first load; otherwise dynamic-update won't be able to put in thumb
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.ogg|400px|a|alt=b]]", String_.Concat_lines_nl_skipLast
		(	"    <div id=\"xowa_media_div\">"
		,	"      <div>"
		,	"        <a href=\"/wiki/File:A.ogg\" class=\"image\" title=\"A.ogg\">"
		,	"          <img id=\"xowa_file_img_0\" src=\"\" width=\"400\" height=\"0\" alt=\"b\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      <div>"
		,	"        <a id=\"xowa_file_play_0\" href=\"file:///mem/wiki/repo/trg/orig/4/2/A.ogg\" xowa_title=\"A.ogg\" class=\"xowa_anchor_button\" style=\"width:398px;max-width:400px;\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/play.png\" width=\"22\" height=\"22\" alt=\"Play sound\" />"
		,	"        </a>"
		,	"      </div>"
		,	"    </div>"
		));		
	}
	@Test  public void Lnki_full_audio() {
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.oga|noicon]]", String_.Concat_lines_nl_skipLast
		(	"    <div id=\"xowa_media_div\">"
		,	"      <div>"
		,	"        <a id=\"xowa_file_play_0\" href=\"file:///mem/wiki/repo/trg/orig/4/f/A.oga\" xowa_title=\"A.oga\" class=\"xowa_anchor_button\" style=\"width:218px;max-width:1024px;\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/play.png\" width=\"22\" height=\"22\" alt=\"Play sound\" />"
		,	"        </a>"
		,	"      </div>"
		,	"    </div>"
		,	"    <div class=\"thumbcaption\">"
		,	"      "
		,	"    </div>"
		));		
	}
	@Test  public void Lnki_full_svg() {
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.svg|a|alt=b]]", String_.Concat_lines_nl_skipLast
		(	"<a href=\"/wiki/File:A.svg\" class=\"image\" xowa_title=\"A.svg\"><img id=\"xowa_file_img_0\" alt=\"b\" src=\"file:///mem/wiki/repo/trg/thumb/7/5/A.svg/-1px.png\" width=\"0\" height=\"0\" /></a>"	// HACK: tries to get orig_w which is not available
		));		
	}
	@Test  public void Lnki_caption_nested_media() { // EX.WP:Beethoven;
		fxt.Test_parse_page_wiki_str("[[File:A.png|thumb|b [[Media:A.ogg]] c]]", String_.Concat_lines_nl_skipLast
		(	"<div class=\"thumb tright\">"
		,	"  <div id=\"xowa_file_div_0\" class=\"thumbinner\" style=\"width:220px;\">"
		,	"    <a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/220px.png\" width=\"0\" height=\"0\" /></a>"
		,	"    <div class=\"thumbcaption\">"
		,	"      <div class=\"magnify\">"
		,	"        <a href=\"/wiki/File:A.png\" class=\"internal\" title=\"Enlarge\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      b <a href=\"file:///mem/wiki/repo/trg/orig/4/2/A.ogg\" xowa_title=\"A.ogg\">"
		,	"</a> c"
		,	"    </div>"
		,	"  </div>"
		,	"</div>"
		,	""
		));
	}
	@Test  public void Lnki_thumb_audio() {
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.oga|thumb|a|alt=b]]", String_.Concat_lines_nl_skipLast
		(	"<div class=\"thumb tright\">"
		,	"  <div id=\"xowa_file_div_0\" class=\"thumbinner\" style=\"width:220px;\">"
		,	"    <div id=\"xowa_media_div\">"
		,	"      <div>"
		,	"        <a id=\"xowa_file_play_0\" href=\"file:///mem/wiki/repo/trg/orig/4/f/A.oga\" xowa_title=\"A.oga\" class=\"xowa_anchor_button\" style=\"width:218px;max-width:1024px;\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/play.png\" width=\"22\" height=\"22\" alt=\"Play sound\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      <div>"
		,	"        <a href=\"/wiki/File:A.oga\" class=\"image\" title=\"About this file\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/info.png\" width=\"22\" height=\"22\" />"
		,	"        </a>"
		,	"      </div>"
		,	"    </div>"
		,	"    <div class=\"thumbcaption\">"
		,	"      <div class=\"magnify\">"
		,	"        <a href=\"/wiki/File:A.oga\" class=\"internal\" title=\"Enlarge\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      a"
		,	"    </div>"
		,	"    <hr/>"
		,	"    <div class=\"thumbcaption\">"
		,	"b"
		,	"    </div>"
		,	"  </div>"
		,	"</div>"
		,	""
		));		
	}
	@Test  public void Lnki_thumb_audio_noicon() {
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.oga|thumb|noicon|a|alt=b]]", String_.Concat_lines_nl_skipLast
		(	"    <div id=\"xowa_media_div\">"
		,	"      <div>"
		,	"        <a id=\"xowa_file_play_0\" href=\"file:///mem/wiki/repo/trg/orig/4/f/A.oga\" xowa_title=\"A.oga\" class=\"xowa_anchor_button\" style=\"width:218px;max-width:1024px;\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/play.png\" width=\"22\" height=\"22\" alt=\"Play sound\" />"
		,	"        </a>"
		,	"      </div>"
		,	"    </div>"
		,	"    <div class=\"thumbcaption\">"
		,	"      a"
		,	"    </div>"
		,	"    <hr/>"
		,	"    <div class=\"thumbcaption\">"
		,	"b"
		,	"    </div>"
		));		
	}
	@Test  public void Lnki_thumb_video() {
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.ogv|thumb|400px|a|alt=b]]", String_.Concat_lines_nl_skipLast
		(	"<div class=\"thumb tright\">"
		,	"  <div id=\"xowa_file_div_0\" class=\"thumbinner\" style=\"width:400px;\">"
		,	"    <div id=\"xowa_media_div\">"
		,	"      <div>"
		,	"        <a href=\"/wiki/File:A.ogv\" class=\"image\" title=\"A.ogv\">"
		,	"          <img id=\"xowa_file_img_0\" src=\"\" width=\"400\" height=\"0\" alt=\"b\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      <div>"
		,	"        <a id=\"xowa_file_play_0\" href=\"file:///mem/wiki/repo/trg/orig/d/0/A.ogv\" xowa_title=\"A.ogv\" class=\"xowa_anchor_button\" style=\"width:398px;max-width:400px;\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/play.png\" width=\"22\" height=\"22\" alt=\"Play sound\" />"
		,	"        </a>"
		,	"      </div>"
		,	"    </div>"
		,	"    <div class=\"thumbcaption\">"
		,	"      <div class=\"magnify\">"
		,	"        <a href=\"/wiki/File:A.ogv\" class=\"internal\" title=\"Enlarge\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      a"
		,	"    </div>"
		,	"    <hr/>"
		,	"    <div class=\"thumbcaption\">"
		,	"b"
		,	"    </div>"
		,	"  </div>"
		,	"</div>"
		,	""
		));		
	}
	@Test  public void Lnki_thumb_webm() {	// PURPOSE: webm thumb wasn't being shown; DATE:2014-01-25
		fxt.Test_parse_page_wiki_str
		(	"[[File:A.webm|thumb|400px|a|alt=b]]", String_.Concat_lines_nl_skipLast
		(	"<div class=\"thumb tright\">"
		,	"  <div id=\"xowa_file_div_0\" class=\"thumbinner\" style=\"width:400px;\">"
		,	"    <div id=\"xowa_media_div\">"
		,	"      <div>"
		,	"        <a href=\"/wiki/File:A.webm\" class=\"image\" title=\"A.webm\">"
		,	"          <img id=\"xowa_file_img_0\" src=\"\" width=\"400\" height=\"0\" alt=\"b\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      <div>"
		,	"        <a id=\"xowa_file_play_0\" href=\"file:///mem/wiki/repo/trg/orig/3/4/A.webm\" xowa_title=\"A.webm\" class=\"xowa_anchor_button\" style=\"width:398px;max-width:400px;\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/play.png\" width=\"22\" height=\"22\" alt=\"Play sound\" />"
		,	"        </a>"
		,	"      </div>"
		,	"    </div>"
		,	"    <div class=\"thumbcaption\">"
		,	"      <div class=\"magnify\">"
		,	"        <a href=\"/wiki/File:A.webm\" class=\"internal\" title=\"Enlarge\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      a"
		,	"    </div>"
		,	"    <hr/>"
		,	"    <div class=\"thumbcaption\">"
		,	"b"
		,	"    </div>"
		,	"  </div>"
		,	"</div>"
		,	""
		));		
	}
	@Test  public void Lnki_media_normal() {
		fxt.Test_parse_page_wiki_str("[[Media:A.png|b]]", String_.Concat_lines_nl_skipLast
		(	"<a href=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" xowa_title=\"A.png\">b"
		,	"</a>"
		));
	}
	@Test  public void Lnki_media_literal() {
		fxt.Test_parse_page_wiki_str("[[:Media:A.ogg|b]]", String_.Concat_lines_nl_skipLast
		(	"<a href=\"file:///mem/wiki/repo/trg/orig/4/2/A.ogg\" xowa_title=\"A.ogg\">b"
		,	"</a>"
		));
	}
	@Test  public void Lnki_media_literal_pdf() {
		fxt.Wiki().Html_mgr().Img_suppress_missing_src_(true);	// simulate missing file; DATE:2014-01-30
		fxt.Test_parse_page_wiki_str("[[Media:A.pdf|b]]", String_.Concat_lines_nl_skipLast
		(	"<a href=\"file:///mem/wiki/repo/trg/orig/e/f/A.pdf\" xowa_title=\"A.pdf\">b"
		,	"</a>"
		));
		Tfds.Eq(0, fxt.Page().File_queue().Count());			// make sure media does not add to queue
		fxt.Wiki().Html_mgr().Img_suppress_missing_src_(false);
	}
	@Test  public void Lnki_file_alt_link() {	// PURPOSE: lnki in caption should not create alt="b<a href="c">cd</a>"
		fxt.Test_parse_page_wiki_str("[[File:A.png|thumb|alt=b [[c]] d]]", String_.Concat_lines_nl_skipLast
		(	"<div class=\"thumb tright\">"
		,	"  <div id=\"xowa_file_div_0\" class=\"thumbinner\" style=\"width:220px;\">"
		,	"    <a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"b c d\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/220px.png\" width=\"0\" height=\"0\" /></a>"
		,	"    <div class=\"thumbcaption\">"
		,	"      <div class=\"magnify\">"
		,	"        <a href=\"/wiki/File:A.png\" class=\"internal\" title=\"Enlarge\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/magnify-clip.png\" width=\"15\" height=\"11\" alt=\"\" />"
		,	"        </a>"
		,	"      </div>"
		,	"      "
		,	"    </div>"
		,	"    <hr/>"
		,	"    <div class=\"thumbcaption\">"
		,	"b <a href=\"/wiki/C\">c</a> d"
		,	"    </div>"
		,	"  </div>"
		,	"</div>"
		,	""
		));
	}
	@Test  public void Lnki_full_audio_ogg() {// PURPOSE: ogg should show src on first load
		fxt.Wiki().Html_mgr().Img_suppress_missing_src_(true);	// simulate release-mode wherein missing images will not have src
		fxt.Test_parse_page_all_str
		(	"[[File:A.ogg]]", String_.Concat_lines_nl_skipLast
		(	"    <div id=\"xowa_media_div\">"
		,	"      <div>"
		,	"        <a href=\"/wiki/File:A.ogg\" class=\"image\" title=\"A.ogg\">"
		,	"          <img id=\"xowa_file_img_0\" src=\"file:///mem/wiki/repo/trg/orig/4/2/A.ogg\" width=\"220\" height=\"-1\" alt=\"\" />"	// note that src still exists (needed for clicking)
		,	"        </a>"
		,	"      </div>"
		,	"      <div>"
		,	"        <a id=\"xowa_file_play_0\" href=\"file:///mem/wiki/repo/trg/orig/4/2/A.ogg\" xowa_title=\"A.ogg\" class=\"xowa_anchor_button\" style=\"width:218px;max-width:220px;\">"
		,	"          <img src=\"file:///mem/xowa/user/test_user/app/img/file/play.png\" width=\"22\" height=\"22\" alt=\"Play sound\" />"
		,	"        </a>"
		,	"      </div>"
		,	"    </div>"
		));
		fxt.Wiki().Html_mgr().Img_suppress_missing_src_(false);
	}
	@Test  public void Img_title() {
		fxt.Wtr_cfg().Lnki_title_(true);
		Tst_img_title("[[File:A.png|frameless|a b]]", "a b");
		Tst_img_title("[[File:A.png|thumb|a b]]", "Enlarge");	// caption should not replace text
		fxt.Wtr_cfg().Lnki_title_(false);
	}
	@Test  public void Lnki_alt_is_text() {	// PURPOSE: (a) alt should default to caption; (b) alt should not show html chars (like <a src=")
		fxt.Wtr_cfg().Lnki_title_(true);
		fxt.Test_parse_page_all_str
		(	"[[File:A.png|a[[b]]c]]"
		,	"<a href=\"/wiki/File:A.png\" class=\"image\" title=\"a\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"abc\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"
		);
		fxt.Wtr_cfg().Lnki_title_(false);
	}
	@Test  public void Alt_ignore_apos() {// PURPOSE: alt should ignore apos; EX: [[File:A.png|''A'']] should have alt of A; DATE:2013-10-25
		fxt.Wtr_cfg().Lnki_title_(true);
		fxt.Test_parse_page_all_str
			(	"[[File:A.png|''b'']]"
			,	"<a href=\"/wiki/File:A.png\" class=\"image\" title=\"b\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"b\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"
			);
		fxt.Wtr_cfg().Lnki_title_(false);
	}
	@Test  public void Alt_ignore_lnke() {// PURPOSE: alt should ignore lnke
		fxt.Wtr_cfg().Lnki_title_(true);
		fxt.Test_parse_page_all_str
			(	"[[File:A.png|b[http://c.org d] e]]"
			,	"<a href=\"/wiki/File:A.png\" class=\"image\" title=\"bd e\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"bd e\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"
			);
		fxt.Wtr_cfg().Lnki_title_(false);
	}
	@Test  public void Alt_ignore_list() {// PURPOSE: alt should ignore list
		fxt.Wtr_cfg().Lnki_title_(true);
		fxt.Test_parse_page_all_str
		(	"[[File:A.png|b\n*c]]"
		,	"<a href=\"/wiki/File:A.png\" class=\"image\" title=\"bc\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"b*c\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"
		);
		fxt.Wtr_cfg().Lnki_title_(false);
	}
	@Test  public void Alt_ignore_tblw() {// PURPOSE: alt should ignore tblw
		fxt.Wtr_cfg().Lnki_title_(true);
		fxt.Test_parse_page_all_str
			(	"[[File:A.png|\n{|\n|-\n|b\n|}\n]]"
			,	"<a href=\"/wiki/File:A.png\" class=\"image\" title=\"b&#10;\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"   b  \" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"
			);
		fxt.Wtr_cfg().Lnki_title_(false);
	}
	@Test  public void Alt_ignore_para() {// PURPOSE: alt should ignore para
		fxt.Wtr_cfg().Lnki_title_(true);
		fxt.Init_para_y_();
		fxt.Test_parse_page_all_str
			(	"[[File:A.png|b\nc]]"
			,	String_.Concat_lines_nl
			(	"<p><a href=\"/wiki/File:A.png\" class=\"image\" title=\"b&#10;c\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"b c\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"
			,	"</p>"
			));
		fxt.Init_para_n_();
		fxt.Wtr_cfg().Lnki_title_(false);
	}
	@Test  public void Lnki_empty_alt_is_omitted() {// PURPOSE: empty alt should be ignored; DATE:2013-07-30
		fxt.Wtr_cfg().Lnki_title_(true);
		fxt.Test_parse_page_all_str
		(	"[[File:A.png|a|alt=]]"
		,	"<a href=\"/wiki/File:A.png\" class=\"image\" title=\"a\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"
		);
		fxt.Wtr_cfg().Lnki_title_(false);
	}
	@Test   public void Href_anchor_leading_space() {	// PURPOSE: space before anchor should be preserved, not " " -> "#"
		fxt.Test_parse_page_all_str("[[A #b]]", "<a href=\"/wiki/A#b\">A #b</a>");
	}
	@Test   public void Href_anchor_leading_space_ns() {	// PURPOSE: same as above, but with ns; DATE:2013-08-29
		fxt.Test_parse_page_all_str("[[Help:A   #b]]", "<a href=\"/wiki/Help:A#b\">Help:A #b</a>");
	}
	@Test   public void Href_anchor_leading_ns_lc() {	// PURPOSE: same as above but with lc title
		fxt.Test_parse_page_all_str("[[Help:a#b]]", "<a href=\"/wiki/Help:A#b\">Help:A#b</a>");
	}
	@Test   public void Href_anchor_leading_space_ns_lc() {	// PURPOSE: same as above but with lc title
		fxt.Test_parse_page_all_str("[[Help:a #b]]", "<a href=\"/wiki/Help:A#b\">Help:A #b</a>");
	}
	@Test  public void Lnki_caption_nested_file() { // PURPOSE: nested lnki in caption breaks alt with html chars; EX:de.w:Wien; DATE:2013-12-16
		fxt.Wtr_cfg().Lnki_title_(true);
		fxt.Test_parse_page_wiki_str("[[File:A.png|none|[[File:B.png|20px|d]] c]]", String_.Concat_lines_nl_skipLast
			(	"<div class=\"floatnone\">"
			,	"<a href=\"/wiki/File:A.png\" class=\"image\" title=\" c\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"d c\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a></div>"
			,	""
			));
		fxt.Wtr_cfg().Lnki_title_(false);
	}
	private void Tst_img_title(String raw, String expd_ttl) {
		String actl = fxt.Exec_parse_page_wiki_as_str(raw);
		String actl_ttl = null;
		int title_bgn = String_.FindFwd(actl, " title=\"");
		if (title_bgn != String_.NotFound) {
			title_bgn += String_.Len(" title=\"");
			int title_end = String_.FindFwd(actl, "\"", title_bgn);
			if (title_end != String_.NotFound) actl_ttl = String_.Mid(actl, title_bgn, title_end);
		}
		Tfds.Eq(expd_ttl, actl_ttl, actl);
	}
}
//		@Test  public void Ogg() {
//			fxt.Src_en_wiki_repo().Ext_rules().Get_or_new(Xof_ext_.Bry_ogg).View_max_(0);
//			fxt	.ini_page_api("commons", "A.ogg", "", 0, 0);
//			fxt	.Lnki_orig_("A.ogg")
//				.Src(	)
//				.Trg(	
//						fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/4/42.csv", "A.ogg|z||2?0,0|0?0,0")
//					)
//				.tst();
//			fxt	.Lnki_orig_("A.ogg")
//			.Html_orig_src_("file:///mem/trg/en.wikipedia.org/raw/4/2/A.ogg")
//			.tst();
//			fxt.Src_en_wiki_repo().Ext_rules().Get_or_new(Xof_ext_.Bry_ogg).View_max_(-1);
//		}

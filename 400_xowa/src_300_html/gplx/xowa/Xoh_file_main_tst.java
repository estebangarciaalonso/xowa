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
import org.junit.*; import gplx.xowa.files.*;
public class Xoh_file_main_tst {
	Xoh_fil_main_fxt fxt = new Xoh_fil_main_fxt();
	@Before public void init() {fxt.Reset();}
	@Test  public void Image() {
		fxt.Ttl_str_("Test.png").Html_src_("mem/file/cur.png").Html_orig_src_("mem/file/orig.png").Html_w_(300).Html_h_(200).Html_file_size_(100)
		.tst(String_.Concat_lines_nl_skip_last
		(	Xoh_fil_main_fxt.Hdr
		,	"<div class=\"fullImageLink\" id=\"file\">"
		,	"  <a href=\"mem/file/orig.png\" xowa_title=\"Test.png\">"
		,	"    <img id=\"xowa_file_img_0\" alt=\"Test.png\" src=\"mem/file/cur.png\" width=\"300\" height=\"200\" />"
		,	"  </a>"
		,	"  <div class=\"mw-filepage-resolutioninfo\">Size of this preview: "
		,	"    <a href=\"mem/file/cur.png\" class=\"mw-thumbnail-link\">"
		,	"      300 × 200 pixels"
		,	"    </a>"
		,	"    ."
		,	"    <span class=\"mw-filepage-other-resolutions\">"
		,	"      Other resolutions:"
		,	""
		,	"    </span>"
		,	"  </div>"
		,	"</div>"
		,	"<div class=\"fullMedia\">"
		,	"  <a href=\"mem/file/orig.png\" class=\"internal\" title=\"Test.png\" xowa_title=\"Test.png\">"
		,	"    Full resolution"
		,	"  </a>"
		,	"  &#8206;"
		,	"  <span class=\"fileInfo\">"
		,	"    (0 × 0 pixels, file size: 100, MIME type: image/png)"
		,	"  </span>"
		,	"</div>"
		,	""		
		));
	}
	@Test  public void Audio() {
		fxt.Ttl_str_("Test.oga").Html_src_("mem/file/cur.oga").Html_orig_src_("mem/file/orig.oga").Html_w_(300).Html_h_(200).Html_file_size_(100)
		.tst(String_.Concat_lines_nl_skip_last
		(	Xoh_fil_main_fxt.Hdr
		,	"<div class=\"fullImageLink\" id=\"file\">"
		,	"  <div>"
		,	"    <a href=\"mem/file/orig.oga\" xowa_title=\"Test.oga\" class=\"xowa_anchor_button\" style=\"width:300px;max-width:300px;\">"
		,	"      <img src=\"file:///mem/xowa/user/test_user/app/img/file/play.png\" width=\"22\" height=\"22\" alt=\"Play sound\" />"
		,	"    </a>"
		,	"  </div>"
		,	"</div>"
		,	""		
		));
	}
	@Test  public void Video() {
		fxt.Ttl_str_("Test.ogv").Html_src_("mem/file/thumb.png").Html_orig_src_("mem/file/orig.ogv").Html_w_(300).Html_h_(200).Html_file_size_(100)
		.tst(String_.Concat_lines_nl_skip_last
		(	Xoh_fil_main_fxt.Hdr
		,	"<div class=\"fullImageLink\" id=\"file\">"
		,	"  <div>"
		,	"    <a href=\"mem/file/thumb.png\" class=\"image\" title=\"Test.ogv\">"
		,	"      <img id=\"xowa_file_img_0\" src=\"mem/file/thumb.png\" width=\"300\" height=\"200\" alt=\"\" />"
		,	"    </a>"
		,	"  </div>"
		,	"  <div>"
		,	"    <a href=\"mem/file/orig.ogv\" xowa_title=\"Test.ogv\" class=\"xowa_anchor_button\" style=\"width:300px;max-width:300px;\">"
		,	"      <img src=\"file:///mem/xowa/user/test_user/app/img/file/play.png\" width=\"22\" height=\"22\" alt=\"Play sound\" />"
		,	"    </a>"
		,	"  </div>"
		,	"</div>"
		,	""		
		));
	}
}
class Xoh_fil_main_fxt {
	Xoh_file_main_wkr wkr = new Xoh_file_main_wkr();
	Xoa_app app; Xow_wiki wiki; Xoh_file_page opt;
	Xof_xfer_itm file = new Xof_xfer_itm();
	Bry_bfr bfr = Bry_bfr.new_();	
	public Xoh_fil_main_fxt Ttl_str_(String v) {this.ttl_str = v; return this;} private String ttl_str;
	public Xoh_fil_main_fxt Html_src_(String v) {this.html_src = v; return this;} private String html_src;
	public Xoh_fil_main_fxt Html_orig_src_(String v) {this.html_orig_src = v; return this;} private String html_orig_src;
	public Xoh_fil_main_fxt Html_w_(int v) {this.html_w = v; return this;} private int html_w;
	public Xoh_fil_main_fxt Html_h_(int v) {this.html_h = v; return this;} private int html_h;
	public Xoh_fil_main_fxt Html_file_size_(int v) {this.html_file_size = v; return this;} private int html_file_size;
	public void Reset() {
		if (app != null) return;
		app = Xoa_app_fxt.app_();
		wiki = Xoa_app_fxt.wiki_tst_(app);
		opt = new Xoh_file_page();
	}
	public void tst(String expd) {
		byte[] ttl_bry = Bry_.new_utf8_(ttl_str);
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
		file.Init_for_test__img(html_w, html_h, Bry_.new_utf8_(html_src), Bry_.new_utf8_(html_orig_src));
		file.Set__ttl(ttl_bry, Bry_.Empty);
		wkr.Bld_html(wiki, bfr, file, ttl, opt, Bry_.XtoStrBytesByInt(html_file_size, 0), play_btn_icon, 0);	// TEST: must pass in elem_val b/c test only uses 2nd Bld_html while app uses 1st
		Tfds.Eq_str_lines(expd, bfr.Xto_str_and_clear());
	}	static final byte[] play_btn_icon = Bry_.new_ascii_("file:///mem/xowa/user/test_user/app/img/file/play.png");
	public static final String Hdr = String_.Concat_lines_nl_skip_last
		(	"<ul id=\"filetoc\">"
		,	"  <li>"
		,	"    <a href=\"#file\">"
		,	"      File"
		,	"    </a>"
		,	"  </li>"
		,	"  <li>"
		,	"    <a href=\"#filehistory\">"
		,	"      File history"
		,	"    </a>"
		,	"  </li>"
		,	"  <li>"
		,	"    <a href=\"#filelinks\">"
		,	"      File usage on Commons"
		,	"    </a>"
		,	"  </li>"
		,	"  <li>"
		,	"    <a href=\"#globalusage\">"
		,	"      File usage on other wikis"
		,	"    </a>"
		,	"  </li>"
		,	"</ul>"
	);
}


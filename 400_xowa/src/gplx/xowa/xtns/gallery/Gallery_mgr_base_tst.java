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
import gplx.xowa.html.*;
public class Gallery_mgr_base_tst {
	private Gallery_mgr_base_fxt fxt = new Gallery_mgr_base_fxt();
	@Before public void init() {fxt.Reset();}
	@Test  public void Basic() {
		fxt.Test_html_all(String_.Concat_lines_nl_skipLast
		( "<gallery widths=200px heights=300px>"
		, "A.png|''a1''"
		, "B.png|''b1''"
		, "</gallery>"
		), String_.Concat_lines_nl_skipLast
		( "<ul class=\"gallery mw-gallery-traditional\">"
		, "  <li class=\"gallerybox\" style=\"width: 235px\">"
		, "    <div style=\"width: 235px\">"
		, "      <div class=\"thumb\" style=\"width: 230px;\">"
		, "        <div style=\"margin:65px auto;\">"
		, "          <a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_1\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/200px.png\" width=\"200\" height=\"300\" /></a>"
		, "        </div>"
		, "      </div>"
		, "      <div class=\"gallerytext\"><p><i>a1</i>"
		, "</p>"
		, ""
		, "      </div>"
		, "    </div>"
		, "  </li>"
		, "  <li class=\"gallerybox\" style=\"width: 235px\">"
		, "    <div style=\"width: 235px\">"
		, "      <div class=\"thumb\" style=\"width: 230px;\">"
		, "        <div style=\"margin:65px auto;\">"
		, "          <a href=\"/wiki/File:B.png\" class=\"image\" xowa_title=\"B.png\"><img id=\"xowa_file_img_3\" alt=\"\" src=\"file:///mem/wiki/repo/trg/thumb/5/7/B.png/200px.png\" width=\"200\" height=\"300\" /></a>"
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
	}
	/*
	"<gallery id=a>File:A.png</gallery>"
	"<gallery style=b>File:A.png</gallery>"
	"<gallery caption=c>File:A.png</gallery>"
	"<gallery>Category:A</gallery>"	-> A
	"<gallery showfilename=true>A.png</gallery>"	-> A
	*/
}
class Gallery_mgr_base_fxt {
	private Xop_fxt fxt = new Xop_fxt();
	private ByteAryBfr bfr = ByteAryBfr.new_();
	public void Reset() {
		fxt.Wiki().Xtn_mgr().Init_by_wiki(fxt.Wiki());
	}
	public void Test_html_all(String raw, String expd) {
		Tfds.Eq_str_lines(expd, Parse_html(raw), raw);
	}
	private String Parse_html(String raw) {
		byte[] raw_bry = ByteAry_.new_utf8_(raw);
		Xop_root_tkn root = fxt.Exec_parse_page_all_as_root(raw_bry);
		Xop_xnde_tkn xnde = (Xop_xnde_tkn)root.Subs_get(0);
		Gallery_xnde gallery_xnde = (Gallery_xnde)xnde.Xnde_xtn();
		Gallery_mgr_base gallery_mgr = Gallery_mgr_traditional._;
		gallery_mgr.Init(gallery_xnde.Itms_per_row(), gallery_xnde.Itm_w(), gallery_xnde.Itm_h());
		gallery_mgr.Write_html(bfr, fxt.Wiki(), fxt.Page(), fxt.Ctx(), raw_bry, gallery_xnde);
		return bfr.XtoStrAndClear();
	}
}

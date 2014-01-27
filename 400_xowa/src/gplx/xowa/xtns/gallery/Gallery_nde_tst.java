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
public class Gallery_nde_tst {
	private Xop_fxt fxt = new Xop_fxt(); String raw_src;
	@Before public void init() {fxt.Reset(); fxt.Wiki().Xtn_mgr().Gallery().Parser().Init_by_wiki(fxt.Wiki());}
	@Test   public void Lnki_no_caption() {
		fxt.tst_Parse_page_wiki("<gallery>File:A.png</gallery>"
		,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
		(	new_chkr_gallery_mgr().Expd_subs_
			(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")
			)
		));
	}
	@Test   public void Lnki_1() {
		fxt.tst_Parse_page_wiki("<gallery>File:A.png|b</gallery>"
		,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
		(	new_chkr_gallery_mgr().Expd_subs_
			(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")
			)
		));
	}
	@Test   public void Lnki_3() {
		fxt.tst_Parse_page_wiki("<gallery>File:A.png|a\nFile:B.png|b\nFile:C.png|c</gallery>"
		,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
		(	new_chkr_gallery_mgr().Expd_subs_
			(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")
			,	new_chkr_gallery_itm().Expd_lnki_("File:B.png")
			,	new_chkr_gallery_itm().Expd_lnki_("File:C.png")
			)
		));
	}
	@Test   public void Ignore_newLines() {
		fxt.tst_Parse_page_wiki("<gallery>\n\n\nFile:A.png|a\n\n\nFile:B.png|b\n\n\n</gallery>"
		,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
		(	new_chkr_gallery_mgr().Expd_subs_
			(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")
			,	new_chkr_gallery_itm().Expd_lnki_("File:B.png")
			)
		));
	}
	@Test   public void Only_first_pipe() {
		fxt.tst_Parse_page_wiki("<gallery>File:A.png|File:B.png|cc</gallery>"
		,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
		(	new_chkr_gallery_mgr().Expd_subs_
			(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")
			)
		));
	}
	@Test   public void Invalid_lnki() {
		fxt.tst_Parse_page_wiki("<gallery>A.png|cc</gallery>"
		,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
		(	new_chkr_gallery_mgr().Expd_subs_
			(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")	// NOTE: MW converts "A.png" to "File:A.png"
			)
		));
	}
	@Test   public void File_only_trailing_nl() {
		fxt.tst_Parse_page_wiki("<gallery>File:A.png\n</gallery>"
		,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
		(	new_chkr_gallery_mgr().Expd_subs_
			(	new_chkr_gallery_itm().Expd_lnki_("File:A.png").Expd_caption_(null)
			)
		));
	}
	@Test   public void Invalid_curly() {
		raw_src = "a\n";			
		fxt.ini_Log_(Xop_ttl_log.Invalid_char).tst_Parse_page_wiki("<gallery>File:A.png|" + raw_src + "}}</gallery>"	// NOTE: }} is ignored since it is not a valid title
		,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
		(	new_chkr_gallery_mgr().Expd_subs_
			(	new_chkr_gallery_itm().Expd_lnki_("File:A.png").Expd_caption_("a")
			)
		));
	}
	@Test   public void Caption() {
		raw_src = "a<br/>c";
		fxt.tst_Parse_page_wiki("<gallery>File:A.png|" + raw_src + "</gallery>"
		,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
		(	new_chkr_gallery_mgr().Expd_subs_
			(	new_chkr_gallery_itm().Expd_lnki_("File:A.png").Expd_caption_(raw_src)
			)
		));
	}
	@Test   public void Xnde_atr() {
		raw_src = "<center>a<br/>b</center>";
		fxt.tst_Parse_page_wiki(String_.Concat_lines_nl_skipLast
		(	"<gallery perrow=3>"
		,	"File:A.jpg|" + raw_src
		,	"</gallery>"
		) ,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
			(	new_chkr_gallery_mgr().Expd_subs_
				(	new_chkr_gallery_itm().Expd_lnki_("File:A.jpg").Expd_caption_(raw_src)
				)
			));
	}
	@Test   public void Err_pre() {	// PURPOSE: leading ws was failing; EX.WP: Vlaardingen; "\nA.jpg| <center>Visbank</center>\n"
		raw_src = " <center>a</center>";
		fxt.tst_Parse_page_wiki(String_.Concat_lines_nl_skipLast
		(	"<gallery>"
		,	"File:A.jpg|" + raw_src
		,	"</gallery>"
		) ,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
			(	new_chkr_gallery_mgr().Expd_subs_
				(	new_chkr_gallery_itm().Expd_lnki_("File:A.jpg").Expd_caption_("<center>a</center>")
				)
			));
	}
	@Test   public void Err_comment() {	// PURPOSE: comment was being rendered; EX.WP: Perpetual motion; <!-- removed A.jpg|bcde -->
		raw_src = "b";
		fxt.ini_Log_(Xop_ttl_log.Comment_eos).tst_Parse_page_wiki(String_.Concat_lines_nl_skipLast
		(	"<gallery>"
		,	"<!-- deleted A.jpg|" + raw_src
		,	"</gallery>"
		) ,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
			(	new_chkr_gallery_mgr().Expd_subs_()
			)
		);
	}
	@Test   public void Html() {
		Init_html();
		fxt.tst_Parse_page_wiki_str("<gallery perrow=2 widths=200px heights=300px>File:A.png|a<br/>c</gallery>", String_.Concat_lines_nl_skipLast
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
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_tmpl", "b");
		Init_html();
		fxt.tst_Parse_page_wiki_str("<gallery>File:A.png|a{{test_tmpl}}c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
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
		fxt.ini_defn_clear();
	}
	@Test   public void Item_defaults_to_120() {
		Init_html();
		fxt.tst_Parse_page_wiki_str("<gallery perrow=3>File:A.png|a<br/>c</gallery>", String_.Concat_lines_nl_skipLast
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
		fxt.Wiki().File_mgr().Cfg_set(Gallery_nde.Fsdb_cfg_grp, Gallery_nde.Fsdb_cfg_key_gallery_fix_defaults, "y");
		try {
		Init_html();
		fxt.tst_Parse_page_wiki_str("<gallery heights=250>File:A.png|a<br/>c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
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
			fxt.Wiki().File_mgr().Cfg_set(Gallery_nde.Fsdb_cfg_grp, Gallery_nde.Fsdb_cfg_key_gallery_fix_defaults, "n");
		}
	}
	@Test   public void Alt() {
		fxt.tst_Parse_page_wiki_str("<gallery>File:A.png|b|alt=c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
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
		fxt.tst_Parse_page_wiki_str("<gallery>File:A.png|b|link=c</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
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
		fxt.tst_Parse_page_wiki_str("<gallery>File:A.png|alt=b|c[[d|e]]f</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
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
		fxt.tst_Parse_page_wiki_str("<gallery>File:A.png|b|alt=c\"d'e</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
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
		fxt.tst_Parse_page_wiki_str("<gallery>File:A.png</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
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
		fxt.tst_Parse_page_wiki_str("<gallery>A.png|b</gallery>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
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
		fxt.tst_Parse_page_wiki_str("<gallery>File:A.png|<ref name='a'>b</ref></gallery><references/>", String_.Concat_lines_nl_skipLast
		(	"<ul id=\"xowa_gallery_ul_0\" class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
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

	private void Init_html() {
		Io_mgr._.InitEngine_mem();	// clear out mem files
		Io_url rootDir = Io_url_.mem_dir_("mem").GenSubDir_nest(Xoa_app_.Name);
		fxt.App().Fsys_mgr().Temp_dir_(rootDir.OwnerDir().GenSubDir("tmp"));
	}
	private Gallery_mgr_data_chkr new_chkr_gallery_mgr()	{return new Gallery_mgr_data_chkr();}
	private Gallery_itm_chkr new_chkr_gallery_itm()	{return new Gallery_itm_chkr();}
}
class Gallery_mgr_data_chkr implements Tst_chkr {
	public Class<?> TypeOf() {return Gallery_nde.class;}
	public Gallery_itm_chkr[] Expd_subs() {return expd_subs;} public Gallery_mgr_data_chkr Expd_subs_(Gallery_itm_chkr... v) {expd_subs = v; return this;} Gallery_itm_chkr[] expd_subs = null;
	public int Chk(Tst_mgr mgr, String path, Object actl_obj) {
		Gallery_nde actl = (Gallery_nde)actl_obj;
		int rv = 0;
		rv += Chk_basic(mgr, path, actl, rv);
		rv += Chk_subs(mgr, path, actl, rv);
		return rv;
	}
	public int Chk_basic(Tst_mgr mgr, String path, Gallery_nde actl, int err) {
		return err;
	}
	public int Chk_subs(Tst_mgr mgr, String path, Gallery_nde actl, int err) {
		if (expd_subs != null) {
			int actl_subs_len = actl.Itms_len();
			Gallery_itm[] actl_subs = new Gallery_itm[actl_subs_len];  
			for (int i = 0; i < actl_subs_len; i++)
				actl_subs[i] = actl.Itms_get(i);
			return mgr.Tst_sub_ary(expd_subs, actl_subs, path, err);
		}
		return err;
	}
}
class Gallery_itm_chkr implements Tst_chkr {
	public Class<?> TypeOf() {return Gallery_itm.class;}
	public Gallery_itm_chkr Expd_lnki_(String v)	{expd_lnki = Xoa_ttl_chkr.new_(v); return this;} private Xoa_ttl_chkr expd_lnki;
	public Gallery_itm_chkr Expd_caption_(String v) {expd_caption = v; return this;} private String expd_caption;
	public int Chk(Tst_mgr mgr, String path, Object actl_obj) {
		Gallery_itm actl = (Gallery_itm)actl_obj;
		int err = 0;
		err += mgr.Tst_sub_obj(expd_lnki, actl.Ttl(), path, err);
		err += mgr.Tst_val(expd_caption == null, "", "caption", expd_caption, String_.new_utf8_(actl.Caption_bry()));
		return err;
	}
}

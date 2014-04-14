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
import gplx.xowa.ctgs.*; import gplx.xowa.xtns.gallery.*;
import gplx.xowa.html.portal.*; import gplx.xowa.html.modules.*;
public class Xow_html_mgr implements GfoInvkAble {
	public Xow_html_mgr(Xow_wiki wiki) {
		this.wiki = wiki;
		Xoa_app app = wiki.App();
		output_mgr = new Xoh_wiki_article(app, app.Gui_mgr().Kit().Tid() != gplx.gfui.Gfui_kit_.Swing_tid);	// reverse logic to handle swt,drd but not mem
		Io_url file_dir = app.User().Fsys_mgr().App_img_dir().GenSubDir_nest("file");
		img_media_play_btn = app.Url_converter_fsys().Encode_http(file_dir.GenSubFil("play.png"));
		img_media_info_btn = app.Url_converter_fsys().Encode_http(file_dir.GenSubFil("info.png"));
		img_thumb_magnify  = app.Url_converter_fsys().Encode_http(file_dir.GenSubFil("magnify-clip.png"));
		img_xowa_protocol = app.Url_converter_fsys().Encode_http(app.User().Fsys_mgr().App_img_dir().GenSubFil_nest("xowa", "protocol.png"));
		portal_mgr = new Xowh_portal_mgr(wiki);
		imgs_mgr = new Xoh_imgs_mgr(this);
//			module_regy = new Xoh_module_regy(wiki);
	}
	public void Init_by_lang(Xol_lang lang) {
		portal_mgr.Init_by_lang(lang);
	}
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public Xowh_portal_mgr Portal_mgr() {return portal_mgr;} private Xowh_portal_mgr portal_mgr;
	public Xoh_wiki_article Output_mgr() {return output_mgr;} private Xoh_wiki_article output_mgr;
//		public Xoh_module_regy Module_regy() {return module_regy;} private Xoh_module_regy module_regy;
	public boolean Tbl_para() {return tbl_para;} public Xow_html_mgr Tbl_para_y_() {tbl_para = true; return this;} public Xow_html_mgr Tbl_para_n_() {tbl_para = false; return this;} private boolean tbl_para = true;
	public int Img_thumb_width() {return img_thumb_width;} private int img_thumb_width = 220;
	public byte[] Img_media_play_btn() {return img_media_play_btn;} private byte[] img_media_play_btn;
	public byte[] Img_media_info_btn() {return img_media_info_btn;} private byte[] img_media_info_btn;
	public byte[] Img_thumb_magnify() {return img_thumb_magnify;} private byte[] img_thumb_magnify;
	public byte[] Img_xowa_protocol() {return img_xowa_protocol;} private byte[] img_xowa_protocol;
	public boolean Img_suppress_missing_src() {return img_suppress_missing_src;} public Xow_html_mgr Img_suppress_missing_src_(boolean v) {img_suppress_missing_src = v; return this;} private boolean img_suppress_missing_src = true;
	public Xohp_ctg_grp_mgr Ctg_mgr() {return ctg_mgr;} private Xohp_ctg_grp_mgr ctg_mgr = new Xohp_ctg_grp_mgr();
	public Xoctg_html_mgr Ns_ctg() {return ns_ctg;} private Xoctg_html_mgr ns_ctg = new Xoctg_html_mgr();
	public Xoh_imgs_mgr Imgs_mgr() {return imgs_mgr;} private Xoh_imgs_mgr imgs_mgr;
	public ByteAryFmtr Lnki_full_image() {return lnki_full_image;} ByteAryFmtr lnki_full_image = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	"<a href=\"~{href}\"~{anchor_class}~{anchor_rel}~{anchor_title} xowa_title=\"~{lnki_title}\"><img id=\"xowa_file_img_~{elem_id}\" alt=\"~{alt}\" src=\"~{src}\" width=\"~{width}\" height=\"~{height}\"~{img_class} /></a>"
		),	"elem_id", "href", "src", "width", "height", "alt", "lnki_title", "anchor_class", "anchor_rel", "anchor_title", "img_class"
		);
	public ByteAryFmtr Lnki_full_media() {return lnki_full_media;} ByteAryFmtr lnki_full_media = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	"<a href=\"~{lnki_src}\" xowa_title=\"~{lnki_title}\">~{lnki_caption}"
		,	"</a>"
		), "lnki_src", "lnki_title", "lnki_caption"
		);

	public ByteAryFmtr Lnki_thumb_core() {return lnki_thumb_core;} ByteAryFmtr lnki_thumb_core = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast	// REF.MW: Linker.php|makeImageLink2
		(	"<div class=\"thumb t~{lnki_halign}\">"
		,	"  <div id=\"xowa_file_div_~{elem_id}\" class=\"thumbinner\" style=\"width:~{div_width}px;\">"
		,	"~{lnki_content}"
		,	"  </div>"
		,	"</div>"
		,	""
		), "div_width", "lnki_halign", "lnki_content", "elem_id"
		);
	public ByteAryFmtr Lnki_thumb_file_image() {return lnki_thumb_file_image;} ByteAryFmtr lnki_thumb_file_image = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	"    ~{thumb_image}~{lnki_caption}~{lnki_alt}"
		), "thumb_image", "lnki_caption", "lnki_alt");    
	public ByteAryFmtr Lnki_thumb_file_video() {return lnki_thumb_file_video;} ByteAryFmtr lnki_thumb_file_video = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	"    <div id=\"xowa_media_div\">~{video_thumb}~{play_btn}"
		,	"    </div>~{lnki_caption}~{lnki_alt}"
		), "play_btn", "video_thumb", "lnki_caption", "lnki_alt");
	public ByteAryFmtr Lnki_thumb_file_audio() {return lnki_thumb_file_audio;} ByteAryFmtr lnki_thumb_file_audio = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	"    <div id=\"xowa_media_div\">~{play_btn}~{info_btn}"
		,	"    </div>~{lnki_caption}~{lnki_alt}"
		), "play_btn", "info_btn", "lnki_caption", "lnki_alt");
	public ByteAryFmtr Lnki_thumb_part_image() {return lnki_thumb_part_image;} ByteAryFmtr lnki_thumb_part_image = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	""
		,	"      <div>" 
		,	"        <a href=\"~{lnki_href}\" class=\"~{lnki_class}\" title=\"~{lnki_title}\">"
		,	"          <img id=\"xowa_file_img_~{elem_id}\" src=\"~{lnki_src}\" width=\"~{lnki_width}\" height=\"~{lnki_height}\" alt=\"~{lnki_alt}\" />"
		,	"        </a>"
		,	"      </div>"
		), "elem_id", "lnki_class", "lnki_href", "lnki_title", "lnki_src", "lnki_width", "lnki_height", "lnki_alt");
	public ByteAryFmtr Lnki_thumb_part_caption() {return lnki_thumb_part_caption;} ByteAryFmtr lnki_thumb_part_caption = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	""
		,	"    <div class=\"thumbcaption\">~{magnify_btn}"
		,	"      ~{lnki_caption}"
		,	"    </div>"
		), "magnify_btn", "lnki_caption");
	public ByteAryFmtr Lnki_thumb_part_alt() {return lnki_thumb_part_alt;} ByteAryFmtr lnki_thumb_part_alt = ByteAryFmtr.new_
		(String_.Concat_lines_nl_skipLast
		(	""
		,	"    <hr/>"
		,	"    <div class=\"thumbcaption\">"
		,	"~{alt_html}"
		,	"    </div>"
		)  
		, "alt_html");
	public ByteAryFmtr Lnki_thumb_part_magnfiy_btn() {return lnki_thumb_part_magnify_btn;} ByteAryFmtr lnki_thumb_part_magnify_btn = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	""
		,	"      <div class=\"magnify\">"
		,	"        <a href=\"~{lnki_src}\" class=\"internal\" title=\"~{lnki_enlarge_msg}\">"
		,	"          <img src=\"~{magnify_icon}\" width=\"15\" height=\"11\" alt=\"\" />"
		,	"        </a>"
		,	"      </div>"
		), "magnify_icon", "lnki_src", "lnki_enlarge_msg");
	public ByteAryFmtr Lnki_thumb_part_play_btn() {return lnki_thumb_part_play_btn;} ByteAryFmtr lnki_thumb_part_play_btn = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	""
		,	"      <div>" 
		,	"        <a id=\"xowa_file_play_~{play_id}\" href=\"~{lnki_url}\" xowa_title=\"~{lnki_title}\" class=\"xowa_anchor_button\" style=\"width:~{play_width}px;max-width:~{play_max_width}px;\">"
		,	"          <img src=\"~{play_icon}\" width=\"22\" height=\"22\" alt=\"Play sound\" />" 
		,	"        </a>" 
		,	"      </div>"
		), "play_id", "play_icon", "play_width", "play_max_width", "lnki_url", "lnki_title");
	public ByteAryFmtr Lnki_thumb_part_info_btn() {return lnki_thumb_part_info_btn;} ByteAryFmtr lnki_thumb_part_info_btn = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	""
		,	"      <div>" 
		,	"        <a href=\"~{lnki_href}\" class=\"image\" title=\"About this file\">"
		,	"          <img src=\"~{info_icon}\" width=\"22\" height=\"22\" />" 
		,	"        </a>" 
		,	"      </div>" 
		), "info_icon", "lnki_href");
	public ByteAryFmtr Plain() {return plain;} ByteAryFmtr plain = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	"~{val}"
		),	"val");
	public void Copy_cfg(Xow_html_mgr html_mgr) {imgs_mgr.Copy_cfg(html_mgr.Imgs_mgr());}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_lnki_full_image_))					lnki_full_image.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lnki_full_media_))					lnki_full_media.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lnki_thumb_core_))					lnki_thumb_core.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lnki_thumb_file_image_))				lnki_thumb_file_image.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lnki_thumb_file_audio_))				lnki_thumb_file_audio.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lnki_thumb_file_video_))				lnki_thumb_file_video.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lnki_thumb_part_image_))				lnki_thumb_part_image.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_lnki_thumb_part_caption_))			lnki_thumb_part_caption.Fmt_(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_lnki_thumb_part_alt_))				lnki_thumb_part_alt.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lnki_thumb_part_magnify_btn_))		lnki_thumb_part_magnify_btn.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lnki_thumb_part_play_btn_))			lnki_thumb_part_play_btn.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lnki_thumb_part_info_btn_))			lnki_thumb_part_info_btn.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_article))							return output_mgr;
		else if	(ctx.Match(k, Invk_portal))								return portal_mgr;
		else if	(ctx.Match(k, Invk_imgs))								return imgs_mgr;
		else if	(ctx.Match(k, Invk_ns_ctg))								return ns_ctg;
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String 
	  Invk_lnki_full_image_ = "lnki_full_image_", Invk_lnki_full_media_ = "lnki_full_media_", Invk_lnki_thumb_core_ = "lnki_thumb_core_"
	, Invk_lnki_thumb_file_image_ = "lnki_thumb_file_image_", Invk_lnki_thumb_file_audio_ = "lnki_thumb_file_audio_", Invk_lnki_thumb_file_video_ = "lnki_thumb_file_video_"
	, Invk_lnki_thumb_part_image_ = "lnki_thumb_part_image_", Invk_lnki_thumb_part_caption_ = "lnki_thumb_part_caption_", Invk_lnki_thumb_part_alt_ = "lnki_thumb_part_alt_"
	, Invk_lnki_thumb_part_magnify_btn_ = "lnki_thumb_part_magnify_btn_", Invk_lnki_thumb_part_play_btn_ = "lnki_thumb_part_play_btn_", Invk_lnki_thumb_part_info_btn_ = "lnki_thumb_part_info_btn_"
	, Invk_article = "article"
	, Invk_portal = "portal", Invk_imgs = "imgs", Invk_ns_ctg = "ns_ctg"
	;
	public static final String Str_img_class_thumbimage = "thumbimage";
	public static final byte[] Bry_anchor_class_image = ByteAry_.new_ascii_(" class=\"image\""), Bry_anchor_class_blank = ByteAry_.Empty, Bry_anchor_rel_nofollow = ByteAry_.new_ascii_(" rel=\"nofollow\""), Bry_anchor_rel_blank = ByteAry_.Empty, Bry_img_class_thumbimage = ByteAry_.new_ascii_(" class=\"thumbimage\""), Bry_img_class_none = ByteAry_.Empty;
}

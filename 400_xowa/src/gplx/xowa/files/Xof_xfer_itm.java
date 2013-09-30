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
package gplx.xowa.files; import gplx.*; import gplx.xowa.*;
public class Xof_xfer_itm {
	public byte 		Lnki_type() {return lnki_type;} private byte lnki_type;
	public int			Lnki_w() {return lnki_w;} private int lnki_w;
	public int			Lnki_h() {return lnki_h;} private int lnki_h;
	public double		Lnki_upright() {return lnki_upright;} double lnki_upright;
	public int			Lnki_thumbtime() {return lnki_thumbtime;} private int lnki_thumbtime = Xop_lnki_tkn.Thumbtime_null;
	public boolean			Lnki_thumbable() {return lnki_thumbable;} private boolean lnki_thumbable;	// SEE:NOTE_1:Lnki_thumbable
	public Xof_xfer_itm Atrs_by_lnki(byte lnki_type, int w, int h, double upright, int seek) {this.lnki_type = lnki_type; this.lnki_w = w; this.lnki_h = h; this.lnki_upright = upright; this.lnki_thumbtime = seek; lnki_thumbable = Xof_xfer_itm.Lnki_thumbable_calc(lnki_type, lnki_w, lnki_h); return this;}

	public int			Orig_w() {return orig_w;} private int orig_w;
	public int			Orig_h() {return orig_h;} private int orig_h;
	public int			Orig_file_len() {return orig_file_len;} private int orig_file_len;
	public Xof_xfer_itm Atrs_by_orig(int w, int h, int orig_file_len) {this.orig_w = w; this.orig_h = h; this.orig_file_len = orig_file_len; return this;}

	public byte[]		Lnki_ttl() {return ttl;} private byte[] ttl;
	public Xof_ext		Lnki_ext() {return ext;} private Xof_ext ext;
	public byte[]		Lnki_md5() {return md5;} private byte[] md5;
	public byte[]		Redirect() {return redirect;} private byte[] redirect;
	public Xof_xfer_itm Atrs_by_ttl(byte[] lnki_ttl, byte[] redirect) {
		this.redirect = redirect;
		ttl = redirect == Xop_redirect_mgr.Redirect_null_bry ? ByteAry_.Copy(lnki_ttl) : redirect;
		ttl = decoder.Decode_lax(Ttl_standardize(ttl));		// NOTE: this line is repeated in static method below
		md5 = Xof_xfer_itm.Md5_calc(ttl);					// NOTE: md5 is calculated off of url_decoded ttl; EX: A%2Cb is converted to A,b and then md5'd. note that A%2Cb still remains the title
		ext = Xof_ext_.new_by_ttl_(ttl);
		return this;
	}	static final Url_encoder decoder = Url_encoder.new_http_url_().Itms_raw_same_many(Byte_ascii.Plus);

	public Xof_meta_itm Meta_itm() {return meta_itm;} private Xof_meta_itm meta_itm; 
	public Xof_repo_itm Trg_repo() {return trg_repo;} public Xof_xfer_itm Trg_repo_(Xof_repo_itm v) {trg_repo = v; return this;} private Xof_repo_itm trg_repo;
	public int Trg_repo_idx() {return trg_repo_idx;} public Xof_xfer_itm Trg_repo_idx_(int trg_repo_idx) {this.trg_repo_idx = trg_repo_idx; return this;} private int trg_repo_idx = Xof_meta_itm.Repo_unknown;
	public void Atrs_by_meta(Xof_meta_itm meta_itm, Xof_repo_itm trg_repo, int thumb_w_img) {this.meta_itm = meta_itm; this.trg_repo = trg_repo; this.thumb_w_img = thumb_w_img;} private int thumb_w_img;
	public void Atrs_by_meta_only(Xof_meta_itm meta_itm) {this.meta_itm = meta_itm; Atrs_by_ttl(meta_itm.Ttl(), meta_itm.Ptr_ttl());}

	public int Html_dynamic_id() {return html_dynamic_id;} private int html_dynamic_id = -1;
	public byte Html_dynamic_tid() {return html_dynamic_tid;} public Xof_xfer_itm Html_dynamic_tid_(byte v) {html_dynamic_tid = v; return this;} private byte html_dynamic_tid = Html_dynamic_tid_none;
	public Xof_xfer_itm Html_dynamic_atrs_(int id, byte tid) {html_dynamic_id = id; html_dynamic_tid = tid; return this;} 
	public int			Html_w() {return html_w;} private int html_w;
	public int			Html_h() {return html_h;} private int html_h;
	public byte[]		Html_view_src() {return html_view_src;} private byte[] html_view_src = ByteAry_.Empty;
	public byte[]		Html_orig_src() {return html_orig_src;} private byte[] html_orig_src = ByteAry_.Empty;
	public boolean			Html_pass() {return html_pass;} private boolean html_pass;
	public void Atrs_by_html_for_tests(int html_w, int html_h, byte[] html_view_src, byte[] html_orig_src) {this.html_w = html_w; this.html_h = html_h; this.html_view_src = html_view_src; this.html_orig_src = html_orig_src;}
	public Object Gallery_data() {return gallery_data;} public Xof_xfer_itm Gallery_data_(Object v) {gallery_data = v; return this;} private Object gallery_data;

	public boolean Atrs_calc_for_html() {return Atrs_calc_for_html(false);}
	public boolean Atrs_calc_for_html(boolean caller_is_file_page) {
		html_pass = false;
		html_orig_src = html_view_src = ByteAry_.Empty;
		html_w = lnki_w; html_h = lnki_h;
		if (meta_itm == null || trg_repo == null) return false;
		if (meta_itm.Ptr_ttl_exists()) {
			ttl = meta_itm.Ptr_ttl();
			md5 = Md5_(ttl);
		}
		boolean limit_size = !ext.Id_is_svg() || (ext.Id_is_svg() && caller_is_file_page);
		if (ext.Id_is_media() && html_w < 1)		// if media and no width, set to default; NOTE: must be set or else dynamic download will resize play button to small size; DATE:20121227
			html_w = Xof_img_size.Thumb_width_ogv;	
		if (lnki_thumbable) {				// file is thumb
			if (ext.Id_is_video()) {		// video is a special case; src is thumb_w but html_w / html_h is based on calc
				html_orig_src = Trg_html(Xof_repo_itm.Mode_orig, Xof_img_size.Size_null_deprecated);
				if (meta_itm.Thumbs_indicates_oga() && ext.Id_is_ogv()) {ext = Xof_ext_.new_by_ext_(Xof_ext_.Bry_oga); return true;}	// if audio, do not thumb; NOTE: must happen after html_orig_src, b/c html must still be generated to auto-download files; NOTE: must change ext to oga b/c ogg may trigger video code elsewhere
				Xof_meta_thumb thumb = meta_itm.Thumbs_get_vid(lnki_thumbtime);
				if (thumb != null) {
					Xof_xfer_itm_.Calc_xfer_size(calc_size, thumb_w_img, thumb.Width(), thumb.Height(), html_w, html_h, lnki_thumbable, lnki_upright);
					html_w = calc_size.Val_0(); html_h = calc_size.Val_1(); 
					html_view_src = Trg_html(Xof_repo_itm.Mode_thumb, thumb.Width());	// NOTE: must pass thumb.Width() not html_w b/c only one thumb generated for a video file
					html_pass = true;
					return true;
				}
			}
			else {							// regular thumb
				html_orig_src = Trg_html(Xof_repo_itm.Mode_orig, Xof_img_size.Size_null_deprecated);
				if (ext.Id_is_audio()) return true;	// if audio, do not thumb; even if user requests thumb;
				Xof_meta_thumb[] thumbs = meta_itm.Thumbs(); int thumbs_len = thumbs.length; Xof_meta_thumb thumb = null;
				if (lnki_h > 0 && orig_w < 1 && thumbs_len > 0) {		// if height is specified and no orig, then iterate over thumbs to find similar height; NOTE: this is a fallback case; orig_w/h is optimal; EX: c:Jacques-Louis David and <gallery>
					Xof_meta_thumb largest = meta_itm.Thumbs_get_largest(thumbs_len);	// get largest thumb
					Xof_xfer_itm_.Calc_xfer_size(calc_size, thumb_w_img, largest.Width(), largest.Height(), html_w, html_h, lnki_thumbable, lnki_upright, false); // use largest to calc correct width/height; note that this is needed for gallery which passes in 120,120; EX:c:Yellowstone Park
					int comp_height = calc_size.Val_1();
					for (int i = 0; i < thumbs_len; i++) {
						Xof_meta_thumb tmp_thumb = thumbs[i];
						if (Int_.Between(tmp_thumb.Height(), comp_height - 1, comp_height + 1)) {
							thumb = tmp_thumb;
							break;
						}
					}
					if (thumb != null) return Atrs_calc_for_html_found(thumb.Width(), thumb.Height());	// thumb found
				}
				
				Xof_xfer_itm_.Calc_xfer_size(calc_size, thumb_w_img, meta_itm.Orig_w(), meta_itm.Orig_h(), html_w, html_h, lnki_thumbable, lnki_upright, limit_size); // calc html_h and html_w; can differ from lnki_w, lnki_h; note that -1 width is handled by thumb_w_img
				html_w = calc_size.Val_0();
				if (html_h != -1) html_h = calc_size.Val_1(); 	// NOTE: if -1 (no height specified) do not set height; EX:Tokage_2011-07-15.jpg; DATE:2013-06-03
				html_view_src = Trg_html(Xof_repo_itm.Mode_thumb, html_w);
				thumb = meta_itm.Thumbs_get_img(html_w, 0);
				if (thumb == null) {					// exact thumb not found
					if (html_w == meta_itm.Orig_w()		// html_w matches orig_w; occurs when thumb,upright requested, but upright size is larger than orig; EX.WP:St. Petersburg
						&& !ext.Id_needs_convert()		// but ext cannot be something that needs conversion; EX: 120,90 svg may match thumb of 120,90, but .png still needs to be generated
						&& meta_itm.Orig_exists() == Xof_meta_itm.Exists_y
						) {	
						html_h = meta_itm.Orig_h();
						html_view_src = Trg_html(Xof_repo_itm.Mode_orig, -1);
						html_pass = true;
						return true;
					}
					if (ext.Id_is_djvu()) {				// NOTE: exact djvu w thumbs are not on server; always seems to be 1 off; EX: 90 requested, but 90 doesn't exist; 89 does
						thumb = meta_itm.Thumbs_get_img(html_w, 1);
						if (thumb != null) return Atrs_calc_for_html_found(thumb.Width(), thumb.Height());	// thumb found
					}
				}
				else {
					html_h = thumb.Height();
					html_pass = true;
					return true;
				}
			}
		}
		else {								// file is orig
			byte mode_id = ext.Id_is_svg() ? Xof_repo_itm.Mode_thumb : Xof_repo_itm.Mode_orig;	// svgs will always return thumb; EX:[[A.svg]] -> A.svg.png
			html_view_src = html_orig_src = Trg_html(mode_id, html_w);
			if (meta_itm.Thumbs_indicates_oga() && ext.Id_is_ogv()) {ext = Xof_ext_.new_by_ext_(Xof_ext_.Bry_oga); return true;}	// if audio, do not thumb; NOTE: must happen after html_orig_src, b/c html must still be generated to auto-download files; NOTE: must change ext to oga b/c ogg may trigger video code elsewhere
			if		(ext.Id_is_audio()) return true;	// if audio, return true; SEE:NOTE_2
			else if (ext.Id_is_video()) {
				Xof_meta_thumb thumb = meta_itm.Thumbs_get_vid(lnki_thumbtime);	// get thumb at lnki_thumbtime; NOTE: in most cases this will just be the 1st thumb; note that orig video files don't have an official thumb
				if (thumb != null) {
					html_w = thumb.Width(); html_h = thumb.Height();	// NOTE: take thumb_size; do not rescale to html_w, html_h b/c html_w will default to 220; native width of thumbnail should be used; DATE:2013-04-11
					html_view_src = Trg_html(Xof_repo_itm.Mode_thumb, thumb.Width());	// NOTE: must pass thumb.Width() not html_w b/c only one thumb generated for a video file
					html_pass = true;
					return true;
				}
			}
			if (meta_itm.Orig_exists() == Xof_meta_itm.Exists_y) {	// file found previously >>> gen html
				html_w = meta_itm.Orig_w(); html_h = meta_itm.Orig_h();
				html_view_src = Trg_html(mode_id, html_w);
				html_pass = true;
				return true;
			}
		}
		// file not found >>> set size to 0 and byte[] to empty
		html_w = lnki_w < 0 ? 0 : lnki_w;
		html_h = lnki_h < 0 ? 0 : lnki_h;
		return false;
	}	Int_2_ref calc_size = new Int_2_ref();
	boolean Atrs_calc_for_html_found(int model_w, int model_h) {
		Xof_xfer_itm_.Calc_xfer_size(calc_size, thumb_w_img, model_w, model_h, html_w, html_h, lnki_thumbable, lnki_upright, false);	// recalc html_w, html_h; note that false passed b/c truncation is not needed
		html_w = calc_size.Val_0(); html_h = calc_size.Val_1();
		html_view_src = Trg_html(Xof_repo_itm.Mode_thumb, model_w);	// note that thumb.Width is used (the actual file width), not html_w
		html_pass = true;
		return true;
	}
	byte[] Trg_html(byte mode_id, int width)		{return Xof_url_bldr.Temp.Set_trg_html_(mode_id, trg_repo, ttl, md5, ext, width, lnki_thumbtime).Xto_bry();}
	public Io_url Trg_file(byte mode_id, int width) {return Xof_url_bldr.Temp.Set_trg_file_(mode_id, trg_repo, ttl, md5, ext, width, lnki_thumbtime).Xto_url();}
	public Xof_xfer_itm Clear() {
		lnki_type = Byte_.MaxValue_127; lnki_w = Int_.Neg1; lnki_h = Int_.Neg1; lnki_upright = Int_.Neg1; lnki_thumbtime = Int_.Neg1; lnki_thumbable = false;
		orig_w = Int_.Neg1; orig_h = Int_.Neg1; orig_file_len = 0;	// NOTE: cannot be -1, or else will always download orig; see ext rule chk and (orig_file_len < 0)
		redirect = null; ttl = null; md5 = null; ext = null;
		html_orig_src = html_view_src = ByteAry_.Empty; html_w = Int_.Neg1; html_h = Int_.Neg1;
		trg_repo_idx = Int_.Neg1; meta_itm = null;
		html_dynamic_id = -1; html_dynamic_tid = Html_dynamic_tid_none;
		return this;
	}
	public Xof_xfer_itm Clone() {
		Xof_xfer_itm rv = new Xof_xfer_itm();
		rv.lnki_type = lnki_type; rv.lnki_w = lnki_w; rv.lnki_h = lnki_h; rv.lnki_upright = lnki_upright; rv.lnki_thumbtime = lnki_thumbtime; rv.lnki_thumbable = lnki_thumbable;
		rv.orig_w = orig_w; rv.orig_h = orig_h; rv.orig_file_len = orig_file_len;
		rv.redirect = redirect; rv.ttl = ttl; rv.md5 = md5; rv.ext = ext;
		rv.html_w = html_w; rv.html_h = html_h; rv.html_view_src = html_view_src; rv.html_orig_src = html_orig_src; 
		rv.trg_repo_idx = trg_repo_idx;
		rv.meta_itm = meta_itm;	 // NOTE: shared reference
		rv.html_dynamic_id = html_dynamic_id; rv.html_dynamic_tid = html_dynamic_tid;
		return rv;
	}
	public static byte[] Md5_calc(byte[] v) {return ByteAry_.new_ascii_(gplx.security.HashAlgo_.Md5.CalcHash(ConsoleDlg_.Null, gplx.ios.IoStream_.ary_(v)));}
	public static byte[] Md5_(byte[] ttl) {
		ttl = decoder.Decode_lax(Ttl_standardize(ttl));	// NOTE: this line is repeated in member above
		return Xof_xfer_itm.Md5_calc(ttl);				// NOTE: md5 is calculated off of url_decoded ttl; EX: A%2Cb is converted to A,b and then md5'd. note that A%2Cb still remains the title
	}
	public static boolean Lnki_thumbable_calc(byte lnki_type, int lnki_w, int lnki_h) {return Xop_lnki_type.Id_is_thumb_like(lnki_type) || lnki_w != -1 || lnki_h != -1;}	// SEE:NOTE_1
	private static byte[] Ttl_standardize(byte[] ttl) {
		int ttl_len = ttl.length;
		for (int i = 0; i < ttl_len; i++) {	// convert all spaces to _; NOTE: not same as lnki.Ttl().Page_url(), b/c Page_url does incompatible encoding
			byte b = ttl[i];
			if (b == Byte_ascii.Space) ttl[i] = Byte_ascii.Underline;
			if (i == 0) {
				if (b > 96 && b < 123) ttl[i] -= 32;	// NOTE: file automatically uppercases 1st letter
			}
		}
		return ttl;
	}
	public static final byte Html_dynamic_tid_none = 0, Html_dynamic_tid_img = 1, Html_dynamic_tid_vid = 2, Html_dynamic_tid_gallery = 3;
}
/*
NOTE_1:Lnki_thumbable
. false only if following form
[[A.png]]		-> must get orig
. true in almost all other cases, especially if (a) type is thumb; (b) size exists; (c) upright;
. basically, indicates that image will be stored on wmf server as "/thumb/" url
[[A.png|thumb]] -> default to 220 and check for 220px
[[A.png|40px]]  -> check for 40px
[[A.png|x40px]] -> calc n width and check for npx

NOTE_2:return true if media
. this seems hackish, but if Atrs_calc_for_html returns false, then file is generally added to the queue
. the problem is that media/audio is usually not found
.. so, for example, when a wiki page does pronunciation [[File:A.oga]], A.oga will return false (since it's not there)
.. however, unlike images which xowa will try to fetch the thumb, xowa will never fetch audios (since audios are not "visible", most will not be played, and some audios are big)
... the corollary is that audios are only fetched when the play buton is clicked (and via code in Xog_win)
.. so, if false is returned, then A.oga gets added to the queue, only to be ignored by queue rules later
... the problem here is that the status bar will flash "downloading: File:A.oga" which is misleading
. so, return true so that it is never added to the queue. this depends on the "click" of the play button code to actually download the file
. note that video doesn't suffer from this issue, b/c video has thumbs which can or can not be found
*/
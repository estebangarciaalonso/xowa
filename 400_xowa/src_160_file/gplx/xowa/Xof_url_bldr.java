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
import gplx.gfui.*;
public class Xof_url_bldr {
	ByteAryBfr bfr = ByteAryBfr.reset_(400);
	public byte Dir_spr() {return dir_spr;} public Xof_url_bldr Dir_spr_(byte v) {dir_spr = v; return this;} private byte dir_spr;
	public byte[] Root() {return root;} public Xof_url_bldr Root_(byte[] v) {root = v; return this;} private byte[] root;
	public byte[] Area() {return area;} public Xof_url_bldr Area_(byte[] v) {area = v; return this;} private byte[] area;
	public byte[] Ttl() {return ttl;} public Xof_url_bldr Ttl_(byte[] v) {ttl = v; return this;} private byte[] ttl;
	public byte[] Md5() {return md5;} public Xof_url_bldr Md5_(byte[] v) {md5 = v; return this;} private byte[] md5;
	public Xof_ext Ext() {return ext;} public Xof_url_bldr Ext_(Xof_ext v) {ext = v; return this;} private Xof_ext ext;
	public int Width() {return width;} public Xof_url_bldr Width_(int v) {width = v; return this;} private int width;
	public int Seek() {return seek;} public Xof_url_bldr Seek_(int v) {seek = v; return this;} private int seek = -1;
	public Xof_url_bldr Wmf_fsys_(boolean v) {wmf_fsys = v; return this;} private boolean wmf_fsys;
	public boolean Thumb() {return thumb;} public Xof_url_bldr Thumb_(boolean v) {thumb = v; return this;} private boolean thumb;
	public Xof_url_bldr Set_trg_html_(byte mode, Xof_repo_itm repo, byte[] ttl, byte[] md5, Xof_ext ext, int width, int seek) {
		this.wmf_fsys = false; this.thumb = mode == Xof_repo_itm.Mode_thumb;
		this.dir_spr = Byte_ascii.Slash; this.root = repo.Root_http(); this.ttl = repo.Gen_name_trg(ttl, md5, ext); this.area = repo.Mode_names()[mode];
		this.md5 = md5; this.ext = ext; this.width = width; this.seek = seek;
		this.repo_file_depth = repo.Dir_depth();
		return this;
	}	private int repo_file_depth;
	public Xof_url_bldr Set_src_file_(byte mode, Xof_repo_itm repo, byte[] ttl, byte[] md5, Xof_ext ext, int width, int seek) {
		this.wmf_fsys = true; this.thumb = mode == Xof_repo_itm.Mode_thumb;
		this.dir_spr = repo.Dir_spr(); this.root = repo.Root(); this.ttl = repo.Gen_name_src(ttl); this.area = repo.Mode_names()[mode];
		this.md5 = md5; this.ext = ext; this.width = width; this.seek = seek;
		this.tarball = repo.Tarball();
		return this;
	}	private boolean tarball;
	public Xof_url_bldr Set_trg_file_(byte mode, Xof_repo_itm repo, byte[] ttl, byte[] md5, Xof_ext ext, int width, int seek) {
		this.wmf_fsys = false; this.thumb = mode == Xof_repo_itm.Mode_thumb;
		this.dir_spr = repo.Dir_spr(); this.root = repo.Root(); this.ttl = repo.Gen_name_trg(ttl, md5, ext); this.area = repo.Mode_names()[mode];
		this.md5 = md5; this.ext = ext; this.width = width; this.seek = seek;
		this.repo_file_depth = repo.Dir_depth();
		return this;
	}
	public byte[] Xto_bry() {Bld(); byte[] rv = bfr.XtoAryAndClear(); Clear(); return rv;}
	public String Xto_str() {Bld(); String rv = bfr.XtoStr(); Clear(); return rv;}
	public Io_url Xto_url() {Bld(); Io_url rv = Io_url_.new_fil_(bfr.XtoStr()); Clear(); return rv;}
	void Bld() {
		Add_core();
		if (thumb) {
			if (wmf_fsys)	Add_thumb_wmf();
			else			Add_thumb_xowa();
		}
	}
	Xof_url_bldr Add_core() {
		bfr.Add(root);																	// add root;				EX: "C:\xowa\file\"; assume trailing dir_spr
		if (area != null && !(wmf_fsys && !thumb))										// !(wmf_fsys && !thumb) means never add if wmf_fsys and orig
			bfr.Add(area).Add_byte(dir_spr);											// add area;				EX: "thumb\"
		byte b0 = md5[0];
		if (wmf_fsys) {
			bfr.Add_byte(b0).Add_byte(dir_spr);											// add md5_0				EX: "0/"
			bfr.Add_byte(b0).Add_byte(md5[1]).Add_byte(dir_spr);						// add md5_01				EX: "01/"
		}
		else {
			for (int i = 0; i < repo_file_depth; i++)
				bfr.Add_byte(md5[i]).Add_byte(dir_spr);									// add md5_0123				EX: "0/1/2/3"
		}
		if (wmf_fsys) {
			if (tarball)																// tarball
				bfr.Add(ttl);															// NOTE: file_names are not url-encoded; this includes symbols (') and foreign characters (ö)
			else																		// wmf_http
				bfr.Add(encoder_src_http.Encode(ttl));									// NOTE: file_names must be url-encoded; JAVA will default to native charset which on Windows will be 1252; foreign character urls will fail due to conversion mismatch (1252 on windows; UTF-8 on WMF); EX.WP:Möbius strip
		}
		else
			bfr.Add(ttl);																// add title;				EX: "A.png"
		return this;
	}
	Xof_url_bldr Add_thumb_xowa() {
		bfr.Add_byte(dir_spr);															// add dir_spr;				EX: "\"
		bfr.Add_int_variable(width).Add(Bry_px);										// add width;				EX: "220px"
		if (seek != Xop_lnki_tkn.Thumbtime_null)
			bfr.Add_byte(Xof_meta_thumb_parser.Dlm_seek).Add_int_variable(seek);		// add seek					EX: "@5"
		bfr.Add_byte(Byte_ascii.Dot);													// add .					EX: "."
		if (thumb)
			bfr.Add(ext.Ext_view());													// add view_ext				EX: ".png"
		else
			bfr.Add(ext.Ext());															// add orig_ext				EX: ".svg"
		return this;
	}
	Xof_url_bldr Add_thumb_wmf() {
		bfr.Add_byte(dir_spr);															// add dir_spr;				EX: "\"
		int file_ext_id = ext.Id();
		switch (file_ext_id) {
			case Xof_ext_.Id_ogg:
			case Xof_ext_.Id_ogv:
			case Xof_ext_.Id_webm:
				if (seek != -1)
					bfr.Add(Bry_seek).Add_int_variable(seek).Add_byte(Byte_ascii.Dash);	// add seek;				EX: "seek%3D5-"
				else
					bfr.Add(Bry_mid);													// add mid;					EX: "mid-"
				break;
			case Xof_ext_.Id_tif:
			case Xof_ext_.Id_tiff:
				bfr.Add(Bry_lossy_page1);												// add "lossy-page1-"		EX: "lossy-page1-"
				bfr.Add_int_variable(width);											// add file_w;				EX: "220"
				bfr.Add(Bry_px_dash);													// add px;					EX: "px-"
				break;
			case Xof_ext_.Id_pdf:
			case Xof_ext_.Id_djvu:
				bfr.Add(Bry_page1);														// add "page1-"				EX: "page1-"
				bfr.Add_int_variable(width);											// add file_w;				EX: "220"
				bfr.Add(Bry_px_dash);													// add px;					EX: "px-"
				break;
			default:
				bfr.Add_int_variable(width);											// add file_w;				EX: "220"
				bfr.Add(Bry_px_dash);													// add px;					EX: "px-"
				break;
		}
		bfr.Add(encoder_src_http.Encode(ttl));											// add ttl again;			EX: "A.png"
		switch (file_ext_id) {
			case Xof_ext_.Id_svg:
				bfr.Add_byte(Byte_ascii.Dot).Add(Xof_ext_.Bry_png);						// add .png;				EX: "A.svg" -> "A.svg.png"		NOTE: MediaWiki always adds as lowercase
				break;
			case Xof_ext_.Id_pdf:
			case Xof_ext_.Id_tif:														// add .jpg					EX: "A.tif" -> "A.tif.jpg"		NOTE: MediaWiki always adds as lowercase
			case Xof_ext_.Id_tiff:
			case Xof_ext_.Id_ogg:
			case Xof_ext_.Id_ogv:						
			case Xof_ext_.Id_djvu:
			case Xof_ext_.Id_webm:
				bfr.Add_byte(Byte_ascii.Dot).Add(Xof_ext_.Bry_jpg);
				break;
		}
		return this;
	}
	Xof_url_bldr Clear() {
		root = area = ttl = md5 = null;
		width = 0; seek = -1;
		ext = null;
		bfr.Clear();
		return this;
	}
	public static final byte[] Bry_reg = ByteAry_.new_ascii_("reg.csv"), Bry_px = ByteAry_.new_ascii_("px"), Bry_px_dash = ByteAry_.new_ascii_("px-"), Bry_thumb = ByteAry_.new_ascii_("thumb"), Bry_mid = ByteAry_.new_ascii_("mid-"), Bry_lossy_page1 = ByteAry_.new_ascii_("lossy-page1-"), Bry_page1 = ByteAry_.new_ascii_("page1-"), Bry_seek = ByteAry_.new_ascii_("seek%3D"), Bry_file = ByteAry_.new_ascii_("file");
	public static final Xof_url_bldr Temp = new Xof_url_bldr();
	private static final Url_encoder encoder_src_http = Url_encoder.new_http_url_(); // NOTE: changed from new_html_href_mw_ to new_url_ on 2012-11-19; issues with A%2Cb becoming A%252Cb
}

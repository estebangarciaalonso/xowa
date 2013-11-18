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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.files.*;
class Xob_xfer_temp_itm {
	public int Lnki_id() {return lnki_id;} private int lnki_id;
	public byte Orig_repo() {return orig_repo;} private byte orig_repo;
	public int Lnki_ext() {return lnki_ext;} private int lnki_ext;
	public String Orig_media_type() {return orig_media_type;} private String orig_media_type;
	public byte Orig_media_type_tid() {return orig_media_type_tid;} private byte orig_media_type_tid;
	public int Orig_page_id() {return orig_page_id;} private int orig_page_id;
	public String Join_ttl() {return join_ttl;} private String join_ttl;
	public String Redirect_src() {return redirect_src;} private String redirect_src;
	public byte Lnki_type() {return lnki_type;} private byte lnki_type;
	public int Lnki_w() {return lnki_w;} private int lnki_w;
	public int Lnki_h() {return lnki_h;} private int lnki_h;
	public int Lnki_count() {return lnki_count;} private int lnki_count;
	public int Lnki_page_id() {return lnki_page_id;} private int lnki_page_id;
	public int Orig_w() {return orig_w;} private int orig_w;
	public int Orig_h() {return orig_h;} private int orig_h;
	public double Lnki_upright() {return lnki_upright;} private double lnki_upright;
	public double Lnki_thumbtime() {return lnki_thumbtime;} private double lnki_thumbtime;
	public void Clear() {		
		lnki_ext = lnki_type 
				= orig_repo = orig_media_type_tid = Byte_.MaxValue_127;
		chk_tid = Chk_tid_none;
		lnki_id = lnki_w = lnki_h = lnki_count =  lnki_page_id
				= orig_w = orig_h = orig_page_id = Int_.Neg1;
		join_ttl =  redirect_src = orig_media_type = null;
		lnki_upright = Xop_lnki_tkn.Upright_null;
		lnki_thumbtime = Xop_lnki_tkn.Thumbtime_null;
	}
	public void Load(DataRdr rdr) {
		lnki_ext = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_ext);
		orig_media_type = rdr.ReadStrOr(Xob_orig_regy_tbl.Fld_oor_orig_media_type, "");	// convert nulls to ""
		orig_media_type_tid = Xof_media_type.Xto_byte(orig_media_type);
		lnki_id = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_id);
		orig_repo = rdr.ReadByte(Xob_orig_regy_tbl.Fld_oor_orig_repo);
		join_ttl = rdr.ReadStr(Xob_orig_regy_tbl.Fld_oor_orig_join_ttl);
		redirect_src = rdr.ReadStr(Xob_orig_regy_tbl.Fld_oor_lnki_ttl);
		lnki_type = rdr.ReadByte(Xob_lnki_regy_tbl.Fld_olr_lnki_type);
		lnki_w = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_w);
		lnki_h = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_h);
		lnki_count = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_count);
		lnki_page_id = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_page_id);
		orig_w = rdr.ReadIntOr(Xob_orig_regy_tbl.Fld_oor_orig_w, -1);
		orig_h = rdr.ReadIntOr(Xob_orig_regy_tbl.Fld_oor_orig_h, -1);
		orig_page_id = rdr.ReadIntOr(Xob_orig_regy_tbl.Fld_oor_orig_page_id, -1);
		lnki_upright = rdr.ReadDouble(Xob_lnki_regy_tbl.Fld_olr_lnki_upright);
		lnki_thumbtime = rdr.ReadDouble(Xob_lnki_regy_tbl.Fld_olr_lnki_thumbtime);
	}
	public static final byte 
		  Chk_tid_none = 0
		, Chk_tid_orig_page_id_is_null = 1
		, Chk_tid_orig_media_type_is_audio = 2
	;
	public byte Chk_tid() {return chk_tid;} private byte chk_tid;
	public boolean Chk(Xof_img_size img_size) {
		if (String_.Eq(join_ttl, redirect_src))	// join_ttl is same as redirect_src; not a redirect; EX:(direct) join="A.png";redirect_src="A.png"; (redirect) join="A.png";redirect_src="B.png" (i.e.: B redirects to A)
			redirect_src = "";
		else {	// redirect; make sure extension matches; EX: A.png redirects to B.png; lnki_ext will be .png (the lnki's ext); should be .png (the actual file's ext)
			Xof_ext join_ext = Xof_ext_.new_by_ttl_(ByteAry_.new_utf8_(join_ttl));
			lnki_ext = join_ext.Id();
		}
		lnki_ext = Xof_media_type.Convert_if_ogg_and_video(lnki_ext, orig_media_type_tid);
		if (	lnki_thumbtime != Xop_lnki_tkn.Thumbtime_null		// thumbtime defined
			&&	orig_media_type_tid != Xof_media_type.Tid_video		// but not a video;
			)
			lnki_thumbtime = Xop_lnki_tkn.Thumbtime_null;			// set thumbtime to NULL; actually occurs for one file: [[File:Crash.arp.600pix.jpg|thumb|thumbtime=2]]
		if (orig_page_id == -1) {	// no orig found (i.e.: not in local's / remote's image.sql);
			chk_tid = Chk_tid_orig_page_id_is_null;
			return false;
		}
		if (orig_media_type_tid == Xof_media_type.Tid_audio) {	// ignore: audio will never have thumbs
			chk_tid = Chk_tid_orig_media_type_is_audio;
			return false;
		}
		if (lnki_ext == Xof_ext_.Id_mid) {	// NOTE: .mid does not have orig_media_type of "AUDIO"
			chk_tid = Chk_tid_orig_media_type_is_audio;
			return false;
		}
		img_size.Html_size_calc(Xof_exec_tid.Tid_wiki_page, lnki_w, lnki_h, lnki_type, lnki_upright, lnki_ext, orig_w, orig_h, Xof_img_size.Thumb_width_img);
		return true;
	}
	public void Insert(Db_stmt stmt, Xof_img_size img_size) {
		Xob_xfer_temp_tbl.Insert(stmt, lnki_id, lnki_page_id, orig_repo, orig_page_id, join_ttl, redirect_src, lnki_ext, lnki_type, orig_media_type, img_size.File_is_orig(), orig_w, orig_h, img_size.File_w(), img_size.File_h(), img_size.Html_w(), img_size.Html_h(), lnki_thumbtime, lnki_count);
	}
}

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
public class Xof_ext_ {
	public static final byte Id_unknown = 0, Id_png = 1, Id_jpg = 2, Id_jpeg = 3, Id_gif = 4, Id_tif = 5, Id_tiff = 6, Id_svg = 7, Id_djvu = 8, Id_pdf = 9, Id_mid = 10, Id_ogg = 11, Id_oga = 12, Id_ogv = 13, Id_webm = 14;
	public static final byte[] Bry_png = ByteAry_.new_ascii_("png"), Bry_jpg = ByteAry_.new_ascii_("jpg"), Bry_jpeg = ByteAry_.new_ascii_("jpeg"), Bry_gif = ByteAry_.new_ascii_("gif"), Bry_tif = ByteAry_.new_ascii_("tif"), Bry_tiff = ByteAry_.new_ascii_("tiff"), Bry_svg = ByteAry_.new_ascii_("svg"), Bry_djvu = ByteAry_.new_ascii_("djvu"), Bry_pdf = ByteAry_.new_ascii_("pdf"), Bry_mid = ByteAry_.new_ascii_("mid"), Bry_ogg = ByteAry_.new_ascii_("ogg"), Bry_oga = ByteAry_.new_ascii_("oga"), Bry_ogv = ByteAry_.new_ascii_("ogv"), Bry_webm = ByteAry_.new_ascii_("webm");
	public static final byte[][] Bry__ary = new byte[][] {ByteAry_.Empty, Bry_png, Bry_jpg, Bry_jpeg, Bry_gif, Bry_tif, Bry_tiff, Bry_svg, Bry_djvu, Bry_pdf, Bry_mid, Bry_ogg, Bry_oga, Bry_ogv, Bry_webm};
	public static final byte[][] Mime_type__ary = new byte[][] {ByteAry_.new_ascii_("application/octet-stream"), ByteAry_.new_ascii_("image/png"), ByteAry_.new_ascii_("image/jpg"), ByteAry_.new_ascii_("image/jpeg"), ByteAry_.new_ascii_("image/gif"), ByteAry_.new_ascii_("image/tiff"), ByteAry_.new_ascii_("image/tiff"), ByteAry_.new_ascii_("image/svg+xml"), ByteAry_.new_ascii_("image/x.djvu"), ByteAry_.new_ascii_("application/pdf"), ByteAry_.new_ascii_("application/x-midi"), ByteAry_.new_ascii_("video/ogg"), ByteAry_.new_ascii_("audio/oga"), ByteAry_.new_ascii_("video/ogg"), ByteAry_.new_ascii_("video/webm")};
	private static final HashAdp id_hash = id_hash_new_();
	private static HashAdp id_hash_new_() {
		HashAdp rv = HashAdp_.new_bry_();
		id_hash_new_(rv, Bry_png, Id_png);
		id_hash_new_(rv, Bry_jpg, Id_jpg);
		id_hash_new_(rv, Bry_jpeg, Id_jpeg);
		id_hash_new_(rv, Bry_gif, Id_gif);
		id_hash_new_(rv, Bry_tif, Id_tif);
		id_hash_new_(rv, Bry_tiff, Id_tiff);
		id_hash_new_(rv, Bry_svg, Id_svg);
		id_hash_new_(rv, Bry_djvu, Id_djvu);
		id_hash_new_(rv, Bry_pdf, Id_pdf);
		id_hash_new_(rv, Bry_mid, Id_mid);
		id_hash_new_(rv, Bry_ogg, Id_ogg);
		id_hash_new_(rv, Bry_oga, Id_oga);
		id_hash_new_(rv, Bry_ogv, Id_ogv);
		id_hash_new_(rv, Bry_webm, Id_webm);
		return rv;
	}
	private static void id_hash_new_(HashAdp hash, byte[] key, int val) {hash.Add(key, IntVal.new_(val));}

	private static final Hash_adp_bry ext_hash = new Hash_adp_bry(false).Add_bry_bry(Bry_png).Add_bry_bry(Bry_jpg).Add_bry_bry(Bry_jpeg).Add_bry_bry(Bry_gif).Add_bry_bry(Bry_tif).Add_bry_bry(Bry_tiff).Add_bry_bry(Bry_svg).Add_bry_bry(Bry_djvu).Add_bry_bry(Bry_pdf).Add_bry_bry(Bry_mid).Add_bry_bry(Bry_ogg).Add_bry_bry(Bry_oga).Add_bry_bry(Bry_ogv).Add_bry_bry(Bry_webm);
	public static final int Ary_max = Id_webm + 1;
	private static final Xof_ext[] Ary = new Xof_ext[Ary_max];

	public static byte[] get_by_id(int id) {
		if (id < 0 || id >= Id_webm) throw Err_.new_fmt_("index out of bounds; {id}", id);
		return Bry__ary[id];
	}
	public static Xof_ext new_by_ttl_(byte[] ttl) {
		int ttl_len = ttl.length;
		int dot_pos = ByteAry_.FindBwd(ttl, Byte_ascii.Dot);
		byte[] ext = (dot_pos == ByteAry_.NotFound || dot_pos == ttl_len) ? ByteAry_.Empty : ByteAry_.Xto_str_lower(ttl, dot_pos + 1, ttl_len); // +1 to bgn after .
		return new_(Id_of_(ext), ext);
	}
	public static Xof_ext new_by_ext_(byte[] ext) {return new_(Id_of_(ext), ext);}
	public static Xof_ext new_(int id, byte[] ext) {
		Xof_ext rv = Ary[id];
		if (rv == null) {
			rv = new Xof_ext(id, ext);
			Ary[id] = rv;
		}
		return rv;
	}
	public static int Id_of_(byte[] ext_bry) {
		Object o = id_hash.Fetch(ext_bry);
		return o == null ? Id_unknown : ((IntVal)o).Val();
	}
	public static byte[] Lower_ext(byte[] ttl) {
		int dot_pos = ByteAry_.FindBwd(ttl, Byte_ascii.Dot);
		int ttl_len = ttl.length;
		if (dot_pos == ByteAry_.NotFound || dot_pos == ttl_len - 1) return ttl;
		Object o = ext_hash.Get_by_mid(ttl, dot_pos + 1, ttl_len);
		if (o == null) return ttl;
		byte[] ext = (byte[])o;
		boolean match = ByteAry_.Match(ttl, dot_pos, ttl_len, ext);
		if (match) return ttl;
		int ext_len = ext.length;
		for (int i = 0; i < ext_len; i++)
			ttl[i + dot_pos + 1] = ext[i];
		return ttl;
	}
	public static boolean Orig_file_is_img(int v) {	// identifies if orig_file can be used for <img src>; EX: png is valid, but svg, ogv, pdf is not
		switch (v) {
			case Id_png: case Id_jpg: case Id_jpeg:
			case Id_gif: case Id_tif: case Id_tiff:				return true;
			default:											return false;
		}
	}
}

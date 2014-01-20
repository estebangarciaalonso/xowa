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
package gplx.xowa.xtns.dynamicPageList; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
class Dpl_itm_keys {
	public static byte Parse(byte[] src, int bgn, int end, byte or) {
		Object o = keys.Get_by_mid(src, bgn, end);
		return o == null ? or : ((ByteVal)o).Val();
	}
	public static byte Parse(byte[] bry, byte or) {
		Object o = keys.Get_by_bry(bry);
		return o == null ? or : ((ByteVal)o).Val();
	}
	public static boolean Parse_as_bool(byte[] bry, boolean or) {
		byte key = Dpl_itm_keys.Parse(bry, Key_null);
		switch (key) {
			case Dpl_itm_keys.Key_false:	return false;
			case Dpl_itm_keys.Key_true:		return true;
			case Dpl_itm_keys.Key_null:		return or;
			default:						throw Err_mgr._.unhandled_(String_.new_utf8_(bry));	// shouldn't happen; should always go to or;
		}
	}
	public static final byte
	  Key_null = Byte_.MaxValue_127
	, Key_category = 1
	, Key_notcategory = 2
	, Key_ns = 3
	, Key_count = 4
	, Key_offset = 5
	, Key_imagewidth = 6
	, Key_imageheight = 7
	, Key_imagesperow = 8
	, Key_mode = 9
	, Key_gallery = 10
	, Key_none = 11
	, Key_ordered = 12
	, Key_unordered = 13
	, Key_inline = 14
	, Key_gallerycaption = 15
	, Key_galleryshowfilesize = 16
	, Key_galleryshowfilename = 17
	, Key_order = 18
	, Key_ordermethod = 19
	, Key_lastedit = 20
	, Key_length = 21
	, Key_created = 22
	, Key_sortkey = 23
	, Key_categorysortkey = 24
	, Key_popularity = 25
	, Key_categoryadd = 26
	, Key_redirects = 27
	, Key_include = 28
	, Key_only = 29
	, Key_exclude = 30
	, Key_stablepages = 31
	, Key_qualitypages = 32
	, Key_suppresserrors = 33
	, Key_addfirstcategorydate = 34
	, Key_shownamespace = 35
	, Key_googlehack = 36
	, Key_nofollow = 37
	, Key_descending = 38
	, Key_ascending = 39
	, Key_false = 40
	, Key_true = 41
	;
	private static final Hash_adp_bry keys = new Hash_adp_bry(false)
	.Add_str_byteVal("category", Dpl_itm_keys.Key_category)
	.Add_str_byteVal("notcategory", Dpl_itm_keys.Key_notcategory)
	.Add_str_byteVal("namespace", Dpl_itm_keys.Key_ns)
	.Add_str_byteVal("count", Dpl_itm_keys.Key_count)
	.Add_str_byteVal("offset", Dpl_itm_keys.Key_offset)
	.Add_str_byteVal("imagewidth", Dpl_itm_keys.Key_imagewidth)
	.Add_str_byteVal("imageheight", Dpl_itm_keys.Key_imageheight)
	.Add_str_byteVal("imagesperow", Dpl_itm_keys.Key_imagesperow)
	.Add_str_byteVal("mode", Dpl_itm_keys.Key_mode)
	.Add_str_byteVal("gallery", Dpl_itm_keys.Key_gallery)
	.Add_str_byteVal("none", Dpl_itm_keys.Key_none)
	.Add_str_byteVal("ordered", Dpl_itm_keys.Key_ordered)
	.Add_str_byteVal("unordered", Dpl_itm_keys.Key_unordered)
	.Add_str_byteVal("inline", Dpl_itm_keys.Key_inline)
	.Add_str_byteVal("gallerycaption", Dpl_itm_keys.Key_gallerycaption)
	.Add_str_byteVal("galleryshowfilesize", Dpl_itm_keys.Key_galleryshowfilesize)
	.Add_str_byteVal("galleryshowfilename", Dpl_itm_keys.Key_galleryshowfilename)
	.Add_str_byteVal("order", Dpl_itm_keys.Key_order)
	.Add_str_byteVal("ordermethod", Dpl_itm_keys.Key_ordermethod)
	.Add_str_byteVal("lastedit", Dpl_itm_keys.Key_lastedit)
	.Add_str_byteVal("length", Dpl_itm_keys.Key_length)
	.Add_str_byteVal("created", Dpl_itm_keys.Key_created)
	.Add_str_byteVal("sortkey", Dpl_itm_keys.Key_sortkey)
	.Add_str_byteVal("categorysortkey", Dpl_itm_keys.Key_categorysortkey)
	.Add_str_byteVal("popularity", Dpl_itm_keys.Key_popularity)
	.Add_str_byteVal("categoryadd", Dpl_itm_keys.Key_categoryadd)
	.Add_str_byteVal("redirects", Dpl_itm_keys.Key_redirects)
	.Add_str_byteVal("include", Dpl_itm_keys.Key_include)
	.Add_str_byteVal("only", Dpl_itm_keys.Key_only)
	.Add_str_byteVal("exclude", Dpl_itm_keys.Key_exclude)
	.Add_str_byteVal("stablepages", Dpl_itm_keys.Key_stablepages)
	.Add_str_byteVal("qualitypages", Dpl_itm_keys.Key_qualitypages)
	.Add_str_byteVal("suppresserrors", Dpl_itm_keys.Key_suppresserrors)
	.Add_str_byteVal("addfirstcategorydate", Dpl_itm_keys.Key_addfirstcategorydate)
	.Add_str_byteVal("shownamespace", Dpl_itm_keys.Key_shownamespace)
	.Add_str_byteVal("googlehack", Dpl_itm_keys.Key_googlehack)
	.Add_str_byteVal("nofollow", Dpl_itm_keys.Key_nofollow)
	.Add_str_byteVal("descending", Dpl_itm_keys.Key_descending)
	.Add_str_byteVal("ascending", Dpl_itm_keys.Key_ascending)
	.Add_str_byteVal("false", Dpl_itm_keys.Key_false)
	.Add_str_byteVal("true", Dpl_itm_keys.Key_true)
	;
}

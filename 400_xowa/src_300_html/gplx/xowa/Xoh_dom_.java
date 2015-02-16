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
import gplx.html.*;
public class Xoh_dom_ {
	private static final byte[] Lt_bry = Bry_.new_ascii_("<"), Space_bry = Bry_.new_ascii_(" ");
	public static byte[] Query_val_by_where(Xoh_find rv, byte[] src, byte[] where_nde, byte[] where_key, byte[] where_val, byte[] query_key, int bgn) {
		int src_len = src.length;		
		where_nde = Bry_.Add(Lt_bry, where_nde, Space_bry);
		while (true) {
			boolean where_val_found = Select_tag(rv, src, where_nde, where_key, bgn, src_len);
			if (where_val_found) {
				int tag_bgn = rv.Tag_bgn();
				int tag_end = rv.Tag_end();
				boolean where_val_match = Bry_.Match(src, rv.Val_bgn(), rv.Val_end(), where_val);
				if (where_val_match) {
					boolean query_val_found = Find_atr_val_in_tag(rv, src, query_key, tag_bgn, tag_end);
					if (query_val_found) {
						return Bry_.Mid(src, rv.Val_bgn(), rv.Val_end());
					}
					else
						return null;
				}
				else
					bgn = tag_end + 1;
			}
			else
				break;
		}
		return null;
	}
	public static boolean Select_tag(Xoh_find rv, byte[] src, byte[] nde, byte[] key, int rng_bgn, int rng_end) {
		int tag_bgn = Bry_finder.Find_fwd(src, nde, 		   rng_bgn, rng_end); 					if (tag_bgn == Bry_.NotFound) return false;
		int tag_end = Bry_finder.Find_fwd(src, Byte_ascii.Gt, tag_bgn, rng_end); 					if (tag_end == Bry_.NotFound) return false;
		int key_bgn = Bry_finder.Find_fwd(src, key, tag_bgn, tag_end);								if (key_bgn == Bry_.NotFound) return false;
		int key_end = key_bgn + key.length;
		int val_bgn = Bry_finder.Find_fwd(src, Byte_ascii.Quote, key_end, tag_end);					if (val_bgn == Bry_.NotFound) return false;
		++val_bgn;
		int val_end = Bry_finder.Find_fwd(src, Byte_ascii.Quote, val_bgn, tag_end);					if (val_end == Bry_.NotFound) return false;
		rv.Set_all(tag_bgn, tag_end, key_bgn, key_end, val_bgn, val_end);
		return true;
	}
	public static boolean Find_atr_val_in_tag(Xoh_find rv, byte[] src, byte[] key, int tag_bgn, int tag_end) {
		int key_bgn = Bry_finder.Find_fwd(src, key, tag_bgn, tag_end);								if (key_bgn == Bry_.NotFound) return false;
		int key_end = key_bgn + key.length;
		int val_bgn = Bry_finder.Find_fwd(src, Byte_ascii.Quote, key_end, tag_end);					if (val_bgn == Bry_.NotFound) return false;
		++val_bgn;
		int val_end = Bry_finder.Find_fwd(src, Byte_ascii.Quote, val_bgn, tag_end);					if (val_end == Bry_.NotFound) return false;
		rv.Set_all(tag_bgn, tag_end, key_bgn, key_end, val_bgn, val_end);
		return true;
	}
	public static String Title_by_href(Url_encoder encoder, Bry_bfr bfr, byte[] href_dec, byte[] html_src) {
		int slash_pos = Bry_finder.Find_bwd(href_dec, Byte_ascii.Slash);
		encoder.Encode(bfr, href_dec, slash_pos + 1, href_dec.length);
		byte[] name_enc = bfr.Xto_bry_and_clear();
		bfr.Add_mid(href_dec, 0, slash_pos + Int_.Const_dlm_len);	// include trailing slash			
		bfr.Add(name_enc);
		byte[] href_enc = bfr.Xto_bry_and_clear();			
		byte[] xowa_title = Xoh_dom_.Query_val_by_where(dom_find, html_src, Html_tag_.A_name_bry, Html_atr_.Href_bry, href_enc, gplx.xowa.html.Xoh_consts.Atr_xowa_title_bry, 0);
		return String_.new_utf8_(xowa_title);
	}	static final Xoh_find dom_find = new Xoh_find(); 
}

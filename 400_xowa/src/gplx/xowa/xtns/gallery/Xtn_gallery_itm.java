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
public class Xtn_gallery_itm {
	public Xoa_ttl Ttl() {return ttl;} public Xtn_gallery_itm Lnki_(Xoa_ttl v) {ttl = v; return this;} private Xoa_ttl ttl;
	public int Ttl_bgn() {return ttl_bgn;} public Xtn_gallery_itm Ttl_bgn_(int v) {ttl_bgn = v; return this;} private int ttl_bgn;
	public int Ttl_end() {return ttl_end;} public Xtn_gallery_itm Ttl_end_(int v) {ttl_end = v; return this;} private int ttl_end;
	public byte[] Caption_bry() {return caption_bry;} public Xtn_gallery_itm Caption_bry_(byte[] v) {caption_bry = v; return this;} private byte[] caption_bry;
	public int Alt_bgn() {return alt_bgn;} public Xtn_gallery_itm Alt_bgn_(int v) {alt_bgn = v; return this;} private int alt_bgn;
	public int Alt_end() {return alt_end;} public Xtn_gallery_itm Alt_end_(int v) {alt_end = v; return this;} private int alt_end;
	public int Link_bgn() {return link_bgn;} public Xtn_gallery_itm Link_bgn_(int v) {link_bgn = v; return this;} private int link_bgn;
	public int Link_end() {return link_end;} public Xtn_gallery_itm Link_end_(int v) {link_end = v; return this;} private int link_end;
	public Xop_lnki_tkn Lnki_tkn() {return lnki_tkn;} public Xtn_gallery_itm Lnki_tkn_(Xop_lnki_tkn v) {lnki_tkn = v; return this;} private Xop_lnki_tkn lnki_tkn;
	public Xtn_gallery_itm Reset() {
		ttl = null;
		ttl_bgn = ttl_end = alt_bgn = alt_end = link_bgn = link_end = ByteAry_.NotFound;
		caption_bry = null;
		return this;
	}
}

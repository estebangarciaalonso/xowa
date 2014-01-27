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
public class Gallery_itm {
	public Xoa_ttl Ttl() {return ttl;} public Gallery_itm Lnki_(Xoa_ttl v) {ttl = v; return this;} private Xoa_ttl ttl;
	public int Ttl_bgn() {return ttl_bgn;} public Gallery_itm Ttl_bgn_(int v) {ttl_bgn = v; return this;} private int ttl_bgn;
	public int Ttl_end() {return ttl_end;} public Gallery_itm Ttl_end_(int v) {ttl_end = v; return this;} private int ttl_end;
	public byte[] Caption_bry() {return caption_bry;} public Gallery_itm Caption_bry_(byte[] v) {caption_bry = v; return this;} private byte[] caption_bry;
	public Xop_root_tkn Caption_tkn() {return caption_tkn;} public Gallery_itm Caption_tkn_(Xop_root_tkn v) {caption_tkn = v; return this;} private Xop_root_tkn caption_tkn;
	public int Alt_bgn() {return alt_bgn;} public Gallery_itm Alt_bgn_(int v) {alt_bgn = v; return this;} private int alt_bgn;
	public int Alt_end() {return alt_end;} public Gallery_itm Alt_end_(int v) {alt_end = v; return this;} private int alt_end;
	public int Link_bgn() {return link_bgn;} public Gallery_itm Link_bgn_(int v) {link_bgn = v; return this;} private int link_bgn;
	public int Link_end() {return link_end;} public Gallery_itm Link_end_(int v) {link_end = v; return this;} private int link_end;
	public Xop_lnki_tkn Lnki_tkn() {return lnki_tkn;} public Gallery_itm Lnki_tkn_(Xop_lnki_tkn v) {lnki_tkn = v; return this;} private Xop_lnki_tkn lnki_tkn;
	public Gallery_itm Reset() {
		ttl = null;
		ttl_bgn = ttl_end = alt_bgn = alt_end = link_bgn = link_end = ByteAry_.NotFound;
		caption_bry = null;	// NOTE: use null instead of ""; more legible tests
		caption_tkn = null;
		return this;
	}
}

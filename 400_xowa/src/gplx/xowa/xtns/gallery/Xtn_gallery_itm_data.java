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
public class Xtn_gallery_itm_data {
	public int Lnki_bgn() {return lnki_bgn;} private int lnki_bgn;
	public int Lnki_end() {return lnki_end;} private int lnki_end;
	public Xtn_gallery_itm_data Lnki_rng_(int bgn, int end) {this.lnki_bgn = bgn; this.lnki_end = end; return this;}
	public int Html_bgn() {return html_bgn;} private int html_bgn;
	public int Html_end() {return html_end;} private int html_end;
	public Xtn_gallery_itm_data Html_rng_(int bgn, int end) {this.html_bgn = bgn; this.html_end = end; return this;}
	public void Src_rng_adj(int adj) {
		lnki_bgn = Src_adj(lnki_bgn, adj); lnki_end = Src_adj(lnki_end, adj);
		html_bgn = Src_adj(html_bgn, adj); html_end = Src_adj(html_end, adj);
	}
	public static int Src_adj(int val, int adj) {return val == -1 ? -1 : val + adj;}

	public Xoa_ttl Lnki() {return lnki;} public Xtn_gallery_itm_data Lnki_(Xoa_ttl v) {lnki = v; return this;} private Xoa_ttl lnki;
	public Xop_root_tkn Html_root() {return html_root;} public Xtn_gallery_itm_data Html_root_(Xop_root_tkn v) {html_root = v; return this;} private Xop_root_tkn html_root;
	public byte[] Html_data() {return html_data;} public Xtn_gallery_itm_data Html_data_(byte[] v) {html_data = v; return this;} private byte[] html_data = ByteAry_.Empty;	// NOTE: need to extract copy of html.Src() b/c <gallery> is xtn and node never gets parsed;
}

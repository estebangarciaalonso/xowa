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
import gplx.xowa.html.modules.*;
public class Xoa_html_data {
	private OrderedHash ctg_hash;
	public byte[]		Content_sub() {return html_content_sub;} public void Content_sub_(byte[] v) {html_content_sub = v;} private byte[] html_content_sub;
	public int			Lnke_autonumber_next() {return lnke_autonumber++;} private int lnke_autonumber = 1;
	public String		Bmk_pos() {return html_bmk_pos;} public void Bmk_pos_(String v) {html_bmk_pos = v;} private String html_bmk_pos;
	public boolean			Restricted() {return html_restricted;} public void Restricted_n_() {html_restricted = false;} private boolean html_restricted = true;
	public Xoh_module_list Modules() {return modules;} private Xoh_module_list modules = new Xoh_module_list();
	public void Clear() {
		if (ctg_hash != null) ctg_hash.Clear();
		lnke_autonumber = 1;
		html_restricted = true;
		html_content_sub = ByteAry_.Empty;
		modules.Clear();
	}
	public byte[][] Ctgs_to_ary() {return ctg_hash == null ? ByteAry_.Ary_empty : (byte[][])ctg_hash.XtoAry(byte[].class);}
	public void Ctgs_add(Xoa_ttl ttl) {
		if (ctg_hash == null) ctg_hash = OrderedHash_.new_bry_();
		byte[] ttl_bry = ttl.Page_txt();
		if (ctg_hash.Has(ttl_bry)) return;
		ctg_hash.Add(ttl_bry, ttl_bry);
	}
}

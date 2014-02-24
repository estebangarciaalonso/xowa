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
public class Xoh_ctx {
	private OrderedHash ctg_hash = OrderedHash_.new_bry_();
	public boolean Lnki_title() {return lnki_title;} public Xoh_ctx Lnki_title_(boolean v) {lnki_title = v; return this;} private boolean lnki_title;
	public boolean Lnki_visited() {return lnki_visited;} public Xoh_ctx Lnki_visited_(boolean v) {lnki_visited = v; return this;} private boolean lnki_visited;
	public boolean Lnki_id() {return lnki_id;} public Xoh_ctx Lnki_id_(boolean v) {lnki_id = v; return this;} private boolean lnki_id;
	public boolean Toc_show() {return toc_show;} public Xoh_ctx Toc_show_(boolean v) {toc_show = v; return this;} private boolean toc_show;
	public int Lnke_autonumber_next() {return lnke_autonumber++;} private int lnke_autonumber = 1;
	public void Clear() {
		lnke_autonumber = 1;
		ctg_hash.Clear();
	}
	public void Lnki_ctg_add(byte[] ttl) {
		if (ctg_hash.Has(ttl)) return;
		ctg_hash.Add(ttl, ttl);
	}
	public byte[][] Lnki_ctg_xto_ary() {return (byte[][])ctg_hash.XtoAry(byte[].class);}
}

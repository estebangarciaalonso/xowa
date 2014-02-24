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
package gplx.xowa.files.qrys.fs_roots; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*; import gplx.xowa.files.qrys.*;
class Orig_fil_mgr {
	private OrderedHash hash = OrderedHash_.new_bry_();
	public boolean Case_match() {return case_match;} public Orig_fil_mgr Case_match_(boolean v) {case_match = v; return this;} private boolean case_match;
	public boolean Has(byte[] lnki_ttl) {return hash.Has(key_bld(lnki_ttl));}
	public Orig_fil_itm Get_by_ttl(byte[] lnki_ttl) {return (Orig_fil_itm)hash.Fetch(key_bld(lnki_ttl));}
	public void Add(Orig_fil_itm fil) {hash.Add(key_bld(fil.Fil_name()), fil);}
	private byte[] key_bld(byte[] v) {return case_match ? v : ByteAry_.Lower_ascii(ByteAry_.Copy(v));}
}
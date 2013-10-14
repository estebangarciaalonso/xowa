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
package gplx.xowa.files.main.orig; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*; import gplx.xowa.files.main.*;
public class Xof_orig_fil_itm {
	public int Id() {return id;} public Xof_orig_fil_itm Id_(int v) {id = v; return this;} private int id;
	public byte[] Ttl() {return ttl;} public Xof_orig_fil_itm Ttl_(byte[] v) {ttl = v; return this;} private byte[] ttl;
	public byte Status() {return status;} public Xof_orig_fil_itm Status_(byte v) {status = v; return this;} private byte status;
	public byte Orig_repo() {return orig_repo;} public Xof_orig_fil_itm Orig_repo_(byte v) {orig_repo = v; return this;} private byte orig_repo;
	public int Orig_w() {return orig_w;} public Xof_orig_fil_itm Orig_w_(int v) {orig_w = v; return this;} private int orig_w;
	public int Orig_h() {return orig_h;} public Xof_orig_fil_itm Orig_h_(int v) {orig_h = v; return this;} private int orig_h;
	public int Orig_ext() {return orig_ext;} public Xof_orig_fil_itm Orig_ext_(int v) {orig_ext = v; return this;} private int orig_ext;
	public byte[] Orig_redirect() {return orig_redirect;} public Xof_orig_fil_itm Orig_redirect_(byte[] v) {orig_redirect = v; return this;} private byte[] orig_redirect;
	public static Xof_orig_fil_itm load_(DataRdr rdr) {
		Xof_orig_fil_itm rv = new Xof_orig_fil_itm();
		rv.ttl = rdr.ReadBryByStr(Xof_orig_fil_tbl.Fld_fo_ttl);
		rv.status = rdr.ReadByte(Xof_orig_fil_tbl.Fld_fo_status);
		rv.orig_repo = rdr.ReadByte(Xof_orig_fil_tbl.Fld_fo_orig_repo);
		rv.orig_w = rdr.ReadInt(Xof_orig_fil_tbl.Fld_fo_orig_w);
		rv.orig_h = rdr.ReadInt(Xof_orig_fil_tbl.Fld_fo_orig_h);
		rv.orig_ext = rdr.ReadInt(Xof_orig_fil_tbl.Fld_fo_orig_ext);
		rv.orig_redirect = rdr.ReadBryByStr(Xof_orig_fil_tbl.Fld_fo_orig_redirect);
		return rv;
	}
}

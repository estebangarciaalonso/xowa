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
package gplx.xowa.files.qrys.dirs; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*; import gplx.xowa.files.qrys.*;
class Xoq_fil {
	public Xoq_fil(int uid, byte[] lnki_ttl, int lnki_ext, int orig_w, int orig_h) {
		this.uid = uid; this.lnki_ttl = lnki_ttl; this.lnki_ext = lnki_ext; this.orig_w = orig_w; this.orig_h = orig_h;
	}
	public int Uid() {return uid;} public Xoq_fil Uid_(int v) {uid = v; return this;} private int uid;
	public byte[] Lnki_ttl() {return lnki_ttl;} public Xoq_fil Lnki_ttl_(byte[] v) {lnki_ttl = v; return this;} private byte[] lnki_ttl;
	public int Lnki_ext() {return lnki_ext;} public Xoq_fil Lnki_ext_(int v) {lnki_ext = v; return this;} private int lnki_ext;
	public int Orig_w() {return orig_w;} public Xoq_fil Orig_w_(int v) {orig_w = v; return this;} private int orig_w;
	public int Orig_h() {return orig_h;} public Xoq_fil Orig_h_(int v) {orig_h = v; return this;} private int orig_h;
	public void Orig_size_(int w, int h) {this.orig_w = w; this.orig_h = h;}
	public Io_url Orig_url() {return orig_url;} public Xoq_fil Orig_url_(Io_url v) {orig_url = v; return this;} private Io_url orig_url;
	public static final Xoq_fil Null = null;
}

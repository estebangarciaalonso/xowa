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
package gplx.fsdb; import gplx.*;
public class Fsdb_bin_itm {
	public Fsdb_bin_itm(int owner, byte[] data) {this.owner = owner; this.data = data;}
	public int Owner() {return owner;} public Fsdb_bin_itm Owner_(int v) {owner = v; return this;} private int owner;
	public byte[] Data() {return data;} public Fsdb_bin_itm Data_(byte[] v) {data = v; return this;} private byte[] data;
        public static final Fsdb_bin_itm Null = new Fsdb_bin_itm(0, ByteAry_.Empty);
}

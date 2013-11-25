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
public class Fsdb_xtn_thm_itm {
	public int Id() {return id;} public Fsdb_xtn_thm_itm Id_(int v) {id = v; return this;} private int id;
	public int Owner() {return owner;} public Fsdb_xtn_thm_itm Owner_(int v) {owner = v; return this;} private int owner;
	public int Width() {return width;} public Fsdb_xtn_thm_itm Width_(int v) {width = v; return this;} private int width;
	public int Thumbtime() {return thumbtime;} public Fsdb_xtn_thm_itm Thumbtime_(int v) {thumbtime = v; return this;} private int thumbtime;
	public int Height() {return height;} public Fsdb_xtn_thm_itm Height_(int v) {height = v; return this;} private int height;
	public long Size() {return size;} public Fsdb_xtn_thm_itm Size_(long v) {size = v; return this;} private long size;
	public String Modified() {return modified;} public Fsdb_xtn_thm_itm Modified_(String v) {modified = v; return this;} private String modified;
	public String Hash() {return hash;} public Fsdb_xtn_thm_itm Hash_(String v) {hash = v; return this;} private String hash;
	public int Dir_id() {return dir_id;} public Fsdb_xtn_thm_itm Dir_id_(int v) {dir_id = v; return this;} private int dir_id;
	public int Db_bin_id() {return bin_db_id;} public Fsdb_xtn_thm_itm Db_bin_id_(int v) {bin_db_id = v; return this;} private int bin_db_id;
	public int Mnt_id() {return mnt_id;} public Fsdb_xtn_thm_itm Mnt_id_(int v) {mnt_id = v; return this;} private int mnt_id;
	public static Fsdb_xtn_thm_itm load_(DataRdr rdr) {
		Fsdb_xtn_thm_itm rv = new Fsdb_xtn_thm_itm();
		rv.id = rdr.ReadInt(Fsdb_xtn_thm_tbl.Fld_thm_id);
		rv.owner = rdr.ReadInt(Fsdb_xtn_thm_tbl.Fld_thm_owner_id);
		rv.width = rdr.ReadInt(Fsdb_xtn_thm_tbl.Fld_thm_w);
		rv.thumbtime = rdr.ReadInt(Fsdb_xtn_thm_tbl.Fld_thm_thumbtime);
		rv.height = rdr.ReadInt(Fsdb_xtn_thm_tbl.Fld_thm_h);
		rv.size = rdr.ReadLong(Fsdb_xtn_thm_tbl.Fld_thm_size);
		rv.modified = rdr.ReadStr(Fsdb_xtn_thm_tbl.Fld_thm_modified);
		rv.hash = rdr.ReadStr(Fsdb_xtn_thm_tbl.Fld_thm_hash);
		rv.bin_db_id = rdr.ReadInt(Fsdb_xtn_thm_tbl.Fld_thm_bin_db_id);
		return rv;
	}
	public static Fsdb_xtn_thm_itm new_() {return new Fsdb_xtn_thm_itm();}
	public static final Fsdb_xtn_thm_itm Null = new Fsdb_xtn_thm_itm();
	public static final Fsdb_xtn_thm_itm[] Ary_empty = new Fsdb_xtn_thm_itm[0];
}

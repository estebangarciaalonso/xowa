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
	public String Sha1() {return sha1;} public Fsdb_xtn_thm_itm Sha1_(String v) {sha1 = v; return this;} private String sha1;
	public int Dir_id() {return dir_id;} public Fsdb_xtn_thm_itm Dir_id_(int v) {dir_id = v; return this;} private int dir_id;
	public Fsdb_xtn_thm_itm Init_by_db(int id, int owner, int w, int thumbtime, int h, long size, String modified, String sha1) {
		this.id = id; this.owner = owner; this.width = w; this.thumbtime = thumbtime; this.height = h; this.size = size; this.modified = modified; this.sha1 = sha1;
		return this;
	}
	public static Fsdb_xtn_thm_itm new_() {return new Fsdb_xtn_thm_itm();}
        public static final Fsdb_xtn_thm_itm Null = new Fsdb_xtn_thm_itm();
	public static final Fsdb_xtn_thm_itm[] Ary_empty = new Fsdb_xtn_thm_itm[0];
}

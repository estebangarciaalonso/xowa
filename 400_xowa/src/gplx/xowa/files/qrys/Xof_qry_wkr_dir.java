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
package gplx.xowa.files.qrys; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.xowa.files.fsdb.*;
public class Xof_qry_wkr_dir implements Xof_qry_wkr {
//		private Xow_wiki wiki;
	public Xof_qry_wkr_dir(Xow_wiki wiki) {}//this.wiki = wiki;}
	public byte Tid() {return Xof_qry_wkr_.Tid_dir;}
	private ListAdp dirs = ListAdp_.new_();
	private OrderedHash fils = OrderedHash_.new_bry_();
	public boolean Qry_file(Xof_fsdb_itm itm) {
		byte[] lnki_ttl = itm.Lnki_ttl();
		Xof_qry_fil qry_fil = (Xof_qry_fil)fils.Fetch(lnki_ttl);
		if (qry_fil == null) qry_fil = Qry_dirs(lnki_ttl);
		if (qry_fil == null) return false;
		itm.Orig_size_(qry_fil.Orig_w(), qry_fil.Orig_h());
		return true;
	}
	private Xof_qry_fil Qry_dirs(byte[] lnki_ttl) {
		int dirs_len = dirs.Count();
		for (int i = 0; i < dirs_len; i++) {
			Xof_qry_dir qry_dir = (Xof_qry_dir)dirs.FetchAt(i);
			Xof_qry_fil rv = (Xof_qry_fil)qry_dir.Get_by_ttl(lnki_ttl);
			if (rv != null) return rv;
		}
		return null;
	}
}
class Xof_qry_dir {
	public int Uid() {return uid;} public Xof_qry_dir Uid_(int v) {uid = v; return this;} private int uid;
	public boolean Recurse() {return recurse;} public Xof_qry_dir Recurse_(boolean v) {recurse = v; return this;} private boolean recurse;
	public int Sort_idx() {return sort_idx;} public Xof_qry_dir Sort_idx_(int v) {sort_idx = v; return this;} private int sort_idx;
	public Io_url Url() {return url;} public Xof_qry_dir Url_(Io_url v) {url = v; return this;} private Io_url url;
	public Xof_qry_dir_fil_mgr Fil_mgr() {return fil_mgr;} private Xof_qry_dir_fil_mgr fil_mgr = new Xof_qry_dir_fil_mgr();
//		public boolean Loaded() {return loaded;} public Xof_qry_dir Loaded_(boolean v) {loaded = v; return this;} private boolean loaded;
//		public void Load() {
//			//if (!qry_dir.Loaded()) qry_dir.Load();
//		}
	public Xof_qry_fil Get_by_ttl(byte[] lnki_ttl) {
		Xof_qry_fil rv = (Xof_qry_fil)fil_mgr.Get_by_ttl(lnki_ttl);
		if (rv != null) {
			if (Io_mgr._.ExistsFil(rv.Url())) return rv;
			// db.Delete(rv);
			rv = null;
		}
		// if (!fsys_scanned) Fsys_scan()
		rv = (Xof_qry_fil)fil_mgr.Get_by_ttl(lnki_ttl);
		/*
		check wiki_orig
		if found, return it
		check directory
		size image
		register in wiki orig
		/image_hive/orig/A.png
		/image_hive/thumb/
		
		*/
		return null;
	}
}
class Xof_qry_dir_fil_mgr {
	public Xof_qry_fil Get_by_ttl(byte[] lnki_ttl) {
		return null;
	}
}
class Xof_qry_fil {
	public int Uid() {return uid;} public Xof_qry_fil Uid_(int v) {uid = v; return this;} private int uid;
	public byte[] Lnki_ttl() {return lnki_ttl;} public Xof_qry_fil Lnki_ttl_(byte[] v) {lnki_ttl = v; return this;} private byte[] lnki_ttl;
	public int Lnki_ext() {return lnki_ext;} public Xof_qry_fil Lnki_ext_(int v) {lnki_ext = v; return this;} private int lnki_ext;
	public int Orig_w() {return orig_w;} public Xof_qry_fil Orig_w_(int v) {orig_w = v; return this;} private int orig_w;
	public int Orig_h() {return orig_h;} public Xof_qry_fil Orig_h_(int v) {orig_h = v; return this;} private int orig_h;
	public Io_url Url() {return url;} public Xof_qry_fil Url_(Io_url v) {url = v; return this;} private Io_url url;
}

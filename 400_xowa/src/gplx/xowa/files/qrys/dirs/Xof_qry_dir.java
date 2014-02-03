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
import gplx.gfui.*;
class Xoq_dir {
	private Xof_img_wkr_query_img_size img_size_wkr;
	private int fil_id_next = 0;
	public int Uid() {return uid;} public Xoq_dir Uid_(int v) {uid = v; return this;} private int uid;
	public Io_url Url() {return url;} public Xoq_dir Url_(Io_url v) {url = v; return this;} private Io_url url;
	public boolean Recurse() {return recurse;} public Xoq_dir Recurse_(boolean v) {recurse = v; return this;} private boolean recurse;
	public boolean Case_match() {return case_match;} public Xoq_dir Case_match_(boolean v) {case_match = v; return this;} private boolean case_match;
	public int Sort_idx() {return sort_idx;} public Xoq_dir Sort_idx_(int v) {sort_idx = v; return this;} private int sort_idx;
	private Xoq_fil_mgr db_fil_mgr = new Xoq_fil_mgr(), fs_fil_mgr;
	private Gfo_usr_dlg usr_dlg;
	public void Init(Gfo_usr_dlg usr_dlg, Xof_img_wkr_query_img_size img_size_wkr) {
		this.usr_dlg = usr_dlg;
		this.img_size_wkr = img_size_wkr;
		// load db_fil_mgr from db
		// Xof_wiki_orig_itm regy_itm = Xof_wiki_orig_tbl.Select_itm(p, lnki_ttl);
		// Xof_wiki_orig_tbl.Select_itm_exists(win_wtr, img_regy_provider, exec_tid, itms, hash, url_bldr, bin_mgr.Repo_mgr());
	}
	public Xoq_fil Get_by_ttl(byte[] lnki_ttl) {
		Xoq_fil rv = (Xoq_fil)db_fil_mgr.Get_by_ttl(lnki_ttl);
		if (rv != null) {
			rv = Read_from_fsys(lnki_ttl); if (rv == null) return Xoq_fil.Null;
			Save_to_db(rv);
			db_fil_mgr.Add(rv);
		}
		return rv;
	}
	private Xoq_fil Read_from_fsys(byte[] file_name_bry) {
		if (fs_fil_mgr == null) fs_fil_mgr = Init_fs_fil_mgr();
		Xoq_fil rv = fs_fil_mgr.Get_by_ttl(file_name_bry);
		if (Xof_ext_.Id_is_image(rv.Lnki_ext())) {
			SizeAdp img_size = img_size_wkr.Exec(rv.Orig_url());
			rv.Orig_size_(img_size.Width(), img_size.Height());
		}
		return rv;
	}
	private void Save_to_db(Xoq_fil fil) {
		/*
		Xof_wiki_orig_tbl.Insert(p, lnki_ttl, Xof_wiki_orig_wkr_.Tid_found_orig, 0, ByteAry_.Eq, ext_id, orig_w, orig_h);
		*/			
	}
	private Xoq_fil_mgr Init_fs_fil_mgr() {
		Xoq_fil_mgr rv = new Xoq_fil_mgr().Case_match_(case_match);
		Io_url[] fils = Io_mgr._.QueryDir_args(url).Recur_(recurse).ExecAsUrlAry();
		int fils_len = fils.length;
		for (int i = 0; i < fils_len; i++) {
			Io_url fil = fils[i];
			byte[] fil_name_bry = ByteAry_.new_utf8_(fil.NameOnly());
			Xoq_fil fil_itm = rv.Get_by_ttl(fil_name_bry);
			if (fil_itm != Xoq_fil.Null) {
				usr_dlg.Warn_many("", "", "file already exists: cur=~{0} new=~{1}", fil_itm.Orig_url().Raw(), fil.Raw());
				continue;
			}
			Xof_ext ext = Xof_ext_.new_by_ext_(ByteAry_.new_utf8_(fil.Ext()));
			fil_itm = new Xoq_fil(fil_id_next++, fil_name_bry, ext.Id(), 0, 0, fil);
			rv.Add(fil_itm);
		}
		return rv;
	}
}
class Xoq_fil_mgr {
	private OrderedHash hash = OrderedHash_.new_bry_();
	public boolean Case_match() {return case_match;} public Xoq_fil_mgr Case_match_(boolean v) {case_match = v; return this;} private boolean case_match;
	public boolean Has(byte[] lnki_ttl) {return hash.Has(key_bld(lnki_ttl));}
	public Xoq_fil Get_by_ttl(byte[] lnki_ttl) {return (Xoq_fil)hash.Fetch(key_bld(lnki_ttl));}
	public void Add(Xoq_fil fil) {hash.Add(key_bld(fil.Lnki_ttl()), fil);}
	private byte[] key_bld(byte[] v) {return case_match ? v : ByteAry_.Lower_ascii(v);}
}
class Xoq_fil {
	public Xoq_fil(int uid, byte[] lnki_ttl, int lnki_ext, int orig_w, int orig_h, Io_url orig_url) {
		this.uid = uid; this.lnki_ttl = lnki_ttl; this.lnki_ext = lnki_ext; this.orig_w = orig_w; this.orig_h = orig_h; this.orig_url = orig_url;
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

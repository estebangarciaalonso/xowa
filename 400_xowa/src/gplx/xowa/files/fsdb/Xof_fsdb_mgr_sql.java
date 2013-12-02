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
package gplx.xowa.files.fsdb; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.dbs.*; import gplx.fsdb.*; import gplx.xowa.files.wiki_orig.*; import gplx.xowa.files.bins.*; import gplx.xowa.files.qrys.*; import gplx.xowa.files.fsdb.caches.*;
public class Xof_fsdb_mgr_sql implements Xof_fsdb_mgr, GfoInvkAble {
	private Db_provider img_regy_provider = null;		
	private Io_url fs_dir;
	private Xof_url_bldr url_bldr = new Xof_url_bldr();
	public boolean Tid_is_mem() {return false;}
	public Xof_qry_mgr Qry_mgr() {return qry_mgr;} private Xof_qry_mgr qry_mgr = new Xof_qry_mgr();
	public Xof_bin_mgr Bin_mgr() {return bin_mgr;} private Xof_bin_mgr bin_mgr;
	public Xof_bin_wkr Bin_wkr_fsdb() {return bin_wkr_fsdb;} private Xof_bin_wkr_fsdb_sql bin_wkr_fsdb;
	public void Db_bin_max_(long v) {mnt_mgr.Bin_db_max_(v);}
	public Fsdb_mnt_mgr Mnt_mgr() {return mnt_mgr;} private Fsdb_mnt_mgr mnt_mgr = new Fsdb_mnt_mgr();
	public Xof_fsdb_mgr_sql Db_dir_(Io_url v) {db_dir = v; mnt_mgr.Init(db_dir); return this;} private Io_url db_dir;
	public Gfo_usr_dlg Usr_dlg() {return usr_dlg;} Gfo_usr_dlg usr_dlg = Gfo_usr_dlg_.Null;
	public Cache_mgr Cache_mgr() {return cache_mgr;} private Cache_mgr cache_mgr;
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public boolean Init_by_wiki(Xow_wiki wiki) {
		if (init) return false;
		this.wiki = wiki;
		usr_dlg = wiki.App().Usr_dlg();
		mnt_mgr.Usr_dlg_(usr_dlg);
		init = true;
		Xow_repo_mgr repo_mgr = wiki.File_mgr().Repo_mgr();
		Init_by_wiki(wiki, wiki.App().Fsys_mgr().File_dir().GenSubDir(wiki.Domain_str()), wiki.App().Fsys_mgr().File_dir(), repo_mgr);
		Xof_qry_wkr_xowa_reg qry_xowa = new Xof_qry_wkr_xowa_reg(img_regy_provider);
//			Xof_qry_wkr_xowa qry_xowa = new Xof_qry_wkr_xowa(new Xof_wiki_finder(wiki.App().Wiki_mgr().Get_by_key_or_make(Xow_wiki_.Domain_commons_bry), wiki), new gplx.xowa.files.qrys.Xof_img_meta_wkr_xowa());
		Xof_qry_wkr_wmf_api qry_wmf_api = new Xof_qry_wkr_wmf_api(wiki, new Xof_img_wkr_api_size_base_wmf());
		qry_mgr.Wkrs_(qry_xowa, qry_wmf_api);
		wiki.Rls_list().Add(this);
		bin_mgr.Resizer_(wiki.App().File_mgr().Img_mgr().Wkr_resize_img());
		return true;
	}
	public void Init_by_wiki(Xow_wiki wiki, Io_url db_dir, Io_url fs_dir, Xow_repo_mgr repo_mgr) {
		this.fs_dir = fs_dir;
		this.db_dir = db_dir;
		img_regy_provider = Init_wiki_orig_provider(db_dir);
		mnt_mgr.Init(db_dir);
		bin_mgr = new Xof_bin_mgr(wiki, this, repo_mgr);
		bin_wkr_fsdb = new Xof_bin_wkr_fsdb_sql(this).Bin_bfr_len_(64 * Io_mgr.Len_kb);	// most thumbs are 40 kb
		cache_mgr = wiki.App().File_mgr().Cache_mgr();
	}	private boolean init = false;
	public void Reg_select(Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms) {
		Xof_wiki_orig_tbl.Select_list(img_regy_provider, exec_tid, itms, url_bldr, bin_mgr.Repo_mgr());
		Xof_fsdb_mgr_utl._.Fsdb_search(this, fs_dir, win_wtr, exec_tid, itms, bin_mgr.Repo_mgr(), url_bldr);
	}
	public Fsdb_db_bin_fil Bin_db_get(int mnt_id, int bin_db_id) {
		return mnt_mgr.Bin_db_get(mnt_id, bin_db_id);
	}
	public Fsdb_fil_itm Fil_select_bin(byte[] dir, byte[] fil, boolean is_thumb, int width, int thumbtime) {return mnt_mgr.Fil_select_bin(dir, fil, is_thumb, width, thumbtime);}
	public Fsdb_xtn_thm_itm Thm_select_bin(byte[] dir, byte[] fil, int width, int thumbtime)			{return mnt_mgr.Thm_select_bin(dir, fil, width, thumbtime);}
	public void Reg_insert(Xof_fsdb_itm itm, byte repo_id, byte status) {
		byte[] orig_page = itm.Orig_ttl();
		if (!Xof_wiki_orig_tbl.Select_itm_exists(img_regy_provider, orig_page))
			Xof_wiki_orig_tbl.Insert(img_regy_provider, orig_page, status, repo_id, itm.Orig_redirect(), itm.Lnki_ext().Id(), itm.Orig_w(), itm.Orig_h());
	}
	public void Fil_insert(Fsdb_fil_itm rv    , byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		mnt_mgr.Fil_insert(rv, dir, fil, ext_id, modified, hash, bin_len, bin_rdr);
	}
	public void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int w, int h, int thumbtime, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		mnt_mgr.Thm_insert(rv, dir, fil, ext_id, w, h, thumbtime, modified, hash, bin_len, bin_rdr);
	}
	public void Img_insert(Fsdb_xtn_img_itm rv, byte[] dir, byte[] fil, int ext_id, int img_w, int img_h, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		mnt_mgr.Img_insert(rv, dir, fil, ext_id, modified, hash, bin_len, bin_rdr, img_w, img_h);
	}
	public void Txn_open() {
		mnt_mgr.Txn_open();
		img_regy_provider.Txn_mgr().Txn_bgn_if_none();
	}
	public void Txn_save() {
		mnt_mgr.Txn_save();
		img_regy_provider.Txn_mgr().Txn_end_all();
	}
	public void Rls() {
		mnt_mgr.Rls();
		img_regy_provider.Rls();
	}
	public static Db_provider Init_wiki_orig_provider(Io_url root_dir) {
		BoolRef created = BoolRef.n_();
		Db_provider rv = Sqlite_engine_.Provider_load_or_make_(root_dir.GenSubFil("wiki.orig#00.sqlite3"), created);
		if (created.Val())
			Xof_wiki_orig_tbl.Create_table(rv);
		return rv;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_mnt_mgr))	return mnt_mgr;
		else	return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_mnt_mgr = "mnt_mgr";
}

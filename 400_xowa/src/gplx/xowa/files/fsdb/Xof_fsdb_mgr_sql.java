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
import gplx.dbs.*; import gplx.fsdb.*; import gplx.xowa.files.regs.*; import gplx.xowa.files.bins.*; import gplx.xowa.files.qrys.*;
public class Xof_fsdb_mgr_sql implements Xof_fsdb_mgr, RlsAble {
	private Fsdb_mnt_itm[] mnt_itms; private int mnt_itms_len;
	private Db_provider mnt_regy_provider = null; private Db_provider img_regy_provider = null;
	private Io_url fs_dir;
	public boolean Tid_is_mem() {return false;}
	public Xof_qry_mgr Qry_mgr() {return qry_mgr;} private Xof_qry_mgr qry_mgr = new Xof_qry_mgr();
	public Xof_bin_mgr Bin_mgr() {return bin_mgr;} private Xof_bin_mgr bin_mgr;
	public Xof_bin_wkr Bin_wkr_fsdb() {return bin_wkr_fsdb;} private Xof_bin_wkr_fsdb_sql bin_wkr_fsdb;
	private Xof_url_bldr url_bldr = new Xof_url_bldr();
	public void Init_by_wiki(Xow_wiki wiki) {
		Xow_repo_mgr repo_mgr = wiki.File_mgr().Repo_mgr();
		Init_by_wiki(wiki.App().Fsys_mgr().File_dir().GenSubDir(wiki.Domain_str()), wiki.App().Fsys_mgr().File_dir(), repo_mgr);
		Xof_qry_wkr_wmf_api qry_wmf_api = new Xof_qry_wkr_wmf_api(wiki, new Xof_img_wkr_api_size_base_wmf());
		qry_mgr.Wkrs_(qry_wmf_api);
		Xof_bin_wkr_wmf_xfer bin_wmf_xfer = new Xof_bin_wkr_wmf_xfer(repo_mgr);
		bin_mgr.Wkrs_(bin_wmf_xfer);
		wiki.Rls_list().Add(this);
		bin_mgr.Resizer_(wiki.App().File_mgr().Img_mgr().Wkr_resize_img());
	}
	public void Init_by_wiki(Io_url db_dir, Io_url fs_dir, Xow_repo_mgr repo_mgr) {
		if (init) return;
		init = true;
		this.fs_dir = fs_dir;
		Init_img_regy_provider(db_dir);
		Init_mnt_regy_provider(db_dir);
		bin_mgr = new Xof_bin_mgr(repo_mgr);
		bin_wkr_fsdb = new Xof_bin_wkr_fsdb_sql(this);
	}	private boolean init = false;
	public Fsdb_vlm_db_data Vlm_find(byte[] ttl) {
		Fsdb_vlm_db_data rv = null;
		for (int i = 0; i < mnt_itms_len; i++) {
			Fsdb_mnt_itm mnt = mnt_itms[i];
			rv = mnt.Vlm_find(ttl);
			if (rv != null) return rv;
		}
		return null;
	}
	public void Reg_select(Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms) {
		int itms_len = itms.Count();
		Xof_reg_fil_tbl.Select_list(img_regy_provider, exec_tid, itms, url_bldr, bin_mgr.Repo_mgr());
		Xof_fsdb_mgr_utl._.Fsdb_search(this, fs_dir, win_wtr, exec_tid, itms, itms_len, bin_mgr.Repo_mgr(), url_bldr);
	}
	public void Reg_insert(Xof_fsdb_itm itm, byte repo_id, byte status) {
		byte[] orig_page = itm.Orig_page();
		if (!Xof_reg_fil_tbl.Select_itm_exists(img_regy_provider, orig_page))
			Xof_reg_fil_tbl.Insert(img_regy_provider, itm.Reg_id(), orig_page, status, repo_id, itm.Orig_redirect(), itm.Lnki_ext().Id(), itm.Orig_w(), itm.Orig_h());
	}
	public int Thm_insert_idx() {return mnt_insert_idx;} private int mnt_insert_idx = 0;
	public void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int w, int thumbtime, int h, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		mnt_itms[mnt_insert_idx].Thm_insert(rv, dir, fil, ext_id, w, thumbtime, h, modified, hash, bin_len, bin_rdr);
	}
	public void Img_insert(Fsdb_xtn_img_itm rv, byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, int bin_len, gplx.ios.Io_stream_rdr bin_rdr, int img_w, int img_h, int img_bits) {
		mnt_itms[mnt_insert_idx].Img_insert(rv, dir, fil, ext_id, modified, hash, bin_len, bin_rdr, img_w, img_h, img_bits);
	}
	public void Rls() {
		img_regy_provider.Rls();
		mnt_regy_provider.Rls();
		for (int i = 0; i < mnt_itms_len; i++) 
			mnt_itms[i].Rls();
	}
	private void Init_img_regy_provider(Io_url root_dir) {
		BoolRef created = BoolRef.false_();
		img_regy_provider = Sqlite_engine_.Provider_load_or_make_(root_dir.GenSubFil("img_regy.sqlite3"), created);
		if (created.Val()) {
			Xof_reg_fil_tbl.Create_table(img_regy_provider);
		}
	}
	private void Init_mnt_regy_provider(Io_url root_dir) {
		BoolRef created = BoolRef.false_();
		mnt_regy_provider = Sqlite_engine_.Provider_load_or_make_(root_dir.GenSubFil("mnt_regy.sqlite3"), created);
		if (created.Val()) {
			Fsdb_mnt_tbl.Create_table(mnt_regy_provider);
			Fsdb_mnt_tbl.Insert(mnt_regy_provider, "main", "main.sqlite3");
		}
		mnt_itms = Fsdb_mnt_tbl.Select_all(mnt_regy_provider, root_dir);
		mnt_itms_len = mnt_itms.length;
		for (int i = 0; i < mnt_itms_len; i++)
			mnt_itms[i].Init_vlm_mgr(root_dir.GenSubDir("main"));
	}
}

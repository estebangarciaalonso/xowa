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
	private Db_provider img_regy_provider = null;
	private Io_url fs_dir;
	public boolean Tid_is_mem() {return false;}
	public Xof_qry_mgr Qry_mgr() {return qry_mgr;} private Xof_qry_mgr qry_mgr = new Xof_qry_mgr();
	public Xof_bin_mgr Bin_mgr() {return bin_mgr;} private Xof_bin_mgr bin_mgr;
	public Xof_bin_wkr Bin_wkr_fsdb() {return bin_wkr_fsdb;} private Xof_bin_wkr_fsdb_sql bin_wkr_fsdb;
	public Fsdb_db_sys_itm Sys_itm() {return sys_itm;} private Fsdb_db_sys_itm sys_itm = new Fsdb_db_sys_itm();
	private Xof_url_bldr url_bldr = new Xof_url_bldr();
	public Xof_fsdb_mgr_sql Init_by_wiki(Xow_wiki wiki) {
		Xow_repo_mgr repo_mgr = wiki.File_mgr().Repo_mgr();
		Init_by_wiki(wiki.Key_str(), wiki.App().Fsys_mgr().File_dir().GenSubDir(wiki.Domain_str()), wiki.App().Fsys_mgr().File_dir(), repo_mgr);
		Xof_qry_wkr_xowa qry_xowa = new Xof_qry_wkr_xowa(new Xof_wiki_finder(wiki.App().Wiki_mgr().Get_by_key_or_make(Xow_wiki_.Domain_commons_bry), wiki), new gplx.xowa.files.qrys.Xof_img_meta_wkr_xowa());
		Xof_qry_wkr_wmf_api qry_wmf_api = new Xof_qry_wkr_wmf_api(wiki, new Xof_img_wkr_api_size_base_wmf());
		qry_mgr.Wkrs_(qry_xowa, qry_wmf_api);
		Xof_bin_wkr_fsdb_sql bin_fsdb = new Xof_bin_wkr_fsdb_sql(this);
		Xof_bin_wkr_wmf_xfer bin_wmf_xfer = new Xof_bin_wkr_wmf_xfer(wiki.App().File_mgr().Download_mgr().Download_wkr().Download_xrg(), repo_mgr);
		bin_mgr.Wkrs_(bin_fsdb, bin_wmf_xfer);
		wiki.Rls_list().Add(this);
		bin_mgr.Resizer_(wiki.App().File_mgr().Img_mgr().Wkr_resize_img());
		return this;
	}
	public void Init_by_wiki(String wiki_domain, Io_url db_dir, Io_url fs_dir, Xow_repo_mgr repo_mgr) {
		if (init) return;
		init = true;
		this.fs_dir = fs_dir;
		Init_img_regy_provider(db_dir, wiki_domain);
		sys_itm.Init(db_dir, wiki_domain);
		bin_mgr = new Xof_bin_mgr(repo_mgr);
		bin_wkr_fsdb = new Xof_bin_wkr_fsdb_sql(this).Url_bfr_len_(64 * Io_mgr.Len_kb);	// most thumbs are 40 kb
	}	private boolean init = false;
	public void Reg_select(Xog_win_wtr win_wtr, byte exec_tid, ListAdp itms) {
		Xof_reg_fil_tbl.Select_list(img_regy_provider, exec_tid, itms, url_bldr, bin_mgr.Repo_mgr());
		Xof_fsdb_mgr_utl._.Fsdb_search(this, fs_dir, win_wtr, exec_tid, itms, bin_mgr.Repo_mgr(), url_bldr);
	}
	public Fsdb_fil_itm Fil_select_bin(byte[] dir, byte[] fil, boolean is_thumb, int width, int thumbtime) {return sys_itm.Fil_select_bin(dir, fil, is_thumb, width, thumbtime);}
	public Fsdb_xtn_thm_itm Thm_select_bin(byte[] dir, byte[] fil, int width, int thumbtime)			{return sys_itm.Thm_select_bin(dir, fil, width, thumbtime);}
	public void Reg_insert(Xof_fsdb_itm itm, byte repo_id, byte status) {
		byte[] orig_page = itm.Orig_ttl();
		if (!Xof_reg_fil_tbl.Select_itm_exists(img_regy_provider, orig_page))
			Xof_reg_fil_tbl.Insert(img_regy_provider, itm.Reg_id(), orig_page, status, repo_id, itm.Orig_redirect(), itm.Lnki_ext().Id(), itm.Orig_w(), itm.Orig_h());
	}
	public void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int w, int h, int thumbtime, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		sys_itm.Thm_insert(rv, dir, fil, ext_id, w, h, thumbtime, modified, hash, bin_len, bin_rdr);
	}
	public void Img_insert(Fsdb_xtn_img_itm rv, byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr, int img_w, int img_h) {
		sys_itm.Img_insert(rv, dir, fil, ext_id, modified, hash, bin_len, bin_rdr, img_w, img_h);
	}
	public void Rls() {
		sys_itm.Commit();
		sys_itm.Rls();
		img_regy_provider.Rls();
	}
	private void Init_img_regy_provider(Io_url root_dir, String wiki_domain) {
		BoolRef created = BoolRef.false_();
		img_regy_provider = Sqlite_engine_.Provider_load_or_make_(root_dir.GenSubFil(wiki_domain + "#core#0000.sqlite3"), created);
		if (created.Val())
			Xof_reg_fil_tbl.Create_table(img_regy_provider);
	}
}

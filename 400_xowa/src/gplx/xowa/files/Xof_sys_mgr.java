/*
XOWA: the extensible offline wiki application
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
package gplx.xowa.files; import gplx.*; import gplx.xowa.*;
import gplx.dbs.*; import gplx.fsdb.*; import gplx.fsdb.tbls.*;
public class Xof_sys_mgr implements RlsAble {
	private Fsdb_mnt_itm[] mnt_itms; private int mnt_itms_len;
	private Db_provider mnt_regy_provider = null; private Db_provider img_regy_provider = null;
	public void Init_by_wiki(Xow_wiki wiki) {Init_by_wiki(wiki.App().Fsys_mgr().File_dir().GenSubDir(wiki.Domain_str()), wiki.Rls_list());}
	public void Init_by_wiki(Io_url root_dir, ListAdp rls_list) {// TEST
		Init_img_regy_provider(root_dir);
		Init_mnt_regy_provider(root_dir);
		rls_list.Add(this);
	}
	public void Select(ListAdp lnki_list) {Select(Select__to_hash(lnki_list));}
	public void Select(OrderedHash itm_hash) {
		int itm_hash_len = itm_hash.Count();
		int found = Xof_tbl_reg.Search(img_regy_provider, itm_hash, itm_hash_len);
		if (found == itm_hash_len) return; // all itms found in regy
		ListAdp fsdb_list = ListAdp_.new_();
		for (int i = 0; i < itm_hash_len; i++) {
			Xof_itm itm = (Xof_itm)itm_hash.FetchAt(i);
			switch (itm.Reg_status()) {
				case Xof_itm.Reg_status_present: case Xof_itm.Reg_status_missing:	break;
				case Xof_itm.Reg_status_unknown: case Xof_itm.Reg_status_deleted:	fsdb_list.Add(itm); break;
				default:															throw Err_.unhandled(itm.Reg_status());
			}
		}
		Xof_itm[] fsdb_itms = (Xof_itm[])fsdb_list.XtoAryAndClear(Xof_itm.class);
		if (fsdb_itms.length > 0) {
			for (int i = 0; i < mnt_itms_len; i++)
				mnt_itms[i].Retrieve(fsdb_itms);
		}
		// download from internet
		// update reg
	}
	private OrderedHash Select__to_hash(ListAdp lnki_list) {
		OrderedHash rv = OrderedHash_.new_bry_();
		int lnki_list_len = lnki_list.Count();
		Xof_itm_key_bldr key_bldr = new Xof_itm_key_bldr();
		Xof_itm tmp_itm = new Xof_itm();
		int html_id = 0;
		for (int i = 0; i < lnki_list_len; i++) {
			Xop_lnki_tkn lnki = (Xop_lnki_tkn)lnki_list.FetchAt(i);				
			tmp_itm.Lnki_atrs_(key_bldr, lnki.Ttl().Page_db(), lnki.Lnki_type(), lnki.Width().Val(), lnki.Height().Val(), lnki.Upright(), lnki.Thumbtime());
			byte[] reg_key = tmp_itm.Reg_key();
			Xof_itm itm = (Xof_itm)rv.Fetch(reg_key);
			if (itm == null) {
				tmp_itm.Link_html_id_add(++html_id);
				rv.Add(reg_key, tmp_itm);
				tmp_itm = new Xof_itm();
			}
			else {
				itm.Link_html_id_add(++html_id);
			}
		}
		return rv;
	}
	public void Regy_insert(String key, String url, byte status, long size, long viewed, int mnt_id, int img_id) {
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = Xof_tbl_reg.Insert_stmt(img_regy_provider);
			Xof_tbl_reg.Insert(stmt, key, url, status, size, viewed, mnt_id, img_id);
		} finally {stmt.Rls();}
	}
	public int Fsdb_insert_idx() {return fsdb_insert_idx;} private int fsdb_insert_idx = 0;
	public void Fsdb_insert(Fsdb_wmf_img_itm rv, byte[] dir, byte[] fil, int w, boolean is_orig, int h, byte[] data) {Fsdb_insert(rv, dir, fil, Xof_ext_.Id_unknown, w, Xop_lnki_tkn.Thumbtime_null, is_orig, h, DateAdp_.MinValue, "", data);}
	public void Fsdb_insert(Fsdb_wmf_img_itm rv, byte[] dir, byte[] fil, int ext_id, int w, int thumbtime, boolean is_orig, int h, DateAdp modified, String md5, byte[] data) {
		mnt_itms[fsdb_insert_idx].Insert_img(rv, dir, fil, ext_id, w, thumbtime, is_orig, h, modified, md5, data);
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
		if (created.Val())
			Xof_tbl_reg.Create_table(img_regy_provider);
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

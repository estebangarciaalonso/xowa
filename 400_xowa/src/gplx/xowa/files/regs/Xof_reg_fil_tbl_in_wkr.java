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
package gplx.xowa.files.regs; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.dbs.*; import gplx.xowa.files.fsdb.*;
class Xof_reg_fil_tbl_in_wkr extends gplx.xowa.dbs.tbls.Xodb_in_wkr_base {
	private ListAdp itms_all;
	@Override public int Interval() {return Sqlite_engine_.Stmt_arg_max;}
	public OrderedHash Itms_by_ttl() {return itms_by_ttl;} private OrderedHash itms_by_ttl = OrderedHash_.new_bry_();
	public Xof_reg_fil_tbl_in_wkr Init(ListAdp v) {this.itms_all = v; itms_by_ttl.Clear(); return this;}
	@Override public Db_qry Build_qry(int bgn, int end) {
		Object[] part_ary = gplx.xowa.dbs.tbls.Xodb_in_wkr_base.In_ary(end - bgn);
		String in_fld_name = Xof_reg_fil_tbl.Fld_rf_ttl; 
		return Db_qry_.select_cols_
		(	Xof_reg_fil_tbl.Tbl_name
		, 	Db_crt_.in_(in_fld_name, part_ary)
		)
		;
	}
	@Override public void Fill_stmt(Db_stmt stmt, int bgn, int end) {
		for (int i = bgn; i < end; i++) {
			Xof_fsdb_itm itm = (Xof_fsdb_itm)itms_all.FetchAt(i);
			stmt.Val_str_by_bry_(itm.Lnki_ttl());
		}
	}
	@Override public void Eval_rslts(Cancelable cancelable, DataRdr rdr) {
		while (rdr.MoveNextPeer()) {
			if (cancelable.Canceled()) return;
			Xof_reg_fil_itm itm = Xof_reg_fil_itm.load_(rdr);
			byte[] itm_ttl = itm.Ttl();
			if (!itms_by_ttl.Has(itm_ttl))	// guard against dupes (shouldn't happen)
				itms_by_ttl.Add(itm_ttl, itm);
		}
	}
}
class Xof_reg_fil_tbl_evaluator {
	public static void Rdr_done(byte exec_tid, ListAdp itms_all, OrderedHash itms_by_ttl, Xof_url_bldr url_bldr, Xow_repo_mgr repo_mgr) {
		int len = itms_all.Count();
		Xof_img_size img_size = new Xof_img_size();
		for (int i = 0; i < len; i++) {
			Xof_fsdb_itm fsdb_itm = (Xof_fsdb_itm)itms_all.FetchAt(i);
			byte[] fsdb_itm_ttl = fsdb_itm.Lnki_ttl();
			fsdb_itm.Rslt_reg_(Xof_reg_wkr_.Tid_missing_reg);
			Xof_reg_fil_itm regy_itm = (Xof_reg_fil_itm)itms_by_ttl.Fetch(fsdb_itm_ttl); if (regy_itm == null) continue; // not in reg; do full search
			byte regy_itm_status = regy_itm.Status();
			fsdb_itm.Rslt_reg_(regy_itm_status);
			if (regy_itm_status != Xof_reg_wkr_.Tid_found_orig) continue;
			byte repo_id = regy_itm.Orig_repo();
			Xof_repo_itm repo = null;
			if (repo_id <= Xof_repo_itm.Repo_local) { // bounds check
				fsdb_itm.Orig_repo_(repo_id);
				Xof_repo_pair repo_pair = repo_mgr.Repos_get_at(repo_id);
				fsdb_itm.Orig_wiki_(repo_pair.Wiki_key());
				repo = repo_pair.Trg();
			}
			fsdb_itm.Orig_size_(regy_itm.Orig_w(), regy_itm.Orig_h());
			fsdb_itm.Rslt_reg_(Xof_reg_wkr_.Tid_found_orig);
			if (ByteAry_.Len_gt_0(regy_itm.Orig_redirect()))	// redirect exists;
				fsdb_itm.Init_by_redirect(regy_itm.Orig_redirect());
			fsdb_itm.Html_size_calc(img_size, exec_tid);
			Io_url html_url = url_bldr.Set_trg_file_(fsdb_itm.Lnki_type_as_mode(), repo, fsdb_itm.Lnki_ttl(), fsdb_itm.Lnki_md5(), fsdb_itm.Lnki_ext(), fsdb_itm.Html_w(), fsdb_itm.Lnki_thumbtime()).Xto_url();
			fsdb_itm.Html_url_(html_url);
			if (!Io_mgr._.ExistsFil(html_url))
				fsdb_itm.Rslt_reg_(Xof_reg_wkr_.Tid_missing_reg);
			// build url; check if exists;
		}
	}
}
class Io_url_exists_mgr {
	private gplx.cache.Gfo_cache_mgr_obj cache_mgr = new gplx.cache.Gfo_cache_mgr_obj();
	public Io_url_exists_mgr() {
		cache_mgr.Compress_max_(Int_.MaxValue);
	}
	public boolean Has(Io_url url) {
		Object rv_obj = cache_mgr.Get_or_null(url.Raw());
		if (rv_obj != null) return ((BoolRef)rv_obj).Val(); // cached val exists; use it
		boolean exists = Io_mgr._.ExistsFil(url);
		cache_mgr.Add(url.Raw(), BoolRef.new_(exists));
		return exists;
	}
}

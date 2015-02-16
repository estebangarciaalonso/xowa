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
package gplx.xowa; import gplx.*;
public class Xowd_hive_mgr {
	public Xowd_hive_mgr(Xow_wiki wiki, byte dir_tid) {
		this.wiki = wiki; fsys_mgr = wiki.Fsys_mgr(); this.dir_tid = dir_tid;
		dir_tid_reg = dir_tid == Xow_dir_info_.Tid_page ? Xow_dir_info_.Tid_ttl : dir_tid; 
	} Xow_wiki wiki; Xow_fsys_mgr fsys_mgr; Xowd_regy_mgr reg_mgr; byte dir_tid;
	byte dir_tid_reg;
	public void Create(Xow_ns ns, byte[] key, byte[] data, gplx.lists.ComparerAble comparer) {
		if (reg_mgr == null) reg_mgr = new Xowd_regy_mgr(fsys_mgr.Url_ns_reg(ns.Num_str(), dir_tid_reg));
		int fil_idx = 0;
		if (reg_mgr.Files_ary().length == 0) {
			reg_mgr.Create(key);
			fil_idx = 0;
		}
		else {
			fil_idx = reg_mgr.Files_find(key);
			reg_mgr.Update_add(fil_idx, key);
		}
		Io_url url = fsys_mgr.Url_ns_fil(dir_tid, ns.Id(), fil_idx);
		byte[] bry = Io_mgr._.LoadFilBry(url);
		Xob_xdat_file xdat = new Xob_xdat_file();
		if (bry != Bry_.Empty)
			xdat.Parse(bry, bry.length, url);
		Bry_bfr tmp = wiki.Utl_bry_bfr_mkr().Get_m001();
		xdat.Insert(tmp, data);
		if (comparer != null)
			xdat.Sort(tmp, comparer);
		tmp.Mkr_rls();
		xdat.Save(url);
		reg_mgr.Save();
	}
	public void Create(byte[] key, byte[] data, gplx.lists.ComparerAble comparer) {
		if (reg_mgr == null) reg_mgr = new Xowd_regy_mgr(fsys_mgr.Url_site_reg(dir_tid));
		int fil_idx = 0;
		if (reg_mgr.Files_ary().length == 0) {
			reg_mgr.Create(key);
			fil_idx = 0;
		}
		else {
			fil_idx = reg_mgr.Files_find(key);
			reg_mgr.Update_add(fil_idx, key);
		}
		Io_url url = fsys_mgr.Url_site_fil(dir_tid, fil_idx);
		byte[] bry = Io_mgr._.LoadFilBry(url);
		Xob_xdat_file xdat = new Xob_xdat_file();
		if (bry != Bry_.Empty)
			xdat.Parse(bry, bry.length, url);
		Bry_bfr tmp = wiki.Utl_bry_bfr_mkr().Get_m001();
		xdat.Insert(tmp, data);
		if (comparer != null)
			xdat.Sort(tmp, comparer);
		tmp.Mkr_rls();
		xdat.Save(url);
		reg_mgr.Save();
	}
	public void Update(Xow_ns ns, byte[] old_key, byte[] new_key, byte[] data, int lkp_bgn, byte lkp_dlm, boolean exact, boolean sort) {
		if (reg_mgr == null) reg_mgr = new Xowd_regy_mgr(fsys_mgr.Url_ns_reg(ns.Num_str(), Xow_dir_info_.Tid_ttl));
		int fil_idx = reg_mgr.Files_find(old_key);
		boolean reg_save = false;
		if (new_key != null)
			reg_save = reg_mgr.Update_change(fil_idx, old_key, new_key);
		Io_url url = fsys_mgr.Url_ns_fil(dir_tid, ns.Id(), fil_idx);
		byte[] bry = Io_mgr._.LoadFilBry(url);
		Xob_xdat_file xdat = new Xob_xdat_file();
		if (bry != Bry_.Empty)
			xdat.Parse(bry, bry.length, url);
		Bry_bfr tmp = wiki.Utl_bry_bfr_mkr().Get_m001();
		Xob_xdat_itm itm = new Xob_xdat_itm(); 
		xdat.Find(itm, old_key, lkp_bgn, lkp_dlm, exact);
		if (itm.Missing()) return;
		xdat.Update(tmp, itm, data);
		if (sort) xdat.Sort(tmp, new Bry_comparer_bgn_eos(lkp_bgn));
		tmp.Mkr_rls();
		xdat.Save(url);
		if (reg_save) reg_mgr.Save();
	}
}

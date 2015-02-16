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
public class Xow_hive_mgr_fxt {
	public void Clear() {
		if (hive_mgr == null) {
			app = Xoa_app_fxt.app_();
			wiki = Xoa_app_fxt.wiki_tst_(app);
			hive_mgr = new Xob_hive_mgr(wiki);
		}
		hive_mgr.Clear();
		Io_mgr._.InitEngine_mem();
	}	private Xob_hive_mgr hive_mgr; Xoa_app app;
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public void Find_nearby(String key, int count, boolean include_redirects, String... expd) {
		ListAdp list = ListAdp_.new_();
		wiki.Hive_mgr().Find_bgn(list, wiki.Ns_mgr().Ns_main(), Bry_.new_ascii_(key), count, include_redirects);
		int actl_len = list.Count();
		String[] actl = new String[actl_len];
		for (int i = 0; i < actl_len; i++) {
			Xodb_page itm = (Xodb_page)list.FetchAt(i);
			actl[i] = String_.new_ascii_(itm.Ttl_wo_ns());
		}
		Tfds.Eq_ary_str(expd, actl);
	}
	public static void Ttls_create_rng(Xow_wiki wiki, int files, int ttls_per_file) {Ttls_create_rng(wiki, wiki.Ns_mgr().Ns_main(), files, ttls_per_file);}
	public static void Ttls_create_rng(Xow_wiki wiki, Xow_ns ns, int files, int ttls_per_file) {
		Xob_reg_wtr reg_wtr = new Xob_reg_wtr();
		byte dir_tid = Xow_dir_info_.Tid_ttl;
		int id = 0;
		int ttl_bry_len = Int_.DigitCount(ttls_per_file);
		Xob_xdat_file_wtr xdat_wtr = Xob_xdat_file_wtr.new_file_(ttls_per_file * 8, wiki.Fsys_mgr().Url_ns_dir(ns.Num_str(), Xow_dir_info_.Tid_ttl));
		Bry_bfr tmp_bfr = Bry_bfr.new_();
		byte ltr = Byte_ascii.Ltr_A; byte[] ttl_0 = Bry_.Empty, ttl_n = Bry_.Empty;
		for (int fil_idx = 0; fil_idx < files; fil_idx++) {
			for (int ttl_idx = 0; ttl_idx < ttls_per_file; ttl_idx++) {
				tmp_bfr.Add_byte(ltr).Add_int_fixed(ttl_idx, ttl_bry_len);
				byte[] ttl_bry = tmp_bfr.Xto_bry_and_clear(); 
				if 		(ttl_idx == 0) 					ttl_0 = ttl_bry;
				else if (ttl_idx == ttls_per_file - 1) 	ttl_n = ttl_bry;
				Xodb_page_.Txt_ttl_save(xdat_wtr.Bfr(), id++, 0, ttl_idx, ttl_idx % 2 == 1, 1, ttl_bry);
				xdat_wtr.Add_idx(Byte_ascii.Nil);
			}
			xdat_wtr.Flush(wiki.App().Usr_dlg());
			reg_wtr.Add(ttl_0, ttl_n, ttls_per_file);
			++ltr;
		}
		reg_wtr.Flush(wiki.Fsys_mgr().Url_ns_reg(ns.Num_str(), dir_tid));
	}
	public Xow_hive_mgr_fxt Create_ctg(String key_str, int... pages) {Create_ctg(app, hive_mgr, key_str, pages); return this;}
	public static void Create_ctg(Xoa_app app, Xob_hive_mgr hive_mgr, String key_str, int... pages) {
		byte[] key_bry = Bry_.new_ascii_(key_str);
		Bry_bfr bfr = app.Utl_bry_bfr_mkr().Get_b512();
		bfr.Add(key_bry);
		int pages_len = pages.length;
		for (int i = 0; i < pages_len; i++)				
			bfr.Add_byte_pipe().Add_base85_len_5(pages[i]);
		bfr.Add_byte_nl();
		byte[] row = bfr.Mkr_rls().Xto_bry_and_clear();
		hive_mgr.Create(Xow_dir_info_.Tid_category, key_bry, row);
	}
	public Xow_hive_mgr_fxt Create_id(int id, int fil_idx, int row_idx, boolean type_redirect, int itm_len, int ns_id, String ttl) {Create_id(app, hive_mgr, id, fil_idx, row_idx, type_redirect, itm_len, ns_id, ttl); return this;}
	public static void Create_id(Xoa_app app, Xob_hive_mgr hive_mgr, int id, int fil_idx, int row_idx, boolean type_redirect, int itm_len, int ns_id, String ttl) {
		Bry_bfr bfr = app.Utl_bry_bfr_mkr().Get_b512();
		byte[] key_bry = Base85_utl.XtoStrByAry(id, 5);
		bfr	.Add(key_bry)						.Add_byte_pipe()
			.Add_base85_len_5(fil_idx)			.Add_byte_pipe()
			.Add_base85_len_5(row_idx)			.Add_byte_pipe()
			.Add_byte(type_redirect	? Byte_ascii.Num_1 : Byte_ascii.Num_0).Add_byte_pipe()
			.Add_base85_len_5(itm_len)			.Add_byte_pipe()
			.Add_base85_len_5(ns_id)			.Add_byte_pipe()
			.Add_str(ttl)						.Add_byte_nl();
		byte[] row = bfr.Xto_bry_and_clear();
		bfr.Mkr_rls();
		hive_mgr.Create(Xow_dir_info_.Tid_id, key_bry, row);
	}
	public Xow_hive_mgr_fxt Load(String url, String... expd) {
		String actl = Io_mgr._.LoadFilStr(url);
		Tfds.Eq_ary_str(expd, String_.SplitLines_nl(actl));
		return this;
	}
}

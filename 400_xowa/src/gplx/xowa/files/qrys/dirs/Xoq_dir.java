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
import gplx.dbs.*; import gplx.gfui.*;
import gplx.fsdb.*;
import gplx.xowa.files.wiki_orig.*;
class Xoq_dir implements GfoInvkAble {
	private Gfo_usr_dlg usr_dlg; private Xof_img_wkr_query_img_size img_size_wkr;
	private Io_url url; private boolean recurse; private boolean case_match;
	private Xoq_fil_mgr db_fil_mgr = new Xoq_fil_mgr(), fs_fil_mgr;	
	private Db_provider db_provider; private Fsdb_cfg_tbl db_cfg_tbl;
	private int fil_id_next = 0;
	public void Init(Gfo_usr_dlg usr_dlg, Xof_img_wkr_query_img_size img_size_wkr) {
		this.usr_dlg = usr_dlg; this.img_size_wkr = img_size_wkr;
		db_fil_mgr.Case_match_(case_match);
	}
	public Xoq_fil Get_by_ttl(byte[] lnki_ttl) {
		Xoq_fil rv = (Xoq_fil)db_fil_mgr.Get_by_ttl(lnki_ttl);
		if (rv == null) {
			rv = Read_from_db(lnki_ttl);
			if (rv == null) {
				rv = Read_from_fs(lnki_ttl); if (rv == null) return Xoq_fil.Null;
				Save_to_db(rv);
			}
			db_fil_mgr.Add(rv);
		}
		return rv;
	}
	private Xoq_fil Read_from_db(byte[] lnki_ttl) {
		if (db_provider == null) Db_init_or_make();
		Xof_wiki_orig_itm orig_itm = Xof_wiki_orig_tbl.Select_itm(db_provider, lnki_ttl);
		if (orig_itm == null) return Xoq_fil.Null;
		return new Xoq_fil(orig_itm.Id(), lnki_ttl, orig_itm.Orig_ext(), orig_itm.Orig_w(), orig_itm.Orig_h());
	}
	private Xoq_fil Read_from_fs(byte[] lnki_ttl) {
		if (fs_fil_mgr == null) fs_fil_mgr = Init_fs_fil_mgr();
		Xoq_fil rv = fs_fil_mgr.Get_by_ttl(lnki_ttl);
		if (Xof_ext_.Id_is_image(rv.Lnki_ext())) {
			SizeAdp img_size = img_size_wkr.Exec(rv.Orig_url());
			rv.Orig_size_(img_size.Width(), img_size.Height());
		}
		return rv;
	}
	private void Save_to_db(Xoq_fil fil) {
		fil.Uid_(++fil_id_next);
		db_fil_mgr.Add(fil);
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
			fil_itm = new Xoq_fil(fil_id_next++, fil_name_bry, ext.Id(), 0, 0);	// fil
			rv.Add(fil_itm);
		}
		return rv;
	}
	private void Db_init_or_make() {
		Io_url db_url = url.GenSubFil("orig_regy.sqlite3");
		BoolRef created_ref = BoolRef.n_();
		db_provider = Sqlite_engine_.Provider_load_or_make_(db_url, created_ref);
		boolean created = created_ref.Val();
		db_cfg_tbl = new Fsdb_cfg_tbl(db_provider, created);
		if (!created)
			fil_id_next = db_cfg_tbl.Select_as_int("xowa.root_dir", "fil_id_next");
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_url))				return url.Raw();
		else if	(ctx.Match(k, Invk_url_))				url = m.ReadIoUrl("v");
		else if	(ctx.Match(k, Invk_recurse))			return Yn.X_to_str(recurse);
		else if	(ctx.Match(k, Invk_recurse_))			recurse = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_case_match))			return Yn.X_to_str(case_match);
		else if	(ctx.Match(k, Invk_case_match_))		case_match = m.ReadYn("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String 
	  Invk_url = "url", Invk_url_ = "url_"
	, Invk_recurse = "recurse", Invk_recurse_ = "recurse_"
	, Invk_case_match = "case_match", Invk_case_match_ = "case_match_"
	;
}

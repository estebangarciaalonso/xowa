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
package gplx.xowa.cfgs; import gplx.*; import gplx.xowa.*;
import gplx.xowa.wikis.*;
public class Xoa_cfg_mgr implements GfoInvkAble {
	public Xoa_cfg_mgr(Xoa_app app) {this.app = app;} private OrderedHash hash = OrderedHash_.new_bry_();
	public Xoa_app App() {return app;} private Xoa_app app;
	public Xoa_cfg_itm Get_itm_or_null(byte[] grp_key, byte[] itm_key) {
		Xoa_cfg_grp grp = (Xoa_cfg_grp)hash.Fetch(grp_key); 
		return grp == null ? null : grp.Get_itm_or_null(itm_key);
	}
	public Xoa_cfg_itm Get_itm_or_make(byte[] grp_key, byte[] itm_key) {
		Xoa_cfg_grp grp = null;
		Object grp_obj = hash.Fetch(grp_key);
		if (grp_obj == null) {
			Xoa_cfg_grp_tid tid = Xoa_cfg_grp_tid.parse_(itm_key);
			grp = new Xoa_cfg_grp(this, tid, grp_key);
			hash.Add(grp_key, grp);
		}
		else
			grp = (Xoa_cfg_grp)grp_obj;
		return grp.Get_itm_or_make(itm_key);
	}
	public void Set_by_app(String grp_key, String val)				{Set(Bry_.new_utf8_(grp_key), Xoa_cfg_grp_tid.Key_app_bry, val);}
	public void Set_by_all(String grp_key, String val)				{Set(Bry_.new_utf8_(grp_key), Xoa_cfg_grp_tid.Key_all_bry, val);}
	public void Set_by_type(String grp_key, byte tid, String val)	{Set(Bry_.new_utf8_(grp_key), Xow_wiki_domain_.Key_by_tid(tid), val);}
	private void Set(byte[] grp_key, byte[] tid_key, String val) {
		Xoa_cfg_itm itm = Get_itm_or_make(grp_key, tid_key);
		itm.Val_(val);
	}
	public void Init(Xow_wiki wiki) {
		int len = hash.Count();
		for (int i = 0; i < len; i++) {
			Xoa_cfg_grp grp = (Xoa_cfg_grp)hash.FetchAt(i);
			Xoa_cfg_itm itm = grp.Get_itm_by_wiki(wiki.Domain_bry(), wiki.Domain_tid());
			if (itm == null) continue;											// grp exists, but not for wiki; EX: rule exists for download.enabled and enwiki, but frwiki loaded
			if (itm.Grp().Tid().Tid() == Xoa_cfg_grp_tid.Tid_app) continue;		// rule is for app; don't run for wiki init
			Eval_set(wiki, grp.Key_str(), itm.Val());
		}
	}
	public boolean Notify(Xoa_cfg_grp grp, Xoa_cfg_itm itm) {
		Xoa_cfg_grp_tid grp_tid = grp.Tid();
		byte tid_byte = grp_tid.Tid();
		switch (tid_byte) {
			case Xoa_cfg_grp_tid.Tid_app:
				return Eval_set(app, grp.Key_str(), itm.Val());
			case Xoa_cfg_grp_tid.Tid_all:
			case Xoa_cfg_grp_tid.Tid_type:
				boolean all = tid_byte == Xoa_cfg_grp_tid.Tid_all; 
				int wiki_count = app.Wiki_mgr().Count();
				boolean rv = true;
				for (int i = 0; i < wiki_count; i++) {
					Xow_wiki wiki = app.Wiki_mgr().Get_at(i);
					if (all || wiki.Domain_tid() == grp_tid.Wiki_tid()) {
						if (!Eval_set(wiki, grp.Key_str(), itm.Val()))
							rv = false;
					}
				}
				return rv;
			case Xoa_cfg_grp_tid.Tid_wiki: {
				Xow_wiki wiki = app.Wiki_mgr().Get_by_key_or_null(itm.Key());
				if (wiki == null) return true; // wiki not installed; return true (no error)
				return Eval_set(wiki, grp.Key_str(), itm.Val());					
			}
			default:
				throw Err_.unhandled(tid_byte);
		}
	}
	public void Reset_all() {
		hash.Clear();
		db_txt.Cfg_reset_all(this);
	}
	public void Db_customized_n_() {
		int len = hash.Count();
		for (int i = 0; i < len; i++) {
			Xoa_cfg_grp grp = (Xoa_cfg_grp)hash.FetchAt(i);
			grp.Db_customized_n_();
		}
	}
	public void Db_loaded_y_() {
		int len = hash.Count();
		for (int i = 0; i < len; i++) {
			Xoa_cfg_grp grp = (Xoa_cfg_grp)hash.FetchAt(i);
			grp.Db_loaded_y_();
		}
	}
	public void Db_load_txt() {Db_load(db_txt);}
	public void Db_load(Xoa_cfg_db db) {
		db.Cfg_load_run(this);
		this.Db_loaded_y_();
	}
	public void Db_save_txt() {Db_save(db_txt);} private Xoa_cfg_db_txt db_txt = new Xoa_cfg_db_txt();
	public void Db_save(Xoa_cfg_db db) {
		int len = hash.Count();
		db.Cfg_save_bgn(this);
		for (int i = 0; i < len; i++) {
			Xoa_cfg_grp grp = (Xoa_cfg_grp)hash.FetchAt(i);
			grp.Db_save(db);
		}
		db.Cfg_save_end(this);
	}
	public void Clear() {
		int len = hash.Count();
		for (int i = 0; i < len; i++) {
			Xoa_cfg_grp grp = (Xoa_cfg_grp)hash.FetchAt(i);
			grp.Clear();
		}
		hash.Clear();
	}
	boolean Eval_set(GfoInvkAble invk, String key, String val) {
		String msg_str = key + "_(<:['\n" + val + "\n']:>);";
		Object rslt = app.Gfs_mgr().Run_str_for(invk, msg_str);
		return rslt != GfoInvkAble_.Rv_error;
	}
	public Object Eval_get(GfoInvkAble invk, String key) {
		String msg_str = key + ";";
		return app.Gfs_mgr().Run_str_for(invk, msg_str);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_get)) 		return Get_itm_or_make(m.ReadBry("itm_key"), m.ReadBry("grp_key"));
		else if	(ctx.Match(k, Invk_reset_all)) 	Reset_all();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_get = "get", Invk_reset_all = "reset_all";
}

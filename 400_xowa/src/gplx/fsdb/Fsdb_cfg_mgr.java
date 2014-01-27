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
package gplx.fsdb; import gplx.*;
import gplx.dbs.*;
public class Fsdb_cfg_mgr {
	private Fsdb_cfg_tbl cfg_tbl;
	private HashAdp grps = HashAdp_.new_();
	public int Next_id()			{return next_id++;} private int next_id = 1;
	public boolean Schema_thm_page()	{return schema_thm_page;} private boolean schema_thm_page = true;
	public void Txn_save() {
		this.Update_next_id();
	}
	public void Rls() {cfg_tbl.Rls();}
	private void Update_next_id()	{cfg_tbl.Update("core", "next_id", Int_.XtoStr(next_id));}
	public void Update(String grp, String key, String new_val) {
		String cur_val = cfg_tbl.Select_as_str(grp, key);
		if (cur_val == null)
			cfg_tbl.Insert(grp, key, new_val);
		else
			cfg_tbl.Update(grp, key, new_val);
	}
	public Fsdb_cfg_grp Grps_get_or_load(String grp_key) {
		Fsdb_cfg_grp grp = (Fsdb_cfg_grp)grps.Fetch(grp_key);
		if (grp == null) {
			grp = Grps_load(grp_key);
			grps.Add(grp_key, grp);
		}
		return grp;
	}
	public Fsdb_cfg_grp Grps_get_or_add(String grp_key) {	// TEST:
		Fsdb_cfg_grp grp = (Fsdb_cfg_grp)grps.Fetch(grp_key);
		if (grp == null) {
			grp = new Fsdb_cfg_grp(grp_key);
			grps.Add(grp_key, grp);
		}
		return grp;
	}
	private Fsdb_cfg_grp Grps_load(String grp_key) {
		Fsdb_cfg_grp rv = null;
		DataRdr rdr = cfg_tbl.Select_by_grp(grp_key);
		try {
			while (rdr.MoveNextPeer()) {
				if (rv == null) rv = new Fsdb_cfg_grp(grp_key);
				String key = rdr.ReadStr(Fsdb_cfg_tbl.Fld_cfg_key);
				String val = rdr.ReadStr(Fsdb_cfg_tbl.Fld_cfg_val);
				rv.Set(key, val);
			}
		}
		finally {rdr.Rls();}
		return rv == null ? Fsdb_cfg_grp.Null : rv;
	}
	public static Fsdb_cfg_mgr load_(Fsdb_db_abc_mgr abc_mgr, Db_provider p) {return new Fsdb_cfg_mgr().Init_by_load(p);}
	public static Fsdb_cfg_mgr make_(Fsdb_db_abc_mgr abc_mgr, Db_provider p) {return new Fsdb_cfg_mgr().Init_by_make(p);}
	private Fsdb_cfg_mgr Init_by_load(Db_provider p) {
		this.cfg_tbl = new Fsdb_cfg_tbl(p, false);
		Fsdb_cfg_grp core_grp = Grps_get_or_load(Grp_core);
		this.next_id			= core_grp.Get_int_or(Key_next_id, -1); if (next_id == -1) throw Err_.new_("next_id not found in fsdb_cfg");
		this.schema_thm_page	= core_grp.Get_yn_or_n(Key_schema_thm_page);
		return this;
	}
	private Fsdb_cfg_mgr Init_by_make(Db_provider p) {
		this.cfg_tbl = new Fsdb_cfg_tbl(p, true);
		this.cfg_tbl.Insert(Grp_core, Key_next_id				, "1");
		this.cfg_tbl.Insert(Grp_core, Key_schema_thm_page		, "y");
		return this;
	}
	public static final String Grp_core = "core";
	private static final String Key_next_id = "next_id", Key_schema_thm_page = "schema.thm.page";
}

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
package gplx.xowa.xtns.refs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xtn_ref_mgr {
	public Xtn_ref_lst Lst_get(byte[] grp_name, int lst_idx) {
		return ByteAry_.Len_eq_0(grp_name) ? grp_default.Lsts_get_at(lst_idx) : ((Xtn_ref_grp)grps.Fetch(grp_name)).Lsts_get_at(lst_idx);	// NOTE: must be ByteAry_.Len_eq_0 else <references group=""/> not same as <references/>; DATE:2013-02-06
	}	
	public void Grps_add(byte[] grp_name, byte[] itm_name, byte[] follow, Xtn_ref_nde itm) {
		Xtn_ref_grp grp = Grps_get(grp_name);
		grp.Lsts_add(itm_name, follow, itm);
		itm.Uid_(uid_last++);
	}
	public int Grps_seal(byte[] grp_name) {
		Xtn_ref_grp grp = Grps_get(grp_name);
		return grp.Grp_seal();
	}
	public void Grps_clear() {
		grps.Clear();
		grp_default.Lsts_clear();
		uid_last = 0;
	}
	public Xtn_ref_grp Grps_get(byte[] grp_name) {
		if (ByteAry_.Len_eq_0(grp_name)) return grp_default;
		Object o = grps.Get_by_bry(grp_name);
		if (o == null) {
			Xtn_ref_grp grp = new Xtn_ref_grp(grp_name);
			grps.Add(grp_name, grp);
			return grp;
		}
		else
			return (Xtn_ref_grp)o;
	}
	Hash_adp_bry grps = Hash_adp_bry.ci_();
	Xtn_ref_grp grp_default = new Xtn_ref_grp(ByteAry_.Empty);
	int uid_last;		
}

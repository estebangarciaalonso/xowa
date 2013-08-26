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
package gplx;
public class Bry_bfr_mkr {
	public static final byte Tid_b128 = 0, Tid_b512 = 1, Tid_k004 = 2, Tid_m001 = 3;
	Bry_bfr_mkr_mgr mkr_b128 = new Bry_bfr_mkr_mgr(Tid_b128, 128), mkr_b512 = new Bry_bfr_mkr_mgr(Tid_b512, 512), mkr_k004 = new Bry_bfr_mkr_mgr(Tid_k004, 4 * Io_mgr.Len_kb), mkr_m001 = new Bry_bfr_mkr_mgr(Tid_m001, 1 * Io_mgr.Len_mb);
	public ByteAryBfr Get_b128() {return mkr_b128.Get();}
	public ByteAryBfr Get_b512() {return mkr_b512.Get();}
	public ByteAryBfr Get_k004() {return mkr_k004.Get();}
	public ByteAryBfr Get_m001() {return mkr_m001.Get();}
	public void Rls(ByteAryBfr v) {
		v.Mkr_mgr().Rls(v);
	}
	public void Reset_if_gt(int v) {
		for (byte i = Tid_b128; i <= Tid_m001; i++)
			mkr(i).Reset_if_gt(v);
	} 
	public void Clear_fail_check() {
		for (byte i = Tid_b128; i <= Tid_m001; i++)
			mkr(i).Clear_fail_check();
	}

	public void Clear() {
		for (byte i = Tid_b128; i <= Tid_m001; i++)
			mkr(i).Clear();
	}
	Bry_bfr_mkr_mgr mkr(byte tid) {
		switch (tid) {
			case Tid_b128: 	return mkr_b128;
			case Tid_b512: 	return mkr_b512;
			case Tid_k004: 	return mkr_k004;
			case Tid_m001: 	return mkr_m001;
			default:		throw Err_.unhandled(tid);
		}
	}
}
class Bry_bfr_mkr_mgr {
	public Bry_bfr_mkr_mgr(byte mgr_id, int reset) {this.mgr_id = mgr_id; this.reset = reset;} private int reset;
	public byte Mgr_id() {return mgr_id;} private byte mgr_id; 
	ByteAryBfr[] ary = Ary_empty; int nxt_idx = 0, ary_max = 0;
	int[] free = Int_.Ary_empty; int free_len = 0;
	public void Reset_if_gt(int v) {
		this.Clear();	// TODO: for now, just call clear
	}
	public void Clear_fail_check() {
		for (int i = 0; i < ary_max; i++) {
			ByteAryBfr itm = ary[i];
			if (itm != null) {
				if (itm.Mkr_mgr() != null) throw Err_.new_("failed to clear bfr");
				itm.Clear();
			}
			ary[i] = null;
		}
		ary = Ary_empty;
		free = Int_.Ary_empty;
		free_len = 0;
		nxt_idx = ary_max = 0;
	}
	public void Clear() {
		for (int i = 0; i < ary_max; i++) {
			ByteAryBfr itm = ary[i];
			if (itm != null) itm.Clear();
			ary[i] = null;
		}
		ary = Ary_empty;
		free = Int_.Ary_empty;
		free_len = 0;
		nxt_idx = ary_max = 0;
	}
	public ByteAryBfr[] Ary() {return ary;}
	public int Nxt_idx() {return nxt_idx;}
	public ByteAryBfr Get() {
		ByteAryBfr rv = null;
		int rv_idx = -1;
		if (free_len > 0) {
			rv_idx = free[--free_len];
			rv = ary[rv_idx];
		}
		else {
			if (nxt_idx == ary_max) {
				Expand();
			}
			rv_idx = nxt_idx++;
			rv = ary[rv_idx];
			if (rv == null) {
				rv = ByteAryBfr.reset_(reset);
				ary[rv_idx] = rv;
			}
		}
		rv.Mkr_(this, rv_idx);
		return rv.Clear();	// NOTE: ALWAYS call Clear when doing Get. caller may forget to call Clear, and reused bfr may have leftover bytes. unit tests will not catch, and difficult to spot in app
	}
	void Expand() {
		int new_max = ary_max == 0 ? 2 : ary_max * 2;
		ByteAryBfr[] new_ary = new ByteAryBfr[new_max];
		Array_.CopyTo(ary, 0, new_ary, 0, ary_max);
		ary = new_ary;
		ary_max = new_max;
		int[] new_free = new int[ary_max];
		Array_.CopyTo(free, 0, new_free, 0, free_len);
		free = new_free;
	}
	public void Rls(ByteAryBfr v) {
		int idx = v.Mkr_itm();
		if (idx == -1) throw Err_mgr._.fmt_("gplx.ByteAryBfr", "rls_failed", "rls called on bfr that was not created by factory");
		int new_ary_len = nxt_idx - 1;
		if (idx == new_ary_len)
			nxt_idx = new_ary_len;
		else
			free[free_len++] = idx;
		v.Mkr_(null, -1);
	}
	public static final ByteAryBfr[] Ary_empty = new ByteAryBfr[0];
}

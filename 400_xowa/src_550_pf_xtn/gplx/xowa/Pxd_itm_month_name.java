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
class Pxd_itm_month_name extends Pxd_itm_base implements Pxd_itm_prototype {
	public Pxd_itm_month_name(int ary_idx, byte[] name, int seg_idx, int seg_val) {Ctor(ary_idx); this.name = name; Seg_idx_(seg_idx); this.seg_val = seg_val;} private byte[] name;
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_month_name;}
	@Override public int Eval_idx() {return 20;}
	int seg_val;
	public Pxd_itm MakeNew(int ary_idx) {
		Pxd_itm_month_name rv = new Pxd_itm_month_name(ary_idx, name, this.Seg_idx(), seg_val);
		return rv;
	}
	@Override public void Eval(Pxd_parser state) {
		state.Seg_idxs_(this, this.Seg_idx(), seg_val);
		switch (this.Data_idx()) {
			case 0: Eval_month_0(state); break;
			case 1: Eval_month_1(state); break;
			case 2: Eval_month_2(state); break;
			default:
				Pxd_itm[] data_ary = state.Data_ary();
				int data_idx = this.Data_idx();
				if (data_idx + 1 < data_ary.length) {
					Pxd_itm_int itm_rhs = Pxd_itm_int_.CastOrNull(data_ary[data_idx + 1]);
					if (itm_rhs.Digits() == 4) {	// handle fmts of 1 January 2010
						Eval_month_1(state);
						return;
					}
				}
				Eval_month_2(state);				
				break;
		} 
	}
	public void Eval_month_0(Pxd_parser state) {
		Pxd_itm[] data_ary = state.Data_ary();
		int data_ary_len = state.Data_ary_len();
		switch (data_ary_len) {
			case 1: break; // noop; nothing needs to be done
			case 2: {
				Pxd_itm_int itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
				if (itm_1 == null) {return;} // trie: fail
				switch (itm_1.Digits()) {
					case 4: 	Pxd_itm_int_.Year_err(state, itm_1); break;
					default: 	Pxd_itm_int_.Day_err(state, itm_1); break;				
				}
				break;
			}
			default:	// NOTE: handle invalid dates like January 2, 1999, 2000; DATE:2013-05-06
			case 3: {
				Pxd_itm_int itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
				Pxd_itm_int itm_2 = Pxd_itm_int_.CastOrNull(data_ary[2]);
				if (itm_1 == null || itm_2 == null) {return;} // trie: fail
				if (itm_1.Digits() == 4) {	// ASSUME: year since there are 4 digits; EX: May 2012
					if (!Pxd_itm_int_.Year_err(state, itm_1)) return;	// no error; return; otherwise continue below;
				}
				if (Pxd_itm_int_.Day_err(state, itm_1)) return;
				if (Pxd_itm_int_.Year_err(state, itm_2)) return;
				break;
			}
		}
	}
	public void Eval_month_1(Pxd_parser state) {
		Pxd_itm[] data_ary = state.Data_ary();
		int data_ary_len = state.Data_ary_len();
		switch (data_ary_len) {
			case 2: {
				Pxd_itm_int itm_0 = Pxd_itm_int_.CastOrNull(data_ary[0]);
				if (itm_0 == null) {return;} // trie: fail
				switch (itm_0.Digits()) {
					case 4: 	Pxd_itm_int_.Year_err(state, itm_0); break;
					default: 	Pxd_itm_int_.Day_err(state, itm_0); break;				
				}
				break;
			}
			default:
			case 3: {
//					Pxd_itm_int itm_0 = Pxd_itm_int_.CastOrNull(data_ary[0]);
//					Pxd_itm_int itm_2 = Pxd_itm_int_.CastOrNull(data_ary[2]);
				Pxd_itm_int itm_0 = Pxd_itm_int_.GetNearest(data_ary, this.Data_idx(), false);
				Pxd_itm_int itm_2 = Pxd_itm_int_.GetNearest(data_ary, this.Data_idx(), true);
				if (itm_0 == null || itm_2 == null) {return;} // trie: fail
				switch (itm_0.Digits()) {
					case 4:
						if (Pxd_itm_int_.Year_err(state, itm_0)) return;						
						if (Pxd_itm_int_.Day_err(state, itm_2)) return;
						break;
					default:
						if (itm_2.Digits() == 4) {
							if (Pxd_itm_int_.Day_err(state, itm_0)) return;							
							if (Pxd_itm_int_.Year_err(state, itm_2)) return;						
						}
						else { // 2 digits on either side of month; assume dd mm yy; EX: 03 Feb 01 -> 2001-02-03
							if (Pxd_itm_int_.Day_err(state, itm_0)) return;						
							if (Pxd_itm_int_.Year_err(state, itm_2)) return;							
						}
						break;
				}
				break;
			}
		}			
	}
	public void Eval_month_2(Pxd_parser state) {
		Pxd_itm[] data_ary = state.Data_ary();
		int data_ary_len = state.Data_ary_len();
		switch (data_ary_len) {
			case 3: {
				Pxd_itm_int itm_0 = Pxd_itm_int_.CastOrNull(data_ary[0]);
				Pxd_itm_int itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
				if (itm_0 == null || itm_1 == null) {return;} // trie: fail
				switch (itm_0.Digits()) {
					case 4:
						if (Pxd_itm_int_.Year_err(state, itm_0)) return;						
						if (Pxd_itm_int_.Day_err(state, itm_1)) return;
						break;
					default:
						if (itm_1.Digits() == 4) {
							if (Pxd_itm_int_.Day_err(state, itm_0)) return;							
							if (Pxd_itm_int_.Year_err(state, itm_1)) return;
						}
						else { // 2 digits on either side of month; assume dd mm yy; EX: 03 Feb 01 -> 2001-02-03
							if (Pxd_itm_int_.Day_err(state, itm_0)) return;						
							if (Pxd_itm_int_.Year_err(state, itm_1)) return;							
						}
						break;
				}
				break;
			}
		}		
		
	}
	@Override public void Time_ini(DateAdpBldr bldr) {
		bldr.Seg_set(this.Seg_idx(), seg_val);
	}
}
class Pxd_itm_unit extends Pxd_itm_base implements Pxd_itm_prototype {
	public Pxd_itm_unit(int ary_idx, byte[] name, int seg_idx, int seg_multiple) {Ctor(ary_idx); this.name = name; Seg_idx_(seg_idx); this.seg_multiple = seg_multiple;} 
	public byte[] Name() {return name;} private byte[] name;
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_unit;}
	@Override public int Eval_idx() {return 10;}
	int seg_val = 1; int seg_multiple;
	public Pxd_itm MakeNew(int ary_idx) {
		return new Pxd_itm_unit(ary_idx, name, this.Seg_idx(), seg_val);
	}
	@Override public void Eval(Pxd_parser state) {
		state.Seg_idxs_(this, this.Seg_idx(), seg_val);
		Pxd_itm[] tkns = state.Tkns();
		Pxd_itm_int itm_int = Pxd_itm_int_.GetNearest(tkns, this.Ary_idx(), false);
		state.Seg_idxs_(itm_int, Pxd_itm_base.Seg_idx_skip);
		seg_val = itm_int.Val();
		for (int i = itm_int.Ary_idx(); i > -1; i--) {
			Pxd_itm itm = tkns[i];
			switch (itm.Tkn_tid()) {
				case Pxd_itm_.TypeId_dash:														// negative sign; stop;
					seg_val *= -1;
					i = -1;
					break;
				case Pxd_itm_.TypeId_dot: case Pxd_itm_.TypeId_int: case Pxd_itm_.TypeId_ws:	// ignore
					break;
				default:																		// word; stop;
					i = -1;
					break;
			}
		}
	}
	@Override public void Time_ini(DateAdpBldr bldr) {
		DateAdp cur = bldr.Date();
		if (cur == null) cur = DateAdp_.Now();
		int val = seg_val * seg_multiple;
		switch (this.Seg_idx()) {
			case DateAdp_.SegIdx_second: cur = cur.Add_second(val); break;
			case DateAdp_.SegIdx_minute: cur = cur.Add_minute(val); break;
			case DateAdp_.SegIdx_hour  : cur = cur.Add_hour  (val); break;
			case DateAdp_.SegIdx_day   : cur = cur.Add_day   (val); break;
			case DateAdp_.SegIdx_month : cur = cur.Add_month (val); break;
			case DateAdp_.SegIdx_year  : cur = cur.Add_year  (val); break;
			default: throw Err_.unhandled(this.Seg_idx());
		}
		bldr.Date_(cur);
	}
}
class Pxd_itm_ago extends Pxd_itm_base implements Pxd_itm_prototype {
	public Pxd_itm_ago(int ary_idx, int seg_idx) {Ctor(ary_idx); Seg_idx_(seg_idx);} 
	public byte[] Name() {return Name_ago;} public static final byte[] Name_ago = ByteAry_.new_ascii_("ago");
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_ago;}
	@Override public int Eval_idx() {return 5;}	// set to high priority  so it can evaluate before unit_name
	public Pxd_itm MakeNew(int ary_idx) {return new Pxd_itm_ago(ary_idx, this.Seg_idx());}
	@Override public void Eval(Pxd_parser state) {
		Pxd_itm[] tkns = state.Tkns();
		int end = this.Ary_idx();
		boolean unit_seen = false;
		for (int i = end - 1; i > -1; i--) {	// loop over tokens, moving backward
			Pxd_itm itm = tkns[i];
			switch (itm.Tkn_tid()) {
				case Pxd_itm_.TypeId_ws: break;	// ignore ws
				case Pxd_itm_.TypeId_unit: unit_seen = true; break;	// unit seen; flag
				case Pxd_itm_.TypeId_int:
					if (unit_seen) {	// only if unit seen, reverse int; EX: "1 month ago" effectively becomes "-1 month"
						Pxd_itm_int itm_int = (Pxd_itm_int)itm;
						itm_int.Val_(-itm_int.Val());
						i = -1;	// end loop
					}
					break;
				default: break;
			}
		}
	}
	@Override public void Time_ini(DateAdpBldr bldr) {
	}
}

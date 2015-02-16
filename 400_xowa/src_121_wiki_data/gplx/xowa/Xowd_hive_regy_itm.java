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
import gplx.core.primitives.*;
public class Xowd_hive_regy_itm {// csv file with the format of "idx|bgn|end|count"; EX: "0|AA|AZ|120\n1|BA|BZ|110"
	public Xowd_hive_regy_itm(int idx) {this.idx = idx;}
	public int		Idx() {return idx;} private int idx;
	public byte[]	Bgn() {return bgn;} public Xowd_hive_regy_itm Bgn_(byte[] v) {bgn = v; return this;} private byte[] bgn;
	public byte[]	End() {return end;} public Xowd_hive_regy_itm End_(byte[] v) {end = v; return this;} private byte[] end;
	public int		Count() {return count;} public Xowd_hive_regy_itm Count_(int v) {this.count = v; return this;} private int count;
	public static Xowd_hive_regy_itm[] parse_fil_(ByteAry_fil utl) {
		ListAdp rv = utl.Itms();
		byte[] ary = utl.Raw_bry();
		int ary_len = utl.Raw_len(); if (ary_len == 0) return Xowd_hive_regy_itm.Ary_empty; //throw Err_mgr._.fmt_("xowa.wiki.data", "title_registry_file_not_found", "title_registry file not found: ~{0}", utl.Fil().Xto_api());
		Int_obj_ref pos = Int_obj_ref.zero_();
		while (pos.Val() < ary_len) {
			Xowd_hive_regy_itm file = new Xowd_hive_regy_itm();
			file.idx	= Bry_.ReadCsvInt(ary, pos, Bry_.Dlm_fld);
			file.bgn 	= Bry_.ReadCsvBry(ary, pos, Bry_.Dlm_fld);	// skip bgn
			file.end	= Bry_.ReadCsvBry(ary, pos, Bry_.Dlm_fld);
			file.count	= Bry_.ReadCsvInt(ary, pos, Bry_.Dlm_row);
			rv.Add(file);
		}
		return (Xowd_hive_regy_itm[])utl.Xto_itms(Xowd_hive_regy_itm.class);
	}
	public Xowd_hive_regy_itm() {}
	public Xowd_hive_regy_itm(int id, byte[] bgn, byte[] end, int count) {
		this.idx = id; this.bgn = bgn; this.end = end; this.count = count;
	}
	public void Srl_save(Bry_bfr bfr) {
		bfr	.Add_int_variable(idx).Add_byte_pipe()
			.Add(bgn).Add_byte_pipe()
			.Add(end).Add_byte_pipe()
			.Add_int_variable(count).Add_byte_nl();
	} 
	public static Xowd_hive_regy_itm tmp_()	{return new Xowd_hive_regy_itm();}
	public static final Xowd_hive_regy_itm[] Ary_empty = new Xowd_hive_regy_itm[0]; 
}
class Xowd_ttl_file_comparer_end implements gplx.lists.ComparerAble {
	public int compare(Object lhsObj, Object rhsObj) {
		Xowd_hive_regy_itm lhs = (Xowd_hive_regy_itm)lhsObj, rhs = (Xowd_hive_regy_itm)rhsObj;
		if 		(lhs.Count() == 0) 	return Bry_.Compare(rhs.End(), lhs.Bgn());
		//else if (rhs.Count() == 0) 	return Bry_.Compare(lhs.End(), rhs.End());	// NOTE: this line mirrors the top, but is actually covered by below
		else						return Bry_.Compare(lhs.End(), rhs.End());
	}
	public static final Xowd_ttl_file_comparer_end _ = new Xowd_ttl_file_comparer_end(); Xowd_ttl_file_comparer_end() {}
}

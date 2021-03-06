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
package gplx.xowa.xtns.wdatas.hwtrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wdatas.*;
import gplx.xowa.xtns.wdatas.core.*;
class Wdata_visitor__html_wtr implements Wdata_claim_visitor {
	private byte[] ttl; private Bry_bfr tmp_bfr; private Wdata_hwtr_msgs msgs; private Wdata_lbl_mgr lbl_mgr;
	private final Bry_fmtr tmp_time_fmtr = Bry_fmtr.new_(); private final Bry_bfr tmp_time_bfr = Bry_bfr.new_(32);
	public Wdata_visitor__html_wtr Init(byte[] ttl, Bry_bfr tmp_bfr, Wdata_hwtr_msgs msgs, Wdata_lbl_mgr lbl_mgr) {
		this.ttl = ttl; this.tmp_bfr = tmp_bfr; this.msgs = msgs; this.lbl_mgr = lbl_mgr;
		return this;
	}
	public void Visit_str(Wdata_claim_itm_str itm) {
		tmp_bfr.Add(itm.Val_str());
	}
	public void Visit_entity(Wdata_claim_itm_entity itm) {
		int entity_id = itm.Entity_id();
		byte[] text = lbl_mgr.Get_text__qid(entity_id);
		Wdata_hwtr_mgr.Write_link_wikidata(tmp_bfr, Bry_.Add(Byte_ascii.Ltr_Q, Int_.Xto_bry(entity_id)), text);			
	}
	public void Visit_monolingualtext(Wdata_claim_itm_monolingualtext itm) {
		tmp_bfr.Add(itm.Text());
		tmp_bfr.Add_byte(Byte_ascii.Space).Add_byte(Byte_ascii.Brack_bgn).Add(itm.Lang()).Add_byte(Byte_ascii.Brack_end);
	}
	public void Visit_quantity(Wdata_claim_itm_quantity itm) {
		try {
			DecimalAdp val = itm.Amount_as_num();
			DecimalAdp hi = itm.Ubound_as_num();
			DecimalAdp lo = itm.Lbound_as_num();
			DecimalAdp hi_diff = hi.Op_subtract(val);
			DecimalAdp lo_diff = val.Op_subtract(lo);
			float hi_diff_val = (float)hi_diff.Xto_double();
			float lo_diff_val = (float)lo_diff.Xto_double();
			tmp_bfr.Add(itm.Amount()).Add_byte_space();
			if (hi_diff.Eq(lo_diff)) {		// delta is same in both directions; EX: val=50 hi=60 lo=40 -> hi_diff == lo_diff == 10
				if (hi_diff_val != 0)		// skip if 0
					tmp_bfr.Add(msgs.Sym_plusminus()).Add_str(hi_diff.Xto_str());
			}
			else {							// delta is diff in both directions; EX: val=50 hi=60 lo=30 -> hi_diff == 10, lo_diff == 20
				if (hi_diff_val != 0)		// skip if 0
					tmp_bfr.Add(msgs.Sym_plus()).Add_str(hi_diff.Xto_str());
				if (lo_diff_val != 0) {		// skip if 0
					if (hi_diff_val != 0) tmp_bfr.Add(Time_plus_minus_spr);
					tmp_bfr.Add(msgs.Sym_minus()).Add_str(lo_diff.Xto_str());
				}
			}
			byte[] unit = itm.Unit();
			if (!Bry_.Eq(unit, Wdata_claim_itm_quantity.Unit_1))
				tmp_bfr.Add_byte_space().Add(unit);				
		} catch (Exception e) {
			Gfo_usr_dlg_._.Warn_many("", "", "failed to write quantity; ttl=~{0} pid=~{1} err=~{2}", String_.new_utf8_(ttl), itm.Pid(), Err_.Message_gplx_brief(e));
		}
	}	private static final byte[] Time_plus_minus_spr = Bry_.new_ascii_(" / ");
	public void Visit_time(Wdata_claim_itm_time itm) {
		try {
			Wdata_date date = itm.Time_as_date();
			boolean calendar_is_julian = itm.Calendar_is_julian();
			byte[] calendar_display = null;
			if (calendar_is_julian) {
				date = Wdata_date.Xto_julian(date);
				calendar_display = msgs.Time_julian();
			}
			Wdata_date.Xto_str(tmp_bfr, tmp_time_fmtr, tmp_time_bfr, msgs, date);
			if (calendar_display != null)
				tmp_bfr.Add_byte_space().Add(calendar_display);
		} catch (Exception e) {
			Gfo_usr_dlg_._.Warn_many("", "", "failed to write time; ttl=~{0} pid=~{1} err=~{2}", String_.new_utf8_(ttl), itm.Pid(), Err_.Message_gplx_brief(e));
		}
	}
	public void Visit_globecoordinate(Wdata_claim_itm_globecoordinate itm) {
		try {
			DecimalAdp precision_frac = itm.Prc_as_num();						// precision is a decimal; EX: .00027777
			int precision_int = Math_.Log10(DecimalAdp_.One.Op_divide(precision_frac).Xto_int());		// convert precision to log10 integer; EX: .00027777 -> 3600 -> 3
			gplx.xowa.xtns.mapSources.Map_dd2dms_func.Deg_to_dms(tmp_bfr, Bool_.Y, itm.Lng(), precision_int);
			tmp_bfr.Add_byte_comma().Add_byte_space();
			gplx.xowa.xtns.mapSources.Map_dd2dms_func.Deg_to_dms(tmp_bfr, Bool_.N, itm.Lat(), precision_int);
			byte[] glb_ttl = itm.Glb_ttl();
			if (glb_ttl != null) {
				byte[] glb_lbl = lbl_mgr.Get_text__ttl(glb_ttl, itm.Glb());
				tmp_bfr.Add_byte_space().Add_byte(Byte_ascii.Paren_bgn);
				Wdata_hwtr_mgr.Write_link_wikidata(tmp_bfr, glb_ttl, glb_lbl);
				tmp_bfr.Add_byte(Byte_ascii.Paren_end);
			}
		} catch (Exception e) {
			Gfo_usr_dlg_._.Warn_many("", "", "failed to write globecoordinate; ttl=~{0} pid=~{1} err=~{2}", String_.new_utf8_(ttl), itm.Pid(), Err_.Message_gplx_brief(e));
		}
	}
	public void Visit_system(Wdata_claim_itm_system itm) {
		switch (itm.Snak_tid()) {
			case Wdata_dict_snak_tid.Tid_somevalue:		tmp_bfr.Add(msgs.Val_tid_somevalue()); break;
			case Wdata_dict_snak_tid.Tid_novalue:		tmp_bfr.Add(msgs.Val_tid_novalue());   break;
		}
	}
}

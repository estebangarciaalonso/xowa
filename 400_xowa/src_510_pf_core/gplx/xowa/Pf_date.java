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
package gplx.xowa; import gplx.*;
class Pf_date_int extends Pf_func_base {
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bb) {
		DateAdp date = DateAdp_.MinValue;
		Xow_wiki wiki = ctx.Wiki(); Xol_lang lang = ctx.Lang();
	    switch (dateType) {
	        case DateType_utc: date = DateAdp_.Now().XtoUtc(); break;
	        case DateType_lcl: date = DateAdp_.Now(); break;
	        case DateType_rev: date = ctx.Page().Page_date(); break;
			default: throw Err_.unhandled(dateType);
	    }
		switch (id) {
			case Xol_kwd_grp_.Id_utc_year:
			case Xol_kwd_grp_.Id_lcl_year:
			case Xol_kwd_grp_.Id_rev_year:
				Pf_str_formatdate.Date_bldr().Format(wiki, lang, DateAdpFormatItm_.Year_len4, date, bb);
				break;
			case Xol_kwd_grp_.Id_utc_month_int_len2:
			case Xol_kwd_grp_.Id_lcl_month_int_len2:
			case Xol_kwd_grp_.Id_rev_month_int_len2:
				Pf_str_formatdate.Date_bldr().Format(wiki, lang, DateAdpFormatItm_.Month_int_len2, date, bb);
				break;
			case Xol_kwd_grp_.Id_utc_month_int:
			case Xol_kwd_grp_.Id_lcl_month_int:
			case Xol_kwd_grp_.Id_rev_month_int:
				Pf_str_formatdate.Date_bldr().Format(wiki, lang, DateAdpFormatItm_.Month_int, date, bb);
				break;
			case Xol_kwd_grp_.Id_utc_day_int_len2:
			case Xol_kwd_grp_.Id_lcl_day_int_len2:
			case Xol_kwd_grp_.Id_rev_day_int_len2:
				Pf_str_formatdate.Date_bldr().Format(wiki, lang, DateAdpFormatItm_.Day_int_len2, date, bb);
				break;
			case Xol_kwd_grp_.Id_utc_day_int:
			case Xol_kwd_grp_.Id_lcl_day_int:
			case Xol_kwd_grp_.Id_rev_day_int:
				Pf_str_formatdate.Date_bldr().Format(wiki, lang, DateAdpFormatItm_.Day_int, date, bb);
				break;
			case Xol_kwd_grp_.Id_lcl_hour:
			case Xol_kwd_grp_.Id_utc_hour:
				Pf_str_formatdate.Date_bldr().Format(wiki, lang, DateAdpFormatItm_.Hour_base24_len2, date, bb);
				break;
			case Xol_kwd_grp_.Id_lcl_dow:
			case Xol_kwd_grp_.Id_utc_dow:
				Pf_str_formatdate.Date_bldr().Format(wiki, lang, DateAdpFormatItm_.Dow_base1_int, date, bb);
				break;
			case Xol_kwd_grp_.Id_lcl_week:
			case Xol_kwd_grp_.Id_utc_week:
				Pf_str_formatdate.Date_bldr().Format(wiki, lang, DateAdpFormatItm_.WeekOfYear_int, date, bb);
				break;
			case Xol_kwd_grp_.Id_lcl_time:
			case Xol_kwd_grp_.Id_utc_time:		// 17:29
				bb.Add_int_fixed(date.Hour(), 2).Add_byte(Byte_ascii.Colon).Add_int_fixed(date.Minute(), 2);
				break;
			case Xol_kwd_grp_.Id_lcl_timestamp:	
			case Xol_kwd_grp_.Id_utc_timestamp:
			case Xol_kwd_grp_.Id_rev_timestamp:	// 20120123172956
				bb	.Add_int_fixed(date.Year()	, 4)
					.Add_int_fixed(date.Month()	, 2)
					.Add_int_fixed(date.Day()	, 2)
					.Add_int_fixed(date.Hour()	, 2)
					.Add_int_fixed(date.Minute(), 2)
					.Add_int_fixed(date.Second(), 2);
				break;
			default: throw Err_.unhandled(id);
		}
	}
	public Pf_date_int(int id, int dateType) {this.id = id; this.dateType = dateType;} private int dateType;
	@Override public int Id() {return id;} private int id;
	@Override public Pf_func New(int id, byte[] name) {return new Pf_date_int(id, dateType).Name_(name);}
	public static final int DateType_utc = 0, DateType_lcl = 1, DateType_rev = 2;
	public static final Pf_date_int
		  _UTC = new Pf_date_int(-1, DateType_utc)
		, _LCL = new Pf_date_int(-1, DateType_lcl)
		, _REV = new Pf_date_int(-1, DateType_rev);
}
class Pf_date_name extends Pf_func_base {
	public Pf_date_name(int id, int dateType, int segIdx, int baseIdx) {this.id = id; this.dateType = dateType; this.segIdx = segIdx; this.baseIdx = baseIdx;} private int dateType, segIdx, baseIdx;
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bb) {
		DateAdp date = DateAdp_.MinValue;
	    switch (dateType) {
	        case Pf_date_int.DateType_utc: date = DateAdp_.Now().XtoUtc(); break;
	        case Pf_date_int.DateType_lcl: date = DateAdp_.Now(); break;
	        case Pf_date_int.DateType_rev: date = ctx.Page().Page_date(); break;
			default: throw Err_.unhandled(dateType);
	    }
		byte[] val = ctx.Wiki().Msg_mgr().Val_by_id(baseIdx + date.Segment(segIdx));
		bb.Add(val);
//			translator.Translate(baseIdx, date.Segment(segIdx), bb);
	}
	@Override public int Id() {return id;} private int id;
	@Override public Pf_func New(int id, byte[] name) {return new Pf_date_name(id, dateType, segIdx, baseIdx).Name_(name);}
}

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
public class Pf_str_formatdate extends Pf_func_base {
	@Override public boolean Func_require_colon_arg() {return true;}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {
		int self_args_len = self.Args_len();
		byte[] date_ary = Eval_argx(ctx, src, caller, self);
		DateAdp date = Pf_xtn_time.ParseDate(date_ary, false, ctx.App().Utl_bry_bfr_mkr().Get_b512().Mkr_rls());
		byte[] fmt_ary = Pf_func_.EvalArgOrEmptyAry(ctx, src, caller, self, self_args_len, 0);
		if (fmt_ary == ByteAry_.Empty) {bfr.Add(date_ary); return;}	// no format given; add self;				
		int fmt_ary_len = fmt_ary.length;
		Object o = byteTrie.MatchAtCur(fmt_ary, 0, fmt_ary_len);
		if (o == null 
			|| o == Fmt_default) {// NOOP for default?
			bfr.Add(date_ary);
			return;
		}
		DateAdpFormatItm[] fmt_itm_ary = (DateAdpFormatItm[])o;
		date_bldr.Format(ctx.Wiki(), ctx.Lang(), fmt_itm_ary, date, bfr);
	}
	public static Pf_str_formatdate_bldr Date_bldr() {return date_bldr;} static Pf_str_formatdate_bldr date_bldr = new Pf_str_formatdate_bldr();
	
	@Override public int Id() {return Xol_kwd_grp_.Id_str_formatdate;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_str_formatdate().Name_(name);}
	
	private static final byte[] Ary_dmy = ByteAry_.new_ascii_("dmy"), Ary_mdy = ByteAry_.new_ascii_("mdy"), Ary_ymd = ByteAry_.new_ascii_("ymd")
		, Ary_ISO_8601 = ByteAry_.new_ascii_("ISO 8601"), Ary_default = ByteAry_.new_ascii_("default");
	private static final DateAdpFormatItm[] 
		  Fmt_dmy = new DateAdpFormatItm[] {DateAdpFormatItm_.Day_int, DateAdpFormatItm_.Byte_space, DateAdpFormatItm_.Month_name, DateAdpFormatItm_.Byte_space, DateAdpFormatItm_.Year_len4}
		, Fmt_mdy = new DateAdpFormatItm[] {DateAdpFormatItm_.Month_name, DateAdpFormatItm_.Byte_space, DateAdpFormatItm_.Day_int, DateAdpFormatItm_.Byte_comma, DateAdpFormatItm_.Byte_space, DateAdpFormatItm_.Year_len4}
		, Fmt_ymd = new DateAdpFormatItm[] {DateAdpFormatItm_.Year_len4, DateAdpFormatItm_.Byte_space, DateAdpFormatItm_.Month_name, DateAdpFormatItm_.Byte_space, DateAdpFormatItm_.Day_int}
		, Fmt_ISO_8601 = new DateAdpFormatItm[] {DateAdpFormatItm_.Year_len4, DateAdpFormatItm_.Byte_dash, DateAdpFormatItm_.Month_int_len2, DateAdpFormatItm_.Byte_dash, DateAdpFormatItm_.Day_int_len2}
		, Fmt_default = new DateAdpFormatItm[] {DateAdpFormatItm_.Byte_space}
		;
	private static ByteTrieMgr_fast byteTrie = ByteTrieMgr_fast.cs_().Add(Ary_dmy, Fmt_dmy).Add(Ary_mdy, Fmt_mdy).Add(Ary_ymd, Fmt_ymd).Add(Ary_ISO_8601, Fmt_ISO_8601).Add(Ary_default, Fmt_default);
}
class IntAryBldr {
	public int[] XtoIntAry() {return ary;} private int[] ary;
	public IntAryBldr Set(int idx, int val) {ary[idx] = val; return this;}
	public IntAryBldr(int len) {ary = new int[len];}
}
class DateAdpFormatItm_ {
	public static final int
		  Tid_seg_int			=  1
		, Tid_hour_base12		=  2
		, Tid_dow_base0			=  3
		, Tid_seg_str			=  4
		, Tid_year_isLeap		=  5
		, Tid_timestamp_unix	=  6
		, Tid_raw_ary			=  7
		, Tid_raw_byt			=  8
		, Tid_dayOfYear			=  9
		, Tid_daysInMonth		= 10
		, Tid_AmPm				= 11
		, Tid_roman				= 12
		, Tid_iso_fmt			= 13
		, Tid_rfc_5322			= 14
		, Tid_raw				= 15
		;
	public static final DateAdpFormatItm 
		  Year_len4				= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_year			, 4, Bool_.Y)
		, Year_len2				= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_year			, 2, Bool_.Y)
		, Month_int_len2		= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_month		, 2, Bool_.Y)
		, Month_int				= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_month		, 2, Bool_.N)
		, Day_int_len2			= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_day			, 2, Bool_.Y)
		, Day_int				= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_day			, 2, Bool_.N)
		, Hour_base24_len2		= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_hour			, 2, Bool_.Y)
		, Hour_base24			= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_hour			, 2, Bool_.N)
		, Hour_base12_len2		= new DateAdpFormatItm_hour_base12(Bool_.Y)
		, Hour_base12			= new DateAdpFormatItm_hour_base12(Bool_.N)
		, Minute_int_len2		= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_minute		, 2, Bool_.Y)
		, Second_int_len2		= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_second		, 2, Bool_.Y)
		, Dow_base1_int			= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_dayOfWeek	, 1, Bool_.Y)
		, Dow_base0_int			= new DateAdpFormatItm_dow_base0()
		, WeekOfYear_int		= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_weekOfYear	, 2, Bool_.N)
		, WeekOfYear_int_len2	= new DateAdpFormatItm_seg_int(DateAdp_.SegIdx_weekOfYear	, 2, Bool_.Y)
		, Month_abrv			= new DateAdpFormatItm_seg_str(DateAdp_.SegIdx_month, Xol_msg_itm_.Id_dte_month_abrv_jan - Int_.Base1)		// Jan
		, Month_name			= new DateAdpFormatItm_seg_str(DateAdp_.SegIdx_month, Xol_msg_itm_.Id_dte_month_name_january - Int_.Base1)	// January
		, Month_gen				= new DateAdpFormatItm_seg_str(DateAdp_.SegIdx_month, Xol_msg_itm_.Id_dte_month_gen_january - Int_.Base1)	// January
		, Dow_abrv				= new DateAdpFormatItm_seg_str(DateAdp_.SegIdx_dayOfWeek, Xol_msg_itm_.Id_dte_dow_abrv_sun)					// Sun
		, Dow_name				= new DateAdpFormatItm_seg_str(DateAdp_.SegIdx_dayOfWeek, Xol_msg_itm_.Id_dte_dow_name_sunday)				// Sunday
		, Year_isLeap			= new DateAdpFormatItm_year_isLeap()
		, Timestamp_unix		= new DateAdpFormatItm_timestamp_unix()
		, Byte_space			= new DateAdpFormatItm_raw_byt(Byte_ascii.Space)
		, Byte_comma			= new DateAdpFormatItm_raw_byt(Byte_ascii.Comma)
		, Byte_dash				= new DateAdpFormatItm_raw_byt(Byte_ascii.Dash)
		, DayOfYear_int			= new DateAdpFormatItm_dayOfYear()
		, DaysInMonth_int		= new DateAdpFormatItm_daysInMonth()
		, AmPm_lower			= new DateAdpFormatItm_AmPm(true)
		, AmPm_upper			= new DateAdpFormatItm_AmPm(false)
		, Roman					= new DateAdpFormatItm_roman()
		, Raw					= new DateAdpFormatItm_raw()
		, Iso_fmt				= new DateAdpFormatItm_iso_fmt()
		, Rfc_5322				= new DateAdpFormatItm_rfc_5322()
		;
}
class DateAdpFormatItm_seg_int implements DateAdpFormatItm {
	public DateAdpFormatItm_seg_int(int segIdx, int len, boolean fixed_len) {this.segIdx = segIdx; this.fixed_len = fixed_len; this.len = len;} private int segIdx, len; boolean fixed_len;
	public int TypeId() {return DateAdpFormatItm_.Tid_seg_int;}
	public int SegIdx() {return segIdx;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {
		int val = date.Segment(segIdx);
		if (fixed_len)	bfr.Add_int_fixed(val, len);
		else			bfr.Add_int_variable(val);
	}
}
class DateAdpFormatItm_raw implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_raw;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {
		// TODO: should flag .Raw() on bldr to skip transliterating numerals in foreign languages; DATE:2013-12-31
	}
}
class DateAdpFormatItm_seg_str implements DateAdpFormatItm {
	public DateAdpFormatItm_seg_str(int segIdx, int type) {this.segIdx = segIdx; this.type = type;} private int segIdx, type;
	public int TypeId() {return DateAdpFormatItm_.Tid_seg_str;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {
		DateAdpTranslator_xapp.Translate(wiki, lang, type, date.Segment(segIdx), bfr);
	}
}
class DateAdpFormatItm_year_isLeap implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_year_isLeap;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {bfr.Add_int_fixed(DateAdp_.IsLeapYear(date.Year()) ? 1 : 0, 1);}
}
class DateAdpFormatItm_hour_base12 implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_hour_base12;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {
		int val = date.Hour();
		switch (val) {
			case 0: val = 12; break;
			case 13: case 14: case 15: case 16: case 17: case 18: case 19: case 20: case 21: case 22: case 23: val -= 12; break;
			default: break;
		}
		if (fixed_len)	bfr.Add_int_fixed(val, 2);
		else			bfr.Add_int_variable(val);
	}
	public DateAdpFormatItm_hour_base12(boolean fixed_len) {this.fixed_len = fixed_len;} private boolean fixed_len;
}
class DateAdpFormatItm_timestamp_unix implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_timestamp_unix;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {bfr.Add_long_variable(date.Timestamp_unix());}
}
class DateAdpFormatItm_raw_ary implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_raw_ary;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {bfr.Add_mid(src, bgn, end);}
	public DateAdpFormatItm_raw_ary(byte[] src, int bgn, int end) {this.src = src; this.bgn = bgn; this.end = end;} private byte[] src; int bgn; int end;
}
class DateAdpFormatItm_raw_byt implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_raw_byt;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {bfr.Add_byte(b);}
	public DateAdpFormatItm_raw_byt(byte b) {this.b = b;} private byte b;
}
class DateAdpFormatItm_daysInMonth implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_daysInMonth;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {bfr.Add_int_variable(DateAdp_.DaysInMonth(date));}
}
class DateAdpFormatItm_dayOfYear implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_dayOfYear;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {bfr.Add_int_variable(date.DayOfYear() - Int_.Base1);}	// php is base1; .net/java is base0
}
class DateAdpFormatItm_AmPm implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_AmPm;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {
		boolean am = date.Hour() < 13;
		byte[] val = null;
		if		( am &&  lower) val = Ary_am_lower;
		else if ( am && !lower) val = Ary_am_upper;
		else if (!am &&  lower) val = Ary_pm_lower;
		else if (!am && !lower) val = Ary_pm_upper;
		bfr.Add(val);
	}	static final byte[] Ary_am_upper = ByteAry_.new_ascii_("AM"), Ary_pm_upper = ByteAry_.new_ascii_("PM"), Ary_am_lower = ByteAry_.new_ascii_("am"), Ary_pm_lower = ByteAry_.new_ascii_("pm");
	public DateAdpFormatItm_AmPm(boolean lower) {this.lower = lower;} private boolean lower;
}
class DateAdpFormatItm_dow_base0 implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_dow_base0;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {
		int dow = date.DayOfWeek();
		if (dow == 0) dow = 7;
		bfr.Add_int_fixed(dow, 1);
	}
}
class DateAdpFormatItm_roman implements DateAdpFormatItm {
	public int TypeId() {return DateAdpFormatItm_.Tid_roman;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {
		int nxt_idx = bldr.Idx_cur() + 1;
		DateAdpFormatItm[] ary = bldr.Fmt_itm_ary();
		if (nxt_idx < ary.length) {
			DateAdpFormatItm itm = (DateAdpFormatItm)ary[nxt_idx];
			if (itm.TypeId() == DateAdpFormatItm_.Tid_seg_int) {
				DateAdpFormatItm_seg_int nxt_int = (DateAdpFormatItm_seg_int)ary[nxt_idx];	// FUTURE: should check tkn type
				int v = date.Segment(nxt_int.SegIdx());
				gplx.xowa.Pfxtp_roman.ToRoman(v, bfr);
				bldr.Idx_nxt_(nxt_idx + 1);
				return;
			}
		}
		bfr.Add_str("xf");
	}
}
class DateAdpFormatItm_iso_fmt implements DateAdpFormatItm {
	public DateAdpFormatItm_iso_fmt() {}
	public int TypeId() {return DateAdpFormatItm_.Tid_iso_fmt;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {
		bfr.Add_str(date.XtoStr_fmt("yyyy-MM-dd"));
		bfr.Add_byte(Byte_ascii.Ltr_T);
		bfr.Add_str(date.XtoStr_fmt("HH:mm:ss"));
		bfr.Add_str(date.XtoStr_tz());
	}
}
class DateAdpFormatItm_rfc_5322 implements DateAdpFormatItm {
	public DateAdpFormatItm_rfc_5322() {}
	public int TypeId() {return DateAdpFormatItm_.Tid_rfc_5322;}
	public void Fmt(Xow_wiki wiki, Xol_lang lang, Pf_str_formatdate_bldr bldr, DateAdp date, ByteAryBfr bfr) {// Mon, 02 Jan 2012 10:15:01 +0000
		int dow = date.DayOfWeek();
		DateAdpTranslator_xapp.Translate(wiki, lang, DateAdp_.SegIdx_dayOfWeek, dow, bfr);
		bfr.Add_byte(Byte_ascii.Comma).Add_byte(Byte_ascii.Space);
		bfr.Add_str(date.XtoStr_fmt("dd MMM yyyy HH:mm:ss"));	// NOTE: always UTC time 
		bfr.Add(CONST_timezone);								// NOTE: always UTC time zone
	}	static final byte[] CONST_timezone = ByteAry_.new_ascii_(" +0000"); 
}

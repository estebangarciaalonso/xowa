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
public class Pf_xtn_time extends Pf_func_base {
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bb) {// REF.MW:ParserFunctions_body.php
		int self_args_len = self.Args_len();
		byte[] arg_fmt = Eval_argx(ctx, src, caller, self);
		DateAdpFormatItm[] fmt_ary = Parse(ctx, arg_fmt);
		byte[] arg_date = Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, self_args_len, 0);
		byte[] arg_lang = Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, self_args_len, 1);
		ByteAryBfr error_bfr = ByteAryBfr.new_();
		DateAdp date = ParseDate(arg_date, utc, error_bfr);
		if (error_bfr.Len() > 0)
			bb.Add_str("<strong class=\"error\">").Add_bfr_and_clear(error_bfr).Add_str("</strong>");
		else {
			Xol_lang lang = ctx.Lang();
			if (ByteAry_.Len_gt_0(arg_lang)) {
				Xol_lang_itm specified_lang_itm = Xol_lang_itm_.Get_by_key(arg_lang);
				if (specified_lang_itm != null) {	// NOTE: if lang_code is bad, then ignore (EX:bad_code)
					Xol_lang specified_lang = ctx.Wiki().App().Lang_mgr().Get_by_key_or_new(arg_lang);
					lang = specified_lang;	
				}
			}
			Pf_str_formatdate.Date_bldr().Format(ctx.Wiki(), lang, fmt_ary, date, bb);
		}
	}
	public static DateAdp ParseDate(byte[] date, boolean utc, ByteAryBfr error_bfr) {
		if (date == ByteAry_.Empty) return utc ? DateAdp_.Now().XtoUtc() : DateAdp_.Now();
		try {
			DateAdp rv = new gplx.xowa.Pxd_parser().Parse(date, error_bfr);
			return rv;
		}
		catch (Exception exc) {
			Err_.Noop(exc);
			error_bfr.Add_str("Invalid time");
			return null;
		}
	}
	public static DateAdpFormatItm[] Parse(Xop_ctx ctx, byte[] fmt) {
		ByteTrieMgr_fast trie = Pf_xtn_time_format.trie_(ctx.Wiki().Lang());
		int i = 0, fmt_len = fmt.length;
		fmt_itms.Clear(); int raw_bgn = String_.Neg1_pos; byte raw_byt = Byte_.Zero;
		while (i < fmt_len) {
			byte b = fmt[i];
			Object o = trie.Match(b, fmt, i, fmt_len);
			if (o != null) {
				if (raw_bgn != String_.Neg1_pos) {fmt_itms.Add(i - raw_bgn == 1 ? new DateAdpFormatItm_raw_byt(raw_byt) : (DateAdpFormatItm)new DateAdpFormatItm_raw_ary(fmt, raw_bgn, i)); raw_bgn = String_.Neg1_pos;}
				fmt_itms.Add((DateAdpFormatItm)o);
				i = trie.Match_pos();
			}
			else {
				switch (b) {
					case Byte_ascii.Backslash:
						if (raw_bgn != String_.Neg1_pos) {fmt_itms.Add(i - raw_bgn == 1 ? new DateAdpFormatItm_raw_byt(raw_byt) : (DateAdpFormatItm)new DateAdpFormatItm_raw_ary(fmt, raw_bgn, i)); raw_bgn = String_.Neg1_pos;}
						++i; // peek next char
						if (i == fmt_len)	// trailing backslash; add one; EX: "b\" -> "b\" not "b"
							fmt_itms.Add(new DateAdpFormatItm_raw_byt(Byte_ascii.Backslash));
						else
							fmt_itms.Add(new DateAdpFormatItm_raw_byt(fmt[i]));
						++i;
						break;
					case Byte_ascii.Quote:
						if (raw_bgn != String_.Neg1_pos) {fmt_itms.Add(i - raw_bgn == 1 ? new DateAdpFormatItm_raw_byt(raw_byt) : (DateAdpFormatItm)new DateAdpFormatItm_raw_ary(fmt, raw_bgn, i)); raw_bgn = String_.Neg1_pos;}
						++i; // skip quote_bgn
						raw_bgn = i;
						while (i < fmt_len) {
							b = fmt[i];
							if (b == Byte_ascii.Quote) {
								break;
							}
							else
								++i;
						}
						fmt_itms.Add(i - raw_bgn == 0 ? new DateAdpFormatItm_raw_byt(Byte_ascii.Quote) : (DateAdpFormatItm)new DateAdpFormatItm_raw_ary(fmt, raw_bgn, i));
						raw_bgn = String_.Neg1_pos;
						++i; // skip quote_end
						break;
					default:
						if (raw_bgn == String_.Neg1_pos) {raw_bgn = i; raw_byt = b;}
						i += gplx.intl.Utf8_.CharLen(b);
						break;
				}
			}
		}
		if (raw_bgn != String_.Neg1_pos) {fmt_itms.Add(fmt_len - raw_bgn == 1 ? new DateAdpFormatItm_raw_byt(fmt[fmt_len - 1]) : (DateAdpFormatItm)new DateAdpFormatItm_raw_ary(fmt, raw_bgn, fmt_len)); raw_bgn = String_.Neg1_pos;}
		return (DateAdpFormatItm[])fmt_itms.XtoAry(DateAdpFormatItm.class);
	}	static ListAdp fmt_itms = ListAdp_.new_();
	@Override public int Id() {return Xol_kwd_grp_.Id_xtn_time;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_xtn_time(utc).Name_(name);}
	public static final Pf_xtn_time _Lcl = new Pf_xtn_time(false), _Utc = new Pf_xtn_time(true);
	Pf_xtn_time(boolean utc) {this.utc = utc;} private boolean utc;
}
class Pf_xtn_time_format {
	private static ByteTrieMgr_fast timeTrie;
	public static ByteTrieMgr_fast trie_(Xol_lang lang) {
		if (timeTrie != null) return timeTrie;
		ByteTrieMgr_fast rv = ByteTrieMgr_fast.cs_();
		rv.Add(Byte_ascii.Ltr_Y	, DateAdpFormatItm_.Year_len4);				// 2012
		rv.Add(Byte_ascii.Ltr_y	, DateAdpFormatItm_.Year_len2);				// 12
		rv.Add(Byte_ascii.Ltr_L	, DateAdpFormatItm_.Year_isLeap);			// 1,0
		rv.Add(Byte_ascii.Ltr_o	, DateAdpFormatItm_.Year_len4);				// 2012: ISO-8601; don't know why it's different vs Ltr_Y
		rv.Add(Byte_ascii.Ltr_n	, DateAdpFormatItm_.Month_int);				// 1
		rv.Add(Byte_ascii.Ltr_m	, DateAdpFormatItm_.Month_int_len2);		// 01
		rv.Add(Byte_ascii.Ltr_M	, DateAdpFormatItm_.Month_abrv);			// Jan
		rv.Add(Byte_ascii.Ltr_F	, DateAdpFormatItm_.Month_name);			// January
		rv.Add(Fmt_month_gen	, DateAdpFormatItm_.Month_gen);				// January
		rv.Add(Byte_ascii.Ltr_W	, DateAdpFormatItm_.WeekOfYear_int_len2);	// 01
		rv.Add(Byte_ascii.Ltr_j	, DateAdpFormatItm_.Day_int);				// 1
		rv.Add(Byte_ascii.Ltr_d	, DateAdpFormatItm_.Day_int_len2);			// 01
		rv.Add(Byte_ascii.Ltr_z	, DateAdpFormatItm_.DayOfYear_int);			// 0
		rv.Add(Byte_ascii.Ltr_D	, DateAdpFormatItm_.Dow_abrv);				// Sun
		rv.Add(Byte_ascii.Ltr_l	, DateAdpFormatItm_.Dow_name);				// Sunday
		rv.Add(Byte_ascii.Ltr_N	, DateAdpFormatItm_.Dow_base0_int);			// 1; Sunday=7
		rv.Add(Byte_ascii.Ltr_w	, DateAdpFormatItm_.Dow_base1_int);			// 1; Sunday=0
		rv.Add(Byte_ascii.Ltr_a	, DateAdpFormatItm_.AmPm_lower);			// am/pm
		rv.Add(Byte_ascii.Ltr_A	, DateAdpFormatItm_.AmPm_upper);			// AM/PM
		rv.Add(Byte_ascii.Ltr_g	, DateAdpFormatItm_.Hour_base12);			// 1;  Base12
		rv.Add(Byte_ascii.Ltr_h	, DateAdpFormatItm_.Hour_base12_len2);		// 01; Base12; pad2
		rv.Add(Byte_ascii.Ltr_G	, DateAdpFormatItm_.Hour_base24);			// 13; Base24;
		rv.Add(Byte_ascii.Ltr_H	, DateAdpFormatItm_.Hour_base24_len2);		// 13; Base24; pad2
		rv.Add(Byte_ascii.Ltr_i	, DateAdpFormatItm_.Minute_int_len2);		// 04
		rv.Add(Byte_ascii.Ltr_s	, DateAdpFormatItm_.Second_int_len2);		// 05
		rv.Add(Byte_ascii.Ltr_t	, DateAdpFormatItm_.DaysInMonth_int);		// 31
		rv.Add(Fmt_roman		, DateAdpFormatItm_.Roman);					// MCXI
		rv.Add(Byte_ascii.Ltr_U	, DateAdpFormatItm_.Timestamp_unix);		// 1343865600
		rv.Add(Byte_ascii.Ltr_c	, DateAdpFormatItm_.Iso_fmt);				// 2012-01-02T03:04:05+00:00
		rv.Add(Byte_ascii.Ltr_r	, DateAdpFormatItm_.Rfc_5322);				// Mon 02 Jan 2012 08:04:05 +0000
		rv.Add(Fmt_raw			, DateAdpFormatItm_.Raw);					// NOTE: really does nothing; REF.MW: Language.php|sprintfdate does $s .= $num; DATE:2013-12-31
		rv.Add(Fmt_raw_toggle	, DateAdpFormatItm_.Raw);					// MCXI
		// foreign
		// space
		// "
		// all else is ignored
		timeTrie = rv;
		return rv;
	}
	private static final byte[] Fmt_month_gen = ByteAry_.new_utf8_("xg"), Fmt_roman = ByteAry_.new_utf8_("xr"), Fmt_raw = ByteAry_.new_utf8_("xn"), Fmt_raw_toggle = ByteAry_.new_utf8_("xN");
}
class DateAdpTranslator_xapp {
	public static void Translate(Xow_wiki wiki, Xol_lang lang, int type, int val, ByteAryBfr bb) {
		lang.Init_by_load_assert();
		byte[] itm_val = lang.Msg_mgr().Val_by_id(type + val); if (itm_val == null) return;
		bb.Add(itm_val);
	}
}
class Pfxtp_roman {
	public static void ToRoman(int num, ByteAryBfr bfr) {
		if (num > 3000 || num <= 0) {
			bfr.Add_int_variable(num);
			return;
		}
		int pow10 = 1000;
		for (int i = 3; i > -1; i--) {
			if (num >= pow10) {
				bfr.Add(Names[i][Math_.Trunc(num / pow10)]);
			}
			num %= pow10;
			pow10 /= 10;
		} 
	}
	private static byte[][][] Names = new byte[][][]
		{ Bry_dim2_new_("", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X")
		, Bry_dim2_new_("", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC", "C")
		, Bry_dim2_new_("", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM", "M")
		, Bry_dim2_new_("", "M", "MM", "MMM")
		};
	private static byte[][] Bry_dim2_new_(String... names) {
		int len = names.length;
		byte[][] rv = new byte[len][];
		for (int i = 0; i < len; i++)
			rv[i] = ByteAry_.new_utf8_(names[i]);
		return rv;
	}
}

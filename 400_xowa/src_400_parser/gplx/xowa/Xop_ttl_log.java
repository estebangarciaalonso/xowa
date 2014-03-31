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
public class Xop_ttl_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "ttl");
	public static final Gfo_msg_itm
		Len_0								= Gfo_msg_itm_.new_warn_(owner, "Len_0")
	, Len_max							= Gfo_msg_itm_.new_warn_(owner, "Len_max")
	, Ttl_has_ns_but_no_page			= Gfo_msg_itm_.new_warn_(owner, "Ttl_has_ns_but_no_page")			
	, Ttl_is_ns_only					= Gfo_msg_itm_.new_warn_(owner, "Ttl_is_ns_only")
	, Amp_unknown						= Gfo_msg_itm_.new_warn_(owner, "Amp_unknown")
	, Comment_eos						= Gfo_msg_itm_.new_warn_(owner, "Comment_eos")
	, Invalid_char						= Gfo_msg_itm_.new_warn_(owner, "Invalid_char")
	;
}
class Xop_lnke_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "lnke");
	public static final Gfo_msg_itm Dangling = Gfo_msg_itm_.new_note_(owner, "dangling");	// NOTE: WP.BOT:YOBOT;EX.WP: Pan_flute
}
class Xop_comment_log {		
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "comment");
	public static final Gfo_msg_itm
		  Eos								= Gfo_msg_itm_.new_warn_(owner, "eos")
		;
}
class Xop_amp_log {		
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "amp");
	public static final Gfo_msg_itm
		  Invalid_hex						= Gfo_msg_itm_.new_warn_(owner, "invalid_hex")
		, Invalid_dec						= Gfo_msg_itm_.new_warn_(owner, "invalid_dec")
		, Eos								= Gfo_msg_itm_.new_warn_(owner, "eos")
		;
}
class Xop_hr_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "hr");
	public static final Gfo_msg_itm Len_5_or_more = Gfo_msg_itm_.new_warn_(owner, "len_5_or_more");
}
class Xop_hdr_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "hdr");
	public static final Gfo_msg_itm
		  Dangling_hdr						= Gfo_msg_itm_.new_warn_(owner, "dangling_hdr")
		, Mismatched						= Gfo_msg_itm_.new_warn_(owner, "mismatched")
		, Len_1								= Gfo_msg_itm_.new_warn_(owner, "len_1")
		, Len_7_or_more						= Gfo_msg_itm_.new_warn_(owner, "len_7_or_more")
		;
}
class Xop_lnki_log {		
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "lnki");
	public static final Gfo_msg_itm
		  Upright_val_is_invalid			= Gfo_msg_itm_.new_warn_(owner, "upright_val_is_invalid")
		, Escaped_lnki						= Gfo_msg_itm_.new_warn_(owner, "escaped_lnki")
		, Key_is_empty						= Gfo_msg_itm_.new_warn_(owner, "key_is_empty")
		, Ext_is_missing					= Gfo_msg_itm_.new_warn_(owner, "ext_is_missing")
		;
}
class Xop_xnde_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "xnde");
	public static final Gfo_msg_itm
		  Dangling_xnde						= Gfo_msg_itm_.new_warn_(owner, "dangling_xnde")
		, End_tag_not_allowed				= Gfo_msg_itm_.new_note_(owner, "end_tag_not_allowed")
		, Escaped_xnde						= Gfo_msg_itm_.new_warn_(owner, "escaped_xnde")
		, Invalid_char						= Gfo_msg_itm_.new_warn_(owner, "invalid_char")
		, Xtn_end_not_found					= Gfo_msg_itm_.new_warn_(owner, "xtn_end_not_found")
		, Invalid_tbl_sub					= Gfo_msg_itm_.new_warn_(owner, "invalid_tbl_sub")
		, Invalid_nest						= Gfo_msg_itm_.new_warn_(owner, "invalid_nest")
		, No_inline							= Gfo_msg_itm_.new_warn_(owner, "no_inline")
		, Tbl_sub_already_opened			= Gfo_msg_itm_.new_warn_(owner, "tbl_sub_already_opened")
		, Auto_closing_section				= Gfo_msg_itm_.new_warn_(owner, "auto_closing_section")
		, Eos_while_closing_tag				= Gfo_msg_itm_.new_warn_(owner, "eos_while_closing_tag")
		, Sub_sup_swapped					= Gfo_msg_itm_.new_warn_(owner, "sub_sup_swapped")
		, Restricted_tag					= Gfo_msg_itm_.new_warn_(owner, "restricted_tag")
		;
}
class Xop_tblw_log {		
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "tblw");
	public static final Gfo_msg_itm
		  Dangling							= Gfo_msg_itm_.new_warn_(owner, "dangling_tblw")
		, Elem_without_tbl					= Gfo_msg_itm_.new_warn_(owner, "elem_without_tbl")
//			, Row_trailing						= Gfo_msg_itm_.new_warn_(owner, "Row_trailing")
		, Caption_after_tr					= Gfo_msg_itm_.new_warn_(owner, "caption_after_tr")
		, Caption_after_td					= Gfo_msg_itm_.new_warn_(owner, "caption_after_td")
		, Caption_after_tc					= Gfo_msg_itm_.new_warn_(owner, "caption_after_tc")
		, Hdr_after_cell					= Gfo_msg_itm_.new_warn_(owner, "hdr_after_cell")
		, Tbl_empty							= Gfo_msg_itm_.new_warn_(owner, "tbl_empty")
		;
}
class Xop_tmpl_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "tmpl");
	public static final Gfo_msg_itm
		  Escaped_tmpl						= Gfo_msg_itm_.new_warn_(owner, "Escaped_tmpl")
		, Escaped_end						= Gfo_msg_itm_.new_warn_(owner, "Escaped_end")
		, Key_is_empty						= Gfo_msg_itm_.new_note_(owner, "Key_is_empty")
		, Dangling							= Gfo_msg_itm_.new_note_(owner, "Dangling_tmpl")
		, Tmpl_end_autoCloses_something		= Gfo_msg_itm_.new_note_(owner, "Tmpl_end_autoCloses_something")
		, Tmpl_is_empty						= Gfo_msg_itm_.new_note_(owner, "Tmpl_is_empty")
		;
}

class Xop_apos_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "apos");
	public static final Gfo_msg_itm
		  Bold_converted_to_ital			= Gfo_msg_itm_.new_note_(owner, "Bold_converted_to_ital")
		, Dangling_apos						= Gfo_msg_itm_.new_note_(owner, "Dangling_apos")
		, Multiple_apos						= Gfo_msg_itm_.new_note_(owner, "Multiple_apos")
		;
//		public final RscStrItm_arg
//			  Dangling_apos_typ					= new RscStrItm_arg(_mgr, "closing_typ")
//			;
}
class Xop_misc_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "super");
	public static final Gfo_msg_itm
		  Eos								= Gfo_msg_itm_.new_warn_(owner, "End_of_string")
		;
}
class Xot_prm_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "tmpl_defn_arg");
	public static final Gfo_msg_itm
		  Dangling							= Gfo_msg_itm_.new_warn_(owner, "Dangling_tmpl_defn_arg")
		, Elem_without_tbl					= Gfo_msg_itm_.new_warn_(owner, "Elem_without_tbl")
		, Lkp_is_nil						= Gfo_msg_itm_.new_note_(owner, "Lkp_is_nil")
		, Lkp_and_pipe_are_nil				= Gfo_msg_itm_.new_warn_(owner, "Lkp_and_pipe_are_nil")
		, Prm_has_2_or_more					= Gfo_msg_itm_.new_note_(owner, "Prm_has_2_or_more")
		;
}
class Xop_curly_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "curly");
	public static final Gfo_msg_itm
		  Bgn_not_found						= Gfo_msg_itm_.new_warn_(owner, "Bgn_not_found")
		, End_should_not_autoclose_anything	= Gfo_msg_itm_.new_warn_(owner, "End_should_not_autoclose_anything")
		, Bgn_len_0							= Gfo_msg_itm_.new_warn_(owner, "Bgn_len_0")
		, Bgn_len_1							= Gfo_msg_itm_.new_warn_(owner, "Bgn_len_1")
		, Tmpl_is_empty						= Gfo_msg_itm_.new_warn_(owner, "Tmpl_is_empty")
		;
}
class Xop_redirect_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "redirect");
	public static final Gfo_msg_itm
		  False_match						= Gfo_msg_itm_.new_warn_(owner, "False_match")
		, Lnki_not_found					= Gfo_msg_itm_.new_warn_(owner, "Lnki_not_found")
		;
}
class Xop_tag_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "tag");
	public static final Gfo_msg_itm
		  Invalid							= Gfo_msg_itm_.new_warn_(owner, "Invalid")
		;
}
class Pf_xtn_titleparts_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "tmpl_func_titleparts");
	public static final Gfo_msg_itm
		  Len_is_invalid					= Gfo_msg_itm_.new_warn_(owner, "Len_is_invalid")
		, Bgn_is_invalid					= Gfo_msg_itm_.new_warn_(owner, "Bgn_is_invalid")
		;
}
class Pf_xtn_time_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "time_parser");
	public static final Gfo_msg_itm
		  Invalid_day					= Gfo_msg_itm_.new_warn_(owner, "Invalid day: ~{0}")
		, Invalid_month					= Gfo_msg_itm_.new_warn_(owner, "Invalid month: ~{0}")
		, Invalid_year					= Gfo_msg_itm_.new_warn_(owner, "Invalid year: ~{0}")
		, Invalid_year_mid				= Gfo_msg_itm_.new_warn_(owner, "Invalid date: 4 digit year must be either yyyy-##-## or ##-##-yyyy")
		, Invalid_hour					= Gfo_msg_itm_.new_warn_(owner, "Invalid hour: ~{0}")
		, Invalid_minute				= Gfo_msg_itm_.new_warn_(owner, "Invalid minute: ~{0}")
		, Invalid_second				= Gfo_msg_itm_.new_warn_(owner, "Invalid second: ~{0}")
		, Invalid_date					= Gfo_msg_itm_.new_warn_(owner, "Invalid date: ~{0}")
		;
}
//	class Pf_func_lang_log {
//		private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "tmpl_func_lang");
//		public static final Gfo_msg_itm
//			  Arg_out_of_bounds					= Gfo_msg_itm_.new_warn_(owner, "Arg_out_of_bounds")
//			;
//	}
//	class Mwl_expr_log {
//		private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "expr");
//		public static final Gfo_msg_itm
//			  Divide_by_zero					= Gfo_msg_itm_.new_warn_(owner, "Divide_by_zero")
//			, Expr_len0							= Gfo_msg_itm_.new_warn_(owner, "Expr_len0")
//			;
//	}

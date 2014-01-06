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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
class Scrib_lua_regx_converter {
	public Scrib_lua_regx_converter() {Init();}
	public String Regx() {return regx;} private String regx;
	public boolean Anypos() {return anypos;} private boolean anypos;
	public ListAdp Grps() {return grps;} ListAdp grps = ListAdp_.new_(), grps_parens = ListAdp_.new_(); ListAdp grps_open = ListAdp_.new_();
	public String Parse(byte[] src, boolean no_anchor) {
		int len = src.length;
		boolean q_flag = false;
		anypos = false;
		grps.Clear(); grps_open.Clear(); grps_parens.Clear();
		int grps_len = 0;
		int bct = 0;
//			bfr.Add_byte(Byte_ascii.Slash); // NOTE: do not add PHP "/" at start
		for (int i = 0; i < len; i++) {
			byte cur = src[i];
			switch (cur) {
				case Byte_ascii.Pow:
					q_flag = i != 0;
					bfr.Add(no_anchor || q_flag ? Bry_pow_escaped : Bry_pow_literal);
					break;
				case Byte_ascii.Dollar:
					q_flag = i < len - 1;
					bfr.Add(q_flag ? Bry_dollar_escaped : Bry_dollar_literal);
					break;
				case Byte_ascii.Paren_bgn: {
					if (i + 1 >= len) throw Err_.new_("Unmatched open-paren at pattern character " + Int_.XtoStr(i));
					boolean grps_itm = src[i + 1] == Byte_ascii.Paren_end;	// current is "()" 						
					grps.Add(grps_itm); ++grps_len;
					if (grps_itm)
						anypos = true;
//						bfr.Add(Bry_grp_bgn).Add_int_variable(grps_len).Add_byte(Bry_grp_end); // NOTE: Scribunto uses (?<m###>.*?) where ### is grps_len; JAVA 1.6 does not support named captures, so try to make without
					bfr.Add_byte(Byte_ascii.Paren_bgn);
					grps_open.Add(grps_len);
					grps_parens.Add(i + 1);
					break;
				}
				case Byte_ascii.Paren_end:
					if (grps_open.Count() == 0)
						throw Err_.new_("Unmatched close-paren at pattern character " + Int_.XtoStr(i));
					ListAdp_.DelAt_last(grps_open);
					bfr.Add_byte(Byte_ascii.Paren_end);
					break;
				case Byte_ascii.Percent:
					++i;
					if (i >= len) throw Err_.new_("malformed pattern (ends with '%')");
					Object percent_obj = percent_hash.Get_by_mid(src, i, i + 1);
					if (percent_obj != null) {
						bfr.Add((byte[])percent_obj);
						q_flag = true;
					}
					else {
						byte nxt = src[i];
						switch (nxt) {
							case Byte_ascii.Ltr_b:
								i += 2;
								if (i >= len) throw Err_.new_("malformed pattern (missing arguments to \'%b\')");
								byte digit_0 = src[i - 1];
								byte digit_1 = src[i];
								if (digit_0 == digit_1) {	// $bfr .= "{$d1}[^$d1]*$d1";
									bfr.Add(Bry_bf0_seg_0);
									Regx_quote(bfr, digit_0);
									bfr.Add(Bry_bf0_seg_1);
									Regx_quote(bfr, digit_0);
									bfr.Add(Bry_bf0_seg_2);
									Regx_quote(bfr, digit_0);
								}
								else {						// $bfr .= "(?<b$bct>$d1(?:(?>[^$d1$d2]+)|(?>b$bct))*$d2)";
									++bct;
									bfr.Add(Bry_bf1_seg_0);
									bfr.Add_int_variable(bct);
									bfr.Add(Bry_bf1_seg_1);
									Regx_quote(bfr, digit_0);
									bfr.Add(Bry_bf1_seg_2);
									Regx_quote(bfr, digit_0);
									Regx_quote(bfr, digit_1);
									bfr.Add(Bry_bf1_seg_3);
									bfr.Add_int_variable(bct);
									bfr.Add(Bry_bf1_seg_4);
									Regx_quote(bfr, digit_1);
									bfr.Add(Bry_bf1_seg_5);
								}
								break;
							case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
							case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
								grps_len = nxt - Byte_ascii.Num_0;
								if (grps_len == 0 || grps_len > grps.Count() || grps_open_Has(grps_open, grps_len))
									throw Err_.new_("invalid capture index %" + grps_len + " at pattern character " + Int_.XtoStr(i));
								bfr.Add(Bry_bf2_seg_0).Add_int_variable(grps_len).Add(Bry_bf2_seg_1);	// $bfr .= "\\g{m$grps_len}";
								break;
							default:
								Regx_quote(bfr, nxt);
								q_flag = true;
								break;
						}
					}
					break;
				case Byte_ascii.Brack_bgn:
					bfr.Add_byte(Byte_ascii.Brack_bgn);
					++i;
					if (i < len && src[i] == Byte_ascii.Pow) {	// ^
						bfr.Add_byte(Byte_ascii.Pow);
						++i;						
					}
					boolean stop = false;
					for (; i < len; i++) {
						byte tmp_b = src[i];
						switch (tmp_b) {
							case Byte_ascii.Brack_end:
								stop = true;
								break;
							case Byte_ascii.Percent:
								++i;
								if (i >= len)
									stop = true;
								else {
									Object brack_obj = brack_hash.Get_by_mid(src, i, i + 1);
									if (brack_obj != null)
										bfr.Add((byte[])brack_obj);
									else
										Regx_quote(bfr, src[i]);
								}
								break;
							default:
								boolean normal = true;
								if (i + 2 < len) {
									byte dash_1 = src[i + 1];
									byte dash_2 = src[i + 2];
									if (dash_1 == Byte_ascii.Dash && dash_2 != Byte_ascii.Brack_end) {
										Regx_quote(bfr, tmp_b);
										bfr.Add_byte(Byte_ascii.Dash);
										Regx_quote(bfr, dash_2);
										i += 2;
										normal = false;
									}
								}
								if (normal)
									Regx_quote(bfr, src[i]);
								break;
						}
						if (stop) break;
					}
					if (i >= len) throw Err_.new_("Missing close-bracket for character set beginning at pattern character $nxt_pos");
					bfr.Add_byte(Byte_ascii.Brack_end);
					q_flag = true;
					break;
				case Byte_ascii.Brack_end: throw Err_.new_("Unmatched close-bracket at pattern character " + Int_.XtoStr(i));
				case Byte_ascii.Dot:
					q_flag = true;
					bfr.Add_byte(Byte_ascii.Dot);
					break;
				default:
					q_flag = true;
					Regx_quote(bfr, cur);
					break;
			}
			if (q_flag && i + 1 < len) {
				byte tmp_b = src[i + 1];
				switch (tmp_b) {
					case Byte_ascii.Asterisk:
					case Byte_ascii.Plus:
					case Byte_ascii.Question:
						bfr.Add_byte(tmp_b);
						++i;
						break;
					case Byte_ascii.Dash:
						bfr.Add(Bry_regx_dash);
						++i;
						break;
				}
			}			
		}
		if (grps_open.Count() > 0) throw Err_.new_("Unclosed capture beginning at pattern character " + Int_.cast_(grps_open.FetchAt(0)));
//			bfr.Add(Bry_regx_end);	// NOTE: do not add PHP /us at end; u=PCRE_UTF8 which is not needed for Java; s=PCRE_DOTALL which will be specified elsewhere
		regx = bfr.XtoStrAndClear();
		return regx;
	}	private ByteAryBfr bfr = ByteAryBfr.new_();
	boolean grps_open_Has(ListAdp list, int v) {
		int len = list.Count();
		for (int i = 0; i < len; i++) {
			Object o = list.FetchAt(i);
			if (Int_.cast_(o) == v) return true;
		}
		return false;
	}
	private void Regx_quote(ByteAryBfr bfr, byte b) {
		if (Regx_char(b)) bfr.Add_byte(Byte_ascii.Backslash);
		bfr.Add_byte(b);
	}
	boolean Regx_char(byte b) {
		switch (b) {
			case Byte_ascii.Dot: case Byte_ascii.Slash: case Byte_ascii.Plus: case Byte_ascii.Asterisk: case Byte_ascii.Question:
			case Byte_ascii.Pow: case Byte_ascii.Dollar: case Byte_ascii.Eq: case Byte_ascii.Bang: case Byte_ascii.Pipe:
			case Byte_ascii.Colon: case Byte_ascii.Dash:
			case Byte_ascii.Brack_bgn: case Byte_ascii.Brack_end: case Byte_ascii.Paren_bgn: case Byte_ascii.Paren_end: case Byte_ascii.Curly_bgn: case Byte_ascii.Curly_end:
			case Byte_ascii.Lt: case Byte_ascii.Gt:
				return true;
			default:
				return false;
		}
	}
	private static final byte[] Bry_pow_literal = ByteAry_.new_ascii_("^"), Bry_pow_escaped = ByteAry_.new_ascii_("\\^")
	, Bry_dollar_literal = ByteAry_.new_ascii_("$"), Bry_dollar_escaped = ByteAry_.new_ascii_("\\$")
	, Bry_bf0_seg_0 = ByteAry_.new_ascii_("{"), Bry_bf0_seg_1 = ByteAry_.new_ascii_("}[^"), Bry_bf0_seg_2 = ByteAry_.new_ascii_("]*")
	, Bry_bf1_seg_0 = ByteAry_.new_ascii_("(?<b"), Bry_bf1_seg_1 = ByteAry_.new_ascii_(">")
	, Bry_bf1_seg_2 = ByteAry_.new_ascii_("(?:(?>[^"), Bry_bf1_seg_3 = ByteAry_.new_ascii_("]+)|(?>b")	// NOTE: PHP uses "]+)|(?P>b", but Java does not support P (named pattern); DATE:2013-12-20
	, Bry_bf1_seg_4 = ByteAry_.new_ascii_("))*"), Bry_bf1_seg_5 = ByteAry_.new_ascii_(")")
	, Bry_bf2_seg_0 = ByteAry_.new_ascii_("\\g{m"), Bry_bf2_seg_1 = ByteAry_.new_ascii_("}")
	, Bry_regx_dash = ByteAry_.new_ascii_("*?")	// was *?
	//, Bry_grp_bgn = ByteAry_.new_ascii_("(?<m")
	//, Bry_regx_end = ByteAry_.new_ascii_("/us")
	;
	private void Init() {
		Init_itm(Bool_.Y, "d", "\\p{Nd}");
		Init_itm(Bool_.Y, "l", "\\p{Ll}");
		Init_itm(Bool_.Y, "u", "\\p{Lu}");
		Init_itm(Bool_.Y, "a", "\\p{L}");
		Init_itm(Bool_.Y, "c", "\\p{Cc}");
		Init_itm(Bool_.Y, "p", "\\p{P}");
		Init_itm(Bool_.Y, "s", "\\s");
		Init_itm(Bool_.Y, "w", "[\\p{L}\\p{Nd}]");
		Init_itm(Bool_.Y, "x", "[0-9A-Fa-f0-9A-Fa-f]");
		Init_itm(Bool_.Y, "z", "\\00");
		Init_itm(Bool_.Y, "D", "\\P{Nd}");
		Init_itm(Bool_.Y, "L", "\\P{Ll}");
		Init_itm(Bool_.Y, "U", "\\P{Lu}");
		Init_itm(Bool_.Y, "A", "\\P{L}");
		Init_itm(Bool_.Y, "C", "\\P{Cc}");
		Init_itm(Bool_.Y, "P", "\\P{P}");
		Init_itm(Bool_.Y, "S", "\\S");						// JAVA: \P{Xps} not valid
		Init_itm(Bool_.Y, "W", "[\\P{L}\\P{Nd}]");
		Init_itm(Bool_.Y, "X", "[^0-9A-Fa-f0-9A-Fa-f]");
		Init_itm(Bool_.Y, "Z", "[^\\0]");
		Init_itm(Bool_.N, "w", "\\p{L}\\p{Nd}");
		Init_itm(Bool_.N, "x", "0-9A-Fa-f0-9A-Fa-f");
		Init_itm(Bool_.N, "W", "\\P{Xan}\\p{Nl}\\p{No}");	// Xan is L plus N, so ^Xan plus Nl plus No is anything that's not L or Nd
		Init_itm(Bool_.N, "X", "\\x00-\\x2f\\x3a-\\x40\\x47-\\x60\\x67-\\x{ff0f}\\x{ff1a}-\\x{ff20}\\x{ff27}-\\x{ff40}\\x{ff47}-\\x{10ffff}");
		Init_itm(Bool_.N, "Z", "\\x01-\\x{10ffff}");
	}
	private void Init_itm(boolean add_to_percent_hash, String lua, String php) {
		byte[] lua_bry = ByteAry_.new_ascii_(lua);
		byte[] php_bry = ByteAry_.new_ascii_(php);
		if (add_to_percent_hash) {
			percent_hash.Add_bry_obj(lua_bry, php_bry);
			brack_hash.Add_bry_obj(lua_bry, php_bry);	// always add to brack_hash; brack_hash = percent_hash + other characters
		}
		else {
			brack_hash.AddReplace(lua_bry, php_bry);	// replace percent_hash definitions
		}
	}
	private final Hash_adp_bry percent_hash = new Hash_adp_bry(true), brack_hash = new Hash_adp_bry(true);
}

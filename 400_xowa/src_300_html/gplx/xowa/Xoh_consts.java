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
public class Xoh_consts {
	public static final byte[] 
		  B_bgn = ByteAry_.new_ascii_("<b>"), B_end = ByteAry_.new_ascii_("</b>")
		, I_bgn = ByteAry_.new_ascii_("<i>"), I_end = ByteAry_.new_ascii_("</i>")
		, P_bgn = ByteAry_.new_ascii_("<p>"), P_end = ByteAry_.new_ascii_("</p>")
		, A_bgn = ByteAry_.new_ascii_("<a href=\""), A_bgn_lnki_0 = ByteAry_.new_ascii_("\" title=\""), A_mid_xowa_title = ByteAry_.new_ascii_("\" xowa_title=\"")
		, A_bgn_lnke_0		= ByteAry_.new_ascii_("\" class=\"external text\" rel=\"nofollow\">")
		, A_bgn_lnke_0_xowa = ByteAry_.new_ascii_("\">")
		, A_end = ByteAry_.new_ascii_("</a>")
		, Div_bgn_open = ByteAry_.new_ascii_("<div ")
		, Div_end = ByteAry_.new_ascii_("</div>")
		, __end = ByteAry_.new_ascii_(">")
		, __end_quote = ByteAry_.new_ascii_("\">")
		, __inline_quote = ByteAry_.new_ascii_("\"/>")
		, Img_bgn = ByteAry_.new_ascii_("<img src=\"")
		, Space_2 = ByteAry_.new_ascii_("  ")
		, Comm_bgn = ByteAry_.new_ascii_("<!--")
		, Comm_end = ByteAry_.new_ascii_("-->")
		, Pre_bgn = ByteAry_.new_ascii_("<pre>"), Pre_end = ByteAry_.new_ascii_("</pre>")
		, Pre_bgn_open = ByteAry_.new_ascii_("<pre")
		, Span_bgn_open			= ByteAry_.new_ascii_("<span")
		, Span_end = ByteAry_.new_ascii_("</span>")
		, Span_bgn			= ByteAry_.new_ascii_("<span>")
		, Br				= ByteAry_.new_ascii_("<br/>")
		, Id_atr = ByteAry_.new_ascii_(" id=\"")
		, Style_atr = ByteAry_.new_ascii_(" style=\"")
		, Pre_style_overflow_auto = ByteAry_.new_ascii_("overflow:auto;")
		, Pre_bgn_overflow = ByteAry_.new_ascii_("<pre style=\"overflow:auto\">")
		, Code_bgn_closed = ByteAry_.new_ascii_("<code>")
		, Code_bgn_open = ByteAry_.new_ascii_("<code")
		, Code_end = ByteAry_.new_ascii_("</code>")
		, Title_atr = ByteAry_.new_ascii_("\" title=\"")
		;
	public static final int Nbsp_int = 160;
}

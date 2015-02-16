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
package gplx.html; import gplx.*;
public class Html_tag_ {
	public static final byte[]
	  Ul_name_bry		= Bry_.new_ascii_("ul")
	, A_name_bry		= Bry_.new_ascii_("a")
	, Code_name_bry		= Bry_.new_ascii_("code")
	, Tr_name_bry		= Bry_.new_ascii_("tr")
	, Td_name_bry		= Bry_.new_ascii_("td")
	, Table_name_bry	= Bry_.new_ascii_("table")
	;
	public static final byte[]
	  Br_inl					= Bry_.new_ascii_("<br/>")
	, Hr_inl					= Bry_.new_ascii_("<hr/>")
	, Body_lhs					= Bry_.new_ascii_("<body>")			, Body_rhs					= Bry_.new_ascii_("</body>")
	, B_lhs						= Bry_.new_ascii_("<b>")			, B_rhs						= Bry_.new_ascii_("</b>")
	, I_lhs						= Bry_.new_ascii_("<i>")			, I_rhs						= Bry_.new_ascii_("</i>")
	, P_lhs						= Bry_.new_ascii_("<p>")			, P_rhs						= Bry_.new_ascii_("</p>")
	, Div_lhs					= Bry_.new_ascii_("<div>")			, Div_rhs					= Bry_.new_ascii_("</div>")
	, Html_rhs					= Bry_.new_ascii_("</html>")
	, Head_lhs_bgn				= Bry_.new_ascii_("<head")
	, Head_rhs					= Bry_.new_ascii_("</head>")
	, Style_lhs_w_type			= Bry_.new_ascii_("<style type=\"text/css\">")
	, Style_rhs					= Bry_.new_ascii_("</style>")
	, Script_lhs				= Bry_.new_ascii_("<script>")
	, Script_lhs_w_type			= Bry_.new_ascii_("<script type='text/javascript'>")
	, Script_rhs				= Bry_.new_ascii_("</script>")
	, Span_rhs					= Bry_.new_ascii_("</span>")
	;

	public static final String 
	  Comm_bgn_str				= "<!--"
	, Comm_end_str				= "-->"
	, Anchor_str				= "#"
	;
	public static final byte[]
	  Comm_bgn = Bry_.new_ascii_(Comm_bgn_str), Comm_end = Bry_.new_ascii_(Comm_end_str)
	;
	public static final int
	  Comm_bgn_len = Comm_bgn.length
	, Comm_end_len = Comm_end.length
	;
}

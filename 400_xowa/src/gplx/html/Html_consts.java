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
public class Html_consts {
	public static final String Nl_str = "&#10;";
	public static final String 
	  Comm_bgn_str = "<!--"
	, Comm_end_str = "-->"
	;
	public static final byte[]
	  Lt = ByteAry_.new_ascii_("&lt;"), Gt = ByteAry_.new_ascii_("&gt;")
	, Amp = ByteAry_.new_ascii_("&amp;"), Quote = ByteAry_.new_ascii_("&quote;")
	, Eq = ByteAry_.new_ascii_("&#61;")
	, Nl_bry = ByteAry_.new_ascii_(Nl_str), Space_bry = ByteAry_.new_ascii_("&#32;")
	, Comm_bgn = ByteAry_.new_ascii_(Comm_bgn_str), Comm_end = ByteAry_.new_ascii_(Comm_end_str)
	, Hr_bry			= ByteAry_.new_ascii_("<hr/>")
	, Td_bgn_bry		= ByteAry_.new_ascii_("<td>")
	, Td_end_bry		= ByteAry_.new_ascii_("</td>")
	, Ul_tag_bry		= ByteAry_.new_ascii_("ul")
	, Script_bgn_bry	= ByteAry_.new_ascii_("<script>")
	, Script_end_bry	= ByteAry_.new_ascii_("</script>")
	;
	public static final int
	  Comm_bgn_len = Comm_bgn.length
	, Comm_end_len = Comm_end.length
	;
}

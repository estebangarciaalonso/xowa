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
package gplx.xowa.parsers.hdrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
public class Xop_hdr_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "hdr");
	public static final Gfo_msg_itm
		  Dangling_hdr						= Gfo_msg_itm_.new_warn_(owner, "dangling_hdr")
		, Mismatched						= Gfo_msg_itm_.new_warn_(owner, "mismatched")
		, Len_1								= Gfo_msg_itm_.new_warn_(owner, "len_1")
		, Len_7_or_more						= Gfo_msg_itm_.new_warn_(owner, "len_7_or_more")
		;
}

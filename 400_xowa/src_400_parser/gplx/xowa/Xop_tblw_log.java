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
public class Xop_tblw_log {		
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

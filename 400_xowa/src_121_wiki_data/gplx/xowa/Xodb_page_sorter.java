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
import gplx.lists.*; /*ComparerAble*/
public class Xodb_page_sorter implements ComparerAble {
	public int compare(Object lhsObj, Object rhsObj) {
		Xodb_page lhs = (Xodb_page)lhsObj, rhs = (Xodb_page)rhsObj;
		return order * Compare_rows(compareType, lhs, rhs);
	}
	private static int Compare_rows(byte compareType, Xodb_page lhs, Xodb_page rhs) {
		switch (compareType) {
			case Tid_ns_ttl:	{
				int rv = Int_.Compare(lhs.Ns_id(), rhs.Ns_id());
				return rv == CompareAble_.Same ? Bry_.Compare(lhs.Ttl_wo_ns(), rhs.Ttl_wo_ns()) : rv;
			}
			case Tid_itm_len:	return Int_.Compare(lhs.Text_len(), rhs.Text_len());
			case Tid_id:		return Int_.Compare(lhs.Id(), rhs.Id());
			case Tid_ttl:		return Bry_.Compare(lhs.Ttl_wo_ns(), rhs.Ttl_wo_ns());
			case Tid_ctg_tid_sortkey:
				gplx.xowa.ctgs.Xoctg_page_xtn lhs_xtn = (gplx.xowa.ctgs.Xoctg_page_xtn)lhs.Xtn();
				gplx.xowa.ctgs.Xoctg_page_xtn rhs_xtn = (gplx.xowa.ctgs.Xoctg_page_xtn)rhs.Xtn();
				if (lhs_xtn == null || rhs_xtn == null) return CompareAble_.Same;
				int tid_comparable = Byte_.Compare(lhs_xtn.Tid(), rhs_xtn.Tid());
				if (tid_comparable != CompareAble_.Same) return tid_comparable;
				return Bry_.Compare(lhs_xtn.Sortkey(), rhs_xtn.Sortkey());
			default:			throw Err_.unhandled(compareType);
		}
	}
	Xodb_page_sorter(byte compareType, int order) {this.compareType = compareType; this.order = order;}
	byte compareType; int order;
	static final byte Tid_ns_ttl = 0, Tid_itm_len = 2, Tid_id = 3, Tid_ttl = 4, Tid_ctg_tid_sortkey = 5;
	static final int Asc = 1, Dsc = -1;
	public static final Xodb_page_sorter TitleAsc				= new Xodb_page_sorter(Tid_ttl				, Asc);
	public static final Xodb_page_sorter EnyLenDsc				= new Xodb_page_sorter(Tid_itm_len			, Dsc);
	public static final Xodb_page_sorter IdAsc					= new Xodb_page_sorter(Tid_id				, Asc);
	public static final Xodb_page_sorter Ns_id_TtlAsc			= new Xodb_page_sorter(Tid_ns_ttl			, Asc);
	public static final Xodb_page_sorter Ctg_tid_sortkey_asc	= new Xodb_page_sorter(Tid_ctg_tid_sortkey	, Asc);
}

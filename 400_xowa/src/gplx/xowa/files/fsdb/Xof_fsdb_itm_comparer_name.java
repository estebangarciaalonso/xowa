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
package gplx.xowa.files.fsdb; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
public class Xof_fsdb_itm_comparer_name implements gplx.lists.ComparerAble {
	public int compare(Object lhsObj, Object rhsObj) {
		Xof_xfer_itm lhs = (Xof_xfer_itm)lhsObj;
		Xof_xfer_itm rhs = (Xof_xfer_itm)rhsObj;
		return ByteAry_.Compare(lhs.Ttl(), rhs.Ttl());
	}
	public static final Xof_fsdb_itm_comparer_name _ = new Xof_fsdb_itm_comparer_name();
}

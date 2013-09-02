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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.cache.*; import gplx.fsdb.*; import gplx.ios.*;
class Xob_fsdb_make {
	public void Make() {
	}
	// select all from xfer_make
	// download
//		public void Select_all(Db_provider p) {
//			ListAdp list = ListAdp_.new_();
//			DataRdr rdr = DataRdr_.Null;
//			byte[] prv_lnki_ttl = ByteAry_.Empty;
//			while (true) {
//				list.Clear();
//				try {
//					rdr = Select(p, Byte_.Zero, prv_lnki_ttl, 990);	// select imgs to download; (1) need to be able to do resume; (2) use ttl order for optimized retrival from fsdb
//					Xof_xfer_itm img = new Xof_xfer_itm();
//					byte[] lnki_ttl = rdr.ReadBryByStr(Fld_oxr_ttl);
//					int lnki_w = rdr.ReadInt(Fld_oxr_width);
//					int lnki_h = rdr.ReadInt(Fld_oxr_height);
//					double lnki_time = rdr.ReadDouble(Fld_oxr_time);
//					img.Lnki_atrs_(null, lnki_ttl, false, lnki_w, lnki_h
//					, Xop_lnki_tkn.Upright_null, lnki_time);
//					list.Add(img);
//					prv_lnki_ttl = lnki_ttl;
//				} finally {rdr.Rls();}
//				int list_count = list.Count();
//				if (list_count == 0) break;	// no more found.
//				// commit txn
//				for (int i = 0; i < list_count; i++) {
//					Xof_xfer_itm itm = (Xof_xfer_itm)list.FetchAt(i);
//					DoDownload(itm);
//				}
//				// process
//			}
//		}	
//		private void DoDownload(Xof_xfer_itm itm) {
//			/*
//			. has to be 1 by 1
//			.. download from fsdb
//			.. download from fsys
//			... download from internet		
//			*/
//		}
}

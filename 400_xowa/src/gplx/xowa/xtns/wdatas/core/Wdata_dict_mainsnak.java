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
package gplx.xowa.xtns.wdatas.core; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wdatas.*;
public class Wdata_dict_mainsnak {
	public static final byte
	  Tid_snaktype								= 0
	, Tid_property								= 1
	, Tid_hash									= 2
	, Tid_datavalue								= 3
	, Tid_type									= 4
	;
	public static byte[] 
	  Bry_snaktype								= Bry_.new_ascii_("snaktype")
	, Bry_property								= Bry_.new_ascii_("property")
	, Bry_hash									= Bry_.new_ascii_("hash")
	, Bry_datavalue								= Bry_.new_ascii_("datavalue")
	, Bry_type									= Bry_.new_ascii_("type")
	;
	public static final Hash_adp_bry Dict = Hash_adp_bry.cs_()
	.Add_bry_byte(Bry_snaktype					, Tid_snaktype)
	.Add_bry_byte(Bry_property					, Tid_property)
	.Add_bry_byte(Bry_hash						, Tid_hash)
	.Add_bry_byte(Bry_datavalue					, Tid_datavalue)
	.Add_bry_byte(Bry_type						, Tid_type)
	;
}

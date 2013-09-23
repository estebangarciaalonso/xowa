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
public class Xop_lnki_type {
	public static final byte Id_null = 0, Id_none = 1, Id_frameless = 2, Id_frame = 4, Id_thumb = 8;
	public static boolean Id_is_thumb_like(byte id) {
		switch (id) {
			case Id_null: case Id_none: case Id_frame:
				return false;
			case Id_frameless:	// NOTE: not sure if this is needed; only 2 users;
			case Id_thumb:
				return true;
			default:
				return Enm_.HasInt(id, Id_thumb);
		}
	}
}

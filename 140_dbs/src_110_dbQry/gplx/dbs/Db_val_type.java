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
package gplx.dbs; import gplx.*;
public class Db_val_type {
	public static final byte //Nonserialized
		  Null		= 0
		, Bool		= 1
		, Byte		= 2
		, Int32		= 3
		, Int64		= 4
		, Date		= 5
		, Decimal	= 6
		, Float		= 7
		, Double	= 8
		, Bry		= 9
		, Varchar	= 10
		, Nvarchar	= 11
		;
}

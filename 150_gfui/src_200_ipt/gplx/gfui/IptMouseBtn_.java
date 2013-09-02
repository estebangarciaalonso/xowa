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
package gplx.gfui; import gplx.*;
public class IptMouseBtn_ {
	public static final IptMouseBtn	// REF: System.Windows.Forms.MouseButtons
		  None		= new IptMouseBtn(0x00000000, "mouse.none")
		, Left		= new IptMouseBtn(0x00100000, "mouse.left")
		, Right		= new IptMouseBtn(0x00200000, "mouse.right")
		, Middle	= new IptMouseBtn(0x00400000, "mouse.middle")
		, X1		= new IptMouseBtn(0x00800000, "mouse.x1")
		, X2		= new IptMouseBtn(0x01000000, "mouse.x2")
		;
	public static IptMouseBtn parse_(String raw) {
		if		(String_.Eq(raw, None.Key())) return None;
		else if	(String_.Eq(raw, Left.Key())) return Left;
		else if	(String_.Eq(raw, Right.Key())) return Right;
		else if	(String_.Eq(raw, Middle.Key())) return Middle;
		else if	(String_.Eq(raw, X1.Key())) return X1;
		else if	(String_.Eq(raw, X2.Key())) return X2;
		else throw Err_.parse_type_(IptMouseBtn.class, raw);
	}
	@gplx.Internal protected static IptMouseBtn api_(int val) {
		if		(val == None.Val()) return None;
		else if	(val == Left.Val()) return Left;
		else if	(val == Right.Val()) return Right;
		else if	(val == Middle.Val()) return Middle;
		else if	(val == X1.Val()) return X1;
		else if	(val == X2.Val()) return X2;
		else throw Err_.unhandled(val);
	}
}

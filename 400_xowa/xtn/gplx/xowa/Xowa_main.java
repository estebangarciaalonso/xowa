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
package gplx.xowa;

import gplx.ByteAry_;
import gplx.Byte_;
import gplx.String_;

public class Xowa_main {
	public static void main(String[] args) {
		gplx.xowa.Xoa_app_.Run(args);
//		String s = gplx.String_.Concat_lines_nl_skipLast
////		( "function A()"
////		, "    return '𐑓'"
////		, "end" 
////		, "print(A())"
////		);
//		("print('𐑓')");
//		org.luaj.vm2.Globals globals = org.luaj.vm2.lib.jse.JsePlatform.standardGlobals();
//		org.luaj.vm2.LuaValue chunk = globals.load(s);
//		chunk.call();
//		String d = "𐑓";
//		byte[] b = ByteAry_.new_utf8_(d);
//		System.out.println(d);
	}
}

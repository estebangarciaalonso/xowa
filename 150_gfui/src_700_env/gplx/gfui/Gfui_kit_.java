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
package gplx.gfui; import gplx.*;
public class Gfui_kit_ {
	public static final byte TypeId_default = 1, TypeId_swt = 2, TypeId_drd = 3;
	static Gfui_kit swt, basic;
	public static final String Cfg_HtmlBox = "HtmlBox";
	public static final String Key_basic = "default";
	public static Gfui_kit key_(String key) {
		if		(String_.Eq(key, "swt"))		{if (swt == null) {swt = Swt_kit._;} return swt;}	
		else if	(String_.Eq(key, Key_basic))	{if (basic == null) {basic = Gfui_kit_base._;} return basic;}
		else throw Err_.unhandled(key);
	}
}

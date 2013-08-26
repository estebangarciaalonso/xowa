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
package gplx.texts; import gplx.*;
import java.lang.*;
public class Encoding_ {
	public static byte[] XtoByteAry(String text) {try {return text.getBytes("UTF-8");} catch (Exception e) {throw Err_.err_(e, "unsupported encoding");}}
	public static byte[] XtoByteAryAscii(String text) {try {return text == null ? null : text.getBytes("ASCII");} catch (Exception e) {throw Err_.err_(e, "unsupported encoding");}}
	public static String XtoStr(byte[] ary)							{return ary == null ? null : XtoStr(ary, 0, ary.length);}
	public static String XtoStr(byte[] ary, int bgn, int len)		{if (ary == null) return null; try {return new String(ary, bgn, len, "UTF-8");} catch (Exception e) {throw Err_.err_(e, "unsupported encoding");}}	
	public static String XtoStrAscii(byte[] ary, int bgn, int len)	{if (ary == null) return null; try {return new String(ary, bgn, len, "ASCII");} catch (Exception e) {throw Err_.err_(e, "unsupported encoding");}}	
}

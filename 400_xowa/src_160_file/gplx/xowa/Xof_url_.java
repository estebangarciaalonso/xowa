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
public class Xof_url_ {
	public static final int Null_size_deprecated = -1, Null_size = 0;
	public static int Width_adjust_by_upright(int width, double upright) {		
		if 		(upright == -1) return width;
		else if (upright ==  1) upright = .75f;
		int rv = (int)(width * upright);  
		return Round_10p2(rv);
	}
	private static int Round_10p2(int v) {
		int mod = v % 10;
		if (mod > 4) 	v += 10 - mod;
		else			v -= mod;
		return v;
	}
}

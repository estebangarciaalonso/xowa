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
import org.junit.*;
public class ScreenAdp_tst {
	@Test  public void parse_() {
		ScreenAdp actl = ScreenAdp_.parse_("{screen{0}");
		Tfds.Eq(0, actl.Index());
	}
	@Test  public void opposite_() {
		ScreenAdp actl = ScreenAdp_.from_point_(PointAdp_.new_(2000, 2000));
		Tfds.Eq(0, actl.Index());
	}
}

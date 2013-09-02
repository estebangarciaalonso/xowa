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
public class Xoh_opts {
	Xoh_opts(boolean lnki_alt) {this.lnki_alt = lnki_alt;}	Xoh_opts() {}
	public boolean Lnki_alt() {return lnki_alt;} private boolean lnki_alt;
	public static Xoh_opts root_()		{return new Xoh_opts(false);}
	public static Xoh_opts lnki_alt_()	{return new Xoh_opts(true);}
}

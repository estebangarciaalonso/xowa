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
package gplx.xowa.html; import gplx.*; import gplx.xowa.*;
public class Xoh_html_wtr_ctx {
	Xoh_html_wtr_ctx(boolean mode_is_alt) {this.mode_is_alt = mode_is_alt;}
	public boolean Mode_is_alt() {return mode_is_alt;} private boolean mode_is_alt;
        public static final Xoh_html_wtr_ctx Basic	= new Xoh_html_wtr_ctx(false);
        public static final Xoh_html_wtr_ctx Alt		= new Xoh_html_wtr_ctx(true);
}

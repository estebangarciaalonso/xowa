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
package gplx.xowa2.apps.urls; import gplx.*; import gplx.xowa2.*; import gplx.xowa2.apps.*;
public class Xoav_url {
	public byte[] Wiki_bry() {return wiki_bry;} public void Wiki_bry_(byte[] v) {wiki_bry = v;} private byte[] wiki_bry;
	public byte[] Page_bry() {return page_bry;} public void Page_bry_(byte[] v) {page_bry = v;} private byte[] page_bry;
	public byte[] Anch_bry() {return anch_bry;} public void Anch_bry_(byte[] v) {anch_bry = v;} private byte[] anch_bry;
	public byte[] Qarg_bry() {return qarg_bry;} public void Qarg_bry_(byte[] v) {qarg_bry = v;} private byte[] qarg_bry;
	public void Clear() {wiki_bry = page_bry = null;}
}

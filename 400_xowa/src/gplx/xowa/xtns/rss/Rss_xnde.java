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
package gplx.xowa.xtns.rss; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.parsers.lnkis.*; import gplx.xowa.parsers.logs.*;
public class Rss_xnde implements Xop_xnde_xtn {	// TODO: not implemented
	public boolean Xtn_literal() {return false;}
	public Xop_root_tkn Xtn_root() {return lnki_root;} private Xop_root_tkn lnki_root = null;
	public void Xtn_compile(Xow_wiki wiki, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
	}
}
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
public class Xofw_repo_pair {
	public Xof_repo_itm Src() {return src;} private Xof_repo_itm src;
	public Xof_repo_itm Trg() {return trg;} private Xof_repo_itm trg;
	public Xof_meta_mgr Trg_meta_mgr() {return trg_meta_mgr;} private Xof_meta_mgr trg_meta_mgr;
	public byte[] Wiki_key() {return wiki_key;} private byte[] wiki_key;
	public Xofw_repo_pair(Xof_repo_itm src, Xof_repo_itm trg, Xof_meta_mgr trg_meta_mgr, byte[] wiki_key) {
		this.src = src; this.trg = trg; this.trg_meta_mgr = trg_meta_mgr; this.wiki_key = wiki_key;
	}
}

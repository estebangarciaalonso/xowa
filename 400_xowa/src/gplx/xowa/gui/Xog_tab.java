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
package gplx.xowa.gui; import gplx.*; import gplx.xowa.*;
import gplx.xowa.parsers.lnkis.*;
import gplx.xowa.files.*;
public class Xog_tab {
	public byte[] Display_ttl() {return display_ttl;} public Xog_tab Display_ttl_(byte[] v) {display_ttl = v; return this;} private byte[] display_ttl = null;
	public Xop_lnki_logger_redlinks_mgr Lnki_redlinks_mgr() {return lnki_redlinks_mgr;} private Xop_lnki_logger_redlinks_mgr lnki_redlinks_mgr = new Xop_lnki_logger_redlinks_mgr();
	public Xof_lnki_file_mgr Lnki_file_mgr() {return lnki_file_mgr;} private Xof_lnki_file_mgr lnki_file_mgr = new Xof_lnki_file_mgr();

	public void Clear() {	// NOTE: must be cleared else pages which have an italicized display titles will persit;
		display_ttl = null;
	}
	public void Clear_mgrs() {
		display_ttl = null;
		lnki_file_mgr.Clear();
	}
	public void Init(Xoa_ttl ttl) {
		this.Clear_mgrs();
	}
}

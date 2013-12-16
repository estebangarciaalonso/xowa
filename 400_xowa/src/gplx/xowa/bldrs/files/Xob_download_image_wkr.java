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
package gplx.xowa.bldrs.files; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*;
public class Xob_download_image_wkr extends Xob_itm_basic_base implements Xob_cmd {
//		private Io_url prv_url;
	public Xob_download_image_wkr(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_ctor(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "file.download_image";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {}
	public void Cmd_run() {
//			String[] server_urls = wiki.App().Setup_mgr().Dump_mgr().Server_urls();
//			if (server_urls.length == 0) throw Err_.new_("no server urls defined");
//			String server_url = server_urls[0];
//			byte[] wiki_abrv = wiki.Wiki_tid_code();
//			return server_url + 
		// http://dumps.wikimedia.org/enwiki/latest/enwiki-latest-image.sql.gz
		// build url
		// download file
		// extract
	}
	
	public void Cmd_end() {}
	public void Cmd_print() {}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		return GfoInvkAble_.Rv_unhandled;
	}
}

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
package gplx.xowa.files.bins; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.ios.*; import gplx.xowa.files.fsdb.*;
public class Xof_bin_wkr_wmf_xfer implements Xof_bin_wkr {
	private gplx.ios.IoEngine_xrg_downloadFil download = IoEngine_xrg_downloadFil.new_("", Io_url_.Null); 
	private Xof_url_bldr url_bldr = new Xof_url_bldr();
	public Xof_bin_wkr_wmf_xfer(Xow_repo_mgr repo_mgr) {this.repo_mgr = repo_mgr;} private Xow_repo_mgr repo_mgr;
	public byte Bin_wkr_tid() {return Xof_bin_wkr_.Tid_inet;}
	public boolean Bin_wkr_get(Xof_fsdb_itm itm, Io_url bin_url, boolean is_thumb, int w) {
		byte mode = is_thumb ? Xof_repo_itm.Mode_thumb : Xof_repo_itm.Mode_orig;
		Xof_repo_pair repo_itm = repo_mgr.Repos_get_by_wiki(itm.Orig_wiki());
		String queue_msg = String_.Format("downloading ~{0} of ~{1}: ~{2};", 0, 0, String_.new_utf8_(itm.Lnki_ttl()));
		download.Prog_fmt_hdr_(queue_msg);
		String src = url_bldr.Set_src_file_(mode, repo_itm.Src(), itm.Lnki_ttl(), itm.Lnki_md5(), itm.Lnki_ext(), w, itm.Lnki_thumbtime()).Xto_str();
		download.Init(src, bin_url); return download.Exec();
	}
}

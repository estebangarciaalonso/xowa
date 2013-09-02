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
class Xof_bin_wkr_wmf_xfer implements Xof_bin_wkr {
	private gplx.ios.IoEngine_xrg_downloadFil download = IoEngine_xrg_downloadFil.new_("", Io_url_.Null); 
	private Xof_url_bldr url_bldr = new Xof_url_bldr();
	public Xof_bin_wkr_wmf_xfer(Xof_repo_itm repo) {this.repo = repo;} private Xof_repo_itm repo;
	public byte Tid() {return Xof_bin_wkr_.Tid_inet;}
	public boolean Get_bin(Xof_fsdb_itm itm, Io_url trg_url, boolean is_thumb) {
		byte mode = is_thumb ? Xof_repo_itm.Mode_thumb : Xof_repo_itm.Mode_orig;
		String src_http = url_bldr.Set_src_file_(mode, repo, itm.Ttl(), itm.Md5(), itm.Ext(), itm.Actl_img_w(), itm.Lnki_thumbtime()).Xto_str();
		byte[] bin = download.Exec_as_bry(src_http);
		itm.Db_data_(bin);
		return download.Rslt() == IoEngine_xrg_downloadFil.Rslt_pass;
	}
}

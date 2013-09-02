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
import gplx.xowa.files.fsdb.*;
abstract class Xof_bin_wkr_fsys_base implements Xof_bin_wkr {
	public Xof_repo_itm Repo() {return repo;} private Xof_repo_itm repo;
	public Xof_url_bldr Url_bldr() {return url_bldr;} private Xof_url_bldr url_bldr;
	public void Ctor(Xof_repo_itm repo, Xof_url_bldr url_bldr) {this.repo = repo; this.url_bldr = url_bldr;}
	public abstract byte Tid();
	public boolean Get_bin(Xof_fsdb_itm itm, Io_url trg_url, boolean is_thumb) {
		byte mode = is_thumb ? Xof_repo_itm.Mode_thumb : Xof_repo_itm.Mode_orig;
		Io_url src_url = this.Get_bin__src_url(mode, String_.new_utf8_(itm.Actl_wiki()), itm.Ttl(), itm.Md5(), itm.Ext(), is_thumb, itm.Actl_img_w(), itm.Lnki_thumbtime());
		if (src_url == Io_url_.Null) return false;
		byte[] bin = Io_mgr._.LoadFilBry(src_url);
		return bin != Io_mgr.LoadFilBry_fail;
	}
	public abstract Io_url Get_bin__src_url(byte mode, String wiki, byte[] ttl_wo_ns, byte[] md5, Xof_ext ext, boolean is_thumb, int w, int thumbtime);
}
class Xof_bin_wkr_fsys_wmf extends Xof_bin_wkr_fsys_base {
	public Xof_bin_wkr_fsys_wmf(Xof_repo_itm repo, Xof_url_bldr url_bldr) {this.Ctor(repo, url_bldr);}
	@Override public byte Tid() {return Xof_bin_wkr_.Tid_fsys_wmf;}
	@Override public Io_url Get_bin__src_url(byte mode, String wiki, byte[] ttl_wo_ns, byte[] md5, Xof_ext ext, boolean is_thumb, int w, int thumbtime) {
		return this.Url_bldr().Set_src_file_(mode, this.Repo(), ttl_wo_ns, md5, ext, w, thumbtime).Xto_url();
	}
}
class Xof_bin_wkr_fsys_xowa extends Xof_bin_wkr_fsys_base {
	public Xof_bin_wkr_fsys_xowa(Xof_repo_itm repo, Xof_url_bldr url_bldr) {this.Ctor(repo, url_bldr);}
	@Override public byte Tid() {return Xof_bin_wkr_.Tid_fsys_xowa;}
	@Override public Io_url Get_bin__src_url(byte mode, String wiki, byte[] ttl_wo_ns, byte[] md5, Xof_ext ext, boolean is_thumb, int w, int thumbtime) {
		return this.Url_bldr().Set_trg_file_(mode, this.Repo(), ttl_wo_ns, md5, ext, w, thumbtime).Xto_url();
	}
}
class Xof_bin_wkr_fsys_dir extends Xof_bin_wkr_fsys_base {
	private Io_url dir;
	public Xof_bin_wkr_fsys_dir(Xof_repo_itm repo, Xof_url_bldr url_bldr, Io_url dir) {this.Ctor(repo, url_bldr); this.dir = dir;}
	@Override public byte Tid() {return Xof_bin_wkr_.Tid_fsys_dir;}
	@Override public Io_url Get_bin__src_url(byte mode, String wiki, byte[] ttl_wo_ns, byte[] md5, Xof_ext ext, boolean is_thumb, int w, int thumbtime) {
		return mode == Xof_repo_itm.Mode_thumb ? Io_url_.Null : dir.GenSubFil(String_.new_utf8_(ttl_wo_ns));
	}
}

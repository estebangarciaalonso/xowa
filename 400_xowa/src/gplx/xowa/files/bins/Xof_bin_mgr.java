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
import gplx.xowa.files.fsdb.*; import gplx.xowa.files.cnvs.*; import gplx.ios.*;
public class Xof_bin_mgr {
	private Xof_bin_wkr[] wkrs = null; private int wkrs_len;
	private Xof_url_bldr url_bldr = new Xof_url_bldr();
	private StringRef resize_warning = StringRef.null_(); private Xof_img_size tmp_size = new Xof_img_size();
	public Xof_bin_mgr(Xow_repo_mgr repo_mgr) {this.repo_mgr = repo_mgr;}
	public Xow_repo_mgr Repo_mgr() {return repo_mgr;} private Xow_repo_mgr repo_mgr;
	public void Resizer_(Xof_img_wkr_resize_img v) {resizer = v;} private Xof_img_wkr_resize_img resizer;
	public void Wkrs_(Xof_bin_wkr... wkrs) {this.wkrs = wkrs; wkrs_len = wkrs.length;}
	public boolean Find_to_url_as_bool(byte exec_tid, Xof_fsdb_itm itm) {
		return Find_to_url(exec_tid, itm) != Io_url_.Null;
	}
	public Io_url Find_to_url(byte exec_tid, Xof_fsdb_itm itm) {
		Io_stream_rdr rdr = Find_as_rdr(exec_tid, itm);
		if (rdr == Io_stream_rdr_.Null) return Io_url_.Null;
		Io_url trg = itm.Html_url();
		if (itm.Rslt_bin_fsys()) return trg;	// rdr is opened directly from trg; return its url; occurs when url goes through imageMagick / inkscape, or when thumb is already in disk;
		Io_stream_wtr_.Save_rdr(trg, rdr);		// rdr is stream; either from http_wmf or fsdb; save to trg and return;
		return trg;
	}
	public Io_stream_rdr Find_as_rdr(byte exec_tid, Xof_fsdb_itm itm) {
		Io_stream_rdr rv = Io_stream_rdr_.Null;
		boolean file_is_orig = itm.File_is_orig();
		if (file_is_orig) {
			Io_url trg = itm.Lnki_ext().Orig_is_not_image()
				? Get_url(itm, Xof_repo_itm.Mode_thumb, Bool_.N)
				: Get_url(itm, Xof_repo_itm.Mode_orig, Bool_.N);
			itm.Html_url_(trg);
			for (int i = 0; i < wkrs_len; i++) {
				Xof_bin_wkr wkr = wkrs[i];
				rv = wkr.Bin_wkr_get_as_rdr(itm, Bool_.N, itm.Html_w());
				if (rv == Io_stream_rdr_.Null) continue;						// orig not found; continue;
				if (itm.Lnki_ext().Id_is_svg()) {
					itm.Lnki_w_(itm.Html_w());
					Io_url trg_png = Get_url(itm, Xof_repo_itm.Mode_thumb, Bool_.N);
					boolean resized = Resize(exec_tid, itm, file_is_orig, trg, trg_png);
					if (!resized) continue;
					itm.Rslt_bin_(wkr.Bin_wkr_tid());
					itm.Rslt_bin_fsys_(true);
					rv = Io_stream_rdr_.file_(trg);							// return stream of resized url; (result of imageMagick / inkscape)
					rv.Open();
					return rv;
				}
				else {
					itm.Rslt_bin_(wkr.Bin_wkr_tid());
					return rv;
				}
			}
		}
		else {	// thumb
			Io_url trg = Get_url(itm, Xof_repo_itm.Mode_thumb, Bool_.N);
			itm.Html_url_(trg);
			for (int i = 0; i < wkrs_len; i++) {
				Xof_bin_wkr wkr = wkrs[i];
				rv = wkr.Bin_wkr_get_as_rdr(itm, Bool_.Y, itm.Html_w());		// get thumb's bin
				if (rv != Io_stream_rdr_.Null) {								// thumb's bin exists;
					itm.Rslt_bin_(wkr.Bin_wkr_tid());
					return rv;
				}
				rv = wkr.Bin_wkr_get_as_rdr(itm, Bool_.N, itm.Orig_w());		// thumb missing; get orig;
				if (rv == Io_stream_rdr_.Null) continue;						// nothing found; continue;
				Io_url orig = Get_url(itm, Xof_repo_itm.Mode_orig, Bool_.N);	// get orig url
				Io_stream_wtr_.Save_rdr(orig, rv);
				boolean resized = Resize(exec_tid, itm, file_is_orig, orig, trg);
				if (!resized) continue;
				itm.Rslt_bin_(wkr.Bin_wkr_tid());
				itm.Rslt_bin_fsys_(true);
				rv = Io_stream_rdr_.file_(trg);								// return stream of resized url; (result of imageMagick / inkscape)
				rv.Open();
				return rv;
			}
		}
		return Io_stream_rdr_.Null;
	}
	private boolean Resize(byte exec_tid, Xof_fsdb_itm itm, boolean file_is_orig, Io_url src, Io_url trg) {
		tmp_size.Html_size_calc(exec_tid, itm.Lnki_w(), itm.Lnki_h(), itm.Lnki_type(), itm.Lnki_upright(), itm.Lnki_ext().Id(), itm.Orig_w(), itm.Orig_h(), Xof_img_size.Thumb_width_img);
		boolean rv = resizer.Exec(src, trg, tmp_size.Html_w(), tmp_size.Html_h(), itm.Lnki_ext().Id(), resize_warning);
		itm.Rslt_cnv_(rv ? Xof_cnv_wkr_.Tid_y : Xof_cnv_wkr_.Tid_n);
		return rv;
	}
	private Io_url Get_url(Xof_fsdb_itm itm, byte mode, boolean src) {
		Xof_repo_pair repo = repo_mgr.Repos_get_by_wiki(itm.Orig_wiki());
		return src 
			? url_bldr.Set_src_file_(mode, repo.Src(), itm.Lnki_ttl(), itm.Lnki_md5(), itm.Lnki_ext(), itm.Html_w(), itm.Lnki_thumbtime()).Xto_url()
			: url_bldr.Set_trg_file_(mode, repo.Trg(), itm.Lnki_ttl(), itm.Lnki_md5(), itm.Lnki_ext(), itm.Html_w(), itm.Lnki_thumbtime()).Xto_url()
			;
	}
}

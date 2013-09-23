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
package gplx.xowa.ctgs; import gplx.*; import gplx.xowa.*;
public class Xoctg_url {
	public byte[]		All_idx() {return all_idx;} private byte[] all_idx;
	public byte[][]		Grp_idxs() {return grp_idxs;} private byte[][] grp_idxs = new byte[3][];
	public byte[]		Grp_fwds() {return grp_fwds;} private byte[] grp_fwds = new byte[3];
	private void Clear() {
		all_idx = null;
		for (int i = 0; i < 3; i++) {
			grp_fwds[i] = Bool_.__byte;
			grp_idxs[i] = null;
		}
	}
	public Xoctg_url Parse(Gfo_usr_dlg usr_dlg, Xoa_url url) {
		this.Clear();
		Gfo_url_arg[] args = url.Args();
		int len = args.length;
		for (int i = 0; i < len; i++) {
			Gfo_url_arg arg = args[i];
			byte[] arg_key = arg.Key_bry();
			Object tid_obj = Arg_keys.Get_by_bry(arg_key);
			if (tid_obj == null) {usr_dlg.Warn_many("", "", "unknown arg_key: ~{0}", String_.new_utf8_(arg_key)); continue;} // ignore invalid args
			byte[] arg_val = arg.Val_bry();
			byte tid = ((ByteVal)tid_obj).Val();
			switch (tid) {
				case Tid_all_bgn: 	all_idx = arg_val; break;
				case Tid_subc_bgn: 	grp_fwds[Xoa_ctg_mgr.Tid_subc] = Bool_.Y_byte; grp_idxs[Xoa_ctg_mgr.Tid_subc] = arg_val; break;
				case Tid_subc_end:  grp_fwds[Xoa_ctg_mgr.Tid_subc] = Bool_.N_byte; grp_idxs[Xoa_ctg_mgr.Tid_subc] = arg_val; break;
				case Tid_file_bgn:	grp_fwds[Xoa_ctg_mgr.Tid_file] = Bool_.Y_byte; grp_idxs[Xoa_ctg_mgr.Tid_file] = arg_val; break;
				case Tid_file_end:  grp_fwds[Xoa_ctg_mgr.Tid_file] = Bool_.N_byte; grp_idxs[Xoa_ctg_mgr.Tid_file] = arg_val; break;
				case Tid_page_bgn:	grp_fwds[Xoa_ctg_mgr.Tid_page] = Bool_.Y_byte; grp_idxs[Xoa_ctg_mgr.Tid_page] = arg_val; break;
				case Tid_page_end:  grp_fwds[Xoa_ctg_mgr.Tid_page] = Bool_.N_byte; grp_idxs[Xoa_ctg_mgr.Tid_page] = arg_val; break;
			}
		}
		return this;
	}		
	public static final byte Tid_all_bgn = 0, Tid_subc_bgn = 1, Tid_subc_end = 2, Tid_file_bgn = 3, Tid_file_end = 4, Tid_page_bgn = 5, Tid_page_end = 6;
	public static final Hash_adp_bry Arg_keys = new Hash_adp_bry(false)
	.Add_bry_byteVal(Xoctg_fmtr_all.Url_arg_all_bgn, Tid_all_bgn)
	.Add_bry_byteVal(Xoctg_fmtr_all.Url_arg_subc_bgn, Tid_subc_bgn)
	.Add_bry_byteVal(Xoctg_fmtr_all.Url_arg_subc_end, Tid_subc_end)
	.Add_bry_byteVal(Xoctg_fmtr_all.Url_arg_file_bgn, Tid_file_bgn)
	.Add_bry_byteVal(Xoctg_fmtr_all.Url_arg_file_end, Tid_file_end)
	.Add_bry_byteVal(Xoctg_fmtr_all.Url_arg_page_bgn, Tid_page_bgn)
	.Add_bry_byteVal(Xoctg_fmtr_all.Url_arg_page_end, Tid_page_end)
	;
}

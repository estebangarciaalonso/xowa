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
import gplx.ios.*; import gplx.xowa.bldrs.*; import gplx.xowa.files.*;
public class Xobc_img_dump_meta extends Xob_itm_dump_base implements Xob_cmd, GfoInvkAble {
	byte repo_idx; int flush_every = 500000; // approximately 1 MB super on estimate of 50 bytes per entry 
	public Xobc_img_dump_meta(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	public String Cmd_key() {return KEY;} public static final String KEY = "img.dump_meta";
	public Io_url Src_dir_merge() 	{return src_dir_merge;} Io_url src_dir_merge;
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		this.Init_dump(KEY);
		src_dir_merge = wiki.Fsys_mgr().Tmp_dir().GenSubDir_nest(Xobc_img_merge_ttl_sql.KEY, "make");
		meta_mgr = wiki.File_mgr().Meta_mgr().Append_only_(true);
		Io_mgr._.DeleteDirDeep_ary(meta_mgr.Root_dir());
	}	Xof_meta_mgr meta_mgr;
	public void Cmd_run() {
		Io_line_rdr src_rdr = rdr_(src_dir_merge);
		Gfo_fld_rdr fld_parser = Gfo_fld_rdr.xowa_();
		Xof_xfer_itm xfer_itm = new Xof_xfer_itm();
		int itm_count = 0; 
		while (src_rdr.Read_next()) {
			Xofo_file itm = new Xofo_file();
			itm.Load_by_merge_rdr(fld_parser, src_rdr);
			byte[] ttl = itm.Name(), redirect = itm.Redirect();
			xfer_itm.Atrs_by_ttl(ttl, redirect);
			byte[] md5 = xfer_itm.Md5();
			Xof_meta_fil meta_fil = meta_mgr.Get_fil_or_new(md5);
			Xof_meta_itm meta_itm = meta_fil.Get_or_new(ttl);
			if (ByteAry_.Len_gt_0(redirect))
				meta_itm.Ptr_ttl_(redirect);
			meta_itm.Vrtl_repo_(repo_idx);
			meta_itm.Update_all(redirect, itm.Orig_w(), itm.Orig_h(), Xof_meta_itm.Exists_unknown, Xof_meta_thumb.Ary_empty);
			if ((itm_count++ % flush_every) == 0) {
				meta_mgr.Save(true);
				Env_.GarbageCollect();
			}
		}
		meta_mgr.Save();
	}
	public void Cmd_end() {}
	public void Cmd_print() {}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_repo_idx_))			repo_idx = (byte)m.ReadInt("v");
		else if	(ctx.Match(k, Invk_flush_every_))		flush_every = m.ReadInt("v");
		else	return super.Invk(ctx, ikey, k, m);
		return this;
	}	private static final String Invk_repo_idx_ = "repo_idx_", Invk_flush_every_ = "flush_every_";
	Io_line_rdr rdr_(Io_url dir) {
		Io_url[] fils = Io_mgr._.QueryDir_fils(dir);
		return new Io_line_rdr(bldr.Usr_dlg(), fils).Key_gen_(Io_line_rdr_key_gen_.first_pipe);
	}
}

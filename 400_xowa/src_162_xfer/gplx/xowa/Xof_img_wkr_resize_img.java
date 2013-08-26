/*
XOWA: the extensible offline wiki application
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
import gplx.gfui.*;
public interface Xof_img_wkr_resize_img {
	boolean Exec(Io_url src, Io_url trg, int trg_w, int trg_h, int ext_id, StringRef rslt_val);
}
class Xof_img_wkr_resize_img_imageMagick implements Xof_img_wkr_resize_img {
	boolean init_needed = true;
	public Xof_img_wkr_resize_img_imageMagick(Xoa_app app, ProcessAdp cmd_convert, ProcessAdp cmd_convert_svg_to_png) {this.app = app; this.cmd_convert = cmd_convert; this.cmd_convert_svg_to_png = cmd_convert_svg_to_png;} ProcessAdp cmd_convert, cmd_convert_svg_to_png; Xoa_app app;
	public boolean Exec(Io_url src, Io_url trg, int trg_w, int trg_h, int ext_id, StringRef rslt_val) {
		if (!Io_mgr._.ExistsFil(src)) return false;
		Io_mgr._.CreateDirIfAbsent(trg.OwnerDir());
		if (init_needed) {
			init_needed = false;
			Gfo_usr_dlg notify_wkr = app.Gui_mgr().Main_win().Gui_wtr();
			cmd_convert.Prog_dlg_(notify_wkr);
			cmd_convert_svg_to_png.Prog_dlg_(notify_wkr);
		}
		ProcessAdp cmd = ext_id == Xof_ext_.Id_svg ? cmd_convert_svg_to_png : cmd_convert;
		cmd.Prog_fmt_(String_.Replace(app.File_mgr().Download_mgr().Download_wkr().Download_xrg().Prog_fmt_hdr(), "~", "~~") + " converting: ~{process_seconds} second(s); ~{process_exe_name} ~{process_exe_args}");
		cmd.Run(src.Raw(), trg.Raw(), trg_w, trg_h);
		rslt_val.Val_(cmd.Rslt_out());
		boolean rv = cmd.Exit_code_pass();
		if (!rv) app.Usr_dlg().Log_many(GRP_KEY, "process_warning", "process completed with warnings: ~{0}", cmd.Rslt_out());
		return true;
	}
	ByteAryBfr tmp_bfr = ByteAryBfr.new_();
	static final String GRP_KEY = "xowa.file.resize";
}
class Xof_img_wkr_resize_img_test implements Xof_img_wkr_resize_img {
	public boolean Exec(Io_url src, Io_url trg, int trg_w, int trg_h, int ext_id, StringRef rslt_val) {
		SizeAdp src_size = ImageAdp_.txt_fil_(src).Size();
		int src_w = src_size.Width(), src_h = src_size.Height();
		if (trg_w < 1) throw Err_.new_fmt_("trg_w must be > 0: {0}", trg_w);
		if (trg_h < 1) trg_h = Xof_xfer_itm_.Scale_height(src_w, src_h, trg_w);
		Io_mgr._.SaveFilStr(trg, SizeAdp_.new_(trg_w, trg_h).XtoStr());
		return true;
	}
}

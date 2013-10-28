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
import gplx.ios.*;
public interface Xobc_lnki_wkr {
	void Wkr_exec(Xop_ctx ctx, Xop_lnki_tkn lnki);
}
class Xobc_lnki_wkr_file extends Xob_itm_dump_base implements Xobc_lnki_wkr {
	public static final String KEY = "img.dump_link";
	public Xobc_lnki_wkr_file(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_ctor(bldr, wiki); this.make_fil_len = 1 * Io_mgr.Len_mb;}
	public Io_url_gen Make_url_gen() {return make_url_gen;} public Xobc_lnki_wkr_file Make_url_gen_(Io_url_gen v) {make_url_gen = v; return this;} Io_url_gen make_url_gen;
	public void Wkr_bgn(Xob_bldr bldr) {
		Io_url temp_dir2 = wiki.Fsys_mgr().Tmp_dir().GenSubDir(KEY);
		this.Init_dump(KEY, temp_dir2.GenSubDir("make"));
		this.lnki_wtr = Gfo_fld_wtr.xowa_().Bfr_(dump_bfr);
		Io_mgr._.DeleteDirDeep_ary(temp_dir);
		dump_url_gen = Io_url_gen_.dir_(temp_dir.GenSubDir("dump"));
		sort_dir = temp_dir.GenSubDir("sort");
		make_url_gen = Io_url_gen_.dir_(temp_dir.GenSubDir("make"));
	}	Io_url sort_dir; Gfo_fld_wtr lnki_wtr;
	public void Wkr_exec(Xop_ctx ctx, Xop_lnki_tkn lnki) {
		byte[] ttl = lnki.Ttl().Page_db();
		ttl = encoder.Decode_lax(lnki.Ttl().Page_db());
		Xofo_lnki.Write(lnki_wtr, ttl, lnki);
		if (dump_bfr.Bry_len() > dump_fil_len) Flush(); // NOTE: doing this after; in order to do before, need to precalc len of width/height which is simply not worth it
	}	Url_encoder encoder = Url_encoder.new_file_();
	public void Wkr_end() {
		Flush();
		Io_sort_cmd_img img_cmd = new Io_sort_cmd_img().Make_url_gen_(make_url_gen).Make_fil_max_(make_fil_len);
		Xobdc_merger.Basic(bldr.Usr_dlg(), dump_url_gen, sort_dir, sort_mem_len, Io_line_rdr_key_gen_all_wo_nl._, img_cmd);		
	}
	public void Flush() {
		Io_mgr._.AppendFilBfr(dump_url_gen.Nxt_url(), dump_bfr);
		dump_bfr.Reset_if_gt(Io_mgr.Len_mb);
	}
}

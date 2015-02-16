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
package gplx.xowa.bldrs.imports.ctgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*; import gplx.xowa.bldrs.imports.*;
import gplx.ios.*;
public class Xoctg_hiddencat_ttl_wkr extends Xob_itm_dump_base implements Xob_cmd, GfoInvkAble {
	public Xoctg_hiddencat_ttl_wkr(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_ctor(bldr, wiki); this.make_fil_len = Io_mgr.Len_mb;} private Xob_sql_join_wkr_ctg_hidden join_wkr;
	public String Cmd_key() {return KEY;} public static final String KEY = "ctg.hiddencat_ttl";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		this.Init_dump(KEY);
		src_sql_dir = wiki.Fsys_mgr().Tmp_dir().GenSubDir_nest(Xoctg_hiddencat_parser_txt.KEY, "make");
		join_wkr = new Xob_sql_join_wkr_ctg_hidden(bldr.App(), wiki, temp_dir, src_sql_dir);
	}	Io_url src_sql_dir;
	public void Cmd_run() {
		Xob_sql_join_mgr.Exec_join(join_wkr);		
	}
	public void Cmd_end() {
		join_wkr.Flush();
		Io_url_gen make_url_gen = Io_url_gen_.dir_(temp_dir.GenSubDir("make"));
		Xobdc_merger.Basic(bldr.Usr_dlg(), join_wkr.Dump_url_gen(), temp_dir.GenSubDir("sort"), sort_mem_len, Io_sort_split_itm_sorter._, Io_line_rdr_key_gen_.first_pipe, new Io_sort_fil_basic(bldr.Usr_dlg(), make_url_gen, make_fil_len));
		if (delete_temp) Io_mgr._.DeleteDirDeep(src_sql_dir);
	}
	public void Cmd_print() {}
}
class Xob_sql_join_wkr_ctg_hidden implements Xob_sql_join_wkr {
	public Xob_sql_join_wkr_ctg_hidden(Xoa_app app, Xow_wiki wiki, Io_url temp_dir, Io_url src_sql_dir) {
		this.app = app; this.wiki = wiki;
		this.dump_url_gen = Io_url_gen_.dir_(temp_dir.GenSubDir("dump"));
		this.src_sql_dir = src_sql_dir;
	}	private Xoa_app app = null; Xow_wiki wiki = null; Io_url src_sql_dir;
	public Io_url_gen Dump_url_gen() {return dump_url_gen;} Io_url_gen dump_url_gen;
	public Io_line_rdr New_main_rdr() {
		Io_url[] urls = Io_mgr._.QueryDir_fils(src_sql_dir);
		return new Io_line_rdr(app.Usr_dlg(), urls).Key_gen_(Io_line_rdr_key_gen_.first_pipe);
	} 
	public Io_line_rdr New_join_rdr() {
		Io_url make_dir = wiki.Fsys_mgr().Url_site_dir(Xow_dir_info_.Tid_id);
		app.Usr_dlg().Prog_many("", "", "getting id files: ~{0}", make_dir.Raw());
		Io_url[] urls = Io_mgr._.QueryDir_args(make_dir).Recur_().FilPath_("*.xdat").ExecAsUrlAry();
		return new Io_line_rdr(app.Usr_dlg(), urls).Key_gen_(Io_line_rdr_key_gen_.first_pipe).File_skip_line0_(true);
	} 
	public void Process_match(Io_line_rdr main, Io_line_rdr join, byte[] key_bry) {
		byte[] src = join.Bfr();
		int itm_end = join.Itm_pos_end();
		int pipe_pos = Bry_finder.Find_bwd(src, Byte_ascii.Pipe, itm_end);
		if (pipe_pos == Bry_.NotFound) throw Err_.new_fmt_("failed to find pipe for name: ~{0}", String_.new_utf8_(src, join.Itm_pos_bgn(), join.Itm_pos_end()));
		file_bfr.Add_mid(src, pipe_pos + 1, itm_end - 1).Add_byte_pipe();
		file_bfr.Add(key_bry).Add_byte_nl();
	}	private Bry_bfr file_bfr = Bry_bfr.new_();
	public void Flush() {
		Io_mgr._.SaveFilBfr(dump_url_gen.Nxt_url(), file_bfr);
	}
}

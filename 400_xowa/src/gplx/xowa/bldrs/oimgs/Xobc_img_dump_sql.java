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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.ios.*; import gplx.xowa.bldrs.*; import gplx.dbs.*; import gplx.xowa.dbs.*;
public class Xobc_img_dump_sql extends Xob_itm_dump_base implements Xob_cmd, GfoInvkAble, Sql_file_parser_cmd {
	private Db_provider provider = null; private Db_stmt stmt = null;
	private Xodb_tbl_image tbl_image = new Xodb_tbl_image();
	private byte[] cur_ttl; private int cur_size, cur_width, cur_height, cur_bits;
	private int commit_count = 10000;
	public Xobc_img_dump_sql(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	public String Cmd_key() {return KEY;} public static final String KEY = "oimg.image";
	public Io_url Src_fil() {return src_fil;} public Xobc_img_dump_sql Src_fil_(Io_url v) {src_fil = v; return this;} private Io_url src_fil;
	public Sql_file_parser Parser() {return parser;} private Sql_file_parser parser = new Sql_file_parser();
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		wiki.Init_assert();	// NOTE: must init wiki for db_mgr_as_sql
		this.Init_dump(KEY);
		if (src_fil == null) {
			src_fil = Xobd_rdr.Find_fil_by(wiki.Fsys_mgr().Root_dir(), "*.sql");
			if (src_fil == null) throw Err_mgr._.fmt_(Xob_cmd_mgr.GRP_KEY, "sql_file_missing", ".sql file not found in dir: ~{0}", wiki.Fsys_mgr().Root_dir());
		}
		parser.Src_fil_(src_fil).Trg_fil_gen_(dump_url_gen).Fld_cmd_(this).Flds_req_idx_(20, 0, 1, 2, 3, 5);

		Xodb_mgr_sql db_mgr = wiki.Db_mgr_as_sql();
		provider = db_mgr.Fsys_mgr().Get_or_make("oimg_image").Provider();
		provider.Txn_mgr().Txn_bgn_if_none();
		tbl_image = new Xodb_tbl_image();
		tbl_image.Create_table(provider);
		stmt = tbl_image.Insert_stmt(provider);		
	}
	public void Cmd_run() {
		parser.Parse(bldr.Usr_dlg());
		provider.Txn_mgr().Txn_end_all();
	}
	public void Exec(byte[] src, byte[] fld_key, int fld_idx, int fld_bgn, int fld_end, ByteAryBfr file_bfr, Sql_file_parser_data data) {
		switch (fld_idx) {
			case Fld_img_name: 		cur_ttl = ByteAry_.Mid(src, fld_bgn, fld_end); break;
			case Fld_img_size: 		cur_size = ByteAry_.XtoIntByPos(src, fld_bgn, fld_end, -1); break;
			case Fld_img_width: 	cur_width = ByteAry_.XtoIntByPos(src, fld_bgn, fld_end, -1); break;
			case Fld_img_height: 	cur_height = ByteAry_.XtoIntByPos(src, fld_bgn, fld_end, -1); break;
			case Fld_img_bits: 		cur_bits = ByteAry_.XtoIntByPos(src, fld_bgn, fld_end, -1);
				tbl_image.Insert(stmt, cur_ttl, cur_size, cur_width, cur_height, cur_bits);
				++commit_count;
				if ((commit_count % 10000) == 0) {
					usr_dlg.Prog_many("", "", "committing: count=~{0} last=~{1}", commit_count, String_.new_utf8_(cur_ttl));
					provider.Txn_mgr().Txn_end_all_bgn_if_none();
				}
				break;
		}
	}
	public void Cmd_end() {}
	public void Cmd_print() {}
	private static final int Fld_img_name = 0, Fld_img_size = 1, Fld_img_width = 2, Fld_img_height = 3, Fld_img_bits = 5;
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_src_fil_))			{src_fil = m.ReadIoUrl("v");}
		else	return super.Invk(ctx, ikey, k, m);
		return this;
	}	private static final String Invk_src_fil_ = "src_fil_";
}

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
package gplx.xowa.bldrs.imports; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.ios.*; import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.dbs.tbls.*;
class Xob_search_sql_make extends Xob_search_base implements Io_make_cmd {
	public Xob_search_sql_make(Xow_wiki wiki) {db_mgr = wiki.Db_mgr_as_sql();} private Xodb_mgr_sql db_mgr = null;
	@Override public String Wkr_key() {return KEY;} public static final String KEY = "import.sql.search_title";
	@Override public gplx.ios.Io_make_cmd Make_cmd_site() {return this;}
	public Io_sort_cmd Make_dir_(Io_url v) {return this;}	// noop	
	public void Sort_bgn() {
		Xodb_file search_db = db_mgr.Fsys_mgr().Get_or_make(Xodb_file.Tid_search, Int_.MaxValue);
		provider = search_db.Provider();
		provider.Txn_mgr().Txn_bgn_if_none();
	}	private Db_provider provider; private int search_id = 0;
	public byte Line_dlm() {return line_dlm;} public Xob_search_sql_make Line_dlm_(byte v) {line_dlm = v; return this;} private byte line_dlm = Byte_ascii.Nil;
	private byte[] prv_word = ByteAry_.Empty;
	public void Sort_do(Io_line_rdr rdr) {
		if (line_dlm == Byte_ascii.Nil) line_dlm = rdr.Line_dlm();
		byte[] bry = rdr.Bfr();
		byte[] cur_word = ByteAry_.Mid(bry, rdr.Key_pos_bgn(), rdr.Key_pos_end());
		if (!ByteAry_.Eq(cur_word, prv_word)) {
			provider.Exec_qry(Db_qry_.insert_("search_title_main")
				.Arg_("search_id", ++search_id)
				.Arg_("search_word", cur_word)
			);
			prv_word = cur_word;
		}
		provider.Exec_qry(Db_qry_.insert_("search_title_link")
			.Arg_("search_id", search_id)
			.Arg_("page_id"	, Base85_utl.XtoIntByAry(bry, rdr.Key_pos_end() + 1, rdr.Key_pos_end() +  5)) // -1: ignore rdr_dlm
			.Arg_("page_len", Base85_utl.XtoIntByAry(bry, rdr.Key_pos_end() + 6, rdr.Key_pos_end() + 10))
		);
	}
	public void Sort_end() {
		provider.Txn_mgr().Txn_end_all();
	}
}
class Xob_search_sql_cmd implements GfoInvkAble {
	public void Exec(Xow_wiki wiki) {
		Xodb_fsys_mgr db_fs = wiki.Db_mgr_as_sql().Fsys_mgr();
		Xodb_file page_db = db_fs.Get_tid_root(Xodb_file.Tid_core);
		Xodb_file search_db = db_fs.Get_or_make(Xodb_file.Tid_search, Int_.MaxValue);
		DataRdr page_rdr =  wiki.Db_mgr_as_sql().Tbl_page().Select_all(page_db.Provider());
		Db_provider search_provider = search_db.Provider();
		Xodb_tbl_search_title_temp search_temp_tbl = new Xodb_tbl_search_title_temp().Create_table(search_provider);
		Db_stmt search_temp_stmt = search_temp_tbl.Insert_stmt(search_provider);
		Gfo_usr_dlg usr_dlg = wiki.App().Usr_dlg();
		Xol_lang lang = wiki.Lang();
		ByteAryBfr bfr = ByteAryBfr.reset_(1024);
		OrderedHash hash = OrderedHash_.new_();
		int page_count = 0;
		while (page_rdr.MoveNextPeer()) {
			int page_id = page_rdr.ReadInt(Xodb_page_tbl.Fld_page_id);
			byte[] ttl = page_rdr.ReadBry(Xodb_page_tbl.Fld_page_title);
			byte[][] words = Xob_search_base.Split(lang, hash, bfr, ttl);
			int words_len = words.length;
			for (int i = 0; i < words_len; i++) {
				byte[] word = words[i];
				search_temp_tbl.Insert(search_temp_stmt, page_id, word);
			}
			++page_count;
			if		((page_count % commit_interval) == 0)
				Commit(search_provider);
			else if ((page_count % progress_interval) == 0)
				usr_dlg.Prog_many("", "", "parse progress: count=~{0} last=~{1}", page_count, String_.new_utf8_(ttl));
		}
		this.Commit(search_provider);
		search_temp_tbl.Make_data(usr_dlg, search_provider);		
	}	private int commit_interval = 100000, progress_interval = 1000;
	private void Commit(Db_provider search_provider) {
		search_provider.Txn_mgr().Txn_end_all_bgn_if_none();
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_commit_interval_))		commit_interval = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_progress_interval_))		progress_interval = m.ReadInt("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_progress_interval_ = "progress_interval_", Invk_commit_interval_ = "commit_interval_";
}
class Xodb_tbl_search_title_temp {
	public Xodb_tbl_search_title_temp Create_table(Db_provider p) {Sqlite_engine_.Tbl_create(p, Tbl_name, Tbl_sql); return this;}
	public void Make_data(Gfo_usr_dlg usr_dlg, Db_provider p) {
		Sqlite_engine_.Idx_create(usr_dlg, p, "search_title_temp", Idx_main);
		p.Exec_sql(Sql_create_word);
		p.Exec_sql(Sql_create_link);
		p.Exec_sql("DROP TABLE IF EXISTS search_title_temp;");
		p.Exec_sql("VACUUM;");
	}
	public Db_stmt Insert_stmt(Db_provider p) {return Db_stmt_.new_insert_(p, Tbl_name, Fld_stt_page_id, Fld_stt_word);}
	public void Insert(Db_stmt stmt, int page_id, byte[] word) {
		stmt.Clear()
		.Val_int_(page_id)
		.Val_str_by_bry_(word)
		.Exec_insert();
	}	
	public static final String Tbl_name = "search_title_temp", Fld_stt_page_id = "stt_page_id", Fld_stt_word = "stt_word";
	public static final Db_idx_itm Idx_main = Db_idx_itm.sql_("CREATE UNIQUE INDEX IF NOT EXISTS search_title_temp__main       ON search_title_word (stt_word, stt_page_id);");
	private static final String Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS search_title_temp"
	,	"( stt_id              integer             NOT NULL    PRIMARY KEY AUTOINCREMENT"
	,	", stt_word            varchar(255)        NOT NULL"
	,	", stt_page_id         integer             NOT NULL"
	,	");"
	);
	private static final String Sql_create_word = String_.Concat_lines_nl
	(	"INSERT INTO search_title_word (stw_word_id, stw_word)"
	,	"SELECT  stt_id"
	,	",       stt_word"
	,	"FROM    search_title_temp"
	,	"GROUP BY "
	,	"        stt_word"
	,	";"
	);
	private static final String Sql_create_link = String_.Concat_lines_nl
	(	"INSERT INTO search_title_link (stl_word_id, stl_page_id)"
	,	"SELECT  stw.stw_word_id"
	,	",       stt.stt_page_id"
	,	"FROM    search_title_temp stt"
	,	"        JOIN search_title_word stw ON stt.stt_id = stw.stt_word_id"
	,	";"
	);
}

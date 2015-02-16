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
package gplx.xowa.bldrs; import gplx.*; import gplx.xowa.*;
import gplx.core.primitives.*; import gplx.core.strings.*;
import gplx.dbs.*; import gplx.dbs.qrys.*; import gplx.xowa.dbs.*; import gplx.xowa.specials.search.*; import gplx.xowa.ctgs.*; import gplx.xowa.dbs.tbls.*;
public class Db_mgr_fxt {
	public Db_mgr_fxt Ctor_fsys()	{bldr_fxt = new Xob_fxt().Ctor(Xoa_test_.Url_root().GenSubDir("root")); return this;} 
	public Db_mgr_fxt Ctor_mem()	{bldr_fxt = new Xob_fxt().Ctor_mem(); return this;} private Xob_fxt bldr_fxt;
	public Xodb_page page_(int id, String modified_on, boolean type_redirect, int text_len) {return new Xodb_page().Id_(id).Modified_on_(DateAdp_.parse_gplx(modified_on)).Type_redirect_(type_redirect).Text_len_(text_len);}
	public Xow_wiki Wiki() {return bldr_fxt.Wiki();}
	public Xob_bldr Bldr() {return bldr_fxt.Bldr();}
	public Db_mgr_fxt doc_ary_(Xodb_page... v) {bldr_fxt.doc_ary_(v); return this;}
	public Xodb_page doc_(int id, String date, String title, String text) {return bldr_fxt.doc_(id, date, title, text);}
	public Xodb_page doc_wo_date_(int id, String title, String text) {return bldr_fxt.doc_(id, "2012-01-02 03:04", title, text);}
	public Xodb_page doc_ttl_(int id, String title) {return bldr_fxt.doc_(id, "2012-01-02 03:04", title, "IGNORE");}
	public Db_mgr_fxt Init_fil(String url, String raw) {return Init_fil(Io_url_.new_fil_(url), raw);}
	public Db_mgr_fxt Init_fil(Io_url url, String raw) {Io_mgr._.SaveFilStr(url, raw); return this;}
	public Db_mgr_fxt Exec_run(Xobd_wkr wkr)		{bldr_fxt.Run(wkr); return this;}
	public Db_mgr_fxt Exec_run(Xob_cmd cmd)			{bldr_fxt.Run_cmds(cmd); return this;}
	public Db_mgr_fxt Exec_run(Xobd_parser_wkr wkr) {bldr_fxt.Run(wkr); return this;}
	public void Init_page_insert(Int_obj_ref page_id_next, int ns_id, String[] ttls) {
		Xow_wiki wiki = this.Wiki();
		Xodb_page_tbl tbl_page = wiki.Db_mgr_as_sql().Tbl_page();
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = tbl_page.Insert_stmt(wiki.Db_mgr_as_sql().Fsys_mgr().Conn_page());
			int len = ttls.length;
			DateAdp modified_on = Tfds.Now_time0_add_min(0);
			for (int i = 0; i < len; i++) {
				String ttl = ttls[i];
				int page_id = page_id_next.Val();
				tbl_page.Insert(stmt, page_id, ns_id, Bry_.new_utf8_(ttl), false, modified_on, 0, page_id, 0, 0);
				page_id_next.Val_add(1);
			}
		} finally {stmt.Rls();}
	}
	public void Test_load_ttl(int ns_id, String ttl_str, Xodb_page expd) {
		Xow_wiki wiki = bldr_fxt.Wiki();
		Xow_ns ns = wiki.Ns_mgr().Ids_get_or_null(ns_id);
		byte[] ttl_bry = Bry_.new_ascii_(ttl_str);
		wiki.Db_mgr_as_sql().Load_mgr().Load_by_ttl(actl, ns, ttl_bry);
		Tfds.Eq(expd.Id(), actl.Id());
		Tfds.Eq_date(expd.Modified_on(), actl.Modified_on());
		Tfds.Eq(expd.Type_redirect(), actl.Type_redirect());
		Tfds.Eq(expd.Text_len(), actl.Text_len());
	}	private Xodb_page actl = new Xodb_page();
	public void Test_load_page(int ns_id, int page_id, String expd) {
		Xow_wiki wiki = bldr_fxt.Wiki();
		Xow_ns ns = wiki.Ns_mgr().Ids_get_or_null(ns_id);
		wiki.Db_mgr_as_sql().Load_mgr().Load_page(actl.Id_(page_id), ns, false);
		Tfds.Eq(expd, String_.new_ascii_(actl.Text()));
	}
	public void Test_search(String search_word_str, int... expd) {
		Xow_wiki wiki = bldr_fxt.Wiki();
		ListAdp rv = ListAdp_.new_();
		byte[] search_word_bry = Bry_.new_ascii_(search_word_str);
		wiki.Db_mgr_as_sql().Load_mgr().Load_search(Cancelable_.Never, rv, search_word_bry, 100);
		Tfds.Eq_ary(expd, Xto_int_ary(rv));
	}
	int[] Xto_int_ary(ListAdp rslts) {
		int len = rslts.Count();
		int[] rv = new int[len];
		for (int i = 0; i < len; i++) {
			Xodb_page page = (Xodb_page)rslts.FetchAt(i);
			rv[i] = page.Id();
		}
		return rv;
	}
	public void Test_category_v1(String ctg_name_str, int... expd) {
		Xow_wiki wiki = bldr_fxt.Wiki();
		byte[] ctg_name_bry = Bry_.new_ascii_(ctg_name_str);
		Xoctg_view_ctg ctg = new Xoctg_view_ctg();
		wiki.Db_mgr_as_sql().Load_mgr().Load_ctg_v1(ctg, ctg_name_bry);
		Tfds.Eq_ary(expd, Xto_int_ary(ctg));
	}
	int[] Xto_int_ary(Xoctg_view_ctg ctg) {
		ListAdp list = ListAdp_.new_();
		byte tid_max = Xoa_ctg_mgr.Tid__max;
		for (byte tid = 0; tid < tid_max; tid++) {
			Xoctg_view_grp grp = ctg.Grp_by_tid(tid); if (grp == null) continue;
			int len = grp.Itms_list().Count();
			for (int i = 0; i < len; i++) {
				Xoctg_view_itm itm = (Xoctg_view_itm)grp.Itms_list().FetchAt(i);
				list.Add(itm.Id());
			}
		}
		return (int[])list.Xto_ary_and_clear(int.class);
	}
	public void Test_category_v2(String ctg_name_str, int... expd) {
		Xow_wiki wiki = bldr_fxt.Wiki();
		byte[] ctg_name_bry = Bry_.new_ascii_(ctg_name_str);
		Xoctg_data_ctg ctg = new Xoctg_data_ctg(ctg_name_bry);
		wiki.Db_mgr_as_sql().Load_mgr().Load_ctg_v2(ctg, ctg_name_bry);
		Tfds.Eq_ary(expd, Xto_int_ary(ctg));
	}
	public void Test_file(String url, String expd) {
		String actl = Io_mgr._.LoadFilStr(url);
		Tfds.Eq_str_lines(expd, actl);
	}
	int[] Xto_int_ary(Xoctg_data_ctg ctg) {
		ListAdp list = ListAdp_.new_();
		byte tid_max = Xoa_ctg_mgr.Tid__max;
		for (byte tid = 0; tid < tid_max; tid++) {
			Xoctg_idx_mgr grp = ctg.Grp_by_tid(tid); if (grp == null) continue;
			int len = grp.Itms_len();
			for (int i = 0; i < len; i++) {
				Xoctg_idx_itm itm = grp.Itms_get_at(i);
				list.Add(itm.Id());
			}
		}
		return (int[])list.Xto_ary_and_clear(int.class);
	}
	public void Init_db_sqlite() {Init_db_sqlite(Xoa_test_.Url_wiki_enwiki().GenSubFil_nest("en.wikipedia.org.sqlite3"));}
	public void Init_db_sqlite(Io_url url) {
		Xow_wiki wiki = this.Wiki();
		Db_conn_pool.I.Clear();
		Xodb_mgr_sql db_mgr = wiki.Db_mgr_create_as_sql();
		db_mgr.Data_storage_format_(gplx.ios.Io_stream_.Tid_file);
		db_mgr.Init_by_ns_map("");
		Db_conn conn = db_mgr.Fsys_mgr().Conn_core();
		conn.Exec_qry(Db_qry_delete.new_all_("xowa_cfg"));
		conn.Exec_qry(Db_qry_delete.new_all_("xowa_db"));
		conn.Exec_qry(Db_qry_delete.new_all_("xowa_ns"));
		conn.Exec_qry(Db_qry_delete.new_all_("page"));
		conn.Exec_qry(Db_qry_delete.new_all_("text"));
		conn.Exec_qry(Db_qry_delete.new_all_("category"));
		conn.Exec_qry(Db_qry_delete.new_all_("categorylinks"));
	}	String dsv_db;
	public void Init_db_tdb() {
		Io_mgr._.InitEngine_mem();
		this.Ctor_mem();
		if (dsv_db == null) dsv_db = dsv_db_();
		// mem/xowa/bin/any/sql/xowa/xowa.sqlite3
		Io_url url = Io_url_.mem_fil_("mem/tdb/en.wikipedia.org/xowa.tdb");
		Io_mgr._.SaveFilStr(url, dsv_db);
		Io_url url2 = Io_url_.mem_fil_("mem/xowa/bin/any/sql/xowa/xowa.sqlite3");
		Io_mgr._.SaveFilStr(url2, dsv_db);
		this.Wiki().Db_mgr_create_as_sql();// .("gplx_key=tdb;url=" + url.Raw());
		this.Wiki().Db_mgr_as_sql().Init_by_ns_map("");
	}
	private static String dsv_db_() {
		String_bldr sb = String_bldr_.new_();
		dsv_tbl_files(sb);
		dsv_tbl_tables(sb);
		dsv_tbl_xowa_cfg(sb);
		dsv_tbl_xowa_db(sb);
		dsv_tbl_xowa_ns(sb);
		dsv_tbl_page(sb);
		dsv_tbl_text(sb);
		dsv_tbl_category(sb);
		dsv_tbl_categorylinks(sb);
		return sb.Xto_str_and_clear();
	}
	private static void Add_lines_w_crlf(String_bldr sb, String... ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			sb.Add(ary[i]);
			sb.Add(String_.CrLf);
		}
	}
	private static void dsv_tbl_files(String_bldr sb) {
		Add_lines_w_crlf(sb
		,	"================================, ,\" \",//"
		,	"_files, ,\" \",#"
		,	"================================, ,\" \",//"
		,	"int,string,string, ,\" \",$"
		,	"id,url,format, ,\" \",@"
		,	"================================, ,\" \",//"
		,	"1,,dsv"
		,	"2,,dsv"
		);
	}
	private static void dsv_tbl_tables(String_bldr sb) {
		Add_lines_w_crlf(sb
		,	"================================, ,\" \",//"
		,	"_tables, ,\" \",#"
		,	"================================, ,\" \",//"
		,	"int,string,int, ,\" \",$"
		,	"id,name,file_id, ,\" \",@"
		,	"================================, ,\" \",//"
		,	"1,xowa_cfg,1"
		,	"2,xowa_db,1"
		,	"3,xowa_ns,1"
		,	"4,page,1"
		,	"5,text,1"
		,	"6,category,1"
		,	"7,categorylinks,1"
//			,	"8,site_stats,1"
//			,	"9,wdata_qids,1"
//			,	"10,wdata_pids,1"
		);
	}
	private static void dsv_tbl_xowa_cfg(String_bldr sb) {
		Add_lines_w_crlf(sb
		,	"================================, ,\" \",//"
		,	"xowa_cfg, ,\" \",#"
		,	"================================, ,\" \",//"
		,	"string,string,string, ,\" \",$"
		,	"cfg_grp,cfg_key,cfg_val, ,\" \",@"
		,	"================================, ,\" \",//"
		);
	}
	private static void dsv_tbl_xowa_db(String_bldr sb) {
		Add_lines_w_crlf(sb
		,	"================================, ,\" \",//"
		,	"xowa_db, ,\" \",#"
		,	"================================, ,\" \",//"
		,	"int,byte,string, ,\" \",$"
		,	"db_id,db_type,db_url, ,\" \",@"
		,	"================================, ,\" \",//"
		);
	}
	private static void dsv_tbl_xowa_ns(String_bldr sb) {
		Add_lines_w_crlf(sb
		,	"================================, ,\" \",//"
		,	"xowa_ns, ,\" \",#"
		,	"================================, ,\" \",//"
		,	"int,string,byte,bool,int, ,\" \",$"
		,	"ns_id,ns_name,ns_case,ns_is_alias,ns_count, ,\" \",@"
		,	"================================, ,\" \",//"
		);
	}
	private static void dsv_tbl_page(String_bldr sb) {
		Add_lines_w_crlf(sb
		,	"================================, ,\" \",//"
		,	"page, ,\" \",#"
		,	"================================, ,\" \",//"
		,	"int,int,string,byte,string,int,int,int, ,\" \",$"
		,	"page_id,page_namespace,page_title,page_is_redirect,page_touched,page_len,page_random_int,page_file_idx, ,\" \",@"
		,	"================================, ,\" \",//"
		);
	}
	private static void dsv_tbl_text(String_bldr sb) {
		Add_lines_w_crlf(sb
		,	"================================, ,\" \",//"
		,	"text, ,\" \",#"
		,	"================================, ,\" \",//"
		,	"int,string, ,\" \",$"
		,	"page_id,old_text, ,\" \",@"
		,	"================================, ,\" \",//"
		);
	}
	private static void dsv_tbl_categorylinks(String_bldr sb) {
		Add_lines_w_crlf(sb
		,	"================================, ,\" \",//"
		,	"categorylinks, ,\" \",#"
		,	"================================, ,\" \",//"
		,	"int,int,string,string,int,byte, ,\" \",$"
		,	"cl_from,cl_to,cl_to_id,cl_sortkey,cl_timestamp,cl_type_id, ,\" \",@"
		,	"================================, ,\" \",//"
		);
	}	
	private static void dsv_tbl_category(String_bldr sb) {
		Add_lines_w_crlf(sb
		,	"================================, ,\" \",//"
		,	"category, ,\" \",#"
		,	"================================, ,\" \",//"
		,	"int,int,int,int,byte,int, ,\" \",$"
		,	"cat_id,cat_pages,cat_subcats,cat_files,cat_hidden,cat_file_idx, ,\" \",@"
		,	"================================, ,\" \",//"
		);
	}	
	public void Rls() {
		this.Wiki().Db_mgr().Rls();
	}
}

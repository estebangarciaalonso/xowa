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
package gplx.xowa.specials.search; import gplx.*; import gplx.xowa.*; import gplx.xowa.specials.*;
import org.junit.*;
public class Xosrh_core_tst {
	@Before public void Init() {fxt.Clear();} private Xos_search_mgr_fxt fxt = new Xos_search_mgr_fxt();
	@Test   public void Basic() {
		fxt.Init_basic();
		fxt.Test_search_exact("b2", "B2_22", "B2_12", "B2__2");
		fxt.Test_search_exact("a" , "A___0");
		fxt.Test_search_exact("b1a");	// missing: mid
		fxt.Test_search_exact("d");		// missing: end
		fxt.Test_search_exact("$");		// missing: bgn
		fxt.Test_search_match_bgn("b*", "B3_23", "B2_22", "B1_21", "B3_13", "B2_12", "B1_11", "B3__3", "B2__2", "B1__1");
	}
	@Test  public void Page_size() {
		fxt.Init_basic();
		fxt.Search_mgr().Page_mgr().Itms_per_page_(1);
		fxt.Test_search("b*", 0, "B3_23");
		fxt.Test_search("b*", 1, "B2_22");
		fxt.Test_search("b*", 2, "B1_21");
		fxt.Test_search("b*", 3, "B3_13");
	}
	@Test  public void Url() {
		Xoa_url url = Xoa_url_parser.Parse_url(fxt.App(), fxt.Wiki(), "Special:Search/Abc?fulltext=y&xowa_sort=len_desc");
		fxt.Search_mgr().Args_mgr().Clear().Parse(url.Args());
		Tfds.Eq(Xosrh_rslt_itm_sorter.Tid_len_dsc, fxt.Search_mgr().Args_mgr().Sort_tid());
	}
	@Test  public void Url_arg_title() {// http://en.wikipedia.org/wiki/Special:Search/Earth?fulltext=yes&title=Mars
		fxt.Test_url_search_bry("Special:Search?fulltext=y&search=Abc"		, "Abc");	// query arg
//			fxt.Test_url_search_bry("Special:Search/Abc?fulltext=y"				, "Abc");	// leaf
		fxt.Test_url_search_bry("Special:Search/Abc?fulltext=y&search=Def"	, "Def");	// leaf overrides query arg
	}
	@Test  public void Url_ns() {
		fxt.Test_url__ns("Special:Search?search=Abc&ns0=1&ns1=1", "0|1");
		fxt.Test_url__ns("Special:Search?search=Abc&ns*=1", "*");
		fxt.Test_url__ns("Special:Search?search=Abc", "0");
	}
	@Test  public void Html() {
		fxt.Init_basic();
		fxt.Test_html_by_url("B1", "", String_.Concat_lines_nl
			(	"Result '''1''' of '''3''' for '''B1'''<br/>"
			,	"{|"
			,	"|-"
			,	"| [[Special:Search/B1?fulltext=y&xowa_page_index=0|&lt;]]"
			,	"| [[Special:Search/B1?fulltext=y&xowa_page_index=1|&gt;]]"
			,	"|-"
			,	"| [[Special:Search/B1?fulltext=y&xowa_sort=len_desc|length]]"
			,	"| [[Special:Search/B1?fulltext=y&xowa_sort=title_asc|title]]"
			,	"|-"
			,	"| 42 || [[B1 21]]"
			,	"|-"
			,	"| 22 || [[B1 11]]"
			,	"|-"
			,	"| 2 || [[B1  1]]"
			,	"|-"
			,	"| [[Special:Search/B1?fulltext=y&xowa_page_index=0|&lt;]]"
			,	"| [[Special:Search/B1?fulltext=y&xowa_page_index=1|&gt;]]"
			,	"|}"
			));
	}
//		@Test   public void Page_next() {
//			fxt.Init_basic();
//			fxt.Search_mgr().Page_size_(1);
//			fxt.Test_search(Xosrh_core.Match_tid_all, "B1", 0, "B1 1");
//			fxt.Test_search(Xosrh_core.Match_tid_all, "B1", 1, "B1 11");
//		}
//		@Test   public void Misc_url() {
//			fxt.Init_basic();
//			fxt.Search_mgr().Page_size_(1);
//			fxt.Expd_address_page_("Special:Search/B1");
//			fxt.Test_search(Xosrh_core.Match_tid_all, "B1", 0, "B1 1");
//		}
	@Test  public void Sort_defaults_to_len_desc() {
		fxt.Init_basic();
		fxt.Search_mgr().Page_mgr().Itms_per_page_(3);
		fxt.Test_search2(Xosrh_core.Match_tid_bgn, "b"	, 0, Xosrh_rslt_itm_sorter.Tid_ttl_asc	, "B1_11", "B1_21", "B1__1");	// sort by name; note that _ sorts after alphabet
		fxt.Test_search2(Xosrh_core.Match_tid_bgn, "b"	, 1, Xosrh_rslt_itm_sorter.Tid_none		, "B2_12", "B2_22", "B2__2");	// sort by name still; next page should not reset
		fxt.Test_search2(Xosrh_core.Match_tid_bgn, "b2"	, 0, Xosrh_rslt_itm_sorter.Tid_none		, "B2_22", "B2_12", "B2__2");	// sort by len desc; new search should reset
	}
}
class Xos_search_mgr_fxt {
	Xoa_app app; Xow_wiki wiki; Bry_bfr bfr = Bry_bfr.reset_(500); Xosrh_core search_mgr;
	public Xoa_app App() {return app;}
	public Xow_wiki Wiki() {return wiki;}
	public Xobl_regy_itm 	regy_itm_(int id, String bgn, String end, int count) {return new Xobl_regy_itm(id, Bry_.new_utf8_(bgn), Bry_.new_utf8_(end), count);}
	public Xodb_page 	data_ttl_(int id, String ttl) {return data_ttl_(id, 0, 0, false, 0, ttl);}
	public Xodb_page 	data_ttl_(int id, int fil, int row, boolean redirect, int len, String ttl) {return new Xodb_page().Set_all_(id, fil, row, redirect, len, Bry_.new_utf8_(ttl));}
	public Xodb_page 		data_id_(int id, String ttl) {return data_id_(id, Xow_ns_.Id_main, ttl);} 
	public Xodb_page 		data_id_(int id, int ns, String ttl) {return new Xodb_page().Id_(id).Ns_id_(ns).Ttl_wo_ns_(Bry_.new_utf8_(ttl)).Text_db_id_(0).Text_len_(0);}
	public Xobl_search_ttl 	data_sttl_(String word, int... ids) {return new Xobl_search_ttl(Bry_.new_utf8_(word), data_ttl_word_page_ary_(ids));}
	public Xobl_search_ttl_page[] data_ttl_word_page_ary_(int... ids) {
		int ids_len = ids.length;
		Xobl_search_ttl_page[] rv = new Xobl_search_ttl_page[ids_len];
		for (int i = 0; i < ids_len; i++) {
			int id = ids[i];
			rv[i] = new Xobl_search_ttl_page(id, id * 2);
		}
		return rv;
	}
	public void Init_regy_site(byte dir_info, Xobl_regy_itm... ary) 	{Init_regy(wiki.Fsys_mgr().Url_site_reg(dir_info), ary);}	
	public void Init_regy_ns  (String ns_num, byte tid, Xobl_regy_itm... ary) 	{Init_regy(wiki.Fsys_mgr().Url_ns_reg(ns_num, tid), ary);}
	public void Init_regy(Io_url url, Xobl_regy_itm[] ary) {		
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			Xobl_regy_itm itm = ary[i];
			itm.Srl_save(tmp_bfr);
		}
		Io_mgr._.SaveFilBfr(url, tmp_bfr);
	}	private Bry_bfr tmp_bfr = Bry_bfr.reset_(255);
	public void Init_data(Io_url fil, Xobl_data_itm... ary) {
		Xob_xdat_file xdat_file = new Xob_xdat_file();
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			Xobl_data_itm itm = ary[i];
			itm.Srl_save(tmp_bfr);
			xdat_file.Insert(bfr, tmp_bfr.Xto_bry_and_clear());
		}
		xdat_file.Save(fil);
	}
	public void Init_basic() {
		this.Init_regy_ns(wiki.Ns_mgr().Ns_main().Num_str(), Xow_dir_info_.Tid_search_ttl, this.regy_itm_(0, "A", "C", 5));
		this.Init_data(wiki.Fsys_mgr().Url_ns_fil(Xow_dir_info_.Tid_search_ttl, Xow_ns_.Id_main, 0)
			, this.data_sttl_("a"	,  0)
			, this.data_sttl_("b1"	,  1, 11, 21)
			, this.data_sttl_("b2"	,  2, 12, 22)
			, this.data_sttl_("b3"	,  3, 13, 23)
			, this.data_sttl_("c"	,  4)
			);
		this.Init_regy_site(Xow_dir_info_.Tid_id, this.regy_itm_(0, "A", "C", 11));
		this.Init_data(wiki.Fsys_mgr().Url_site_fil(Xow_dir_info_.Tid_id, 0)
			, this.data_id_( 0, "A___0")
			, this.data_id_( 1, "B1__1")
			, this.data_id_( 2, "B2__2")
			, this.data_id_( 3, "B3__3")
			, this.data_id_( 4, "C___4")
			, this.data_id_(11, "B1_11")
			, this.data_id_(12, "B2_12")
			, this.data_id_(13, "B3_13")
			, this.data_id_(21, "B1_21")
			, this.data_id_(22, "B2_22")
			, this.data_id_(23, "B3_23")
			);
		search_mgr.Page_mgr().Ns_mgr().Add_all(); // WORKAROUND: xdat fmt does not store ns with search data; pages will be retrieved with ns_id = null; force ns_all (instead of allowing ns_main default);
	}
	public void Clear() {
		Io_mgr._.InitEngine_mem();
		app = Xoa_app_fxt.app_();
		wiki = Xoa_app_fxt.wiki_tst_(app);
		search_mgr = wiki.Special_mgr().Page_search();
		wiki.App().Gui_mgr().Search_suggest_mgr().Args_default_str_("ns*=1"); // WORKAROUND: xdat fmt does not store ns with search data; pages will be retrieved with ns_id = null; force ns_all (instead of allowing ns_main default);
	}
	public Xosrh_core Search_mgr() {return search_mgr;}
	public void Test_url_search_bry(String url_str, String expd) {
		Xoa_url url = Xoa_url_parser.Parse_url(app, wiki, url_str);
		search_mgr.Args_mgr().Clear().Parse(url.Args());
		Tfds.Eq(expd, String_.new_utf8_(search_mgr.Args_mgr().Search_bry()));
	}
	public void Test_url__ns(String url_str, String expd) {
		Xoa_url url = Xoa_url_parser.Parse_url(app, wiki, url_str);
		search_mgr.Args_mgr().Clear().Parse(url.Args());
		Tfds.Eq(expd, String_.new_ascii_(search_mgr.Args_mgr().Ns_mgr().Xto_hash_key()));
	}
	public void Test_search_exact(String ttl_str, String... expd_ary) {Test_search(ttl_str, 0, expd_ary);}
	public void Test_search_match_bgn(String ttl_str, String... expd_ary) {Test_search(ttl_str, 0, expd_ary);}
	public void Test_search(String ttl_str, int page_idx, String... expd_ary) {
		byte[] ttl_bry = Bry_.new_ascii_(ttl_str);
		Bry_bfr bfr = wiki.Utl_bry_bfr_mkr().Get_b128();
		Xosrh_rslt_grp page = search_mgr.Page_mgr().Search(bfr, wiki, ttl_bry, page_idx, search_mgr.Page_mgr());
		bfr.Mkr_rls();
		Tfds.Eq_ary(expd_ary, Search_itms_to_int_ary(page));
	}
	public void Test_html_by_url(String ttl_str, String args_str, String expd_html) {
		wiki.Init_needed_(false);
		byte[] ttl_bry = Bry_.new_ascii_(ttl_str);
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
		Xoa_page page = Xoa_page.test_(wiki, ttl);
		byte[] url_bry = Bry_.new_utf8_("http://en.wikipedia.org/wiki/Special:Search/" + ttl_str + args_str);
		Xoa_url url = wiki.App().Url_parser().Parse(url_bry);
		search_mgr.Special_gen(url, page, wiki, ttl);
		Tfds.Eq_str_lines(expd_html, String_.new_utf8_(page.Root().Data_htm()));
	}
	public void Test_search2(byte match_tid, String ttl_str, int page_idx, byte sort_tid, String... expd_ary) {
		Bry_bfr bfr = wiki.Utl_bry_bfr_mkr().Get_b128();
		Xoa_url_parser url_parser = new Xoa_url_parser();			
		byte[] url_raw = Bry_.new_ascii_("Special:Search/" + ttl_str + ((match_tid == Xosrh_core.Match_tid_all) ? "" : "*")  + "?fulltext=y" + Xosrh_rslt_itm_sorter.Xto_url_arg(sort_tid) + "&xowa_page_size=1&xowa_page_index=" + page_idx);
		Xoa_url url = url_parser.Parse(url_raw);
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, url_raw);
		Xoa_page page = wiki.Ctx().Cur_page();
		search_mgr.Special_gen(url, page, wiki, ttl);
		Xosrh_rslt_grp cur_grp = search_mgr.Cur_grp();
		bfr.Mkr_rls();
		Tfds.Eq_ary(expd_ary, Search_itms_to_int_ary(cur_grp));
	}		
	String[] Search_itms_to_int_ary(Xosrh_rslt_grp page) {
		int itms_len = page.Itms_len();
		String[] rv = new String[itms_len];
		for (int i = 0; i < itms_len; i++) {
			Xodb_page itm = page.Itms_get_at(i);
			rv[i] = String_.new_utf8_(itm.Ttl_wo_ns());
		}
		return rv;
	}
}

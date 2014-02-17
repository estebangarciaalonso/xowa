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
public class Xop_fxt {
	public Xop_fxt() {
		Xoa_app app = Xoa_app_fxt.app_();
		wiki = Xoa_app_fxt.wiki_tst_(app);
		ctor(app, wiki);
	}
	public Xop_fxt(Xoa_app app, Xow_wiki wiki) {
		this.ctor(app, wiki);
	}
	private void ctor(Xoa_app app, Xow_wiki wiki) {
		this.app = app;
		this.wiki = wiki;
		app.Wiki_mgr().Add(wiki);
		app.File_mgr().Repo_mgr().Set("src:wiki", "mem/wiki/repo/src/", wiki.Domain_str()).Ext_rules_(Xoft_rule_grp.Grp_app_default).Dir_depth_(2);
		app.File_mgr().Repo_mgr().Set("trg:wiki", "mem/wiki/repo/trg/", wiki.Domain_str()).Ext_rules_(Xoft_rule_grp.Grp_app_default).Dir_depth_(2).Primary_(true);
		wiki.File_mgr().Repo_mgr().Add_repo(ByteAry_.new_utf8_("src:wiki"), ByteAry_.new_utf8_("trg:wiki"));
		ctx = wiki.Ctx();
		mock_wkr.Clear_commons();	// assume all files are in repo 0
		wiki.File_mgr().Repo_mgr().Page_finder_(mock_wkr);
		parser = wiki.Parser();
		tkn_mkr = app.Tkn_mkr();
		ctx.Para().Enabled_n_();
		html_wtr = new Xoh_html_wtr(wiki);
		wiki.Html_mgr().Img_suppress_missing_src_(false);
		wiki.Xtn_mgr().Init_by_wiki(wiki);
		Page_ttl_(Ttl_str);
	}
	private Xofw_wiki_wkr_mock mock_wkr = new Xofw_wiki_wkr_mock();
	public Xoa_app App() {return app;} private Xoa_app app;
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public Xop_ctx Ctx() {return ctx;} private Xop_ctx ctx;
	public Xop_parser Parser() {return parser;} private Xop_parser parser; 
	public Xoa_page Page() {return ctx.Page();}
	public void Lang_by_id_(int id) {ctx.Page().Lang_(wiki.App().Lang_mgr().Get_by_key_or_new(Xol_lang_itm_.Get_by_id(id).Key()));}
	public Xoh_ctx Hctx() {return html_wtr.Hctx();} private Xoh_html_wtr html_wtr;
	public Xop_fxt Reset() {
		ctx.Clear();
		ctx.App().Free_mem(false);
		ctx.Page().Clear();
		ctx.Wiki().Db_mgr().Load_mgr().Clear();
		ctx.App().Wiki_mgr().Clear();
		ctx.Wiki().View_data().Clear();	// needed for {{#lst}} tests
		return this;
	}
	public Xoa_ttl Page_ttl_(String txt) {
		Xoa_ttl rv = Xoa_ttl.parse_(wiki, ByteAry_.new_utf8_(txt));
		ctx.Page().Ttl_(rv);
		return rv;
	}

	public Xop_tkn_chkr_base tkn_bry_(int bgn, int end)				{return new Xop_tkn_chkr_base().TypeId_dynamic(Xop_tkn_itm_.Tid_bry).Src_rng_(bgn, end);}
	public Xop_tkn_chkr_base tkn_txt_() 							{return tkn_txt_(String_.Neg1_pos, String_.Neg1_pos);}
	public Xop_tkn_chkr_base tkn_txt_(int bgn, int end)				{return new Xop_tkn_chkr_base().TypeId_dynamic(Xop_tkn_itm_.Tid_txt).Src_rng_(bgn, end);}
	public Xop_tkn_chkr_base tkn_space_() 							{return tkn_space_(String_.Neg1_pos, String_.Neg1_pos);}
	public Xop_tkn_chkr_base tkn_space_(int bgn, int end)			{return new Xop_tkn_chkr_base().TypeId_dynamic(Xop_tkn_itm_.Tid_space).Src_rng_(bgn, end);}
	public Xop_tkn_chkr_base tkn_eq_(int bgn) 						{return tkn_eq_(bgn, bgn + 1);}
	public Xop_tkn_chkr_base tkn_eq_(int bgn, int end) 				{return new Xop_tkn_chkr_base().TypeId_dynamic(Xop_tkn_itm_.Tid_eq).Src_rng_(bgn, end);}
	public Xop_tkn_chkr_base tkn_colon_(int bgn) 					{return new Xop_tkn_chkr_base().TypeId_dynamic(Xop_tkn_itm_.Tid_colon).Src_rng_(bgn, bgn + 1);}
	public Xop_tkn_chkr_base tkn_pipe_(int bgn)						{return new Xop_tkn_chkr_base().TypeId_dynamic(Xop_tkn_itm_.Tid_pipe).Src_rng_(bgn, bgn + 1);}
	public Xop_tkn_chkr_base tkn_tab_(int bgn)						{return new Xop_tkn_chkr_base().TypeId_dynamic(Xop_tkn_itm_.Tid_tab).Src_rng_(bgn, bgn + 1);}
	@gplx.Internal protected Xop_apos_tkn_chkr tkn_apos_(int cmd) 					{return new Xop_apos_tkn_chkr().Apos_cmd_(cmd);}
	public Xop_tkn_chkr_base tkn_html_ref_(String v) 				{return new Xop_html_ref_tkn_chkr().Html_ref_key_(v);}
	public Xop_tkn_chkr_base tkn_html_ncr_(int v) 					{return new Xop_html_ncr_tkn_chkr().Html_ncr_val_(v);}
	@gplx.Internal protected Xop_ignore_tkn_chkr tkn_comment_(int bgn, int end)		{return tkn_ignore_(bgn, end, Xop_ignore_tkn.Ignore_tid_comment);}
	@gplx.Internal protected Xop_ignore_tkn_chkr tkn_ignore_(int bgn, int end, byte t){return (Xop_ignore_tkn_chkr)new Xop_ignore_tkn_chkr().Ignore_tid_(t).Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_tkn_chkr_hr   tkn_hr_(int bgn, int end)				{return new Xop_tkn_chkr_hr(bgn, end).Hr_len_(Xop_hr_lxr.Hr_len);}
	@gplx.Internal protected Xop_tblw_tb_tkn_chkr tkn_tblw_tb_(int bgn, int end) 		{return (Xop_tblw_tb_tkn_chkr)new Xop_tblw_tb_tkn_chkr().Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_tblw_tc_tkn_chkr tkn_tblw_tc_(int bgn, int end) 		{return (Xop_tblw_tc_tkn_chkr)new Xop_tblw_tc_tkn_chkr().Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_tblw_td_tkn_chkr tkn_tblw_td_(int bgn, int end) 		{return (Xop_tblw_td_tkn_chkr)new Xop_tblw_td_tkn_chkr().Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_tblw_th_tkn_chkr tkn_tblw_th_(int bgn, int end) 		{return (Xop_tblw_th_tkn_chkr)new Xop_tblw_th_tkn_chkr().Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_tblw_tr_tkn_chkr tkn_tblw_tr_(int bgn, int end) 		{return (Xop_tblw_tr_tkn_chkr)new Xop_tblw_tr_tkn_chkr().Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_hdr_tkn_chkr tkn_hdr_(int bgn, int end, int hdr_len)	{return (Xop_hdr_tkn_chkr)new Xop_hdr_tkn_chkr().Hdr_len_(hdr_len).Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_xnde_tkn_chkr tkn_xnde_br_(int pos)					{return tkn_xnde_(pos, pos).Xnde_tagId_(Xop_xnde_tag_.Tid_br);}
	public Xop_xnde_tkn_chkr tkn_xnde_()							{return tkn_xnde_(String_.Neg1_pos, String_.Neg1_pos);}
	public Xop_xnde_tkn_chkr tkn_xnde_(int bgn, int end)			{return (Xop_xnde_tkn_chkr)new Xop_xnde_tkn_chkr().Src_rng_(bgn, end);}
	public Xop_tkn_chkr_base tkn_curly_bgn_(int bgn)				{return new Xop_tkn_chkr_base().TypeId_dynamic(Xop_tkn_itm_.Tid_tmpl_curly_bgn).Src_rng_(bgn, bgn + 2);}
	public Xop_tkn_chkr_base tkn_para_blank_(int pos)				{return tkn_para_(pos, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_none);}
	public Xop_tkn_chkr_base tkn_para_bgn_pre_(int pos)				{return tkn_para_(pos, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_pre);}
	public Xop_tkn_chkr_base tkn_para_bgn_para_(int pos)			{return tkn_para_(pos, Xop_para_tkn.Para_typeId_none, Xop_para_tkn.Para_typeId_para);}
	public Xop_tkn_chkr_base tkn_para_mid_para_(int pos)			{return tkn_para_(pos, Xop_para_tkn.Para_typeId_para, Xop_para_tkn.Para_typeId_para);}
	public Xop_tkn_chkr_base tkn_para_end_para_(int pos)			{return tkn_para_(pos, Xop_para_tkn.Para_typeId_para, Xop_para_tkn.Para_typeId_none);}
	public Xop_tkn_chkr_base tkn_para_end_pre_bgn_para_(int pos)	{return tkn_para_(pos, Xop_para_tkn.Para_typeId_pre , Xop_para_tkn.Para_typeId_para);}
	public Xop_tkn_chkr_base tkn_para_end_para_bgn_pre_(int pos)	{return tkn_para_(pos, Xop_para_tkn.Para_typeId_para, Xop_para_tkn.Para_typeId_pre);}
	public Xop_tkn_chkr_base tkn_para_end_pre_(int pos)				{return tkn_para_(pos, Xop_para_tkn.Para_typeId_pre , Xop_para_tkn.Para_typeId_none);}
	public Xop_tkn_chkr_base tkn_para_(int pos, byte tid, byte bgn) {return new Xop_para_tkn_chkr().Para_typeId_(tid).Para_bgn_(bgn).Src_rng_(pos, pos);}
	public Xop_tkn_chkr_base tkn_nl_auto_(int pos)					{return tkn_nl_(pos, pos, Xop_nl_tkn.Tid_auto);}
	public Xop_tkn_chkr_base tkn_nl_char_(int bgn, int end)			{return tkn_nl_(bgn, end, Xop_nl_tkn.Tid_char);}
	public Xop_tkn_chkr_base tkn_nl_char_len1_(int bgn)				{return tkn_nl_(bgn, bgn + 1, Xop_nl_tkn.Tid_char);}
	public Xop_tkn_chkr_base tkn_nl_char_len0_(int pos)				{return tkn_nl_(pos, pos, Xop_nl_tkn.Tid_char);}
	public Xop_tkn_chkr_base tkn_nl_(int bgn, int end, byte tid)	{return new Xop_nl_tkn_chkr().Nl_typeId_(tid).Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_list_tkn_chkr tkn_list_bgn_(int bgn, int end, byte listType) {return (Xop_list_tkn_chkr)new Xop_list_tkn_chkr().List_itmTyp_(listType).Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_list_tkn_chkr tkn_list_end_(int pos)					{return (Xop_list_tkn_chkr)new Xop_list_tkn_chkr().Src_rng_(pos, pos);}
	@gplx.Internal protected Xop_tkn_chkr_lnke tkn_lnke_(int bgn, int end)			{return new Xop_tkn_chkr_lnke(bgn, end);}
	@gplx.Internal protected Xop_lnki_tkn_chkr tkn_lnki_()							{return tkn_lnki_(-1, -1);}
	@gplx.Internal protected Xop_lnki_tkn_chkr tkn_lnki_(int bgn, int end)			{return (Xop_lnki_tkn_chkr)new Xop_lnki_tkn_chkr().Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_arg_itm_tkn_chkr	tkn_arg_itm_(Xop_tkn_chkr_base... subs) {return (Xop_arg_itm_tkn_chkr)new Xop_arg_itm_tkn_chkr().Subs_(subs);}
	@gplx.Internal protected Xop_arg_nde_tkn_chkr	tkn_arg_nde_()						{return tkn_arg_nde_(String_.Neg1_pos, String_.Neg1_pos);}
	@gplx.Internal protected Xop_arg_nde_tkn_chkr	tkn_arg_nde_(int bgn, int end)		{return (Xop_arg_nde_tkn_chkr)new Xop_arg_nde_tkn_chkr().Src_rng_(bgn, end);}
	@gplx.Internal protected Xop_arg_nde_tkn_chkr tkn_arg_val_(Xop_tkn_chkr_base... subs) {
		Xop_arg_nde_tkn_chkr rv = new Xop_arg_nde_tkn_chkr();
		Xop_arg_itm_tkn_chkr val = new Xop_arg_itm_tkn_chkr();
		val.Subs_(subs);
		rv.Val_tkn_(val);
		return rv;
	}
	@gplx.Internal protected Xop_arg_nde_tkn_chkr tkn_arg_val_txt_(int bgn, int end) {
		Xop_arg_nde_tkn_chkr rv = new Xop_arg_nde_tkn_chkr();
		Xop_arg_itm_tkn_chkr itm = new Xop_arg_itm_tkn_chkr();
		rv.Val_tkn_(itm);
		itm.Src_rng_(bgn, end).Subs_(tkn_txt_(bgn, end));
		return rv;
	}
	Xop_arg_nde_tkn_chkr tkn_arg_key_txt_(int bgn, int end) {
		Xop_arg_nde_tkn_chkr rv = new Xop_arg_nde_tkn_chkr();
		Xop_arg_itm_tkn_chkr itm = new Xop_arg_itm_tkn_chkr();
		rv.Key_tkn_(itm);
		itm.Src_rng_(bgn, end).Subs_(tkn_txt_(bgn, end));
		return rv;
	}
	@gplx.Internal protected Xot_invk_tkn_chkr tkn_tmpl_invk_(int bgn, int end) {return (Xot_invk_tkn_chkr)new Xot_invk_tkn_chkr().Src_rng_(bgn, end);}
	@gplx.Internal protected Xot_invk_tkn_chkr tkn_tmpl_invk_w_name(int bgn, int end, int name_bgn, int name_end) {
		Xot_invk_tkn_chkr rv = new Xot_invk_tkn_chkr();
		rv.Src_rng_(bgn, end);
		rv.Name_tkn_(tkn_arg_key_txt_(name_bgn, name_end));
		return rv;
	}
	@gplx.Internal protected Xot_prm_chkr tkn_tmpl_prm_find_(Xop_tkn_chkr_base find) {
		Xot_prm_chkr rv = new Xot_prm_chkr();
		rv.Find_tkn_(tkn_arg_itm_(find));
		return rv;
	}
	public Xop_fxt	Init_log_(Gfo_msg_itm... itms) {for (Gfo_msg_itm itm : itms) log_itms.Add(itm); return this;} ListAdp log_itms = ListAdp_.new_();
	public void		Init_defn_add(String name, String text) {Init_defn_add(name, text, Xow_ns_case_.Id_all);}
	public void		Init_defn_add(String name, String text, byte case_match) {
		Xot_defn_tmpl itm = run_Parse_tmpl(ByteAry_.new_ascii_(name), ByteAry_.new_utf8_(text));
		wiki.Cache_mgr().Defn_cache().Add(itm, case_match);
	}
	public void		Init_defn_clear() {wiki.Cache_mgr().Defn_cache().Free_mem_all();}
	public Xop_fxt	Init_id_create(int id, int fil_idx, int row_idx, boolean type_redirect, int itm_len, int ns_id, String ttl) {Xow_hive_mgr_fxt.Create_id(app, wiki.Hive_mgr(), id, fil_idx, row_idx, type_redirect, itm_len, ns_id, ttl); return this;}
	public Xop_fxt	Init_ctg_create(String ctg, int... pages) {Xow_hive_mgr_fxt.Create_ctg(app, wiki.Hive_mgr(), ctg, pages); return this;}
	public Xop_fxt	Init_page_create(String ttl) {return Init_page_create(wiki, ttl, "");}
	public Xop_fxt	Init_page_create(String ttl, String txt) {return Init_page_create(wiki, ttl, txt);}
	public Xop_fxt	Init_page_create(Xow_wiki wiki, String ttl, String txt) {Init_page_create_static(wiki, ttl, txt);return this;}
	public static void Init_page_create_static(Xow_wiki wiki, String ttl_str, String text_str) {
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ByteAry_.new_utf8_(ttl_str));
		byte[] text = ByteAry_.new_utf8_(text_str);
		wiki.Db_mgr().Save_mgr().Data_create(ttl, text);
	}
	public Xop_fxt	Init_page_update(String ttl, String txt) {return Init_page_update(wiki, ttl, txt);}
	public Xop_fxt	Init_page_update(Xow_wiki wiki, String ttl, String txt) {
		Xoa_ttl page_ttl = Xoa_ttl.parse_(wiki, ByteAry_.new_utf8_(ttl));
		byte[] page_raw = ByteAry_.new_utf8_(txt);
		Xoa_page page = wiki.Data_mgr().Get_page(page_ttl, false);
		wiki.Db_mgr().Save_mgr().Data_update(page, page_raw);
		return this;
	}
	public Xop_fxt	Init_xwiki_clear() {
		wiki.Xwiki_mgr().Clear();
		app.User().Wiki().Xwiki_mgr().Clear();
		return this;
	}
	public Xop_fxt	Init_xwiki_add_wiki_and_user_(String alias, String domain) {
		wiki.Xwiki_mgr().Add_full(alias, domain);
		app.User().Wiki().Xwiki_mgr().Add_full(domain, domain);
		return this;
	}
	public Xop_fxt	Init_xwiki_add_user_(String domain) {
		app.User().Wiki().Xwiki_mgr().Add_full(domain, domain);
		return this;
	}
	public void Test_parse_tmpl_str_test(String tmpl_raw, String page_raw, String expd) {
		Init_defn_add("test", tmpl_raw);
		Test_parse_tmpl_str(page_raw, expd);
	}
	public void Test_parse_tmpl_str(String raw, String expd) {
		byte[] actl = Test_parse_tmpl_str_rv(raw);
		Tfds.Eq_str_lines(expd, String_.new_utf8_(actl));
		tst_Log_check();
	}
	public byte[] Test_parse_tmpl_str_rv(String raw) {
		byte[] raw_bry = ByteAry_.new_utf8_(raw);
		Xop_root_tkn root = tkn_mkr.Root(raw_bry);
		return parser.Parse_page_tmpl(root, ctx, tkn_mkr, raw_bry);
	}
	public Xot_defn_tmpl run_Parse_tmpl(byte[] name, byte[] raw) {return parser.Parse_tmpl(ctx, ctx.Tkn_mkr(), wiki.Ns_mgr().Ns_template(), name, raw);}
	public void Test_parse_tmpl(String raw, Tst_chkr... expd) {
		byte[] raw_bry = ByteAry_.new_utf8_(raw);
		Xot_defn_tmpl itm = run_Parse_tmpl(ByteAry_.Empty, raw_bry);
		Parse_chk(raw_bry, itm.Root(), expd);
	}
	public void Test_parse_page_tmpl_str(String raw, String expd) {
		byte[] raw_bry = ByteAry_.new_utf8_(raw);
		Xop_root_tkn root = tkn_mkr.Root(raw_bry);
		byte[] actl = parser.Parse_page_tmpl(root, ctx, tkn_mkr, raw_bry);
		Tfds.Eq(expd, String_.new_utf8_(actl));
		tst_Log_check();
	}
	public void Test_parse_page_tmpl(String raw, Tst_chkr... expd_ary) {
		byte[] raw_bry = ByteAry_.new_utf8_(raw);
		Xop_root_tkn root = tkn_mkr.Root(raw_bry);
		parser.Parse_page_tmpl(root, ctx, tkn_mkr, raw_bry);
		Parse_chk(raw_bry, root, expd_ary);
	}
	public void Test_parse_page_wiki(String raw, Tst_chkr... expd_ary) {
		byte[] raw_bry = ByteAry_.new_utf8_(raw);
		Xop_root_tkn root = Test_parse_page_wiki_root(raw_bry);
		Parse_chk(raw_bry, root, expd_ary);
	}
	public Xop_root_tkn Test_parse_page_wiki_root(String raw) {return Test_parse_page_wiki_root(ByteAry_.new_utf8_(raw));}
	Xop_root_tkn Test_parse_page_wiki_root(byte[] raw_bry) {
		Xop_root_tkn root = tkn_mkr.Root(raw_bry);
		parser.Parse_page_wiki(root, ctx, tkn_mkr, raw_bry, Xop_parser_.Doc_bgn_bos);
		return root;
	}
	public void Test_parse_page_all(String raw, Tst_chkr... expd_ary) {
		byte[] raw_bry = ByteAry_.new_utf8_(raw);
		Xop_root_tkn root = Exec_parse_page_all_as_root(ByteAry_.new_utf8_(raw));
		Parse_chk(raw_bry, root, expd_ary);
	}
	public void Data_create(String ttl_str, String text_str) {Init_page_create(wiki, ttl_str, text_str);}
	public void Test_parse_page_all_str(String raw, String expd) {
		String actl = Exec_parse_page_all_as_str(raw);
		Tfds.Eq_ary_str(String_.SplitLines_nl(expd), String_.SplitLines_nl(actl), raw);
	}
	public Xop_root_tkn Exec_parse_page_all_as_root(byte[] raw_bry) {
		Xop_root_tkn root = tkn_mkr.Root(raw_bry);
		parser.Parse_page_all_clear(root, ctx, tkn_mkr, raw_bry);
		return root;
	}
	public String Exec_parse_page_all_as_str(String raw) {
		Xop_root_tkn root = Exec_parse_page_all_as_root(ByteAry_.new_utf8_(raw));
		ByteAryBfr actl_bfr = ByteAryBfr.new_();
		html_wtr.Write_all(ctx, root, root.Root_src(), actl_bfr);
		return actl_bfr.XtoStrAndClear();
	}
	private void Parse_chk(byte[] raw_bry, Xop_root_tkn root, Tst_chkr[] expd_ary) {
		int subs_len = root.Subs_len();
		Object[] actl_ary = new Object[subs_len];
		for (int i = 0; i < subs_len; i++)
			actl_ary[i] = root.Subs_get(i);
		tst_mgr.Vars().Clear().Add("raw_bry", raw_bry);
		tst_mgr.Tst_ary("tkns:", expd_ary, actl_ary);
		tst_Log_check();
	}
	public String Exec_parse_page_wiki_as_str(String raw) {
		byte[] raw_bry = ByteAry_.new_utf8_(raw);
		Xop_root_tkn root = tkn_mkr.Root(raw_bry);
		parser.Parse_page_wiki(root, ctx, tkn_mkr, raw_bry, Xop_parser_.Doc_bgn_bos);
		ByteAryBfr actl_bfr = ByteAryBfr.new_();
		html_wtr.Write_all(ctx, root, raw_bry, actl_bfr);
		return actl_bfr.XtoStrAndClear();
	}
	public Xop_fxt Test_parse_page_wiki_str(String raw, String expd) {
		Tfds.Eq_str_lines(expd, Exec_parse_page_wiki_as_str(raw), raw);
		return this;
	}
	public void Log_clear() {ctx.App().Msg_log().Clear();}
	public String[] Log_xtoAry() {
		Gfo_msg_log msg_log = app.Msg_log();
		int len = msg_log.Ary_len();
		ListAdp actl_list = ListAdp_.new_();
		for (int i = 0; i < len; i++) {
			Gfo_msg_data eny = msg_log.Ary_get(i);
			if (eny.Item().Cmd() > Gfo_msg_itm_.Cmd_note) {
				actl_list.Add(String_.new_utf8_(eny.Item().Path_bry()));
			}
		}
		String[] actl = actl_list.XtoStrAry();
		msg_log.Clear();
		return actl;
	}
	public Xop_fxt tst_Log_check() {
		int len = log_itms.Count();
		String[] expd = new String[len];
		for (int i = 0; i < len; i++) {
			Gfo_msg_itm itm = (Gfo_msg_itm)log_itms.FetchAt(i);
			expd[i] = itm.Path_str();
		}
		log_itms.Clear();
		String[] actl = Log_xtoAry();
		Tfds.Eq_ary_str(expd, actl);
		return this;
	}
	public void tst_Warn(String... expd) {
		Gfo_usr_dlg usr_dlg = app.Usr_dlg();
		Gfo_usr_dlg_ui_test ui_wkr = (Gfo_usr_dlg_ui_test)usr_dlg.Ui_wkr();
		String[] actl = ui_wkr.Warns().XtoStrAry();
		Tfds.Eq_ary_str(expd, actl);
	}
	public void Test_parse_page(String ttl, String expd) {
		byte[] actl = Load_page(wiki, ttl);
		Tfds.Eq(expd, String_.new_utf8_(actl));
	}
	public static byte[] Load_page(Xow_wiki wiki, String ttl_str) {
		byte[] ttl_bry = ByteAry_.new_utf8_(ttl_str);
		Xoa_url page_url = Xoa_url.new_(wiki.Domain_bry(), ttl_bry);
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
		return wiki.GetPageByTtl(page_url, ttl).Data_raw();
	}
	public static void Reg_xwiki_alias(Xow_wiki wiki, String alias, String domain) {
		byte[] domain_bry = ByteAry_.new_ascii_(domain);
		wiki.Xwiki_mgr().Add_full(ByteAry_.new_ascii_(alias), domain_bry);
		wiki.App().User().Wiki().Xwiki_mgr().Add_full(domain_bry, domain_bry);
	}
	public static String html_img_none(String trg, String alt, String src, String ttl) {
		return String_.Format(String_.Concat_lines_nl_skipLast("<a href=\"/wiki/{0}\" class=\"image\" xowa_title=\"{3}\"><img id=\"xowa_file_img_0\" alt=\"{1}\" src=\"{2}\" width=\"9\" height=\"8\" /></a>"), trg, alt, src, ttl);
	}
	private Tst_mgr tst_mgr = new Tst_mgr(); private Xop_tkn_mkr tkn_mkr;
	public static final String Ttl_str = "Test page";
}

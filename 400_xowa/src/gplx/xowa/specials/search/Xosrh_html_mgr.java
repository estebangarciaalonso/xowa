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
class Xosrh_html_mgr implements GfoInvkAble {
	public ByteAryFmtr Html_all_bgn()	{return html_all_bgn;} ByteAryFmtr html_all_bgn = ByteAryFmtr.new_(String_.Concat_lines_nl
	(	"~{search_results_header}<br/>"
	,	"{|"
	,	"|-"
	,	"| [[Special:Search/~{title}?fulltext=y&xowa_page_index=~{page_index_prev}|&lt;]]"
	,	"| [[Special:Search/~{title}?fulltext=y&xowa_page_index=~{page_index_next}|&gt;]]"
	,	"|-"
	,	"| [[Special:Search/~{title}?fulltext=y&xowa_sort=len_desc|length]]"
	,	"| [[Special:Search/~{title}?fulltext=y&xowa_sort=title_asc|title]]"
	), 	"search_results_header", "title", "page_index_prev", "page_index_next"
	);
	public ByteAryFmtr Html_itm()		{return html_itm;} ByteAryFmtr html_itm = ByteAryFmtr.new_(String_.Concat_lines_nl
	(	"|-"
	,	"| ~{length} || [[~{title}]]"
	), "title", "length"
	);
	public ByteAryFmtr Html_all_end()	{return html_all_end;} ByteAryFmtr html_all_end = ByteAryFmtr.new_(String_.Concat_lines_nl
	(	"|-"
	,	"| [[Special:Search/~{title}?fulltext=y&xowa_page_index=~{page_index_prev}|&lt;]]"
	,	"| [[Special:Search/~{title}?fulltext=y&xowa_page_index=~{page_index_next}|&gt;]]"
	,	"|}\n"
	), "title", "page_index_prev", "page_index_next"
	);
	public void Bld_html(ByteAryBfr bfr, Xosrh_core core, Xosrh_rslt_grp grp, byte[] search_bry, int page_idx, int pages_len) {
		int itms_len = grp.Itms_len();
		int xowa_idx_bwd = (page_idx == 0) 			? 0 			: page_idx - 1;
		int xowa_idx_fwd = (page_idx >= pages_len) 	? pages_len - 1 : page_idx + 1;
		Xow_wiki wiki = core.Wiki();
		gplx.intl.Gfo_num_fmt_mgr num_fmt_mgr = wiki.Lang().Num_fmt_mgr();
		int itms_bgn = core.Page_mgr().Itms_bgn() + 1;
		if (itms_len == 0) itms_bgn = 0;
		byte[] search_results_header = wiki.Msg_mgr().Val_by_id_args(Xol_msg_itm_.Id_search_results_header, num_fmt_mgr.Fmt(itms_bgn), num_fmt_mgr.Fmt(core.Page_mgr().Itms_end()), num_fmt_mgr.Fmt(grp.Itms_total()), search_bry, pages_len);
		html_all_bgn.Bld_bfr_many(bfr, search_results_header, search_bry, xowa_idx_bwd, xowa_idx_fwd);
		for (int i = 0; i < itms_len; i++) {
			Xodb_page itm = grp.Itms_get_at(i);
			html_itm.Bld_bfr_many(bfr, Xoa_ttl.Replace_unders(itm.Ttl_wo_ns()), itm.Text_len());
		}
		html_all_end.Bld_bfr_many(bfr, search_bry, xowa_idx_bwd, xowa_idx_fwd);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_all_bgn_))				html_all_bgn.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_all_end_))				html_all_end.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_itm_))					html_itm.Fmt_(m.ReadBry("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_all_bgn_ = "all_bgn_", Invk_all_end_ = "all_end_", Invk_itm_ = "itm_";
}

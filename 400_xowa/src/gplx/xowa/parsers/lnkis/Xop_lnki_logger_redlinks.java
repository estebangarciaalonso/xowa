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
package gplx.xowa.parsers.lnkis; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import gplx.xowa.dbs.tbls.*;
public class Xop_lnki_logger_redlinks {
	private ListAdp lnki_list = ListAdp_.new_(), work_list = ListAdp_.new_();
	private OrderedHash page_hash = OrderedHash_.new_bry_();
	private int lnki_idx;
	private boolean disabled = false;
	public void Page_bgn(Xop_ctx ctx) {
		lnki_idx = 1;	// NOTE: must start at 1; html_wtr checks for > 0
		lnki_list.Clear();
		disabled = ctx.Page().Page_ttl().Ns().Id_module();		// never redlink in Module ns; particularly since Lua has multi-line comments for [[ ]]
	}
	public void Wkr_exec(Xop_ctx ctx, Xop_lnki_tkn lnki) {
		if (disabled) return;
		Xoa_ttl ttl = lnki.Ttl();
		if (ttl == null) return; // occurs for invalid links
		Xow_ns ns = ttl.Ns();
		if (	ns.Id_file_or_media()							// ignore files which will usually not be in local wiki (most are in commons), and whose html is built up separately
			||	ns.Id_ctg()										// ignore ctgs which have their own html builder
			||	ns.Id_special()									// ignore special, especially Search; EX: Special:Search/Earth
			||	ttl.Anch_bgn() == Xoa_ttl.Anch_bgn_anchor_only	// anchor only link; EX: [[#anchor]]
			||	ttl.Wik_itm() != null							// xwiki lnki; EX: simplewiki links in homewiki; [[simplewiki:Earth]]
			)
			return;				
		lnki.Html_id_(lnki_idx);
		lnki_list.Add(lnki);
		++lnki_idx;
	}
	public void Redlink(Xow_wiki wiki, Xog_win win) {
		page_hash.Clear(); // NOTE: do not clear in Page_bgn, else will fail b/c of threading; EX: Open Page -> Preview -> Save; DATE:2013-11-17
		work_list .Clear();
		int len = lnki_list.Count();
		for (int i = 0; i < len; i++)	// make a copy of list else thread issues
			work_list.Add(lnki_list.FetchAt(i));
		for (int i = 0; i < len; i++) {
			Xop_lnki_tkn lnki = (Xop_lnki_tkn)work_list.FetchAt(i);
			Xoa_ttl ttl = lnki.Ttl();
			Xodb_page page = new Xodb_page().Ttl_(ttl);
			byte[] full_txt = ttl.Full_db();
			if (!page_hash.Has(full_txt))
				page_hash.Add(full_txt, page);
		}
		int page_len = page_hash.Count();
		for (int i = 0; i < page_len; i += Batch_size) {
			if (win.Gui_wtr().Canceled()) return;
			int end = i + Batch_size;
			if (end > page_len) end = page_len;
			wiki.Db_mgr().Load_mgr().Load_by_ttls(win.Gui_wtr(), page_hash, Xodb_page_tbl.Load_idx_flds_only_y, i, end);
		}
		for (int j = 0; j < len; j++) {
			if (win.Gui_wtr().Canceled()) return;
			Xop_lnki_tkn lnki = (Xop_lnki_tkn)work_list.FetchAt(j);
			byte[] full_txt = lnki.Ttl().Full_db();
			Xodb_page page = (Xodb_page)page_hash.Fetch(full_txt);
			if (page == null) continue;	// pages shouldn't be null, but just in case
			if (!page.Exists())
				win.Gui_wtr().Html_elem_atr_set_append(Lnki_id_prefix + Int_.XtoStr(lnki.Html_id()), "class", " new");
		}
	}
	public static final String Lnki_id_prefix = "xowa_lnki_";
	private static final int Batch_size = 32;
}
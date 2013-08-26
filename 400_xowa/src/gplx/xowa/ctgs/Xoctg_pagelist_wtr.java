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
package gplx.xowa.ctgs; import gplx.*; import gplx.xowa.*;
import gplx.xowa.users.history.*; import gplx.xowa.dbs.tbls.*;
public class Xoctg_pagelist_wtr {
	private Xoctg_pagelist_mgr pagelist_mgr = new Xoctg_pagelist_mgr();
	public Xoctg_pagelist_wtr Init_by_app(Xoa_app app) {pagelist_mgr.Init_by_app(app, this); return this;}
	public void Write(ByteAryBfr bfr, Xow_wiki wiki, Xoa_page page) {
		Xodb_page[] page_ary = wiki.Db_mgr().Load_mgr().Load_ctg_list(page.Category_list());
		Print_hidden(bfr, wiki, page_ary);
	}
	public void Print_hidden(ByteAryBfr bfr, Xow_wiki wiki, Xodb_page[] page_ary) {
		int len = page_ary.length;
		for (int i = 0; i < len; i++) {
			Xodb_page page = page_ary[i];
			Xodb_itm_category ctg_xtn = (Xodb_itm_category)page.Xtn();				
			Xoctg_pagelist_grp list = ctg_xtn != null && ctg_xtn.Hidden() ? pagelist_mgr.Grp_hidden() : pagelist_mgr.Grp_normal(); 
			list.Itms().Itms_add(page);
		}
		pagelist_mgr.Init_by_wiki(wiki);
		pagelist_mgr.XferAry(bfr, 0);
		pagelist_mgr.Grp_hidden().Itms().Itms_clear();
		pagelist_mgr.Grp_normal().Itms().Itms_clear();
	}
	public ByteAryFmtr Fmtr_all() {return fmtr_all;} private ByteAryFmtr fmtr_all = ByteAryFmtr.new_(String_.Concat_lines_nl
	( "<div id=\"catlinks\" class=\"catlinks\">~{grp_normal}~{grp_hidden}"
	, "</div>"
	), "grp_normal", "grp_hidden"
	);
	public ByteAryFmtr Fmtr_grp_normal() {return fmtr_grp_normal;} private ByteAryFmtr fmtr_grp_normal = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	( "" 
	, "  <div id=\"mw-normal-catlinks\" class=\"mw-normal-catlinks\">"
	, "    <a href=\"/wiki/~{ctg_help_page}\" title=\"~{ctg_help_page}\">~{ctg_text}</a>:"
	, "    <ul>~{grp_itms}"
	, "    </ul>"
	, "  </div>"
	), "ctg_help_page", "ctg_text", "grp_itms"
	);
	public ByteAryFmtr Fmtr_grp_hidden() {return fmtr_grp_hidden;} private ByteAryFmtr fmtr_grp_hidden = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	( "" 
	, "  <div id=\"mw-hidden-catlinks\" class=\"mw-hidden-catlinks mw-hidden-cats-user-shown\">~{hidden_ctg_txt}:"
	, "    <ul>~{grp_itms}"
	, "    </ul>"
	, "  </div>"
	), "hidden_ctg_txt", "grp_itms"
	);
	public ByteAryFmtr Fmtr_itm() {return fmtr_itm;} private ByteAryFmtr fmtr_itm = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	( ""
	, "      <li>"
	, "        <a~{lnki_cls} href=\"~{lnki_href}\" title=\"~{lnki_ttl}\">~{lnki_text}</a>"
	, "      </li>"
	), "lnki_cls", "lnki_href", "lnki_ttl", "lnki_text"
	);
}

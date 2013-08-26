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
package gplx.xowa.users.history; import gplx.*; import gplx.xowa.*; import gplx.xowa.users.*;
import gplx.xowa.specials.*;
public class Xou_history_html implements ByteAryFmtrArg, Xows_page {
	public ByteAryFmtr Html_grp() {return html_grp;} ByteAryFmtr html_grp = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	"<table class='sortable'>"
		,	"  <tr>"
		,	"    <th>page</th>"
		,	"    <th>wiki</th>"
		,	"    <th>views</th>"
		,	"    <th>time</th>"
		,	"  </tr>~{itms}"
		,	"</table>"
		), "itms");
	public ByteAryFmtr Html_itm() {return html_itm;} ByteAryFmtr html_itm = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	""
		,	"  <tr>"
		,	"    <td>[[~{itm_wiki}:~{itm_page}|~{itm_page}]]</td>"
		,	"    <td>~{itm_wiki}</td>"
		,	"    <td>~{itm_count}</td>"
		,	"    <td>~{itm_last}</td>"
		,	"  </tr>"
		), "itm_wiki", "itm_page", "itm_count", "itm_last");
	public void Special_gen(Xoa_url calling_url, Xoa_page page, Xow_wiki wiki, Xoa_ttl ttl) {
		this.app = wiki.App(); this.mgr = app.User().History_mgr();
		mgr.Sort();
		ByteAryBfr bfr = app.Utl_bry_bfr_mkr().Get_m001(); 
		html_grp.Bld_bfr_many(bfr, this);
		page.Data_raw_(bfr.Mkr_rls().XtoAryAndClear());
	}
	public void XferAry(ByteAryBfr bfr, int idx) {
		int len = mgr.Count();
		for (int i = 0; i < len; i++) {
			Xou_history_itm itm = mgr.Get_at(i);
			html_itm.Bld_bfr_many(bfr, itm.Wiki(), itm.Page(), itm.View_count(), itm.View_end().XtoStr_fmt_yyyy_MM_dd_HH_mm());
		}		
	}	Xou_history_mgr mgr; Xoa_app app;
}

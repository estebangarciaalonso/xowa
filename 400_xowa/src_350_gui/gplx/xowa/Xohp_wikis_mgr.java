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
import gplx.xowa.wikis.*;
public class Xohp_wikis_mgr implements GfoInvkAble {
	public Xohp_wikis_mgr(Xoa_app app) {this.app = app;} private Xoa_app app;
	public String Itms_as_html() {
		if (itms_as_html == null) {
			ByteAryBfr tmp_bfr = ByteAryBfr.new_(); // NOTE: do not use app.Utl_bry_bfr_mkr().Get_k004() as it is being used simultaneously by another caller; TODO: find call
			Xow_xwiki_mgr xwiki_mgr = app.User().Wiki().Xwiki_mgr();
			xwiki_mgr.Sort_by_key();
			int len = xwiki_mgr.Len();
			for (int i = 0; i < len; i++) {
				Xow_xwiki_itm itm = xwiki_mgr.Get_at(i);
				if (itm.Wiki_tid() == Xow_wiki_domain_.Tid_home) continue;// don't show home wiki
				itms_as_html_fmtr.Bld_bfr_many(tmp_bfr, itm.Domain());
			}
			itms_as_html = tmp_bfr.XtoStr();
		}
		return itms_as_html;
	}	String itms_as_html;
	public void Itms_refresh() {itms_as_html = null;}
	ByteAryFmtr itms_as_html_fmtr = ByteAryFmtr.new_("\n        <li><a href=\"/site/~{domain}/\">~{domain}</a></li>", "domain");
	public boolean Visible() {return visible;} private boolean visible = true;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_itms_as_html))		return this.Itms_as_html();
		else if	(ctx.Match(k, Invk_itms_refresh))		Itms_refresh();
		else if	(ctx.Match(k, Invk_visible))			return Yn.X_to_str(visible);
		else if	(ctx.Match(k, Invk_visible_))			visible = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_visible_toggle))		{visible = !visible; app.Gui_mgr().Main_win().Html_box().Html_js_eval_proc("xowa-portal-wikis-visible-toggle", Bool_.XtoStr_lower(visible));}
		else if	(ctx.Match(k, Invk_itms_as_html_fmtr_))	itms_as_html_fmtr.Fmt_(m.ReadBry("v"));
		else return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_visible = "visible", Invk_visible_ = "visible_", Invk_visible_toggle = "visible_toggle", Invk_itms_as_html = "itms_as_html", Invk_itms_as_html_fmtr_ = "itms_as_html_fmtr_", Invk_itms_refresh = "itms_refresh";
}

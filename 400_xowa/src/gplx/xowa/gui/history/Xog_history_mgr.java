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
package gplx.xowa.gui.history; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
public class Xog_history_mgr {
	private final OrderedHash hash = OrderedHash_.new_bry_(); private final Xog_history_stack stack = new Xog_history_stack();
	public int Count() {return hash.Count();}
	public Xoa_page Cur_page(Xow_wiki wiki) {return Get_or_fetch(wiki, stack.Cur_itm());}
	public Xoa_page Go_bwd(Xow_wiki wiki) {return Go_by_dir(wiki, Bool_.N);}
	public Xoa_page Go_fwd(Xow_wiki wiki) {return Go_by_dir(wiki, Bool_.Y);}
	public Xoa_page Go_by_dir(Xow_wiki wiki, boolean fwd) {
		Xog_history_itm itm = fwd ? stack.Go_fwd() : stack.Go_bwd();
		if (itm == Xog_history_itm.Null) return Xoa_page.Empty;
		Xoa_page rv = Get_or_fetch(wiki, itm);
		byte[] anch_key = itm.Anch();
		rv.Url().Anchor_bry_(anch_key); // must override anchor as it may be different for cached page
		rv.Html_data().Bmk_pos_(itm.Bmk_pos());
		return rv;
	}
	public void Add(Xoa_page page) {
		Xog_history_itm new_itm = Xog_history_mgr.new_(page);
		stack.Add(new_itm);
		byte[] page_key = Build_page_key(page);
		if (!hash.Has(page_key))
			hash.Add(page_key, page);
	}
	public void Update_html_doc_pos(Xoa_page page, byte history_nav_type) {
		Xog_history_itm itm = Get_recent(page, history_nav_type);
		if (itm != null) itm.Bmk_pos_(page.Html_data().Bmk_pos());
	}
	private Xog_history_itm Get_recent(Xoa_page page, byte history_nav_type) {
		int pos = -1;
		int list_pos = stack.Cur_pos();
		switch (history_nav_type) {
			case Xog_history_stack.Nav_fwd:			pos = list_pos - 1; break;
			case Xog_history_stack.Nav_bwd:			pos = list_pos + 1; break;
			case Xog_history_stack.Nav_by_anchor:	pos = list_pos; break;
		}
		if (pos < 0 || pos >= stack.Len()) return null;
		Xog_history_itm recent = stack.Get_at(pos);
		Xog_history_itm page_itm = Xog_history_mgr.new_(page);
		return page_itm.Eq_wo_bmk_pos(recent) ? recent : null;	// check that recent page actually matches current; DATE:2014-05-10
	}
	private Xoa_page Get_or_fetch(Xow_wiki wiki, Xog_history_itm itm) {
		byte[] page_key = Build_page_key(itm.Wiki(), itm.Page(), itm.Qarg());
		Xoa_page rv = (Xoa_page)hash.Fetch(page_key);
		if (rv != null) return rv;
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, itm.Page());
		return wiki.Data_mgr().Get_page(ttl, false);
	}
	private static byte[] Build_page_key(Xoa_page page) {return Build_page_key(page.Wiki().Domain_bry(), page.Ttl().Full_url(), page.Url().Args_all_as_bry());}
	private static byte[] Build_page_key(byte[] wiki_key, byte[] page_key, byte[] args_key) {return Bry_.Add_w_dlm(Byte_ascii.Pipe, wiki_key, page_key, args_key);}
	public static Xog_history_itm new_(Xoa_page pg) {
		byte[] wiki = pg.Wiki().Domain_bry();
		byte[] page = pg.Ttl().Full_url();		// get page_name only (no anchor; no query args)
		byte[] anch = pg.Url().Anchor_bry();
		byte[] qarg = pg.Url().Args_all_as_bry();
		boolean redirect_force = pg.Url().Redirect_force();
		String bmk_pos = pg.Html_data().Bmk_pos();
		if (bmk_pos == null) bmk_pos = Xog_history_itm.Html_doc_pos_toc;	// never allow null doc_pos; set to top
		return new Xog_history_itm(wiki, page, anch, qarg, redirect_force, bmk_pos);
	}
}

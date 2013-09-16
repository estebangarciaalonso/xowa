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
import gplx.gfui.*; import gplx.xowa.gui.history.*;
public class Xog_win_view_adp extends Xog_win_view_base {
	public void Init(Xog_win win_mgr, Xoa_app app, GfuiWin win, GfuiTextBox url, GfuiTextBox info, GfuiTextBox find, Gfui_html html) {
		this.win_mgr = win_mgr;
		this.Ctor(app);
		page_stack = win_mgr.History_mgr();
		win_box = new Gfui_box_win_adp(win);
		url_box = new Gfui_box_text_adp(url, win_box);
		find_box = new Gfui_box_text_adp(find, win_box);
		prog_box = new Gfui_box_text_adp(info, win_box);
		html_box = new Gfui_box_html_adp(html, win_box);
	}	private Xog_win win_mgr;
	@Override public Xoa_page Page() {return win_mgr.Page();} @Override public void Page_(Xoa_page v) {win_mgr.Page_(v);}
	@Override public Xog_history_mgr History_mgr() {return page_stack;} private Xog_history_mgr page_stack;
	@Override public Gfui_box_win Win_box() {return win_box;} Gfui_box_win win_box;
	@Override public Xog_box_html Html_box() {return html_box;} Xog_box_html html_box;
	@Override public Gfui_box_text Url_box() {return url_box;} Gfui_box_text url_box;
	@Override public Gfui_box_text Prog_box() {return prog_box;} Gfui_box_text prog_box;
	@Override public Gfui_box_text Find_box() {return find_box;} Gfui_box_text find_box;
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		return win_mgr.Invk(ctx, ikey, k, m);
	}
	@Override public void Exec_async(String cmd) {win_mgr.Exec_async(cmd);}
	@Override public void Exec_sync(String cmd) {win_mgr.Exec_sync(cmd);}
}
class Gfui_box_win_adp implements Gfui_box_win {	
	public Gfui_box_win_adp(GfuiWin under) {this.under = under;} GfuiWin under;
	public String Id() {return under.Key_of_GfuiElem();}
	public Gfui_box_grp Grp() {return null;}
	public String Text() {return under.Text();} public void Text_(String v) {under.Text_(v);}
	public Gfui_box Focus_box() {
		GfuiElem elem = under.SubElems().FetchAt(under.Focus_idx());	// ASSUME: one level of subs only; otherwise, recursively search
		return Subs_get_by(elem.Key_of_GfuiElem());
	}
	public void Focus_box_(Gfui_box box) {box.Focus();}
	public void Focus() {under.Focus();}
	public int Subs_len() {return hash.Count();} private OrderedHash hash = OrderedHash_.new_();
	public Gfui_box Subs_get_at(int i) {return (Gfui_box)hash.FetchAt(i);}
	public Gfui_box Subs_get_by(String key) {return (Gfui_box)hash.Fetch(key);}
	public void Subs_add(Gfui_box sub) {hash.Add(sub.Id(), sub);}
}
class Gfui_box_text_adp implements Gfui_box_text {
	public Gfui_box_text_adp(GfuiTextBox under, Gfui_box_grp grp) {this.under = under; this.grp = grp;} GfuiTextBox under;
	public String Id() {return under.Key_of_GfuiElem();}
	public Gfui_box_grp Grp() {return grp;} Gfui_box_grp grp;
	public String Text() {return under.Text();} public void Text_(String v) {under.Text_(v);}
	public FontAdp Font() {return under.TextMgr().Font();} public void Font_(FontAdp v) {under.TextMgr().Font_(v);} 
	public void Focus() {under.Focus();}
}
class Gfui_box_html_adp implements Xog_box_html {
	public Gfui_box_html_adp(Gfui_html under, Gfui_box_grp grp) {this.under = under; this.grp = grp;} Gfui_html under;
	public String Id() {return under.Key_of_GfuiElem();}
	public Gfui_box_grp Grp() {return grp;} Gfui_box_grp grp;
	public String Text() {return under.Text();} public void Text_(String v) {under.Text_(v);}
	public FontAdp Font() {return under.TextMgr().Font();} public void Font_(FontAdp v) {under.TextMgr().Font_(v);}
	public String Html_elem_atr_get_str(String elem_id, String atr_key) {return under.Html_elem_atr_get_str(elem_id, atr_key);}
	public boolean Html_elem_atr_get_bool(String elem_id, String atr_key) {return under.Html_elem_atr_get_bool(elem_id, atr_key);}
	public void Focus() {under.Focus();}
	public void Html_doc_body_focus() {under.Html_doc_body_focus();}
}

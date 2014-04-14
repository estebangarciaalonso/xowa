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
package gplx.gfui; import gplx.*;
class Mem_html extends GxwTextMemo_lang implements Gxw_html {		public String Html_doc_html() {
		return String_.Replace(this.TextVal(), "\r\n", "\n");
	}
	public String Html_doc_selected_get(String host, String page) {return "";}
	public boolean Html_window_print_preview() {return false;}
	public void Html_invk_src_(GfoEvObj v) {}
	public String Html_elem_atr_get_str(String elem_id, String atr_key) {
		if		(String_.Eq(atr_key, Gfui_html.Atr_value))	return String_.Replace(this.TextVal(), "\r\n", "\n");
		else													throw Err_.unhandled(atr_key);
	}
	public Object Html_elem_atr_get_obj(String elem_id, String atr_key) {
		if		(String_.Eq(atr_key, Gfui_html.Atr_value))	return String_.Replace(this.TextVal(), "\r\n", "\n");
		else													throw Err_.unhandled(atr_key);
	}
	public boolean Html_elem_atr_get_bool(String elem_id, String atr_key) {
		if		(String_.Eq(atr_key, Gfui_html.Atr_value))	return Bool_.parse_(String_.Replace(this.TextVal(), "\r\n", "\n"));
		else													throw Err_.unhandled(atr_key);
	}
	public boolean Html_elem_atr_set(String elem_id, String atr_key, String v) {
		if		(String_.Eq(atr_key, Gfui_html.Atr_value))		this.TextVal_set(v);
		else													throw Err_.unhandled(atr_key);
		return true;
	}
	public boolean Html_elem_atr_set_append(String elem_id, String atr_key, String append) {
		if		(String_.Eq(atr_key, Gfui_html.Atr_value))		this.TextVal_set(this.TextVal() + append);
		else													throw Err_.unhandled(atr_key);
		return true;
	}
	public void Html_doc_html_(String s) {
//			this.Core().ForeColor_set(plainText ? ColorAdp_.Black : ColorAdp_.Gray);
		s = String_.Replace(s, "\r", "");
		s = String_.Replace(s, "\n", "\r\n");
		this.TextVal_set(s);
		this.SelBgn_set(0);
	}
	public String Html_active_atr_get_str(String atrKey, String or) { // NOTE: fuzzy way of finding current href; EX: <a href="a">b</a>
		String txt = this.TextVal();
		int pos = this.SelBgn();
		String rv = ExtractAtr(atrKey, txt, pos);
		return rv == null ? or : rv;
	}
	public void Html_doc_body_focus() {}
	public String Html_window_vpos() {return "";}
	public boolean Html_window_vpos_(String v) {return true;}
	public boolean Html_elem_focus(String v) {return true;}
	public boolean Html_elem_img_update(String elem_id, String elem_src, int elem_width, int elem_height) {return true;}
	public boolean Html_elem_delete(String elem_id) {return true;}
	public boolean Html_elem_replace_html(String id, String html) {return true;}
	public boolean Html_gallery_packed_exec() {return true;}
	public String Html_js_eval_script(String script) {return "";}
	String ExtractAtr(String key, String txt, int pos) {
		int key_pos = String_.FindBwd(txt, key, pos);	if (key_pos == String_.NotFound) return null;
		int q0 = String_.FindFwd(txt, "\"", key_pos);	if (q0 == String_.NotFound) return null;
		int q1 = String_.FindFwd(txt, "\"", q0 + 1);	if (q1 == String_.NotFound) return null;
		if (!Int_.Between(pos, q0, q1)) return null;	// current pos is not between nearest quotes
		return String_.Mid(txt, q0 + 1, q1);
	}
	public boolean Html_doc_find(String elem_id, String find, boolean dir_fwd, boolean case_match, boolean wrap_find) {
//			String txt = this.TextVal();
//			int pos = this.SelBgn();
//			int bgn = String_.FindFwd(txt, find, pos);	if (bgn == String_.NotFound) return false;
//			if (bgn == pos) {
//				bgn = String_.FindFwd(txt, find, pos + 1);
//				if (bgn == String_.NotFound) {
//					bgn = String_.FindFwd(txt, find, 0);
//					if (bgn == String_.NotFound) return false;
//				}
//			}
//			this.SelBgn_set(bgn);
//			this.SelLen_set(String_.Len(find));
//			this.ScrollTillSelectionStartIsFirstLine();
		txtFindMgr.Text_(this.TextVal());
		int cur = this.SelBgn();
		int[] ary = txtFindMgr.FindByUi(find, this.SelBgn(), this.SelLen(), false);
		if (ary[0] != cur) {
			this.SelBgn_set(ary[0]);
			this.SelLen_set(ary[1]);
			this.ScrollTillCaretIsVisible();
		}
		else {
			ary = txtFindMgr.FindByUi(find, this.SelBgn() + 1, 0, false);
			if (ary[0] != 0) {
				this.SelBgn_set(ary[0]);
				this.SelLen_set(ary[1]);
				this.ScrollTillCaretIsVisible();
//					this.ScrollTillSelectionStartIsFirstLine();
			}
		}
		return true;
	}
	public boolean Html_elem_scroll_into_view(String id) {return false;}
	public void Html_js_enabled_(boolean v) {}
	public void Html_js_eval_proc(String proc, String... args) {}
	public void Html_js_cbks_add(String js_func_name, GfoInvkAble invk) {}
	private TxtFindMgr txtFindMgr = new TxtFindMgr();
	public Mem_html() {
		this.ctor_MsTextBoxMultiline_();
	}
}

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
package gplx.gfui; import gplx.*;
public class Gfui_kit_base implements Gfui_kit {
	public byte Tid() {return tid;} public Gfui_kit_base Tid_(byte v) {this.tid = v; return this;} private byte tid = Gfui_kit_.TypeId_default;
	public Gfui_kit Main_win_(GfuiWin v) {main_win = v; return this;} GfuiWin main_win;
	public void Cfg_set(String type, String key, Object val) {}
	public boolean Kit_init_done() {return true;}
	public void Kit_init(Gfo_usr_dlg gui_wtr) {}
	public void Kit_run() {GfuiEnv_.Run(main_win);}
	public void Kit_term() {term_cbk.Invk(); GfuiEnv_.Exit();}
	public void	Kit_term_cbk_(GfoInvkAbleCmd v) {term_cbk = v;} GfoInvkAbleCmd term_cbk;
	public void Ask_ok(String grp_key, String msg_key, String fmt, Object... args) {GfuiEnv_.ShowMsg(ask_fmtr.Bld_str_many(ask_bfr, fmt, args));} ByteAryFmtr ask_fmtr = ByteAryFmtr.new_(); ByteAryBfr ask_bfr = ByteAryBfr.new_();
	public boolean Ask_yes_no(String grp_key, String msg_key, String fmt, Object... args) {return false;}
	public int Ask_yes_no_cancel(String grp_key, String msg_key, String fmt, Object... args) {return Gfui_dlg_msg_.Btn_cancel;}
	public boolean Ask_ok_cancel(String grp_key, String msg_key, String fmt, Object... args) {return false;}
	public Gfui_clipboard Clipboard() {return Gfui_clipboard_null.Null;}
	public void Btn_img_(GfuiBtn btn, IconAdp v) {}
	public GfuiInvkCmd New_cmd_sync(GfoInvkAble invk) {return new Base_GfuiInvkCmd_sync(invk);}
	public GfuiInvkCmd New_cmd_async(GfoInvkAble invk) {return new Base_GfuiInvkCmd_async(invk);}
	public GfuiWin New_win_app(String key, KeyVal... args) {
		GfuiWin rv = GfuiWin_.kit_(this, key, GxwWin_lang.new_(), ctor_args);
		main_win = rv;
		return rv;
	}
	public GfuiWin New_win_utl(String key, GfuiWin owner, KeyVal... args) {return GfuiWin_.kit_(this, key, GxwWin_lang.new_(), ctor_args);}
	public Gfui_html New_html_box(String key, GfuiElem owner, KeyVal... args) {
		Gfui_html rv = Gfui_html.kit_(this, key, new Base_HtmlBox(), ctor_args);
		owner.SubElems().Add(rv);
		return rv;
	}
	public GfuiTextBox New_text_box(String key, GfuiElem owner, KeyVal... args) {
		GfuiTextBox rv = GfuiTextBox_.kit_(this, key, GxwTextBox_lang_.fld_(), ctor_args);
		owner.SubElems().Add(rv);
		return rv;
	}
	public GfuiBtn New_btn(String key, GfuiElem owner, KeyVal... args) {
		GfuiBtn rv = GfuiBtn_.kit_(this, key, new GxwElem_lang(), ctor_args);
		owner.SubElems().Add(rv);
		return rv;
	}
	public GfuiStatusBox New_status_box(String key, GfuiElem owner, KeyVal... args) {
		GfuiStatusBox rv = GfuiStatusBox_.kit_(this, key, GxwTextBox_lang_.memo_());
		owner.SubElems().Add(rv);
		return rv;
	}
	public Gfui_dlg_file New_dlg_file(String msg) {return Gfui_dlg_file_null._;}
	public Gfui_dlg_msg New_dlg_msg(String msg) {return Gfui_dlg_msg_null._;}
	public Gfui_mnu_grp New_mnu_popup(GfuiElem owner) {return Gfui_mnu_grp_null.Null;}
	public Gfui_mnu_grp New_mnu_bar(GfuiWin owner) {return Gfui_mnu_grp_null.Null;}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		return this;
	}
	public Object New_color(int a, int r, int g, int b) {return null;}
	public ImageAdp New_img_load(Io_url url) {return ImageAdp_.file_(url);}
	public float Calc_font_height(GfuiElem elem, String s) {return 13;}
	KeyValHash ctor_args = KeyValHash.new_();
        public static final Gfui_kit_base _ = new Gfui_kit_base(); Gfui_kit_base() {}
}
class Base_GfuiInvkCmd_sync implements GfuiInvkCmd {
	public Base_GfuiInvkCmd_sync(GfoInvkAble target) {this.target = target;} GfoInvkAble target;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		return target.Invk(ctx, ikey, k, m);
	}
	public void Rls() {target = null;}
}
class Base_GfuiInvkCmd_async implements GfuiInvkCmd {
	public void Rls() {target = null;}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		return target.Invk(ctx, ikey, k, m);
	}
	public Base_GfuiInvkCmd_async(GfoInvkAble target) {this.target = target;} GfoInvkAble target;
}
class Base_HtmlBox extends GxwTextMemo_lang implements Gxw_html {		public String Html_doc_html() {
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
		if		(String_.Eq(atr_key, Gfui_html.Atr_value))	this.TextVal_set(v);
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
	public String Html_js_eval_script(String script) {return "";}
	String ExtractAtr(String key, String txt, int pos) {
		int key_pos = String_.FindBwd(txt, key, pos);	if (key_pos == String_.NotFound) return null;
		int q0 = String_.FindFwd(txt, "\"", key_pos);	if (q0 == String_.NotFound) return null;
		int q1 = String_.FindFwd(txt, "\"", q0 + 1);	if (q1 == String_.NotFound) return null;
		if (!Int_.Between(pos, q0, q1)) return null;	// current pos is not between nearest quotes
		return String_.MidByPos(txt, q0 + 1, q1);
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
	TxtFindMgr txtFindMgr = new TxtFindMgr();
	public Base_HtmlBox() {
		this.ctor_MsTextBoxMultiline_();
	}
}

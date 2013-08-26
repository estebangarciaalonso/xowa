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
package gplx.xowa; import gplx.*;
import gplx.gfui.*;
public interface Xog_win_wtr extends Gfo_usr_dlg {
	void Ini(Gfui_kit kit, Xog_win win);
	void Info_box_clear(boolean clear_text);
	void Html_img_update(String elem_id, String elem_src, int elem_width, int elem_height);
	void Html_atr_set(String elem_id, String atr_key, String atr_val);
	void Html_elem_delete(String elem_id);
}
class Xog_win_wtr_null implements Xog_win_wtr {
	public Gfo_usr_dlg_ui Ui_wkr() {return ui_wkr;} public void Ui_wkr_(Gfo_usr_dlg_ui v) {ui_wkr = v;} Gfo_usr_dlg_ui ui_wkr = Gfo_usr_dlg_ui_.Null;
	public Gfo_log_wtr Log_wtr() {return log_wtr;} public void Log_wtr_(Gfo_log_wtr v) {log_wtr = v;} Gfo_log_wtr log_wtr = Gfo_log_wtr_.Null;
	public boolean Canceled() {return canceled;} public void Canceled_y_() {canceled = true;} public void Canceled_n_() {canceled = false;} private boolean canceled;
	public void Clear() {canceled = false;}
	public void Ini(gplx.gfui.Gfui_kit kit, Xog_win win) {}
	public void Log_text(String v) {}
	public String Log_many(String grp_key, String msg_key, String fmt, Object... args) {return "";}
	public String Warn_many(String grp_key, String msg_key, String fmt, Object... args) {return "";}
	public String Prog_many(String grp_key, String msg_key, String fmt, Object... args) {return "";}
	public String Prog_none(String grp_key, String msg_key, String fmt) {return "";}
	public String Prog_direct(String msg) {return "";}
	public String Log_direct(String msg) {return "";}
	public String Note_many(String grp_key, String msg_key, String fmt, Object... args) {return "";}
	public String Note_none(String grp_key, String msg_key, String fmt) {return "";}
	public String Prog_one(String grp_key, String msg_key, String fmt, Object arg) {return "";}
	public Err Fail_many(String grp_key, String msg_key, String fmt, Object... args) {return Err_mgr._.fmt_(grp_key, msg_key, fmt, args);}
	public void Info_box_clear(boolean clear_text) {}
	public void Html_img_update(String elem_id, String elem_src, int elem_width, int elem_height) {}
	public void Html_atr_set(String elem_id, String atr_key, String atr_val) {}
	public void Html_elem_delete(String elem_id) {}
	public static final Xog_win_wtr_null _ = new Xog_win_wtr_null(); Xog_win_wtr_null() {}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {return this;}
}

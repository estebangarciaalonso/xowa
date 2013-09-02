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
import gplx.gfui.*;
public class Gfo_usr_dlg_xowa extends Gfo_usr_dlg_base implements GfoInvkAble, Xog_win_wtr {
	public void Ini(Gfui_kit kit, Xog_win win) {
		cmd_async	= kit.New_cmd_async(this);	// NOTE: cmd_async needed for prog/warn else will block indefinitely when cancelling download
		cmd_sync	= kit.New_cmd_sync(this);	// NOTE: cmd_sync needed for Html_updates
		this.win = win;
		this.prog_box = win.Prog_box();
		this.info_box = win.Info_box();
		this.Log_wtr_(win.App().Log_wtr());
	}	GfuiInvkCmd cmd_async, cmd_sync; GfuiTextBox prog_box; GfuiTextBox info_box; Xog_win win;
	@Override public void Clear() {info_box.Text_(""); prog_box.Text_(""); this.Canceled_n_(); Ui_wkr().Clear();}
	public void Info_box_clear(boolean clear_text) {Ui_wkr().Clear();}
	public void Html_img_update(String elem_id, String elem_src, int elem_width, int elem_height) {
		GfoMsg m = GfoMsg_.new_cast_(Invk_html_img_update).Add("elem_id", elem_id).Add("elem_src", elem_src).Add("elem_width", elem_width).Add("elem_height", elem_height);
		GfoInvkAble_.InvkCmd_msg(cmd_sync, Invk_html_img_update, m);
	}	Object guard = new Object();
	public void Html_elem_delete(String elem_id) {
		GfoMsg m = GfoMsg_.new_cast_(Invk_html_elem_delete).Add("elem_id", elem_id);
		GfoInvkAble_.InvkCmd_msg(cmd_sync, Invk_html_elem_delete, m);
	}
	public void Html_atr_set(String elem_id, String atr_key, String atr_val) {
		GfoMsg m = GfoMsg_.new_cast_(Invk_html_elem_atr_set).Add("elem_id", elem_id).Add("atr_key", atr_key).Add("atr_val", atr_val);
		GfoInvkAble_.InvkCmd_msg(cmd_sync, Invk_html_elem_atr_set, m);
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_html_img_update))	win.Html_box().Html_elem_img_update(m.ReadStr("elem_id"), m.ReadStr("elem_src"), m.ReadInt("elem_width"), m.ReadInt("elem_height"));
		else if	(ctx.Match(k, Invk_html_elem_atr_set))  win.Html_box().Html_elem_atr_set(m.ReadStr("elem_id"), m.ReadStr("atr_key"), m.ReadStr("atr_val"));
		else if	(ctx.Match(k, Invk_html_elem_delete))	win.Html_box().Html_elem_delete(m.ReadStr("elem_id"));
		else	return super.Invk(ctx, ikey, k, m);
		return this;
	}	private static final String Invk_html_img_update = "html_img_update", Invk_html_elem_atr_set = "html_elem_atr_set", Invk_html_elem_delete = "html_elem_delete";
	public static Gfo_usr_dlg_xowa console_() {
		Gfo_usr_dlg_xowa rv = new Gfo_usr_dlg_xowa();
		rv.Ui_wkr_(Gfo_usr_dlg_ui_.Console);
		rv.Log_wtr_(new Gfo_log_wtr_base());
		rv.Log_wtr().Queue_enabled_(true);
		return rv;
	}
	public static Gfo_usr_dlg_xowa test_xowa_() {
		if (Test == null) {
			Test = new Gfo_usr_dlg_xowa();
			Test.Ui_wkr_(Gfo_usr_dlg_ui_.Test);
			Test.Log_wtr_(Gfo_log_wtr_.Null);
		}
		return Test;
	}	static Gfo_usr_dlg_xowa Test;
}

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
class Swt_popup_grp implements Gfui_mnu_grp {
	private Decorations owner_win; private Control owner_box; private boolean menu_is_bar = false;
	Swt_popup_grp(){}
	public Menu Under_menu() {return menu;} private Menu menu;
	public Object Under() {return menu;}
	@Override public void Itms_clear() {
		menu.dispose();
		if (menu_is_bar) {
			menu = new Menu(owner_win, SWT.BAR);
			owner_win.setMenuBar(menu);
		}
		else {
			menu = new Menu(owner_box);
			owner_box.setMenu(menu);		
		}
	}
	@Override public Gfui_mnu_itm Itms_add_cmd(String txt, ImageAdp img, GfoInvkAble invk, String cmd) {
		Swt_popup_itm itm = new Swt_popup_itm(menu);
		itm.Text_(txt);
		if (img != null) itm.Img_(img);
		itm.Invk_set_(invk, cmd);
		return itm;
	}
	@Override public Gfui_mnu_itm Itms_add_msg(String txt, ImageAdp img, GfoInvkAble invk, GfoMsg invk_msg, GfoInvkRootWkr root_wkr) {
		Swt_popup_itm itm = new Swt_popup_itm(menu);
		itm.Text_(txt);
		if (img != null) itm.Img_(img);
		itm.Invk_set_(root_wkr, invk, invk_msg);
		return itm;
	}
	@Override public Gfui_mnu_itm Itms_add_separator() {
		new MenuItem(menu, SWT.SEPARATOR);
		return null;
	}
	@Override public Gfui_mnu_grp Itms_add_grp(String txt, ImageAdp img) {
		Swt_popup_itm itm = new Swt_popup_itm(menu, SWT.CASCADE);
		if (img != null) itm.Img_(img);
		itm.Text_(txt);

		Swt_popup_grp grp = new_grp(owner_win);
		itm.Text_(txt);
		
		itm.Under_itm().setMenu(grp.Under_menu());
		return grp;
	}
	public static Swt_popup_grp new_popup(GfuiElem owner_elem) 	{
		Swt_popup_grp rv = new Swt_popup_grp();
		Shell owner_win = cast_to_shell(owner_elem.OwnerWin());
		Control owner_box = ((Swt_control)owner_elem.UnderElem()).Under_control();	// HACK: need GxwElem.Under; for now, cast to Swt_html
		rv.owner_win = owner_win; rv.owner_box = owner_box;
		rv.menu = new Menu(owner_box);
		owner_box.setMenu(rv.menu);
		return rv;
	} 
	private static Swt_popup_grp new_grp(Decorations owner_win) {
		Swt_popup_grp rv = new Swt_popup_grp();
		rv.owner_win = owner_win;
		rv.menu = new Menu(owner_win, SWT.DROP_DOWN);
		return rv;
	}
	public static Swt_popup_grp new_bar(GfuiWin win) {
		Swt_popup_grp rv = new Swt_popup_grp();
		Shell owner_win = cast_to_shell(win);
		rv.owner_win = owner_win;
		rv.menu_is_bar = true;
		rv.menu = new Menu(owner_win, SWT.BAR);
		owner_win.setMenuBar(rv.menu);
		return rv;
	}
	private static Shell cast_to_shell(GfuiWin win) {
		return ((Swt_win)win.UnderElem()).UnderShell();
	}
}
class Swt_popup_itm implements Gfui_mnu_itm {
	private Menu menu; private ImageAdp img;
	public Swt_popup_itm(Menu menu) {this.menu = menu; itm = new MenuItem(menu, SWT.NONE);}
	public Swt_popup_itm(Menu menu, int type) {this.menu = menu; itm = new MenuItem(menu, type);}
	public MenuItem Under_itm() {return itm;} private MenuItem itm; 
	public Object Under() {return menu;}
	public String Text() {return itm.getText();} public Swt_popup_itm Text_(String v) {text = v; itm.setText(v); return this;} String text;
	public ImageAdp Img() {return img;} public Swt_popup_itm Img_(ImageAdp v) {
		img = v; 
		if (v != ImageAdp_.Null)
			itm.setImage((Image)v.Under());
		return this;
	}
	public void Invk_set_(GfoInvkAble invk, String cmd) {
		itm.addListener(SWT.Selection, new Swt_lnr_invk_cmd(invk, cmd));
	}
	public void Invk_set_(GfoInvkRootWkr root_wkr, GfoInvkAble invk, GfoMsg msg) {
		itm.addListener(SWT.Selection, new Swt_lnr_invk_msg(root_wkr, invk, msg));
	}
}
class Swt_lnr_invk_cmd implements Listener {
	public Swt_lnr_invk_cmd(GfoInvkAble invk, String cmd) {this.invk = invk; this.cmd = cmd;} GfoInvkAble invk; String cmd;
	public void handleEvent(Event ev) {
		try {
			GfoInvkAble_.InvkCmd(invk, cmd);
		} catch (Exception e) {Swt_kit._.Ask_ok("", "", "error while invoking command: cmd=~{0} err=~{1}", cmd, Err_.Message_gplx_brief(e));}
	}	
}
class Swt_lnr_invk_msg implements Listener {
	private GfoInvkRootWkr root_wkr; private GfoInvkAble invk; private GfoMsg msg;;
	public Swt_lnr_invk_msg(GfoInvkRootWkr root_wkr, GfoInvkAble invk, GfoMsg msg) {this.root_wkr = root_wkr; this.invk = invk; this.msg = msg;}
	public void handleEvent(Event ev) {
		try {
			msg.Args_reset();
			root_wkr.Run_str_for(invk, msg);
		} catch (Exception e) {Swt_kit._.Ask_ok("", "", "error while invoking command: cmd=~{0} err=~{1}", msg.Key(), Err_.Message_gplx_brief(e));}
	}	
}

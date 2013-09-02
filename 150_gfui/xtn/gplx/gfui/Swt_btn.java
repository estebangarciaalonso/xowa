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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
class Swt_btn implements GxwElem, Swt_control {
	@Override public Control Under_control() {return btn;}
//	@Override public int SelBgn() {return textBox.getCaretPosition();} 	@Override public void SelBgn_set(int v) {textBox.setSelection(v);}
//	@Override public int SelLen() {return textBox.getSelectionCount();} @Override public void SelLen_set(int v) {textBox.setSelection(this.SelBgn(), this.SelBgn() + v);}	
	@Override public String TextVal() {return btn.getText();} @Override public void TextVal_set(String v) {btn.setText(v);}
	@Override public GxwCore_base Core() {return core;} GxwCore_base core;
	@Override public GxwCbkHost Host() {return host;} @Override public void Host_set(GxwCbkHost host) {this.host = host;} GxwCbkHost host;
	@Override public void EnableDoubleBuffering() {}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		return null;
	}
//	Text textBox;
	Button btn;
	public Swt_btn(GxwElem ownerElem, KeyValHash ctorArgs) {
		Composite owner = ((Swt_win)ownerElem).UnderShell();
//		if (ctorArgs.Has(GfuiTextBox_.Ctor_Memo)) {
//			textBox = new Text(owner, SWT.MULTI | SWT.WRAP);
//		}
//		else {
//			textBox = new Text(owner, SWT.NONE);
//		}	
		btn = new Button(owner, SWT.FLAT | SWT.PUSH);
		core = new Swt_core_cmds(btn);
		btn.addKeyListener(new Swt_KeyLnr(this));
		btn.addMouseListener(new Swt_MouseLnr(this));
	}

//	@Override public boolean Border_on() {return false;} @Override public void Border_on_(boolean v) {} // SWT_TODO:borderWidth doesn't seem mutable
//	@Override public void CreateControlIfNeeded() {}
//	@Override public boolean OverrideTabKey() {return false;} @Override public void OverrideTabKey_(boolean v) {}
}

class Swt_btn_no_border implements GxwElem, Swt_control {
	public Swt_btn_no_border(GxwElem owner_elem, KeyValHash ctorArgs) {
		Composite owner = ((Swt_win)owner_elem).UnderShell();
		Make_btn_no_border(owner.getDisplay(), owner.getShell(), owner);
		core = new Swt_core_cmds(box_btn);
		box_btn.addKeyListener(new Swt_KeyLnr(this));
		box_btn.addMouseListener(new Swt_MouseLnr(this));
	}
	@Override public Control Under_control() {return box_btn;}
	@Override public String TextVal() {return box_btn.getText();} @Override public void TextVal_set(String v) {box_btn.setText(v);}
	@Override public GxwCore_base Core() {return core;} Swt_core_cmds core;
	@Override public GxwCbkHost Host() {return host;} @Override public void Host_set(GxwCbkHost host) {this.host = host;} GxwCbkHost host;
	@Override public void EnableDoubleBuffering() {}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, GfuiBtn.Invk_btn_img))	return btn_img;
		else if	(ctx.Match(k, GfuiBtn.Invk_btn_img_))	Btn_img_((ImageAdp)m.CastObj("v"));
		return null;
	}
	void Btn_img_(ImageAdp v) {
		if (box_btn == null || v == null) return;
		SizeAdp size = core.Size();
		int dif = 6;
		box_btn.setImage((Image)v.Resize(size.Width() - dif, size.Height() - dif).Under());
	}
	ImageAdp btn_img;
	Composite box_grp;
	Label box_btn;
	void Make_btn_no_border(Display display, Shell shell, Control owner) {
		box_grp = new Composite(shell, SWT.FLAT);
		box_btn = new Label(shell, SWT.FLAT);
		box_btn.setSize(25, 25);
		box_btn.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		box_btn.addFocusListener(new Swt_clabel_lnr_focus(box_grp));
	}
}
class Swt_clabel_lnr_focus implements FocusListener {
	public Swt_clabel_lnr_focus(Control v) {this.surrogate = v;} Control surrogate;
	@Override public void focusGained(org.eclipse.swt.events.FocusEvent e) {
		surrogate.forceFocus();
	}
	@Override public void focusLost(org.eclipse.swt.events.FocusEvent arg0) {}
}
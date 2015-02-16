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
package gplx.gfui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import gplx.GfoInvkAbleCmd;
import gplx.GfoMsg;
import gplx.GfsCtx;
import gplx.Io_url_;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
class Swt_win implements GxwWin, Swt_control {
	public Display UnderDisplay() {return display;} private Display display;
	public Shell UnderShell() {return shell;} private Shell shell;
	@Override public Control Under_control() {return shell;}
	@Override public Composite Under_composite() {return shell;}
	@Override public Control Under_menu_control() {return shell;}
	public Swt_win(Shell owner) 		{ctor(new Shell(owner, SWT.RESIZE | SWT.DIALOG_TRIM), owner.getDisplay());}
	public Swt_win(Display display) 	{ctor(new Shell(display), display);	}
	Swt_lnr_show showLnr;	// use ptr to dispose later
	void ctor(Shell shell, Display display) {
		this.shell = shell;		
		this.display = display;
		ctrlMgr = new Swt_core_cmds(shell);
		showLnr = new Swt_lnr_show(this);
		resizeLnr = new Swt_lnr_resize(this);
		shell.addListener(SWT.Show, showLnr);
		shell.addListener(SWT.Resize, resizeLnr);
	}
	Swt_lnr_resize resizeLnr;
	public void ShowWin() 		{shell.setVisible(true);}
	public void HideWin() 		{shell.setVisible(false);}
	public boolean Maximized() 	{return shell.getMaximized();} public void Maximized_(boolean v) {shell.setMaximized(v);}
	public boolean Minimized() 	{return shell.getMinimized();} public void Minimized_(boolean v) {shell.setMinimized(v);}
	public void CloseWin() 		{shell.close();}
	public boolean Pin() 		{return pin;}
	public void Pin_set(boolean val) {
	//	shell.setAlwaysOnTop(val);
		pin = val;
	} 	boolean pin = false;
	public IconAdp IconWin() {return icon;} IconAdp icon;
	public void IconWin_set(IconAdp i) {
		if (i == null || i.Url() == Io_url_.Null) return;
		icon = i;
        Image image = null;
        try {
          image = new Image(display, new FileInputStream(i.Url().Xto_api()));
        } catch (FileNotFoundException e1) {e1.printStackTrace();}
		shell.setImage(image);
	}
	public void OpenedCmd_set(GfoInvkAbleCmd v) {whenLoadedCmd = v;} GfoInvkAbleCmd whenLoadedCmd = GfoInvkAbleCmd.Null;
	public void Opened() {whenLoadedCmd.Invk();}
	public GxwCore_base Core() {return ctrlMgr;} GxwCore_base ctrlMgr;
	public GxwCbkHost Host() {return host;} public void Host_set(GxwCbkHost host) {this.host = host;} GxwCbkHost host = GxwCbkHost_.Null;
	public String TextVal() {
		return shell.getText();} 
	public void TextVal_set(String v) {
		shell.setText(v);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {return this;}
	public void SendKeyDown(IptKey key)				{}
	public void SendMouseMove(int x, int y)			{}
	public void SendMouseDown(IptMouseBtn btn)		{}
	//public void windowActivated(WindowEvent e) 		{}
	//public void windowClosed(WindowEvent e) 		{}
	//public void windowClosing(WindowEvent e) 		{host.DisposeCbk();}
	//public void windowDeactivated(WindowEvent e) 	{}
	//public void windowDeiconified(WindowEvent e) 	{host.SizeChangedCbk();}
	//public void windowIconified(WindowEvent e) 		{host.SizeChangedCbk();}
	//public void windowOpened(WindowEvent e) 		{whenLoadedCmd.Invk();}
	//@Override public void processKeyEvent(KeyEvent e) 					{if (GxwCbkHost_.ExecKeyEvent(host, e)) 	super.processKeyEvent(e);}
	//@Override public void processMouseEvent(MouseEvent e) 				{if (GxwCbkHost_.ExecMouseEvent(host, e)) 	super.processMouseEvent(e);}
	//@Override public void processMouseWheelEvent(MouseWheelEvent e) 	{if (GxwCbkHost_.ExecMouseWheel(host, e)) 	super.processMouseWheelEvent(e);}
	//@Override public void processMouseMotionEvent(MouseEvent e)			{if (host.MouseMoveCbk(IptEvtDataMouse.new_(IptMouseBtn_.None, IptMouseWheel_.None, e.getX(), e.getY()))) super.processMouseMotionEvent(e);}
	//@Override public void paint(Graphics g) {
	//	if (host.PaintCbk(PaintArgs.new_(GfxAdpBase.new_((Graphics2D)g), RectAdp_.Zero)))	// ClipRect not used by any clients; implement when necessary	
	//		super.paint(g);
	//}
	public void EnableDoubleBuffering() {}
	public void TaskbarVisible_set(boolean val) {} 	public void TaskbarParkingWindowFix(GxwElem form) {}
	void ctor_GxwForm() {
	//	this.setLayout(null);										// use gfui layout
	//	this.ctrlMgr.BackColor_set(ColorAdp_.White);				// default form backColor to white
	//	this.setUndecorated(true);									// remove icon, titleBar, minimize, maximize, close, border
	//	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	// JAVA: cannot cancel alt+f4; set Close to noop, and manually control closing by calling this.CloseForm
	//	enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
	//	this.addWindowListener(this);
	//	GxwBoxListener lnr = new GxwBoxListener(this);
	//	this.addComponentListener(lnr);
	//	this.addFocusListener(lnr);	
	}
}

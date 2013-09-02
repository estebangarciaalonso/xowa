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
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class Anchor_link_demo {
	public static void main(String[] args) {
		String html
			= "<html>\n"
			+ "<head></head>\n"
			+ "<body>\n" 
			+ "  <input id='test_checkbox_id' type='checkbox'>test_checkbox</input><br/>\n"
			+ "  <br/>\n"
			+ "  <a href='#a'/>anchor link generates no events</a><br/>\n"
			+ "  <a href='http://localhost/generates_events'/>regular link generates events</a><br/>\n"
			+ "  <span id='a'>destination for anchor link</span>"
			+ "</body>\n"
			+ "</html>\n"
			;
		System.setProperty("org.eclipse.swt.browser.XULRunnerPath", "C:\\xowa\\bin\\windows\\xulrunner");
		System.out.println("swt_version: " + SWT.getVersion());
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Browser browser;
		try {
			browser = new Browser(shell, SWT.MOZILLA);	// changed from none
			browser.addLocationListener(new LocationListener() {				
				@Override
				public void changing(LocationEvent arg0) {
					if (arg0.location.equals("about:blank")) return;
					browser.evaluate("alert('location changing');");
					arg0.doit = false;
				}
				
				@Override
				public void changed(LocationEvent arg0) {
					if (arg0.location.equals("about:blank")) return;
					browser.evaluate("alert('location changed');");
					arg0.doit = false;
				}
			});
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			display.dispose();
			return;
		}
		browser.setText(html);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}

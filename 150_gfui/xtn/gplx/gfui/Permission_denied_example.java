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
public class Permission_denied_example {
	public static void main(String[] args) {
		String html
			= "<html>\n"
			+ "<head>\n" 
			+ "<script>\n"
			+ "  function permissionDeniedExample() {\n"
			+ "    var sel = window.getSelection();\n" 
			+ "    alert('calling sel.rangeCount');\n"
			+ "    alert('sel.rangeCount = ' + sel.rangeCount);\n"
			+ "  }\n"
			+ "</script>\n"
			+ "</head>\n"
			+ "<body>\n" 
			+ "  <a href='#direct_call_fails'/>click to call permissionDeniedExample -> will throw error and not show sel.rangeCount</a><br/>\n"
			+ "  <a href='#wrapped_call_works'/>click to call permissionDeniedExample inside a setTimeout -> will show sel.rangeCount</a><br/>\n"
			+ "</body>\n"
			+ "</html>\n"
			;

		System.setProperty
		( "org.eclipse.swt.browser.XULRunnerPath"
		// ADJUST THIS PATH AS NECESSARY ON YOUR MACHINE
		, "C:\\xowa\\bin\\windows\\xulrunner_v24"
		);
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
					arg0.doit = false;
				}
				
				@Override
				public void changed(LocationEvent arg0) {
					String location = arg0.location;
					if (location.equals("about:blank")) return;
					
					// build code
					String code = "alert('unknown_link:" + location + "')";
					if 		(location.contains("direct_call_fails"))
						code = "permissionDeniedExample();";
					else if (location.contains("wrapped_call_works"))
						code = "setTimeout(function(){permissionDeniedExample();}, 1);";
					
					// evaluate code
					try {
						browser.evaluate(code);
					} catch (Exception e) {
						System.out.println(e);
					}
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

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
package gplx.xowa.servers.tcp; import gplx.*; import gplx.xowa.*; import gplx.xowa.servers.*;
public class Xosrv_cmd_types {
	public static final byte[]
		Cmd_exec 		= ByteAry_.new_ascii_("xowa.cmd.exec")	, Cmd_pass	 	= ByteAry_.new_ascii_("xowa.cmd.result")	, Cmd_fail		= ByteAry_.new_ascii_("xowa.cmd.error")
	,	Js_exec 		= ByteAry_.new_ascii_("xowa.js.exec")	, Js_pass		= ByteAry_.new_ascii_("xowa.js.result")		, Js_fail		= ByteAry_.new_ascii_("xowa.js.error")
	,	Browser_exec 	= ByteAry_.new_ascii_("browser.js.exec"), Browser_pass 	= ByteAry_.new_ascii_("browser.js.result")	, Browser_fail 	= ByteAry_.new_ascii_("browser.js.error")
	;
}

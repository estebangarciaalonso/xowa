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
public class Xow_cfg_lnke {
	public String[] Protocol_ary() {if (protocol_ary == null) protocol_ary = protocol_list.XtoStrAry(); return protocol_ary;} 
	public boolean Protocol_rel_enabled() {return protocol_rel_enabled;} private boolean protocol_rel_enabled = true;
	String[] protocol_ary = new String[] {"http://", "https://", "ftp://", "irc://", "gopher://", "telnet://", "nntp://", "worldwind://", "svn://", "git://", "mms://", "mailto:"}; // REF.MW:DefaultSettings|$wgUrlProtocols}; NOTE: removing "news:" because it breaks alias wikinews:
	public static final byte Tid_http = 0, Tid_https = 1, Tid_ftp = 2, Tid_irc = 3, Tid_gopher = 4, Tid_telnet = 5, Tid_nntp = 6, Tid_worldwind = 7, Tid_svn = 8, Tid_git = 9, Tid_mms = 10, Tid_mailto = 11, Tid_xowa = 12, Tid_relative_1 = 13, Tid_relative_2 = 14;
	ListAdp protocol_list = ListAdp_.new_();
}

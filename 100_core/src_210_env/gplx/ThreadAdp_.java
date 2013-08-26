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
package gplx;
public class ThreadAdp_ {
		public static void Sleep(int milliseconds) {
		try {Thread.sleep(milliseconds);} catch (InterruptedException e) {throw Err_.err_key_(e, "gplx.Thread", "thread interrupted").Add("milliseconds", milliseconds);}
	}
		public static ThreadAdp invk_(GfoInvkAble invk, String invkCmd) {return new ThreadAdp(invk, invkCmd, GfoMsg_.Null);}
	public static ThreadAdp invk_msg_(GfoInvkAble invk, GfoMsg m) {return new ThreadAdp(invk, m.Key(), m);}
}

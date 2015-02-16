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
package gplx.threads; import gplx.*;
public class ThreadAdp_ {
		public static void Sleep(int milliseconds) {
		try {Thread.sleep(milliseconds);} catch (InterruptedException e) {throw Err_.err_key_(e, "gplx.Thread", "thread interrupted").Add("milliseconds", milliseconds);}
	}
		public static ThreadAdp invk_(GfoInvkAble invk, String cmd)						{return invk_(Name_null, invk, cmd);}
	public static ThreadAdp invk_(String name, GfoInvkAble invk, String cmd)		{return new ThreadAdp(name, invk, cmd, GfoMsg_.Null);}
	public static ThreadAdp invk_msg_(GfoInvkAble invk, GfoMsg msg)					{return invk_msg_(Name_null, invk, msg);}
	public static ThreadAdp invk_msg_(String name, GfoInvkAble invk, GfoMsg msg)	{return new ThreadAdp(name, invk, msg.Key(), msg);}
	public static void Run_invk_msg(String name, GfoInvkAble invk, GfoMsg m) {
		ThreadAdp_.invk_msg_(name, invk, m).Start();
	}
	public static final String Name_null = null;
}

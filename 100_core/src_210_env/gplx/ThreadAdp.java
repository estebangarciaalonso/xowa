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
package gplx;
import java.lang.*;
public class ThreadAdp implements Runnable {
	@gplx.Internal protected ThreadAdp(GfoInvkAble invk, String invkCmd, GfoMsg m) {
		this.invk = invk; this.invkCmd = invkCmd; this.m = m;
		this.ctor_ThreadAdp();
	}	GfoInvkAble invk; String invkCmd; GfoMsg m;
		public ThreadAdp Start() {thread.start(); return this;}
	public void Interrupt() {thread.interrupt();}
	public void Join() {
		try {
			thread.join();		
		}
		catch (Exception e) {
			Err_.Noop(e);
		}
	}
//	public void Stop() {thread.stop();}
	public boolean IsAlive() {return thread.isAlive();}
	void ctor_ThreadAdp() {
		thread = new Thread(this);
	}
	@Override public void run() {
		invk.Invk(GfsCtx._, 0, invkCmd, m);
	}
	Thread thread;
		public static final ThreadAdp Null = new ThreadAdp(GfoInvkAble_.Null, "", GfoMsg_.Null);
}

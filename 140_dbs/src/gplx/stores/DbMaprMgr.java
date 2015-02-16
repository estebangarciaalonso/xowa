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
package gplx.stores; import gplx.*;
public class DbMaprMgr {
	public DbMaprArg[] RootIndexFlds() {return rootIndexFlds;} public DbMaprMgr RootIndexFlds_(DbMaprArg... val) {rootIndexFlds = val; return this;} DbMaprArg[] rootIndexFlds;
	public DbMaprItm Root() {return root;} public DbMaprMgr Root_(DbMaprItm v) {root = v; return this;} DbMaprItm root;
	public ListAdp OwnerStack() {return ownerStack;} ListAdp ownerStack = ListAdp_.new_();
	public OrderedHash ContextVars() {return contextVars;} OrderedHash contextVars = OrderedHash_.new_();
	public ListAdp MaprStack() {return maprStack;} ListAdp maprStack = ListAdp_.new_();
	public void EnvStack_add(DbMaprItm mapr, SrlObj gobj) {
		for (Object argObj : mapr.ContextFlds()) {
			DbMaprArg arg = (DbMaprArg)argObj;
			Object contextVal = GfoInvkAble_.InvkCmd((GfoInvkAble)gobj, arg.ObjProp());
			this.ContextVars().AddReplace(arg.DbFld(), contextVal);
		}
		this.OwnerStack().Add(gobj);
		this.MaprStack().Add(mapr);
	}
	public void EnvStack_del(DbMaprItm mapr, SrlObj gobj) {
		for (Object argObj : mapr.ContextFlds()) {
			DbMaprArg arg = (DbMaprArg)argObj;
			this.ContextVars().Del(arg.DbFld());
		}
		ListAdp_.DelAt_last(this.OwnerStack());
		ListAdp_.DelAt_last(this.MaprStack());
	}
	public void Clear() {
		ownerStack.Clear();
		contextVars.Clear();
		maprStack.Clear();
	}
	public static DbMaprMgr new_() {return new DbMaprMgr();} DbMaprMgr() {}
}

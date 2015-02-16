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
public interface IptCfg extends NewAble, GfoInvkAble {
	String CfgKey();
	IptCfgItm GetOrDefaultArgs(String key, GfoMsg m, IptArg[] argAry);
	void Owners_add(String key, IptBndsOwner owner);
	void Owners_del(String key);
}
class IptCfg_base implements IptCfg {
	public String CfgKey() {return cfgKey;} private String cfgKey;
	public IptCfgItm GetOrDefaultArgs(String bndKey, GfoMsg defaultMsg, IptArg[] defaultArgs) {
		IptCfgItm rv = (IptCfgItm)hash.Fetch(bndKey);
		if (rv == null) {	// no cfg
			rv = IptCfgItm.new_().Key_(bndKey).Ipt_(ListAdp_.many_((Object[])defaultArgs)).Msg_(defaultMsg);
			hash.Add(bndKey, rv);
		}
		else {				// cfg exists
			if (rv.Msg() == null) rv.Msg_(defaultMsg); // no msg defined; use default
		}
		return rv;
	}
	public IptCfgItm Set(String bndKey, GfoMsg m, IptArg[] argAry) {
		IptCfgItm rv = GetOrDefaultArgs(bndKey, m, argAry);
		rv.Msg_(m); // always overwrite msg
		if (Dif(rv.Ipt(), argAry)) {
			rv.Ipt_(ListAdp_.many_((Object[])argAry));
			this.Change(bndKey, argAry);
		}
		return rv;
	}
	boolean Dif(ListAdp lhs, IptArg[] rhs) {
		if (lhs.Count() != rhs.length) return true;
		for (int i = 0; i < rhs.length; i++) {
			IptArg lhsArg = (IptArg)lhs.FetchAt(i);
			IptArg rhsArg = rhs[i];
			if (!lhsArg.Eq(rhsArg)) return true;
		}
		return false;
	}
	void Change(String bndKey, IptArg[] ary) {
		ListAdp list = (ListAdp)owners.Fetch(bndKey);
		if (list == null) return;
		for (int i = 0; i < list.Count(); i++) {
			IptBndsOwner owner = (IptBndsOwner)list.FetchAt(i);
			owner.IptBnds().Change(bndKey, ary);
		}
	}
	public void Owners_del(String bndKey) {owners.Del(bndKey);}
	public void Owners_add(String bndKey, IptBndsOwner owner) {
		ListAdp list = (ListAdp)owners.Fetch(bndKey);
		if (list == null) {
			list = ListAdp_.new_();
			owners.Add(bndKey, list);
		}
		list.Add(owner);
		owner.IptBnds().Cfgs().Add(new IptCfgPtr(cfgKey, bndKey));
	}	OrderedHash owners = OrderedHash_.new_();
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.MatchIn(k, Invk_Add, Invk_set)) {
			String bndKey = m.ReadStr("bndKey");
			String iptStr = m.ReadStr("ipt");
			String cmd = m.ReadStrOr("cmd", "");
			if (ctx.Deny()) return this;
			Set(bndKey, gplx.gfml.GfmlDataNde.XtoMsgNoRoot(cmd), IptArg_.parse_ary_(iptStr));
		}
		return this;
	}	public static final String Invk_Add = "Add", Invk_set = "set";
	public IptCfg_base(String cfgKey) {this.cfgKey = cfgKey;}
	OrderedHash hash = OrderedHash_.new_();
	public Object NewByKey(Object o) {return new IptCfg_base((String)o);} @gplx.Internal protected static final IptCfg HashProto = new IptCfg_base(); @gplx.Internal protected IptCfg_base() {}
}
class IptCfgPtr {
	public String CfgKey() {return cfgKey;} private String cfgKey;
	public String BndKey() {return bndKey;} private String bndKey;
	public IptCfgPtr(String cfgKey, String bndKey) {this.cfgKey = cfgKey; this.bndKey = bndKey;}
}

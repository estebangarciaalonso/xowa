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
import gplx.core.strings.*;
public class UsrMsg {
	public int VisibilityDuration() {return visibilityDuration;} public UsrMsg VisibilityDuration_(int v) {visibilityDuration = v; return this;} int visibilityDuration = 3000;
	public String Hdr() {return hdr;} public UsrMsg Hdr_(String val) {hdr = val; return this;} private String hdr;
	public OrderedHash Args() {return args;} OrderedHash args = OrderedHash_.new_();
	public UsrMsg Add(String k, Object v) {
		args.Add(k, KeyVal_.new_(k, v));
		return this;
	}
	public UsrMsg AddReplace(String k, Object v) {
		args.AddReplace(k, KeyVal_.new_(k, v));
		return this;
	}
	public String XtoStrSingleLine()	{return XtoStr(" ");}
	public String XtoStr()				{return XtoStr(Op_sys.Cur().Nl_str());}
	String XtoStr(String spr) {
		if (hdr == null) {
			GfoMsg m = GfoMsg_.new_cast_(cmd);
			for (int i = 0; i < args.Count(); i++) {
				KeyVal kv = (KeyVal)args.FetchAt(i);
				m.Add(kv.Key(), kv.Val());
			}
			return Object_.Xto_str_strict_or_null_mark(invk.Invk(GfsCtx._, 0, cmd, m));
		}
		String_bldr sb = String_bldr_.new_();
		sb.Add(hdr).Add(spr);
		for (int i = 0; i < args.Count(); i++) {
			KeyVal kv = (KeyVal)args.FetchAt(i);
			sb.Add_spr_unless_first("", " ", i);
			sb.Add_fmt("{0}={1}", kv.Key(), kv.Val(), spr);
		}
		return sb.XtoStr();
	}		
        public static UsrMsg fmt_(String hdr, Object... ary) {
		UsrMsg rv = new UsrMsg();
		rv.hdr = String_.Format(hdr, ary);
		return rv;
	}	UsrMsg() {}
        public static UsrMsg new_(String hdr) {
		UsrMsg rv = new UsrMsg();
		rv.hdr = hdr;
		return rv;
	}
        public static UsrMsg invk_(GfoInvkAble invk, String cmd) {
		UsrMsg rv = new UsrMsg();
		rv.invk = invk;
		rv.cmd = cmd;
		return rv;
	}	GfoInvkAble invk; String cmd;
}

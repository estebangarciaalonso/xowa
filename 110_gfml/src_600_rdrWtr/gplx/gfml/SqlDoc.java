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
package gplx.gfml; import gplx.*;
import gplx.core.strings.*; import gplx.core.criterias.*;
public class SqlDoc {
	public static GfmlDoc XtoDoc(String raw) {
		GfmlBldr bldr = GfmlBldr_.new_();
		bldr.Doc().RootLxr_set(RootLxr_());
		return bldr.XtoGfmlDoc(raw);
	}	
	static GfmlLxr RootLxr_() {
		GfmlTkn txtTkn = GfmlTkn_.cmd_("tkn:text", SqlCmd_root._);
		GfmlLxr rv = GfmlLxr_.general_("lxr:root", txtTkn);
		whitespace_(rv);
		operator_(rv
			, SqlConsts.Op_eq
			, SqlConsts.Op_eqn
			, SqlConsts.Op_eqn2
			, SqlConsts.Op_lt
			, SqlConsts.Op_mt
			, SqlConsts.Op_lte
			, SqlConsts.Op_mte
			, SqlConsts.Op_in_bgn
			, SqlConsts.Op_in_end
			, SqlConsts.Op_in_dlm
			);
		quote_(rv, "'");
		quote_(rv, "\"");
		return rv;
	}
	static GfmlLxr whitespace_(GfmlLxr lxr) {
		GfmlTkn tkn = GfmlTkn_.cmd_("key:gfml.whitespace_0", GfmlBldrCmd_.Null);
		GfmlLxr rv = GfmlLxr_.range_("lxr:gfml.whitespace_0", String_.Ary(" ", String_.Tab, String_.CrLf, String_.Lf), tkn, false);
		lxr.SubLxr_Add(rv);
		return rv;
	}
	static GfmlLxr quote_(GfmlLxr lxr, String quote) {
		GfmlLxr rv = GfmlLxr_frame.new_("gfml.quote_0", SqlFrame_quote._, quote, quote, SqlCmd_quote_str._, SqlCmd_quote_end._);

		GfmlLxr escape = lxr_escape_("gfml.quote_0_escape", quote + quote, quote);
		rv.SubLxr_Add(escape);
		lxr.SubLxr_Add(rv);
		return rv;
	}
	static GfmlLxr lxr_escape_(String key, String raw, String escape) {return GfmlLxr_.symbol_(key, raw, escape, GfmlBldrCmd_pendingTkns_add._);}

	static void operator_(GfmlLxr lxr, String... opAry) {
		for (String op : opAry) {
			GfmlLxr opLxr = GfmlLxr_.symbol_("sql:" + op, op, op, SqlCmd_operator.new_(op));
			lxr.SubLxr_Add(opLxr);
		}
	}
}
class SqlCmd_quote_str implements GfmlBldrCmd {
	public String Key() {return "sql:root";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {
		bldr.CurFrame().WaitingTkns().Add(GfmlTkn_.raw_(tkn.Raw()));
	}
	public static final SqlCmd_quote_str _ = new SqlCmd_quote_str(); SqlCmd_quote_str() {}
}
class SqlCmd_quote_end implements GfmlBldrCmd {
	public String Key() {return "sql:root";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {
		String_bldr sb = String_bldr_.new_();
		GfmlObjList list = bldr.CurFrame().WaitingTkns();
		for (int i = 0; i < list.Count(); i++) {
			GfmlTkn pnd = (GfmlTkn)list.FetchAt(i);
			sb.Add(pnd.Val());
		}
		//Int_.Xto_str(bldr.CurNdeFrame().Nde().SubTkns().length)
		GfmlAtr atr = GfmlAtr.new_(GfmlTkn_.raw_("word"), GfmlTkn_.raw_(sb.XtoStr()), GfmlType_.String);
		bldr.CurNdeFrame().CurNde().SubObjs_Add(atr);
		bldr.Frames_end();
	}
	public static final SqlCmd_quote_end _ = new SqlCmd_quote_end(); SqlCmd_quote_end() {}
}
class SqlCmd_root implements GfmlBldrCmd {
	public String Key() {return "sql:root";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {			
		GfmlSqlUtl.Atr_add(bldr, "word", tkn);
	}
	public static final SqlCmd_root _ = new SqlCmd_root(); SqlCmd_root() {}
}
class SqlFrame_quote extends GfmlFrame_base {
	@Override public int FrameType() {return GfmlFrame_.Type_data;}
	@Override public void Build_end(GfmlBldr bldr, GfmlFrame ownerFrame) {
	}
	@Override protected GfmlFrame_base MakeNew_hook() {return new SqlFrame_quote();}
	public static final SqlFrame_quote _ = new SqlFrame_quote(); SqlFrame_quote() {}
}
class SqlCmd_operator implements GfmlBldrCmd {
	public String Key() {return "sql:operator";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {
		GfmlSqlUtl.Atr_add(bldr, "op", tkn);
	}
	String op;
	public static SqlCmd_operator new_(String op) {
		SqlCmd_operator rv = new SqlCmd_operator();
		rv.op = op;
		return rv;
	}	SqlCmd_operator() {}
}
class GfmlSqlUtl {
	public static void Nde_bgn(GfmlBldr bldr, String name) {
		bldr.CurNdeFrame().NdeBody_bgn(GfmlTkn_.Null);
		bldr.CurNde().Hnd_set(name);
	}
	public static void Nde_end(GfmlBldr bldr) {
		bldr.Frames_end();
	}
	public static void Atr_add(GfmlBldr bldr, String raw, GfmlTkn tkn) {
		GfmlAtr atr = GfmlAtr.new_(GfmlTkn_.raw_(raw), tkn, GfmlType_.String);
		bldr.CurNde().SubObjs_Add(atr);
	}
}
class GfmlNdeWrapper {
	public GfmlNde Nde() {return nde;} GfmlNde nde;
	public GfmlNdeWrapper Name_(String v) {nde.Hnd_set(v); return this;}
	public GfmlNdeWrapper Atrs_add_(String name, String val) {
		GfmlAtr atr = GfmlAtr.new_(GfmlTkn_.raw_(name), GfmlTkn_.raw_(val), GfmlType_.String);
		nde.SubObjs_Add(atr);
		return this;
	}
	public static GfmlNdeWrapper new_() {
		GfmlNdeWrapper rv = new GfmlNdeWrapper();
		rv.nde = GfmlNde.new_(GfmlTkn_.Null, GfmlType_.Null, false);
		return rv;
	}
}

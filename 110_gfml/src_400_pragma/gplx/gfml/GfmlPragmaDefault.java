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
class GfmlPragmaDefault implements GfmlPragma {
	public String KeyOfPragma() {return "_default";}
	public void Exec(GfmlBldr bldr, GfmlNde pragmaNde) {
		ListAdp list = Compile(pragmaNde);
		GfmlDefaultPragma_bgnCmd.ExecList(bldr.TypeMgr().TypeRegy(), bldr.CurNdeFrame().CurDocPos(), list);
//			bldr.PragmaMgr.EndCmds_add(GfmlDocPos_.up_(bldr.CurNdeFrame.DocPos), GfmlDefaultPragma_endCmd.new_(list));
	}
	@gplx.Internal protected ListAdp Compile(GfmlNde pragmaNde) {
		ListAdp list = ListAdp_.new_();
		for (int i = 0; i < pragmaNde.SubHnds().Count(); i++) {
			GfmlNde subNde = (GfmlNde)pragmaNde.SubHnds().FetchAt(i);
			CompileSubNde(subNde, list);
		}
		return list;
	}
	@gplx.Internal protected void CompileSubNde(GfmlNde nde, ListAdp list) {
		String typeKey = nde.SubKeys().FetchDataOrNull("typeKey"); if (typeKey == null) throw Err_.missing_key_("typeKey");
		for (int i = 0; i < nde.SubHnds().Count(); i++) {
			GfmlNde subNde = (GfmlNde)nde.SubHnds().FetchAt(i);
			GfmlDefaultItem item = CompileItem(subNde, typeKey);
			list.Add(item);
		}
	}
	@gplx.Internal protected GfmlDefaultItem CompileItem(GfmlNde nde, String typeKey) {
		String key = nde.SubKeys().FetchDataOrFail("key");
		GfmlTkn valTkn = nde.SubKeys().FetchDataTknOrNull("val");
		return GfmlDefaultItem.new_(typeKey, key, valTkn);
	}
	public GfmlType[] MakePragmaTypes(GfmlTypeMakr makr) {
		makr.MakeSubTypeAsOwner(	"_default");
		makr.MakeSubTypeAsOwner(		"type", "typeKey");
		makr.MakeSubTypeAsOwner(			"atr", "key", "val");
		return makr.Xto_bry();
	}
	public static GfmlPragmaDefault new_() {return new GfmlPragmaDefault();} GfmlPragmaDefault() {}
	public static final GfmlTkn Default_none = GfmlTkn_.raw_("DEFAULT NONE");
}
class GfmlDefaultItem {
	public String TypeKey() {return typeKey;} private String typeKey;
	public String Key() {return key;} private String key;
	public GfmlObj Val() {return val;} GfmlObj val;
	public GfmlObj ValPrev() {return valPrev;} @gplx.Internal protected GfmlDefaultItem ValPrev_(GfmlObj tkn) {valPrev = tkn; return this;} GfmlObj valPrev;
	
	public void Exec_bgn(GfmlType type) {
		GfmlFld fld = type.SubFlds().Fetch(key);
		if (fld == null) {						// no default defined; create
			valPrev = GfmlPragmaDefault.Default_none;
			fld = GfmlFld.new_(true, key, GfmlType_.StringKey).DefaultTkn_(val);
			type.SubFlds().Add(fld);
		}
		else {									// default exists; overwrite
			valPrev = fld.DefaultTkn();
			fld.DefaultTkn_(val);
		}
	}
	public void Exec_end(GfmlType type) {
		GfmlFld fld = type.SubFlds().Fetch(key); if (fld == null) return;
//			if (fld == null) throw Err_.arg_range_msg_("fatal: could not find fld; typeKey={0} fld={1}", typeKey, key);
		if (valPrev == GfmlPragmaDefault.Default_none)			// drop default; (had been created by pragma)
			type.SubFlds().Del(fld);
		else													// restore default
			fld.DefaultTkn_(valPrev);
	}
	public static GfmlDefaultItem new_(String typeKey, String key, GfmlTkn val) {
		GfmlDefaultItem rv = new GfmlDefaultItem();
		rv.typeKey = typeKey;
		rv.key = key;
		rv.val = val;
		return rv;
	}	GfmlDefaultItem() {}
}
class GfmlDefaultPragma_bgnCmd implements GfmlBldrCmd {
	public String Key() {return "pragma:gfml.default.bgnCmd";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {ExecList(bldr.TypeMgr().TypeRegy(), bldr.CurNdeFrame().CurDocPos(), list);}
	@gplx.Internal protected static void ExecList(GfmlTypRegy regy, GfmlDocPos pos, ListAdp list) {
		GfmlType type = GfmlType_.Null;
		for (Object itemObj : list) {
			GfmlDefaultItem item = (GfmlDefaultItem)itemObj;
			if (!String_.Eq(item.TypeKey(), type.Key())) {
				type = regy.FetchOrNull(item.TypeKey(), pos);
				if (type == GfmlType_.Null) throw Err_.new_("default type must exist").Add("typeKey", item.TypeKey());
			}
			type = type.Clone().DocPos_(pos);
			regy.Add(type);
			item.Exec_bgn(type);
		}
	}
	ListAdp list;
	public static GfmlDefaultPragma_bgnCmd new_(ListAdp list) {
		GfmlDefaultPragma_bgnCmd rv = new GfmlDefaultPragma_bgnCmd();
		rv.list = list;
		return rv;
	}	GfmlDefaultPragma_bgnCmd() {}
}
class GfmlDefaultPragma_endCmd implements GfmlBldrCmd {
	public String Key() {return "pragma:gfml.default.endCmd";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {ExecList(bldr.TypeMgr().TypeRegy(), list);}
	public static void ExecList(GfmlTypRegy regy, ListAdp list) {
		GfmlType type = GfmlType_.Null;
		for (Object itemObj : list) {
			GfmlDefaultItem item = (GfmlDefaultItem)itemObj;
			if (!String_.Eq(item.TypeKey(), type.Key())) {
				type = regy.FetchOrNull(item.TypeKey());
				if (type == GfmlType_.Null) throw Err_.new_("fatal: default type must exist").Add("typeKey", item.TypeKey());
			}
			item.Exec_end(type);
		}
	}
	ListAdp list;
	public static GfmlDefaultPragma_endCmd new_(ListAdp list) {
		GfmlDefaultPragma_endCmd rv = new GfmlDefaultPragma_endCmd();
		rv.list = list;
		return rv;
	}	GfmlDefaultPragma_endCmd() {}
}

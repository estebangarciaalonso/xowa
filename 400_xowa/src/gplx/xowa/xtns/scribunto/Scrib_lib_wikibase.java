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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.xtns.wdatas.*;
import gplx.json.*;
class Scrib_lib_wikibase implements Scrib_lib {
	public Scrib_lib_wikibase(Scrib_engine engine) {this.engine = engine;} Scrib_engine engine;
	public Scrib_mod Mod() {return mod;} Scrib_mod mod;
	public Scrib_mod Register(Scrib_engine engine, Io_url script_dir) {
		mod = engine.RegisterInterface(script_dir.GenSubFil("mw.wikibase.lua"), this
			, String_.Ary(Invk_getEntity, Invk_getEntityId, Invk_getGlobalSiteId)
			);
		return mod;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_getEntity))			return GetEntity((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_getEntityId))		return GetEntityId((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_getGlobalSiteId))	return GetGlobalSiteId((KeyVal[])m.CastObj("v"));
		else	return GfoInvkAble_.Rv_unhandled;
	}
	public static final String Invk_getEntity = "getEntity", Invk_getEntityId = "getEntityId", Invk_getGlobalSiteId = "getGlobalSiteId";
	public KeyVal[] GetEntity(KeyVal[] values) {
		byte[] ttl_bry = Scrib_kv_utl.Val_to_bry(values, 0);
		Xow_wiki wiki = engine.Wiki();
		Wdata_wiki_mgr wdata_mgr = wiki.App().Wiki_mgr().Wdata_mgr();
		Wdata_doc page_doc = wdata_mgr.Pages_get(ttl_bry); if (page_doc == null) return KeyVal_.Ary_empty;
		if (parser == null) parser = new Wdata_doc_parser(wiki.App().Usr_dlg());
		return Scrib_kv_utl.base1_obj_(Scrib_lib_wikibase_srl.Srl(parser, page_doc, true));
	}	private Wdata_doc_parser parser;
	public KeyVal[] GetEntityId(KeyVal[] values) {
		byte[] ttl_bry = Scrib_kv_utl.Val_to_bry(values, 0);
		Xow_wiki wiki = engine.Wiki();
		Wdata_wiki_mgr wdata_mgr = wiki.App().Wiki_mgr().Wdata_mgr();
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
		byte[] rv = wdata_mgr.Qids_get(wiki, ttl); if (rv == null) rv = ByteAry_.Empty;
		return Scrib_kv_utl.base1_obj_(rv);
	}
	public KeyVal[] GetGlobalSiteId(KeyVal[] values) {			
		return Scrib_kv_utl.base1_obj_(engine.Wiki().Wiki_tid_code());	// ;siteGlobalID: This site's global ID (e.g. <code>'itwiki'</code>), as used in the sites table. Default: <code>$wgDBname</code>.; REF:/xtns/Wikibase/docs/options.wiki
	}
}

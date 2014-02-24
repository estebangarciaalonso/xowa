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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.xtns.wdatas.*;
import gplx.json.*;
class Scrib_lib_wikibase_entity implements Scrib_lib {
	public Scrib_lib_wikibase_entity(Scrib_engine engine) {this.engine = engine;} Scrib_engine engine;
	public Scrib_mod Mod() {return mod;} Scrib_mod mod;
	public Scrib_mod Register(Scrib_engine engine, Io_url script_dir) {
		mod = engine.RegisterInterface(script_dir.GenSubFil("mw.wikibase.entity.lua"), this
			, String_.Ary(Invk_getGlobalSiteId, Invk_formatPropertyValues)
			);
		return mod;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_getGlobalSiteId))		return GetGlobalSiteId((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_formatPropertyValues))	return FormatPropertyValues((KeyVal[])m.CastObj("v"));
		else	return GfoInvkAble_.Rv_unhandled;
	}
	public static final String Invk_getGlobalSiteId = "getGlobalSiteId", Invk_formatPropertyValues = "formatPropertyValues";
	public KeyVal[] GetGlobalSiteId(KeyVal[] values) {			
		return Scrib_kv_utl.base1_obj_(engine.Wiki().Domain_abrv());	// ;siteGlobalID: This site's global ID (e.g. <code>'itwiki'</code>), as used in the sites table. Default: <code>$wgDBname</code>.; REF:/xtns/Wikibase/docs/options.wiki
	}
	public KeyVal[] FormatPropertyValues(KeyVal[] values)	{
		Scrib_args args = new Scrib_args(values);
		byte[] qid = args.Pull_bry(0);
		byte[] pid = args.Pull_bry(1);

		Xoa_app app = engine.App(); Xow_wiki wiki = engine.Wiki();
		Wdata_wiki_mgr wdata_mgr = app.Wiki_mgr().Wdata_mgr();
		byte[] lang = wiki.Wdata_wiki_lang();
		Wdata_doc wdoc = wdata_mgr.Pages_get(qid); if (wdoc == null) return Scrib_kv_utl.base1_obj_null();
		int pid_int = wdata_mgr.Pids_get(lang, pid); if (pid_int == Wdata_wiki_mgr.Pid_null) return Scrib_kv_utl.base1_obj_null();
		Wdata_prop_grp prop_grp = wdoc.Claim_list_get(pid_int); if (prop_grp == null) return Scrib_kv_utl.base1_obj_null();
		ByteAryBfr bfr = app.Utl_bry_bfr_mkr().Get_b512();
		wdata_mgr.Resolve_to_bfr(bfr, prop_grp, lang);
		return Scrib_kv_utl.base1_obj_(bfr.Mkr_rls().XtoAryAndClear());
	}
}
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
class Scrib_lib_site implements Scrib_lib {
	public Scrib_lib_site(Scrib_engine engine) {this.engine = engine;} Scrib_engine engine;
	public Scrib_mod Mod() {return mod;} Scrib_mod mod;
	public Scrib_mod Register(Scrib_engine engine, Io_url script_dir) {
		mod = engine.RegisterInterface(script_dir.GenSubFil("mw.site.lua"), this
			, String_.Ary(Invk_getNsIndex, Invk_pagesInCategory, Invk_pagesInNs, Invk_usersInGroup, Invk_init_site_for_wiki)
			// NOTE: info not passed by default; rely on Init_for_wiki
			);
		notify_wiki_changed_fnc = mod.Fncs_get_by_key("notify_wiki_changed");
		return mod;
	}	Scrib_fnc notify_wiki_changed_fnc;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_getNsIndex))				return GetNsIndex((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_pagesInCategory))		return PagesInCategory((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_pagesInNs))				return PagesInNs((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_usersInGroup))			return UsersInGroup((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_init_site_for_wiki))		return Init_site_for_wiki();
		else	return GfoInvkAble_.Rv_unhandled;
	}	public static final String Invk_getNsIndex = "getNsIndex", Invk_pagesInCategory = "pagesInCategory", Invk_pagesInNs = "pagesInName"+"space", Invk_usersInGroup = "usersInGroup", Invk_init_site_for_wiki = "init_site_for_wiki";
	public KeyVal[] GetNsIndex(KeyVal[] values) {
		byte[] ns_name = Scrib_kv_utl.Val_to_bry(values, 0);
		Object ns_obj = engine.Wiki().Ns_mgr().Trie_match_exact(ns_name, 0, ns_name.length);
		return ns_obj == null ? KeyVal_.Ary_empty : Scrib_kv_utl.base1_obj_(((Xow_ns)ns_obj).Id());
	}
	public KeyVal[] PagesInCategory(KeyVal[] values) {
		byte[] ctg_name = Scrib_kv_utl.Val_to_bry(values, 0);
		// byte[] ctg_type = Scrib_kv_utl.Val_to_bry(values, 1);	// TODO.7: get by categoy type; WHEN: categories
		int ctg_count = engine.Wiki().Db_mgr().Load_mgr().Load_ctg_count(ctg_name);
		return Scrib_kv_utl.base1_obj_(ctg_count);
	}
	public KeyVal[] PagesInNs(KeyVal[] values) {
		int ns_id = Scrib_kv_utl.Val_to_int(values, 0);
		Xow_ns ns = engine.Wiki().Ns_mgr().Get_by_id(ns_id);
		int ns_count = ns == null ? 0 : ns.Count();
		return Scrib_kv_utl.base1_obj_(ns_count);
	}
	public KeyVal[] UsersInGroup(KeyVal[] values) {	// TODO.9: get user_groups table
		// byte[] grp_name = Scrib_kv_utl.Val_to_bry(values, 0);
		return Scrib_kv_utl.base1_obj_(0);
	}
	public void Notify_wiki_changed() {if (notify_wiki_changed_fnc != null) engine.Interpreter().CallFunction(notify_wiki_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public KeyVal[] Init_site_for_wiki() {			
		Xow_wiki wiki = engine.Wiki();
		KeyVal[] rv = new KeyVal[7];
		Bld_info(rv);
		rv[5] = KeyVal_.new_("name" + "spaces", Bld_ns_ary(wiki));
		rv[6] = KeyVal_.new_("stats", Bld_stats(wiki));
		return Scrib_kv_utl.base1_obj_(rv);
	}
	private void Bld_info(KeyVal[] rv) {
		Xow_wiki_props props = engine.Wiki().Props();
		rv[0] = KeyVal_.new_("siteName"			, props.SiteName());
		rv[1] = KeyVal_.new_("server"			, props.Server());
		rv[2] = KeyVal_.new_("scriptPath"		, props.ScriptPath());
		rv[3] = KeyVal_.new_("stylePath"		, props.StylePath());
		rv[4] = KeyVal_.new_("currentVersion"	, props.CurrentVersion());
	}
	KeyVal[] Bld_ns_ary(Xow_wiki wiki) {
		Xow_ns_mgr ns_mgr = wiki.Ns_mgr();
		int len = ns_mgr.Id_count();
		KeyVal[] rv = new KeyVal[len];
		int rv_idx = 0;
		for (int i = 0; i < len; i++) {
			Xow_ns ns = ns_mgr.Id_get_at(i);
			if (ns == null) continue;
			int ns_id = ns.Id();
			rv[rv_idx++] = KeyVal_.int_(ns_id, Bld_ns(wiki, ns, ns_id));
		}
		return rv;
	}
	KeyVal[] Bld_ns(Xow_wiki wiki, Xow_ns ns, int ns_id) {
		int len = 16;
		if		(ns_id <  Xow_ns_.Id_main) len = 14;
		else if (ns_id == Xow_ns_.Id_main) len = 17;
		KeyVal[] rv = new KeyVal[len];
		rv[ 0] = KeyVal_.new_("id"						, ns_id);
		rv[ 1] = KeyVal_.new_("name"					, ns.Name_txt());
		rv[ 2] = KeyVal_.new_("canonicalName"			, ns.Name_str());				// strtr( $canonical, "_", " " ),
		rv[ 3] = KeyVal_.new_("hasSubpages"				, ns.Subpages_enabled());		// MWNs::hasSubpages( $ns ),
		rv[ 4] = KeyVal_.new_("hasGenderDistinction"	, ns.Is_gender_aware());		// MWNs::hasGenderDistinction( $ns ),
		rv[ 5] = KeyVal_.new_("isCapitalized"			, ns.Is_capitalized());			// MWNs::isCapitalized( $ns ),
		rv[ 6] = KeyVal_.new_("isContent"				, ns.Is_content());				// MWNs::isContent( $ns ),
		rv[ 7] = KeyVal_.new_("isIncludable"			, ns.Is_includable());			// !MWNs::isNonincludable( $ns ),
		rv[ 8] = KeyVal_.new_("isMovable"				, ns.Is_movable());				// MWNs::isMovable( $ns ),
		rv[ 9] = KeyVal_.new_("isSubject"				, ns.Id_subj());
		rv[10] = KeyVal_.new_("isTalk"					, ns.Id_talk());
		rv[11] = KeyVal_.new_("defaultContentModel"		, null);						// MWNs::getNsContentModel( $ns ), ASSUME: always null?
		rv[12] = KeyVal_.new_("aliases"					, KeyVal_.Ary_empty);			// array(),
		if (ns_id < 0)
			rv[13] = KeyVal_.new_("subject"				, ns_id);						// MWNs::getSubject( $ns );
		else {
			rv[13] = KeyVal_.new_("subject"				, ns_id);						// MWNs::getSubject( $ns );
			rv[14] = KeyVal_.new_("talk"				, ns.Id_talk_id());				// MWNs::getTalk( $ns );
			rv[15] = KeyVal_.new_("associated"			, ns.Id_alt_id());				// MWNs::getAssociated( $ns );
			// TODO.3: get aliases for ns
			if (ns_id == Xow_ns_.Id_main)
				rv[16] = KeyVal_.new_("displayName"		, wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_ns_blankns));	// MWNs::getAssociated( $ns );
		}
		return rv;
	}
	KeyVal[] Bld_stats(Xow_wiki wiki) {
		Xow_wiki_stats stats = wiki.Stats();
		KeyVal[] rv = new KeyVal[8];
		rv[0] = KeyVal_.new_("pages", stats.NumPages());			// SiteStats::pages(),
		rv[1] = KeyVal_.new_("articles", stats.NumArticles());		// SiteStats::articles(),
		rv[2] = KeyVal_.new_("files", stats.NumFiles());			// SiteStats::images(),
		rv[3] = KeyVal_.new_("edits", stats.NumEdits());			// SiteStats::edits(),
		rv[4] = KeyVal_.new_("views", stats.NumViews());			// $wgDisableCounters ? null : (int)SiteStats::views(),
		rv[5] = KeyVal_.new_("users", stats.NumUsers());			// SiteStats::users(),
		rv[6] = KeyVal_.new_("activeUsers", stats.NumUsersActive());// SiteStats::activeUsers(),
		rv[7] = KeyVal_.new_("admins", stats.NumAdmins());			// SiteStats::activeUsers(),
		return rv;
	}
}

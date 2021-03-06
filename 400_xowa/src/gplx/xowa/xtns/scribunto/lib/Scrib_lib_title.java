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
package gplx.xowa.xtns.scribunto.lib; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*;
import gplx.core.primitives.*;
import gplx.xowa.wikis.caches.*; import gplx.xowa.xtns.pfuncs.ttls.*; import gplx.xowa.wikis.xwikis.*;
import gplx.xowa2.files.commons.*; import gplx.xowa2.files.metas.*;
public class Scrib_lib_title implements Scrib_lib {
	public Scrib_lib_title(Scrib_core core) {this.core = core;} private Scrib_core core;
	public Scrib_lua_mod Mod() {return mod;} private Scrib_lua_mod mod;
	public Scrib_lib Init() {procs.Init_by_lib(this, Proc_names); return this;}
	public Scrib_lua_mod Register(Scrib_core core, Io_url script_dir) {
		Init();
		mod = core.RegisterInterface(this, script_dir.GenSubFil("mw.title.lua")
			, KeyVal_.new_("thisTitle", "")					// NOTE: pass blank; will be updated by GetCurrentTitle
			, KeyVal_.new_("NS_MEDIA", Xow_ns_.Id_media)	// NOTE: MW passes down NS_MEDIA; this should be -2 on all wikis...
			);
		notify_page_changed_fnc = mod.Fncs_get_by_key("notify_page_changed");
		return mod;
	}	private Scrib_lua_proc notify_page_changed_fnc;
	public Scrib_proc_mgr Procs() {return procs;} private Scrib_proc_mgr procs = new Scrib_proc_mgr();
	public boolean Procs_exec(int key, Scrib_proc_args args, Scrib_proc_rslt rslt) {
		switch (key) {
			case Proc_newTitle:							return NewTitle(args, rslt);
			case Proc_makeTitle:						return MakeTitle(args, rslt);
			case Proc_getExpensiveData:					return GetExpensiveData(args, rslt);
			case Proc_getUrl:							return GetUrl(args, rslt);
			case Proc_getContent:						return GetContent(args, rslt);
			case Proc_getFileInfo:						return GetFileInfo(args, rslt);
			case Proc_getCurrentTitle:					return GetCurrentTitle(args, rslt);
			case Proc_protectionLevels:					return ProtectionLevels(args, rslt);
			case Proc_cascadingProtection:				return CascadingProtection(args, rslt);
			default: throw Err_.unhandled(key);
		}
	}
	private static final int Proc_newTitle = 0, Proc_makeTitle = 1, Proc_getExpensiveData = 2, Proc_getUrl = 3, Proc_getContent = 4, Proc_getFileInfo = 5, Proc_getCurrentTitle = 6, Proc_protectionLevels = 7, Proc_cascadingProtection = 8;
	public static final String 
	  Invk_newTitle = "newTitle", Invk_getExpensiveData = "getExpensiveData", Invk_makeTitle = "makeTitle"
	, Invk_getUrl = "getUrl", Invk_getContent = "getContent", Invk_getFileInfo = "getFileInfo", Invk_getCurrentTitle = "getCurrentTitle"
	, Invk_protectionLevels = "protectionLevels", Invk_cascadingProtection = "cascadingProtection"
	;
	private static final String[] Proc_names = String_.Ary(Invk_newTitle, Invk_makeTitle, Invk_getExpensiveData, Invk_getUrl, Invk_getContent, Invk_getFileInfo, Invk_getCurrentTitle, Invk_protectionLevels, Invk_cascadingProtection);
	public boolean NewTitle(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte[] ttl_bry = args.Pull_bry(0);
		Object ns_obj = args.Cast_obj_or_null(1);
		Xow_wiki wiki = core.Wiki();
		byte[] ns_bry = null;
		if (ns_obj != null) {
			ns_bry = Parse_ns(wiki, ns_obj); if (ns_bry == null) throw Err_.new_fmt_("unknown ns: {0}", Object_.Xto_str_strict_or_empty(ns_bry));
		}
		if (ns_bry != null) {
			Bry_bfr bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
			ttl_bry = bfr.Add(ns_bry).Add_byte(Byte_ascii.Colon).Add(ttl_bry).Mkr_rls().Xto_bry_and_clear();
		}
		Xoa_ttl ttl = Xoa_ttl.parse_(core.Wiki(), ttl_bry);
		if (ttl == null) return rslt.Init_obj(null);	// invalid title; exit; matches MW logic
		return rslt.Init_obj(GetInexpensiveTitleData(ttl));
	}
	public void Notify_page_changed() {if (notify_page_changed_fnc != null) core.Interpreter().CallFunction(notify_page_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public boolean GetUrl(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xow_wiki wiki = core.Wiki();
		byte[] ttl_bry = args.Pull_bry(0);
		byte[] url_func_bry = args.Pull_bry(1);
		Object url_func_obj = url_func_hash.Fetch(url_func_bry);
		if (url_func_obj == null) throw Err_.new_fmt_("url_function is not valid: {0}", String_.new_utf8_(url_func_bry));
		byte url_func_tid = ((Byte_obj_val)url_func_obj).Val();
		byte[] qry_bry = args.Extract_qry_args(wiki, 2);
		// byte[] proto = Scrib_kv_utl_.Val_to_bry_or(values, 3, null);	// NOTE: Scribunto has more conditional logic around argument 2 and setting protocols; DATE:2014-07-07
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry); if (ttl == null) return rslt.Init_obj(null);
		Bry_bfr bfr = wiki.App().Utl_bry_bfr_mkr().Get_b512();
		//if (url_func_tid == Pfunc_urlfunc.Tid_full) {
		//	if (proto == null) proto = Proto_relative;
		//	Object proto_obj = proto_hash.Fetch(proto); if (proto_obj == null) throw Err_.new_fmt_("protocol is not valid: {0}", proto);
		//	//qry_bry = (byte[])proto_obj;
		//	byte proto_tid = ((Byte_obj_val)proto_obj).Val();
		//	bfr.Add();
		//}
		Pfunc_urlfunc.UrlString(core.Ctx(), url_func_tid, false, ttl_bry, bfr, qry_bry);
		return rslt.Init_obj(bfr.Mkr_rls().Xto_str_and_clear());
	}
	private static final Hash_adp_bry url_func_hash = Hash_adp_bry.ci_ascii_()
	.Add_str_byte("fullUrl", Pfunc_urlfunc.Tid_full)
	.Add_str_byte("localUrl", Pfunc_urlfunc.Tid_local)
	.Add_str_byte("canonicalUrl", Pfunc_urlfunc.Tid_canonical);
	// private static final byte[] Proto_relative = Bry_.new_ascii_("relative");
	// private static final Hash_adp_bry proto_hash = Hash_adp_bry.ci_ascii_().Add_str_obj("http", Bry_.new_ascii_("http://")).Add_str_obj("https", Bry_.new_ascii_("https://")).Add_str_obj("relative", Bry_.new_ascii_("//")).Add_str_obj("canonical", Bry_.new_ascii_("1"));
	private byte[] Parse_ns(Xow_wiki wiki, Object ns_obj) {
		if (ClassAdp_.Eq_typeSafe(ns_obj, String.class))
			return Bry_.new_utf8_(String_.cast_(ns_obj));
		else {
			int ns_id = Int_.cast_(ns_obj);
			Xow_ns ns = wiki.Ns_mgr().Ids_get_or_null(ns_id);
			if (ns != null)
				return ns.Name_bry();
		}
		return null;
	}
	public boolean MakeTitle(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xow_wiki wiki = core.Wiki();
		byte[] ns_bry = Parse_ns(wiki, args.Cast_obj_or_null(0));
		String ttl_str = args.Pull_str(1);
		String anchor_str = args.Cast_str_or_null(2);
		String xwiki_str = args.Cast_str_or_null(3);
		Bry_bfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_k004();
		if (xwiki_str != null) tmp_bfr.Add_str(xwiki_str).Add_byte(Byte_ascii.Colon);		
		if (Bry_.Len_gt_0(ns_bry))	// only prefix ns if available; EX:"Template:Title"; else will get ":Title"; DATE:2014-10-30
			tmp_bfr.Add(ns_bry).Add_byte(Byte_ascii.Colon);
		tmp_bfr.Add_str(ttl_str);
		if (anchor_str != null) tmp_bfr.Add_byte(Byte_ascii.Hash).Add_str(anchor_str);
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, tmp_bfr.Mkr_rls().Xto_bry_and_clear());
		if (ttl == null) return rslt.Init_obj(null);	// invalid title; exit;
		return rslt.Init_obj(GetInexpensiveTitleData(ttl));
	}
	public boolean GetExpensiveData(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte[] ttl_bry = args.Pull_bry(0);
		Xoa_ttl ttl = Xoa_ttl.parse_(core.Wiki(), ttl_bry);
		if (ttl == Xoa_ttl.Null) return rslt.Init_null();
		// TODO: MW does extra logic here to cache ttl in ttl cache to avoid extra title lookups
		boolean ttl_exists = false, ttl_redirect = false; int ttl_id = 0;
		synchronized (tmp_db_page) {
			ttl_exists = core.Wiki().Db_mgr().Load_mgr().Load_by_ttl(tmp_db_page, ttl.Ns(), ttl.Page_db());
		}
		if (ttl_exists) {
			ttl_redirect = tmp_db_page.Type_redirect();
			ttl_id = tmp_db_page.Id();
		}
		KeyVal[] rv = new KeyVal[4];
		rv[ 0] = KeyVal_.new_("isRedirect"			, ttl_redirect);						// title.isRedirect
		rv[ 1] = KeyVal_.new_("id"					, ttl_id);								// $title->getArticleID(),
		rv[ 2] = KeyVal_.new_("contentModel"		, Key_wikitexet);						// $title->getContentModel(); see Defines.php and CONTENT_MODEL_
		rv[ 3] = KeyVal_.new_("exists"				, ttl_exists);							// $ret['id'] > 0; TODO: if Special: check regy of implemented pages
		return rslt.Init_obj(rv);
	}
	public boolean GetFileInfo(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte[] ttl_bry = args.Pull_bry(0);
		Xow_wiki wiki = core.Wiki();
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
		if (	ttl == null
			|| !ttl.Ns().Id_file_or_media()
			) return rslt.Init_obj(GetFileInfo_absent);
		if (ttl.Ns().Id_media()) ttl = Xoa_ttl.parse_(wiki, Xow_ns_.Id_file, ttl.Page_db());	// if [[Media:]] change to [[File:]]; theoretically, this should be changed in Get_page, but not sure if I want to put this logic that low; DATE:2014-01-07
		// Xoa_page file_page = Pfunc_filepath.Load_page(wiki, ttl);	// EXPENSIVE
		// boolean exists = !file_page.Missing();
		// if (!exists) return rslt.Init_obj(KeyVal_.Ary(KeyVal_.new_("exists", false)));	// NOTE: do not reinstate; will exit early if commons is not installed; DATE:2015-01-25; NOTE: Media objects are often flagged as absent in offline mode
		// NOTE: MW registers image if deleted; XOWA doesn't register b/c needs width / height also, not just image name
		Xof_file_meta_itm itm = wiki.File_mgr().File_meta_wkr().Get_or_null(ttl.Page_db());
		if (itm == null) return rslt.Init_obj(GetFileInfo_absent);
		KeyVal[] rv = KeyVal_.Ary
		( KeyVal_.new_("exists"		, true)
		, KeyVal_.new_("width"		, itm.W())
		, KeyVal_.new_("height"		, itm.H())
		, KeyVal_.new_("pages"		, null)	// TODO: get pages info
		);
		return rslt.Init_obj(rv);
	}
	private static final KeyVal[] GetFileInfo_absent = KeyVal_.Ary(KeyVal_.new_("exists", false));
	public boolean GetContent(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte[] ttl_bry = args.Pull_bry(0);
		Xow_wiki wiki = core.Wiki();
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry); if (ttl == null) return rslt.Init_obj(null);
		Xow_page_cache_itm page_itm = wiki.Cache_mgr().Page_cache().Get_or_load_as_itm(ttl);
		byte[] rv = null;
		if (page_itm != null) {
			byte[] redirected_src = page_itm.Redirected_src_wtxt();
			if (redirected_src != null) {						// page is redirect; use its src, not its target's src; DATE:2014-07-11
				rv = redirected_src;
				core.Frame_parent().Rslt_is_redirect_(true);	// flag frame as redirect, so that \n won't be prepended; EX:"#REDIRECT" x> "\n#REDIRECT"
			}
			else
				rv = page_itm.Wtxt();
		}
		return rv == null ? rslt.Init_obj(null) : rslt.Init_obj(String_.new_utf8_(rv));
	}
	public boolean GetCurrentTitle(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		return rslt.Init_obj(GetInexpensiveTitleData(core.Page().Ttl()));
	}
	public boolean ProtectionLevels(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		// byte[] ttl_bry = args.Pull_bry(0);
		// Xow_wiki wiki = core.Wiki();
		// Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry); if (ttl == null) return rslt.Init_obj(null);
		return rslt.Init_many_objs("");	// NOTE: always return no protection; protectionLevels are stored in different table which is currently not mirrored; DATE:2014-04-09
	}
	public boolean CascadingProtection(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte[] ttl_bry = args.Pull_bry(0);
		Xow_wiki wiki = core.Wiki();
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry); if (ttl == null) return rslt.Init_obj(null);
		return rslt.Init_obj(CascadingProtection_rv);
	}
	public static final KeyVal[] CascadingProtection_rv = KeyVal_.Ary(KeyVal_.new_("sources", Bool_.N), KeyVal_.new_("restrictions", KeyVal_.Ary_empty));
	private KeyVal[] GetInexpensiveTitleData(Xoa_ttl ttl) {
		Xow_ns ns = ttl.Ns();
		boolean ns_file_or_media = ns.Id_file_or_media(), ns_special = ns.Id_special();
		int rv_len = 7, rv_idx = 7;
		if (ns_special) ++rv_len;
		if (!ns_file_or_media) ++rv_len;
		Xow_xwiki_itm xwiki_itm = ttl.Wik_itm();
		String xwiki_str = xwiki_itm == null ? "" : xwiki_itm.Key_str();
		KeyVal[] rv = new KeyVal[rv_len];
		rv[ 0] = KeyVal_.new_("isLocal"				, true);								// title.isLocal; NOTE: always true; passing "http:" also returns true; not sure how to handle "Interwiki::fetch( $this->mInterwiki )->isLocal()"
		rv[ 1] = KeyVal_.new_("interwiki"			, xwiki_str);							// $title->getInterwiki(),
		rv[ 2] = KeyVal_.new_("namespace"		, ns.Id());								// $ns,
		rv[ 3] = KeyVal_.new_("nsText"				, ns.Name_str());						// $title->getNsText(),
		rv[ 4] = KeyVal_.new_("text"				, ttl.Page_txt());						// $title->getText(),
		rv[ 5] = KeyVal_.new_("fragment"			, ttl.Anch_txt());						// $title->getFragment(),
		rv[ 6] = KeyVal_.new_("thePartialUrl"		, ttl.Page_db());						// $title->getPartialUrl(),
		if (ns_special)
			rv[rv_idx++] = KeyVal_.new_("exists"	, false);								// TODO: lookup specials
		if (!ns_file_or_media)
			rv[rv_idx++] = KeyVal_.new_("file"		, false);								// REF.MW: if ( $ns !== NS_FILE && $ns !== NS_MEDIA )  $ret['file'] = false;
		return rv;
	}	private static final Xodb_page tmp_db_page = Xodb_page.tmp_();
	public static final String Key_wikitexet = "wikitext";
}

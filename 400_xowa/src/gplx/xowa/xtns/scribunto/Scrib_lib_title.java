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
class Scrib_lib_title implements Scrib_lib {
	public Scrib_lib_title(Scrib_engine engine) {this.engine = engine;} Scrib_engine engine;
	public Scrib_mod Mod() {return mod;} Scrib_mod mod;
	public Scrib_mod Register(Scrib_engine engine, Io_url script_dir) {
		mod = engine.RegisterInterface(script_dir.GenSubFil("mw.title.lua"), this
			, String_.Ary(Invk_newTitle, Invk_makeTitle, Invk_getUrl, Invk_getContent, Invk_fileExists, Invk_getCurrentTitle)
			, KeyVal_.new_("thisTitle", "")					// NOTE: pass blank; will be updated by GetCurrentTitle
			, KeyVal_.new_("NS_MEDIA", Xow_ns_.Id_media)	// NOTE: MW passes down NS_MEDIA; this should be -2 on all wikis...
			);
		notify_page_changed_fnc = mod.Fncs_get_by_key("notify_page_changed");
		return mod;
	}	Scrib_fnc notify_page_changed_fnc;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_newTitle))			return NewTitle((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_makeTitle))			return MakeTitle((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_getUrl))				return GetUrl((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_getContent))			return GetContent((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_fileExists))			return FileExists((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_getCurrentTitle))	return GetCurrentTitle((KeyVal[])m.CastObj("v"));
		else	return GfoInvkAble_.Rv_unhandled;
	}
	public static final String Invk_newTitle = "newTitle", Invk_makeTitle = "makeTitle", Invk_getUrl = "getUrl", Invk_getContent = "getContent", Invk_fileExists = "fileExists", Invk_getCurrentTitle = "getCurrentTitle";
	public KeyVal[] NewTitle(KeyVal[] values) {
		byte[] ttl_bry = Scrib_kv_utl.Val_to_bry(values, 0);
		Object ns_obj = Scrib_kv_utl.Val_to_obj_or(values, 1, null);
		Xow_wiki wiki = engine.Wiki();
		byte[] ns_bry = null;
		if (ns_obj != null) {
			ns_bry = Parse_ns(wiki, ns_obj); if (ns_bry == null) throw Err_.new_fmt_("unknown ns: {0}", Object_.XtoStr_OrEmpty(ns_bry));
		}
		if (ns_bry != null) {
			ByteAryBfr bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
			ttl_bry = bfr.Add(ns_bry).Add_byte(Byte_ascii.Colon).Add(ttl_bry).Mkr_rls().XtoAryAndClear();
		}
		Xoa_ttl ttl = Xoa_ttl.parse_(engine.Wiki(), ttl_bry);
		if (ttl == null) return Scrib_kv_utl.base1_obj_(null);	// invalid title; exit; matches MW logic
		return Scrib_kv_utl.base1_obj_(Xto_kv_ary(ttl));
	}
	public void Notify_page_changed() {if (notify_page_changed_fnc != null) engine.Interpreter().CallFunction(notify_page_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public KeyVal[] GetUrl(KeyVal[] values) {
		byte[] ttl_bry = Scrib_kv_utl.Val_to_bry(values, 0);
		byte[] url_func_bry = Scrib_kv_utl.Val_to_bry(values, 1);
		Object url_func_obj = url_func_hash.Fetch(url_func_bry);
		if (url_func_obj == null) throw Err_.new_fmt_("url_function is not valid: {0}", String_.new_utf8_(url_func_bry));
		byte url_func_tid = ((ByteVal)url_func_obj).Val();
		byte[] qry_bry = Scrib_kv_utl.Val_to_bry_or(values, 2, null);
		if (qry_bry == null || qry_bry.length == 0) qry_bry = ByteAry_.Empty;
	//		byte[] proto = Scrib_kv_utl.Val_to_bry_or(values, 3, null);
		Xow_wiki wiki = engine.Wiki();
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry); if (ttl == null) return Scrib_kv_utl.base1_obj_(null);
		ByteAryBfr bfr = wiki.App().Utl_bry_bfr_mkr().Get_b512();
	//		if (url_func_tid == Pf_url_urlfunc.Tid_full) {
	//			if (proto == null) proto = Proto_relative;
	//			Object proto_obj = proto_hash.Fetch(proto); if (proto_obj == null) throw Err_.new_fmt_("protocol is not valid: {0}", proto);
	//			//qry_bry = (byte[])proto_obj;
	//			byte proto_tid = ((ByteVal)proto_obj).Val();
	//			bfr.Add();
	//		}
		Pf_url_urlfunc.UrlString(engine.Ctx(), url_func_tid, false, ttl_bry, bfr,  qry_bry);
		return Scrib_kv_utl.base1_obj_(bfr.Mkr_rls().XtoStrAndClear());
	}
	private static final Hash_adp_bry url_func_hash = new Hash_adp_bry(false).Add_str_byteVal("fullUrl", Pf_url_urlfunc.Tid_full).Add_str_byteVal("localUrl", Pf_url_urlfunc.Tid_local).Add_str_byteVal("canonicalUrl", Pf_url_urlfunc.Tid_canonical);
	// private static final byte[] Proto_relative = ByteAry_.new_ascii_("relative");
	// private static final Hash_adp_bry proto_hash = new Hash_adp_bry(false).Add_str_obj("http", ByteAry_.new_ascii_("http://")).Add_str_obj("https", ByteAry_.new_ascii_("https://")).Add_str_obj("relative", ByteAry_.new_ascii_("//")).Add_str_obj("canonical", ByteAry_.new_ascii_("1"));

	byte[] Parse_ns(Xow_wiki wiki, Object ns_obj) {
		if (ClassAdp_.Eq_typeSafe(ns_obj, String.class))
			return ByteAry_.new_utf8_(String_.cast_(ns_obj));
		else {
			int ns_id = Int_.cast_(ns_obj);
			Xow_ns ns = wiki.Ns_mgr().Get_by_id(ns_id);
			if (ns != null)
				return ns.Name_bry();
		}
		return null;
	}
	public KeyVal[] MakeTitle(KeyVal[] values) {
		Xow_wiki wiki = engine.Wiki();
		byte[] ns_bry = Parse_ns(wiki, Scrib_kv_utl.Val_to_obj(values, 0));
		String ttl_str = Scrib_kv_utl.Val_to_str(values, 1);
		String anchor_str = Scrib_kv_utl.Val_to_str_or(values, 2, null);
		String xwiki_str = Scrib_kv_utl.Val_to_str_or(values, 3, null);
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_k004();
		if (xwiki_str != null) tmp_bfr.Add_str(xwiki_str).Add_byte(Byte_ascii.Colon);		
		tmp_bfr.Add(ns_bry).Add_byte(Byte_ascii.Colon).Add_str(ttl_str);
		if (anchor_str != null) tmp_bfr.Add_byte(Byte_ascii.Hash).Add_str(anchor_str);
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, tmp_bfr.Mkr_rls().XtoAryAndClear());
		if (ttl == null) return Scrib_kv_utl.base1_obj_(null);	// invalid title; exit;
		return Scrib_kv_utl.base1_obj_(Xto_kv_ary(ttl));
	}
	public KeyVal[] FileExists(KeyVal[] values) {
		byte[] ttl_bry = Scrib_kv_utl.Val_to_bry(values, 0);
		Xow_wiki wiki = engine.Wiki();
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
		if (	ttl == null
			|| !ttl.Ns().Id_file_or_media()
			) return Scrib_kv_utl.base1_obj_(false);
		Xoa_page file_page = wiki.Data_mgr().Get_page(ttl, false);
		return Scrib_kv_utl.base1_obj_(!file_page.Missing());
	}
	public KeyVal[] GetContent(KeyVal[] values) {
		byte[] ttl_bry = Scrib_kv_utl.Val_to_bry(values, 0);
		Xow_wiki wiki = engine.Wiki();
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry); if (ttl == null) return Scrib_kv_utl.base1_obj_(null);
		Xoa_page page = wiki.Data_mgr().Get_page(ttl, false); if (page.Missing()) return Scrib_kv_utl.base1_obj_(null);
		return Scrib_kv_utl.base1_obj_(String_.new_utf8_(page.Data_raw()));
	}
	public KeyVal[] GetCurrentTitle(KeyVal[] values) {
		return Scrib_kv_utl.base1_obj_(Xto_kv_ary(engine.Ctx().Page().Page_ttl()));
	}
	KeyVal[] Xto_kv_ary(Xoa_ttl ttl) {
		Xow_ns ns = ttl.Ns();
		boolean ttl_exists = engine.Wiki().Db_mgr().Load_mgr().Load_ttl(tmp_db_page, ttl.Ns(), ttl.Page_db());
		boolean ttl_redirect = false; int ttl_id = 0;
		if (ttl_exists) {
			ttl_redirect = tmp_db_page.Type_redirect();
			ttl_id = tmp_db_page.Id();
		}
		boolean ttl_file_or_media = ns.Id_file_or_media();
		int rv_len = ttl_file_or_media ? 11 : 12;
		Xow_xwiki_itm xwiki_itm = ttl.Wik_itm();
		String xwiki_str = xwiki_itm == null ? "" : xwiki_itm.Key_str();
		KeyVal[] rv = new KeyVal[rv_len];
		rv[ 0] = KeyVal_.new_("isLocal"				, true);								// title.isLocal; NOTE: always true; passing "http:" also returns true; not sure how to handle "Interwiki::fetch( $this->mInterwiki )->isLocal()"
		rv[ 1] = KeyVal_.new_("isRedirect"			, ttl_redirect);						// title.isRedirect
		rv[ 2] = KeyVal_.new_("interwiki"			, xwiki_str);							// $title->getInterwiki(),
		rv[ 3] = KeyVal_.new_("namespace"		, ns.Id());								// $ns,
		rv[ 4] = KeyVal_.new_("nsText"				, ns.Name_str());						// $title->getNsText(),
		rv[ 5] = KeyVal_.new_("text"				, ttl.Page_txt());						// $title->getText(),
		rv[ 6] = KeyVal_.new_("id"					, ttl_id);								// $title->getArticleID(),
		rv[ 7] = KeyVal_.new_("fragment"			, ttl.Anch_txt());						// $title->getFragment(),
		rv[ 8] = KeyVal_.new_("contentModel"		, Xoa_content_model.Key_wikitexet);		// $title->getContentModel(); see Defines.php and CONTENT_MODEL_
		rv[ 9] = KeyVal_.new_("thePartialUrl"		, ttl.Page_db());						// $title->getPartialUrl(),
		rv[10] = KeyVal_.new_("exists"				, ttl_exists);							// $ret['id'] > 0; TODO: if Special: check regy of implemented pages
		if (!ttl_file_or_media)
			rv[11] = KeyVal_.new_("fileExists"		, false);								// REF.MW: if ( $ns !== NS_FILE && $ns !== NS_MEDIA )  $ret['fileExists'] = false;
		return rv;
	}	static final Xodb_page tmp_db_page = Xodb_page.tmp_();
}
class Xoa_content_model {
	public static final String Key_wikitexet = "wikitext";
}

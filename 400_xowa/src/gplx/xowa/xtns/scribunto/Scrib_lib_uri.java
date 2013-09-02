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
class Scrib_lib_uri implements Scrib_lib {
	public Scrib_lib_uri(Scrib_engine engine) {this.engine = engine;} Scrib_engine engine;
	public Scrib_mod Mod() {return mod;} Scrib_mod mod;
	public Scrib_mod Register(Scrib_engine engine, Io_url script_dir) {
		mod = engine.RegisterInterface(script_dir.GenSubFil("mw.uri.lua"), this, String_.Ary
		(	Invk_anchorEncode, Invk_localUrl, Invk_fullUrl, Invk_canonicalUrl, Invk_init_uri_for_page));	// NOTE: defaultUrl handled by Init_lib_url
		notify_page_changed_fnc = mod.Fncs_get_by_key("notify_page_changed");
		return mod;
	}	Scrib_fnc notify_page_changed_fnc;
	public void Notify_page_changed() {if (notify_page_changed_fnc != null) engine.Interpreter().CallFunction(notify_page_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_anchorEncode))			return AnchorEncode((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_localUrl))				return Url_func((KeyVal[])m.CastObj("v"), Pf_url_urlfunc.Tid_local);
		else if	(ctx.Match(k, Invk_fullUrl))				return Url_func((KeyVal[])m.CastObj("v"), Pf_url_urlfunc.Tid_full);
		else if	(ctx.Match(k, Invk_canonicalUrl))			return Url_func((KeyVal[])m.CastObj("v"), Pf_url_urlfunc.Tid_canonical);
		else if	(ctx.Match(k, Invk_init_uri_for_page))		return Init_uri_for_page();
		else	return GfoInvkAble_.Rv_unhandled;
	}
	public static final String Invk_anchorEncode = "anchorEncode", Invk_localUrl = "localUrl", Invk_fullUrl = "fullUrl", Invk_canonicalUrl = "canonicalUrl", Invk_init_uri_for_page = "init_uri_for_page";
	public KeyVal[] AnchorEncode(KeyVal[] values) {
		byte[] raw_bry = Scrib_kv_utl.Val_to_bry(values, 0);
		ByteAryBfr bfr = engine.App().Utl_bry_bfr_mkr().Get_b512();
		ByteAryBfr tmp_bfr = engine.App().Utl_bry_bfr_mkr().Get_b512();
		Pf_url_anchorencode.Func_init(engine.Ctx());
		Pf_url_anchorencode.AnchorEncode(raw_bry, bfr, tmp_bfr);
		tmp_bfr.Mkr_rls().Clear();
		return Scrib_kv_utl.base1_obj_(bfr.Mkr_rls().XtoStrAndClear());
	}
	public KeyVal[] Url_func(KeyVal[] values, byte url_tid) {
		ByteAryBfr bfr = engine.App().Utl_bry_bfr_mkr().Get_b512();
		byte[] ttl_bry = Scrib_kv_utl.Val_to_bry(values, 0);
		byte[] qry_bry = Scrib_kv_utl.Val_to_bry_or(values, 1, ByteAry_.Empty);
		Xoa_ttl ttl = Xoa_ttl.parse_(engine.Wiki(), ttl_bry);
		if (ttl.Ns().Id() == Xow_ns_.Id_media) {	// change "Media:" -> "File:"
			bfr.Add(engine.Wiki().Ns_mgr().Ns_file().Name_db_w_colon());
			bfr.Add(ttl.Page_db());
			ttl_bry = bfr.XtoAryAndClear();
		}				
		Pf_url_urlfunc.UrlString(engine.Ctx(), url_tid, false, ttl_bry, bfr, qry_bry);
		return Scrib_kv_utl.base1_obj_(bfr.Mkr_rls().XtoStrAndClear());
	}
	KeyVal[] Init_uri_for_page() {
		Xop_ctx ctx = engine.Ctx();
		byte[] ttl_bry = ctx.Page().Page_ttl().Raw();
		ByteAryBfr tmp_bfr = ctx.Wiki().Utl_bry_bfr_mkr().Get_b512();
		Pf_url_urlfunc.UrlString(ctx, Pf_url_urlfunc.Tid_full, false, ttl_bry, tmp_bfr, ByteAry_.Empty);
		return Scrib_kv_utl.base1_obj_(tmp_bfr.Mkr_rls().XtoAryAndClear());
	}
}

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
public class Scrib_lib_uri implements Scrib_lib {
	public Scrib_lib_uri(Scrib_core core) {this.core = core;} Scrib_core core;
	public Scrib_lua_mod Mod() {return mod;} Scrib_lua_mod mod;
	public Scrib_lib Init() {procs.Init_by_lib(this, Proc_names); return this;}
	public Scrib_lua_mod Register(Scrib_core core, Io_url script_dir) {
		Init();
		mod = core.RegisterInterface(this, script_dir.GenSubFil("mw.uri.lua"));	// NOTE: defaultUrl handled by Init_lib_url
		notify_page_changed_fnc = mod.Fncs_get_by_key("notify_page_changed");
		return mod;
	}	Scrib_lua_proc notify_page_changed_fnc;
	public void Notify_page_changed() {if (notify_page_changed_fnc != null) core.Interpreter().CallFunction(notify_page_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public Scrib_proc_mgr Procs() {return procs;} private Scrib_proc_mgr procs = new Scrib_proc_mgr();
	public boolean Procs_exec(int key, Scrib_proc_args args, Scrib_proc_rslt rslt) {
		switch (key) {
			case Proc_anchorEncode:							return AnchorEncode(args, rslt);
			case Proc_localUrl:								return Url_func(args, rslt, Pf_url_urlfunc.Tid_local);
			case Proc_fullUrl:								return Url_func(args, rslt, Pf_url_urlfunc.Tid_full);
			case Proc_canonicalUrl:							return Url_func(args, rslt, Pf_url_urlfunc.Tid_canonical);
			case Proc_init_uri_for_page:					return Init_uri_for_page(args, rslt);
			default: throw Err_.unhandled(key);
		}
	}
	private static final int Proc_anchorEncode = 0, Proc_localUrl = 1, Proc_fullUrl = 2, Proc_canonicalUrl = 3, Proc_init_uri_for_page = 4;
	public static final String Invk_anchorEncode = "anchorEncode", Invk_localUrl = "localUrl", Invk_fullUrl = "fullUrl", Invk_canonicalUrl = "canonicalUrl", Invk_init_uri_for_page = "init_uri_for_page";
	private static final String[] Proc_names = String_.Ary(Invk_anchorEncode, Invk_localUrl, Invk_fullUrl, Invk_canonicalUrl, Invk_init_uri_for_page);
	public boolean AnchorEncode(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte[] raw_bry = args.Pull_bry(0);
		ByteAryBfr bfr = core.App().Utl_bry_bfr_mkr().Get_b512();
		ByteAryBfr tmp_bfr = core.App().Utl_bry_bfr_mkr().Get_b512();
		Pf_url_anchorencode.Func_init(core.Ctx());
		Pf_url_anchorencode.Anchor_encode(raw_bry, bfr, tmp_bfr);
		tmp_bfr.Mkr_rls().Clear();
		return rslt.Init_obj(bfr.Mkr_rls().XtoStrAndClear());
	}
	public boolean Url_func(Scrib_proc_args args, Scrib_proc_rslt rslt, byte url_tid) {
		Xow_wiki wiki = core.Wiki();
		byte[] ttl_bry = args.Pull_bry(0);
		byte[] qry_bry = args.Extract_qry_args(wiki, 1);
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
		if (ttl == null) return rslt.Init_null();
		ByteAryBfr bfr = core.App().Utl_bry_bfr_mkr().Get_b512();
		if (ttl.Ns().Id() == Xow_ns_.Id_media) {	// change "Media:" -> "File:"
			bfr.Add(wiki.Ns_mgr().Ns_file().Name_db_w_colon());
			bfr.Add(ttl.Page_db());
			ttl_bry = bfr.XtoAryAndClear();
		}				
		Pf_url_urlfunc.UrlString(core.Ctx(), url_tid, false, ttl_bry, bfr, qry_bry);
		return rslt.Init_obj(bfr.Mkr_rls().XtoStrAndClear());
	}
	private boolean Init_uri_for_page(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		Xop_ctx ctx = core.Ctx();
		byte[] ttl_bry = ctx.Page().Ttl().Raw();
		ByteAryBfr tmp_bfr = ctx.Wiki().Utl_bry_bfr_mkr().Get_b512();
		Pf_url_urlfunc.UrlString(ctx, Pf_url_urlfunc.Tid_full, false, ttl_bry, tmp_bfr, ByteAry_.Empty);
		return rslt.Init_obj(tmp_bfr.Mkr_rls().XtoAryAndClear());
	}
}
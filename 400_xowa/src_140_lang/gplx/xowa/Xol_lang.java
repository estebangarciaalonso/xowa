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
package gplx.xowa; import gplx.*;
import gplx.xowa.langs.*; import gplx.xowa.langs.grammars.*; import gplx.intl.*; import gplx.xowa.xtns.lst.*;
public class Xol_lang implements GfoInvkAble {
	public Xol_lang(Xoa_app app, byte[] key_bry) {
		this.app = app; this.key_bry = key_bry; this.key_str = String_.new_utf8_(key_bry);
		Xol_lang_itm lang_itm = Xol_lang_itm_.Get_by_key(key_bry); if (lang_itm == null) throw Err_.new_fmt_("unknown lang_key: {0}", String_.new_utf8_(key_bry));
		lang_id = lang_itm.Id();
		func_regy = new Xot_func_regy(this);
		ns_names = new Xol_ns_grp(this); ns_aliases = new Xol_ns_grp(this);
		keyword_mgr = new Xol_kwd_mgr(this);
		message_mgr = new Xol_msg_mgr(this, true);
		fragment_mgr = new Xol_fragment_mgr(this);
		specials_mgr = new Xol_specials_mgr(this);
		if (Env_.Mode_testing() && lang_id == Xol_lang_itm_.Id_en)	// NOTE: if test (and english) load case_mgr; NOTE: placed here b/c tests do not call load; NOTE: using English b/c Universal is large
			case_mgr.Add_bulk(Xol_case_itm_.English);
		grammar = Xol_grammar_.new_by_lang_id(lang_id);
		lnki_trail_mgr = new Xol_lnki_trail_mgr(this);
	}
	public Xoa_app App() {return app;} private Xoa_app app;
	public byte[] Key_bry() {return key_bry;} private byte[] key_bry; public String Key_str() {return key_str;} private String key_str;
	public int Lang_id() {return lang_id;} private int lang_id;
	public boolean Dir_ltr() {return dir_ltr;} public void Dir_ltr_(boolean v) {dir_ltr = v;} private boolean dir_ltr = true;
	public byte[] Dir_bry() {return dir_ltr ? Dir_bry_ltr : Dir_bry_rtl;}
	public Xol_ns_grp Ns_names() {return ns_names;} private Xol_ns_grp ns_names;
	public Xol_ns_grp Ns_aliases() {return ns_aliases;} private Xol_ns_grp ns_aliases;
	public Xol_kwd_mgr Kwd_mgr() {return keyword_mgr;} private Xol_kwd_mgr keyword_mgr;
	public Xol_msg_mgr Msg_mgr() {return message_mgr;} private Xol_msg_mgr message_mgr;
	public Xol_case_mgr Case_mgr() {return case_mgr;} private Xol_case_mgr case_mgr = new Xol_case_mgr();
	public Xol_font_info Gui_font() {return gui_font;} private Xol_font_info gui_font = new Xol_font_info(null, 0, gplx.gfui.FontStyleAdp_.Plain);
	public byte[] Fallback_bry() {return fallback_bry;} public Xol_lang Fallback_bry_(byte[] v) {fallback_bry = v; return this;} private byte[] fallback_bry;
	public boolean Variants_enabled() {return variants_enabled;} public Xol_lang Variants_enabled_(boolean v) {variants_enabled = v; return this;} private boolean variants_enabled;
	public Xol_fragment_mgr Fragment_mgr() {return fragment_mgr;} private Xol_fragment_mgr fragment_mgr;
	public Xol_grammar Grammar() {return grammar;} private Xol_grammar grammar;
	public Xol_lnki_trail_mgr Lnki_trail_mgr() {return lnki_trail_mgr;} private Xol_lnki_trail_mgr lnki_trail_mgr;
	public Xol_specials_mgr Specials_mgr() {return specials_mgr;} private Xol_specials_mgr specials_mgr;
	
	public Xol_lnki_arg_parser Lnki_arg_parser() {return lnki_arg_parser;} private Xol_lnki_arg_parser lnki_arg_parser = new Xol_lnki_arg_parser(); 
	public Xot_func_regy Func_regy() {return func_regy;} private Xot_func_regy func_regy;

	public Gfo_num_fmt_mgr Num_fmt_mgr() {return num_fmt_mgr;} Gfo_num_fmt_mgr num_fmt_mgr = new Gfo_num_fmt_mgr();
	public byte Img_thumb_halign_default() {return Xop_lnki_halign.Right;}	// change for rtl languages

	public Hash_adp_bry Xatrs_ref() {return xatrs_ref;} Hash_adp_bry xatrs_ref = new Hash_adp_bry(false);
	public Hash_adp_bry Xatrs_references() {return xatrs_references;} Hash_adp_bry xatrs_references = new Hash_adp_bry(false);
	public Hash_adp_bry Xatrs_gallery() {return xatrs_gallery;} Hash_adp_bry xatrs_gallery = new Hash_adp_bry(false);
	public Hash_adp_bry Xatrs_pages() {return xatrs_pages;} Hash_adp_bry xatrs_pages = new Hash_adp_bry(false);
	public Hash_adp_bry Xatrs_section() {if (xatrs_section == null) xatrs_section = Xtn_lst.new_xatrs_(this); return xatrs_section;} Hash_adp_bry xatrs_section;
	public void Evt_lang_changed() {
		lnki_arg_parser.Evt_lang_changed(this);
		func_regy.Evt_lang_changed(this);
	}	boolean loaded = false;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_ns_names))				return ns_names;
		else if	(ctx.Match(k, Invk_ns_aliases))				return ns_aliases;
		else if	(ctx.Match(k, Invk_keywords))				return keyword_mgr;
		else if	(ctx.Match(k, Invk_messages))				return message_mgr;
		else if	(ctx.Match(k, Invk_specials))				return specials_mgr;
		else if	(ctx.Match(k, Invk_casings))				return case_mgr;
		else if	(ctx.Match(k, Invk_dir_rtl_))				dir_ltr = !m.ReadYn("v");
		else if	(ctx.Match(k, Invk_dir_str))				return Dir_bry();
		else if	(ctx.Match(k, Invk_gui_font_))				gui_font.Name_(m.ReadStr("name")).Size_(m.ReadFloatOr("size", 8));
		else if	(ctx.Match(k, Invk_fallback_load))			Exec_fallback_load(m.ReadBry("v"));
		else if (ctx.Match(k, Invk_variants_enabled_))		variants_enabled = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_num_fmt))				return num_fmt_mgr;
		else if	(ctx.Match(k, Invk_link_trail))				return lnki_trail_mgr;
		else if	(ctx.Match(k, Invk_this))					return this;
		else if	(ctx.Match(k, Xoa_gfs_mgr.Invk_app))		return app;
		else												return GfoInvkAble_.Rv_unhandled;
		return this;
	}	public static final String Invk_ns_names = "ns_names", Invk_ns_aliases = "ns_aliases", Invk_keywords = "keywords", Invk_messages = "messages", Invk_specials = "specials", Invk_casings = "casings", Invk_dir_rtl_ = "dir_rtl_", Invk_gui_font_ = "gui_font_"
			, Invk_fallback_load = "fallback_load", Invk_this = "this", Invk_variants_enabled_ = "variants_enabled_", Invk_num_fmt = "num_fmt", Invk_dir_str = "dir_str", Invk_link_trail = "link_trail";
	public Xol_lang Load_assert(Xoa_app app) {if (!loaded) Load(app); return this;}
	public boolean Load(Xoa_app app) {
		if (this.loaded) return false;
		app.Lang_mgr().Fallback_regy().Clear();
		this.loaded = true;
		this.app = app;
		if (!ByteAry_.Eq(key_bry, Xol_lang_.Key_en)) Xol_lang_.Lang_init(this);
		case_mgr.Add_bulk(lang_id == Xol_lang_itm_.Id_en && Env_.Mode_testing() ? Xol_case_itm_.English : Xol_case_itm_.Universal);
		Xol_msg_itm msg_itm = message_mgr.Itm_by_key_or_new(ByteAry_.new_utf8_("Lang"));
		msg_itm.Atrs_set(key_bry, false, false);
		Load_lang(key_bry);
		ns_aliases.Ary_add_(Xow_ns_.Canonical);	// NOTE: always add English canonical as aliases to all languages
		this.Evt_lang_changed();
		return true;
	}
	void Exec_fallback_load(byte[] v) {
		if (app.Lang_mgr().Fallback_regy().Has(v)) return;
		app.Lang_mgr().Fallback_regy().Add(v, v);
		fallback_bry = v;
		Load_lang(v);
		app.Lang_mgr().Fallback_regy().Del(v);
	}
	void Load_lang(byte[] v) {
		app.Gfs_mgr().Run_url_for(this, Xol_lang_.xo_lang_fil_(app, String_.new_ascii_(v)));
	}
	private static final byte[] Dir_bry_ltr = ByteAry_.new_ascii_("ltr"), Dir_bry_rtl = ByteAry_.new_ascii_("rtl");
	public static final int Tid_lower = 1, Tid_upper = 2;
}

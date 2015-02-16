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
import gplx.xowa.apps.*; import gplx.xowa.langs.*; import gplx.intl.*; import gplx.xowa.xtns.lst.*; import gplx.xowa.wikis.caches.*;
import gplx.xowa.langs.cases.*; import gplx.xowa.langs.cnvs.*; import gplx.xowa.langs.grammars.*; import gplx.xowa.langs.plurals.*; import gplx.xowa.langs.vnts.*; import gplx.xowa.langs.numbers.*; import gplx.xowa.langs.durations.*;
import gplx.xowa.parsers.lnkis.*;
public class Xol_lang implements GfoInvkAble {
	public Xol_lang(Xoa_app app, byte[] key_bry) {
		this.app = app; this.key_bry = key_bry; this.key_str = String_.new_utf8_(key_bry);
		Xol_lang_itm lang_itm = Xol_lang_itm_.Get_by_key(key_bry); if (lang_itm == null) throw Err_.new_fmt_("unknown lang_key: {0}", String_.new_utf8_(key_bry));
		lang_id = lang_itm.Id();
		func_regy = new Xol_func_name_regy(this);
		ns_names = new Xol_ns_grp(this); ns_aliases = new Xol_ns_grp(this);
		keyword_mgr = new Xol_kwd_mgr(this);
		message_mgr = new Xol_msg_mgr(this, true);
		specials_mgr = new Xol_specials_mgr(this);
		case_mgr = Env_.Mode_testing() ? Xol_case_mgr_.Ascii() : Xol_case_mgr_.Utf8(); // NOTE: if test load ascii b/c utf8 is large; NOTE: placed here b/c tests do not call load; DATE:2014-07-04
		if (lang_id != Xol_lang_itm_.Id_en) fallback_bry_ary = Fallback_bry_ary__en;	// NOTE: do not set fallback_ary for en to en, else recursive loop
		grammar = Xol_grammar_.new_by_lang_id(lang_id);
		plural = Xol_plural_.new_by_lang_id(lang_id);
		num_mgr = Xol_num_mgr_.new_by_lang_id(lang_id);
		duration_mgr = new Xol_duration_mgr(this);
		lnki_trail_mgr = new Xol_lnki_trail_mgr(this);
		vnt_mgr = new Xol_vnt_mgr(this);
		cnv_mgr = new Xol_cnv_mgr(this);
	}
	public Xoa_app App() {return app;} private Xoa_app app;
	public byte[] Key_bry() {return key_bry;} private byte[] key_bry; public String Key_str() {return key_str;} private String key_str;
	public int Lang_id() {return lang_id;} private int lang_id;
	public boolean Dir_ltr() {return dir_ltr;} 
	public void Dir_ltr_(boolean v) {
		dir_ltr = v;
		img_thumb_halign_default = dir_ltr ? Xop_lnki_align_h.Right : Xop_lnki_align_h.Left;
	}	private boolean dir_ltr = true;
	public byte[] Dir_bry() {return dir_ltr ? Dir_bry_ltr : Dir_bry_rtl;}
	public byte[] X_axis_end() {return dir_ltr ? X_axis_end_right : X_axis_end_left;}
	public Xol_ns_grp Ns_names() {return ns_names;} private Xol_ns_grp ns_names;
	public Xol_ns_grp Ns_aliases() {return ns_aliases;} private Xol_ns_grp ns_aliases;
	public Xol_kwd_mgr Kwd_mgr() {return keyword_mgr;} private Xol_kwd_mgr keyword_mgr;
	public Xol_msg_mgr Msg_mgr() {return message_mgr;} private Xol_msg_mgr message_mgr;
	public Xol_case_mgr Case_mgr() {return case_mgr;} private Xol_case_mgr case_mgr;
	public void Case_mgr_utf8_() {case_mgr = Xol_case_mgr_.Utf8();}		// TEST:
	public Xol_font_info Gui_font() {return gui_font;} private Xol_font_info gui_font = new Xol_font_info(null, 0, gplx.gfui.FontStyleAdp_.Plain);
	public byte[] Fallback_bry() {return fallback_bry;}
	public Xol_lang Fallback_bry_(byte[] v) {
		fallback_bry = v;
		fallback_bry_ary = Fallbacy_bry_ary__bld(v);
		return this;
	}	private byte[] fallback_bry;
	public byte[][] Fallback_bry_ary() {return fallback_bry_ary;} private byte[][] fallback_bry_ary = Bry_.Ary_empty;
	public Xol_vnt_mgr Vnt_mgr() {return vnt_mgr;} private Xol_vnt_mgr vnt_mgr;
	public Xol_cnv_mgr Cnv_mgr() {return cnv_mgr;} private Xol_cnv_mgr cnv_mgr;
	public Xol_num_mgr Num_mgr() {return num_mgr;} private Xol_num_mgr num_mgr;
	public Xol_grammar Grammar() {return grammar;} private Xol_grammar grammar;
	public Xol_plural Plural() {return plural;} private Xol_plural plural;
	public Xol_duration_mgr Duration_mgr() {return duration_mgr;} private Xol_duration_mgr duration_mgr;
	public Xol_lnki_trail_mgr Lnki_trail_mgr() {return lnki_trail_mgr;} private Xol_lnki_trail_mgr lnki_trail_mgr;
	public Xol_specials_mgr Specials_mgr() {return specials_mgr;} private Xol_specials_mgr specials_mgr;
	
	public Xop_lnki_arg_parser Lnki_arg_parser() {return lnki_arg_parser;} private Xop_lnki_arg_parser lnki_arg_parser = new Xop_lnki_arg_parser(); 
	public Xol_func_name_regy Func_regy() {return func_regy;} private Xol_func_name_regy func_regy;
	public byte Img_thumb_halign_default() {return img_thumb_halign_default;} private byte img_thumb_halign_default = Xop_lnki_align_h.Right;
	public Hash_adp_bry Xatrs_section() {if (xatrs_section == null) xatrs_section = Lst_section_nde.new_xatrs_(this); return xatrs_section;} private Hash_adp_bry xatrs_section;
	public void Evt_lang_changed() {
		lnki_arg_parser.Evt_lang_changed(this);
		func_regy.Evt_lang_changed(this);
	}	private boolean loaded = false;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_ns_names))				return ns_names;
		else if	(ctx.Match(k, Invk_ns_aliases))				return ns_aliases;
		else if	(ctx.Match(k, Invk_keywords))				return keyword_mgr;
		else if	(ctx.Match(k, Invk_messages))				return message_mgr;
		else if	(ctx.Match(k, Invk_specials))				return specials_mgr;
		else if	(ctx.Match(k, Invk_casings))				return case_mgr;
		else if	(ctx.Match(k, Invk_converts))				return cnv_mgr;
		else if	(ctx.Match(k, Invk_variants))				return vnt_mgr;
		else if	(ctx.Match(k, Invk_dir_rtl_))				Dir_ltr_(!m.ReadYn("v"));
		else if	(ctx.Match(k, Invk_dir_str))				return Dir_bry();
		else if	(ctx.Match(k, Invk_gui_font_))				gui_font.Name_(m.ReadStr("name")).Size_(m.ReadFloatOr("size", 8));
		else if	(ctx.Match(k, Invk_fallback_load))			Exec_fallback_load(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_numbers))				return num_mgr;
		else if	(ctx.Match(k, Invk_link_trail))				return lnki_trail_mgr;
		else if	(ctx.Match(k, Invk_x_axis_end))				return String_.new_utf8_(X_axis_end());
		else if	(ctx.Match(k, Invk_this))					return this;
		else if	(ctx.Match(k, Xoa_app.Invk_app))			return app;
		else												return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_ns_names = "ns_names", Invk_ns_aliases = "ns_aliases"
	, Invk_keywords = "keywords", Invk_messages = "messages", Invk_specials = "specials", Invk_casings = "casings", Invk_converts = "converts", Invk_variants = "variants"
	, Invk_numbers = "numbers"
	, Invk_dir_rtl_ = "dir_rtl_", Invk_gui_font_ = "gui_font_"
	, Invk_fallback_load = "fallback_load", Invk_this = "this", Invk_dir_str = "dir_str", Invk_link_trail = "link_trail"
	, Invk_x_axis_end = "x_axis_end"
	;
	public Xol_lang Init_by_load_assert() {if (!loaded) Init_by_load(); return this;}
	public boolean Init_by_load() {
		if (this.loaded) return false;
		app.Lang_mgr().Fallback_regy().Clear();
		this.loaded = true;
		boolean lang_is_en = lang_id == Xol_lang_itm_.Id_en;
		if (!lang_is_en) Xol_lang_.Lang_init(this);
		message_mgr.Itm_by_key_or_new(Bry_.new_utf8_("Lang")).Atrs_set(key_bry, false, false);	// set "Lang" keyword; EX: for "fr", "{{int:Lang}}" -> "fr"
		Load_lang(key_bry);
		ns_aliases.Ary_add_(Xow_ns_.Canonical);	// NOTE: always add English canonical as aliases to all languages
		this.Evt_lang_changed();
		return true;
	}
	private void Exec_fallback_load(byte[] fallback_lang) {
		Fallback_bry_(fallback_lang);
		if (app.Lang_mgr().Fallback_regy().Has(fallback_lang)) return;			// fallback_lang loaded; avoid recursive loop; EX: zh with fallback of zh-hans which has fallback of zh
		if (Bry_.Eq(fallback_lang, Xoa_lang_mgr.Fallback_false)) return;	// fallback_lang is "none" exit
		app.Lang_mgr().Fallback_regy().Add(fallback_lang, fallback_lang);
		Load_lang(fallback_lang);
		app.Lang_mgr().Fallback_regy().Del(fallback_lang);
	}
	private void Load_lang(byte[] v) {
		app.Gfs_mgr().Run_url_for(this, Xol_lang_.xo_lang_fil_(app, String_.new_ascii_(v)));
		app.Gfs_mgr().Run_url_for(app, Xol_cnv_mgr.Bld_url(app, key_str));
	}
	private static final byte[] 
	  Dir_bry_ltr = Bry_.new_ascii_("ltr"), Dir_bry_rtl = Bry_.new_ascii_("rtl")
	, X_axis_end_right = Bry_.new_ascii_("right"), X_axis_end_left = Bry_.new_ascii_("left")
	;
	public static final int Tid_lower = 1, Tid_upper = 2;
	private static byte[][] Fallbacy_bry_ary__bld(byte[] v) {
		byte[][] rv = Bry_.Split(v, Byte_ascii.Comma, true); // gan -> 'gan-hant, zh-hant, zh-hans'
		boolean en_needed = true;
		int rv_len = rv.length;
		for (int i = 0; i < rv_len; i++) {
			byte[] itm = rv[i];
			if (Bry_.Eq(itm, Xol_lang_.Key_en)) {
				en_needed = false;
				break;
			}
		}
		if (en_needed) {
			int new_len = rv_len + 1;
			byte[][] new_ary  = new byte[new_len][];
			for (int i = 0; i < rv_len; i++)
				new_ary[i] = rv[i];
			new_ary[rv_len] = Xol_lang_.Key_en;
			rv = new_ary;
		}
		return rv;
	}
	private static final byte[][] Fallback_bry_ary__en = new byte[][] {Xol_lang_.Key_en};
}

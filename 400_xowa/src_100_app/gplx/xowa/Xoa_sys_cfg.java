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
public class Xoa_sys_cfg implements GfoInvkAble {
	private Xoa_app app;
	public Xoa_sys_cfg(Xoa_app app) {this.app = app;}
	public byte[] Lang() {return lang_key;}
	public Xoa_sys_cfg Lang_(byte[] v) {
		lang_key = Xol_lang_itm_.Get_by_key_or_en(v).Key();
		if (app.Stage() == gplx.xowa.apps.Xoa_stage_.Tid_launch) {	// do not update user lang unless launched; DATE:2014-05-26
			Xol_lang lang = app.Lang_mgr().Get_by_key_or_load(lang_key);
			app.User().Lang_(lang);
			app.User().Wiki().Html_mgr().Portal_mgr().Init();
		}
		return this;
	}	private byte[] lang_key = Xol_lang_.Key_en;
	public int Options_version() {return options_version;} public Xoa_sys_cfg Options_version_(int v) {options_version = v; return this;} private int options_version = 1;
	public KeyVal[] Options_lang_list() {if (options_lang_list == null) options_lang_list = Options_list_lang_.new_(); return options_lang_list;} private KeyVal[] options_lang_list;
	public long Free_mem_when() {return free_mem_when;} long free_mem_when;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_version))			return Xoa_app_.Version;
		else if	(ctx.Match(k, Invk_build_date))			return Xoa_app_.Build_date;
		else if	(ctx.Match(k, Invk_free_mem_when_))		free_mem_when = gplx.ios.Io_size_.parse_or_(m.ReadStr("v"), Io_mgr.Len_mb * 5);
		else if	(ctx.Match(k, Invk_lang))				return lang_key;
		else if	(ctx.Match(k, Invk_lang_))				Lang_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lang_list))			return Options_lang_list();
		else if	(ctx.Match(k, Invk_options_version))	return options_version;
		else if	(ctx.Match(k, Invk_options_version_))	options_version = m.ReadInt("v");
		else return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String Invk_version = "version", Invk_build_date = "build_date", Invk_free_mem_when_ = "free_mem_when_", Invk_options_version = "options_version", Invk_options_version_ = "options_version_"
		, Invk_lang = "lang", Invk_lang_ = "lang_", Invk_lang_list = "lang_list";
}
class Options_list_lang_ {
	public static KeyVal[] new_() {
		OrderedHash translated = OrderedHash_.new_bry_();
		ListAdp untranslated = ListAdp_.new_();
		Add_itm_many(translated, Xol_lang_itm_.Id_en, Xol_lang_itm_.Id_de, Xol_lang_itm_.Id_pl, Xol_lang_itm_.Id_zh_hans, Xol_lang_itm_.Id_zh_hant); // add langs with translations first, so they alphabetize to top of list			
		int len = Xol_lang_itm_.Id__max;
		for (int i = 0; i < len; i++) {	// add rest of langs, but sort by code
			Xol_lang_itm itm = Xol_lang_itm_.Get_by_id(i);
			if (translated.Has(itm.Key())) continue;
			untranslated.Add(itm);
		}
		untranslated.SortBy(Xol_lang_itm_.Comparer_code);

		KeyVal[] rv = new KeyVal[len];
		int translated_max = translated.Count();
		for (int i = 0; i < translated_max; i++)
			rv[i] = new_itm((Xol_lang_itm)translated.FetchAt(i));

		for (int i = translated_max; i < len; i++)
			rv[i] = new_itm((Xol_lang_itm)untranslated.FetchAt(i - translated_max));
		return rv;
	}
	private static KeyVal new_itm(Xol_lang_itm itm) {
		String key_str = String_.new_utf8_(itm.Key());
		String name_str = String_.new_utf8_(itm.Local_name());
		return KeyVal_.new_(key_str, name_str + " [" + key_str + "]");
	}
	private static void Add_itm_many(OrderedHash translated, int... langs) {
		int langs_len = langs.length;
		for (int i = 0; i < langs_len; i++) {
			Xol_lang_itm itm = Xol_lang_itm_.Get_by_id(langs[i]);
			translated.AddReplace(itm.Key(), itm);
		}
	}
}

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
	public Xoa_sys_cfg(Xoa_app app) {}
	public String Launch_url() {return launch_url;} public Xoa_sys_cfg Launch_url_(String v) {launch_url = v; return this;} private String launch_url = Launch_url_dflt; public static final String Launch_url_dflt = "home/wiki/Main_Page";
	public byte[] Lang() {return lang;} public Xoa_sys_cfg Lang_(byte[] v) {lang = v; return this;} private byte[] lang = Xol_lang_.Key_en;
	public int Options_version() {return options_version;} public Xoa_sys_cfg Options_version_(int v) {options_version = v; return this;} private int options_version = 1;
	public long Free_mem_when() {return free_mem_when;} long free_mem_when;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_version))			return Xoa_app_.Version;
		else if	(ctx.Match(k, Invk_build_date))			return Xoa_app_.Build_date;
		else if	(ctx.Match(k, Invk_free_mem_when_))		free_mem_when = gplx.ios.Io_size_.parse_or_(m.ReadStr("v"), Io_mgr.Len_mb * 5);
		else if	(ctx.Match(k, Invk_lang))				return lang;
		else if	(ctx.Match(k, Invk_lang_))				lang = Xol_lang_itm_.Get_by_key_or_en(m.ReadBry("v")).Key();
		else if	(ctx.Match(k, Invk_lang_list))			return Options_lang_list;
		else if	(ctx.Match(k, Invk_options_version))	return options_version;
		else if	(ctx.Match(k, Invk_options_version_))	options_version = m.ReadInt("v");
		else return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	static final String Invk_version = "version", Invk_build_date = "build_date", Invk_free_mem_when_ = "free_mem_when_", Invk_options_version = "options_version", Invk_options_version_ = "options_version_"
		, Invk_lang = "lang", Invk_lang_ = "lang_", Invk_lang_list = "lang_list";
	private static KeyVal[] Options_lang_list = KeyVal_.Ary(KeyVal_.new_("en", "English"), KeyVal_.new_("de", "Deutsch"), KeyVal_.new_("pl", "Polski")); 
}

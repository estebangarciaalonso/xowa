/*
XOWA: the extensible offline wiki application
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
class Xow_mainpage_finder {
	public static byte[] Find(Xow_wiki wiki) {
		byte[] rv = Find_by_mediawiki_page(wiki); if (rv != null) return rv;
		rv = Find_by_lang(wiki.Lang()); if (rv != null) return rv;
		return wiki.Props().Main_page();	// default to mainpage from siteinfo
	}
	private static byte[] Find_by_mediawiki_page(Xow_wiki wiki) {
		byte[] rv = wiki.Msg_mgr().Val_by_key_args(Msg_mainpage);
		return ByteAry_.Len_eq_0(rv) ? null : rv;	// NOTE: msg returns "" by default; return null instead
	}	
	public static final byte[] Ttl_mainpage = ByteAry_.new_ascii_("MediaWiki:Mainpage");
	private static byte[] Find_by_lang(Xol_lang lang) {
		Xol_msg_itm itm = lang.Msg_mgr().Itm_by_key_or_null(Msg_mainpage);
		return itm == null ? null : itm.Val();
	}
	public static final byte[] Msg_mainpage = ByteAry_.new_ascii_("mainpage");
}

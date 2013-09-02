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
public class Xow_wiki_type {
	public Xow_wiki_type(byte[] domain, byte[] lang_key, byte wiki_tid) {this.domain = domain; this.lang_key = lang_key; this.wiki_tid = wiki_tid;}
	public byte[] Domain() {return domain;} private byte[] domain;
	public byte[] Lang_key() {return lang_key;} private byte[] lang_key;
	public byte Wiki_tid() {return wiki_tid;} private byte wiki_tid;
	public static Xow_wiki_type new_custom_(byte[] domain) {return new Xow_wiki_type(domain, Xol_lang_itm_.Bry__null, Xow_wiki_type_.Tid_custom);}
}

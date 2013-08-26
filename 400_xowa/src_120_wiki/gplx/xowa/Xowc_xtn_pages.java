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
public class Xowc_xtn_pages implements GfoInvkAble {
	public boolean Init_needed() {return init_needed;} private boolean init_needed = true;
	public int Ns_page_id() {return ns_page_id;} private int ns_page_id = Int_.MinValue;
	public int Ns_page_talk_id() {return ns_page_talk_id;} private int ns_page_talk_id = Int_.MinValue;
	public int Ns_index_id() {return ns_index_id;} private int ns_index_id = Int_.MinValue;
	public int Ns_index_talk_id() {return ns_index_talk_id;} private int ns_index_talk_id = Int_.MinValue;
	public void Init(Xow_ns_mgr ns_mgr) {
		init_needed = false;
		int len = ns_mgr.Ords_len();
		for (int i = 0; i < len; i++) {
			Xow_ns ns = ns_mgr.Get_by_ord(i); if (ns == null) continue;
			byte[] ns_name = ns.Name_enc();
			if		(ByteAry_.Eq(ns_name, page_name))		ns_page_id = ns.Id();
			else if (ByteAry_.Eq(ns_name, page_talk_name))	ns_page_talk_id = ns.Id();
			else if (ByteAry_.Eq(ns_name, index_name))		ns_index_id = ns.Id();
			else if (ByteAry_.Eq(ns_name, index_talk_name))	ns_index_talk_id = ns.Id();
		}
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_ns_names_))				Ns_names_(m.ReadBry("page"), m.ReadBry("page_talk"), m.ReadBry("index"), m.ReadBry("index_talk"));
		else return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_ns_names_ = "ns_names_";
	void Ns_names_(byte[] page_name, byte[] page_talk_name, byte[] index_name, byte[] index_talk_name) {
		this.page_name = page_name; this.page_talk_name = page_talk_name; this.index_name = index_name; this.index_talk_name = index_talk_name;
	}	byte[] page_name = ByteAry_.new_ascii_("Page"), page_talk_name = ByteAry_.new_ascii_("Page_talk"), index_name = ByteAry_.new_ascii_("Index"), index_talk_name = ByteAry_.new_ascii_("Index_talk");	// NOTE: default to en
	public static final byte[] Key_pages = ByteAry_.new_ascii_("pages");
	public static final int Ns_page_id_default = 104;
}

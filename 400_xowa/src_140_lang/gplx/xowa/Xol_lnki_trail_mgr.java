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
import gplx.core.btries.*;
public class Xol_lnki_trail_mgr implements GfoInvkAble {
	public Xol_lnki_trail_mgr(Xol_lang lang) {}
	public void Clear() {trie.Clear();}
	public int Count() {return trie.Count();}
	public Btrie_slim_mgr Trie() {return trie;} Btrie_slim_mgr trie = Btrie_slim_mgr.cs_();
	public void Add(byte[] v) {trie.Add_obj(v, v);}
	public void Del(byte[] v) {trie.Del(v);}
	private void Add(String... ary) {
		for (String itm_str : ary) {
			byte[] itm = Bry_.new_utf8_(itm_str);
			trie.Add_obj(itm, itm);
		}
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_add_range)) 	Add_range(m);
		else if	(ctx.Match(k, Invk_add_many)) 	Add_many(m);
		else if	(ctx.Match(k, Invk_add_bulk)) 	Add_bulk(m);
		else if	(ctx.Match(k, Invk_clear)) 		Clear();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_add_many = "add_many", Invk_add_range = "add_range", Invk_add_bulk = "add_bulk", Invk_clear = "clear";
	private void Add_bulk(GfoMsg m) {byte[] src = m.ReadBry("bulk"); Add_bulk(src);}
	public void Add_bulk(byte[] src) {
		int pos = 0, src_len = src.length;
		while (true) {
			byte[] itm = gplx.intl.Utf8_.Get_char_at_pos_as_bry(src, pos);
			Add(itm);
			pos += itm.length;
			if (pos >= src_len) break;
		}
	}
	private void Add_many(GfoMsg m) {
		int len = m.Args_count();
		for (int i = 0; i < len; i++) {
			KeyVal kv = m.Args_getAt(i);
			Add(kv.Val_to_str_or_empty());
		}
	}
	private void Add_range(GfoMsg m) {
		byte bgn = Add_rng_extract(m, "bgn");
		byte end = Add_rng_extract(m, "end");
		for (byte i = bgn; i <= end; i++)
			Add(new byte[] {i});
	}
	public void Add_range(byte bgn, byte end) {
		for (byte i = bgn; i <= end; i++)
			Add(new byte[] {i});
	}
	byte Add_rng_extract(GfoMsg m, String key) {
		byte[] bry = m.ReadBry(key);
		if (bry.length != 1) throw Err_.new_fmt_("argument must be ascii character: ~{0} ~{1}", key, String_.new_utf8_(bry));
		return bry[0];
	}
}

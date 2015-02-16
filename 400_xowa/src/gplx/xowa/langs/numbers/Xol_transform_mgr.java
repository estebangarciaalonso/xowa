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
package gplx.xowa.langs.numbers; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
import gplx.core.btries.*;
public class Xol_transform_mgr implements GfoInvkAble {
	private Btrie_fast_mgr trie_k_to_v = Btrie_fast_mgr.cs_();
	private Btrie_fast_mgr trie_v_to_k = Btrie_fast_mgr.cs_();
	private OrderedHash hash = OrderedHash_.new_bry_();
	private boolean empty = true;
	public void Clear() {hash.Clear(); trie_k_to_v.Clear(); trie_v_to_k.Clear(); empty = true;}
	public int Len() {return hash.Count();}
	public KeyVal Get_at(int i) {return (KeyVal)hash.FetchAt(i);}
	public byte[] Get_val_or_self(byte[] k) {	// NOTE: return self; note that MW defaults "." and "," to self, even though MessagesLa.php only specifies ","; i.e.: always return something for "."; DATE:2014-05-13
		KeyVal kv = (KeyVal)hash.Fetch(k);
		return kv == null ? k : (byte[])kv.Val();
	}
	public Xol_transform_mgr Set(byte[] k, byte[] v) {
		trie_k_to_v.Add(k, v);
		trie_v_to_k.Add(v, k);
		KeyVal kv = KeyVal_.new_(String_.new_utf8_(k), v);
		hash.Del(k);
		hash.Add(k, kv);
		empty = false;
		return this;
	}
	public byte[] Replace(Bry_bfr tmp_bfr, byte[] src, boolean k_to_v) {
		if (empty || src == null) return src;
		int src_len = src.length; if (src_len == 0) return src;
		Btrie_fast_mgr trie = k_to_v ? trie_k_to_v : trie_v_to_k;
		return trie.Replace(tmp_bfr, src, 0, src_len);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_set))			Set(m.ReadBry("k"), m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_clear))			Clear();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_set = "set", Invk_clear = "clear";
}

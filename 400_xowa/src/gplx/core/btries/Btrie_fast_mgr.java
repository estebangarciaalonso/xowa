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
package gplx.core.btries; import gplx.*; import gplx.core.*;
import gplx.core.primitives.*;
public class Btrie_fast_mgr {
	private ByteTrieItm_fast root;
	public boolean CaseAny() {return root.CaseAny();} public Btrie_fast_mgr CaseAny_(boolean v) {root.CaseAny_(v); return this;}
	public int Match_pos() {return match_pos;} private int match_pos;
	public Object Match_exact(byte[] src, int bgn_pos, int end_pos) {
		Object rv = Match_bgn_w_byte(src[bgn_pos], src, bgn_pos, end_pos);
		return rv == null ? null : match_pos - bgn_pos == end_pos - bgn_pos ? rv : null;
	}
	public Object Match_bgn(byte[] src, int bgn_pos, int end_pos) {return Match_bgn_w_byte(src[bgn_pos], src, bgn_pos, end_pos);}
	public Object Match_bgn_w_byte(byte b, byte[] src, int bgn_pos, int src_len) {
		match_pos = bgn_pos;
		ByteTrieItm_fast nxt = root.Ary_find(b); if (nxt == null) return null;	// nxt does not have b; return rv;
		Object rv = null; int cur_pos = bgn_pos + 1;
		ByteTrieItm_fast cur = root;
		while (true) {
			if (nxt.Ary_is_empty()) {match_pos = cur_pos; return nxt.Val();}	// nxt is leaf; return nxt.Val() (which should be non-null)
			Object nxt_val = nxt.Val();
			if (nxt_val != null) {match_pos = cur_pos; rv = nxt_val;}			// nxt is node; cache rv (in case of false match)
			if (cur_pos == src_len) return rv;									// eos; exit
			b = src[cur_pos];
			cur = nxt;
			nxt = cur.Ary_find(b); if (nxt == null) return rv;
			++cur_pos;
		}
	}
	public Btrie_fast_mgr Add_bry_bval(byte   key, byte val) {return Add(new byte[] {key}, Byte_obj_val.new_(val));}
	public Btrie_fast_mgr Add_bry_bval(byte[] key, byte val) {return Add(key, Byte_obj_val.new_(val));}
	public Btrie_fast_mgr Add(byte key, Object val) {return Add(new byte[] {key}, val);}
	public Btrie_fast_mgr Add(String key, Object val) {return Add(Bry_.new_utf8_(key), val);}
	public Btrie_fast_mgr Add(byte[] key, Object val) {
		if (val == null) throw Err_.new_("null objects cannot be registered").Add("key", String_.new_utf8_(key));
		int key_len = key.length; int key_end = key_len - 1;
		ByteTrieItm_fast cur = root;
		for (int i = 0; i < key_len; i++) {
			byte b = key[i];
			ByteTrieItm_fast nxt = cur.Ary_find(b);
			if (nxt == null)
				nxt = cur.Ary_add(b, null);
			if (i == key_end)
				nxt.Val_set(val);
			cur = nxt;
		}
		return this;
	}
	public Btrie_fast_mgr Add_stub(byte tid, String s) {
		byte[] bry = Bry_.new_utf8_(s);
		Btrie_itm_stub stub = new Btrie_itm_stub(tid, bry);
		return Add(bry, stub);
	}
	public void Del(byte[] key) {
		int key_len = key.length;
		ByteTrieItm_fast cur = root;
		for (int i = 0; i < key_len; i++) {
			byte b = key[i];
			Object itm_obj = cur.Ary_find(b);
			if (itm_obj == null) break;	// b not found; no match; exit;
			ByteTrieItm_fast itm = (ByteTrieItm_fast)itm_obj;
			if (i == key_len - 1) {	// last char
				if (itm.Val() == null) break; // itm does not have val; EX: trie with "abc", and "ab" deleted
				if (itm.Ary_is_empty())
					cur.Ary_del(b);
				else
					itm.Val_set(null);
			}
			else {					// mid char; set itm as cur and continue
				cur = itm;
			}
		}
	}
	public void Clear() {root.Clear();}
	public byte[] Replace(Bry_bfr tmp_bfr, byte[] src, int bgn, int end) {
		int pos = bgn;
		boolean dirty = false;
		while (pos < end) {
			byte b = src[pos];
			Object o = this.Match_bgn_w_byte(b, src, pos, end);
			if (o == null) {
				if (dirty)
					tmp_bfr.Add_byte(b);
				pos++;
			}
			else {
				if (!dirty) {
					tmp_bfr.Add_mid(src, bgn, pos);
					dirty = true;
				}
				tmp_bfr.Add((byte[])o);
				pos = match_pos;
			}
		}
		return dirty ? tmp_bfr.Xto_bry_and_clear() : src;
	}
	public static Btrie_fast_mgr cs_()		{return new Btrie_fast_mgr(false);}
	public static Btrie_fast_mgr ci_ascii_()	{return new Btrie_fast_mgr(true);}
	public static Btrie_fast_mgr new_(boolean case_any) {return new Btrie_fast_mgr(case_any);}
	Btrie_fast_mgr(boolean caseAny) {
		root = new ByteTrieItm_fast(Byte_.Zero, null, caseAny);
	}
}
class ByteTrieItm_fast {
	private ByteTrieItm_fast[] ary = new ByteTrieItm_fast[256];
	public byte Key_byte() {return key_byte;} private byte key_byte;
	public Object Val() {return val;} public void Val_set(Object val) {this.val = val;} Object val;
	public boolean Ary_is_empty() {return ary_is_empty;} private boolean ary_is_empty;
	public boolean CaseAny() {return caseAny;} public ByteTrieItm_fast CaseAny_(boolean v) {caseAny = v; return this;} private boolean caseAny;
	public void Clear() {
		val = null;
		for (int i = 0; i < 256; i++) {
			if (ary[i] != null) {
				ary[i].Clear();
				ary[i] = null;
			}
		}
		ary_len = 0;
		ary_is_empty = true;
	}
	public ByteTrieItm_fast Ary_find(byte b) {
		int key_byte = (caseAny && (b > 64 && b < 91) ? b + 32 : b) & 0xff;// PATCH.JAVA:need to convert to unsigned byte
		return ary[key_byte];
	}
	public ByteTrieItm_fast Ary_add(byte b, Object val) {
		int key_byte = (caseAny && (b > 64 && b < 91) ? b + 32 : b) & 0xff;// PATCH.JAVA:need to convert to unsigned byte
		ByteTrieItm_fast rv = new ByteTrieItm_fast(b, val, caseAny);
		ary[key_byte] = rv;
		++ary_len;
		ary_is_empty = false;
		return rv;
	}
	public void Ary_del(byte b) {
		int key_byte = (caseAny && (b > 64 && b < 91) ? b + 32 : b) & 0xff;// PATCH.JAVA:need to convert to unsigned byte
		ary[key_byte] = null;
		--ary_len;
		ary_is_empty = ary_len == 0;
	}	int ary_len = 0;
	public ByteTrieItm_fast(byte key_byte, Object val, boolean caseAny) {this.key_byte = key_byte; this.val = val; this.caseAny = caseAny;}
}

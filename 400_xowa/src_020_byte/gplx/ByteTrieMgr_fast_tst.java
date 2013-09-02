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
package gplx;
import org.junit.*;
public class ByteTrieMgr_fast_tst {
	@Before public void init() {
		trie = ByteTrieMgr_fast.cs_();
		run_Add(1	, Byte_ascii.Ltr_a);
		run_Add(123	, Byte_ascii.Ltr_a, Byte_ascii.Ltr_b, Byte_ascii.Ltr_c);
	}	private ByteTrieMgr_fast trie;
	@Test  public void Fetch() {
		tst_MatchAtCur("a"		, 1);
		tst_MatchAtCur("abc"	, 123);
		tst_MatchAtCur("ab"		, 1);
		tst_MatchAtCur("abcde"	, 123);
		tst_MatchAtCur(" a"		, null);
	}
	@Test  public void Bos() {
		tst_Match("bc", Byte_ascii.Ltr_a, -1, 123);
	}
	@Test  public void MatchAtCurExact() {
		tst_MatchAtCurExact("a", 1);
		tst_MatchAtCurExact("ab", null);
		tst_MatchAtCurExact("abc", 123);
	}
	void run_Add(int val, byte... ary) {trie.Add(ary, val);}
	void tst_Match(String srcStr, byte b, int bgnPos, int expd) {
		byte[] src = ByteAry_.new_ascii_(srcStr);
		Object actl = trie.Match(b, src, bgnPos, src.length);
		Tfds.Eq(expd, actl);
	}
	void tst_MatchAtCur(String srcStr, Object expd) {
		byte[] src = ByteAry_.new_ascii_(srcStr);
		Object actl = trie.MatchAtCur(src, 0, src.length);
		Tfds.Eq(expd, actl);
	}
	void tst_MatchAtCurExact(String srcStr, Object expd) {
		byte[] src = ByteAry_.new_ascii_(srcStr);
		Object actl = trie.MatchAtCurExact(src, 0, src.length);
		Tfds.Eq(expd, actl);
	}
}

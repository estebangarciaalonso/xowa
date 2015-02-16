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
package gplx.xowa.xtns.syntaxHighlight; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Int_rng_mgr_tst {
	@Before public void init() {fxt.Clear();} Int_rng_mgr_fxt fxt = new Int_rng_mgr_fxt();
	@Test  public void Val()	{fxt.Test_parse_y("2")				.Test_match_y(2)					.Test_match_n(1, 3);}
	@Test  public void Rng()	{fxt.Test_parse_y("2-5")			.Test_match_y(2, 3, 4, 5)			.Test_match_n(0, 1, 6);}
	@Test  public void Many()	{fxt.Test_parse_y("1,3-5,7,9-10")	.Test_match_y(1, 3, 4, 5, 7, 9, 10)	.Test_match_n(0, 2, 6, 8, 11);}
}
class Int_rng_mgr_fxt {
	public void Clear() {
		if (mgr == null)
			mgr = new Int_rng_mgr_base();
		mgr.Clear();
	}	Int_rng_mgr_base mgr;
	public Int_rng_mgr_fxt Test_parse_y(String raw) {return Test_parse(raw, true);}
	public Int_rng_mgr_fxt Test_parse_n(String raw) {return Test_parse(raw, false);}
	public Int_rng_mgr_fxt Test_parse(String raw, boolean expd) {Tfds.Eq(expd, mgr.Parse(Bry_.new_ascii_(raw))); return this;}
	public Int_rng_mgr_fxt Test_match_y(int... v) {return Test_match(v, true);}
	public Int_rng_mgr_fxt Test_match_n(int... v) {return Test_match(v, false);}
	public Int_rng_mgr_fxt Test_match(int[] ary, boolean expd) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			Tfds.Eq(expd, mgr.Match(ary[i]), Int_.Xto_str(ary[i]));
		}
		return this;
	}
}

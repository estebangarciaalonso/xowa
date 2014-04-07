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
package gplx.xowa.xtns.hiero; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Srl_wkr_tst {		
	@Before public void init() {fxt.Reset();} private Srl_wkr_fxt fxt = new Srl_wkr_fxt();
	@Test  public void Basic() {
		fxt	.Test_load(String_.Concat_lines_nl_skipLast
		( "a|A"
		, "b|B"
		)
		, fxt.itm_("a", "A")
		, fxt.itm_("b", "B")
		);
	}
	@Test  public void Blank_lines() {
		fxt	.Test_load(String_.Concat_lines_nl_skipLast
		( ""
		, "a|A"
		, ""
		, "b|B"
		, ""
		)
		, fxt.itm_("a", "A")
		, fxt.itm_("b", "B")
		);
	}
	@Test  public void Incomplete_row() {
		fxt	.Test_load(String_.Concat_lines_nl_skipLast
		( "a"
		, "b"
		, ""
		)
		, fxt.itm_("a")
		, fxt.itm_("b")
		);
	}
//		@Test  public void Escape() {
//			fxt	.Test_load(String_.Concat_lines_nl_skipLast
//			( "a\\|\\nb|c"
//			)
//			, fxt.itm_("a|\nb", "c")
//			);
//		}
}
class Srl_wkr_fxt {
	private Srl_mok_mgr mgr = new Srl_mok_mgr();
	private Srl_tbl_parser tbl_parser = new Srl_tbl_parser();
	public Srl_wkr_fxt() {
		tbl_parser.Init(mgr, Srl_fld_parser_bry._, Srl_fld_parser_bry._);
	}
	public void Reset() {
		tbl_parser.Clear();
		mgr.Clear();
	}
	public Srl_mok_itm itm_(String... flds) {return new Srl_mok_itm(flds);}
	public Srl_wkr_fxt Init_flds_len_(int len) {
		Srl_fld_parser[] ary = new Srl_fld_parser[len];
		for (int i = 0; i < len; i++) 
			ary[i] = Srl_fld_parser_bry._;
		tbl_parser.Init(mgr, ary);
		return this;
	}
	public void Test_load(String src, Srl_mok_itm... expd) {
		tbl_parser.Parse(ByteAry_.new_utf8_(src));
		Tfds.Eq_ary_str(expd, mgr.Itms());
	}
}
class Srl_mok_itm implements XtoStrAble {
	private String[] flds;
	public Srl_mok_itm(String[] flds) {this.flds = flds;}
	public String XtoStr() {return String_.Concat_with_str("|", flds);}
}
class Srl_mok_mgr extends Srl_mgr_base {		
	public void Clear() {itms.Clear();}
	public Srl_mok_itm[] Itms() {return (Srl_mok_itm[])itms.XtoAry(Srl_mok_itm.class);} private ListAdp itms = ListAdp_.new_();
	private ListAdp flds = ListAdp_.new_();
	@Override public void Write_bry(Srl_tbl_parser ctx, int fld_idx, byte[] src, int bgn, int end) {
		flds.Add(String_.new_utf8_(src, bgn, end));
	}
	@Override public void Commit_itm() {
		Srl_mok_itm itm = new Srl_mok_itm((String[])flds.XtoAryAndClear(String.class));
		itms.Add(itm);
	}
//		public void Srl_add(int flds_len, byte[][] flds) {
//			String[] flds_new = new String[flds.length];	// create flds with same len as flds
//			for (int i = 0; i < flds_len; i++)				// only fill # of flds found; needed for incomplete row
//				flds_new[i] = String_.new_ascii_(flds[i]);
//			itms.Add(new Srl_mok_itm(String_.Ary(flds_new)));
//		}
}

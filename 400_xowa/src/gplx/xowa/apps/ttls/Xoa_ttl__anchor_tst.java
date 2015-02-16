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
package gplx.xowa.apps.ttls; import gplx.*; import gplx.xowa.*; import gplx.xowa.apps.*;
import org.junit.*;
public class Xoa_ttl__anchor_tst {
	@Before public void init() {fxt.Reset();} private Xoa_ttl_fxt fxt = new Xoa_ttl_fxt();
	@Test   public void Anch_y()					{fxt.Init_ttl("a#b")			.Expd_full_txt("A").Expd_page_txt("A").Expd_anch_txt("b").Test();}
	@Test   public void Anch_only()					{fxt.Init_ttl("#a")				.Expd_full_txt("").Expd_page_txt("").Expd_anch_txt("a").Test();}
	@Test   public void Anchor_fails() {	// PURPOSE: :#batch:Main Page causes ttl to fail b/c # is treated as anchor; DATE:2013-01-02
		fxt.Wiki().Xwiki_mgr().Add_full(Bry_.new_ascii_("#batch"), Bry_.new_ascii_("none"));
		fxt.Init_ttl(":#batch:Main Page").Expd_full_txt("Main Page").Test();
	}
	@Test   public void Anchor_angles() {// PURPOSE: angles in anchor should be encoded; DATE: 2013-01-23
		fxt.Init_ttl("A#b<c>d").Expd_full_txt("A").Expd_anch_txt("b.3Cc.3Ed").Test();
	}
	@Test   public void Anchor_arg_in_non_main_ns() {
		fxt.Init_ttl("Help:A#B").Expd_full_txt("Help:A").Expd_anch_txt("B").Test();
	}
	@Test   public void Anchor_and_slash() {	// PURPOSE: slash in anchor was being treated as a subpage; DATE:2014-01-14
		fxt.Init_ttl("A#b/c").Expd_full_txt("A").Expd_anch_txt("b/c").Expd_leaf_txt("A").Test();	// NOTE: Leaf_txt should be Page_txt; used to fail
	}
	@Test   public void Decode_ncr() {	// PURPOSE: convert &#x23; to #; PAGE:en.s:The_English_Constitution_(1894) DATE:2014-09-07
		fxt.Init_ttl("A&#x23;b").Expd_full_txt("A").Expd_page_txt("A").Expd_anch_txt("b").Test();
		fxt.Init_ttl("A&#35;b").Expd_full_txt("A").Expd_page_txt("A").Expd_anch_txt("b").Test();
		fxt.Init_ttl("A&#36;b").Expd_full_txt("A$b").Expd_page_txt("A$b").Expd_anch_txt("").Test();
	}
}
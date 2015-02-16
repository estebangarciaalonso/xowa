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
import org.junit.*;
public class Xow_wiki_tst {
	@Before public void init() {fxt.Clear();} private Xow_wiki_fxt fxt = new Xow_wiki_fxt();
	@Test   public void GetPageByTtl() {	// PURPOSE.fix: unknown page causes null reference error in scribunto; DATE:2013-08-27
		gplx.xowa.xtns.scribunto.Scrib_core.Core_new_(fxt.Fxt().App(), fxt.Fxt().Ctx());
		fxt.Test_getPageByTtl("Does_not_exist", null);
	}
}
class Xow_wiki_fxt {
	public void Clear() {
		fxt = new Xop_fxt();
	}
	public Xop_fxt Fxt() {return fxt;} private Xop_fxt fxt;
	public void Test_getPageByTtl(String ttl_str, String expd) {
		Xow_wiki wiki = fxt.Wiki();
		byte[] ttl_bry = Bry_.new_ascii_(ttl_str);
		Xoa_url url = Xoa_url.blank_().Raw_(ttl_bry);
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
		Xoa_page actl = fxt.Wiki().GetPageByTtl(url, ttl);
		if (expd == null) Tfds.Eq_true(actl.Missing());
		else Tfds.Eq(expd, String_.new_utf8_(actl.Ttl().Raw()));
	}
}

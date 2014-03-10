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
package gplx.html; import gplx.*;
import org.junit.*;
public class Html_utl_tst {		
	@Before public void init() {fxt.Clear();} private Html_utl_fxt fxt = new Html_utl_fxt();
	@Test   public void Basic() 		{fxt.Test_del_comments("a<!-- b -->c"				, "ac");}
	@Test   public void Bgn_missing() 	{fxt.Test_del_comments("a b c"						, "a b c");}
	@Test   public void End_missing() 	{fxt.Test_del_comments("a<!-- b c"					, "a<!-- b c");}
	@Test   public void Multiple()	 	{fxt.Test_del_comments("a<!--b-->c<!--d-->e"		, "ace");}
}
class Html_utl_fxt {
	private ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255);
	public void Clear() {
		tmp_bfr.Clear();
	}
	public void Test_del_comments(String src, String expd) {
		byte[] actl = Html_utl.Del_comments(tmp_bfr, ByteAry_.new_utf8_(src));
		Tfds.Eq(expd, String_.new_ascii_(actl));
	}
}

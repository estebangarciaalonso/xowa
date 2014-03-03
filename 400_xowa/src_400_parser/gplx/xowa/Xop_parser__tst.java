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
public class Xop_parser__tst {
	@Before public void init() {fxt.Clear();} private Xop_parser__fxt fxt = new Xop_parser__fxt();
	@Test  public void Para_y() {
		fxt.Test_parse_to_html(String_.Concat_lines_nl_skipLast
		( "a"
		, ""
		, "b"
		), true, String_.Concat_lines_nl_skipLast
		( "<p>a"
		, "</p>"
		, ""
		, "<p>b"
		, "</p>"
		, ""
		));
	}
	@Test  public void Para_n() {
		fxt.Test_parse_to_html(String_.Concat_lines_nl_skipLast
		( "a"
		, ""
		, "b"
		), false, String_.Concat_lines_nl_skipLast
		( "a"
		, "b"
		));
	}
}
class Xop_parser__fxt {
	private Xop_fxt fxt = new Xop_fxt();
	private ByteAryBfr bfr = ByteAryBfr.reset_(255);
	public void Clear() {
		fxt.Reset();
	}
	public void Test_parse_to_html(String raw, boolean para_enabled, String expd)  {
		byte[] raw_bry = ByteAry_.new_utf8_(raw);
		Xop_parser_.Parse_to_html(bfr, fxt.Wiki(), para_enabled, raw_bry);
		Tfds.Eq(expd, bfr.XtoStrAndClear());
	}
}

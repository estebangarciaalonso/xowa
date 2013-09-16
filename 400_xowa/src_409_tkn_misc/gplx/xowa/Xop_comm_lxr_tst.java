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
public class Xop_comm_lxr_tst {
	private Xop_fxt fxt = new Xop_fxt();
//		@Test  public void Basic() {
//			fxt.tst_Parse_page_tmpl("a<!-- b -->c", fxt.tkn_txt_(0, 1), fxt.tkn_comment_(1, 11), fxt.tkn_txt_(11, 12));
//		}
//		@Test  public void Err() {
//			fxt.ini_Log_(Xop_comment_log.Eos).tst_Parse_page_tmpl("<!-- ", fxt.tkn_comment_(0, 5));
//		}
//		@Test  public void Ws_end() {
//			fxt.tst_Parse_page_all_str("a\n<!-- b --> \nc", "a\nc");
//		}
	@Test  public void Ws_bgn_end() {
		fxt.tst_Parse_page_all_str("a\n <!-- b --> \nc", "a\nc");
	}
	@Test  public void Ws_noop() {	// PURPOSE: assert that comments are not stripped
		fxt.tst_Parse_page_all_str("a <!-- b -->c", "a c");
	}
	@Test  public void Noinclude() {// PURPOSE: templates can construct comments; EX:WBK: {{Subjects/allbooks|subject=Computer programming|origin=Computer programming languages|diagnose=}}
		fxt.tst_Parse_page_all_str("a <!-<noinclude></noinclude>- b -->c", "a c");
	}
//		@Test  public void Ws_bgn_needs_nl() {	// PURPOSE: do not strip new line unles *entire* line is comment
//			fxt.tst_Parse_page_all_str("a <!-- b -->\nc", "a \nc");
//		}
}

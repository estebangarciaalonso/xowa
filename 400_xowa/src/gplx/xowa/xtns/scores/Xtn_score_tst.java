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
package gplx.xowa.xtns.scores; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Xtn_score_tst {
	@Test  public void Version() {
		Tfds.Eq_bry(ByteAry_.new_ascii_("2.16.2"), Xtn_score.Get_lilypond_version("GNU LilyPond 2.16.2\nline1\nline2"));
		Tfds.Eq_bry(ByteAry_.new_ascii_("2.16.2"), Xtn_score.Get_lilypond_version("GNU LilyPond 2.16.2\r\nline1\r\nline2"));
	}
	@Test  public void Tab() {
		Xop_fxt fxt = new Xop_fxt();
		fxt.tst_Parse_page_wiki_str("<score>a&#09;b</score>", String_.Concat_lines_nl
			(	""
			,	"<div id=\"xowa_score_0_pre\" class=\"xowa-score-lilypond\">"
			,	"  <pre style=\"overflow:auto\">a	b"	// NOTE: embedded tab
			,	"</pre>"
			,	"</div>"
			,	""
			,	"<p>"
			,	"  <a id=\"xowa_score_0_a\" href=\"\" xowa_title=\"\">"
			,	"    <img id=\"xowa_score_0_img\" src=\"\"  />"
			,	"  </a>"
			,	"</p>"
			));
	}
}

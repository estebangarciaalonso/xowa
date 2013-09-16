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
public class Xop_hr_lxr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Basic()					{fxt.tst_Parse_page_wiki("----"				, fxt.tkn_hr_(0, 4));}
	@Test  public void Basic_w_nl()				{fxt.tst_Parse_page_wiki("\n----a"				, fxt.tkn_hr_(0, 5), fxt.tkn_txt_(5, 6));}
	@Test  public void Many()					{fxt.tst_Parse_page_wiki("---------"			, fxt.ini_Log_(Xop_hr_log.Len_5_or_more).tkn_hr_(0, 9).Hr_len_(9));}
	@Test  public void Exc_short()				{fxt.tst_Parse_page_wiki("---"					, fxt.tkn_txt_(0, 3));}
	@Test  public void Exc_interrupt()			{fxt.tst_Parse_page_wiki("\na----"				, fxt.tkn_nl_char_len1_(0), fxt.tkn_txt_(1, 6));}
	@Test  public void Html_basic()				{fxt.tst_Parse_page_wiki_str("----"			, "<hr/>");}
	@Test  public void Html_extended()			{fxt.tst_Parse_page_wiki_str("------"			, "<hr/>");}
	@Test  public void Nl_bgn()					{fxt.tst_Parse_page_wiki_str("a\n----"			, "a<hr/>");}
	@Test  public void Nl_end()					{fxt.tst_Parse_page_wiki_str("----\na"			, "<hr/>\na");}
}
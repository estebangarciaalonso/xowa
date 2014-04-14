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
public class Xop_amp_wkr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Named() {
		fxt.Test_parse_page_wiki("&amp;"			, fxt.tkn_html_ref_("&amp;"));								// basic
		fxt.Test_parse_page_wiki("a&amp;"			, fxt.tkn_txt_(0, 1), fxt.tkn_html_ref_("&amp;"));			// basic_bgn
		fxt.Test_parse_page_wiki("&amp;nbsp;"		, fxt.tkn_html_ref_("&amp;"), fxt.tkn_txt_(5, 10));			// basic_end
		fxt.Test_parse_page_wiki("&nil;"			, fxt.tkn_txt_(0, 5));										// unknown
		fxt.Test_parse_page_wiki("a&nil;"			, fxt.tkn_txt_(0, 6));										// unknown_bgn
		fxt.Test_parse_page_wiki_str("&amp;"		, "&amp;");
		fxt.Test_parse_page_wiki_str("&aacute;"		, "&#225;");
	}
	@Test  public void Numeric() {// Σ: http://en.wikipedia.org/wiki/Numeric_character_reference
		fxt.Test_parse_page_wiki("&#x3A3;"			, fxt.tkn_html_ncr_(931));
		fxt.Test_parse_page_wiki("&#x3a3;"			, fxt.tkn_html_ncr_(931));
		fxt.Test_parse_page_wiki("&#x03a3;"			, fxt.tkn_html_ncr_(931));
		fxt.Test_parse_page_wiki("&#X3A3;"			, fxt.tkn_html_ncr_(931));
		fxt.Test_parse_page_wiki("&#931;"			, fxt.tkn_html_ncr_(931));
		fxt.Test_parse_page_wiki("&#0931;"			, fxt.tkn_html_ncr_(931));
		fxt.Test_parse_page_wiki("&#538189831;"		, fxt.tkn_txt_());
	}
	@Test  public void Outliers() {
		fxt.Test_parse_page_wiki("&#xx26D0;"		, fxt.tkn_html_ncr_(9936));	// NOTE: 2nd x is ignored
	}
	@Test  public void Err() {
		fxt.Init_log_(Xop_amp_log.Eos)			.Test_parse_page_wiki("&#"			, fxt.tkn_txt_(0, 2));
		fxt.Init_log_(Xop_amp_log.Invalid_dec)	.Test_parse_page_wiki("&#a;"		, fxt.tkn_txt_());
		fxt.Init_log_(Xop_amp_log.Invalid_hex)	.Test_parse_page_wiki("&#x!;"		, fxt.tkn_txt_());
	}
	@Test  public void Amp_only() {	// PURPOSE: html_wtr was not handling & only
		fxt.Test_parse_page_all_str("&"				, "&amp;");
	}
	@Test  public void Parse_static() {
		Tst_Parse("a", "a");
		Tst_Parse("a&quot;b", "a\"b");
		Tst_Parse("a&quotb", "a&quotb");
		Tst_Parse("a&", "a&");
	}
	private void Tst_Parse(String raw, String expd) {
		ByteTrieMgr_slim amp_trie = fxt.App().Amp_trie();
		ByteAryBfr bfr = fxt.App().Utl_bry_bfr_mkr().Get_b128();
		byte[] actl = Xop_amp_wkr.Parse(bfr, amp_trie, ByteAry_.new_ascii_(raw));
		bfr.Mkr_rls();
		Tfds.Eq(expd, String_.new_ascii_(actl));
	}
}

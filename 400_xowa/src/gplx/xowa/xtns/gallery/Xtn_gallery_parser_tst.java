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
package gplx.xowa.xtns.gallery; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Xtn_gallery_parser_tst {
	@Before public void init() {fxt.Init();} private Xtn_gallery_parser_fxt fxt = new Xtn_gallery_parser_fxt();
	@Test   public void All() {
		fxt.Test_parse("File:A.png|a|alt=b|link=c", fxt.Expd("File:A.png", "a", "b", "c"));
	}
	@Test   public void Ttl_only() {
		fxt.Test_parse("File:A.png", fxt.Expd("File:A.png", null, null, null));
	}
	@Test   public void Caption_only() {
		fxt.Test_parse("File:A.png|a", fxt.Expd("File:A.png", "a", null, null));
	}
	@Test   public void Alt_only() {
		fxt.Test_parse("File:A.png|alt=a", fxt.Expd("File:A.png", null, "a", null));
	}
	@Test   public void Link_only() {
		fxt.Test_parse("File:A.png|link=a", fxt.Expd("File:A.png", null, null, "a"));
	}
	@Test   public void Caption_alt() {
		fxt.Test_parse("File:A.png|a|alt=b", fxt.Expd("File:A.png", "a", "b"));
	}
	@Test   public void Alt_caption() {
		fxt.Test_parse("File:A.png|alt=a|b", fxt.Expd("File:A.png", "b", "a"));
	}
	@Test   public void Itm_many() {
		fxt.Test_parse("File:A.png\nFile:B.png", fxt.Expd("File:A.png"), fxt.Expd("File:B.png"));
	}
	@Test   public void Itm_blank() {
		fxt.Test_parse("File:A.png\n\nFile:B.png", fxt.Expd("File:A.png"), fxt.Expd("File:B.png"));
	}
	@Test   public void Itm_blank_w_ws() {
		fxt.Test_parse("File:A.png\n \t \nFile:B.png", fxt.Expd("File:A.png"), fxt.Expd("File:B.png"));
	}
	@Test   public void Ttl_invalid() {
		fxt.Test_parse("File:A.png\n<invalid_title>\nFile:B.png", fxt.Expd("File:A.png"), fxt.Expd("File:B.png"));
	}
	@Test   public void Alt_invalid() {
		fxt.Test_parse("File:A.png|alta=b", fxt.Expd("File:A.png", "alta=b"));
	}
	@Test   public void Alt_blank() {
		fxt.Test_parse("File:A.png|alt=|b", fxt.Expd("File:A.png", "b", ""));
	}
	@Test   public void Ws() {
		fxt.Test_parse("File:A.png| alt = b | c", fxt.Expd("File:A.png", "c", "b"));
	}
	@Test   public void Caption_multiple() {
		fxt.Test_parse("File:A.png|a|b", fxt.Expd("File:A.png", "a|b"));
	}
	@Test   public void Caption_multiple_ws() {
		fxt.Test_parse("File:A.png| a | b | c ", fxt.Expd("File:A.png", "a | b | c"));
	}
	@Test   public void Caption_multiple_w_alt() {
		fxt.Test_parse("File:A.png|alt=a|b[[c|d]]e ", fxt.Expd("File:A.png", "b[[c|d]]e", "a"));
	}
	@Test   public void Blank() {
		fxt.Test_parse("");
	}
	@Test   public void Err_empty() {
		fxt.Test_parse("|File:A.png");
	}
	@Test   public void Err_ws() {
		fxt.Test_parse(" |File:A.png");
	}
	@Test   public void Alt_magic_word_has_arg() {	// PURPOSE: img_alt magic_word is of form "alt=$1"; make sure =$1 is stripped for purpose of parser; DATE:2013-09-12
		fxt.Init_kwd_set(Xol_kwd_grp_.Id_img_alt, "alt=$1");
		fxt.Test_parse("File:A.png|alt=a|b", fxt.Expd("File:A.png", "b", "a"));
	}
}
class Xtn_gallery_parser_fxt {
	private Xoa_app app; private Xow_wiki wiki;
	public Xtn_gallery_parser Parser() {return parser;} private Xtn_gallery_parser parser;
	public Xtn_gallery_parser_fxt Init() {
		this.app = Xoa_app_fxt.app_();
		this.wiki = Xoa_app_fxt.wiki_tst_(app);
		parser = new Xtn_gallery_parser();
		parser.Init_by_wiki(wiki);
		return this;
	}
	public String[] Expd(String ttl) {return new String[] {ttl, null, null, null};}
	public String[] Expd(String ttl, String caption) {return new String[] {ttl, caption, null, null};}
	public String[] Expd(String ttl, String caption, String alt) {return new String[] {ttl, caption, alt, null};}
	public String[] Expd(String ttl, String caption, String alt, String link) {return new String[] {ttl, caption, alt, link};}
	public void Init_kwd_set(int kwd_id, String kwd_val) {
		wiki.Lang().Kwd_mgr().Get_or_new(kwd_id).Itms()[0].Bry_set(ByteAry_.new_ascii_(kwd_val));
		parser.Init_by_wiki(wiki);
	}
	public void Test_parse(String raw, String[]... expd) {
		ListAdp actl = ListAdp_.new_();
		byte[] src = ByteAry_.new_ascii_(raw); int src_len = src.length;
		parser.Parse_all(actl, src, 0, src_len);
		Tfds.Eq_ary(Ary_flatten(expd), Ary_flatten(To_str_ary(src, actl)));
	}
	private String[] Ary_flatten(String[][] src_ary) {
		int trg_len = 0;
		int src_len = src_ary.length;
		for (int i = 0; i < src_len; i++) {
			String[] itm = src_ary[i];
			if (itm != null) trg_len += itm.length;
		}
		String[] trg_ary = new String[trg_len];
		trg_len = 0;
		for (int i = 0; i < src_len; i++) {
			String[] itm = src_ary[i];
			if (itm == null) continue;
			int itm_len = itm.length;
			for (int j = 0; j < itm_len; j++)
				trg_ary[trg_len++] = itm[j];
		}
		return trg_ary;
	}
	private String[][] To_str_ary(byte[] src, ListAdp list) {
		int len = list.Count();
		String[][] rv = new String[len][];
		for (int i = 0; i < len; i++) {
			Xtn_gallery_itm itm = (Xtn_gallery_itm)list.FetchAt(i);
			String[] ary = new String[4];
			rv[i] = ary;
			ary[0] = To_str_ary_itm(src, itm.Ttl_bgn(), itm.Ttl_end());
			ary[2] = To_str_ary_itm(src, itm.Alt_bgn(), itm.Alt_end());
			ary[3] = To_str_ary_itm(src, itm.Link_bgn(), itm.Link_end());
			byte[] caption = itm.Caption_bry();
			ary[1] =  caption == null ? null : String_.new_utf8_(caption);
		}
		return rv;
	}
	private String To_str_ary_itm(byte[] src, int bgn, int end) {
		return bgn == ByteAry_.NotFound && end == ByteAry_.NotFound ? null : String_.new_utf8_(src, bgn, end);
	}
}

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
package gplx.xowa.bldrs.xmls; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import org.junit.*;
import gplx.ios.*;
public class Xob_xml_parser_tst {
	@Before public void init() {
		Io_mgr._.InitEngine_mem();
		Xoa_app app = Xoa_app_fxt.app_();
		bldr = new Xob_bldr(app);
	}	private Xow_ns_mgr ns_mgr = Xow_ns_mgr_.default_(gplx.xowa.langs.cases.Xol_case_mgr_.Ascii());
	@Test  public void Basic_docs_1() {
		Xodb_page doc = doc_(1, "a", "a a", Date_1);
		fil = page_bldr.Add(doc).XtoByteStreamRdr();
		tst_parse(fil, doc, 0);
	}
	@Test  public void Basic_docs_2() {
		Xodb_page doc1 = doc_(1, "a", "a a", Date_1);
		Xodb_page doc2 = doc_(2, "b", "b b", Date_2);
		fil = page_bldr.Add_ary(doc1, doc2).XtoByteStreamRdr();
		int pos = tst_parse(fil, doc1, 0);
		tst_parse(fil, doc2, pos);
	}
	@Test  public void Basic_space() {
		Xodb_page doc1 = doc_(1, "a_b", "abc", Date_1);
		fil = page_bldr.Add_ary(doc1).Upd("a_b", "a b").XtoByteStreamRdr();
		tst_parse(fil, doc1, 0);
	}
	@Test  public void Xml() {
		Xodb_page doc = doc_(1, "a", "&quot;a &amp; b &lt;&gt; a | b&quot;", Date_1);
		fil = page_bldr.Add(doc).XtoByteStreamRdr();
		tst_parse(fil, doc.Text_(Bry_.new_utf8_("\"a & b <> a | b\"")), 0);
	}
	@Test  public void Tab() {
		Xodb_page doc = doc_(1, "a", "a \t b", Date_1);
		fil = page_bldr.Add(doc).XtoByteStreamRdr();
		tst_parse(fil, doc.Text_(Bry_.new_utf8_("a &#09; b")), 0);
	}
	@Test  public void Tab_disable() {
		Xodb_page doc = doc_(1, "a", "a \t b", Date_1);
		page_parser.Trie_tab_del_();
		fil = page_bldr.Add(doc).XtoByteStreamRdr();
		tst_parse(fil, doc.Text_(Bry_.new_utf8_("a \t b")), 0);
	}
	@Test  public void Cr_nl() {
		Xodb_page doc = doc_(1, "a", "a \r\n b", Date_1);
		fil = page_bldr.Add(doc).XtoByteStreamRdr();
		tst_parse(fil, doc.Text_(Bry_.new_utf8_("a \n b")), 0);
	}
	@Test  public void Cr() {
		Xodb_page doc = doc_(1, "a", "a \r b", Date_1);
		fil = page_bldr.Add(doc).XtoByteStreamRdr();
		tst_parse(fil, doc.Text_(Bry_.new_utf8_("a \n b")), 0);
	}
	@Test  public void Text_long() {
		String s = String_.Repeat("a", 1024);
		Xodb_page doc = doc_(1, "a", s, Date_1);
		page_parser.Tag_len_max_(32);
		fil = page_bldr.Add(doc).XtoByteStreamRdr(512);
		tst_parse(fil, doc, 0);
	}
	@Test  public void Text_empty() {
		Xodb_page doc = doc_(1, "a", "", Date_1);
		fil = page_bldr.Add(doc).Upd("<text></text>", "<text />").XtoByteStreamRdr();
		tst_parse(fil, doc, 0);
	}
	@Test  public void Text_frag() {
		Xodb_page doc = doc_(1, "a", "a", Date_1);
		fil = page_bldr.Add(doc).Upd("<text>a</text>", "<text xml:space=\"preserve\">a</text>").XtoByteStreamRdr();
		tst_parse(fil, doc, 0);
	}
	@Test  public void Ns_file() {
		Xodb_page doc = doc_(1, "File:a", "a", Date_1);
		Tfds.Eq(Xow_ns_.Id_file, doc.Ns_id());
		Tfds.Eq("a", String_.new_utf8_(doc.Ttl_wo_ns()));
	}
	@Test  public void Ns_main() {
		Xodb_page doc = doc_(1, "a", "a", Date_1);
		Tfds.Eq(Xow_ns_.Id_main, doc.Ns_id());
		Tfds.Eq("a", String_.new_utf8_(doc.Ttl_wo_ns()));
	}
	@Test  public void Ns_main_book() {
		Xodb_page doc = doc_(1, "Book", "a", Date_1);
		Tfds.Eq(Xow_ns_.Id_main, doc.Ns_id());
		Tfds.Eq("Book", String_.new_utf8_(doc.Ttl_wo_ns()));
	}
	@Test  public void XmlEntities() {
		Xodb_page orig = doc_(1, "A&amp;b", "a", Date_1);
		Xodb_page actl = new Xodb_page();
		fil = page_bldr.Add(orig).XtoByteStreamRdr();
		page_parser.Parse_page(actl, usr_dlg, fil, fil.Bfr(), 0, ns_mgr);
		Tfds.Eq("A&b", String_.new_utf8_(actl.Ttl_w_ns()));
	}
	@Test  public void Root() {
		Xodb_page doc = doc_(1, "a", "a", Date_1);
		page_bldr.Bfr().Add_str("<root>\n");
		page_bldr.Add(doc);
		page_bldr.Bfr().Add_str("</root>");
		fil = page_bldr.XtoByteStreamRdr();
		tst_parse(fil, doc, 0);
	}
	private static final String Date_1 = "2012-01-01T01:01:01Z", Date_2 = "2012-02-02T02:02:02Z"; DateAdp_parser dateParser = DateAdp_parser.new_();
	Bry_bfr bfr = Bry_bfr.new_();
	Xob_xml_page_bldr page_bldr = new Xob_xml_page_bldr(); Io_buffer_rdr fil; Xob_xml_parser page_parser = new Xob_xml_parser(); Xob_bldr bldr;
	Gfo_usr_dlg usr_dlg = Gfo_usr_dlg_base.test_();
	int tst_parse(Io_buffer_rdr fil, Xodb_page expd, int cur_pos) {
		Xodb_page actl = new Xodb_page();
		int rv = page_parser.Parse_page(actl, usr_dlg, fil, fil.Bfr(), cur_pos, ns_mgr);
		Tfds.Eq(expd.Id(), actl.Id(), "id");
		Tfds.Eq(String_.new_utf8_(expd.Ttl_w_ns()), String_.new_utf8_(actl.Ttl_w_ns()), "title");
		Tfds.Eq(String_.new_utf8_(expd.Text()), String_.new_utf8_(actl.Text()), "text");
		Tfds.Eq_date(expd.Modified_on(), actl.Modified_on(), "timestamp");
		return rv;
	}
	Xodb_page doc_(int id, String title, String text, String date) {
		Xodb_page rv = new Xodb_page().Id_(id).Ttl_(Bry_.new_ascii_(title), ns_mgr).Text_(Bry_.new_ascii_(text));
		int[] modified_on = new int[7];
		dateParser.Parse_iso8651_like(modified_on, date);
		rv.Modified_on_(DateAdp_.seg_(modified_on));
		return rv;
	}
}

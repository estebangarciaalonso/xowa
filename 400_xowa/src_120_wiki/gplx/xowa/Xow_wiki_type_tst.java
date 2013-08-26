/*
XOWA: the extensible offline wiki application
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
public class Xow_wiki_type_tst {
	Xow_wiki_type_fxt fxt = new Xow_wiki_type_fxt();
	@Before public void init() {fxt.Clear();}
	@Test  public void Parse_en_wikipedia() 			{fxt.Wiki_tid_(Xow_wiki_type_.Tid_wikipedia).Lang_key_("en").tst("en.wikipedia.org");}
	@Test  public void Parse_fr_wikipedia() 			{fxt.Wiki_tid_(Xow_wiki_type_.Tid_wikipedia).Lang_key_("fr").tst("fr.wikipedia.org");}
	@Test  public void Parse_en_wiktionary() 			{fxt.Wiki_tid_(Xow_wiki_type_.Tid_wiktionary).Lang_key_("en").tst("en.wiktionary.org");}
	@Test  public void Parse_commons() 				{fxt.Wiki_tid_(Xow_wiki_type_.Tid_commons).Lang_key_("").tst("commons.wikimedia.org");}
	@Test  public void Parse_species() 				{fxt.Wiki_tid_(Xow_wiki_type_.Tid_species).Lang_key_("").tst("species.wikimedia.org");}
	@Test  public void Parse_home() 					{fxt.Wiki_tid_(Xow_wiki_type_.Tid_home).Lang_key_("").tst("home");}
	@Test  public void En()							{tst_Extract_lang("en.wikipedia.org", "en");}
	@Test  public void Fr()							{tst_Extract_lang("fr.wikipedia.org", "fr");}
	@Test  public void Commons()						{tst_Extract_lang("commons.wikimedia.org", null);}
	void tst_Extract_lang(String raw, String expd) {Tfds.Eq(expd, String_.new_utf8_(Xow_wiki_type_.Extract_lang(ByteAry_.new_utf8_(raw))));}
}
class Xow_wiki_type_fxt {
	public void Clear() {lang_key = ""; wiki_tid = Xow_wiki_type_.Tid_custom;}
	public Xow_wiki_type_fxt Lang_key_(String v) {lang_key = v; return this;} private String lang_key;
	public Xow_wiki_type_fxt Wiki_tid_(byte v) {wiki_tid = v; return this;} private byte wiki_tid;
	public void tst(String raw) {
		byte[] raw_bry = ByteAry_.new_ascii_(raw);
		Xow_wiki_type wiki_type = Xow_wiki_type_.parse_(raw_bry);
		if (wiki_type == null) {
			Tfds.Eq_true(wiki_tid == Xow_wiki_type_.Tid_custom);			
		}
		else {
			Tfds.Eq(wiki_tid, wiki_type.Wiki_tid());
			if (lang_key == null)
				Tfds.Eq_true(wiki_type.Lang_key() == ByteAry_.Empty);			
			else
				Tfds.Eq(lang_key, String_.new_ascii_(wiki_type.Lang_key()));
		}
	}
}

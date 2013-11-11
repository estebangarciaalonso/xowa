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
package gplx.xowa.wikis; import gplx.*; import gplx.xowa.*;
public class Xow_wiki_type_ {
	public static byte[] Key_by_tid(byte tid) {return Key__ary[tid];}
	public static final byte Tid_by_key_null = Byte_.MaxValue_127;
	public static byte Tid_by_key(byte[] key) {
		if (key_hash == null) key_hash_new();
		Object o = key_hash.Get_by_bry(key);
		return o == null ? Tid_by_key_null : ((ByteVal)o).Val();
	}
	public static Xow_wiki_type parse_by_domain(byte[] raw) {
		/*
		~{type}.org				EX: wikimediafoundation
		~{type}.wikimedia.org	EX: commons; species; meta; incubator
		~{lang}.~{type}.org		EX: en.wikipedia, etc;
		~www.~{type}.org		EX: mediawiki; wikidata;
		*/
		if (key_hash == null) key_hash_new();
		int raw_len = raw.length;
		int dot_0 = ByteAry_.FindFwd(raw, Byte_ascii.Dot, 0, raw_len);
		if (dot_0 == ByteAry_.NotFound) {	// 0 dots; check for "home"
			return ByteAry_.Eq(raw, Key_home_bry)
				? new Xow_wiki_type(raw, Xol_lang_itm_.Key__unknown, Tid_home)
				: Xow_wiki_type.new_other_(raw);
		}
		int dot_1 = ByteAry_.FindFwd(raw, Byte_ascii.Dot, dot_0 + 1, raw_len);
		if (dot_1 == ByteAry_.NotFound) {	// 1 dot; check for "wikimediafoundation.org"
			return ByteAry_.Match(raw, 0, dot_0, Key_wikimediafoundation_bry)
				? new Xow_wiki_type(raw, Xol_lang_itm_.Key__unknown, Tid_wikimediafoundation)
				: Xow_wiki_type.new_other_(raw);
		}
		// 2 dots
		Object seg_1_obj = key_hash.Get_by_mid(raw, dot_0 + 1, dot_1);
		if (seg_1_obj == null) // seg_1 is unknown; should be wikimedia, or any of the other types; return other;
			return Xow_wiki_type.new_other_(raw);
		byte seg_1_tid = ((ByteVal)seg_1_obj).Val();
		switch (seg_1_tid) {
			case Tid_wikipedia: case Tid_wiktionary: case Tid_wikisource: case Tid_wikibooks:
			case Tid_wikiversity: case Tid_wikiquote: case Tid_wikinews: case Tid_wikivoyage:	// ~{lang}.~{type}.org
				byte[] lang_key = ByteAry_.Mid(raw, 0, dot_0);
				return new Xow_wiki_type(raw, lang_key, seg_1_tid);	// NOTE: seg_tids must match wiki_tids
			case Tid_wikidata: case Tid_mediawiki:												// ~www.~{type}.org
				return new Xow_wiki_type(raw, Xol_lang_itm_.Key__unknown, seg_1_tid);
			case Tid_wikimedia:																	// ~{type}.wikimedia.org
				Object seg_0_obj = key_hash.Get_by_mid(raw, 0, dot_0); if (seg_0_obj == null) return Xow_wiki_type.new_other_(raw);
				byte seg_0_tid = ((ByteVal)seg_0_obj).Val();
				switch (seg_0_tid) {
					case Tid_commons: case Tid_species: case Tid_meta: case Tid_incubator:
						return new Xow_wiki_type(raw, Xol_lang_itm_.Key__unknown, seg_0_tid);	// NOTE: seg_tids must match wiki_tids; NOTE: lang_key is "en" (really, "multi" but making things easier)
					default:
						return Xow_wiki_type.new_other_(raw);
				}
			default:
			case Tid_other:
				return Xow_wiki_type.new_other_(raw);
		}
	}
	private static Hash_adp_bry key_hash;
	private static void key_hash_new() {
		key_hash = new Hash_adp_bry(false);
		key_hash.Add_bry_byteVal(Seg_wikimedia_bry, Tid_wikimedia);		// PERF: not a "key" but makes Parse quicker
		key_hash.Add_bry_byteVal(Key_home_bry, Tid_home);
		key_hash.Add_bry_byteVal(Key_commons_bry, Tid_commons);
		key_hash.Add_bry_byteVal(Key_species_bry, Tid_species);
		key_hash.Add_bry_byteVal(Key_meta_bry, Tid_meta);
		key_hash.Add_bry_byteVal(Key_incubator_bry, Tid_incubator);
		key_hash.Add_bry_byteVal(Key_wikimediafoundation_bry, Tid_wikimediafoundation);
		key_hash.Add_bry_byteVal(Key_wikidata_bry, Tid_wikidata);
		key_hash.Add_bry_byteVal(Key_mediawiki_bry, Tid_mediawiki);
		key_hash.Add_bry_byteVal(Key_wikipedia_bry, Tid_wikipedia);
		key_hash.Add_bry_byteVal(Key_wiktionary_bry, Tid_wiktionary);
		key_hash.Add_bry_byteVal(Key_wikisource_bry, Tid_wikisource);
		key_hash.Add_bry_byteVal(Key_wikibooks_bry, Tid_wikibooks);
		key_hash.Add_bry_byteVal(Key_wikiversity_bry, Tid_wikiversity);
		key_hash.Add_bry_byteVal(Key_wikiquote_bry, Tid_wikiquote);
		key_hash.Add_bry_byteVal(Key_wikinews_bry, Tid_wikinews);
		key_hash.Add_bry_byteVal(Key_wikivoyage_bry, Tid_wikivoyage);
		key_hash.Add_bry_byteVal(Key_other_bry, Tid_other);
	}
	public static final byte 
		  Tid_other		=  0, Tid_home       =  1
		, Tid_wikipedia =  2, Tid_wiktionary =  3, Tid_wikisource =  4, Tid_wikibooks =  5, Tid_wikiversity =  6, Tid_wikiquote = 7, Tid_wikinews = 8, Tid_wikivoyage = 9
		, Tid_commons   = 10, Tid_species    = 11, Tid_meta       = 12, Tid_incubator = 13
		, Tid_wikidata  = 14, Tid_mediawiki  = 15, Tid_wikimediafoundation = 16
		;
	private static final byte Tid_wikimedia = Tid_by_key_null;	// NOTE: wikimedia isn't a type, but for PERF, it will be placed in the key_hash
	public static final String Key_home_str = "home";
	private static final byte[] Key_other_bry = ByteAry_.new_ascii_("other");
	public static final byte[] 
		  Key_home_bry = ByteAry_.new_ascii_(Key_home_str)
		, Key_wikipedia_bry = ByteAry_.new_ascii_("wikipedia"), Key_wiktionary_bry = ByteAry_.new_ascii_("wiktionary"), Key_wikisource_bry = ByteAry_.new_ascii_("wikisource")
		, Key_wikibooks_bry = ByteAry_.new_ascii_("wikibooks"), Key_wikiversity_bry = ByteAry_.new_ascii_("wikiversity"), Key_wikiquote_bry = ByteAry_.new_ascii_("wikiquote")
		, Key_wikinews_bry = ByteAry_.new_ascii_("wikinews"), Key_wikivoyage_bry = ByteAry_.new_ascii_("wikivoyage")
		, Key_commons_bry = ByteAry_.new_ascii_("commons"), Key_species_bry = ByteAry_.new_ascii_("species"), Key_meta_bry = ByteAry_.new_ascii_("meta")
		, Key_incubator_bry = ByteAry_.new_ascii_("incubator"), Key_wikidata_bry = ByteAry_.new_ascii_("wikidata"), Key_mediawiki_bry = ByteAry_.new_ascii_("mediawiki")
		, Key_wikimediafoundation_bry = ByteAry_.new_ascii_("wikimediafoundation")			
		;
	private static byte[][] Key__ary = new byte[][]
		{ Key_other_bry, Key_home_bry
		, Key_wikipedia_bry, Key_wiktionary_bry, Key_wikisource_bry, Key_wikibooks_bry, Key_wikiversity_bry, Key_wikiquote_bry, Key_wikinews_bry, Key_wikivoyage_bry
		, Key_commons_bry, Key_species_bry, Key_meta_bry, Key_incubator_bry
		, Key_wikidata_bry, Key_mediawiki_bry, Key_wikimediafoundation_bry
		};
	public static final byte[] Seg_org_bry = ByteAry_.new_ascii_("org"), Seg_wikimedia_bry = ByteAry_.new_ascii_("wikimedia"), Seg_www_bry = ByteAry_.new_utf8_("www");
}

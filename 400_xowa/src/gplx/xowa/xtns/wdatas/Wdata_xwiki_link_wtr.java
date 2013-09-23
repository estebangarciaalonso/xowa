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
package gplx.xowa.xtns.wdatas; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.json.*; import gplx.xowa.xtns.wdatas.*;
public class Wdata_xwiki_link_wtr implements ByteAryFmtrArg {
	public Wdata_xwiki_link_wtr Page_(Xoa_page page) {this.page = page; return this;} private Xoa_page page;
	public void XferAry(ByteAryBfr bfr, int idx) {
		ListAdp langs = page.Langs();
		byte[] qid = Write_wdata_links(langs, page.Wiki(), page.Page_ttl(), page.Wdata_external_lang_links());
		int langs_len = langs.Count();
		if (langs_len > 0)
			page.Wiki().Xwiki_mgr().Lang_mgr().Html_bld(bfr, page.Wiki(), langs, qid);
	}
	public static byte[] Write_wdata_links(ListAdp langs, Xow_wiki wiki, Xoa_ttl ttl, Wdata_external_lang_links_data external_links_mgr) {
		try {
			switch (wiki.Wiki_tid()) {
				case Xow_wiki_type_.Tid_home:		// home will never be in wikidata
				case Xow_wiki_type_.Tid_wikidata:	// wikidata will never be in wikidata
					return Qid_null;
			}
			Wdata_wiki_mgr wdata_mgr = wiki.App().Wiki_mgr().Wdata_mgr();
			Wdata_doc doc = wdata_mgr.Pages_get(wiki, ttl); if (doc == null) return Qid_null;	// no links
			boolean external_links_mgr_enabled = external_links_mgr.Enabled();
			OrderedHash links = doc.Links();
			ByteAryBfr tmp_bfr = wiki.App().Utl_bry_bfr_mkr().Get_k004();
			int len = links.Count();
			for (int i = 0; i < len; i++) {
				Json_itm_kv kv = (Json_itm_kv)links.FetchAt(i);
				byte[] xwiki_key = kv.Key().Data_bry();
				int lang_pos = Xob_bz2_file.Extract_lang(xwiki_key);
				if (external_links_mgr_enabled && external_links_mgr.Langs_hide(xwiki_key, 0, lang_pos + 1)) continue;
				tmp_bfr.Add_mid(xwiki_key, 0, lang_pos + 1);
				tmp_bfr.Add_byte(Byte_ascii.Colon);
				tmp_bfr.Add(Wdata_doc_.Link_extract(kv));
				Xoa_ttl lang_ttl = Xoa_ttl.parse_(wiki, tmp_bfr.XtoAryAndClear());
				if (lang_ttl == null) continue;								// invalid ttl
				Xow_xwiki_itm xwiki_itm = lang_ttl.Wik_itm();
				if (	xwiki_itm == null									// not a known xwiki; EX: [[zzz:abc]]
					||	ByteAry_.Eq(xwiki_itm.Domain(), wiki.Domain_bry())	// skip if same as self; i.e.: do not include links to enwiki if already in enwiki
					) continue;
				langs.Add(lang_ttl);
			}
			tmp_bfr.Mkr_rls();
			if (external_links_mgr_enabled && external_links_mgr.Sort())
				langs.SortBy(Xoa_ttl_sorter._);
			return doc.Qid();
		} catch (Exception e) {Err_.Noop(e); return Qid_null;}
	}
	public static final byte[] Qid_null = ByteAry_.Empty;	// NOTE: return Empty, not null else ByteAryFmtr will fail
}
class Xoa_ttl_sorter implements gplx.lists.ComparerAble {
	public int compare(Object lhsObj, Object rhsObj) {
		Xoa_ttl lhs = (Xoa_ttl)lhsObj, rhs = (Xoa_ttl)rhsObj;
		return ByteAry_.Compare(lhs.Raw(), rhs.Raw());
	}
	public static final Xoa_ttl_sorter _ = new Xoa_ttl_sorter(); Xoa_ttl_sorter() {}
}

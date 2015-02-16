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
package gplx.xowa.xtns.wdatas.hwtrs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wdatas.*;
import gplx.xowa.xtns.wdatas.core.*; import gplx.xowa.apis.xowa.xtns.*;
class Wdata_fmtr__oview_tbl implements Bry_fmtr_arg {
	private Xoapi_wikibase wikibase_api; private Url_encoder href_encoder;
	private Wdata_fmtr__oview_alias_itm fmtr_aliases = new Wdata_fmtr__oview_alias_itm();
	private Bry_fmtr slink_fmtr = Bry_fmtr.new_("<a href='/site/~{domain_bry}/wiki/~{page_href}'>~{page_text}</a>", "domain_bry", "page_href", "page_text");
	private Bry_bfr tmp_bfr = Bry_bfr.new_(255);
	private Wdata_doc wdoc;
	private byte[] hdr_alias_y, hdr_alias_n;
	public void Init_by_ctor(Xoapi_wikibase wikibase_api, Url_encoder href_encoder) {this.wikibase_api = wikibase_api; this.href_encoder = href_encoder;}
	public void Init_by_lang(byte[] lang_0, Wdata_hwtr_msgs msgs) {
		this.hdr_alias_y = msgs.Oview_alias_y();
		this.hdr_alias_n = msgs.Oview_alias_n();
	}
	public void Init_by_wdoc(Wdata_doc wdoc) {
		this.wdoc = wdoc;
	}
	public void XferAry(Bry_bfr bfr, int idx) {
		byte[][] core_langs		= wikibase_api.Core_langs();			
		byte[] oview_label		= Wdata_langtext_itm.Get_text_or_empty(wdoc.Label_list(), core_langs);
		byte[] oview_descr		= Wdata_langtext_itm.Get_text_or_empty(wdoc.Descr_list(), core_langs);
		byte[][] oview_alias	= Alias_get_or_empty(wdoc.Alias_list(), core_langs);
		byte[] aliases_hdr		= oview_alias == Bry_.Ary_empty ? hdr_alias_n : hdr_alias_y;
		fmtr_aliases.Init_by_itm(oview_alias);
		Wdata_sitelink_itm slink = (Wdata_sitelink_itm)wdoc.Slink_list().Fetch(wikibase_api.Link_wikis());
		if (slink != null) {
			oview_label = slink_fmtr.Bld_bry_many(tmp_bfr, slink.Domain_info().Domain_bry(), href_encoder.Encode(slink.Name()), oview_label);
		}
		row_fmtr.Bld_bfr_many(bfr, wdoc.Qid(), oview_label, oview_descr, aliases_hdr, fmtr_aliases);
	}
	private Bry_fmtr row_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "  <h1 id='wb-firstHeading-~{ttl}' class='wb-firstHeading'>"
	, "    <div class='wikibase-labelview '>"
	, "      <div class='wikibase-labelview-container'>"
	, "        <span class='wikibase-labelview-text'>~{ttl_label}</span>"
	, "        <span class='wikibase-labelview-entityid'>(~{ttl})</span>"
	, "      </div>"
	, "    </div>"
	, "  </h1>"
	, "  <div class='wikibase-descriptionview ' dir='auto'>"
	, "    <div class='wikibase-descriptionview-container'>"
	, "      <span class='wikibase-descriptionview-text'>~{ttl_descr}</span>"
	, "    </div>"
	, "  </div>"
	, "  <hr class='wb-hr' />"
	, "  <div class='wikibase-aliasesview '>"
	, "    <div class='wikibase-aliasesview-container'>"
	, "      <span class='wikibase-aliasesview-label'>~{hdr_aliases}</span>"
	, "      <ul class='wikibase-aliasesview-list'>~{ttl_aliases}"
	, "      </ul>"
	, "    </div>"
	, "  </div>"
	), "ttl", "ttl_label", "ttl_descr", "hdr_aliases", "ttl_aliases"
	);
	private static byte[][] Alias_get_or_empty(OrderedHash list, byte[][] langs) {
		if (list == null) return Bry_.Ary_empty;
		int langs_len = langs.length;
		for (int i = 0; i < langs_len; ++i) {
			Object itm_obj = list.Fetch(langs[i]);
			if (itm_obj != null) {
				Wdata_alias_itm itm = (Wdata_alias_itm)itm_obj;
				return itm.Vals();
			}
		}
		return Bry_.Ary_empty;
	}
}
class Wdata_fmtr__oview_alias_itm implements Bry_fmtr_arg {
	private byte[][] ary;
	public void Init_by_itm(byte[][] ary) {this.ary = ary;}
	public void XferAry(Bry_bfr bfr, int idx) {
		if (ary == null) return;
		int len = ary.length;
		for (int i = 0; i < len; ++i)
			row_fmtr.Bld_bfr_many(bfr, ary[i]);
	}
	private Bry_fmtr row_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "        <li class='wikibase-aliasesview-list-item'>~{itm}</li>"
	), "itm"
	);
}
